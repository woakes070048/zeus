package it.swb.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import it.swb.business.LogBusiness;
import it.swb.model.LogArticolo;

@ManagedBean(name = "logBean")
@ViewScoped
public class LogBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    private List<LogArticolo> logArticoli;  
    
    private List<LogArticolo> logArticoliFiltrati;  
    
    private LogArticolo logArticoloSelezionato;
    
	public List<LogArticolo> getLogArticoli() {
		if (logArticoli==null)
			logArticoli = LogBusiness.getInstance().getLogArticoli();
		return logArticoli;
	}
	
	public void reloadLogArticoli() {
			logArticoli = LogBusiness.getInstance().reloadLogArticoli();
	}

	public void setLogArticoli(List<LogArticolo> logArticoli) {
		this.logArticoli = logArticoli;
	}

	public List<LogArticolo> getLogArticoliFiltrati() {
		return logArticoliFiltrati;
	}

	public void setLogArticoliFiltrati(List<LogArticolo> logArticoliFiltrati) {
		this.logArticoliFiltrati = logArticoliFiltrati;
	}

	public LogArticolo getLogArticoloSelezionato() {
		return logArticoloSelezionato;
	}

	public void setLogArticoloSelezionato(LogArticolo logArticoloSelezionato) {
		this.logArticoloSelezionato = logArticoloSelezionato;
	}
    

  }
