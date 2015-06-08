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
import it.swb.model.ArticoloAcquistato;
import it.swb.model.Indirizzo;
import it.swb.model.Ordine;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import au.com.bytecode.opencsv.CSVReader;




public class Methods {
	
	public static String cercaLinkTracciamento(int idCorriere){
		String link = "";
		if (idCorriere==1) link = Costanti.linkTrackingCorriere1;
		else if (idCorriere==2) link = Costanti.linkTrackingCorriere2;
		return link;
	}
	
	public static String cercaNomeCorriere(int idCorriere){
		String nome = "";
		if (idCorriere==1) nome = "SDA";
		else if (idCorriere==2) nome = "GLS";
		return nome;
	}
	
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
		if (s.length()>=9)
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
	
	public static String stampaOrdine(Ordine o){
		String s = "";
		
		System.out.println("Piattaforma ---> "+o.getPiattaforma());
		System.out.println("ID ordine piat. ---> "+o.getIdOrdinePiattaforma());
		System.out.println("Email ---> "+o.getEmail());
		System.out.println("Stato ---> "+o.getStato());
		System.out.println("Totale ---> "+o.getTotale());
		System.out.println("Met. Pagamento ---> "+o.getMetodoPagamento());
		System.out.println("Data Acquisto ---> "+o.getDataAcquisto());
		System.out.println("Data Ult. Mod. ---> "+o.getDataUltimaModifica());
		
		Indirizzo i = o.getIndirizzoSpedizione();
		
		System.out.println("Telefono ---> "+i.getTelefono());
		System.out.println("Indirizzo 1 ---> "+i.getIndirizzo1());
		System.out.println("Indirizzo 2 ---> "+i.getIndirizzo2());
		System.out.println("Località ---> "+i.getComune());
		System.out.println("Cap ---> "+i.getCap());
		System.out.println("Provincia ---> "+i.getProvincia());
		System.out.println("Nazione ---> "+i.getNazione());
		System.out.println("ELENCO ARTICOLI");
		
		List<ArticoloAcquistato> articoli = o.getElencoArticoli();
		
		if (articoli!=null && !articoli.isEmpty()){
			for (ArticoloAcquistato a : articoli){
				System.out.println(a.getCodice()+" - €"+a.getPrezzoUnitario()+" - "+a.getQuantitaAcquistata()+" pezzi - tot €"+a.getPrezzoTotale()+" - "+a.getNome()+" - "+a.getIdOrdinePiattaforma());
			}
		}
		
		System.out.println();
		
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
	
	public static String checkProvincia(String s){
		
		s = s.toLowerCase();
		
		if (s.equals("agrigento")) s = "AG";
		if (s.equals("alessandria")) s = "AL";
		if (s.equals("ancona")) s = "AN";
		if (s.equals("aosta")) s = "AO";
		if (s.equals("l'aquila")) s = "AQ";
		if (s.equals("arezzo")) s = "AR";
		if (s.equals("ascoli-piceno") || s.equals("ascoli piceno")) s = "AP";
		if (s.equals("asti")) s = "AT";
		if (s.equals("avellino")) s = "AV";
		if (s.equals("bari")) s = "BA";
		if (s.equals("barletta-andria-trani") || s.equals("barletta andria trani")) s = "BT";
		if (s.equals("belluno")) s = "BL";
		if (s.equals("benevento")) s = "BN";
		if (s.equals("bergamo")) s = "BG";
		if (s.equals("biella")) s = "BI";
		if (s.equals("bologna")) s = "BO";
		if (s.equals("bolzano")) s = "BZ";
		if (s.equals("brescia")) s = "BS";
		if (s.equals("brindisi")) s = "BR";
		if (s.equals("cagliari")) s = "CA";
		if (s.equals("caltanissetta")) s = "CL";
		if (s.equals("campobasso")) s = "CB";
		if (s.equals("carbonia iglesias")) s = "CI";
		if (s.equals("caserta")) s = "CE";
		if (s.equals("catania")) s = "CT";
		if (s.equals("catanzaro")) s = "CZ";
		if (s.equals("chieti")) s = "CH";
		if (s.equals("como")) s = "CO";
		if (s.equals("cosenza")) s = "CS";
		if (s.equals("cremona")) s = "CR";
		if (s.equals("crotone")) s = "KR";
		if (s.equals("cuneo")) s = "CN";
		if (s.equals("enna")) s = "EN";
		if (s.equals("fermo")) s = "FM";
		if (s.equals("ferrara")) s = "FE";
		if (s.equals("firenze")) s = "FI";
		if (s.equals("foggia")) s = "FG";
		if (s.equals("forli-cesena") || s.equals("forli cesena")) s = "FC";
		if (s.equals("frosinone")) s = "FR";
		if (s.equals("genova")) s = "GE";
		if (s.equals("gorizia")) s = "GO";
		if (s.equals("grosseto")) s = "GR";
		if (s.equals("imperia")) s = "IM";
		if (s.equals("isernia")) s = "IS";
		if (s.equals("la-spezia") || s.equals("la spezia")) s = "SP";
		if (s.equals("latina")) s = "LT";
		if (s.equals("lecce")) s = "LE";
		if (s.equals("lecco")) s = "LC";
		if (s.equals("livorno")) s = "LI";
		if (s.equals("lodi")) s = "LO";
		if (s.equals("lucca")) s = "LU";
		if (s.equals("macerata")) s = "MC";
		if (s.equals("mantova")) s = "MN";
		if (s.equals("massa-carrara") || s.equals("massa carrara")) s = "MS";
		if (s.equals("matera")) s = "MT";
		if (s.equals("medio campidano")) s = "VS";
		if (s.equals("messina")) s = "ME";
		if (s.equals("milano")) s = "MI";
		if (s.equals("modena")) s = "MO";
		if (s.equals("monza-brianza") || s.equals("monza brianza")) s = "MB";
		if (s.equals("napoli")) s = "NA";
		if (s.equals("novara")) s = "NO";
		if (s.equals("nuoro")) s = "NU";
		if (s.equals("ogliastra")) s = "OG";
		if (s.equals("olbia tempio")) s = "OT";
		if (s.equals("oristano")) s = "OR";
		if (s.equals("padova")) s = "PD";
		if (s.equals("palermo")) s = "PA";
		if (s.equals("parma")) s = "PR";
		if (s.equals("pavia")) s = "PV";
		if (s.equals("perugia")) s = "PG";
		if (s.equals("pesaro-urbino") || s.equals("pesaro urbino") || s.equals("pesaro e urbino")) s = "PU";
		if (s.equals("pescara")) s = "PE";
		if (s.equals("piacenza")) s = "PC";
		if (s.equals("pisa")) s = "PI";
		if (s.equals("pistoia")) s = "PT";
		if (s.equals("pordenone")) s = "PN";
		if (s.equals("potenza")) s = "PZ";
		if (s.equals("prato")) s = "PO";
		if (s.equals("ragusa")) s = "RG";
		if (s.equals("ravenna")) s = "RA";
		if (s.equals("reggio-calabria") || s.equals("reggio calabria")) s = "RC";
		if (s.equals("reggio-emilia") || s.equals("reggio emilia") || s.equals("reggio nell'emilia")) s = "RE";
		if (s.equals("rieti")) s = "RI";
		if (s.equals("rimini")) s = "RN";
		if (s.equals("roma")) s = "RM";
		if (s.equals("rovigo")) s = "RO";
		if (s.equals("salerno")) s = "SA";
		if (s.equals("sassari")) s = "SS";
		if (s.equals("savona")) s = "SV";
		if (s.equals("siena")) s = "SI";
		if (s.equals("siracusa")) s = "SR";
		if (s.equals("sondrio")) s = "SO";
		if (s.equals("taranto")) s = "TA";
		if (s.equals("teramo")) s = "TE";
		if (s.equals("terni")) s = "TR";
		if (s.equals("torino")) s = "TO";
		if (s.equals("trapani")) s = "TP";
		if (s.equals("trento")) s = "TN";
		if (s.equals("treviso")) s = "TV";
		if (s.equals("trieste")) s = "TS";
		if (s.equals("udine")) s = "UD";
		if (s.equals("varese")) s = "VA";
		if (s.equals("venezia")) s = "VE";
		if (s.equals("verbania")) s = "VB";
		if (s.equals("vercelli")) s = "VC";
		if (s.equals("verona")) s = "VR";
		if (s.equals("vibo-valentia") || s.equals("vibo valentia")) s = "VV";
		if (s.equals("vicenza")) s = "VI";
		if (s.equals("viterbo")) s = "VT";
		
		return s;
	}

}
