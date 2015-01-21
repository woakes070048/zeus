package it.swb.utility;

import it.swb.log.Log;
import it.swb.model.Articolo;
import it.swb.model.Variante_Articolo;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class EbayStuff {
	
	/*	
	 * Questa classe contiene i metodi che servono per scaricare le informazioni
	 *  di un prodotto a partire dal link della relativa inserzione su ebay	
	 */
	
	
	
	/*	
	 * Questo metodo prende in input un URL e restituisce il codice html del contenuto della pagina	
	 */
	public static String leggiDaUrl(String urlebay){
		System.out.println("Cerco di ottenere il codice html dall'url: "+urlebay);
		String s = "";
		try
		{
			URL url = new URL(urlebay);
			int count;

			BufferedInputStream inStream =
				new BufferedInputStream(url.openStream());
			InputStreamReader reader = new InputStreamReader(inStream);

			while( (count = reader.read()) != -1)
			{
				//System.out.print( (char)count );
				char c = (char)count;
				s = s.concat(String.valueOf(c));			
			}
			
			System.out.println("Codice html ottenuto.");
		}
		catch(MalformedURLException ex)
		{
			ex.printStackTrace();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		return s;
	}
	
	
	/*	Questo metodo non mi ricordo a cosa serve.	*/
	public static String getUrl(String current){
		String url = "";
		try{
			current = current.replace("\n", "");
			int a = current.indexOf("Homepage del Negozio");	
			int b = current.indexOf("Vedi tutti gli oggetti nel Negozio");
			
			if(a==-1 || b==-1){
				System.out.println("Qualcosa non va...");
			}
			
			String actual = current.substring(a,b);
			
			if (actual.contains("ZELDABOMBONIERE")){
				int x = actual.indexOf("ZELDABOMBONIERE")+15;
				
				String z = actual.substring(x);
				int y = z.indexOf("m322")+4;
				
				url = z.substring(0,y);
				url = url.replace("amp;", "");
			}
		}catch (StringIndexOutOfBoundsException e){
			e.printStackTrace();
		}
		return url;
	}
	
	/*	Pure questo non mi ricordo a cosa serve	*/
	public static Map<String,String> elaboraCategoria(String current){
		Map<String,String> map = new HashMap<String,String>();
		String id_categoria = "";
		String nome_categoria="";
		try{
			current = current.replace("\n", "");
			int a = current.indexOf("Homepage del Negozio");	
			int b = current.indexOf("Vedi tutti gli oggetti nel Negozio");
			String actual = current.substring(a,b);
			
			if (actual.contains("_fsub=")){
				int x = actual.indexOf("_fsub=")+6;
				
				String z = actual.substring(x);
				int y = z.indexOf("&amp");
				
				id_categoria = z.substring(0,y);
			}
			if (actual.contains("m322")){
				int x = actual.indexOf("m322")+6;
				
				String z = actual.substring(x);
				int y = z.indexOf("</a>");
				
				nome_categoria = z.substring(0,y);
			}
					
		}catch (StringIndexOutOfBoundsException e){
			e.printStackTrace();
		}
		map.put("id_categoria", id_categoria);
		map.put("nome_categoria", nome_categoria);
		return map;
	}
	
	/*	???	*/
	public static Map<String,String> elaboraCategoriaPrincipale(String current){
		Map<String,String> map = new HashMap<String,String>();
		String id_categoria_principale = "";
		String nome_categoria_principale="";
//		String id_categoria = "";
//		String nome_categoria="";
		String url = "";
		
		current = current.replace("\n", "");
		int a = current.indexOf("Homepage del Negozio");	
		int b = current.indexOf("Vedi tutti gli oggetti nel Negozio");
		String actual = current.substring(a,b);
		
		if (actual.contains("_fsub=")){
			int x = actual.indexOf("_fsub=")+6;
			
			String z = actual.substring(x);
			int y = z.indexOf("&amp");
			
			id_categoria_principale = z.substring(0,y);
		}
		if (actual.contains("m322")){
			int x = actual.indexOf("m322")+6;
			
			String z = actual.substring(x);
			int y = z.indexOf("</a>");
			
			nome_categoria_principale = z.substring(0,y);
		}
		if (actual.contains("a href=")){
			int x = actual.indexOf("a href=")+9;
			
			String z = actual.substring(x);
			int y = z.indexOf("m322")+4;
			
			url = z.substring(0,y);
			url = url.replace("amp;", "");
		}
		
		
		map.put("url", url);
		map.put("id_categoria_principale", id_categoria_principale);
		map.put("nome_categoria_principale", nome_categoria_principale);
		return map;
	}
	
	
	/*	
	 *  Metodo principale della classe, prende in input la stringa che contiene il codice html della pagina
	 *  e ne estrapola le informazioni di interesse. Alla fine restituisce l'oggetto articolo corrispondente.
	 *  
	 *  	*/
	public static Articolo elaboraDescrizione(Articolo art){
		
		Log.debug("Inizio elaborazione della descrizione dell'articolo...");
		
		String current = art.getInfoEbay().getDescrizioneEbay();
		
		try{ 
		
			String quantitaInserzione="";
			String dimensioni="";
			String descrizione="";
			String immagine1 = null;
			String immagine2 = null;
			String immagine3 = null;
			String immagine4 = null;
			String immagine5 = null;
	//		List<Variante_Articolo> varianti = null;
			
			//questo serve solo se ci sono i merdatag nascosti:
			String codiceArticolo = "";
			String nomeArticolo;
			
			//System.out.println(current);
			
			//se nell'inserzione ci sono i MERDATAG NASCOSTI
			if (current.contains("<!--INIZIOMERDA")){
				System.out.println("In questa inserzione ci sono i MerdaTag...");
				String merda;
				int from1 = current.indexOf("<!--INIZIOMERDA")+15;
				int to1=current.indexOf("FINEMERDA -->");
				merda = current.substring(from1, to1);
				
				if (merda.contains("CODICE_ARTICOLO")){
					codiceArticolo = merda.substring(merda.indexOf("<CODICE_ARTICOLO>")+17,merda.indexOf("</CODICE_ARTICOLO>"));
					art.setCodice(codiceArticolo);
				}
				if (merda.contains("NOME_ARTICOLO")){
					nomeArticolo = merda.substring(merda.indexOf("<NOME_ARTICOLO>")+15,merda.indexOf("</NOME_ARTICOLO>"));
					art.setNome(nomeArticolo);
				}
				if (merda.contains("<DIMENSIONI>") && merda.contains("</DIMENSIONI>"))
					dimensioni = merda.substring(merda.indexOf("<DIMENSIONI>")+12,merda.indexOf("</DIMENSIONI>"));
				
				if (merda.contains("QUANTITA_INSERZIONE"))
					quantitaInserzione = merda.substring(merda.indexOf("<QUANTITA_INSERZIONE>")+21,merda.indexOf("</QUANTITA_INSERZIONE>"));
				
				if (merda.contains("DESCRIZIONE")){
					int start = merda.indexOf("<DESCRIZIONE>")+13;
					int end = merda.indexOf("</DESCRIZIONE>");
					if (end<0){
						end=merda.length();
					}
					descrizione = merda.substring(start,end).trim();
				}
				
	//			if (merda.contains("IMMAGINI")){
	//				String immagini = merda.substring(merda.indexOf("<IMMAGINI>")+10,merda.indexOf("</IMMAGINI>"));
	//				String[] array_immagini = immagini.split(";");
	//				
	//				for(int i=0;i<array_immagini.length;i++){
	//					if (i==0 && array_immagini[0]!=null) immagine1=array_immagini[0];
	//					if (i==1 && array_immagini[1]!=null) immagine2=array_immagini[1];
	//					if (i==2 && array_immagini[2]!=null) immagine3=array_immagini[2];
	//					if (i==3 && array_immagini[3]!=null) immagine4=array_immagini[3];
	//					if (i==4 && array_immagini[4]!=null) immagine5=array_immagini[4];			
	//				}	
	//			}
	//			if (merda.contains("VARIANTI_COLORE")){
	//				String variantiColore = merda.substring(merda.indexOf("<VARIANTI_COLORE>")+17,merda.indexOf("</VARIANTI_COLORE>"));
	//				String[] array_variantiColore = variantiColore.split(";");
	//				
	//				varianti = new ArrayList<Variante_Articolo>();
	//				art.setVarianti(varianti);
	//				
	//				for(int i=0;i<array_variantiColore.length;i++){
	//					Variante_Articolo v = new Variante_Articolo();
	//					v.setCodiceArticolo(codiceArticolo);
	//					
	//					int position = array_variantiColore[i].indexOf("=");
	//					int length = array_variantiColore[i].length();
	//					v.setTipo("Colore");
	//					v.setImmagine(array_variantiColore[i].substring(0,position));
	//					v.setValore(array_variantiColore[i].substring(position+1,length));	
	////					v.setItemId(art.getItemId());
	////					v.setTitolo(art.getNomeArticolo());
	//					varianti.add(v);
	//				}
	//			}
	//			if (merda.contains("VARIANTI_TEMA")){
	//				String variantiTema = merda.substring(merda.indexOf("<VARIANTI_TEMA>")+15,merda.indexOf("</VARIANTI_TEMA>"));
	//				String[] array_variantiTema = variantiTema.split(";");
	//				
	//				if (varianti==null){
	//					varianti = new ArrayList<Variante_Articolo>();
	//					art.setVarianti(varianti);
	//				}
	//				
	//				for(int i=0;i<array_variantiTema.length;i++){
	//					Variante_Articolo v = new Variante_Articolo();
	//					v.setCodiceArticolo(codiceArticolo);
	//					
	//					int position = array_variantiTema[i].indexOf("=");
	//					int length = array_variantiTema[i].length();
	//					v.setTipo("Tema");
	//					v.setImmagine(array_variantiTema[i].substring(0,position));
	//					v.setValore(array_variantiTema[i].substring(position+1,length));		
	////					v.setItemId(art.getItemId());
	////					v.setTitolo(art.getNomeArticolo());
	//					varianti.add(v);
	//				}				
	//			}
			}		
			
			
			//altrimenti se tag nascosti NON ci sono
			
				System.out.println("NON ci sono i MerdaTag...");
				current = current.toLowerCase().replace("&nbsp;", "").replace("nuovo", " nuovo").replace("personalizzazione:", "")
						.replace("  "," ").replace("\n", "").replace("  "," ").replace("<strong>","").replace("  "," ")
						.replace("</strong>", "").replace("  "," ").replace("</u>", "").replace("  "," ").replace("<u>", "").replace("  "," ")
						.replace("potete chiedere variazioni sulla composizione della bomboniera inquestione tramite mail o tramite telefono, provvederemo alla creazionedi una nuova bomboniera su misura.", "")
						.replace("  "," ").replace("<p>", "").replace("  "," ").replace("</p>", "").replace("  "," ");
				String temp ="";
				String temp_active ="";
				
				if(current.contains("quantita' inserzione:") && quantitaInserzione.isEmpty()){
					int from1=current.indexOf("quantita' inserzione:")+26;
					temp=current.substring(from1, current.length());
					int to1=temp.indexOf("</dt>");
					temp=temp.substring(0,to1);
					quantitaInserzione=temp.replace("<dt>", " ").replace("  "," ").replace("<br>", " ").replace("  "," ")
							.replace("<br>", " ").replace("  "," ").replace("br></dd> ", " ").replace("  "," ").replace("</dd>"," ")
							.replace("  "," ").replace("br>"," ").replace("  "," ").replace("<b>"," ").replace("  "," ")
							.replace("</b>"," ").replace("  "," ").trim();
					if (quantitaInserzione.contains("dimensioni:")) quantitaInserzione="";
				}
				if (current.contains("dimensioni da confezionata:") && dimensioni.isEmpty()) {
					int from2 = current.indexOf("dimensioni da confezionata:") + 32;
					temp = current.substring(from2, current.length());
					int to2 = temp.indexOf("</dt>");
					temp = temp.substring(0, to2);
					dimensioni = temp.replace("<dt>", " ").replace("  "," ").replace("<br>", " ").replace("  "," ")
							.replace("/dd>", " ").replace("  "," ")
							.replace("<br>", " ").replace("  "," ").replace("br></dd> ", " ").replace("  "," ").replace("</dd>"," ")
							.replace("  "," ").replace("br>"," ").replace("  "," ").replace("<b>"," ").replace("  "," ")
							.replace("</b>"," ").replace("  "," ").trim();
					
					if (dimensioni.contains("<br style=")){
						while (dimensioni.contains("<br style=")){
							int x = dimensioni.indexOf("<br style=");
							int y = dimensioni.indexOf("left;")+8;
							dimensioni = (dimensioni.substring(0,x)).concat(" ").concat(dimensioni.substring(y,dimensioni.length()));
						}							
					}
					
				}
				else if (current.contains("dimensioni:") && dimensioni.isEmpty()) {
					int from2 = current.indexOf("dimensioni:") + 16;
					temp = current.substring(from2, current.length());
					int to2 = temp.indexOf("</dt>");
					temp = temp.substring(0, to2);
					dimensioni = temp.replace("<dt>", " ").replace("  "," ").replace("<br>", " ").replace("  "," ")
							.replace("/dd>", " ").replace("  "," ")
							.replace("<br>", " ").replace("  "," ").replace("br></dd> ", " ").replace("  "," ").replace("</dd>"," ")
							.replace("  "," ").replace("br>"," ").replace("  "," ").replace("<b>"," ").replace("  "," ")
							.replace("</b>"," ").replace("  "," ").trim();
					
					if (dimensioni.contains("<br style=")){
						while (dimensioni.contains("<br style=")){
							int x = dimensioni.indexOf("<br style=");
							int y = dimensioni.indexOf("left;")+8;
							dimensioni = (dimensioni.substring(0,x)).concat(dimensioni.substring(y,dimensioni.length()));
						}							
					}
				}
				
	
				if (current.contains("descrizione:") && descrizione.isEmpty()) {
					int from4 = current.indexOf("descrizione:") + 17;
					temp = current.substring(from4, current.length());
					int to4 = temp.indexOf("</dt>");
					temp = temp.substring(0, to4);
					descrizione = temp.replace("<dt class=\"lastrow\">", " ").replace("  "," ")
							.replace("<br>", " ").replace("  "," ").replace("br></dd> ", " ").replace("  "," ").replace("</dd>"," ")
							.replace("  "," ").replace("br>"," ").replace("  "," ").replace("<b>"," ").replace("  "," ")
							.replace("</b>"," ").replace("  "," ").trim();
					
					descrizione = descrizione.replace("al seguente link:", "");
					descrizione = descrizione.replace("<span style=\"font-weight: bold;\"></span>", "");
					descrizione = descrizione.replace(" confezione regalo", "; confezione regalo");	
					
					
					while (descrizione.contains("<a") || descrizione.contains("/a>")) {
						
							int a = descrizione.indexOf("<a");
							String c = descrizione.substring(0, a);
							String d = descrizione.substring(a, descrizione.length());
							int b = d.indexOf("/a>") + 3;					
							String e = d.substring(b, d.length());
							descrizione = c.concat(e);
							
							descrizione = descrizione.replace("disponibili nel nostro store", ",");	
					}
				}
				
				try {
					if (current.contains("www.zeldabomboniere.it/immagini/")) {
						int from5 = current.indexOf("www.zeldabomboniere.it/immagini/") + 32;
						temp = current.substring(from5, current.length());
						int to5 = temp.indexOf(".jpg") + 4;
						if (to5 > 0) {
							immagine1 = temp.substring(0, to5);
							//System.out.println(immagine1);
							// else System.out.println(temp.substring(0, 30));
							temp_active = temp.substring(to5);
							if (temp_active.contains("/"+immagine1.substring(0, 7))) {
								int from6 = temp_active.indexOf("/"+immagine1.substring(0,7))+1;
								int to6 = temp_active.indexOf(".jpg") + 4;
								if (to6 > 0) {
									immagine2 = temp_active.substring(from6, to6);
									//System.out.println(immagine2);
									
									temp = temp_active.substring( to6 -1);
									if (temp.contains("/"+immagine1.substring(0, 7))) {
										int from7 = temp
												.indexOf("/"+immagine1.substring(0, 7))+1;
										int to7 = temp.indexOf(".jpg") + 4;
										if (to7 > 0) {				
											 immagine3 = temp.substring(from7,to7);
											 //System.out.println(immagine3);
		
											 temp_active = temp.substring( to7 -1);
											if (temp_active.contains("/"+immagine1.substring(0, 7))) {
												int from8 = temp_active
														.indexOf("/"+immagine1.substring(0, 7))+1;
												int to8 = temp_active.indexOf(".jpg") + 4;
												if (to8 > 0) {
													immagine4 = temp_active.substring(from8,to8);
													//System.out.println(immagine4);
		
													temp = temp_active.substring( to8 -1);
													if (temp.contains("/"+immagine1.substring(0, 7))) {
														int from9 = temp
																.indexOf("/"+immagine1.substring(0, 7))+1;
														int to9 = temp.indexOf(".jpg") + 4;
														if (to9 > 0)
															immagine5 = temp.substring(
																	from9, to9);
														//System.out.println(immagine5);
													}
												}
											}
										}
									}
								}
							}
						} //System.out.println();
					}
				}
				catch (StringIndexOutOfBoundsException e) {
					e.printStackTrace();
					System.out.println();
				}
			
			// int from = current.indexOf("descrizione:");
			// int to = current.indexOf("fourcards");
	//		temp = current.substring(from, to);
	//		int from1 = temp.indexOf("lastrow")+9;
	//		int to1 = temp.indexOf("</dt>");
	//		System.out.println("Descrizione: "+temp.substring(from1, to1).replace("<br>", "").toLowerCase());
	//		
	//		int from2 = current.indexOf("www.zeldabomboniere.it/immagini/") + 32;
	//		int to2 = current.substring(from2).indexOf(".jpg")+4;
	//		temp = current.substring(from2, from2+to2);
	//		int to3 = temp.indexOf(".jpg") + 4;
	//		System.out.println("Percorso immagine: "+ temp.substring(0, to3));
			
			art.setQuantitaInserzione(quantitaInserzione);
			art.setDimensioni(dimensioni);
			art.setDescrizione(descrizione);
			
			if (immagine1!=null) {
				immagine1=immagine1.toLowerCase().trim();
				int from = immagine1.indexOf("/")+1;
				int to = immagine1.indexOf(".jpg");
				String codice = immagine1.substring(from,to).toUpperCase();
				art.setCodice(codice);
			}
			if (immagine2!=null) immagine2=immagine2.toLowerCase().trim();
			if (immagine3!=null) immagine3=immagine3.toLowerCase().trim();
			if (immagine4!=null) immagine4=immagine4.toLowerCase().trim();
			if (immagine5!=null) immagine5=immagine5.toLowerCase().trim();
			
			art.setImmagine1(immagine1);
			art.setImmagine2(immagine2);
			art.setImmagine3(immagine3);
			art.setImmagine4(immagine4);
			art.setImmagine5(immagine5);
			
			Log.debug("Fine elaborazione descrizone.");
		}
		catch(Exception e){
			Log.error(e.getMessage());
			e.printStackTrace();
		}			
			return art;	
	}
	
	public static void printArticolo(Articolo a){
		Log.debug("NOME ARTICOLO: "+a.getNome());
		Log.debug("TITOLO INSERZIONE: "+a.getInfoEbay().getTitoloInserzione());
		Log.debug("PREZZO: "+a.getInfoEbay().getPrezzo());
		Log.debug("CODICE ARTICOLO: "+a.getCodice());
		Log.debug("ID CATEGORIA: "+a.getIdCategoria());
		Log.debug("CATEGORIA EBAY 1: "+a.getInfoEbay().getIdCategoriaEbay1()+" --> "+a.getInfoEbay().getNomeCategoriaEbay1());
		Log.debug("CATEGORIA EBAY 2: "+a.getInfoEbay().getIdCategoriaEbay2()+" --> "+a.getInfoEbay().getNomeCategoriaEbay2());
//		Log.debug("NOME CATEGORIA: "+a.getNomeCategoria());
		Log.debug("QUANTITA: "+a.getQuantitaMagazzino());
		Log.debug("QUANTITA INSERZIONE: "+a.getQuantitaInserzione());
		Log.debug("DIMENSIONI: "+a.getDimensioni());
		Log.debug("DESCRIZIONE: "+a.getDescrizione());
		Log.debug("IMG1: "+a.getImmagine1());
		Log.debug("IMG2: "+a.getImmagine2());
		Log.debug("IMG3: "+a.getImmagine3());
		
		if (a.getVarianti()!=null){
			for (Variante_Articolo v : a.getVarianti()){
				Log.debug("TIPO VARIANTE: "+v.getTipo());
				Log.debug("VALORE VARIANTE: "+v.getValore());
				Log.debug("QUANTITA VARIANTE: "+v.getQuantita());	
				Log.debug("IMMAGINE VARIANTE: "+v.getImmagine());				
			}
		}
		
		Log.debug("");
	}

}
