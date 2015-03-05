package it.swb.ftp;

import it.swb.log.Log;
import it.swb.utility.Costanti;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;



public class FTPmethods {

	public static void main(String[] args) throws IOException{
		
		rinominaFileECartelleZB();
		
	}
	
	public static boolean caricaImmagineSuFtp(String nome_file, String sorgente, String destinazione, FTPClient f1) {
		FTPClient f = null;
		boolean risultato = true;
		try {
			if (f1!=null) f = f1;
			else f = FTPutil.getConnection();
			
			boolean caricato = false;
			
			boolean cambioCartella = f.changeWorkingDirectory(destinazione);		
			
			if (cambioCartella){
				Log.debug("Provo a caricare su ftp: "+ nome_file +" nella cartella "+ f.printWorkingDirectory());
				
				File file = new File(sorgente);
				
				if (file.exists() && file.isFile()){
					FileInputStream in = new FileInputStream(file);
					f.setFileType(FTP.BINARY_FILE_TYPE);
					f.enterLocalPassiveMode();
			          
					caricato = f.storeFile(nome_file,in);
					if (caricato) Log.debug("File caricato correttamente.");
					else {
						risultato = false;
						Log.debug("!!! File NON caricato.");
					}
				}
			}
			else {
				risultato = false;
				Log.error("Non è stato possibile entrare nella cartella FTP "+destinazione);
			}
			
			if (f1==null) FTPutil.closeConnection(f);
			
		} catch (SocketException e) {
			Log.info(e); e.printStackTrace();
			risultato = false;
		} catch (IOException e) {
			Log.info(e); e.printStackTrace();
			risultato = false;
		}
		return risultato;
	}
	
	public static boolean caricaImmaginiSuFtp(String nome_file, String estensione, String sorgente, String destinazione, List<String> misure,FTPClient f1) {
		FTPClient f = null;
		boolean risultato = true;
		
		try {
			if (f1!=null) f = f1;
			else f = FTPutil.getConnection();
			
			boolean caricato = false;
			
			boolean cambioCartella = f.changeWorkingDirectory(destinazione);		
			
			if (cambioCartella){
				Log.debug("Provo a caricare su ftp: "+ nome_file +" nella cartella "+ f.printWorkingDirectory());
				
				f.setFileType(FTP.BINARY_FILE_TYPE);
				f.enterLocalPassiveMode();
				
				for (String misura : misure){
					File file = new File(sorgente+"-"+misura+estensione);
					
					if (file.exists() && file.isFile()){
						FileInputStream in = new FileInputStream(file);
						
						caricato = f.storeFile(nome_file+"-"+misura+estensione,in);
						if (caricato) Log.debug(misura+" caricato correttamente.");
						else { Log.debug(misura+" non caricato.");
						}
					}
				}
				
			}
			else {
				risultato = false;
				Log.error("Non è stato possibile entrare nella cartella FTP "+destinazione);
			}
			
			if (f1==null) FTPutil.closeConnection(f);
			
		} catch (SocketException e) {
			Log.info(e); e.printStackTrace();
			risultato = false;
		} catch (IOException e) {
			Log.info(e); e.printStackTrace();
			risultato = false;
		}
		return risultato;
	}

