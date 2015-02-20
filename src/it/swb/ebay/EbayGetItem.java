package it.swb.ebay;

import it.swb.log.Log;
import it.swb.model.InfoEbay;
import it.swb.model.Variante_Articolo;
import it.swb.utility.Costanti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ebay.sdk.ApiException;
import com.ebay.sdk.SdkSoapException;
import com.ebay.sdk.call.GetItemCall;
import com.ebay.soap.eBLBaseComponents.DetailLevelCodeType;
import com.ebay.soap.eBLBaseComponents.ErrorType;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.ebay.soap.eBLBaseComponents.NameValueListArrayType;
import com.ebay.soap.eBLBaseComponents.NameValueListType;
import com.ebay.soap.eBLBaseComponents.PicturesType;
import com.ebay.soap.eBLBaseComponents.StorefrontType;
import com.ebay.soap.eBLBaseComponents.VariationSpecificPictureSetType;
import com.ebay.soap.eBLBaseComponents.VariationType;
import com.ebay.soap.eBLBaseComponents.VariationsType;

public class EbayGetItem {
	
	public static void main(String[] args){
		
		//getItem("170970061839");
		
		getVariantiInserzione("181140545204");
	}
	
	
	
	/* dato l'ID di un inserzione ebay mi restituisce un array di 3 elementi,
	 *  contenente al primo posto la descrizione dell'articolo (oggetto InfoEbay), 
	 *  al secondo la lista di eventuali varianti e al terzo la quantità residua (iniziale meno venduto)*/
	public static Object[] getItem(String itemID, Map<String,Boolean> cosaScaricare) {
		
		Log.debug("Inizio download informazioni dall'inserzione eBay "+itemID+" ...");
		Object[] oggetti = new Object[3];
		
		InfoEbay ie = new InfoEbay();
		
        GetItemCall gic = new GetItemCall(EbayApiUtility.getApiContext("produzione"));

        gic.setDetailLevel(new DetailLevelCodeType[]{DetailLevelCodeType.RETURN_ALL,DetailLevelCodeType.ITEM_RETURN_ATTRIBUTES});
        gic.setItemID(itemID);
        
        ItemType itemReturned = null;
        
        try {                        
            itemReturned = gic.getItem();
            
            if (cosaScaricare.get("titolo")) //titolo inserzione
            	ie.setTitoloInserzione(itemReturned.getTitle());
            
            if (cosaScaricare.get("categorie")) {//categorie
	            if (itemReturned.getPrimaryCategory()!=null && itemReturned.getPrimaryCategory().getCategoryID()!=null && !itemReturned.getPrimaryCategory().getCategoryID().isEmpty())
	            {
	            	ie.setIdCategoriaEbay1(itemReturned.getPrimaryCategory().getCategoryID());
	            	ie.setNomeCategoriaEbay1(itemReturned.getPrimaryCategory().getCategoryName());
	            }
	            if (itemReturned.getSecondaryCategory()!=null && itemReturned.getSecondaryCategory().getCategoryID()!=null && !itemReturned.getSecondaryCategory().getCategoryID().isEmpty())
	            {
	            	ie.setIdCategoriaEbay2(itemReturned.getSecondaryCategory().getCategoryID());
	            	ie.setNomeCategoriaEbay2(itemReturned.getSecondaryCategory().getCategoryName());
	            }
            }
            
            if (cosaScaricare.get("prezzo")) //prezzo
                ie.setPrezzo(itemReturned.getStartPrice().getValue());
            
            if (cosaScaricare.get("descrizione")) //descrizione
            	ie.setDescrizioneEbay(itemReturned.getDescription());
            
            
            oggetti[0] = ie;
            
            if (cosaScaricare.get("varianti")) { //varianti
	            // Cerco di ricostruire le varianti, se ci sono
	            if (itemReturned.getVariations()!=null){
	            	
	            	VariationsType vt = itemReturned.getVariations();
	            	VariationType[] variants = vt.getVariation();
	            	
	            	//oggetto che contiene l'array per le immagini
	            	PicturesType pic = vt.getPictures(0);
	            	//array per le immagini (contiene oggetti immagini-valore variante)
	            	VariationSpecificPictureSetType[] vspSet = pic.getVariationSpecificPictureSet();
	            	
	            	List<Variante_Articolo> varianti_articolo = new ArrayList<Variante_Articolo>();
	            	
	            	for (int i=0;i<variants.length;i++){
	            		VariationType var = variants[i];
	            		
	            		Variante_Articolo v = new Variante_Articolo();
	            		
	            		v.setQuantita(var.getQuantity()-var.getSellingStatus().getQuantitySold());
	            		
	            		NameValueListArrayType nvListArraySpec = var.getVariationSpecifics();
	            		NameValueListType typeVariationSpec = nvListArraySpec.getNameValueList(0);
	            		
	            		v.setTipo(typeVariationSpec.getName());
	            		v.setValore(typeVariationSpec.getValue(0));
	            		
	            		VariationSpecificPictureSetType vsp = vspSet[i];	
	            		try {
	            			v.setImmagine(vsp.getPictureURL(0).toLowerCase().replace(Costanti.vecchioPercorsoImmaginiRemoto.toLowerCase(), ""));            		
	            		} catch (Exception e){
	            			Log.info(e);
	            		}
	            		
	            		varianti_articolo.add(v);
	            		
	//            		System.out.println("TIPO VARIANTE: "+v.getTipo());
	//            		System.out.println("VALORE: "+v.getValore());
	//            		System.out.println("IMMAGINE: "+v.getImmagine());
	//            		System.out.println("QUANTITA: "+v.getQuantita());
	            	}
	            	oggetti[1] = varianti_articolo;
	            } 
	            else oggetti[1] = null;
            }
            else oggetti[1] = null;
            
            //System.out.println("Quantita' iniziale: "+itemReturned.getQuantity()+", quantita' venduta: "+itemReturned.getSellingStatus().getQuantitySold());
            oggetti[2] = itemReturned.getQuantity()-itemReturned.getSellingStatus().getQuantitySold();
            
            
//            AttributeSetArrayType attrSetArrayType =itemReturned.getAttributeSetArray();
//            
//            if (attrSetArrayType != null){
//                AttributeSetType[] attributes = attrSetArrayType.getAttributeSet();
//                
//                for (int index=0; index<attributes.length; index++){
//                    AttributeSetType theSet = attributes[index];
//                   //check if it's an event ticket attributeSet                    
//                   if (theSet.getAttributeSetID() ==1){
//                        getTicketAttr(theSet,itemReturned);
//                    }
//                }
//            }      
            
            Log.debug("Informazioni ottenute correttamente.");
            
        }catch (Exception e) {
        	Log.info(e);
            if( e instanceof SdkSoapException ){
                 SdkSoapException sdkExe = (SdkSoapException)e;
                ErrorType errs = sdkExe.getErrorType();
                System.out.println("SdkSoapException error code " +errs.getErrorCode()+ "error shot message" + errs.getShortMessage());
            }
            if (e instanceof ApiException ){
                ApiException apiExe = (ApiException)e;
                ErrorType[] errs = apiExe.getErrors();
                for (int j=0; j<errs.length; j++){
                    System.out.println("ApiException error code " +errs[j].getErrorCode()+ "error shot message" + errs[j].getShortMessage());
                }
            }          
        }
        return oggetti;
    }
	
	
	public static Map<String,Object> scaricaInserzione(String itemID, Map<String,Boolean> cosaScaricare) {
		
		Log.debug("Inizio download informazioni dall'inserzione eBay "+itemID+" ...");
		Map<String,Object> map = new HashMap<String,Object>();
		
		InfoEbay ie = new InfoEbay();
		
        GetItemCall gic = new GetItemCall(EbayApiUtility.getApiContext("produzione"));

        gic.setDetailLevel(new DetailLevelCodeType[]{DetailLevelCodeType.RETURN_ALL,DetailLevelCodeType.ITEM_RETURN_ATTRIBUTES});
        gic.setItemID(itemID);
        
        ItemType itemReturned = null;
        
        try {                        
            itemReturned = gic.getItem();
            
            if (cosaScaricare.get("titolo")) //titolo inserzione
            	ie.setTitoloInserzione(itemReturned.getTitle());
            
            if (cosaScaricare.get("categorie")) {//categorie ebay
	            if (itemReturned.getPrimaryCategory()!=null && itemReturned.getPrimaryCategory().getCategoryID()!=null && !itemReturned.getPrimaryCategory().getCategoryID().isEmpty())
	            {
	            	ie.setIdCategoriaEbay1(itemReturned.getPrimaryCategory().getCategoryID());
	            	ie.setNomeCategoriaEbay1(itemReturned.getPrimaryCategory().getCategoryName());
	            }
	            if (itemReturned.getSecondaryCategory()!=null && itemReturned.getSecondaryCategory().getCategoryID()!=null && !itemReturned.getSecondaryCategory().getCategoryID().isEmpty())
	            {
	            	ie.setIdCategoriaEbay2(itemReturned.getSecondaryCategory().getCategoryID());
	            	ie.setNomeCategoriaEbay2(itemReturned.getSecondaryCategory().getCategoryName());
	            }
            }
            
            if (cosaScaricare.get("categorie_negozio")) {//categorie interne del negozio
            	if (itemReturned.getStorefront()!=null){
            		StorefrontType sf = itemReturned.getStorefront();
            		long id_cat = sf.getStoreCategoryID();
            		ie.setIdCategoriaNegozio1(id_cat);
            	}
            }
            
            if (cosaScaricare.get("prezzo")) //prezzo
                ie.setPrezzo(itemReturned.getStartPrice().getValue());
            
            if (cosaScaricare.get("descrizione")) //descrizione
            	ie.setDescrizioneEbay(itemReturned.getDescription());
            
            map.put("info_ebay", ie);
            
            if (cosaScaricare.get("varianti")) { //varianti
	            // Cerco di ricostruire le varianti, se ci sono
	            if (itemReturned.getVariations()!=null){
	            	
	            	VariationsType vt = itemReturned.getVariations();
	            	VariationType[] variants = vt.getVariation();
	            	
	            	//oggetto che contiene l'array per le immagini
	            	PicturesType pic = vt.getPictures(0);
	            	//array per le immagini (contiene oggetti immagini-valore variante)
	            	VariationSpecificPictureSetType[] vspSet = pic.getVariationSpecificPictureSet();
	            	
	            	List<Variante_Articolo> varianti_articolo = new ArrayList<Variante_Articolo>();
	            	
	            	for (int i=0;i<variants.length;i++){
	            		VariationType var = variants[i];
	            		
	            		Variante_Articolo v = new Variante_Articolo();
	            		
	            		v.setQuantita(var.getQuantity()-var.getSellingStatus().getQuantitySold());
	            		
	            		NameValueListArrayType nvListArraySpec = var.getVariationSpecifics();
	            		NameValueListType typeVariationSpec = nvListArraySpec.getNameValueList(0);
	            		
	            		v.setTipo(typeVariationSpec.getName());
	            		v.setValore(typeVariationSpec.getValue(0));
	            		
	            		VariationSpecificPictureSetType vsp = vspSet[i];	
	            		try {
	            			v.setImmagine(vsp.getPictureURL(0).toLowerCase().replace(Costanti.vecchioPercorsoImmaginiRemoto.toLowerCase(), ""));            		
	            		} catch (Exception e){
	            			Log.info(e);
	            		}
	            		
	            		varianti_articolo.add(v);
	            		
	//            		System.out.println("TIPO VARIANTE: "+v.getTipo());
	//            		System.out.println("VALORE: "+v.getValore());
	//            		System.out.println("IMMAGINE: "+v.getImmagine());
	//            		System.out.println("QUANTITA: "+v.getQuantita());
	            	}
	            	map.put("varianti", varianti_articolo);
	            } 
	            else map.put("varianti", null);
            }
            else map.put("varianti", null);
            
            int quantita_residua = itemReturned.getQuantity()-itemReturned.getSellingStatus().getQuantitySold();  
            map.put("quantita_residua", quantita_residua);           
            
            Log.debug("Informazioni ottenute correttamente.");
            
        }catch (Exception e) {
        	Log.info(e);
            if( e instanceof SdkSoapException ){
                 SdkSoapException sdkExe = (SdkSoapException)e;
                ErrorType errs = sdkExe.getErrorType();
                System.out.println("SdkSoapException error code " +errs.getErrorCode()+ "error shot message" + errs.getShortMessage());
            }
            if (e instanceof ApiException ){
                ApiException apiExe = (ApiException)e;
                ErrorType[] errs = apiExe.getErrors();
                for (int j=0; j<errs.length; j++){
                    System.out.println("ApiException error code " +errs[j].getErrorCode()+ "error shot message" + errs[j].getShortMessage());
                }
            }          
        }
        return map;
    }
	
	
	public static List<Variante_Articolo> getVariantiInserzione(String itemID) {
		
		List<Variante_Articolo> varianti_articolo = null;
		
        GetItemCall gic = new GetItemCall(EbayApiUtility.getApiContext("produzione"));

        gic.setDetailLevel(new DetailLevelCodeType[]{DetailLevelCodeType.RETURN_ALL,DetailLevelCodeType.ITEM_RETURN_ATTRIBUTES});
        gic.setItemID(itemID);
        
        ItemType itemReturned = null;
        
        try {                        
            itemReturned = gic.getItem();
            
            // Cerco di ricostruire le varianti, se ci sono (MORTACCI LORO)
            if (itemReturned.getVariations()!=null){
            	
            	VariationsType vt = itemReturned.getVariations();
            	VariationType[] variants = vt.getVariation();
            	
            	//oggetto che contiene l'array per le immagini
            	PicturesType pic = vt.getPictures(0);
            	//array per le immagini (contiene oggetti immagini-valore variante)
            	VariationSpecificPictureSetType[] vspSet = pic.getVariationSpecificPictureSet();
            	
            	varianti_articolo = new ArrayList<Variante_Articolo>();
            	
            	for (int i=0;i<variants.length;i++){
            		VariationType var = variants[i];
            		
            		Variante_Articolo v = new Variante_Articolo();
            		
            		v.setQuantita(var.getQuantity()-var.getSellingStatus().getQuantitySold());
            		
            		NameValueListArrayType nvListArraySpec = var.getVariationSpecifics();
            		NameValueListType typeVariationSpec = nvListArraySpec.getNameValueList(0);
            		
            		v.setTipo(typeVariationSpec.getName());
            		v.setValore(typeVariationSpec.getValue(0));
            		
            		VariationSpecificPictureSetType vsp = vspSet[i];
            		if (vsp!=null && vsp.getPictureURL().length!=0)
            			v.setImmagine(vsp.getPictureURL(0).toLowerCase().replace(Costanti.vecchioPercorsoImmaginiRemoto.toLowerCase(), ""));      
            		
            		varianti_articolo.add(v);
            		
            		System.out.println("TIPO VARIANTE: "+v.getTipo());
            		System.out.println("VALORE: "+v.getValore());
            		System.out.println("IMMAGINE: "+v.getImmagine());
            		System.out.println("QUANTITA: "+v.getQuantita());
            	}
            } 
            
            //System.out.println("Get Varianti Inserzione "+itemID+": "+varianti_articolo.size()+" varianti ottenute");
            
            
        }catch (Exception e) {
        	e.printStackTrace();
            if( e instanceof SdkSoapException ){
                 SdkSoapException sdkExe = (SdkSoapException)e;
                ErrorType errs = sdkExe.getErrorType();
                System.out.println("SdkSoapException error code " +errs.getErrorCode()+ "error shot message" + errs.getShortMessage());
            }
            if (e instanceof ApiException ){
                ApiException apiExe = (ApiException)e;
                ErrorType[] errs = apiExe.getErrors();
                for (int j=0; j<errs.length; j++){
                    System.out.println("ApiException error code " +errs[j].getErrorCode()+ "error shot message" + errs[j].getShortMessage());
                }
            }          
        }
        return varianti_articolo;
    }

/*
 *  obtain event ticket item specific data
 */
//	private void getTicketAttr(AttributeSetType attrSet,ItemType item ){
//
//       // create a TicketBean object for storing the attribute data
//       // or you can store data to a Collection 
//        TicketBean ticket = new TicketBean();
//        ticket.setItemId(item.getItemID().getValue());
//        ticket.setStartPrice(item.getStartPrice().getValue());
//        AttributeType[] attrTypeArray = attrSet.getAttribute();
//        
//        for(int attrIndex=0; attrIndex<attrTypeArray.length; attrIndex++){
//            AttributeType theAttr = attrTypeArray[attrIndex];
//           
//            //<Attribute labelVisible='true' id='10399' > <Label><![CDATA[Row]]
//            if (theAttr.getAttributeID()== 10399){
//                ticket.setRow(theAttr.getValue(0).getValueLiteral());
//            }
//            //<Attribute labelVisible='true' id='10400' > <Label><![CDATA[Section]]>
//            if (theAttr.getAttributeID() ==10400){
//                ticket.setSection(theAttr.getValue(0).getValueLiteral());
//            }
//            //<Attribute labelVisible='true' IsRequired='true' id='1' > <Label><![CDATA[Number of Tickets]]>
//            if (theAttr.getAttributeID()==1){
//                ticket.setNumOfTicket(theAttr.getValue(0).getValueLiteral());
//            }
//            // <Attribute labelVisible='true' id='2' > <Label><![CDATA[Month]]>
//            if (theAttr.getAttributeID()==2){
//                ticket.setMonth(theAttr.getValue(0).getValueLiteral());
//            }
//            //<Attribute labelVisible='true' id='44812' parentAttrId='2' > <Label><![CDATA[Day]]>
//            if (theAttr.getAttributeID()==44812){
//                ticket.setDay(theAttr.getValue(0).getValueLiteral());
//            }
//            //<Attribute labelVisible='true' id='44811' > <Label><![CDATA[Year]]>
//            if (theAttr.getAttributeID()==44811){
//                ticket.setYear(theAttr.getValue(0).getValueLiteral());
//            }
//            //<Attribute labelVisible='true' id='63' parentAttrId='10' > <Label><![CDATA[Venue Name]]>
//            if (theAttr.getAttributeID()==63){
//                ticket.setVenueName(theAttr.getValue(0).getValueLiteral());
//            }
//        }
//        System.out.println("The itemId : "+ ticket.getItemId());
//        System.out.println("The ticket's ROW -> " + ticket.getRow());
//        System.out.println("The number of ticket -> " + ticket.getNumOfTicket());
//        System.out.println("The Venue Name: " +ticket.getVenueName());
//        System.out.println("The ticket is on " +ticket.getMonth()+" "+ticket.getDay()+", "+ticket.getYear());
//        System.out.println("The Item's start price : "+ticket.getStartPrice());
//    }

}
