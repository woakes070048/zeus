package it.swb.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Ordine implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	int idOrdine;
	String idOrdinePiattaforma;
	String piattaforma;
	int idCliente;
	Cliente cliente;
	String email;
	
	Date dataAcquisto;
	Date dataPagamento;
	Date dataSpedizione;
	Date dataUltimaModifica;
	
	Date dataLDV;
	int codaLDV;
	int archiviato;
	int scontrinoStampato;
	
	String stDataAcquisto;
	String stDataPagamento;
	String stDataSpedizione;
	String stDataUltimaModifica;
	
	String metodoPagamento;
	String metodoSpedizione;
	double costoSpedizione;
	double tasse;
	double totale;
	String valuta;
	
	boolean sconto;
	String nomeBuonoSconto;
	double valoreBuonoSconto;
	
	int quantitaAcquistata;
	String stato;
	
	String commento;
	
	String numeroTracciamento;
	
	Indirizzo indirizzoSpedizione;
	Indirizzo indirizzoFatturazione;
	List<Articolo> articoli;
	
	public Ordine(){}

	public String getPiattaforma() {
		return piattaforma;
	}

	public void setPiattaforma(String piattaforma) {
		this.piattaforma = piattaforma;
	}

	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Date getDataAcquisto() {
		return dataAcquisto;
	}

	public void setDataAcquisto(Date dataAcquisto) {
		this.dataAcquisto = dataAcquisto;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public Date getDataSpedizione() {
		return dataSpedizione;
	}

	public void setDataSpedizione(Date dataSpedizione) {
		this.dataSpedizione = dataSpedizione;
	}

	public Date getDataUltimaModifica() {
		return dataUltimaModifica;
	}

	public void setDataUltimaModifica(Date dataUltimaModifica) {
		this.dataUltimaModifica = dataUltimaModifica;
	}

	public String getMetodoPagamento() {
		return metodoPagamento;
	}

	public void setMetodoPagamento(String metodoPagamento) {
		this.metodoPagamento = metodoPagamento;
	}

	public String getMetodoSpedizione() {
		return metodoSpedizione;
	}

	public void setMetodoSpedizione(String metodoSpedizione) {
		this.metodoSpedizione = metodoSpedizione;
	}

	public double getCostoSpedizione() {
		return costoSpedizione;
	}

	public void setCostoSpedizione(double costoSpedizione) {
		this.costoSpedizione = costoSpedizione;
	}

	public double getTasse() {
		return tasse;
	}

	public void setTasse(double tasse) {
		this.tasse = tasse;
	}

	public double getTotale() {
		return totale;
	}

	public void setTotale(double totale) {
		this.totale = totale;
	}

	public String getValuta() {
		return valuta;
	}

	public void setValuta(String valuta) {
		this.valuta = valuta;
	}

	public int getQuantitaAcquistata() {
		return quantitaAcquistata;
	}

	public void setQuantitaAcquistata(int quantitaAcquistata) {
		this.quantitaAcquistata = quantitaAcquistata;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public String getCommento() {
		return commento;
	}

	public void setCommento(String commento) {
		this.commento = commento;
	}

	public String getNumeroTracciamento() {
		return numeroTracciamento;
	}

	public void setNumeroTracciamento(String numeroTracciamento) {
		this.numeroTracciamento = numeroTracciamento;
	}

	public Indirizzo getIndirizzoSpedizione() {
		return indirizzoSpedizione;
	}

	public void setIndirizzoSpedizione(Indirizzo indirizzoSpedizione) {
		this.indirizzoSpedizione = indirizzoSpedizione;
	}

	public Indirizzo getIndirizzoFatturazione() {
		return indirizzoFatturazione;
	}

	public void setIndirizzoFatturazione(Indirizzo indirizzoFatturazione) {
		this.indirizzoFatturazione = indirizzoFatturazione;
	}

	public List<Articolo> getArticoli() {
		return articoli;
	}

	public void setArticoli(List<Articolo> articoli) {
		this.articoli = articoli;
	}

	public String getIdOrdinePiattaforma() {
		return idOrdinePiattaforma;
	}

	public void setIdOrdinePiattaforma(String idOrdinePiattaforma) {
		this.idOrdinePiattaforma = idOrdinePiattaforma;
	}

	public void setIdOrdine(int idOrdine) {
		this.idOrdine = idOrdine;
	}
	
	public int getIdOrdine() {
		return idOrdine;
	}

	public String getStDataAcquisto() {
		return stDataAcquisto;
	}

	public void setStDataAcquisto(String stDataAcquisto) {
		this.stDataAcquisto = stDataAcquisto;
	}

	public String getStDataPagamento() {
		return stDataPagamento;
	}

	public void setStDataPagamento(String stDataPagamento) {
		this.stDataPagamento = stDataPagamento;
	}

	public String getStDataSpedizione() {
		return stDataSpedizione;
	}

	public void setStDataSpedizione(String stDataSpedizione) {
		this.stDataSpedizione = stDataSpedizione;
	}

	public String getStDataUltimaModifica() {
		return stDataUltimaModifica;
	}

	public void setStDataUltimaModifica(String stDataUltimaModifica) {
		this.stDataUltimaModifica = stDataUltimaModifica;
	}

	public boolean isSconto() {
		return sconto;
	}

	public void setSconto(boolean sconto) {
		this.sconto = sconto;
	}

	public String getNomeBuonoSconto() {
		return nomeBuonoSconto;
	}

	public void setNomeBuonoSconto(String nomeBuonoSconto) {
		this.nomeBuonoSconto = nomeBuonoSconto;
	}

	public double getValoreBuonoSconto() {
		return valoreBuonoSconto;
	}

	public void setValoreBuonoSconto(double valoreBuonoSconto) {
		this.valoreBuonoSconto = valoreBuonoSconto;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDataLDV() {
		return dataLDV;
	}

	public void setDataLDV(Date dataLDV) {
		this.dataLDV = dataLDV;
	}

	public int getCodaLDV() {
		return codaLDV;
	}

	public void setCodaLDV(int codaLDV) {
		this.codaLDV = codaLDV;
	}

	public int getArchiviato() {
		return archiviato;
	}

	public void setArchiviato(int archiviato) {
		this.archiviato = archiviato;
	}

	public int getScontrinoStampato() {
		return scontrinoStampato;
	}

	public void setScontrinoStampato(int scontrinoStampato) {
		this.scontrinoStampato = scontrinoStampato;
	}
	
}
