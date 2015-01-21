package it.swb.business;

import java.util.List;

import it.swb.database.Mcd_DAO;

public class McdBusiness {
	
    private static McdBusiness instance = new McdBusiness();
    
    /* Costruttore privato della classe */
    private McdBusiness() {}

    /* Metodo che permette di ottenere l'istanza della classe */
    public static McdBusiness getInstance() {
        return instance;
    }
    
    public static int aggiungiAMcd(String codiceArticolo, String piattaforma){
    	
    	return Mcd_DAO.aggiungiAMcd(codiceArticolo,piattaforma);
    	
    }
    
	public static List<String> getMcd(String piattaforma){
		
		return Mcd_DAO.getMcd(piattaforma);
	}
	
	public static int getNumeroArticoliInAttesa(String piattaforma){
		
		return Mcd_DAO.getNumeroArticoliInAttesa(piattaforma);
	}

	
	public static int segnaComeElaborati(String piattaforma){
		
		return Mcd_DAO.segnaComeElaborati(piattaforma);
	}
}
