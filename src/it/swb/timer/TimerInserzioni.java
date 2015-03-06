package it.swb.timer;

import it.swb.business.ArticoloBusiness;
import it.swb.utility.DateMethods;

import java.util.Date;
import java.util.TimerTask;

public class TimerInserzioni extends TimerTask{
	
	public TimerInserzioni(){}

	public void run(){

		System.out.println("Pubblicazione automatica delle inserzioni sulle piattaforme, orario: " + getData());
		
		ArticoloBusiness.getInstance().elaboraCodaInserzioni();
		
	}

	private String getData(){
		Date d = new Date();
		return DateMethods.formattaData2(d);
	}

}
