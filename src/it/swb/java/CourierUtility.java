package it.swb.java;

import it.swb.database.Ordine_DAO;
import it.swb.log.Log;
import it.swb.model.Indirizzo;
import it.swb.model.Ordine;
import it.swb.piattaforme.amazon.EditorModelliAmazon;
import it.swb.utility.DateMethods;
import it.swb.utility.Methods;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import au.com.bytecode.opencsv.CSVReader;

public class CourierUtility {
	
	public static void main(String[] args){
		salvaNumeriTracciamentoGLS("D:\\zeus\\export_spedizioni\\", "C__inetpub_wwwroot_Secure_Page_ExportFile_2015-06-04-1722_R1_141314_Bolle.xls");
	}
	
	
	
	public static String generaModelloConfermaSpedizioniAmazon(Date d){
		
		List<Map<String,String>> numeriTracciamento = Ordine_DAO.getNumeriTracciamento(d,1);
		
		return EditorModelliAmazon.generaModelloConfermaSpedizioni(numeriTracciamento);
		
	}
	
	@SuppressWarnings("resource")
	public static int salvaNumeriTracciamentoSDA(String cartella, String file){
		
		int salvati = 0;
		
		Log.info("Inizio elaborazione file contenente numeri di tracciamento SDA "+file);
		CSVReader reader;
		
		try{
			if (cartella==null || cartella.isEmpty() || file==null || file.isEmpty()){
				Properties config = new Properties();	   
				config.load(Log.class.getResourceAsStream("/zeus.properties"));
				
				cartella = config.getProperty("percorso_file_import_spedizioni");	
				file = config.getProperty("nome_file_import_spedizioni_sda");
				
				file = file.replace("data", DateMethods.getDataPerNomeFileTesto().replace("-", ""));
			}
			
			String percorso = cartella+file;
			
			reader = new CSVReader(new FileReader(percorso), ';');
			
			String [] nextLine;
			int i = 1;
			
			List<Map<String,String>> numeri = new ArrayList<Map<String,String>>();
			
		    while ((nextLine = reader.readNext()) != null) {
		    	if (i>=4){
		    		
		    		Map<String,String> num = new HashMap<String,String>();
		    		
		    		num.put("numero_tracciamento",nextLine[0]);
		    		
		    		String idOrdine = nextLine[1];
		    		idOrdine = idOrdine.replace("ORDINE", "");
		    		idOrdine = idOrdine.replace("NR", "");
		    		idOrdine = idOrdine.replace(".", "");
		    		idOrdine = idOrdine.replace("#", "");
		    		idOrdine = idOrdine.trim();
		    		
		    		if (idOrdine.contains("EBAY")){
		    			idOrdine = idOrdine.replace("EBAY", "");
		    			idOrdine = idOrdine.trim();
		    			num.put("piattaforma", "eBay");
		    			num.put("id_ordine_piattaforma", idOrdine);
		    		}
		    		else if (idOrdine.contains("ZELDA")){
		    			idOrdine = idOrdine.replace("ZELDA", "");
		    			idOrdine = idOrdine.trim();
		    			num.put("piattaforma", "ZeldaBomboniere.it");
		    			num.put("id_ordine_piattaforma", idOrdine);
		    		}
		    		else if (idOrdine.contains("-")){
		    			num.put("piattaforma", "Amazon");
		    			num.put("id_ordine_piattaforma", idOrdine);
		    		}
		    		else num.put("id_ordine", idOrdine);
		    		
			        num.put("data", nextLine[2]);
			        
			        num.put("id_corriere", "1"); //SDA
			        
			        numeri.add(num);       
			        
		    	}
		        i++;
		    }
		    
		    salvati = Ordine_DAO.salvaNumeriTracciamento(numeri);
		    
		    Log.info("Elaborazione file completata, "+salvati+" numeri di tracciamento salvati.");
			
		} catch (IOException e) {
			Log.info(e.getMessage());
			e.printStackTrace();
		}
		return salvati;
	}
	
	public static int salvaNumeriTracciamentoGLS(String cartella, String file){
		
		int salvati = 0;
		
		String percorso = cartella+file;
		
		Log.info("Inizio elaborazione file contenente numeri di tracciamento GLS "+file);
		
		try{
			File f = new File(percorso); 
			FileInputStream fis;
			
			fis = new FileInputStream(f);

			HSSFWorkbook wb = new HSSFWorkbook(fis); 
			HSSFSheet st = wb.getSheet("spedizioni"); 
			
			List<Map<String,String>> numeri = new ArrayList<Map<String,String>>();
			
			for (int i=1;i<=st.getLastRowNum();i++){
				
				Map<String,String> num = new HashMap<String,String>();
				
				HSSFRow row=st.getRow(i); 
				
				num.put("numero_tracciamento",row.getCell(0).getStringCellValue());
				
				num.put("id_ordine", row.getCell(21).getStringCellValue().replace("#", ""));
		        
		        num.put("id_corriere", "2"); //GLS
		        
		        String d = row.getCell(1).getStringCellValue();
		        
		        String giorno = d.substring(0,2);
		        String mese = d.substring(2,4);
		        String anno = d.substring(4,6);
		        
		        String data = giorno+"/"+mese+"/20"+anno;
		        
		        num.put("data", data);
		        
		        numeri.add(num);
		        
//		        System.out.print(" "+num.get("numero_tracciamento"));
//		        System.out.print(" "+num.get("data"));
//		        System.out.print(" "+num.get("id_ordine"));
//		        System.out.print(" "+num.get("id_corriere"));
//		        System.out.println(" "+num.get("nome_corriere"));
		        
		    }
			
		    salvati = Ordine_DAO.salvaNumeriTracciamento(numeri);
		    
		    Log.info("Elaborazione file completata, "+salvati+" numeri di tracciamento salvati.");
		    
		    fis.close(); 
			
		} catch (IOException e) {
			Log.info(e.getMessage());
			e.printStackTrace();
		}
		return salvati;
	}
	
