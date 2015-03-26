package it.swb.csv;

import it.swb.business.CategorieBusiness;
import it.swb.model.Articolo;
import it.swb.model.Categoria;
import it.swb.utility.Costanti;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

public class AmazonXLS {
	
	public static void creaXlsAmazon(List<Articolo> articoli){
		System.out.println("Inizio creazione foglio xls Amazon");
		String filePath = "D:\\Piattaforme\\Amazon\\modello_caricamento_amazon.xls";
		
		try {
			File file = new File(filePath); 
			FileInputStream fis;
			
			fis = new FileInputStream(file);

			HSSFWorkbook wb = new HSSFWorkbook(fis); 
			HSSFSheet st = wb.getSheet("Modello Caricamento"); 
			
			int i = 2;	//riga
			
			Map<Long, Categoria> catMap = CategorieBusiness.getInstance().getMappaCategorie();	
			
			//int j = 0;
			
			for (Articolo a : articoli){
				
				Categoria c = (Categoria) catMap.get(a.getIdCategoria());
				
				if (a.getCodiceBarre()!=null && !a.getCodiceBarre().trim().isEmpty() && c.getIdCategoriaAmazon()!=null && !c.getIdCategoriaAmazon().trim().isEmpty() ){
					
					HSSFRow row = st.getRow(i); 
					if (row == null) row = st.createRow(i);
					
					/* inizio product basic information */
					
					//(A) SKU 
					HSSFCell cell = row.getCell(0); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(0);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue(a.getCodice()); 
				    
				    //(B) CODICE BARRE 
				    cell = row.getCell(1); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(1);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue(a.getCodiceBarre()); 
				    
				    //(C) TIPO CODICE (EAN) 
				    cell = row.getCell(2); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(2);    
					cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue("EAN"); 
				    
				    //(D) NOME ARTICOLO 
				    cell = row.getCell(3); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(3);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue(a.getNome()); 

				    //(E) MARCA 
				    cell = row.getCell(4); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(4);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue(""); 

				    //(F) PRODUTTORE 
				    cell = row.getCell(5); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(5);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue("Zelda Bomboniere"); 

				    //(G) DESCRIZIONE 
				    cell = row.getCell(6); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(6);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue(a.getDescrizione());  

				    //(H) PUNTO ELENCO 1 
				    if (a.getQuantitaInserzione()!=null && !a.getQuantitaInserzione().trim().isEmpty()){
					    cell = row.getCell(7); 					
						if (cell != null) row.removeCell(cell);
						cell = row.createCell(7);    
					    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
					    cell.setCellValue("Quantità inserzione: "+a.getQuantitaInserzione());  
				    }

				    //(I) PUNTO ELENCO 2 
				    if (a.getDimensioni()!=null && !a.getDimensioni().trim().isEmpty()){
					    cell = row.getCell(8); 					
						if (cell != null) row.removeCell(cell);
						cell = row.createCell(8);    
					    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
					    cell.setCellValue("Dimensioni: "+a.getDimensioni()); 
					}

				    //(J) PUNTO ELENCO 3 
//				    cell = row.getCell(9); 					
//					if (cell != null) row.removeCell(cell);
//					cell = row.createCell(9);    
//				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
//				    cell.setCellValue("");  

				    //(K) PUNTO ELENCO 4 
//				    cell = row.getCell(10); 					
//					if (cell != null) row.removeCell(cell);
//					cell = row.createCell(10);    
//				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
//				    cell.setCellValue("");  

				    //(L) PUNTO ELENCO 5 
//				    cell = row.getCell(11); 					
//					if (cell != null) row.removeCell(cell);
//					cell = row.createCell(11);    
//				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
//				    cell.setCellValue("");  

				    //(M) NODO NAVIGAZIONE 1 
				    cell = row.getCell(12); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(12);    
				    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
				    cell.setCellValue(c.getIdCategoriaAmazon());  

				    //(N) NODO NAVIGAZIONE 2 
//				    cell = row.getCell(13); 					
//					if (cell != null) row.removeCell(cell);
//					cell = row.createCell(13);    
//				    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
//				    cell.setCellValue();   

				    //(O) TIPO PRODOTTO 
				    cell = row.getCell(14); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(14);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue("FurnitureAndDecor"); 
				    
				    /* fine product basic information */
				    
				    /* inizio informazioni sull'immagine */
				    
				    //(BB) URL IMMAGINE PRINCIPALE
				    cell = row.getCell(53); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(53);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue(Costanti.percorsoImmaginiRemoto+a.getImmagine1()); 
				    
				    //(BC) URL ALTRA IMMAGINE 1
				    if (a.getImmagine2()!=null && !a.getImmagine2().trim().isEmpty()){
					    cell = row.getCell(54); 					
						if (cell != null) row.removeCell(cell);
						cell = row.createCell(54);    
					    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
					    cell.setCellValue(Costanti.percorsoImmaginiRemoto+a.getImmagine2());  
				    }
				    
				    //(BD) URL ALTRA IMMAGINE 2
				    if (a.getImmagine3()!=null && !a.getImmagine3().trim().isEmpty()){
					    cell = row.getCell(55); 					
						if (cell != null) row.removeCell(cell);
						cell = row.createCell(55);    
					    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
					    cell.setCellValue(Costanti.percorsoImmaginiRemoto+a.getImmagine3());  
				    }
				    
				    //(BE) URL ALTRA IMMAGINE 3
				    if (a.getImmagine4()!=null && !a.getImmagine4().trim().isEmpty()){
					    cell = row.getCell(56); 					
						if (cell != null) row.removeCell(cell);
						cell = row.createCell(56);    
					    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
					    cell.setCellValue(Costanti.percorsoImmaginiRemoto+a.getImmagine4());  
				    }
				    
				    //(BF) URL ALTRA IMMAGINE 4
				    if (a.getImmagine5()!=null && !a.getImmagine5().trim().isEmpty()){
					    cell = row.getCell(57); 					
						if (cell != null) row.removeCell(cell);
						cell = row.createCell(57);    
					    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
					    cell.setCellValue(Costanti.percorsoImmaginiRemoto+a.getImmagine5());  
				    }
				    
				    //(BG) URL ALTRA IMMAGINE 5				    
					//(BH) URL ALTRA IMMAGINE 6
					//(BI) URL ALTRA IMMAGINE 7
					//(BJ) URL ALTRA IMMAGINE 8
				    
				    //(BK) URL IMMAGINE SWITCH
//				    cell = row.getCell(25); 					
//					if (cell != null) row.removeCell(cell);
//					cell = row.createCell(25);    
//				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
//				    cell.setCellValue(""); 
				    
				    /* fine informazioni sull'immagine */
				    
				    /* inizio informazioni legali */
				    
				    //(BL) ESCLUSIONE RESPOSABILITA'			    
				    //(BM) DESCRIZIONE GARANZIA VENDITORE			    
				    //(BN) AVVERTENZE SICUREZZA
				    
				    /* fine informazioni legali */
				    
				    //(BO) AGGIORNA O RIMUOVI
				    
				    /* inizio informazioni sull'offerta */
				    
				    //(BP) PREZZO
				    cell = row.getCell(67); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(67);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue(String.valueOf(a.getPrezzoDettaglio()).replace(",", ".")); 
				    
				    //(BQ) VALUTA
				    cell = row.getCell(68); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(68);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue("EUR"); 
				    
				    //(BR) QUANTITA'
				    cell = row.getCell(69); 
				    if (cell != null) row.removeCell(cell);
					cell = row.createCell(69);    
				    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 
				    if (a.getQuantitaMagazzino()>0)
				    	cell.setCellValue(a.getQuantitaMagazzino());  
				    else cell.setCellValue(20);  
				    
				    //(BS) CONDIZIONI
				    cell = row.getCell(70); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(70);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue("New"); 
				    
				    //(BT) NOTA CONDIZIONI
				    //(BU) VOCE PACCHETTO QUANTITA'
				    //(BV) NUMERO PEZZI
				    //(BW) DATA LANCIO
				    //(BX) DATA RILASCIO
				    //(BY) TEMPI ESECUZIONE SPEDIZIONE
				    //(BZ) DATA RIFORNIMENTO
				    //(CA) QUANTITA' MASSIMA SPEDIZIONE CUMULATIVA
				    //(CB) PAESE DI ORIGINE
				    cell = row.getCell(79); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(79);    
				    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
				    cell.setCellValue(""); 
				    
				    /* fine informazioni sull'offerta */
				    
				    /* inizio dimensioni prodotto */
				    
				    //(GG) PESO SPEDIZIONE
//				    cell = row.getCell(80); 					
//					if (cell != null) row.removeCell(cell);
//					cell = row.createCell(80);    
//				    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
//				    cell.setCellValue(""); 
				    
				    //(GH) UNITA' MISURA PESO SPEDIZIONE
//				    cell = row.getCell(81); 					
//					if (cell != null) row.removeCell(cell);
//					cell = row.createCell(81);    
//				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
//				    cell.setCellValue(""); 
				    
				    //(GI) LUNGHEZZA ARTICOLO
				    cell = row.getCell(82); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(82);    
				    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
				    cell.setCellValue(""); 
				    
				    //(GJ) ALTEZZA ARTICOLO
				    cell = row.getCell(83); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(83);    
				    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
				    cell.setCellValue(""); 
				    
				    //(GK) PESO ARTICOLO
				    cell = row.getCell(84); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(84);    
				    cell.setCellType(Cell.CELL_TYPE_NUMERIC); 			    
				    cell.setCellValue(""); 
				    
				    //(GL) UNITA' MISURA PESO ARTICOLO
				    cell = row.getCell(85); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(85);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue(""); 
				    
				    /* fine dimensioni prodotto */
				    
				    /* inizio informazioni sulla scoperta del prodotto */
				    
				    //(GM) CODICE ARTICOLO DEL PRODUTTORE
				    cell = row.getCell(86); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(86);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue(""); 
				    
				    //(GN) CRITERI DI RICERCA 1
				    cell = row.getCell(87); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(87);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue(""); 
				    
				    //(GO) CRITERI DI RICERCA 2
				    cell = row.getCell(88); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(88);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue(""); 
				    
				    //(GP) CRITERI DI RICERCA 3
				    cell = row.getCell(89); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(89);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue(""); 
				    
				    //(GQ) CRITERI DI RICERCA 4
				    cell = row.getCell(90); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(90);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue(""); 
				    
				    //(GR) CRITERI DI RICERCA 5
				    cell = row.getCell(91); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(91);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue(""); 
				    
				    /* fine informazioni sulla scoperta del prodotto */
				    
				    /* inizio informazioni prezzo saldo e ribasso */
				    
				    //(GS) PREZZO DI VENDITA
				    cell = row.getCell(92); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(92);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue(""); 
				    
				    //(GT) DATA INIZIO SALDO
				    cell = row.getCell(93); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(93);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue(""); 
				    
				    //(GU) DATA FINE SALDO
				    cell = row.getCell(94); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(94);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue(""); 
				    
				    /* fine informazioni prezzo saldo e ribasso */
				    
				    /* inizio informazioni varianti */
				    
				  	//(GV) FILIAZIONE (parent, child)
				    cell = row.getCell(95); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(95);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 	
				    cell.setCellValue(""); 
				    
				  	//(GW) PARENT SKU
				    cell = row.getCell(96); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(96);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue(""); 
				    
				  	//(GX) TIPO RELAZIONE (variante, accessorio)
				    cell = row.getCell(97); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(97);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue(""); 
				    
				  	//(GY) TEMA VARIANTE (colore, taglia, taglia-colore,fragranza, taglia-fragranza)
				    cell = row.getCell(98); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(98);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue(""); 
				    
				  	//(GZ) DIMENSIONI (SE IL TEMA è TAGLIA, ES. VALORE VALIDO: S)
				    cell = row.getCell(99); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(99);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue(""); 
				    
				  	//(HA) MAPPA DIMENSIONI
				    cell = row.getCell(100); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(100);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue(""); 
				    
				  	//(HB) COLORE
				    cell = row.getCell(101); 					
					if (cell != null) row.removeCell(cell);
					cell = row.createCell(101);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue(""); 
				    
				  	//(HC) MAPPA COLORE
				    cell = row.getCell(102); 					
					if (cell != null) row.removeCell(cell);		
					cell = row.createCell(102);    
				    cell.setCellType(Cell.CELL_TYPE_STRING); 			    
				    cell.setCellValue(""); 
				    
				    /* fine informazioni varianti */
				    
				    /* fine !!! */
				    
					
					i++;
					//j++;
				}
			}		
			
			FileOutputStream out = new FileOutputStream("D:\\Piattaforme\\Amazon\\zeldabomboniere_amazon.xls");
			wb.write(out);
			
			//cell.setCellValue(val); 
			fis.close(); 
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		System.out.println("Fine creazione foglio xls Amazon");
	}

}
