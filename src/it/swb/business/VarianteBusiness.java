package it.swb.business;

import it.swb.database.DbTool;
import it.swb.database.Variante_Articolo_DAO;
import it.swb.model.Variante_Articolo;

import java.util.List;
import java.util.Map;

public class VarianteBusiness {
	
    private static VarianteBusiness instance = new VarianteBusiness();
    
    /* Costruttore privato della classe */
    private VarianteBusiness() {}

    /* Metodo che permette di ottenere l'istanza della classe */
    public static VarianteBusiness getInstance() {
        return instance;
    }
    
    private Map<String, List<Variante_Articolo>> mappaVarianti;
	
	public Map<String, List<Variante_Articolo>> getMappaVarianti() {
		if (mappaVarianti==null)
			mappaVarianti = Variante_Articolo_DAO.getMappaVariantiCompleta(null);
		return mappaVarianti;
	}
	
	public Map<String, List<Variante_Articolo>> getMappaVarianti(DbTool dbt) {
		if (mappaVarianti==null)
			mappaVarianti = Variante_Articolo_DAO.getMappaVariantiCompleta(dbt);
		return mappaVarianti;
	}
	
	public Map<String, List<Variante_Articolo>> reloadMappaVarianti(){		
		mappaVarianti = null;
		return getMappaVarianti();
	}
	
	public Map<String, List<Variante_Articolo>> reloadMappaVarianti(DbTool dbt){		
		mappaVarianti = null;
		return getMappaVarianti(dbt);
	}
	
}
