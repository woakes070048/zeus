package it.swb.piattaforme.amazon;

import it.swb.business.ArticoloBusiness;
import it.swb.log.Log;
import it.swb.model.Articolo;
import it.swb.model.ArticoloAcquistato;
import it.swb.model.Cliente;
import it.swb.model.Indirizzo;
import it.swb.model.Ordine;
import it.swb.utility.Methods;

import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

public class ElaboratoreOrdini {
	
	public static List<Ordine> leggiCsvOrdiniYatego(String percorso){
		List<Ordine> listaOrdini = null;
		Map<String,Ordine> mappaOrdini = null;
		
		try {
			@SuppressWarnings("resource")
			CSVReader reader = new CSVReader(new FileReader(percorso), ';', '\"', 2);
			
			mappaOrdini = new HashMap<String,Ordine>();
			 
			String [] n;
		    while ((n = reader.readNext()) != null) {
		    	
		    	String id_ordine = n[0];
	    		String data = n[2];
	    		String email_acquirente = n[3];
	    		String c_azienda = n[4];
	    		String c_titolo = n[6];
	    		String c_nome = n[7];
	    		String c_cognome = n[8];
	    		String c_via = n[9];
	    		String c_cap = n[10];
	    		String c_comune = n[11];
	    		String c_provincia = n[12];
	    		String c_nazione = n[13];
	    		String c_telefono = n[14];
	    		String c_fax = n[15];
	    		String s_azienda = n[17];
	    		String s_titolo = n[18];
	    		String s_nome = n[19];
	    		String s_cognome = n[20];
	    		String s_via = n[21];
	    		String s_cap = n[22];
	    		String s_comune = n[23];
	    		String s_provincia = n[24];
	    		String s_nazione = n[25];
	    		String s_telefono = n[26];
//	    		String s_fax = n[27];
	    		String metodo_pagamento = n[28];
	    		String costo_spedizione = n[35];
	    		String contrassegno = n[36];
	    		String maggiorazione_estero = n[37];
	    		String totale = n[38];
	    		String messaggio = n[40];
	    		String sku = n[41];
	    		String titolo_inserzione = n[42];
	    		String variante = n[43];
	    		String quantita = n[44];
//	    		String prezzo_unitario = n[45];
	    		String prezzo_complessivo = n[47];
	    		String iva = n[48];
		    	
		    	if (!mappaOrdini.containsKey(id_ordine)){
		    		
//		    		for (int i=0;i<n.length;i++){
//		    			System.out.println(i+" --> "+n[i]);
//		    		}
		    		
		    		/* Informazioni sull'ordine */
			        Ordine o = new Ordine();
			        
			        o.setPiattaforma("Yatego");
			        o.setStato("Spedito");
			        o.setIdOrdinePiattaforma(id_ordine);
			        
			        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			        o.setDataAcquisto(df.parse(data));
			        o.setMetodoPagamento(metodo_pagamento);
			        
			        Double csp = Double.valueOf(costo_spedizione.replace(",", "."));
			        
			        if (contrassegno!=null && !contrassegno.isEmpty() && !contrassegno.equals("0,00")) 
			        	csp+=Double.valueOf(contrassegno.replace(",", "."));
			        	//costo_spedizione+=" + "+contrassegno+" (CS)";
			        if (maggiorazione_estero!=null && !maggiorazione_estero.isEmpty() && !maggiorazione_estero.equals("0,00")) 
			        	csp+=Double.valueOf(maggiorazione_estero.replace(",", "."));
			        	//costo_spedizione+=" + "+maggiorazione_estero+" (E)";
			        	
			        o.setCostoSpedizione(csp);
			        o.setTotale(Double.valueOf(totale.replace(",", "."))+csp);
			        o.setValuta("EUR");
			        o.setQuantitaAcquistata(0);
			        o.setCommento(messaggio);
			        
			        // n[33] spese di spedizione
			        // n[34] costo aggiuntivo per contrassegno
			        // n[35] maggiorazione paesi
			        // 
			        
			        /* Informazioni sul cliente */
			        Cliente c = new Cliente();
			        
			        c.setPiattaforma("Yatego");
			        c.setEmail(email_acquirente);
			        c.setAzienda(c_azienda);
			        c.setTitolo(c_titolo);
			        c.setNome(c_nome);
			        c.setCognome(c_cognome);
			        c.setNomeCompleto(c_nome+" "+c_cognome);
			        c.setTelefono(c_telefono);
			        c.setFax(c_fax);
			        
			        /* Informazioni sull'indirizzo */
			        Indirizzo i = new Indirizzo();
			        if (s_azienda!=null && !s_azienda.isEmpty()) i.setAzienda(n[15]);
			        else i.setAzienda(c_azienda);
			        
			        if (s_titolo!=null && !s_titolo.isEmpty()) i.setTitolo(n[16]);
			        
			        if (s_nome!=null && !s_nome.isEmpty()) i.setNomeCompleto(s_nome+" "+s_cognome);
			        else i.setNomeCompleto(c_nome+" "+c_cognome);
			        
			        if (s_via!=null && !s_via.isEmpty()) {
			        	i.setIndirizzo1(s_via);
				        i.setCap(s_cap);
				        i.setComune(s_comune);
				        i.setProvincia(s_provincia);
				        i.setNazione(s_nazione);
			        }
			        else {
				        i.setIndirizzo1(c_via);
				        i.setCap(c_cap);
				        i.setComune(c_comune);
				        i.setProvincia(c_provincia);
				        i.setNazione(c_nazione);
			        }
			        
			        if (s_telefono!=null && !s_telefono.isEmpty()) i.setTelefono(s_telefono);
			        else i.setTelefono(c_telefono);
			        
			        /* settaggi vari */
			        c.setIndirizzoSpedizione(i);
			        o.setCliente(c);
			        
			        
			        List<ArticoloAcquistato> articoli = new ArrayList<ArticoloAcquistato>();
			        
			        /* informazioni sull'articolo attuale (di questa linea del foglio) */
			        ArticoloAcquistato a = new ArticoloAcquistato();
			        
			        if (sku!=null && !sku.isEmpty()) a.setCodice(sku);
			        a.setNome(titolo_inserzione);
			        a.setTitoloInserzione("");
			        if (variante!=null && !variante.isEmpty()) a.setVariante(variante); //variante
			        a.setQuantitaAcquistata(Integer.valueOf(quantita.replace(",", ".")));
			        o.setQuantitaAcquistata(Integer.valueOf(quantita.replace(",", ".")));
			        // n[43] prezzo unitario
			        // n[44] sconto quantità
			        // n[45] prezzo complessivo (prezzo unitario * quantita articolo)
			        a.setPrezzoUnitario(Double.valueOf(prezzo_complessivo.replace(",", ".")));
			        a.setIva(Integer.valueOf(iva.replace(",00", "").replace(".00", "")));
			        a.setIdTransazione(id_ordine+"_1");
			        
			        /* settaggi vari */
			        articoli.add(a);
			        o.setElencoArticoli(articoli);
			        
			        mappaOrdini.put(o.getIdOrdinePiattaforma(), o);
		    	}
		    	else {
		    		ArticoloAcquistato a = new ArticoloAcquistato();
			        
		    		if (sku!=null && !sku.isEmpty()) a.setCodice(sku);
			        a.setNome(titolo_inserzione);
			        a.setTitoloInserzione("");
			        if (variante!=null && !variante.isEmpty()) a.setVariante(variante); //variante
			        a.setQuantitaAcquistata(Integer.valueOf(quantita.replace(",", ".")));
			        a.setPrezzoUnitario(Double.valueOf(prezzo_complessivo.replace(",", ".")));
			        a.setIva(Integer.valueOf(iva.replace(",00", "").replace(".00", "")));
			        a.setIdTransazione(id_ordine+"_"+(mappaOrdini.get(id_ordine).getElencoArticoli().size()+1));
			        
			        mappaOrdini.get(id_ordine).setQuantitaAcquistata(mappaOrdini.get(id_ordine).getQuantitaAcquistata()+Integer.valueOf(quantita));
			        
		    		mappaOrdini.get(id_ordine).getElencoArticoli().add(a);
		    	}
		    }
		    
		    listaOrdini = new ArrayList<Ordine>(mappaOrdini.values());
			
		} catch (Exception e) {
			Log.info(e);
			e.printStackTrace();
		}
		return listaOrdini;
	}
	
	
	public static List<Ordine> leggiTxtOrdiniAmazon(String percorso){
		List<Ordine> listaOrdini = null;
		Map<String,Ordine> mappaOrdini = null;
		Map<String, Articolo> mappaArticoli = ArticoloBusiness.getInstance().getMappaArticoli();
		
		try {
			String s = Methods.leggiFiles(percorso);
			
			String[] righe = s.split("\n");
			
			mappaOrdini = new HashMap<String,Ordine>();
			
			for (int i=1;i<righe.length;i++){
				//System.out.println(i+" --> "+righe[i]);
				
				String[] colonne = righe[i].split("	");	
				
				String order_id = colonne[0];
				String order_item_id = colonne[1];
				String purchase_date = colonne[2];
				String payments_date = colonne[3];
				String buyer_email = colonne[4];
				String buyer_name = Methods.primeLettereMaiuscole(colonne[5]);
				String buyer_phone_number = colonne[6];
				String sku = colonne[7];
				String product_name = colonne[8];
				int quantity_purchased = Integer.valueOf(colonne[9]);
				String currency = colonne[10];
				double item_price = Double.valueOf(colonne[11]);
//				String item_tax = colonne[12];
				double shipping_price = Double.valueOf(colonne[13]);
//				String shipping_tax = colonne[14];
				String ship_service_level = colonne[15];
				String recipient_name = colonne[16];
				String ship_address_1 = colonne[17];
				String ship_address_2 = colonne[18];
				String ship_address_3 = colonne[19];
				String ship_city = colonne[20];
				String ship_state = colonne[21];
				String ship_postal_code = colonne[22];
				String ship_country = colonne[23];
				String ship_phone_number;
				try {
					ship_phone_number = colonne[24];
				}
				catch (ArrayIndexOutOfBoundsException e){
					ship_phone_number = "";
				}
//				String delivery_start_date = colonne[25];
//				String delivery_end_date = colonne[26];
//				String delivery_time_zone = colonne[27];
//				String delivery_Instructions = colonne[28];
//				String sales_channel = colonne[29];
				
				if (sku.contains("-")){
					int x = sku.indexOf("-");
					sku = sku.substring(0,x);
				}
				
				
				
				/* costruisco l'articolo */
				ArticoloAcquistato a = new ArticoloAcquistato();
		        
		        Articolo a1 = mappaArticoli.get(sku);
		        
		        if (a1!=null){
		        	a.setIdArticolo(a1.getIdArticolo());
		        }
		        
		        a.setCodice(sku);
		        a.setNome(product_name);
		        a.setTitoloInserzione(product_name);
		        
		        /* variante */
		        if (product_name.charAt(product_name.length()-1)==')'){
		        	int x = product_name.lastIndexOf("(");
		        	
		        	String variante = product_name.substring(x+1,product_name.length()-1);
		        	a.setVariante(variante);
		        }
		        	
		        a.setQuantitaAcquistata(quantity_purchased);
		        
		        
		        double prezzoUnitario = Methods.round(item_price/quantity_purchased,2);
		        
		        a.setPrezzoUnitario(Methods.veryRound(prezzoUnitario));
		        a.setPrezzoTotale(item_price);
		        a.setIdTransazione(order_item_id);
				
		        
				/* Caso in cui è il primo articolo dell'ordine */
				if (!mappaOrdini.containsKey(order_id)){
				
					Ordine o = new Ordine();
					o.setPiattaforma("Amazon");
					o.setIdOrdinePiattaforma(order_id);
					if (ship_service_level.equals("Expedited"))
						o.setStato("Spedito");
					else o.setStato(ship_service_level);
					
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					purchase_date = purchase_date.replace("T", " ").replace("+00:00", "");
					payments_date = payments_date.replace("T", " ").replace("+00:00", "");
					
					//System.out.println(purchase_date);
				    o.setDataAcquisto(df.parse(purchase_date));
				    o.setDataPagamento(df.parse(payments_date));
				     
				    o.setCostoSpedizione(shipping_price);
				    o.setTotale(item_price+shipping_price);
				    o.setValuta(currency);
				    o.setQuantitaAcquistata(0);
				    o.setMetodoPagamento("Checkout by Amazon");
				    
				    o.setEmail(buyer_email);
				    o.setNomeAcquirente(buyer_name);
				    
				    Cliente c = new Cliente();
				    c.setPiattaforma("Amazon");
				    c.setEmail(buyer_email);
				    c.setNomeCompleto(buyer_name);
				    c.setTelefono(buyer_phone_number);
				    
				    Indirizzo indSpedizione = new Indirizzo();
				    indSpedizione.setNomeCompleto(recipient_name);
				    indSpedizione.setIndirizzo1((ship_address_1+" "+ship_address_2+" "+ship_address_3).trim());
				    indSpedizione.setComune(ship_city);
				    indSpedizione.setProvincia(ship_state);
				    indSpedizione.setCap(ship_postal_code);
				    indSpedizione.setNazione(ship_country);
				    indSpedizione.setTelefono(ship_phone_number);
				    
				    c.setIndirizzoSpedizione(indSpedizione);
				    o.setIndirizzoSpedizione(indSpedizione);
				    o.setCliente(c);
			    
				    
				    List<ArticoloAcquistato> articoli = new ArrayList<ArticoloAcquistato>();
			        
			        /* settaggi vari */
			        articoli.add(a);
			        o.setElencoArticoli(articoli);
			        
			        mappaOrdini.put(o.getIdOrdinePiattaforma(), o);
				}
				
				/* caso in cui non è il primo articolo dell'ordine */
				else {
			        
			        mappaOrdini.get(order_id).setQuantitaAcquistata(mappaOrdini.get(order_id).getQuantitaAcquistata()+quantity_purchased);
			        
			        double w = mappaOrdini.get(order_id).getCostoSpedizione();
			        mappaOrdini.get(order_id).setCostoSpedizione(w+shipping_price);
			        
			        mappaOrdini.get(order_id).setTotale(mappaOrdini.get(order_id).getTotale()+item_price+shipping_price);
			        
		    		mappaOrdini.get(order_id).getElencoArticoli().add(a);
				}
				
				listaOrdini = new ArrayList<Ordine>(mappaOrdini.values());
			    
			}
		} catch (Exception e) {
			Log.info(e);
			e.printStackTrace();
		}
		return listaOrdini;
	}

}
