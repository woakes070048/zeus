package it.swb.timer;

import it.swb.business.ArticoloBusiness;
import it.swb.utility.Methods;

import java.util.Date;
import java.util.TimerTask;

public class TimerArticoli extends TimerTask{
	
	public TimerArticoli(){}

	public void run(){

		System.out.println("Pubblicazione automatica delle inserzioni sulle piattaforme, orario: " + getData());
		
		//String s = ArticoloBusiness.getInstance().pubblicaInserzioniInAttesa();
		
		//System.out.println(s);

	}

	private String getData(){
		Date d = new Date();
		return Methods.formattaData2(d);
	}

}
