package it.swb.piattaforme.zelda;

import it.swb.log.Log;
import it.swb.model.Articolo;
import it.swb.model.Variante_Articolo;
import it.swb.utility.DateMethods;
import it.swb.utility.Methods;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

public class ZeldaSQL {
	
	public static boolean aggiungiOEliminaDaModelloZelda(Articolo a, boolean aggiungi){		
		 Log.debug("Start aggiungiProdottoAModelloZelda");
		 Properties config = new Properties();	   
		 boolean ok = false;
	     try {
			config.load(Log.class.getResourceAsStream("/zeus.properties"));
			
			String percorso = config.getProperty("percorso_modello_provvisorio_zelda");		
			String data = DateMethods.getMesePerNomeFileTesto();
			percorso = percorso.replace("DATA", data);
			
			File f = new File(percorso);
			
			if (!f.exists()) {
				f.createNewFile();
			}
			
			FileOutputStream fos = new FileOutputStream (percorso, true);
			
			PrintWriter pw = new PrintWriter (fos);
			
			if (aggiungi){
				insertIntoProduct(a,pw);
				
				insertIntoProductDescription(a, pw);
				insertIntoProductImage(a, pw);
				insertIntoProductToCategory(a, pw);
				insertIntoProductToStore(a, pw);
				
				if (a.getVarianti()!=null && !a.getVarianti().isEmpty()){
					insertIntoProductOption(a,pw);
				}
			}
			else deleteProduct(a, pw);
			
			pw.close();
			
			ok = true;
	
		} catch (IOException e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}		
	     Log.debug("Fine aggiungiProdottoAModelloZelda");
	     return ok;
	}
	
	private static void insertIntoProduct(Articolo a,PrintWriter pw){
			
		java.sql.Date data = new java.sql.Date(new java.util.Date().getTime());
		String codbarre = "";
		if (a.getCodiceBarre()!=null) codbarre = a.getCodiceBarre();
		
		double prezzo = a.getPrezzoDettaglio();
	  	if (a.getPrezzoPiattaforme()>0) prezzo = a.getPrezzoPiattaforme();
		
		String query = "INSERT INTO `product` (`product_id`, `model`, `sku`, `ean`, `quantity`," +		/* 5 */
						" `stock_status_id`, `image`, `manufacturer_id`, `shipping`, `price`," +		/* 10 */
						" `points`, `tax_class_id`, `date_available`, `weight`, `weight_class_id`, " +	/* 15 */
						"`length`, `width`, `height`, `length_class_id`, `subtract`, " +				/* 20 */
						"`minimum`, `sort_order`, `status`, `date_added`, `date_modified`, " +			/* 25 */
						"`viewed`,`upc`,`jan`,`isbn`,`mpn`,`location`) " +								/* 31 */
						"VALUES ("+a.getIdArticolo()+",'"+a.getCodice()+"','"+a.getCodice()+"','"+codbarre+"',"+a.getQuantitaMagazzino()+",6,'articoli/"+a.getImmagine1()+
						"',0,1,"+prezzo+",0,0,'"+data+"',0,1,0,0,0,1,1,1,0,1,'"+data+"','"+data+"',1,'','','','','');";	/* sono 26 */
		
		pw.println(query);
	
		Log.debug("Inserimento riuscito.");					
	}
	
	
	
	private static void insertIntoProductDescription(Articolo a,PrintWriter pw){
		
			String nome = Methods.primeLettereMaiuscole(a.getNome());
		
			String descrizione = "<p>"+Methods.MaiuscolaDopoPunto(a.getDescrizione())+"<br/><br/><b>Quantit&agrave; inserzione:</b> "
					+Methods.MaiuscolaDopoPunto(a.getQuantitaInserzione())+"<br/><br/><b>Dimensioni:</b> "
					+Methods.MaiuscolaDopoPunto(a.getDimensioni())+"</p>";
			
			nome = Methods.escapeQuotes(nome);
			descrizione = Methods.escapeQuotes(descrizione);
			
			String keyword = a.getParoleChiave1();
			if (!a.getParoleChiave2().isEmpty()) keyword+=", "+a.getParoleChiave2();
			if (!a.getParoleChiave3().isEmpty()) keyword+=", "+a.getParoleChiave3();
			if (!a.getParoleChiave4().isEmpty()) keyword+=", "+a.getParoleChiave4();
			if (!a.getParoleChiave5().isEmpty()) keyword+=", "+a.getParoleChiave5();
			//String tag = a.getParoleChiave1()+" "+a.getParoleChiave2()+" "+a.getParoleChiave3()+" "+a.getParoleChiave4()+" "+a.getParoleChiave5();
			
			keyword = Methods.escapeQuotes(keyword);
			keyword = keyword.toLowerCase();
			String tag = keyword;
			
			String query1 = "INSERT INTO `product_description` (`product_id`, `language_id`, `name`, `description`, `meta_description`, `meta_keyword`, `tag`) " +																	/* 26 */
							"VALUES ("+a.getIdArticolo()+",2,'"+nome+"','"+descrizione+"','','"+keyword+"','"+tag+"');";	/* sono 7 */
			
			String query2 = "INSERT INTO `product_description` (`product_id`, `language_id`, `name`, `description`, `meta_description`, `meta_keyword`, `tag`) " +																	/* 26 */
							"VALUES ("+a.getIdArticolo()+",1,'"+nome+"','"+descrizione+"','','','');";	/* sono 7 */
		
			
			pw.println(query1);
			pw.println(query2);
						
	}
	
