package it.swb.bean;

import it.swb.business.ArticoloBusiness;
import it.swb.business.OrdineBusiness;
import it.swb.database.Articolo_DAO;
import it.swb.database.Ordine_DAO;
import it.swb.java.CourierUtility;
import it.swb.java.StampanteFiscale;
import it.swb.log.Log;
import it.swb.model.Articolo;
import it.swb.model.ArticoloAcquistato;
import it.swb.model.InfoEbay;
import it.swb.model.Ordine;
import it.swb.piattaforme.amazon.AmazonSubmitFeed;
import it.swb.piattaforme.amazon.EditorModelliAmazon;
import it.swb.piattaforme.ebay.EbayController;
import it.swb.piattaforme.ebay.EbayGetOrders;
import it.swb.piattaforme.ebay.EbayStuff;
import it.swb.piattaforme.zelda.ZB_IT_DAO;
import it.swb.utility.BarcodeGenerator;
import it.swb.utility.Costanti;
import it.swb.utility.DateMethods;
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
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean(name = "ordineBean")
@SessionScoped//ViewScoped
public class OrdineBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    /* Costruttore privato della classe */
    public OrdineBean() {}
	
    private List<Ordine> ordini; 
    private List<Ordine> ordiniFiltrati;  
    
    private List<Ordine> ordiniInCodaLDV;  
    private List<Ordine> ordiniFiltratiLDV;  
    
    private String filtroOrdini = "nonarchiviati";
    
	private StreamedContent fileSpedizioni;
    
    private Ordine ordineSelezionato;
    
    private Articolo articoloSelezionato;
    
    private String codArticolo;
    
    private String codiceArticoloDaAssociare;
    private Articolo articoloDaAssociare;
    
    private Date mostraDa = DateMethods.sottraiGiorniAData(DateMethods.oraDelleStreghe(new Date()), 7);
    private Date mostraA = DateMethods.ventitreCinquantanove(new Date());
        
    private Date scaricaDa = DateMethods.sottraiGiorniAData(DateMethods.oraDelleStreghe(new Date()), 4);
    private Date scaricaA = DateMethods.ventitreCinquantanove(new Date());
    
    private Date dataConfermaSpedizioni = new Date();
    
    private Date dataOggi = new Date();
    
    private ArticoloAcquistato artDaModificare;
    
	public void showMessage(String titolo, String messaggio) {
		FacesMessage message = new FacesMessage(titolo,messaggio);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public void showErrorMessage(String titolo, String messaggio) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, titolo,messaggio);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
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
    
    public void salvaNumeriTracciamento(){
    	int t = CourierUtility.salvaNumeriTracciamentoSDA(null,null);
    	
    	showMessage("Operazione completata", "Salvati "+t+" numeri di tracciamento");
    }
    
    private double totaleOrdiniInStampa = 0;
    private int numeroOrdiniTotale = 0;
    private int numeroOrdiniAmazon = 0;
    private int numeroOrdiniEbay = 0;
    private int numeroOrdiniZb = 0;
    
    public String stampaDettagliOrdini(){
    	String s = "";
    	
    	totaleOrdiniInStampa = 0;
    	numeroOrdiniTotale = 0;
        numeroOrdiniAmazon = 0;
        numeroOrdiniEbay = 0;
        numeroOrdiniZb = 0;
    	
    	if (getOrdiniInCodaLDV()!=null && !getOrdiniInCodaLDV().isEmpty()){  	
    		numeroOrdiniTotale = getOrdiniInCodaLDV().size();
    		
	    	for (Ordine o : getOrdiniInCodaLDV()){
	    		if (o.getPiattaforma().contains("Amazon")) numeroOrdiniAmazon++;
	    		else if (o.getPiattaforma().contains("eBay")) numeroOrdiniEbay++;
	    		else if (o.getPiattaforma().contains("ZeldaBomboniere.it")) numeroOrdiniZb++;
	    		
	    		totaleOrdiniInStampa+=o.getTotale();
	    	}
    	}
    	
    	s+= "Orario di stampa: "+DateMethods.formattaData3(new Date());
    	
    	return s;
    }
    
    public String stampaOrdine(){
    	BarcodeGenerator.generaBarcode(String.valueOf(ordineSelezionato.getIdOrdine()));
    	
    	return "pages/ordini/stampa_ordine";
    }
    
    public void inviaNumeriTracciamento(){
    	
    	List<Map<String,String>> numeriTracciamento = Ordine_DAO.getNumeriTracciamento(dataConfermaSpedizioni,0);
    	
    	System.out.println("data: "+dataConfermaSpedizioni);
    	
    	List<Map<String,String>> listaAmazon = new ArrayList<Map<String,String>>();
    	List<Map<String,String>> listaEbay = new ArrayList<Map<String,String>>();
    	List<Map<String,String>> listaZelda = new ArrayList<Map<String,String>>();
    	
    	int speditiZelda = 0;
    	int speditiEbay = 0;
    	
    	for (Map<String,String> num : numeriTracciamento){
    		
    		if (num.get("piattaforma").contains("eBay")){
	    		//EbayGetOrders.completeSale(num.get("id_ordine_piattaforma"), num.get("numero_tracciamento"));
    			listaEbay.add(num);
	    	}
	    	else if (num.get("piattaforma").contains("ZeldaBomboniere.it")){
	    		//ZB_IT_DAO.confirmShipment(num.get("id_ordine_piattaforma").replace("ZB_", ""), num.get("numero_tracciamento"));
	    		listaZelda.add(num);
	    	}
	    	else if (num.get("piattaforma").contains("Amazon")){
	    		listaAmazon.add(num);
	    	}
    	}
    	
    	speditiEbay = EbayGetOrders.inviaNumeriDiTracciamento(listaEbay);
    	speditiZelda = ZB_IT_DAO.confirmShipments(listaZelda);
    	
    	String fileSpeditiAmazon = EditorModelliAmazon.generaModelloConfermaSpedizioni(listaAmazon);
    	
    	Properties config = new Properties();	      
    	try {
    		
			config.load(Log.class.getResourceAsStream("/zeus.properties"));
			String path = config.getProperty("percorso_conferma_spedizioni");
	    	AmazonSubmitFeed.inviaModelloNumeriTracciamento(path+fileSpeditiAmazon);
	    	
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	Log.info("Caricati i numeri di tracciamento su eBay: "+speditiEbay+" su "+listaEbay.size());
    	Log.info("Caricati i numeri di tracciamento su ZeldaBomboniere.it: "+speditiZelda+" su "+listaZelda.size());
    	
    	showMessage("Operazione completata", "Caricati i numeri di tracciamento su eBay: "+speditiEbay+" su "+listaEbay.size()+
    			". Caricati i numeri di tracciamento su ZeldaBomboniere.it: "+speditiZelda+" su "+listaZelda.size()+
    			". Generato ed inviato il file da caricare su amazon: "+fileSpeditiAmazon);
    	
    	
    }
    
    public void segnaComeSpedito(){
    	Log.debug("Imposto come spedito l'ordine: "+ordineSelezionato.getIdOrdinePiattaforma()+", con numero di tracciamento: "+ordineSelezionato.getNumeroTracciamento());
    	
    	if (ordineSelezionato.getNumeroTracciamento()!=null && !ordineSelezionato.getNumeroTracciamento().isEmpty()){
    		boolean spedito = false;
    	
	    	if (ordineSelezionato.getPiattaforma().contains("eBay")){
	    		spedito = EbayGetOrders.completeSale(ordineSelezionato.getIdOrdinePiattaforma(), 
	    				ordineSelezionato.getNumeroTracciamento(), ordineSelezionato.getNomeCorriere());
	    	}
	    	else if (ordineSelezionato.getPiattaforma().contains("ZeldaBomboniere.it")){
	    		spedito = ZB_IT_DAO.confirmShipment(ordineSelezionato.getIdOrdinePiattaforma().replace("ZB_", ""), 
	    				ordineSelezionato.getNumeroTracciamento(), ordineSelezionato.getNomeCorriere());
	    	}
	    	else if (ordineSelezionato.getPiattaforma().contains("Amazon")){
	    		//TODO fare il metodo segna come spedito amazon
	    	}
	    	
	    	if (spedito) showMessage("Operazione completata", "Numero di tracciamento inviato su "+ordineSelezionato.getPiattaforma());
	    	else showErrorMessage("Errore", "Si � verificato un errore.");
    	}
    	else showErrorMessage("Errore", "Numero di tracciamento non presente per l'ordine selezionato.");
    	
    	Log.debug("Fine dell'operazione.");
    }
    
    public void modificaArticoloInElenco(){
		Log.debug("modificaArticoloInElenco: " + artDaModificare.getCodice());
		if (ordineSelezionato.getElencoArticoli() != null || ordineSelezionato.getElencoArticoli().isEmpty()) {
			List<ArticoloAcquistato> artTemp = new ArrayList<ArticoloAcquistato>();

			for (ArticoloAcquistato a : ordineSelezionato.getElencoArticoli()) {
				if (!a.getCodice().equals(artDaModificare.getCodice()))
					artTemp.add(a);
			}
			ordineSelezionato.setElencoArticoli(artTemp);
		}
    }
    
    public void eliminaArticoloInElenco(){
		Log.debug("modificaArticoloInElenco: " + artDaModificare.getCodice());
		if (ordineSelezionato.getElencoArticoli() != null || ordineSelezionato.getElencoArticoli().isEmpty()) {
			List<ArticoloAcquistato> artTemp = new ArrayList<ArticoloAcquistato>();

			for (ArticoloAcquistato a : ordineSelezionato.getElencoArticoli()) {
				if (!a.getCodice().equals(artDaModificare.getCodice()))
					artTemp.add(a);
			}
			ordineSelezionato.setElencoArticoli(artTemp);
			ordineSelezionato.setTotale(getTotaleOrdineSelezionato());
			artDaModificare = null;
		}
    }
    
    public void aggiungiArticoloInElenco(){
    	List<ArticoloAcquistato> elenco = new ArrayList<ArticoloAcquistato>();
    	
		if (ordineSelezionato.getElencoArticoli() != null || !ordineSelezionato.getElencoArticoli().isEmpty())
			elenco = ordineSelezionato.getElencoArticoli();

		Log.debug("Aggiungo Articolo In Elenco: " + artDaModificare.getCodice());
		ArticoloAcquistato a = artDaModificare;
		a.setPrezzoTotale(Methods.veryRound(a.getPrezzoUnitario()*a.getQuantitaAcquistata()));
		
		if (a.getTitoloInserzione()==null) a.setTitoloInserzione(a.getNome());
		
		elenco.add(a);
		
		ordineSelezionato.setElencoArticoli(elenco);
		
		ordineSelezionato.setTotale(getTotaleOrdineSelezionato());

		artDaModificare = null;
    }
    
    public void modificaOrdine(){
    	System.out.println("modifica ordine");
    	OrdineBusiness.getInstance().modificaOrdine(ordineSelezionato);
    	
    	FacesMessage msg = new FacesMessage("Ordine Modificato", null);  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }
    
    
    public void stampaScontrino(){
    	
    	boolean stampato = StampanteFiscale.stampaScontrino(ordineSelezionato);
    	if (stampato) showMessage("Scontrino mandato in stampa", "");  
    	else showErrorMessage("Impossibile stampare lo scontrino", "Si � verificato un errore. Controllare i log.");  
    }
    
    String linkOrdine;
    
    public String getLinkOrdine(){
    	String link = "";
    	if (ordineSelezionato!=null){
	    	if (ordineSelezionato.getPiattaforma().contains("ZeldaBomboniere.it")){
	    		String id = ordineSelezionato.getIdOrdinePiattaforma().replace("ZB_", "");
	    		link = "http://zeldabomboniere.it/admin/?route=sale/order/info&order_id="+id;
	    	}
	    	else if (ordineSelezionato.getPiattaforma().contains("eBay")){
	    		String id = ordineSelezionato.getIdOrdinePiattaforma();
	    		link = "http://k2b-bulk.ebay.it/ws/eBayISAPI.dll?EditSalesRecord&orderid="+id;
	    		
	    	}
	    	else if (ordineSelezionato.getPiattaforma().contains("Amazon")){
	    		String id = ordineSelezionato.getIdOrdinePiattaforma();
	    		link = "https://sellercentral.amazon.it/gp/orders-v2/details/ref=ag_orddet_cont_myo?ie=UTF8&orderID="+id;
	    	}
    	}
    	return link;
    }
    
    String linkTracking;
    
    public String getLinkTracking(){
    	String link = "#";
    	
    	if (ordineSelezionato!=null){
	    	if (ordineSelezionato.getNumeroTracciamento()!=null && !ordineSelezionato.getNumeroTracciamento().isEmpty()){
	    		
//	    		if (ordineSelezionato.getLinkTracciamento()!=null && ordineSelezionato.getLinkTracciamento().isEmpty())
//	    			link = ordineSelezionato.getLinkTracciamento()+ ordineSelezionato.getNumeroTracciamento();
	    		
//	    		else 
		    		link = Methods.cercaLinkTracciamento(ordineSelezionato.getIdCorriere()) + ordineSelezionato.getNumeroTracciamento();
	    	}
    	}
    	return link;
    }
    
    int quantitaOrdineSelezionato;
    
    public int getQuantitaOrdineSelezionato(){
    	int qt = 0;
    	
    	if (ordineSelezionato!=null && ordineSelezionato.getElencoArticoli()!=null)
	    	for (ArticoloAcquistato a : ordineSelezionato.getElencoArticoli()){
	    		qt+=a.getQuantitaAcquistata();
	    	}
    	
    	return qt;
    }
    
    double totaleOrdineSelezionato;
    
    public double getTotaleOrdineSelezionato(){
    	double tot = 0;
        
    	if (ordineSelezionato!=null && ordineSelezionato.getElencoArticoli()!=null)
	    	for (ArticoloAcquistato a : ordineSelezionato.getElencoArticoli()){
	    		tot+=a.getPrezzoTotale();
	    	}
    	
	    tot = Methods.round(tot,2);
	    
    	return tot;
    }
    
    double granTotale = 0;
    
    public double getGranTotale(){
    	double tot = 0;
    	
    	tot = getTotaleOrdineSelezionato();
    	if (ordineSelezionato!=null){
    		tot+=ordineSelezionato.getCostoSpedizione();
	    	
    		if (ordineSelezionato.getMetodoPagamento()!=null && ordineSelezionato.getMetodoPagamento().toLowerCase().contains("contrassegno"))
	    		tot+=3;
    		
	    		tot+=ordineSelezionato.getValoreBuonoSconto();
	    }
    	tot = Methods.round(tot, 2);
    	
    	return tot;
    }
    
	public void archivia(){
		
		int x = Ordine_DAO.archivia(ordineSelezionato.getIdOrdine());
		
		Log.info("Archiviato ordine "+ordineSelezionato.getIdOrdine()+" con risultato: "+x);
		
		//Ordine_DAO.modificaCodaLDV(ordineSelezionato.getIdOrdine(), 0, null); //ora viene fatto nel metodo archivia
		ordiniInCodaLDV.remove(ordineSelezionato);
		
		//reloadOrdini(); vedi sotto
		ordini.remove(ordineSelezionato);
		
		if (x==1) {
			FacesMessage msg = new FacesMessage("Operazione Completata", "Ordine "+ordineSelezionato.getIdOrdine()+" archiviato");  
	        FacesContext.getCurrentInstance().addMessage(null, msg);  
		} else {
			FacesMessage msg = new FacesMessage("Operazione Non Completata", "Si � verificato un errore.");  
	        FacesContext.getCurrentInstance().addMessage(null, msg);  
		}	
	}
    
	public void setFileSpedizioni(StreamedContent fileSpedizioni) {
		this.fileSpedizioni = fileSpedizioni;
	}
	
	public void cambiaCorriere(){
		Log.info("Cambio corriere per l'ordine "+ordineSelezionato.getIdOrdine());
		
		int vecchioCorriere = ordineSelezionato.getIdCorriere();
		int nuovoCorriere = 0;
		
		if (vecchioCorriere==1) nuovoCorriere = 2;
		else if (vecchioCorriere==2) nuovoCorriere = 1;
		
		ordineSelezionato.setIdCorriere(nuovoCorriere);
		ordineSelezionato.setNomeCorriere(Methods.cercaNomeCorriere(nuovoCorriere));
		
		Ordine_DAO.modificaCodaLDV(ordineSelezionato.getIdOrdine(), 1, nuovoCorriere);
	}
	
	public void cambiaTuttiCorrieri(){
		Log.info("Cambio corriere per tutti gli ordini");
		
		for (Ordine o : ordiniInCodaLDV)
		{
			ordineSelezionato.setIdCorriere(2);
			ordineSelezionato.setNomeCorriere("GLS");	
		}
		
		Ordine_DAO.modificaCodaLDV(ordineSelezionato.getIdOrdine(), 1, nuovoCorriere);
	}
	
	public void inviaAcodaLdvCorriere1(){
		inviaAcodaLDV(1);
	}
	
	public void inviaAcodaLdvCorriere2(){
		inviaAcodaLDV(2);
	}
	
	public void inviaAcodaLDV(int corriere){
		
		int x = Ordine_DAO.modificaCodaLDV(ordineSelezionato.getIdOrdine(), 1, corriere);
		
		ordiniInCodaLDV.add(ordineSelezionato);
		
		ordiniFiltratiLDV=null;
		
		//reloadOrdiniInCodaLDV()
		
		Log.info("Inviato ordine "+ordineSelezionato.getIdOrdine()+" a coda LDV con risultato: "+x);
		
		if (x==1) {
			FacesMessage msg = new FacesMessage("Operazione Completata", "Ordine "+ordineSelezionato.getIdOrdine()+" inviato alla coda LDV");  
	        FacesContext.getCurrentInstance().addMessage(null, msg);  
		} else {
			FacesMessage msg = new FacesMessage("Operazione Non Completata", "Si � verificato un errore.");  
	        FacesContext.getCurrentInstance().addMessage(null, msg);  
		}	
	}
	
	public void togliDaCodaLDV(){
		
		int x = Ordine_DAO.modificaCodaLDV(ordineSelezionato.getIdOrdine(), 0, 0);
		
		Log.info("Eliminato ordine "+ordineSelezionato.getIdOrdine()+" da coda LDV con risultato: "+x);
		
		ordiniInCodaLDV.remove(ordineSelezionato);
		
		ordiniFiltratiLDV=null;
		
		if (x==1) {
			FacesMessage msg = new FacesMessage("Operazione Completata", "Ordine "+ordineSelezionato.getIdOrdine()+" eliminato dalla coda LDV");  
	        FacesContext.getCurrentInstance().addMessage(null, msg);  
		} else {
			FacesMessage msg = new FacesMessage("Operazione Non Completata", "Si � verificato un errore.");  
	        FacesContext.getCurrentInstance().addMessage(null, msg);  
		}	
	}
	
	

	public void generaFileLdv(int corriere){
	  	Properties config = new Properties();	   
	  	
	  	String nomeFile = "";
		
			try {
				config.load(Log.class.getResourceAsStream("/zeus.properties"));
				
				String percorsoFile = config.getProperty("percorso_ldv");	
				nomeFile = config.getProperty("nome_ldv");
	    	
				String data = DateMethods.getDataCompletaPerNomeFileTesto();
				
				nomeFile = nomeFile.replace("DATA", data+"_corriere"+corriere);
				
				List<Ordine> ordiniPerCoda = new ArrayList<Ordine>();
				
				if (corriere>0){
					for (Ordine o : ordiniInCodaLDV){
						if (o.getIdCorriere()==corriere)
							ordiniPerCoda.add(o);
					}
				}
				else ordiniPerCoda = ordiniInCodaLDV;
				
				CourierUtility.aggiungiOrdineALDV(ordiniPerCoda,percorsoFile+nomeFile);
		    	
		    	Log.info("Generata LDV per corriere"+corriere+": "+percorsoFile+nomeFile);
		    	
		    	//Ordine_DAO.togliDaCodaLDV(ordiniInCodaLDV);
		    	
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
    	
    	for (Ordine o : ordiniInCodaLDV)
    		System.out.println(o.getIdOrdine());
    	
    	Properties config = new Properties();	   
		
		try {
			config.load(Log.class.getResourceAsStream("/zeus.properties"));
			
			String percorsoFile = config.getProperty("percorso_ldv");	
			String nomeFile = config.getProperty("nome_ldv");
    	
			String data = DateMethods.getDataCompletaPerNomeFileTesto();
			
			nomeFile = nomeFile.replace("DATA", data);
			
			CourierUtility.aggiungiOrdineALDV(ordiniInCodaLDV,percorsoFile+nomeFile);
	    	
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
    		
    		/* scarico da ebay le informazioni sull'articolo (poi dovr� distinguere i casi in cui mi servono o meno, ad esempio se ci sono gi�) */
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
	    	if (ok) /* se l'aggiornamento � andato a buon fine, associo il codice articolo */
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
    	String link;
    	if (articoloSelezionato.getAsin()!=null && !articoloSelezionato.getAsin().isEmpty())
    		link = Costanti.linkArticoloAmazonFrontend+articoloSelezionato.getAsin();
    	else 
    		link = Costanti.ricercaSuAmazonFrontend+(articoloSelezionato.getNome().replace(" ", "+"));
	    	
    	return link;
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
    	ordiniFiltrati = null;
    	ordini = OrdineBusiness.getInstance().reloadOrdini(getMostraDa(),getMostraA(), filtroOrdini);
    }
    
	public List<Ordine> getOrdini() {
//		if (ordini==null){
			ordini = OrdineBusiness.getInstance().getOrdini(getMostraDa(),getMostraA(), filtroOrdini);
//			ordiniFiltrati = null;
//		}
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
		scaricaDa = DateMethods.oraDelleStreghe(scaricaDa);
		scaricaA = DateMethods.ventitreCinquantanove(scaricaA);
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
		return DateMethods.oraDelleStreghe(mostraDa);
	}

	public void setMostraDa(Date mostraDa) {
		this.mostraDa = mostraDa;
	}

	public Date getMostraA() {
		return DateMethods.ventitreCinquantanove(mostraA);
	}

	public void setMostraA(Date mostraA) {
		this.mostraA = mostraA;
	}

	public ArticoloAcquistato getArtDaModificare() {
		if (artDaModificare==null) artDaModificare = new ArticoloAcquistato();
		return artDaModificare;
	}

	public void setArtDaModificare(ArticoloAcquistato artDaModificare) {
		this.artDaModificare = artDaModificare;
	}
	
	public void reloadOrdiniInCodaLDV() {
		ordiniInCodaLDV = OrdineBusiness.getInstance().reloadOrdiniInCodaLDV();
		ordiniFiltratiLDV=null;
	}

	public List<Ordine> getOrdiniInCodaLDV() {
//		if (ordiniInCodaLDV==null){
			ordiniInCodaLDV = OrdineBusiness.getInstance().getOrdiniInCodaLDV();
//			ordiniFiltratiLDV=null;
//		}
		return ordiniInCodaLDV;
	}
	
	public void setOrdiniInCodaLDV(List<Ordine> ordiniInCodaLDV) {
		this.ordiniInCodaLDV = ordiniInCodaLDV;
	}

	public String getFiltroOrdini() {
		return filtroOrdini;
	}

	public void setFiltroOrdini(String filtroOrdini) {
		this.filtroOrdini = filtroOrdini;
	}

	public Date getDataConfermaSpedizioni() {
		return dataConfermaSpedizioni;
	}

	public void setDataConfermaSpedizioni(Date dataConfermaSpedizioni) {
		this.dataConfermaSpedizioni = dataConfermaSpedizioni;
	}

	public List<Ordine> getOrdiniFiltratiLDV() {
		return ordiniFiltratiLDV;
	}

	public void setOrdiniFiltratiLDV(List<Ordine> ordiniFiltratiLDV) {
		this.ordiniFiltratiLDV = ordiniFiltratiLDV;
	}

	public double getTotaleOrdiniInStampa() {
		return Methods.round(totaleOrdiniInStampa,2);
	}

	public int getNumeroOrdiniAmazon() {
		return numeroOrdiniAmazon;
	}

	public int getNumeroOrdiniEbay() {
		return numeroOrdiniEbay;
	}

	public int getNumeroOrdiniZb() {
		return numeroOrdiniZb;
	}

	public int getNumeroOrdiniTotale() {
		return numeroOrdiniTotale;
	}
    

    

 }
