package it.swb.bean;

import it.swb.business.ArticoloBusiness;
import it.swb.business.ClienteBusiness;
import it.swb.business.OrdineBusiness;
import it.swb.database.Articolo_DAO;
import it.swb.database.Ordine_DAO;
import it.swb.ebay.EbayController;
import it.swb.ebay.EbayStuff;
import it.swb.java.SdaUtility;
import it.swb.java.StampanteFiscale;
import it.swb.log.Log;
import it.swb.model.Articolo;
import it.swb.model.InfoEbay;
import it.swb.model.Ordine;
import it.swb.utility.Costanti;
import it.swb.utility.EditorDescrizioni;
import it.swb.utility.Methods;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
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

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean(name = "ordineBean")
@ViewScoped
public class OrdineBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    /* Costruttore privato della classe */
    public OrdineBean() {}
	
    private List<Ordine> ordini; 
    
    private String filtroOrdini = "tutti";
    
    private List<Ordine> ordiniFiltrati;  
    
    private List<Ordine> ordiniPerLDV;  
	private StreamedContent fileSpedizioni;
    
    private Ordine ordineSelezionato;
    
    private Articolo articoloSelezionato;
    
    private String codArticolo;
    
    private String codiceArticoloDaAssociare;
    private Articolo articoloDaAssociare;
    
    private Date mostraDa = Methods.sottraiGiorniAData(Methods.oraDelleStreghe(new Date()), 7);
    private Date mostraA = Methods.ventitreCinquantanove(new Date());
        
    private Date scaricaDa = Methods.sottraiGiorniAData(Methods.oraDelleStreghe(new Date()), 7);
    private Date scaricaA = Methods.ventitreCinquantanove(new Date());
    
    private Date dataOggi = new Date();
    
    private Articolo artDaModificare;
    
    public void onRowSelect(SelectEvent event) {  
        FacesMessage msg = new FacesMessage("Ordine Selezionato:", ((Ordine) event.getObject()).getIdOrdinePiattaforma());  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }  
    
    public void onArtSelect(SelectEvent event) {  
    	System.out.println(articoloSelezionato.getCodice());
        FacesMessage msg = new FacesMessage("Articolo Selezionato:", ((Articolo) event.getObject()).getCodice());  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    } 
    
    public void onItemDeleted(ActionEvent event) {  
        FacesMessage msg = new FacesMessage("Ordine Eliminato", null);  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    } 
    
    public void onClick(ActionEvent event) {  
        FacesMessage msg = new FacesMessage("E' stato fatto un click", null);  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }
    
    
    public void modificaArticoloInElenco(){
		Log.debug("modificaArticoloInElenco: " + artDaModificare.getCodice());
		if (ordineSelezionato.getArticoli() != null || ordineSelezionato.getArticoli().isEmpty()) {
			List<Articolo> artTemp = new ArrayList<Articolo>();

			for (Articolo a : ordineSelezionato.getArticoli()) {
				if (!a.getCodice().equals(artDaModificare.getCodice()))
					artTemp.add(a);
			}
			ordineSelezionato.setArticoli(artTemp);
		}
    }
    
    public void eliminaArticoloInElenco(){
		Log.debug("modificaArticoloInElenco: " + artDaModificare.getCodice());
		if (ordineSelezionato.getArticoli() != null || ordineSelezionato.getArticoli().isEmpty()) {
			List<Articolo> artTemp = new ArrayList<Articolo>();

			for (Articolo a : ordineSelezionato.getArticoli()) {
				if (!a.getCodice().equals(artDaModificare.getCodice()))
					artTemp.add(a);
			}
			ordineSelezionato.setArticoli(artTemp);
			artDaModificare = null;
		}
    }
    
    public void aggiungiArticoloInElenco(){
		if (ordineSelezionato.getArticoli() == null || ordineSelezionato.getArticoli().isEmpty())
			ordineSelezionato.setArticoli(new ArrayList<Articolo>());

		Log.debug("aggiungi Articolo In Elenco: " + artDaModificare.getCodice());
		Articolo a = artDaModificare;
		
		ordineSelezionato.getArticoli().add(a);

		artDaModificare = null;
    }
    
    public void modificaOrdine(){
    	OrdineBusiness.getInstance().modificaOrdine(ordineSelezionato);
    	
    	FacesMessage msg = new FacesMessage("Ordine Modificato", null);  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }
    
    
    public void stampaScontrino(){
    	
    	StampanteFiscale.stampaScontrino(ordineSelezionato);
    	FacesMessage msg = new FacesMessage("Operazione Completata", "Scontrino mandato in stampa");  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }
    
    String linkOrdine;
    
    public String getLinkOrdine(){
    	String link = "";
    	if (ordineSelezionato!=null){
	    	if (ordineSelezionato.getPiattaforma().equals("ZeldaBomboniere.it")){
	    		String id = ordineSelezionato.getIdOrdinePiattaforma().replace("ZB_", "");
	    		link = "http://zeldabomboniere.it/admin/?route=sale/order/info&order_id="+id;
	    	}
	    	else if (ordineSelezionato.getPiattaforma().equals("eBay")){
	    		String id = ordineSelezionato.getIdOrdinePiattaforma();
	    		link = "http://k2b-bulk.ebay.it/ws/eBayISAPI.dll?EditSalesRecord&orderid="+id;
	    		
	    	}
	    	else if (ordineSelezionato.getPiattaforma().equals("Amazon")){
	    		String id = ordineSelezionato.getIdOrdinePiattaforma();
	    		link = "https://sellercentral.amazon.it/gp/orders-v2/details/ref=ag_orddet_cont_myo?ie=UTF8&orderID="+id;
	    	}
    	}
    	return link;
    }
    
    int quantitaOrdineSelezionato;
    
    public int getQuantitaOrdineSelezionato(){
    	int qt = 0;
    	
    	if (ordineSelezionato!=null && ordineSelezionato.getArticoli()!=null)
	    	for (Articolo a : ordineSelezionato.getArticoli()){
	    		qt+=a.getQuantitaMagazzino();
	    	}
    	
    	return qt;
    }
    
    double totaleOrdineSelezionato;
    
    public double getTotaleOrdineSelezionato(){
    	double tot = 0;
        
    	if (ordineSelezionato!=null && ordineSelezionato.getArticoli()!=null)
	    	for (Articolo a : ordineSelezionato.getArticoli()){
	    		tot+=a.getPrezzoPiattaforme();
	    	}
    	
	    tot = Methods.round(tot,2);
	    
    	return tot;
    }
    
    double granTotale;
    
    public double getGranTotale(){
    	double tot = 0;
    	
    	tot = getTotaleOrdineSelezionato();
    	if (ordineSelezionato!=null){
    		tot+=ordineSelezionato.getCostoSpedizione();
	    	
    		if (ordineSelezionato.getMetodoPagamento().equals("Contrassegno"))
	    		tot+=3;
    		
	    		tot+=ordineSelezionato.getValoreBuonoSconto();
	    }
    	tot = Methods.round(tot, 2);
    	
    	return tot;
    }
    
	public void archivia(){
		int x = Ordine_DAO.archivia(ordineSelezionato.getIdOrdine());
		
		Log.info("Archiviato ordine "+ordineSelezionato.getIdOrdine()+" con risultato: "+x);
		
		Ordine_DAO.inserisciInCodaLDV(ordineSelezionato.getIdOrdine(), 0);
		ordiniPerLDV.remove(ordineSelezionato);
		
		//reloadOrdini(); vedi sotto
		ordini.remove(ordineSelezionato);
		
		if (x==1) {
			FacesMessage msg = new FacesMessage("Operazione Completata", "Ordine "+ordineSelezionato.getIdOrdine()+" archiviato");  
	        FacesContext.getCurrentInstance().addMessage(null, msg);  
		} else {
			FacesMessage msg = new FacesMessage("Operazione Non Completata", "Si è verificato un errore.");  
	        FacesContext.getCurrentInstance().addMessage(null, msg);  
		}	
	}
    
	public void setFileSpedizioni(StreamedContent fileSpedizioni) {
		this.fileSpedizioni = fileSpedizioni;
	}
	
	public void inviaAcodaLDV(){
		
		int x = Ordine_DAO.inserisciInCodaLDV(ordineSelezionato.getIdOrdine(), -1);
		
		ordiniPerLDV.add(ordineSelezionato);
		
		//aggiornaOrdiniPerLDV()
		
		Log.info("Inviato ordine "+ordineSelezionato.getIdOrdine()+" a coda LDV con risultato: "+x);
		
		if (x==1) {
			FacesMessage msg = new FacesMessage("Operazione Completata", "Ordine "+ordineSelezionato.getIdOrdine()+" inviato alla coda LDV");  
	        FacesContext.getCurrentInstance().addMessage(null, msg);  
		} else {
			FacesMessage msg = new FacesMessage("Operazione Non Completata", "Si è verificato un errore.");  
	        FacesContext.getCurrentInstance().addMessage(null, msg);  
		}	
	}
	
	public void togliDaCodaLDV(){
		
		int x = Ordine_DAO.inserisciInCodaLDV(ordineSelezionato.getIdOrdine(), 0);
		
		Log.info("Eliminato ordine "+ordineSelezionato.getIdOrdine()+" da coda LDV con risultato: "+x);
		
		ordiniPerLDV.remove(ordineSelezionato);
		
		if (x==1) {
			FacesMessage msg = new FacesMessage("Operazione Completata", "Ordine "+ordineSelezionato.getIdOrdine()+" eliminato dalla coda LDV");  
	        FacesContext.getCurrentInstance().addMessage(null, msg);  
		} else {
			FacesMessage msg = new FacesMessage("Operazione Non Completata", "Si è verificato un errore.");  
	        FacesContext.getCurrentInstance().addMessage(null, msg);  
		}	
	}

	public void generaLDV(){
	  	Properties config = new Properties();	   
	  	
	  	String nomeFile = "";
		
			try {
				config.load(Log.class.getResourceAsStream("/zeus.properties"));
				
				String percorsoFile = config.getProperty("percorso_ldv");	
				nomeFile = config.getProperty("nome_ldv");
	    	
				String data = Methods.getDataCompletaPerNomeFileTesto();
				
				nomeFile = nomeFile.replace("DATA", data);
				
				SdaUtility.aggiungiOrdineALDV(ordiniPerLDV,percorsoFile+nomeFile);
		    	
		    	Log.info("Generata LDV: "+percorsoFile+nomeFile);
		    	
		    	//Ordine_DAO.togliDaCodaLDV(ordiniPerLDV);
		    	
			} catch (IOException e) {
				e.printStackTrace();
				Log.error(e.getMessage());
			}	
	    	FacesMessage msg = new FacesMessage("Lettera di vettura generata", nomeFile);  
	        FacesContext.getCurrentInstance().addMessage(null, msg);  
	}
	
