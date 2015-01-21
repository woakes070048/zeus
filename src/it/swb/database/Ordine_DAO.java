package it.swb.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.PreparedStatement;

import it.swb.business.ClienteBusiness;
import it.swb.log.Log;
import it.swb.model.Articolo;
import it.swb.model.Cliente;
import it.swb.model.Indirizzo;
import it.swb.model.LogArticolo;
import it.swb.model.Ordine;
import it.swb.utility.Methods;

public class Ordine_DAO {
	
	public static String elaboraOrdini(List<Ordine> ordini){
		String result = "";
		Connection con = DataSource.getLocalConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		if (ordini!=null && !ordini.isEmpty()){
			
			try {
			
				Map<String,Ordine> omap = getMappaOrdini(con);
		//		Map<String,String> ovsamap = getMappaOrdiniVsArticoli(con, ps, rs);
				Map<String,Cliente> cmap = ClienteBusiness.getInstance().getMappaClientiZeldaCompleta();
				 
				Log.debug("Elaborazione di "+ordini.size()+" ordini...");
				
				int i = 0;	/* ordini nuovi 	*/
				int j = 0;  /* ordini esistenti */
				int x = 0;  /* ordini cancellati */
				int y = 0;  /* ordini spediti */
				
				int w = 0;	/* clienti inseriti */
				int k = 0;  /* clienti aggiornati */
				
				for (Ordine o : ordini) {
					
					int id_cliente = 0;
					
					if (o.getPiattaforma().equals("eBay")){
							
						/*	se questo cliente non ha mai comprato prima	*/
						if (!cmap.containsKey(o.getCliente().getUsername())){
					
							id_cliente = Cliente_DAO.inserisciClienteZelda(o.getCliente(), con, ps);
							o.setIdCliente(id_cliente);
							o.getCliente().setIdCliente(id_cliente);
							cmap.put(o.getCliente().getUsername(), o.getCliente());
							w++;
						}
						/*	altrimenti se è un cliente che già aveva comprato presso di noi	*/
						else {
							Cliente c = cmap.get(o.getCliente().getUsername());
							o.setIdCliente(c.getIdCliente());
							o.getCliente().setIdCliente(c.getIdCliente());
							Cliente_DAO.modificaClienteZelda(o.getCliente(), c.getIdCliente(), con, ps);
							k++;
						}
						
					}
					else if (!o.getPiattaforma().equals("eBay")){ 
						
						if (!cmap.containsKey(o.getCliente().getEmail())){
					
							id_cliente = Cliente_DAO.inserisciClienteZelda(o.getCliente(), con, ps);
							o.setIdCliente(id_cliente);
							o.getCliente().setIdCliente(id_cliente);
							cmap.put(o.getCliente().getEmail(), o.getCliente());
							w++;
						}
						else {
							Cliente c = cmap.get(o.getCliente().getEmail());
							o.setIdCliente(c.getIdCliente());
							Cliente_DAO.modificaClienteZelda(o.getCliente(), c.getIdCliente(), con, ps);
							k++;
						}
					}
					
					/* Casi in cui l'ordine è già presente */
					if (omap.containsKey(o.getIdOrdinePiattaforma())){
						
						j++;
						
						Ordine odb = omap.get(o.getIdOrdinePiattaforma()); //l'ordine equivalente che ho nel db
						
						o.setIdOrdine(odb.getIdOrdine());
						
						modificaIdCliente(o.getIdOrdine(),o.getIdCliente(),con,ps);
						
						/* Se l'ordine risulta cancellato aumento la giacenza effettiva degli articoli */
						if ((!odb.getStato().contains("Cancellato") && o.getStato().contains("Cancellato")) 
								|| (!odb.getStato().contains("Annullato") && o.getStato().contains("Annullato"))){
							x++;
							
							aumentaGiacenzaEffettivaArticoli(o, con, ps);
							modificaOrdine(o, con, ps);
							
							omap.remove(o.getIdOrdinePiattaforma());
							omap.put(o.getIdOrdinePiattaforma(),o);
						}
						/* Se l'ordine è stato contrassegnato come spedito scalo la giacenza reale in magazzino degli articoli */
						else if (!odb.getStato().contains("Spedito") && o.getStato().contains("Spedito")){
							y++;
							scalaGiacenzaMagazzinoArticoli(o, con, ps);
							modificaOrdine(o, con, ps);
							//scalaGiacenzaAltrePiattaforme();
							
							omap.remove(o.getIdOrdinePiattaforma());
							omap.put(o.getIdOrdinePiattaforma(),o);
						}
						 else {		/*(se l'ordine è da pagare o da spedire) */
							 /* devo controllare se sono stati aggiunti altri articoli da quando l'ordine è stato aperto */
							 salvaListaArticoli(o.getArticoli(), o, con,ps);
						 }
					}	
					
					else {	/*	ordine non presente nel DB	*/
						
						int id_ordine = inserisciOrdine(o, con,ps);
						o.setIdOrdine(id_ordine);
						salvaListaArticoli(o.getArticoli(), o, con,ps);
						omap.put(o.getIdOrdinePiattaforma(), o);
						i++;
						
						//per ora commento per fare delle prove
						
	//					if (o.getStato().contains("Spedito")){
	//						scalaGiacenzaMagazzinoArticoli(o, con, ps);
	//						//scalaGiacenzaAltrePiattaforme();
	//					}
	//					else if (o.getStato().contains("Cancellato")){
	//						/*	non fare niente	*/
	//					}
	//					else {	/* tutti gli altri casi, cioè se l'ordine è da pagare o da spedire */
	//						scalaGiacenzaEffettivaArticoli(o,con,ps);
	//					}
						
					}				
				}
			
				con.commit();
				
				result = j+" ordini gia' esistenti (di cui "+x+" cancellati e "+y+" settati a Spedito) e "
						+i+" inseriti con scalo giacenze. "+w+" nuovi clienti inseriti e "+k+" aggiornati";
				
				Log.info(result);
			
			} catch (Exception e) {
				e.printStackTrace();
				Log.info(e);
				try {
					con.rollback();
				} catch (SQLException e1) { 
					e1.printStackTrace();	
					Log.info(e1);
				}
			}
			
			DataSource.closeConnections(con,null,ps,rs);
			
			ClienteBusiness.getInstance().reloadMappaClientiZeldaCompletaByID();
		}
		
		
		else {
			result = "Nessun ordine da elaborare.";
			Log.info(result);
		}
		return result;
	}
	
	
	public static void scalaGiacenze(List<Ordine> ordini){
		Connection con = DataSource.getLocalConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Log.debug("Verifica di "+ordini.size()+" ordini nel database locale...");
		
		int i = 0;
		int j = 0;
		for (Ordine o : ordini) {
			if (checkIfOrdineExist(o.getIdOrdinePiattaforma(),con,ps,rs)){
				j++;
			}				
			else {	
				inserisciOrdine(o, con,ps);
				scalaGiacenzaEffettivaArticoli(o,con,ps);
				i++;
			}				
		}
		
		try {
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			Log.info(e);
			try {
				con.rollback();
			} catch (SQLException e1) { 
				e1.printStackTrace();	
				Log.info(e1);
			}
		}
		Log.debug("Fine. "+j+" ordini gia' esistenti e "+i+" inseriti con scalo giacenze.");
		
		DataSource.closeConnections(con,null,ps,rs);
	}
	
	
	public static boolean checkIfOrdineExist(String id_ordine,Connection con, PreparedStatement ps, ResultSet rs){
		boolean exist = false;
		try {			
			ps = con.prepareStatement("SELECT * FROM ORDINI WHERE ID_ORDINE LIKE ? ");
			ps.setString(1, id_ordine);
			rs = ps.executeQuery();
			
			while (rs.next()){
				exist = true;
			}
		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
		}
		return exist;
	}	
	
	
	public static int inserisciOrdine(Ordine ord, Connection con,PreparedStatement ps){
		int id_ordine = 0;
		ResultSet rs = null;

		try {			
			String query = "INSERT INTO ORDINI(`id_ordine_piattaforma`,`piattaforma`,`id_cliente`," +	/*3*/
								"`data_acquisto`,`data_pagamento`,`data_spedizione`,`metodo_pagamento`,`totale`," +	/*8*/
								"`commento`,`stato`,`quantita_acquistata`,`valuta`,`costo_spedizione`," +
								"`spedizione_nome`,`spedizione_azienda`,`spedizione_partita_iva`,`spedizione_codice_fiscale`,`spedizione_indirizzo`," +
								"`spedizione_citta`,`spedizione_cap`,`spedizione_provincia`,`spedizione_nazione`,`spedizione_telefono`)" +
								" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) " + /* 23*/
								" ON DUPLICATE KEY UPDATE " +
								"`id_cliente`=?, `data_pagamento`=?, `data_spedizione`=?, `metodo_pagamento`=?, `totale`=?," +
								"`commento`=?,`stato`=?,`quantita_acquistata`=?";  /*sono 31*/
			
			ps = con.prepareStatement(query);
			ps.setString(1, ord.getIdOrdinePiattaforma());			
			ps.setString(2, ord.getPiattaforma());
			ps.setInt(3, ord.getIdCliente());
			
			Timestamp t1 = new Timestamp(ord.getDataAcquisto().getTime());
			ps.setTimestamp(4, t1);
			
			if (ord.getDataPagamento()!=null){
				Timestamp t2 = new Timestamp(ord.getDataPagamento().getTime());
				ps.setTimestamp(5, t2);
			} else ps.setNull(5, Types.NULL);
			if (ord.getDataSpedizione()!=null){
				Timestamp t3 = new Timestamp(ord.getDataSpedizione().getTime());
				ps.setTimestamp(6, t3);
			} else ps.setNull(6, Types.NULL);
			
			ps.setString(7, ord.getMetodoPagamento());
			ps.setDouble(8, ord.getTotale());			
			if (ord.getCommento()!=null)
				ps.setString(9, ord.getCommento());
			else ps.setNull(9, Types.NULL);
			ps.setString(10, ord.getStato());
			ps.setInt(11, ord.getQuantitaAcquistata());
			ps.setString(12, ord.getValuta());
			ps.setDouble(13, ord.getCostoSpedizione());
			
			Indirizzo inSp = ord.getIndirizzoSpedizione();
			
			if (inSp!=null){
				ps.setString(14, inSp.getNome());
				ps.setString(15,inSp.getAzienda());
				ps.setString(16, inSp.getPartitaIva());
				ps.setString(17, inSp.getCodiceFiscale());
				ps.setString(18, inSp.getIndirizzo1());
				ps.setString(19, inSp.getComune());
				ps.setString(20, inSp.getCap());
				ps.setString(21, inSp.getProvincia());
				ps.setString(22, inSp.getNazione());
				ps.setString(23, inSp.getTelefono());
			}
			else {
				ps.setNull(14, Types.NULL);
				ps.setNull(15, Types.NULL);
				ps.setNull(16, Types.NULL);
				ps.setNull(17, Types.NULL);
				ps.setNull(18, Types.NULL);
				ps.setNull(19, Types.NULL);
				ps.setNull(20, Types.NULL);
				ps.setNull(21, Types.NULL);
				ps.setNull(22, Types.NULL);
				ps.setNull(23, Types.NULL);
			}
			
			ps.setInt(24, ord.getIdCliente());
			if (ord.getDataPagamento()!=null){
				Timestamp t2 = new Timestamp(ord.getDataPagamento().getTime());
				ps.setTimestamp(25, t2);
			} else ps.setNull(25, Types.NULL);
			if (ord.getDataSpedizione()!=null){
				Timestamp t3 = new Timestamp(ord.getDataSpedizione().getTime());
				ps.setTimestamp(26, t3);
			} else ps.setNull(26, Types.NULL);
			ps.setString(27, ord.getMetodoPagamento());
			ps.setDouble(28, ord.getTotale());			
			if (ord.getCommento()!=null)
				ps.setString(29, ord.getCommento());
			else ps.setNull(29, Types.NULL);
			ps.setString(30, ord.getStato());
			ps.setInt(31, ord.getQuantitaAcquistata());
			
			ps.executeUpdate();
			
			ps = con.prepareStatement("SELECT LAST_INSERT_ID() as id_ordine");
			
			rs = ps.executeQuery();
			
			while (rs.next()) id_ordine =  rs.getInt("id_ordine");
			
		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
			try { 
				con.rollback();
			} catch (SQLException e) { 
				Log.info(ex); 
				e.printStackTrace();	
			}
		} finally {
			DataSource.closeStatements(null, null, rs);
		}
		return id_ordine;
	}
	
	
	/** Viene usato per modificare tutti i dati */
	public static void modificaOrdineCompleta(Ordine ord, Connection con,PreparedStatement ps){
		try {			
			String query = "UPDATE ORDINI SET " +
							"`data_pagamento` = ?, `data_spedizione` = ?,`metodo_pagamento` = ?,`totale` = ?,`commento` = ?," +
							"`stato` = ?,`quantita_acquistata` = ?, `costo_spedizione` = ?, `id_cliente`=?,`numeroTracciamento`=?, " +
							"`spedizione_nome` = ?,`spedizione_azienda` = ?,`spedizione_partita_iva` = ?,`spedizione_codice_fiscale` = ?,`spedizione_indirizzo` = ?," +
							"`spedizione_citta` = ?,`spedizione_cap` = ?,`spedizione_provincia` = ?,`spedizione_nazione` = ?,`spedizione_telefono` = ?)" +
							"where `id_ordine` = ?";  /*sono 20*/
			
			ps = con.prepareStatement(query);
				
			if (ord.getDataPagamento()!=null){
				Timestamp t1 = new Timestamp(ord.getDataPagamento().getTime());
				ps.setTimestamp(1, t1);
			} else ps.setNull(1, Types.NULL);
			if (ord.getDataPagamento()!=null){
				Timestamp t2 = new Timestamp(ord.getDataSpedizione().getTime());
				ps.setTimestamp(2, t2);
			} else ps.setNull(2, Types.NULL);
			ps.setString(3, ord.getMetodoPagamento());
			
			ps.setDouble(4, ord.getTotale());
			if (ord.getCommento()!=null)
				ps.setString(5, ord.getCommento());
			else ps.setNull(5, Types.NULL);
			ps.setString(6, ord.getStato());
			ps.setInt(7, ord.getQuantitaAcquistata());
			ps.setDouble(8, ord.getCostoSpedizione());
			ps.setInt(9, ord.getIdCliente());	
			ps.setString(10, ord.getNumeroTracciamento());
			
			Indirizzo inSp = ord.getIndirizzoSpedizione();
			

			
			if (inSp!=null){
				ps.setString(11, inSp.getNome());
				ps.setString(12, inSp.getAzienda());
				ps.setString(13, inSp.getPartitaIva());
				ps.setString(14, inSp.getCodiceFiscale());
				ps.setString(15, inSp.getIndirizzo1());
				ps.setString(16, inSp.getComune());
				ps.setString(17, inSp.getCap());
				ps.setString(18, inSp.getProvincia());
				ps.setString(19, inSp.getNazione());
				ps.setString(20, inSp.getTelefono());
			}
			else {
				ps.setNull(11, Types.NULL);
				ps.setNull(12, Types.NULL);
				ps.setNull(13, Types.NULL);
				ps.setNull(14, Types.NULL);
				ps.setNull(15, Types.NULL);
				ps.setNull(16, Types.NULL);
				ps.setNull(17, Types.NULL);
				ps.setNull(18, Types.NULL);
				ps.setNull(19, Types.NULL);
				ps.setNull(20, Types.NULL);
			}
			
			ps.setInt(21, ord.getIdOrdine());		
			
			ps.executeUpdate();
			
		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
			try { 
				con.rollback();
			} catch (SQLException e) { 
				Log.info(ex); 
				e.printStackTrace();	
			}
		}
	}
	
