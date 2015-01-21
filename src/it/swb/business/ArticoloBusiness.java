package it.swb.business;

import it.swb.database.Articolo_DAO;
import it.swb.model.Articolo;
import it.swb.model.Filtro;

import java.util.List;
import java.util.Map;

public class ArticoloBusiness {
	
    private static ArticoloBusiness instance = new ArticoloBusiness();
    
    /* Costruttore privato della classe */
    private ArticoloBusiness() {}

    /* Metodo che permette di ottenere l'istanza della classe */
    public static ArticoloBusiness getInstance() {
        return instance;
    }
    
    private List<Articolo> articoli;  
    private Map<String, Articolo> mappaArticoli;
    private Filtro filtro;
    
	public Filtro getFiltro() {
		if (filtro==null)
			filtro = new Filtro();
		return filtro;
	}
	
	public Filtro resetFiltro() {
		filtro=null;
		return getFiltro();
	}

	public List<Articolo> getArticoli(){
		if (articoli==null){
			//VarianteBusiness.getInstance().reloadMappaVarianti();
			//LogBusiness.getInstance().reloadMappaLogArticoli();
			articoli = Articolo_DAO.getArticoli();
		}
		return articoli;
	}
	
	public List<Articolo> reloadArticoli(){		
		articoli = null;
		return getArticoli();
	}
	
	public Map<String, Articolo> getMappaArticoli() {
		if (mappaArticoli==null){
			//VarianteBusiness.getInstance().reloadMappaVarianti();
			//LogBusiness.getInstance().reloadMappaLogArticoli();
			mappaArticoli = Articolo_DAO.getMappaArticoliCompleta();
		}
		return mappaArticoli;
	}
	
	public Map<String, Articolo> reloadMappaArticoli(){		
		mappaArticoli = null;
		return getMappaArticoli();
	}
	

	
	public void reloadAll(){
		reloadArticoli();
		reloadMappaArticoli();
	}
	
	public int inserisciArticolo(Articolo art){
		int x = Articolo_DAO.inserisciArticolo(art);
		reloadAll();
		return x;
	}
	
	public int modificaArticolo(Articolo art,String s){
		int x = Articolo_DAO.modificaArticolo(art,s);
		reloadAll();
		return x;
	}
	
	public int modificaArticolo2(Articolo art){
		int x = Articolo_DAO.modificaArticolo2(art);
		reloadAll();
		return x;
	}
	
	public void eliminaArticolo(long idArticolo,String codiceArticolo){
		Articolo_DAO.eliminaArticolo(idArticolo,codiceArticolo);
		reloadAll();
	}
	
	public void setPresenze(Articolo a){
		Articolo_DAO.setPresenze(a);
		reloadAll();
	}
	
	
//	public void modificaArticoli(List<Articolo> arts){
//		Articolo_DAO.modificaArticoli(arts);
//		reloadAll();
//	}
	
	public void inserisciOModificaArticoli(List<Articolo> arts){
		Articolo_DAO.inserisciOModificaArticoli(arts);
		//reloadAll();
	}
	
	public int inserisciOModificaArticolo(Articolo a){
		int idArticolo = Articolo_DAO.checkIfArticoloExist2(a.getCodice());
		
		if (idArticolo==-1) idArticolo = Articolo_DAO.inserisciArticoloNew(a);
		else {
			a.setIdArticolo(idArticolo);
			int res = Articolo_DAO.modificaArticoloNew(a);
			if (res==-2) idArticolo = -2;
		}
		return idArticolo;
	}
	
	public void salvaCodiciBarreVarianti(Map<String, List<String>> m){
		Articolo_DAO.salvaCodiciBarreVarianti(m);
		reloadAll();
	}



}
