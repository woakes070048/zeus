package it.swb.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.PreparedStatement;

import it.swb.business.ArticoloBusiness;
import it.swb.business.CategorieBusiness;
import it.swb.business.LogBusiness;
import it.swb.business.VarianteBusiness;
import it.swb.log.Log;
import it.swb.model.Articolo;
import it.swb.model.Categoria;
import it.swb.model.Filtro;
import it.swb.model.InfoAmazon;
import it.swb.model.InfoEbay;
import it.swb.model.LogArticolo;
import it.swb.model.Variante_Articolo;
import it.swb.utility.Costanti;
import it.swb.utility.Methods;

public class Articolo_DAO {
	
	//static Log Log = Log.getLog("GeBisLog");
	
	public static List<Articolo> getArticoli(){
		Log.info("Caricamento lista degli articoli...");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		List<Articolo> articoli = null;

		try {			
			con = DataSource.getLocalConnection();
			st = con.createStatement();
			
			DbTool dbt = new DbTool(con,st,rs);
			
			Map<Long,Categoria> catMap = CategorieBusiness.getInstance().getMappaCategorie(dbt);
//			Map<Long, String> catEbay = CategorieBusiness.getInstance().getMappaCategorieEbay(dbt);
//			Map<Long, String> catAmazon = CategorieBusiness.getInstance().getMappaCategorieAmazon(dbt);
			Map<String, List<Variante_Articolo>> varianti = VarianteBusiness.getInstance().reloadMappaVarianti(dbt);
//			Map<String, List<LogArticolo>> logs = LogBusiness.getInstance().reloadMappaLogArticoli(dbt);
			
			Filtro f = ArticoloBusiness.getInstance().getFiltro();
			
			String select = "SELECT * FROM ARTICOLI ";
			String where = "";
			if (!f.getCodiceArticolo().trim().isEmpty() || !f.getCodiceBarre().trim().isEmpty() || !f.getNomeArticolo().trim().isEmpty() || f.getIdCategoria()>0){
				where = " WHERE ";
				boolean virgola = false;
				if (!f.getCodiceArticolo().trim().isEmpty()){
					where+=" CODICE LIKE '%"+f.getCodiceArticolo().trim().toUpperCase()+"%' ";
					virgola=true;
				} 
				if (!f.getCodiceBarre().trim().isEmpty()){
					if (virgola) where+=", ";
					where+=" CODICE_BARRE LIKE '%"+f.getCodiceBarre().trim().toUpperCase()+"%' ";
					virgola=true;
				} 
				if (!f.getNomeArticolo().trim().isEmpty()){
					if (virgola) where+=", ";
					where+=" NOME LIKE '%"+f.getNomeArticolo().trim().toUpperCase()+"%' ";
					virgola=true;
				} 
				if (f.getIdCategoria()>0){
					where+=" ID_CATEGORIA = "+f.getIdCategoria()+" ";
				} 
			}
			String order = " ORDER BY "+f.getOrdinamento1()+" "+f.getDirezioneOrdinamento1()+", "+f.getOrdinamento2()+" "+f.getDirezioneOrdinamento2();
			
			String query = select+where+order;
			
			//System.out.println(query);
			//query = "SELECT * FROM ARTICOLI ORDER BY DATA_ULTIMA_MODIFICA DESC, DATA_INSERIMENTO DESC ";
			
			rs = st.executeQuery(query);
			
			articoli = new ArrayList<Articolo>();
			int i = 0;
			
			while (rs.next()){
				Articolo a = new Articolo();
				
				a.setIdArticolo(rs.getLong("ID_ARTICOLO"));
				a.setCodice(rs.getString("CODICE"));
				a.setCodiceFornitore(rs.getString("CODICE_FORNITORE"));
				a.setCodiceArticoloFornitore(rs.getString("CODICE_ARTICOLO_FORNITORE"));
				a.setNome(rs.getString("NOME"));
//				a.setNote(rs.getString("NOTE"));
				a.setIdCategoria(rs.getLong("ID_CATEGORIA"));
				a.setIdCategoria2(rs.getLong("ID_CATEGORIA_2"));
				a.setCategoria(catMap.get(a.getIdCategoria()));
				a.setCategoria2(catMap.get(a.getIdCategoria2()));
				a.setPrezzoDettaglio(rs.getDouble("PREZZO_DETTAGLIO"));
				a.setPrezzoIngrosso(rs.getDouble("PREZZO_INGROSSO"));
				a.setPrezzoPiattaforme(rs.getDouble("PREZZO_PIATTAFORME"));
//				a.setCostoAcquisto(rs.getDouble("COSTO_ACQUISTO"));
//				a.setCostoSpedizione(rs.getDouble("COSTO_SPEDIZIONE"));
				a.setAliquotaIva(rs.getInt("ALIQUOTA_IVA"));
//				a.setTitoloInserzione(rs.getString("TITOLO_INSERZIONE"));
//				a.setIdEbay(rs.getString("ID_EBAY"));
				
//				a.setDimensioni(rs.getString("DIMENSIONI"));
				a.setQuantitaMagazzino(rs.getInt("QUANTITA"));
				a.setQuantitaEffettiva(rs.getInt("QUANTITA_EFFETTIVA"));
				a.setQuantitaInserzione(rs.getString("QUANTITA_INSERZIONE"));
//				a.setDescrizioneBreve(rs.getString("DESCRIZIONE_BREVE"));
//				a.setDescrizione(rs.getString("DESCRIZIONE"));
				a.setCodiceBarre(rs.getString("CODICE_BARRE"));
//				a.setTipoCodiceBarre(rs.getString("TIPO_CODICE_BARRE"));
				a.setDataInserimento(rs.getDate("DATA_INSERIMENTO"));
				a.setDataUltimaModifica(rs.getDate("DATA_ULTIMA_MODIFICA"));
				a.setPresente_su_ebay(rs.getInt("PRESENTE_SU_EBAY"));
				a.setPresente_su_gm(rs.getInt("PRESENTE_SU_GM"));
				a.setPresente_su_amazon(rs.getInt("PRESENTE_SU_AMAZON"));
				a.setPresente_su_zb(rs.getInt("PRESENTE_SU_ZB"));
				
//				a.setParoleChiave1(rs.getString("PAROLE_CHIAVE_1"));
//				a.setParoleChiave2(rs.getString("PAROLE_CHIAVE_2"));
//				a.setParoleChiave3(rs.getString("PAROLE_CHIAVE_3"));
//				a.setParoleChiave4(rs.getString("PAROLE_CHIAVE_4"));
//				a.setParoleChiave5(rs.getString("PAROLE_CHIAVE_5"));
				
				if (rs.getString("IMMAGINE1")!=null && rs.getString("IMMAGINE1").isEmpty()) a.setImmagine1(null);
				else a.setImmagine1(rs.getString("IMMAGINE1"));
				
//				if (rs.getString("IMMAGINE2")!=null && rs.getString("IMMAGINE2").isEmpty()) a.setImmagine2(null);
//				else a.setImmagine2(rs.getString("IMMAGINE2"));
//				
//				if (rs.getString("IMMAGINE3")!=null && rs.getString("IMMAGINE3").isEmpty()) a.setImmagine3(null);
//				else a.setImmagine3(rs.getString("IMMAGINE3"));
//				
//				if (rs.getString("IMMAGINE4")!=null && rs.getString("IMMAGINE4").isEmpty()) a.setImmagine4(null);
//				else a.setImmagine4(rs.getString("IMMAGINE4"));
//				
//				if (rs.getString("IMMAGINE5")!=null && rs.getString("IMMAGINE5").isEmpty()) a.setImmagine5(null);
//				else a.setImmagine5(rs.getString("IMMAGINE5"));
				
				a.setCodiciBarreVarianti(rs.getString("CODICI_BARRE_VARIANTI"));
				
				if (varianti.containsKey(a.getCodice()))
				{
					a.setHaVarianti(1);
//					a.setVarianti(varianti.get(a.getCodice()));
					
					String var = "";
					for (Variante_Articolo v : varianti.get(a.getCodice())){
						var+=v.getCodiceBarre()+" ";
					}
					a.setCodiciBarreVarianti(a.getCodiciBarreVarianti()+" "+var);
				}
				else a.setHaVarianti(0);
//				
//				if (logs.containsKey(a.getCodice()))
//				{
//					a.setLogArticolo(logs.get(a.getCodice()));
//				}
				
				/*	Costruzione informazioni eBay	*/
//				if (rs.getString("TITOLO_INSERZIONE")!=null && !rs.getString("TITOLO_INSERZIONE").trim().isEmpty()){				
//					InfoEbay ei = new InfoEbay();
//					ei.setTitoloInserzione(rs.getString("TITOLO_INSERZIONE"));
//					
//					if (rs.getString("ID_CATEGORIA_EBAY_1")!=null && !rs.getString("ID_CATEGORIA_EBAY_1").trim().isEmpty()) {
//						ei.setIdCategoria1(rs.getString("ID_CATEGORIA_EBAY_1"));
//						
//						ei.setNomeCategoria1(catEbay.get(Long.valueOf(rs.getString("ID_CATEGORIA_EBAY_1"))));
//					}
//						
//						
//					if (rs.getString("ID_CATEGORIA_EBAY_2")!=null && !rs.getString("ID_CATEGORIA_EBAY_2").trim().isEmpty()){
//						ei.setIdCategoria2(rs.getString("ID_CATEGORIA_EBAY_2"));
//						
//						ei.setNomeCategoria2(catEbay.get(Long.valueOf(rs.getString("ID_CATEGORIA_EBAY_2"))));
//					}
//					
//					
//					
//					a.setInfoEbay(ei);
//				}
				
				
				/* Fine costruzione informazioni eBay*/
				
				/*	informazioni amazon	*/
				//if (rs.getLong("NODO_AMAZON_1")!=0 ){				
//					InfoAmazon ia = new InfoAmazon();
//					ia.setIdCategoria1(rs.getLong("ID_CATEGORIA_AMAZON_1"));
//					ia.setIdCategoria2(rs.getLong("ID_CATEGORIA_AMAZON_2"));
//					ia.setNomeCategoria1(catAmazon.get(ia.getIdCategoria1()));
//					ia.setNomeCategoria2(catAmazon.get(ia.getIdCategoria2()));
//					ia.setVocePacchettoQuantita(rs.getInt("Voce_Pacchetto_Quantita"));
//					ia.setNumeroPezzi(rs.getInt("Numero_Pezzi"));
//					ia.setQuantitaMassimaSpedizioneCumulativa(rs.getInt("Quantita_Max_Spedizione"));
//					a.setInfoAmazon(ia);
				//}
				
				if (a.getImmagine1()!=null && !a.getImmagine1().trim().isEmpty())
				{
					String[] arr = Methods.dividiCartellaEImmagine(a.getImmagine1());
					a.setThumbnail(Costanti.percorsoImmaginiPiccoleRemoto+arr[0]+"/piccola_"+arr[1]);
				} else {
					a.setThumbnail(Costanti.percorsoImmaginiRemoto+"/noimage.gif");
				}			
				articoli.add(a);
				i++;
			}
			Log.info("Lista degli articoli caricata, "+i+" articoli ottenuti.");

		} catch (Exception ex) {
			Log.info(ex);
			ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,st,null,rs);
		}
		return articoli;
	}
	
	public static List<Articolo> getArticoli(String query){
		Log.debug("Cerco di ottenere la lista di articoli...");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		List<Articolo> articoli = null;

		try {			
			con = DataSource.getLocalConnection();
			st = con.createStatement();
			rs = st.executeQuery(query);
			
			articoli = new ArrayList<Articolo>();
			int i = 0;
			
			Map<Long,Categoria> catMap = CategorieBusiness.getInstance().getMappaCategorie();
			Map<String, List<Variante_Articolo>> varianti = VarianteBusiness.getInstance().getMappaVarianti();
			
			while (rs.next()){
				Articolo a = new Articolo();
				
				a.setIdArticolo(rs.getLong("ID_ARTICOLO"));
				a.setCodice(rs.getString("CODICE"));
				a.setCodiceFornitore(rs.getString("CODICE_FORNITORE"));
				a.setCodiceArticoloFornitore(rs.getString("CODICE_ARTICOLO_FORNITORE"));
				a.setNome(rs.getString("NOME"));
				a.setNote(rs.getString("NOTE"));
				a.setIdCategoria(rs.getLong("ID_CATEGORIA"));
				a.setIdCategoria2(rs.getLong("ID_CATEGORIA_2"));
				a.setCategoria(catMap.get(a.getIdCategoria()));
				a.setCategoria2(catMap.get(a.getIdCategoria2()));
				a.setPrezzoDettaglio(rs.getDouble("PREZZO_DETTAGLIO"));
				a.setPrezzoIngrosso(rs.getDouble("PREZZO_INGROSSO"));
				a.setPrezzoPiattaforme(rs.getDouble("PREZZO_PIATTAFORME"));
				a.setCostoAcquisto(rs.getDouble("COSTO_ACQUISTO"));
				a.setCostoSpedizione(rs.getDouble("COSTO_SPEDIZIONE"));
				a.setAliquotaIva(rs.getInt("ALIQUOTA_IVA"));
				a.setTitoloInserzione(rs.getString("TITOLO_INSERZIONE"));
				a.setIdEbay(rs.getString("ID_EBAY"));
				a.setDimensioni(rs.getString("DIMENSIONI"));
				a.setQuantitaMagazzino(rs.getInt("QUANTITA"));
				a.setQuantitaEffettiva(rs.getInt("QUANTITA_EFFETTIVA"));
				a.setQuantitaInserzione(rs.getString("QUANTITA_INSERZIONE"));
				a.setDescrizioneBreve(rs.getString("DESCRIZIONE_BREVE"));
				a.setDescrizione(rs.getString("DESCRIZIONE"));
				a.setCodiceBarre(rs.getString("CODICE_BARRE"));
				a.setTipoCodiceBarre(rs.getString("TIPO_CODICE_BARRE"));
				a.setDataInserimento(rs.getDate("DATA_INSERIMENTO"));
				a.setDataUltimaModifica(rs.getDate("DATA_ULTIMA_MODIFICA"));
				a.setPresente_su_ebay(rs.getInt("PRESENTE_SU_EBAY"));
				a.setPresente_su_gm(rs.getInt("PRESENTE_SU_GM"));
				a.setPresente_su_amazon(rs.getInt("PRESENTE_SU_AMAZON"));
				a.setPresente_su_zb(rs.getInt("PRESENTE_SU_ZB"));
				
				a.setParoleChiave1(rs.getString("PAROLE_CHIAVE_1"));
				a.setParoleChiave2(rs.getString("PAROLE_CHIAVE_2"));
				a.setParoleChiave3(rs.getString("PAROLE_CHIAVE_3"));
				a.setParoleChiave4(rs.getString("PAROLE_CHIAVE_4"));
				a.setParoleChiave5(rs.getString("PAROLE_CHIAVE_5"));
				
				if (rs.getString("IMMAGINE1")!=null && rs.getString("IMMAGINE1").isEmpty())
					a.setImmagine1(null);
				else a.setImmagine1(rs.getString("IMMAGINE1"));
				
				if (rs.getString("IMMAGINE2")!=null && rs.getString("IMMAGINE2").isEmpty())
					a.setImmagine2(null);
				else a.setImmagine2(rs.getString("IMMAGINE2"));
				
				if (rs.getString("IMMAGINE3")!=null && rs.getString("IMMAGINE3").isEmpty())
					a.setImmagine3(null);
				else a.setImmagine3(rs.getString("IMMAGINE3"));
				
				if (rs.getString("IMMAGINE4")!=null && rs.getString("IMMAGINE4").isEmpty())
					a.setImmagine4(null);
				else a.setImmagine4(rs.getString("IMMAGINE4"));
				
				if (rs.getString("IMMAGINE5")!=null && rs.getString("IMMAGINE5").isEmpty())
					a.setImmagine5(null);
				else a.setImmagine5(rs.getString("IMMAGINE5"));
				
				if (varianti.containsKey(a.getCodice()))
				{
					a.setHaVarianti(1);
					a.setVarianti(varianti.get(a.getCodice()));
				}
				else a.setHaVarianti(0);
				
				/*	Costruzione informazioni eBay	*/
				if (rs.getString("TITOLO_INSERZIONE")!=null && !rs.getString("TITOLO_INSERZIONE").trim().isEmpty()){				
					InfoEbay ei = new InfoEbay();
					ei.setTitoloInserzione(rs.getString("TITOLO_INSERZIONE"));
					
					if (rs.getString("ID_CATEGORIA_EBAY_1")!=null && !rs.getString("ID_CATEGORIA_EBAY_1").trim().isEmpty())
						ei.setIdCategoria1(rs.getString("ID_CATEGORIA_EBAY_1"));
						
					if (rs.getString("ID_CATEGORIA_EBAY_2")!=null && !rs.getString("ID_CATEGORIA_EBAY_2").trim().isEmpty())
						ei.setIdCategoria2(rs.getString("ID_CATEGORIA_EBAY_2"));
					
					
					
					a.setInfoEbay(ei);
				}
				/* Fine costruzione informazioni eBay*/
				
				/*	informazioni amazon	*/
				//if (rs.getLong("NODO_AMAZON_1")!=0 ){				
					InfoAmazon ia = new InfoAmazon();
					ia.setIdCategoria1(rs.getString("ID_CATEGORIA_AMAZON_1"));
					ia.setIdCategoria2(rs.getString("ID_CATEGORIA_AMAZON_2"));
					ia.setVocePacchettoQuantita(rs.getInt("Voce_Pacchetto_Quantita"));
					ia.setNumeroPezzi(rs.getInt("Numero_Pezzi"));
					ia.setQuantitaMassimaSpedizioneCumulativa(rs.getInt("Quantita_Max_Spedizione"));
					a.setInfoAmazon(ia);
				//}
				
				if (a.getImmagine1()!=null && !a.getImmagine1().trim().isEmpty())
				{
					String[] arr = Methods.dividiCartellaEImmagine(a.getImmagine1());
					a.setThumbnail(Costanti.percorsoImmaginiPiccoleRemoto+arr[0]+"/piccola_"+arr[1]);
				} else {
					a.setThumbnail(Costanti.percorsoImmaginiRemoto+"/noimage.gif");
				}			
				articoli.add(a);
				i++;
			}
			Log.debug("getArticoli(): "+i+" articoli ottenuti.");

		} catch (Exception ex) {
			Log.info(ex);
			ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,st,null,rs);
		}
		return articoli;
	}

	
	
	public static Map<String, String> getArticoliMap(){
		Log.debug("Caricamento mappa degli articoli...");
		Statement st = null;
		ResultSet rs = null;
		Map<String,String> mapart = null;
		Connection con = null;

		try {	
			
			con = DataSource.getLocalConnection();
			
			st = con.createStatement();
			rs = st.executeQuery("SELECT codice,quantita FROM articoli order by codice asc");
			
			mapart = new HashMap<String,String>();
			
			while (rs.next()){
				mapart.put(rs.getString("CODICE"), rs.getString("QUANTITA"));
			}
			Log.debug("Mappa degli articoli caricata.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {			 
				 DataSource.closeConnections(con,st,null,rs);			 
		}
		return mapart;
	}
	
	public static Map<String, Articolo> getMappaArticoliCompleta(){
		Log.debug("Caricamento mappa degli articoli...");
		Statement st = null;
		ResultSet rs = null;
		Map<String,Articolo> mapart = null;
		Connection con = null;

		try {	
			
			con = DataSource.getLocalConnection();
			st = con.createStatement();
			
			DbTool dbt = new DbTool(con,st,rs);
			
			Map<Long,Categoria> catMap = CategorieBusiness.getInstance().getMappaCategorie(dbt);
			Map<Long, String> catEbay = CategorieBusiness.getInstance().getMappaCategorieEbay(dbt);
			Map<String, String> catAmazon = CategorieBusiness.getInstance().getMappaCategorieAmazon(dbt);
			Map<String, List<Variante_Articolo>> varianti = VarianteBusiness.getInstance().reloadMappaVarianti(dbt);
			Map<String, List<LogArticolo>> logs = LogBusiness.getInstance().reloadMappaLogArticoli(dbt);
			
			rs = st.executeQuery("SELECT * FROM articoli order by codice asc");
			
			mapart = new HashMap<String,Articolo>();
			
			while (rs.next()){
				Articolo a = new Articolo();
				a.setIdArticolo(rs.getLong("ID_ARTICOLO"));
				a.setCodice(rs.getString("CODICE"));
				a.setCodiceFornitore(rs.getString("CODICE_FORNITORE"));
				a.setCodiceArticoloFornitore(rs.getString("CODICE_ARTICOLO_FORNITORE"));
				a.setNome(rs.getString("NOME"));
				a.setIdCategoria(rs.getLong("ID_CATEGORIA"));
				a.setIdCategoria2(rs.getLong("ID_CATEGORIA_2"));
				a.setCategoria(catMap.get(a.getIdCategoria()));
				a.setCategoria2(catMap.get(a.getIdCategoria2()));
				a.setPrezzoDettaglio(rs.getDouble("PREZZO_DETTAGLIO"));
				a.setPrezzoIngrosso(rs.getDouble("PREZZO_INGROSSO"));
				a.setPrezzoPiattaforme(rs.getDouble("PREZZO_PIATTAFORME"));
				a.setCostoAcquisto(rs.getDouble("COSTO_ACQUISTO"));
				a.setCostoSpedizione(rs.getDouble("COSTO_SPEDIZIONE"));
				a.setAliquotaIva(rs.getInt("ALIQUOTA_IVA"));	
				a.setTitoloInserzione(rs.getString("TITOLO_INSERZIONE"));
				a.setIdEbay(rs.getString("ID_EBAY"));
				a.setDimensioni(rs.getString("DIMENSIONI"));
				a.setQuantitaMagazzino(rs.getInt("QUANTITA"));
				a.setQuantitaEffettiva(rs.getInt("QUANTITA_EFFETTIVA"));
				a.setQuantitaInserzione(rs.getString("QUANTITA_INSERZIONE"));
				a.setDescrizioneBreve(rs.getString("DESCRIZIONE_BREVE"));
				a.setDescrizione(rs.getString("DESCRIZIONE"));
				a.setCodiceBarre(rs.getString("CODICE_BARRE"));
				a.setTipoCodiceBarre(rs.getString("TIPO_CODICE_BARRE"));
				a.setDataInserimento(rs.getDate("DATA_INSERIMENTO"));
				a.setDataUltimaModifica(rs.getDate("DATA_ULTIMA_MODIFICA"));
				
				a.setPresente_su_ebay(rs.getInt("PRESENTE_SU_EBAY"));
				a.setPresente_su_gm(rs.getInt("PRESENTE_SU_GM"));
				a.setPresente_su_amazon(rs.getInt("PRESENTE_SU_AMAZON"));
				a.setPresente_su_zb(rs.getInt("PRESENTE_SU_ZB"));
				
				a.setParoleChiave1(rs.getString("PAROLE_CHIAVE_1"));
				a.setParoleChiave2(rs.getString("PAROLE_CHIAVE_2"));
				a.setParoleChiave3(rs.getString("PAROLE_CHIAVE_3"));
				a.setParoleChiave4(rs.getString("PAROLE_CHIAVE_4"));
				a.setParoleChiave5(rs.getString("PAROLE_CHIAVE_5"));

				if (rs.getString("IMMAGINE1")!=null && rs.getString("IMMAGINE1").isEmpty())
					a.setImmagine1(null);
				else a.setImmagine1(rs.getString("IMMAGINE1"));
				
				if (rs.getString("IMMAGINE2")!=null && rs.getString("IMMAGINE2").isEmpty())
					a.setImmagine2(null);
				else a.setImmagine2(rs.getString("IMMAGINE2"));
				
				if (rs.getString("IMMAGINE3")!=null && rs.getString("IMMAGINE3").isEmpty())
					a.setImmagine3(null);
				else a.setImmagine3(rs.getString("IMMAGINE3"));
				
				if (rs.getString("IMMAGINE4")!=null && rs.getString("IMMAGINE4").isEmpty())
					a.setImmagine4(null);
				else a.setImmagine4(rs.getString("IMMAGINE4"));
				
				if (rs.getString("IMMAGINE5")!=null && rs.getString("IMMAGINE5").isEmpty())
					a.setImmagine5(null);
				else a.setImmagine5(rs.getString("IMMAGINE5"));
				
				if (varianti.containsKey(a.getCodice()))
				{
					a.setHaVarianti(1);
					a.setVarianti(varianti.get(a.getCodice()));
				}
				else a.setHaVarianti(0);
				
				if (logs.containsKey(a.getCodice()))
				{
					a.setLogArticolo(logs.get(a.getCodice()));
				}
				
				/*	Costruzione informazioni eBay	*/
				if (rs.getString("TITOLO_INSERZIONE")!=null && !rs.getString("TITOLO_INSERZIONE").trim().isEmpty()){				
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
				}
				/* Fine costruzione informazioni eBay*/
				
				/*	informazioni amazon	*/
					InfoAmazon ia = new InfoAmazon();
					ia.setIdCategoria1(rs.getString("ID_CATEGORIA_AMAZON_1"));
					ia.setIdCategoria2(rs.getString("ID_CATEGORIA_AMAZON_2"));
					ia.setNomeCategoria1(catAmazon.get(ia.getIdCategoria1()));
					ia.setNomeCategoria2(catAmazon.get(ia.getIdCategoria2()));
					ia.setVocePacchettoQuantita(rs.getInt("Voce_Pacchetto_Quantita"));
					ia.setNumeroPezzi(rs.getInt("Numero_Pezzi"));
					ia.setQuantitaMassimaSpedizioneCumulativa(rs.getInt("Quantita_Max_Spedizione"));
					a.setInfoAmazon(ia);
				
				if (a.getImmagine1()!=null && !a.getImmagine1().trim().isEmpty())
				{
					String[] arr = Methods.dividiCartellaEImmagine(a.getImmagine1());
					a.setThumbnail(Costanti.percorsoImmaginiPiccoleRemoto+arr[0]+"/piccola_"+arr[1]);
				} else {
					a.setThumbnail(Costanti.percorsoImmaginiRemoto+"/noimage.gif");
				}	

				
				mapart.put(rs.getString("CODICE"), a);
			}
			Log.debug("Mappa degli articoli caricata.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {			 
				 DataSource.closeConnections(con,st,null,rs);			 
		}
		return mapart;
	}
	
	public static List<Articolo> getArticoliByCodice(List<String> codici){
		DbTool dbt = new DbTool();
		
		List<Articolo>articoli = new ArrayList<Articolo>();
		
		for (String c : codici){
			articoli.add(getArticoloByCodice(c,dbt));
		}
		
		dbt.close();
		
		return articoli;
	}
	
	public static Articolo getArticoloByCodice(String codice, DbTool dbt){
		//Log.debug("Cerco di ottenere l'articolo "+codice);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Articolo a = null;

		try {			
			if (dbt==null){
				con = DataSource.getLocalConnection();				
			}else {
				con = dbt.getConnection();				
				rs = dbt.getResultSet();
			}
			
			ps = con.prepareStatement("SELECT * " +
										"FROM articoli " +
										"where codice = ?");
			ps.setString(1, codice);
			
			rs = ps.executeQuery();
			
			Map<String, List<Variante_Articolo>> varianti = VarianteBusiness.getInstance().getMappaVarianti();
			Map<Long,Categoria> catMap = CategorieBusiness.getInstance().getMappaCategorie();
			Map<Long, String> catEbay = CategorieBusiness.getInstance().getMappaCategorieEbay(dbt);
			Map<String, String> catAmazon = CategorieBusiness.getInstance().getMappaCategorieAmazon(dbt);
			Map<String, List<LogArticolo>> logs = LogBusiness.getInstance().getMappaLogArticoli(dbt);
			
			while (rs.next()){
				a = new Articolo();
				
				a.setIdArticolo(rs.getLong("ID_ARTICOLO"));
				a.setCodice(rs.getString("CODICE"));
				a.setCodiceFornitore(rs.getString("CODICE_FORNITORE"));
				a.setCodiceArticoloFornitore(rs.getString("CODICE_ARTICOLO_FORNITORE"));
				a.setNome(rs.getString("NOME"));
				a.setIdCategoria(rs.getLong("ID_CATEGORIA"));
				a.setIdCategoria2(rs.getLong("ID_CATEGORIA_2"));
				a.setCategoria(catMap.get(a.getIdCategoria()));
				a.setCategoria2(catMap.get(a.getIdCategoria2()));
				a.setPrezzoDettaglio(rs.getDouble("PREZZO_DETTAGLIO"));
				a.setPrezzoIngrosso(rs.getDouble("PREZZO_INGROSSO"));
				a.setPrezzoPiattaforme(rs.getDouble("PREZZO_PIATTAFORME"));
				a.setCostoAcquisto(rs.getDouble("COSTO_ACQUISTO"));
				a.setCostoSpedizione(rs.getDouble("COSTO_SPEDIZIONE"));
				a.setAliquotaIva(rs.getInt("ALIQUOTA_IVA"));
				a.setTitoloInserzione(rs.getString("TITOLO_INSERZIONE"));
				
					
				InfoEbay ei = new InfoEbay();
				
				ei.setTitoloInserzione(rs.getString("TITOLO_INSERZIONE"));
				ei.setIdOggetto(rs.getString("ID_EBAY"));
				
				if (rs.getString("ID_CATEGORIA_EBAY_1")!=null && !rs.getString("ID_CATEGORIA_EBAY_1").trim().isEmpty()) {
					ei.setIdCategoria1(rs.getString("ID_CATEGORIA_EBAY_1"));
					ei.setNomeCategoria1(catEbay.get(Long.valueOf(rs.getString("ID_CATEGORIA_EBAY_1"))));
				}
					
				if (rs.getString("ID_CATEGORIA_EBAY_2")!=null && !rs.getString("ID_CATEGORIA_EBAY_2").trim().isEmpty()){
					ei.setIdCategoria2(rs.getString("ID_CATEGORIA_EBAY_2"));
					ei.setNomeCategoria2(catEbay.get(Long.valueOf(rs.getString("ID_CATEGORIA_EBAY_2"))));
				}
				
				a.setInfoEbay(ei);
					
				a.setIdEbay(rs.getString("ID_EBAY"));
				a.setDimensioni(rs.getString("DIMENSIONI"));
				a.setQuantitaMagazzino(rs.getInt("QUANTITA"));
				a.setQuantitaEffettiva(rs.getInt("QUANTITA_EFFETTIVA"));
				a.setQuantitaInserzione(rs.getString("QUANTITA_INSERZIONE"));
				a.setDescrizioneBreve(rs.getString("DESCRIZIONE_BREVE"));
				a.setDescrizione(rs.getString("DESCRIZIONE"));
				a.setCodiceBarre(rs.getString("CODICE_BARRE"));
				a.setTipoCodiceBarre(rs.getString("TIPO_CODICE_BARRE"));
				a.setDataInserimento(rs.getDate("DATA_INSERIMENTO"));
				a.setDataUltimaModifica(rs.getDate("DATA_ULTIMA_MODIFICA"));
				
				a.setPresente_su_ebay(rs.getInt("PRESENTE_SU_EBAY"));
				a.setPresente_su_gm(rs.getInt("PRESENTE_SU_GM"));
				a.setPresente_su_amazon(rs.getInt("PRESENTE_SU_AMAZON"));
				a.setPresente_su_zb(rs.getInt("PRESENTE_SU_ZB"));
				
				a.setParoleChiave1(rs.getString("PAROLE_CHIAVE_1"));
				a.setParoleChiave2(rs.getString("PAROLE_CHIAVE_2"));
				a.setParoleChiave3(rs.getString("PAROLE_CHIAVE_3"));
				a.setParoleChiave4(rs.getString("PAROLE_CHIAVE_4"));
				a.setParoleChiave5(rs.getString("PAROLE_CHIAVE_5"));
				
				InfoAmazon ia = new InfoAmazon();
				ia.setIdCategoria1(rs.getString("ID_CATEGORIA_AMAZON_1"));
				ia.setIdCategoria2(rs.getString("ID_CATEGORIA_AMAZON_2"));
				ia.setNomeCategoria1(catAmazon.get(ia.getIdCategoria1()));
				ia.setNomeCategoria2(catAmazon.get(ia.getIdCategoria2()));
				ia.setVocePacchettoQuantita(rs.getInt("Voce_Pacchetto_Quantita"));
				ia.setNumeroPezzi(rs.getInt("Numero_Pezzi"));
				ia.setQuantitaMassimaSpedizioneCumulativa(rs.getInt("Quantita_Max_Spedizione"));
				a.setInfoAmazon(ia);

				if (rs.getString("IMMAGINE1")!=null && rs.getString("IMMAGINE1").isEmpty())
					a.setImmagine1(null);
				else a.setImmagine1(rs.getString("IMMAGINE1"));
				
				if (rs.getString("IMMAGINE2")!=null && rs.getString("IMMAGINE2").isEmpty())
					a.setImmagine2(null);
				else a.setImmagine2(rs.getString("IMMAGINE2"));
				
				if (rs.getString("IMMAGINE3")!=null && rs.getString("IMMAGINE3").isEmpty())
					a.setImmagine3(null);
				else a.setImmagine3(rs.getString("IMMAGINE3"));
				
				if (rs.getString("IMMAGINE4")!=null && rs.getString("IMMAGINE4").isEmpty())
					a.setImmagine4(null);
				else a.setImmagine4(rs.getString("IMMAGINE4"));
				
				if (rs.getString("IMMAGINE5")!=null && rs.getString("IMMAGINE5").isEmpty())
					a.setImmagine5(null);
				else a.setImmagine5(rs.getString("IMMAGINE5"));
				
				
				if (varianti.containsKey(a.getCodice()))
				{
					a.setHaVarianti(1);
					a.setVarianti(varianti.get(a.getCodice()));
				}
				else a.setHaVarianti(0);
				
				if (logs.containsKey(a.getCodice()))
					a.setLogArticolo(logs.get(a.getCodice()));
				
				if (a.getImmagine1()!=null && !a.getImmagine1().trim().isEmpty())
				{
					String[] arr = Methods.dividiCartellaEImmagine(a.getImmagine1());
					a.setThumbnail(Costanti.percorsoImmaginiPiccoleRemoto+arr[0]+"/piccola_"+arr[1]);
				} else {
					a.setThumbnail(Costanti.percorsoImmaginiRemoto+"/noimage.gif");
				}			
				//Log.debug("Articolo trovato: "+a.getNome());
			}
			
			//if (a==null)
				//Log.debug("Articolo NON trovato.");
			
		} catch (Exception ex) {
			Log.info(ex);
			ex.printStackTrace();
		}
		return a;
	}
	
	/** Non salva info piattaforme */
	public static int inserisciArticolo(Articolo art){
		Log.info("Inserimento articolo "+art.getCodice()+" nel database locale...");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int result = 0;

		try {			
			con = DataSource.getLocalConnection();
			String query = "INSERT INTO ARTICOLI(`codice`,`nome`,`id_categoria`,`prezzo_ingrosso`,`prezzo_dettaglio`," +	/*5*/
												"`costo_acquisto`,`quantita`,`note`,`unita_misura`,`dimensioni`," +	/*10*/
												"`quantita_inserzione`,`descrizione`,`descrizione_breve`,`codice_fornitore`," +
												"`codice_articolo_fornitore`," +	/*15*/
												"`codice_barre`,`tipo_codice_barre`,`data_inserimento`,`data_ultima_modifica`,`aliquota_iva`," +	/*20*/	
												"`immagine1`,`immagine2`,`immagine3`,`immagine4`,`immagine5`," + /*25*/	
												"`quantita_effettiva`,`costo_spedizione`,`prezzo_piattaforme`,`video`,`id_video`,`id_categoria_2`,`id_ebay`)"+ /* 32 */
												" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";  /*sono 32*/
			
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
			ps.setDate(18, new java.sql.Date(new java.util.Date().getTime()));
			ps.setDate(19, new java.sql.Date(new java.util.Date().getTime()));
			ps.setDouble(20, art.getAliquotaIva());
			
			ps.setString(21, Methods.trimAndToLower(art.getImmagine1()));
			ps.setString(22, Methods.trimAndToLower(art.getImmagine2()));
			ps.setString(23, Methods.trimAndToLower(art.getImmagine3()));
			ps.setString(24, Methods.trimAndToLower(art.getImmagine4()));
			ps.setString(25, Methods.trimAndToLower(art.getImmagine5()));
			
			ps.setInt(26, art.getQuantitaEffettiva());
			ps.setDouble(27, art.getCostoSpedizione());
			ps.setDouble(28, art.getPrezzoPiattaforme());
			if (art.getIdVideo()!=null)
				ps.setString(29, art.getVideo());
			else ps.setNull(29, Types.NULL);
			if (art.getIdVideo()!=null)
				ps.setString(30, art.getIdVideo());
			else ps.setNull(30, Types.NULL);
			
			ps.setLong(31, art.getIdCategoria2());
			ps.setString(32, art.getIdEbay());
						
			ps.executeUpdate();
			
			ps = con.prepareStatement("select id_articolo from articoli where codice = ? order by id_articolo asc");
			ps.setString(1, art.getCodice());
			rs = ps.executeQuery();
			
			while (rs.next()){
				result = rs.getInt("id_articolo");
			}
			
			String var = "";
			
			if (art.getVarianti()!=null && !art.getVarianti().isEmpty()){
				Variante_Articolo_DAO.inserisciOModificaVarianti(art.getVarianti(), art.getCodice(), con,ps);
				var = art.getVarianti().size()+" varianti.";
			}
			
			LogArticolo l = new LogArticolo();
			l.setCodiceArticolo(art.getCodice());
			l.setAzione("Creazione (da Zeus)");
			l.setNote("Articolo creato su Zeus. "+var);
			LogArticolo_DAO.inserisciLogArticolo(l, con, ps);
			
			con.commit();
			
			Log.info("Inserimento riuscito.");

		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
			try { con.rollback();
			} catch (SQLException e) { 
				Log.info(ex); e.printStackTrace();	
			}
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,rs);
		}
		return result;
	}
	
	
	/** Salva tutto, anche info piattaforme */
	public static int inserisciArticoloNew(Articolo art){
		Log.info("Inserimento articolo "+art.getCodice()+" nel database locale...");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int result = 0;

		try {			
			con = DataSource.getLocalConnection();
			String query = "INSERT INTO ARTICOLI(`codice`,`nome`,`id_categoria`,`prezzo_ingrosso`,`prezzo_dettaglio`," +	/*5*/
												"`costo_acquisto`,`quantita`,`note`,`unita_misura`,`dimensioni`," +	/*10*/
												"`quantita_inserzione`,`descrizione`,`descrizione_breve`,`codice_fornitore`," +
												"`codice_articolo_fornitore`," +	/*15*/
												"`codice_barre`,`tipo_codice_barre`,`data_inserimento`,`data_ultima_modifica`,`aliquota_iva`," +	/*20*/	
												"`immagine1`,`immagine2`,`immagine3`,`immagine4`,`immagine5`," + /*25*/	
												"`quantita_effettiva`,`costo_spedizione`,`prezzo_piattaforme`,`video`,`id_video`,`id_categoria_2`,"+ /* 31 */
												"`parole_chiave_1`,`parole_chiave_2`,`parole_chiave_3`,`parole_chiave_4`,`parole_chiave_5`,"+ /* 36 */
												"`titolo_inserzione`,`id_categoria_ebay_1`,`id_categoria_ebay_2`,"+ /* 39 */
												"`id_categoria_amazon_1`,`id_categoria_amazon_2`,`voce_pacchetto_quantita`,`numero_pezzi`,`quantita_max_spedizione`,`id_ebay`)"+ /* 45 */
												" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";  /*sono 45*/
			
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
			ps.setDate(18, new java.sql.Date(new java.util.Date().getTime()));
			ps.setDate(19, new java.sql.Date(new java.util.Date().getTime()));
			ps.setDouble(20, art.getAliquotaIva());
			
			ps.setString(21, Methods.trimAndToLower(art.getImmagine1()));
			ps.setString(22, Methods.trimAndToLower(art.getImmagine2()));
			ps.setString(23, Methods.trimAndToLower(art.getImmagine3()));
			ps.setString(24, Methods.trimAndToLower(art.getImmagine4()));
			ps.setString(25, Methods.trimAndToLower(art.getImmagine5()));
			
			ps.setInt(26, art.getQuantitaEffettiva());
			ps.setDouble(27, art.getCostoSpedizione());
			ps.setDouble(28, art.getPrezzoPiattaforme());
			if (art.getVideo()!=null)	ps.setString(29, art.getVideo());
			else ps.setNull(29, Types.NULL);
			if (art.getIdVideo()!=null)	ps.setString(30, art.getIdVideo());
			else ps.setNull(30, Types.NULL);
			
			ps.setLong(31, art.getIdCategoria2());
			
			ps.setString(32, art.getParoleChiave1());
			ps.setString(33, art.getParoleChiave2());
			ps.setString(34, art.getParoleChiave3());
			ps.setString(35, art.getParoleChiave4());
			ps.setString(36, art.getParoleChiave5());
			
			ps.setString(37, art.getInfoEbay().getTitoloInserzione());
			ps.setString(38, art.getInfoEbay().getIdCategoria1());
			ps.setString(39, art.getInfoEbay().getIdCategoria2());
			
			ps.setString(40, art.getInfoAmazon().getIdCategoria1());
			ps.setString(41, art.getInfoAmazon().getIdCategoria2());
			ps.setInt(42, art.getInfoAmazon().getVocePacchettoQuantita());
			ps.setInt(43, art.getInfoAmazon().getNumeroPezzi());
			ps.setInt(44, art.getInfoAmazon().getQuantitaMassimaSpedizioneCumulativa());
			
			ps.setString(45, art.getIdEbay());
						
			ps.executeUpdate();
			
			ps = con.prepareStatement("select id_articolo from articoli where codice = ?");
			ps.setString(1, art.getCodice());
			rs = ps.executeQuery();
			
			while (rs.next()){
				result = rs.getInt("id_articolo");
			}
			
			String var = "";
			
			if (art.getVarianti()!=null && !art.getVarianti().isEmpty()){
				Variante_Articolo_DAO.inserisciOModificaVarianti(art.getVarianti(), art.getCodice(), con,ps);
				var = art.getVarianti().size()+" varianti.";
			}
			
			LogArticolo l = new LogArticolo();
			l.setCodiceArticolo(art.getCodice());
			l.setAzione("Creazione (da Zeus)");
			l.setNote("Articolo creato su Zeus. "+var);
			LogArticolo_DAO.inserisciLogArticolo(l, con, ps);
			
			con.commit();
			
			Log.info("Salvataggio riuscito.");

		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
			try { con.rollback();
			} catch (SQLException e) { 
				Log.info(ex); e.printStackTrace();	
			}
			result = -2;
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,rs);
		}
		return result;
	}
	
	/** Per il salvataggio */
	public static int modificaArticoloNew(Articolo art){
		Log.info("Modifica articolo con codice "+art.getCodice()+" e ID "+art.getIdArticolo()+" nel database locale...");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int result = 0;

		try {			
			con = DataSource.getLocalConnection();
			String query = "UPDATE ARTICOLI SET `codice` = ?,`nome` = ?,`id_categoria` = ?,`prezzo_ingrosso` = ?,`prezzo_dettaglio` = ?," +	/*5*/
												"`costo_acquisto` = ?,`quantita` = ?,`note` = ?,`unita_misura` = ?,`dimensioni` = ?," +	/*10*/
												"`quantita_inserzione` = ?,`descrizione` = ?,`descrizione_breve` = ?,`codice_fornitore` = ?," +
												"`codice_articolo_fornitore` = ?," +	/*15*/
												"`codice_barre` = ?,`tipo_codice_barre` = ?,`data_ultima_modifica` = ?,`aliquota_iva` = ?," +	/*19*/
												"`immagine1` = ?,`immagine2` = ?,`immagine3` = ?,`immagine4` = ?,`immagine5` = ?, " + /*24*/
												"`quantita_effettiva` = ?,`costo_spedizione` = ?,`prezzo_piattaforme` = ?,`id_categoria_2` = ?,  "+ /* 28 */
												"`video` = ?,`id_video` = ?,"+
												"`parole_chiave_1` = ?,`parole_chiave_2` = ?,`parole_chiave_3` = ?,`parole_chiave_4` = ?,`parole_chiave_5` = ?, "+ /* 35 */
												"`titolo_inserzione`= ?,`id_categoria_ebay_1` = ?,`id_categoria_ebay_2` = ?,"+ /* 38 */
												"`id_categoria_amazon_1`= ?,`id_categoria_amazon_2`= ?, " + /* 40 */
												"`voce_pacchetto_quantita` = ?,`numero_pezzi` = ?,`quantita_max_spedizione` = ?, id_ebay = ? "+ /* 44 */
												" WHERE `id_articolo` = ?";  /*sono 45*/
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
			
			ps.setInt(25, art.getQuantitaEffettiva());
			ps.setDouble(26, art.getCostoSpedizione());
			ps.setDouble(27, art.getPrezzoPiattaforme());	
			
			ps.setLong(28, art.getIdCategoria2());
			
			if (art.getVideo()!=null) ps.setString(29, art.getVideo());
			else ps.setNull(29, Types.NULL);
			if (art.getIdVideo()!=null)	ps.setString(30, art.getIdVideo());
			else ps.setNull(30, Types.NULL);
			
			ps.setString(31, art.getParoleChiave1());
			ps.setString(32, art.getParoleChiave2());
			ps.setString(33, art.getParoleChiave3());
			ps.setString(34, art.getParoleChiave4());
			ps.setString(35, art.getParoleChiave5());
			
			ps.setString(36, art.getInfoEbay().getTitoloInserzione());
			ps.setString(37, art.getInfoEbay().getIdCategoria1());
			ps.setString(38, art.getInfoEbay().getIdCategoria2());
			
			ps.setString(39, art.getInfoAmazon().getIdCategoria1());
			ps.setString(40, art.getInfoAmazon().getIdCategoria2());
			ps.setInt(41, art.getInfoAmazon().getVocePacchettoQuantita());
			ps.setInt(42, art.getInfoAmazon().getNumeroPezzi());
			ps.setInt(43, art.getInfoAmazon().getQuantitaMassimaSpedizioneCumulativa());
			
			ps.setString(44, art.getIdEbay());
			
			ps.setLong(45, art.getIdArticolo());
			
			result = ps.executeUpdate();
			
			if (art.getVarianti()!=null && !art.getVarianti().isEmpty()){
				Variante_Articolo_DAO.inserisciOModificaVarianti(art.getVarianti(), art.getCodice(), con, ps);
			}
			
			LogArticolo l = new LogArticolo();
			l.setCodiceArticolo(art.getCodice());
			l.setAzione("Modifica");
			l.setNote("Modificate alcune informazioni");
			
			LogArticolo_DAO.inserisciLogArticolo(l, con, ps);
			
			con.commit();
			
			Log.info("Modifica riuscita.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { con.rollback();
			} catch (SQLException e) { Log.info(ex); e.printStackTrace();	}
			result = -2;
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,rs);
		}
		return result;
	}
	
	
	
	public static void inserisciOModificaArticoli(List<Articolo> articoli){
		DbTool dbt = new DbTool();
		
		Log.info("Inserimento o modifica di "+articoli.size()+" articoli nel database locale...");
		
		int i = 0;
		int j = 0;
		for (Articolo a : articoli) {
			if (checkIfArticoloExist(a.getCodice(),dbt)){
				modificaArticolo(a,dbt);
				j++;
			}				
			else {	inserisciArticolo(a,dbt);
				i++;
			}				
		}
		
		try {
			dbt.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try { dbt.rollback();
			} catch (SQLException e1) { 
				e1.printStackTrace();	
			}
		}
		
		Log.info("Fine. "+i+" articoli inseriti e "+j+" modificati.");
		
		dbt.close();
	}
	
	
	
	
	
	
	
	
	/*	Viene usato per l'inserimento di articoli direttamente dai file DBF del gestionale G1	*/
	public static void inserisciArticolo(Articolo art, DbTool dbt){
		//Log.debug("Inserimento articolo "+art.getCodice()+" nel database locale...");
		PreparedStatement ps = dbt.getPreparedStatement();

		try {			
			String query = "INSERT INTO ARTICOLI(`codice`,`nome`,`id_categoria`,`prezzo_ingrosso`,`prezzo_dettaglio`," +	/*5*/
												"`costo_acquisto`,`quantita`,`note`,`unita_misura`,`dimensioni`," +	/*10*/
												"`quantita_inserzione`,`descrizione`,`descrizione_breve`,`codice_fornitore`," +
												"`codice_articolo_fornitore`," +	/*15*/
												"`codice_barre`,`tipo_codice_barre`,`data_inserimento`,`data_ultima_modifica`,`aliquota_iva`," +	/*20*/	
												"`immagine1`,`immagine2`,`immagine3`,`immagine4`,`immagine5`," + /*25*/		
												"`quantita_effettiva`,`costo_spedizione`,`prezzo_piattaforme`,`id_categoria_2`)"+ /* 29 */
												" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";  /*sono 29*/
			
			ps = dbt.getConnection().prepareStatement(query);
			ps.setString(1, art.getCodice());			
			ps.setString(2, art.getNome());
			ps.setLong(3, art.getIdCategoria());
			ps.setDouble(4, art.getPrezzoIngrosso());
			ps.setDouble(5, art.getPrezzoDettaglio());
			
			ps.setDouble(6, art.getCostoAcquisto());
			ps.setInt(7, art.getQuantitaMagazzino());
			ps.setString(8, "");
			ps.setString(9, art.getUnitaMisura());
			ps.setString(10, art.getDimensioni());			
			
			ps.setString(11, art.getQuantitaInserzione());
			ps.setString(12, art.getDescrizione());
			ps.setString(13, art.getDescrizioneBreve());
			ps.setString(14, art.getCodiceFornitore());
			ps.setString(15, art.getCodiceArticoloFornitore());
			
			ps.setString(16, art.getCodiceBarre());
			ps.setString(17, art.getTipoCodiceBarre());
			ps.setDate(18, new java.sql.Date(new java.util.Date().getTime()));
			ps.setDate(19, new java.sql.Date(new java.util.Date().getTime()));
			ps.setDouble(20, art.getAliquotaIva());
			
			ps.setString(21, Methods.trimAndToLower(art.getImmagine1()));
			ps.setString(22, Methods.trimAndToLower(art.getImmagine2()));
			ps.setString(23, Methods.trimAndToLower(art.getImmagine3()));
			ps.setString(24, Methods.trimAndToLower(art.getImmagine4()));
			ps.setString(25, Methods.trimAndToLower(art.getImmagine5()));
			
			ps.setInt(26, art.getQuantitaEffettiva());
			ps.setDouble(27, 7);
			ps.setDouble(28, art.getPrezzoPiattaforme());
			
			ps.setLong(29, art.getIdCategoria2());
			
			ps.executeUpdate();
			
			String var = "";
			if (art.getVarianti()!=null && !art.getVarianti().isEmpty()){
				Variante_Articolo_DAO.inserisciOModificaVarianti(art.getVarianti(), art.getCodice(), dbt);
				var = art.getVarianti().size()+" varianti.";
			}
			
			LogArticolo l = new LogArticolo();
			l.setCodiceArticolo(art.getCodice());
			l.setAzione("Creazione (da Sync)");
			l.setNote("Articolo inserito dalla sincronizzazione. "+var);
			LogArticolo_DAO.inserisciLogArticolo(l, dbt);
			

		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
		}
	}
	
	
	/*
	*	Questo metodo NON modifica anche la presenza sulle piattaforme, a differenza di quello sotto.	
	*/
	public static int modificaArticolo(Articolo art, String s){
		Log.info("Modifica articolo con codice "+art.getCodice()+" e ID "+art.getIdArticolo()+" nel database locale...");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int result = 0;

		try {			
			con = DataSource.getLocalConnection();
			String query = "UPDATE ARTICOLI SET `codice` = ?,`nome` = ?,`id_categoria` = ?,`prezzo_ingrosso` = ?,`prezzo_dettaglio` = ?," +	/*5*/
												"`costo_acquisto` = ?,`quantita` = ?,`note` = ?,`unita_misura` = ?,`dimensioni` = ?," +	/*10*/
												"`quantita_inserzione` = ?,`descrizione` = ?,`descrizione_breve` = ?,`codice_fornitore` = ?," +
												"`codice_articolo_fornitore` = ?," +	/*15*/
												"`codice_barre` = ?,`tipo_codice_barre` = ?,`data_ultima_modifica` = ?,`aliquota_iva` = ?," +	/*19*/
												"`immagine1` = ?,`immagine2` = ?,`immagine3` = ?,`immagine4` = ?,`immagine5` = ?, " + /*24*/
												"`quantita_effettiva` = ?,`costo_spedizione` = ?,`prezzo_piattaforme` = ?,`id_categoria_2` = ?, `id_ebay` = ? "+
												"WHERE `id_articolo` = ?";  /*sono 30*/
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
			
			ps.setInt(25, art.getQuantitaEffettiva());
			ps.setDouble(26, art.getCostoSpedizione());
			ps.setDouble(27, art.getPrezzoPiattaforme());			
			ps.setLong(28, art.getIdCategoria2());
			ps.setString(29, art.getIdEbay());
			
			ps.setLong(30, art.getIdArticolo());
			
			ps.executeUpdate();
			
//			ps = con.prepareStatement("select id_articolo from articoli where codice = ? order by id_articolo asc");
//			ps.setString(1, art.getCodice());
//			rs = ps.executeQuery();
//			
//			while (rs.next()){
//				result = rs.getInt("id_articolo");
//			}
			result = 1;

			if (art.getVarianti()!=null && !art.getVarianti().isEmpty()){
				Variante_Articolo_DAO.inserisciOModificaVarianti(art.getVarianti(), art.getCodice(), con, ps);
			}
			
			LogArticolo l = new LogArticolo();
			l.setCodiceArticolo(art.getCodice());
			
			if (s.equals("sync")){
				l.setAzione("Sincronizzazione");
				l.setNote("Informazioni modificate: "+art.getNote());
			} else if (s.equals("ebay")){
				l.setAzione("Modifica (eBay)");
				l.setNote("Scaricate le informazioni dall'inserzione ebay");
			} else {
				l.setAzione("Modifica");
				l.setNote("Modificate alcune informazioni");
			}
			
			LogArticolo_DAO.inserisciLogArticolo(l, con, ps);
			
			con.commit();
			
			Log.info("Modifica riuscita.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { con.rollback();
			} catch (SQLException e) { Log.info(ex); e.printStackTrace();	}
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,rs);
		}
		return result;
	}
	
	
	
	/*
	*	Questo metodo modifica anche la presenza sulle piattaforme, a differenza di quello sopra.	
	*/
	public static int modificaArticolo2(Articolo art){
		Log.info("Modifica articolo con codice "+art.getCodice()+" e ID "+art.getIdArticolo()+" nel database locale...");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int result = 0;

		try {			
			con = DataSource.getLocalConnection();
			String query = "UPDATE ARTICOLI SET `codice` = ?,`nome` = ?,`id_categoria` = ?,`prezzo_ingrosso` = ?,`prezzo_dettaglio` = ?," +	/*5*/
												"`costo_acquisto` = ?,`quantita` = ?,`note` = ?,`unita_misura` = ?,`dimensioni` = ?," +	/*10*/
												"`quantita_inserzione` = ?,`descrizione` = ?,`descrizione_breve` = ?,`codice_fornitore` = ?," +
												"`codice_articolo_fornitore` = ?," +	/*15*/
												"`codice_barre` = ?,`tipo_codice_barre` = ?,`data_ultima_modifica` = ?,`aliquota_iva` = ?," +	/*19*/
												"`immagine1` = ?,`immagine2` = ?,`immagine3` = ?,`immagine4` = ?,`immagine5` = ?, " + /*24*/
												"`presente_su_ebay`= ?, `presente_su_gm`= ?, `presente_su_amazon`= ?, `presente_su_zb`= ?, "+ /*28*/
												"`quantita_effettiva` = ?,`costo_spedizione` = ?,`prezzo_piattaforme` = ?,`id_categoria_2` = ?,   "+
												"`parole_chiave_1` = ?,`parole_chiave_2` = ?,`parole_chiave_3` = ?,`parole_chiave_4` = ?,`parole_chiave_5` = ?, "+ /*37*/
												"`id_ebay` = ? "+
												"WHERE `id_articolo` = ?";  /*sono 39*/
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
			ps.setInt(28, art.getPresente_su_zb());
			
			ps.setInt(29, art.getQuantitaEffettiva());
			ps.setDouble(30, art.getCostoSpedizione());
			ps.setDouble(31, art.getPrezzoPiattaforme());
			ps.setLong(32, art.getIdCategoria2());
			
			ps.setString(33, art.getParoleChiave1());
			ps.setString(34, art.getParoleChiave2());
			ps.setString(35, art.getParoleChiave3());
			ps.setString(36, art.getParoleChiave4());
			ps.setString(37, art.getParoleChiave5());	
			
			ps.setString(38, art.getIdEbay());
			
			ps.setLong(39, art.getIdArticolo());
			
			ps.executeUpdate();
			
			LogArticolo l = new LogArticolo();
			l.setCodiceArticolo(art.getCodice());
			l.setAzione("Modifica");
			l.setNote("Modificato.");
			LogArticolo_DAO.inserisciLogArticolo(l, con, ps);
			
			con.commit();
			
//			ps = con.prepareStatement("select id_articolo from articoli where codice = ? order by id_articolo asc");
//			ps.setString(1, art.getCodice());
//			rs = ps.executeQuery();
//			
//			while (rs.next()){
//				result = rs.getInt("id_articolo");
//			}
			result = 1;
			
//			if (art.getVarianti()!=null && !art.getVarianti().isEmpty())
//				Variante_Articolo_DAO.inserisciVarianti(art.getVarianti(), art.getCodice(),con);
			
			Log.info("Modifica riuscita.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { con.rollback();
			} catch (SQLException e) { Log.info(ex); e.printStackTrace();	}
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,rs);
		}
		return result;
	}
	
	
	
	
	public static void modificaArticolo(Articolo art, DbTool dbt){
		//Log.debug("Modifica articolo nel database locale...");
		String query = "";
		PreparedStatement ps = dbt.getPreparedStatement();

		try {			
				
				query = "UPDATE ARTICOLI SET ";  
				boolean virgola = false;
				
				if (art.getNome()!=null && !art.getNome().isEmpty()) {
					query+=" `nome`= '"+art.getNome().replace("'", "\\'")+"'";
					virgola = true;
				} if (art.getCodiceFornitore()!=null && !art.getCodiceFornitore().isEmpty()) {
					if (virgola) query+=",";
					query+=" `codice_fornitore`= '"+art.getCodiceFornitore().replace("'", "\\'")+"'";
					virgola = true;
				} if (art.getCodiceArticoloFornitore()!=null && !art.getCodiceArticoloFornitore().isEmpty()) {
					if (virgola) query+=",";
					query+=" `codice_articolo_fornitore`= '"+art.getCodiceArticoloFornitore().replace("'", "\\'")+"'";
					virgola = true;
				} if (art.getCodiceBarre()!=null && !art.getCodiceBarre().isEmpty()) {
					if (virgola) query+=",";
					query+=" `codice_barre`= '"+art.getCodiceBarre().replace("'", "\\'")+"'";
					virgola = true;
				} if (art.getTipoCodiceBarre()!=null && !art.getTipoCodiceBarre().isEmpty()) {
					if (virgola) query+=",";
					query+=" `tipo_codice_barre`= '"+art.getTipoCodiceBarre().replace("'", "\\'")+"'";
					virgola = true;
				} if ((Integer)art.getAliquotaIva()!=null) {
					if (virgola) query+=",";
					query+=" `aliquota_iva`= "+art.getAliquotaIva();
					virgola = true;
				} if ((Long)art.getIdCategoria()!=null) {
					if (virgola) query+=",";
					query+=" `id_categoria`= "+art.getIdCategoria();
					virgola = true;
				} if ((Double)art.getPrezzoDettaglio()!=null) {
					if (virgola) query+=",";
					query+=" `prezzo_dettaglio`= "+art.getPrezzoDettaglio();
					virgola = true;
				} if ((Double)art.getPrezzoIngrosso()!=null) {
					if (virgola) query+=",";
					query+=" `prezzo_ingrosso`= "+art.getPrezzoIngrosso();
					virgola = true;
				} if ((Double)art.getCostoAcquisto()!=null) {
					if (virgola) query+=",";
					query+=" `costo_acquisto`= "+art.getCostoAcquisto();
				}
				
				if (virgola) query+=",";
				query+=" `data_inserimento` = '"+art.getDataInserimento()+"',`data_ultima_modifica` = '"+art.getDataUltimaModifica()+"' WHERE `codice` = '"+art.getCodice()+"'";
				
				ps = dbt.getConnection().prepareStatement(query);
								
				ps.executeUpdate();	
				
				LogArticolo l = new LogArticolo();
				l.setCodiceArticolo(art.getCodice());
				l.setAzione("Sincronizzazione");
				l.setNote("Informazioni modificate: "+art.getNote());
				LogArticolo_DAO.inserisciLogArticolo(l, dbt);
				
			//Log.debug("Modifica riuscita.");

		} catch (Exception ex) {
			Log.info(ex); 
			Log.error(query);
			ex.printStackTrace();
		}
	}
	
	
	
