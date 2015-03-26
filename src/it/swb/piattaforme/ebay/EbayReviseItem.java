package it.swb.piattaforme.ebay;

import it.swb.log.Log;
import it.swb.model.Articolo;

import com.ebay.sdk.ApiException;
import com.ebay.sdk.call.ReviseItemCall;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.ebay.soap.eBLBaseComponents.ListingTypeCodeType;
import com.ebay.soap.eBLBaseComponents.SiteCodeType;

public class EbayReviseItem {
	       
	public static void reviseItem(Articolo a, String itemID) {
		ItemType item = EbayApiUtility.buildItem(a);	
		item.setItemID(itemID.trim());
		
		try {		
			
			ReviseItemCall ric = new ReviseItemCall(EbayApiUtility.getApiContext("produzione"));
			ric.setSite(SiteCodeType.ITALY);
			ric.setItemToBeRevised(item);
			
//			ric.setPictureFiles(pictureFiles);
//			PictureDetailsType pdt = new PictureDetailsType();
//			pdt.setGalleryType(GalleryTypeCodeType.GALLERY);						
//			item.setItemID(new ItemIDType(itemId));
//			item.setPictureDetails(pdt);
			
			Log.debug("Inizio della chiamata alle API eBay, attendere...");
			
			//@SuppressWarnings("unused")
			//FeesType fees;
			
			try{
				//fees = 
				ric.reviseItem();
			} 
			catch(ApiException ae){
				Log.error(ae.getMessage());
				Log.debug("...Se ha dato l'errore sul tipo di inserzione, provo con STORES_FIXED_PRICE...");
				try{
					item.setListingType(ListingTypeCodeType.STORES_FIXED_PRICE);
					//fees = 
					ric.reviseItem();
				}catch(ApiException ae2){
					Log.info(ae2);
					ae2.printStackTrace();
				}
			}
			Log.debug("Fine della chiamata alle API eBay.");			
		    Log.info("ID dell'inserzione: " + item.getItemID());		
		
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static boolean reviseItemStoresFixedPrice(Articolo a, String itemID) {
		ItemType item = EbayApiUtility.buildItem(a);	
		item.setItemID(itemID.trim());
		boolean ok = false;
		
		Log.debug("Inizio aggiornamento inserzione eBay "+itemID+" relativa all'articolo "+a.getCodice()+" ...");
		
		try {		
			
			ReviseItemCall ric = new ReviseItemCall(EbayApiUtility.getApiContext("produzione"));
			ric.setSite(SiteCodeType.ITALY);
			ric.setItemToBeRevised(item);
			item.setListingType(ListingTypeCodeType.STORES_FIXED_PRICE);
			
			ric.reviseItem();
			
			ok = true;
			
			Log.debug("Fine aggiornamento inserzione eBay.");			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ok;
	}
}
