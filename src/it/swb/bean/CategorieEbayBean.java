package it.swb.bean;

import it.swb.business.CategorieBusiness;
import it.swb.log.Log;
import it.swb.model.CategoriaEbay;

import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

@ManagedBean(name = "catebayBean")
@SessionScoped
public class CategorieEbayBean {

	private Map<Long,CategoriaEbay> categorieEbayMap;
	
	private long categoria_ebay1_level_1_selezionata =-1;
	private long categoria_ebay1_level_2_selezionata =-1;
	private long categoria_ebay1_level_3_selezionata =-1;
	private long categoria_ebay1_level_4_selezionata =-1;
	private List<CategoriaEbay> categorie_ebay1_level_1;	
	private List<CategoriaEbay> categorie_ebay1_level_2;	
	private List<CategoriaEbay> categorie_ebay1_level_3;	
	private List<CategoriaEbay> categorie_ebay1_level_4;
	
	   public void svuotaCategorie1(){	    	
//	    	categorie_ebay1_level_2.clear();
//	    	categorie_ebay1_level_3.clear();
//	    	categorie_ebay1_level_4.clear();
	    }
	   
	   
	public void CatEbay1Level1Selezionata(ActionEvent ev){
		System.out.println("ciao!");	
		Log.debug("CatEbay1Level1Selezionata: "+categoria_ebay1_level_1_selezionata);
    	categorie_ebay1_level_2 = CategorieBusiness.getCategorieEbayLevel_2(categoria_ebay1_level_1_selezionata);   	
	}
	   
	public Map<Long,CategoriaEbay> getCategorieEbayMap() {
		if (categorieEbayMap==null || categorieEbayMap.isEmpty())
			categorieEbayMap = CategorieBusiness.getInstance().getMappaCategorieEbay();
		return categorieEbayMap;
	}

	public void setCategorieEbayMap(Map<Long,CategoriaEbay> categorieEbayMap) {
		this.categorieEbayMap = categorieEbayMap;
	}

	public List<CategoriaEbay> getCategorie_ebay1_level_1() {
		if (categorie_ebay1_level_1==null || categorie_ebay1_level_1.isEmpty())
			categorie_ebay1_level_1 = CategorieBusiness.getCategorieEbayLevel_1();
		return categorie_ebay1_level_1;
	}

	public void setCategorie_ebay1_level_1(
			List<CategoriaEbay> categorie_ebay1_level_1) {
		this.categorie_ebay1_level_1 = categorie_ebay1_level_1;
	}

	public List<CategoriaEbay> getCategorie_ebay1_level_2() {
		return categorie_ebay1_level_2;
	}

	public void setCategorie_ebay1_level_2(
			List<CategoriaEbay> categorie_ebay1_level_2) {
		this.categorie_ebay1_level_2 = categorie_ebay1_level_2;
	}

	public List<CategoriaEbay> getCategorie_ebay1_level_3() {
		return categorie_ebay1_level_3;
	}

	public void setCategorie_ebay1_level_3(
			List<CategoriaEbay> categorie_ebay1_level_3) {
		this.categorie_ebay1_level_3 = categorie_ebay1_level_3;
	}

	public List<CategoriaEbay> getCategorie_ebay1_level_4() {
		return categorie_ebay1_level_4;
	}

	public void setCategorie_ebay1_level_4(
			List<CategoriaEbay> categorie_ebay1_level_4) {
		this.categorie_ebay1_level_4 = categorie_ebay1_level_4;
	}

	public long getCategoria_ebay1_level_1_selezionata() {
		return categoria_ebay1_level_1_selezionata;
	}

	public void setCategoria_ebay1_level_1_selezionata(
			long categoria_ebay1_level_1_selezionata) {
		this.categoria_ebay1_level_1_selezionata = categoria_ebay1_level_1_selezionata;
	}


	public long getCategoria_ebay1_level_2_selezionata() {
		return categoria_ebay1_level_2_selezionata;
	}


	public void setCategoria_ebay1_level_2_selezionata(
			long categoria_ebay1_level_2_selezionata) {
		this.categoria_ebay1_level_2_selezionata = categoria_ebay1_level_2_selezionata;
	}


	public long getCategoria_ebay1_level_3_selezionata() {
		return categoria_ebay1_level_3_selezionata;
	}


	public void setCategoria_ebay1_level_3_selezionata(
			long categoria_ebay1_level_3_selezionata) {
		this.categoria_ebay1_level_3_selezionata = categoria_ebay1_level_3_selezionata;
	}


	public long getCategoria_ebay1_level_4_selezionata() {
		return categoria_ebay1_level_4_selezionata;
	}


	public void setCategoria_ebay1_level_4_selezionata(
			long categoria_ebay1_level_4_selezionata) {
		this.categoria_ebay1_level_4_selezionata = categoria_ebay1_level_4_selezionata;
	}
}
