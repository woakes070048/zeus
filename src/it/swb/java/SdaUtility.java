package it.swb.java;

import it.swb.log.Log;
import it.swb.model.Indirizzo;
import it.swb.model.Ordine;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;

public class SdaUtility {
	
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
		if (in.getProvincia()!=null) pw.print(in.getProvincia().toUpperCase());
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
		if (o.getCliente().getEmail()!=null && !o.getPiattaforma().equals("Amazon")) pw.print(o.getCliente().getEmail());
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
		pw.print("Ordine nr. "+o.getIdOrdine());
		pw.print(";");
		
		//numero colli
		pw.print("1");
		pw.print(";");
		
		//peso
		pw.print("2");
		pw.print(";");
		
		//importo contrassegno
		String contrassegno = "";
		if (o.getMetodoPagamento().equals("Contrassegno")) contrassegno = String.valueOf(o.getTotale());
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
		pw.print("Ordine "+o.getPiattaforma()+" "+o.getIdOrdinePiattaforma());
		
	    
	    
	    /* fine !!! */
	    
	    pw.println();
		
	}
}