	/** Viene usato per modificare solamente date, metodo pagamento, totale, commento, stato, quantità, costo spedizione ed id cliente */
	public static void modificaOrdine(Ordine ord, Connection con,PreparedStatement ps){
		try {			
			String query = "UPDATE ORDINI SET `data_pagamento` = ?, `data_spedizione` = ?,`metodo_pagamento` = ?," +
							"`totale` = ?,`commento` = ?,`stato` = ?,`quantita_acquistata` = ?, `costo_spedizione` = ?, `id_cliente`=?   where `id_ordine` = ?";  /*sono 10*/
			
			ps = con.prepareStatement(query);
				
			if (ord.getDataPagamento()!=null){
				Timestamp t1 = new Timestamp(ord.getDataPagamento().getTime());
				ps.setTimestamp(1, t1);
			} else ps.setNull(1, Types.NULL);
			if (ord.getDataPagamento()!=null){
				Timestamp t2 = new Timestamp(ord.getDataSpedizione().getTime());
				ps.setTimestamp(2, t2);
			} else ps.setNull(2, Types.NULL);
			ps.setString(3, ord.getMetodoPagamento());
			
			ps.setDouble(4, ord.getTotale());
			if (ord.getCommento()!=null)
				ps.setString(5, ord.getCommento());
			else ps.setNull(5, Types.NULL);
			ps.setString(6, ord.getStato());
			ps.setInt(7, ord.getQuantitaAcquistata());
			ps.setDouble(8, ord.getCostoSpedizione());
			ps.setInt(9, ord.getIdCliente());	
			ps.setInt(10, ord.getIdOrdine());		
			
			ps.executeUpdate();
			
		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
			try { 
				con.rollback();
			} catch (SQLException e) { 
				Log.info(ex); 
				e.printStackTrace();	
			}
		}
	}
	
