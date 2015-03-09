package it.swb.bean;

import it.swb.business.ArticoloBusiness;
import it.swb.business.CategorieBusiness;
import it.swb.business.VarianteBusiness;
import it.swb.database.Articolo_DAO;
import it.swb.database.GM_IT_DAO;
import it.swb.database.Variante_Articolo_DAO;
import it.swb.database.ZB_IT_DAO;
import it.swb.dbf.DbfUtil;
import it.swb.ebay.EbayController;
import it.swb.ebay.EbayStuff;
import it.swb.ftp.FTPmethods;
import it.swb.ftp.FTPutil;
import it.swb.java.EditorModelliAmazon;
import it.swb.log.Log;
import it.swb.model.Articolo;
import it.swb.model.Categoria;
import it.swb.model.Filtro;
import it.swb.model.InfoAmazon;
import it.swb.model.InfoEbay;
import it.swb.model.Variante_Articolo;
import it.swb.utility.Costanti;
import it.swb.utility.DateMethods;
import it.swb.utility.EditorDescrizioni;
import it.swb.utility.Methods;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.net.ftp.FTPClient;
import org.primefaces.event.SelectEvent;

@ManagedBean(name = "articoloBean")
@ViewScoped
public class ArticoloBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    private List<Articolo> articoli;  
    
    private List<Articolo> articoliFiltrati;  
    
    private Articolo articoloSelezionato;
    
    private Articolo[] articoliSelezionati;
    
    private int idVarianteSelezionata;
    
    private static ArticoloBean instance = new ArticoloBean();
    
    /* Costruttore privato della classe */
    public ArticoloBean() {}

    /* Metodo che permette di ottenere l'istanza della classe */
    public static ArticoloBean getInstance() {
        return instance;
    }
    
	private Filtro filtro;
        
	private String linkImmagini = Costanti.percorsoImmaginiRemoto;
    
    private String idEbay;  
    private String idEbay2;
    private String idEbayChiudiInserzione;
    private String idEbayXVarianti;
    private Articolo articoloEbay;
    private String titoloInserzione;
    
    private List<Variante_Articolo> varianti;
    private Variante_Articolo varianteSelezionata;
    
    //eliminazione articolo
    private boolean eliminaInserzioni;
    
    //sincronizzazione con G1 zucchetti
    private boolean syncSoloNuovi;
    private Date syncData = DateMethods.oraDelleStreghe(DateMethods.sottraiGiorniAData(new Date(), 30));
    private boolean syncNome;
    private boolean syncDimensioni;
    private boolean syncIva;
    private boolean syncCategoria;
    private boolean syncPrezzoDettaglio;
    private boolean syncPrezzoIngrosso;
    private boolean syncCostoAcquisto;
    private boolean syncCodiceFornitore;
    private boolean syncCodiceArticoloFornitore;
    private boolean syncCodiceBarre;
    private boolean syncTipoCodiceBarre;
    private Integer progress;  
    
    //download informazioni articolo da eBay
    private boolean ebayTitoloInserzione = true;
    private boolean ebayCategorie = true;
    private boolean ebayPrezzo;
    private boolean ebayDescrizione;
    private boolean ebayVarianti = true;
    private boolean ebayQuantita = false;
    
    
    public void onRowSelect(SelectEvent event) {  
        FacesMessage msg = new FacesMessage("Articolo Selezionato", null);  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }  
    
    public void onItemDeleted(ActionEvent event) {  
        FacesMessage msg = new FacesMessage("Articolo Eliminato", null);  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }  
    
    public void onItemUpdated(ActionEvent event) {  
        FacesMessage msg = new FacesMessage("Articolo Modificato", null);  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }
    
    public void onTableRefreshed(ActionEvent event) {  
        FacesMessage msg = new FacesMessage("Tabella Aggiornata", null);  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }
    
    public void onSyncFinished(ActionEvent event) {  
        FacesMessage msg = new FacesMessage("Sincronizzazione Terminata", null);  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }
    
	public void showMessage(String titolo, String messaggio) {
		FacesMessage message = new FacesMessage(titolo,messaggio);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
    
    public void caricaArticolo(SelectEvent event){
    	Log.debug("Carico i dati dell'articolo "+articoloSelezionato.getCodice());
    	articoloSelezionato = Articolo_DAO.getArticoloByCodice(articoloSelezionato.getCodice(), null);
    }
    
    public void mettiInCodaInserzioni(){
    	ArticoloBusiness.getInstance().salvaArticoloInCodaInserzioni(articoloSelezionato);
    	ArticoloBusiness.getInstance().setPresenze(articoloSelezionato);
    	
    	showMessage("Operazione completata", "Le inserzioni per questo articolo verranno pubblicate automaticamente.");
    }
    
    public void inviaAllaCodaDiStampa(){
    	Methods.aggiungiAllaCodaDiStampa(articoloSelezionato.getCodice());
    }
    
    public void printArt(){
    	System.out.println(articoloSelezionato.getCodice());
    }
    
    public void filtra(){
    	articoli = ArticoloBusiness.getInstance().reloadArticoli();
    }
    
    public void reset(){
    	filtro = ArticoloBusiness.getInstance().resetFiltro(); 	
    	articoli = ArticoloBusiness.getInstance().reloadArticoli();
    }
    
    public void prodottoFinito(){
    	
    	eliminaDaZb();
    	eliminaDaGm();
    	
    	idEbayChiudiInserzione = articoloSelezionato.getIdEbay();
    	
    	if (idEbayChiudiInserzione!=null && !idEbayChiudiInserzione.isEmpty()){
    		Log.info("Chisura inserzione su eBay con ID: "+idEbayChiudiInserzione);
    		String ebayChiuso = EbayController.chiudiInserzione(idEbayChiudiInserzione);
    		if (ebayChiuso.equals("ok")) {
    			Articolo_DAO.setPresenzaSu(articoloSelezionato.getCodice(), "ebay", 0, null);
    		}
    	}
    	
    	Articolo_DAO.setPresenzaSu(articoloSelezionato.getCodice(), "amazon", 0, null);
    	
    	Articolo_DAO.modificaQuantitaArticolo(articoloSelezionato.getCodice(), 0);
    	//modifica q	uantita varianti
    	
    	FacesMessage msg = new FacesMessage("Articolo finito", "Articolo eliminato dalle piattaforme");      
    	FacesContext.getCurrentInstance().addMessage(null, msg);  
    }
    
    private Map<String,Boolean> getCosaScaricareDaEbay(boolean minimal){
    	Map<String,Boolean> cosaScaricare = new HashMap<String,Boolean>();
    	cosaScaricare.put("titolo", ebayTitoloInserzione);
    	cosaScaricare.put("categorie", ebayCategorie);
    	if (minimal){
    		cosaScaricare.put("prezzo", false);
    	    cosaScaricare.put("descrizione", false);
    	    cosaScaricare.put("quantita", false);
    	    cosaScaricare.put("varianti", false);
    	} else {
    		cosaScaricare.put("prezzo", ebayPrezzo);
    	    cosaScaricare.put("descrizione", ebayDescrizione);
    	    cosaScaricare.put("quantita", ebayQuantita);
    	    cosaScaricare.put("varianti", ebayVarianti);
    	}
    	return cosaScaricare;
    }
    
    public void chiudiInserzioneEbayStep1(){
    	idEbayChiudiInserzione = articoloSelezionato.getIdEbay();
    }
    
    public void chiudiInserzioneEbayStep2(){
    	FacesMessage msg = null;
    	if (idEbayChiudiInserzione!=null && !idEbayChiudiInserzione.isEmpty()){
    		Log.info("Chisura inserzione su eBay con ID: "+idEbayChiudiInserzione);
    		String chiudi = EbayController.chiudiInserzione(idEbayChiudiInserzione);
    		if (chiudi.equals("ok")) {
    			msg = new FacesMessage("Chiudi inserzione su eBay", "Inserzione chiusa correttamente");    
    			Log.info("Inserzione chiusa correttamente");
    			Articolo_DAO.setPresenzaSu(articoloSelezionato.getCodice(), "ebay", 0, null);
    			Articolo_DAO.setPresenzaSu(articoloSelezionato.getCodice(), "amazon", 0, null);
    		}
    		else {
    			msg = new FacesMessage("Chiudi inserzione su eBay", chiudi);      
    			Log.error(chiudi);
    		}
    	} else {
    		msg = new FacesMessage("Inserzione eBay non chiusa", "ID non valido");  
    		Log.info("Inserzione eBay non chiusa, ID non valido");
    	}
    	FacesContext.getCurrentInstance().addMessage(null, msg);  
    	
    	idEbayChiudiInserzione = "";
    }
    
    
	@SuppressWarnings("unchecked")
	public void scaricaDaEbay(){
    	Log.debug("Provo a ottenere da eBay le informazioni per l'articolo: "+articoloSelezionato.getCodice()+", ID inserzione: "+idEbay);
    	
    	articoloEbay = new Articolo();
    	articoloEbay.setCodice(articoloSelezionato.getCodice());
    	
    	Object[] oggetti = EbayController.ottieniInformazioniDaID(idEbay,getCosaScaricareDaEbay(false));
    	
    	InfoEbay ie = (InfoEbay)oggetti[0];
    	
    	articoloEbay.setInfoEbay(ie);
    	articoloEbay.setIdEbay(idEbay);
    	   	    	
    	if (ebayDescrizione)
    		articoloEbay = EbayStuff.elaboraDescrizione(articoloEbay);   
    	
    	if (ebayVarianti && oggetti[1]!=null)
    		articoloEbay.setVarianti((List<Variante_Articolo>)oggetti[1]);
    	
    	if (ebayQuantita && oggetti[2]!=null)
    		articoloEbay.setQuantitaMagazzino((Integer) oggetti[2]);
    	
    	if (ebayPrezzo)
    		articoloEbay.setPrezzoPiattaforme(ie.getPrezzo());
    	
    	//EbayStuff.printArticolo(articoloEbay);
    	
    	FacesContext context = FacesContext.getCurrentInstance();
    	context.getExternalContext().getSessionMap().put("articolo_ebay", articoloEbay);
    	
    	idEbay="";
    }
	
    public void salvaInfoEbay(){
    	//TODO RIVEDERE 
    	    	
    	FacesContext context = FacesContext.getCurrentInstance();
    	articoloEbay = (Articolo) context.getExternalContext().getSessionMap().get("articolo_ebay");
    	
    	articoloSelezionato.setInfoEbay(articoloEbay.getInfoEbay());
    	articoloSelezionato.setIdEbay(articoloEbay.getIdEbay());
    	
    	if (ebayDescrizione){
	    	articoloSelezionato.setQuantitaInserzione(articoloEbay.getQuantitaInserzione());
	    	articoloSelezionato.setDimensioni(articoloEbay.getDimensioni());
	    	articoloSelezionato.setDescrizione(articoloEbay.getDescrizione());
	    	articoloSelezionato.setImmagine1(articoloEbay.getImmagine1());
	    	articoloSelezionato.setImmagine2(articoloEbay.getImmagine2());
	    	articoloSelezionato.setImmagine3(articoloEbay.getImmagine3());
	    	articoloSelezionato.setImmagine4(articoloEbay.getImmagine4());
	    	articoloSelezionato.setImmagine5(articoloEbay.getImmagine5());
	    	
	    	ricreaImmagini();
    	}
    	if (ebayQuantita)
    		articoloSelezionato.setQuantitaMagazzino(articoloEbay.getQuantitaMagazzino());
    	
    	if (ebayVarianti)
    		articoloSelezionato.setVarianti(articoloEbay.getVarianti());
    	
    	if (ebayPrezzo)
    		articoloSelezionato.setPrezzoPiattaforme(articoloEbay.getPrezzoPiattaforme());
    	
    	ArticoloBusiness.getInstance().modificaArticolo(articoloSelezionato,"ebay");
    	Articolo_DAO.setPresenzaSu(articoloSelezionato.getCodice(), "ebay", 1, null);
    	Articolo_DAO.modificaInformazioniEbay(articoloSelezionato.getCodice(),articoloSelezionato.getIdEbay(), articoloSelezionato.getInfoEbay(), null);
    	
    	context.getExternalContext().getSessionMap().remove("articolo_ebay");
    }
	
	
	public void scaricaVariantiDaEbay(){
		Log.debug("Provo a ottenere da eBay le varianti per l'articolo: "+articoloSelezionato.getCodice()+", ID inserzione: "+idEbayXVarianti);
    	
    	articoloEbay = new Articolo();
    	articoloEbay.setCodice(articoloSelezionato.getCodice());
    	
    	List<Variante_Articolo> varianti = EbayController.ottieniVariantiDaID(idEbayXVarianti);
    	
    	
   		articoloEbay.setVarianti(varianti);
    	
//    	EbayStuff.printArticolo(articoloEbay);
    	
    	FacesContext context = FacesContext.getCurrentInstance();
    	context.getExternalContext().getSessionMap().put("varianti_ebay", articoloEbay);
    	
    	idEbayXVarianti="";
	}
    
    public void salvaVariantiEbay(){
    	//TODO FINIRE 
    	    	
    	FacesContext context = FacesContext.getCurrentInstance();
    	articoloEbay = (Articolo) context.getExternalContext().getSessionMap().get("varianti_ebay");
    	
    	Variante_Articolo_DAO.inserisciOModificaVarianti(articoloEbay.getVarianti(), articoloEbay.getCodice(), null, null);
    	
    	context.getExternalContext().getSessionMap().remove("articolo_ebay");
    }
    
    public void aggiornaTemplateEbay(){
    	
    	Log.info("Aggiornamento template inserzione eBay per articolo con con codice: "+articoloSelezionato.getCodice()+", ID inserzione: "+idEbay2);
    	
    	
    	if (idEbay2!=null && !idEbay2.trim().isEmpty()){
    	  	if (articoloSelezionato.getInfoEbay()!=null){
        		        		
    	  		articoloSelezionato.getInfoEbay().setDescrizioneEbay(EditorDescrizioni.creaDescrizioneEbay(articoloSelezionato));   		
        		
        		if(articoloSelezionato.getInfoEbay().getIdCategoria1()==null || articoloSelezionato.getInfoEbay().getIdCategoria1().trim().isEmpty()){
        			Object[] oggetti = EbayController.ottieniInformazioniDaID(idEbay2, getCosaScaricareDaEbay(false));
        			InfoEbay ie2 = (InfoEbay)oggetti[0];
        			
        			if (ie2.getIdCategoria1()!=null && !ie2.getIdCategoria1().trim().isEmpty())
        				articoloSelezionato.getInfoEbay().setIdCategoria1(ie2.getIdCategoria1());
        			
        			if (ie2.getIdCategoria2()!=null && !ie2.getIdCategoria2().trim().isEmpty())
        				articoloSelezionato.getInfoEbay().setIdCategoria2(ie2.getIdCategoria2());
        		}
        		Log.debug("Categorie: "+articoloSelezionato.getInfoEbay().getIdCategoria1()
	  					+","+articoloSelezionato.getInfoEbay().getIdCategoria2()
	  					+". Nome inserzione: "+articoloSelezionato.getInfoEbay().getTitoloInserzione());
    	  		
        	   	EbayController.modificaInserzione(articoloSelezionato, idEbay2);
        	   	Log.debug("OK");
        	}
    	  	else {
    	  		Object[] oggetti = EbayController.ottieniInformazioniDaID(idEbay2,getCosaScaricareDaEbay(false));
    	  		
    	  		articoloSelezionato.setInfoEbay((InfoEbay)oggetti[0]);
    	  		
    	  		Log.debug("Categorie: "+articoloSelezionato.getInfoEbay().getIdCategoria1()
    	  					+","+articoloSelezionato.getInfoEbay().getIdCategoria2()
    	  					+". Nome inserzione: "+articoloSelezionato.getInfoEbay().getTitoloInserzione());
    	  		
    	  		EbayController.modificaInserzione(articoloSelezionato, idEbay2);
    	  		Log.debug("OK");
    	  	}
    	}
    	
    	else Log.error("Aggiornamento fallito: manca L'ID eBay.");
    	
    }
    
    public String getLinkEbay2(){
    	return Costanti.linkEbayProduzione+idEbay2;
    }
    
    public void setEbay2Null(){
    	idEbay2 = "";
    }
    
    
	public void aggiungiAdAmazon(){
		
		
		Map<Long, Categoria> categorie = CategorieBusiness.getInstance().getMappaCategorie();
		Categoria c = (Categoria) categorie.get(articoloSelezionato.getIdCategoria());
		long id_categoria_amazon = c.getIdCategoriaAmazon();
		
		InfoAmazon ia = new InfoAmazon();
		ia.setPuntoElenco1(articoloSelezionato.getQuantitaInserzione());
		ia.setPuntoElenco2(articoloSelezionato.getDimensioni());
		ia.setIdCategoria1(id_categoria_amazon);
		
		articoloSelezionato.setInfoAmazon(ia);
		
		int res = EditorModelliAmazon.aggiungiProdottoAModelloAmazon(articoloSelezionato);
			
		if (res==1) {
			Articolo_DAO.setPresenzaSu(articoloSelezionato.getCodice(), "amazon", -1, null);
			articoloSelezionato.setPresente_su_amazon(-1);
			FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Operazione completata", articoloSelezionato.getCodice()+" aggiunto al modello caricamento articoli di Amazon"));
		}
		else FacesContext.getCurrentInstance().
			addMessage(null, new FacesMessage("Operazione non completata", "Si è verificato qualche errore."));
		
	}
	
	
	public void aggiungiEsistenteAdAmazon(){
		
		int res = EditorModelliAmazon.aggiungiProdottoEsistenteAModelloAmazon(articoloSelezionato);
		
		articoloSelezionato.setPresente_su_amazon(-1);
		
		if (res==1) {
			Articolo_DAO.setPresenzaSu(articoloSelezionato.getCodice(), "amazon", -1, null);
			FacesContext.getCurrentInstance().
			addMessage(null, new FacesMessage("Operazione completata", articoloSelezionato.getCodice()+" aggiunto al modello caricamento articoli esistenti di Amazon"));
		} else FacesContext.getCurrentInstance().
		addMessage(null, new FacesMessage("Operazione non completata", "Si è verificato qualche errore."));
		
	}
	
	public void aggiungiProdottoSenzaSpedizione(){
		
		int res = EditorModelliAmazon.spedizioneGratis(articoloSelezionato);
		
		if (res==1) {
			FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Operazione completata", articoloSelezionato.getCodice()+" aggiunto al modello spedizione gratis di Amazon"));
		} else FacesContext.getCurrentInstance().
		addMessage(null, new FacesMessage("Operazione non completata", "Si è verificato qualche errore."));
		
		//if (res==1) Articolo_DAO.setPresenteSu(articoloSelezionato.getCodice(), "amazon");
	}
	
    
	
	public void aggiungiAZb(){
		
		if ( ZB_IT_DAO.insertIntoProduct(articoloSelezionato)==1 ) {
			Articolo_DAO.setPresenzaSu(articoloSelezionato.getCodice(), "zb", 1, null);
			articoloSelezionato.setPresente_su_zb(1);
			FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Operazione completata", articoloSelezionato.getCodice()+" inserito su ZeldaBomboniere.it")); 
		} else FacesContext.getCurrentInstance().
		addMessage(null, new FacesMessage("Operazione non completata", "Si è verificato qualche errore."));
	}
    
	public void eliminaDaZb(){
		
		if ( ZB_IT_DAO.deleteProduct(articoloSelezionato)==1 ) {
			Articolo_DAO.setPresenzaSu(articoloSelezionato.getCodice(), "zb", 0, null);
			articoloSelezionato.setPresente_su_zb(0);
			FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Operazione completata", articoloSelezionato.getCodice()+" eliminato da ZeldaBomboniere.it"));  
		} else FacesContext.getCurrentInstance().
		addMessage(null, new FacesMessage("Operazione non completata", "Si è verificato qualche errore."));
	}
	
	public void eliminaDaAmazon(){
		
		articoloSelezionato.setPresente_su_amazon(-2);
	}
	
	public void aggiungiAGm(){
		
		if (GM_IT_DAO.insertIntoProduct(articoloSelezionato)==1){
			Articolo_DAO.setPresenzaSu(articoloSelezionato.getCodice(), "gm", 1, null);
			articoloSelezionato.setPresente_su_gm(1);
			FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Operazione completata", articoloSelezionato.getCodice()+" inserito su GloriaMoraldi.it")); 
		} else FacesContext.getCurrentInstance().
		addMessage(null, new FacesMessage("Operazione non completata", "Si è verificato qualche errore."));
	}
	
	public void eliminaDaGm(){
		
		if (GM_IT_DAO.deleteProduct(articoloSelezionato)==1){
		
			Articolo_DAO.setPresenzaSu(articoloSelezionato.getCodice(), "gm", 0, null);
			articoloSelezionato.setPresente_su_gm(0);
			FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Operazione completata", articoloSelezionato.getCodice()+" eliminato da GloriaMoraldi.it"));  
	} else FacesContext.getCurrentInstance().
		addMessage(null, new FacesMessage("Operazione non completata", "Si è verificato qualche errore."));
	}
	
    
    public void salvaSpunte(){
    	ArticoloBusiness.getInstance().setPresenze(articoloSelezionato);
    	
    	showMessage("Operazione Completata","Le presenze sulle piattaforme sono state impostate.");  
    }
    
    public void salvaModifica(){
    	Log.debug("Salvataggio modifiche per articolo: "+articoloSelezionato.getCodice());
    	
		ArticoloBusiness.getInstance().modificaArticolo2(articoloSelezionato);
	
    }
    
    public void mostraVarianteSelezionata(ActionEvent ev){
    	System.out.println(ev.toString());
    	System.out.println("Mod ID VAR: "+idVarianteSelezionata);
 //   	varianteSelezionata.setIdVariante(idVarianteSelezionata);
    }
    
    public void mostraVarianteSelezionata(){
    	System.out.println("Modifica Variante con ID: "+idVarianteSelezionata);
 //   	varianteSelezionata.setIdVariante(idVarianteSelezionata);
    }
    
    public void modificaVariante(){
    	System.out.println("salvo");
    	System.out.println(varianteSelezionata.getIdVariante());
    	System.out.println(varianteSelezionata.getTipo());
    	System.out.println(varianteSelezionata.getValore());
    	System.out.println("barcode: "+varianteSelezionata.getCodiceBarre());
    	Variante_Articolo_DAO.modificaVariante(varianteSelezionata, articoloSelezionato.getCodice(), null,null);
    	
    	varianteSelezionata = null;
    	idVarianteSelezionata = 0;
    }
    
    public void eliminaArticolo(){
    	if (eliminaInserzioni){
    		eliminaDaAmazon();
    		eliminaDaGm();
    	}
    	
    	if (articoloSelezionato.getVarianti()!=null && !articoloSelezionato.getVarianti().isEmpty())
    		Variante_Articolo_DAO.eliminaVarianti(articoloSelezionato.getCodice());
    	ArticoloBusiness.getInstance().eliminaArticolo(articoloSelezionato.getIdArticolo(),articoloSelezionato.getCodice());
    	
    	articoli = ArticoloBusiness.getInstance().getArticoli();
    	
    	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Operazione completata", articoloSelezionato.getCodice()+" è stato eliminato."));
    }
    
    public void eliminaVariante(){
//    	Log.debug("eliminaVariante: "+idVarianteSelezionata);
//		if (articoloSelezionato.getVarianti()!=null || !articoloSelezionato.getVarianti().isEmpty()){
//			List<Variante_Articolo> variantiTemp = new ArrayList<Variante_Articolo>();
//			
//			for (Variante_Articolo v : articoloSelezionato.getVarianti()){
//				if (!v.getValore().equals(idVarianteSelezionata))
//					variantiTemp.add(v);
//			}
//			articoloSelezionato.setVarianti(variantiTemp);
//		}
    	
    	Variante_Articolo_DAO.eliminaVariante(idVarianteSelezionata);
    	articoloSelezionato.setVarianti(VarianteBusiness.getInstance().reloadMappaVarianti().get(articoloSelezionato.getCodice()));
    	
    	idVarianteSelezionata = 0;
    	
    	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Operazione completata", "La variante "+idVarianteSelezionata+" è stata eliminata."));
    }
    
    public void ricreaImmagini(){
    	Log.info("Scarica e crea Thumbnails new: "+articoloSelezionato.getCodice());  
    	FTPClient f = FTPutil.getConnection();
    	boolean ok = true;
    	
    	if (Methods.controlloSintassiImmagine(articoloSelezionato.getImmagine1())){
			if (!FTPmethods.creaThumbnailsEcaricaSuFtp(articoloSelezionato.getImmagine1(),true,f))
				ok = false;
		}
		if (Methods.controlloSintassiImmagine(articoloSelezionato.getImmagine2())){
			if (!FTPmethods.creaThumbnailsEcaricaSuFtp(articoloSelezionato.getImmagine2(),false,f))
				ok = false;
		}
		if (Methods.controlloSintassiImmagine(articoloSelezionato.getImmagine3())){
			if (!FTPmethods.creaThumbnailsEcaricaSuFtp(articoloSelezionato.getImmagine3(),false,f))
				ok = false;
		}
		if (Methods.controlloSintassiImmagine(articoloSelezionato.getImmagine4())){
			if (!FTPmethods.creaThumbnailsEcaricaSuFtp(articoloSelezionato.getImmagine4(),false,f))
				ok = false;
		}
		if (Methods.controlloSintassiImmagine(articoloSelezionato.getImmagine5())){
			if (!FTPmethods.creaThumbnailsEcaricaSuFtp(articoloSelezionato.getImmagine5(),false,f))
				ok = false;
		}
		
		if (articoloSelezionato.getVarianti()!=null && !articoloSelezionato.getVarianti().isEmpty()){
			for (Variante_Articolo v : articoloSelezionato.getVarianti()){
				if (Methods.controlloSintassiImmagine(v.getImmagine()))
				{
					if (!FTPmethods.creaThumbnailsEcaricaSuFtp(v.getImmagine(),false,f))
						ok=false;
				}
			}
		}
		FTPutil.closeConnection(f);
		
		if (ok) FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Operazione completata", "Le thumbnails sono state ricreate."));
		if (!ok) FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Operazione non completata", "Alcune immagini non sono state caricate. Controllare i log."));
    }
    
    
    public String getLinkEbay(){ 
    	String link;
    	if (articoloSelezionato.getIdEbay()!=null && !articoloSelezionato.getIdEbay().isEmpty())
    		link = Costanti.linkEbayProduzione+articoloSelezionato.getIdEbay();
    	else {
	    	String titolo = articoloSelezionato.getTitoloInserzione();   		
	    	if (titolo==null || titolo.isEmpty())
	    		titolo = articoloSelezionato.getNome().replace(" ", "+");
	    		link = Costanti.ricercaSuEbay+titolo;
	    	}
    	return link;
    }
    
    public String getLinkYatego1(){
    	if (articoloSelezionato.getTitoloInserzione()!=null && !articoloSelezionato.getTitoloInserzione().isEmpty())
    		return Costanti.ricercaSuYatego+articoloSelezionato.getTitoloInserzione();
    	else return Costanti.ricercaSuYatego+articoloSelezionato.getNome();
    }
    
    public String getLinkYatego2(){
    	return Costanti.ricercaSuYatego+articoloSelezionato.getNome();
    }
    
    public String getLinkGmFrontend(){ 	
    	return Costanti.ricercaSuGmFrontend+articoloSelezionato.getIdArticolo();
    }
    
    public String getLinkGmBackend(){ 	
    	return Costanti.ricercaSuGmBackend+articoloSelezionato.getCodice();
    }
    
    public String getLinkZbFrontend(){ 	
    	return Costanti.ricercaSuZbFrontend+articoloSelezionato.getIdArticolo();
    }
    
    public String getLinkZbBackend(){ 	
    	return Costanti.ricercaSuZbBackend+articoloSelezionato.getCodice();
    }
    
    public String getLinkAmazonFrontend(){ 	
    	return Costanti.ricercaSuAmazonFrontend+(articoloSelezionato.getNome().replace(" ", "+"));
    }
    
    public String getLinkAmazonBackend(){ 	
    	return Costanti.ricercaSuAmazonBackend+(articoloSelezionato.getCodice());
    }
    
    
//    public String creaInserzioni(){
//    	logger.info("Crea Inserzioni: "+articoloSelezionato.getCodice());
//    	AddItemBean.getInstance().caricaDatiArticolo(articoloSelezionato);
//    	return "additem.xhtml";
//    }
    
    
    public void sincronizzaDati(){
    	try{
    		
	    	Log.info("Sincronizzazione dati articoli con gestionale G1");
	    	
	    	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Inizio lettura file...", "Inizio lettura file..."));  
	    	
	    	Properties config = new Properties();	   
	    	config.load(Log.class.getResourceAsStream("/zeus.properties"));			
			String file_articoli = config.getProperty("file_articoli_remoto");
			
			Map<String,Boolean> whatToSync = new HashMap<String,Boolean>();
			
			whatToSync.put("soloNuovi", syncSoloNuovi);
			whatToSync.put("nome", syncNome);
			whatToSync.put("dimensioni", syncDimensioni);
			whatToSync.put("iva", syncIva);
			whatToSync.put("prezzoDettaglio", syncPrezzoDettaglio);
			whatToSync.put("prezzoIngrosso", syncPrezzoIngrosso);
			whatToSync.put("costoAcquisto)", syncCostoAcquisto);
			whatToSync.put("categoria", syncCategoria);
			whatToSync.put("codiceFornitore", syncCodiceFornitore);
			whatToSync.put("codiceArticoloFornitore", syncCodiceArticoloFornitore);
			whatToSync.put("codiceBarre", syncCodiceBarre);
			whatToSync.put("tipoCodiceBarre", syncTipoCodiceBarre);
	    	
	    	List<Articolo> arts = DbfUtil.syncArticoli(file_articoli, whatToSync, syncData); 
	    	
	    	ArticoloBusiness.getInstance().inserisciOModificaArticoli(arts);
	    	
	    	String file_varianti = config.getProperty("file_varianti_remoto");
	    	
	    	Map<String,List<String>> m = DbfUtil.syncCodiciBarreVarianti(file_varianti);		
	    	ArticoloBusiness.getInstance().salvaCodiciBarreVarianti(m);
	    	
	    	articoli = ArticoloBusiness.getInstance().getArticoli(); //il reload è stato già fatto
	    	
    	} catch(Exception e){
    		Log.error(e.getMessage());
    		e.printStackTrace();
    	}
    }
    
    public void sincronizzaGiacenze(){
    	try{ 		
    		Log.info("Sincronizzazione giacenze con gestionale G1");
    
		    List<Articolo> l = DbfUtil.getGiacenze("Z:\\DB\\001\\GIACEN.DBF"); /*	D:\\BACKUP\\GIACEN.DBF	*/
			Articolo_DAO.modificaQuantitaArticoli(l);
			articoli = ArticoloBusiness.getInstance().reloadArticoli();
		
    	} catch(Exception e){
    		Log.error(e.getMessage());
    		e.printStackTrace();
    	}
    }
    
    
    public void sincronizzaGiacenzaArticolo(){
		Log.info("Sincronizzazione Giacenza Articolo "+articoloSelezionato.getCodice()+" da gestionale G1");
	    int giacenza = DbfUtil.getGiacenzaArticolo("Z:\\DB\\001\\GIACEN.DBF",articoloSelezionato.getCodice()); /*	D:\\BACKUP\\GIACEN.DBF	*/
	    Articolo_DAO.modificaQuantitaArticolo(articoloSelezionato.getCodice(),giacenza);
	    articoloSelezionato.setQuantitaMagazzino(giacenza);
	    articoli = ArticoloBusiness.getInstance().reloadArticoli();
    }
    
    
    
    public void sincronizzaArticolo(){
    	try{
	    	Log.info("Sincronizzazione Articolo "+articoloSelezionato.getCodice()+" da gestionale G1");
	    	Articolo art = DbfUtil.getArticoloFromDbf("Z:\\DB\\001\\ARTICOLI.DBF",articoloSelezionato.getCodice()); /* D:\\BACKUP\\ARTICOLI.DBF */
	    	
	    	if (art!=null){
	    		String note = "";
	    		
	    		//articoloSelezionato.setNome(art.getNome());
	    		if (!articoloSelezionato.getCodiceBarre().equals(art.getCodiceBarre())){
					articoloSelezionato.setCodiceBarre(art.getCodiceBarre());	
	    			note+="Codice barre da \""+articoloSelezionato.getCodiceBarre()+"\" a \""+art.getCodiceBarre()+"\". ";
	    		}
	    		if (articoloSelezionato.getPrezzoIngrosso()!=art.getPrezzoIngrosso()){
	    			articoloSelezionato.setPrezzoIngrosso(art.getPrezzoIngrosso());
	    			note+="Prezzo ingrosso da \""+articoloSelezionato.getPrezzoIngrosso()+"\" a "+art.getPrezzoIngrosso()+"\". ";
	    		}
	    		if (articoloSelezionato.getPrezzoDettaglio()!=art.getPrezzoDettaglio()){
	    			articoloSelezionato.setPrezzoDettaglio(art.getPrezzoDettaglio());
	    			note+="Prezzo dettaglio da \""+articoloSelezionato.getPrezzoDettaglio()+"\" a "+art.getPrezzoDettaglio()+"\". ";
	    		}
	    		if (articoloSelezionato.getCostoAcquisto()!=art.getCostoAcquisto()){
	    			articoloSelezionato.setCostoAcquisto(art.getCostoAcquisto());
	    			note+="Costo acquisto da \""+articoloSelezionato.getCostoAcquisto()+"\" a "+art.getCostoAcquisto()+"\". ";
	    		}
	    		if (articoloSelezionato.getAliquotaIva()!=art.getAliquotaIva()){
	    			articoloSelezionato.setAliquotaIva(art.getAliquotaIva());
	    			note+="IVA da \""+articoloSelezionato.getAliquotaIva()+"\" a \""+art.getAliquotaIva()+"\". ";
	    		}
	    		if (articoloSelezionato.getIdCategoriaGestionale()!=art.getIdCategoriaGestionale()){
	    			articoloSelezionato.setIdCategoriaGestionale(art.getIdCategoriaGestionale());
	    			note+="Categoria gestionale da \""+articoloSelezionato.getIdCategoriaGestionale()+"\" a \""+art.getIdCategoriaGestionale()+"\". ";
	    		}
	    		if (articoloSelezionato.getIdCategoria()!=art.getIdCategoria()){
	    			articoloSelezionato.setIdCategoria(art.getIdCategoria());
	    			note+="Categoria da \""+articoloSelezionato.getIdCategoria()+"\" a \""+art.getIdCategoria()+"\". ";
	    		}
	    		if (!articoloSelezionato.getCodiceFornitore().equals(art.getCodiceFornitore())){
	    			articoloSelezionato.setCodiceFornitore(art.getCodiceFornitore());
	    			note+="Codice fornitore da \""+articoloSelezionato.getCodiceFornitore()+"\" a \""+art.getCodiceFornitore()+"\". ";
	    		}
	    		if (!articoloSelezionato.getCodiceArticoloFornitore().equals(art.getCodiceArticoloFornitore())){
	    			articoloSelezionato.setCodiceArticoloFornitore(art.getCodiceArticoloFornitore());
	    			note+="Codice articolo fornitore da \""+articoloSelezionato.getCodiceArticoloFornitore()+"\" a \""+art.getCodiceArticoloFornitore()+"\". ";
	    		}
				if (!articoloSelezionato.getTipoCodiceBarre().equals(art.getTipoCodiceBarre())){
					articoloSelezionato.setTipoCodiceBarre(art.getTipoCodiceBarre());
					note+="Tipo codice a barre da \""+articoloSelezionato.getTipoCodiceBarre()+"\" a \""+art.getTipoCodiceBarre()+"\". ";
				}
				
				articoloSelezionato.setNote(note);
	    		
		    	ArticoloBusiness.getInstance().modificaArticolo(articoloSelezionato,"sync");
		    	articoloSelezionato.setNote("");
		    	articoli = ArticoloBusiness.getInstance().reloadArticoli();
	    	}
	    	
	    	
    	} catch(Exception e){
    		Log.error(e.getMessage());
    		e.printStackTrace();
    	}
    }
      
      
    public void syncComplete() {  
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sincronizzazione Completata", "Sincronizzazione Completata"));  
    }  

    public void aggiornaTabella(){
    	Log.info("Aggiornamento tabella articoli");
    	articoli = ArticoloBusiness.getInstance().reloadArticoli();
    }

	public Articolo getArticoloSelezionato() {
		if (articoloSelezionato==null) articoloSelezionato = new Articolo();
//			Log.debug("Dettaglio articolo: "+articoloSelezionato.getCodice());
		return articoloSelezionato;
	}

	public void setArticoloSelezionato(Articolo articoloSelezionato) {
		this.articoloSelezionato = articoloSelezionato;
	}

	public List<Articolo> getArticoli() {
		//Log.debug("Visualizzazione articoli");
		if (articoli==null)
			articoli = ArticoloBusiness.getInstance().getArticoli();
		return articoli;
	}

	public List<Articolo> getArticoliFiltrati() {
		return articoliFiltrati;
	}

	public void setArticoliFiltrati(List<Articolo> articoliFiltrati) {
		this.articoliFiltrati = articoliFiltrati;
	}

	public int getIdVarianteSelezionata() {
		return idVarianteSelezionata;
	}

	public void setIdVarianteSelezionata(int idVarianteSelezionata) {
		this.idVarianteSelezionata = idVarianteSelezionata;
	}

	public Articolo[] getArticoliSelezionati() {
		return articoliSelezionati;
	}

	public void setArticoliSelezionati(Articolo[] articoliSelezionati) {
		this.articoliSelezionati = articoliSelezionati;
	}



	public void setArticoli(List<Articolo> articoli) {
		this.articoli = articoli;
	}

	public String getIdEbay() {
		return idEbay;
	}

	public void setIdEbay(String idEbay) {
		this.idEbay = idEbay;
	}

	public Articolo getArticoloEbay() {
		return articoloEbay;
	}

	public void setArticoloEbay(Articolo articoloEbay) {
		this.articoloEbay = articoloEbay;
	}

	public List<Variante_Articolo> getVarianti() {
		return varianti;
	}

	public void setVarianti(List<Variante_Articolo> varianti) {
		this.varianti = varianti;
	}

	public String getTitoloInserzione() {
		return titoloInserzione;
	}

	public void setTitoloInserzione(String titoloInserzione) {
		this.titoloInserzione = titoloInserzione;
	}

	public String getIdEbay2() {
		return idEbay2;
	}

	public void setIdEbay2(String idEbay2) {
		this.idEbay2 = idEbay2;
	}

	public Variante_Articolo getVarianteSelezionata() {
		if (varianteSelezionata==null)
			varianteSelezionata = new Variante_Articolo();
		return varianteSelezionata;
	}

	public void setVarianteSelezionata(Variante_Articolo varianteSelezionata) {
		this.varianteSelezionata = varianteSelezionata;
	}

	public boolean isSyncSoloNuovi() {
		return syncSoloNuovi;
	}

	public void setSyncSoloNuovi(boolean syncSoloNuovi) {
		this.syncSoloNuovi = syncSoloNuovi;
	}

	public Date getSyncData() {
		return syncData;
	}

	public void setSyncData(Date syncData) {
		this.syncData = syncData;
	}

	public boolean isSyncNome() {
		return syncNome;
	}

	public void setSyncNome(boolean syncNome) {
		this.syncNome = syncNome;
	}

	public boolean isSyncDimensioni() {
		return syncDimensioni;
	}

	public void setSyncDimensioni(boolean syncDimensioni) {
		this.syncDimensioni = syncDimensioni;
	}

	public boolean isSyncCategoria() {
		return syncCategoria;
	}

	public void setSyncCategoria(boolean syncCategoria) {
		this.syncCategoria = syncCategoria;
	}

	public boolean isSyncPrezzoDettaglio() {
		return syncPrezzoDettaglio;
	}

	public void setSyncPrezzoDettaglio(boolean syncPrezzoDettaglio) {
		this.syncPrezzoDettaglio = syncPrezzoDettaglio;
	}

	public boolean isSyncPrezzoIngrosso() {
		return syncPrezzoIngrosso;
	}

	public void setSyncPrezzoIngrosso(boolean syncPrezzoIngrosso) {
		this.syncPrezzoIngrosso = syncPrezzoIngrosso;
	}

	public boolean isSyncCostoAcquisto() {
		return syncCostoAcquisto;
	}

	public void setSyncCostoAcquisto(boolean syncCostoAcquisto) {
		this.syncCostoAcquisto = syncCostoAcquisto;
	}

	public boolean isSyncCodiceFornitore() {
		return syncCodiceFornitore;
	}

	public void setSyncCodiceFornitore(boolean syncCodiceFornitore) {
		this.syncCodiceFornitore = syncCodiceFornitore;
	}

	public boolean isSyncCodiceArticoloFornitore() {
		return syncCodiceArticoloFornitore;
	}

	public void setSyncCodiceArticoloFornitore(boolean syncCodiceArticoloFornitore) {
		this.syncCodiceArticoloFornitore = syncCodiceArticoloFornitore;
	}

	public boolean isSyncCodiceBarre() {
		return syncCodiceBarre;
	}

	public void setSyncCodiceBarre(boolean syncCodiceBarre) {
		this.syncCodiceBarre = syncCodiceBarre;
	}

	public boolean isSyncTipoCodiceBarre() {
		return syncTipoCodiceBarre;
	}

	public void setSyncTipoCodiceBarre(boolean syncTipoCodiceBarre) {
		this.syncTipoCodiceBarre = syncTipoCodiceBarre;
	}

	public boolean isSyncIva() {
		return syncIva;
	}

	public void setSyncIva(boolean syncIva) {
		this.syncIva = syncIva;
	}

	public Integer getProgress() {  
          
        return progress;  
    }  

	public void setProgress(Integer progress) {
		this.progress = progress;
	}

	
	public Filtro getFiltro() {
		if (filtro==null)
			filtro = ArticoloBusiness.getInstance().getFiltro();
		return filtro;
	}


	public String getIdEbayXVarianti() {
		return idEbayXVarianti;
	}

	public void setIdEbayXVarianti(String idEbayXVarianti) {
		this.idEbayXVarianti = idEbayXVarianti;
	}

	public boolean isEbayTitoloInserzione() {
		return ebayTitoloInserzione;
	}

	public void setEbayTitoloInserzione(boolean ebayTitoloInserzione) {
		this.ebayTitoloInserzione = ebayTitoloInserzione;
	}

	public boolean isEbayCategorie() {
		return ebayCategorie;
	}

	public void setEbayCategorie(boolean ebayCategorie) {
		this.ebayCategorie = ebayCategorie;
	}

	public boolean isEbayPrezzo() {
		return ebayPrezzo;
	}

	public void setEbayPrezzo(boolean ebayPrezzo) {
		this.ebayPrezzo = ebayPrezzo;
	}

	public boolean isEbayDescrizione() {
		return ebayDescrizione;
	}

	public void setEbayDescrizione(boolean ebayDescrizione) {
		this.ebayDescrizione = ebayDescrizione;
	}

	public boolean isEbayVarianti() {
		return ebayVarianti;
	}

	public void setEbayVarianti(boolean ebayVarianti) {
		this.ebayVarianti = ebayVarianti;
	}

	public boolean isEbayQuantita() {
		return ebayQuantita;
	}

	public void setEbayQuantita(boolean ebayQuantita) {
		this.ebayQuantita = ebayQuantita;
	}

	public boolean isEliminaInserzioni() {
		return eliminaInserzioni;
	}

	public void setEliminaInserzioni(boolean eliminaInserzioni) {
		this.eliminaInserzioni = eliminaInserzioni;
	}


	public String getLinkImmagini() {
		return linkImmagini;
	}

	public void setLinkImmagini(String linkImmagini) {
		this.linkImmagini = linkImmagini;
	}

	public String getIdEbayChiudiInserzione() {
		return idEbayChiudiInserzione;
	}

	public void setIdEbayChiudiInserzione(String idEbayChiudiInserzione) {
		this.idEbayChiudiInserzione = idEbayChiudiInserzione;
	}



}
