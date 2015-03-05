package it.swb.database;

import it.swb.business.ArticoloBusiness;
import it.swb.business.CategorieBusiness;
import it.swb.log.Log;
import it.swb.model.Articolo;
import it.swb.model.Categoria;
import it.swb.model.Cliente;
import it.swb.model.Indirizzo;
import it.swb.model.InfoEbay;
import it.swb.model.Variante_Articolo;
import it.swb.utility.Methods;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InserzioniEbay_DAO {
	

	
	public static List<Articolo> getInserzioni(){
		Log.info("Cerco di ottenere la lista di inserzioni ebay...");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		List<Articolo> articoli = null;

		try {			
			con = DataSource.getTestConnection();
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM INSERZIONI_EBAY ORDER BY codice ASC");
			
			Map<Long, String> catEbay = CategorieBusiness.getInstance().getMappaCategorieEbay(null);
			Map<Long,Categoria> catMap = CategorieBusiness.getInstance().getMappaCategorieNegozioEbay();
			Map<String,Articolo> articoliMap = ArticoloBusiness.getInstance().getMappaArticoli();
			
			articoli = new ArrayList<Articolo>();
			int i = 1;
			
			while (rs.next()){
				Articolo a = new Articolo();
				
				a.setIdArticolo(rs.getInt("id_articolo"));
				a.setCodice(rs.getString("codice"));
				a.setIdEbay(rs.getString("id_ebay"));
				a.setPrezzoDettaglio(rs.getInt("prezzo_dettaglio"));
				a.setPrezzoPiattaforme(rs.getInt("prezzo_dettaglio"));
				a.setQuantitaMagazzino(rs.getInt("quantita"));
				a.setQuantitaInserzione(rs.getString("quantita_inserzione"));
				a.setDimensioni(rs.getString("dimensioni"));
				a.setDescrizione(rs.getString("descrizione"));
				
				if (rs.getLong("id_categoria")!=-1){
					Categoria cc = catMap.get(rs.getLong("id_categoria"));
					if (cc!=null){
						a.setCategoria(cc);
						a.setIdCategoria(rs.getLong("id_categoria"));
					} 
					else a.setIdCategoria(-1);
				}
				else a.setIdCategoria(-1);
				
				
				if (rs.getString("IMMAGINE1")!=null && rs.getString("IMMAGINE1").isEmpty()) a.setImmagine1(null);
				else a.setImmagine1(rs.getString("IMMAGINE1"));
				if (rs.getString("IMMAGINE2")!=null && rs.getString("IMMAGINE2").isEmpty()) a.setImmagine2(null);
				else a.setImmagine2(rs.getString("IMMAGINE2"));
				if (rs.getString("IMMAGINE3")!=null && rs.getString("IMMAGINE3").isEmpty()) a.setImmagine3(null);
				else a.setImmagine3(rs.getString("IMMAGINE3"));
				if (rs.getString("IMMAGINE4")!=null && rs.getString("IMMAGINE4").isEmpty()) a.setImmagine4(null);
				else a.setImmagine4(rs.getString("IMMAGINE4"));
				if (rs.getString("IMMAGINE5")!=null && rs.getString("IMMAGINE5").isEmpty()) a.setImmagine5(null);
				else a.setImmagine5(rs.getString("IMMAGINE5"));
						
				InfoEbay ei = new InfoEbay();
				ei.setTitoloInserzione(rs.getString("TITOLO_INSERZIONE"));
				
				if (rs.getString("ID_CATEGORIA_EBAY_1")!=null && !rs.getString("ID_CATEGORIA_EBAY_1").trim().isEmpty()) {
					ei.setIdCategoria1(rs.getString("ID_CATEGORIA_EBAY_1"));
					
					ei.setNomeCategoria1(catEbay.get(Long.valueOf(rs.getString("ID_CATEGORIA_EBAY_1"))));
				}
				if (rs.getString("ID_CATEGORIA_EBAY_2")!=null && !rs.getString("ID_CATEGORIA_EBAY_2").trim().isEmpty()){
					ei.setIdCategoria2(rs.getString("ID_CATEGORIA_EBAY_2"));
					
					ei.setNomeCategoria2(catEbay.get(Long.valueOf(rs.getString("ID_CATEGORIA_EBAY_2"))));
				}
				a.setInfoEbay(ei);
				
				if (rs.getString("PAROLE_CHIAVE_3")!=null) a.setParoleChiave3(rs.getString("PAROLE_CHIAVE_3"));
				if (rs.getString("PAROLE_CHIAVE_4")!=null) a.setParoleChiave4(rs.getString("PAROLE_CHIAVE_4"));
				
				if (rs.getString("PAROLE_CHIAVE_5")!=null) a.setParoleChiave5(rs.getString("PAROLE_CHIAVE_5"));
				else if (articoliMap.containsKey(a.getCodice())) a.setParoleChiave5(a.getCodice());
								
				articoli.add(a);
				i++;
			}
			Log.info("Ottenute "+i+" inserzioni.");

		} catch (Exception ex) {
			Log.info(ex);
			ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,st,null,rs);
		}
		return articoli;
	}
	
	
	
	public static Map<String,String> getMappaInserzioni(){
		Log.info("Cerco di ottenere la mappa di clienti Zelda...");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		Map<String,String> clienti = null;

		try {			
			con = DataSource.getTestConnection();
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM inserzioni_ebay");
			
			clienti = new HashMap<String,String>();
			
			while (rs.next()){
				//clienti.put(rs.getString("USERNAME"),rs.getString("NOME_COMPLETO"));
			}
			Log.info("Mappa clienti Zelda ottenuta.");

		} catch (Exception ex) {
			Log.info(ex);
			ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,st,null,rs);
		}
		return clienti;
	}
	
	public static Map<String,Cliente> getMappaInserzioniCompleta(){
		Log.info("Cerco di ottenere la mappa di clienti Zelda...");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		Map<String,Cliente> clienti = null;

		try {			
			con = DataSource.getTestConnection();
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM inserzioni_ebay");
			
			clienti = new HashMap<String,Cliente>();
			
			while (rs.next()){
				Cliente c = new Cliente();
				
				c.setUsername(rs.getString("USERNAME"));
				c.setNomeCompleto(rs.getString("NOME_COMPLETO"));
				c.setAzienda(rs.getString("azienda"));
				c.setCellulare(rs.getString("cellulare"));
				c.setTelefono(rs.getString("telefono"));
				c.setIdCliente(rs.getInt("id_utente"));
				c.setEmail(rs.getString("email"));
				c.setPiattaforma(rs.getString("piattaforma"));
				c.setPartitaIva(rs.getString("partita_iva"));
				c.setCodiceFiscale(rs.getString("codice_fiscale"));
				c.setFax(rs.getString("fax"));
				
				Indirizzo is = new Indirizzo();
				is.setAzienda(rs.getString("azienda"));
				is.setCap(rs.getString("cap_spedizione"));
				is.setIndirizzo2(rs.getString("civico_spedizione"));
				is.setComune(rs.getString("comune_spedizione"));
				is.setProvincia(rs.getString("provincia_spedizione"));
				is.setNazione(rs.getString("nazione_spedizione"));
				is.setIndirizzo1(rs.getString("indirizzo_spedizione"));
				
				c.setIndirizzoSpedizione(is);
				
				if (c.getUsername()!=null && !c.getUsername().isEmpty())
					clienti.put(c.getUsername(),c);
				else clienti.put(c.getEmail(), c);
			}
			Log.info("Mappa clienti Zelda ottenuta.");

		} catch (Exception ex) {
			Log.info(ex);
			ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,st,null,rs);
		}
		return clienti;
	}	
	
	public static int modificaInserzione(Articolo art){
		Log.info("Modifica articolo con codice "+art.getCodice()+" e ID "+art.getIdArticolo()+" nel database test...");
		Connection con = null;
		PreparedStatement ps = null;
		int result = 0;

		try {			
			con = DataSource.getTestConnection();
			String query = "UPDATE INSERZIONI_EBAY SET `codice` = ?,`nome` = ?,`id_categoria` = ?,`prezzo_ingrosso` = ?,`prezzo_dettaglio` = ?," +	/*5*/
												"`costo_acquisto` = ?,`quantita` = ?,`note` = ?,`unita_misura` = ?,`dimensioni` = ?," +	/*10*/
												"`quantita_inserzione` = ?,`descrizione` = ?,`descrizione_breve` = ?,`codice_fornitore` = ?," +
												"`codice_articolo_fornitore` = ?," +	/*15*/
												"`codice_barre` = ?,`tipo_codice_barre` = ?,`data_ultima_modifica` = ?,`aliquota_iva` = ?," +	/*19*/
												"`immagine1` = ?,`immagine2` = ?,`immagine3` = ?,`immagine4` = ?,`immagine5` = ?, " + /*24*/
												"`presente_su_ebay`= ?, `presente_su_gm`= ?, `presente_su_amazon`= ?,  "+ /*27*/
												"`quantita_effettiva` = ?,`costo_spedizione` = ?,`prezzo_piattaforme` = ?,`id_categoria_2` = ?,   "+
												"`parole_chiave_1` = ?,`parole_chiave_2` = ?,`parole_chiave_3` = ?,`parole_chiave_4` = ?,`parole_chiave_5` = ?, "+ /*36*/
												"`id_ebay` = ? "+
												"WHERE `id_articolo` = ?";  /*sono 38*/
			ps = con.prepareStatement(query);
			ps.setString(1, art.getCodice());				
			ps.setString(2, art.getNome());
			ps.setLong(3, art.getIdCategoria());
			ps.setDouble(4, art.getPrezzoIngrosso());
			ps.setDouble(5, art.getPrezzoDettaglio());
			
			ps.setDouble(6, art.getCostoAcquisto());
			ps.setInt(7, art.getQuantitaMagazzino());
			ps.setString(8, art.getNote());
			ps.setString(9, art.getUnitaMisura());
			ps.setString(10, art.getDimensioni());		
			
			ps.setString(11, art.getQuantitaInserzione());
			ps.setString(12, art.getDescrizione());
			ps.setString(13, art.getDescrizioneBreve());
			ps.setString(14, art.getCodiceFornitore());
			ps.setString(15, art.getCodiceArticoloFornitore());
			
			ps.setString(16, art.getCodiceBarre());
			ps.setString(17, art.getTipoCodiceBarre());
			ps.setDate(18, new Date(new java.util.Date().getTime()));	//data ultima modifica
			ps.setDouble(19, art.getAliquotaIva());
			ps.setString(20, Methods.trimAndToLower(art.getImmagine1()));
			
			ps.setString(21, Methods.trimAndToLower(art.getImmagine2()));
			ps.setString(22, Methods.trimAndToLower(art.getImmagine3()));
			ps.setString(23, Methods.trimAndToLower(art.getImmagine4()));
			ps.setString(24, Methods.trimAndToLower(art.getImmagine5()));		

			ps.setInt(25, art.getPresente_su_ebay());
			ps.setInt(26, art.getPresente_su_gm());
			ps.setInt(27, art.getPresente_su_amazon());
			
			ps.setInt(28, art.getQuantitaEffettiva());
			ps.setDouble(29, art.getCostoSpedizione());
			ps.setDouble(30, art.getPrezzoPiattaforme());
			ps.setLong(31, art.getIdCategoria2());
			
			ps.setString(32, art.getParoleChiave1());
			ps.setString(33, art.getParoleChiave2());
			ps.setString(34, art.getParoleChiave3());
			ps.setString(35, art.getParoleChiave4());
			ps.setString(36, art.getParoleChiave5());	
			
			ps.setString(37, art.getIdEbay());
			
			ps.setLong(38, art.getIdArticolo());
			
			ps.executeUpdate();

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { con.rollback();
			} catch (SQLException e) { e.printStackTrace();	}
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,null);
		}
		return result;
	}

	
	public static int aggiornaCodiceCorrispondente(long idInserzione,String codice){
//		Log.info("Modifica cliente "+f.getCodiceCliente()+" nel database locale...");
		Connection con = null;
		PreparedStatement ps = null;
		int result = 0;

		try {			
			con = DataSource.getTestConnection();
			String query = "UPDATE inserzioni_ebay SET `parole_chiave_5`= ? WHERE `id_articolo` = ?"; 		

			ps = con.prepareStatement(query);
			ps.setString(1, codice);
			ps.setLong(2, idInserzione);
			
			result = ps.executeUpdate();
			
			con.commit();
			
		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { con.rollback();
			} catch (SQLException e) { e.printStackTrace();	}
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,null);
		}
		return result;
	}
	
	public static int associaArticolo(String codice, Articolo art, Map<String,Boolean> cosaAssociare){
		Log.info("Associa Articolo "+codice+" nel database locale...");
		Connection con = null;
		PreparedStatement ps = null;
		int result = 0;

		try {			
			con = DataSource.getLocalConnection();
			
			boolean virgola = false;
			String query = "UPDATE articoli SET ";
			
			if (cosaAssociare.get("dimensioni")) {
				query+="`dimensioni`= '"+art.getDimensioni()+"'";
				virgola=true;
			}
			if (cosaAssociare.get("quantita_inserzione")) {
				if (virgola) query+=",";
				virgola=true;
				query+="`quantita_inserzione`= '"+art.getQuantitaInserzione()+"'";
			}
			if (cosaAssociare.get("descrizione")) {
				if (virgola) query+=",";
				virgola=true;
				query+="`descrizione`= '"+art.getDescrizione()+"'";
			}
			if (cosaAssociare.get("quantita")) {
				if (virgola) query+=",";
				virgola=true;
				query+="`quantita`= "+art.getQuantitaMagazzino();
			}
			if (cosaAssociare.get("prezzo")) {
				if (virgola) query+=",";
				virgola=true;
				query+="`prezzo_piattaforme`= "+art.getPrezzoPiattaforme();
			}
			if (cosaAssociare.get("immagini")) {
				if (virgola) query+=",";
				virgola=true;
				query+="`immagine1`= '"+art.getImmagine1()+"' ";
				
				if (art.getImmagine2()!=null) query+=",`immagine2`= '"+art.getImmagine2()+"' ";
				if (art.getImmagine3()!=null) query+=",`immagine4`= '"+art.getImmagine3()+"' ";
				if (art.getImmagine4()!=null) query+=",`immagine4`= '"+art.getImmagine4()+"' ";
				if (art.getImmagine5()!=null) query+=",`immagine5`= '"+art.getImmagine5()+"' ";
			}
			
			if (virgola) query+=",";
			query+="`data_ultima_modifica`= ?,`titolo_inserzione`=?,`id_categoria_ebay_1`=?,`id_categoria_ebay_2`=?,`id_ebay`=? WHERE `codice` = ?"; 		

			ps = con.prepareStatement(query);
			ps.setDate(1, new java.sql.Date(new java.util.Date().getTime()));
			ps.setString(2, art.getInfoEbay().getTitoloInserzione());
			ps.setString(3, art.getInfoEbay().getIdCategoria1());
			ps.setString(4, art.getInfoEbay().getIdCategoria2());
			ps.setString(5, art.getIdEbay());
			ps.setString(6, codice);
			
			//System.out.println(query);
			
			result = ps.executeUpdate();
			
			con.commit();
			
		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { con.rollback();
			} catch (SQLException e) { e.printStackTrace();	}
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,null);
		}
		return result;
	}
	
	
	public static int associaVariante(Variante_Articolo v, String codice){
		Log.info("Associa Variante all'articolo "+codice+" nel database locale...");
		Connection con = null;
		PreparedStatement ps = null;
		int result = 0;

		try {			
			con = DataSource.getLocalConnection();
			
			String query = "INSERT INTO VARIANTI_ARTICOLO(`codice_articolo`,`tipo`,`valore`,`immagine`,`quantita`)" +
					" VALUES (?,?,?,?,?) " +
					" ON DUPLICATE KEY UPDATE tipo=?, immagine=?, quantita=?";
			ps = con.prepareStatement(query);
			ps.setString(1, codice);	
			ps.setString(2, v.getTipo());	
			ps.setString(3, Methods.primeLettereMaiuscole(v.getValore()));	
			ps.setString(4, Methods.trimAndToLower(v.getImmagine()));	
			ps.setInt(5, v.getQuantita());	
			
			ps.setString(6, v.getTipo());	
			ps.setString(7, Methods.trimAndToLower(v.getImmagine()));	
			ps.setInt(8, v.getQuantita());	
			
			ps.executeUpdate();
			
			result = ps.executeUpdate();
			
			con.commit();
			
		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { con.rollback();
			} catch (SQLException e) { e.printStackTrace();	}
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,null);
		}
		return result;
	}
	
	
	public static void eliminaInserzione(String codice_articolo){
		Log.info("eliminaInserzione("+codice_articolo+")...");
		Connection con = null;
		PreparedStatement ps = null;

		try {			
			con = DataSource.getTestConnection();
			ps = con.prepareStatement("DELETE FROM inserzioni_ebay WHERE CODICE = ? ");
			ps.setString(1, codice_articolo);
			ps.executeUpdate();
			
			Log.info("Inserzione eliminata");
			
			con.commit();

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,null);
		}
	}
	
	public static void eliminaInserzione(long id_articolo){
		Log.info("Elimina Inserzione "+id_articolo+"...");
		Connection con = null;
		PreparedStatement ps = null;

		try {			
			con = DataSource.getTestConnection();
			ps = con.prepareStatement("DELETE FROM inserzioni_ebay WHERE id_articolo = ? ");
			ps.setLong(1, id_articolo);
			ps.executeUpdate();
			
			Log.info("Inserzione eliminata");
			
			con.commit();

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,null);
		}
	}

}
