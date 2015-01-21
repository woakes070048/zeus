package it.swb.bean;

import it.swb.business.ArticoloBusiness;
import it.swb.business.CategorieBusiness;
import it.swb.business.VarianteBusiness;
import it.swb.database.Articolo_DAO;
import it.swb.database.GM_IT_DAO;
import it.swb.database.GloriaMoraldi_DAO;
import it.swb.database.Variante_Articolo_DAO;
import it.swb.database.ZB_IT_DAO;
import it.swb.dbf.DbfUtil;
import it.swb.ftp.FTPutil;
import it.swb.java.EbayController;
import it.swb.java.EditorModelliAmazon;
import it.swb.java.EditorModelliYatego;
import it.swb.log.Log;
import it.swb.model.Articolo;
import it.swb.model.Categoria;
import it.swb.model.Filtro;
import it.swb.model.InfoAmazon;
import it.swb.model.InfoEbay;
import it.swb.model.Variante_Articolo;
import it.swb.utility.Costanti;
import it.swb.utility.EbayStuff;
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
    
    private boolean modalitaModifica;
    
    private static ArticoloBean instance = new ArticoloBean();
    
    /* Costruttore privato della classe */
    public ArticoloBean() {}

    /* Metodo che permette di ottenere l'istanza della classe */
    public static ArticoloBean getInstance() {
        return instance;
    }
    
	private Filtro filtro;
        
	private String linkImmagini = Costanti.percorsoImmaginiRemoto;
	
    private String codiceArticolo;
    private String nomeArticolo;
    private String noteArticolo;
    private long idCategoria1;
    private long idCategoria2;
    private String codiceFornitore;
	private String codiceArticoloFornitore;
    private double prezzoIngrosso;
    private double prezzoDettaglio;
    private double prezzoPiattaforme;
    private double costoAcquisto;
    private double costoSpedizione;
    private int iva;
    private int quantitaMagazzino;
    private int quantitaEffettiva;
    private String codiceBarre;
    private String tipoCodiceBarre;
    private String quantitaInserzione;
    private String dimensioni;
    private String descrizioneBreve;
    private String descrizione;
    private String immagine1;
    private String immagine2;
    private String immagine3;
    private String immagine4;
    private String immagine5;
    private String video;
    private String idVideo;
	private String paroleChiave1;
	private String paroleChiave2;
	private String paroleChiave3;
	private String paroleChiave4;
	private String paroleChiave5;	
    private int presente_su_ebay;
    private int presente_su_gm;
    private int presente_su_zb;
    private int presente_su_amazon;
    private int presente_su_yatego;
    
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
    private Date syncData = Methods.oraDelleStreghe(Methods.sottraiGiorniAData(new Date(), 30));
    private boolean syncNome;
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
    	svuotaCampi();
    	
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
    	svuotaCampi();
    	
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
        		
        		if(articoloSelezionato.getInfoEbay().getIdCategoriaEbay1()==null || articoloSelezionato.getInfoEbay().getIdCategoriaEbay1().trim().isEmpty()){
        			Object[] oggetti = EbayController.ottieniInformazioniDaID(idEbay2, getCosaScaricareDaEbay(false));
        			InfoEbay ie2 = (InfoEbay)oggetti[0];
        			
        			if (ie2.getIdCategoriaEbay1()!=null && !ie2.getIdCategoriaEbay1().trim().isEmpty())
        				articoloSelezionato.getInfoEbay().setIdCategoriaEbay1(ie2.getIdCategoriaEbay1());
        			
        			if (ie2.getIdCategoriaEbay2()!=null && !ie2.getIdCategoriaEbay2().trim().isEmpty())
        				articoloSelezionato.getInfoEbay().setIdCategoriaEbay2(ie2.getIdCategoriaEbay2());
        		}
        		Log.debug("Categorie: "+articoloSelezionato.getInfoEbay().getIdCategoriaEbay1()
	  					+","+articoloSelezionato.getInfoEbay().getIdCategoriaEbay2()
	  					+". Nome inserzione: "+articoloSelezionato.getInfoEbay().getTitoloInserzione());
    	  		
        	   	EbayController.modificaInserzione(articoloSelezionato, idEbay2);
        	   	Log.debug("OK");
        	}
    	  	else {
    	  		Object[] oggetti = EbayController.ottieniInformazioniDaID(idEbay2,getCosaScaricareDaEbay(false));
    	  		
    	  		articoloSelezionato.setInfoEbay((InfoEbay)oggetti[0]);
    	  		
    	  		Log.debug("Categorie: "+articoloSelezionato.getInfoEbay().getIdCategoriaEbay1()
    	  					+","+articoloSelezionato.getInfoEbay().getIdCategoriaEbay2()
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
//		ia.setPuntoElenco3(punto_elenco_3);
//		ia.setPuntoElenco4(punto_elenco_4);
//		ia.setPuntoElenco5(punto_elenco_5);
//		ia.setEsclusioneResponsabilita(esclusione_responsabilita);
//		ia.setDescrizioneGaranziaVenditore(descrizione_garanzia_venditore);
//		ia.setAvvertenzeSicurezza(avvertenze_sicurezza);
//		ia.setNotaCondizioni(nota_condizioni);
//		ia.setVocePacchettoQuantita(articoloSelezionato.getQuantitaInserzione());
//		ia.setNumeroPezzi();
		ia.setQuantitaMassimaSpedizioneCumulativa(articoloSelezionato.getQuantitaMagazzino());
//		ia.setPaeseOrigine(paese_origine);
//		ia.setLunghezzaArticolo(lunghezza_articolo);
//		ia.setAltezzaArticolo(altezza_articolo);
//		ia.setPesoArticolo(peso_articolo);
//		ia.setUnitaMisuraPesoArticolo(unita_misura_peso_articolo);
//		ia.setParoleChiave1("Bomboniera");
//		ia.setParoleChiave2("Bomboniere");
//		ia.setParoleChiave3(paroleChiave3);
//		ia.setParoleChiave4(paroleChiave4);
//		ia.setParoleChiave5(paroleChiave5);
		ia.setCategoria1(id_categoria_amazon);
//		ia.setNodo2(nodoSelezionato2);
		
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
	
	public void aggiungiAYatego(){
		
		int res = EditorModelliYatego.aggiungiProdottoAModelloYatego(articoloSelezionato);
			
		if (res==1) {
			Articolo_DAO.setPresenzaSu(articoloSelezionato.getCodice(), "yatego", -1, null);
			articoloSelezionato.setPresente_su_yatego(-1);
			FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Operazione completata", articoloSelezionato.getCodice()+" aggiunto al modello caricamento articoli di Yatego")); 
		} else FacesContext.getCurrentInstance().
		addMessage(null, new FacesMessage("Operazione non completata", "Si è verificato qualche errore."));
	}
    
	public void eliminaDaYatego(){
		
		int res = EditorModelliYatego.eliminaArticolo(articoloSelezionato);
			
		if (res==1) {
			Articolo_DAO.setPresenzaSu(articoloSelezionato.getCodice(), "yatego", -2, null);
			articoloSelezionato.setPresente_su_yatego(-2);
			FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Operazione completata", articoloSelezionato.getCodice()+" aggiunto al modello eliminazione articoli di Yatego"));  
		} else FacesContext.getCurrentInstance().
		addMessage(null, new FacesMessage("Operazione non completata", "Si è verificato qualche errore."));
	}
	
	public void aggiungiAZb(){
		
		if ( ZB_IT_DAO.insertIntoProduct(articoloSelezionato)==1 /*ZeldaSQL.aggiungiOEliminaDaModelloZelda(articoloSelezionato,true)*/ ) {
			Articolo_DAO.setPresenzaSu(articoloSelezionato.getCodice(), "zb", 1, null);
			articoloSelezionato.setPresente_su_zb(1);
			FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Operazione completata", articoloSelezionato.getCodice()+" inserito su ZeldaBomboniere.it")); 
		} else FacesContext.getCurrentInstance().
		addMessage(null, new FacesMessage("Operazione non completata", "Si è verificato qualche errore."));
	}
    
	public void eliminaDaZb(){
		
		if ( ZB_IT_DAO.deleteProduct(articoloSelezionato)==1 /* ZeldaSQL.aggiungiOEliminaDaModelloZelda(articoloSelezionato,false) */) {
			Articolo_DAO.setPresenzaSu(articoloSelezionato.getCodice(), "zb", 0, null);
			articoloSelezionato.setPresente_su_zb(0);
			FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Operazione completata", articoloSelezionato.getCodice()+" eliminato da ZeldaBomboniere.it"));  
		} else FacesContext.getCurrentInstance().
		addMessage(null, new FacesMessage("Operazione non completata", "Si è verificato qualche errore."));
	}
	
	public void eliminaDaAmazon(){
		
		//int res = 
		//EditorModelliAmazon.eliminaArticolo(articoloSelezionato);
		
		articoloSelezionato.setPresente_su_amazon(-2);
			
		//if (res==1) Articolo_DAO.setPresenzaSu(articoloSelezionato.getCodice(), "amazon", -2);
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
	
	public void eliminaDaGmOld(){
		
		int res = GloriaMoraldi_DAO.deleteProduct(articoloSelezionato.getIdArticolo());
			
		if (res==1) {
			Articolo_DAO.setPresenzaSu(articoloSelezionato.getCodice(), "gm", 0, null);
			articoloSelezionato.setPresente_su_gm(0);
			FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Operazione completata", articoloSelezionato.getCodice()+" eliminato da Gloriamoraldi.it"));
		} else FacesContext.getCurrentInstance().
		addMessage(null, new FacesMessage("Operazione non completata", "Si è verificato qualche errore."));
	}
    
    
    public void abilitaModifica(){
    	Log.debug("Modifica abilitata: "+articoloSelezionato.getCodice());
    	
    	codiceArticolo = articoloSelezionato.getCodice();
        nomeArticolo = articoloSelezionato.getNome();
        noteArticolo = articoloSelezionato.getNote();
        idCategoria1 = articoloSelezionato.getIdCategoria();
        idCategoria2 = articoloSelezionato.getIdCategoria2();
        codiceFornitore = articoloSelezionato.getCodiceFornitore();
    	codiceArticoloFornitore = articoloSelezionato.getCodiceArticoloFornitore();
        prezzoIngrosso = articoloSelezionato.getPrezzoIngrosso();
        prezzoDettaglio = articoloSelezionato.getPrezzoDettaglio();
        prezzoPiattaforme = articoloSelezionato.getPrezzoPiattaforme();
        costoAcquisto = articoloSelezionato.getCostoAcquisto();
        costoSpedizione = articoloSelezionato.getCostoSpedizione();
        iva = articoloSelezionato.getAliquotaIva();
        quantitaMagazzino = articoloSelezionato.getQuantitaMagazzino();
        quantitaEffettiva = articoloSelezionato.getQuantitaEffettiva();
        codiceBarre = articoloSelezionato.getCodiceBarre();
        tipoCodiceBarre = articoloSelezionato.getTipoCodiceBarre();
        quantitaInserzione = articoloSelezionato.getQuantitaInserzione();
        dimensioni = articoloSelezionato.getDimensioni();
        descrizione = articoloSelezionato.getDescrizione();
        descrizioneBreve = articoloSelezionato.getDescrizioneBreve();  
        idEbay = articoloSelezionato.getIdEbay();
        
        immagine1 = articoloSelezionato.getImmagine1();
        immagine2 = articoloSelezionato.getImmagine2();
        immagine3 = articoloSelezionato.getImmagine3();
        immagine4 = articoloSelezionato.getImmagine4();
        immagine5 = articoloSelezionato.getImmagine5();
        
        paroleChiave1 = articoloSelezionato.getParoleChiave1();
        paroleChiave2 = articoloSelezionato.getParoleChiave2();
        paroleChiave3 = articoloSelezionato.getParoleChiave3();
        paroleChiave4 = articoloSelezionato.getParoleChiave4();
        paroleChiave5 = articoloSelezionato.getParoleChiave5();
        
        presente_su_ebay = articoloSelezionato.getPresente_su_ebay();
        presente_su_gm = articoloSelezionato.getPresente_su_gm();
        presente_su_zb = articoloSelezionato.getPresente_su_zb();
        presente_su_amazon = articoloSelezionato.getPresente_su_amazon();
        presente_su_yatego = articoloSelezionato.getPresente_su_yatego();
     
       //Log.info(codiceArticolo+" "+nomeArticolo);
    }
    
    public void svuotaCampi(){
    	Log.debug("Svuota campi");
    	
    	codiceArticolo = "";
        nomeArticolo = "";
        noteArticolo = "";
        codiceFornitore = "";
    	codiceArticoloFornitore = "";
        prezzoIngrosso = 0;
        prezzoDettaglio = 0;
        prezzoPiattaforme = 0;
        costoAcquisto = 0;
        costoSpedizione = 0;
        iva = 21;
        quantitaMagazzino = 0;
        quantitaEffettiva = 0;
        codiceBarre = "";
        tipoCodiceBarre = "";
        quantitaInserzione = "";
        dimensioni = "";
        descrizione = "";
        descrizioneBreve = "";
        
        immagine1 = "";
        immagine2 = "";
        immagine3 = "";
        immagine4 = "";
        immagine5 = "";
        
        paroleChiave1 = "";
        paroleChiave2 = "";
        paroleChiave3 = "";
        paroleChiave4 = "";
        paroleChiave5 = "";
        
        presente_su_ebay = 0;
        presente_su_gm = 0;
        presente_su_zb = 0;
        presente_su_amazon = 0;
        presente_su_yatego = 0;
    }
    
    public void annullaModifica(){
    	Log.debug("Modifica annullata");
    	
    	codiceArticolo = "";
        nomeArticolo = "";
        noteArticolo = "";
        codiceFornitore = "";
    	codiceArticoloFornitore = "";
        prezzoIngrosso = 0;
        prezzoDettaglio = 0;
        prezzoPiattaforme = 0;
        costoAcquisto = 0;
        costoSpedizione = 0;
        iva = 21;
        quantitaMagazzino = 0;
        quantitaEffettiva = 0;
        codiceBarre = "";
        tipoCodiceBarre = "";
        quantitaInserzione = "";
        dimensioni = "";
        descrizione = "";
        descrizioneBreve = "";
        
        immagine1 = "";
        immagine2 = "";
        immagine3 = "";
        immagine4 = "";
        immagine5 = "";
        
        paroleChiave1 = "";
        paroleChiave2 = "";
        paroleChiave3 = "";
        paroleChiave4 = "";
        paroleChiave5 = "";
        
        presente_su_ebay = 0;
        presente_su_gm = 0;
        presente_su_zb = 0;
        presente_su_amazon = 0;
        presente_su_yatego = 0;
    }
    
//    public void NOsalvaModifica(){
//    	Log.debug("Salvataggio modifiche per articolo: "+codiceArticolo);
//    	
//    	Articolo a = new Articolo();
//    	    	
//    	a.setCodice(codiceArticolo);
//    	a.setNome(nomeArticolo);
//		a.setNote(noteArticolo);
//		a.setCodiceFornitore(codiceFornitore);
//		a.setCodiceArticoloFornitore(codiceArticoloFornitore);
//		a.setPrezzoDettaglio(prezzoDettaglio);
//		a.setPrezzoIngrosso(prezzoIngrosso);
//		a.setCostoAcquisto(costoAcquisto);
//		a.setCostoSpedizione(costoSpedizione);
//		a.setAliquotaIva(iva);
//		a.setCodiceBarre(codiceBarre);
//		a.setTipoCodiceBarre(tipoCodiceBarre);
//		a.setQuantita(quantita);
//		a.setQuantitaInserzione(quantitaInserzione);
//		a.setDimensioni(dimensioni);
//		a.setDescrizioneBreve(descrizioneBreve);
//		a.setDescrizione(descrizione);
//		
//		a.setImmagine1(immagine1);
//        a.setImmagine2(immagine2);
//        a.setImmagine3(immagine3);
//        a.setImmagine4(immagine4);
//        a.setImmagine5(immagine5);
//        
//        a.setPresente_su_ebay(presente_su_ebay);
//        a.setPresente_su_gm(presente_su_gm);
//        a.setPresente_su_amazon(presente_su_amazon);
//        a.setPresente_su_yatego(presente_su_yatego);
//  	
//		ArticoloBusiness.getInstance().modificaArticolo2(a);
//		
//		articoloSelezionato = a;
//	
//    }
    
    public void salvaSpunte(){
    	ArticoloBusiness.getInstance().setPresenze(articoloSelezionato);
    	
    	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Operazione Completata","Le presenze sulle piattaforme sono state impostate."));  
    }
    
    public void salvaModifica(){
    	Log.debug("Salvataggio modifiche per articolo: "+codiceArticolo);
    	
    	articoloSelezionato.setCodice(codiceArticolo);
    	articoloSelezionato.setNome(nomeArticolo);
		articoloSelezionato.setNote(noteArticolo);
		articoloSelezionato.setIdCategoria(idCategoria1);
        articoloSelezionato.setIdCategoria2(idCategoria2);
		articoloSelezionato.setCodiceFornitore(codiceFornitore);
		articoloSelezionato.setCodiceArticoloFornitore(codiceArticoloFornitore);
		articoloSelezionato.setPrezzoDettaglio(prezzoDettaglio);
		articoloSelezionato.setPrezzoIngrosso(prezzoIngrosso);
		articoloSelezionato.setPrezzoPiattaforme(prezzoPiattaforme);
		articoloSelezionato.setCostoAcquisto(costoAcquisto);
		articoloSelezionato.setCostoSpedizione(costoSpedizione);
		articoloSelezionato.setAliquotaIva(iva);
		articoloSelezionato.setCodiceBarre(codiceBarre);
		articoloSelezionato.setTipoCodiceBarre(tipoCodiceBarre);
		articoloSelezionato.setQuantitaMagazzino(quantitaMagazzino);
		articoloSelezionato.setQuantitaEffettiva(quantitaEffettiva);
		articoloSelezionato.setQuantitaInserzione(quantitaInserzione);
		articoloSelezionato.setDimensioni(dimensioni);
		articoloSelezionato.setDescrizioneBreve(descrizioneBreve);
		articoloSelezionato.setDescrizione(descrizione);
		
		articoloSelezionato.setImmagine1(immagine1);
        articoloSelezionato.setImmagine2(immagine2);
        articoloSelezionato.setImmagine3(immagine3);
        articoloSelezionato.setImmagine4(immagine4);
        articoloSelezionato.setImmagine5(immagine5);
        
        articoloSelezionato.setParoleChiave1(paroleChiave1);
        articoloSelezionato.setParoleChiave2(paroleChiave2);
        articoloSelezionato.setParoleChiave3(paroleChiave3);
        articoloSelezionato.setParoleChiave4(paroleChiave4);
        articoloSelezionato.setParoleChiave5(paroleChiave5);
        
        articoloSelezionato.setPresente_su_ebay(presente_su_ebay);
        articoloSelezionato.setIdEbay(idEbay);
        articoloSelezionato.setPresente_su_gm(presente_su_gm);
        articoloSelezionato.setPresente_su_gm(presente_su_zb);
        articoloSelezionato.setPresente_su_amazon(presente_su_amazon);
        articoloSelezionato.setPresente_su_yatego(presente_su_yatego);
  	
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
    		eliminaDaYatego();
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
			if (!Methods.creaThumbnailsEcaricaSuFtp(articoloSelezionato.getImmagine1(),true,f))
				ok = false;
		}
		if (Methods.controlloSintassiImmagine(articoloSelezionato.getImmagine2())){
			if (!Methods.creaThumbnailsEcaricaSuFtp(articoloSelezionato.getImmagine2(),false,f))
				ok = false;
		}
		if (Methods.controlloSintassiImmagine(articoloSelezionato.getImmagine3())){
			if (!Methods.creaThumbnailsEcaricaSuFtp(articoloSelezionato.getImmagine3(),false,f))
				ok = false;
		}
		if (Methods.controlloSintassiImmagine(articoloSelezionato.getImmagine4())){
			if (!Methods.creaThumbnailsEcaricaSuFtp(articoloSelezionato.getImmagine4(),false,f))
				ok = false;
		}
		if (Methods.controlloSintassiImmagine(articoloSelezionato.getImmagine5())){
			if (!Methods.creaThumbnailsEcaricaSuFtp(articoloSelezionato.getImmagine5(),false,f))
				ok = false;
		}
		
		if (articoloSelezionato.getVarianti()!=null && !articoloSelezionato.getVarianti().isEmpty()){
			for (Variante_Articolo v : articoloSelezionato.getVarianti()){
				if (Methods.controlloSintassiImmagine(v.getImmagine()))
				{
					if (!Methods.creaThumbnailsEcaricaSuFtp(v.getImmagine(),false,f))
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
	    	
	    	List<Articolo> arts = DbfUtil.syncArticoli(file_articoli, syncSoloNuovi, syncNome, syncData, syncIva, syncPrezzoDettaglio,
	    												syncPrezzoIngrosso, syncCostoAcquisto, syncCategoria,syncCodiceFornitore, 
	    												syncCodiceArticoloFornitore, syncCodiceBarre, syncTipoCodiceBarre); 
	    	
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
//		if (articoloSelezionato!=null)
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

	public boolean isModalitaModifica() {
		return modalitaModifica;
	}

	public void setModalitaModifica(boolean modalitaModifica) {
		this.modalitaModifica = modalitaModifica;
	}

	public String getCodiceArticolo() {
		return codiceArticolo;
	}

	public void setCodiceArticolo(String codiceArticolo) {
		this.codiceArticolo = codiceArticolo;
	}

	public String getNomeArticolo() {
		return nomeArticolo;
	}

	public void setNomeArticolo(String nomeArticolo) {
		this.nomeArticolo = nomeArticolo;
	}

	public double getPrezzoIngrosso() {
		return prezzoIngrosso;
	}

	public void setPrezzoIngrosso(double prezzoIngrosso) {
		this.prezzoIngrosso = prezzoIngrosso;
	}

	public double getPrezzoDettaglio() {
		return prezzoDettaglio;
	}

	public void setPrezzoDettaglio(double prezzoDettaglio) {
		this.prezzoDettaglio = prezzoDettaglio;
	}
	
	public double getPrezzoPiattaforme() {
		return prezzoPiattaforme;
	}

	public void setPrezzoPiattaforme(double prezzoPiattaforme) {
		this.prezzoPiattaforme = prezzoPiattaforme;
	}

	public double getCostoAcquisto() {
		return costoAcquisto;
	}

	public void setCostoAcquisto(double costoAcquisto) {
		this.costoAcquisto = costoAcquisto;
	}

	public int getQuantitaMagazzino() {
		return quantitaMagazzino;
	}

	public void setQuantitaMagazzino(int quantitaMagazzino) {
		this.quantitaMagazzino = quantitaMagazzino;
	}

	public String getCodiceBarre() {
		return codiceBarre;
	}

	public void setCodiceBarre(String codiceBarre) {
		this.codiceBarre = codiceBarre;
	}

	public String getTipoCodiceBarre() {
		return tipoCodiceBarre;
	}

	public void setTipoCodiceBarre(String tipoCodiceBarre) {
		this.tipoCodiceBarre = tipoCodiceBarre;
	}

	public String getQuantitaInserzione() {
		return quantitaInserzione;
	}

	public void setQuantitaInserzione(String quantitaInserzione) {
		this.quantitaInserzione = quantitaInserzione;
	}

	public String getDimensioni() {
		return dimensioni;
	}

	public void setDimensioni(String dimensioni) {
		this.dimensioni = dimensioni;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public int getIva() {
		return iva;
	}

	public void setIva(int iva) {
		this.iva = iva;
	}

	public String getDescrizioneBreve() {
		return descrizioneBreve;
	}

	public void setDescrizioneBreve(String descrizioneBreve) {
		this.descrizioneBreve = descrizioneBreve;
	}

	public String getCodiceFornitore() {
		return codiceFornitore;
	}

	public void setCodiceFornitore(String codiceFornitore) {
		this.codiceFornitore = codiceFornitore;
	}

	public String getCodiceArticoloFornitore() {
		return codiceArticoloFornitore;
	}

	public void setCodiceArticoloFornitore(String codiceArticoloFornitore) {
		this.codiceArticoloFornitore = codiceArticoloFornitore;
	}

	public String getNoteArticolo() {
		return noteArticolo;
	}

	public void setNoteArticolo(String noteArticolo) {
		this.noteArticolo = noteArticolo;
	}

	public String getImmagine1() {
		return immagine1;
	}

	public void setImmagine1(String immagine1) {
		this.immagine1 = immagine1;
	}

	public String getImmagine2() {
		return immagine2;
	}

	public void setImmagine2(String immagine2) {
		this.immagine2 = immagine2;
	}

	public String getImmagine3() {
		return immagine3;
	}

	public void setImmagine3(String immagine3) {
		this.immagine3 = immagine3;
	}

	public String getImmagine4() {
		return immagine4;
	}

	public void setImmagine4(String immagine4) {
		this.immagine4 = immagine4;
	}

	public String getImmagine5() {
		return immagine5;
	}

	public void setImmagine5(String immagine5) {
		this.immagine5 = immagine5;
	}

	public int getPresente_su_ebay() {
		return presente_su_ebay;
	}

	public void setPresente_su_ebay(int presente_su_ebay) {
		this.presente_su_ebay = presente_su_ebay;
	}

	public int getPresente_su_gm() {
		return presente_su_gm;
	}

	public void setPresente_su_gm(int presente_su_gm) {
		this.presente_su_gm = presente_su_gm;
	}
	
	public int getPresente_su_zb() {
		return presente_su_zb;
	}

	public void setPresente_su_zb(int presente_su_zb) {
		this.presente_su_zb = presente_su_zb;
	}

	public int getPresente_su_amazon() {
		return presente_su_amazon;
	}

	public void setPresente_su_amazon(int presente_su_amazon) {
		this.presente_su_amazon = presente_su_amazon;
	}

	public int getPresente_su_yatego() {
		return presente_su_yatego;
	}

	public void setPresente_su_yatego(int presente_su_yatego) {
		this.presente_su_yatego = presente_su_yatego;
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

	public double getCostoSpedizione() {
		return costoSpedizione;
	}

	public void setCostoSpedizione(double costoSpedizione) {
		this.costoSpedizione = costoSpedizione;
	}  
	
	public Filtro getFiltro() {
		if (filtro==null)
			filtro = ArticoloBusiness.getInstance().getFiltro();
		return filtro;
	}

	public int getQuantitaEffettiva() {
		return quantitaEffettiva;
	}

	public void setQuantitaEffettiva(int quantitaEffettiva) {
		this.quantitaEffettiva = quantitaEffettiva;
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

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public String getIdVideo() {
		return idVideo;
	}

	public void setIdVideo(String idVideo) {
		this.idVideo = idVideo;
	}

	public String getParoleChiave1() {
		return paroleChiave1;
	}

	public void setParoleChiave1(String paroleChiave1) {
		this.paroleChiave1 = paroleChiave1;
	}

	public String getParoleChiave2() {
		return paroleChiave2;
	}

	public void setParoleChiave2(String paroleChiave2) {
		this.paroleChiave2 = paroleChiave2;
	}

	public String getParoleChiave3() {
		return paroleChiave3;
	}

	public void setParoleChiave3(String paroleChiave3) {
		this.paroleChiave3 = paroleChiave3;
	}

	public String getParoleChiave4() {
		return paroleChiave4;
	}

	public void setParoleChiave4(String paroleChiave4) {
		this.paroleChiave4 = paroleChiave4;
	}

	public String getParoleChiave5() {
		return paroleChiave5;
	}

	public void setParoleChiave5(String paroleChiave5) {
		this.paroleChiave5 = paroleChiave5;
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

	public long getIdCategoria1() {
		return idCategoria1;
	}

	public void setIdCategoria1(long idCategoria1) {
		this.idCategoria1 = idCategoria1;
	}

	public long getIdCategoria2() {
		return idCategoria2;
	}

	public void setIdCategoria2(long idCategoria2) {
		this.idCategoria2 = idCategoria2;
	}


}