	public static void uploadFile(String percorso_file, String nome_file) {
		FTPClient f = null;
		try {
			f = FTPutil.getConnection();
			
			f.changeWorkingDirectory("_thumbnails/medie/contenitori");
			
			//Log.debug(f.printWorkingDirectory());
			
			boolean caricato = false;
			Log.debug("Provo a caricare su FTP: "+ percorso_file+nome_file);
			
			File file = new File(percorso_file+nome_file);
			FileInputStream in = new FileInputStream(file);
			f.setFileType(FTP.BINARY_FILE_TYPE);
			f.enterLocalPassiveMode();
	          
			caricato = f.storeFile(nome_file,in);

			if (caricato) {
				Log.debug("File caricato correttamente.");
			}
			
			FTPutil.closeConnection(f);
			
		} catch (SocketException e) {
			Log.info(e); e.printStackTrace();
		} catch (IOException e) {
			Log.info(e); e.printStackTrace();
		}
	}
	
	
	public static boolean uploadThumbnailsNoLog(String cartella, String nome_file, FTPClient f1) {
		FTPClient f = null;
		boolean risultato = true;
		try {
			if (f1!=null) f = f1;
			else f = FTPutil.getConnection();
			
			String percorso_file_media = Costanti.percorsoImmaginiMedieLocale+cartella+"\\media_"+nome_file;	
			String percorso_file_piccola = Costanti.percorsoImmaginiPiccoleLocale+cartella+"\\piccola_"+nome_file;
			
			boolean caricato = false;
			
			boolean cambioCartella = f.changeWorkingDirectory(Costanti.cartellaFtpImmaginiPiccole+cartella);		
			
			if (cambioCartella){
				//Log.debug("Provo a caricare su ftp: "+ percorso_file_piccola+" nella cartella "+ f.printWorkingDirectory());
				
				File file = new File(percorso_file_piccola);
				FileInputStream in = new FileInputStream(file);
				f.setFileType(FTP.BINARY_FILE_TYPE);
				f.enterLocalPassiveMode();
		          
				caricato = f.storeFile("piccola_"+nome_file,in);
				if (caricato) {
					//Log.debug("File caricato correttamente.");
				}
				else {
					risultato = false;
					Log.debug("!!! File NON caricato.");
				}
			}
			else {
				risultato = false;
				Log.error("Non è stato possibile entrare nella cartella FTP "+Costanti.cartellaFtpImmaginiPiccole+cartella);
			}
			
			cambioCartella = f.changeWorkingDirectory(Costanti.cartellaFtpImmaginiMedie+cartella);		
			caricato = false;
			
			if (cambioCartella){
				//Log.debug("Provo a caricare su ftp: "+ percorso_file_media+" nella cartella"+ f.printWorkingDirectory());
				
				File file = new File(percorso_file_media);
				FileInputStream in = new FileInputStream(file);
				f.setFileType(FTP.BINARY_FILE_TYPE);
				f.enterLocalPassiveMode();
		          
				caricato = f.storeFile("media_"+nome_file,in);
				if (caricato) {
					//Log.debug("File caricato correttamente.");
				}
				else {
					risultato = false;
					Log.debug("!!! File NON caricato.");
				}
			}
			else {
				risultato = false;
				Log.error("Non è stato possibile entrare nella cartella FTP "+Costanti.cartellaFtpImmaginiMedie+cartella);
			}
			
			if (f1==null) FTPutil.closeConnection(f);
			
		} catch (SocketException e) {
			Log.info(e); e.printStackTrace();
			risultato = false;
		} catch (IOException e) {
			Log.info(e); e.printStackTrace();
			risultato = false;
		}
		return risultato;
	}
	
	
	
//	public static List<String> getFilesInSubdirectories() {
//		FTPClient f = new FTPClient();
//		List<String> files = new ArrayList<String>();
//		try {
//			Log.debug("Provo a connettermi...");
//
//			f = FTPutil.getConnection();
//			
//			List<String> directories = getDirectoriesList(null, f);
//			List<String> subdirectories = null;
//
//			for (String dir : directories) {
//				subdirectories = getDirectoriesList(dir,f);
//
//				for (String subdir : subdirectories) {
//					int id_cartella_principale = DBmethods.getIdCartellaFromName(dir);
//					DBmethods.insertOrUpdateCartelleFtp(subdir, id_cartella_principale, dir);
//					
////					List<String> filesInSubdir = getDirectoryFilesList(subdir,f);
////
////					for (String file : filesInSubdir) {
////						files.add(dir + "/" + subdir + "/" + file);
////					}
//				}
//
//			}
//			
//			f.disconnect();
//			
//		} catch (SocketException e) {
//			Log.info(e); e.printStackTrace();
//		} catch (IOException e) {
//			Log.info(e); e.printStackTrace();
//		}
//		return files;
//	}
	
	
	public static void downloadFileByName(String nome, FTPClient f) {
		try {
			boolean scaricato = false;

			Log.debug("Cerco di scaricare: " + nome);
			File file = new File("D:\\JavaDownload\\" + nome);
			if (file.exists()) {
				Log.debug("Il file esiste gia', chiudo...");
				System.exit(1);
			}
			FileOutputStream dfile = new FileOutputStream(file);

			scaricato = f.retrieveFile(nome, dfile);
			if (scaricato) {
				Log.debug("File scaricato correttamente.");
			}
		} catch (SocketException e) {
			Log.info(e); e.printStackTrace();
		} catch (IOException e) {
			Log.info(e); e.printStackTrace();
		}
	}
	
	
	public static boolean downloadFileByName(String nomefile, String cartella, String destinazione, FTPClient f1) {
		FTPClient f = null;
		boolean download = false;
		try {
			if (f1!=null) f = f1;
			else f = FTPutil.getConnection();
			
			boolean scaricato = false;

			//System.out.print("Cerco di scaricare: " + cartella +"/"+ nomefile+" ... ");
//			if (!(new File(destinazione + "\\" + nomefile)).exists()){		//se il file esiste già non lo scarica
			
			File cart = new File(destinazione);
			if (!cart.isDirectory())
				cart.mkdir();
				
			File file = new File(destinazione + "\\" + nomefile);
			
			FileOutputStream dfile = new FileOutputStream(file);
			
			boolean entrato = false;
			
			//System.out.println(f.printWorkingDirectory());
						
			entrato = f.changeWorkingDirectory(Costanti.cartellaFtpImmagini+cartella);		
			
			if (entrato){
				//scaricato = f.retrieveFile(nomefile, dfile);
				
				f.setFileType(FTP.BINARY_FILE_TYPE);
			    f.enterLocalPassiveMode();
			    f.setAutodetectUTF8(true);

			    //Create an InputStream to the File Data and use FileOutputStream to write it
			    InputStream inputStream = f.retrieveFileStream(nomefile);
			    //Using org.apache.commons.io.IOUtils
			    IOUtils.copy(inputStream, dfile);
			    dfile.flush();
			    IOUtils.closeQuietly(dfile);
			    IOUtils.closeQuietly(inputStream);
			    scaricato = f.completePendingCommand();
				
				if (scaricato) {
					Log.debug("Immagine "+nomefile+" scaricata correttamente da FTP.");
					download = true;
				} else Log.error("!!! Non è stato possibile scaricare "+nomefile+" da FTP.");
			} else Log.error("!!! Non è stato possibile accedere alla cartella FTP: "+cartella);			

			//f.changeToParentDirectory();
			
			if (f1==null) FTPutil.closeConnection(f);
			
		} catch (SocketException e) {
			Log.info(e); e.printStackTrace();
		} catch (IOException e) {
			Log.info(e); e.printStackTrace();
		}
		return download;
	}
	
	
	public static void downloadFilesFromList(List<String> nomi) {
		FTPClient f = new FTPClient();
		
		try {
			Log.debug("Provo a connettermi...");

			f = FTPutil.getConnection();


			f.changeWorkingDirectory("/zeldabomboniere.it/immagini/FARE");

			FTPFile[] files = f.listFiles();
			Log.debug("Nella cartella "+f.printWorkingDirectory()+" ci sono "+files.length+" file.");
			
			List<String> nomis = new ArrayList<String>();
			nomis.add("BOF08.jpg");
			
			boolean scaricato = false;
			int fileScaricati = 0;
			
			for (FTPFile x : files){
				if (nomis.contains(x.getName())) {
					
					Log.debug("Cerco di scaricare: " + x.getName());
					File file = new File("D:\\JavaDownload\\" + x.getName());
					if (file.exists()) {
						Log.debug("Il file esiste gia', chiudo...");
						System.exit(1);
					}
					FileOutputStream dfile = new FileOutputStream(file);

					scaricato = f.retrieveFile(x.getName(), dfile);
					if (scaricato){
						Log.debug("File scaricato correttamente.");
						fileScaricati++;
					}
				}
			}
			
			Log.debug("Totale file scaricati: "+fileScaricati);
			Log.debug("Disconnessione...");

			f.disconnect();

		} catch (SocketException e) {
			Log.info(e); e.printStackTrace();
		} catch (IOException e) {
			Log.info(e); e.printStackTrace();
		}
	}
	
	
	public static void createDirectories(List<String> directories, String dir, FTPClient f){
		try{
			f.changeWorkingDirectory("/zeldabomboniere.it/immagini_thumbnails/"+dir);
			Log.debug("Creo le cartelle in: "+"zeldabomboniere.it/immagini_thumbnails/"+dir);
			
			for (String s : directories)
			{
				Log.debug(s);
			}		
		} catch (SocketException e) {
			Log.info(e); e.printStackTrace();
		} catch (IOException e) {
			Log.info(e); e.printStackTrace();
		}
	}
	
	
	
