package it.swb.model;

import java.io.Serializable;

public class InfoAmazon implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String puntoElenco1;
	String puntoElenco2;
	String puntoElenco3;
	String puntoElenco4;
	String puntoElenco5;
	String esclusioneResponsabilita;
	String descrizioneGaranziaVenditore;
	String avvertenzeSicurezza;
	String notaCondizioni;
	int vocePacchettoQuantita;
	int numeroPezzi;
	int tempiEsecuzioneSpedizione;
	int quantitaMassimaSpedizioneCumulativa;
	String paeseOrigine;
	double lunghezzaArticolo;
	double altezzaArticolo;
	double pesoArticolo;
	String unitaMisuraPesoArticolo;
	long idCategoria1;
	long idCategoria2;
	String nomeCategoria1;
	String nomeCategoria2;
		
	public InfoAmazon() {
		super();
		vocePacchettoQuantita = 1;
		numeroPezzi = 1;
		quantitaMassimaSpedizioneCumulativa = 100;
	}
	
	public String getPuntoElenco1() {
		return puntoElenco1;
	}
	public void setPuntoElenco1(String puntoElenco1) {
		this.puntoElenco1 = puntoElenco1;
	}
	public String getPuntoElenco2() {
		return puntoElenco2;
	}
	public void setPuntoElenco2(String puntoElenco2) {
		this.puntoElenco2 = puntoElenco2;
	}
	public String getPuntoElenco3() {
		return puntoElenco3;
	}
	public void setPuntoElenco3(String puntoElenco3) {
		this.puntoElenco3 = puntoElenco3;
	}
	public String getPuntoElenco4() {
		return puntoElenco4;
	}
	public void setPuntoElenco4(String puntoElenco4) {
		this.puntoElenco4 = puntoElenco4;
	}
	public String getPuntoElenco5() {
		return puntoElenco5;
	}
	public void setPuntoElenco5(String puntoElenco5) {
		this.puntoElenco5 = puntoElenco5;
	}
	public String getEsclusioneResponsabilita() {
		return esclusioneResponsabilita;
	}
	public void setEsclusioneResponsabilita(String esclusioneResponsabilita) {
		this.esclusioneResponsabilita = esclusioneResponsabilita;
	}
	public String getDescrizioneGaranziaVenditore() {
		return descrizioneGaranziaVenditore;
	}
	public void setDescrizioneGaranziaVenditore(String descrizioneGaranziaVenditore) {
		this.descrizioneGaranziaVenditore = descrizioneGaranziaVenditore;
	}
	public String getAvvertenzeSicurezza() {
		return avvertenzeSicurezza;
	}
	public void setAvvertenzeSicurezza(String avvertenzeSicurezza) {
		this.avvertenzeSicurezza = avvertenzeSicurezza;
	}
	public String getNotaCondizioni() {
		return notaCondizioni;
	}
	public void setNotaCondizioni(String notaCondizioni) {
		this.notaCondizioni = notaCondizioni;
	}
	public int getVocePacchettoQuantita() {
		return vocePacchettoQuantita;
	}
	public void setVocePacchettoQuantita(int vocePacchettoQuantita) {
		this.vocePacchettoQuantita = vocePacchettoQuantita;
	}
	public int getNumeroPezzi() {
		return numeroPezzi;
	}
	public void setNumeroPezzi(int numeroPezzi) {
		this.numeroPezzi = numeroPezzi;
	}

	public int getQuantitaMassimaSpedizioneCumulativa() {
		return quantitaMassimaSpedizioneCumulativa;
	}
	public void setQuantitaMassimaSpedizioneCumulativa(
			int quantitaMassimaSpedizioneCumulativa) {
		this.quantitaMassimaSpedizioneCumulativa = quantitaMassimaSpedizioneCumulativa;
	}
	public String getPaeseOrigine() {
		return paeseOrigine;
	}
	public void setPaeseOrigine(String paeseOrigine) {
		this.paeseOrigine = paeseOrigine;
	}

	public double getLunghezzaArticolo() {
		return lunghezzaArticolo;
	}
	public void setLunghezzaArticolo(double lunghezzaArticolo) {
		this.lunghezzaArticolo = lunghezzaArticolo;
	}
	public double getAltezzaArticolo() {
		return altezzaArticolo;
	}
	public void setAltezzaArticolo(double altezzaArticolo) {
		this.altezzaArticolo = altezzaArticolo;
	}
	public double getPesoArticolo() {
		return pesoArticolo;
	}
	public void setPesoArticolo(double pesoArticolo) {
		this.pesoArticolo = pesoArticolo;
	}
	public String getUnitaMisuraPesoArticolo() {
		return unitaMisuraPesoArticolo;
	}
	public void setUnitaMisuraPesoArticolo(String unitaMisuraPesoArticolo) {
		this.unitaMisuraPesoArticolo = unitaMisuraPesoArticolo;
	}

	public long getIdCategoria1() {
		return idCategoria1;
	}

	public void setIdCategoria1(long idCategoria1) {
		this.idCategoria1 = idCategoria1;
	}

	public long getIdCategoria2() {
		return idCategoria2;
	}

	public void setIdCategoria2(long idCategoria2) {
		this.idCategoria2 = idCategoria2;
	}

	public int getTempiEsecuzioneSpedizione() {
		return tempiEsecuzioneSpedizione;
	}

	public void setTempiEsecuzioneSpedizione(int tempiEsecuzioneSpedizione) {
		this.tempiEsecuzioneSpedizione = tempiEsecuzioneSpedizione;
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

}
