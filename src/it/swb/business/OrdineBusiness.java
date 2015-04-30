package it.swb.business;

import it.swb.database.Ordine_DAO;
import it.swb.model.Articolo;
import it.swb.model.ArticoloAcquistato;
import it.swb.model.Ordine;
import it.swb.piattaforme.amazon.AmazonListOrders;
import it.swb.piattaforme.ebay.EbayGetOrders;
import it.swb.piattaforme.zelda.OrdiniZelda;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class OrdineBusiness {
	
    private static OrdineBusiness instance = new OrdineBusiness();
    
    /* Costruttore privato della classe */
    private OrdineBusiness() {}

    /* Metodo che permette di ottenere l'istanza della classe */
    public static OrdineBusiness getInstance() {
        return instance;
    }
    
    private List<Ordine> ordini;  
    private Map<String,List<ArticoloAcquistato>> mappaOrdiniVsArticoli;
    private Date dataDa;
    private Date dataA;
    private String filtroOrdini;
	
	public List<Ordine> getOrdini(Date dataDa, Date dataA, String filtro){
		this.dataDa = dataDa;
		this.dataA = dataA;
		this.filtroOrdini = filtro;
		if (ordini==null){
			ordini = Ordine_DAO.getOrdini(this.dataDa, this.dataA, filtroOrdini);
		}
		return ordini;
	}
	
	public List<Ordine> getOrdiniPerLDV(){
		return Ordine_DAO.getOrdiniPerLDV();
	}
	
	public String downloadOrdiniEbay(Date dataDa, Date dataA){
		ordini = EbayGetOrders.getOrdini(dataDa, dataA);
		return Ordine_DAO.elaboraOrdini(ordini);
	}
	
	public String downloadOrdiniZelda(Date dataDa, Date dataA){
		ordini = OrdiniZelda.getOrdini(dataDa, dataA);
		return Ordine_DAO.elaboraOrdini(ordini);
	}
	
	public String downloadOrdini(Date dataDa, Date dataA){
		List<Ordine> ordiniEbay = EbayGetOrders.getOrdini(dataDa, dataA);
		List<Ordine> ordiniAmazon = AmazonListOrders.scaricaOrdini(dataDa, dataA);
		List<Ordine> ordiniZelda = OrdiniZelda.getOrdini(dataDa, dataA);
		
		ordini = new ArrayList<Ordine>();
		if (ordiniEbay!=null) ordini.addAll(ordiniEbay);
		if (ordiniAmazon!=null) ordini.addAll(ordiniAmazon);
		if (ordiniZelda!=null) ordini.addAll(ordiniZelda);
		
		return Ordine_DAO.elaboraOrdini(ordini);
	}
	
	public String downloadOrdini(Date dataDa, Date dataA, boolean ebay, boolean zb, boolean amazon){
		ordini = new ArrayList<Ordine>();
		
		if (ebay){
			List<Ordine> ordiniEbay = EbayGetOrders.getOrdini(dataDa, dataA);
			ordini.addAll(ordiniEbay);
		}
		if (amazon){
			List<Ordine> ordiniAmazon = AmazonListOrders.scaricaOrdini(dataDa, dataA);
			ordini.addAll(ordiniAmazon);
		}
		if (zb){
			List<Ordine> ordiniZelda = OrdiniZelda.getOrdini(dataDa, dataA);
			ordini.addAll(ordiniZelda);
		}
		
		//return ordini.size() +" ordini trovati";
		return Ordine_DAO.elaboraOrdini(ordini);
	}
	
	public List<Ordine> reloadOrdini(){
		ordini = null;
		return getOrdini(this.dataDa, this.dataA, filtroOrdini);
	}
	
	public List<Ordine> reloadOrdini(Date dataDa, Date dataA, String filtro){
		this.dataDa = dataDa;
		this.dataA = dataA;
		this.filtroOrdini = filtro;
		ordini = null;
		return getOrdini(this.dataDa, this.dataA, filtroOrdini);
	}
	
	public Map<String,List<ArticoloAcquistato>> getMappaOrdiniVsArticoli(){
		if (mappaOrdiniVsArticoli==null)
			mappaOrdiniVsArticoli = Ordine_DAO.getMappaOrdiniConListaArticoli(null,null,null);
		return mappaOrdiniVsArticoli;
	}
	
	public void reloadMappaOrdiniVsArticoli(){
		mappaOrdiniVsArticoli = null;
	}
	
	public List<Ordine> eliminaOrdine(int idOrdine){
		Ordine_DAO.eliminaOrdine(idOrdine);
		reloadOrdini();
		reloadMappaOrdiniVsArticoli();
		return getOrdini(this.dataDa, this.dataA, filtroOrdini);
	}
	
	public void modificaArticolo(Articolo a, String idOrdine){
		Ordine_DAO.modificaArticoloVsOrdine(a, idOrdine,null, null);
	}

	public void modificaOrdine(Ordine o){
		Ordine_DAO.modificaOrdineCompleta(o,null, null);
	}

}
