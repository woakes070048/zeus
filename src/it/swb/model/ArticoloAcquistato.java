package it.swb.model;

import java.io.Serializable;

public class ArticoloAcquistato implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	long idArticolo;
	String codice;
	String nome;
	String titoloInserzione;
	String asin;
	String piattaforma;
	String idOrdinePiattaforma;
	String idInserzione;
	String idTransazione;
	String variante;
	int quantitaAcquistata;
	double prezzoUnitario;
	double prezzoTotale;
	double iva;
	double tasse;
	double costoSpedizione;
	double note;
	String thumbnail;

	public ArticoloAcquistato(){}
	
	public long getIdArticolo() {
		return idArticolo;
	}
	public void setIdArticolo(long idArticolo) {
		this.idArticolo = idArticolo;
	}
	public String getCodice() {
		return codice;
	}
	public void setCodice(String codice) {
		this.codice = codice;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public int getQuantitaAcquistata() {
		return quantitaAcquistata; 
	}
	public void setQuantitaAcquistata(int quantitaAcquistata) {
		this.quantitaAcquistata = quantitaAcquistata;
	}
	public String getAsin() {
		return asin;
	}
	public void setAsin(String asin) {
		this.asin = asin;
	}
	public double getTasse() {
		return tasse;
	}
	public void setTasse(double tasse) {
		this.tasse = tasse;
	}
	public double getCostoSpedizione() {
		return costoSpedizione;
	}
	public void setCostoSpedizione(double costoSpedizione) {
		this.costoSpedizione = costoSpedizione;
	}
	public double getNote() {
		return note;
	}
	public void setNote(double note) {
		this.note = note;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getPiattaforma() {
		return piattaforma;
	}

	public void setPiattaforma(String piattaforma) {
		this.piattaforma = piattaforma;
	}

	public String getIdOrdinePiattaforma() {
		return idOrdinePiattaforma;
	}

	public void setIdOrdinePiattaforma(String idOrdinePiattaforma) {
		this.idOrdinePiattaforma = idOrdinePiattaforma;
	}

	public String getIdInserzione() {
		return idInserzione;
	}

	public void setIdInserzione(String idInserzione) {
		this.idInserzione = idInserzione;
	}

	public String getIdTransazione() {
		return idTransazione;
	}

	public void setIdTransazione(String idTransazione) {
		this.idTransazione = idTransazione;
	}

	public String getVariante() {
		return variante;
	}

	public void setVariante(String variante) {
		this.variante = variante;
	}

	public double getPrezzoUnitario() {
		return prezzoUnitario;
	}

	public void setPrezzoUnitario(double prezzoUnitario) {
		this.prezzoUnitario = prezzoUnitario;
	}

	public double getPrezzoTotale() {
		return prezzoTotale;
	}

	public void setPrezzoTotale(double prezzoTotale) {
		this.prezzoTotale = prezzoTotale;
	}

	public double getIva() {
		return iva;
	}

	public void setIva(double iva) {
		this.iva = iva;
	}

	public String getTitoloInserzione() {
		return titoloInserzione;
	}

	public void setTitoloInserzione(String titoloInserzione) {
		this.titoloInserzione = titoloInserzione;
	}

}