	public static void modificaIdCliente(int idOrdine, int idCliente, Connection con,PreparedStatement ps){
		try {			
			String query = "UPDATE ORDINI SET `id_cliente`=? where `id_ordine` = ?"; 
			
			ps = con.prepareStatement(query);
				
		
			ps.setInt(1, idCliente);	
			ps.setInt(2, idOrdine);		
			
			ps.executeUpdate();
			
		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
			try { 
				con.rollback();
			} catch (SQLException e) { 
				Log.info(ex); 
				e.printStackTrace();	
			}
		}
	}
	
	public static void eliminaOrdine(int idOrdine){
		Log.debug("elimino ordine "+idOrdine+" ...");
		Connection con = null;
		PreparedStatement ps = null;
		try {			
			con = DataSource.getLocalConnection();
			
			ps = con.prepareStatement("DELETE FROM ordini WHERE `id_ordine` = ? ");
			ps.setInt(1, idOrdine);
			ps.executeUpdate();
			
			eliminaElencoArticoliByIdOrdine(idOrdine, con, ps);
				
			con.commit();
			
			Log.info("Ordine "+idOrdine+" eliminato");
			
		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
		}finally {
			 DataSource.closeConnections(con,null,ps,null);
		}
	}
	
	public static List<Ordine> getOrdini(Date dataDa, Date dataA){
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Ordine> ordini = null;

		try {	
			String d1 = Methods.formattaData2(dataDa);
			String d2 = Methods.formattaData2(dataA);
			Log.debug("Cerco di ottenere la lista degli ordini da "+d1+" a "+d2);
			con = DataSource.getLocalConnection();
			
			String query = "SELECT * FROM ordini WHERE data_acquisto BETWEEN '"+d1+"' AND '"+d2+"' ORDER BY data_acquisto DESC";
			//System.out.println(query);
			
			ps = con.prepareStatement(query);
			
			rs = ps.executeQuery();
			
			ClienteBusiness.getInstance().reloadMappaClientiZeldaCompleta();
			Map<Integer,Cliente> mapclienti = ClienteBusiness.getInstance().getMappaClientiZeldaCompletaByID();
			Map<String,List<Articolo>> maparticoli = getMappaOrdiniConListaArticoli(con,ps,rs);
			
			ordini = new ArrayList<Ordine>();
			
			while (rs.next()){
				Ordine o = new Ordine();
				
				o.setIdOrdine(rs.getInt("id_ordine"));
				o.setIdOrdinePiattaforma(rs.getString("id_ordine_piattaforma"));
				o.setPiattaforma(rs.getString("piattaforma"));
				o.setIdCliente(rs.getInt("id_cliente"));
				o.setDataAcquisto(rs.getTimestamp("data_acquisto"));
				o.setDataPagamento(rs.getTimestamp("data_pagamento"));
				o.setDataSpedizione(rs.getTimestamp("data_spedizione"));
				o.setMetodoPagamento(rs.getString("metodo_pagamento"));
				
				o.setStDataAcquisto(Methods.formattaData3(o.getDataAcquisto()));
				o.setStDataPagamento(Methods.formattaData3(o.getDataPagamento()));
				o.setStDataSpedizione(Methods.formattaData3(o.getDataSpedizione()));
				o.setStDataUltimaModifica(Methods.formattaData3(o.getDataUltimaModifica()));
				
				o.setCommento(rs.getString("commento"));
				o.setTotale(rs.getDouble("totale"));
				o.setStato(rs.getString("stato"));
				o.setQuantitaAcquistata(rs.getInt("quantita_acquistata"));
				o.setValuta(rs.getString("valuta"));
				o.setCostoSpedizione(rs.getDouble("costo_spedizione"));
				
				if (rs.getString("spedizione_nome")!=null){
					Indirizzo inSp = new Indirizzo();
					inSp.setNome(rs.getString("spedizione_nome"));
					inSp.setNomeCompleto(rs.getString("spedizione_nome"));
					inSp.setAzienda(rs.getString("spedizione_azienda"));
					inSp.setPartitaIva(rs.getString("spedizione_partita_iva"));
					inSp.setCodiceFiscale(rs.getString("spedizione_codice_fiscale"));
					inSp.setIndirizzo1(rs.getString("spedizione_indirizzo"));
					inSp.setComune(rs.getString("spedizione_citta"));
					inSp.setProvincia(rs.getString("spedizione_provincia"));
					inSp.setCap(rs.getString("spedizione_cap"));
					inSp.setNazione(rs.getString("spedizione_nazione"));
					inSp.setTelefono(rs.getString("spedizione_telefono"));
				}
				
				if (mapclienti.containsKey(o.getIdCliente()))
					o.setCliente(mapclienti.get(o.getIdCliente()));
				
				if (maparticoli.containsKey(o.getIdOrdinePiattaforma()))
					o.setArticoli(maparticoli.get(o.getIdOrdinePiattaforma()));
				
				ordini.add(o);
				
			}
			Log.debug("Lista degli ordini ottenuta, occorrenze:"+ordini.size());

		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
		}
		 finally {			 
				 DataSource.closeConnections(con,null,ps,rs);	
		}
		return ordini;
	}
	
	
	public static Map<String, Ordine> getMappaOrdini(Connection con){
		Log.debug("Cerco di ottenere la mappa degli ordini...");
		Statement st = null;
		ResultSet rs = null;
		Map<String,Ordine> mapord = null;
		boolean closeCon = false;

		try {	
			if (con==null) {
				con = DataSource.getLocalConnection();
				closeCon = true;
			}
			
			st = con.createStatement();
			rs = st.executeQuery("SELECT id_ordine_piattaforma, id_ordine, id_cliente, stato FROM ordini");
			
			mapord = new HashMap<String,Ordine>();
			
			while (rs.next()){
				Ordine o = new Ordine();
				o.setIdOrdine(rs.getInt("id_ordine"));
				o.setIdOrdinePiattaforma(rs.getString("id_ordine_piattaforma"));
				o.setIdCliente(rs.getInt("id_ordine"));
				o.setStato(rs.getString("stato"));
				
				mapord.put(rs.getString("id_ordine_piattaforma"), o);
			}
			Log.debug("Mappa degli ordini creata.");

		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
		}
		 finally {			 
			 if (closeCon)
				 DataSource.closeConnections(con,st,null,rs);	
			 else DataSource.closeConnections(null,st,null,rs);	
		}
		return mapord;
	}
	
	
	public static void scalaGiacenzaEffettivaArticoli(Ordine o,Connection con,PreparedStatement ps){
		try {			
			for(Articolo a : o.getArticoli()){
				if (a.getCodice()!=null && !a.getCodice().trim().isEmpty()){
					ps = con.prepareStatement("UPDATE ARTICOLI SET `quantita_effettiva`= (`quantita_effettiva`-?) WHERE `codice` = ?");
					
					ps.setInt(1, a.getQuantitaMagazzino());
					ps.setString(2, a.getCodice());
					
					ps.executeUpdate();
					
					LogArticolo l = new LogArticolo();
					l.setCodiceArticolo(a.getCodice());
					l.setAzione("Acquisto");
					l.setData(o.getDataAcquisto());
					l.setExtra_1(a.getQuantitaMagazzino());
					l.setNote("Ordine "+o.getPiattaforma()+" "+o.getIdOrdinePiattaforma()+", scalate "+a.getQuantitaMagazzino()+" giacenze effettive");
					LogArticolo_DAO.inserisciLogArticolo(l, con, ps);
	
				}
			}
		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
			try { 
				con.rollback();
			} catch (SQLException e) { 
				Log.info(ex); 
				e.printStackTrace();	
			}
		}
	}
	
