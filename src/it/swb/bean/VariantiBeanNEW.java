package it.swb.bean;

import it.swb.log.Log;
import it.swb.model.Variante_Articolo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;


@ManagedBean(name = "variantiBeanNew")
@SessionScoped
public class VariantiBeanNEW  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static VariantiBeanNEW instance;
	
	public static synchronized VariantiBeanNEW getInstance() {
		if (instance == null)
		{
			synchronized(VariantiBeanNEW.class) {      //1
				VariantiBeanNEW inst = instance;         //2
				if (inst == null)
				{
					synchronized(VariantiBeanNEW.class) {  //3
						instance = new VariantiBeanNEW();
					}
					//instance = inst;               //5
				}
			}
		}
		return instance;
	}
	
	private List<Variante_Articolo> varianti;
	private String tipoVariante;
	private String valoreVariante;
	private String codiceBarreVariante;
	private String dimensioniVariante;
	private int quantitaVariante;
	private String immagineVariante;
	
	private boolean mostraVariante = false;
	
	
	
	public void creaVariante(){
		if (varianti==null || varianti.isEmpty()) varianti = new ArrayList<Variante_Articolo>();
		
		Log.debug("creaVariante: "+valoreVariante);
		Variante_Articolo v = new Variante_Articolo();
		v.setTipo(tipoVariante);
		v.setValore(valoreVariante);
		v.setCodiceBarre(codiceBarreVariante);
		v.setDimensioni(dimensioniVariante);
		v.setQuantita(quantitaVariante);
		v.setImmagine(immagineVariante);
		
		varianti.add(v);			
	}
	
	public void showVariante() {
		Log.debug("showVariante");
		mostraVariante = true;
	}
	
	public void hideVariante() {
		Log.debug("hideVariante");
		mostraVariante = false;
		tipoVariante = "";
		valoreVariante = "";
		immagineVariante = "";
		quantitaVariante = 0;
		codiceBarreVariante = "";
		dimensioniVariante = "";
	}
	
	public void eliminaVariante(){
		Log.debug("eliminaVariante: "+valoreVariante);
		if (varianti!=null || !varianti.isEmpty()){
			List<Variante_Articolo> variantiTemp = new ArrayList<Variante_Articolo>();
			
			for (Variante_Articolo v : varianti){
				if (!v.getValore().equals(valoreVariante))
					variantiTemp.add(v);
			}
			varianti = variantiTemp;
			valoreVariante = "";
		}
	}

	public List<Variante_Articolo> getVarianti() {
		return varianti;
	}

	public void setVarianti(List<Variante_Articolo> varianti) {
		this.varianti = varianti;
	}

	public String getTipoVariante() {
		return tipoVariante;
	}

	public void setTipoVariante(String tipoVariante) {
		this.tipoVariante = tipoVariante;
	}

	public String getValoreVariante() {
		return valoreVariante;
	}

	public void setValoreVariante(String valoreVariante) {
		this.valoreVariante = valoreVariante;
	}

	public String getCodiceBarreVariante() {
		return codiceBarreVariante;
	}

	public void setCodiceBarreVariante(String codiceBarreVariante) {
		this.codiceBarreVariante = codiceBarreVariante;
	}

	public String getDimensioniVariante() {
		return dimensioniVariante;
	}

	public void setDimensioniVariante(String dimensioniVariante) {
		this.dimensioniVariante = dimensioniVariante;
	}

	public int getQuantitaVariante() {
		return quantitaVariante;
	}

	public void setQuantitaVariante(int quantitaVariante) {
		this.quantitaVariante = quantitaVariante;
	}

	public String getImmagineVariante() {
		return immagineVariante;
	}

	public void setImmagineVariante(String immagineVariante) {
		this.immagineVariante = immagineVariante;
	}

	public boolean isMostraVariante() {
		return mostraVariante;
	}

	public void setMostraVariante(boolean mostraVariante) {
		this.mostraVariante = mostraVariante;
	}


}
