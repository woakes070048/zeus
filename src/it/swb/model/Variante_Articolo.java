package it.swb.model;

public class Variante_Articolo {
	
	int idVariante;	
	String codiceArticolo;
	long id_articolo;
	String tipo;
	String valore;
	String immagine;
	int quantita;
	int attr_id;
	String dimensioni;
	String codiceBarre;
	String tipoCodiceBarre;
	
	
	public Variante_Articolo() {
		super();
	}
	
	public Variante_Articolo(String tipo, String valore, String immagine) {
		this.tipo = tipo;
		this.valore = valore;
		this.immagine = immagine;
	} 
	
	public Variante_Articolo(String tipo, String valore, String immagine, int quantita) {
		this.tipo = tipo;
		this.valore = valore;
		this.immagine = immagine;
		this.quantita = quantita;
	}
	
	public Variante_Articolo(String tipo, String valore, String immagine, int quantita, String codiceBarre, String dimensioni) {
		this.tipo = tipo;
		this.valore = valore;
		this.immagine = immagine;
		this.quantita = quantita;
		this.codiceBarre = codiceBarre;
		this.dimensioni = dimensioni;
	}


	public int getIdVariante() {
		return idVariante;
	}


	public void setIdVariante(int idVariante) {
		this.idVariante = idVariante;
	}


	public String getCodiceArticolo() {
		return codiceArticolo;
	}


	public void setCodiceArticolo(String codiceArticolo) {
		this.codiceArticolo = codiceArticolo;
	}


	public long getId_articolo() {
		return id_articolo;
	}


	public void setId_articolo(long id_articolo) {
		this.id_articolo = id_articolo;
	}


	public String getTipo() {
		return tipo;
	}


	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	public String getValore() {
		return valore;
	}


	public void setValore(String valore) {
		this.valore = valore;
	}


	public String getImmagine() {
		return immagine;
	}


	public void setImmagine(String immagine) {
		this.immagine = immagine;
	}

	public int getAttr_id() {
		if (this.tipo.toLowerCase().equals("colore")) attr_id = 1; 
		else if (this.tipo.toLowerCase().equals("tema")) attr_id = 3;	
		else if (this.tipo.toLowerCase().equals("gusto")) attr_id = 4;	
		else if (this.tipo.toLowerCase().equals("variante")) attr_id = 5;			
		
		return attr_id;
	}

	public void setAttr_id(int attr_id) {
		this.attr_id = attr_id;
	}

	public int getQuantita() {
		return quantita;
	}

	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}

	public String getCodiceBarre() {
		return codiceBarre;
	}

	public void setCodiceBarre(String codiceBarre) {
		this.codiceBarre = codiceBarre;
	}

	public String getTipoCodiceBarre() {
		return tipoCodiceBarre;
	}

	public void setTipoCodiceBarre(String tipoCodiceBarre) {
		this.tipoCodiceBarre = tipoCodiceBarre;
	}

	public String getDimensioni() {
		return dimensioni;
	}

	public void setDimensioni(String dimensioni) {
		this.dimensioni = dimensioni;
	}
	
	
}
