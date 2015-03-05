package it.swb.timer;

import it.swb.business.OrdineBusiness;
import it.swb.utility.DateMethods;

import java.util.Date;
import java.util.TimerTask;

public class TimerOrdini extends TimerTask{
	
	
	 private Date dataDa = DateMethods.sottraiGiorniAData(DateMethods.oraDelleStreghe(new Date()), 5);
	 private Date dataA = DateMethods.ventitreCinquantanove(new Date());

	public TimerOrdini(){}

	public void run(){

		System.out.println("Recupero automatico degli ordini da eBay e da ZeldaBomboniere.it " + getData());
		
		String s = OrdineBusiness.getInstance().downloadOrdini(dataDa, dataA);
		
		OrdineBusiness.getInstance().reloadOrdini(); 
		
		System.out.println(s);

	}

	private String getData(){
		Date d = new Date();
		return DateMethods.formattaData2(d);
	}

}
