package it.swb.piattaforme.amazon;

import it.swb.log.Log;
import it.swb.model.Articolo;
import it.swb.model.Variante_Articolo;
import it.swb.utility.Costanti;
import it.swb.utility.DateMethods;
import it.swb.utility.Methods;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class EditorModelliAmazon {
	
	public static int aggiungiAModelloAmazon(Articolo a, String percorso){		
		 //Log.debug("Aggiungo l'articolo al modello di caricamento di Amazon");
		 int risultato = 0;
	     try {
			
	    	 boolean esiste = true;
				
			File f = new File(percorso);
			
			if (!f.exists()) {
				esiste = false;
				f.createNewFile();
			}
						
			FileOutputStream fos = new FileOutputStream (percorso, true);
			
			PrintWriter pw = new PrintWriter (fos);
			
			if (!esiste) {
				//System.out.println("il file non esisteva");
				pw.println("TemplateType=Home	Version=1.0	This row for Amazon.com use only.  Do not modify or delete.		");
				pw.println("sku	StandardProductId	ProductIdType	ProductName	Brand	Manufacturer	Description	BulletPoint1	BulletPoint2	BulletPoint3	BulletPoint4	BulletPoint5	RecommendedBrowseNode1	RecommendedBrowseNode2	ProductType	MainImageUrl	OtherImageUrl1	OtherImageUrl2	OtherImageUrl3	OtherImageUrl4	OtherImageUrl5	OtherImageUrl6	OtherImageUrl7	OtherImageUrl8	SwatchImageUrl	LegalDisclaimer	SellerWarrantyDescription	ManufacturerSafetyWarning	UpdateDelete	ItemPrice	Currency	Quantity	ConditionType	ConditionNote	ItemPackageQuantity	NumberOfPieces	LaunchDate	ReleaseDate	LeadtimeToShip	RestockDate	MaxAggregateShipQuantity	CountryProducedIn	ShippingWeight	ShippingWeightUnitOfMeasure	ItemLength	ItemHeight	ItemWeight	ItemWeightUnitOfMeasure	ManufacturerPartNumber	SearchTerms1	SearchTerms2	SearchTerms3	SearchTerms4	SearchTerms5	SalesPrice	SaleEndDate	SaleStartDate	Parentage	ParentSku	RelationshipType	VariationTheme	Size	SizeMap	Color	ColorMap");
			}			
			/*	Controllo se il prodotto ha varianti. */
			if (a.getVarianti()!=null && !a.getVarianti().isEmpty()){
				
				/*	Se ha varianti creo prima un prodotto genitore fantoccio	*/
/* ---> */			aggiungiParent(a,pw, false);
				
				for (Variante_Articolo v : a.getVarianti()){
					/*	Ora aggiungo le varianti una ad una	*/
					if (v.getQuantita()>0)
/* ---> */				aggiungiChild(a,v,pw, false);
				//	aggiungiVariante(a,v,pw);		<-------
				}
			}
			else if (a.getCodiceBarre()==null || a.getCodiceBarre().trim().isEmpty())
				aggiungiProdottoSenzaCodiceBarre(a, pw, false);
			
			else aggiungiProdotto(a,pw,false);
			
			pw.close();
			
			risultato = 1;
	
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
			risultato = 0;
		}		
	     //Log.debug("Fine del processo di aggiunta dell'articolo al modello Amazon");
	     return risultato;
	}
	
	/** Se l'attributo delete è true l'articolo viene caricato per essere eliminato */
	public static String aggiungiProdottoAModelloAmazon(Articolo a, boolean delete){		
		 Log.debug("Aggiungo l'articolo al modello di caricamento di Amazon");
		 Properties config = new Properties();	   
		 String percorso = null;
		 
	     try {
			config.load(Log.class.getResourceAsStream("/zeus.properties"));
			
			percorso = config.getProperty("percorso_modello_caricamento_dati_amazon");		
			String data = DateMethods.getDataCompletaPerNomeFileTesto();
			
			percorso = percorso.replace("DATA", data);
			
			if (delete) percorso = percorso.replace(".txt", "_delete.txt");
			
			//System.out.println("Percorso file: "+percorso);
			
			boolean esiste = true;
			
			File f = new File(percorso);
			
			if (!f.exists()) {
				esiste = false;
				f.createNewFile();
			}
			
			FileOutputStream fos = new FileOutputStream (percorso, true);
			
			PrintWriter pw = new PrintWriter (fos);
			
			if (!esiste) {
				//System.out.println("il file non esisteva");
				pw.println("TemplateType=Home	Version=1.0	This row for Amazon.com use only.  Do not modify or delete.		");
				pw.println("sku	StandardProductId	ProductIdType	ProductName	Brand	Manufacturer	Description	BulletPoint1	BulletPoint2	BulletPoint3	BulletPoint4	BulletPoint5	RecommendedBrowseNode1	RecommendedBrowseNode2	ProductType	MainImageUrl	OtherImageUrl1	OtherImageUrl2	OtherImageUrl3	OtherImageUrl4	OtherImageUrl5	OtherImageUrl6	OtherImageUrl7	OtherImageUrl8	SwatchImageUrl	LegalDisclaimer	SellerWarrantyDescription	ManufacturerSafetyWarning	UpdateDelete	ItemPrice	Currency	Quantity	ConditionType	ConditionNote	ItemPackageQuantity	NumberOfPieces	LaunchDate	ReleaseDate	LeadtimeToShip	RestockDate	MaxAggregateShipQuantity	CountryProducedIn	ShippingWeight	ShippingWeightUnitOfMeasure	ItemLength	ItemHeight	ItemWeight	ItemWeightUnitOfMeasure	ManufacturerPartNumber	SearchTerms1	SearchTerms2	SearchTerms3	SearchTerms4	SearchTerms5	SalesPrice	SaleEndDate	SaleStartDate	Parentage	ParentSku	RelationshipType	VariationTheme	Size	SizeMap	Color	ColorMap");
			}
			
			/*	Controllo se il prodotto ha varianti. */
			if (a.getVarianti()!=null && !a.getVarianti().isEmpty()){
				
				/*	Se ha varianti creo prima un prodotto genitore fantoccio	*/
/* ---> */			aggiungiParent(a,pw, delete);
				
				for (Variante_Articolo v : a.getVarianti()){
					/*	Ora aggiungo le varianti una ad una	*/
					if (v.getQuantita()>0)
/* ---> */				aggiungiChild(a,v,pw, delete);
				//	aggiungiVariante(a,v,pw);		<-------
				}
			}
			else if (a.getCodiceBarre()==null || a.getCodiceBarre().trim().isEmpty())
				aggiungiProdottoSenzaCodiceBarre(a, pw, delete);
			
			else aggiungiProdotto(a,pw,delete);
			
			pw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}		
	     Log.debug("Fine del processo di aggiunta dell'articolo al modello Amazon");
	     return percorso;
	}
	
	
	public static int aggiungiProdottoEsistenteAModelloAmazon(Articolo a){		
		 Log.debug("Start aggiungiProdottoEsistenteAModelloAmazon");
		 Properties config = new Properties();	   
		 int risultato = 0;
	     try {
			config.load(Log.class.getResourceAsStream("/zeus.properties"));
									
			String percorso = config.getProperty("percorso_listing_loader_amazon")+DateMethods.getMesePerNomeFileTesto();		
			String data = DateMethods.getMesePerNomeFileTesto();
			percorso = percorso.replace("DATA", data);
			//System.out.println("Percorso file: "+percorso);
			
			boolean esiste = true;
			
			File f = new File(percorso);
			
			if (!f.exists()) {
				esiste = false;
				f.createNewFile();
			}
			
			FileOutputStream fos = new FileOutputStream (percorso, true);
			
			PrintWriter pw = new PrintWriter (fos);
			
			if (!esiste) {
				//System.out.println("il file non esiste");
				pw.println("TemplateType=Offer	Version=1.4");
				pw.println("sku	price	quantity	product-id	product-id-type	condition-type	condition-note	ASIN-hint	title	product-tax-code	operation-type	sale-price	sale-start-date	sale-end-date	leadtime-to-ship	launch-date	is-giftwrap-available	is-gift-message-available	fulfillment-center-id");
			}
			
			if (a.getVarianti()!=null && !a.getVarianti().isEmpty()){
				
				for (Variante_Articolo v : a.getVarianti()){
					aggiungiProdottoEsistente(a,v,pw);
				}
			} else aggiungiProdottoEsistente(a,pw);
			
			pw.close();
			
			risultato = 1;
	
		} catch (IOException e) {
			e.printStackTrace();
			Log.error(e.getMessage());
			risultato = 0;
		}		
	     Log.info(risultato);
	     Log.debug("Fine aggiungiProdottoEsistenteAModelloAmazon");
	     return risultato;
	}
	
	
	public static int spedizioneGratis(Articolo a){		
		 Log.debug("Start aggiungiProdottoSpedizioneGratis");
		 Properties config = new Properties();	   
		 int risultato = 0;
	     try {
			config.load(Log.class.getResourceAsStream("/zeus.properties"));
									
			String percorso = config.getProperty("percorso_shipping_overrides_amazon")+DateMethods.getMesePerNomeFileTesto();		
			String data = DateMethods.getMesePerNomeFileTesto();
			percorso = percorso.replace("DATA", data);
			//System.out.println("Percorso file: "+percorso);
			
			boolean esiste = true;
			
			File f = new File(percorso);
			
			if (!f.exists()) {
				esiste = false;
				f.createNewFile();
			}
			
			FileOutputStream fos = new FileOutputStream (percorso, true);
			
			PrintWriter pw = new PrintWriter (fos);
			
			if (!esiste) {
				//System.out.println("il file non esiste");
				pw.println("TemplateType=Overrides	Version=2.0	Questa riga è riservata ad Amazon.it.  Non modificare, né eliminare.");
				pw.println("sku	Currency	ShipOption1	ShippingAmt1	Type1	IsShippingRestricted1	ShipOption2	ShippingAmt2	Type2	IsShippingRestricted2	ShipOption3	ShippingAmt3	Type3	IsShippingRestricted3	ShipOption4	ShippingAmt4	Type4	IsShippingRestricted4	ShipOption5	ShippingAmt5	Type5	IsShippingRestricted5	ShipOption6	ShippingAmt6	Type6	IsShippingRestricted6	UpdateDelete");
			}
			
			/*if (a.getVarianti()!=null && !a.getVarianti().isEmpty()){
				
				for (Variante_Articolo v : a.getVarianti()){
					aggiungiProdottoSenzaSpedizione(a,v,pw);
				}
			} else */
			spedizioneGratis(a,null,pw);
			
			pw.close();
			
			risultato = 1;
	
		} catch (IOException e) {
			e.printStackTrace();
			Log.error(e.getMessage());
			risultato = 0;
		}		
	     Log.info(risultato);
	     Log.debug("Fine aggiungiProdottoSpedizioneGratis");
	     return risultato;
	}
	
	public static void eliminaArticolo(){
		
	}
	
	public static String generaModelloConfermaSpedizioni(List<Map<String,String>> numeriTracciamento){		
		Properties config = new Properties();	   
		String nomeFile = "";
		 Log.info("Generazione del modello di conferma spedizioni Amazon...");
		 
	     try {
	    	 config.load(Log.class.getResourceAsStream("/zeus.properties"));
				
			String percorsoFile = config.getProperty("percorso_conferma_spedizioni");	
			nomeFile = config.getProperty("nome_file_conferma_spedizioni");
    	
			String data = DateMethods.getDataCompletaPerNomeFileTesto();
			
			nomeFile = nomeFile.replace("DATA", data);
			
			String percorso = percorsoFile+nomeFile;
			
			File f = new File(percorso);
			
			if (!f.exists()) {
				f.createNewFile();
			}
						
			FileOutputStream fos = new FileOutputStream (percorso, true);
			
			PrintWriter pw = new PrintWriter (fos);
			
			pw.println("order-id	order-item-id	quantity	ship-date	carrier-code	carrier-name	tracking-number	ship-method");
			
			for (Map<String,String> num : numeriTracciamento){
				aggiungiSpedizione(num,pw);	
			}
			
			pw.close();
			
			
			Log.info("Generazione completata. Nome del file: "+nomeFile);
	
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
			nomeFile = "error";
		}		
	     return nomeFile;
	}

	
	
	private static void aggiungiProdotto(Articolo a, PrintWriter pw, boolean delete){
		//System.out.println("Inizio a scrivere sul file");
		
		/* inizio product basic information */
		
		//(A) SKU 
		pw.print(a.getCodice());
		pw.print("	");
	    
	    //(B) CODICE BARRE
		if (a.getCodiceBarre()!=null && !a.getCodiceBarre().trim().isEmpty())
			pw.print(a.getCodiceBarre().toUpperCase().trim());
		pw.print("	");
	    
	    //(C) TIPO CODICE (EAN) 
		if (a.getCodiceBarre()!=null && !a.getCodiceBarre().trim().isEmpty())
			pw.print("EAN");
		pw.print("	");
	    
	    //(D) NOME ARTICOLO 
		String nome = Methods.primeLettereMaiuscole(a.getNome());
		if (a.getCodice().contains("ZELDA")){
			nome = nome+"("+a.getCodice()+")";
		} else if (a.getCodice().contains("TORTA")){
			nome = nome+"("+a.getCodice().replace("BOMBONIERE-", "").replace("BOMBONIERE_", "")+")";
		}
		pw.print(nome);
		pw.print("	");

	    //(E) MARCA
		if (a.getCategoria().getNomeCategoria()!=null)
			pw.print(a.getCategoria().getNomeCategoria());
		else pw.print("Zelda Bomboniere");
		pw.print("	");

	    //(F) PRODUTTORE 
		pw.print("Zelda Bomboniere");
		pw.print("	");

	    //(G) DESCRIZIONE 
		pw.print(Methods.MaiuscolaDopoPunto(a.getDescrizione()).replaceAll("[\n\r]", ""));
		pw.print("	");

	    //(H) PUNTO ELENCO 1 
	    if (a.getQuantitaInserzione()!=null && !a.getQuantitaInserzione().trim().isEmpty())
	    	pw.print("Quantità inserzione: "+Methods.primeLettereMaiuscole(a.getQuantitaInserzione()));  
	    pw.print("	");

	    //(I) PUNTO ELENCO 2 
	    if (a.getDimensioni()!=null && !a.getDimensioni().trim().isEmpty()){
	    	pw.print("Dimensioni: "+Methods.primeLettereMaiuscole(a.getDimensioni()).replace("Cm", "cm")); 
		}
	    pw.print("	");

	    //(J) PUNTO ELENCO 3 
	    /*if (a.getInfoAmazon().getPuntoElenco3()!=null && !a.getInfoAmazon().getPuntoElenco3().trim().isEmpty()){
	    	pw.print(a.getInfoAmazon().getPuntoElenco3()); 
		}*/
	    pw.print("Codice Articolo: "+a.getCodice());
	    pw.print("	");

	    //(K) PUNTO ELENCO 4 
	    /*if (a.getInfoAmazon().getPuntoElenco4()!=null && !a.getInfoAmazon().getPuntoElenco4().trim().isEmpty()){
	    	pw.print(a.getInfoAmazon().getPuntoElenco4()); 
		}*/
	    pw.print(Methods.cut500("Descrizione: "+Methods.MaiuscolaDopoPunto(a.getDescrizione()).replaceAll("[\n\r]", "")));
	    pw.print("	");

	    //(L) PUNTO ELENCO 5 
	    if (a.getInfoAmazon().getPuntoElenco5()!=null && !a.getInfoAmazon().getPuntoElenco5().trim().isEmpty()){
	    	pw.print(a.getInfoAmazon().getPuntoElenco5()); 
		}
	    pw.print("	");

	    //(M) NODO NAVIGAZIONE 1
	    if (a.getInfoAmazon().getIdCategoria1()!=null 
	    		&& !a.getInfoAmazon().getIdCategoria1().trim().isEmpty()
	    		&& !a.getInfoAmazon().getIdCategoria1().trim().equals("null")
		    	&& a.getInfoAmazon().getIdCategoria1()!="-1"
		    	&& a.getInfoAmazon().getIdCategoria1()!="0")
	    	pw.print(a.getInfoAmazon().getIdCategoria1());  
	    else pw.print("731697031");
	    pw.print("	");

	    //(N) NODO NAVIGAZIONE 2 
	    if (a.getInfoAmazon().getIdCategoria2()!=null 
		    	&& !a.getInfoAmazon().getIdCategoria2().trim().isEmpty() 
		    	&& !a.getInfoAmazon().getIdCategoria2().trim().equals("null")
		    	&& a.getInfoAmazon().getIdCategoria2()!="-1"
		    	&& a.getInfoAmazon().getIdCategoria2()!="0")
	    	pw.print(a.getInfoAmazon().getIdCategoria2());  
	    else pw.print("2906926031");
	    pw.print("	");

	    //(O) TIPO PRODOTTO 
	    pw.print("FurnitureAndDecor"); 
	    pw.print("	");
	    
	    /* fine product basic information */
	    
	    /* inizio informazioni sull'immagine */
	    
	    //(BB) URL IMMAGINE PRINCIPALE
	    if (!delete)
	    	pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine1()); 
	    pw.print("	");
	    
	    //(BC) URL ALTRA IMMAGINE 1
	    if (!delete && a.getImmagine2()!=null && !a.getImmagine2().trim().isEmpty()){
	    	pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine2()+"");  
	    }
	    pw.print("	");
	    
	    //(BD) URL ALTRA IMMAGINE 2
	    if (!delete && a.getImmagine3()!=null && !a.getImmagine3().trim().isEmpty()){
	    	pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine3());  
	    }
	    pw.print("	");
	    
	    //(BE) URL ALTRA IMMAGINE 3
	    if (!delete && a.getImmagine4()!=null && !a.getImmagine4().trim().isEmpty()){
	    	pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine4());  
	    }
	    pw.print("	");
	    
	    //(BF) URL ALTRA IMMAGINE 4
	    if (!delete && a.getImmagine5()!=null && !a.getImmagine5().trim().isEmpty()){
	    	pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine5());  
	    }
	    pw.print("	");
	    
	    //(BG) URL ALTRA IMMAGINE 5	
	    pw.print("	");
		//(BH) URL ALTRA IMMAGINE 6
	    pw.print("	");
		//(BI) URL ALTRA IMMAGINE 7
	    pw.print("	");
		//(BJ) URL ALTRA IMMAGINE 8
	    pw.print("	");
	    
	    //(BK) URL IMMAGINE SWITCH
	    pw.print("	");
	    
	    /* fine informazioni sull'immagine */
	    
	    /* inizio informazioni legali */
	    
	    //(BL) ESCLUSIONE RESPOSABILITA'
	    if (a.getInfoAmazon().getEsclusioneResponsabilita()!=null && !a.getInfoAmazon().getEsclusioneResponsabilita().trim().isEmpty())
	    	pw.print(a.getInfoAmazon().getEsclusioneResponsabilita());
	    pw.print("	");
	    
	    //(BM) DESCRIZIONE GARANZIA VENDITORE
	    if (a.getInfoAmazon().getDescrizioneGaranziaVenditore()!=null && !a.getInfoAmazon().getDescrizioneGaranziaVenditore().trim().isEmpty())
	   	 	pw.print(a.getInfoAmazon().getDescrizioneGaranziaVenditore());
	    pw.print("	");
	    
	    //(BN) AVVERTENZE SICUREZZA
	    if (a.getInfoAmazon().getAvvertenzeSicurezza()!=null && !a.getInfoAmazon().getAvvertenzeSicurezza().trim().isEmpty())
	    	pw.print(a.getInfoAmazon().getAvvertenzeSicurezza());
	    pw.print("	");
	    
	    /* fine informazioni legali */
	    
	    //(BO) AGGIORNA O RIMUOVI
	    if (delete) pw.print("Delete");
	    pw.print("	");
	    
	    /* inizio informazioni sull'offerta */
	    
	    //(BP) PREZZO
	    double prezzo = a.getPrezzoDettaglio();
  	  	if (a.getPrezzoPiattaforme()>0) prezzo = a.getPrezzoPiattaforme();
	    pw.print(String.valueOf(prezzo).replace(",", ".")); 
	    pw.print("	");
	    
	    //(BQ) VALUTA
	    pw.print("EUR"); 
	    pw.print("	");
	    
	    //(BR) QUANTITA'
	    if (a.getQuantitaMagazzino()>0)
	    	pw.print(a.getQuantitaMagazzino());  
	    else pw.print(20);  
	    pw.print("	");
	    
	    //(BS) CONDIZIONI
	    pw.print("New"); 
	    pw.print("	");
	    
	    //(BT) NOTA CONDIZIONI
	    if (a.getInfoAmazon().getNotaCondizioni()!=null && !a.getInfoAmazon().getNotaCondizioni().trim().isEmpty())
	    	pw.print(a.getInfoAmazon().getNotaCondizioni());
	    pw.print("	");
	    
	    //(BU) VOCE PACCHETTO QUANTITA'
	    if (a.getInfoAmazon().getVocePacchettoQuantita()!=0)
	    	pw.print(a.getInfoAmazon().getVocePacchettoQuantita());
	    else pw.print("1");
	    pw.print("	");
	    
	    //(BV) NUMERO PEZZI
	    if (a.getInfoAmazon().getNumeroPezzi()!=0)
	    	pw.print(a.getInfoAmazon().getNumeroPezzi());
	    else pw.print("1");
	    pw.print("	");
	    
	    //(BW) DATA LANCIO
	    pw.print("	");
	    
	    //(BX) DATA RILASCIO
	    pw.print("	");
	    
	    //(BY) TEMPI ESECUZIONE SPEDIZIONE
	    String tempi = "";
	    if (a.getInfoAmazon().getTempiEsecuzioneSpedizione()!=0)
	    	tempi = String.valueOf(a.getInfoAmazon().getTempiEsecuzioneSpedizione());
	    else	if (a.getCodice().contains("ZELDA") || a.getCodice().contains("TORTA")){
	    	tempi = "7";
		}
	    pw.print(tempi);
	    pw.print("	");
	    
	    //(BZ) DATA RIFORNIMENTO
	    pw.print("	");
	    
	    //(CA) QUANTITA' MASSIMA SPEDIZIONE CUMULATIVA
	    pw.print("200");
	    pw.print("	");
	    
	    //(CB) PAESE DI ORIGINE
	    if (a.getInfoAmazon().getPaeseOrigine()!=null && !a.getInfoAmazon().getPaeseOrigine().trim().isEmpty())
	    	pw.print(a.getInfoAmazon().getPaeseOrigine());
	    pw.print("	");
	    
	    /* fine informazioni sull'offerta */
	    
	    /* inizio dimensioni prodotto */
	    
	    //(GG) PESO SPEDIZIONE
	    pw.print("	");
	    
	    //(GH) UNITA' MISURA PESO SPEDIZIONE
	    pw.print("	");
	    
	    //(GI) LUNGHEZZA ARTICOLO
	    if (a.getInfoAmazon().getLunghezzaArticolo()!=0)
	    	pw.print(a.getInfoAmazon().getLunghezzaArticolo()); 
	    pw.print("	");
	    
	    //(GJ) ALTEZZA ARTICOLO
	    if (a.getInfoAmazon().getAltezzaArticolo()!=0)
	    	pw.print(a.getInfoAmazon().getAltezzaArticolo()); 
	    pw.print("	");
	    
	    //(GK) PESO ARTICOLO
	    if (a.getInfoAmazon().getPesoArticolo()!=0)
	    	pw.print(a.getInfoAmazon().getPesoArticolo()); 
	    pw.print("	");
	    
	    //(GL) UNITA' MISURA PESO ARTICOLO
	    if (a.getInfoAmazon().getUnitaMisuraPesoArticolo()!=null && !a.getInfoAmazon().getUnitaMisuraPesoArticolo().trim().isEmpty())
	    	pw.print(a.getInfoAmazon().getUnitaMisuraPesoArticolo()); 
	    pw.print("	");
	    
	    /* fine dimensioni prodotto */
	    
	    /* inizio informazioni sulla scoperta del prodotto */
	    
	    //(GM) CODICE ARTICOLO DEL PRODUTTORE (MFR, MANIFACTURER PART NUMBER)
	    if (a.getCodiceArticoloFornitore()!=null && !a.getCodiceArticoloFornitore().trim().isEmpty())
	    	pw.print(a.getCodiceArticoloFornitore());
	    else pw.print(a.getCodice());
	    pw.print("	");
	    
	    //(GN) PAROLE CHIAVE 1
	    if (a.getParoleChiave1()!=null && !a.getParoleChiave1().trim().isEmpty())
	    	pw.print(Methods.cut100(a.getParoleChiave1()));
	    pw.print("	");
	    
	    //(GO) PAROLE CHIAVE 2
	    if (a.getParoleChiave2()!=null && !a.getParoleChiave2().trim().isEmpty())
	    	pw.print(Methods.cut100(a.getParoleChiave2()));
	    pw.print("	");
	    
	    //(GP) PAROLE CHIAVE 3
	    if (a.getParoleChiave3()!=null && !a.getParoleChiave3().trim().isEmpty())
	    	pw.print(Methods.cut100(a.getParoleChiave3()));
	    pw.print("	");
	    
	    //(GQ) PAROLE CHIAVE 4
	    if (a.getParoleChiave4()!=null && !a.getParoleChiave4().trim().isEmpty())
	    	pw.print(Methods.cut100(a.getParoleChiave4()));
	    pw.print("	");
	    
	    //(GR) PAROLE CHIAVE 5
	    if (a.getParoleChiave5()!=null && !a.getParoleChiave5().trim().isEmpty())
	    	pw.print(Methods.cut100(a.getParoleChiave5()));
	    pw.print("	");
	    
	    /* fine informazioni sulla scoperta del prodotto */
	    
	    /* inizio informazioni prezzo saldo e ribasso */
	    
	    //(GS) PREZZO DI VENDITA
	    pw.print("	");
	    
	    //(GT) DATA FINE SALDO
	    pw.print("	");
	    
	    //(GU) DATA INIZIO SALDO
	    pw.print("	");
	    
	    /* fine informazioni prezzo saldo e ribasso */
	    
	    /* inizio informazioni varianti */
	    
	  	//(GV) FILIAZIONE (parent, child)
	    pw.print("	");
	    
	  	//(GW) PARENT SKU
	    pw.print("	");
	    
	  	//(GX) TIPO RELAZIONE (variante, accessorio)
	    pw.print("	");
	    
	  	//(GY) TEMA VARIANTE (colore, taglia, taglia-colore,fragranza, taglia-fragranza)
	    pw.print("	");
	    
	  	//(GZ) DIMENSIONI (SE IL TEMA è TAGLIA, ES. VALORE VALIDO: S)
	    pw.print("	");
	    
	  	//(HA) MAPPA DIMENSIONI
	    pw.print("	");
	    
	  	//(HB) COLORE
	    pw.print("	");
	    
	  	//(HC) MAPPA COLORE
	    pw.print("	");
	    
	    /* fine informazioni varianti */
	    
	    /* fine !!! */
	    
	    pw.println();
		
	}
	
