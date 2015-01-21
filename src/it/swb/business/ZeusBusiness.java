package it.swb.business;

public class ZeusBusiness {
	
    private static ZeusBusiness instance = new ZeusBusiness();
    
    /* Costruttore privato della classe */
    private ZeusBusiness() {}

    /* Metodo che permette di ottenere l'istanza della classe */
    public static ZeusBusiness getInstance() {
        return instance;
    }
    
    



}
