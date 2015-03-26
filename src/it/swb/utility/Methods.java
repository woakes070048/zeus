package it.swb.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import it.swb.images.ImageUtil;
import it.swb.log.Log;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import au.com.bytecode.opencsv.CSVReader;




public class Methods {
	
	public static void aggiungiAllaCodaDiStampa(String codice){
		
	}
	
	public static String cut500(String s){
		String str = s;
		if (s.length()>=499){
			str = s.substring(0,499);
		}
		return str;
	}
	
	public static String cut100(String s){
		String str = s;
		if (s.length()>=99){
			str = s.substring(0,99);
		}
		return str;
	}
	
	public static double veryRound(double value) {
		String s = String.valueOf(value);
		if (s.length()>=8)
			s = s.substring(0,9);
		value = Double.valueOf(s);
	    return round(value,2);
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	

	
	public static String escapeQuotes(String s){
		return s.replace("'", "\\'").trim();
	}
	
	public static String rimuoviApiciSlash(String s){
		s = s.replace("'", " ");
		s = s.replace("\\", " ");
		s = s.replace("/", " ");
		s = s.replace("%", " ");
		s = s.replace("_", " ");
		s = s.replace("(", " ");
		s = s.replace(")", " ");
		s = s.replace("   ", " ");
		s = s.replace("  ", " ");
		
		s = s.trim();
		
		return s;
	}
	
	public static boolean controlloSintassiImmagine(String s){
		if (s!=null && !s.trim().isEmpty() && s.contains("/") && s.toLowerCase().contains(".jpg") )
			return true;
		else return false;
	}
	
	public static String trimAndToLower(String s) {
		if (s!=null) return s.trim().toLowerCase();
		else return null;
	}
	
	public static void copyFile(String sorgente, String destinazione) throws IOException{
		Log.debug("Copio il file "+sorgente+" in "+destinazione);
		
		InputStream in = null;
		OutputStream out = null;
		
		String ext = "";
		if (destinazione.contains(".txt")) ext = ".txt";
		else if (destinazione.contains(".csv")) ext = ".csv";
		
			File source = new File(sorgente);
			File dest = new File(destinazione.replace(ext, "")+DateMethods.getDataCompletaPerNomeFileTesto()+ext);
			
			if (!dest.exists()) 
				dest.createNewFile();

			in = new FileInputStream(source);
			out = new FileOutputStream(dest);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
	}
	
	public static void copiaImmaginePerZeldaBomboniere(String nomeImmagine){
		try{
			String sorgente = Costanti.percorsoImmaginiLocale+nomeImmagine;
			String destinazione = Costanti.percorsoImmaginiZeldaBomboniere+nomeImmagine;
			
			Log.debug("Copio il file "+sorgente+" in "+destinazione);
			
			InputStream in = null;
			OutputStream out = null;
					
			File source = new File(sorgente);
			File dest = new File(destinazione);
			
			if (!dest.exists()) 
				dest.createNewFile();
	
			in = new FileInputStream(source);
			out = new FileOutputStream(dest);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		}
		catch (IOException e){
			e.printStackTrace();
			Log.error(e.getMessage());
		}
	}

	public static boolean delete(String path) {
		Log.debug("Elimino "+path);
		File resource = new File(path);
		
		if (resource.isDirectory()) {
			File[] childFiles = resource.listFiles();
			for (File child : childFiles) {
				delete(child.getPath());
			}
		}
		boolean eliminato = resource.delete();
		Log.debug("Eliminato: "+eliminato);
		
		return eliminato;
	}
	
	public static List<String[]> creaRigheXTabella(String s){
		String[] a = s.split("\n");	
		List<String[]> x = new ArrayList<String[]>();
		
		for (int i=0;i<a.length;i++){
			x.add(a[i].split("	"));			
		}
		return x;
	}
	
	public static List<String[]> leggiMcdAmazon(String path) throws IOException {
		List<String[]> list = null;
		File name = new File(path);
		if (name.isFile()) {
			BufferedReader input = new BufferedReader(new FileReader(name));
			//StringBuffer buffer = new StringBuffer();
			String text;
			list = new ArrayList<String[]>();
			int i = 0;
			
			while ((text = input.readLine()) != null){
				if (i>=2){
					String[] riga = text.split("	");	
					//System.out.println (riga[0]+" "+riga.length);
					if (riga.length<=54 || !riga[57].equals("child")){
						list.add(new String[]{riga[0],riga[6],riga[1]});
					}
				}
				i++;
				//buffer.append(text + "\n");
			}
			
			input.close();

			//System.out.println(buffer.toString());
		} return list;
	}
	
	public static List<String[]> leggiMcdYatego(String path) {
		List<String[]> list = null;
		Map<String,String[]> map = null;
		CSVReader reader;
		
		File name = new File(path);
		if (name.isFile()) {
			try {
				
				reader = new CSVReader(new FileReader(path), ';');
				
				String [] nextLine;
				
				map = new HashMap<String,String[]>();
				
				 while ((nextLine = reader.readNext()) != null) {
					 
					 String cod = nextLine[0];
					 if (!map.containsKey(cod) && !cod.isEmpty() && !cod.equals("foreign_id") && !cod.equals("variant_set_id"))
						 map.put(cod, new String[]{cod,""});
				}
				
				 list = new ArrayList<String[]>(map.values());
				 reader.close();

			} catch (Exception e) {
				Log.info(e);
				e.printStackTrace();
			}
		} return list;
	}
	
	
	
	public static String leggiFiles(String path) {
		File name = new File(path);
		String s = null;
		if (name.isFile()) {
			try {
				BufferedReader input = new BufferedReader(new FileReader(name));
				StringBuffer buffer = new StringBuffer();
				String text;
				while ((text = input.readLine()) != null)
					buffer.append(text + "\n");
				input.close();

				//System.out.println(buffer.toString());
				s = buffer.toString();
			} catch (Exception e) {
				Log.info(e);
				e.printStackTrace();
			}
		}
		return s;
	}
	
	public static void scriviSuReport(String s){
		//System.out.println(s);
		FileOutputStream fos;
		try {
			
			String path = "D:\\dev\\log\\report.txt";
			
			fos = new FileOutputStream (path, true);
			
			PrintWriter pw = new PrintWriter (fos);
					
			pw.println(s);
			
			pw.close();
			
		} catch (FileNotFoundException f) {
			f.printStackTrace();
		} 
	}
	
	public static void scriviSuReportSenzaAndareACapo(String s){
		//System.out.println(s);
		FileOutputStream fos;
		try {
			
			String path = "D:\\dev\\log\\report.txt";
			
			fos = new FileOutputStream (path, true);
			
			PrintWriter pw = new PrintWriter (fos);
					
			pw.print(s);
			
			pw.close();
			
		} catch (FileNotFoundException f) {
			f.printStackTrace();
		} 
	}

	
	public static void creaThumbPerZeldaBomboniere(String nomeImmagine){
		controllaSeCartellaEsiste(Costanti.percorsoImmaginiCacheZeldaBomboniere+nomeImmagine);
		
		String ext = getEstensione(nomeImmagine);
		
		copiaImmaginePerZeldaBomboniere(nomeImmagine);
		
		ImageUtil.creaThumb(47, 47, Costanti.percorsoImmaginiLocale+nomeImmagine,
				Costanti.percorsoImmaginiCacheZeldaBomboniere+nomeImmagine.toLowerCase().replace(ext, "")+"-47x47"+ext);
		ImageUtil.creaThumb(80, 80, Costanti.percorsoImmaginiLocale+nomeImmagine,
				Costanti.percorsoImmaginiCacheZeldaBomboniere+nomeImmagine.toLowerCase().replace(ext, "")+"-80x80"+ext);
		ImageUtil.creaThumb(140, 140, Costanti.percorsoImmaginiLocale+nomeImmagine,
				Costanti.percorsoImmaginiCacheZeldaBomboniere+nomeImmagine.toLowerCase().replace(ext, "")+"-140x140"+ext);
		ImageUtil.creaThumb(300, 300, Costanti.percorsoImmaginiLocale+nomeImmagine,
				Costanti.percorsoImmaginiCacheZeldaBomboniere+nomeImmagine.toLowerCase().replace(ext, "")+"-300x300"+ext);
		ImageUtil.creaThumb(700, 700, Costanti.percorsoImmaginiLocale+nomeImmagine,
				Costanti.percorsoImmaginiCacheZeldaBomboniere+nomeImmagine.toLowerCase().replace(ext, "")+"-700x700"+ext);	
	}
	
	public static void creaThumbPerGloriaMoraldi(String nomeImmagine){
		nomeImmagine = nomeImmagine.toLowerCase();
		nomeImmagine = nomeImmagine.replace("/", "\\");
		String percorso = "D:\\immagini\\cache\\articoli\\";
		controllaSeCartellaEsiste(percorso+nomeImmagine);
		
		String ext = getEstensione(nomeImmagine);
		//copiaImmaginePerZeldaBomboniere(nomeImmagine);
		
		ImageUtil.creaThumb(50, 50, Costanti.percorsoImmaginiLocale+nomeImmagine,percorso+nomeImmagine.toLowerCase().replace(ext, "")+"-50x50"+ext);
		//ImageUtil.creaThumb(80, 80, Costanti.percorsoImmaginiLocale+nomeImmagine,percorso+nomeImmagine.replace(".jpg", "")+"-80x80.jpg");
		//ImageUtil.creaThumb(140, 140, Costanti.percorsoImmaginiLocale+nomeImmagine,percorso+nomeImmagine.toUpperCase().replace(".JPG", "")+"-140x140.JPG");
		//ImageUtil.creaThumb(300, 300, Costanti.percorsoImmaginiLocale+nomeImmagine,percorso+nomeImmagine.toUpperCase().replace(".JPG", "")+"-300x300.JPG");
		//ImageUtil.creaThumb(700, 700, Costanti.percorsoImmaginiLocale+nomeImmagine,	percorso+nomeImmagine.toUpperCase().replace(".JPG", "")+"-700x700.JPG");	
	}
	
	public static String primeLettereMaiuscole(String parola){
		
		if (parola!=null && !parola.isEmpty()){
			parola = parola.toLowerCase();
			
			if (parola.contains("(")) parola = parola.replace("(", "- ");
			if (parola.contains(")")) parola = parola.replace(")", " -");
			
			StringBuffer result = new StringBuffer();
			String tmp = null;
			StringTokenizer stTk = null;
			int pos;
			//try{
			stTk = new StringTokenizer(parola, " ");
			
			try{
				while (stTk.hasMoreTokens()) {
					tmp = stTk.nextToken();
					result.append(tmp.replaceFirst(tmp.substring(0, 1),tmp.substring(0, 1).toUpperCase()));
					result.append(" ");
				}
				
				parola = result.toString();
				pos=parola.indexOf("'");
				if (pos>0) 
					parola = parola.substring(0,pos) + 
					parola.substring(pos,pos+2).toUpperCase() +
					parola.substring(pos+2);
			}
			catch(Exception e){
				Log.error("primeLettereMaiuscole, parola non valida: "+parola);
				Log.error(e.getMessage());			
				e.printStackTrace();
			}
		} else parola="";
		return parola;
	}
	
	public static String MaiuscolaDopoPunto(String parola){
		if (parola!=null){
			if (parola.contains(". ")) parola = parola.replace(". ", ".");
			
			if (parola.contains("(")) parola = parola.replace("(", "- ");
			
			if (parola.contains(")")) parola = parola.replace(")", " -");
			
			parola = parola.toLowerCase();
			StringBuffer result = new StringBuffer();
			String tmp = null;
			StringTokenizer stTk = null;
			int pos;
			//try{
			stTk = new StringTokenizer(parola, ".");
	
			while (stTk.hasMoreTokens()) {
				tmp = stTk.nextToken();
				result.append(tmp.replaceFirst(tmp.substring(0, 1),tmp.substring(0, 1).toUpperCase()));
				result.append(".");
			}
			
			parola = result.toString();
			pos=parola.indexOf("'");
			if (pos>0) 
				parola = parola.substring(0,pos) + 
				parola.substring(pos,pos+2).toUpperCase() +
				parola.substring(pos+2);
			
			if (parola.contains("."))
				parola = parola.replace(".", ". ");
		}
		return parola;
	}
	


	
	public static String[] dividiCartellaEImmagine(String s){
		String result[] = new String[2];
		try {
			int x = 0;
			String cartella = "";
			String nome = "";

			x = s.lastIndexOf("/");
			cartella = s.substring(0, x);
			nome = s.substring(x + 1, s.length());

			result[0] = cartella;
			result[1] = nome;
		} catch (StringIndexOutOfBoundsException e) {
			Log.error("dividiCartellaEImmagine("+s+"): Nome immagine inserito non valido. ");
			//e.printStackTrace();
		}
		return result;
	}
	
	/** Controlla se una cartella esiste, e se non esiste la crea */
	public static void controllaSeCartellaEsiste(String path){
		File f = new File (getNomeCartella(path));

		boolean esiste = f.isDirectory();
				
		if (!esiste){
			f.mkdirs();
		}
	}
	
	public static String getEstensione(String s){
		String estensione = "";
		try {
			int x = 0;
			x = s.lastIndexOf(".");
			estensione = s.substring(x, s.length());
		} catch (StringIndexOutOfBoundsException e) {
			Log.error("getEstensione("+s+"): Nome inserito non valido.");
			//e.printStackTrace();
		}
		return estensione;
	}
	
	public static String getNomeCartella(String s){
		String cartella = "";
		try {
			int x = 0;
			if (s.contains("/"))
				x = s.lastIndexOf("/");
			else if (s.contains("\\"))
				x = s.lastIndexOf("\\");
			cartella = s.substring(0, x);
		} catch (StringIndexOutOfBoundsException e) {
			Log.error("getNomeCartella("+s+"): Nome inserito non valido.");
			//e.printStackTrace();
		}
		return cartella;
	}
	
	
	public static String getNomeImmagine(String s){
		String immagine = "";
		try {
			int x = 0;
			x = s.lastIndexOf("/");
			immagine = s.substring(x + 1, s.length());
		} catch (StringIndexOutOfBoundsException e) {
			Log.error("getNomeImmagine("+s+"): Nome inserito non valido.");
		}
		return immagine;
	}
	
	
	

	
//	public static void creaMiniThumbnail(String nomefile, String cartella) {
//		FTPmethods.downloadFileByName(nomefile, cartella, Costanti.percorsoImmaginiLocale+cartella);
//		ImageUtil.creaMiniThumb(Costanti.percorsoImmaginiLocale+cartella+"\\"+nomefile,
//				"D:\\zeldabomboniere.it\\immagini\\_thumbnails\\mini\\"+cartella+"\\mini_"+nomefile);
//	}
	
	
	public static String creaAlias(String nomeArticolo){
		String alias = "";
		if (nomeArticolo!=null){
			alias = nomeArticolo.toLowerCase().trim();
		
			if (alias!=null && alias.contains(" "))
				alias = alias.replace(" ", "-");
			if (alias!=null && alias.contains("/"))
				alias = alias.replace("/", "_");
		}
		
		return alias;
	}
	
	public static String creaAliasRandom(String nomeArticolo){
		String alias = nomeArticolo.toLowerCase().trim();
		
		if (alias!=null && alias.contains(" "))
			alias = alias.replace(" ", "-");
		if (alias!=null && alias.contains("/"))
			alias = alias.replace("/", "_");
		
		Double d = Math.random()*1000;
		int x= d.shortValue();
		
		alias+="_"+x;
		
		return alias;
	}
	

	
	
	
	public static void addErrorMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary,  null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public static void addInfoMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

}
