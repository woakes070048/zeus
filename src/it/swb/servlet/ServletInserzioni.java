package it.swb.servlet;

import it.swb.log.Log;
import it.swb.timer.TimerInserzioni;
import it.swb.utility.DateMethods;

import java.util.Date;
import java.util.Timer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class ServletInserzioni extends HttpServlet {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServletInserzioni() {
        super();
    }
    
    public void init(ServletConfig config) throws ServletException {
		super.init(config);
    
		try{
			
			long interval = Long.parseLong(config.getInitParameter("interval"))  * 60 * 1000;  //1 minuto moltiplicato il valore che c'è in web.xml
			
			TimerInserzioni taskInserzioni = new TimerInserzioni();
			Timer timer = new Timer();
			
			Date dataInizio = new Date();
			
			dataInizio = DateMethods.setDataConOra(dataInizio, DateMethods.getOra(dataInizio)+1, 15);	
			
			Log.info("*********");
			Log.info("*********");
			Log.info("********* La pubblicazione automatica delle inserzioni è abilitata ");
			Log.info("********* si ripeterà ogni: "+interval/60/1000+" minuti (calcolati a partire dalle "+DateMethods.getOra(dataInizio)+")");
			Log.info("********* data e ora del primo avvio: "+dataInizio);
			Log.info("*********");
			Log.info("*********");
			
			timer.schedule(taskInserzioni, dataInizio, interval);
			
		} catch(Exception e){
			e.printStackTrace();
			Log.info(e);
		}
		
    }

}
