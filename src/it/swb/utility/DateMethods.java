package it.swb.utility;

import it.swb.log.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateMethods {
	
	public static Date trasformaDataAmazon(String s){
		s = s.replace("T", " ");
		s = s.replace("Z", "");
		
		return creaDataDaStringa1(s);
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
	
	public static Date creaData(int anno, int mese, int giorno, int ora, int minuti){
		mese = mese-1; //gennaio è il mese 0
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, anno);
		cal.set(Calendar.MONTH, mese);
		cal.set(Calendar.DAY_OF_MONTH, giorno);
		cal.set(Calendar.HOUR_OF_DAY, ora);
		cal.set(Calendar.MINUTE, minuti);
		cal.set(Calendar.SECOND, 00);
		return cal.getTime();
	}
	
	public static Date creaDataConOra(Date date, int ora, int minuti) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, ora);
		cal.set(Calendar.MINUTE, minuti);
		cal.set(Calendar.SECOND, 00);
		return cal.getTime();
	}
	
	public static String getDataCompletaPerNomeFileTesto(){
		String st = null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
		st = df.format(new Date());
		
		return "("+st+")";
	}
	
	/** Get back a new date in the format yyyy-MM-dd */
	public static String getDataPerNomeFileTesto(){
		String st = null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		st = df.format(new Date());
		
		return st;
	}
	
	/** Get back a new date in the format yyyy-MM */
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
	
	/** Restituisce una data a partire da una stringa nel formato yyyy-MM-dd HH:mm:ss */
	public static Date creaDataDaStringa1(String s){
		Date d = null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if (s!=null){
			try {
				d = df.parse(s);
			} catch (ParseException e) {
				e.printStackTrace();
				Log.info(e);
			}
		}
		return d;
	}
	
	public static Date sottraiGiorniAData(Date data, int giorni){				
		Calendar cal = new GregorianCalendar();
		cal.setTime(data);
		cal.add(Calendar.DAY_OF_YEAR, -giorni);		
		Date dat = cal.getTime();	
		return dat;
	}
	
	public static Date sottraiOreAData(Date data, int ore){				
		Calendar cal = new GregorianCalendar();
		cal.setTime(data);
		cal.add(Calendar.HOUR_OF_DAY, -ore);		
		cal.add(Calendar.MINUTE, -10);		
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
	
	/** Set the hour of the input date to 00:00:00 and get it back */
	public static Date oraDelleStreghe(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}
	
	/** Set the hour of the input date to 23:59:59 and get it back */
	public static Date ventitreCinquantanove(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
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
}