	public static List<String> getDirectoriesList(String dir, FTPClient f){
		List<String> directories = null;
		   
		    try {	    	
				//se dir è passato come parametro, il metodo ritorna l'elenco delle sottodirectory della directory dir.
				//altrimenti ritorna l'elenco delle directory principali
				if(dir!=null && !dir.equals("")) {
					f.changeWorkingDirectory("/");	
					f.changeWorkingDirectory("/zeldabomboniere.it/immagini/"+dir);	
				}
							
				else 
					f.changeWorkingDirectory("/zeldabomboniere.it/immagini");											
				
//				FTPFile[] filess = f.listFiles();
//				List<String> list = new ArrayList<String>();
//				
//				for (int i=0;i<filess.length;i++)
//				{
//					if (filess[i].getType()==1)
//						list.add(filess[i].getName());
//				}
//				FTPFile[] files = (FTPFile[])list.toArray();
				
				FTPFile[] files = f.listDirectories();
				
				Log.debug("dir: "+dir+", num. of subdir: "+(files.length-2));
				
				directories = new ArrayList<String>();
				
				for (FTPFile x : files){
					if (!x.getName().equals("thumb") && !x.getName().equals("medium"))
						directories.add(x.getName());
				}
				
				directories.remove(0);
				directories.remove(0);
			} catch (SocketException e) { Log.info(e); e.printStackTrace();
			} catch (IOException e) { Log.info(e); e.printStackTrace(); }
		    return directories;
	}
	

	
	
