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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import it.swb.ftp.FTPmethods;
import it.swb.images.ImageUtil;
import it.swb.log.Log;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.net.ftp.FTPClient;

import au.com.bytecode.opencsv.CSVReader;




public class Methods {
	
	public static void aggiungiAllaCodaDiStampa(String codice){
		
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
	
	public static int getOra(Date date){
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int ora = cal.get(Calendar.HOUR_OF_DAY);
		
		return ora;
	}
	
	public static boolean checkOra(Date date, int ora){
		boolean b = true;
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int oraData = cal.get(Calendar.HOUR_OF_DAY);
		
		if (oraData>ora) {
			b = false;
		}
		
		return b;
	}
	
	public static Date setDataConOra(Date date, int ora, int minuti) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, ora);
		cal.set(Calendar.MINUTE, minuti);
		cal.set(Calendar.SECOND, 00);
		return cal.getTime();
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
		if (s!=null && !s.trim().isEmpty() && s.contains("/") && (s.toLowerCase().contains(".jpg") || s.toLowerCase().contains(".png"))){
			s = s.toLowerCase().trim();
			return true;
		}
			
		else return false;
	}
	
	public static String toLower(String s) {
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
			File dest = new File(destinazione.replace(ext, "")+getDataCompletaPerNomeFileTesto()+ext);
			
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
	
	public static String getDataCompletaPerNomeFileTesto(){
		String st = null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
		st = df.format(new Date());
		
		return "("+st+")";
	}
	
	public static String getDataPerNomeFileTesto(){
		String st = null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		st = df.format(new Date());
		
		return st;
	}
	
	public static String getMesePerNomeFileTesto(){
		String st = null;
		DateFormat df = new SimpleDateFormat("yyyy-MM");
		st = df.format(new Date());
		
		return st;
	}
	
	/** Prende in ingresso un oggetto data e ne restituisce una stringa nel formato "yyyy-MM-dd" */
	public static String formattaData1(Date d){
		String st = null;
		if (d!=null){
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			st = df.format(d);
		}
		return st;
	}
	
	/** Prende in ingresso un oggetto data e ne restituisce una stringa nel formato "yyyy-MM-dd HH:mm:ss" */
	public static String formattaData2(Date d){
		String st = null;
		if (d!=null){
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			st = df.format(d);
		}
		return st;
	}
	
	/** Prende in ingresso un oggetto data e ne restituisce una stringa nel formato "dd/MM/yyyy HH:mm" */
	public static String formattaData3(Date d){
		String st = null;
		if (d!=null){
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			st = df.format(d);
		}
		return st;
	}
	
	public static Date sottraiGiorniAData(Date data, int quantiGiorniFa){				
		Calendar cal = new GregorianCalendar();
		cal.setTime(data);
		cal.add(Calendar.DAY_OF_YEAR, -quantiGiorniFa);		
		Date dat = cal.getTime();	
		return dat;
	}
	
	public static Date calcolaSettimanaPrecedente(Date data){				
		Calendar cal = new GregorianCalendar();
		cal.setTime(data);
		cal.add(Calendar.DAY_OF_YEAR, -7);		
		Date dat = cal.getTime();	
		return dat;
	}
	
	public static Date calcolaMesePrecedente(Date data){				
		Calendar cal = new GregorianCalendar();
		cal.setTime(data);
		cal.add(Calendar.DAY_OF_YEAR, -31);		
		Date ieri = cal.getTime();	
		return ieri;
	}
	
	public static Date oraDelleStreghe(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}
	
	public static Date ventitreCinquantanove(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
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
	
	
	
	/*** Prende in input il nome di un immagine (che deve essere presente nell'hdd locale),
	 *  crea le thumb di tutte le misure per i siti (se è la prima immagine) oppure solo delle misure necessarie, e carica le miniature su ftp  */
	public static boolean creaThumbnailsEcaricaSuFtp(String immagine, boolean tutteLeMisure, FTPClient f) {
		boolean ok = true;
		try{
			String nomefile = Methods.getNomeImmagine(immagine);	
			String cartella = Methods.getNomeCartella(immagine);
			String estensione = Methods.getEstensione(immagine);
			
			List<String> misure = new ArrayList<String>();
			misure.add("300x300");
			misure.add("700x700");
			misure.add("50x50");
			
			if (tutteLeMisure){
				misure.add("40x40");
				misure.add("80x80");
				misure.add("140x140");
			}
			
			if (nomefile!=null && !nomefile.trim().isEmpty() && cartella!=null && !cartella.trim().isEmpty()){
				
				FTPmethods.caricaImmagineSuFtp(nomefile,Costanti.percorsoImmaginiLocale+cartella+"\\"+nomefile,
						Costanti.cartellaFtpImmagini+cartella,f);
												
				if (tutteLeMisure){
					ImageUtil.creaThumb(150, 250, Costanti.percorsoImmaginiLocale+cartella+"\\"+nomefile,
							Costanti.percorsoImmaginiPiccoleLocale+cartella+"\\piccola_"+nomefile);
					FTPmethods.caricaImmagineSuFtp("piccola_"+nomefile,Costanti.percorsoImmaginiPiccoleLocale+cartella+"\\"+"piccola_"+nomefile,
							Costanti.cartellaFtpImmaginiPiccole+cartella,f);
				}
				
				nomefile = nomefile.toLowerCase().replace(estensione, "");
				
				for (String misura : misure){
					int altezza = Integer.valueOf(misura.split("x")[0]);
					int larghezza = Integer.valueOf(misura.split("x")[1]);
					
					ImageUtil.creaThumb(altezza, larghezza, Costanti.percorsoImmaginiLocale+cartella+"\\"+nomefile+estensione,
							Costanti.percorsoImmaginiThumbLocale+cartella+"\\"+nomefile+"-"+misura+estensione);
				}
					
				FTPmethods.caricaImmaginiSuFtp(nomefile,estensione,Costanti.percorsoImmaginiThumbLocale+cartella+"\\"+nomefile,Costanti.cartellaFtpImmaginiCache+cartella,misure,f);
				
			}
			else ok = false;
		}
		catch(Exception e){
			e.printStackTrace();
			Log.info(e);
			ok = false;
		}
		return ok;
	}
	
	

	public static boolean creaThumbnailsNew(String nomefile, String cartella, FTPClient f) {
		boolean ok = true;
		boolean esiste = true;
		
		if (nomefile!=null && !nomefile.trim().isEmpty() && cartella!=null && cartella.length()!=0){
			
			File file = new File(Costanti.percorsoImmaginiLocale+cartella+"\\"+nomefile);
			
			if (!file.exists()){
				esiste = FTPmethods.downloadFileByName(nomefile, cartella, Costanti.percorsoImmaginiLocale+cartella, f);
			}
			
			if (esiste){
				ImageUtil.creaThumbMedia(Costanti.percorsoImmaginiLocale+cartella+"\\"+nomefile,
						Costanti.percorsoImmaginiMedieLocale+cartella+"\\media_"+nomefile,true);
				
				ImageUtil.creaThumbPiccola(Costanti.percorsoImmaginiLocale+cartella+"\\"+nomefile,
						Costanti.percorsoImmaginiPiccoleLocale+cartella+"\\piccola_"+nomefile,true);
				
				ok = FTPmethods.uploadThumbnailsNoLog(cartella,nomefile, f);
			}
			else ok = false;
		}
		else ok = false;
		
		return ok;
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
	
	/** Modifica una data "yyyy-MM-dd" in "dd/MM/yyyy" */
	public static String cambiaFormatoData(String data){
		String anno = data.substring(0,4);
		String mese = data.substring(5,7);
		String giorno = data.substring(8,10);
		
		return anno+"/"+mese+"/"+giorno;		
	}
	
	public static String modificaDate(String data){
		String s = new java.sql.Date(new java.util.Date().getTime()).toString();
		try {
			s = data.substring(0, 4)+ "-" + data.substring(4, 6) + "-" + data.substring(6, 8);
			
			java.sql.Date.valueOf(s);
		}
		catch (Exception e){
			e.printStackTrace();
			Log.error("Data non valida: "+data);
			Log.error(e.getMessage());
			s = new java.sql.Date(new java.util.Date().getTime()).toString();
		}
		//System.out.println(data);
		//return data.substring(6, 10)+ "-" + data.substring(3, 5) + "-" + data.substring(0, 2);
		//System.out.println(data.substring(0, 3)+ "-" + data.substring(4, 5) + "-" + data.substring(6, 7));
		return s;		
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
