package it.swb.model;

import java.io.Serializable;

public class InfoEbay  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String titoloInserzione;
	double prezzo;
	String idCategoria1;
	String idCategoria2;
	String nomeCategoria1;
	String nomeCategoria2;
	long idCategoriaNegozio1;
	long idCategoriaNegozio2;
	int durataInserzione;
	boolean contrassegno;
	String descrizioneEbay;
	String ambiente;
	String idOggetto;
	boolean boxBomboniere;
	boolean boxSecco;
	
	public InfoEbay(){
		super();
		durataInserzione = 999;
		boxBomboniere = false;
		boxSecco = false;
		ambiente = "produzione";
		contrassegno = true;
		idCategoria1 = "121";
		idCategoria2 = "10034";
	}
	
	public String getTitoloInserzione() {
		return titoloInserzione;
	}
	public void setTitoloInserzione(String titoloInserzione) {
		this.titoloInserzione = titoloInserzione;
	}
	public String getIdCategoria1() {
		return idCategoria1;
	}
	public void setIdCategoria1(String idCategoria1) {
		this.idCategoria1 = idCategoria1;
	}
	public String getIdCategoria2() {
		return idCategoria2;
	}
	public void setIdCategoria2(String idCategoria2) {
		this.idCategoria2 = idCategoria2;
	}
	public int getDurataInserzione() {
		return durataInserzione;
	}
	public void setDurataInserzione(int durataInserzione) {
		this.durataInserzione = durataInserzione;
	}
	public boolean isContrassegno() {
		return contrassegno;
	}
	public void setContrassegno(boolean contrassegno) {
		this.contrassegno = contrassegno;
	}
	public String getDescrizioneEbay() {
		return descrizioneEbay;
	}
	public void setDescrizioneEbay(String descrizioneEbay) {
		this.descrizioneEbay = descrizioneEbay;
	}
	public String getAmbiente() {
		return ambiente;
	}
	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}

	public double getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(double prezzo) {
		this.prezzo = prezzo;
	}

	public String getNomeCategoria1() {
		return nomeCategoria1;
	}

	public void setNomeCategoria1(String nomeCategoria1) {
		this.nomeCategoria1 = nomeCategoria1;
	}

	public String getNomeCategoria2() {
		return nomeCategoria2;
	}

	public void setNomeCategoria2(String nomeCategoria2) {
		this.nomeCategoria2 = nomeCategoria2;
	}

	public boolean isBoxBomboniere() {
		return boxBomboniere;
	}

	public void setBoxBomboniere(boolean boxBomboniere) {
		this.boxBomboniere = boxBomboniere;
	}

	public boolean isBoxSecco() {
		return boxSecco;
	}

	public void setBoxSecco(boolean boxSecco) {
		this.boxSecco = boxSecco;
	}

	public long getIdCategoriaNegozio1() {
		return idCategoriaNegozio1;
	}

	public void setIdCategoriaNegozio1(long idCategoriaNegozio1) {
		this.idCategoriaNegozio1 = idCategoriaNegozio1;
	}

	public long getIdCategoriaNegozio2() {
		return idCategoriaNegozio2;
	}

	public void setIdCategoriaNegozio2(long idCategoriaNegozio2) {
		this.idCategoriaNegozio2 = idCategoriaNegozio2;
	}

	public String getIdOggetto() {
		return idOggetto;
	}

	public void setIdOggetto(String idOggetto) {
		this.idOggetto = idOggetto;
	}
	
	

}
