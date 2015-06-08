package it.swb.servlet;

import it.swb.log.Log;
import it.swb.timer.TimerOrdini;
import it.swb.utility.DateMethods;

import java.util.Date;
import java.util.Timer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class ServletOrdini extends HttpServlet {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServletOrdini() {
        super();
    }
    
    public void init(ServletConfig config) throws ServletException {
		super.init(config);
    
		try{
			
			long interval = Long.parseLong(config.getInitParameter("interval"))  * 60 * 1000;  //1 minuto moltiplicato il valore che c'è in web.xml
			
			TimerOrdini taskOrdini = new TimerOrdini();
			Timer timer = new Timer();
			
			Date dataInizio = new Date();
			
			dataInizio = DateMethods.creaDataConOra(dataInizio, DateMethods.getOra(dataInizio)+1, 10);	
			
			Log.info("*********");
			Log.info("*********");
			Log.info("********* il download automatico degli ordini è abilitato ");
			Log.info("********* si ripeterà ogni: "+interval/60/1000+" minuti (calcolati a partire dalle "+DateMethods.getOra(dataInizio)+")");
			Log.info("********* data e ora del primo avvio: "+dataInizio);
			Log.info("*********");
			Log.info("*********");
			
			timer.schedule(taskOrdini, dataInizio, interval);
			
		} catch(Exception e){
			e.printStackTrace();
			Log.info(e);
		}
		
    }

}
