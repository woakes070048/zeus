package it.swb.model;

import java.io.Serializable;

public class Filtro  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String codiceArticolo;
	String codiceBarre;
	String nomeArticolo;
	int idCategoria;
	boolean completa;
	String ordinamento1;
	String ordinamento2;
	String direzioneOrdinamento1;
	String direzioneOrdinamento2;
	
	public Filtro(){
		super();
		setCodiceArticolo("");
		setCodiceBarre("");
		setNomeArticolo("");
		setIdCategoria(-1);
		setCompleta(true);
		setOrdinamento1("DATA_ULTIMA_MODIFICA");
		setOrdinamento2("DATA_INSERIMENTO");
		setDirezioneOrdinamento1("DESC");
		setDirezioneOrdinamento2("DESC");
	}


	public String getCodiceArticolo() {
		return codiceArticolo;
	}


	public void setCodiceArticolo(String codiceArticolo) {
		this.codiceArticolo = codiceArticolo;
	}


	public String getCodiceBarre() {
		return codiceBarre;
	}


	public void setCodiceBarre(String codiceBarre) {
		this.codiceBarre = codiceBarre;
	}


	public String getNomeArticolo() {
		return nomeArticolo;
	}


	public void setNomeArticolo(String nomeArticolo) {
		this.nomeArticolo = nomeArticolo;
	}


	public int getIdCategoria() {
		return idCategoria;
	}


	public void setIdCategoria(int idCategoria) {
		this.idCategoria = idCategoria;
	}


	public boolean isCompleta() {
		return completa;
	}


	public void setCompleta(boolean completa) {
		this.completa = completa;
	}


	public String getOrdinamento1() {
		return ordinamento1;
	}


	public void setOrdinamento1(String ordinamento1) {
		this.ordinamento1 = ordinamento1;
	}


	public String getOrdinamento2() {
		return ordinamento2;
	}


	public void setOrdinamento2(String ordinamento2) {
		this.ordinamento2 = ordinamento2;
	}


	public String getDirezioneOrdinamento1() {
		return direzioneOrdinamento1;
	}


	public void setDirezioneOrdinamento1(String direzioneOrdinamento1) {
		this.direzioneOrdinamento1 = direzioneOrdinamento1;
	}


	public String getDirezioneOrdinamento2() {
		return direzioneOrdinamento2;
	}


	public void setDirezioneOrdinamento2(String direzioneOrdinamento2) {
		this.direzioneOrdinamento2 = direzioneOrdinamento2;
	}


	
	
	
}