	public static void aumentaGiacenzaEffettivaArticoli(Ordine o,Connection con,PreparedStatement ps){
		try {			
			for(Articolo a : o.getArticoli()){
				if (a.getCodice()!=null && !a.getCodice().trim().isEmpty()){
					ps = con.prepareStatement("UPDATE ARTICOLI SET `quantita_effettiva`= (`quantita_effettiva`+?) WHERE `codice` = ?");
					
					ps.setInt(1, a.getQuantitaMagazzino());
					ps.setString(2, a.getCodice());
					
					ps.executeUpdate();
					
					LogArticolo l = new LogArticolo();
					l.setCodiceArticolo(a.getCodice());
					l.setAzione("Acquisto annullato");
					l.setNote("Aumentate "+a.getQuantitaMagazzino()+" giacenze effettive, annullato ordine "+o.getPiattaforma()+" "+o.getIdOrdinePiattaforma()
							+" del "+Methods.formattaData2(o.getDataAcquisto()));
					LogArticolo_DAO.inserisciLogArticolo(l, con, ps);
	
				}
			}
		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
			try { 
				con.rollback();
			} catch (SQLException e) { 
				Log.info(ex); 
				e.printStackTrace();	
			}
		}
	}
	
	public static void scalaGiacenzaMagazzinoArticoli(Ordine o,Connection con,PreparedStatement ps){
		try {			
			for(Articolo a : o.getArticoli()){
				if (a.getCodice()!=null && !a.getCodice().trim().isEmpty()){
					
					ps = con.prepareStatement("UPDATE ARTICOLI SET `quantita`= (`quantita_effettiva`-?) WHERE `codice` = ?");
					
					ps.setInt(1, a.getQuantitaMagazzino());
					ps.setString(2, a.getCodice());
					
					ps.executeUpdate();
					
					LogArticolo l = new LogArticolo();
					l.setCodiceArticolo(a.getCodice());
					l.setAzione("Vendita");
					if (o.getDataSpedizione()!=null)
						l.setData(o.getDataSpedizione());
					else l.setData(o.getDataAcquisto());
					l.setExtra_1(a.getQuantitaMagazzino());
					l.setNote("Ordine "+o.getPiattaforma()+" "+o.getIdOrdinePiattaforma()+", scalate "+a.getQuantitaMagazzino()+" giacenze in magazzino");
					LogArticolo_DAO.inserisciLogArticolo(l, con, ps);
					
				}
			}
		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
			try { 
				con.rollback();
			} catch (SQLException e) { 
				Log.info(ex); 
				e.printStackTrace();	
			}
		}
	}
	
	
//	public static void scalaGiacenzaMagazzinoArticoli(List<Articolo> articoli){
//		Connection con = null;		
//		PreparedStatement ps = null;
//		try {			
//			con = DataSource.getLocalConnection();
//			for(Articolo a : articoli){
//				if (a.getCodice()!=null && !a.getCodice().trim().isEmpty()){
//					
//					ps = con.prepareStatement("UPDATE ARTICOLI SET `quantita`= `quantita_effettiva` WHERE `codice` = ?");
//					
//					ps.setString(1, a.getCodice());
//					
//					ps.executeUpdate();
//					
//					LogArticolo l = new LogArticolo();
//					l.setCodiceArticolo(a.getCodice());
//					l.setAzione("Scalo Giacenza Magazzino");
//					l.setNote("Impostata la giacenza in magazzino a pari valore con quella effettiva");
//					LogArticolo_DAO.inserisciLogArticolo(l, con, ps);
//	
//				}
//			}
//		} catch (Exception ex) {
//			Log.info(ex); ex.printStackTrace();
//			try { 
//				con.rollback();
//			} catch (SQLException e) { 
//				Log.info(ex); 
//				e.printStackTrace();	
//			}
//		}
//		 finally {
//			 DataSource.closeConnections(con, null, ps, null);
//		}
//	}
	
	
	public static void salvaListaArticoli(List<Articolo> articoli, Ordine o,Connection con,PreparedStatement ps) throws SQLException {
			for(Articolo a : articoli){
//				System.out.println();
//				System.out.println("id ordine: "+id_ordine);
//				System.out.println("cod art: "+a.getCodice());
//				System.out.println("id ebay: "+a.getTitoloInserzione());
//				System.out.println("titolo inserz: "+a.getNome());
//				System.out.println("variante: "+a.getNote());
//				System.out.println("quantita: "+a.getQuantitaMagazzino());
//				System.out.println("prezzo: "+a.getPrezzoDettaglio());
//				System.out.println();
		//		if (!checkIfArticoloVsOrdineExist(idOrdine,a.getNote2(),con,ps)) /* se non esiste questa transizione */
					inserisciArticoloVsOrdine(a,o,con,ps);
			}
				
	}
	
