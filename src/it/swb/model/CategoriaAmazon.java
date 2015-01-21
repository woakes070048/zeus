package it.swb.model;

import java.io.Serializable;

public class CategoriaAmazon implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	long idCategoria;
	String nomeCategoria;
	
	public CategoriaAmazon(long idCategoria, String nomeCategoria) {
		super();
		this.idCategoria = idCategoria;
		this.nomeCategoria = nomeCategoria;
	}

	public CategoriaAmazon() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(long idCategoria) {
		this.idCategoria = idCategoria;
	}

	public String getNomeCategoria() {
		return nomeCategoria;
	}

	public void setNomeCategoria(String nomeCategoria) {
		this.nomeCategoria = nomeCategoria;
	}
	
	

}