	public static List<String> getDirectoryFilesListByCodice(String directory, List<String> codici){
		List<String> names = null;
		FTPClient f = new FTPClient();
		   
		    try {
		    	Log.debug("Provo a connettermi...");
		    	
				f.connect("ftp.zeldabomboniere.it");
				f.login("2029242@aruba.it", "moraldi0");
				
				Log.debug("Connessione riuscita!!");
				Log.debug(f.getSystemType());
				
				f.changeWorkingDirectory("/zeldabomboniere.it/immagini/"+directory);											
				
				FTPFile[] files = f.listFiles();
				//Log.debug(files.length);
				
				names = new ArrayList<String>();
				
				for (FTPFile x : files){
					if (x.getType()==0 && codici.contains(x.getName().toUpperCase().substring(0,2)))
						names.add(x.getName());
				}
								
				f.disconnect();
				
			} catch (SocketException e) { Log.info(e); e.printStackTrace();
			} catch (IOException e) { Log.info(e); e.printStackTrace(); }
		    return names;
	}
	
/*	
	public static List<String> getDirectoryFilesList(String directory, FTPClient f){
		List<String> names = null;
		   
		    try {
		    	boolean close = false;
		    	
		    	if (f==null){
		    		f = FTPutil.getConnection();
		    	}
				
				f.changeWorkingDirectory("/zeldabomboniere.it/immagini/"+directory);											
				
				FTPFile[] files = f.listFiles();
				//Log.debug(files.length);
				
				names = new ArrayList<String>();
				
				for (FTPFile x : files){
					if (x.getType()==0 && x.getName().toLowerCase().contains(".jpg"))
						names.add(x.getName());
				}
								
				if (close) f.disconnect();
				
			} catch (SocketException e) { Log.info(e); e.printStackTrace();
			} catch (IOException e) { Log.info(e); e.printStackTrace(); }
		    return names;
	}
*/	
	
	public static void rinominaCartelle(){
		
		FTPClient f = null;
		    try {	    
		    	
		    	f = FTPutil.getConnection();
				
				FTPFile[] files = f.listDirectories();
				
				Log.debug("num. of dir: "+(files.length-2));
				
				//List<String> directories = new ArrayList<String>();
				
				for (FTPFile x : files){
					System.out.println(x.getName());
				//	if (!x.getName().equals("thumb") && !x.getName().equals("medium"))
				//		directories.add(x.getName());
				}
				
				f.disconnect();
				
			} catch (SocketException e) { Log.info(e); e.printStackTrace();
			} catch (IOException e) { Log.info(e); e.printStackTrace(); }
		    
	}
	
