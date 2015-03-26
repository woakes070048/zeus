package it.swb.bean;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.net.ftp.FTPClient;

import it.swb.business.ArticoloBusiness;
import it.swb.business.InserzioniEbayBusiness;
import it.swb.database.Articolo_DAO;
import it.swb.database.DataSource;
import it.swb.database.InserzioniEbay_DAO;
import it.swb.ftp.FTPmethods;
import it.swb.ftp.FTPutil;
import it.swb.log.Log;
import it.swb.model.Articolo;
import it.swb.model.InfoAmazon;
import it.swb.model.InfoEbay;
import it.swb.model.Variante_Articolo;
import it.swb.piattaforme.ebay.EbayController;
import it.swb.utility.EditorDescrizioni;
import it.swb.utility.Methods;

@ManagedBean(name = "inserzioniEbayBean")
@ViewScoped
public class InserzioniEbayBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<Articolo> inserzioni;  
	private List<Articolo> inserzioniFiltrate;  
	private List<Articolo> inserzioniSelezionate;  
	private Articolo inserzioneSelezionata;
	
	private String codiceDaAssociare;
	private boolean associaDimensioni;
	private boolean associaQuantita;
	private boolean associaQuantitaInserzione;
	private boolean associaDescrizione;
	private boolean associaPrezzo;
	private boolean associaImmagini;
	private boolean associaVarianti;
	
	private String codiceDaAssociareVariante;
	private String valoreVarianteDaAssociare;
	private String immagineVarianteDaAssociare;
	private int quantitaVarianteDaAssociare;
	private String tipoVarianteDaAssociare;
	
	private Articolo articoloDaCreare;
	private String creaNome;
	private boolean creaSpedizioneGratis;
	private boolean creaBoxBomboniere;
	private String creaNote;
	
	public void chiudiInserzioni(){
		for (Articolo a : inserzioniSelezionate){
			String idEbayChiudiInserzione = a.getIdEbay();

	    	FacesMessage msg = null;
	    	if (idEbayChiudiInserzione!=null && !idEbayChiudiInserzione.isEmpty()){
	    		Log.info("Chisura inserzione su eBay con ID: "+idEbayChiudiInserzione);
	    		String chiudi = EbayController.chiudiInserzione(idEbayChiudiInserzione);
	    		if (chiudi.equals("ok")) {
	    			msg = new FacesMessage("Chiudi inserzione su eBay", "Inserzione chiusa correttamente");    
	    			Log.info("Inserzione chiusa correttamente");
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
		}
	}
	
	public void riapriInserzioni(){
		
	}
	
	public void eliminaInserzioni(){
		for (Articolo a : inserzioniSelezionate)
			InserzioniEbay_DAO.eliminaInserzione(a.getIdArticolo());
		
		InserzioniEbayBusiness.getInstance().reloadInserzioni();
    	getInserzioni();
    	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Operazione Completata", "Inserzioni eliminate"));  
	}
	
	public void associaAdArticolo(){
		Map<String,Boolean> cosaAssociare = new HashMap<String,Boolean>();
		cosaAssociare.put("dimensioni", associaDimensioni);
		cosaAssociare.put("quantita", associaQuantita);
		cosaAssociare.put("quantita_inserzione", associaQuantitaInserzione);
		cosaAssociare.put("descrizione", associaDescrizione);
		cosaAssociare.put("prezzo", associaPrezzo);
		cosaAssociare.put("immagini", associaImmagini);
		cosaAssociare.put("varianti", associaVarianti);
		
		codiceDaAssociare = codiceDaAssociare.toUpperCase();
		
		int res = InserzioniEbay_DAO.associaArticolo(codiceDaAssociare,inserzioneSelezionata,cosaAssociare);
		
		inserzioneSelezionata.setParoleChiave5(codiceDaAssociare);
		
		InserzioniEbay_DAO.aggiornaCodiceCorrispondente(inserzioneSelezionata.getIdArticolo(),codiceDaAssociare);
		
		ArticoloBusiness.getInstance().reloadAll();
		
		FacesMessage msg = new FacesMessage("Operazione completata", "Inserzione associata all'articolo "+codiceDaAssociare);
		
		if (res!=1) msg = new FacesMessage("Operazione non completata", "Si è verificato un errore.");
		
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	
	public void associaComeVariante(){
		
		Variante_Articolo v = new Variante_Articolo();
		
		codiceDaAssociareVariante = codiceDaAssociareVariante.toUpperCase();
		
		v.setCodiceArticolo(codiceDaAssociareVariante);
		v.setValore(valoreVarianteDaAssociare);
		v.setQuantita(quantitaVarianteDaAssociare);
		v.setImmagine(immagineVarianteDaAssociare);
		v.setTipo(tipoVarianteDaAssociare);
		
		int res = InserzioniEbay_DAO.associaVariante(v,codiceDaAssociareVariante);
		
		ArticoloBusiness.getInstance().reloadAll();
		
		FacesMessage msg = new FacesMessage("Operazione completata", "Inserzione associata come variante dell'articolo "+codiceDaAssociareVariante);
		if (res!=1) msg = new FacesMessage("Operazione non completata", "Si è verificato un errore.");
		
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
	}
	
	public void preparaCreaNuovoArticolo(){
		articoloDaCreare = new Articolo();
		
		articoloDaCreare.setCodice(inserzioneSelezionata.getCodice());
		articoloDaCreare.setDimensioni(inserzioneSelezionata.getDimensioni());
		articoloDaCreare.setQuantitaMagazzino(inserzioneSelezionata.getQuantitaMagazzino());
		articoloDaCreare.setQuantitaInserzione(inserzioneSelezionata.getQuantitaInserzione());
		articoloDaCreare.setDescrizione(inserzioneSelezionata.getDescrizione());
		articoloDaCreare.setPrezzoDettaglio(inserzioneSelezionata.getPrezzoDettaglio());
		articoloDaCreare.setCostoSpedizione(7);
		articoloDaCreare.setImmagine1(inserzioneSelezionata.getImmagine1());
		articoloDaCreare.setImmagine2(inserzioneSelezionata.getImmagine2());
		articoloDaCreare.setImmagine3(inserzioneSelezionata.getImmagine3());
		articoloDaCreare.setImmagine4(inserzioneSelezionata.getImmagine4());
		articoloDaCreare.setImmagine5(inserzioneSelezionata.getImmagine5());
		if (inserzioneSelezionata.getCategoria()!=null)
			articoloDaCreare.setIdCategoria(inserzioneSelezionata.getCategoria().getIdCategoria());
		
		articoloDaCreare.setInfoEbay(inserzioneSelezionata.getInfoEbay()); 
		
		
		//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Operazione non completata", "xxx."));
	}
	
	public void creaNuovoArticolo(){
		Log.info("Salvataggio informazioni articolo "+articoloDaCreare.getCodice()); 
		
		//articoloDaCreare.setParoleChiave5(articoloDaCreare.getCodice());
		articoloDaCreare.setNome(creaNome);
		articoloDaCreare.setPrezzoPiattaforme(articoloDaCreare.getPrezzoDettaglio());
		if (creaSpedizioneGratis) articoloDaCreare.setCostoSpedizione(0);
		articoloDaCreare.setAliquotaIva(22);
		articoloDaCreare.setQuantitaEffettiva(articoloDaCreare.getQuantitaMagazzino());
		
		InfoEbay ie = articoloDaCreare.getInfoEbay();
		
		ie.setDurataInserzione(999);
		ie.setAmbiente("produzione");
		ie.setContrassegno(true);
		ie.setBoxBomboniere(creaBoxBomboniere);
		
		articoloDaCreare.setInfoEbay(ie);
		
		articoloDaCreare.setInfoAmazon(new InfoAmazon());
		
		InserzioniEbay_DAO.aggiornaCodiceCorrispondente(articoloDaCreare.getIdArticolo(), articoloDaCreare.getCodice());
		
		inserzioneSelezionata.setParoleChiave3("1");
		inserzioneSelezionata.setParoleChiave5(articoloDaCreare.getCodice());
		
		long id_articolo = ArticoloBusiness.getInstance().inserisciOModificaArticolo(articoloDaCreare);
		
		String risultato_inserimento_locale;
		
		if (id_articolo>0)
			risultato_inserimento_locale = "Articolo salvato correttamente nel database di Zeus.";
		else risultato_inserimento_locale = "!!! Articolo NON salvato nel database di Zeus, si è verificato qualche problema. Controllare i log.";
		
		Log.info(risultato_inserimento_locale);
		
		Articolo_DAO.setPresenzaSu(articoloDaCreare.getCodice(), "ebay", 1, null);
		ArticoloBusiness.getInstance().reloadAll();
		
		scaricaECreaThumbnail(articoloDaCreare.getImmagine1());
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Operazione Completata",risultato_inserimento_locale));  
	}
	
	private void scaricaECreaThumbnail(String immagine1) {
		Log.info("scaricaECreaThumbnail: ");
		FTPClient f = FTPutil.getConnection();

		if (Methods.controlloSintassiImmagine(immagine1)) 
			FTPmethods.creaThumbnailsEcaricaSuFtp(immagine1,true, f);
		
		FTPutil.closeConnection(f);
	}
	
	public void preparaModificaInserzione(){
		
	}
	
	public void modificaInserzione(){
		InserzioniEbay_DAO.modificaInserzione(inserzioneSelezionata);
		ArticoloBusiness.getInstance().reloadAll();
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Operazione Completata","Informazioni salvate"));  
	}
	
    public void chiudiInserzione(){
    	String idEbayChiudiInserzione = inserzioneSelezionata.getIdEbay();

    	FacesMessage msg = null;
    	if (idEbayChiudiInserzione!=null && !idEbayChiudiInserzione.isEmpty()){
    		Log.info("Chisura inserzione su eBay con ID: "+idEbayChiudiInserzione);
    		String chiudi = EbayController.chiudiInserzione(idEbayChiudiInserzione);
    		if (chiudi.equals("ok")) {
    			msg = new FacesMessage("Chiudi inserzione su eBay", "Inserzione chiusa correttamente");    
    			Log.info("Inserzione chiusa correttamente");
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
    }
    
    public void riapriInserzione(){
    	Connection con = null;		
		PreparedStatement ps = null;
		Connection con1 = null;
		PreparedStatement ps1 = null;
		
		FacesMessage msg = new FacesMessage("Riapri inserzione", "Inserzione riaperta correttamente");
	
		try{
			con = DataSource.getTestConnection();
			con1 = DataSource.getLocalConnection();
			
			String codice_articolo_corrispondente = inserzioneSelezionata.getParoleChiave5();
			
			if (codice_articolo_corrispondente==null || codice_articolo_corrispondente.isEmpty())
			{
				msg = new FacesMessage("Riapri inserzione", "Codice articolo corrispondente mancante");
			}
			else {
				Articolo a = Articolo_DAO.getArticoloByCodice(codice_articolo_corrispondente,null);
				
				if (a.getInfoEbay()==null) {
					Map<String,Boolean> cosaAssociare = new HashMap<String,Boolean>();
					cosaAssociare.put("dimensioni", false);
					cosaAssociare.put("quantita", false);
					cosaAssociare.put("quantita_inserzione", false);
					cosaAssociare.put("descrizione", false);
					cosaAssociare.put("prezzo", false);
					cosaAssociare.put("immagini", false);
					cosaAssociare.put("varianti", false);
					
					InserzioniEbay_DAO.associaArticolo(codice_articolo_corrispondente,inserzioneSelezionata,cosaAssociare);
					
					a = Articolo_DAO.getArticoloByCodice(codice_articolo_corrispondente,null);
				}
				
				String desc = EditorDescrizioni.creaDescrizioneEbay(a);
				if (desc!=null && !desc.isEmpty()){
					a.getInfoEbay().setDescrizioneEbay(desc);
				}
		    	String x[] = EbayController.creaInserzione(a);
				String ok = x[0];
				String nuovoid = x[1];
				
				if (ok.equals("1") && nuovoid.length()<15){
					
					inserzioneSelezionata.setParoleChiave3("1");
					inserzioneSelezionata.setParoleChiave4(nuovoid);
					
					System.out.println("Vecchia inserzione: www.ebay.it/itm/"+inserzioneSelezionata.getIdEbay());
					System.out.println("Nuova inserzione: www.ebay.it/itm/"+nuovoid);
					
					String query = "update inserzioni_ebay set parole_chiave_3=1, parole_chiave_4=? where id_articolo=?";
					ps = con.prepareStatement(query);
					ps.setString(1, nuovoid);
					ps.setLong(2, inserzioneSelezionata.getIdArticolo());
					
					ps.setString(1, inserzioneSelezionata.getCodice());
					ps.executeUpdate();
					
					String query1 = "update articoli set id_ebay=? where codice=?";
					ps1 = con1.prepareStatement(query1);
				
					ps1.setString(1, nuovoid);
					ps1.setString(2, inserzioneSelezionata.getParoleChiave5());
					
					ps1.executeUpdate();
					
					con1.commit();
					con.commit();
					
				}
				else msg = new FacesMessage("Riapri inserzione", "Si è verificato un errore. Controllare i log.");
			}
			
			
		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
			msg = new FacesMessage("Riapri inserzione", "Si è verificato un errore.");
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,null);
			 DataSource.closeConnections(con1,null,ps1,null);
		}		
		FacesContext.getCurrentInstance().addMessage(null, msg);  
    }
    
    public void eliminaInserzione(){
    	InserzioniEbay_DAO.eliminaInserzione(inserzioneSelezionata.getIdArticolo());
    	InserzioniEbayBusiness.getInstance().reloadInserzioni();
    	getInserzioni();
    	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Operazione Completata", "Inserzione eliminata"));  
    }
	
	public List<Articolo> getInserzioni() {
		if (inserzioni==null)
			inserzioni = InserzioniEbayBusiness.getInstance().getInserzioni();
		return inserzioni;
	}
    
	public void reloadInserzioni(){
		InserzioniEbayBusiness.getInstance().reloadInserzioni();
	}

	public void setInserzioni(List<Articolo> inserzioni) {
		this.inserzioni = inserzioni;
	}

	public void setInserzioniFiltrate(List<Articolo> inserzioniFiltrate) {
		this.inserzioniFiltrate = inserzioniFiltrate;
	}
	
	public List<Articolo> getInserzioniFiltrate() {
		return inserzioniFiltrate;
	}
	
    public List<Articolo> getInserzioniSelezionate() {  
        return inserzioniSelezionate;  
    }  
    public void setInserzioniSelezionate(List<Articolo> inserzioniSelezionate) {  
        this.inserzioniSelezionate = inserzioniSelezionate;  
    }

	public Articolo getInserzioneSelezionata() {
		if (inserzioneSelezionata==null)
			inserzioneSelezionata = new Articolo();
		return inserzioneSelezionata;
	}

	public void setInserzioneSelezionata(Articolo inserzioneSelezionata) {
		this.inserzioneSelezionata = inserzioneSelezionata;
	}

	public String getCodiceDaAssociare() {
		return codiceDaAssociare;
	}

	public void setCodiceDaAssociare(String codiceDaAssociare) {
		this.codiceDaAssociare = codiceDaAssociare;
	}

	public String getCreaNome() {
		return creaNome;
	}

	public void setCreaNome(String creaNome) {
		this.creaNome = creaNome;
	}

	public boolean isAssociaDimensioni() {
		return associaDimensioni;
	}

	public void setAssociaDimensioni(boolean associaDimensioni) {
		this.associaDimensioni = associaDimensioni;
	}

	public boolean isAssociaQuantita() {
		return associaQuantita;
	}

	public void setAssociaQuantita(boolean associaQuantita) {
		this.associaQuantita = associaQuantita;
	}

	public boolean isAssociaQuantitaInserzione() {
		return associaQuantitaInserzione;
	}

	public void setAssociaQuantitaInserzione(boolean associaQuantitaInserzione) {
		this.associaQuantitaInserzione = associaQuantitaInserzione;
	}

	public boolean isAssociaDescrizione() {
		return associaDescrizione;
	}

	public void setAssociaDescrizione(boolean associaDescrizione) {
		this.associaDescrizione = associaDescrizione;
	}

	public boolean isAssociaPrezzo() {
		return associaPrezzo;
	}

	public void setAssociaPrezzo(boolean associaPrezzo) {
		this.associaPrezzo = associaPrezzo;
	}

	public boolean isAssociaVarianti() {
		return associaVarianti;
	}

	public void setAssociaVarianti(boolean associaVarianti) {
		this.associaVarianti = associaVarianti;
	}

	public boolean isAssociaImmagini() {
		return associaImmagini;
	}

	public void setAssociaImmagini(boolean associaImmagini) {
		this.associaImmagini = associaImmagini;
	}

	public Articolo getArticoloDaCreare() {
		if (articoloDaCreare==null)
			articoloDaCreare = new Articolo();
		return articoloDaCreare;
	}

	public void setArticoloDaCreare(Articolo articoloDaCreare) {
		this.articoloDaCreare = articoloDaCreare;
	}

	public String getCreaNote() {
		return creaNote;
	}

	public void setCreaNote(String creaNote) {
		this.creaNote = creaNote;
	}

	public boolean isCreaBoxBomboniere() {
		return creaBoxBomboniere;
	}

	public void setCreaBoxBomboniere(boolean creaBoxBomboniere) {
		this.creaBoxBomboniere = creaBoxBomboniere;
	}

	public boolean isCreaSpedizioneGratis() {
		return creaSpedizioneGratis;
	}

	public void setCreaSpedizioneGratis(boolean creaSpedizioneGratis) {
		this.creaSpedizioneGratis = creaSpedizioneGratis;
	}

	public String getImmagineVarianteDaAssociare() {
		return immagineVarianteDaAssociare;
	}

	public void setImmagineVarianteDaAssociare(
			String immagineVarianteDaAssociare) {
		this.immagineVarianteDaAssociare = immagineVarianteDaAssociare;
	}

	public int getQuantitaVarianteDaAssociare() {
		return quantitaVarianteDaAssociare;
	}

	public void setQuantitaVarianteDaAssociare(int quantitaVarianteDaAssociare) {
		this.quantitaVarianteDaAssociare = quantitaVarianteDaAssociare;
	}

	public String getValoreVarianteDaAssociare() {
		return valoreVarianteDaAssociare;
	}

	public void setValoreVarianteDaAssociare(String valoreVarianteDaAssociare) {
		this.valoreVarianteDaAssociare = valoreVarianteDaAssociare;
	}

	public String getTipoVarianteDaAssociare() {
		return tipoVarianteDaAssociare;
	}

	public void setTipoVarianteDaAssociare(String tipoVarianteDaAssociare) {
		this.tipoVarianteDaAssociare = tipoVarianteDaAssociare;
	}

	public String getCodiceDaAssociareVariante() {
		return codiceDaAssociareVariante;
	}

	public void setCodiceDaAssociareVariante(String codiceDaAssociareVariante) {
		this.codiceDaAssociareVariante = codiceDaAssociareVariante;
	}

  }
