package it.swb.model;

import java.io.Serializable;

public class Categoria  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	long idCategoria;
	String nomeCategoria;
	boolean isPrincipale;
	long idCategoriaPrincipale;
	String nomeCategoriaPrincipale;
	int ordinamento;
	String idCategoriaEbay;
	String idCategoriaYatego;
	String idCategoriaAmazon;
	int idCategoriaGestionale;
	int soloDettaglio;
	
	public Categoria() {
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

	public boolean isPrincipale() {
		return isPrincipale;
	}

	public void setPrincipale(boolean isPrincipale) {
		this.isPrincipale = isPrincipale;
	}

	public long getIdCategoriaPrincipale() {
		return idCategoriaPrincipale;
	}

	public void setIdCategoriaPrincipale(long idCategoriaPrincipale) {
		this.idCategoriaPrincipale = idCategoriaPrincipale;
	}

	public String getNomeCategoriaPrincipale() {
		return nomeCategoriaPrincipale;
	}

	public void setNomeCategoriaPrincipale(String nomeCategoriaPrincipale) {
		this.nomeCategoriaPrincipale = nomeCategoriaPrincipale;
	}

	public int getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(int ordinamento) {
		this.ordinamento = ordinamento;
	}

	public String getIdCategoriaEbay() {
		return idCategoriaEbay;
	}

	public void setIdCategoriaEbay(String idCategoriaEbay) {
		this.idCategoriaEbay = idCategoriaEbay;
	}

	public String getIdCategoriaYatego() {
		return idCategoriaYatego;
	}

	public void setIdCategoriaYatego(String idCategoriaYatego) {
		this.idCategoriaYatego = idCategoriaYatego;
	}

	public int getIdCategoriaGestionale() {
		return idCategoriaGestionale;
	}

	public void setIdCategoriaGestionale(int idCategoriaGestionale) {
		this.idCategoriaGestionale = idCategoriaGestionale;
	}

	public int getSoloDettaglio() {
		return soloDettaglio;
	}

	public void setSoloDettaglio(int soloDettaglio) {
		this.soloDettaglio = soloDettaglio;
	}

	public String getIdCategoriaAmazon() {
		return idCategoriaAmazon;
	}

	public void setIdCategoriaAmazon(String idCategoriaAmazon) {
		this.idCategoriaAmazon = idCategoriaAmazon;
	}


}
