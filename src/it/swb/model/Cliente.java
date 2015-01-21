package it.swb.model;

import java.io.Serializable;

public class Cliente  implements Serializable{
	
	/*	informazioni comuni	*/
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int idCliente;
	String titolo;
	String azienda;
	String nome;
	String cognome;
	String nomeCompleto;
	String email;
	String fax; 
	String telefono1;
	String telefono2;
	String codiceFiscale;
	String partitaIva;
	
	String piattaforma;
	String telefono;
	String cellulare;
	
	
	/*	informazioni piattaforme e-commerce	*/
	
	String username;
	Indirizzo indirizzoSpedizione;
	Indirizzo indirizzoFatturazione;
	
	/*	informazioni gloria moraldi	*/
	
	String codiceCliente;
	String ragioneSociale;
	String proprietario;
	String codicePagamento;
	String responsabileRappresentante;
	String tipoAttivita;
	String codiceTipoAttivita;
	Indirizzo indirizzoSedeLegale;
	Indirizzo indirizzoUffici;
	
	public Cliente(){
		super();
	}

	public String getCodiceCliente() {
		return codiceCliente;
	}

	public void setCodiceCliente(String codiceCliente) {
		this.codiceCliente = codiceCliente;
	}

	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public String getProprietario() {
		return proprietario;
	}

	public void setProprietario(String proprietario) {
		this.proprietario = proprietario;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getPartitaIva() {
		return partitaIva;
	}

	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}

	public String getCodicePagamento() {
		return codicePagamento;
	}

	public void setCodicePagamento(String codicePagamento) {
		this.codicePagamento = codicePagamento;
	}

	public String getTelefono1() {
		return telefono1;
	}

	public void setTelefono1(String telefono1) {
		this.telefono1 = telefono1;
	}

	public String getTelefono2() {
		return telefono2;
	}

	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getResponsabileRappresentante() {
		return responsabileRappresentante;
	}

	public void setResponsabileRappresentante(String responsabileRappresentante) {
		this.responsabileRappresentante = responsabileRappresentante;
	}

	public String getTipoAttivita() {
		return tipoAttivita;
	}

	public void setTipoAttivita(String tipoAttivita) {
		this.tipoAttivita = tipoAttivita;
	}

	public String getCodiceTipoAttivita() {
		return codiceTipoAttivita;
	}

	public void setCodiceTipoAttivita(String codiceTipoAttivita) {
		this.codiceTipoAttivita = codiceTipoAttivita;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getAzienda() {
		return azienda;
	}

	public void setAzienda(String azienda) {
		this.azienda = azienda;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}

	public String getPiattaforma() {
		return piattaforma;
	}

	public void setPiattaforma(String piattaforma) {
		this.piattaforma = piattaforma;
	}



	public Indirizzo getIndirizzoSpedizione() {
		return indirizzoSpedizione;
	}

	public void setIndirizzoSpedizione(Indirizzo indirizzoSpedizione) {
		this.indirizzoSpedizione = indirizzoSpedizione;
	}

	public Indirizzo getIndirizzoSedeLegale() {
		return indirizzoSedeLegale;
	}

	public void setIndirizzoSedeLegale(Indirizzo indirizzoSedeLegale) {
		this.indirizzoSedeLegale = indirizzoSedeLegale;
	}

	public Indirizzo getIndirizzoUffici() {
		return indirizzoUffici;
	}

	public void setIndirizzoUffici(Indirizzo indirizzoUffici) {
		this.indirizzoUffici = indirizzoUffici;
	}

	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCellulare() {
		return cellulare;
	}

	public void setCellulare(String cellulare) {
		this.cellulare = cellulare;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public Indirizzo getIndirizzoFatturazione() {
		return indirizzoFatturazione;
	}

	public void setIndirizzoFatturazione(Indirizzo indirizzoFatturazione) {
		this.indirizzoFatturazione = indirizzoFatturazione;
	}


	

}
