package it.swb.bean;

import java.io.Serializable;
import java.util.List;

import it.swb.java.StampanteFiscale;
import it.swb.model.Articolo;
import it.swb.model.InfoEbay;
import it.swb.utility.EditorDescrizioni;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "utilityBean")
@RequestScoped
public class UtilityBean  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String codice;
	String codiceBarre;
	String piattaforma;
	String descrizione;
	String quantitaInserzione;
	String dimensioni;
	String nomeArticolo;
	String immagine1;
	String immagine2;
	String immagine3;
	String immagine4;
	String immagine5;
	String html = "codice html";
	
	List<Articolo> listaArticoli;
	double costoSpedizione;
	boolean contrassegno;
	
	public void stampaScontrino(){
		StampanteFiscale.stampaScontrino(listaArticoli,costoSpedizione,contrassegno);
    	FacesMessage msg = new FacesMessage("Operazione Completata", "Scontrino mandato in stampa");  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
	}

	public void generaHtmlEbay(){
		if(piattaforma.equals("ebay")){
			
			Articolo a = new Articolo();
			
			InfoEbay ie = new InfoEbay();
			ie.setTitoloInserzione(nomeArticolo);
			a.setInfoEbay(ie);
			
			a.setCodice(codice);
			a.setCodiceBarre(codiceBarre);
			a.setNome(nomeArticolo);
			a.setDescrizione(descrizione);
			a.setQuantitaInserzione(quantitaInserzione);
			a.setDimensioni(dimensioni);
			a.setImmagine1(immagine1);
			a.setImmagine2(immagine2);
			a.setImmagine3(immagine3);
			a.setImmagine4(immagine4);
			a.setImmagine5(immagine5);
			
			html = EditorDescrizioni.creaDescrizioneEbay(a);
		}
		
		else if(piattaforma.equals("yatego"))
			html = EditorDescrizioni.creaDescrizioneYatego(nomeArticolo, quantitaInserzione, dimensioni, descrizione);
		
		else if(piattaforma.equals("gm")){
			Articolo a = new Articolo();
			a.setCodice(codice);
			a.setCodiceBarre(codiceBarre);
			a.setQuantitaInserzione(quantitaInserzione);
			a.setDimensioni(dimensioni);
			a.setDescrizione(descrizione);
			html = EditorDescrizioni.creaDescrizioneSitoGM(a);
		}
			
	
	}
	
	
	public String handleCommand(String command, String[] params) {  
            return "";  
    }  

	public String getPiattaforma() {
		return piattaforma;
	}

	public void setPiattaforma(String piattaforma) {
		this.piattaforma = piattaforma;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
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

	public String getNomeArticolo() {
		return nomeArticolo;
	}

	public void setNomeArticolo(String nomeArticolo) {
		this.nomeArticolo = nomeArticolo;
	}


	public String getHtml() {
		return html;
	}


	public void setHtml(String html) {
		this.html = html;
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


	public String getCodice() {
		return codice;
	}


	public void setCodice(String codice) {
		this.codice = codice;
	}


	public String getCodiceBarre() {
		return codiceBarre;
	}


	public void setCodiceBarre(String codiceBarre) {
		this.codiceBarre = codiceBarre;
	}

	public List<Articolo> getListaArticoli() {
		return listaArticoli;
	}

	public void setListaArticoli(List<Articolo> listaArticoli) {
		this.listaArticoli = listaArticoli;
	}

	public double getCostoSpedizione() {
		return costoSpedizione;
	}

	public void setCostoSpedizione(double costoSpedizione) {
		this.costoSpedizione = costoSpedizione;
	}

	public boolean isContrassegno() {
		return contrassegno;
	}

	public void setContrassegno(boolean contrassegno) {
		this.contrassegno = contrassegno;
	}

}
