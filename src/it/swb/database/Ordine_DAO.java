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
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import it.swb.business.ClienteBusiness;
import it.swb.log.Log;
import it.swb.model.Articolo;
import it.swb.model.ArticoloAcquistato;
import it.swb.model.Cliente;
import it.swb.model.Indirizzo;
import it.swb.model.LogArticolo;
import it.swb.model.Ordine;
import it.swb.utility.DateMethods;
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
				Map<String,String> ovsamap = getMappaOrdiniVsArticoli(con, ps, rs);
				Map<String,Cliente> cmap = ClienteBusiness.getInstance().getMappaClientiZeldaCompleta();
				 
				Log.debug("Elaborazione di "+ordini.size()+" ordini...");
				
				int i = 0;	/* ordini nuovi 	*/
				int j = 0;  /* ordini esistenti */
				int x = 0;  /* ordini cancellati */
				int y = 0;  /* ordini spediti */
				int z = 0; /* ordini aggiornati */
				
				int w = 0;	/* clienti inseriti */
				int k = 0;  /* clienti aggiornati */
				
				for (Ordine o : ordini) {
					
					int id_cliente = 0;
					Cliente c = o.getCliente();
					
					if (c!=null && c.getEmail()!=null && c.getNomeCompleto()!=null && (!o.getStato().contains("Cancellato") || !o.getStato().contains("Annullato"))){
					
						if (o.getPiattaforma().equals("eBay")){
								
							/*	se questo cliente non ha mai comprato prima	*/
							if (!cmap.containsKey(c.getUsername())){
						
								id_cliente = Cliente_DAO.inserisciClienteZelda(c, con, ps);
								o.setIdCliente(id_cliente);
								o.getCliente().setIdCliente(id_cliente);
								cmap.put(c.getUsername(), c);
								w++;
							}
							/*	altrimenti se è un cliente che già aveva comprato presso di noi	*/
							else {
								Cliente c1 = cmap.get(c.getUsername());
								o.setIdCliente(c1.getIdCliente());
								//o.getCliente().setIdCliente(c.getIdCliente());
								//Cliente_DAO.modificaClienteZelda(c, c.getIdCliente(), con, ps);
								k++;
							}
							
						}
						else if (!o.getPiattaforma().equals("eBay")){ 
							
							if (!cmap.containsKey(c.getEmail())){
						
								id_cliente = Cliente_DAO.inserisciClienteZelda(c, con, ps);
								o.setIdCliente(id_cliente);
								o.getCliente().setIdCliente(id_cliente);
								cmap.put(c.getEmail(), c);
								w++;
							}
							else {
								Cliente c1 = cmap.get(c.getEmail());
								o.setIdCliente(c1.getIdCliente());
								//comunque non mi serve a niente
								//Cliente_DAO.modificaClienteZelda(o.getCliente(), c.getIdCliente(), con, ps);
								k++;
							}
						}
					}
					/* Casi in cui l'ordine è già presente */
					if (omap.containsKey(o.getIdOrdinePiattaforma())){
						
						j++;
						
						Ordine odb = omap.get(o.getIdOrdinePiattaforma()); //l'ordine equivalente che ho nel db
						
						o.setIdOrdine(odb.getIdOrdine());
						
						if (!ovsamap.containsKey(o.getIdOrdinePiattaforma())){
							salvaElencoArticoli(o.getElencoArticoli(), o.getIdOrdine(), con,ps);
							
							z++;
						}
						
						//a che serviva?!
						//modificaIdCliente(o.getIdOrdine(),o.getIdCliente(),con,ps);
						
						//inserisciOrdine(o, con, ps);
						modificaOrdineCompleta(o, con, ps);
						
						/* Se l'ordine risulta cancellato aumento la giacenza effettiva degli articoli */
						if ((!odb.getStato().contains("Cancellato") && o.getStato().contains("Cancellato")) 
								|| (!odb.getStato().contains("Annullato") && o.getStato().contains("Annullato"))){
							x++;
							
							aumentaGiacenzaEffettivaArticoli(o, con, ps);
							
							omap.remove(o.getIdOrdinePiattaforma());
							omap.put(o.getIdOrdinePiattaforma(),o);
							
							//setto sul db lo stato
							modificaOrdine(o, con, ps);
						}
						/* Se l'ordine è stato contrassegnato come spedito scalo la giacenza reale in magazzino degli articoli */
						else if (!odb.getStato().contains("Spedito") && o.getStato().contains("Spedito")){
							y++;
							scalaGiacenzaMagazzinoArticoli(o, con, ps);
							//scalaGiacenzaAltrePiattaforme();
							
							omap.remove(o.getIdOrdinePiattaforma());
							omap.put(o.getIdOrdinePiattaforma(),o);
							
							//setto sul db lo stato e la data del pagamento e della spedizione se è già stato spedito
							modificaOrdine(o, con, ps);
						}
						 else {		/*(se l'ordine è da pagare o da spedire) */
							//non fare niente
						 }
					}	
					
					else {	/*	ordine non presente nel DB	*/
						
						int id_ordine = inserisciOrdine(o, con,ps);
						o.setIdOrdine(id_ordine);
						//System.out.println("Per questo ordine ci sono "+o.getElencoArticoli().size()+" oggetti da salvare");
						//int inseriti = 
								salvaElencoArticoli(o.getElencoArticoli(), o.getIdOrdine(), con,ps);
						//System.out.println("salvati "+inseriti+" oggetti acquistati");
						omap.put(o.getIdOrdinePiattaforma(), o);
						i++;
						
						
						if (o.getStato().contains("Spedito")){
							scalaGiacenzaMagazzinoArticoli(o, con, ps);
							//TODO implementare funzione scalaGiacenzaSuAltrePiattaforme(); 
						}
						else if (o.getStato().contains("Cancellato")){
							/*	non fare niente	*/
						}
						else {	/* tutti gli altri casi, cioè se l'ordine è da pagare o da spedire */
							scalaGiacenzaEffettivaArticoli(o,con,ps);
						}
						
					}				
				}
			
				con.commit();
				
				result = j+" ordini gia' esistenti (di cui "+z+" modificati, "+x+" cancellati e "+y+" settati a Spedito) e "
						+i+" inseriti con scalo giacenze. "+w+" nuovi clienti inseriti e "+k+" aggiornati. ";
				
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
		
		try {
			
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
		
			con.commit();
			
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
			
			String query = "INSERT INTO ORDINI(" +
								"`id_ordine_piattaforma`,`piattaforma`,`id_cliente`,`email`,`nome_acquirente`,`username_cliente`," + /*6*/

								"`data_acquisto`,`data_pagamento`,`data_ultima_modifica`,`data_spedizione`," +	/*10*/
		
								"`metodo_pagamento`,`totale`,`commento`,`stato`," +											/*14*/
								"`quantita_acquistata`,`valuta`,`costo_spedizione`,`tasse`,`numero_tracciamento`," +												/*19*/
								"`sconto`,`nome_buono_sconto`,`valore_buono_sconto`," +																							/*22*/
								
								"`bomboniere`, `coda_ldv`, `data_ldv`,  `id_corriere`, " +
								
								"`spedizione_nome`,`spedizione_azienda`,`spedizione_partita_iva`,`spedizione_codice_fiscale`,`spedizione_indirizzo`," +	/*31*/
								"`spedizione_citta`,`spedizione_cap`,`spedizione_provincia`,`spedizione_nazione`,`spedizione_telefono`,`spedizione_cellulare`," +	/*37*/
								
								"`fatturazione_nome`,`fatturazione_azienda`,`fatturazione_partita_iva`,`fatturazione_codice_fiscale`,`fatturazione_indirizzo`," +	/*42*/
								"`fatturazione_citta`,`fatturazione_cap`,`fatturazione_provincia`,`fatturazione_nazione`)" +						/*46*/
								
								" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) " + /* 46*/
								
								" ON DUPLICATE KEY UPDATE " +
								"`id_cliente`=?, `data_pagamento`=?, `data_ultima_modifica`=?,`metodo_pagamento`=?," +	/*50*/
								" `totale`=?, `commento`=?,`stato`=?,`quantita_acquistata`=?,`costo_spedizione`=?," +						/*55*/
								"`tasse`=?, `numero_tracciamento`=?,`sconto`=?,`nome_buono_sconto`=?,`valore_buono_sconto`=?";  /*sono 60*/
			
			ps = con.prepareStatement(query);
			
			int i = 1;
			
			ps.setString(i, ord.getIdOrdinePiattaforma()); i++;		
			ps.setString(i, ord.getPiattaforma()); i++;	
			ps.setInt(i, ord.getIdCliente()); i++;	
			ps.setString(i, ord.getEmail()); i++;
			ps.setString(i, ord.getNomeAcquirente()); i++;
			ps.setString(i, ord.getUsername()); i++;
			
			ps.setTimestamp(i, new Timestamp(ord.getDataAcquisto().getTime())); i++;
			
			if (ord.getDataPagamento()!=null)
				ps.setTimestamp(i, new Timestamp(ord.getDataPagamento().getTime())); 
			else ps.setNull(i, Types.NULL);
			i++;
			
			if (ord.getDataUltimaModifica()!=null)
				ps.setTimestamp(i, new Timestamp(ord.getDataUltimaModifica().getTime()));
			else ps.setNull(i, Types.NULL); 
			i++;
			
			if (ord.getDataSpedizione()!=null)
				ps.setTimestamp(i, new Timestamp(ord.getDataSpedizione().getTime())); 
			else	ps.setNull(i, Types.NULL); 
			i++;
			
			ps.setString(i, ord.getMetodoPagamento()); i++;
			ps.setDouble(i, ord.getTotale());		 i++;
			if (ord.getCommento()!=null)
				ps.setString(i, ord.getCommento());	
			else  ps.setNull(i, Types.NULL);	
			i++;
			ps.setString(i, ord.getStato()); i++;
			
			
			ps.setInt(i, ord.getQuantitaAcquistata()); i++;
			ps.setString(i, ord.getValuta()); i++;
			ps.setDouble(i, ord.getCostoSpedizione()); i++;
			ps.setDouble(i, ord.getTasse()); i++;
			ps.setString(i, ord.getNumeroTracciamento()); i++;
			
			ps.setBoolean(i, ord.isSconto()); i++;
			ps.setString(i, ord.getNomeBuonoSconto()); i++;
			ps.setDouble(i, ord.getValoreBuonoSconto()); i++;
			
			ps.setBoolean(i, ord.isBomboniere()); i++;
			
			if (!ord.isBomboniere() && (ord.getMetodoPagamento()!=null && (ord.getMetodoPagamento().contains("PayPal") || ord.getMetodoPagamento().contains("Amazon") ||
					ord.getMetodoPagamento().contains("Contrassegno")))){
				ps.setInt(i, 1); i++;
				Timestamp t1 = new Timestamp(new Date().getTime());
				ps.setTimestamp(i, t1); i++;
				ps.setInt(i, 1); //corriere 1 = sda
			}
			else {
				ps.setInt(i, 0); i++;
				ps.setNull(i, Types.NULL); i++;
				ps.setNull(i, Types.NULL);
			}
			i++;
			
			
			Indirizzo inSp = ord.getIndirizzoSpedizione();
			
			if (inSp!=null){
				ps.setString(i, inSp.getNomeCompleto()); i++;
				ps.setString(i,inSp.getAzienda()); i++;
				ps.setString(i, inSp.getPartitaIva()); i++;
				ps.setString(i, inSp.getCodiceFiscale()); i++;
				ps.setString(i, inSp.getIndirizzo1()); i++;
				
				ps.setString(i, inSp.getComune()); i++;
				ps.setString(i, inSp.getCap()); i++;
				ps.setString(i, inSp.getProvincia()); i++;
				ps.setString(i, inSp.getNazione()); i++;
				ps.setString(i, inSp.getTelefono()); i++;
				ps.setString(i, inSp.getCellulare()); i++;
			}
			else {
				ps.setNull(i, Types.NULL); i++;
				ps.setNull(i, Types.NULL); i++;
				ps.setNull(i, Types.NULL); i++;
				ps.setNull(i, Types.NULL); i++;
				ps.setNull(i, Types.NULL); i++;
				
				ps.setNull(i, Types.NULL); i++;
				ps.setNull(i, Types.NULL); i++; 
				ps.setNull(i, Types.NULL); i++;
				ps.setNull(i, Types.NULL); i++;
				ps.setNull(i, Types.NULL); i++;
				ps.setNull(i, Types.NULL); i++;
			}
			
			Indirizzo inFatt = ord.getIndirizzoFatturazione();
			
			if (inFatt!=null){
				ps.setString(i, inFatt.getNomeCompleto()); i++;
				ps.setString(i,inFatt.getAzienda()); i++;
				ps.setString(i, inFatt.getPartitaIva()); i++;
				ps.setString(i, inFatt.getCodiceFiscale()); i++;
				ps.setString(i, inFatt.getIndirizzo1()); i++;
				
				ps.setString(i, inFatt.getComune()); i++;
				ps.setString(i, inFatt.getCap()); i++;
				ps.setString(i, inFatt.getProvincia()); i++;
				ps.setString(i, inFatt.getNazione()); i++;
			}
			else {
				ps.setNull(i, Types.NULL); i++;
				ps.setNull(i, Types.NULL); i++;
				ps.setNull(i, Types.NULL); i++;
				ps.setNull(i, Types.NULL); i++;
				ps.setNull(i, Types.NULL); i++;
				
				ps.setNull(i, Types.NULL); i++;
				ps.setNull(i, Types.NULL); i++;
				ps.setNull(i, Types.NULL); i++;
				ps.setNull(i, Types.NULL); i++;
			}
			
			/* seconda parte della query (ON DUPLICATE KEY) */
			
			ps.setInt(i, ord.getIdCliente()); i++;
			if (ord.getDataPagamento()!=null)
				ps.setTimestamp(i, new Timestamp(ord.getDataPagamento().getTime())); 
			else ps.setNull(i, Types.NULL); 
			i++;
			
//			if (ord.getDataSpedizione()!=null)
//				ps.setTimestamp(i, new Timestamp(ord.getDataSpedizione().getTime())); 
//			else ps.setNull(i, Types.NULL); 
//			i++;
			
			if (ord.getDataUltimaModifica()!=null)
				ps.setTimestamp(i, new Timestamp(ord.getDataUltimaModifica().getTime()));
			else ps.setNull(i, Types.NULL); 
			i++;
			
			ps.setString(i, ord.getMetodoPagamento()); i++;
			
			ps.setDouble(i, ord.getTotale());	  i++;
			if (ord.getCommento()!=null)
				ps.setString(i, ord.getCommento()); 
			else ps.setNull(i, Types.NULL); 
			i++;
			
			ps.setString(i, ord.getStato()); i++;
			ps.setInt(i, ord.getQuantitaAcquistata()); i++;
			ps.setDouble(i, ord.getCostoSpedizione()); i++;
			
			ps.setDouble(i, ord.getTasse()); i++;
			ps.setString(i, ord.getNumeroTracciamento()); i++;
			ps.setBoolean(i, ord.isSconto()); i++;
			ps.setString(i, ord.getNomeBuonoSconto()); i++;
			ps.setDouble(i, ord.getValoreBuonoSconto());
			
			//System.out.println(ps);
			
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
	
	
	/** Viene usato per modificare tutti i dati, dal metodo elabora ordini */
	public static void modificaOrdineCompleta(Ordine ord, Connection con,PreparedStatement ps){
		try {			
			if (con==null)
				con = DataSource.getLocalConnection();

			String query = "UPDATE ordini " +
							"SET `data_pagamento` = ?, `metodo_pagamento` = ?,`totale` = ?,`commento` = ?," +
							"`nome_acquirente`=?, `username_cliente`=?, " +
							"`stato` = ?,`quantita_acquistata` = ?, `costo_spedizione` = ?, `id_cliente`=?,`tasse`=?, " +
							"`spedizione_nome` = ?,`spedizione_azienda` = ?,`spedizione_partita_iva` = ?,`spedizione_codice_fiscale` = ?,`spedizione_indirizzo` = ?," +
							"`spedizione_citta` = ?,`spedizione_cap` = ?,`spedizione_provincia` = ?,`spedizione_nazione` = ?,`spedizione_telefono` = ?, `spedizione_cellulare` = ? " +
							"WHERE `id_ordine` = ?";  /*sono 19*/
			
			ps = con.prepareStatement(query);
			
			int i = 1;
				
			if (ord.getDataPagamento()!=null){
				ps.setTimestamp(i, new Timestamp(ord.getDataPagamento().getTime()));
			} else ps.setNull(i, Types.NULL);
			i++;
			
//			if (ord.getDataSpedizione()!=null){
//				ps.setTimestamp(i, new Timestamp(ord.getDataSpedizione().getTime()));
//			} else ps.setNull(i, Types.NULL);
//			i++;
			
			ps.setString(i, ord.getMetodoPagamento()); i++;
			
			ps.setDouble(i, ord.getTotale()); i++;
			if (ord.getCommento()!=null)
				ps.setString(i, ord.getCommento());
			else ps.setNull(i, Types.NULL);
			 i++;
			 ps.setString(i, ord.getNomeAcquirente()); i++;
			 ps.setString(i, ord.getUsername()); i++;
			ps.setString(i, ord.getStato()); i++;
			ps.setInt(i, ord.getQuantitaAcquistata()); i++;
			ps.setDouble(i, ord.getCostoSpedizione()); i++;
			ps.setInt(i, ord.getIdCliente());	 i++;
			ps.setDouble(i, ord.getTasse()); i++;
			
			Indirizzo inSp = ord.getIndirizzoSpedizione();
			
			if (inSp!=null){
				ps.setString(i, inSp.getNomeCompleto()); i++;
				ps.setString(i,inSp.getAzienda()); i++;
				ps.setString(i, inSp.getPartitaIva()); i++;
				ps.setString(i, inSp.getCodiceFiscale()); i++;
				ps.setString(i, inSp.getIndirizzo1()); i++;
				
				ps.setString(i, inSp.getComune()); i++;
				ps.setString(i, inSp.getCap()); i++;
				ps.setString(i, inSp.getProvincia()); i++;
				ps.setString(i, inSp.getNazione()); i++;
				ps.setString(i, inSp.getTelefono()); i++;
				ps.setString(i, inSp.getCellulare()); i++;
			}
			else {
				ps.setNull(i, Types.NULL); i++;
				ps.setNull(i, Types.NULL); i++;
				ps.setNull(i, Types.NULL); i++;
				ps.setNull(i, Types.NULL); i++;
				ps.setNull(i, Types.NULL); i++;
				
				ps.setNull(i, Types.NULL); i++;
				ps.setNull(i, Types.NULL); i++; 
				ps.setNull(i, Types.NULL); i++;
				ps.setNull(i, Types.NULL); i++;
				ps.setNull(i, Types.NULL); i++;
				ps.setNull(i, Types.NULL); i++;
			}
			
			ps.setInt(i, ord.getIdOrdine());		
			
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
	
	/** Viene usato per modificare tutti i dati, dal metodo elabora ordini */
	public static int modificaOrdineByUtente(Ordine ord){
		Log.debug("modifica ordine "+ord.getIdOrdine());
		
		Connection con = DataSource.getLocalConnection();
		PreparedStatement ps = null;
		int res = 0;
		
		try {			
			con = DataSource.getLocalConnection();

			String query = "UPDATE ordini " +
								"SET " +
								"`spedizione_nome` = ?,`spedizione_azienda` = ?,`spedizione_indirizzo` = ?,`spedizione_citta` = ?,`spedizione_provincia` = ?," +
								"`spedizione_cap` = ?,`spedizione_nazione` = ?,`spedizione_telefono` = ?, `spedizione_cellulare` = ?, `email` = ?, " +
								
								"`nome_acquirente` = ?, `stato` = ?, `metodo_pagamento` = ?, `data_pagamento` = ?, `data_spedizione` = ?, " +
								"`id_corriere` = ?, `numero_tracciamento` = ?, `costo_spedizione` = ?, `totale` = ?,`commento` = ? " +
								
								"WHERE `id_ordine` = ?";  /*sono 20*/
			
			ps = con.prepareStatement(query);
			
			int i = 1;
			
			Indirizzo inSp = ord.getIndirizzoSpedizione();
			
			if (inSp!=null){
				ps.setString(i, inSp.getNomeCompleto()); i++;
				ps.setString(i,inSp.getAzienda()); i++;
				ps.setString(i, inSp.getIndirizzo1()); i++;
				ps.setString(i, inSp.getComune()); i++;
				ps.setString(i, inSp.getProvincia()); i++;
				
				ps.setString(i, inSp.getCap()); i++;
				ps.setString(i, inSp.getNazione()); i++;
				ps.setString(i, inSp.getTelefono()); i++;
				ps.setString(i, inSp.getCellulare()); i++;
			}
			else {
				ps.setNull(i, Types.NULL); i++;
				ps.setNull(i, Types.NULL); i++;
				ps.setNull(i, Types.NULL); i++;
				ps.setNull(i, Types.NULL); i++;
				ps.setNull(i, Types.NULL); i++;
				
				ps.setNull(i, Types.NULL); i++;
				ps.setNull(i, Types.NULL); i++; 
				ps.setNull(i, Types.NULL); i++;
				ps.setNull(i, Types.NULL); i++;
			}
			
			ps.setString(i, ord.getEmail()); i++;
			
			ps.setString(i, ord.getNomeAcquirente()); i++;
			ps.setString(i, ord.getStato()); i++;
			ps.setString(i, ord.getMetodoPagamento()); i++;
			
			if (ord.getStDataPagamento()!=null && !ord.getStDataPagamento().isEmpty()){
				Date d = DateMethods.creaDataDaStringa2(ord.getStDataPagamento());
						if (d!=null) ps.setTimestamp(i, new Timestamp(d.getTime()));
						else ps.setNull(i, Types.NULL);
			} else ps.setNull(i, Types.NULL);
			i++;
			
			if (ord.getStDataSpedizione()!=null && !ord.getStDataSpedizione().isEmpty()){
				Date d = DateMethods.creaDataDaStringa2(ord.getStDataSpedizione());
				if (d!=null) ps.setTimestamp(i, new Timestamp(d.getTime()));
				else ps.setNull(i, Types.NULL);
			} else ps.setNull(i, Types.NULL);
			i++;
			
			ps.setInt(i, ord.getIdCorriere()); i++;
			if (ord.getNumeroTracciamento()!=null) ps.setString(i, ord.getNumeroTracciamento());
			else ps.setNull(i, Types.NULL);
			i++;
			ps.setDouble(i, ord.getCostoSpedizione()); i++;
			ps.setDouble(i, ord.getTotale()); i++;
			if (ord.getCommento()!=null) ps.setString(i, ord.getCommento());
			else ps.setNull(i, Types.NULL);
			i++;
			
			ps.setInt(i, ord.getIdOrdine());		
			
			res = ps.executeUpdate();
			
			eliminaElencoArticoliByIdOrdine(ord.getIdOrdine(),con,ps);
			
			int j = 1;
			
			for (ArticoloAcquistato a : ord.getElencoArticoli()){
				if (a.getIdOrdinePiattaforma()==null) a.setIdOrdinePiattaforma(ord.getIdOrdinePiattaforma());
				if (a.getIdTransazione()==null) a.setIdTransazione(ord.getIdOrdinePiattaforma()+"_"+j);
				inserisciArticoloVsOrdine(a, ord.getIdOrdine(), con, ps);
				j++;
			}
			
			con.commit();
			
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
		finally {
			DataSource.closeConnections(con, null, ps, null);
		}
		return res;
	}
	
	/** Viene usato per modificare solamente date, metodo pagamento, totale, commento, stato, quantità, costo spedizione ed id cliente */
	public static void modificaOrdine(Ordine ord, Connection con,PreparedStatement ps){
		try {			
			String query = "UPDATE ORDINI SET `data_pagamento` = ?, `metodo_pagamento` = ?," +
							"`totale` = ?,`commento` = ?,`stato` = ?,`quantita_acquistata` = ?, `costo_spedizione` = ?, `id_cliente`=?   where `id_ordine` = ?";  /*sono 9*/
			
			ps = con.prepareStatement(query);
				
			if (ord.getDataPagamento()!=null){
				Timestamp t1 = new Timestamp(ord.getDataPagamento().getTime());
				ps.setTimestamp(1, t1);
			} else ps.setNull(1, Types.NULL);
//			if (ord.getDataSpedizione()!=null){
//				Timestamp t2 = new Timestamp(ord.getDataSpedizione().getTime());
//				ps.setTimestamp(2, t2);
//			} else ps.setNull(2, Types.NULL);
			ps.setString(2, ord.getMetodoPagamento());
			
			ps.setDouble(3, ord.getTotale());
			if (ord.getCommento()!=null)
				ps.setString(4, ord.getCommento());
			else ps.setNull(4, Types.NULL);
			ps.setString(5, ord.getStato());
			ps.setInt(6, ord.getQuantitaAcquistata());
			ps.setDouble(7, ord.getCostoSpedizione());
			ps.setInt(8, ord.getIdCliente());	
			ps.setInt(9, ord.getIdOrdine());		
			
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
		int risultato = 0;
		try {			
			con = DataSource.getLocalConnection();
			
			ps = con.prepareStatement("DELETE FROM ordini WHERE `id_ordine` = ? ");
			ps.setInt(1, idOrdine);
			risultato = ps.executeUpdate();
			
			eliminaElencoArticoliByIdOrdine(idOrdine, con, ps);
				
			con.commit();
			
			if (risultato==1) Log.info("Ordine "+idOrdine+" eliminato");
			else Log.info("Ordine non eliminato.");
			
		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
		}finally {
			 DataSource.closeConnections(con,null,ps,null);
		}
	}
	
	public static List<Ordine> getOrdini(Date dataDa, Date dataA, String filtroOrdini){
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Ordine> ordini = null;

		try {	
			String d1 = DateMethods.formattaData2(dataDa);
			String d2 = DateMethods.formattaData2(dataA);
			Log.debug("Cerco di ottenere la lista degli ordini da "+d1+" a "+d2);
			
			con = DataSource.getLocalConnection();
			
			if (filtroOrdini==null) filtroOrdini = "tutti";
			
			String filtro = "";
			if (filtroOrdini.equals("tutti")) filtro = "1";
			else if (filtroOrdini.equals("nonarchiviati")) filtro = " stato<>'Cancellato' AND stato<>'Annullato' AND archiviato=0 ";
			else if (filtroOrdini.equals("nonspediti")) filtro = " stato<>'Cancellato' AND stato<>'Annullato' AND stato<>'Spedito' AND archiviato=0 ";
			else if (filtroOrdini.equals("spediti")) filtro = " stato='Spedito' AND archiviato=0 ";
			else if (filtroOrdini.equals("archiviati")) filtro = " archiviato=1 ";
			
//			String query = "SELECT o.*, cz.nome_completo as acquirente, cz.username, cz.telefono as cell " +
//									" FROM ordini as o " +
//									"LEFT JOIN clienti_zelda as cz ON o.id_cliente = cz.id_cliente " +
//									" WHERE data_acquisto BETWEEN '"+d1+"' AND '"+d2+"' AND "+filtro+
//									" ORDER BY data_acquisto DESC";
			
			String query = "SELECT * " +
									" FROM ordini " +
									" WHERE data_acquisto BETWEEN '"+d1+"' AND '"+d2+"' AND "+filtro+
									" ORDER BY data_acquisto DESC";
			
			ps = con.prepareStatement(query);
			
			rs = ps.executeQuery();
			
			Map<String,List<ArticoloAcquistato>> maparticoli = getMappaOrdiniConListaArticoli(con,ps,rs);
			
			ordini = new ArrayList<Ordine>();
			
			while (rs.next()){
				Ordine o = new Ordine();
				
				/* dettagli ordine */
				o.setIdOrdine(rs.getInt("id_ordine"));
				o.setIdOrdinePiattaforma(rs.getString("id_ordine_piattaforma"));
				o.setPiattaforma(rs.getString("piattaforma"));
				
				/* dettagli acquirente */
				o.setIdCliente(rs.getInt("id_cliente"));
				o.setNomeAcquirente(rs.getString("nome_acquirente"));
				o.setUsername(rs.getString("username_cliente"));
				o.setEmail(rs.getString("email"));
				
				/* date */
				o.setDataAcquisto(rs.getTimestamp("data_acquisto"));
				o.setDataPagamento(rs.getTimestamp("data_pagamento"));
				o.setDataSpedizione(rs.getTimestamp("data_spedizione"));
				o.setDataUltimaModifica(rs.getTimestamp("data_ultima_modifica"));
				
				o.setStDataAcquisto(DateMethods.formattaData3(o.getDataAcquisto()));
				o.setStDataPagamento(DateMethods.formattaData3(o.getDataPagamento()));
				o.setStDataSpedizione(DateMethods.formattaData3(o.getDataSpedizione()));
				o.setStDataUltimaModifica(DateMethods.formattaData3(o.getDataUltimaModifica()));
				
				/* varie */
				o.setCommento(rs.getString("commento"));
				o.setStato(rs.getString("stato"));
				o.setQuantitaAcquistata(rs.getInt("quantita_acquistata"));
				o.setBomboniere(rs.getBoolean("bomboniere"));
				
				/* costi */
				o.setMetodoPagamento(rs.getString("metodo_pagamento"));
				o.setValuta(rs.getString("valuta"));
				o.setCostoSpedizione(rs.getDouble("costo_spedizione"));
				o.setSconto(rs.getBoolean("sconto"));
				o.setNomeBuonoSconto(rs.getString("nome_buono_sconto"));
				o.setValoreBuonoSconto(rs.getDouble("valore_buono_sconto"));
				o.setTasse(rs.getDouble("tasse"));
				o.setTotale(rs.getDouble("totale"));
				
				/* gestione dell'ordine */
				o.setNumeroTracciamento(rs.getString("numero_tracciamento"));
				o.setCodaLDV(rs.getInt("coda_ldv"));
				o.setDataLDV(rs.getTimestamp("data_ldv"));
				o.setIdCorriere(rs.getInt("id_corriere"));
				o.setNomeCorriere(Methods.cercaNomeCorriere(o.getIdCorriere()));
				o.setLinkTracciamento(Methods.cercaLinkTracciamento(o.getIdCorriere()));
				o.setStampato(rs.getInt("stampato"));
				o.setDataStampato(rs.getTimestamp("data_stampato"));
				o.setScontrinoStampato(rs.getInt("scontrino_stampato"));
				o.setDataScontrino(rs.getTimestamp("data_scontrino"));
				o.setArchiviato(rs.getInt("archiviato"));
				
				/* indirizzi */
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
				inSp.setCellulare(rs.getString("spedizione_cellulare"));
				
				Indirizzo inFatt = new Indirizzo();
				inFatt.setNome(rs.getString("fatturazione_nome"));
				inFatt.setNomeCompleto(rs.getString("fatturazione_nome"));
				inFatt.setAzienda(rs.getString("fatturazione_azienda"));
				inFatt.setPartitaIva(rs.getString("fatturazione_partita_iva"));
				inFatt.setCodiceFiscale(rs.getString("fatturazione_codice_fiscale"));
				inFatt.setIndirizzo1(rs.getString("fatturazione_indirizzo"));
				inFatt.setComune(rs.getString("fatturazione_citta"));
				inFatt.setProvincia(rs.getString("fatturazione_provincia"));
				inFatt.setCap(rs.getString("fatturazione_cap"));
				inFatt.setNazione(rs.getString("fatturazione_nazione"));
				
//				if (mapclienti.containsKey(o.getIdCliente())){
//					Cliente c = mapclienti.get(o.getIdCliente());
//					o.setUsername(c.getUsername());
//					inSp.setCellulare(c.getCellulare());
//					o.setCliente(c);
//					//if (o.getIndirizzoSpedizione().getNome()==null || o.getIndirizzoSpedizione().getNome().isEmpty())
//					//	o.setIndirizzoSpedizione(o.getCliente().getIndirizzoSpedizione());
//				}
				
				o.setIndirizzoSpedizione(inSp);
				o.setIndirizzoFatturazione(inFatt);
				
				if (maparticoli.containsKey(o.getIdOrdinePiattaforma()))
					o.setElencoArticoli(maparticoli.get(o.getIdOrdinePiattaforma()));
				
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
	
	
	public static void scalaGiacenzaEffettivaArticoli(Ordine o,Connection con,PreparedStatement ps) throws Exception {
		//try {			
			if (o.getElencoArticoli()!=null && !o.getElencoArticoli().isEmpty()){
				for(ArticoloAcquistato a : o.getElencoArticoli()){
					if (a.getCodice()!=null && !a.getCodice().trim().isEmpty()){
						ps = con.prepareStatement("UPDATE ARTICOLI SET `quantita_effettiva`= (`quantita_effettiva`-?) WHERE `codice` = ?");
						
						ps.setInt(1, a.getQuantitaAcquistata());
						ps.setString(2, a.getCodice());
						
						ps.executeUpdate();
						
						LogArticolo l = new LogArticolo();
						l.setCodiceArticolo(a.getCodice());
						l.setAzione("Acquisto");
						l.setData(o.getDataAcquisto());
						l.setExtra_1(a.getQuantitaAcquistata());
						l.setNote("Ordine "+o.getPiattaforma()+" "+o.getIdOrdinePiattaforma()+", scalate "+a.getQuantitaAcquistata()+" giacenze effettive");
						LogArticolo_DAO.inserisciLogArticolo(l, con, ps);
		
					}
				}
			}
//		} catch (Exception ex) {
//			Log.info(ex); 
//			ex.printStackTrace();
//			try { 
//				con.rollback();
//			} catch (SQLException e) { 
//				Log.info(ex); 
//				e.printStackTrace();	
//			}
//		}
	}
	
	public static void aumentaGiacenzaEffettivaArticoli(Ordine o,Connection con,PreparedStatement ps){
		try {
			if (o.getElencoArticoli()!=null && !o.getElencoArticoli().isEmpty()){
				for(ArticoloAcquistato a : o.getElencoArticoli()){
					if (a.getCodice()!=null && !a.getCodice().trim().isEmpty()){
						ps = con.prepareStatement("UPDATE ARTICOLI SET `quantita_effettiva`= (`quantita_effettiva`+?) WHERE `codice` = ?");
						
						ps.setInt(1, a.getQuantitaAcquistata());
						ps.setString(2, a.getCodice());
						
						ps.executeUpdate();
						
						LogArticolo l = new LogArticolo();
						l.setCodiceArticolo(a.getCodice());
						l.setAzione("Acquisto annullato");
						l.setNote("Aumentate "+a.getQuantitaAcquistata()+" giacenze effettive, annullato ordine "+o.getPiattaforma()+" "+o.getIdOrdinePiattaforma()
								+" del "+DateMethods.formattaData2(o.getDataAcquisto()));
						LogArticolo_DAO.inserisciLogArticolo(l, con, ps);
		
					}
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
			if (o.getElencoArticoli()!=null && !o.getElencoArticoli().isEmpty()){
				for(ArticoloAcquistato a : o.getElencoArticoli()){
					if (a.getCodice()!=null && !a.getCodice().trim().isEmpty()){
						
						ps = con.prepareStatement("UPDATE ARTICOLI SET `quantita`= (`quantita_effettiva`-?) WHERE `codice` = ?");
						
						ps.setInt(1, a.getQuantitaAcquistata());
						ps.setString(2, a.getCodice());
						
						ps.executeUpdate();
						
						LogArticolo l = new LogArticolo();
						l.setCodiceArticolo(a.getCodice());
						l.setAzione("Vendita");
						if (o.getDataSpedizione()!=null)
							l.setData(o.getDataSpedizione());
						else l.setData(o.getDataAcquisto());
						l.setExtra_1(a.getQuantitaAcquistata());
						l.setNote("Ordine "+o.getPiattaforma()+" "+o.getIdOrdinePiattaforma()+", scalate "+a.getQuantitaAcquistata()+" giacenze in magazzino");
						LogArticolo_DAO.inserisciLogArticolo(l, con, ps);
						
					}
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
	
/*	Rimpiazzati con ArticoloAcquistato
 * 
	public static void salvaListaArticoli(List<Articolo> articoli, Ordine o,Connection con,PreparedStatement ps) throws SQLException {
		if (articoli!=null && articoli.isEmpty()){
			for(Articolo a : articoli){
					inserisciArticoloVsOrdine(a,o,con,ps);
			}
		}
				
	}
	
	private static void inserisciArticoloVsOrdine(Articolo a, Ordine o,Connection con,PreparedStatement ps) throws SQLException {
		
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
			int iva = a.getAliquotaIva();
			if (iva==0) iva=22;
			ps.setInt(13, iva);
			
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
			ps.setInt(22, iva);
			ps.setInt(23, o.getIdOrdine());
			
			ps.executeUpdate();
	}
*/

	public static int salvaElencoArticoli(List<ArticoloAcquistato> articoli, long idOrdine,Connection con,PreparedStatement ps) throws SQLException {
		int value = 0;
		if (articoli!=null && !articoli.isEmpty()){
			for(ArticoloAcquistato a : articoli){
					value+=inserisciArticoloVsOrdine(a,idOrdine,con,ps);
			}
		}
		return value;
	}
	
	private static int inserisciArticoloVsOrdine(ArticoloAcquistato a, long idOrdine,Connection con,PreparedStatement ps) {
		int value = 0;
		
		try{
		
			ps = con.prepareStatement(
					"INSERT INTO ORDINI_VS_ARTICOLI(" +
								"`id_ordine`,`piattaforma`,`id_ordine_piattaforma`,`codice_articolo`,`id_inserzione`," +
								"`nome_articolo`,`titolo_inserzione`,`asin`,`variante`,`quantita`," +
								"`prezzo_unitario`,`totale`,`id_transazione`,`id_articolo`,`aliquota_iva`)" +
								" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) " +
					"ON DUPLICATE KEY UPDATE " +
								"`codice_articolo`=?, `id_inserzione`=?, `titolo_inserzione`=?,`asin`=?,`variante`=?," +
								"`quantita`=?,`prezzo_unitario`=?,`totale`=?,`id_articolo`=?,`aliquota_iva`=?, `id_ordine`=? ");
			
			ps.setLong(1, idOrdine);
			ps.setString(2, a.getPiattaforma());
			ps.setString(3, a.getIdOrdinePiattaforma());
			ps.setString(4, a.getCodice());
			ps.setString(5, a.getIdInserzione());
			if (a.getNome()==null) a.setNome("Senza nome");
			ps.setString(6, a.getNome());
			ps.setString(7, a.getTitoloInserzione());
			if (a.getAsin()!=null && !a.getAsin().isEmpty())
				ps.setString(8, a.getAsin());
			else ps.setNull(8, Types.NULL);
			if (a.getVariante()!=null && !a.getVariante().isEmpty())
				ps.setString(9, a.getVariante());
			else ps.setNull(9, Types.NULL);
			ps.setInt(10, a.getQuantitaAcquistata());
			ps.setDouble(11, a.getPrezzoUnitario());	
			ps.setDouble(12, a.getPrezzoTotale());
			ps.setString(13, a.getIdTransazione());
			ps.setLong(14, a.getIdArticolo());
			double iva = a.getIva();
			if (iva==0) iva=22;
			ps.setDouble(15, iva);
			
			ps.setString(16, a.getCodice());
			ps.setString(17, a.getIdInserzione());
			ps.setString(18, a.getNome());
			if (a.getAsin()!=null && !a.getAsin().isEmpty())
				ps.setString(19, a.getAsin());
			else ps.setNull(19, Types.NULL);
			
			if (a.getVariante()!=null && !a.getVariante().isEmpty())
				ps.setString(20, a.getVariante());
			else ps.setNull(20, Types.NULL);
			ps.setInt(21, a.getQuantitaAcquistata());
			ps.setDouble(22, a.getPrezzoUnitario());
			ps.setDouble(23, a.getPrezzoTotale());
			ps.setLong(24, a.getIdArticolo());
			ps.setDouble(25, iva);
			ps.setLong(26, idOrdine);
			
			value = ps.executeUpdate();
			
			if (a.getAsin()!=null && !a.getAsin().isEmpty() && a.getCodice()!=null && !a.getCodice().isEmpty()) 
				Articolo_DAO.salvaAsin(a.getCodice(), a.getAsin(), con, ps);
		
		}
		catch(SQLException e){
			System.out.println("id_ordine: "+idOrdine+", transazione: "+a.getIdTransazione()+", id_ordine_piattaforma: "+a.getIdOrdinePiattaforma());
			System.out.println("prezzo unitario: "+a.getPrezzoUnitario());
			e.printStackTrace();
			Log.info(e);
		}
		return value;
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
				
				//ps = con.prepareStatement("SELECT * FROM ORDINI_VS_ARTICOLI ORDER BY ID_ORDINE_PIATTAFORMA ASC, ID_TRANSAZIONE ASC");
			ps = con.prepareStatement("SELECT distinct ID_ORDINE_PIATTAFORMA FROM ORDINI_VS_ARTICOLI");
				
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
					
					map.put(rs.getString("id_ordine_piattaforma"),"");
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
	public static Map<String,List<ArticoloAcquistato>> getMappaOrdiniConListaArticoli(Connection con,PreparedStatement ps,ResultSet rs){
		Log.debug("Cerco di ottenere la mappa degli articoli relativi agli ordini...");
		Map<String,List<ArticoloAcquistato>> map = null;
		boolean closeCon = false;
		
		try {			
			if (con==null) {
				con = DataSource.getLocalConnection();
				closeCon = true;
			}				
				ps = con.prepareStatement("SELECT * FROM ORDINI_VS_ARTICOLI order by id_ordine_piattaforma asc, id_transazione asc");
				
				rs = ps.executeQuery();
				
				map = new HashMap<String,List<ArticoloAcquistato>>();
				
				while (rs.next()){
					ArticoloAcquistato a = new ArticoloAcquistato();
					a.setIdTransazione(rs.getString("id_transazione"));	
					a.setIdArticolo(rs.getInt("id_articolo"));
					a.setIdInserzione(rs.getString("id_inserzione"));
					a.setAsin(rs.getString("asin"));
					a.setCodice(rs.getString("codice_articolo"));
					a.setNome(rs.getString("nome_articolo"));
					a.setTitoloInserzione(rs.getString("titolo_inserzione"));
					if (a.getNome()==null || a.getNome().isEmpty())
						a.setNome(a.getTitoloInserzione());
					a.setVariante(rs.getString("variante"));
					a.setQuantitaAcquistata(rs.getInt("quantita"));
					a.setPrezzoUnitario(rs.getDouble("prezzo_unitario"));
					a.setPrezzoTotale(rs.getDouble("totale"));
					a.setIva(rs.getInt("aliquota_iva"));
					
					
					
					String id_ordine = rs.getString("id_ordine_piattaforma");
					
					if (map.containsKey(id_ordine))
						map.get(id_ordine).add(a);
					else {
						List<ArticoloAcquistato> l = new ArrayList<ArticoloAcquistato>();
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
					a.setNome(rs.getString("nome_articolo"));
					a.setTitoloInserzione(rs.getString("titolo_inserzione"));
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
	
	
	public static int archivia(int idOrdine){
		Connection con = DataSource.getLocalConnection();
		PreparedStatement ps = null;
		int result = 0;

		try {			
			String query = "UPDATE ordini " +
									"SET `archiviato`=1, `coda_ldv` = 0 " +
									"WHERE `id_ordine`=? ";  
			
			ps = con.prepareStatement(query);
			ps.setInt(1, idOrdine);			
			
			
			result = ps.executeUpdate();
			
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
			DataSource.closeConnections(con, null, ps, null);
		}
		return result;
	}
	
	
	public static int modificaCodaLDV(int idOrdine, int stato, int corriere){
		Connection con = DataSource.getLocalConnection();
		PreparedStatement ps = null;
		int result = 0;
		
		String nuovo_corriere = "";
		if (corriere>0) nuovo_corriere = ", `id_corriere`= "+corriere+" ";

		try {			
			String query = "UPDATE ordini " +
									"SET `coda_ldv`=?, `data_ldv`=? " + nuovo_corriere +
									"WHERE `id_ordine`=? ";  
			
			ps = con.prepareStatement(query);
			ps.setInt(1, stato);
			if (stato == 1){
				Timestamp t1 = new Timestamp(new Date().getTime());
				ps.setTimestamp(2, t1);
			}
			else 
				ps.setNull(2, Types.NULL);			
			ps.setInt(3, idOrdine);			
			
			
			result = ps.executeUpdate();
			
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
			DataSource.closeConnections(con, null, ps, null);
		}
		return result;
	}
	
	public static int togliDaCodaLDV(List<Ordine> ordini){
		Connection con = DataSource.getLocalConnection();
		PreparedStatement ps = null;
		int result = 0;

		try {			
			String query = "UPDATE ordini " +
									"SET `coda_ldv`=? " +
									"WHERE `id_ordine`=? ";  
			
			ps = con.prepareStatement(query);
			
			for (Ordine o : ordini){
				ps.setInt(1, 0);
				ps.setInt(2, o.getIdOrdine());			
				
				ps.executeUpdate();
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
			DataSource.closeConnections(con, null, ps, null);
		}
		return result;
	}
	
	
	public static List<Ordine> getOrdiniPerLDV(int corriere){
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Ordine> ordini = null;

		try {	
			con = DataSource.getLocalConnection();
			
			String filtroCorriere = "";
			if (corriere>0) filtroCorriere = "AND `id_corriere`= "+corriere+" ";
			
			String query = "SELECT * " +
									"FROM ordini " +
									"WHERE  `coda_ldv` >= 1 " +
									filtroCorriere +
									"ORDER BY `piattaforma` DESC, `id_ordine` DESC";
			
			ps = con.prepareStatement(query);
			
			rs = ps.executeQuery();
			
			Map<String,List<ArticoloAcquistato>> maparticoli = getMappaOrdiniConListaArticoli(con,ps,rs);
			
			ordini = new ArrayList<Ordine>();
			
			while (rs.next()){
				Ordine o = new Ordine();
				
				o.setIdOrdine(rs.getInt("id_ordine"));
				o.setIdOrdinePiattaforma(rs.getString("id_ordine_piattaforma"));
				o.setPiattaforma(rs.getString("piattaforma"));
				o.setNomeAcquirente(rs.getString("nome_acquirente"));
				o.setUsername(rs.getString("username_cliente"));
				o.setEmail(rs.getString("email"));
				
				o.setDataAcquisto(rs.getTimestamp("data_acquisto"));
				o.setDataPagamento(rs.getTimestamp("data_pagamento"));
				o.setDataSpedizione(rs.getTimestamp("data_spedizione"));
				o.setMetodoPagamento(rs.getString("metodo_pagamento"));
				o.setDataLDV(rs.getTimestamp("data_ldv"));
				o.setIdCorriere(rs.getInt("id_corriere"));
				o.setNomeCorriere(Methods.cercaNomeCorriere(o.getIdCorriere()));
				
				o.setCommento(rs.getString("commento"));
				o.setTotale(rs.getDouble("totale"));
				o.setStato(rs.getString("stato"));
				o.setQuantitaAcquistata(rs.getInt("quantita_acquistata"));
				o.setValuta(rs.getString("valuta"));
				o.setCostoSpedizione(rs.getDouble("costo_spedizione"));
				o.setBomboniere(rs.getBoolean("bomboniere"));
				
				o.setCostoProdotti(Methods.round(o.getTotale()-o.getCostoSpedizione(),2));
				
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
				inSp.setCellulare(rs.getString("spedizione_cellulare"));
				
				o.setIndirizzoSpedizione(inSp);
				
				
				if (maparticoli.containsKey(o.getIdOrdinePiattaforma()))
					o.setElencoArticoli(maparticoli.get(o.getIdOrdinePiattaforma()));
				
				ordini.add(o);
				
			}
			Log.debug("Lista degli ordini nella coda LDV ottenuta:"+ordini.size());

		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
		}
		 finally {			 
				 DataSource.closeConnections(con,null,ps,rs);	
		}
		return ordini;
	}
	
	public static int salvaNumeroTracciamento(String idOrdine, String numeroTracciamento, String data){
		Connection con = null;
		PreparedStatement ps = null;
		int res = 0;
		
		try {	
			con = DataSource.getLocalConnection();
			
			String query = "update ordini set numero_tracciamento = ?, data_spedizione = ? where id_ordine = ?";
			
			ps = con.prepareStatement(query);
			
			DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			Date date = format.parse(data);		
			date = DateMethods.creaDataConOra(date, 17, 30);
			Timestamp t1 = new Timestamp(date.getTime());
			ps.setTimestamp(5, t1);
			
			ps.setString(1, numeroTracciamento);
			ps.setTimestamp(2, t1);
			ps.setString(3, idOrdine);
			
			res = ps.executeUpdate();
			
		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
		}
		 finally {			 
				 DataSource.closeConnections(con,null,ps,null);	
		}
		 return res;
	}
	
	public static int salvaNumeriTracciamento(List<Map<String,String>> numeri){
		Connection con = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		int res = 0;
		Log.info("Salvataggio nel database di "+numeri.size()+" numeri di tracciamento...");
		
		try {	
			con = DataSource.getLocalConnection();
			
			String query1 = "UPDATE ordini " +
										"SET numero_tracciamento = ?, data_spedizione = ?, id_corriere = ?, stato=? " +
										"WHERE id_ordine = ?";
			
			String query2 = "UPDATE ordini " +
										"SET numero_tracciamento = ?, data_spedizione = ?, id_corriere = ?, stato=? " +
										"WHERE id_ordine_piattaforma = ?";
			
			ps1 = con.prepareStatement(query1);
			ps2 = con.prepareStatement(query2);
			
			DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			
			for (Map<String,String> num : numeri){
				
				Date date = format.parse(num.get("data"));		
				date = DateMethods.creaDataConOra(date, 17, 30);
				Timestamp t = new Timestamp(date.getTime());
				
				if (num.containsKey("id_ordine_piattaforma")){
					ps2.setString(1, num.get("numero_tracciamento"));
					ps2.setTimestamp(2, t);
					ps2.setInt(3, Integer.valueOf(num.get("id_corriere")));
					ps2.setString(4, "Spedito");
					ps2.setString(5, num.get("id_ordine_piattaforma"));
					
					res = res+ps2.executeUpdate();
				}
				else {
					ps1.setString(1, num.get("numero_tracciamento"));
					ps1.setTimestamp(2, t);
					ps1.setInt(3, Integer.valueOf(num.get("id_corriere")));
					ps1.setString(4, "Spedito");
					ps1.setString(5, num.get("id_ordine"));
					
					res = res+ps1.executeUpdate();
				}
			}
			
			con.commit();
			
			Log.info("Salvataggio completato.");
			
		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
		}
		 finally {			 			 	
				 DataSource.closeConnections(con,null,ps1,null);	
				 DataSource.closeStatements(null,ps1,null);	
		}
		return res;
	}
	
	/** Get back a list of the tracking numbers of the selected date. Parameter "piattaforme": 0 for all, 1 for just amazon, 2 for just ebay and website */
	public static List<Map<String,String>> getNumeriTracciamento(Date d, int piattaforme){
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Map<String,String>> numeri = null;
		
		Log.info("Cerco di ottenere la lista di numeri tracciamento per il giorno "+d.toString());
		
		try {	
			con = DataSource.getLocalConnection();
			
			Date d1 = DateMethods.oraDelleStreghe(d);
			Date d2 = DateMethods.ventitreCinquantanove(d);
			
			Timestamp t1 = new Timestamp(d1.getTime());
			Timestamp t2 = new Timestamp(d2.getTime());
			
			String filtroPiattaforma = "";
			if (piattaforme==1) filtroPiattaforma = "AND `piattaforma` like 'Amazon%' ";
			else if (piattaforme==2) filtroPiattaforma = "AND ( `piattaforma` = 'eBay' OR  `piattaforma` = 'ZeldaBomboniere.it' ) ";
			
			String query = "SELECT `id_ordine`, `id_ordine_piattaforma`, `piattaforma`, `data_spedizione`, o.id_corriere, `nome_corriere`, " +
												"`link_tracciamento`, `numero_tracciamento`,`email` " +
									"FROM `ordini` as o " +
									"LEFT JOIN `corrieri` as c ON o.id_corriere = c.id_corriere " +
									"WHERE  `numero_tracciamento` is not null " +
										"AND `data_spedizione` between ? and ? " + 
										filtroPiattaforma +
									"ORDER BY `piattaforma` ASC, `id_ordine` ASC";
			
			ps = con.prepareStatement(query);
			ps.setTimestamp(1, t1);
			ps.setTimestamp(2, t2);
			
			System.out.println(ps);
			
			rs = ps.executeQuery();
			
			numeri = new ArrayList<Map<String,String>>();
			
			while (rs.next()){
				Map<String,String> m = new HashMap<String,String>();
				
				m.put("id_ordine", rs.getString("id_ordine"));
				m.put("id_ordine_piattaforma", rs.getString("id_ordine_piattaforma"));
				m.put("piattaforma", rs.getString("piattaforma"));
				m.put("id_corriere", rs.getString("id_corriere"));
				m.put("nome_corriere", rs.getString("nome_corriere"));
				m.put("link_tracking", rs.getString("link_tracciamento"));
				m.put("numero_tracciamento", rs.getString("numero_tracciamento"));
				m.put("data", DateMethods.formattaData1(rs.getTimestamp("data_spedizione")));
				m.put("email", rs.getString("email"));
				
				numeri.add(m);
				
			}
			Log.debug("Lista ottenuta, occorrenze: "+numeri.size());

		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
		}
		 finally {			 
				 DataSource.closeConnections(con,null,ps,rs);	
		}
		return numeri;
	}

}