	private static void insertIntoProductImage(long idArticolo, String immagine, int ordinamento, PrintWriter pw){
		if (Methods.controlloSintassiImmagine(immagine)){
			String query = "INSERT INTO `product_image` (`product_id`, `image`, `sort_order`) VALUES ("+idArticolo+",'articoli/"+immagine+"',"+ordinamento+");";
			pw.println(query);
		}
	}
	
	
	private static void insertIntoProductImage(Articolo a,PrintWriter pw){
		//la prima immagine è già presente nella descrizione del prodotto
		//insertIntoProductImage(a.getIdArticolo(),a.getImmagine1(),1,con);
		insertIntoProductImage(a.getIdArticolo(),a.getImmagine2(),2,pw);
		insertIntoProductImage(a.getIdArticolo(),a.getImmagine3(),3,pw);
		insertIntoProductImage(a.getIdArticolo(),a.getImmagine4(),4,pw);
		insertIntoProductImage(a.getIdArticolo(),a.getImmagine5(),5,pw);
	}
	
	private static void insertIntoProductToCategory(Articolo a,PrintWriter pw){
			String query = "INSERT INTO `product_to_category` (`product_id`, `category_id`) VALUES ("+a.getIdArticolo()+","+a.getIdCategoria()+");";	
			
			pw.println(query);
		
			if (a.getIdCategoria2()>0){
				query = "INSERT INTO `product_to_category` (`product_id`, `category_id`) VALUES ("+a.getIdArticolo()+","+a.getIdCategoria2()+");";	
				
				pw.println(query);	
			}
	}
	
	private static void insertIntoProductToStore(Articolo a,PrintWriter pw){
			String query = "INSERT INTO `product_to_store` (`product_id`, `store_id`) VALUES ("+a.getIdArticolo()+",0);";	
			pw.println(query);	
	}
	
	private static void insertIntoProductOption(Articolo a,PrintWriter pw){
		
		int option_id=4;
		
		Variante_Articolo v1 = a.getVarianti().get(0);
		if (v1.getTipo()!=null && !v1.getTipo().isEmpty()){
			if (v1.getTipo().equals("Colore")) option_id=1;
			else if (v1.getTipo().equals("Gusto")) option_id=3;
			else if (v1.getTipo().equals("Tema")) option_id=2;
			else if (v1.getTipo().equals("Variante")) option_id=4;
			else if (v1.getTipo().equals("Misura")) option_id=5;
		}

		
		String query = "INSERT INTO `product_option` (`product_id`, `option_id`, `option_value`,`required`) " +
						"VALUES ("+a.getIdArticolo()+","+option_id+",'',1);";	
			
		pw.println(query);	
		
		for (Variante_Articolo v : a.getVarianti()){
		
			query = "INSERT INTO `option_value` (`option_id`, `image`,`sort_order`) VALUES ("+option_id+",'articoli/"+Methods.trimAndToLower(v.getImmagine())+"',0);";
			pw.println(query);	
			
			
			
			query = "INSERT INTO `option_value_description` (`option_value_id`,`language_id` ,`option_id`, `name`) " +
					"VALUES (LAST_INSERT_ID(),2,"+option_id+",'"+Methods.escapeQuotes(v.getValore())+"');";
			pw.println(query);
			
			query = "INSERT INTO `product_option_value` (`product_option_id`,`product_id` ,`option_id`, `option_value_id`," +
														"`quantity`,`subtract`,`price`,`price_prefix`,`points`,`points_prefix`,`weight`,`weight_prefix`)" +
														" VALUES ((select product_option_id from product_option where product_id="+a.getIdArticolo()+"),"+a.getIdArticolo()+
														","+option_id+"," +"LAST_INSERT_ID(),"+v.getQuantita()+",1,0,'+',0,'+',0,'+');";
			pw.println(query);
			
			insertIntoProductImage(a.getIdArticolo(),v.getImmagine(),0,pw);
		}
			
	}
	
	
	private static void deleteProduct(Articolo a, PrintWriter pw){	
			
			String query = "delete from `product` where `product_id` = "+a.getIdArticolo()+";";
			pw.println(query);	
			
			query = "delete from `product_description` where `product_id` = "+a.getIdArticolo()+";";
			pw.println(query);	
			
			query = "delete from `product_image` where `product_id` = "+a.getIdArticolo()+";";
			pw.println(query);	
			
			query = "delete from `product_to_category` where `product_id` = "+a.getIdArticolo()+";";
			pw.println(query);	
			
			query = "delete from `product_to_store` where `product_id` = "+a.getIdArticolo()+";";
			pw.println(query);	
			
			query = "delete from `product_option` where `product_id` = "+a.getIdArticolo()+";";
			pw.println(query);	
			
			query = "delete from `product_option_value` where `product_id` = "+a.getIdArticolo()+";";
			pw.println(query);	

	}
}
