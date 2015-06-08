package it.swb.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import it.swb.log.Log;
import it.swb.model.Articolo;

public class EditorDescrizioni {
	

	
	public static String creaDescrizioneEbay(Articolo a){
		Log.debug("Inizio creazione del template eBay con la descrizione dell'articolo...");
		String desc = "";
	     try {
	    	 if (a.getInfoEbay()!=null){
				URL url = EditorDescrizioni.class.getResource("/ebay_desc.txt");				
			
				desc = readFile(url.toURI());
				
				desc = desc.replace("__CODICE_ARTICOLO__", a.getCodice());
				String codbarre="";
				if (a.getCodiceBarre()!=null) codbarre = a.getCodiceBarre();
				desc = desc.replace("__CODICE_BARRE__", codbarre);
				desc = desc.replace("__TITOLO_INSERZIONE__", a.getInfoEbay().getTitoloInserzione());
				desc = desc.replace("__NOME_ARTICOLO__", Methods.primeLettereMaiuscole(a.getNome()));
				desc = desc.replace("__QUANTITA_INSERZIONE__", Methods.primeLettereMaiuscole(a.getQuantitaInserzione()));
				desc = desc.replace("__DIMENSIONI__", Methods.primeLettereMaiuscole(a.getDimensioni()).replace("Cm", "cm"));
				desc = desc.replace("__DESCRIZIONE__", Methods.MaiuscolaDopoPunto(a.getDescrizione()));
				
				if (a.getCategoria()!=null){
					desc = desc.replace("__CATEGORIA_PRINCIPALE__", a.getCategoria().getNomeCategoriaPrincipale());
					
					if (a.getCategoria().getNomeCategoria()!=null) 
						desc = desc.replace("__CATEGORIA__", a.getCategoria().getNomeCategoria());
					else 
						desc = desc.replace("__CATEGORIA__", "");
					
					desc = desc.replace("__LINK_CATEGORIA__", String.valueOf(a.getCategoria().getIdCategoriaEbay()));
				}
				
				
				if (a.getInfoEbay().isBoxBomboniere()){
					desc = desc.replace("<!--BOX_BOMBONIERE", "");
					desc = desc.replace("BOX_BOMBONIERE-->", "");
				}
				if (a.getInfoEbay().isBoxNaturale()){
					desc = desc.replace("<!--BOX_NATURALE", "");
					desc = desc.replace("BOX_NATURALE-->", "");
				}
				
				if (a.getVideo()!=null && !a.getVideo().isEmpty() && !a.getVideo().equals("") && a.getIdVideo()!=null && !a.getIdVideo().isEmpty()){
					if (a.getVideo().equals("youtube")){
						desc = desc.replace("<!--VIDEO_YOUTUBE", "");
						desc = desc.replace("VIDEO_YOUTUBE-->", "");
						
						desc = desc.replace("__ID_VIDEO_YOUTUBE__", a.getIdVideo());	
					}
					else if (a.getVideo().equals("vimeo")){
						desc = desc.replace("<!--VIDEO_VIMEO", "");
						desc = desc.replace("VIDEO_VIMEO-->", "");
						
						desc = desc.replace("__ID_VIDEO_VIMEO__", a.getIdVideo());	
					}
								
				}
				
				String cartella = Methods.getNomeCartella(a.getImmagine1());
				desc = desc.replace("__CARTELLA__", cartella);
				
				String immagine1 = Methods.getNomeImmagine(a.getImmagine1());
				desc = desc.replace("__IMMAGINE_1__", immagine1);
				
				if (a.getImmagine2()!=null && !a.getImmagine2().isEmpty()){
					String immagine2 = Methods.getNomeImmagine(a.getImmagine2());
					
					desc = desc.replace("__IMMAGINE_2__", immagine2);
					
					desc = desc.replace("<!--IMMAGINE_2", "");
					desc = desc.replace("IMMAGINE_2-->", "");
				}
				
				if (a.getImmagine3()!=null && !a.getImmagine3().isEmpty()){
					
					String immagine3 = Methods.getNomeImmagine(a.getImmagine3());
					
					desc = desc.replace("__IMMAGINE_3__", immagine3);
					
					desc = desc.replace("<!--IMMAGINE_3", "");
					desc = desc.replace("IMMAGINE_3-->", "");
				}
				
				if (a.getImmagine4()!=null && !a.getImmagine4().isEmpty()){
					
					String immagine4 = Methods.getNomeImmagine(a.getImmagine4());
					
					desc = desc.replace("__IMMAGINE_4__", immagine4);
					
					desc = desc.replace("<!--IMMAGINE_4", "");
					desc = desc.replace("IMMAGINE_4-->", "");
				}
				
				if (a.getImmagine5()!=null && !a.getImmagine5().isEmpty()){
					
					String immagine5 = Methods.getNomeImmagine(a.getImmagine5());
					
					desc = desc.replace("__IMMAGINE_5__", immagine5);
					
					desc = desc.replace("<!--IMMAGINE_5", "");
					desc = desc.replace("IMMAGINE_5-->", "");
				}
				
				/* System.out.println(desc); */
				Log.debug("Fine creazione template eBay.");
	    	 } else Log.debug("Informazioni eBay mancanti, template non creato.");
	     	}catch (Exception e) {
				e.printStackTrace();
				Log.error(e.getMessage());
				desc = "";
	     	}
	
		return desc;
	}
	
	
	
