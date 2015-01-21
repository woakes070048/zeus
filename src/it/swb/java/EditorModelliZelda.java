package it.swb.java;

import it.swb.business.CategorieBusiness;
import it.swb.database.Articolo_DAO;
import it.swb.log.Log;
import it.swb.model.Articolo;
import it.swb.model.Categoria;
import it.swb.model.Variante_Articolo;
import it.swb.utility.Methods;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

public class EditorModelliZelda {
	
	public static void main (String[] args){
		
		List<Articolo> l = Articolo_DAO.getArticoli("select * from articoli where presente_su_ebay=1");
		
//		for (Articolo a : l){
//			EditorModelliYatego.aggiungiProdottoAModelloYatego(a);
//		}
		
		aggiungiProdottiAModelloZelda(l);
		
		
	}
	
	public static boolean aggiungiProdottoAModelloZelda(Articolo a){
		Log.info("Inserimento articolo sul modello di caricamento di ZeldaBomboniere...");
		Properties config = new Properties();	   
		boolean ok = false;
		
		try {
		
			config.load(Log.class.getResourceAsStream("/zeus.properties"));
			
			String data = Methods.getMesePerNomeFileTesto();
			
			String base = config.getProperty("percorso_modello_base_zelda");		
			String percorso = config.getProperty("percorso_modello_caricamento_dati_zelda");		
			
			//base = base.replace("DATA", data);
			percorso = percorso.replace("DATA", data);
			
			//File file = new File(base); 
			File file;
			FileInputStream fis;
			
			file = new File(percorso); 
			
			if (!file.exists()) file = new File(base); 
								
			fis = new FileInputStream(file);
	
			HSSFWorkbook wb = new HSSFWorkbook(fis); 
						
			aggiungiProdotto(a, wb);
			aggiungiImmagini(a, wb);
			if (a.getVarianti() != null && !a.getVarianti().isEmpty()) 
				aggiungiVarianti(a, wb);			
			
			FileOutputStream out = new FileOutputStream(percorso);
			wb.write(out);
			
			fis.close(); 
			out.close();
			
			ok = true;
			
		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e);
		} 
		System.out.println("Fine inserimento articolo sul modello di caricamento di ZeldaBomboniere.");
		return ok;
	}
	
	public static void aggiungiProdottiAModelloZelda(List<Articolo> articoli){
		Log.debug("Start aggiungiProdottoAModelloZelda");
		Properties config = new Properties();	   
		
		try {
		
			config.load(Log.class.getResourceAsStream("/zeus.properties"));
			
			String data = Methods.getMesePerNomeFileTesto();
			
			String base = config.getProperty("percorso_modello_base_zelda");		
			String percorso = config.getProperty("percorso_modello_caricamento_dati_zelda");		
			
			//base = base.replace("DATA", data);
			percorso = percorso.replace("DATA", data);
			
			//File file = new File(base); 
			File file;
			FileInputStream fis;
			
			file = new File(percorso); 
			
			if (!file.exists()) file = new File(base); 
								
			fis = new FileInputStream(file);
	
			HSSFWorkbook wb = new HSSFWorkbook(fis); 
			
			for (Articolo a : articoli){
				aggiungiProdotto(a, wb);
				aggiungiImmagini(a, wb);
				if (a.getVarianti() != null && !a.getVarianti().isEmpty()) 
					aggiungiVarianti(a, wb);
			}
			
			FileOutputStream out = new FileOutputStream(percorso);
			wb.write(out);
			
			fis.close(); 
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e);
		} 
		System.out.println("Fine creazione foglio xls");
	}
		
	
	
	private static void aggiungiProdotto(Articolo a, HSSFWorkbook wb){
		try {
			HSSFSheet st = wb.getSheet("Products"); 
			
			int i = st.getPhysicalNumberOfRows();	//riga
			
			Map<Long, Categoria> catMap = CategorieBusiness.getInstance().getMappaCategorie();	
				
			Categoria c = (Categoria) catMap.get(a.getIdCategoria());
			
			HSSFRow row = st.getRow(i); 
			if (row == null) row = st.createRow(i);
			
			//(A) PRODUCT_ID 
			HSSFCell cell = row.getCell(0); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(0);    
		    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
		    cell.setCellValue(a.getIdArticolo()); 
		    
		    //(B) NAME 
		    cell = row.getCell(1); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(1);    
		    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
		    cell.setCellValue(a.getNome()); 
		    
		    //(C) CATEGORIES
		    cell = row.getCell(2); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(2);    
			cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
		    cell.setCellValue(c.getIdCategoria()); 
		    
		    //(D) SKU
		    cell = row.getCell(3); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(3);    
		    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
		    cell.setCellValue(a.getCodice()); 

		    //(E) UPC 
		    cell = row.getCell(4); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(4);    
		    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
		    cell.setCellValue(""); 

		    //(F) EAN
		    cell = row.getCell(5); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(5);    
		    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
		    cell.setCellValue(a.getCodiceBarre()); 

		    //(G) JAN 
		    cell = row.getCell(6); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(6);    
		    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
		    cell.setCellValue("");  

		    //(H) ISBN
		    cell = row.getCell(7); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(7);    
		    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
		    cell.setCellValue("");  

		    //(I) MPN
		    cell = row.getCell(8); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(8);    
		    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
		    cell.setCellValue(""); 

		    //(J) LOCATION
		    cell = row.getCell(9); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(9);    
		    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
		    cell.setCellValue("");  

		    //(K) QUANTITY
		    cell = row.getCell(10); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(10);    
		    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
		    cell.setCellValue(a.getQuantitaEffettiva());  

		    //(L) MODEL
		    cell = row.getCell(11); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(11);    
		    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
		    cell.setCellValue(a.getCodice());  

		    //(M) MANUFACTURER
		    cell = row.getCell(12); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(12);    
		    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
		    cell.setCellValue("");  

		    //(N) IMAGE_NAME
		    cell = row.getCell(13); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(13);    
		    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
		    cell.setCellValue("articoli/"+a.getImmagine1());   

		    //(O) REQUIRES SHIPPING
		    cell = row.getCell(14); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(14);    
		    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
		    cell.setCellValue("yes"); 
		    
		    //(P) PRICE
		    cell = row.getCell(15); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(15);    
			double prezzo = a.getPrezzoDettaglio();
		  	if (a.getPrezzoPiattaforme()>0) prezzo = a.getPrezzoPiattaforme();
		    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
		    cell.setCellValue(prezzo); 
		    
		    //(Q) POINTS
		    cell = row.getCell(16); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(16);    
		    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
		    cell.setCellValue(0);  
		    
		    //(R) DATE ADDED
		    cell = row.getCell(17); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(17);    
		    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
		    cell.setCellValue(new Date().toString());  
		    
		    //(S) DATE MODIFIED
		    cell = row.getCell(18); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(18);    
		    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
		    cell.setCellValue(new Date().toString());  
		    
		    //(T) DATE AVAILABLE
		    cell = row.getCell(19); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(19);    
		    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
		    cell.setCellValue(new Date().toString());  
		   				    
		    //(U) WEIGHT
		    cell = row.getCell(20); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(20);    
		    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
		    cell.setCellValue(0); 
		    
		    //(V) UNIT (WEIGHT CLASS ID)
		    cell = row.getCell(21); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(21);    
		    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
		    cell.setCellValue("kg");  //1 = KG
		    
		    //(W) LENGTH
		    cell = row.getCell(22); 
		    if (cell != null) row.removeCell(cell);
			cell = row.createCell(22);    
		    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 
		    cell.setCellValue(0);  
		    
		    //(X) WIDTH
		    cell = row.getCell(23); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(23);    
		    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
		    cell.setCellValue(0); 
		    
		    //(Y) HEIGTH
		    cell = row.getCell(24); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(24);    
		    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
		    cell.setCellValue(0); 
		    
		    //(Z) LENGTH UNIT
		    cell = row.getCell(25); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(25);    
		    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
		    cell.setCellValue("cm"); //1 = CM
		    
		    //(AA) STATUS ENABLED
		    cell = row.getCell(26); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(26);    
		    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
		    cell.setCellValue("true");  //1 = TRUE
		    
		    //(AB) TAX CLASS ID //0 = IVA GIà INCLUSA; 11 = IVA 22%
		    cell = row.getCell(27); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(27);    
		    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
		    cell.setCellValue(0); 
		    
		    //(AC) VIEWED
		    cell = row.getCell(28); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(28);    
		    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
		    cell.setCellValue(5); 
		    
		    //(AD) LANGUAGE ID
		    cell = row.getCell(29); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(29);    
		    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
		    cell.setCellValue(2); 
		    
		    //(AE) SEO KEYWORD
		    cell = row.getCell(30); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(30);    
		    cell.setCellType(Cell.CELL_TYPE_STRING); 			   
		    if (a.getTitoloInserzione()!=null)
		    	cell.setCellValue(a.getTitoloInserzione().toLowerCase()); 
		    else cell.setCellValue("");
		    
		    //(AF) DESCRIPTION
		    cell = row.getCell(31); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(31);    
		    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
		    cell.setCellValue("<p>"+Methods.MaiuscolaDopoPunto(a.getDescrizione())+"<br/><br/><b>Quantit&agrave; inserzione:</b> "
					+Methods.MaiuscolaDopoPunto(a.getQuantitaInserzione())+"<br/><br/><b>Dimensioni:</b> "
					+Methods.MaiuscolaDopoPunto(a.getDimensioni())+"</p>"); 
		    
		    
		    //(AG) META_DESCRIPTION
		    cell = row.getCell(32); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(32);    
		    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
		    cell.setCellValue(""); 
		    
		    //(AH) META_KEYWORDS
		    cell = row.getCell(33); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(33);    
		    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
		    if (a.getTitoloInserzione()!=null)
		    	cell.setCellValue(a.getTitoloInserzione().toLowerCase()); 
		    else cell.setCellValue("");
		    
		    //(AI) STOCK STATUS ID
		    cell = row.getCell(34); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(34);    
		    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
		    cell.setCellValue(6); 
		    
		    //(AJ) STORE IDS
		    cell = row.getCell(35); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(35);    
		    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
		    cell.setCellValue(0); 
		    
		    //(AK) LAYOUT
		    cell = row.getCell(36); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(36);    
		    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
		    cell.setCellValue(""); 
		    
		    //(AL) RELATED IDS
		    cell = row.getCell(37); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(37);    
		    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
		    cell.setCellValue(""); 
		    
		    //(AM) TAGS
		    cell = row.getCell(38); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(38);    
		    cell.setCellType(Cell.CELL_TYPE_STRING); 	
		    cell.setCellValue(""); 
		    
		  	//(AN) SORT ORDER (0)
		    cell = row.getCell(39); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(39);    
		    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
		    cell.setCellValue(0); 
		    
		  	//(AO) SUBTRACT
		    cell = row.getCell(40); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(40);    
		    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
		    cell.setCellValue("true"); 
		    
		  	//(AP) MINIMUM
		    cell = row.getCell(41); 					
			if (cell != null) row.removeCell(cell);
			cell = row.createCell(41);    
		    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
		    cell.setCellValue(1); 
			
		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e);
		} 
	}
	
	private static void aggiungiImmagini(Articolo a, HSSFWorkbook wb){
		try {
			HSSFSheet st = wb.getSheet("AdditionalImages"); 
			
			int i = st.getPhysicalNumberOfRows();	//riga
			
			/* la prima immagine è ridondante */
//			if (a.getImmagine1()!=null && !a.getImmagine1().isEmpty()){	
//				HSSFRow row = st.getRow(i); 
//				if (row == null) row = st.createRow(i);
//				
//				//(A) PRODUCT_ID 
//				HSSFCell cell = row.getCell(0); 					
//				cell = row.createCell(0);    
//			    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
//			    cell.setCellValue(a.getIdArticolo()); 
//			    
//			    //(B) IMAGE 
//			    cell = row.getCell(1); 					
//				cell = row.createCell(1);    
//			    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
//			    cell.setCellValue("articoli/"+a.getImmagine1()); 
//			    
//			    //(C) SORT_ORDER
//			    cell = row.getCell(2); 					
//				cell = row.createCell(2);    
//				cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
//			    cell.setCellValue(0);
//			    
//			    i++;
//			}
			
			if (a.getImmagine2()!=null && !a.getImmagine2().isEmpty()){	
				HSSFRow row = st.getRow(i); 
				if (row == null) row = st.createRow(i);
				
				//(A) PRODUCT_ID 
				HSSFCell cell = row.getCell(0); 					
				cell = row.createCell(0);    
			    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
			    cell.setCellValue(a.getIdArticolo()); 
			    
			    //(B) IMAGE 
			    cell = row.getCell(1); 					
				cell = row.createCell(1);    
			    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
			    cell.setCellValue("articoli/"+a.getImmagine2()); 
			    
			    //(C) SORT_ORDER
			    cell = row.getCell(2); 					
				cell = row.createCell(2);    
				cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
			    cell.setCellValue(0);
			    
			    i++;
			}
			
			if (a.getImmagine3()!=null && !a.getImmagine3().isEmpty()){	
				HSSFRow row = st.getRow(i); 
				if (row == null) row = st.createRow(i);
				
				//(A) PRODUCT_ID 
				HSSFCell cell = row.getCell(0); 					
				cell = row.createCell(0);    
			    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
			    cell.setCellValue(a.getIdArticolo()); 
			    
			    //(B) IMAGE 
			    cell = row.getCell(1); 					
				cell = row.createCell(1);    
			    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
			    cell.setCellValue("articoli/"+a.getImmagine3()); 
			    
			    //(C) SORT_ORDER
			    cell = row.getCell(2); 					
				cell = row.createCell(2);    
				cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
			    cell.setCellValue(0);
			    
			    i++;
			}
			
			if (a.getImmagine4()!=null && !a.getImmagine4().isEmpty()){	
				HSSFRow row = st.getRow(i); 
				if (row == null) row = st.createRow(i);
				
				//(A) PRODUCT_ID 
				HSSFCell cell = row.getCell(0); 					
				cell = row.createCell(0);    
			    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
			    cell.setCellValue(a.getIdArticolo()); 
			    
			    //(B) IMAGE 
			    cell = row.getCell(1); 					
				cell = row.createCell(1);    
			    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
			    cell.setCellValue("articoli/"+a.getImmagine4()); 
			    
			    //(C) SORT_ORDER
			    cell = row.getCell(2); 					
				cell = row.createCell(2);    
				cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
			    cell.setCellValue(0);
			    
			    i++;
			}
			
			if (a.getImmagine5()!=null && !a.getImmagine5().isEmpty()){	
				HSSFRow row = st.getRow(i); 
				if (row == null) row = st.createRow(i);
				
				//(A) PRODUCT_ID 
				HSSFCell cell = row.getCell(0); 					
				cell = row.createCell(0);    
			    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
			    cell.setCellValue(a.getIdArticolo()); 
			    
			    //(B) IMAGE 
			    cell = row.getCell(1); 					
				cell = row.createCell(1);    
			    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
			    cell.setCellValue("articoli/"+a.getImmagine5()); 
			    
			    //(C) SORT_ORDER
			    cell = row.getCell(2); 					
				cell = row.createCell(2);    
				cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
			    cell.setCellValue(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e);
		} 
	}
	
	private static void aggiungiVarianti(Articolo a, HSSFWorkbook wb){
		try{
			
			HSSFSheet st = wb.getSheet("Options"); 
			
			int i = st.getPhysicalNumberOfRows();	//riga
			
			for (Variante_Articolo v : a.getVarianti()){
				HSSFRow row = st.getRow(i); 
				
				if (row == null) row = st.createRow(i);
				
				//(A) PRODUCT_ID 
				HSSFCell cell = row.getCell(0); 					
				cell = row.createCell(0);    
			    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
			    cell.setCellValue(a.getIdArticolo()); 
			    
			    //(B) LANGUAGE ID 
			    cell = row.getCell(1); 					
				cell = row.createCell(1);    
			    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
			    cell.setCellValue(2); 
			    
			    //(C) OPTION
			    cell = row.getCell(2); 					
				cell = row.createCell(2);    
				cell.setCellType(Cell.CELL_TYPE_STRING); 
			    cell.setCellValue(v.getTipo()); 
			    
			    //(D) TYPE
			    cell = row.getCell(3); 					
				if (cell != null) row.removeCell(cell);
				cell = row.createCell(3);    
			    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
			    cell.setCellValue("image"); 

			    //(E) VALUE
			    cell = row.getCell(4); 					
				cell = row.createCell(4);    
			    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
			    cell.setCellValue(v.getValore()); 

			    //(F) IMAGE
			    cell = row.getCell(5); 					
				cell = row.createCell(5);    
			    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
			    cell.setCellValue("articoli/"+v.getImmagine()); 

			    //(G) REQUIRED 
			    cell = row.getCell(6); 					
				cell = row.createCell(6);    
			    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
			    cell.setCellValue("true");  

			    //(H) QUANTITY
			    cell = row.getCell(7); 					
				cell = row.createCell(7);    
			    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
			    cell.setCellValue(v.getQuantita());  

			    //(I) SUBTRACT
			    cell = row.getCell(8); 					
				cell = row.createCell(8);    
			    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
			    cell.setCellValue("true"); 

			    //(J) PRICE
			    cell = row.getCell(9); 					
				cell = row.createCell(9);    
			    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
			    cell.setCellValue(0);  

			    //(K) PRICE PREFIX
			    cell = row.getCell(10); 					
				cell = row.createCell(10);    
			    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
			    cell.setCellValue("+");  

			    //(L) POINTS
			    cell = row.getCell(11); 					
				cell = row.createCell(11);    
			    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
			    cell.setCellValue(0);  

			    //(M) POINTS PREFIX
			    cell = row.getCell(12); 					
				cell = row.createCell(12);    
			    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
			    cell.setCellValue("+");  

			    //(N) WEIGHT
			    cell = row.getCell(13); 					
				cell = row.createCell(13);    
			    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
			    cell.setCellValue(0);   

			    //(O) WEIGHT PREFIX
			    cell = row.getCell(14); 					
				cell = row.createCell(14);    
			    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
			    cell.setCellValue("+"); 
			    
			    //(P) SORT_ORDER
			    cell = row.getCell(15); 					
				cell = row.createCell(15);    
			    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
			    cell.setCellValue(0); 
			    
			    i++;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e);
		} 
	}

}
