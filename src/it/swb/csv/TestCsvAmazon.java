package it.swb.csv;



import it.swb.database.Categoria_DAO;
import it.swb.database.Variante_Articolo_DAO;
import it.swb.model.Articolo;
import it.swb.model.Variante_Articolo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class TestCsvAmazon {
	
	public static void main(String[] args){
		
		//List<Articolo> articoli= Articolo_DAO.getArticoli("SELECT * FROM articoli where descrizione is not null;");
		
		//AmazonXLS.creaXlsAmazon(articoli);
		
		//leggiCsvYatego();
		
		nodiAmazon();
		
	}
	
	public static void test1(List<Articolo> articoli){
		System.out.println("start");
		String filePath = "D:\\csv\\AmazonFlatFileListingLoader.xls";
		
		try {
			File file = new File(filePath); 
			FileInputStream fis;
			
			fis = new FileInputStream(file);

			HSSFWorkbook wb = new HSSFWorkbook(fis); 
			HSSFSheet st = wb.getSheet("Modello offerte"); 
			
			int i = 2;	//riga
			
			for (Articolo a : articoli){
				
				List<Variante_Articolo> v = Variante_Articolo_DAO.getVarianti(a.getCodice());
				
				if (a.getCodiceBarre()!=null && !a.getCodiceBarre().trim().isEmpty() && v.isEmpty()){
					
					HSSFRow row = st.getRow(i); 
					if (row == null) row = st.createRow(i);
					
					HSSFCell cell = row.getCell(0); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(0);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue(a.getCodice()); 
				    
				    cell = row.getCell(1); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(1);    
				    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
				    cell.setCellValue(String.valueOf(a.getPrezzoDettaglio()).replace(",", ".")); 
				    
				    cell = row.getCell(2); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(2);    
				    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
				    cell.setCellValue(a.getQuantitaMagazzino()); 
				    
				    cell = row.getCell(3); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(3);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue(a.getCodiceBarre()); 
				    
				    cell = row.getCell(4); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(4);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue("EAN"); 
				    
				    cell = row.getCell(5); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(5);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue("NEW"); 
				    
				    cell = row.getCell(8); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(8);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue(a.getNome()); 
				    
	//				System.out.println(cell.getStringCellValue());
					
					i++;
				}
			}		
			
			FileOutputStream out = new FileOutputStream("D:\\csv\\Amazon_FlatFileListingLoader.xls");
			wb.write(out);
			
			//cell.setCellValue(val); 
			fis.close(); 
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		System.out.println("end");
	}
	
	public static void leggiEScriviXls(){
		System.out.println("start");
		String filePath = "D:\\csv\\AmazonFlatFileListingLoader.xls";
		
		try {
			File file = new File(filePath); 
			FileInputStream fis;
			
			fis = new FileInputStream(file);

			HSSFWorkbook wb = new HSSFWorkbook(fis); 
			HSSFSheet st = wb.getSheet("Modello offerte"); 
			HSSFRow row=st.getRow(2); 
			HSSFCell cell= row.getCell(0); 
			
			if (cell == null)  {  
			    cell = row.createCell(0);    
			    cell.setCellType(Cell.CELL_TYPE_STRING);    
			}
			else {
				row.removeCell(cell);
				cell = row.createCell(0);    
			    cell.setCellType(Cell.CELL_TYPE_STRING);    
			}
			cell.setCellValue("test");    
			
			System.out.println(cell.getStringCellValue());
			
			FileOutputStream out = new FileOutputStream("D:\\csv\\Amazon1.xls");
			wb.write(out);
			
			//cell.setCellValue(val); 
			fis.close(); 
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		System.out.println("end");
	}
	
	
	public static void nodiAmazon(){
		System.out.println("start");
//		String filePath = "D:\\Piattaforme\\Amazon\\it_garden_browse_tree_guide.xls";
		
		try {
//			File file = new File(filePath); 
//			FileInputStream fis;
//			
//			fis = new FileInputStream(file);
			InputStream is1 = new FileInputStream("D:\\zeus\\it_kitchen_browse_tree_guide.xls"); 
			HSSFWorkbook wb = new HSSFWorkbook(is1); 
			HSSFSheet st = wb.getSheetAt(1); 
			
			String[][] nodi = new String[2054][2];
			
			for(int i=1;i<2054;i++){
				HSSFRow row=st.getRow(i); 
				
				HSSFCell cell1= row.getCell(0); 
				
				nodi[i][0] = String.valueOf(cell1.getNumericCellValue()).replace(".", "").replace("E8", "").replace("E9", "");
				
				HSSFCell cell2= row.getCell(1); 
				
				nodi[i][1] = cell2.getStringCellValue();
				
				System.out.println(i+" --> "+nodi[i][0]+" --> "+nodi[i][1]);
			}
			
			is1.close(); 
			
			Categoria_DAO.salvaCategorieAmazon(nodi);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		System.out.println("end");
	}
	
	
	@SuppressWarnings("unused")
	public static void leggiCsvAmazon(){
		System.out.println("start");
		CSVReader reader;
		CSVWriter writer;
		
		try{
			
			reader = new CSVReader(new FileReader("D:\\csv\\AmazonFlatFileListingLoader.xls"));
			writer = new CSVWriter(new FileWriter("D:\\csv\\amazon.csv"), ';');
			
			String[] header = "TemplateType=Offer#Version=1.4".split("#");
			String[] title = "sku#price#quantity#product-id#product-id-type#condition-type#condition-note#ASIN-hint#title#product-tax-code#operation-type#sale-price#sale-start-date#sale-end-date#leadtime-to-ship#launch-date#is-giftwrap-available#is-gift-message-available#fulfillment-center-id".split("#");
			String[] esempio = "OB221#3.4#100#8022934102348#EAN#NEW".split("#");
			
			writer.writeNext(header);
			writer.writeNext(title);
			writer.writeNext(esempio);
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("end");
	}
	
	
	public static void leggiCsvYatego(){
		System.out.println("start");
		CSVReader reader;
		
		try{
			
			reader = new CSVReader(new FileReader("D:\\zeus\\mcd\\mcd_yatego_2013-07.csv"), ';');
			
			String [] nextLine;
		    while ((nextLine = reader.readNext()) != null) {
		        // nextLine[] is an array of values from the line
		    	System.out.println(nextLine[0]);
//		    	System.out.println(nextLine.length);
//		        System.out.println(nextLine[0] + nextLine[1] + "etc...");
//		    	for (int i=0; i<nextLine.length;i++)
//		    		System.out.println(nextLine[i]);
		    }
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("end");
	}

}
