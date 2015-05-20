package it.swb.business;

import it.swb.database.Articolo_DAO;
import it.swb.database.GM_IT_DAO;
import it.swb.ftp.FTPmethods;
import it.swb.log.Log;
import it.swb.model.Articolo;
import it.swb.model.Filtro;
import it.swb.model.InfoEbay;
import it.swb.piattaforme.ebay.EbayController;
import it.swb.piattaforme.zelda.ZB_IT_DAO;
import it.swb.utility.Costanti;
import it.swb.utility.EditorDescrizioni;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticoloBusiness {
	
    private static ArticoloBusiness instance = new ArticoloBusiness();
    
    /* Costruttore privato della classe */
    private ArticoloBusiness() {}

    /* Metodo che permette di ottenere l'istanza della classe */
    public static ArticoloBusiness getInstance() {
        return instance;
    }
    
    private List<Articolo> articoli;  
    private Map<String, Articolo> mappaArticoli;
    private Map<String, Articolo> mappaArticoliPerOrdini;
    private Filtro filtro;
    
	public Filtro getFiltro() {
		if (filtro==null)
			filtro = new Filtro();
		return filtro;
	}
	
	public Filtro resetFiltro() {
		filtro=null;
		return getFiltro();
	}

	public List<Articolo> getArticoli(){
		if (articoli==null){
			//VarianteBusiness.getInstance().reloadMappaVarianti();
			//LogBusiness.getInstance().reloadMappaLogArticoli();
			articoli = Articolo_DAO.getArticoli();
		}
		return articoli;
	}
	
	public List<Articolo> reloadArticoli(){		
		articoli = null;
		return getArticoli();
	}
	
	public Map<String, Articolo> getMappaArticoli() {
		if (mappaArticoli==null){
			//VarianteBusiness.getInstance().reloadMappaVarianti();
			//LogBusiness.getInstance().reloadMappaLogArticoli();
			mappaArticoli = Articolo_DAO.getMappaArticoliCompleta();
		}
		return mappaArticoli;
	}
	
	public Map<String, Articolo> getMappaArticoliPerOrdini() {
		if (mappaArticoliPerOrdini==null){
			mappaArticoliPerOrdini = Articolo_DAO.getMappaArticoliPerOrdini();
		}
		return mappaArticoliPerOrdini;
	}
	
	public Map<String, Articolo> reloadMappaArticoli(){		
		mappaArticoli = null;
		return getMappaArticoli();
	}
	
	public Map<String, Articolo> reloadMappaArticoliPerOrdini(){		
		mappaArticoliPerOrdini = null;
		return getMappaArticoliPerOrdini();
	}
	
	public void reloadAll(){
		reloadArticoli();
		reloadMappaArticoli();
		reloadMappaArticoliPerOrdini();
		VarianteBusiness.getInstance().reloadMappaVarianti();
	}
	
	public int inserisciArticolo(Articolo art){
		int x = Articolo_DAO.inserisciArticolo(art);
		reloadAll();
		return x;
	}
	
	public int modificaArticolo(Articolo art,String s){
		int x = Articolo_DAO.modificaArticolo(art,s);
		reloadAll();
		return x;
	}
	
	public int modificaArticolo2(Articolo art){
		int x = Articolo_DAO.modificaArticolo2(art);
		reloadAll();
		return x;
	}
	
	public void eliminaArticolo(long idArticolo,String codiceArticolo){
		Articolo_DAO.eliminaArticolo(idArticolo,codiceArticolo);
		reloadAll();
	}
	
	public void setPresenze(Articolo a){
		Articolo_DAO.setPresenze(a);
		reloadAll();
	}
	
	
//	public void modificaArticoli(List<Articolo> arts){
//		Articolo_DAO.modificaArticoli(arts);
//		reloadAll();
//	}
	
	public void inserisciOModificaArticoli(List<Articolo> arts){
		Articolo_DAO.inserisciOModificaArticoli(arts);
		//reloadAll();
	}
	
	public int inserisciOModificaArticolo(Articolo a){
		int idArticolo = Articolo_DAO.checkIfArticoloExist2(a.getCodice());
		
		if (idArticolo==-1) idArticolo = Articolo_DAO.inserisciArticoloNew(a);
		else {
			a.setIdArticolo(idArticolo);
			int res = Articolo_DAO.modificaArticoloNew(a);
			if (res==-2) idArticolo = -2;
		}
		
		
		return idArticolo;
	}
	
	public void salvaCodiciBarreVarianti(Map<String, List<String>> m){
		Articolo_DAO.salvaCodiciBarreVarianti(m);
		reloadAll();
	}
	
	public void pubblicaInserzioni(List<Articolo> articoli, Map<String,Boolean> piattaforme){
		
		for (Articolo a : articoli){
			pubblicaInserzioni(a, piattaforme);
		}
	}

	
	public Map<String,Map<String,String>> pubblicaInserzioni(Articolo a, Map<String,Boolean> piattaforme){
		
		Map<String,Map<String,String>> risultati = new HashMap<String,Map<String,String>>();
		
		Log.info("Pubblicazione inserzioni articolo con codice: " + a.getCodice() + " e ID: " + a.getIdArticolo());
		
		if (piattaforme.get("zb")) {
			risultati.put("zb", creaInserzioneSuZB(a));
		}
		
		if (piattaforme.get("gm"))  {
			risultati.put("gm", creaInserzioneSuGM(a));
		}

		if (piattaforme.get("amazon"))  {
			risultati.put("amazon", creaInserzioneSuAmazon(a));
		}
		
		if (piattaforme.get("ebay")) {
			risultati.put("ebay", creaInserzioneSuEbay(a));
		}
		
		this.reloadArticoli();
		
		return risultati;
	}
	
	
	
	public int  salvaArticoloInCodaInserzioni(Articolo a){
//		if (this.articoli.contains(a)){
//			this.articoli.remove(a);
//		}
//		this.articoli.add(a);
		
		int res = Articolo_DAO.salvaArticoloInCodaInserzioni(a);
		
		this.reloadArticoli();
		
		return res;
	}
	
	
	public void elaboraCodaInserzioni(){
		
		List<Articolo> articoli = Articolo_DAO.getArticoliInCodaInserzioni();
		
		Log.info("Inizio elaborazione coda inserzioni...");
		
		for (Articolo a : articoli){
			Log.info("Pubblicazione inserzioni per l'articolo "+a.getCodice());
			
			Map<String,String> m;
			
			boolean thumbCreate = FTPmethods.creaThumbnails(a);	
			
			if (thumbCreate){
				if (a.getPresente_su_amazon()==-1){
//					m = creaInserzioneSuAmazon(a);
//					Log.info(m.get("pubblicato"));
				}
				
				if (a.getPresente_su_zb()==-1){
					m = creaInserzioneSuZB(a);
					Log.info(m.get("pubblicato"));
				}
				
				if (a.getPresente_su_gm()==-1){
					m = creaInserzioneSuGM(a);
					Log.info(m.get("pubblicato"));
				}
				
				if (a.getPresente_su_ebay()==-1){
					m = creaInserzioneSuEbay(a);
					Log.info(m.get("pubblicato"));
					if (m.get("errore")!=null) Log.info(m.get("errore"));
				}
				
				Articolo_DAO.impostaComeElaborato(a.getCodice());
				
				Log.info("Fine pubblicazione inserzioni per l'articolo "+a.getCodice());
			}
			else Log.info("Inserzioni non pubblicate per l'articolo "+a.getCodice()+" per un errore nella creazione delle thumbnail.");
		}
		
		this.reloadArticoli();
		
		Log.info("Fine elaborazione coda inserzioni.");
	}
	

	private Map<String,String> creaInserzioneSuEbay(Articolo a) {
		
		Map<String,String> risultato = new HashMap<String,String>();
		
		InfoEbay informazioniEbay = a.getInfoEbay();
	      if (informazioniEbay==null){
	    	  informazioniEbay = new InfoEbay();
	    	  informazioniEbay.setTitoloInserzione(a.getNome());
	    	  
	    	  a.setInfoEbay(informazioniEbay);
	      }

		a.getInfoEbay().setDescrizioneEbay(EditorDescrizioni.creaDescrizioneEbay(a));

		String[] z = EbayController.creaInserzione(a);
		
		if (z[0].equals("0")) {
			risultato.put("pubblicato", "Inserzione NON creata su eBay: si è verificato un errore");
			risultato.put("errore", z[1]);
			
		} else if (z[0].equals("1")) {
			risultato.put("pubblicato", "Inserzione creata correttamente su eBay");
			risultato.put("link", Costanti.linkArticoloEbayProduzione + z[1]);

			Articolo_DAO.setPresenzaSu(a.getCodice(), "ebay", 1, z[1]);
		}
		
		return risultato;
	}


	private Map<String,String> creaInserzioneSuAmazon(Articolo a) {
		
		Map<String,String> risultato = new HashMap<String,String>();

		//int res = McdBusiness.aggiungiAMcd(a.getCodice(),"amazon");
		int res  = Articolo_DAO.setPresenzaSu(a.getCodice(), "amazon", -1, null);
		
		if (res == 1) {
			risultato.put("pubblicato", "Articolo inserito correttamente nel modello caricamento dati di Amazon.");
//			Articolo_DAO.setPresenzaSu(a.getCodice(), "amazon", -1, null);
		} else
			risultato.put("pubblicato", "Articolo NON inserito nel modello caricamento dati di Amazon. Si è verificato qualche problema.");

		return risultato;
	}

	
	private Map<String,String> creaInserzioneSuZB(Articolo a) {
		
		Map<String,String> risultato = new HashMap<String,String>();

		if ( ZB_IT_DAO.insertIntoProduct(a)==1 ){
		
			risultato.put("pubblicato", "Inserzione creata correttamente su ZeldaBomboniere.it");
			risultato.put("link", Costanti.linkArticoloZeldaBomboniereFrontend + a.getIdArticolo());
			
			Articolo_DAO.setPresenzaSu(a.getCodice(), "zb", 1, null);
		} else
			risultato.put("pubblicato", "Inserzione NON creata su ZeldaBomboniere.it: Si è verificato qualche problema.");	
		
		return risultato;
	}
	
	private Map<String,String> creaInserzioneSuGM(Articolo a) {
		
		Map<String,String> risultato = new HashMap<String,String>();

		if ( GM_IT_DAO.insertIntoProduct(a)==1 ){
		
			risultato.put("pubblicato", "Inserzione creata correttamente su GloriaMoraldi.it");
			risultato.put("link", Costanti.linkArticoloGloriamoraldiFrontend + a.getIdArticolo());
			
			Articolo_DAO.setPresenzaSu(a.getCodice(), "gm", 1, null);
		} else
			risultato.put("pubblicato", "Inserzione NON creata su GloriaMoraldi.it: Si è verificato qualche problema.");		
		
		return risultato;
	}

}