	private static void inserisciArticoloVsOrdine(Articolo a, Ordine o,Connection con,PreparedStatement ps) throws SQLException {
		
//		String s = o.getIdOrdinePiattaforma();
//		double prz = a.getPrezzoDettaglio();
//			
//		try{
		
			ps = con.prepareStatement("INSERT INTO ORDINI_VS_ARTICOLI(`id_ordine`,`piattaforma`,`id_ordine_piattaforma`,`codice_articolo`,`id_inserzione`," +
					"`titolo_inserzione`,`variante`,`quantita`,`prezzo_unitario`,`totale`,`id_transazione`,`id_articolo`,`aliquota_iva`)" +
					" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE `codice_articolo`=?, `id_inserzione`=?, `titolo_inserzione`=?," +
					"`variante`=?,`quantita`=?,`prezzo_unitario`=?,`totale`=?,`id_articolo`=?,`aliquota_iva`=?, `id_ordine`=? ");
			
			ps.setInt(1, o.getIdOrdine());
			ps.setString(2, o.getPiattaforma());
			ps.setString(3, o.getIdOrdinePiattaforma());
			ps.setString(4, a.getCodice());
			ps.setString(5, a.getTitoloInserzione());
			ps.setString(6, a.getNome());
			if (a.getNote()!=null && !a.getNote().isEmpty())
				ps.setString(7, a.getNote());
			else ps.setNull(7, Types.NULL);
			ps.setInt(8, a.getQuantitaMagazzino());
			ps.setDouble(9, a.getPrezzoDettaglio());	//su prezzo dettaglio ho salvato il prezzo unitario
			ps.setDouble(10, a.getPrezzoPiattaforme());	//su prezzo piattaforme ho salvato il totale (quantita * prezzo unitario)
			ps.setString(11, a.getNote2());
			ps.setLong(12, a.getIdArticolo());
			ps.setInt(13, a.getAliquotaIva());
			
			ps.setString(14, a.getCodice());
			ps.setString(15, a.getTitoloInserzione());
			ps.setString(16, a.getNome());
			if (a.getNote()!=null && !a.getNote().isEmpty())
				ps.setString(17, a.getNote());
			else ps.setNull(17, Types.NULL);
			ps.setInt(18, a.getQuantitaMagazzino());
			ps.setDouble(19, a.getPrezzoDettaglio());
			ps.setDouble(20, a.getPrezzoPiattaforme());
			ps.setLong(21, a.getIdArticolo());
			int aliva = a.getAliquotaIva();
			if (aliva==0) aliva=22;
			ps.setInt(22, aliva);
			ps.setInt(23, o.getIdOrdine());
			
			ps.executeUpdate();
			
//		}
//		catch(Exception e){
//			Log.error("ecco l'errore: id = "+s+", prz = "+prz);
//			e.printStackTrace();
//		}
			
	}
	
