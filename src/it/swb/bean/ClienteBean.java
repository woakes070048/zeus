package it.swb.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import it.swb.business.ClienteBusiness;
import it.swb.model.Cliente;

@ManagedBean(name = "clienteBean")
@ViewScoped
public class ClienteBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<Cliente> clientiZelda;  
	private List<Cliente> clientiZeldaFiltrati;  
	private Cliente clienteZeldaSelezionato;
	
    private List<Cliente> clientiGm;  
    private List<Cliente> clientiGmFiltrati;  
    private Cliente clienteGmSelezionato;
    
	public List<Cliente> getClientiZelda() {
		if (clientiZelda==null)
			clientiZelda = ClienteBusiness.getInstance().getClientiZelda();
		return clientiZelda;
	}
    
	public List<Cliente> getClientiGm() {
		if (clientiGm==null)
			clientiGm = ClienteBusiness.getInstance().getClientiGloriaMoraldi();
		return clientiGm;
	}
	
	public void reloadClienti(){
		ClienteBusiness.getInstance().reloadClientiGloriaMoraldi();
		ClienteBusiness.getInstance().reloadClientiZelda();
		
		clientiGm = ClienteBusiness.getInstance().getClientiGloriaMoraldi();
		clientiZelda = ClienteBusiness.getInstance().getClientiZelda();
	}

	public void setClientiGm(List<Cliente> clientiGm) {
		this.clientiGm = clientiGm;
	}

	public List<Cliente> getClientiGmFiltrati() {
		return clientiGmFiltrati;
	}

	public void setClientiGmFiltrati(List<Cliente> clientiGmFiltrati) {
		this.clientiGmFiltrati = clientiGmFiltrati;
	}

	public Cliente getClienteGmSelezionato() {
		return clienteGmSelezionato;
	}

	public void setClienteGmSelezionato(Cliente clienteGmSelezionato) {
		this.clienteGmSelezionato = clienteGmSelezionato;
	}



	public void setClientiZelda(List<Cliente> clientiZelda) {
		this.clientiZelda = clientiZelda;
	}

	public List<Cliente> getClientiZeldaFiltrati() {
		return clientiZeldaFiltrati;
	}

	public void setClientiZeldaFiltrati(List<Cliente> clientiZeldaFiltrati) {
		this.clientiZeldaFiltrati = clientiZeldaFiltrati;
	}

	public Cliente getClienteZeldaSelezionato() {
		return clienteZeldaSelezionato;
	}

	public void setClienteZeldaSelezionato(Cliente clienteZeldaSelezionato) {
		this.clienteZeldaSelezionato = clienteZeldaSelezionato;
	}



  }
