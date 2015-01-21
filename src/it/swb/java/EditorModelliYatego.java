package it.swb.java;

import it.swb.log.Log;
import it.swb.model.Articolo;
import it.swb.model.Categoria;
import it.swb.model.Variante_Articolo;
import it.swb.model.Yatego_Articolo;
import it.swb.utility.Convertitore;
import it.swb.utility.Costanti;
import it.swb.utility.Methods;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class EditorModelliYatego {
	
	public static int aggiungiProdottoAModelloYatego(Articolo a){
		Log.debug("Start aggiungiProdottoAModelloYatego");
		Properties config = new Properties();	   
		int risultato = 0;
		CSVWriter writer = null;
		 
	    try {
			config.load(Log.class.getResourceAsStream("/zeus.properties"));
			
			String data = Methods.getMesePerNomeFileTesto();
									
			String percorso = config.getProperty("percorso_modello_caricamento_prodotti_yatego");		
			
			percorso = percorso.replace("DATA", data);
			
			File f = new File(percorso);
			
			if (!f.exists()) {
				writer = new CSVWriter(new FileWriter(percorso), ';');
				
				String xxx = "foreign_id#article_nr#title#tax#price#price_uvp#price_purchase#tax_differential" +
			     		"#units#delivery_surcharge#delivery_calc_once#short_desc#long_desc#url#auto_linefeet" +
			     		"#picture#picture2#picture3#picture4#picture5#categories#variants#discount_set_id#stock" +
			     		"#delivery_date#quantity_unit#package_size#cross_selling#ean#isbn#manufacturer" +
			     		"#mpn#delitem#status#top_offer";
			     
			    String[] colonne = xxx.split("#");			     			     
			    writer.writeNext(colonne);
			}
			else writer = new CSVWriter(new FileWriter(percorso,true), ';');
			
			
			
			
			if (a.getVarianti()!=null && !a.getVarianti().isEmpty()){
				Log.debug("Inizio scrittura varianti...");
				
				/*	inizio creazione del file testata	*/
				percorso = config.getProperty("percorso_modello_caricamento_varianti_testata_yatego").replace("DATA", data);						
				f = new File(percorso);				
				CSVWriter writerVariantiTestata = null;				
				if (!f.exists()) {
					writerVariantiTestata = new CSVWriter(new FileWriter(percorso), ';');					
					String xxx = "foreign_id#vs_title#variant_set_name#set_sorting#delitem";				     
				    String[] colonne = xxx.split("#");				     			     
				    writerVariantiTestata.writeNext(colonne);
				}
				else writerVariantiTestata = new CSVWriter(new FileWriter(percorso,true), ';'); 
				/*	fine creazione del file testata, ora posso scriverci	*/				
				
				/*	inizio creazione del file dettagli	*/
				percorso = config.getProperty("percorso_modello_caricamento_varianti_dettaglio_yatego").replace("DATA", data);						
				f = new File(percorso);				
				CSVWriter writerVariantiDettaglio = null;				
				if (!f.exists()) {
					writerVariantiDettaglio = new CSVWriter(new FileWriter(percorso), ';');					
					String xxx = "variant_set_id#foreign_id#title#small_desc#long_desc#picture#price#sorting_number#delitem";				     
				    String[] colonne = xxx.split("#");				     			     
				    writerVariantiDettaglio.writeNext(colonne);
				}
				else writerVariantiDettaglio = new CSVWriter(new FileWriter(percorso,true), ';'); 				
				/*	fine creazione del file dettagli	*/
				
				/*	inizio creazione del file magazzino	*/
				percorso = config.getProperty("percorso_modello_caricamento_magazzino_yatego").replace("DATA", data);						
				f = new File(percorso);				
				CSVWriter writerMagazzino = null;				
				if (!f.exists()) {
					writerMagazzino = new CSVWriter(new FileWriter(percorso), ';');										
					String xxx = "foreign_id#article_id#variant_ids#stock_value#delivery_date#active#article_nr#price#quantity_unit#package_size#ean#isbn#mpn#delitem";				     
				    String[] colonne = xxx.split("#");				     			     
				    writerMagazzino.writeNext(colonne);
				}
				else writerMagazzino = new CSVWriter(new FileWriter(percorso,true), ';'); 				
				/*	fine creazione del file magazzino	*/
				
				int i=0;			
				
				for (Variante_Articolo v : a.getVarianti()){
					
					if (i==0) aggiungiTestataVariante(v, writerVariantiTestata,false);
					
					aggiungiDettaglioVariante(v, writerVariantiDettaglio, i,false);
					aggiungiAlMagazzino(v, writerMagazzino,false);
					i++;
				}
								
				writerVariantiTestata.close();
				writerVariantiDettaglio.close();
				writerMagazzino.close();
				
				Log.debug("Fine scrittura varianti.");
			}
						
			Yatego_Articolo y = Convertitore.convertiArticoloInYatego_Articolo(a);
			aggiungiArticolo(y,writer,false);
		
			writer.close();
			
			risultato = 1;
	
		} catch (IOException e) {
			e.printStackTrace();
			Log.info(e);
			risultato = 0;
		}		
	     Log.debug("Fine aggiungiProdottoAModelloYatego");
	     return risultato;		
	}
	
	
	public static void aggiungiArticolo(Yatego_Articolo y,CSVWriter wr,boolean elimina){
		Log.debug("Inizio scrittura dei dati del prodotto sul foglio CSV per Yatego");
		try {		
			
			String[] rigaAttuale = new String[35];
	    	 
	    	 String foreign_id = y.getForeign_id();
	    	 String article_nr = y.getArticle_nr(); if (article_nr==null) article_nr="";
	    	 String title	=	y.getTitle();
	    	 String tax = String.valueOf(y.getTax());
	    	 String price = String.valueOf(y.getPrice()).replace(".", ",");
	    	 String price_uvp = "";
	    	 String price_purchase = "";
	    	 String tax_differential = "";
	    	 String units = "";
	    	 String delivery_surcharge = "";
	    	 String delivery_calc_once = "";
	    	 String short_desc = y.getShort_desc();
	    	 String long_desc; 
	    	 String url = "";
	    	 String auto_linefeet = "1";
	    	 String picture;
	    	 String picture2;
	    	 String picture3;
	    	 String picture4;
	    	 String picture5;
	    	 String categories = y.getCategories();
	    	 String variants = y.getVariants();
	    	 String discount_set_id = "";
	    	 String stock = String.valueOf(y.getStock());
	    	 String delivery_date = "";
	    	 String quantity_unit = y.getQuantity_unit(); if (quantity_unit==null) quantity_unit="";
	    	 String package_size = "";
	    	 String cross_selling = "";
	    	 String ean = y.getEan(); if (ean==null || ean.isEmpty()) ean="";
	    	 String isbn = "";
	    	 String manufacturer = "";
	    	 String mpn = y.getMpn(); if (mpn==null || mpn.isEmpty()) mpn="";
	    	 String delitem;
	    	 if(elimina) {
	    		 delitem= "1";
	    		 long_desc="";
	    		 picture="";
		    	 picture2="";
		    	 picture3="";
		    	 picture4="";
		    	 picture5="";
	    	 }
	    	 else {
	    		 delitem = "";
	    		 long_desc = y.getLong_desc();
	    		 picture = y.getPicture();	if (picture ==null) picture="";
		    	 picture2 = y.getPicture2(); if (picture2==null) picture2="";
		    	 picture3 = y.getPicture3(); if (picture3==null) picture3="";
		    	 picture4 = y.getPicture4(); if (picture4==null) picture4="";
		    	 picture5 = y.getPicture5(); if (picture5==null) picture5="";
	    	 }
	    	 String status = "1";
	    	 //String top_offer = "";
	    	 
	    	 rigaAttuale[0] = foreign_id;
	    	 rigaAttuale[1] = article_nr;
	    	 rigaAttuale[2] = title;
	    	 rigaAttuale[3] = tax;
	    	 rigaAttuale[4] = price;
	    	 rigaAttuale[5] = price_uvp;
	    	 rigaAttuale[6] = price_purchase;
	    	 rigaAttuale[7] = tax_differential;
	    	 rigaAttuale[8] = units;
	    	 rigaAttuale[9] = delivery_surcharge;
	    	 rigaAttuale[10] = delivery_calc_once;
	    	 rigaAttuale[11] = short_desc;
	    	 rigaAttuale[12] = long_desc;
	    	 rigaAttuale[13] = url;
	    	 rigaAttuale[14] = auto_linefeet;
	    	 rigaAttuale[15] = picture;
	    	 rigaAttuale[16] = picture2;
	    	 rigaAttuale[17] = picture3;
	    	 rigaAttuale[18] = picture4;
	    	 rigaAttuale[19] = picture5;
	    	 rigaAttuale[20] = categories;
	    	 rigaAttuale[21] = variants;
	    	 rigaAttuale[22] = discount_set_id;
	    	 rigaAttuale[23] = stock;
	    	 rigaAttuale[24] = delivery_date;
	    	 rigaAttuale[25] = quantity_unit;
	    	 rigaAttuale[26] = package_size;
	    	 rigaAttuale[27] = cross_selling;
	    	 rigaAttuale[28] = ean;
	    	 rigaAttuale[29] = isbn;
	    	 rigaAttuale[30] = manufacturer;
	    	 rigaAttuale[31] = mpn;
	    	 rigaAttuale[32] = delitem;
	    	 rigaAttuale[33] = status;
	    	 //rigaAttuale[34] = top_offer = "";
	    	 
	    	 
		     wr.writeNext(rigaAttuale);			   
			
		} catch (Exception e) {
			Log.info(e);
			e.printStackTrace();
		}
		Log.debug("Fine scrittura dei dati del prodotto sul foglio CSV per Yatego");
	}
	
	
	public static void aggiungiTestataVariante(Variante_Articolo v,CSVWriter wr,boolean elimina){
		//Log.debug("Inizio scrittura della testata variante sul foglio CSV per Yatego");
		try {				
			String[] riga = new String[5];
	    	 
	    	String foreign_id = v.getCodiceArticolo();
	    	String vs_title = v.getTipo();
	    	String variant_set_name	=	"";
	    	String set_sorting = "abc";
	    	String delitem;
	    	if(elimina) delitem= "1";
	    	else delitem = "";    	
	    	
	    	riga[0] = foreign_id;
	    	riga[1] = vs_title;
	    	riga[2] = variant_set_name;
	    	riga[3] = set_sorting;
	    	riga[4] = delitem;
	    	
		    wr.writeNext(riga);			   
			
		} catch (Exception e) {
			Log.info(e);
			e.printStackTrace();
		}
		//Log.debug("Fine scrittura della testata variante sul foglio CSV per Yatego");
	}
	
	public static void aggiungiDettaglioVariante(Variante_Articolo v,CSVWriter wr, int sortnumber,boolean elimina){
		//Log.debug("Inizio scrittura del dettaglio variante sul foglio CSV per Yatego");
		try {				
			String[] riga = new String[9];
			
			String nomeVariante = v.getCodiceArticolo()+"-"+v.getValore().replace(" ", "_");
			
			if(nomeVariante.length()>30)
				nomeVariante = nomeVariante.substring(0, 30);
	    	 
			String variant_set_id = v.getCodiceArticolo();
	    	String foreign_id = nomeVariante;
	    	String title = v.getValore();
	    	String small_desc = "";
	    	String long_desc = "";
	    	String picture = Costanti.percorsoImmaginiRemoto+v.getImmagine();
	    	String price = "0";
	    	String sorting_number = String.valueOf(sortnumber);
	    	String delitem;
	    	if(elimina) delitem= "1";
	    	else delitem = "";	
	    	
	    	riga[0] = variant_set_id;
	    	riga[1] = foreign_id;
	    	riga[2] = title;
	    	riga[3] = small_desc;
	    	riga[4] = long_desc;
	    	riga[5] = picture;
	    	riga[6] = price;
	    	riga[7] = sorting_number;
	    	riga[8] = delitem;
	    	
		    wr.writeNext(riga);			   
			
		} catch (Exception e) {
			Log.info(e);
			e.printStackTrace();
		}
		//Log.debug("Fine scrittura del dettaglio variante sul foglio CSV per Yatego");
	}
	
	
	public static void aggiungiAlMagazzino(Variante_Articolo v,CSVWriter wr,boolean elimina){
		try {				
			String[] riga = new String[14];
			
			String nomeVariante = v.getCodiceArticolo()+"-"+v.getValore().replace(" ", "_");
			
			if(nomeVariante.length()>30)
				nomeVariante = nomeVariante.substring(0, 30);
					
			String quantita = "10";
			if (v.getQuantita()!=0) quantita = String.valueOf(v.getQuantita());
			
			String foreign_id = v.getCodiceArticolo();
			String article_id = v.getCodiceArticolo();
			String variant_ids = nomeVariante;
	    	String stock_value = quantita;
	    	String delivery_date = "";
	    	String active = "1";
	    	String article_nr = v.getCodiceArticolo();
	    	String price = "";
	    	String quantity_unit = "";
	    	String package_size = "";
	    	String ean = "";
	    	String isbn = "";
	    	String mpn = "";
	    	String delitem;
	    	if(elimina) delitem= "1";
	    	else delitem = "";	    	
	    	
	    	riga[0] = foreign_id;
	    	riga[1] = article_id;
	    	riga[2] = variant_ids;
	    	riga[3] = stock_value;
	    	riga[4] = delivery_date;
	    	riga[5] = active;
	    	riga[6] = article_nr;
	    	riga[7] = price;
	    	riga[8] = quantity_unit;
	    	riga[9] = package_size;
	    	riga[10] = ean;
	    	riga[11] = isbn;
	    	riga[12] = mpn;
	    	riga[13] = delitem;
	    	
		    wr.writeNext(riga);			   
			
		} catch (Exception e) {
			Log.info(e);
			e.printStackTrace();
		}
	}
	
	
	
	public static int eliminaArticolo(Articolo a){
		Log.info("Eliminazione articolo "+a.getCodice()+" da Yatego...");
		Properties config = new Properties();	   
		int risultato = 0;
		CSVWriter writer = null;
		 
	    try {
			config.load(Log.class.getResourceAsStream("/zeus.properties"));
			
			String data = Methods.getMesePerNomeFileTesto();
									
			String percorso = config.getProperty("percorso_modello_caricamento_prodotti_yatego");		
			
			percorso = percorso.replace("DATA", data);
			
			File f = new File(percorso);
			
			if (!f.exists()) {
				writer = new CSVWriter(new FileWriter(percorso), ';');
				
				String xxx = "foreign_id#article_nr#title#tax#price#price_uvp#price_purchase#tax_differential" +
			     		"#units#delivery_surcharge#delivery_calc_once#short_desc#long_desc#url#auto_linefeet" +
			     		"#picture#picture2#picture3#picture4#picture5#categories#variants#discount_set_id#stock" +
			     		"#delivery_date#quantity_unit#package_size#cross_selling#ean#isbn#manufacturer" +
			     		"#mpn#delitem#status#top_offer";
			     
			    String[] colonne = xxx.split("#");			     			     
			    writer.writeNext(colonne);
			}
			else writer = new CSVWriter(new FileWriter(percorso,true), ';');
			
			
			
			
			if (a.getVarianti()!=null && !a.getVarianti().isEmpty()){
				Log.debug("Inizio scrittura varianti...");
				
				/*	inizio creazione del file testata	*/
				percorso = config.getProperty("percorso_modello_caricamento_varianti_testata_yatego").replace("DATA", data);						
				f = new File(percorso);				
				CSVWriter writerVariantiTestata = null;				
				if (!f.exists()) {
					writerVariantiTestata = new CSVWriter(new FileWriter(percorso), ';');					
					String xxx = "foreign_id#vs_title#variant_set_name#set_sorting#delitem";				     
				    String[] colonne = xxx.split("#");				     			     
				    writerVariantiTestata.writeNext(colonne);
				}
				else writerVariantiTestata = new CSVWriter(new FileWriter(percorso,true), ';'); 
				/*	fine creazione del file testata, ora posso scriverci	*/				
				
				/*	inizio creazione del file dettagli	*/
				percorso = config.getProperty("percorso_modello_caricamento_varianti_dettaglio_yatego").replace("DATA", data);						
				f = new File(percorso);				
				CSVWriter writerVariantiDettaglio = null;				
				if (!f.exists()) {
					writerVariantiDettaglio = new CSVWriter(new FileWriter(percorso), ';');					
					String xxx = "variant_set_id#foreign_id#title#small_desc#long_desc#picture#price#sorting_number#delitem";				     
				    String[] colonne = xxx.split("#");				     			     
				    writerVariantiDettaglio.writeNext(colonne);
				}
				else writerVariantiDettaglio = new CSVWriter(new FileWriter(percorso,true), ';'); 				
				/*	fine creazione del file dettagli	*/
				
				/*	inizio creazione del file magazzino	*/
				percorso = config.getProperty("percorso_modello_caricamento_magazzino_yatego").replace("DATA", data);						
				f = new File(percorso);				
				CSVWriter writerMagazzino = null;				
				if (!f.exists()) {
					writerMagazzino = new CSVWriter(new FileWriter(percorso), ';');										
					String xxx = "foreign_id#article_id#variant_ids#stock_value#delivery_date#active#article_nr#price#quantity_unit#package_size#ean#isbn#mpn#delitem";				     
				    String[] colonne = xxx.split("#");				     			     
				    writerMagazzino.writeNext(colonne);
				}
				else writerMagazzino = new CSVWriter(new FileWriter(percorso,true), ';'); 				
				/*	fine creazione del file magazzino	*/
				
				int i=0;			
				
				for (Variante_Articolo v : a.getVarianti()){
					
					if (i==0) aggiungiTestataVariante(v, writerVariantiTestata,true);
					
					aggiungiDettaglioVariante(v, writerVariantiDettaglio, i,true);
					aggiungiAlMagazzino(v, writerMagazzino,true);
					i++;
				}
								
				writerVariantiTestata.close();
				writerVariantiDettaglio.close();
				writerMagazzino.close();
				
				Log.debug("Fine scrittura varianti.");
			}
						
			Yatego_Articolo y = Convertitore.convertiArticoloInYatego_Articolo(a);
			aggiungiArticolo(y,writer,true);
		
			writer.close();
			
			risultato = 1;
	
		} catch (IOException e) {
			e.printStackTrace();
			Log.info(e);
			risultato = 0;
		}		
	     Log.info(risultato);
	     Log.debug("Fine eliminazione articolo da Yatego.");
	     return risultato;		
	}
	
	
	
	
	
	
	/* Passare la data come parametro. Se la data è nulla provvederà con la data del giorno corrente */
	public static void unificaModelli(String data){
		Log.debug("Provo a unificare i modelli...");
		Properties config = new Properties();	   
		CSVWriter writer = null;
		CSVReader reader = null;
		
		try{			
			config.load(Log.class.getResourceAsStream("/zeus.properties"));
			
			if(data==null || data.trim().isEmpty())
				data = Methods.getMesePerNomeFileTesto();
			
			String percorsoVariantiTestata = config.getProperty("percorso_modello_caricamento_varianti_testata_yatego");
			percorsoVariantiTestata = percorsoVariantiTestata.replace("DATA", data);
			String percorsoVariantiDettaglio = config.getProperty("percorso_modello_caricamento_varianti_dettaglio_yatego");	
			percorsoVariantiDettaglio = percorsoVariantiDettaglio.replace("DATA", data);
			String percorsoProdotti = config.getProperty("percorso_modello_caricamento_prodotti_yatego");		
			percorsoProdotti = percorsoProdotti.replace("DATA", data);
			String percorsoMagazzino = config.getProperty("percorso_modello_caricamento_magazzino_yatego");		
			percorsoMagazzino = percorsoMagazzino.replace("DATA", data);
			
			String percorsoFileUnificato = config.getProperty("percorso_modello_caricamento_yatego");		
			percorsoFileUnificato = percorsoFileUnificato.replace("DATA", data);
			writer = new CSVWriter(new FileWriter(percorsoFileUnificato), ';');
			String [] nextLine;
			
			File f1 = new File(percorsoVariantiTestata);			
			if (f1.exists()) {
				reader = new CSVReader(new FileReader(percorsoVariantiTestata), ';');
				
				while ((nextLine = reader.readNext()) != null) {
			    	writer.writeNext(nextLine);
			    }
			    writer.writeNext(new String[0]);
			} else { Log.debug("Nessun file testata varianti trovato."); }
			
			File f2 = new File(percorsoVariantiDettaglio);			
			if (f2.exists()) {
				reader = new CSVReader(new FileReader(percorsoVariantiDettaglio), ';');
				
				while ((nextLine = reader.readNext()) != null) {
			    	writer.writeNext(nextLine);
			    }
			    writer.writeNext(new String[0]);
			} else { Log.debug("Nessun file dettaglio varianti trovato."); }
			
			File f3 = new File(percorsoProdotti);			
			if (f3.exists()) {
				reader = new CSVReader(new FileReader(percorsoProdotti), ';');
				
				while ((nextLine = reader.readNext()) != null) {
			    	writer.writeNext(nextLine);
			    }
				writer.writeNext(new String[0]);
			} else { Log.debug("Nessun file prodotti trovato."); }
			
			File f4 = new File(percorsoMagazzino);			
			if (f4.exists()) {
				reader = new CSVReader(new FileReader(percorsoMagazzino), ';');
				
				while ((nextLine = reader.readNext()) != null) {
			    	writer.writeNext(nextLine);
			    }
			} else { Log.debug("Nessun file magazzino trovato."); }
					    
		    writer.close();
		    if (reader!=null) reader.close();
		    
		}
		catch (Exception e){
			e.printStackTrace();
			Log.info(e);
		}
		Log.debug("Fine unificazione modelli.");
	}
	
	
	public static void creaCsvYategoArticoli(List<Yatego_Articolo> list){
		System.out.println("Inizio creazione Csv Yatego Articoli...");
		  CSVWriter writer;
		try {
			writer = new CSVWriter(new FileWriter("D:\\Yatego_Articoli.csv"), ';');
		
		     // feed in your array (or convert your data to an array)
		     String xxx = "foreign_id#article_nr#title#tax#price#price_uvp#price_purchase#tax_differential" +
		     		"#units#delivery_surcharge#delivery_calc_once#short_desc#long_desc#url#auto_linefeet" +
		     		"#picture#picture2#picture3#picture4#picture5#categories#variants#discount_set_id#stock" +
		     		"#delivery_date#quantity_unit#package_size#cross_selling#ean#isbn#manufacturer" +
		     		"#mpn#delitem#status#top_offer";
		     
		     String[] colonne = xxx.split("#");
		     
		     
		     writer.writeNext(colonne);
		     
		     for (Yatego_Articolo y : list){
		    	 String[] rigaAttuale = new String[35];
		    	 
		    	 String foreign_id = "";
		    	 String article_nr = y.getArticle_nr(); if (article_nr==null) article_nr="";
		    	 String title	=	y.getTitle();
		    	 String tax = "21";
		    	 String price = String.valueOf(y.getPrice()).replace(".", ",");
		    	 String price_uvp = "";
		    	 String price_purchase = "";
		    	 String tax_differential = "";
		    	 String units = "";
		    	 String delivery_surcharge =String.valueOf(y.getDelivery_surcharge());
		    	 String delivery_calc_once = String.valueOf(y.getDelivery_calc_once());
		    	 String short_desc = y.getShort_desc();
		    	 String long_desc = y.getLong_desc();
		    	 String url = "";
		    	 String auto_linefeet = "1";
		    	 String picture = y.getPicture();	if (picture ==null) picture="";
		    	 String picture2 = y.getPicture2(); if (picture2==null) picture2="";
		    	 String picture3 = y.getPicture3(); if (picture3==null) picture3="";
		    	 String picture4 = y.getPicture4(); if (picture4==null) picture4="";
		    	 String picture5 = y.getPicture5(); if (picture5==null) picture5="";
		    	 String categories = y.getCategories();
		    	 String variants = "";
		    	 String discount_set_id = "";
		    	 String stock = "";
		    	 String delivery_date = "";
		    	 String quantity_unit = y.getQuantity_unit(); if (quantity_unit==null) quantity_unit="";
		    	 String package_size = "";
		    	 String cross_selling = "";
		    	 String ean = y.getEan(); if (ean==null) ean="";
		    	 String isbn = "";
		    	 String manufacturer = "";
		    	 String mpn = "";
		    	 String delitem = "";
		    	 String status = "1";
		    	 //String top_offer = "";
		    	 
		    	 rigaAttuale[0] = foreign_id;
		    	 rigaAttuale[1] = article_nr;
		    	 rigaAttuale[2] = title;
		    	 rigaAttuale[3] = tax;
		    	 rigaAttuale[4] = price;
		    	 rigaAttuale[5] = price_uvp;
		    	 rigaAttuale[6] = price_purchase;
		    	 rigaAttuale[7] = tax_differential;
		    	 rigaAttuale[8] = units;
		    	 rigaAttuale[9] = delivery_surcharge;
		    	 rigaAttuale[10] = delivery_calc_once;
		    	 rigaAttuale[11] = short_desc;
		    	 rigaAttuale[12] = long_desc;
		    	 rigaAttuale[13] = url;
		    	 rigaAttuale[14] = auto_linefeet;
		    	 rigaAttuale[15] = picture;
		    	 rigaAttuale[16] = picture2;
		    	 rigaAttuale[17] = picture3;
		    	 rigaAttuale[18] = picture4;
		    	 rigaAttuale[19] = picture5;
		    	 rigaAttuale[20] = categories;
		    	 rigaAttuale[21] = variants;
		    	 rigaAttuale[22] = discount_set_id;
		    	 rigaAttuale[23] = stock;
		    	 rigaAttuale[24] = delivery_date;
		    	 rigaAttuale[25] = quantity_unit;
		    	 rigaAttuale[26] = package_size;
		    	 rigaAttuale[27] = cross_selling;
		    	 rigaAttuale[28] = ean;
		    	 rigaAttuale[29] = isbn;
		    	 rigaAttuale[30] = manufacturer;
		    	 rigaAttuale[31] = mpn;
		    	 rigaAttuale[32] = delitem;
		    	 rigaAttuale[33] = status;
		    	 //rigaAttuale[34] = top_offer = "";
		    	 
		    	 
//		    	 rigaAttuale=(foreign_id+"#"+article_nr+"#"+title+"#"+tax+"#"+price+"#"+price_uvp+"#"+price_purchase+"#"+tax_differential
//		    			 +"#"+units+"#"+delivery_surcharge+"#"+delivery_calc_once+"#"+short_desc+"#"+long_desc+"#"+url
//		    			 +"#"+auto_linefeet+"#"+picture+"#"+picture2+"#"+picture3+"#"+picture4+"#"+picture5
//		    			 +"#"+categories+"#"+variants+"#"+discount_set_id+"#"+stock+"#"+delivery_date+"#"+quantity_unit
//		    			 +"#"+package_size+"#"+cross_selling+"#"+ean+"#"+isbn+"#"+manufacturer+"#"+mpn+"#"+delitem+"#"+status).split("#");
		    	  		    	
			     writer.writeNext(rigaAttuale);			   
		     }
		     		    
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Fine creazione Csv Yatego Articoli.");
	}
	
	
	public static void creaCsvYategoVarianti(List<Yatego_Articolo> list){
		System.out.println("Inizio creazione Csv Yatego Articoli...");
		  CSVWriter writer;
		try {
			writer = new CSVWriter(new FileWriter("D:\\Yatego_Articoli.csv"), ';');
		
		     // feed in your array (or convert your data to an array)
		     String xxx = "foreign_id#article_nr#title#tax#price#price_uvp#price_purchase#tax_differential" +
		     		"#units#delivery_surcharge#delivery_calc_once#short_desc#long_desc#url#auto_linefeet" +
		     		"#picture#picture2#picture3#picture4#picture5#categories#variants#discount_set_id#stock" +
		     		"#delivery_date#quantity_unit#package_size#cross_selling#ean#isbn#manufacturer" +
		     		"#mpn#delitem#status#top_offer";
		     
		     String[] colonne = xxx.split("#");
		     
		     
		     writer.writeNext(colonne);
		     
		     for (Yatego_Articolo y : list){
		    	 String[] rigaAttuale = new String[35];
		    	 
		    	 String foreign_id = "";
		    	 String article_nr = y.getArticle_nr(); if (article_nr==null) article_nr="";
		    	 String title	=	y.getTitle();
		    	 String tax = "21";
		    	 String price = String.valueOf(y.getPrice()).replace(".", ",");
		    	 String price_uvp = "";
		    	 String price_purchase = "";
		    	 String tax_differential = "";
		    	 String units = "";
		    	 String delivery_surcharge =String.valueOf(y.getDelivery_surcharge());
		    	 String delivery_calc_once = String.valueOf(y.getDelivery_calc_once());
		    	 String short_desc = y.getShort_desc();
		    	 String long_desc = y.getLong_desc();
		    	 String url = "";
		    	 String auto_linefeet = "1";
		    	 String picture = y.getPicture();	if (picture ==null) picture="";
		    	 String picture2 = y.getPicture2(); if (picture2==null) picture2="";
		    	 String picture3 = y.getPicture3(); if (picture3==null) picture3="";
		    	 String picture4 = y.getPicture4(); if (picture4==null) picture4="";
		    	 String picture5 = y.getPicture5(); if (picture5==null) picture5="";
		    	 String categories = y.getCategories();
		    	 String variants = "";
		    	 String discount_set_id = "";
		    	 String stock = "";
		    	 String delivery_date = "";
		    	 String quantity_unit = y.getQuantity_unit(); if (quantity_unit==null) quantity_unit="";
		    	 String package_size = "";
		    	 String cross_selling = "";
		    	 String ean = y.getEan(); if (ean==null) ean="";
		    	 String isbn = "";
		    	 String manufacturer = "";
		    	 String mpn = "";
		    	 String delitem = "";
		    	 String status = "1";
		    	 //String top_offer = "";
		    	 
		    	 rigaAttuale[0] = foreign_id;
		    	 rigaAttuale[1] = article_nr;
		    	 rigaAttuale[2] = title;
		    	 rigaAttuale[3] = tax;
		    	 rigaAttuale[4] = price;
		    	 rigaAttuale[5] = price_uvp;
		    	 rigaAttuale[6] = price_purchase;
		    	 rigaAttuale[7] = tax_differential;
		    	 rigaAttuale[8] = units;
		    	 rigaAttuale[9] = delivery_surcharge;
		    	 rigaAttuale[10] = delivery_calc_once;
		    	 rigaAttuale[11] = short_desc;
		    	 rigaAttuale[12] = long_desc;
		    	 rigaAttuale[13] = url;
		    	 rigaAttuale[14] = auto_linefeet;
		    	 rigaAttuale[15] = picture;
		    	 rigaAttuale[16] = picture2;
		    	 rigaAttuale[17] = picture3;
		    	 rigaAttuale[18] = picture4;
		    	 rigaAttuale[19] = picture5;
		    	 rigaAttuale[20] = categories;
		    	 rigaAttuale[21] = variants;
		    	 rigaAttuale[22] = discount_set_id;
		    	 rigaAttuale[23] = stock;
		    	 rigaAttuale[24] = delivery_date;
		    	 rigaAttuale[25] = quantity_unit;
		    	 rigaAttuale[26] = package_size;
		    	 rigaAttuale[27] = cross_selling;
		    	 rigaAttuale[28] = ean;
		    	 rigaAttuale[29] = isbn;
		    	 rigaAttuale[30] = manufacturer;
		    	 rigaAttuale[31] = mpn;
		    	 rigaAttuale[32] = delitem;
		    	 rigaAttuale[33] = status;
		    	 //rigaAttuale[34] = top_offer = "";
		    	 
		    	 
//		    	 rigaAttuale=(foreign_id+"#"+article_nr+"#"+title+"#"+tax+"#"+price+"#"+price_uvp+"#"+price_purchase+"#"+tax_differential
//		    			 +"#"+units+"#"+delivery_surcharge+"#"+delivery_calc_once+"#"+short_desc+"#"+long_desc+"#"+url
//		    			 +"#"+auto_linefeet+"#"+picture+"#"+picture2+"#"+picture3+"#"+picture4+"#"+picture5
//		    			 +"#"+categories+"#"+variants+"#"+discount_set_id+"#"+stock+"#"+delivery_date+"#"+quantity_unit
//		    			 +"#"+package_size+"#"+cross_selling+"#"+ean+"#"+isbn+"#"+manufacturer+"#"+mpn+"#"+delitem+"#"+status).split("#");
		    	  		    	
			     writer.writeNext(rigaAttuale);			   
		     }
		     		    
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Fine creazione Csv Yatego Articoli.");
	}
	
	
	public static void creaCsvYategoCategorie(List<Categoria> list){
		System.out.println("Inizio creaCsvYategoCategorie...");
		  CSVWriter writer;
		try {
			writer = new CSVWriter(new FileWriter("D:\\Yatego_Categorie_NEW.csv"), ';');
		
		     // feed in your array (or convert your data to an array)
		     String[] colonne = "foreign_id_h#foreign_id_m#foreign_id_l#title_h#title_m#title_l#sorting".split("#");
		     String[] rigaVuota = "".split("");
		     
		     writer.writeNext(colonne);
		     
		     for (Categoria c : list){
		    	 String[] rigaAttuale;
		    	 
		    	 if(c.getIdCategoriaPrincipale()==0)
		    		 rigaAttuale=(String.valueOf(c.getIdCategoria())+"###"+c.getNomeCategoria()+"###"+String.valueOf(c.getOrdinamento())).split("#");
		    	 else
		    		 rigaAttuale=(String.valueOf(c.getIdCategoriaPrincipale())+"#"+String.valueOf(c.getIdCategoria())
			    			 +"##"+c.getNomeCategoriaPrincipale()+"#"+c.getNomeCategoria()+"##"+String.valueOf(c.getIdCategoriaPrincipale()*2)).split("#");
		    	 
		    	 writer.writeNext(rigaVuota);
			     writer.writeNext(rigaAttuale);
			   
		     }
		     		    
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Fine creaCsvYategoCategorie.");
	}
	
	
	
//	//NUOVA PROCEDURA
//	//Scorro il csv e faccio la query nel db sui titoli delle inserzioni per controllare quali ho già
//	//Mi faccio restituire la lista di id_inserzioni che NON ho nel db
//	public static List<String> readEbayScambioFileCsvStep1(String percorsoCsv){
//		
//		System.out.println("Inizio readEbayScambioFileCsvStep1: verrà letto "+percorsoCsv);
//		
//		List<String> list = null;
//		CSVReader reader;
//		try {
//			reader = new CSVReader(new FileReader(percorsoCsv),';');		
//			
//			String[] header = reader.readNext();				
//			String [] line;
//			
//			list = new ArrayList<String>();
//			int i = 0;
//			int n = 1;
//			
//			
//			while (((line = reader.readNext()) != null)) {
//				i++;
//				if (i%27==0){
//					System.out.println(n+"%");
//					n++;
//				}
//				if(i>0){
//					String itemIdAttuale = null;
//					String titolo = null;
//					
//					for (int j = 0; j < line.length; j++) {					
//						String current = line[j]; // .trim().toLowerCase();					
//						if (j == 0) {
//							itemIdAttuale = current;
//						}
//						if (j == 13){
//								titolo = current;	//Titolo
//						}						
//					}
//					if(Ebay_DAO.checkIfAlreadyExist(titolo)==0){
//						if(Ebay_DAO.checkIfAlreadyExist(Long.valueOf(itemIdAttuale))==0)
//							list.add(itemIdAttuale);
//					}
//				}	
//			}
//			System.out.println("Fine readEbayScambioFileCsvStep1: "+list.size()+" elementi letti.");
//				
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return list;
//	}
//	
//	//Scarico la descrizione dei soli articoli che ho in lista, e uno alla volta li inserisco nel db
//	public static List<Ebay_Articolo> readEbayScambioFileCsvStep2(List<String> nuovi_articoli,String percorsoCsv){
//		
//		System.out.println("Inizio readEbayScambioFileCsvStep2: verrà letto "+percorsoCsv);
//		
//		List<Ebay_Articolo> list = null;
//		CSVReader reader;
//		try {
//			reader = new CSVReader(new FileReader(percorsoCsv),';');		
//			
//			String[] header = reader.readNext();				
//			String [] line;
//			
//			list = new ArrayList<Ebay_Articolo>();
//			int i = 0;
//			int n = 1;
//			
//			
//			while (((line = reader.readNext()) != null)) {
//				i++;
//				if (i%27==0){
//					System.out.println(n+"%");
//					n++;
//				}
//				if(i>0){
//					
//					boolean exist = true;
//					String idAttuale = null;
//					Ebay_Articolo b = new Ebay_Articolo();
//					
//					for (int j = 0; j < line.length; j++) {					
//						String current = line[j]; // .trim().toLowerCase();						
//											
//						if (j == 0) {
//							 idAttuale = current; // Item ID
//							
//							if (nuovi_articoli.contains(idAttuale)){
//								exist = false;							
//								b.setItemId(Long.valueOf(current));
//								b = Methods.elaboraDescrizione(b,Methods.leggiDaUrl("http://vi.ebaydesc.it/ws/eBayISAPI.dll?ViewItemDescV4&sd=3&item="+current));
//							}
//						}
//						if (!exist){
//							if (j == 5) b.setQuantita(Integer.valueOf(current));		//Quantita
//							if (j == 8) b.setPrezzoArticolo(Double.valueOf(current.replace("EUR ", "").replace(",", ".")));		//Prezzo
//							if (j == 13){
//								if (b.getNomeArticolo()==null)
//									b.setNomeArticolo(current);	//Titolo
//								
//								String url = "http://stores.ebay.it/ZELDABOMBONIERE"+Methods.getUrl(HtmlSource.leggiDaUrl("http://stores.ebay.it/ZELDABOMBONIERE/_i.html?_nkw="+(current.replace(" ", "+"))));
//								if (!url.equals("")){
//									Map<String, String> map_cat = Methods.elaboraCategoria(HtmlSource.leggiDaUrl(url));
//									b.setIdCategoria(Long.valueOf(map_cat.get("id_categoria")));
//									b.setNomeCategoria(map_cat.get("nome_categoria"));
//								}
//								
//							}
//							if (j == 14) b.setNomeCategoriaEbay1(current);	//Categoria Ebay
//							if (j == 15) b.setIdCategoriaEbay1(Long.valueOf(current));		//ID Categoria Ebay							
//						}
//					}
//					if(!exist){
//						list.add(b);
//						Methods.printArticolo(b);
//						Ebay_DAO.insertOrUpdateEbayArticolo(b);
//					}
//				}
//			}
//			System.out.println("Fine readEbayScambioFileCsvPlusHtmlDescription: "+list.size()+" elementi letti.");
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return list;
//	}
	
	
	
	
	

	
	
	

	
	

	public static ArrayList<String> getCsvHeadRow() {

		// Take reader
		ArrayList<String> row = new ArrayList<String>();
		CSVReader reader;
		try {
			reader = new CSVReader(new FileReader("D:\\ebay.csv"),';');
			
			
			@SuppressWarnings("unused")
			String[] header = reader.readNext();
			
			String [] line;

			while (((line = reader.readNext()) != null)) {
				for (int j = 0; j < line.length; j++) {
					String current = line[j]; // .trim().toLowerCase();
					row.add(current);
					if (j == 0) System.out.println("Titolo: " + current);
					if (j == 1) System.out.println("Categoria: " + current);
					if (j == 4) System.out.println("Quantita: " + current);
					if (j == 5) System.out.println("Prezzo: " + current);
					if (j == 7) {
						current = current.toLowerCase();
						int from = current.indexOf("descrizione:");
						int to = current.indexOf("fourcards");
						String temp = current.substring(from, to);
						int from1 = temp.indexOf("lastrow")+9;
						int to1 = temp.indexOf("</dt>");
						System.out.println("Descrizione: "+temp.substring(from1, to1).replace("<br>", "").toLowerCase());
						
						int from2 = current.indexOf("www.zeldabomboniere.it/immagini/") + 32;
						int to2 = current.substring(from2).indexOf(".jpg")+4;
						temp = current.substring(from2, from2+to2);
						int to3 = temp.indexOf(".jpg") + 4;
						System.out.println("Percorso immagine: "+ temp.substring(0, to3));
					}
				}
				System.out.println();
			}
			
			reader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			row = null;
		}
		return row;
	}
	
}