	public static void modificaArticoloVsOrdine(Articolo a, String idOrdine, Connection con,PreparedStatement ps){
		boolean closeCon = false;
		try {			
			if (con==null) {
				con = DataSource.getLocalConnection();
				closeCon=true;
			}
			ps = con.prepareStatement("UPDATE ORDINI_VS_ARTICOLI set `quantita` = ?,`codice_articolo`=?  where `id_ordine_piattaforma`=? and `id_transazione` = ? ");
				
			ps.setInt(1, a.getQuantitaMagazzino());
			ps.setString(2, a.getCodice());
			ps.setString(3, idOrdine);
			ps.setString(4, a.getNote2());
			
			ps.executeUpdate();
			
			if (closeCon) con.commit();
			
		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
			try { 
				con.rollback();
			} catch (SQLException e) { 
				Log.info(ex); 
				e.printStackTrace();	
			}
		} finally{
			if (closeCon)
				DataSource.closeConnections(con, null, ps, null);
		}
	}
	
	public static boolean checkIfArticoloVsOrdineExist(String idOrdine, String idTransazione,Connection con,PreparedStatement ps){
		ResultSet rs = null;
		boolean exist = false;
		try {			
				ps = con.prepareStatement("SELECT * FROM ORDINI_VS_ARTICOLI WHERE `id_ordine_piattaforma` = ? AND `ID_TRANSAZIONE` = ?");
				ps.setString(1, idOrdine);
				ps.setString(2, idTransazione);
				
				rs = ps.executeQuery();
				
				while (rs.next()){
					exist = true;
				}
		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
		}
		 finally {
			 DataSource.closeStatements(null, null, rs);
		}
		return exist;
	}
	
