package it.swb.ebay;

import it.swb.log.Log;
import it.swb.model.Articolo;
import com.ebay.sdk.ApiContext;
import com.ebay.sdk.call.AddItemCall;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.ebay.soap.eBLBaseComponents.SiteCodeType;

public class EbayAddItem {
	
	  public static String[] addItem(Articolo art) {
		  	String[] risultato = {"0","Si è verificato qualche problema."};
		    try {

		      Log.info("Creazione inserzione su eBay in corso...");
		      
		      // [Step 1] Initialize eBay ApiContext object
		      ApiContext apiContext = EbayApiUtility.getApiContext(art.getInfoEbay().getAmbiente());
		      
		      // [Step 2] Create a new item object.
		      ItemType item = EbayApiUtility.buildItem(art);
		      
		      // [Step 3] Create call object and execute the call.		      
		      AddItemCall aic = new AddItemCall(apiContext);		      
		      aic.setSite(SiteCodeType.ITALY);		      
		      aic.setItem(item);
		      
		      Log.debug("Inizio della chiamata alle API eBay, attendere...");
		      
		      //@SuppressWarnings("unused")
			  aic.addItem();

		      Log.info("Inserzione creata correttamente, ID: " + item.getItemID());
		      
		      risultato[0] = "1";
		      risultato[1] = item.getItemID();
		       
		      //EbayApiUtility.assegnaRegoleDiProgrammazione(item.getItemID(), art.getInfoEbay().getAmbiente());
		    }
		    catch(Exception e) {
		      Log.info("!!! Inserzione NON creata. Si è verificato qualche problema.");
		      risultato[0] = "0";
		      risultato[1] = e.toString();
		      e.printStackTrace();
			  Log.info(e);
		    }
		    return risultato;
		  }
	

}