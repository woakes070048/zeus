package it.swb.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import it.swb.database.Fornitore_DAO;
import it.swb.log.Log;
import it.swb.model.Fornitore;

@ManagedBean(name = "fornitoreBean")
@RequestScoped
public class FornitoreBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    private List<Fornitore> fornitori;  
    
    private List<Fornitore> fornitoriFiltrati;  
    
    private Fornitore fornitoreSelezionato;
    
    private Fornitore[] fornitoriSelezionati;

	public List<Fornitore> getFornitori() {
		Log.debug("Visualizzazione fornitori");
		if (fornitori==null)
			fornitori = Fornitore_DAO.getFornitori();
		return fornitori;
	}

	public void setFornitori(List<Fornitore> fornitori) {
		this.fornitori = fornitori;
	}

	public List<Fornitore> getFornitoriFiltrati() {
		return fornitoriFiltrati;
	}

	public void setFornitoriFiltrati(List<Fornitore> fornitoriFiltrati) {
		this.fornitoriFiltrati = fornitoriFiltrati;
	}

	public Fornitore getFornitoreSelezionato() {
		return fornitoreSelezionato;
	}

	public void setFornitoreSelezionato(Fornitore fornitoreSelezionato) {
		this.fornitoreSelezionato = fornitoreSelezionato;
	}

	public Fornitore[] getFornitoriSelezionati() {
		return fornitoriSelezionati;
	}

	public void setFornitoriSelezionati(Fornitore[] fornitoriSelezionati) {
		this.fornitoriSelezionati = fornitoriSelezionati;
	}
    

  }