	public static int aggiungiOrdineALDV(List<Ordine> ordini, String percorso){		
		 int risultato = 0;
	     try {
			
			File f = new File(percorso);
			
			if (!f.exists()) {
				f.createNewFile();
			}
						
			FileOutputStream fos = new FileOutputStream (percorso, true);
			
			PrintWriter pw = new PrintWriter (fos);
			
			for (Ordine o : ordini){
				aggiungiOrdine(o,pw);	
			}
			
			pw.close();
			
			risultato = 1;
	
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
			risultato = 0;
		}		
	     return risultato;
	}

	
	private static void aggiungiOrdine(Ordine o, PrintWriter pw){
		
		Indirizzo in = o.getIndirizzoSpedizione();

		//Destinatario Ragione Sociale
		boolean azienda = false;
		if (in.getAzienda()!=null && !in.getAzienda().trim().isEmpty()) {
			pw.print(in.getAzienda());
			azienda = true;
		}
		if (!azienda) pw.print(in.getNomeCompleto());
		pw.print(";");
		
		//Destinatario Referente (non obbligatorio)
		if (in.getNomeCompleto()!=null && azienda) pw.print(in.getNomeCompleto());
		pw.print(";");
		
		//Destinatario Indirizzo
		if (in.getIndirizzo1()!=null) pw.print(in.getIndirizzo1());
		if (in.getIndirizzo2()!=null) pw.print(in.getIndirizzo2());
		pw.print(";");
		
		//Destinatario Cap
		if (in.getCap()!=null) pw.print(in.getCap());
		pw.print(";");
		
		//Destinatario Localita
		if (in.getComune()!=null) pw.print(in.getComune());
		pw.print(";");
		
		//Destinatario Provincia
		if (in.getProvincia()!=null) {
			String prov = in.getProvincia(); 
			if (prov.toLowerCase().equals("italy") || prov.toLowerCase().equals("italia")) prov = in.getComune();
			prov = Methods.checkProvincia(prov);
			pw.print(prov.toUpperCase());
		}		
		pw.print(";");
		
		//Destinatario Nazione
		pw.print("ITA");
		pw.print(";");
		
		//Destinatario Telefono
		if (in.getTelefono()!=null) pw.print(in.getTelefono());
		pw.print(";");
		
		//Destinatario Fax
		if (in.getFax()!=null) pw.print(in.getFax());
		pw.print(";");
		
		//Destinatario Cellulare
		if (in.getCellulare()!=null) pw.print(in.getCellulare());
		pw.print(";");
		
		//Destinatario Email
		if (o.getEmail()!=null && !o.getPiattaforma().contains("Amazon")) 
			pw.print(o.getEmail());
		pw.print(";");
		
		//Destinatario ID Fiscale
		if (in.getPartitaIva()!=null) pw.print(in.getPartitaIva());
		pw.print(";");
		
		//Codice Servizio
		pw.print("S09"); //EXTRALARGE
		pw.print(";");
		
		//Codice Accessorio Base (non obbligatorio)
		pw.print("");
		pw.print(";");
		
		//Codice sotto accessorio (non obbligatorio)
		pw.print("");
		pw.print(";");
		
		//numero riferimento interno
		pw.print("#"+o.getIdOrdine());
		pw.print(";");
		
		//numero colli
		pw.print("1");
		pw.print(";");
		
		//peso
		pw.print("2");
		pw.print(";");
		
		//importo contrassegno
		String contrassegno = "";
		if (o.getMetodoPagamento().toLowerCase().contains("contrassegno")) contrassegno = String.valueOf(o.getTotale());
		pw.print(contrassegno);
		pw.print(";");
		
		//metodo pagamento contrassegno
		String mpc = "";
		if (!contrassegno.isEmpty()) mpc =  "CON";
		pw.print(mpc);
		pw.print(";");
		
		//tipo contenuto (obbligatorio solo internazionali)
		pw.print("");
		pw.print(";");
		
		//descrizione contenuto (obbligatorio solo internazionali)
		pw.print("");
		pw.print(";");
		
		//tipo imballo (obbligatorio solo internazionali)
		pw.print("");
		pw.print(";");
		
		//altezza (obbligatorio solo internazionali)
		pw.print("");
		pw.print(";");
		
		//larghezza (obbligatorio solo internazionali)
		pw.print("");
		pw.print(";");
		
		//profondità (obbligatorio solo internazionali)
		pw.print("");
		pw.print(";");
		
		//pagamento oneri (obbligatorio solo internazionali)
		pw.print("");
		pw.print(";");
		
		//valore dichiarato (obbligatorio solo internazionali)
		pw.print("");
		pw.print(";");
		
		//assicurazione (obbligatorio solo internazionali)
		pw.print("");
		pw.print(";");
		
		//note (non obbligatorio)
		pw.print(o.getPiattaforma());
		
		
		
		/* SOLO PER GLS */
		
		//email
		pw.print(";");
		String email = o.getEmail();
		if (email==null || email.isEmpty())
			email = "io@asd.it";
		pw.print(email);
		
		//tel + id ordine interno
		pw.print(";");
		String cell = in.getCellulare();
		if (cell==null)	cell = in.getTelefono();
		if (cell!=null) pw.print(cell);
		pw.print(" - ordine #"+o.getIdOrdine());
	    
	    
	    /* fine !!! */
	    
	    pw.println();
		
	}
}
