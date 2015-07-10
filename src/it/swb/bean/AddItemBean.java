package it.swb.bean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import it.swb.business.ArticoloBusiness;
import it.swb.business.CategorieBusiness;
import it.swb.database.Articolo_DAO;
import it.swb.ftp.FTPmethods;
import it.swb.log.Log;
import it.swb.model.Articolo;
import it.swb.model.Categoria;
import it.swb.model.CategoriaAmazon;
import it.swb.model.CategoriaEbay;
import it.swb.model.InfoAmazon;
import it.swb.model.InfoEbay;
import it.swb.model.Variante_Articolo;
import it.swb.utility.Methods;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.event.FileUploadEvent;

@ManagedBean(name = "additemBean")
@SessionScoped
public class AddItemBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static AddItemBean instance;

	public static synchronized AddItemBean getInstance() {
		if (instance == null) {
			synchronized (AddItemBean.class) { // 1
				AddItemBean inst = instance; // 2
				if (inst == null) {
					synchronized (AddItemBean.class) { // 3
						instance = new AddItemBean();
					}
					// instance = inst; //5
				}
			}
		}
		return instance;
	}

	private String codiceArticoloDaCaricare;	

	// INFORMAZIONI GENERALI
	private Articolo artDaInserzionare;
	
	
	private List<Variante_Articolo> varianti;

	private List<Categoria> categorie;
	
	private boolean creaThumbnails = true;
	
	private boolean invia_a_gm = true;
	private boolean invia_ad_ebay = true;
	private boolean invia_ad_amazon = true;
	private boolean invia_a_zb = true;
	
	// INFORMAZIONI EBAY
	private InfoEbay informazioniEbay;

	private List<CategoriaEbay> categorieEbay;


	// INFORMAZIONI AMAZON
	private InfoAmazon informazioniAmazon;
	
	private List<CategoriaAmazon> categorieAmazon;

	private Map<String,String> messaggi;
	
	public AddItemBean() {
	}

	public void save(ActionEvent actionEvent) {
		// Persist user
		showMessage("Successful", "Inserito");
	}
	
	
	public void handleFileUpload(FileUploadEvent event) {
		Properties config = new Properties();	      
        try {
        	config.load(Log.class.getResourceAsStream("/zeus.properties"));
        	
            File targetFolder = new File(config.getProperty("percorso_upload_file_ordini"));
            String nomeFile = event.getFile().getFileName();
            InputStream inputStream = event.getFile().getInputstream();
            File f = new File(targetFolder,nomeFile);
            
            if (!f.exists()) f.createNewFile();
            OutputStream out = new FileOutputStream(f);
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            inputStream.close();
            out.flush();
            out.close();
            
            showMessage("Operazione completata", nomeFile + " è stato caricato.");  
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public void preparaArticolo(){
		
		
		artDaInserzionare.setCategoria(CategorieBusiness.getInstance().getMappaCategorie().get(artDaInserzionare.getIdCategoria()));
		artDaInserzionare.setCategoria2(CategorieBusiness.getInstance().getMappaCategorie().get(artDaInserzionare.getIdCategoria2()));
		
		informazioniEbay.setNomeCategoria1(CategorieBusiness.getInstance().getMappaCategorieEbay().get(informazioniEbay.getIdCategoria1()));
		informazioniEbay.setNomeCategoria2(CategorieBusiness.getInstance().getMappaCategorieEbay().get(informazioniEbay.getIdCategoria2()));
		
		informazioniAmazon.setNomeCategoria1(CategorieBusiness.getInstance().getMappaCategorieAmazon().get(informazioniAmazon.getIdCategoria1()));
		informazioniAmazon.setNomeCategoria2(CategorieBusiness.getInstance().getMappaCategorieAmazon().get(informazioniAmazon.getIdCategoria2()));
		
	}

	public void salvaArticolo(){
		
		Log.info("Salvataggio informazioni articolo "+artDaInserzionare.getCodice()); 
							
		artDaInserzionare.setDataUltimaModifica(new Date(new java.util.Date().getTime()));

		/*	Scheda info generali	*/
		artDaInserzionare.setCorrelati(Methods.getCorrelati(artDaInserzionare.getNote2()));
		
		if (artDaInserzionare.getCorrelati()!=null)
			for (Articolo a : artDaInserzionare.getCorrelati()){
				System.out.println(a.getCodice());
			}
		
		/*	Scheda categorie & media	*/
		controlloSintassiImmagini();
		
		/*	eBay	*/
		if (artDaInserzionare.getCategoria()!=null && artDaInserzionare.getCategoria().getIdCategoriaPrincipale()==109)
			informazioniEbay.setIdCategoria1("13273");
		artDaInserzionare.setInfoEbay(informazioniEbay);
		
		/*	Amazon	*/		
		artDaInserzionare.setInfoAmazon(informazioniAmazon);
		
		/*	Varianti	*/
		artDaInserzionare.setVarianti(varianti);		
		
		int id_articolo = ArticoloBusiness.getInstance().inserisciOModificaArticolo(artDaInserzionare);
		artDaInserzionare.setIdArticolo(id_articolo);
		
		if (id_articolo>0)
			messaggi.put("risultato_salvataggio", "Articolo salvato correttamente nel database di Zeus.");
		else messaggi.put("risultato_salvataggio", "!!! Articolo NON salvato nel database di Zeus, si è verificato qualche problema. Controllare i log.");
		
		Log.info(messaggi.get("risultato_salvataggio"));
		
		ArticoloBusiness.getInstance().reloadArticoli();
		
		showMessage("Operazione Completata",messaggi.get("risultato_salvataggio"));  
	}
	
	public void pubblicaInserzioniSubito() {
		
		if (artDaInserzionare.getCategoria()==null){
			artDaInserzionare.setCategoria(CategorieBusiness.getInstance().getMappaCategorie().get(artDaInserzionare.getIdCategoria()));
		}

		salvaArticolo();
		Log.info("Pubblicazione inserzioni articolo con codice: " + artDaInserzionare.getCodice() + " e ID: " + artDaInserzionare.getIdArticolo());
		
		artDaInserzionare.setDataUltimaModifica(new Date(new java.util.Date().getTime()));

		if (creaThumbnails) FTPmethods.creaThumbnails(artDaInserzionare);
		
		Map<String,Boolean> piattaforme = new HashMap<String,Boolean>();
		piattaforme.put("ebay", invia_ad_ebay);
		piattaforme.put("zb", invia_a_zb);
		piattaforme.put("gm", invia_a_gm);
		piattaforme.put("amazon", invia_ad_amazon);
		
		Map<String,Map<String,String>> risultati = ArticoloBusiness.getInstance().pubblicaInserzioni(artDaInserzionare, piattaforme);
		
		Map<String, String> ebay = risultati.get("ebay");
		Map<String, String> zb = risultati.get("zb");
		Map<String, String> gm = risultati.get("gm");
		Map<String, String> amazon = risultati.get("amazon");
		
		messaggi = new HashMap<String, String>();
		
		if (ebay!=null){
			messaggi.put("risultato_pubblicazione_ebay", ebay.get("pubblicato"));
			messaggi.put("link_ebay", ebay.get("link"));
			messaggi.put("errore_ebay", ebay.get("errore"));
		}
		if (gm!=null){
			messaggi.put("risultato_pubblicazione_gm", gm.get("pubblicato"));
			messaggi.put("link_gm", gm.get("link"));
		}
		
		if (zb!=null){
			messaggi.put("risultato_pubblicazione_zb", zb.get("pubblicato"));
			messaggi.put("link_zb", zb.get("link"));
			
		}
		if (amazon!=null){
			messaggi.put("risultato_pubblicazione_amazon", amazon.get("pubblicato"));
		}
	}
	
	public void mettiInCoda(){
		
		salvaArticolo();
		
		if (invia_ad_ebay) artDaInserzionare.setPresente_su_ebay(-1);
		if (invia_a_zb) artDaInserzionare.setPresente_su_zb(-1);
		if (invia_a_gm) artDaInserzionare.setPresente_su_gm(-1);
		if (invia_ad_amazon) artDaInserzionare.setPresente_su_amazon(-1);
		
		Articolo_DAO.setPresenze(artDaInserzionare);
		
		int result = ArticoloBusiness.getInstance().salvaArticoloInCodaInserzioni(artDaInserzionare);
		
		messaggi = new HashMap<String, String>();
		
		if (result==1) { 
			messaggi.put("articolo_in_coda", "Articolo messo in coda. Le inserzioni verranno pubblicate automaticamente.");
		} else 
			messaggi.put("articolo_in_coda", "Articolo NON messo in coda. Si è verificato qualche problema.");
	}





	public void showMessage(String titolo, String messaggio) {
		FacesMessage message = new FacesMessage(titolo,messaggio);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}


	public void caricaDatiArticolo() {
		Log.info("Caricamento dati articolo: " + codiceArticoloDaCaricare);
		
		artDaInserzionare = Articolo_DAO.getArticoloByCodice(codiceArticoloDaCaricare, null);
		
		if (!Methods.controlloSintassiImmagine(artDaInserzionare.getImmagine1())) {
			String cartella = "";
			if  (artDaInserzionare.getCategoria().getIdCategoriaPrincipale()==7) cartella = "contenitori";
			else if  (artDaInserzionare.getCategoria().getIdCategoriaPrincipale()==11) cartella = "arredo";
			else if  (artDaInserzionare.getIdCategoria()==35) cartella = "buste";
			else if  (artDaInserzionare.getIdCategoria()==43) cartella = "fiorellini-complementi";
			else if  (artDaInserzionare.getIdCategoria()==47) cartella = "astucci-portaconfetti";
			else if  (artDaInserzionare.getIdCategoria()==58) cartella = "fiori-da-composizione";
			else if  (artDaInserzionare.getIdCategoria()==74) cartella = "animaletti";
			else if  (artDaInserzionare.getIdCategoria()==77) cartella = "cuori";
			else if  (artDaInserzionare.getIdCategoria()==80) cartella = "oggettistica";
			else if  (artDaInserzionare.getIdCategoria()==104) cartella = "decorazioni-addobbi";
			else if  (artDaInserzionare.getIdCategoria()==106 || artDaInserzionare.getIdCategoria()==107) cartella = "accessori";
			
			
			
			artDaInserzionare.setImmagine1(cartella+"/"+artDaInserzionare.getCodice().toLowerCase()+".jpg");
			artDaInserzionare.setImmagine2(cartella+"/"+artDaInserzionare.getCodice().toLowerCase()+"-1.jpg");
		}
		
		if (artDaInserzionare.getQuantitaMagazzino()==0) artDaInserzionare.setQuantitaMagazzino(100);

		artDaInserzionare.setParoleChiave1("bomboniere per matrimonio nozze sposi anniversario 25esimo oro argento confetti");
		artDaInserzionare.setParoleChiave2("bomboniere per cresima battesimo comunione nascita laurea bambino bambini neonato neonati confetti");
		
		creaThumbnails = true;

		hideVariante();
		varianti = artDaInserzionare.getVarianti();

		// INFORMAZIONI EBAY
		invia_ad_ebay = true;
		if (artDaInserzionare.getInfoEbay()!=null) {
			informazioniEbay = artDaInserzionare.getInfoEbay();
			if (informazioniEbay.getTitoloInserzione()==null || !informazioniEbay.getTitoloInserzione().trim().isEmpty())
				informazioniEbay.setTitoloInserzione(artDaInserzionare.getNome().toUpperCase());
		}
		else {
			informazioniEbay = new InfoEbay();
			informazioniEbay.setTitoloInserzione(artDaInserzionare.getNome().toUpperCase());
		}
		
		// INFORMAZIONI AMAZON
		invia_ad_amazon = true;
		if (artDaInserzionare.getInfoAmazon() != null) {
			informazioniAmazon = artDaInserzionare.getInfoAmazon();
			
			if (informazioniAmazon.getVocePacchettoQuantita()==0)
				informazioniAmazon.setVocePacchettoQuantita(1);
			
			if (informazioniAmazon.getNumeroPezzi()==0)
				informazioniAmazon.setNumeroPezzi(1);
			
			if (informazioniAmazon.getQuantitaMassimaSpedizioneCumulativa()==0)
				informazioniAmazon.setQuantitaMassimaSpedizioneCumulativa(100);
			
			if (informazioniAmazon.getIdCategoria1()==null || informazioniAmazon.getIdCategoria1().trim().isEmpty()){
				informazioniAmazon.setIdCategoria1("731697031");
				informazioniAmazon.setIdCategoria2("2906926031");
			}
		}
		else informazioniAmazon = new InfoAmazon();

		invia_a_zb = true;
		invia_a_gm = true;
		
		if (messaggi!=null) messaggi.clear();
	}

	public void caricaDatiArticoloSimile() {
		Log.info("Caricamento dati per creazione articolo simile: "	+ codiceArticoloDaCaricare);
		
		artDaInserzionare = Articolo_DAO.getArticoloByCodice(codiceArticoloDaCaricare, null);
		
		artDaInserzionare.setCodice("");

		creaThumbnails = true;

		varianti = artDaInserzionare.getVarianti();

		// INFORMAZIONI EBAY
		if (artDaInserzionare.getInfoEbay()!=null) 	informazioniEbay = artDaInserzionare.getInfoEbay();
		else informazioniEbay = new InfoEbay();

		// INFORMAZIONI AMAZON
		if (artDaInserzionare.getInfoAmazon() != null) informazioniAmazon = artDaInserzionare.getInfoAmazon();
		else informazioniAmazon = new InfoAmazon();

		if (messaggi!=null) messaggi.clear();
	}

	public void caricaDatiTest() {

		int x = (int) (Math.random() * 100);
		artDaInserzionare.setCodice("TEST_" + x);
		artDaInserzionare.setCodiceFornitore("XXX");
		artDaInserzionare.setCodiceArticoloFornitore("XXX_" + x);
		artDaInserzionare.setCodiceBarre("10000000000" + x);
		artDaInserzionare.setTipoCodiceBarre("EAN-13");
		artDaInserzionare.setNome("ARTICOLO TEST " + x);
		artDaInserzionare.setPrezzoDettaglio(99.99);
		artDaInserzionare.setPrezzoIngrosso(5.55);
		artDaInserzionare.setPrezzoPiattaforme(99.99);
		artDaInserzionare.setCostoSpedizione(7);
		artDaInserzionare.setCostoAcquisto(0);
		artDaInserzionare.setAliquotaIva(21);
		artDaInserzionare.setQuantitaEffettiva(99);
		artDaInserzionare.setQuantitaMagazzino(99);
		artDaInserzionare.setQuantitaInserzione("Una confezione da 1 pezzo");
		artDaInserzionare.setDimensioni("20,0 cm 8,0 cm 8,0 cm");
		artDaInserzionare.setDescrizioneBreve("coppia di test studiosi.");
		artDaInserzionare.setDescrizione("coppia di test studiosi dai colori pastello chiaro. nuovo.");
		
		artDaInserzionare.setIdCategoria(80);
		artDaInserzionare.setIdCategoria2(79);
		artDaInserzionare.setCategoria(CategorieBusiness.getInstance().getMappaCategorie().get(80));
		artDaInserzionare.setCategoria2(CategorieBusiness.getInstance().getMappaCategorie().get(79));
		
		
		artDaInserzionare.setImmagine1("oggettistica/ob234");
		artDaInserzionare.setImmagine2("oggettistica/ob234-1");
		artDaInserzionare.setImmagine3("");
		artDaInserzionare.setImmagine4("");
		artDaInserzionare.setImmagine5("");
		creaThumbnails = false;

		 varianti = new ArrayList<Variante_Articolo>();
		 Variante_Articolo v1 = new
		 Variante_Articolo("Colore","Azzurro","oggettistica/ob234.jpg",15);
		 varianti.add(v1);
		 Variante_Articolo v2 = new
		 Variante_Articolo("Colore","Rosa","oggettistica/ob234-6.jpg",20);
		 varianti.add(v2);
		 
		 //artDaInserzionare.setVarianti(varianti);

		// INFORMAZIONI GLORIAMORALDI.IT
		invia_a_gm = false;

		// INFORMAZIONI EBAY
		invia_ad_ebay = false;
		
		informazioniEbay = new InfoEbay();
		
		informazioniEbay.setTitoloInserzione("TEST_" + x+" OGGETTISTICA");

		artDaInserzionare.setInfoEbay(informazioniEbay);
		
		// INFORMAZIONI AMAZON
		invia_ad_amazon = false;
		
		informazioniAmazon = new InfoAmazon();
		
		informazioniAmazon.setIdCategoria1("731676031");
		informazioniAmazon.setIdCategoria2("652530031");
		informazioniAmazon.setQuantitaMassimaSpedizioneCumulativa(100);
		informazioniAmazon.setNumeroPezzi(1);
		informazioniAmazon.setVocePacchettoQuantita(1);
		
		artDaInserzionare.setParoleChiave1("LAMPADA CANDELA SOSPENSIONE APPESA");
		artDaInserzionare.setParoleChiave2("METALLO VETRO GRIGIO FORATO MOLLA");
		artDaInserzionare.setParoleChiave3("CASA ARREDAMENTO GIARDINO ILLUMINAZIONE");
		artDaInserzionare.setParoleChiave4("ALLESTIMENTI MATRIMONIO NOZZE");
		artDaInserzionare.setParoleChiave5("CERA PARAFFINA GANCIO CAPPA");

		artDaInserzionare.setInfoAmazon(informazioniAmazon);

		invia_a_zb = false;

		if (messaggi!=null) messaggi.clear();
	}

	public void SvuotaCampi() {
		Log.debug("SvuotaCampi");

		codiceArticoloDaCaricare = "";
		artDaInserzionare = new Articolo();
		
		varianti = new ArrayList<Variante_Articolo>();
		hideVariante();
		creaThumbnails = false;

		creaThumbnails = false;

		// INFORMAZIONI GLORIAMORALDI.IT
		invia_a_gm = false;

		// INFORMAZIONI EBAY
		invia_ad_ebay = false;
		informazioniEbay = new InfoEbay();

		// INFORMAZIONI AMAZON
		invia_ad_amazon = false;
		informazioniAmazon = new InfoAmazon();
		
		invia_a_zb = false;

		if (messaggi!=null) messaggi.clear();
	}

	
	/** Se le immagini non sono nulle o vuote le trasforma in minuscolo  */
	private void controlloSintassiImmagini(){
		if (Methods.controlloSintassiImmagine(artDaInserzionare.getImmagine1())) artDaInserzionare.setImmagine1(Methods.trimAndToLower(artDaInserzionare.getImmagine1()));
		if (Methods.controlloSintassiImmagine(artDaInserzionare.getImmagine2())) artDaInserzionare.setImmagine2(Methods.trimAndToLower(artDaInserzionare.getImmagine2()));
		if (Methods.controlloSintassiImmagine(artDaInserzionare.getImmagine3())) artDaInserzionare.setImmagine3(Methods.trimAndToLower(artDaInserzionare.getImmagine3()));
		if (Methods.controlloSintassiImmagine(artDaInserzionare.getImmagine4())) artDaInserzionare.setImmagine4(Methods.trimAndToLower(artDaInserzionare.getImmagine4()));
		if (Methods.controlloSintassiImmagine(artDaInserzionare.getImmagine5())) artDaInserzionare.setImmagine5(Methods.trimAndToLower(artDaInserzionare.getImmagine5()));
		
		if (varianti!=null && !varianti.isEmpty())
			for (Variante_Articolo v : varianti)	
				if (Methods.controlloSintassiImmagine(v.getImmagine())) 
					v.setImmagine(Methods.trimAndToLower(v.getImmagine()));				
	}
	

	
	/***** GESTIONE DELLE VARIANTI ******/

	private String tipoVariante;
	private String valoreVariante;
	private String codiceBarreVariante;
	private String dimensioniVariante;
	private int quantitaVariante;
	private String immagineVariante;

	private boolean mostraVariante = false;

	public void creaVariante() {
		if (varianti == null || varianti.isEmpty())
			varianti = new ArrayList<Variante_Articolo>();

		Log.debug("creaVariante: " + valoreVariante);
		Variante_Articolo v = new Variante_Articolo();
		v.setTipo(tipoVariante);
		v.setValore(valoreVariante);
		v.setCodiceBarre(codiceBarreVariante);
		v.setDimensioni(dimensioniVariante);
		v.setQuantita(quantitaVariante);

		if (immagineVariante.equals(".JPG"))
			immagineVariante = "";

		
		if (immagineVariante!=null && !immagineVariante.trim().isEmpty()) {
			immagineVariante=immagineVariante.toLowerCase();
			if (!immagineVariante.contains(".jpg"))
				immagineVariante = immagineVariante+".jpg";
		}
		v.setImmagine(immagineVariante);
		
		varianti.add(v);

		valoreVariante = "";
		immagineVariante = immagineVariante.replace(".jpg", "");
		// quantitaVariante = 0;
		codiceBarreVariante = "";
		//dimensioniVariante = "";
	}

	public void showVariante() {
		Log.debug("showVariante");
		mostraVariante = true;
	}

	public void hideVariante() {
		Log.debug("hideVariante");
		mostraVariante = false;
		tipoVariante = "";
		valoreVariante = "";
		immagineVariante = "";
		quantitaVariante = 0;
		codiceBarreVariante = "";
		dimensioniVariante = "";
	}

	public void eliminaVariante() {
		Log.debug("eliminaVariante: " + valoreVariante);
		if (varianti != null || !varianti.isEmpty()) {
			List<Variante_Articolo> variantiTemp = new ArrayList<Variante_Articolo>();

			for (Variante_Articolo v : varianti) {
				if (!v.getValore().equals(valoreVariante))
					variantiTemp.add(v);
			}
			varianti = variantiTemp;
			valoreVariante = "";
		}
	}

	public void modificaVariante() {
		Log.debug("eliminaVariante: " + valoreVariante);
		if (varianti != null || !varianti.isEmpty()) {
			List<Variante_Articolo> variantiTemp = new ArrayList<Variante_Articolo>();

			for (Variante_Articolo v : varianti) {
				if (!v.getValore().equals(valoreVariante))
					variantiTemp.add(v);
			}
			varianti = variantiTemp;
			// valoreVariante = "";
		}
	}

	public boolean isInvia_ad_ebay() {
		return invia_ad_ebay;
	}

	public void setInvia_ad_ebay(boolean invia_ad_ebay) {
		this.invia_ad_ebay = invia_ad_ebay;
	}

	public boolean isInvia_a_gm() {
		return invia_a_gm;
	}

	public void setInvia_a_gm(boolean invia_a_gm) {
		this.invia_a_gm = invia_a_gm;
	}


	public List<Categoria> getCategorie() {
		if (categorie == null || categorie.isEmpty())
			categorie = CategorieBusiness.getInstance().getCategorie();
		return categorie;
	}

	public void setCategorie(List<Categoria> categorie) {
		this.categorie = categorie;
	}


	public List<Variante_Articolo> getVarianti() {
		return varianti;
	}

	public void setVarianti(List<Variante_Articolo> varianti) {
		this.varianti = varianti;
	}


	public boolean isCreaThumbnails() {
		return creaThumbnails;
	}

	public void setCreaThumbnails(boolean creaThumbnails) {
		this.creaThumbnails = creaThumbnails;
	}


	public boolean isInvia_ad_amazon() {
		return invia_ad_amazon;
	}

	public void setInvia_ad_amazon(boolean invia_ad_amazon) {
		this.invia_ad_amazon = invia_ad_amazon;
	}


	public List<CategoriaAmazon> getCategorieAmazon() {
		if (categorieAmazon == null || categorieAmazon.isEmpty())
			categorieAmazon = CategorieBusiness.getInstance()
					.getCategorieAmazon();
		return categorieAmazon;
	}

	public void setCategorieAmazon(List<CategoriaAmazon> categorieAmazon) {
		this.categorieAmazon = categorieAmazon;
	}



	public String getTipoVariante() {
		return tipoVariante;
	}

	public void setTipoVariante(String tipoVariante) {
		this.tipoVariante = tipoVariante;
	}

	public String getValoreVariante() {
		return valoreVariante;
	}

	public void setValoreVariante(String valoreVariante) {
		this.valoreVariante = valoreVariante;
	}

	public String getCodiceBarreVariante() {
		return codiceBarreVariante;
	}

	public void setCodiceBarreVariante(String codiceBarreVariante) {
		this.codiceBarreVariante = codiceBarreVariante;
	}

	public String getDimensioniVariante() {
		return dimensioniVariante;
	}

	public void setDimensioniVariante(String dimensioniVariante) {
		this.dimensioniVariante = dimensioniVariante;
	}

	public int getQuantitaVariante() {
		return quantitaVariante;
	}

	public void setQuantitaVariante(int quantitaVariante) {
		this.quantitaVariante = quantitaVariante;
	}

	public String getImmagineVariante() {
		return immagineVariante;
	}

	public void setImmagineVariante(String immagineVariante) {
		this.immagineVariante = immagineVariante;
	}

	public boolean isMostraVariante() {
		return mostraVariante;
	}

	public void setMostraVariante(boolean mostraVariante) {
		this.mostraVariante = mostraVariante;
	}


	public boolean isInvia_a_zb() {
		return invia_a_zb;
	}

	public void setInvia_a_zb(boolean invia_a_zb) {
		this.invia_a_zb = invia_a_zb;
	}


	public List<CategoriaEbay> getCategorieEbay() {
		if (categorieEbay == null || categorieEbay.isEmpty())
			categorieEbay = CategorieBusiness.getInstance().getCategorieEbay();
		return categorieEbay;
	}

	public void setCategorieEbay(List<CategoriaEbay> categorieEbay) {
		this.categorieEbay = categorieEbay;
	}


	public Articolo getArtDaInserzionare() {
		if (artDaInserzionare==null){
			artDaInserzionare = new Articolo();
		}
		return artDaInserzionare;
	}


	public void setArtDaInserzionare(Articolo artDaInserzionare) {
		this.artDaInserzionare = artDaInserzionare;
	}


	public InfoEbay getInformazioniEbay() {
		if (informazioniEbay==null){
			informazioniEbay = new InfoEbay();
		}
		return informazioniEbay;
	}


	public void setInformazioniEbay(InfoEbay informazioniEbay) {
		this.informazioniEbay = informazioniEbay;
	}


	public InfoAmazon getInformazioniAmazon() {
		if (informazioniAmazon==null){
			informazioniAmazon = new InfoAmazon();
		}
		return informazioniAmazon;
	}


	public void setInformazioniAmazon(InfoAmazon informazioniAmazon) {
		this.informazioniAmazon = informazioniAmazon;
	}

	public Map<String, String> getMessaggi() {
		if (messaggi==null) messaggi = new HashMap<String, String>();
		return messaggi;
	}

	public void setMessaggi(Map<String, String> messaggi) {
		this.messaggi = messaggi;
	}

	public String getCodiceArticoloDaCaricare() {
		return codiceArticoloDaCaricare;
	}

	public void setCodiceArticoloDaCaricare(String codiceArticoloDaCaricare) {
		this.codiceArticoloDaCaricare = codiceArticoloDaCaricare;
	}


}