	private static void eliminaElencoArticoliByIdOrdine(int idOrdine,Connection con,PreparedStatement ps){
		try {			
			ps = con.prepareStatement("DELETE FROM ORDINI_VS_ARTICOLI WHERE `id_ordine`= ?");
				
			ps.setInt(1, idOrdine);
			
			ps.executeUpdate();
			
		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
			try { 
				con.rollback();
			} catch (SQLException e) { 
				Log.info(ex); 
				e.printStackTrace();	
			}
		}
	}
	
	/** Mappa con elenco di idOrdine_idTransazione, serve solo per verificare la presenza nel DB di una transazione relativa ad un ordine **/
	public static Map<String,String> getMappaOrdiniVsArticoli(Connection con,PreparedStatement ps,ResultSet rs){
		Map<String,String> map = null;
		boolean closeCon = false;
		try {	
			if (con==null){
				con = DataSource.getLocalConnection();
				closeCon = true;
			}
				
				ps = con.prepareStatement("SELECT * FROM ORDINI_VS_ARTICOLI ORDER BY ID_ORDINE_PIATTAFORMA ASC, ID_TRANSAZIONE ASC");
				
				rs = ps.executeQuery();
				
				map = new HashMap<String,String>();
				
				while (rs.next()){
//					Articolo a = new Articolo();
//					a.setTitoloInserzione(rs.getString("id_inserzione"));
//					a.setCodice(rs.getString("codice_articolo"));
//					a.setQuantitaMagazzino(rs.getInt("quantita"));
//					a.setPrezzoDettaglio(rs.getDouble("prezzo"));
//					a.setNome(rs.getString("titolo_inserzione"));
//					a.setNote(rs.getString("variante"));
//					a.setNote2(rs.getString("id_transazione"));	
					
					map.put(rs.getString("id_ordine_piattaforma")+"_"+rs.getString("id_transazione"),"");
				}
		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { 
				con.rollback();
			} catch (SQLException e) { 
				Log.info(ex); 
				e.printStackTrace();	
			}
		}
		 finally {
			 if (closeCon) DataSource.closeConnections(con, null, ps, rs);
		}
		return map;
	}
	
