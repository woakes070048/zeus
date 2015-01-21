package it.swb.business;

import it.swb.database.InserzioniEbay_DAO;
import it.swb.model.Articolo;

import java.util.List;
import java.util.Map;

public class InserzioniEbayBusiness {
	
    private static InserzioniEbayBusiness instance = new InserzioniEbayBusiness();
    
    /* Costruttore privato della classe */
    private InserzioniEbayBusiness() {}

    /* Metodo che permette di ottenere l'istanza della classe */
    public static InserzioniEbayBusiness getInstance() {
        return instance;
    }

    private List<Articolo> inserzioni;
    
    private Map<String,String> mappaInserzioni;

	public List<Articolo> getInserzioni() {
		if (inserzioni==null)
			inserzioni = InserzioniEbay_DAO.getInserzioni();
		return inserzioni;
	}

	
	public Map<String,String> getMappaInserzioni() {
		if (mappaInserzioni==null)
			mappaInserzioni = InserzioniEbay_DAO.getMappaInserzioni();
		return mappaInserzioni;
	}
    
	public void reloadInserzioni(){
		inserzioni = null;
	}
	
	
	
	public void reloadMappaInserzioni(){
		mappaInserzioni = null;
	}
	
}
