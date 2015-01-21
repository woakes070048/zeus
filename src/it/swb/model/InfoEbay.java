package it.swb.model;

import java.io.Serializable;

public class InfoEbay  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String titoloInserzione;
	double prezzo;
	String idCategoriaEbay1;
	String idCategoriaEbay2;
	String nomeCategoriaEbay1;
	String nomeCategoriaEbay2;
	long idCategoriaNegozio1;
	long idCategoriaNegozio2;
	int durata_inserzione;
	boolean rimettiInVendita;
	boolean contrassegno;
	String descrizioneEbay;
	String ambiente;
	boolean boxBomboniere;
	boolean boxSecco;
	
	public InfoEbay(){
		super();
	}
	
	public String getTitoloInserzione() {
		return titoloInserzione;
	}
	public void setTitoloInserzione(String titoloInserzione) {
		this.titoloInserzione = titoloInserzione;
	}
	public String getIdCategoriaEbay1() {
		return idCategoriaEbay1;
	}
	public void setIdCategoriaEbay1(String idCategoriaEbay1) {
		this.idCategoriaEbay1 = idCategoriaEbay1;
	}
	public String getIdCategoriaEbay2() {
		return idCategoriaEbay2;
	}
	public void setIdCategoriaEbay2(String idCategoriaEbay2) {
		this.idCategoriaEbay2 = idCategoriaEbay2;
	}
	public int getDurata_inserzione() {
		return durata_inserzione;
	}
	public void setDurata_inserzione(int durata_inserzione) {
		this.durata_inserzione = durata_inserzione;
	}
	public boolean isRimettiInVendita() {
		return rimettiInVendita;
	}
	public void setRimettiInVendita(boolean rimettiInVendita) {
		this.rimettiInVendita = rimettiInVendita;
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

	public String getNomeCategoriaEbay1() {
		return nomeCategoriaEbay1;
	}

	public void setNomeCategoriaEbay1(String nomeCategoriaEbay1) {
		this.nomeCategoriaEbay1 = nomeCategoriaEbay1;
	}

	public String getNomeCategoriaEbay2() {
		return nomeCategoriaEbay2;
	}

	public void setNomeCategoriaEbay2(String nomeCategoriaEbay2) {
		this.nomeCategoriaEbay2 = nomeCategoriaEbay2;
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
	
	

}