	public static String creaDescrizioneSitoGM(Articolo a){
		Log.debug("Inizio creazione descrizione GM");
		String desc = "";
	     try {
			URL url = EditorDescrizioni.class.getResource("/gm_desc.txt");				
		
			desc = readFile(url.toURI());
			
			desc = desc.replace("__QUANTITA_INSERZIONE__", Methods.primeLettereMaiuscole(a.getQuantitaInserzione()));
			desc = desc.replace("__DIMENSIONI__", Methods.primeLettereMaiuscole(a.getDimensioni()).replace("Cm", "cm"));
			desc = desc.replace("__DESCRIZIONE__", Methods.MaiuscolaDopoPunto(a.getDescrizione()));
			if (a.getCodiceBarre()!=null)
				desc = desc.replace("__CODICE_BARRE__", a.getCodiceBarre());
			else desc = desc.replace("__CODICE_BARRE__", "");
			desc = desc.replace("__CODICE_ARTICOLO__", a.getCodice());
			
			
			/* System.out.println(desc); */
			
	     	}catch (URISyntaxException e) {
				e.printStackTrace();
				Log.error(e.getMessage());
	     	}
		     Log.debug("Fine creazione descrizione GM");
	
		return desc;
	}
	
	
	public static String creaDescrizioneYatego(String nome, String quantita, String dimensioni, String descrizione){
		Log.debug("Inizio creazione descrizione Yatego");
		String desc = "";
	     try {
			URL url = EditorDescrizioni.class.getResource("/yatego_desc.txt");				
		
			desc = readFile(url.toURI());
			
			desc = desc.replace("__NOME_ARTICOLO__", Methods.primeLettereMaiuscole(nome));
			desc = desc.replace("__QUANTITA_INSERZIONE__", Methods.primeLettereMaiuscole(quantita));
			desc = desc.replace("__DIMENSIONI__", Methods.primeLettereMaiuscole(dimensioni).replace("Cm", "cm"));
			desc = desc.replace("__DESCRIZIONE__", Methods.MaiuscolaDopoPunto(descrizione));
			
			
			/* System.out.println(desc); */
	     	}catch (Exception e) {
				e.printStackTrace();
				Log.error(e.getMessage());
	     	}
		     Log.debug("Fine creazione descrizione Yatego");
	
		return desc;
	}
	
	
	
	
	private static String readFile(URI percorso) {
        File file = new File(percorso);
        StringBuilder contents = new StringBuilder();
        BufferedReader reader = null;
 
        try {
            reader = new BufferedReader(new FileReader(file));
            String text = null;
 
            // repeat until all lines is read
            while ((text = reader.readLine()) != null) {
                contents.append(text)
                        .append(System.getProperty(
                                "line.separator"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
 
        // show file contents here
        return contents.toString();
    }
	
	
	
	
//	public static Ebay_Articolo elaboraDescrizione(Ebay_Articolo art,String current){
//		
//		String quantitaInserzione="";
//		String dimensioni="";
//		String ambitoUtilizzo="";
//		String descrizione="";
//		String immagine1="";
//		String immagine2="";
//		String immagine3="";
//		String immagine4="";
//		String immagine5="";
//		List<Variante_Articolo> varianti = null;
//		
//		//solo tag nascosti:
//		String codiceArticolo = "";
//		String nomeArticolo;
//		
//		//se nell'inserzione ci sono i MERDATAG NASCOSTI
//		if (current.contains("<!--INIZIOMERDA")){
//			String merda;
//			int from1 = current.indexOf("<!--INIZIOMERDA")+15;
//			int to1=current.indexOf("FINEMERDA -->");
//			merda = current.substring(from1, to1);
//			
//			if (merda.contains("CODICE_ARTICOLO")){
//				codiceArticolo = merda.substring(merda.indexOf("<CODICE_ARTICOLO>")+17,merda.indexOf("</CODICE_ARTICOLO>"));
//				art.setCodiceArticolo(codiceArticolo);
//			}
//			if (merda.contains("NOME_ARTICOLO")){
//				nomeArticolo = merda.substring(merda.indexOf("<NOME_ARTICOLO>")+15,merda.indexOf("</NOME_ARTICOLO>"));
//				art.setNomeArticolo(nomeArticolo);
//			}
//			if (merda.contains("<DIMENSIONI>") && merda.contains("</DIMENSIONI>"))
//				dimensioni = merda.substring(merda.indexOf("<DIMENSIONI>")+12,merda.indexOf("</DIMENSIONI>"));
//			if (merda.contains("QUANTITA_INSERZIONE"))
//				quantitaInserzione = merda.substring(merda.indexOf("<QUANTITA_INSERZIONE>")+21,merda.indexOf("</QUANTITA_INSERZIONE>"));
//			if (merda.contains("DESCRIZIONE")){
//				int start = merda.indexOf("<DESCRIZIONE>")+13;
//				int end = merda.indexOf("</DESCRIZIONE>");
//				if (end<0){
//					end=merda.length();
//				}
//				descrizione = merda.substring(start,end).trim();
//			}
//			
//			if (merda.contains("IMMAGINI")){
//				String immagini = merda.substring(merda.indexOf("<IMMAGINI>")+10,merda.indexOf("</IMMAGINI>"));;
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
//				String variantiColore = merda.substring(merda.indexOf("<VARIANTI_COLORE>")+17,merda.indexOf("</VARIANTI_COLORE>"));;
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
//					v.setImmagine(array_variantiColore[i].substring(0,position));
//					v.setTipo(array_variantiColore[i].substring(position+1,length));	
//					v.setItemId(art.getItemId());
//					v.setTitolo(art.getNomeArticolo());
//					varianti.add(v);
//				}
//			}
//			if (merda.contains("VARIANTI_TEMA")){
//				String variantiTema = merda.substring(merda.indexOf("<VARIANTI_TEMA>")+15,merda.indexOf("</VARIANTI_TEMA>"));;
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
//					v.setImmagine(array_variantiTema[i].substring(0,position));
//					v.setTipo(array_variantiTema[i].substring(position+1,length));		
//					v.setItemId(art.getItemId());
//					v.setTitolo(art.getNomeArticolo());
//					varianti.add(v);
//				}				
//			}
//		}		
//		//altrimenti se tag nascosti NON ci sono
//		else {
//			current = current.toLowerCase().replace("&nbsp;", "").replace("nuovo", " nuovo").replace("personalizzazione:", "")
//					.replace("  "," ").replace("\n", "").replace("  "," ").replace("<strong>","").replace("  "," ")
//					.replace("</strong>", "").replace("  "," ").replace("</u>", "").replace("  "," ").replace("<u>", "").replace("  "," ")
//					.replace("potete chiedere variazioni sulla composizione della bomboniera inquestione tramite mail o tramite telefono, provvederemo alla creazionedi una nuova bomboniera su misura.", "")
//					.replace("  "," ").replace("<p>", "").replace("  "," ").replace("</p>", "").replace("  "," ");
//			String temp ="";
//			String temp_active ="";
//			
//			if(current.contains("quantita' inserzione:")){
//			int from1=current.indexOf("quantita' inserzione:")+26;
//			temp=current.substring(from1, current.length());
//			int to1=temp.indexOf("</dt>");
//			temp=temp.substring(0,to1);
//			quantitaInserzione=temp.replace("<dt>", " ").replace("  "," ").replace("<br>", " ").replace("  "," ")
//					.replace("<br>", " ").replace("  "," ").replace("br></dd> ", " ").replace("  "," ").replace("</dd>"," ")
//					.replace("  "," ").replace("br>"," ").replace("  "," ").replace("<b>"," ").replace("  "," ")
//					.replace("</b>"," ").replace("  "," ").trim();
//			if (quantitaInserzione.contains("dimensioni:")) quantitaInserzione="";
//			}
//			if (current.contains("dimensioni da confezionata:")) {
//				int from2 = current.indexOf("dimensioni da confezionata:") + 32;
//				temp = current.substring(from2, current.length());
//				int to2 = temp.indexOf("</dt>");
//				temp = temp.substring(0, to2);
//				dimensioni = temp.replace("<dt>", " ").replace("  "," ").replace("<br>", " ").replace("  "," ")
//						.replace("/dd>", " ").replace("  "," ")
//						.replace("<br>", " ").replace("  "," ").replace("br></dd> ", " ").replace("  "," ").replace("</dd>"," ")
//						.replace("  "," ").replace("br>"," ").replace("  "," ").replace("<b>"," ").replace("  "," ")
//						.replace("</b>"," ").replace("  "," ").trim();
//				
//				if (dimensioni.contains("<br style=")){
//					while (dimensioni.contains("<br style=")){
//						int x = dimensioni.indexOf("<br style=");
//						int y = dimensioni.indexOf("left;")+8;
//						dimensioni = (dimensioni.substring(0,x)).concat(" ").concat(dimensioni.substring(y,dimensioni.length()));
//					}							
//				}
//				
//			}
//			else if (current.contains("dimensioni:")) {
//				int from2 = current.indexOf("dimensioni:") + 16;
//				temp = current.substring(from2, current.length());
//				int to2 = temp.indexOf("</dt>");
//				temp = temp.substring(0, to2);
//				dimensioni = temp.replace("<dt>", " ").replace("  "," ").replace("<br>", " ").replace("  "," ")
//						.replace("/dd>", " ").replace("  "," ")
//						.replace("<br>", " ").replace("  "," ").replace("br></dd> ", " ").replace("  "," ").replace("</dd>"," ")
//						.replace("  "," ").replace("br>"," ").replace("  "," ").replace("<b>"," ").replace("  "," ")
//						.replace("</b>"," ").replace("  "," ").trim();
//				
//				if (dimensioni.contains("<br style=")){
//					while (dimensioni.contains("<br style=")){
//						int x = dimensioni.indexOf("<br style=");
//						int y = dimensioni.indexOf("left;")+8;
//						dimensioni = (dimensioni.substring(0,x)).concat(dimensioni.substring(y,dimensioni.length()));
//					}							
//				}
//			}
//			
//			if (current.contains("ambito di utilizzo:")) {
//				int from3 = current.indexOf("ambito di utilizzo:") + 24;
//				temp = current.substring(from3, current.length());
//				int to3 = temp.indexOf("</dt>");
//				temp = temp.substring(0, to3);
//				ambitoUtilizzo = temp.replace("<dt>", " ").replace("<br>", " ").replace("  "," ")
//						.replace("<br>", " ").replace("  "," ").replace("br></dd> ", " ").replace("  "," ").replace("</dd>"," ")
//						.replace("  "," ").replace("br>"," ").replace("  "," ").replace("<b>"," ").replace("  "," ")
//						.replace("</b>"," ").replace("  "," ").trim();
//			}
//			if (current.contains("descrizione:")) {
//				int from4 = current.indexOf("descrizione:") + 17;
//				temp = current.substring(from4, current.length());
//				int to4 = temp.indexOf("</dt>");
//				temp = temp.substring(0, to4);
//				descrizione = temp.replace("<dt class=\"lastrow\">", " ").replace("  "," ")
//						.replace("<br>", " ").replace("  "," ").replace("br></dd> ", " ").replace("  "," ").replace("</dd>"," ")
//						.replace("  "," ").replace("br>"," ").replace("  "," ").replace("<b>"," ").replace("  "," ")
//						.replace("</b>"," ").replace("  "," ").trim();
//				
//				descrizione = descrizione.replace("al seguente link:", "");
//				descrizione = descrizione.replace("<span style=\"font-weight: bold;\"></span>", "");
//				descrizione = descrizione.replace(" confezione regalo", "; confezione regalo");	
//				
//				
//				while (descrizione.contains("<a") || descrizione.contains("/a>")) {
//					
//						int a = descrizione.indexOf("<a");
//						String c = descrizione.substring(0, a);
//						String d = descrizione.substring(a, descrizione.length());
//						int b = d.indexOf("/a>") + 3;					
//						String e = d.substring(b, d.length());
//						descrizione = c.concat(e);
//						
//						descrizione = descrizione.replace("disponibili nel nostro store", ",");	
//				}
//			}
//			try {
//				if (current.contains("www.zeldabomboniere.it/immagini/")) {
//					int from5 = current.indexOf("www.zeldabomboniere.it/immagini/") + 32;
//					temp = current.substring(from5, current.length());
//					int to5 = temp.indexOf(".jpg") + 4;
//					if (to5 > 0) {
//						immagine1 = temp.substring(0, to5);
//						//System.out.println(immagine1);
//						// else System.out.println(temp.substring(0, 30));
//						temp_active = temp.substring(to5);
//						if (temp_active.contains("/"+immagine1.substring(0, 7))) {
//							int from6 = temp_active.indexOf("/"+immagine1.substring(0,7))+1;
//							int to6 = temp_active.indexOf(".jpg") + 4;
//							if (to6 > 0) {
//								immagine2 = temp_active.substring(from6, to6);
//								//System.out.println(immagine2);
//								
//								temp = temp_active.substring( to6 -1);
//								if (temp.contains("/"+immagine1.substring(0, 7))) {
//									int from7 = temp
//											.indexOf("/"+immagine1.substring(0, 7))+1;
//									int to7 = temp.indexOf(".jpg") + 4;
//									if (to7 > 0) {				
//										 immagine3 = temp.substring(from7,to7);
//										 //System.out.println(immagine3);
//	
//										 temp_active = temp.substring( to7 -1);
//										if (temp_active.contains("/"+immagine1.substring(0, 7))) {
//											int from8 = temp_active
//													.indexOf("/"+immagine1.substring(0, 7))+1;
//											int to8 = temp_active.indexOf(".jpg") + 4;
//											if (to8 > 0) {
//												immagine4 = temp_active.substring(from8,to8);
//												//System.out.println(immagine4);
//	
//												temp = temp_active.substring( to8 -1);
//												if (temp.contains("/"+immagine1.substring(0, 7))) {
//													int from9 = temp
//															.indexOf("/"+immagine1.substring(0, 7))+1;
//													int to9 = temp.indexOf(".jpg") + 4;
//													if (to9 > 0)
//														immagine5 = temp.substring(
//																from9, to9);
//													//System.out.println(immagine5);
//												}
//											}
//										}
//									}
//								}
//							}
//						}
//					} //System.out.println();
//				}
//			}
//			catch (StringIndexOutOfBoundsException e) {
//				e.printStackTrace();
//				System.out.println();
//			}
//		}
//		// int from = current.indexOf("descrizione:");
//		// int to = current.indexOf("fourcards");
////		temp = current.substring(from, to);
////		int from1 = temp.indexOf("lastrow")+9;
////		int to1 = temp.indexOf("</dt>");
////		System.out.println("Descrizione: "+temp.substring(from1, to1).replace("<br>", "").toLowerCase());
////		
////		int from2 = current.indexOf("www.zeldabomboniere.it/immagini/") + 32;
////		int to2 = current.substring(from2).indexOf(".jpg")+4;
////		temp = current.substring(from2, from2+to2);
////		int to3 = temp.indexOf(".jpg") + 4;
////		System.out.println("Percorso immagine: "+ temp.substring(0, to3));
//		
//		art.setQuantitaInserzione(quantitaInserzione);
//		art.setDimensioni(dimensioni);
//		art.setAmbitoUtilizzo(ambitoUtilizzo);
//		art.setDescrizioneArticolo(descrizione);
//		
//		if (immagine1!=null) immagine1=immagine1.toUpperCase();
//		if (immagine2!=null) immagine2=immagine2.toUpperCase();
//		if (immagine3!=null) immagine3=immagine3.toUpperCase();
//		if (immagine4!=null) immagine4=immagine4.toUpperCase();
//		if (immagine5!=null) immagine5=immagine5.toUpperCase();
//		
//		art.setNomeImmagine1(immagine1);
//		art.setNomeImmagine2(immagine2);
//		art.setNomeImmagine3(immagine3);
//		art.setNomeImmagine4(immagine4);
//		art.setNomeImmagine5(immagine5);
//		
//		return art;	
//	}

}
