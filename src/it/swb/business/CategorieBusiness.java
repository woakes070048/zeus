package it.swb.business;

import it.swb.database.Categoria_DAO;
import it.swb.database.DbTool;
import it.swb.model.Categoria;
import it.swb.model.CategoriaAmazon;
import it.swb.model.CategoriaEbay;

import java.util.List;
import java.util.Map;

public class CategorieBusiness {
	
    private static CategorieBusiness instance = new CategorieBusiness();
    
    /* Costruttore privato della classe */
    private CategorieBusiness() {}

    /* Metodo che permette di ottenere l'istanza della classe */
    public static CategorieBusiness getInstance() {
        return instance;
    }
    
    private List<Categoria> categorie;
    private Map<Long,CategoriaEbay> mappaCategorieEbay;
    private Map<Long,Categoria> mappaCategorieNegozioEbay;
    private Map<Long,Categoria> mappaCategorie;  
    private List<CategoriaEbay> categorieEbay;
    private List<CategoriaAmazon> categorieAmazon;
    private Map<Long,String> mappaCategorieAmazon;
    
	public Map<Long,Categoria> getMappaCategorie(){
		if (mappaCategorie==null)
			mappaCategorie = Categoria_DAO.getMappaCategorie(null);
		return mappaCategorie;
	}
	
	public Map<Long,Categoria> getMappaCategorie(DbTool dbt){
		if (mappaCategorie==null)
			mappaCategorie = Categoria_DAO.getMappaCategorie(dbt);
		return mappaCategorie;
	}
	
	public Map<Long,CategoriaEbay> getMappaCategorieEbay(){
		if (mappaCategorieEbay==null)
			mappaCategorieEbay = Categoria_DAO.getMappaCategorieEbay(null);
		return mappaCategorieEbay;
	}
	
	public Map<Long,CategoriaEbay> getMappaCategorieEbay(DbTool dbt){
		if (mappaCategorieEbay==null)
			mappaCategorieEbay = Categoria_DAO.getMappaCategorieEbay(dbt);
		return mappaCategorieEbay;
	}
	
	public Map<Long,Categoria> getMappaCategorieNegozioEbay(){
		if (mappaCategorieNegozioEbay==null)
			mappaCategorieNegozioEbay = Categoria_DAO.getMappaCategorieNegozioEbay();
		return mappaCategorieNegozioEbay;
	}
	
	public Map<Long,String> getMappaCategorieAmazon(){
		if (mappaCategorieAmazon==null)
			mappaCategorieAmazon = Categoria_DAO.getMappaCategorieAmazon(null);
		return mappaCategorieAmazon;
	}
	
	public Map<Long,String> getMappaCategorieAmazon(DbTool dbt){
		if (mappaCategorieAmazon==null)
			mappaCategorieAmazon = Categoria_DAO.getMappaCategorieAmazon(dbt);
		return mappaCategorieAmazon;
	}
	
	public List<CategoriaAmazon> getCategorieAmazon() {
		if (categorieAmazon==null)
			categorieAmazon = Categoria_DAO.getCategorieAmazon();
		return categorieAmazon;
	}

	public void setCategorieAmazon(List<CategoriaAmazon> categorieAmazon) {
		this.categorieAmazon = categorieAmazon;
	}
	
	public List<Categoria> getCategorie(){
		if (categorie==null)
			categorie=Categoria_DAO.getCategorie();
		return categorie;
	}
	
	public static List<Categoria> getCategoriePrincipali(){
		return Categoria_DAO.getCategoriePrincipali();
	}
	
//	public static List<Categoria> getSottoCategorieByIdCategoriaPrincipale(int id_categoria_principale){
//		return Categoria_DAO.getSottoCategorieByIdCategoriaPrincipale(id_categoria_principale);
//	}
	
//	public static List<Categoria_Ebay> getCategorieEbay(){
//		return Categoria_DAO.getCategorieEbay();
//	}
	
	public List<CategoriaEbay> getCategorieEbay(){
		if (categorieEbay==null)
			categorieEbay = Categoria_DAO.getCategorieEbay();
		return categorieEbay;
	}
	
	public static List<CategoriaEbay> getCategorieEbayLevel_1(){
		return Categoria_DAO.getCategorieEbayLevel_1();
	}
	
	public static List<CategoriaEbay> getCategorieEbayLevel_2(long parent_id){
		return Categoria_DAO.getCategorieEbayLevel_2(parent_id);
	}
	
	public static List<CategoriaEbay> getCategorieEbayLevel_3(long parent_id){
		return Categoria_DAO.getCategorieEbayLevel_3(parent_id);
	}
	
	public static List<CategoriaEbay> getCategorieEbayLevel_4(long parent_id){
		return Categoria_DAO.getCategorieEbayLevel_4(parent_id);
	}



}
