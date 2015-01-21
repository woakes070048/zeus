package it.swb.business;

import it.swb.database.DbTool;
import it.swb.database.LogArticolo_DAO;
import it.swb.model.LogArticolo;

import java.util.List;
import java.util.Map;

public class LogBusiness {
	
    private static LogBusiness instance = new LogBusiness();
    
    /* Costruttore privato della classe */
    private LogBusiness() {}

    /* Metodo che permette di ottenere l'istanza della classe */
    public static LogBusiness getInstance() {
        return instance;
    }
    

    private List<LogArticolo> logArticoli;
    private Map<String,List<LogArticolo>> mappaLogArticoli;

	public List<LogArticolo> getLogArticoli() {
		if (logArticoli==null)
			logArticoli = LogArticolo_DAO.getLogArticoli();
		return logArticoli;
	}
	
	public Map<String,List<LogArticolo>> getMappaLogArticoli() {
		if (mappaLogArticoli==null)
			mappaLogArticoli = LogArticolo_DAO.getMappaLogArticoli(null);
		return mappaLogArticoli;
	}
	
	public Map<String,List<LogArticolo>> getMappaLogArticoli(DbTool dbt) {
		if (mappaLogArticoli==null)
			mappaLogArticoli = LogArticolo_DAO.getMappaLogArticoli(dbt);
		return mappaLogArticoli;
	}
	
	
	public List<LogArticolo> reloadLogArticoli(){
		logArticoli = null;
		return getLogArticoli();
	}
	
	
	public Map<String,List<LogArticolo>> reloadMappaLogArticoli(){
		mappaLogArticoli = null;
		return getMappaLogArticoli();
	}
	
	public Map<String,List<LogArticolo>> reloadMappaLogArticoli(DbTool dbt){
		mappaLogArticoli = null;
		return getMappaLogArticoli(dbt);
	}
	
}