//	private static void aggiungiVariante(Articolo a,Variante_Articolo v, PrintWriter pw){
//		//System.out.println("Inizio a scrivere sul file");
//		
//		/* inizio product basic information */
//		
//		//(A) SKU 
//		pw.print(a.getCodice()+"-"+v.getValore().trim().replace(" ", "_"));
//		pw.print("	");
//	    
//	    //(B) CODICE BARRE
//		if (v.getCodiceBarre()!=null && !v.getCodiceBarre().trim().isEmpty())
//			pw.print(v.getCodiceBarre().toUpperCase().trim());
//		pw.print("	");
//	    
//	    //(C) TIPO CODICE (EAN) 
//		pw.print("EAN");
//		pw.print("	");
//	    
//	    //(D) NOME ARTICOLO 
//		pw.print(Methods.primeLettereMaiuscole(a.getNome())+" ("+v.getValore()+")");
//		pw.print("	");
//
//	    //(E) MARCA 
//		pw.print(a.getCategoria().getNomeCategoria());
//		pw.print("	");
//
//	    //(F) PRODUTTORE 
//		pw.print("Zelda Bomboniere");
//		pw.print("	");
//
//	    //(G) DESCRIZIONE 
//		pw.print(Methods.MaiuscolaDopoPunto(a.getDescrizione()).replaceAll("[\n\r]", ""));
//		pw.print("	");
//
//	    //(H) PUNTO ELENCO 1 
//	    if (a.getQuantitaInserzione()!=null && !a.getQuantitaInserzione().trim().isEmpty())
//	    	pw.print("Quantità inserzione: "+Methods.primeLettereMaiuscole(a.getQuantitaInserzione()));  
//	    pw.print("	");
//
//	    //(I) PUNTO ELENCO 2 
//	    if (a.getDimensioni()!=null && !a.getDimensioni().trim().isEmpty()){
//	    	pw.print("Dimensioni: "+Methods.primeLettereMaiuscole(a.getDimensioni()).replace("Cm", "cm")); 
//		}
//	    pw.print("	");
//
//	  //(J) PUNTO ELENCO 3 
//	    /*if (a.getInfoAmazon().getPuntoElenco3()!=null && !a.getInfoAmazon().getPuntoElenco3().trim().isEmpty()){
//	    	pw.print(a.getInfoAmazon().getPuntoElenco3()); 
//		}*/
//	    pw.print("Codice Articolo: "+a.getCodice());
//	    pw.print("	");
//
//	    //(K) PUNTO ELENCO 4 
//	    /*if (a.getInfoAmazon().getPuntoElenco4()!=null && !a.getInfoAmazon().getPuntoElenco4().trim().isEmpty()){
//	    	pw.print(a.getInfoAmazon().getPuntoElenco4()); 
//		}*/
//	    pw.print("Descrizione: "+Methods.MaiuscolaDopoPunto(a.getDescrizione()).replaceAll("[\n\r]", ""));
//	    pw.print("	");
//
//	    //(L) PUNTO ELENCO 5 
//	    if (a.getInfoAmazon().getPuntoElenco5()!=null && !a.getInfoAmazon().getPuntoElenco5().trim().isEmpty()){
//	    	pw.print(a.getInfoAmazon().getPuntoElenco5()); 
//		}
//	    pw.print("	");
//
//	    //(M) NODO NAVIGAZIONE 1
//	    if (a.getInfoAmazon().getIdCategoria1()!=null && !a.getInfoAmazon().getIdCategoria1().trim().isEmpty())
//	    	pw.print(a.getInfoAmazon().getIdCategoria1());  
//	    else pw.print(a.getCategoria().getIdCategoriaAmazon());
//	    pw.print("	");
//
//	    //(N) NODO NAVIGAZIONE 2 
//	    if (a.getInfoAmazon().getIdCategoria2()!=null && !a.getInfoAmazon().getIdCategoria2().trim().isEmpty())
//	    	pw.print(a.getInfoAmazon().getIdCategoria2());  
//	    pw.print("	");
//
//	    //(O) TIPO PRODOTTO 
//	    pw.print("FurnitureAndDecor"); 
//	    pw.print("	");
//	    
//	    /* fine product basic information */
//	    
//	    /* inizio informazioni sull'immagine */
//	    
//	    //(BB) URL IMMAGINE PRINCIPALE
//	    pw.print(Costanti.percorsoImmaginiRemoto+v.getImmagine()); 
//	    pw.print("	");
//	    
//	    //(BC) URL ALTRA IMMAGINE 1
//	    if (a.getImmagine1()!=null && !a.getImmagine1().trim().isEmpty()){
//	    	pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine1()+"");  
//	    }
//	    pw.print("	");
//	    
//	    //(BD) URL ALTRA IMMAGINE 2
//	    pw.print("	");	    
//	    //(BE) URL ALTRA IMMAGINE 3
//	    pw.print("	");	    
//	    //(BF) URL ALTRA IMMAGINE 4
//	    pw.print("	");	    
//	    //(BG) URL ALTRA IMMAGINE 5	
//	    pw.print("	");
//		//(BH) URL ALTRA IMMAGINE 6
//	    pw.print("	");
//		//(BI) URL ALTRA IMMAGINE 7
//	    pw.print("	");
//		//(BJ) URL ALTRA IMMAGINE 8
//	    pw.print("	");
//	    
//	    //(BK) URL IMMAGINE SWITCH
//	    pw.print("	");
//	    
//	    /* fine informazioni sull'immagine */
//	    
//	    /* inizio informazioni legali */
//	    
//	    //(BL) ESCLUSIONE RESPOSABILITA'
//	    if (a.getInfoAmazon().getEsclusioneResponsabilita()!=null && !a.getInfoAmazon().getEsclusioneResponsabilita().trim().isEmpty())
//	    	pw.print(a.getInfoAmazon().getEsclusioneResponsabilita());
//	    pw.print("	");
//	    
//	    //(BM) DESCRIZIONE GARANZIA VENDITORE
//	    if (a.getInfoAmazon().getDescrizioneGaranziaVenditore()!=null && !a.getInfoAmazon().getDescrizioneGaranziaVenditore().trim().isEmpty())
//	   	 	pw.print(a.getInfoAmazon().getDescrizioneGaranziaVenditore());
//	    pw.print("	");
//	    
//	    //(BN) AVVERTENZE SICUREZZA
//	    if (a.getInfoAmazon().getAvvertenzeSicurezza()!=null && !a.getInfoAmazon().getAvvertenzeSicurezza().trim().isEmpty())
//	    	pw.print(a.getInfoAmazon().getAvvertenzeSicurezza());
//	    pw.print("	");
//	    
//	    /* fine informazioni legali */
//	    
//	    //(BO) AGGIORNA O RIMUOVI
//	    pw.print("	");
//	    
//	    /* inizio informazioni sull'offerta */
//	    
//	    //(BP) PREZZO
//	    double prezzo = a.getPrezzoDettaglio();
//  	  	if (a.getPrezzoPiattaforme()>0) prezzo = a.getPrezzoPiattaforme();
//	    pw.print(String.valueOf(prezzo).replace(",", ".")); 
//	    pw.print("	");
//	    
//	    //(BQ) VALUTA
//	    pw.print("EUR"); 
//	    pw.print("	");
//	    
//	    //(BR) QUANTITA'
//	    if (a.getQuantitaMagazzino()>0)
//	    	pw.print(a.getQuantitaMagazzino());  
//	    else pw.print(20);  
//	    pw.print("	");
//	    
//	    //(BS) CONDIZIONI
//	    pw.print("New"); 
//	    pw.print("	");
//	    
//	    //(BT) NOTA CONDIZIONI
//	    if (a.getInfoAmazon().getNotaCondizioni()!=null && !a.getInfoAmazon().getNotaCondizioni().trim().isEmpty())
//	    	pw.print(a.getInfoAmazon().getNotaCondizioni());
//	    pw.print("	");
//	    
//	    //(BU) VOCE PACCHETTO QUANTITA'
//	    if (a.getInfoAmazon().getVocePacchettoQuantita()!=0)
//	    	pw.print(a.getInfoAmazon().getVocePacchettoQuantita());
//	    pw.print("	");
//	    
//	    //(BV) NUMERO PEZZI
//	    if (a.getInfoAmazon().getNumeroPezzi()!=0)
//	    	pw.print(a.getInfoAmazon().getNumeroPezzi());
//	    pw.print("	");
//	    
//	    //(BW) DATA LANCIO
//	    pw.print("	");
//	    
//	    //(BX) DATA RILASCIO
//	    pw.print("	");
//	    
//	    //(BY) TEMPI ESECUZIONE SPEDIZIONE
//	    if (a.getInfoAmazon().getTempiEsecuzioneSpedizione()!=0)
//	    	pw.print(a.getInfoAmazon().getTempiEsecuzioneSpedizione());
//	    pw.print("	");
//	    
//	    //(BZ) DATA RIFORNIMENTO
//	    pw.print("	");
//	    
//	    //(CA) QUANTITA' MASSIMA SPEDIZIONE CUMULATIVA
//	    pw.print("100");
//	    pw.print("	");
//	    
//	    //(CB) PAESE DI ORIGINE
//	    if (a.getInfoAmazon().getPaeseOrigine()!=null && !a.getInfoAmazon().getPaeseOrigine().trim().isEmpty())
//	    	pw.print(a.getInfoAmazon().getPaeseOrigine());
//	    pw.print("	");
//	    
//	    /* fine informazioni sull'offerta */
//	    
//	    /* inizio dimensioni prodotto */
//	    
//	    //(GG) PESO SPEDIZIONE
//	    pw.print("	");
//	    
//	    //(GH) UNITA' MISURA PESO SPEDIZIONE
//	    pw.print("	");
//	    
//	    //(GI) LUNGHEZZA ARTICOLO
//	    if (a.getInfoAmazon().getLunghezzaArticolo()!=0)
//	    	pw.print(a.getInfoAmazon().getLunghezzaArticolo()); 
//	    pw.print("	");
//	    
//	    //(GJ) ALTEZZA ARTICOLO
//	    if (a.getInfoAmazon().getAltezzaArticolo()!=0)
//	    	pw.print(a.getInfoAmazon().getAltezzaArticolo()); 
//	    pw.print("	");
//	    
//	    //(GK) PESO ARTICOLO
//	    if (a.getInfoAmazon().getPesoArticolo()!=0)
//	    	pw.print(a.getInfoAmazon().getPesoArticolo()); 
//	    pw.print("	");
//	    
//	    //(GL) UNITA' MISURA PESO ARTICOLO
//	    if (a.getInfoAmazon().getUnitaMisuraPesoArticolo()!=null && !a.getInfoAmazon().getUnitaMisuraPesoArticolo().trim().isEmpty())
//	    	pw.print(a.getInfoAmazon().getUnitaMisuraPesoArticolo()); 
//	    pw.print("	");
//	    
//	    /* fine dimensioni prodotto */
//	    
//	    /* inizio informazioni sulla scoperta del prodotto */
//	    
//	    //(GM) CODICE ARTICOLO DEL PRODUTTORE (MFR, MANIFACTURER PART NUMBER)
//	    if (a.getCodiceArticoloFornitore()!=null && !a.getCodiceArticoloFornitore().trim().isEmpty())
//	    	pw.print(a.getCodiceArticoloFornitore());
//	    else pw.print(a.getCodice());
//	    pw.print("	");
//	    
//	    //(GN) PAROLE CHIAVE 1
//	    if (a.getParoleChiave1()!=null && !a.getParoleChiave1().trim().isEmpty())
//	    	pw.print(Methods.cut100(a.getParoleChiave1()));
//	    pw.print("	");
//	    
//	    //(GO) PAROLE CHIAVE 2
//	    if (a.getParoleChiave2()!=null && !a.getParoleChiave2().trim().isEmpty())
//	    	pw.print(Methods.cut100(a.getParoleChiave2()));
//	    pw.print("	");
//	    
//	    //(GP) PAROLE CHIAVE 3
//	    if (a.getParoleChiave3()!=null && !a.getParoleChiave3().trim().isEmpty())
//	    	pw.print(Methods.cut100(a.getParoleChiave3()));
//	    pw.print("	");
//	    
//	    //(GQ) PAROLE CHIAVE 4
//	    if (a.getParoleChiave4()!=null && !a.getParoleChiave4().trim().isEmpty())
//	    	pw.print(Methods.cut100(a.getParoleChiave4()));
//	    pw.print("	");
//	    
//	    //(GR) PAROLE CHIAVE 5
//	    if (a.getParoleChiave5()!=null && !a.getParoleChiave5().trim().isEmpty())
//	    	pw.print(Methods.cut100(a.getParoleChiave5()));
//	    pw.print("	");
//	    
//	    /* fine informazioni sulla scoperta del prodotto */
//	    
//	    /* inizio informazioni prezzo saldo e ribasso */
//	    
//	    //(GS) PREZZO DI VENDITA
//	    pw.print("	");
//	    
//	    //(GT) DATA FINE SALDO
//	    pw.print("	");
//	    
//	    //(GU) DATA INIZIO SALDO
//	    pw.print("	");
//	    
//	    /* fine informazioni prezzo saldo e ribasso */
//	    
//	    /* inizio informazioni varianti */
//	    
//	  	//(GV) FILIAZIONE (parent, child)
//	    pw.print("	");
//	    
//	  	//(GW) PARENT SKU
//	    pw.print("	");
//	    
//	  	//(GX) TIPO RELAZIONE (variante, accessorio)
//	    pw.print("	");
//	    
//	  	//(GY) TEMA VARIANTE (colore, taglia, taglia-colore,fragranza, taglia-fragranza)
//	    pw.print("	");
//	    
//	  	//(GZ) DIMENSIONI (SE IL TEMA è TAGLIA, ES. VALORE VALIDO: S)
//	    pw.print("	");
//	    
//	  	//(HA) MAPPA DIMENSIONI
//	    pw.print("	");
//	    
//	  	//(HB) COLORE
//	    pw.print("	");
//	    
//	  	//(HC) MAPPA COLORE
//	    pw.print("	");
//	    
//	    /* fine informazioni varianti */
//	    
//	    /* fine !!! */
//	    
//	    pw.println();
//		
//	}
	
	
	private static void aggiungiProdottoSenzaCodiceBarre(Articolo a, PrintWriter pw, boolean delete){
		//System.out.println("Inizio a scrivere sul file");
		
		/* inizio product basic information */
		
		//(A) SKU 
		pw.print(a.getCodice());
		pw.print("	");
	    
	    //(B) CODICE BARRE 
		pw.print("	");
	    
	    //(C) TIPO CODICE BARRE 
		pw.print("	");
	    
	    //(D) NOME ARTICOLO 
		String nome = Methods.primeLettereMaiuscole(a.getNome());
		if (a.getCodice().contains("ZELDA")){
			nome = nome+"("+a.getCodice()+")";
		} else if (a.getCodice().contains("TORTA")){
			nome = nome+"("+a.getCodice().replace("BOMBONIERE-", "").replace("BOMBONIERE_", "")+")";
		}
		pw.print(nome);
		pw.print("	");

	    //(E) MARCA 
		pw.print("Zelda Bomboniere");
		pw.print("	");

	    //(F) PRODUTTORE 
		pw.print("Zelda Bomboniere");
		pw.print("	");

	    //(G) DESCRIZIONE 
		pw.print(Methods.MaiuscolaDopoPunto(a.getDescrizione()).replaceAll("[\n\r]", ""));
		pw.print("	");

	    //(H) PUNTO ELENCO 1 
	    if (a.getQuantitaInserzione()!=null && !a.getQuantitaInserzione().trim().isEmpty()){
	    	pw.print("Quantità inserzione: "+Methods.primeLettereMaiuscole(a.getQuantitaInserzione()));  
	    }
	    pw.print("	");

	    //(I) PUNTO ELENCO 2 
	    if (a.getDimensioni()!=null && !a.getDimensioni().trim().isEmpty()){
	    	pw.print("Dimensioni: "+Methods.primeLettereMaiuscole(a.getDimensioni()).replace("Cm", "cm")); 
		}
	    pw.print("	");

	  //(J) PUNTO ELENCO 3 
	    /*if (a.getInfoAmazon().getPuntoElenco3()!=null && !a.getInfoAmazon().getPuntoElenco3().trim().isEmpty()){
	    	pw.print(a.getInfoAmazon().getPuntoElenco3()); 
		}*/
	    pw.print("Codice Articolo: "+a.getCodice());
	    pw.print("	");

	    //(K) PUNTO ELENCO 4 
	    /*if (a.getInfoAmazon().getPuntoElenco4()!=null && !a.getInfoAmazon().getPuntoElenco4().trim().isEmpty()){
	    	pw.print(a.getInfoAmazon().getPuntoElenco4()); 
		}*/
	    pw.print(Methods.cut500("Descrizione: "+Methods.MaiuscolaDopoPunto(a.getDescrizione()).replaceAll("[\n\r]", "")));
	    pw.print("	");

	    //(L) PUNTO ELENCO 5 
	    if (a.getInfoAmazon().getPuntoElenco5()!=null && !a.getInfoAmazon().getPuntoElenco5().trim().isEmpty()){
	    	pw.print(a.getInfoAmazon().getPuntoElenco5()); 
		}
	    pw.print("	");

	    //(M) NODO NAVIGAZIONE 1
	    if (a.getInfoAmazon().getIdCategoria1()!=null 
	    		&& !a.getInfoAmazon().getIdCategoria1().trim().isEmpty()
	    		&& !a.getInfoAmazon().getIdCategoria1().trim().equals("null")
		    	&& a.getInfoAmazon().getIdCategoria1()!="-1"
		    	&& a.getInfoAmazon().getIdCategoria1()!="0")
	    	pw.print(a.getInfoAmazon().getIdCategoria1());  
	    else pw.print("731697031");
	    pw.print("	");

	    //(N) NODO NAVIGAZIONE 2 
	     if (a.getInfoAmazon().getIdCategoria2()!=null 
	    		&& !a.getInfoAmazon().getIdCategoria2().trim().isEmpty()
	    		&& !a.getInfoAmazon().getIdCategoria2().trim().equals("null")
		    	&& a.getInfoAmazon().getIdCategoria2()!="-1"
		    	&& a.getInfoAmazon().getIdCategoria2()!="0")
	    	pw.print(a.getInfoAmazon().getIdCategoria2());  
	    else pw.print("2906926031");
	    pw.print("	");

	    //(O) TIPO PRODOTTO 
	    pw.print("FurnitureAndDecor"); 
	    pw.print("	");
	    
	    /* fine product basic information */
	    
	    /* inizio informazioni sull'immagine */
	    
	    //(BB) URL IMMAGINE PRINCIPALE
	    if (!delete)
	    	pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine1()); 
	    pw.print("	");
	    
	    //(BC) URL ALTRA IMMAGINE 1
	    if (!delete && a.getImmagine2()!=null && !a.getImmagine2().trim().isEmpty()){
	    	pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine2()+"");  
	    }
	    pw.print("	");
	    
	    //(BD) URL ALTRA IMMAGINE 2
	    if (!delete && a.getImmagine3()!=null && !a.getImmagine3().trim().isEmpty()){
	    	pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine3());  
	    }
	    pw.print("	");
	    
	    //(BE) URL ALTRA IMMAGINE 3
	    if (!delete && a.getImmagine4()!=null && !a.getImmagine4().trim().isEmpty()){
	    	pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine4());  
	    }
	    pw.print("	");
	    
	    //(BF) URL ALTRA IMMAGINE 4
	    if (!delete && a.getImmagine5()!=null && !a.getImmagine5().trim().isEmpty()){
	    	pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine5());  
	    }
	    pw.print("	");
	    
	    //(BG) URL ALTRA IMMAGINE 5	
	    pw.print("	");
		//(BH) URL ALTRA IMMAGINE 6
	    pw.print("	");
		//(BI) URL ALTRA IMMAGINE 7
	    pw.print("	");
		//(BJ) URL ALTRA IMMAGINE 8
	    pw.print("	");
	    
	    //(BK) URL IMMAGINE SWITCH
	    pw.print("	");
	    
	    /* fine informazioni sull'immagine */
	    
	    /* inizio informazioni legali */
	    
	    //(BL) ESCLUSIONE RESPOSABILITA'
	    if (a.getInfoAmazon().getEsclusioneResponsabilita()!=null && !a.getInfoAmazon().getEsclusioneResponsabilita().trim().isEmpty())
	    	pw.print(a.getInfoAmazon().getEsclusioneResponsabilita());
	    pw.print("	");
	    
	    //(BM) DESCRIZIONE GARANZIA VENDITORE
	    if (a.getInfoAmazon().getDescrizioneGaranziaVenditore()!=null && !a.getInfoAmazon().getDescrizioneGaranziaVenditore().trim().isEmpty())
	   	 	pw.print(a.getInfoAmazon().getDescrizioneGaranziaVenditore());
	    pw.print("	");
	    
	    //(BN) AVVERTENZE SICUREZZA
	    if (a.getInfoAmazon().getAvvertenzeSicurezza()!=null && !a.getInfoAmazon().getAvvertenzeSicurezza().trim().isEmpty())
	    	pw.print(a.getInfoAmazon().getAvvertenzeSicurezza());
	    pw.print("	");
	    
	    /* fine informazioni legali */
	    
	    //(BO) AGGIORNA O RIMUOVI
	    if (delete) pw.print("Delete");
	    pw.print("	");
	    
	    /* inizio informazioni sull'offerta */
	    
	    //(BP) PREZZO
	    double prezzo = a.getPrezzoDettaglio();
  	  	if (a.getPrezzoPiattaforme()>0) prezzo = a.getPrezzoPiattaforme();
	    pw.print(String.valueOf(prezzo).replace(",", ".")); 
	    pw.print("	");
	    
	    //(BQ) VALUTA
	    pw.print("EUR"); 
	    pw.print("	");
	    
	    //(BR) QUANTITA'
	    if (a.getQuantitaMagazzino()>0)
	    	pw.print(a.getQuantitaMagazzino());  
	    else pw.print(20);  
	    pw.print("	");
	    
	    //(BS) CONDIZIONI
	    pw.print("New"); 
	    pw.print("	");
	    
	    //(BT) NOTA CONDIZIONI
	    if (a.getInfoAmazon().getNotaCondizioni()!=null && !a.getInfoAmazon().getNotaCondizioni().trim().isEmpty())
	    	pw.print(a.getInfoAmazon().getNotaCondizioni());
	    pw.print("	");
	    
	    //(BU) VOCE PACCHETTO QUANTITA'
	    if (a.getInfoAmazon().getVocePacchettoQuantita()!=0)
	    	pw.print(a.getInfoAmazon().getVocePacchettoQuantita());
	    else pw.print("1");
	    pw.print("	");
	    
	    //(BV) NUMERO PEZZI
	    if (a.getInfoAmazon().getNumeroPezzi()!=0)
	    	pw.print(a.getInfoAmazon().getNumeroPezzi());
	    else pw.print("1");
	    pw.print("	");
	    
	    //(BW) DATA LANCIO
	    pw.print("	");
	    
	    //(BX) DATA RILASCIO
	    pw.print("	");
	    
	    //(BY) TEMPI ESECUZIONE SPEDIZIONE
	    String tempi = "";
	    if (a.getInfoAmazon().getTempiEsecuzioneSpedizione()!=0)
	    	tempi = String.valueOf(a.getInfoAmazon().getTempiEsecuzioneSpedizione());
	    else	if (a.getCodice().contains("ZELDA") || a.getCodice().contains("TORTA")){
	    	tempi = "7";
		}
	    pw.print(tempi);
	    pw.print("	");
	    
	    //(BZ) DATA RIFORNIMENTO
	    pw.print("	");
	    
	    //(CA) QUANTITA' MASSIMA SPEDIZIONE CUMULATIVA
	    pw.print("100");
	    pw.print("	");
	    
	    //(CB) PAESE DI ORIGINE
	    if (a.getInfoAmazon().getPaeseOrigine()!=null && !a.getInfoAmazon().getPaeseOrigine().trim().isEmpty())
	    	pw.print(a.getInfoAmazon().getPaeseOrigine());
	    pw.print("	");
	    
	    /* fine informazioni sull'offerta */
	    
	    /* inizio dimensioni prodotto */
	    
	    //(GG) PESO SPEDIZIONE
	    pw.print("	");
	    
	    //(GH) UNITA' MISURA PESO SPEDIZIONE
	    pw.print("	");
	    
	    //(GI) LUNGHEZZA ARTICOLO
	    if (a.getInfoAmazon().getLunghezzaArticolo()!=0)
	    	pw.print(a.getInfoAmazon().getLunghezzaArticolo()); 
	    pw.print("	");
	    
	    //(GJ) ALTEZZA ARTICOLO
	    if (a.getInfoAmazon().getAltezzaArticolo()!=0)
	    	pw.print(a.getInfoAmazon().getAltezzaArticolo()); 
	    pw.print("	");
	    
	    //(GK) PESO ARTICOLO
	    if (a.getInfoAmazon().getPesoArticolo()!=0)
	    	pw.print(a.getInfoAmazon().getPesoArticolo()); 
	    pw.print("	");
	    
	    //(GL) UNITA' MISURA PESO ARTICOLO
	    if (a.getInfoAmazon().getUnitaMisuraPesoArticolo()!=null && !a.getInfoAmazon().getUnitaMisuraPesoArticolo().trim().isEmpty())
	    	pw.print(a.getInfoAmazon().getUnitaMisuraPesoArticolo()); 
	    pw.print("	");
	    
	    /* fine dimensioni prodotto */
	    
	    /* inizio informazioni sulla scoperta del prodotto */
	    
	    //(GM) CODICE ARTICOLO DEL PRODUTTORE
	    pw.print(a.getCodice());
	    pw.print("	");
	    
	    //(GN) PAROLE CHIAVE 1
	    if (a.getParoleChiave1()!=null && !a.getParoleChiave1().trim().isEmpty())
	    	pw.print(Methods.cut100(a.getParoleChiave1()));
	    pw.print("	");
	    
	    //(GO) PAROLE CHIAVE 2
	    if (a.getParoleChiave2()!=null && !a.getParoleChiave2().trim().isEmpty())
	    	pw.print(Methods.cut100(a.getParoleChiave2()));
	    pw.print("	");
	    
	    //(GP) PAROLE CHIAVE 3
	    if (a.getParoleChiave3()!=null && !a.getParoleChiave3().trim().isEmpty())
	    	pw.print(Methods.cut100(a.getParoleChiave3()));
	    pw.print("	");
	    
	    //(GQ) PAROLE CHIAVE 4
	    if (a.getParoleChiave4()!=null && !a.getParoleChiave4().trim().isEmpty())
	    	pw.print(Methods.cut100(a.getParoleChiave4()));
	    pw.print("	");
	    
	    //(GR) PAROLE CHIAVE 5
	    if (a.getParoleChiave5()!=null && !a.getParoleChiave5().trim().isEmpty())
	    	pw.print(Methods.cut100(a.getParoleChiave5()));
	    pw.print("	");
	    
	    /* fine informazioni sulla scoperta del prodotto */
	    
	    /* inizio informazioni prezzo saldo e ribasso */
	    
	    //(GS) PREZZO DI VENDITA
	    pw.print("	");
	    
	    //(GT) DATA FINE SALDO
	    pw.print("	");
	    
	    //(GU) DATA INIZIO SALDO
	    pw.print("	");
	    
	    /* fine informazioni prezzo saldo e ribasso */
	    
	    /* inizio informazioni varianti */
	    
	  	//(GV) FILIAZIONE (parent, child)
	    pw.print("	");
	    
	  	//(GW) PARENT SKU
	    pw.print("	");
	    
	  	//(GX) TIPO RELAZIONE (variante, accessorio)
	    pw.print("	");
	    
	  	//(GY) TEMA VARIANTE (colore, taglia, taglia-colore,fragranza, taglia-fragranza)
	    pw.print("	");
	    
	  	//(GZ) DIMENSIONI (SE IL TEMA è TAGLIA, ES. VALORE VALIDO: S)
	    pw.print("	");
	    
	  	//(HA) MAPPA DIMENSIONI
	    pw.print("	");
	    
	  	//(HB) COLORE
	    pw.print("	");
	    
	  	//(HC) MAPPA COLORE
	    pw.print("	");
	    
	    /* fine informazioni varianti */
	    
	    /* fine !!! */
	    
	    pw.println();
		
	}
	
	
	private static void aggiungiProdottoEsistente(Articolo a, PrintWriter pw){
		//System.out.println("Inizio a scrivere sul file");
		
		//(A) SKU 
		pw.print(a.getCodice());
		pw.print("	");
	    
	    //(B) PRICE
		double prezzo = a.getPrezzoDettaglio();
  	  	if (a.getPrezzoPiattaforme()>0) prezzo = a.getPrezzoPiattaforme();
	    pw.print(String.valueOf(prezzo).replace(",", ".")); 
		pw.print("	");
	    
	    //(C) QUANTITY
		if (a.getQuantitaMagazzino()>0)
	    	pw.print(a.getQuantitaMagazzino());  
	    else pw.print(20);  
		pw.print("	");
	    
	    //(D) PRODUCT-ID (BARCODE O ASIN)
		pw.print(a.getCodiceBarre());
		pw.print("	");

	    //(E) PRODUCT-ID-TYPE 
		pw.print("EAN");
		pw.print("	");

	    //(F) CONDITION-TYPE 
		pw.print("New");
		pw.print("	");

	    //(G) CONDITION-NOTE 
		pw.print("	");

	    //(H) ASIN-HINT
		pw.print("	");

	    //(I) TITLE
		pw.print(Methods.primeLettereMaiuscole(a.getNome()));
		pw.print("	");

	    //(J) PRODUCT-TAX-CODE
	    pw.print("	");

	    //(K) OPERATION-TYPE
	    pw.print("	");

	    //(L) SALE-PRICE
	    pw.print("	");

	    //(M) SALE-START-DATE
	    pw.print("	");

	    //(N) SALE-END-DATE
	    pw.print("	");

	    //(O) LEADTIME-TO-SHIP
	    if (a.getInfoAmazon()!=null){
		    if (a.getInfoAmazon().getTempiEsecuzioneSpedizione()!=0)
		    	pw.print(a.getInfoAmazon().getTempiEsecuzioneSpedizione());
	    }
	    pw.print("	");
	    
	    //(P) LAUNCH-DATE
	    pw.print("	");
	    
	    //(Q) is-giftwrap-available
	    pw.print("	");
	    
	    //(R) is-gift-message-available
	    pw.print("	");
	    
	    //(S) fulfillment-center-id
	    pw.print("	");

	    pw.println();
		
	}
	
	
	private static void aggiungiProdottoEsistente(Articolo a, Variante_Articolo v, PrintWriter pw){
		//System.out.println("Inizio a scrivere sul file");
		
		//(A) SKU 
		pw.print(a.getCodice()+"-"+v.getValore().trim().replace(" ", "_"));
		pw.print("	");
	    
	    //(B) PRICE
		double prezzo = a.getPrezzoDettaglio();
  	  	if (a.getPrezzoPiattaforme()>0) prezzo = a.getPrezzoPiattaforme();
	    pw.print(String.valueOf(prezzo).replace(",", ".")); 
		pw.print("	");
	    
	    //(C) QUANTITY
		if (v.getQuantita()>0)
	    	pw.print(v.getQuantita());  
	    else pw.print(20);  
		pw.print("	");
	    
	    //(D) PRODUCT-ID (BARCODE O ASIN)
		pw.print(v.getCodiceBarre());
		pw.print("	");

	    //(E) PRODUCT-ID-TYPE
		pw.print("EAN");
		pw.print("	");

	    //(F) CONDITION-TYPE 
		pw.print("New");
		pw.print("	");

	    //(G) CONDITION-NOTE 
		pw.print("	");

	    //(H) ASIN-HINT
		pw.print("	");

	    //(I) TITLE
		pw.print(Methods.primeLettereMaiuscole(a.getNome())+" (Variante "+v.getValore()+")");
		pw.print("	");

	    //(J) PRODUCT-TAX-CODE
	    pw.print("	");

	    //(K) OPERATION-TYPE
	    pw.print("	");

	    //(L) SALE-PRICE
	    pw.print("	");

	    //(M) SALE-START-DATE
	    pw.print("	");

	    //(N) SALE-END-DATE
	    pw.print("	");

	    //(O) LEADTIME-TO-SHIP
	    if (a.getInfoAmazon()!=null){
		    if (a.getInfoAmazon().getTempiEsecuzioneSpedizione()!=0)
		    	pw.print(a.getInfoAmazon().getTempiEsecuzioneSpedizione());
	    }
	    pw.print("	");
	    
	    //(P) LAUNCH-DATE
	    pw.print("	");
	    
	    //(Q) is-giftwrap-available
	    pw.print("	");
	    
	    //(R) is-gift-message-available
	    pw.print("	");
	    
	    //(S) fulfillment-center-id
	    pw.print("	");

	    pw.println();
		
	}
	
	
	
	private static void spedizioneGratis(Articolo a, Variante_Articolo v, PrintWriter pw){
		//System.out.println("Inizio a scrivere sul file");
		
		//(A) SKU 
		pw.print(a.getCodice());
		pw.print("	");
	    
	    //(B) CURRENCY
		pw.print("EUR"); 
		pw.print("	");
	    
	    //(C) SHIP OPTION 1
		pw.print("IT Exp Domestic"); 
		pw.print("	");

	    //(C) SHIPPING AMT 1
		pw.print("0"); 
		pw.print("	");
		
	    //(C) TYPE 1
		pw.print("Exclusive"); 
		pw.print("	");
		
	    //(C) IS SHIPPING RESTRICTED 1
		pw.print("False"); 
		pw.print("	");
		
		//(C) SHIP OPTION 2
		pw.print("	");
	
	    //(C) SHIPPING AMT 2
		pw.print("	");
		
	    //(C) TYPE 2
		pw.print("	");
		
	    //(C) IS SHIPPING RESTRICTED 2
		pw.print("	");
		
		//(C) SHIP OPTION 3
		pw.print("	");

	    //(C) SHIPPING AMT 3
		pw.print("	");
		
	    //(C) TYPE 3
		pw.print("	");
		
	    //(C) IS SHIPPING RESTRICTED 3
		pw.print("	");
				
		//(C) SHIP OPTION 4
		pw.print("	");

	    //(C) SHIPPING AMT 4
		pw.print("	");
		
	    //(C) TYPE 4
		pw.print("	");
		
	    //(C) IS SHIPPING RESTRICTED 4
		pw.print("	");
				
		//(C) UPDATE DELETE
		pw.print("	");				
				

	    pw.println();
		
	}
	
	
	
	private static void aggiungiParent(Articolo a, PrintWriter pw, boolean delete){
		//System.out.println("Inizio a scrivere sul file");
		
		/* inizio product basic information */
		
		//(A) SKU 
		pw.print(a.getCodice());
		pw.print("	");
	    
		/*	lascia vuoto	*/
	    //(B) CODICE BARRE 
		pw.print("	");
	    
		/* lascia vuoto */
	    //(C) TIPO CODICE (EAN) 
		pw.print("	");
	    
	    //(D) NOME ARTICOLO 
		String nome = Methods.primeLettereMaiuscole(a.getNome());
		if (a.getCodice().contains("ZELDA")){
			nome = nome+"("+a.getCodice()+")";
		} else if (a.getCodice().contains("TORTA")){
			nome = nome+"("+a.getCodice().replace("BOMBONIERE-", "").replace("BOMBONIERE_", "")+")";
		}
		pw.print(nome);
		pw.print("	");

	    //(E) MARCA 
		if (a.getCodiceBarre()==null || a.getCodiceBarre().trim().isEmpty())
			pw.print("Zelda Bomboniere");		
		else pw.print(a.getCategoria().getNomeCategoria());
		pw.print("	");

	    //(F) PRODUTTORE 
		pw.print("Zelda Bomboniere");
		pw.print("	");

	    //(G) DESCRIZIONE 
		pw.print(Methods.MaiuscolaDopoPunto(a.getDescrizione()).replaceAll("[\n\r]", ""));
		pw.print("	");

	    //(H) PUNTO ELENCO 1 
	    if (a.getQuantitaInserzione()!=null && !a.getQuantitaInserzione().trim().isEmpty()){
	    	pw.print("Quantità inserzione: "+a.getQuantitaInserzione());  
	    }
	    pw.print("	");

	    //(I) PUNTO ELENCO 2 
	    if (a.getDimensioni()!=null && !a.getDimensioni().trim().isEmpty()){
	    	pw.print("Dimensioni: "+a.getDimensioni()); 
		}
	    pw.print("	");

	    //(J) PUNTO ELENCO 3 
	    /*if (a.getInfoAmazon().getPuntoElenco3()!=null && !a.getInfoAmazon().getPuntoElenco3().trim().isEmpty()){
	    	pw.print(a.getInfoAmazon().getPuntoElenco3()); 
		}*/
	    pw.print("Codice Articolo: "+a.getCodice());
	    pw.print("	");

	    //(K) PUNTO ELENCO 4 
	    /*if (a.getInfoAmazon().getPuntoElenco4()!=null && !a.getInfoAmazon().getPuntoElenco4().trim().isEmpty()){
	    	pw.print(a.getInfoAmazon().getPuntoElenco4()); 
		}*/
	    pw.print(Methods.cut500("Descrizione: "+Methods.MaiuscolaDopoPunto(a.getDescrizione()).replaceAll("[\n\r]", "")));
	    pw.print("	");

	    //(L) PUNTO ELENCO 5 
	    if (a.getInfoAmazon().getPuntoElenco5()!=null && !a.getInfoAmazon().getPuntoElenco5().trim().isEmpty()){
	    	pw.print(a.getInfoAmazon().getPuntoElenco5()); 
		}
	    pw.print("	");

	    //(M) NODO NAVIGAZIONE 1 
	    if (a.getInfoAmazon().getIdCategoria1()!=null 
	    		&& !a.getInfoAmazon().getIdCategoria1().trim().isEmpty()
	    		&& !a.getInfoAmazon().getIdCategoria1().trim().equals("null")
		    	&& a.getInfoAmazon().getIdCategoria1()!="-1"
		    	&& a.getInfoAmazon().getIdCategoria1()!="0")
	    	pw.print(a.getInfoAmazon().getIdCategoria1());  
	    else pw.print("731697031");
//	    pw.print(a.getInfoAmazon().getIdCategoria1());  
	    pw.print("	");

	    //(N) NODO NAVIGAZIONE 2 
	    if (a.getInfoAmazon().getIdCategoria2()!=null 
	    		&& !a.getInfoAmazon().getIdCategoria2().trim().isEmpty()
	    		&& !a.getInfoAmazon().getIdCategoria2().trim().equals("null")
		    	&& a.getInfoAmazon().getIdCategoria2()!="-1"
		    	&& a.getInfoAmazon().getIdCategoria2()!="0")
	    	pw.print(a.getInfoAmazon().getIdCategoria2());  
	    else pw.print("2906926031");
	    //	    pw.print(a.getInfoAmazon().getIdCategoria2());
	    pw.print("	");

	    //(O) TIPO PRODOTTO 
	    pw.print("FurnitureAndDecor"); 
	    pw.print("	");
	    
	    /* fine product basic information */
	    
	    /* inizio informazioni sull'immagine */
	    
	    //(BB) URL IMMAGINE PRINCIPALE
	    if (!delete)
	    	pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine1()); 
	    pw.print("	");
	    
	    //(BC) URL ALTRA IMMAGINE 1
	    if (!delete && a.getImmagine2()!=null && !a.getImmagine2().trim().isEmpty()){
	    	pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine2()+"");  
	    }
	    pw.print("	");
	    
	    //(BD) URL ALTRA IMMAGINE 2
	    if (!delete && a.getImmagine3()!=null && !a.getImmagine3().trim().isEmpty()){
	    	pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine3());  
	    }
	    pw.print("	");
	    
	    //(BE) URL ALTRA IMMAGINE 3
	    if (!delete && a.getImmagine4()!=null && !a.getImmagine4().trim().isEmpty()){
	    	pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine4());  
	    }
	    pw.print("	");
	    
	    //(BF) URL ALTRA IMMAGINE 4
	    if (!delete && a.getImmagine5()!=null && !a.getImmagine5().trim().isEmpty()){
	    	pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine5());  
	    }
	    pw.print("	");
	    
	    //(BG) URL ALTRA IMMAGINE 5	
	    pw.print("	");
		//(BH) URL ALTRA IMMAGINE 6
	    pw.print("	");
		//(BI) URL ALTRA IMMAGINE 7
	    pw.print("	");
		//(BJ) URL ALTRA IMMAGINE 8
	    pw.print("	");
	    
	    //(BK) URL IMMAGINE SWITCH
	    pw.print("	");
	    
	    /* fine informazioni sull'immagine */
	    
	    /* inizio informazioni legali */
	    
	    //(BL) ESCLUSIONE RESPOSABILITA'
	    if (a.getInfoAmazon().getEsclusioneResponsabilita()!=null && !a.getInfoAmazon().getEsclusioneResponsabilita().trim().isEmpty())
	    	pw.print(a.getInfoAmazon().getEsclusioneResponsabilita());
	    pw.print("	");
	    
	    //(BM) DESCRIZIONE GARANZIA VENDITORE
	    if (a.getInfoAmazon().getDescrizioneGaranziaVenditore()!=null && !a.getInfoAmazon().getDescrizioneGaranziaVenditore().trim().isEmpty())
	   	 	pw.print(a.getInfoAmazon().getDescrizioneGaranziaVenditore());
	    pw.print("	");
	    
	    //(BN) AVVERTENZE SICUREZZA
	    if (a.getInfoAmazon().getAvvertenzeSicurezza()!=null && !a.getInfoAmazon().getAvvertenzeSicurezza().trim().isEmpty())
	    	pw.print(a.getInfoAmazon().getAvvertenzeSicurezza());
	    pw.print("	");
	    
	    /* fine informazioni legali */
	    
	    //(BO) AGGIORNA O RIMUOVI
	    if (delete) pw.print("Delete");
	    pw.print("	");
	    
	    /* inizio informazioni sull'offerta */
	    
	    /*	lascia vuoto	*/
	    //(BP) PREZZO
	    pw.print("	");
	    
	    /*	lascia vuoto	*/
	    //(BQ) VALUTA
	    pw.print("	");
	    
	    /*	lascia vuoto	*/
	    //(BR) QUANTITA'
	    pw.print("	");
	    
	    /*	lascia vuoto	*/
	    //(BS) CONDIZIONI
	    pw.print("	");
	    
	    /*	lascia vuoto	*/
	    //(BT) NOTA CONDIZIONI
	    pw.print("	");
	    
	    //(BU) VOCE PACCHETTO QUANTITA'
	    pw.print("	");
	    
	    //(BV) NUMERO PEZZI
	    pw.print("	");
	    
	    //(BW) DATA LANCIO
	    pw.print("	");
	    
	    //(BX) DATA RILASCIO
	    pw.print("	");
	    
	    /*	lascia vuoto	*/
	    //(BY) TEMPI ESECUZIONE SPEDIZIONE
	    pw.print("	");
	    
	    //(BZ) DATA RIFORNIMENTO
	    pw.print("	");
	    
	    //(CA) QUANTITA' MASSIMA SPEDIZIONE CUMULATIVA
	    if (a.getInfoAmazon().getNumeroPezzi()!=0)
	    	pw.print(a.getInfoAmazon().getQuantitaMassimaSpedizioneCumulativa());
	    else pw.print("100");
	    pw.print("	");
	    
	    //(CB) PAESE DI ORIGINE
	    if (a.getInfoAmazon().getPaeseOrigine()!=null && !a.getInfoAmazon().getPaeseOrigine().trim().isEmpty())
	    	pw.print(a.getInfoAmazon().getPaeseOrigine());
	    pw.print("	");
	    
	    /* fine informazioni sull'offerta */
	    
	    /* inizio dimensioni prodotto */
	    
	    //(GG) PESO SPEDIZIONE
	    pw.print("	");
	    
	    //(GH) UNITA' MISURA PESO SPEDIZIONE
	    pw.print("	");
	    
	    //(GI) LUNGHEZZA ARTICOLO
	    pw.print("	");
	    
	    //(GJ) ALTEZZA ARTICOLO
	    pw.print("	");
	    
	    //(GK) PESO ARTICOLO
	    pw.print("	");
	    
	    //(GL) UNITA' MISURA PESO ARTICOLO
	    pw.print("	");
	    
	    /* fine dimensioni prodotto */
	    
	    /* inizio informazioni sulla scoperta del prodotto */
	    
	    //(GM) CODICE ARTICOLO DEL PRODUTTORE
	    if (a.getCodiceArticoloFornitore()!=null && !a.getCodiceArticoloFornitore().trim().isEmpty())
	    	pw.print(a.getCodiceArticoloFornitore());
	    else pw.print(a.getCodice());
	    pw.print("	");
	    
	    //(GN) PAROLE CHIAVE 1
	    if (a.getParoleChiave1()!=null && !a.getParoleChiave1().trim().isEmpty())
	    	pw.print(Methods.cut100(a.getParoleChiave1()));
	    pw.print("	");
	    
	    //(GO) PAROLE CHIAVE 2
	    if (a.getParoleChiave2()!=null && !a.getParoleChiave2().trim().isEmpty())
	    	pw.print(Methods.cut100(a.getParoleChiave2()));
	    pw.print("	");
	    
	    //(GP) PAROLE CHIAVE 3
	    if (a.getParoleChiave3()!=null && !a.getParoleChiave3().trim().isEmpty())
	    	pw.print(Methods.cut100(a.getParoleChiave3()));
	    else pw.print(Methods.primeLettereMaiuscole(a.getNome()));
	    pw.print("	");
	    
	    //(GQ) PAROLE CHIAVE 4
	    if (a.getParoleChiave4()!=null && !a.getParoleChiave4().trim().isEmpty())
	    	pw.print(Methods.cut100(a.getParoleChiave4()));
	    pw.print("	");
	    
	    //(GR) PAROLE CHIAVE 5
	    if (a.getParoleChiave5()!=null && !a.getParoleChiave5().trim().isEmpty())
	    	pw.print(Methods.cut100(a.getParoleChiave5()));
	    pw.print("	");
	    
	    /* fine informazioni sulla scoperta del prodotto */
	    
	    /* inizio informazioni prezzo saldo e ribasso */
	    
	    //(GS) PREZZO DI VENDITA
	    pw.print("	");
	    
	    //(GT) DATA FINE SALDO
	    pw.print("	");
	    
	    //(GU) DATA INIZIO SALDO
	    pw.print("	");
	    
	    /* fine informazioni prezzo saldo e ribasso */
	    
	    /* inizio informazioni varianti */
	    
	  	//(GV) FILIAZIONE (parent, child)
	    pw.print("parent");
	    pw.print("	");
	    
	    /*	lascia vuoto	*/
	  	//(GW) PARENT SKU
	    pw.print("	");
	    
	    /*	lascia vuoto	*/
	  	//(GX) TIPO RELAZIONE (variante, accessorio)
	    pw.print("	");
	    
	  	//(GY) TEMA VARIANTE (colore, taglia, taglia-colore,fragranza, taglia-fragranza)
	    pw.print("Color");
	    pw.print("	");
	    
	  	//(GZ) DIMENSIONI (SE IL TEMA è TAGLIA, ES. VALORE VALIDO: S)
	    pw.print("	");
	    
	  	//(HA) MAPPA DIMENSIONI
	    pw.print("	");
	    
	    /*	lascia vuoto	*/
	  	//(HB) COLORE
	    pw.print("	");
	    
	    /*	lascia vuoto	*/
	  	//(HC) MAPPA COLORE
	    pw.print("	");
	    
	    /* fine informazioni varianti */
	    
	    /* fine !!! */
	    
	    pw.println();
		
	}
	
	private static void aggiungiChild(Articolo a, Variante_Articolo v, PrintWriter pw, boolean delete){
		//System.out.println("Inizio a scrivere sul file");
		
		/* inizio product basic information */
		
		//(A) SKU 
		pw.print(a.getCodice()+"-"+v.getValore().trim().replace(" ", "_"));
		pw.print("	");
	    
	    //(B) CODICE BARRE 
		if (v.getCodiceBarre()==null || v.getCodiceBarre().trim().isEmpty())
			pw.print("");		
		else pw.print(v.getCodiceBarre());		
		pw.print("	");
	    
	    //(C) TIPO CODICE (EAN) 
		if (v.getCodiceBarre()==null || v.getCodiceBarre().trim().isEmpty())
			pw.print("");		
		else pw.print("EAN");
		pw.print("	");
	    
	    //(D) NOME ARTICOLO 
		String nome = Methods.primeLettereMaiuscole(a.getNome());
		if (a.getCodice().contains("ZELDA")){
			nome = nome+"("+a.getCodice()+")";
		} else if (a.getCodice().contains("TORTA")){
			nome = nome+"("+a.getCodice().replace("BOMBONIERE-", "").replace("BOMBONIERE_", "")+")";
		}
		pw.print(nome+" ("+v.getValore()+")");
		pw.print("	");

		
	    //(E) MARCA 
		if (v.getCodiceBarre()==null || v.getCodiceBarre().trim().isEmpty())
			pw.print("Zelda Bomboniere");		
		else pw.print(a.getCategoria().getNomeCategoria());
		pw.print("	");

	    //(F) PRODUTTORE 
		pw.print("Zelda Bomboniere");
		pw.print("	");

	    //(G) DESCRIZIONE 
		pw.print(Methods.MaiuscolaDopoPunto(a.getDescrizione())+" (Variante "+v.getValore()+")".replaceAll("[\n\r]", ""));
		pw.print("	");

	    //(H) PUNTO ELENCO 1 
	    if (a.getQuantitaInserzione()!=null && !a.getQuantitaInserzione().trim().isEmpty()){
	    	pw.print("Quantità inserzione: "+a.getQuantitaInserzione());  
	    }
	    pw.print("	");

	    //(I) PUNTO ELENCO 2 
	    if (a.getDimensioni()!=null && !a.getDimensioni().trim().isEmpty()){
	    	pw.print("Dimensioni: "+a.getDimensioni()); 
		}
	    pw.print("	");

	    //(J) PUNTO ELENCO 3 
	    /*if (a.getInfoAmazon().getPuntoElenco3()!=null && !a.getInfoAmazon().getPuntoElenco3().trim().isEmpty()){
	    	pw.print(a.getInfoAmazon().getPuntoElenco3()); 
		}*/
	    pw.print("Codice Articolo: "+a.getCodice());
	    pw.print("	");

	    //(K) PUNTO ELENCO 4 
	    /*if (a.getInfoAmazon().getPuntoElenco4()!=null && !a.getInfoAmazon().getPuntoElenco4().trim().isEmpty()){
	    	pw.print(a.getInfoAmazon().getPuntoElenco4()); 
		}*/
	    pw.print(Methods.cut500("Descrizione: "+Methods.MaiuscolaDopoPunto(a.getDescrizione())+" (Variante "+v.getValore()+")".replaceAll("[\n\r]", "")));
	    pw.print("	");

	    //(L) PUNTO ELENCO 5 
	    if (a.getInfoAmazon().getPuntoElenco5()!=null && !a.getInfoAmazon().getPuntoElenco5().trim().isEmpty()){
	    	pw.print(a.getInfoAmazon().getPuntoElenco5()); 
		}
	    pw.print("	");

	    //(M) NODO NAVIGAZIONE 1 
	    if (a.getInfoAmazon().getIdCategoria1()!=null 
	    		&& !a.getInfoAmazon().getIdCategoria1().trim().isEmpty()
	    		&& !a.getInfoAmazon().getIdCategoria1().trim().equals("null")
		    	&& a.getInfoAmazon().getIdCategoria1()!="-1"
		    	&& a.getInfoAmazon().getIdCategoria1()!="0")
	    	pw.print(a.getInfoAmazon().getIdCategoria1());  
	    else pw.print("731697031");
	    //	    pw.print(a.getInfoAmazon().getIdCategoria1());  
	    pw.print("	");

	    //(N) NODO NAVIGAZIONE 2 
	    if (a.getInfoAmazon().getIdCategoria2()!=null 
	    		&& !a.getInfoAmazon().getIdCategoria2().trim().isEmpty()
	    		&& !a.getInfoAmazon().getIdCategoria2().trim().equals("null")
		    	&& a.getInfoAmazon().getIdCategoria2()!="-1"
		    	&& a.getInfoAmazon().getIdCategoria2()!="0")
	    	pw.print(a.getInfoAmazon().getIdCategoria2());  
	    else pw.print("2906926031");
	    //	    pw.print(a.getInfoAmazon().getIdCategoria2());
	    pw.print("	");

	    //(O) TIPO PRODOTTO 
	    pw.print("FurnitureAndDecor"); 
	    pw.print("	");
	    
	    /* fine product basic information */
	    
	    /* inizio informazioni sull'immagine */
	    
	    //(BB) URL IMMAGINE PRINCIPALE
	    if (!delete)
	    	pw.print(Costanti.percorsoImmaginiRemoto+v.getImmagine()); 
	    pw.print("	");
	    
	    boolean img1messa =  false;
	    
	    //(BC) URL ALTRA IMMAGINE 1
	    if (!delete && a.getImmagine2()!=null && !a.getImmagine2().trim().isEmpty()){
	    	pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine2()+"");  
	    } else if (!img1messa) {
	    	pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine1()); 
	    	img1messa=true;
	    }
	    pw.print("	");
	    
	    //(BD) URL ALTRA IMMAGINE 2
	    if (!delete && a.getImmagine3()!=null && !a.getImmagine3().trim().isEmpty()){
	    	pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine3()+"");  
	    } else if (!img1messa) {
	    	pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine1()); 
	    	img1messa=true;
	    }
	    pw.print("	");
	    
	    //(BE) URL ALTRA IMMAGINE 3
	    if (!delete && a.getImmagine4()!=null && !a.getImmagine4().trim().isEmpty()){
	    	pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine4()+"");  
	    } else if (!img1messa) {
	    	pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine1()); 
	    	img1messa=true;
	    }
	    pw.print("	");
	    
	    //(BF) URL ALTRA IMMAGINE 4
	    if (!delete && a.getImmagine5()!=null && !a.getImmagine5().trim().isEmpty()){
	    	pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine5()+"");  
	    } else if (!img1messa) {
	    	pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine1()); 
	    	img1messa=true;
	    }
	    pw.print("	");
	    
	    //(BG) URL ALTRA IMMAGINE 5	
	    if (!img1messa) {
	    	pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine1()); 
	    	img1messa=true;
	    }
	    pw.print("	");
		//(BH) URL ALTRA IMMAGINE 6
	    pw.print("	");
		//(BI) URL ALTRA IMMAGINE 7
	    pw.print("	");
		//(BJ) URL ALTRA IMMAGINE 8
	    pw.print("	");
	    
	    //(BK) URL IMMAGINE SWITCH
	    pw.print("	");
	    
	    /* fine informazioni sull'immagine */
	    
	    /* inizio informazioni legali */
	    
	    //(BL) ESCLUSIONE RESPOSABILITA'
	    if (a.getInfoAmazon().getEsclusioneResponsabilita()!=null && !a.getInfoAmazon().getEsclusioneResponsabilita().trim().isEmpty())
	    	pw.print(a.getInfoAmazon().getEsclusioneResponsabilita());
	    pw.print("	");
	    
	    //(BM) DESCRIZIONE GARANZIA VENDITORE
	    if (a.getInfoAmazon().getDescrizioneGaranziaVenditore()!=null && !a.getInfoAmazon().getDescrizioneGaranziaVenditore().trim().isEmpty())
	   	 	pw.print(a.getInfoAmazon().getDescrizioneGaranziaVenditore());
	    pw.print("	");
	    
	    //(BN) AVVERTENZE SICUREZZA
	    if (a.getInfoAmazon().getAvvertenzeSicurezza()!=null && !a.getInfoAmazon().getAvvertenzeSicurezza().trim().isEmpty())
	    	pw.print(a.getInfoAmazon().getAvvertenzeSicurezza());
	    pw.print("	");
	    
	    /* fine informazioni legali */
	    
	    //(BO) AGGIORNA O RIMUOVI
	    if (delete) pw.print("Delete");
	    pw.print("	");
	    
	    /* inizio informazioni sull'offerta */
	    
	    //(BP) PREZZO
	    double prezzo = a.getPrezzoDettaglio();
  	  	if (a.getPrezzoPiattaforme()>0) prezzo = a.getPrezzoPiattaforme();
	    pw.print(String.valueOf(prezzo).replace(",", ".")); 
	    pw.print("	");
	    
	    //(BQ) VALUTA
	    pw.print("EUR"); 
	    pw.print("	");
	    
	    //(BR) QUANTITA'
	    if (v.getQuantita()>0)
	    	pw.print(v.getQuantita());  
	    else pw.print(10);  
	    pw.print("	");
	    
	    //(BS) CONDIZIONI
	    pw.print("New"); 
	    pw.print("	");
	    
	    //(BT) NOTA CONDIZIONI
	    if (a.getInfoAmazon().getNotaCondizioni()!=null && !a.getInfoAmazon().getNotaCondizioni().trim().isEmpty())
	    	pw.print(a.getInfoAmazon().getNotaCondizioni());
	    pw.print("	");
	    
	    //(BU) VOCE PACCHETTO QUANTITA'
	    if (a.getInfoAmazon().getVocePacchettoQuantita()!=0)
	    	pw.print(a.getInfoAmazon().getVocePacchettoQuantita());
	    else pw.print("1");
	    pw.print("	");
	    
	    //(BV) NUMERO PEZZI
	    if (a.getInfoAmazon().getNumeroPezzi()!=0)
	    	pw.print(a.getInfoAmazon().getNumeroPezzi());
	    else pw.print("1");
	    pw.print("	");
	    
	    //(BW) DATA LANCIO
	    pw.print("	");
	    
	    //(BX) DATA RILASCIO
	    pw.print("	");
	    
	    //(BY) TEMPI ESECUZIONE SPEDIZIONE	    
	    String tempi = "";
	    if (a.getInfoAmazon().getTempiEsecuzioneSpedizione()!=0)
	    	tempi = String.valueOf(a.getInfoAmazon().getTempiEsecuzioneSpedizione());
	    else	if (a.getCodice().contains("ZELDA") || a.getCodice().contains("TORTA")){
	    	tempi = "7";
		}
	    pw.print(tempi);
	    pw.print("	");
	    
	    //(BZ) DATA RIFORNIMENTO
	    pw.print("	");
	    
	    //(CA) QUANTITA' MASSIMA SPEDIZIONE CUMULATIVA
	    if (a.getInfoAmazon().getNumeroPezzi()!=0)
	    	pw.print(a.getInfoAmazon().getQuantitaMassimaSpedizioneCumulativa());
	    else pw.print("100");
	    pw.print("	");
	    
	    //(CB) PAESE DI ORIGINE
	    if (a.getInfoAmazon().getPaeseOrigine()!=null && !a.getInfoAmazon().getPaeseOrigine().trim().isEmpty())
	    	pw.print(a.getInfoAmazon().getPaeseOrigine());
	    pw.print("	");
	    
	    /* fine informazioni sull'offerta */
	    
	    /* inizio dimensioni prodotto */
	    
	    //(GG) PESO SPEDIZIONE
	    pw.print("	");
	    
	    //(GH) UNITA' MISURA PESO SPEDIZIONE
	    pw.print("	");
	    
	    //(GI) LUNGHEZZA ARTICOLO
	    if (a.getInfoAmazon().getLunghezzaArticolo()!=0)
	    	pw.print(a.getInfoAmazon().getLunghezzaArticolo()); 
	    pw.print("	");
	    
	    //(GJ) ALTEZZA ARTICOLO
	    if (a.getInfoAmazon().getAltezzaArticolo()!=0)
	    	pw.print(a.getInfoAmazon().getAltezzaArticolo()); 
	    pw.print("	");
	    
	    //(GK) PESO ARTICOLO
	    if (a.getInfoAmazon().getPesoArticolo()!=0)
	    	pw.print(a.getInfoAmazon().getPesoArticolo()); 
	    pw.print("	");
	    
	    //(GL) UNITA' MISURA PESO ARTICOLO
	    if (a.getInfoAmazon().getUnitaMisuraPesoArticolo()!=null && !a.getInfoAmazon().getUnitaMisuraPesoArticolo().trim().isEmpty())
	    	pw.print(a.getInfoAmazon().getUnitaMisuraPesoArticolo()); 
	    pw.print("	");
	    
	    /* fine dimensioni prodotto */
	    
	    /* inizio informazioni sulla scoperta del prodotto */
	    
	    //(GM) CODICE ARTICOLO DEL PRODUTTORE
	    if (a.getCodiceArticoloFornitore()!=null && !a.getCodiceArticoloFornitore().trim().isEmpty())
	    	pw.print(a.getCodiceArticoloFornitore());
	    else pw.print(a.getCodice());
	    pw.print("	");
	    
	    //(GN) PAROLE CHIAVE 1
	    if (a.getParoleChiave1()!=null && !a.getParoleChiave1().trim().isEmpty())
	    	pw.print(Methods.cut100(a.getParoleChiave1()));
	    pw.print("	");
	    
	    //(GO) PAROLE CHIAVE 2
	    if (a.getParoleChiave2()!=null && !a.getParoleChiave2().trim().isEmpty())
	    	pw.print(Methods.cut100(a.getParoleChiave2()));
	    pw.print("	");
	    
	    //(GP) PAROLE CHIAVE 3
	    if (a.getParoleChiave3()!=null && !a.getParoleChiave3().trim().isEmpty())
	    	pw.print(Methods.cut100(a.getParoleChiave3()));
	    else pw.print(Methods.primeLettereMaiuscole(a.getNome()));
	    pw.print("	");
	    
	    //(GQ) PAROLE CHIAVE 4
	    if (a.getParoleChiave4()!=null && !a.getParoleChiave4().trim().isEmpty())
	    	pw.print(Methods.cut100(a.getParoleChiave4()));
	    pw.print("	");
	    
	    //(GR) PAROLE CHIAVE 5
	    if (a.getParoleChiave5()!=null && !a.getParoleChiave5().trim().isEmpty())
	    	pw.print(Methods.cut100(a.getParoleChiave5()));
	    pw.print("	");
	    
	    /* fine informazioni sulla scoperta del prodotto */
	    
	    /* inizio informazioni prezzo saldo e ribasso */
	    
	    //(GS) PREZZO DI VENDITA
	    pw.print("	");
	    
	    //(GT) DATA FINE SALDO
	    pw.print("	");
	    
	    //(GU) DATA INIZIO SALDO
	    pw.print("	");
	    
	    /* fine informazioni prezzo saldo e ribasso */
	    
	    /* inizio informazioni varianti */
	    
	  	//(GV) FILIAZIONE (parent, child)
	    pw.print("child");
	    pw.print("	");
	    
	  	//(GW) PARENT SKU
	    pw.print(a.getCodice());
	    pw.print("	");
	    
	  	//(GX) TIPO RELAZIONE (variante, accessorio)
	    pw.print("Variation");
	    pw.print("	");
	    
	  	//(GY) TEMA VARIANTE (colore, taglia, taglia-colore,fragranza, taglia-fragranza)
	    pw.print("Color");
	    pw.print("	");
	    
	  	//(GZ) DIMENSIONI (SE IL TEMA è TAGLIA, ES. VALORE VALIDO: S)
	    pw.print("	");
	    
	  	//(HA) MAPPA DIMENSIONI
	    pw.print("	");
	    
	  	//(HB) COLORE
	    if (v.getValore()!=null && !v.getValore().trim().isEmpty())
	    	pw.print(v.getValore());
	    pw.print("	");
	    
	  	//(HC) MAPPA COLORE (black, blue, bronze, brown, gold, grey, green, metallic, off-white, orange, pink, purple, red, silver, white)
	    if (v.getValore()!=null && !v.getValore().trim().isEmpty())
	    	pw.print(mapColor(v.getValore()));
	    pw.print("	");
	    
	    /* fine informazioni varianti */
	    
	    /* fine !!! */
	    
	    pw.println();
		
	}
	
	
	
	
	private static String mapColor(String val){
		
		String s = "";
		
		if (val.toLowerCase().contains("ner")) s = "black";
		else if (val.toLowerCase().contains("blu")) s = "blue";
		else if (val.toLowerCase().contains("turches")) s = "turquoise";
		else if (val.toLowerCase().contains("celest")) s = "blue";
		else if (val.toLowerCase().contains("azzurr")) s = "blue";
		else if (val.toLowerCase().contains("bronzo")) s = "bronze";
		else if (val.toLowerCase().contains("rame")) s = "bronze";
		else if (val.toLowerCase().contains("ruggine")) s = "bronze";
		else if (val.toLowerCase().contains("marron")) s = "brown";
		else if (val.toLowerCase().contains("legno")) s = "brown";
		else if (val.toLowerCase().contains("sabbi")) s = "brown";
		else if (val.toLowerCase().contains("oro")) s = "gold";
		else if (val.toLowerCase().contains("grigi")) s = "grigio";
		else if (val.toLowerCase().contains("verd")) s = "green";
		else if (val.toLowerCase().contains("tiffan")) s = "green";
		else if (val.toLowerCase().contains("metal")) s = "metallico";
		else if (val.toLowerCase().contains("panna")) s = "bianco sporco";
		else if (val.toLowerCase().contains("avorio")) s = "bianco sporco";
		else if (val.toLowerCase().contains("ecru")) s = "bianco sporco";
		else if (val.toLowerCase().contains("beige")) s = "beige";
		else if (val.toLowerCase().contains("aranc")) s = "orange";
		else if (val.toLowerCase().contains("pesc")) s = "orange";
		else if (val.toLowerCase().contains("ambra")) s = "orange";
		else if (val.toLowerCase().contains("rosa")) s = "pink";
		else if (val.toLowerCase().contains("fucsia")) s = "pink";
		else if (val.toLowerCase().contains("fuxia")) s = "pink";
		else if (val.toLowerCase().contains("ciclamino")) s = "pink";
		else if (val.toLowerCase().contains("viol")) s = "purple";
		else if (val.toLowerCase().contains("lavanda")) s = "purple";
		else if (val.toLowerCase().contains("lilla")) s = "purple";
		else if (val.toLowerCase().contains("ross")) s = "red";
		else if (val.toLowerCase().contains("bord")) s = "red";
		else if (val.toLowerCase().contains("argent")) s = "silver";
		else if (val.toLowerCase().contains("giallo")) s = "yellow";
		else if (val.toLowerCase().contains("senape")) s = "yellow";
		else if (val.toLowerCase().contains("ocra")) s = "yellow";
		else if (val.toLowerCase().contains("bianc")) s = "white";
		else s="multicolore";
		
		return s;
	}
	

	
	private static void aggiungiSpedizione(Map<String,String> num, PrintWriter pw){
		
		//order-id
		pw.print(num.get("id_ordine_piattaforma"));
		pw.print("	");	
		
		//order-item-id (obbligatorio solo per spedizioni parziali)
		pw.print("");
		pw.print("	");
		
		//quantity (obbligatorio solo per spedizioni parziali)
		pw.print("");
		pw.print("	");
		
		//Data Spedizione nel formato aaaa-mm-gg (obbligatorio solo per spedizioni parziali)
		pw.print(num.get("data"));
		pw.print("	");
		
		//Codice Corriere
		pw.print(num.get("nome_corriere"));
		pw.print("	");
				
		//Nome Corriere
		pw.print(num.get("nome_corriere"));
		pw.print("	");
		
		//Numero tracciamento
		pw.print(num.get("numero_tracciamento"));
		pw.print("	");
		
		//Metodo spedizione
		pw.print("Corriere Espresso");
		pw.print("	");
		
	    /* fine !!! */
	    
	    pw.println();
		
	}
	
}
