package it.swb.business;

import it.swb.database.Cliente_DAO;
import it.swb.model.Cliente;

import java.util.List;
import java.util.Map;

public class ClienteBusiness {
	
    private static ClienteBusiness instance = new ClienteBusiness();
    
    /* Costruttore privato della classe */
    private ClienteBusiness() {}

    /* Metodo che permette di ottenere l'istanza della classe */
    public static ClienteBusiness getInstance() {
        return instance;
    }
    

    private List<Cliente> clientiGloriaMoraldi;
    private List<Cliente> clientiZelda;
    private Map<Integer,Cliente> mappaClientiZelda;
    private Map<String,Cliente> mappaClientiZeldaCompleta;
    private Map<Integer,Cliente> mappaClientiZeldaCompletaByID;

	public List<Cliente> getClientiGloriaMoraldi() {
		if (clientiGloriaMoraldi==null)
			clientiGloriaMoraldi = Cliente_DAO.getClienti();
		return clientiGloriaMoraldi;
	}

	public List<Cliente> getClientiZelda() {
		if (clientiZelda==null)
			clientiZelda = Cliente_DAO.getClientiZelda();
		return clientiZelda;
	}
	
	public Map<Integer,Cliente> getMappaClientiZelda() {
		if (mappaClientiZelda==null)
			mappaClientiZelda = Cliente_DAO.getMappaClientiZelda();
		return mappaClientiZelda;
	}
	
	public Map<String,Cliente> getMappaClientiZeldaCompleta() {
		if (mappaClientiZeldaCompleta==null)
			mappaClientiZeldaCompleta = Cliente_DAO.getMappaClientiZeldaCompleta();
		return mappaClientiZeldaCompleta;
	}
	
	public Map<Integer,Cliente> getMappaClientiZeldaCompletaByID() {
		if (mappaClientiZeldaCompletaByID==null)
			mappaClientiZeldaCompletaByID = Cliente_DAO.getMappaClientiZeldaCompletaByID();
		return mappaClientiZeldaCompletaByID;
	}
    
	public void reloadClientiGloriaMoraldi(){
		clientiGloriaMoraldi = null;
	}
	
	public void reloadClientiZelda(){
		clientiZelda = null;
	}
	
	public void reloadMappaClientiZelda(){
		mappaClientiZelda = null;
	}

	public void reloadMappaClientiZeldaCompleta(){
		mappaClientiZeldaCompleta = null;
	}
	
	public void reloadMappaClientiZeldaCompletaByID(){
		mappaClientiZeldaCompletaByID = null;
	}

}