	public static void rinominaFile(){
		
		FTPClient f = null;
		    try {	    
		    	
		    	f = FTPutil.getConnection();
		    	
		    	f.changeWorkingDirectory("/");
		    	f.changeWorkingDirectory("www.zeldafiori.it/immagini/_thumbnails/piccole");
				
				FTPFile[] files = f.listFiles();
				
				Log.debug("num. of dir: "+(files.length-2));
				
				//List<String> directories = new ArrayList<String>();
				
				for (FTPFile x : files){
					if (!x.getName().equals(".") 
							&& !x.getName().equals("..") 
							&& !x.getName().equals("_thumbnails") 
							&&  !x.getName().equals("_template_ebay")
							&&  !x.getName().equals("accessori")){
						if (x.getType()!=0){
							f.rename(x.getName(), x.getName().toLowerCase());
							
							System.out.println(x.getName());
							
							f.changeWorkingDirectory("/");
							f.changeWorkingDirectory("www.zeldafiori.it/immagini/_thumbnails/medie/"+x.getName());
							
							FTPFile[] filesSub = f.listFiles();
							
							for (FTPFile y : filesSub){
								f.rename(y.getName(), y.getName().toLowerCase());
								System.out.println(y.getName());
							}
						}
						else	f.rename(x.getName(), x.getName().toLowerCase());
					}	
						
				}
			
				f.disconnect();
				
			} catch (SocketException e) { Log.info(e); e.printStackTrace();
			} catch (IOException e) { Log.info(e); e.printStackTrace(); }
		    
	}
	
	
	public static void rinominaSottoCartelle(){
		
		FTPClient f = null;
		    try {	    
		    	
		    	f = FTPutil.getConnection();
		    	
		    	f.changeWorkingDirectory("/");
		    	f.changeWorkingDirectory("www.zeldafiori.it/immagini");
				
				FTPFile[] files = f.listFiles();
				
				Log.debug("num. of dir: "+(files.length-2));
				
				//List<String> directories = new ArrayList<String>();
				
				for (FTPFile x : files){
					if (x.isDirectory()
						&& !x.getName().equals(".") 
						&& !x.getName().equals("..") 
						&& !x.getName().equals("_thumbnails") 
						&& !x.getName().equals("_template_ebay")){
					
						f.changeWorkingDirectory("/");
						f.changeWorkingDirectory("www.zeldafiori.it/immagini/"+x.getName());
						
						System.out.println("Cartella Principale: "+x.getName());
						
						FTPFile[] filesSub = f.listFiles();
						
						for (FTPFile y : filesSub){
							
							if (y.isDirectory()
								&& !y.getName().equals(".") 
								&& !y.getName().equals("..") ){
								
								f.changeWorkingDirectory(y.getName());
								
								System.out.println("Sotto Cartella: "+y.getName());
							
								FTPFile[] filesSubSub = f.listFiles();
								
								for (FTPFile z : filesSubSub){
									f.rename(z.getName(), z.getName().toLowerCase());
									System.out.println(z.getName());
								}
							}
						}
						
					}	
						
				}
			
				f.disconnect();
				
			} catch (SocketException e) { Log.info(e); e.printStackTrace();
			} catch (IOException e) { Log.info(e); e.printStackTrace(); }
		    
	}
	
	public static void rinominaFileECartelleZB(){
		
		FTPClient f = null;
		    try {	    
		    	
		    	f = FTPutil.getConnection();
		    	FTPutil.closeConnection(f);
		    	
		    	f = FTPutil.getConnection();
		    	
		    	String path = "/public_html/images/articoli/caramelle/";
		    	//
		    	f.changeWorkingDirectory(path);
		    	
				FTPFile[] files = f.listFiles();
				
				Log.debug("num. of dir: "+(files.length-2));
				
				//List<String> directories = new ArrayList<String>();
				
				for (FTPFile x : files){
					
					f.rename(x.getName(), x.getName().toLowerCase());
					System.out.println(x.getName());
					
//					if (x.isDirectory()
//						&& !x.getName().equals(".") 
//						&& !x.getName().equals("..") 
//						&& !x.getName().equals("_thumbnails") 
//						&& !x.getName().equals("_template_ebay")){
//						
//							//System.out.println("Sono nella cartella "+f.printWorkingDirectory());
//							System.out.println("Entro nella Cartella: "+x.getName());
//							
//							f.changeWorkingDirectory(path+x.getName());	
//							
//							System.out.println("Ora sono nella cartella: "+f.printWorkingDirectory());
//							
//							FTPFile[] filesSub = f.listFiles();
//							
//							for (FTPFile y : filesSub){
//								f.rename(y.getName(), y.getName().toLowerCase());
//								System.out.println(" --> "+y.getName());
//								
//								if (y.isDirectory()
//									&& !y.getName().equals(".") 
//									&& !y.getName().equals("..") ){
//									
//									System.out.println("Entro nella sotto Cartella: "+y.getName());
//									
//									f.changeWorkingDirectory(path+x.getName()+"/"+y.getName());
//									
//									System.out.println("Ora sono nella cartella: "+f.printWorkingDirectory());
//									
//								
//									FTPFile[] filesSubSub = f.listFiles();
//									
//									for (FTPFile z : filesSubSub){
//										f.rename(z.getName(), z.getName().toLowerCase());
//										System.out.println(" ------> "+z.getName());
//									}
//							}
//						}
//					}
				}
			
				f.disconnect();
				
			} catch (SocketException e) { Log.info(e); e.printStackTrace();
			} catch (IOException e) { Log.info(e); e.printStackTrace(); }
		    
	}
	
}
