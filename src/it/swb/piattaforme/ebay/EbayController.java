package it.swb.piattaforme.ebay;

import java.util.List;
import java.util.Map;

import it.swb.model.Articolo;
import it.swb.model.Variante_Articolo;

public class EbayController {
	
	public static String[] creaInserzione(Articolo a){
		return EbayAddItem.addItem(a);
	}
	
	public static void modificaInserzione(Articolo a, String itemID){
		EbayReviseItem.reviseItem(a,itemID);
	}
	
	public static boolean modificaInserzioneStores(Articolo a, String itemID){
		return EbayReviseItem.reviseItemStoresFixedPrice(a,itemID);
	}
	
	public static Object[] ottieniInformazioniDaID(String itemID, Map<String,Boolean> cosaScaricare){
		return EbayGetItem.getItem(itemID, cosaScaricare);
	}
	
	public static List<Variante_Articolo> ottieniVariantiDaID(String itemID){
		return EbayGetItem.getVariantiInserzione(itemID);
	}
	
	public static String chiudiInserzione(String itemID){
		return EbayRelistItem.endFixedPriceItem(itemID);
	}
	
	public static String rimettiInVendita(Articolo a, String itemID){
		return EbayRelistItem.relistItem(a,itemID);
	}

}
