package it.swb.java;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import it.swb.log.Log;
import it.swb.model.Articolo;
import it.swb.model.Ordine;
import it.swb.utility.Methods;

public class StampanteFiscale {
	
	public static void stampaScontrino(Ordine o){
		Log.info("Stampa scontrino in corso");
		
		try {
			//int random = (int) (Math.random()*100);
			
			String text = "_"+o.getIdOrdine()+Methods.getDataCompletaPerNomeFileTesto();
			
			String percorso = "D:\\zeus\\scontrini\\ScoZeus"+text+".txt";		
			
			File f = new File(percorso);
			
			f.createNewFile();
			
			FileOutputStream fos = new FileOutputStream (percorso, true);
			
			PrintWriter pw = new PrintWriter (fos);
			
			pw.println("printerFiscalReceipt");
			pw.println("Printer|1");
			
			for (Articolo a : o.getArticoli()){
				
				String nome = a.getNome();
				int quantita = a.getQuantitaMagazzino();
				String prezzo = String.valueOf(a.getPrezzoDettaglio()).replace(".", ",");
				String note = "";
				if (a.getNote()!=null) note = a.getNote();				
				String reparto = "1"; //iva al 22%
				if (a.getAliquotaIva()==10) reparto="2"; //iva al 10%
				else if (a.getAliquotaIva()==4) reparto="3"; //iva al 4%
				
				pw.println("printRecMessage|1|4|1|1|"+nome);
				pw.println("printRecItem|1|"+note+"|"+quantita+"|"+prezzo+"|"+reparto+"|1");				
				
			}
			pw.println("printRecSubtotal|3|1");
			
			String totale = String.valueOf(o.getTotale()).replace(".", ",");
			String costoSpedizione = String.valueOf(o.getCostoSpedizione()).replace(".", ",");
			
			pw.println("printRecMessage|1|4|1|1|Spedizione con corriere espresso");
			pw.println("printRecItem|1||1|"+costoSpedizione+"|1|1"); //il penultimo 1 è l'iva al 22% sulla spedizione
			
			if (o.getMetodoPagamento().equals("Contrassegno")){
				pw.println("printRecMessage|1|4|1|1|Contrassegno");
				pw.println("printRecItem|1||1|3,0|1|1"); //il penultimo 1 è l'iva al 22% sul contrassegno
			}
			
			pw.println("printRecTotal|4|Pagamento|"+totale+"|2|0|2");
			
			//pw.println("displayText|1|Customer Display    Printed Fisc Receipt");			
			pw.close();
			
		}
		 catch (IOException e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}		
		
	}
	
	public static void stampaScontrino(List<Articolo> articoli, double costo_spedizione, boolean contrassegno){
		Log.info("Stampa scontrino in corso");
		
		try {
			//int random = (int) (Math.random()*100);
			
			String text = "_Manuale_"+Methods.getDataCompletaPerNomeFileTesto();
			
			String percorso = "D:\\zeus\\scontrini\\ScoZeus"+text+".txt";		
			
			File f = new File(percorso);
			
			f.createNewFile();
			
			FileOutputStream fos = new FileOutputStream (percorso, true);
			
			PrintWriter pw = new PrintWriter (fos);
			
			pw.println("printerFiscalReceipt");
			pw.println("Printer|1");
			
			double tot = 0;
			
			for (Articolo a : articoli){
				
				String nome = a.getNome();
				int quantita = a.getQuantitaMagazzino();
				String prezzo = String.valueOf(a.getPrezzoDettaglio()).replace(".", ",");
				String note = "";
				if (a.getNote()!=null) note = a.getNote();
				
				tot+=a.getPrezzoDettaglio()*a.getQuantitaMagazzino();
				
				pw.println("printRecMessage|1|4|1|1|"+nome);
				pw.println("printRecItem|1|"+note+"|"+quantita+"|"+prezzo+"|15|1");				
				
			}
			pw.println("printRecSubtotal|3|1");
			
			tot = Methods.round(tot, 2);
			
			String totale = String.valueOf(tot).replace(".", ",");
			String costoSpedizione = String.valueOf(costo_spedizione).replace(".", ",");
			
			pw.println("printRecMessage|1|4|1|1|Spedizione con corriere espresso");
			pw.println("printRecItem|1||1|"+costoSpedizione+"|15|1");
			
			if (contrassegno){
				pw.println("printRecMessage|1|4|1|1|Contrassegno");
				pw.println("printRecItem|1||1|3,0|15|1");
			}
			
			pw.println("printRecTotal|4|Pagamento|"+totale+"|2|0|2");
			
			pw.println("displayText|1|Customer Display    Printed Fisc Receipt");			
			pw.close();
			
		}
		 catch (IOException e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}		
		
	}

}