	/** Ad ogni id ordine è associata la lista di articoli corrispondenti **/
	public static Map<String,List<Articolo>> getMappaOrdiniConListaArticoli(Connection con,PreparedStatement ps,ResultSet rs){
		Log.debug("Cerco di ottenere la mappa degli articoli relativi agli ordini...");
		Map<String,List<Articolo>> map = null;
		boolean closeCon = false;
		
		try {			
			if (con==null) {
				con = DataSource.getLocalConnection();
				closeCon = true;
			}				
				ps = con.prepareStatement("SELECT * FROM ORDINI_VS_ARTICOLI order by id_ordine_piattaforma asc, id_transazione asc");
				
				rs = ps.executeQuery();
				
				map = new HashMap<String,List<Articolo>>();
				
				while (rs.next()){
					Articolo a = new Articolo();
					a.setIdArticolo(rs.getInt("id_articolo"));
					a.setTitoloInserzione(rs.getString("id_inserzione"));
					a.setCodice(rs.getString("codice_articolo"));
					a.setQuantitaMagazzino(rs.getInt("quantita"));
					a.setPrezzoDettaglio(rs.getDouble("prezzo_unitario"));
					a.setPrezzoPiattaforme(rs.getDouble("totale"));
					a.setAliquotaIva(rs.getInt("aliquota_iva"));
					a.setNome(rs.getString("titolo_inserzione"));
					a.setNote(rs.getString("variante"));
					a.setNote2(rs.getString("id_transazione"));	
					
					String id_ordine = rs.getString("id_ordine_piattaforma");
					
					if (map.containsKey(id_ordine))
						map.get(id_ordine).add(a);
					else {
						List<Articolo> l = new ArrayList<Articolo>();
						l.add(a);
						map.put(id_ordine,l);
					} 
				}
				Log.debug("Mappa degli articoli relativi agli ordini ottenuta.");
		} catch (Exception ex) {
			Log.info(ex);
			ex.printStackTrace();
		}
		 finally {
			 if (closeCon)
				 DataSource.closeConnections(con,null,ps,rs);	
		 }
		return map;
	}
	
	
	/** Mappa con i numeri delle transazioni corrispondenti all'id ordine dato in input **/
	public static Map<String,String> getMappaArticoliByIdOrdine(String idOrdine,Connection con,PreparedStatement ps,ResultSet rs){
		//Log.debug("Cerco di ottenere la mappa degli articoli relativi agli ordini...");
		Map<String,String> map = null;
		
		try {			
			if (con==null) {
				con = DataSource.getLocalConnection();
			}				
				ps = con.prepareStatement("SELECT * FROM ORDINI_VS_ARTICOLI where id_ordine = ?");
				ps.setString(1, idOrdine);
				
				rs = ps.executeQuery();
				
				map = new HashMap<String,String>();
				
				while (rs.next()){
					map.put(rs.getString("id_transazione"), ""); 
				}
		} catch (Exception ex) {
			Log.info(ex);
			ex.printStackTrace();
		}
		return map;
	}
	
	/** Ad ogni id ordine è associata la mappa di articoli corrispondenti (nella quale ad ogni id transazione è associato il relativo articolo) **/
	public static Map<String,Map<String,Articolo>> getMappaMappeOrdiniVsArticoli(Connection con){
		Log.debug("Cerco di ottenere la mappa di mappe degli articoli relativi agli ordini...");
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String,Map<String,Articolo>> map = null;
		boolean closeCon = false;
		
		try {			
			if (con==null) {
				con = DataSource.getLocalConnection();
				closeCon = true;
			}				
				ps = con.prepareStatement("SELECT * FROM ORDINI_VS_ARTICOLI order by id_ordine asc");
				
				rs = ps.executeQuery();
				
				map = new HashMap<String,Map<String,Articolo>>();
				
				while (rs.next()){
					Articolo a = new Articolo();
					a.setTitoloInserzione(rs.getString("id_inserzione"));
					a.setCodice(rs.getString("codice_articolo"));
					a.setQuantitaMagazzino(rs.getInt("quantita"));
					a.setPrezzoDettaglio(rs.getDouble("prezzo"));
					a.setAliquotaIva(rs.getInt("aliquota_iva"));
					a.setNome(rs.getString("titolo_inserzione"));
					a.setNote(rs.getString("variante"));
					a.setNote2(rs.getString("id_transazione"));	
					
					String id_ordine = rs.getString("id_ordine");
					
					if (map.containsKey(id_ordine))
						map.get(id_ordine).put(a.getNote2(),a);
					else {
						Map<String,Articolo> m = new HashMap<String,Articolo>();
						m.put(a.getNote2(),a);
						map.put(id_ordine,m);
					} 
				}
				Log.debug("Mappa di mappe degli articoli relativi agli ordini ottenuta.");
		} catch (Exception ex) {
			Log.info(ex);
			ex.printStackTrace();
		}
		 finally {
			 if (closeCon)
				 DataSource.closeConnections(con,null,ps,rs);	
			 else DataSource.closeConnections(null,null,ps,rs);			}
		return map;
	}

}