//	public static void modificaArticoli(List<Articolo> articoli){
//		Log.debug("Modifica articoli nel database locale...");
//		Connection con = null;
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//
//		try {			
//			con = DataSource.getLocalConnection();
//			
//			for (Articolo art : articoli){			
//				
//				String query = "UPDATE ARTICOLI SET `costo_acquisto` = ?,`codice_fornitore` = ?,`codice_articolo_fornitore` = ?," +	
//													"`codice_barre` = ?,`tipo_codice_barre` = ?, data_inserimento = ?,`data_ultima_modifica` = ? " +
//													"WHERE `codice` = ?";  
//				ps = con.prepareStatement(query);
//	
//				ps.setDouble(1, art.getCostoAcquisto());				
//				ps.setString(2, art.getCodiceFornitore());
//				ps.setString(3, art.getCodiceArticoloFornitore());
//				ps.setString(4, art.getCodiceBarre());
//				ps.setString(5, art.getTipoCodiceBarre());				
//				ps.setDate(6, art.getDataInserimento());
//				ps.setDate(7, art.getDataUltimaModifica());
//				ps.setString(8, art.getCodice());
//				
//				ps.executeUpdate();		
//			}
//			con.commit();
//			
//			Log.debug("Modifica riuscita.");
//
//		} catch (Exception ex) {
//			Log.info(ex); ex.printStackTrace();
//			try { con.rollback();
//			} catch (SQLException e) { Log.info(ex); e.printStackTrace();	}
//		}
//		 finally {
//			 DataSource.closeConnections(con,null,ps,rs);
//		}
//	}
	
	public static void modificaQuantitaArticoli(List<Articolo> articoli){
		Log.info("Modifica giacenza di "+articoli.size()+" articoli nel database locale...");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {			
			con = DataSource.getLocalConnection();
			
			for (Articolo art : articoli){			
				
				String query = "UPDATE ARTICOLI SET `quantita` = ?,`quantita_effettiva` = ? WHERE `codice` = ?";  
				ps = con.prepareStatement(query);
	
				ps.setInt(1, art.getQuantitaMagazzino());
				ps.setInt(1, art.getQuantitaEffettiva());
				ps.setString(3, art.getCodice());
				
				ps.executeUpdate();		
				
				LogArticolo l = new LogArticolo();
				l.setCodiceArticolo(art.getCodice());
				l.setAzione("Cambio giacenza");
				l.setNote("Giacenza modificata a "+art.getQuantitaMagazzino());
				LogArticolo_DAO.inserisciLogArticolo(l, con, ps);
			}
			con.commit();
			
			Log.info("Modifica riuscita.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { con.rollback();
			} catch (SQLException e) { Log.info(ex); e.printStackTrace();	}
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,rs);
		}
	}
	
	public static void modificaQuantitaArticolo(String codice, int quantita){
		Log.info("Modifica la giacenza di "+codice+" impostandola a "+quantita);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {			
			con = DataSource.getLocalConnection();
			
				
			String query = "UPDATE ARTICOLI SET `quantita` = ?, `quantita_effettiva` = ? WHERE `codice` = ?";  
			ps = con.prepareStatement(query);

			ps.setInt(1, quantita);
			ps.setInt(2, quantita);
			ps.setString(3, codice);
			
			ps.executeUpdate();		
			
			LogArticolo l = new LogArticolo();
			l.setCodiceArticolo(codice);
			l.setAzione("Cambio Giacenza");
			l.setNote("Giacenza modificata a "+quantita);
			LogArticolo_DAO.inserisciLogArticolo(l, con, ps);
			
			con.commit();
			
			Log.info("Modifica riuscita.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { con.rollback();
			} catch (SQLException e) { Log.info(ex); e.printStackTrace();	}
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,rs);
		}
	}
	
	
	public static boolean checkIfArticoloExist(String codiceArticolo,DbTool dbt){
		//Log.debug("checkIfArticoloExist("+codiceArticolo+")...");
		PreparedStatement ps = dbt.getPreparedStatement();
		ResultSet rs = dbt.getResultSet();
		boolean exist = false;

		try {			
			ps = dbt.getConnection().prepareStatement("SELECT ID_ARTICOLO FROM ARTICOLI WHERE UPPER(CODICE) LIKE ? ");
			ps.setString(1, codiceArticolo.toUpperCase().trim());
			rs = ps.executeQuery();
			
			while (rs.next()){
				exist = true;
				//Log.debug("checkIfArticoloExist("+codiceArticolo+"): esiste gia' un articolo con questo codice");
			}
			

		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
		}
		return exist;
	}	
	
	/*** Restituisce solo vero o falso */
	public static boolean checkIfArticoloExist(String codiceArticolo){
		Log.debug("checkIfArticoloExist("+codiceArticolo+")...");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean exist = false;

		try {			
			con = DataSource.getLocalConnection();
			ps = con.prepareStatement("SELECT ID_ARTICOLO FROM ARTICOLI WHERE UPPER(CODICE) LIKE ? ");
			ps.setString(1, codiceArticolo.toUpperCase().trim());
			rs = ps.executeQuery();
			
			while (rs.next()){
				exist = true;
				Log.debug("checkIfArticoloExist("+codiceArticolo+"): esiste gia' un articolo con questo codice");
			}
			

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,rs);
		}
		return exist;
	}
	
	/*** Se esiste restituisce l'ID altrimenti -1 */
	public static int checkIfArticoloExist2(String codiceArticolo){
		Log.debug("Controllo se esiste l'articolo "+codiceArticolo+"...");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int id_articolo = -1;

		try {			
			con = DataSource.getLocalConnection();
			ps = con.prepareStatement("SELECT ID_ARTICOLO FROM ARTICOLI WHERE UPPER(CODICE) LIKE ? ");
			ps.setString(1, codiceArticolo.toUpperCase().trim());
			rs = ps.executeQuery();
			
			while (rs.next()){
				id_articolo = rs.getInt("ID_ARTICOLO");
			}
			if (id_articolo<0) Log.debug(codiceArticolo+" non esiste."); else Log.debug(codiceArticolo+" esiste.");
			
		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,rs);
		}
		return id_articolo;
	}
	
	
	public static void eliminaArticolo(long id_articolo, String codiceArticolo){
		Log.info("Eliminazione articolo "+codiceArticolo+" con ID "+id_articolo+" dal database locale...");
		Connection con = null;
		PreparedStatement ps = null;

		try {			
			con = DataSource.getLocalConnection();
			ps = con.prepareStatement("DELETE FROM ARTICOLI WHERE ID_ARTICOLO = ? ");
			ps.setLong(1, id_articolo);
			ps.executeUpdate();
			
			LogArticolo l = new LogArticolo();
			l.setCodiceArticolo(codiceArticolo);
			l.setAzione("Eliminazione");
			l.setNote("Eliminato.");
			LogArticolo_DAO.inserisciLogArticolo(l, con, ps);
			
			con.commit();
			
			Log.info("Articolo eliminato dal database locale.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,null);
		}
	}
	
	public static void setPresenzaSu(List<String[]> codici, String piattaforma, int valore){
		Log.info("Imposto la presenza sulla piattaforma "+piattaforma+" degli articoli contenuti nel file...");
		Connection con = null;
		PreparedStatement ps = null;

		try {			
			con = DataSource.getLocalConnection();
			
			for (String[] s : codici){
	        	
	        	ps = con.prepareStatement("UPDATE ARTICOLI SET presente_su_"+piattaforma+" = ? WHERE CODICE = ?");
				ps.setInt(1, valore);
				ps.setString(2, s[0]);
				ps.executeUpdate();
				
				LogArticolo l = new LogArticolo();
				l.setCodiceArticolo(s[0]);
				l.setAzione("Modifica");
				l.setNote("Impostata a "+valore+" la presenza sulla piattaforma "+piattaforma);
				LogArticolo_DAO.inserisciLogArticolo(l, con, ps);
	        }
			
			con.commit();
			
		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,null);
		}
	}
	
	
	public static int setPresenzaSu(String codice_articolo, String piattaforma, int valore, String id_inserzione){
		Log.info("Imposto a "+valore+" la presenza dell'articolo "+codice_articolo+" sulla piattaforma "+piattaforma+".");
		Connection con = null;
		PreparedStatement ps = null;
		int res = 0;

		try {			
			con = DataSource.getLocalConnection();
			
			String query = "UPDATE ARTICOLI SET presente_su_"+piattaforma+" = ? WHERE CODICE = ?";
			
			if (id_inserzione!=null && !id_inserzione.isEmpty()){
				query = "UPDATE ARTICOLI SET presente_su_"+piattaforma+" = ?, id_ebay = ? WHERE CODICE = ?";
				ps = con.prepareStatement(query);
				ps.setInt(1, valore);
				ps.setString(2, id_inserzione);
				ps.setString(3, codice_articolo);
			} else{
				ps = con.prepareStatement(query);
				ps.setInt(1, valore);
				ps.setString(2, codice_articolo);
			}
			res = ps.executeUpdate();
			
			Log.info("Presenza dell'articolo "+codice_articolo+" sulla piattaforma "+piattaforma+" impostata.");
			
			LogArticolo l = new LogArticolo();
			l.setCodiceArticolo(codice_articolo);
			l.setAzione("Modifica");
			l.setNote("Impostata la presenza sulla piattaforma "+piattaforma);
			LogArticolo_DAO.inserisciLogArticolo(l, con, ps);
			
			con.commit();
			
			//Log.info("Presenza dell'articolo "+codice_articolo+" sulla piattaforma "+piattaforma+" impostata a "+valore+".");

		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,null);
		}
		return res;
	}
	
	public static void setPresenze(Articolo a){
		Log.info("Imposto le presenze dell'articolo "+a.getCodice()+" sulle piattaforme .");
		Connection con = null;
		PreparedStatement ps = null;

		try {			
			con = DataSource.getLocalConnection();
			ps = con.prepareStatement("UPDATE ARTICOLI SET presente_su_ebay = ?, presente_su_amazon = ?, presente_su_gm = ?, presente_su_zb = ? WHERE CODICE = ?");
			ps.setInt(1, a.getPresente_su_ebay());
			ps.setInt(2, a.getPresente_su_amazon());
			ps.setInt(3, a.getPresente_su_gm());
			ps.setInt(4, a.getPresente_su_zb());
			ps.setString(5, a.getCodice());
			ps.executeUpdate();
			
			Log.info("Presenze dell'articolo "+a.getCodice()+" sulle piattaforme impostate.");
			
			LogArticolo l = new LogArticolo();
			l.setCodiceArticolo(a.getCodice());
			l.setAzione("Modifica");
			l.setNote("Impostata la presenza sulle piattaforme.");
			LogArticolo_DAO.inserisciLogArticolo(l, con, ps);
			
			con.commit();
		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,null);
		}
	}
	
	
	public static void modificaInformazioniEbay(String codice_articolo,String id_ebay, InfoEbay ie,Connection con){
		Log.info("Salvataggio delle informazioni eBay dell'articolo "+codice_articolo);
		PreparedStatement ps = null;
		boolean closeCon = false;

		try {			
			if (con==null) {
				con = DataSource.getLocalConnection();
				closeCon=true;
			}
			ps = con.prepareStatement("UPDATE ARTICOLI SET `titolo_inserzione`= ?,`id_categoria_ebay_1` = ?,`id_categoria_ebay_2` = ?,`data_ultima_modifica` = ?, " +
												"`id_ebay`=? WHERE `codice` = ?");
			ps.setString(1, ie.getTitoloInserzione());
			ps.setString(2, ie.getIdCategoria1());
			ps.setString(3, ie.getIdCategoria2());
			ps.setDate(4, new java.sql.Date(new java.util.Date().getTime()));
			ps.setString(5, id_ebay);
			ps.setString(6, codice_articolo);
			
			ps.executeUpdate();
			
			
			LogArticolo l = new LogArticolo();
			l.setCodiceArticolo(codice_articolo);
			l.setAzione("Modifica");
			l.setNote("Modificate le informazioni eBay");
			LogArticolo_DAO.inserisciLogArticolo(l, con, ps);
			
			con.commit();
			
			Log.info("Fine salvataggio informazioni eBay dell'articolo "+codice_articolo+" .");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 if(closeCon)
				 DataSource.closeConnections(con,null,ps,null);
			 else DataSource.closeStatements(null, ps, null);
		}
	}
	
	
	public static void modificaInformazioniAmazon(String codice_articolo, InfoAmazon ia,Connection con){
		Log.info("Imposto le informazioni Amazon dell'articolo "+codice_articolo);
		PreparedStatement ps = null;
		boolean closeCon = false;

		try {			
			if (con==null) {
				con = DataSource.getLocalConnection();
				closeCon=true;
			}
			ps = con.prepareStatement("UPDATE ARTICOLI SET `id_categoria_amazon_1`= ?,`id_categoria_amazon_2`= ?,`data_ultima_modifica` = ?, " +
										"`voce_pacchetto_quantita` = ?,`numero_pezzi` = ?,`quantita_max_spedizione` = ? " +
												"WHERE `codice` = ?");
			ps.setString(1, ia.getIdCategoria1());
			ps.setString(2, ia.getIdCategoria2());
			ps.setDate(3, new java.sql.Date(new java.util.Date().getTime()));
			ps.setInt(4, ia.getVocePacchettoQuantita());
			ps.setInt(5, ia.getNumeroPezzi());
			ps.setInt(6, ia.getQuantitaMassimaSpedizioneCumulativa());
			
			ps.setString(7, codice_articolo);
			ps.executeUpdate();
			
			LogArticolo l = new LogArticolo();
			l.setCodiceArticolo(codice_articolo);
			l.setAzione("Modifica");
			l.setNote("Modificate le informazioni Amazon");
			LogArticolo_DAO.inserisciLogArticolo(l, con, ps);
			
			con.commit();
			
			Log.info("informazioni Amazon sull'articolo "+codice_articolo+" impostate.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 if(closeCon)
				 DataSource.closeConnections(con,null,ps,null);
			 else DataSource.closeStatements(null, ps, null);
		}
	}
	
	
	
	public static void salvaCodiciBarreVarianti(Map<String,List<String>> m){
		DbTool dbt = new DbTool();
		
		Log.info("Inizio salvaCodiciBarreVarianti "+m.size());
		
		List<String> l = new ArrayList<String>(m.keySet());
		
		for (String s : l){
			String aaa = "";
			for (String d : m.get(s)){
				aaa+=" "+d;
			}				
			salvaCodiciBarreVariante(s,aaa,dbt);
		}
		
		try {
			dbt.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try { dbt.rollback();
			} catch (SQLException e1) { 
				e1.printStackTrace();	
			}
		}
		
		Log.info("Fine.");
		
		dbt.close();
	}
	
	
	public static void salvaCodiciBarreVariante(String codice_articolo, String codiciBarreVariante,DbTool dbt){
		//Log.info("Imposto le informazioni Amazon dell'articolo "+codice_articolo);
		PreparedStatement ps = dbt.getPreparedStatement();

		try {			
			ps = dbt.getConnection().prepareStatement("UPDATE ARTICOLI SET `codici_barre_varianti`= ? WHERE `codice` = ?");
			
			ps.setString(1, codiciBarreVariante);
			ps.setString(2, codice_articolo);
			
			ps.executeUpdate();
			
//			LogArticolo l = new LogArticolo();
//			l.setCodiceArticolo(codice_articolo);
//			l.setAzione("Modifica");
//			l.setNote("");
//			LogArticolo_DAO.inserisciLogArticolo(l, con, ps);
			
			//con.commit();
			
			//Log.info("informazioni Amazon sull'articolo "+codice_articolo+" impostate.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}		 
	}
	
	public static int salvaArticoloInCodaInserzioni(Articolo a){
		Log.info("Inserimento articolo "+a.getCodice()+" nella coda inserzioni...");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int result = 0;

		try {			
			con = DataSource.getLocalConnection();
			String query = "INSERT INTO coda_inserzioni(`id_articolo`,`codice_articolo`,`elaborato`) " +
									"VALUES (?,?,0) " +
									"ON DUPLICATE KEY UPDATE elaborato = 0";
			
			ps = con.prepareStatement(query);
			ps.setLong(1, a.getIdArticolo());		
			ps.setString(2, a.getCodice());			
						
			result = ps.executeUpdate();
			
			LogArticolo l = new LogArticolo();
			l.setCodiceArticolo(a.getCodice());
			l.setAzione("Inserito in coda inserzioni");
			l.setNote("Articolo inserito in coda inserzioni");
			LogArticolo_DAO.inserisciLogArticolo(l, con, ps);
			
			con.commit();
			
			Log.info("Inserimento riuscito.");

		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
			try { con.rollback();
			} catch (SQLException e) { 
				Log.info(ex); e.printStackTrace();	
			}
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,rs);
		}
		return result;
	}
	
	public static List<Articolo> getArticoliInCodaInserzioni(){
		Log.info("Caricamento lista degli articoli nella coda inserzioni...");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		List<Articolo> articoli = null;

		try {			
			con = DataSource.getLocalConnection();
			st = con.createStatement();
			
			DbTool dbt = new DbTool(con,st,rs);
			
			Map<Long,Categoria> catMap = CategorieBusiness.getInstance().getMappaCategorie(dbt);
			Map<String, List<Variante_Articolo>> varianti = VarianteBusiness.getInstance().reloadMappaVarianti(dbt);
			
			String query = "SELECT a.* " +
									"FROM coda_inserzioni as ci " +
									"INNER JOIN articoli as a ON ci.id_articolo = a.id_articolo " +
									"WHERE ci.elaborato=0";
			
			rs = st.executeQuery(query);
			
			articoli = new ArrayList<Articolo>();
			int i = 0;
			
			while (rs.next()){
				Articolo a = new Articolo();
				
				a.setIdArticolo(rs.getLong("ID_ARTICOLO"));
				a.setCodice(rs.getString("CODICE"));
				a.setNome(rs.getString("NOME"));
				a.setNote(rs.getString("NOTE"));
				a.setIdCategoria(rs.getLong("ID_CATEGORIA"));
				a.setIdCategoria2(rs.getLong("ID_CATEGORIA_2"));
				a.setCategoria(catMap.get(a.getIdCategoria()));
				a.setCategoria2(catMap.get(a.getIdCategoria2()));
				a.setPrezzoDettaglio(rs.getDouble("PREZZO_DETTAGLIO"));
				a.setPrezzoIngrosso(rs.getDouble("PREZZO_INGROSSO"));
				a.setPrezzoPiattaforme(rs.getDouble("PREZZO_PIATTAFORME"));
				a.setCostoSpedizione(rs.getDouble("COSTO_SPEDIZIONE"));
				a.setAliquotaIva(rs.getInt("ALIQUOTA_IVA"));
				a.setTitoloInserzione(rs.getString("TITOLO_INSERZIONE"));
				a.setIdEbay(rs.getString("ID_EBAY"));
				
				a.setDimensioni(rs.getString("DIMENSIONI"));
				a.setQuantitaMagazzino(rs.getInt("QUANTITA"));
				a.setQuantitaEffettiva(rs.getInt("QUANTITA_EFFETTIVA"));
				a.setQuantitaInserzione(rs.getString("QUANTITA_INSERZIONE"));
				a.setDescrizioneBreve(rs.getString("DESCRIZIONE_BREVE"));
				a.setDescrizione(rs.getString("DESCRIZIONE"));
				a.setCodiceBarre(rs.getString("CODICE_BARRE"));
				a.setDataInserimento(rs.getDate("DATA_INSERIMENTO"));
				a.setDataUltimaModifica(rs.getDate("DATA_ULTIMA_MODIFICA"));
				a.setPresente_su_ebay(rs.getInt("PRESENTE_SU_EBAY"));
				a.setPresente_su_gm(rs.getInt("PRESENTE_SU_GM"));
				a.setPresente_su_amazon(rs.getInt("PRESENTE_SU_AMAZON"));
				a.setPresente_su_zb(rs.getInt("PRESENTE_SU_ZB"));
				
				a.setParoleChiave1(rs.getString("PAROLE_CHIAVE_1"));
				a.setParoleChiave2(rs.getString("PAROLE_CHIAVE_2"));
				a.setParoleChiave3(rs.getString("PAROLE_CHIAVE_3"));
				a.setParoleChiave4(rs.getString("PAROLE_CHIAVE_4"));
				a.setParoleChiave5(rs.getString("PAROLE_CHIAVE_5"));
				
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
				
				if (varianti.containsKey(a.getCodice()))
					a.setVarianti(varianti.get(a.getCodice()));

				
				/*	Costruzione informazioni eBay	*/
				if (rs.getString("TITOLO_INSERZIONE")!=null && !rs.getString("TITOLO_INSERZIONE").trim().isEmpty()){				
					InfoEbay ei = new InfoEbay();
					ei.setTitoloInserzione(rs.getString("TITOLO_INSERZIONE"));
					
					if (rs.getString("ID_CATEGORIA_EBAY_1")!=null && !rs.getString("ID_CATEGORIA_EBAY_1").trim().isEmpty()) {
						ei.setIdCategoria1(rs.getString("ID_CATEGORIA_EBAY_1"));
					}
						
					if (rs.getString("ID_CATEGORIA_EBAY_2")!=null && !rs.getString("ID_CATEGORIA_EBAY_2").trim().isEmpty()){
						ei.setIdCategoria2(rs.getString("ID_CATEGORIA_EBAY_2"));
					}
					
					a.setInfoEbay(ei);
				}
				/* Fine costruzione informazioni eBay*/
				
				/*	informazioni amazon	*/
					InfoAmazon ia = new InfoAmazon();
					ia.setIdCategoria1(rs.getString("ID_CATEGORIA_AMAZON_1"));
					ia.setIdCategoria2(rs.getString("ID_CATEGORIA_AMAZON_2"));
					ia.setVocePacchettoQuantita(rs.getInt("Voce_Pacchetto_Quantita"));
					ia.setNumeroPezzi(rs.getInt("Numero_Pezzi"));
					ia.setQuantitaMassimaSpedizioneCumulativa(rs.getInt("Quantita_Max_Spedizione"));
					a.setInfoAmazon(ia);
				/* Fine costruzione informazioni amazon*/
					
				articoli.add(a);
				i++;
			}
			Log.info("Lista degli articoli caricata, "+i+" articoli ottenuti.");

		} catch (Exception ex) {
			Log.info(ex);
			ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,st,null,rs);
		}
		return articoli;
	}
	
	
	
	
	public static void impostaComeElaborato(String codice){
		Log.info("Imposto l'articolo "+codice+" come elaborato");
		Connection con = null;
		PreparedStatement ps = null;

		try {			
			con = DataSource.getLocalConnection();
			ps = con.prepareStatement("UPDATE coda_inserzioni " +
														"SET elaborato= 1 " +
														"WHERE codice_articolo = ? ");
			ps.setString(1, codice);
			ps.executeUpdate();
			
			LogArticolo l = new LogArticolo();
			l.setCodiceArticolo(codice);
			l.setAzione("Elaborazione inserzioni");
			l.setNote("Fine elaborazione automatica delle inserzioni.");
			LogArticolo_DAO.inserisciLogArticolo(l, con, ps);
			
			con.commit();
		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,null);
		}
	}
}
