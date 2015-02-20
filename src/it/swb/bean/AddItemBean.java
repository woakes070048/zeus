package it.swb.bean;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import it.swb.business.ArticoloBusiness;
import it.swb.business.CategorieBusiness;
import it.swb.business.McdBusiness;
import it.swb.database.Articolo_DAO;
import it.swb.database.GM_IT_DAO;
import it.swb.database.ZB_IT_DAO;
import it.swb.ebay.EbayController;
import it.swb.ftp.FTPutil;
import it.swb.log.Log;
import it.swb.model.Articolo;
import it.swb.model.Categoria;
import it.swb.model.CategoriaAmazon;
import it.swb.model.CategoriaEbay;
import it.swb.model.InfoAmazon;
import it.swb.model.InfoEbay;
import it.swb.model.Variante_Articolo;
import it.swb.utility.Costanti;
import it.swb.utility.EditorDescrizioni;
import it.swb.utility.Methods;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.net.ftp.FTPClient;
import org.primefaces.event.FlowEvent;

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

	private Articolo art;
	private Articolo ultimoArticolo;
	private Map<String, String> mappaErrori;

	// INFORMAZIONI GENERALI
	private long id_articolo;
	private String codice_articolo;
	private String codice_fornitore;
	private String codice_articolo_fornitore;
	private String nome;
	private String note;
	private double prezzo_dettaglio;
	private double prezzo_ingrosso;
	private double prezzo_piattaforme;
	private double costo_acquisto;
	private int iva;
	private String codice_barre;
	private String tipo_codice_barre;
	private int quantita;
	private String quantita_inserzione = "Una confezione da X pezzi";
	private String dimensioni;
	private String descrizione_breve;
	private String descrizione;
	private Categoria categoria1;
	private Categoria categoria2;
	private String immagine1;
	private String immagine2;
	private String immagine3;
	private String immagine4;
	private String immagine5;
	private String video;
	private String idVideo;
	private String idEbay;

	private List<Variante_Articolo> varianti;

	private List<Categoria> categorie;
	private long idCategoria1 = -1;
	private long idCategoria2 = -1;
	private Map<Long, Categoria> categorieMap;

	private boolean inserisci_su_gestionale = true;
	private boolean modificaAbilitata = false;
	private boolean creaThumbnails = true;
	private boolean boxBomboniere = false;
	private boolean boxSecco = false;

	// INFORMAZIONI GLORIAMORALDI.IT
	private boolean invia_a_gm = true;
	private String meta_titolo;
	private String meta_descrizione;
	private String meta_keywords;
	private String titolo_inglese;
	private String descrizione_breve_inglese;
	private String descrizione_inglese;
	private String meta_titolo_inglese;
	private String meta_descrizione_inglese;
	private String meta_keywords_inglese;
	private String linkGM;

	// INFORMAZIONI EBAY
	private boolean invia_ad_ebay = true;
	private String titoloInserzione;
	private long id_categoria_ebay1 = -1;
	private long id_categoria_ebay2 = -1;
	private String nomeCategoriaEbay1;
	private String nomeCategoriaEbay2;
	private int durata_inserzione;
	private boolean rimettiInVendita;
	private boolean contrassegno;
	private double costo_spedizione = 7;
	private String linkEbay;

	private List<CategoriaEbay> categorieEbay;

	private long categoria_ebay1_level_1 = -1;
	private long categoria_ebay1_level_2 = -1;
	private long categoria_ebay1_level_3 = -1;
	private long categoria_ebay1_level_4 = -1;
	private List<CategoriaEbay> categorie_ebay1_level_1;
	private List<CategoriaEbay> categorie_ebay1_level_2;
	private List<CategoriaEbay> categorie_ebay1_level_3;
	private List<CategoriaEbay> categorie_ebay1_level_4;
	private long categoria_ebay2_level_1 = -1;
	private long categoria_ebay2_level_2 = -1;
	private long categoria_ebay2_level_3 = -1;
	private long categoria_ebay2_level_4 = -1;
	private List<CategoriaEbay> categorie_ebay2_level_1;
	private List<CategoriaEbay> categorie_ebay2_level_2;
	private List<CategoriaEbay> categorie_ebay2_level_3;
	private List<CategoriaEbay> categorie_ebay2_level_4;
	private String categoriaEbay1Selezionata = "";
	private String categoriaEbay2Selezionata = "";

	private String ambiente = "produzione";

	// INFORMAZIONI YATEGO
	private boolean invia_a_yatego = true;

	// INFORMAZIONI AMAZON
	private boolean invia_ad_amazon = true;
	private String punto_elenco_1;
	private String punto_elenco_2;
	private String punto_elenco_3;
	private String punto_elenco_4;
	private String punto_elenco_5;
	private int tempi_gestione;
	private String esclusione_responsabilita;
	private String descrizione_garanzia_venditore;
	private String avvertenze_sicurezza;
	private String nota_condizioni;
	private int voce_pacchetto_quantita = 1;
	private int numero_pezzi = 1;
	private int quantita_massima_spedizione_cumulativa = 100;
	private String paese_origine;
	private double lunghezza_articolo;
	private double altezza_articolo;
	private double peso_articolo;
	private String unita_misura_peso_articolo;
	private String paroleChiave1;
	private String paroleChiave2;
	private String paroleChiave3;
	private String paroleChiave4;
	private String paroleChiave5;
	private List<CategoriaAmazon> categorieAmazon;
	private long idCategoriaAmazon1 = -1;
	private long idCategoriaAmazon2 = -1;
	private String nomeCategoriaAmazon1;
	private String nomeCategoriaAmazon2;

	// INFORMAZIONI ZELDABOMBONIERE.IT
	private boolean invia_a_zb = true;
	private String linkZB;

	private String risultato_inserimento_locale;
	private String risultato_inserimento_gloriamoraldi;
	private String risultato_inserimento_ebay;
	private String risultato_inserimento_amazon;
	private String risultato_inserimento_yatego;
	private String risultato_inserimento_zb;

	public AddItemBean() {
	}

	private boolean skip;

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	public String onFlowProcess(FlowEvent event) {
		// logger.info("Current wizard step:" + event.getOldStep());
		// logger.info("Next step:" + event.getNewStep());

		if (skip) {
			skip = false; // reset in case user goes back
			return "tabYatego";
		} else
			return event.getNewStep();
	}

	public void save(ActionEvent actionEvent) {
		// Persist user
		FacesMessage msg = new FacesMessage("Successful", "Inserito");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void salvaArticolo(){
		
		Log.info("Salvataggio informazioni articolo "+codice_articolo); 
							
		Articolo a = new Articolo();
		
		InfoEbay ie = new InfoEbay();
		InfoAmazon ia = new InfoAmazon();
		
		a.setDataUltimaModifica(new Date(new java.util.Date().getTime()));

		/*	Scheda info generali	*/
		a.setCodice(codice_articolo);
		a.setNome(nome);
		a.setCodiceBarre(codice_barre);
		a.setTipoCodiceBarre(tipo_codice_barre);
		a.setCodiceFornitore(codice_fornitore);
		a.setCodiceArticoloFornitore(codice_articolo_fornitore);
		a.setPrezzoDettaglio(prezzo_dettaglio);
		a.setPrezzoIngrosso(prezzo_ingrosso);
		a.setPrezzoPiattaforme(prezzo_piattaforme);
		a.setCostoAcquisto(costo_acquisto);
		a.setCostoSpedizione(costo_spedizione);
		a.setAliquotaIva(iva);
		a.setQuantitaMagazzino(quantita);
		a.setQuantitaEffettiva(quantita);
		a.setQuantitaInserzione(quantita_inserzione);
		a.setDimensioni(dimensioni);
		a.setDescrizioneBreve(descrizione_breve);
		a.setDescrizione(descrizione);
		a.setParoleChiave1(paroleChiave1);
		a.setParoleChiave2(paroleChiave2);
		a.setParoleChiave3(paroleChiave3);
		a.setParoleChiave4(paroleChiave4);
		a.setParoleChiave5(paroleChiave5);
		a.setNote(note);
		
		/*	Scheda categorie & media	*/
		a.setIdCategoria(idCategoria1);
		a.setIdCategoria2(idCategoria2);
		
		categoria1 = CategorieBusiness.getInstance().getMappaCategorie().get(idCategoria1);
		categoria2 = CategorieBusiness.getInstance().getMappaCategorie().get(idCategoria2);
		
		a.setCategoria(categoria1);
		a.setCategoria2(categoria2);
		
		ie.setIdCategoriaEbay1(String.valueOf(id_categoria_ebay1));
		ie.setIdCategoriaEbay2(String.valueOf(id_categoria_ebay2));
		
		ia.setCategoria1(idCategoriaAmazon1);
		ia.setCategoria2(idCategoriaAmazon2);
		
		controlloSintassiImmagini();
		
		a.setImmagine1(immagine1);
		a.setImmagine2(immagine2);
		a.setImmagine3(immagine3);
		a.setImmagine4(immagine4);
		a.setImmagine5(immagine5);
		a.setVideo(video);
		a.setIdVideo(idVideo);
		
		/*	eBay	*/
		a.setIdEbay(idEbay);
		ie.setTitoloInserzione(titoloInserzione);
		ie.setDurata_inserzione(durata_inserzione);
		ie.setRimettiInVendita(rimettiInVendita);
		ie.setContrassegno(contrassegno);
		ie.setAmbiente("produzione");
		ie.setBoxBomboniere(boxBomboniere);
		ie.setBoxSecco(boxSecco);
		
		/*	Amazon	*/		
		ia.setNotaCondizioni(nota_condizioni);
		ia.setVocePacchettoQuantita(voce_pacchetto_quantita);
		ia.setNumeroPezzi(numero_pezzi);
		ia.setQuantitaMassimaSpedizioneCumulativa(quantita_massima_spedizione_cumulativa);
		
		/*	imposto sta roba	*/
		a.setInfoEbay(ie);
		a.setInfoAmazon(ia);
		
		/*	Varianti	*/
		a.setVarianti(varianti);		
		
		//if (creaThumbnails) scaricaECreaThumbnail();
				
		id_articolo = ArticoloBusiness.getInstance().inserisciOModificaArticolo(a);
		a.setIdArticolo(id_articolo);
		
		if (id_articolo>0)
			risultato_inserimento_locale = "Articolo salvato correttamente nel database di Zeus.";
		else risultato_inserimento_locale = "!!! Articolo NON salvato nel database di Zeus, si è verificato qualche problema. Controllare i log.";
		
		Log.info(risultato_inserimento_locale);
		
		ArticoloBusiness.getInstance().reloadArticoli();
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Operazione Completata",risultato_inserimento_locale));  
	}

	public void pubblicaInserzioni() {

		salvaArticolo();
		Log.info("Pubblicazione inserzioni articolo con codice: " + codice_articolo + " e ID: " + id_articolo);
		
		Articolo a = new Articolo();
		a.setIdArticolo(id_articolo);

		InfoEbay ie = new InfoEbay();
		InfoAmazon ia = new InfoAmazon();
		
		a.setDataUltimaModifica(new Date(new java.util.Date().getTime()));

		/*	Scheda info generali	*/
		a.setCodice(codice_articolo);
		a.setNome(nome);
		a.setCodiceBarre(codice_barre);
		a.setTipoCodiceBarre(tipo_codice_barre);
		a.setCodiceFornitore(codice_fornitore);
		a.setCodiceArticoloFornitore(codice_articolo_fornitore);
		a.setPrezzoDettaglio(prezzo_dettaglio);
		a.setPrezzoIngrosso(prezzo_ingrosso);
		a.setPrezzoPiattaforme(prezzo_piattaforme);
		a.setCostoAcquisto(costo_acquisto);
		a.setCostoSpedizione(costo_spedizione);
		a.setAliquotaIva(iva);
		a.setQuantitaMagazzino(quantita);
		a.setQuantitaEffettiva(quantita);
		a.setQuantitaInserzione(quantita_inserzione);
		a.setDimensioni(dimensioni);
		a.setDescrizioneBreve(descrizione_breve);
		a.setDescrizione(descrizione);
		a.setParoleChiave1(paroleChiave1);
		a.setParoleChiave2(paroleChiave2);
		a.setParoleChiave3(paroleChiave3);
		a.setParoleChiave4(paroleChiave4);
		a.setParoleChiave5(paroleChiave5);
		a.setNote(note);
		
		/*	Scheda categorie & media	*/
		a.setIdCategoria(idCategoria1);
		a.setIdCategoria2(idCategoria2);
		
		categoria1 = CategorieBusiness.getInstance().getMappaCategorie().get(idCategoria1);
		categoria2 = CategorieBusiness.getInstance().getMappaCategorie().get(idCategoria2);
		
		a.setCategoria(categoria1);
		a.setCategoria2(categoria2);
		
		ie.setIdCategoriaEbay1(String.valueOf(id_categoria_ebay1));
		ie.setIdCategoriaEbay2(String.valueOf(id_categoria_ebay2));
		
		ia.setCategoria1(idCategoriaAmazon1);
		ia.setCategoria2(idCategoriaAmazon2);
		
		controlloSintassiImmagini();
		
		a.setImmagine1(immagine1);
		a.setImmagine2(immagine2);
		a.setImmagine3(immagine3);
		a.setImmagine4(immagine4);
		a.setImmagine5(immagine5);
		a.setVideo(video);
		a.setIdVideo(idVideo);
		
		/*	eBay	*/
		a.setIdEbay(idEbay);
		ie.setTitoloInserzione(titoloInserzione);
		ie.setDurata_inserzione(durata_inserzione);
		ie.setRimettiInVendita(rimettiInVendita);
		ie.setContrassegno(contrassegno);
		ie.setAmbiente("produzione");
		ie.setBoxBomboniere(boxBomboniere);
		ie.setBoxSecco(boxSecco);
		
		/*	Amazon	*/		
		ia.setNotaCondizioni(nota_condizioni);
		ia.setVocePacchettoQuantita(voce_pacchetto_quantita);
		ia.setNumeroPezzi(numero_pezzi);
		ia.setQuantitaMassimaSpedizioneCumulativa(quantita_massima_spedizione_cumulativa);
		
		/*	imposto sta roba	*/
		a.setInfoEbay(ie);
		a.setInfoAmazon(ia);
		
		/*	Varianti	*/
		a.setVarianti(varianti);		
		
		if (creaThumbnails) scaricaECreaThumbnail();

		risultato_inserimento_gloriamoraldi = "";
		risultato_inserimento_ebay = "";
		linkGM = "";
		linkEbay = "";

		//a.setInfoEbay(salvaDatiEbay());
		if (invia_ad_ebay) {
			creaInserzioneSuEbay(a);
		}
		
		if (invia_a_zb) {
			creaInserzioneSuZB(a);
		}
		
		if (invia_a_gm) {
			creaInserzioneSuGM(a);
		}

		//a.setInfoAmazon(salvaDatiAmazon());
		if (invia_ad_amazon) {
			creaInserzioneSuAmazon(a);
		}

		if (invia_a_yatego) {
			creaInserzioneSuYatego(a);
		}

		art = null;
		id_articolo = -1;
		ultimoArticolo = a;
		
		ArticoloBusiness.getInstance().reloadArticoli();
	}
	
	public int creaArticolo() {

		if (!modificaAbilitata)
			Log.info("Creazione nuovo articolo: " + codice_articolo);
		else
			Log.info("Modifica articolo con codice: " + codice_articolo
					+ " e ID: " + id_articolo);

		if (modificaAbilitata
				|| !Articolo_DAO.checkIfArticoloExist(codice_articolo)) {

			Articolo a = new Articolo();

			if (modificaAbilitata) {
				a.setIdArticolo(id_articolo);
				a.setDataUltimaModifica(new Date(new java.util.Date().getTime()));
			}

			a.setCodice(codice_articolo);
			a.setCodiceFornitore(codice_fornitore);
			a.setCodiceArticoloFornitore(codice_articolo_fornitore);
			a.setNome(nome);
			a.setNote(note);
			a.setTitoloInserzione(titoloInserzione);
			a.setPrezzoDettaglio(prezzo_dettaglio);
			a.setPrezzoIngrosso(prezzo_ingrosso);
			a.setPrezzoPiattaforme(prezzo_piattaforme);
			a.setCostoAcquisto(costo_acquisto);
			a.setCostoSpedizione(costo_spedizione);
			a.setAliquotaIva(iva);
			a.setCodiceBarre(codice_barre);
			a.setTipoCodiceBarre(tipo_codice_barre);
			a.setQuantitaMagazzino(quantita);
			a.setQuantitaEffettiva(quantita);
			a.setQuantitaInserzione(quantita_inserzione);
			a.setDimensioni(dimensioni);
			a.setDescrizioneBreve(descrizione_breve);
			a.setDescrizione(descrizione);
			a.setIdCategoria(idCategoria1);
			a.setIdCategoria2(idCategoria2);
			a.setCategoria(getCategorieMap().get(idCategoria1));
			a.setCategoria2(getCategorieMap().get(idCategoria2));

			controlloSintassiImmagini();

			a.setImmagine1(immagine1);
			a.setImmagine2(immagine2);
			a.setImmagine3(immagine3);
			a.setImmagine4(immagine4);
			a.setImmagine5(immagine5);

			a.setVideo(video);
			a.setIdVideo(idVideo);

			a.setVarianti(varianti);

			risultato_inserimento_locale = "";
			risultato_inserimento_gloriamoraldi = "";
			risultato_inserimento_ebay = "";
			linkGM = "";
			linkEbay = "";

			if (creaThumbnails)
				scaricaECreaThumbnail();

			id_articolo = -1;

			if (inserisci_su_gestionale) {
				if (!modificaAbilitata) {
					id_articolo = ArticoloBusiness.getInstance().inserisciArticolo(a);
					a.setIdArticolo(id_articolo);
					if (id_articolo != 0)
						risultato_inserimento_locale = "Articolo inserito correttamente nel database di Zeus.";
					else
						risultato_inserimento_locale = "!!! Articolo NON inserito nel database di Zeus. Si è verificato qualche problema.";
				} else {
					id_articolo = ArticoloBusiness.getInstance().modificaArticolo(a, "");
					if (id_articolo != 0)
						risultato_inserimento_locale = "Dati dell'articolo modificati correttamente nel database di Zeus.";
					else
						risultato_inserimento_locale = "!!! Dati dell'articolo NON modificati nel database di Zeus. Si è verificato qualche problema.";
				}

				Log.info(risultato_inserimento_locale);
			}

			if (invia_a_gm) {
				creaInserzioneSuGM(a);
			}

			a.setInfoEbay(salvaDatiEbay());
			if (invia_ad_ebay) {
				creaInserzioneSuEbay(a);
			}

			a.setInfoAmazon(salvaDatiAmazon());
			if (invia_ad_amazon) {
				creaInserzioneSuAmazon(a);
			}

			if (invia_a_yatego) {
				creaInserzioneSuYatego(a);
			}

			if (invia_a_zb) {
				creaInserzioneSuZB(a);
			}

			modificaAbilitata = false;
			art = null;
			id_articolo = -1;
			ultimoArticolo = a;
		} else {
			risultato_inserimento_locale = "!!! Nel database esiste già un articolo con lo stesso codice: "
					+ codice_articolo
					+ ". Se la relativa inserzione su eBay o su gloriamoraldi.it non è stata ancora pubblicata, vai su "
					+ "File -> Visualizza -> Articoli, cerca l'articolo tramite il codice e da lì potrai pubblicare le inserzioni.";
			risultato_inserimento_gloriamoraldi = "";
			risultato_inserimento_ebay = "";
			linkEbay = "";

			Log.info("Nel database esiste già un articolo con lo stesso codice: ");
		}
		return 1;
	}

		/* Vecchio metodo di quando Gloria Moraldi era fatto in joomla */
//	private void creaInserzioneSuGM(Articolo a) {
//		InfoGM gmInfo = new InfoGM();
//		gmInfo.setDescrizioneBreveInglese("");
//		gmInfo.setDescrizioneInglese("");
//		gmInfo.setMetaDescrizione(paroleChiave1+" "+paroleChiave2+" "+paroleChiave3+" "+paroleChiave4+" "+paroleChiave5);
//		gmInfo.setMetaDescrizioneInglese("");
//		gmInfo.setMetaKeywords(paroleChiave1+" "+paroleChiave2+" "+paroleChiave3+" "+paroleChiave4+" "+paroleChiave5);
//		gmInfo.setMetaKeywordsInglese("");
//		gmInfo.setMetaTitolo(titoloInserzione);
//		gmInfo.setMetaTitoloInglese("");
//		gmInfo.setTitoloInglese("");
//
//		a.setGmInfo(gmInfo);
//
//		Product p = Convertitore.convertiArticoloInProduct(a);
//
//		// GloriaMoraldi_DAO.insertIntoProducts(p);
//		int y = GloriaMoraldiRemoto_DAO.insertIntoProducts(p);
//
//		if (y != 0) {
//			risultato_inserimento_gloriamoraldi = "Articolo inserito correttamente su GloriaMoraldi.it";
//			linkGM = Costanti.ricercaSuGmBackend+a.getIdArticolo();
//			Articolo_DAO.setPresenzaSu(a.getCodice(), "gm", 1, null);
//		} else
//			risultato_inserimento_gloriamoraldi = "!!! Articolo NON inserito su GloriaMoraldi.it : Si è verificato qualche problema.";
//
//		Log.info(risultato_inserimento_gloriamoraldi);
//	}

	private InfoEbay salvaDatiEbay() {
		InfoEbay ie = new InfoEbay();
		ie.setTitoloInserzione(titoloInserzione);
		ie.setIdCategoriaEbay1(String.valueOf(id_categoria_ebay1));
		ie.setIdCategoriaEbay2(String.valueOf(id_categoria_ebay2));
		ie.setDurata_inserzione(durata_inserzione);
		ie.setRimettiInVendita(rimettiInVendita);
		ie.setContrassegno(contrassegno);
		ie.setAmbiente("produzione");
		ie.setBoxBomboniere(boxBomboniere);
		ie.setBoxSecco(boxSecco);

		Articolo_DAO.modificaInformazioniEbay(codice_articolo,"", ie, null);

		return ie;
	}

	private void creaInserzioneSuEbay(Articolo a) {

		a.getInfoEbay().setDescrizioneEbay(
				EditorDescrizioni.creaDescrizioneEbay(a));

		String[] z = EbayController.creaInserzione(a);

		if (z[0].equals("0")) {
			risultato_inserimento_ebay = "!!! Inserzione NON creata su eBay: "
					+ z[1];
		} else if (z[0].equals("1")) {

			risultato_inserimento_ebay = "Inserzione creata correttamente su eBay: ";
			linkEbay = Costanti.linkEbayProduzione + z[1];

			Articolo_DAO.setPresenzaSu(a.getCodice(), "ebay", 1, z[1]);
		}
		Log.info(risultato_inserimento_ebay + linkEbay);
	}

	private InfoAmazon salvaDatiAmazon() {
		InfoAmazon ia = new InfoAmazon();
		ia.setPuntoElenco3(punto_elenco_3);
		ia.setPuntoElenco4(punto_elenco_4);
		ia.setPuntoElenco5(punto_elenco_5);
		ia.setEsclusioneResponsabilita(esclusione_responsabilita);
		ia.setDescrizioneGaranziaVenditore(descrizione_garanzia_venditore);
		ia.setAvvertenzeSicurezza(avvertenze_sicurezza);
		ia.setNotaCondizioni(nota_condizioni);
		ia.setVocePacchettoQuantita(voce_pacchetto_quantita);
		ia.setNumeroPezzi(numero_pezzi);
		ia.setQuantitaMassimaSpedizioneCumulativa(quantita_massima_spedizione_cumulativa);
		ia.setPaeseOrigine(paese_origine);
		ia.setLunghezzaArticolo(lunghezza_articolo);
		ia.setAltezzaArticolo(altezza_articolo);
		ia.setPesoArticolo(peso_articolo);
		ia.setUnitaMisuraPesoArticolo(unita_misura_peso_articolo);
//		ia.setParoleChiave1(paroleChiave1);
//		ia.setParoleChiave2(paroleChiave2);
//		ia.setParoleChiave3(paroleChiave3);
//		ia.setParoleChiave4(paroleChiave4);
//		ia.setParoleChiave5(paroleChiave5);
		ia.setCategoria1(idCategoriaAmazon1);
		ia.setCategoria2(idCategoriaAmazon2);

		Articolo_DAO.modificaInformazioniAmazon(codice_articolo, ia, null);

		return ia;
	}

	private void creaInserzioneSuAmazon(Articolo a) {

		int res = McdBusiness.aggiungiAMcd(a.getCodice(),"amazon");
		//int res = EditorModelliAmazon.aggiungiProdottoAModelloAmazon(a);

		if (res == 1) {
			risultato_inserimento_amazon = "Articolo inserito correttamente nel modello caricamento dati di Amazon.";
			Articolo_DAO.setPresenzaSu(a.getCodice(), "amazon", -1, null);
		} else
			risultato_inserimento_amazon = "Articolo NON inserito nel modello caricamento dati di Amazon. Si è verificato qualche problema.";

		// Articolo_DAO.modificaInformazioniAmazon(a.getCodice(),ia,null);
	}

	private void creaInserzioneSuYatego(Articolo a) {

		int res = McdBusiness.aggiungiAMcd(a.getCodice(),"yatego");
		//int res = EditorModelliYatego.aggiungiProdottoAModelloYatego(a);

		if (res == 1) {
			risultato_inserimento_yatego = "Articolo inserito correttamente nel modello caricamento dati di Yatego.";
			Articolo_DAO.setPresenzaSu(a.getCodice(), "yatego", -1, null);
		} else
			risultato_inserimento_yatego = "Articolo NON inserito nel modello caricamento dati di Yatego. Si è verificato qualche problema.";

		//
	}
	
	private void creaInserzioneSuZB(Articolo a) {

		//if (EditorModelliZelda.aggiungiProdottoAModelloZelda(a)){
		//EditorModelliZelda.aggiungiProdottoAModelloZelda(a);
		if ( ZB_IT_DAO.insertIntoProduct(a)==1 /*ZeldaSQL.aggiungiOEliminaDaModelloZelda(a,true)*/){
		
			setRisultato_inserimento_zb("Articolo inserito su ZeldaBomboniere.it: ");
			linkZB = Costanti.linkZeldaBomboniere + a.getIdArticolo();
			
			Articolo_DAO.setPresenzaSu(a.getCodice(), "zb", 1, null);
		} else
			setRisultato_inserimento_zb("Articolo NON inserito su ZeldaBomboniere.it: Si è verificato qualche problema.");		
}
	
	private void creaInserzioneSuGM(Articolo a) {

		//if (EditorModelliZelda.aggiungiProdottoAModelloZelda(a)){
		//EditorModelliZelda.aggiungiProdottoAModelloZelda(a);
		if ( GM_IT_DAO.insertIntoProduct(a)==1 /*ZeldaSQL.aggiungiOEliminaDaModelloZelda(a,true)*/){
		
			setRisultato_inserimento_gloriamoraldi("Articolo inserito su GloriaMoraldi.it: ");
			linkGM = Costanti.linkGloriamoraldi + a.getIdArticolo();
			
			Articolo_DAO.setPresenzaSu(a.getCodice(), "gm", 1, null);
		} else
			setRisultato_inserimento_zb("Articolo NON inserito su GloriaMoraldi.it: Si è verificato qualche problema.");		
}

	/* Vecchio metodo di quando il db di zb era in locale  */
//	private void creaInserzioneSuZB(Articolo a) {
//		int r = ZeldaBomboniereIT_DAO.insertIntoProduct(a);
//
//		if (r != 0) {
//			if (a.getImmagine1() != null && !a.getImmagine1().trim().isEmpty()){
//				Methods.copiaImmaginePerZeldaBomboniere(a.getImmagine1());
//			}
//			if (a.getImmagine2() != null && !a.getImmagine2().trim().isEmpty()){
//				Methods.copiaImmaginePerZeldaBomboniere(a.getImmagine2());
//			}
//			if (a.getImmagine3() != null && !a.getImmagine3().trim().isEmpty()){
//				Methods.copiaImmaginePerZeldaBomboniere(a.getImmagine3());
//			}
//			if (a.getImmagine4() != null && !a.getImmagine4().trim().isEmpty()){
//				Methods.copiaImmaginePerZeldaBomboniere(a.getImmagine4());
//			}
//			if (a.getImmagine5() != null && !a.getImmagine5().trim().isEmpty()){
//				Methods.copiaImmaginePerZeldaBomboniere(a.getImmagine5());				
//			}
//			
//			if (a.getVarianti()!=null && !a.getVarianti().isEmpty()){
//				for (Variante_Articolo v : a.getVarianti()){
//					Methods.copiaImmaginePerZeldaBomboniere(v.getImmagine());
//				}
//			}
//			setRisultato_inserimento_zb("Articolo inserito su ZeldaBomboniere.it: ");
//			linkZB = Costanti.linkZeldaBomboniere + a.getIdArticolo();
//			
//			Articolo_DAO.setPresenzaSu(a.getCodice(), "zb", 1);
//		} else
//			setRisultato_inserimento_zb("Articolo NON inserito su ZeldaBomboniere.it");		
//	}

	/** Se le immagini non sono nulle o vuote le trasforma in minuscolo  */
	private void controlloSintassiImmagini(){
		if (immagine1!=null && !immagine1.trim().isEmpty()) {
			immagine1=immagine1.toLowerCase().trim();
			//if (!immagine1.contains(".jpg")) immagine1 = immagine1+".jpg";
		}
		if (immagine2!=null && !immagine2.trim().isEmpty()) {
			immagine2=immagine2.toLowerCase().trim();
			//if (!immagine2.contains(".jpg")) immagine2 = immagine2+".jpg";
		}
		if (immagine3!=null && !immagine3.trim().isEmpty()) {
			immagine3=immagine3.toLowerCase().trim();
			//if (!immagine3.contains(".jpg")) immagine3 = immagine3+".jpg";
		}
		if (immagine4!=null && !immagine4.trim().isEmpty()) {
			immagine4=immagine4.toLowerCase().trim();
			//if (!immagine4.contains(".jpg")) immagine4 = immagine4+".jpg";
		}
		if (immagine5!=null && !immagine5.trim().isEmpty()) {
			immagine5=immagine5.toLowerCase().trim();
			//if (!immagine5.contains(".jpg")) immagine5 = immagine5+".jpg";
		}
		
		if (varianti!=null && !varianti.isEmpty()){
			for (Variante_Articolo v : varianti){
				if (v.getImmagine()!=null && !v.getImmagine().trim().isEmpty()) {
					v.setImmagine(v.getImmagine().toLowerCase().trim());
				}
			}
		}
	}
	
	private void scaricaECreaThumbnail() {
		Log.info("Crea Thumbnails: ");
		FTPClient f = FTPutil.getConnection();

		if (Methods.controlloSintassiImmagine(immagine1)) Methods.creaThumbnailsEcaricaSuFtp(immagine1,true, f);
		
		if (Methods.controlloSintassiImmagine(immagine2)) Methods.creaThumbnailsEcaricaSuFtp(immagine2,false, f);
		
		if (Methods.controlloSintassiImmagine(immagine3)) Methods.creaThumbnailsEcaricaSuFtp(immagine3,false, f);
		
		if (Methods.controlloSintassiImmagine(immagine4)) Methods.creaThumbnailsEcaricaSuFtp(immagine4,false, f);
		
		if (Methods.controlloSintassiImmagine(immagine5)) Methods.creaThumbnailsEcaricaSuFtp(immagine5,false, f);
		

		if (varianti != null && !varianti.isEmpty()) {
			for (Variante_Articolo v : varianti) {
				if (Methods.controlloSintassiImmagine(v.getImmagine())) Methods.creaThumbnailsEcaricaSuFtp(v.getImmagine(),false, f);
			}
		}
		FTPutil.closeConnection(f);
	}

	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
				summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void CatEbay1Level1Selezionata() {
		Log.debug("CatEbay1Level1Selezionata: " + categoria_ebay1_level_1);
		categorie_ebay1_level_2 = CategorieBusiness
				.getCategorieEbayLevel_2(categoria_ebay1_level_1);
		categorie_ebay1_level_3.clear();
		categorie_ebay1_level_4.clear();
	}

	public void CatEbay1Level2Selezionata() {
		Log.debug("CatEbay1Level2Selezionata: " + categoria_ebay1_level_2);
		categorie_ebay1_level_3 = CategorieBusiness
				.getCategorieEbayLevel_3(categoria_ebay1_level_2);
		categorie_ebay1_level_4.clear();
	}

	public void CatEbay1Level3Selezionata() {
		Log.debug("CatEbay1Level3Selezionata: " + categoria_ebay1_level_3);
		categorie_ebay1_level_4 = CategorieBusiness
				.getCategorieEbayLevel_4(categoria_ebay1_level_3);
	}

	public void CatEbay2Level1Selezionata() {
		Log.debug("CatEbay2Level1Selezionata: " + categoria_ebay2_level_1);
		categorie_ebay2_level_2 = CategorieBusiness
				.getCategorieEbayLevel_2(categoria_ebay2_level_1);
	}

	public void CatEbay2Level2Selezionata() {
		Log.debug("CatEbay2Level2Selezionata: " + categoria_ebay2_level_2);
		categorie_ebay2_level_3 = CategorieBusiness
				.getCategorieEbayLevel_3(categoria_ebay2_level_2);
	}

	public void CatEbay2Level3Selezionata() {
		Log.debug("CatEbay2Level3Selezionata: " + categoria_ebay2_level_3);
		categorie_ebay2_level_4 = CategorieBusiness
				.getCategorieEbayLevel_4(categoria_ebay2_level_3);
	}

	public void svuotaCategorie1() {
		categoria_ebay1_level_1 = -1;
		categoria_ebay1_level_2 = -1;
		categoria_ebay1_level_3 = -1;
		categoria_ebay1_level_4 = -1;

		if (categorie_ebay1_level_1 != null)
			categorie_ebay1_level_1.clear();
		if (categorie_ebay1_level_2 != null)
			categorie_ebay1_level_2.clear();
		if (categorie_ebay1_level_3 != null)
			categorie_ebay1_level_3.clear();
		if (categorie_ebay1_level_4 != null)
			categorie_ebay1_level_4.clear();

		categoriaEbay1Selezionata = "";
	}

	public void svuotaCategorie2() {
		categoria_ebay2_level_1 = -1;
		categoria_ebay2_level_2 = -1;
		categoria_ebay2_level_3 = -1;
		categoria_ebay2_level_4 = -1;

		if (categorie_ebay2_level_1 != null)
			categorie_ebay2_level_1.clear();
		if (categorie_ebay2_level_2 != null)
			categorie_ebay2_level_2.clear();
		if (categorie_ebay2_level_3 != null)
			categorie_ebay2_level_3.clear();
		if (categorie_ebay2_level_4 != null)
			categorie_ebay2_level_4.clear();

		categoriaEbay2Selezionata = "";
	}

	public void selezioneCategoriaEbay1() {
		if (categoria_ebay1_level_2 <= 0)
			id_categoria_ebay1 = categoria_ebay1_level_1;
		else if (categoria_ebay1_level_3 <= 0)
			id_categoria_ebay1 = categoria_ebay1_level_2;
		else if (categoria_ebay1_level_4 <= 0)
			id_categoria_ebay1 = categoria_ebay1_level_3;
		else
			id_categoria_ebay1 = categoria_ebay1_level_4;
	}

	public void selezioneCategoriaEbay2() {
		if (categoria_ebay2_level_2 <= 0)
			id_categoria_ebay2 = categoria_ebay2_level_1;
		else if (categoria_ebay2_level_3 <= 0)
			id_categoria_ebay2 = categoria_ebay2_level_2;
		else if (categoria_ebay2_level_4 <= 0)
			id_categoria_ebay2 = categoria_ebay2_level_3;
		else
			id_categoria_ebay2 = categoria_ebay2_level_4;
	}

	public void caricaDatiArticolo() {
		Log.info("Caricamento dati articolo: " + art.getCodice());

		modificaAbilitata = true;
		inserisci_su_gestionale = true;
		creaThumbnails = true;

		id_articolo = art.getIdArticolo();
		codice_articolo = art.getCodice();
		codice_fornitore = art.getCodiceFornitore();
		codice_articolo_fornitore = art.getCodiceArticoloFornitore();
		codice_barre = art.getCodiceBarre();
		tipo_codice_barre = art.getTipoCodiceBarre();
		nome = art.getNome();
		note = art.getNote();
		prezzo_dettaglio = art.getPrezzoDettaglio();
		prezzo_ingrosso = art.getPrezzoIngrosso();
		prezzo_piattaforme = art.getPrezzoPiattaforme();
		costo_acquisto = art.getCostoAcquisto();
		iva = art.getAliquotaIva();
		quantita = art.getQuantitaMagazzino();
		if (quantita <= 0)
			quantita = 100;
		quantita_inserzione = art.getQuantitaInserzione();
		dimensioni = art.getDimensioni();
		descrizione_breve = art.getDescrizioneBreve();
		descrizione = art.getDescrizione();
		idCategoria1 = art.getIdCategoria();
		idCategoria2 = art.getIdCategoria2();
		categoria1 = CategorieBusiness.getInstance().getMappaCategorie().get(idCategoria1);
		categoria2 = CategorieBusiness.getInstance().getMappaCategorie().get(idCategoria2);
		
		paroleChiave1 = art.getParoleChiave1();
		paroleChiave2 = art.getParoleChiave2();
		paroleChiave3 = art.getParoleChiave3();
		paroleChiave4 = art.getParoleChiave4();
		paroleChiave5 = art.getParoleChiave5();
		
		if (art.getImmagine1()!=null && !art.getImmagine1().trim().isEmpty()) immagine1 = art.getImmagine1().toLowerCase(); else immagine1 = "";
		if (art.getImmagine2()!=null && !art.getImmagine2().trim().isEmpty()) immagine2 = art.getImmagine2().toLowerCase(); else immagine2 = "";
		if (art.getImmagine3()!=null && !art.getImmagine3().trim().isEmpty()) immagine3 = art.getImmagine3().toLowerCase(); else immagine3 = "";
		if (art.getImmagine4()!=null && !art.getImmagine4().trim().isEmpty()) immagine4 = art.getImmagine4().toLowerCase(); else immagine4 = "";
		if (art.getImmagine5()!=null && !art.getImmagine5().trim().isEmpty()) immagine5 = art.getImmagine5().toLowerCase(); else immagine5 = "";
		creaThumbnails = false;

		varianti = art.getVarianti();

		// INFORMAZIONI GLORIAMORALDI.IT
		// invia_a_gm = art.isPresente_su_gm();
		meta_titolo = "";
		meta_descrizione = "";
		meta_keywords = "";
		titolo_inglese = "";
		descrizione_breve_inglese = "";
		descrizione_inglese = "";
		meta_titolo_inglese = "";
		meta_descrizione_inglese = "";
		meta_keywords_inglese = "";

		// INFORMAZIONI EBAY
		// invia_ad_ebay = art.isPresente_su_ebay();
		titoloInserzione = art.getTitoloInserzione();
		if (art.getInfoEbay() != null) {
			id_categoria_ebay1 = Long.valueOf(art.getInfoEbay()
					.getIdCategoriaEbay1());
			id_categoria_ebay2 = Long.valueOf(art.getInfoEbay()
					.getIdCategoriaEbay2());
		}
		// id_categoria_ebay1 = -1;
		// id_categoria_ebay2 = -1;
		idEbay = art.getIdEbay();
		durata_inserzione = 999;
		contrassegno = false;
		costo_spedizione = art.getCostoSpedizione();
		ambiente = "produzione";

		// INFORMAZIONI AMAZON
		invia_ad_amazon = true;
		if (art.getInfoAmazon() != null) {
			InfoAmazon ia = art.getInfoAmazon();
			idCategoriaAmazon1 = ia.getCategoria1();
			idCategoriaAmazon2 = ia.getCategoria2();
			voce_pacchetto_quantita = ia.getVocePacchettoQuantita();
			quantita_massima_spedizione_cumulativa = 100; //ia.getQuantitaMassimaSpedizioneCumulativa();
			numero_pezzi = ia.getNumeroPezzi();
		}

		invia_a_yatego = true;

		invia_a_zb = true;

		risultato_inserimento_amazon = "";
		risultato_inserimento_ebay = "";
		risultato_inserimento_gloriamoraldi = "";
		risultato_inserimento_locale = "";
		risultato_inserimento_yatego = "";
		risultato_inserimento_zb = "";
		linkEbay = "";
		linkZB = "";
		linkGM = "";
	}

	public void caricaDatiArticoloSimile() {
		Log.info("Caricamento dati per creazione articolo simile: "
				+ art.getCodice());

		inserisci_su_gestionale = true;
		creaThumbnails = false;

		id_articolo = -1;
		codice_articolo = "";
		codice_fornitore = art.getCodiceFornitore();
		codice_articolo_fornitore = art.getCodiceArticoloFornitore();
		codice_barre = art.getCodiceBarre();
		tipo_codice_barre = art.getTipoCodiceBarre();
		nome = art.getNome();
		prezzo_dettaglio = art.getPrezzoDettaglio();
		prezzo_ingrosso = art.getPrezzoIngrosso();
		prezzo_piattaforme = art.getPrezzoPiattaforme();
		costo_acquisto = art.getCostoAcquisto();
		iva = art.getAliquotaIva();
		quantita = art.getQuantitaMagazzino();
		if (quantita <= 0)
			quantita = 100;
		quantita_inserzione = art.getQuantitaInserzione();
		dimensioni = art.getDimensioni();
		descrizione_breve = art.getDescrizioneBreve();
		descrizione = art.getDescrizione();
		idCategoria1 = art.getIdCategoria();
		idCategoria2 = art.getIdCategoria2();
		categoria1 = CategorieBusiness.getInstance().getMappaCategorie().get(idCategoria1);
		categoria2 = CategorieBusiness.getInstance().getMappaCategorie().get(idCategoria2);
		
		paroleChiave1 = art.getParoleChiave1();
		paroleChiave2 = art.getParoleChiave2();
		paroleChiave3 = art.getParoleChiave3();
		paroleChiave4 = art.getParoleChiave4();
		paroleChiave5 = art.getParoleChiave5();
		
		if (art.getImmagine1()!=null && !art.getImmagine1().trim().isEmpty()) immagine1 = art.getImmagine1().toLowerCase(); else immagine1 = "";
		if (art.getImmagine2()!=null && !art.getImmagine2().trim().isEmpty()) immagine2 = art.getImmagine2().toLowerCase(); else immagine2 = "";
		if (art.getImmagine3()!=null && !art.getImmagine3().trim().isEmpty()) immagine3 = art.getImmagine3().toLowerCase(); else immagine3 = "";
		if (art.getImmagine4()!=null && !art.getImmagine4().trim().isEmpty()) immagine4 = art.getImmagine4().toLowerCase(); else immagine4 = "";
		if (art.getImmagine5()!=null && !art.getImmagine5().trim().isEmpty()) immagine5 = art.getImmagine5().toLowerCase(); else immagine5 = "";
		creaThumbnails = false;

		varianti = art.getVarianti();

		// INFORMAZIONI GLORIAMORALDI.IT
		// invia_a_gm = art.isPresente_su_gm();
		meta_titolo = "";
		meta_descrizione = "";
		meta_keywords = "";
		titolo_inglese = "";
		descrizione_breve_inglese = "";
		descrizione_inglese = "";
		meta_titolo_inglese = "";
		meta_descrizione_inglese = "";
		meta_keywords_inglese = "";
		art.setPresente_su_gm(0);

		// INFORMAZIONI EBAY
		// invia_ad_ebay = art.isPresente_su_ebay();
		titoloInserzione = art.getTitoloInserzione();
		if (art.getInfoEbay() != null) {
			id_categoria_ebay1 = Long.valueOf(art.getInfoEbay()
					.getIdCategoriaEbay1());
			id_categoria_ebay2 = Long.valueOf(art.getInfoEbay()
					.getIdCategoriaEbay2());
		}
		// id_categoria_ebay1 = -1;
		// id_categoria_ebay2 = -1;
		idEbay = "";
		durata_inserzione = 999;
		contrassegno = false;
		costo_spedizione = art.getCostoSpedizione();
		ambiente = "produzione";
		art.setPresente_su_ebay(0);

		// INFORMAZIONI AMAZON
		invia_ad_amazon = true;
		if (art.getInfoAmazon() != null) {
			InfoAmazon ia = art.getInfoAmazon();
			idCategoriaAmazon1 = ia.getCategoria1();
			idCategoriaAmazon2 = ia.getCategoria2();
			voce_pacchetto_quantita = ia.getVocePacchettoQuantita();
			quantita_massima_spedizione_cumulativa = 100; //ia.getQuantitaMassimaSpedizioneCumulativa();
			numero_pezzi = ia.getNumeroPezzi();
		}

		invia_a_yatego = true;

		invia_a_zb = true;

		risultato_inserimento_amazon = "";
		risultato_inserimento_ebay = "";
		risultato_inserimento_gloriamoraldi = "";
		risultato_inserimento_locale = "";
		risultato_inserimento_yatego = "";
		risultato_inserimento_zb = "";
		linkEbay = "";
		linkZB = "";
		linkGM = "";
	}

	public void caricaDatiTest() {

		int x = (int) (Math.random() * 100);
		codice_articolo = "TEST_" + x;
		codice_fornitore = "XXX";
		codice_articolo_fornitore = "XXX_" + x;
		codice_barre = "10000000000" + x;
		tipo_codice_barre = "EAN-13";
		nome = "ARTICOLO TEST " + x;
		prezzo_dettaglio = 99.99;
		prezzo_ingrosso = 5.55;
		prezzo_piattaforme = 99.99;
		costo_acquisto = 3.33;
		iva = 21;
		quantita = 99;
		quantita_inserzione = "Una confezione da 1 pezzo";
		dimensioni = "20,0 cm 8,0 cm 8,0 cm";
		descrizione_breve = "coppia di test studiosi.";
		descrizione = "coppia di test studiosi dai colori pastello chiaro. nuovo.";
		idCategoria1 = 80;
		idCategoria2 = 79;
		categoria1 = CategorieBusiness.getInstance().getMappaCategorie().get(idCategoria1);
		categoria2 = CategorieBusiness.getInstance().getMappaCategorie().get(idCategoria2);
		
		immagine1 = "oggettistica/ob234";
		immagine2 = "oggettistica/ob234-1";
		immagine3 = "";
		immagine4 = "";
		immagine5 = "";
		creaThumbnails = false;

		 varianti = new ArrayList<Variante_Articolo>();
		 Variante_Articolo v1 = new
		 Variante_Articolo("Colore","Azzurro","oggettistica/ob234.jpg",15);
		 varianti.add(v1);
		 Variante_Articolo v2 = new
		 Variante_Articolo("Colore","Rosa","oggettistica/ob234-6.jpg",20);
		 varianti.add(v2);

		// INFORMAZIONI GLORIAMORALDI.IT
		invia_a_gm = false;
		meta_titolo = "";
		meta_descrizione = "";
		meta_keywords = "";
		titolo_inglese = "";
		descrizione_breve_inglese = "";
		descrizione_inglese = "";
		meta_titolo_inglese = "";
		meta_descrizione_inglese = "";
		meta_keywords_inglese = "";

		// INFORMAZIONI EBAY
		invia_ad_ebay = false;
		titoloInserzione = nome+" OGGETTISTICA";
		id_categoria_ebay1 = 38232;
		id_categoria_ebay2 = 121;
		durata_inserzione = 999;
		contrassegno = false;
		costo_spedizione = 7;
		ambiente = "produzione";

		// INFORMAZIONI AMAZON
		invia_ad_amazon = false;
		idCategoriaAmazon1 = 731676031;
		idCategoriaAmazon2 = 652530031;
		paroleChiave1 = "LAMPADA CANDELA SOSPENSIONE APPESA";
		paroleChiave2 = "METALLO VETRO GRIGIO FORATO MOLLA";
		paroleChiave3 = "CASA ARREDAMENTO GIARDINO ILLUMINAZIONE";
		paroleChiave4 = "ALLESTIMENTI MATRIMONIO NOZZE";
		paroleChiave5 = "CERA PARAFFINA GANCIO CAPPA";

		invia_a_yatego = false;

		invia_a_zb = false;

		risultato_inserimento_amazon = "";
		risultato_inserimento_ebay = "";
		risultato_inserimento_gloriamoraldi = "";
		risultato_inserimento_locale = "";
		risultato_inserimento_yatego = "";
		risultato_inserimento_zb = "";
		linkEbay = "";
		linkZB = "";
		linkGM = "";

	}

	public void SvuotaCampi() {
		Log.debug("SvuotaCampi");

		art = null;
		varianti = new ArrayList<Variante_Articolo>();
		hideVariante();
		mappaErrori = null;
		modificaAbilitata = false;
		creaThumbnails = false;
		inserisci_su_gestionale = true;

		codice_articolo = "";
		codice_fornitore = "";
		codice_articolo_fornitore = "";
		codice_barre = "";
		tipo_codice_barre = "";
		nome = "";
		prezzo_dettaglio = 0;
		prezzo_ingrosso = 0;
		prezzo_piattaforme = 0;
		costo_acquisto = 0;
		iva = 21;
		quantita = 0;
		quantita_inserzione = "";
		dimensioni = "";
		descrizione_breve = "";
		descrizione = "";
		idCategoria1 = -1;
		idCategoria2 = -1;
		categoria1 = null;
		categoria2 = null;
		immagine1 = "";
		immagine2 = "";
		immagine3 = "";
		immagine4 = "";
		immagine5 = "";
		creaThumbnails = false;

		// INFORMAZIONI GLORIAMORALDI.IT
		invia_a_gm = false;
		meta_titolo = "";
		meta_descrizione = "";
		meta_keywords = "";
		titolo_inglese = "";
		descrizione_breve_inglese = "";
		descrizione_inglese = "";
		meta_titolo_inglese = "";
		meta_descrizione_inglese = "";
		meta_keywords_inglese = "";

		// INFORMAZIONI EBAY
		idEbay = "";
		invia_ad_ebay = false;
		titoloInserzione = "";
		id_categoria_ebay1 = -1;
		id_categoria_ebay2 = -1;
		svuotaCategorie1();
		svuotaCategorie2();
		durata_inserzione = 999;
		contrassegno = false;
		costo_spedizione = 7;
		linkEbay = "";

		// INFORMAZIONI YATEGO
		invia_a_yatego = false;

		// INFORMAZIONI AMAZON
		invia_ad_amazon = false;
		idCategoriaAmazon1 = -1;
		idCategoriaAmazon2 = -1;
		paroleChiave1 = "";
		paroleChiave2 = "";
		paroleChiave3 = "";
		paroleChiave4 = "";
		paroleChiave5 = "";
		voce_pacchetto_quantita = 0;
		quantita_massima_spedizione_cumulativa = 0;
		numero_pezzi = 0;

		invia_a_zb = false;

		risultato_inserimento_amazon = "";
		risultato_inserimento_ebay = "";
		risultato_inserimento_gloriamoraldi = "";
		risultato_inserimento_locale = "";
		risultato_inserimento_yatego = "";
		risultato_inserimento_zb = "";
		linkEbay = "";
		linkZB = "";
		linkGM = "";

	}

	public String getCodice_articolo() {
		return codice_articolo;
	}

	public void setCodice_articolo(String codice_articolo) {
		this.codice_articolo = codice_articolo;
	}

	public double getPrezzo_dettaglio() {
		return prezzo_dettaglio;
	}

	public void setPrezzo_dettaglio(double prezzo_dettaglio) {
		this.prezzo_dettaglio = prezzo_dettaglio;
	}

	public double getPrezzo_ingrosso() {
		return prezzo_ingrosso;
	}

	public void setPrezzo_ingrosso(double prezzo_ingrosso) {
		this.prezzo_ingrosso = prezzo_ingrosso;
	}

	public double getCosto_acquisto() {
		return costo_acquisto;
	}

	public void setCosto_acquisto(double costo_acquisto) {
		this.costo_acquisto = costo_acquisto;
	}

	public String getQuantita_inserzione() {
		return quantita_inserzione;
	}

	public void setQuantita_inserzione(String quantita_inserzione) {
		this.quantita_inserzione = quantita_inserzione;
	}

	public String getDimensioni() {
		return dimensioni;
	}

	public void setDimensioni(String dimensioni) {
		this.dimensioni = dimensioni;
	}

	public String getDescrizione_breve() {
		return descrizione_breve;
	}

	public void setDescrizione_breve(String descrizione_breve) {
		this.descrizione_breve = descrizione_breve;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
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

	public String getTitoloInserzione() {
		return titoloInserzione;
	}

	public void setTitoloInserzione(String titoloInserzione) {
		this.titoloInserzione = titoloInserzione;
	}

	public long getId_categoria_ebay1() {
		return id_categoria_ebay1;
	}

	public void setId_categoria_ebay1(long id_categoria_ebay1) {
		this.id_categoria_ebay1 = id_categoria_ebay1;
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

	public boolean isInvia_a_yatego() {
		return invia_a_yatego;
	}

	public void setInvia_a_yatego(boolean invia_a_yatego) {
		this.invia_a_yatego = invia_a_yatego;
	}

	public String getRisultato_inserimento_locale() {
		return risultato_inserimento_locale;
	}

	public void setRisultato_inserimento_locale(
			String risultato_inserimento_locale) {
		this.risultato_inserimento_locale = risultato_inserimento_locale;
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

	public List<Categoria> getCategorie() {
		if (categorie == null || categorie.isEmpty())
			categorie = CategorieBusiness.getInstance().getCategorie();
		return categorie;
	}

	public long getIdCategoria1() {
		return idCategoria1;
	}

	public void setIdCategoria1(long idCategoria1) {
		this.idCategoria1 = idCategoria1;
	}

	public void setCategorie(List<Categoria> categorie) {
		this.categorie = categorie;
	}

	public int getDurata_inserzione() {
		return durata_inserzione;
	}

	public void setDurata_inserzione(int durata_inserzione) {
		this.durata_inserzione = durata_inserzione;
	}

	public int getQuantita() {
		return quantita;
	}

	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}

	public double getCosto_spedizione() {
		return costo_spedizione;
	}

	public void setCosto_spedizione(double costo_spedizione) {
		this.costo_spedizione = costo_spedizione;
	}

	public String getCodice_barre() {
		return codice_barre;
	}

	public void setCodice_barre(String codice_barre) {
		this.codice_barre = codice_barre;
	}

	public String getMeta_titolo() {
		return meta_titolo;
	}

	public void setMeta_titolo(String meta_titolo) {
		this.meta_titolo = meta_titolo;
	}

	public String getMeta_descrizione() {
		return meta_descrizione;
	}

	public void setMeta_descrizione(String meta_descrizione) {
		this.meta_descrizione = meta_descrizione;
	}

	public String getMeta_keywords() {
		return meta_keywords;
	}

	public void setMeta_keywords(String meta_keywords) {
		this.meta_keywords = meta_keywords;
	}

	public String getTitolo_inglese() {
		return titolo_inglese;
	}

	public void setTitolo_inglese(String titolo_inglese) {
		this.titolo_inglese = titolo_inglese;
	}

	public String getDescrizione_breve_inglese() {
		return descrizione_breve_inglese;
	}

	public void setDescrizione_breve_inglese(String descrizione_breve_inglese) {
		this.descrizione_breve_inglese = descrizione_breve_inglese;
	}

	public String getDescrizione_inglese() {
		return descrizione_inglese;
	}

	public void setDescrizione_inglese(String descrizione_inglese) {
		this.descrizione_inglese = descrizione_inglese;
	}

	public String getMeta_titolo_inglese() {
		return meta_titolo_inglese;
	}

	public void setMeta_titolo_inglese(String meta_titolo_inglese) {
		this.meta_titolo_inglese = meta_titolo_inglese;
	}

	public String getMeta_descrizione_inglese() {
		return meta_descrizione_inglese;
	}

	public void setMeta_descrizione_inglese(String meta_descrizione_inglese) {
		this.meta_descrizione_inglese = meta_descrizione_inglese;
	}

	public String getMeta_keywords_inglese() {
		return meta_keywords_inglese;
	}

	public void setMeta_keywords_inglese(String meta_keywords_inglese) {
		this.meta_keywords_inglese = meta_keywords_inglese;
	}

	public int getIva() {
		return iva;
	}

	public void setIva(int iva) {
		this.iva = iva;
	}

	public long getCategoria_ebay1_level_1() {
		return categoria_ebay1_level_1;
	}

	public void setCategoria_ebay1_level_1(long categoria_ebay1_level_1) {
		this.categoria_ebay1_level_1 = categoria_ebay1_level_1;
	}

	public List<CategoriaEbay> getCategorie_ebay1_level_1() {
		if (categorie_ebay1_level_1 == null
				|| categorie_ebay1_level_1.isEmpty())
			categorie_ebay1_level_1 = CategorieBusiness
					.getCategorieEbayLevel_1();
		return categorie_ebay1_level_1;
	}

	public void setCategorie_ebay1_level_1(
			List<CategoriaEbay> categorie_ebay1_level_1) {
		this.categorie_ebay1_level_1 = categorie_ebay1_level_1;
	}

	public long getCategoria_ebay1_level_2() {
		return categoria_ebay1_level_2;
	}

	public void setCategoria_ebay1_level_2(long categoria_ebay1_level_2) {
		this.categoria_ebay1_level_2 = categoria_ebay1_level_2;
	}

	public List<CategoriaEbay> getCategorie_ebay1_level_2() {
		if (categorie_ebay1_level_2 == null)
			categorie_ebay1_level_2 = CategorieBusiness
					.getCategorieEbayLevel_2(categoria_ebay1_level_1);
		return categorie_ebay1_level_2;
	}

	public void setCategorie_ebay1_level_2(
			List<CategoriaEbay> categorie_ebay1_level_2) {
		this.categorie_ebay1_level_2 = categorie_ebay1_level_2;
	}

	public long getCategoria_ebay1_level_3() {
		return categoria_ebay1_level_3;
	}

	public void setCategoria_ebay1_level_3(long categoria_ebay1_level_3) {
		this.categoria_ebay1_level_3 = categoria_ebay1_level_3;
	}

	public List<CategoriaEbay> getCategorie_ebay1_level_3() {
		if (categorie_ebay1_level_3 == null)
			categorie_ebay1_level_3 = CategorieBusiness
					.getCategorieEbayLevel_3(categoria_ebay1_level_2);
		return categorie_ebay1_level_3;
	}

	public void setCategorie_ebay1_level_3(
			List<CategoriaEbay> categorie_ebay1_level_3) {
		this.categorie_ebay1_level_3 = categorie_ebay1_level_3;
	}

	public long getCategoria_ebay1_level_4() {
		return categoria_ebay1_level_4;
	}

	public void setCategoria_ebay1_level_4(long categoria_ebay1_level_4) {
		this.categoria_ebay1_level_4 = categoria_ebay1_level_4;
	}

	public List<CategoriaEbay> getCategorie_ebay1_level_4() {
		if (categorie_ebay1_level_4 == null)
			categorie_ebay1_level_4 = CategorieBusiness
					.getCategorieEbayLevel_4(categoria_ebay1_level_3);
		return categorie_ebay1_level_4;
	}

	public void setCategorie_ebay1_level_4(
			List<CategoriaEbay> categorie_ebay1_level_4) {
		this.categorie_ebay1_level_4 = categorie_ebay1_level_4;
	}

	public long getId_categoria_ebay2() {
		return id_categoria_ebay2;
	}

	public void setId_categoria_ebay2(long id_categoria_ebay2) {
		this.id_categoria_ebay2 = id_categoria_ebay2;
	}

	public boolean isContrassegno() {
		return contrassegno;
	}

	public void setContrassegno(boolean contrassegno) {
		this.contrassegno = contrassegno;
	}

	public String getRisultato_inserimento_gloriamoraldi() {
		return risultato_inserimento_gloriamoraldi;
	}

	public void setRisultato_inserimento_gloriamoraldi(
			String risultato_inserimento_gloriamoraldi) {
		this.risultato_inserimento_gloriamoraldi = risultato_inserimento_gloriamoraldi;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getRisultato_inserimento_ebay() {
		return risultato_inserimento_ebay;
	}

	public void setRisultato_inserimento_ebay(String risultato_inserimento_ebay) {
		this.risultato_inserimento_ebay = risultato_inserimento_ebay;
	}

	public String getCategoriaEbay1Selezionata() {
		if (id_categoria_ebay1 != -1) {
			CategoriaEbay c = CategorieBusiness.getInstance()
					.getMappaCategorieEbay().get(id_categoria_ebay1);
			if (c != null) {
				categoriaEbay1Selezionata = c.getLevel_1();
				if (c.getLevel_2() != null && !c.getLevel_2().isEmpty())
					categoriaEbay1Selezionata += " --> " + c.getLevel_2();
				if (c.getLevel_3() != null && !c.getLevel_3().isEmpty())
					categoriaEbay1Selezionata += " --> " + c.getLevel_3();
				if (c.getLevel_4() != null && !c.getLevel_4().isEmpty())
					categoriaEbay1Selezionata += " --> " + c.getLevel_4();
			}
		}
		return categoriaEbay1Selezionata;
	}

	public String getCategoriaEbay2Selezionata() {
		if (id_categoria_ebay2 != -1) {
			CategoriaEbay c = CategorieBusiness.getInstance()
					.getMappaCategorieEbay().get(id_categoria_ebay2);
			if (c != null) {
				categoriaEbay2Selezionata = c.getLevel_1();
				if (c.getLevel_2() != null && !c.getLevel_2().isEmpty())
					categoriaEbay2Selezionata += " --> " + c.getLevel_2();
				if (c.getLevel_3() != null && !c.getLevel_3().isEmpty())
					categoriaEbay2Selezionata += " --> " + c.getLevel_3();
				if (c.getLevel_4() != null && !c.getLevel_4().isEmpty())
					categoriaEbay2Selezionata += " --> " + c.getLevel_4();
			}
		}
		return categoriaEbay2Selezionata;
	}

	public long getCategoria_ebay2_level_1() {
		return categoria_ebay2_level_1;
	}

	public void setCategoria_ebay2_level_1(long categoria_ebay2_level_1) {
		this.categoria_ebay2_level_1 = categoria_ebay2_level_1;
	}

	public long getCategoria_ebay2_level_2() {
		return categoria_ebay2_level_2;
	}

	public void setCategoria_ebay2_level_2(long categoria_ebay2_level_2) {
		this.categoria_ebay2_level_2 = categoria_ebay2_level_2;
	}

	public long getCategoria_ebay2_level_3() {
		return categoria_ebay2_level_3;
	}

	public void setCategoria_ebay2_level_3(long categoria_ebay2_level_3) {
		this.categoria_ebay2_level_3 = categoria_ebay2_level_3;
	}

	public long getCategoria_ebay2_level_4() {
		return categoria_ebay2_level_4;
	}

	public void setCategoria_ebay2_level_4(long categoria_ebay2_level_4) {
		this.categoria_ebay2_level_4 = categoria_ebay2_level_4;
	}

	public List<CategoriaEbay> getCategorie_ebay2_level_1() {
		if (categorie_ebay2_level_1 == null
				|| categorie_ebay2_level_1.isEmpty())
			categorie_ebay2_level_1 = CategorieBusiness
					.getCategorieEbayLevel_1();
		return categorie_ebay2_level_1;
	}

	public void setCategorie_ebay2_level_1(
			List<CategoriaEbay> categorie_ebay2_level_1) {
		this.categorie_ebay2_level_1 = categorie_ebay2_level_1;
	}

	public List<CategoriaEbay> getCategorie_ebay2_level_2() {
		if (categorie_ebay2_level_2 == null)
			categorie_ebay2_level_2 = CategorieBusiness
					.getCategorieEbayLevel_4(categoria_ebay2_level_1);
		return categorie_ebay2_level_2;
	}

	public void setCategorie_ebay2_level_2(
			List<CategoriaEbay> categorie_ebay2_level_2) {
		this.categorie_ebay2_level_2 = categorie_ebay2_level_2;
	}

	public List<CategoriaEbay> getCategorie_ebay2_level_3() {
		if (categorie_ebay2_level_3 == null)
			categorie_ebay2_level_3 = CategorieBusiness
					.getCategorieEbayLevel_4(categoria_ebay2_level_2);
		return categorie_ebay2_level_3;
	}

	public void setCategorie_ebay2_level_3(
			List<CategoriaEbay> categorie_ebay2_level_3) {
		this.categorie_ebay2_level_3 = categorie_ebay2_level_3;
	}

	public List<CategoriaEbay> getCategorie_ebay2_level_4() {
		if (categorie_ebay2_level_4 == null)
			categorie_ebay2_level_4 = CategorieBusiness
					.getCategorieEbayLevel_4(categoria_ebay2_level_3);
		return categorie_ebay2_level_4;
	}

	public void setCategorie_ebay2_level_4(
			List<CategoriaEbay> categorie_ebay2_level_4) {
		this.categorie_ebay2_level_4 = categorie_ebay2_level_4;
	}

	public String getAmbiente() {
		return ambiente;
	}

	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}

	public void setCategoriaEbay1Selezionata(String categoriaEbay1Selezionata) {
		this.categoriaEbay1Selezionata = categoriaEbay1Selezionata;
	}

	public void setCategoriaEbay2Selezionata(String categoriaEbay2Selezionata) {
		this.categoriaEbay2Selezionata = categoriaEbay2Selezionata;
	}

	public Map<Long, Categoria> getCategorieMap() {
		if (categorieMap == null || categorieMap.isEmpty())
			categorieMap = CategorieBusiness.getInstance().getMappaCategorie();
		return categorieMap;
	}

	public void setCategorieMap(Map<Long, Categoria> categorieMap) {
		this.categorieMap = categorieMap;
	}

	public List<Variante_Articolo> getVarianti() {
		return varianti;
	}

	public void setVarianti(List<Variante_Articolo> varianti) {
		this.varianti = varianti;
	}

	public String getLinkEbay() {
		return linkEbay;
	}

	public void setLinkEbay(String linkEbay) {
		this.linkEbay = linkEbay;
	}

	public boolean isRimettiInVendita() {
		return rimettiInVendita;
	}

	public void setRimettiInVendita(boolean rimettiInVendita) {
		this.rimettiInVendita = rimettiInVendita;
	}

	public String getLinkGM() {
		return linkGM;
	}

	public void setLinkGM(String linkGM) {
		this.linkGM = linkGM;
	}

	public boolean isInserisci_su_gestionale() {
		return inserisci_su_gestionale;
	}

	public void setInserisci_su_gestionale(boolean inserisci_su_gestionale) {
		this.inserisci_su_gestionale = inserisci_su_gestionale;
	}

	public boolean isModificaAbilitata() {
		return modificaAbilitata;
	}

	public void setModificaAbilitata(boolean modificaAbilitata) {
		this.modificaAbilitata = modificaAbilitata;
	}

	public Articolo getArt() {
		return art;
	}

	public void setArt(Articolo art) {
		this.art = art;
	}

	public Map<String, String> getMappaErrori() {
		return mappaErrori;
	}

	public void setMappaErrori(Map<String, String> mappaErrori) {
		this.mappaErrori = mappaErrori;
	}

	public long getId_articolo() {
		return id_articolo;
	}

	public void setId_articolo(long id_articolo) {
		this.id_articolo = id_articolo;
	}

	public boolean isCreaThumbnails() {
		return creaThumbnails;
	}

	public void setCreaThumbnails(boolean creaThumbnails) {
		this.creaThumbnails = creaThumbnails;
	}

	public String getTipo_codice_barre() {
		return tipo_codice_barre;
	}

	public void setTipo_codice_barre(String tipo_codice_barre) {
		this.tipo_codice_barre = tipo_codice_barre;
	}

	public boolean isInvia_ad_amazon() {
		return invia_ad_amazon;
	}

	public void setInvia_ad_amazon(boolean invia_ad_amazon) {
		this.invia_ad_amazon = invia_ad_amazon;
	}

	public String getCodice_fornitore() {
		return codice_fornitore;
	}

	public void setCodice_fornitore(String codice_fornitore) {
		this.codice_fornitore = codice_fornitore;
	}

	public String getCodice_articolo_fornitore() {
		return codice_articolo_fornitore;
	}

	public void setCodice_articolo_fornitore(String codice_articolo_fornitore) {
		this.codice_articolo_fornitore = codice_articolo_fornitore;
	}

	public String getPunto_elenco_1() {
		return punto_elenco_1;
	}

	public void setPunto_elenco_1(String punto_elenco_1) {
		this.punto_elenco_1 = punto_elenco_1;
	}

	public String getPunto_elenco_2() {
		return punto_elenco_2;
	}

	public void setPunto_elenco_2(String punto_elenco_2) {
		this.punto_elenco_2 = punto_elenco_2;
	}

	public String getPunto_elenco_3() {
		return punto_elenco_3;
	}

	public void setPunto_elenco_3(String punto_elenco_3) {
		this.punto_elenco_3 = punto_elenco_3;
	}

	public String getPunto_elenco_4() {
		return punto_elenco_4;
	}

	public void setPunto_elenco_4(String punto_elenco_4) {
		this.punto_elenco_4 = punto_elenco_4;
	}

	public String getPunto_elenco_5() {
		return punto_elenco_5;
	}

	public void setPunto_elenco_5(String punto_elenco_5) {
		this.punto_elenco_5 = punto_elenco_5;
	}

	public String getEsclusione_responsabilita() {
		return esclusione_responsabilita;
	}

	public void setEsclusione_responsabilita(String esclusione_responsabilita) {
		this.esclusione_responsabilita = esclusione_responsabilita;
	}

	public String getDescrizione_garanzia_venditore() {
		return descrizione_garanzia_venditore;
	}

	public void setDescrizione_garanzia_venditore(
			String descrizione_garanzia_venditore) {
		this.descrizione_garanzia_venditore = descrizione_garanzia_venditore;
	}

	public String getAvvertenze_sicurezza() {
		return avvertenze_sicurezza;
	}

	public void setAvvertenze_sicurezza(String avvertenze_sicurezza) {
		this.avvertenze_sicurezza = avvertenze_sicurezza;
	}

	public String getNota_condizioni() {
		return nota_condizioni;
	}

	public void setNota_condizioni(String nota_condizioni) {
		this.nota_condizioni = nota_condizioni;
	}

	public int getVoce_pacchetto_quantita() {
		return voce_pacchetto_quantita;
	}

	public void setVoce_pacchetto_quantita(int voce_pacchetto_quantita) {
		this.voce_pacchetto_quantita = voce_pacchetto_quantita;
	}

	public int getNumero_pezzi() {
		return numero_pezzi;
	}

	public void setNumero_pezzi(int numero_pezzi) {
		this.numero_pezzi = numero_pezzi;
	}

	public int getQuantita_massima_spedizione_cumulativa() {
		return quantita_massima_spedizione_cumulativa;
	}

	public void setQuantita_massima_spedizione_cumulativa(
			int quantita_massima_spedizione_cumulativa) {
		this.quantita_massima_spedizione_cumulativa = quantita_massima_spedizione_cumulativa;
	}

	public String getPaese_origine() {
		return paese_origine;
	}

	public void setPaese_origine(String paese_origine) {
		this.paese_origine = paese_origine;
	}

	public double getLunghezza_articolo() {
		return lunghezza_articolo;
	}

	public void setLunghezza_articolo(double lunghezza_articolo) {
		this.lunghezza_articolo = lunghezza_articolo;
	}

	public double getAltezza_articolo() {
		return altezza_articolo;
	}

	public void setAltezza_articolo(double altezza_articolo) {
		this.altezza_articolo = altezza_articolo;
	}

	public double getPeso_articolo() {
		return peso_articolo;
	}

	public void setPeso_articolo(double peso_articolo) {
		this.peso_articolo = peso_articolo;
	}

	public String getUnita_misura_peso_articolo() {
		return unita_misura_peso_articolo;
	}

	public void setUnita_misura_peso_articolo(String unita_misura_peso_articolo) {
		this.unita_misura_peso_articolo = unita_misura_peso_articolo;
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

	public String getRisultato_inserimento_amazon() {
		return risultato_inserimento_amazon;
	}

	public void setRisultato_inserimento_amazon(
			String risultato_inserimento_amazon) {
		this.risultato_inserimento_amazon = risultato_inserimento_amazon;
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

	public long getIdCategoriaAmazon1() {
		return idCategoriaAmazon1;
	}

	public void setIdCategoriaAmazon1(long idCategoriaAmazon1) {
		this.idCategoriaAmazon1 = idCategoriaAmazon1;
	}

	public long getIdCategoriaAmazon2() {
		return idCategoriaAmazon2;
	}

	public void setIdCategoriaAmazon2(long idCategoriaAmazon2) {
		this.idCategoriaAmazon2 = idCategoriaAmazon2;
	}

	public Articolo getUltimoArticolo() {
		return ultimoArticolo;
	}

	public void setUltimoArticolo(Articolo ultimoArticolo) {
		this.ultimoArticolo = ultimoArticolo;
	}

	public String getRisultato_inserimento_yatego() {
		return risultato_inserimento_yatego;
	}

	public void setRisultato_inserimento_yatego(
			String risultato_inserimento_yatego) {
		this.risultato_inserimento_yatego = risultato_inserimento_yatego;
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

	public int getTempi_gestione() {
		return tempi_gestione;
	}

	public void setTempi_gestione(int tempi_gestione) {
		this.tempi_gestione = tempi_gestione;
	}

	public String getRisultato_inserimento_zb() {
		return risultato_inserimento_zb;
	}

	public void setRisultato_inserimento_zb(String risultato_inserimento_zb) {
		this.risultato_inserimento_zb = risultato_inserimento_zb;
	}

	public String getLinkZB() {
		return linkZB;
	}

	public void setLinkZB(String linkZB) {
		this.linkZB = linkZB;
	}

	public boolean isInvia_a_zb() {
		return invia_a_zb;
	}

	public void setInvia_a_zb(boolean invia_a_zb) {
		this.invia_a_zb = invia_a_zb;
	}

	public boolean isBoxBomboniere() {
		return boxBomboniere;
	}

	public void setBoxBomboniere(boolean boxBomboniere) {
		this.boxBomboniere = boxBomboniere;
	}

	public boolean isBoxSecco() {
		return boxSecco;
	}

	public void setBoxSecco(boolean boxSecco) {
		this.boxSecco = boxSecco;
	}

	public double getPrezzo_piattaforme() {
		return prezzo_piattaforme;
	}

	public void setPrezzo_piattaforme(double prezzo_piattaforme) {
		this.prezzo_piattaforme = prezzo_piattaforme;
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

	public List<CategoriaEbay> getCategorieEbay() {
		if (categorieEbay == null || categorieEbay.isEmpty())
			categorieEbay = CategorieBusiness.getInstance().getCategorieEbay();
		return categorieEbay;
	}

	public void setCategorieEbay(List<CategoriaEbay> categorieEbay) {
		this.categorieEbay = categorieEbay;
	}

	public long getIdCategoria2() {
		return idCategoria2;
	}

	public void setIdCategoria2(long idCategoria2) {
		this.idCategoria2 = idCategoria2;
	}

	public Categoria getCategoria1() {
		if (idCategoria1>0) 
			categoria1 = CategorieBusiness.getInstance().getMappaCategorie().get(idCategoria1);
		return categoria1;
	}

	public void setCategoria1(Categoria categoria1) {
		this.categoria1 = categoria1;
	}

	public Categoria getCategoria2() {
		if (idCategoria2>0) 
			categoria2 = CategorieBusiness.getInstance().getMappaCategorie().get(idCategoria2);
		return categoria2;
	}

	public void setCategoria2(Categoria categoria2) {
		this.categoria2 = categoria2;
	}
	
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getNomeCategoriaEbay1() {
		if (id_categoria_ebay1>0){
			CategoriaEbay c = CategorieBusiness.getInstance().getMappaCategorieEbay().get(id_categoria_ebay1);
			if (c!=null) nomeCategoriaEbay1 = c.getLevel_1()+c.getLevel_2()+c.getLevel_3()+c.getLevel_4();
		}
		return nomeCategoriaEbay1;
	}

	public void setNomeCategoriaEbay1(String nomeCategoriaEbay1) {
		this.nomeCategoriaEbay1 = nomeCategoriaEbay1;
	}

	public String getNomeCategoriaEbay2() {
		if (id_categoria_ebay2>0){
			CategoriaEbay c = CategorieBusiness.getInstance().getMappaCategorieEbay().get(id_categoria_ebay2);
			if (c!=null) nomeCategoriaEbay2 = c.getLevel_1()+c.getLevel_2()+c.getLevel_3()+c.getLevel_4();
		}
		return nomeCategoriaEbay2;
	}

	public void setNomeCategoriaEbay2(String nomeCategoriaEbay2) {
		this.nomeCategoriaEbay2 = nomeCategoriaEbay2;
	}

	public String getNomeCategoriaAmazon1() {
		if (idCategoriaAmazon1>0)
			nomeCategoriaAmazon1 = CategorieBusiness.getInstance().getMappaCategorieAmazon().get(idCategoriaAmazon1);
		return nomeCategoriaAmazon1;
	}

	public void setNomeCategoriaAmazon1(String nomeCategoriaAmazon1) {
		this.nomeCategoriaAmazon1 = nomeCategoriaAmazon1;
	}

	public String getNomeCategoriaAmazon2() {
		if (idCategoriaAmazon2>0)
			nomeCategoriaAmazon2 = CategorieBusiness.getInstance().getMappaCategorieAmazon().get(idCategoriaAmazon2);
		return nomeCategoriaAmazon2;
	}

	public void setNomeCategoriaAmazon2(String nomeCategoriaAmazon2) {
		this.nomeCategoriaAmazon2 = nomeCategoriaAmazon2;
	}

	public String getIdEbay() {
		return idEbay;
	}

	public void setIdEbay(String idEbay) {
		this.idEbay = idEbay;
	}

}
