package it.swb.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Log {
	
	boolean debug_abilitato;
	String percorso_file_log;
	String percorso_file_log_debug;
	String data;
	
	public Log(){
		 Properties config = new Properties();	        
	     try {
			config.load(Log.class.getResourceAsStream("/zeus.properties"));
			
			this.debug_abilitato = Boolean.getBoolean(config.getProperty("debug_abilitato"));
			this.percorso_file_log = config.getProperty("percorso_file_log");
			this.percorso_file_log_debug = config.getProperty("percorso_file_log_debug");
			
			String pattern = config.getProperty("pattern_data");
			
			DateFormat df = new SimpleDateFormat(pattern);
			this.data = df.format(new Date())+" : ";
	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void info(String s){
		System.out.println(s);
		FileOutputStream fos;
		try {
			Log l = new Log();
			
			File f = new File(l.getPercorso());
			if (f.exists()) {
				if ((f.length()/1024)>1024){
					rinomina(l.getPercorso());
				}
			}
			
			fos = new FileOutputStream (l.getPercorso(), true);
			
			PrintWriter pw = new PrintWriter (fos);
					
			//pw.println();
			pw.println(l.getData()+s);
			
			pw.close();
			
			debug(s,false);			
		} catch (FileNotFoundException f) {
			f.printStackTrace();
		} 
	}
	
	public static void info(Exception e){
		FileOutputStream fos;
		try {
			Log l = new Log();
			
			File f = new File(l.getPercorso());
			if (f.exists()) {
				if ((f.length()/1024)>1024){
					rinomina(l.getPercorso());
				}
			}
			
			fos = new FileOutputStream (l.getPercorso(), true);
	
			PrintWriter pw = new PrintWriter (fos);
			
			pw.println();
			pw.println();
			pw.println();
			pw.println();
			pw.println("ECCEZIONE");
			pw.println(l.getData()+e.toString());
			pw.println();
			
			for (StackTraceElement s : e.getStackTrace()){
				pw.println(s.toString());
			}
			
			pw.close();
			
			debug(e);	
		} catch (FileNotFoundException f) {
			f.printStackTrace();
		} 
	}
	
	public static void debug(String s, boolean syso){
		if (syso) System.out.println(s);
		FileOutputStream fos;
		try {
			Log l = new Log();
			
			File f = new File(l.getPercorsoDebug());
			if (f.exists()) {
				if ((f.length()/1024)>1024){
					rinomina(l.getPercorsoDebug());
				}
			}
			
			fos = new FileOutputStream (l.getPercorsoDebug(), true);
			
			PrintWriter pw = new PrintWriter (fos);
					
			//pw.println();
			pw.println(l.getData()+s);
			
			pw.close();
			
		} catch (FileNotFoundException f) {
			f.printStackTrace();
		} 
	}
	
	public static void debug(String s){
		System.out.println(s);
		FileOutputStream fos;
		try {
			Log l = new Log();
			
			File f = new File(l.getPercorsoDebug());
			if (f.exists()) {
				if ((f.length()/1024)>1024){
					rinomina(l.getPercorsoDebug());
				}
			}
			
			fos = new FileOutputStream (l.getPercorsoDebug(), true);
			
			PrintWriter pw = new PrintWriter (fos);
					
			//pw.println();
			pw.println(l.getData()+s);
			
			pw.close();
			
		} catch (FileNotFoundException f) {
			f.printStackTrace();
		} 
	}
	
	public static void debug(Exception e){
		FileOutputStream fos;
		try {
			Log l = new Log();
			
			File f = new File(l.getPercorsoDebug());
			if (f.exists()) {
				if ((f.length()/1024)>1024){
					rinomina(l.getPercorsoDebug());
				}
			}
			
			fos = new FileOutputStream (l.getPercorsoDebug(), true);
	
			PrintWriter pw = new PrintWriter (fos);
			
			pw.println();
			pw.println();
			pw.println();
			pw.println();
			pw.println("ECCEZIONE");
			pw.println(l.getData()+e.toString());
			pw.println();
			
			for (StackTraceElement s : e.getStackTrace()){
				pw.println(s.toString());
			}
			
			pw.close();
			
		} catch (FileNotFoundException f) {
			f.printStackTrace();
		} 
	}
	
	public static void info(int i){
		info(String.valueOf(i));
	}
	
	public static void info(long l){
		info(String.valueOf(l));
	}
	
	public static void info(double d){
		info(String.valueOf(d));
	}
	
	public static void info(boolean b){
		info(String.valueOf(b));
	}
		
	public static void debug(int i){
		debug(String.valueOf(i));
	}
	
	public static void debug(long l){
		debug(String.valueOf(l));
	}
	
	public static void debug(double d){
		debug(String.valueOf(d));
	}
	
	public static void debug(boolean b){
		debug(String.valueOf(b));
	}
	
	public static void error(String s){
		info("ERRORE: "+s);
	}
	
	
//	private boolean getDebug(){
//		return this.debug_abilitato;
//	}
	
	private String getPercorso(){
		return this.percorso_file_log;
	}
	private String getPercorsoDebug(){
		return this.percorso_file_log_debug;
	}
	
	private String getData(){
		return this.data;
	}
	
	private static void rinomina(String percorso){
		File f10 = new File(percorso.replace(".log", "10.log"));
		if (f10.exists()) f10.delete();
		
		File f9 = new File(percorso.replace(".log", "9.log"));
		if (f9.exists()) f9.renameTo(new File(percorso.replace(".log", "10.log")));
		
		File f8 = new File(percorso.replace(".log", "8.log"));
		if (f8.exists()) f8.renameTo(new File(percorso.replace(".log", "9.log")));
		
		File f7 = new File(percorso.replace(".log", "7.log"));
		if (f7.exists()) f7.renameTo(new File(percorso.replace(".log", "8.log")));
		
		File f6 = new File(percorso.replace(".log", "6.log"));
		if (f6.exists()) f6.renameTo(new File(percorso.replace(".log", "7.log")));
		
		File f5 = new File(percorso.replace(".log", "5.log"));
		if (f5.exists()) f5.renameTo(new File(percorso.replace(".log", "6.log")));
		
		File f4 = new File(percorso.replace(".log", "4.log"));
		if (f4.exists()) f4.renameTo(new File(percorso.replace(".log", "5.log")));
		
		File f3 = new File(percorso.replace(".log", "3.log"));
		if (f3.exists()) f3.renameTo(new File(percorso.replace(".log", "4.log")));
		
		File f2 = new File(percorso.replace(".log", "2.log"));
		if (f2.exists()) f2.renameTo(new File(percorso.replace(".log", "3.log")));
		
		File f1 = new File(percorso.replace(".log", "1.log"));
		if (f1.exists()) f1.renameTo(new File(percorso.replace(".log", "2.log")));
		
		File f = new File(percorso);
		if (f.exists()) f.renameTo(new File(percorso.replace(".log", "1.log")));
	}

}
