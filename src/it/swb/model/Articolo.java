package it.swb.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public class Articolo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long idArticolo;
	
	//informazioni gestionale
	String codice;
	String nome;
	String note;
	String note2;
	double prezzoIngrosso;
	double prezzoDettaglio;
	double prezzoPiattaforme;
	double costoAcquisto;
	double costoSpedizione;
	int aliquotaIva;
	String unitaMisura;
	int idCategoriaGestionale;
	long idCategoria;
	long idCategoria2;
	Date dataInserimento;
	Date dataUltimaModifica;
	String codiceFornitore;
	String codiceArticoloFornitore;	//codice articolo usato dal fornitore
	String descrizioneCategoria;	//???
	String codiceBarre;				//codice a barre
	String codiciBarreVarianti;
	String tipoCodiceBarre;
	String idEbay;
	String asin;
	
	String descrizioneBreve;
	String descrizione;
	int quantitaMagazzino;
	int quantitaEffettiva;
	String quantitaInserzione;
	String dimensioni;
	
	String thumbnail;
	String immagine1;	
	String immagine2;
	String immagine3;
	String immagine4;
	String immagine5;
	
	String paroleChiave1;
	String paroleChiave2;
	String paroleChiave3;
	String paroleChiave4;
	String paroleChiave5;	
	
	String video;
	String idVideo;
	
	int haVarianti;
	List<Variante_Articolo> varianti;
	Categoria categoria;
	Categoria categoria2;
	List<LogArticolo> logArticolo;
	
	String titoloInserzione;
	
	//informazioni sito gloriamoraldi
	int presente_su_gm;
	
	//informazioni ebay
	InfoEbay infoEbay;
	int presente_su_ebay;
	
	//informazioni amazon
	int presente_su_amazon;
	InfoAmazon infoAmazon;
	
	int presente_su_zb;
	
	boolean nonEliminare;
	boolean nonSincronizzare;
	

	public Articolo() {	
		super();
		this.idArticolo = 0;
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
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getUnitaMisura() {
		return unitaMisura;
	}
	public void setUnitaMisura(String unitaMisura) {
		this.unitaMisura = unitaMisura;
	}
	public int getAliquotaIva() {
		return aliquotaIva;
	}
	public void setAliquotaIva(int aliquotaIva) {
		this.aliquotaIva = aliquotaIva;
	}
	public double getPrezzoIngrosso() {
		return prezzoIngrosso;
	}
	public void setPrezzoIngrosso(double prezzoIngrosso) {
		this.prezzoIngrosso = prezzoIngrosso;
	}
	public double getPrezzoDettaglio() {
		return prezzoDettaglio;
	}
	public void setPrezzoDettaglio(double prezzoDettaglio) {
		this.prezzoDettaglio = prezzoDettaglio;
	}
	public double getCostoAcquisto() {
		return costoAcquisto;
	}
	public void setCostoAcquisto(double costoAcquisto) {
		this.costoAcquisto = costoAcquisto;
	}
	public int getIdCategoriaGestionale() {
		return idCategoriaGestionale;
	}
	public void setCategoriaGestionale(int idCategoriaGestionale) {
		this.idCategoriaGestionale = idCategoriaGestionale;
	}
	public Date getDataInserimento() {
		return dataInserimento;
	}
	public void setDataInserimento(Date dataInserimento) {
		this.dataInserimento = dataInserimento;
	}
	public Date getDataUltimaModifica() {
		return dataUltimaModifica;
	}
	public void setDataUltimaModifica(Date dataUltimaModifica) {
		this.dataUltimaModifica = dataUltimaModifica;
	}
	public String getCodiceFornitore() {
		return codiceFornitore;
	}
	public void setCodiceFornitore(String codiceFornitore) {
		this.codiceFornitore = codiceFornitore;
	}
	public String getCodiceArticoloFornitore() {
		return codiceArticoloFornitore;
	}
	public void setCodiceArticoloFornitore(String codiceArticoloFornitore) {
		this.codiceArticoloFornitore = codiceArticoloFornitore;
	}
	public String getDescrizioneCategoria() {
		return descrizioneCategoria;
	}
	public void setDescrizioneCategoria(String descrizioneCategoria) {
		this.descrizioneCategoria = descrizioneCategoria;
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
	public long getIdArticolo() {
		return idArticolo;
	}
	public void setIdArticolo(long idArticolo) {
		this.idArticolo = idArticolo;
	}
	
	public String getDescrizioneBreve() {
		return descrizioneBreve;
	}
	public void setDescrizioneBreve(String descrizioneBreve) {
		this.descrizioneBreve = descrizioneBreve;
	}
	public String getQuantitaInserzione() {
		return quantitaInserzione;
	}
	public void setQuantitaInserzione(String quantitaInserzione) {
		this.quantitaInserzione = quantitaInserzione;
	}
	public String getDimensioni() {
		return dimensioni;
	}
	public void setDimensioni(String dimensioni) {
		this.dimensioni = dimensioni;
	}

	public void setIdCategoriaGestionale(int idCategoriaGestionale) {
		this.idCategoriaGestionale = idCategoriaGestionale;
	}
	public String getImmagine1() {
		return immagine1;
	}
	public void setImmagine1(String immagine1) {
		this.immagine1 = immagine1;
	}
	public String getImmagine2() {
		return immagine2;
	}
	public void setImmagine2(String immagine2) {
		this.immagine2 = immagine2;
	}
	public String getImmagine3() {
		return immagine3;
	}
	public void setImmagine3(String immagine3) {
		this.immagine3 = immagine3;
	}
	public String getImmagine4() {
		return immagine4;
	}
	public void setImmagine4(String immagine4) {
		this.immagine4 = immagine4;
	}
	public String getImmagine5() {
		return immagine5;
	}
	public void setImmagine5(String immagine5) {
		this.immagine5 = immagine5;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public long getIdCategoria() {
		return idCategoria;
	}
	public void setIdCategoria(long idCategoria) {
		this.idCategoria = idCategoria;
	}
	public int getPresente_su_gm() {
		return presente_su_gm;
	}
	public void setPresente_su_gm(int presente_su_gm) {
		this.presente_su_gm = presente_su_gm;
	}
	public int getPresente_su_ebay() {
		return presente_su_ebay;
	}
	public void setPresente_su_ebay(int presente_su_ebay) {
		this.presente_su_ebay = presente_su_ebay;
	}
	public List<Variante_Articolo> getVarianti() {
//		if (varianti==null){
//			//varianti = Variante_Articolo_DAO.getVarianti(codice);
//			varianti = VarianteBusiness.getInstance().getMappaVarianti().get(codice);
//		}
		return varianti;
	}
	public void setVarianti(List<Variante_Articolo> varianti) {
		this.varianti = varianti;
	}
	public Categoria getCategoria() {
		return categoria;
	}
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	public int getQuantitaMagazzino() {
		return quantitaMagazzino;
	}
	public void setQuantitaMagazzino(int quantitaMagazzino) {
		this.quantitaMagazzino = quantitaMagazzino;
	}

	public InfoEbay getInfoEbay() {
		return infoEbay;
	}
	public void setInfoEbay(InfoEbay infoEbay) {
		this.infoEbay = infoEbay;
	}
	public String getTitoloInserzione() {
		return titoloInserzione;
	}
	public void setTitoloInserzione(String titoloInserzione) {
		this.titoloInserzione = titoloInserzione;
	}
	public int getPresente_su_amazon() {
		return presente_su_amazon;
	}
	public void setPresente_su_amazon(int presente_su_amazon) {
		this.presente_su_amazon = presente_su_amazon;
	}
	public InfoAmazon getInfoAmazon() {
		return infoAmazon;
	}
	public void setInfoAmazon(InfoAmazon infoAmazon) {
		this.infoAmazon = infoAmazon;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public int getHaVarianti() {
		return haVarianti;
	}
	public void setHaVarianti(int haVarianti) {
		this.haVarianti = haVarianti;
	}
	public int getQuantitaEffettiva() {
		return quantitaEffettiva;
	}
	public void setQuantitaEffettiva(int quantitaEffettiva) {
		this.quantitaEffettiva = quantitaEffettiva;
	}
	public double getCostoSpedizione() {
		return costoSpedizione;
	}
	public void setCostoSpedizione(double costoSpedizione) {
		this.costoSpedizione = costoSpedizione;
	}
	public List<LogArticolo> getLogArticolo() {
		return logArticolo;
	}
	public void setLogArticolo(List<LogArticolo> logArticolo) {
		this.logArticolo = logArticolo;
	}
	public String getNote2() {
		return note2;
	}
	public void setNote2(String note2) {
		this.note2 = note2;
	}
	public double getPrezzoPiattaforme() {
		return prezzoPiattaforme;
	}
	public void setPrezzoPiattaforme(double prezzoPiattaforme) {
		this.prezzoPiattaforme = prezzoPiattaforme;
	}
	public boolean isNonEliminare() {
		return nonEliminare;
	}
	public void setNonEliminare(boolean nonEliminare) {
		this.nonEliminare = nonEliminare;
	}
	public boolean isNonSincronizzare() {
		return nonSincronizzare;
	}
	public void setNonSincronizzare(boolean nonSincronizzare) {
		this.nonSincronizzare = nonSincronizzare;
	}
	public String getCodiciBarreVarianti() {
		return codiciBarreVarianti;
	}
	public void setCodiciBarreVarianti(String codiciBarreVarianti) {
		this.codiciBarreVarianti = codiciBarreVarianti;
	}
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}
	public String getIdVideo() {
		return idVideo;
	}
	public void setIdVideo(String idVideo) {
		this.idVideo = idVideo;
	}
	public long getIdCategoria2() {
		return idCategoria2;
	}
	public void setIdCategoria2(long idCategoria2) {
		this.idCategoria2 = idCategoria2;
	}
	public Categoria getCategoria2() {
		return categoria2;
	}
	public void setCategoria2(Categoria categoria2) {
		this.categoria2 = categoria2;
	}
	public String getParoleChiave1() {
		return paroleChiave1;
	}
	public void setParoleChiave1(String paroleChiave1) {
		this.paroleChiave1 = paroleChiave1;
	}
	public String getParoleChiave2() {
		return paroleChiave2;
	}
	public void setParoleChiave2(String paroleChiave2) {
		this.paroleChiave2 = paroleChiave2;
	}
	public String getParoleChiave3() {
		return paroleChiave3;
	}
	public void setParoleChiave3(String paroleChiave3) {
		this.paroleChiave3 = paroleChiave3;
	}
	public String getParoleChiave4() {
		return paroleChiave4;
	}
	public void setParoleChiave4(String paroleChiave4) {
		this.paroleChiave4 = paroleChiave4;
	}
	public String getParoleChiave5() {
		return paroleChiave5;
	}
	public void setParoleChiave5(String paroleChiave5) {
		this.paroleChiave5 = paroleChiave5;
	}
	public String getIdEbay() {
		return idEbay;
	}
	public void setIdEbay(String idEbay) {
		this.idEbay = idEbay;
	}
	public int getPresente_su_zb() {
		return presente_su_zb;
	}
	public void setPresente_su_zb(int presente_su_zb) {
		this.presente_su_zb = presente_su_zb;
	}
	public String getAsin() {
		return asin;
	}
	public void setAsin(String asin) {
		this.asin = asin;
	}
	
}