/* sostituito
	public void generaLDV(){
    	Properties config = new Properties();	   
		
		try {
			config.load(Log.class.getResourceAsStream("/zeus.properties"));
			
			String percorsoFile = config.getProperty("percorso_ldv");	
			String nomeFile = config.getProperty("nome_ldv");
    	
			String data = Methods.getDataCompletaPerNomeFileTesto();
			
			nomeFile = nomeFile.replace("DATA", data);
			
			SdaUtility.aggiungiOrdineALDV(ordiniSelezionati,percorsoFile+nomeFile);
	    	
	    	Log.info("Download file: "+percorsoFile+nomeFile);
	    	
		} catch (IOException e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}	
    	FacesMessage msg = new FacesMessage("Operazione Completata", "Generata LDV");  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
	}
*/  
	
    public StreamedContent getFileSpedizioni() throws FileNotFoundException {  
    	
    	for (Ordine o : ordiniPerLDV)
    		System.out.println(o.getIdOrdine());
    	
    	Properties config = new Properties();	   
		
		try {
			config.load(Log.class.getResourceAsStream("/zeus.properties"));
			
			String percorsoFile = config.getProperty("percorso_ldv");	
			String nomeFile = config.getProperty("nome_ldv");
    	
			String data = Methods.getDataCompletaPerNomeFileTesto();
			
			nomeFile = nomeFile.replace("DATA", data);
			
			SdaUtility.aggiungiOrdineALDV(ordiniPerLDV,percorsoFile+nomeFile);
	    	
	    	Log.info("Download file: "+percorsoFile+nomeFile);
	    	
	    	if (percorsoFile!=null && !percorsoFile.isEmpty() && nomeFile!=null && !nomeFile.isEmpty()){
	    		
	    		File f = new File(percorsoFile+nomeFile);
	            
	            if (f.exists()){
	            	InputStream stream = new FileInputStream(f);
	                fileSpedizioni = new DefaultStreamedContent(stream, "text/txt", nomeFile); 
	            }
	    	}
		} catch (IOException e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}	
    	FacesMessage msg = new FacesMessage("Operazione Completata", "Generata LDV");  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
        
	        return fileSpedizioni;  
    }  
    
    
    public void associaCodiceArticolo(){
    	System.out.println("codice: "+codiceArticoloDaAssociare);
    	System.out.println("id: "+articoloDaAssociare.getTitoloInserzione());
    	System.out.println("ordine corrente: "+ordineSelezionato.getIdOrdinePiattaforma());
    	
    	codiceArticoloDaAssociare = codiceArticoloDaAssociare.trim().toUpperCase();
    	
    	Articolo art = ArticoloBusiness.getInstance().getMappaArticoli().get(codiceArticoloDaAssociare);
    	articoloDaAssociare.setCodice(codiceArticoloDaAssociare);
    	
    	if (art!=null){		/* se esiste un articolo che ha il codice inserito */
    		//InfoEbay ie = articoloDaAssociare.getInfoEbay();
    		
    		/* scarico da ebay le informazioni sull'articolo (poi dovrò distinguere i casi in cui mi servono o meno, ad esempio se ci sono già) */
    		Object[] oggetti = EbayController.ottieniInformazioniDaID(articoloDaAssociare.getTitoloInserzione(),getCosaScaricareDaEbay(false));
	  		
    		art.setInfoEbay((InfoEbay)oggetti[0]);
    		art.setQuantitaMagazzino((Integer)oggetti[2]);
    		
    		/* questo passaggio mi serve o meno a seconda della distinzione di cui sopra */
    		art = EbayStuff.elaboraDescrizione(art);   
    		
    		
    		Articolo_DAO.modificaInformazioniEbay(codiceArticoloDaAssociare,articoloDaAssociare.getTitoloInserzione(), art.getInfoEbay(), null);
    		
    		/* creo la descrizione con il nuovo template */
    		art.getInfoEbay().setDescrizioneEbay(EditorDescrizioni.creaDescrizioneEbay(art));   	
	    	
	    	/* provo ad aggiornare il template dell'inserzione (oltre a questo, si occupa anche di impostare lo SKU */
	    	boolean ok = EbayController.modificaInserzioneStores(art, articoloDaAssociare.getTitoloInserzione());
	    	
	    	//EbayGetOrders.getOrderTransaction(ordineSelezionato.getIdOrdine());
	    	if (ok) /* se l'aggiornamento è andato a buon fine, associo il codice articolo */
	    	OrdineBusiness.getInstance().modificaArticolo(articoloDaAssociare, ordineSelezionato.getIdOrdinePiattaforma());
    	}
    }
    
    private Map<String,Boolean> getCosaScaricareDaEbay(boolean minimal){
    	Map<String,Boolean> cosaScaricare = new HashMap<String,Boolean>();
    	cosaScaricare.put("titolo", true);
    	cosaScaricare.put("categorie", true);
    	if (minimal){
    		cosaScaricare.put("prezzo", false);
    	    cosaScaricare.put("descrizione", false);
    	    cosaScaricare.put("quantita", false);
    	    cosaScaricare.put("varianti", false);
    	} else {
    		cosaScaricare.put("prezzo", true);
    	    cosaScaricare.put("descrizione", true);
    	    cosaScaricare.put("quantita", true);
    	    cosaScaricare.put("varianti", true);
    	}
    	return cosaScaricare;
    }
    
    public String getLinkEbay(){ 	
    	String titolo = articoloSelezionato.getTitoloInserzione();   		
    	if (titolo==null || titolo.isEmpty())
    		titolo = articoloSelezionato.getNome().replace(" ", "+");
    	
    	return Costanti.ricercaSuEbay+titolo;
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
    	return Costanti.ricercaSuGmBackend+articoloSelezionato.getIdArticolo();
    }
    
    public String getLinkAmazonFrontend(){ 	
    	return Costanti.ricercaSuAmazonFrontend+(articoloSelezionato.getNome().replace(" ", "+"));
    }
    
    public String getLinkAmazonBackend(){ 	
    	return Costanti.ricercaSuAmazonBackend+(articoloSelezionato.getCodice());
    }
    
    public String getLinkZbFrontend(){ 	
    	return Costanti.ricercaSuZbFrontend+articoloSelezionato.getIdArticolo();
    }
    
    public String getLinkZbBackend(){ 	
    	return Costanti.ricercaSuZbBackend+articoloSelezionato.getCodice();
    }
    
    public void reloadMappaArticoli(){
    	ArticoloBusiness.getInstance().reloadMappaArticoli();
    }
    
    public void segnaSpedito(){
    	Log.debug("Imposto come spedito: "+ordineSelezionato.getIdOrdinePiattaforma()+", TrackingNumber: "+ordineSelezionato.getNumeroTracciamento());
    	//EbayGetOrders.completeSale(ordineSelezionato.getIdOrdine(), ordineSelezionato.getTrackingNumber());
    }
    
    
    public void getArt(){ 	
    	Log.debug("Dettaglio Articolo: "+codArticolo);
    	if(codArticolo!=null && !codArticolo.trim().isEmpty())
    		articoloSelezionato = ArticoloBusiness.getInstance().getMappaArticoli().get(codArticolo);
    }
    
    
    public void eliminaOrdine(){
    	ordini = OrdineBusiness.getInstance().eliminaOrdine(ordineSelezionato.getIdOrdine());
    }
    

    
    public void reloadOrdini(){
    	Log.debug("Reload ordini...");
    	ClienteBusiness.getInstance().reloadMappaClientiZeldaCompletaByID();
    	ordini = OrdineBusiness.getInstance().reloadOrdini(getMostraDa(),getMostraA(), filtroOrdini);
    }
    
	public List<Ordine> getOrdini() {
		if (ordini==null){
			ordini = OrdineBusiness.getInstance().getOrdini(getMostraDa(),getMostraA(), filtroOrdini);
		}
		return ordini;
	}
	
	public void downloadOrdini() {
		sistemaOre();
		String s = OrdineBusiness.getInstance().downloadOrdini(scaricaDa, scaricaA);
		reloadOrdini(); 
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Operazione Completata",s));  
	}
	
	public boolean noButton(){
		if (filtroOrdini.equals("archiviati")) return false;
		else return true;
	}
	

	
	
	private void sistemaOre(){
		scaricaDa = Methods.oraDelleStreghe(scaricaDa);
		scaricaA = Methods.ventitreCinquantanove(scaricaA);
	}
	
	

	public void setOrdini(List<Ordine> ordini) {
		this.ordini = ordini;
	}

	public List<Ordine> getOrdiniFiltrati() {
		return ordiniFiltrati;
	}

	public void setOrdiniFiltrati(List<Ordine> ordiniFiltrati) {
		this.ordiniFiltrati = ordiniFiltrati;
	}

	public Ordine getOrdineSelezionato() {
		return ordineSelezionato;
	}

	public void setOrdineSelezionato(Ordine ordineSelezionato) {
		this.ordineSelezionato = ordineSelezionato;
	}

	public Date getScaricaDa() {
		return scaricaDa;
	}

	public void setScaricaDa(Date scaricaDa) {
		this.scaricaDa = scaricaDa;
	}

	public Date getScaricaA() {
		return scaricaA;
	}

	public void setScaricaA(Date scaricaA) {
		this.scaricaA = scaricaA;
	}

	public Date getDataOggi() {
		return dataOggi;
	}

	public void setDataOggi(Date dataOggi) {
		this.dataOggi = dataOggi;
	}

	public Articolo getArticoloSelezionato() {
		return articoloSelezionato;
	}

	public void setArticoloSelezionato(Articolo articoloSelezionato) {
		this.articoloSelezionato = articoloSelezionato;
	}

	public String getCodArticolo() {
		return codArticolo;
	}

	public void setCodArticolo(String codArticolo) {
		this.codArticolo = codArticolo;
	}

	public String getCodiceArticoloDaAssociare() {
		return codiceArticoloDaAssociare;
	}

	public void setCodiceArticoloDaAssociare(String codiceArticoloDaAssociare) {
		this.codiceArticoloDaAssociare = codiceArticoloDaAssociare;
	}

	public Articolo getArticoloDaAssociare() {
		return articoloDaAssociare;
	}

	public void setArticoloDaAssociare(Articolo articoloDaAssociare) {
		this.articoloDaAssociare = articoloDaAssociare;
	}

	public Date getMostraDa() {
		return Methods.oraDelleStreghe(mostraDa);
	}

	public void setMostraDa(Date mostraDa) {
		this.mostraDa = mostraDa;
	}

	public Date getMostraA() {
		return Methods.ventitreCinquantanove(mostraA);
	}

	public void setMostraA(Date mostraA) {
		this.mostraA = mostraA;
	}

	public Articolo getArtDaModificare() {
		if (artDaModificare==null) artDaModificare = new Articolo();
		return artDaModificare;
	}

	public void setArtDaModificare(Articolo artDaModificare) {
		this.artDaModificare = artDaModificare;
	}

	public List<Ordine> getOrdiniPerLDV() {
		if (ordiniPerLDV==null){
			ordiniPerLDV = OrdineBusiness.getInstance().getOrdiniPerLDV();
		}
		return ordiniPerLDV;
	}
	
	public void aggiornaOrdiniPerLDV() {
		ordiniPerLDV = OrdineBusiness.getInstance().getOrdiniPerLDV();
	}

	public void setOrdiniPerLDV(List<Ordine> ordiniPerLDV) {
		this.ordiniPerLDV = ordiniPerLDV;
	}

	public String getFiltroOrdini() {
		return filtroOrdini;
	}

	public void setFiltroOrdini(String filtroOrdini) {
		this.filtroOrdini = filtroOrdini;
	}
    

    

 }
