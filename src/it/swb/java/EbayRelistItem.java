package it.swb.java;

import it.swb.model.Articolo;

import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiException;
import com.ebay.sdk.SdkException;
import com.ebay.sdk.call.EndFixedPriceItemCall;
import com.ebay.sdk.call.RelistItemCall;
import com.ebay.soap.eBLBaseComponents.EndReasonCodeType;
import com.ebay.soap.eBLBaseComponents.ItemType;

public class EbayRelistItem {
	
   public static void main(String[] args) {
      endFixedPriceItem("171259261683");
//      endFixedPriceItem("171252851450");
//      endFixedPriceItem("181334745942");
//      endFixedPriceItem("181324945155");
//      endFixedPriceItem("171252251677");
//      endFixedPriceItem("171253114069");
//      endFixedPriceItem("171251009968");
//      endFixedPriceItem("181333491465");
//      endFixedPriceItem("181333510546");
//      endFixedPriceItem("171251207013");
//      endFixedPriceItem("181333633312");
        
        
//        List<Articolo> list = Articolo_DAO.getArticoli("select * from articoli where codice='TEST_15'");
//        
//        for (Articolo a : list){
//        	relistItem(a, "181340822770");
//        }
	   
	   //	ebayDetails();
   }
	
    public static boolean endItem(String itemID){
    	boolean ok = false;
    	ApiContext apiContext = EbayApiUtility.getApiContext(null);
        
        EndFixedPriceItemCall endItem = new EndFixedPriceItemCall(apiContext);
        
        //endItem.setSKU("CODE_SAMPLE_RELIST_VARIATION_SKU2");
        endItem.setItemID(itemID);
        endItem.setEndingReason(EndReasonCodeType.OTHER_LISTING_ERROR);
        
        try {
             endItem.endFixedPriceItem();
             ok = true;
        } catch (ApiException e) {
        	System.out.println(e.getMessage());
           //  e.printStackTrace();
        } catch (SdkException e) {
             e.printStackTrace();
        } catch (Exception e) {
             e.printStackTrace();
        }
        return ok;
   }
    
    public static String endFixedPriceItem(String itemID){
    	String chiudi = "";
    	ApiContext apiContext = EbayApiUtility.getApiContext(null);
        
        EndFixedPriceItemCall endItem = new EndFixedPriceItemCall(apiContext);
        
        //endItem.setSKU("CODE_SAMPLE_RELIST_VARIATION_SKU2");
        endItem.setItemID(itemID);
        endItem.setEndingReason(EndReasonCodeType.OTHER_LISTING_ERROR);
        
        try {
             endItem.endFixedPriceItem();
             chiudi = "ok";
        } catch (ApiException e) {
        	chiudi = e.getMessage();
           e.printStackTrace();
        } catch (SdkException e) {
             e.printStackTrace();
        } catch (Exception e) {
             e.printStackTrace();
        }
        return chiudi;
   }
    
    
    public static String relistItem(Articolo a, String itemID){
    	String id = "";
    	ApiContext apiContext = EbayApiUtility.getApiContext(null);
        
        RelistItemCall ric = new RelistItemCall(apiContext);
        
        ItemType item = EbayApiUtility.buildItem(a);	
        item.setItemID(itemID);
		ric.setItemToBeRelisted(item);
        
        try {
        	ric.relistItem();
        	id = item.getItemID();
        } catch (ApiException e) {
             e.printStackTrace();
        } catch (SdkException e) {
             e.printStackTrace();
        } catch (Exception e) {
             e.printStackTrace();
        }
        return id;
   }
    
    
}
