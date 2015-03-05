package it.swb.servlet;

import it.swb.log.Log;
import it.swb.timer.TimerArticoli;
import it.swb.utility.Methods;

import java.util.Date;
import java.util.Timer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class ServletArticoli extends HttpServlet {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServletArticoli() {
        super();
    }
    
    public void init(ServletConfig config) throws ServletException {
		super.init(config);
    
		try{
			
			long interval = Long.parseLong(config.getInitParameter("interval"))  * 60 * 1000;  //1 minuto moltiplicato il valore che c'è in web.xml
			
			TimerArticoli taskArticoli = new TimerArticoli();
			Timer timer = new Timer();
			
			Date dataInizio = new Date();
			
			dataInizio = Methods.setDataConOra(dataInizio, Methods.getOra(dataInizio)+1, 00);	
			
			Log.info("*********");
			Log.info("*********");
			Log.info("********* La pubblicazione automatica delle inserzioni è abilitata ");
			Log.info("********* si ripeterà ogni: "+interval/60/1000+" minuti (calcolati a partire dalle "+Methods.getOra(dataInizio)+")");
			Log.info("********* data e ora del primo avvio: "+dataInizio);
			Log.info("*********");
			Log.info("*********");
			
			timer.schedule(taskArticoli, dataInizio, interval);
			
		} catch(Exception e){
			e.printStackTrace();
			Log.info(e);
		}
		
    }

}
