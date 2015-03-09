package it.swb.ebay;

import it.swb.log.Log;
import it.swb.model.Articolo;
import it.swb.model.InfoEbay;
import it.swb.model.Variante_Articolo;
import it.swb.utility.Costanti;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.ebay.sdk.ApiAccount;
import com.ebay.sdk.ApiCall;
import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.ApiLogging;
import com.ebay.sdk.call.GetCategoryFeaturesCall;
import com.ebay.soap.eBLBaseComponents.AmountType;
import com.ebay.soap.eBLBaseComponents.BuyerPaymentMethodCodeType;
import com.ebay.soap.eBLBaseComponents.CategoryFeatureType;
import com.ebay.soap.eBLBaseComponents.CategoryType;
import com.ebay.soap.eBLBaseComponents.ConditionEnabledCodeType;
import com.ebay.soap.eBLBaseComponents.CountryCodeType;
import com.ebay.soap.eBLBaseComponents.CurrencyCodeType;
import com.ebay.soap.eBLBaseComponents.DetailLevelCodeType;
import com.ebay.soap.eBLBaseComponents.InsuranceOptionCodeType;
import com.ebay.soap.eBLBaseComponents.InternationalShippingServiceOptionsType;
import com.ebay.soap.eBLBaseComponents.InventoryTrackingMethodCodeType;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.ebay.soap.eBLBaseComponents.ListingDurationCodeType;
import com.ebay.soap.eBLBaseComponents.ListingTypeCodeType;
import com.ebay.soap.eBLBaseComponents.NameValueListArrayType;
import com.ebay.soap.eBLBaseComponents.NameValueListType;
import com.ebay.soap.eBLBaseComponents.PictureDetailsType;
import com.ebay.soap.eBLBaseComponents.PicturesType;
import com.ebay.soap.eBLBaseComponents.ReturnPolicyType;
import com.ebay.soap.eBLBaseComponents.SellingManagerAutoRelistOptionCodeType;
import com.ebay.soap.eBLBaseComponents.SellingManagerAutoRelistType;
import com.ebay.soap.eBLBaseComponents.SellingManagerAutoRelistTypeCodeType;
import com.ebay.soap.eBLBaseComponents.SetSellingManagerItemAutomationRuleRequestType;
import com.ebay.soap.eBLBaseComponents.ShippingDetailsType;
import com.ebay.soap.eBLBaseComponents.ShippingRegionCodeType;
import com.ebay.soap.eBLBaseComponents.ShippingServiceCodeType;
import com.ebay.soap.eBLBaseComponents.ShippingServiceOptionsType;
import com.ebay.soap.eBLBaseComponents.ShippingTypeCodeType;
import com.ebay.soap.eBLBaseComponents.SiteCodeType;
import com.ebay.soap.eBLBaseComponents.StorefrontType;
import com.ebay.soap.eBLBaseComponents.TaxJurisdictionType;
import com.ebay.soap.eBLBaseComponents.TaxTableType;
import com.ebay.soap.eBLBaseComponents.VariationSpecificPictureSetType;
import com.ebay.soap.eBLBaseComponents.VariationType;
import com.ebay.soap.eBLBaseComponents.VariationsType;

public class EbayApiUtility {
	
	  public static ApiContext getApiContext(String ambiente) {
		  	String DEV_ID = "";
		    String APP_ID = "";
		    String CERT_ID = "";
		    String TOKEN = "";
		    String API_URL = "";	  		    
		    Properties config = new Properties();	
		    
		    ApiContext apiContext = new ApiContext();
		    
		    try {
				config.load(EbayApiUtility.class.getResourceAsStream("/ebay.properties"));
			
		      if (ambiente!=null && ambiente.equals("sandbox")){
		    	  DEV_ID = config.getProperty("Sandbox_DevID");
		    	  APP_ID = config.getProperty("Sandbox_AppID");
		    	  CERT_ID = config.getProperty("Sandbox_CertID");
		    	  TOKEN = config.getProperty("Sandbox_Token");
		    	  API_URL = config.getProperty("Sandbox_API_ServerURL");		      
		      } else {
		    	  DEV_ID = config.getProperty("Production_DevID");
		    	  APP_ID = config.getProperty("Production_AppID");
		    	  CERT_ID = config.getProperty("Production_CertID");
		    	  TOKEN = config.getProperty("Production_Token");
		    	  API_URL = config.getProperty("Production_API_ServerURL");
		      }
		    
		    ApiAccount apiAccount =  new ApiAccount();
		    apiAccount.setDeveloper(DEV_ID);
		    apiAccount.setApplication(APP_ID);
		    apiAccount.setCertificate(CERT_ID);

		    ApiCredential apiCredential = new ApiCredential();
		    apiCredential.setApiAccount(apiAccount);
		    apiCredential.seteBayToken(TOKEN);

		    		    
		    apiContext.setApiCredential(apiCredential);
		    apiContext.setApiServerUrl(API_URL);  
		    apiContext.setSite(SiteCodeType.ITALY); 
		    apiContext.setErrorLanguage("it_IT");
	        apiContext.setApiLogging(new ApiLogging());
		    
//			Log.debug("getApiContext: "+ambiente);
//			Log.debug("DEV_ID: "+DEV_ID);
//			Log.debug("APP_ID: "+APP_ID);
//			Log.debug("CERT_ID: "+CERT_ID);
//			Log.debug("TOKEN: "+TOKEN);
//			Log.debug("API_URL: "+API_URL);
		    
		    } catch (IOException e) {
				Log.error(e.getMessage());
				e.printStackTrace();
			}
	      return apiContext;
	  }
	  
	  
	  public static ItemType buildItem(Articolo art) {
		  ItemType item = new ItemType();
		  
		  try{		      
		      /*	If you want to use SKU instead of ItemID as a unique identifier (such as when retrieving items and orders), you can 
		       * set Item.InventoryTrackingMethod to SKU in AddFixedPriceItem and related calls. In this case, the SKU must be unique across
		       *  your (the seller's) active listings. Note that if you relist the item, you must reset Item.InventoryTrackingMethod to SKU; 
		       *  otherwise the relisted item will default to ItemID as the tracking method. 	*/
		      item.setInventoryTrackingMethod(InventoryTrackingMethodCodeType.SKU);
		      
		      InfoEbay ei = art.getInfoEbay();
		      
		      //CODICE ARTICOLO
		      item.setSKU(art.getCodice());
	
		      // TITOLO
		      item.setTitle(ei.getTitoloInserzione());
		      
		      // DESCRIZIONE
		      item.setDescription(ei.getDescrizioneEbay());
		      
		      // TIPO DI INSERZIONE
		      item.setListingType(ListingTypeCodeType.FIXED_PRICE_ITEM);
		      
		      // PREZZO
		      item.setCurrency(CurrencyCodeType.EUR);
		      
		      double prezzo = art.getPrezzoDettaglio();
	    	  if (art.getPrezzoPiattaforme()>0) prezzo = art.getPrezzoPiattaforme();
	
		      AmountType amount = new AmountType();
		      amount.setValue(prezzo);
		      
		      item.setStartPrice(amount);
		      
		      // DURATA INSERZIONE	      
		        
		      if (ei.getDurataInserzione()==999)
		    	  item.setListingDuration(ListingDurationCodeType.GTC.value());	    	    
		      else if (ei.getDurataInserzione()==1)
		    	  item.setListingDuration(ListingDurationCodeType.DAYS_1.value());	
		      else if (ei.getDurataInserzione()==3)
		    	  item.setListingDuration(ListingDurationCodeType.DAYS_3.value());	 
		      else if (ei.getDurataInserzione()==5)
		    	  item.setListingDuration(ListingDurationCodeType.DAYS_5.value());	 
		      else if (ei.getDurataInserzione()==7)
		    	  item.setListingDuration(ListingDurationCodeType.DAYS_7.value());	 
		      else if (ei.getDurataInserzione()==10)
		    	  item.setListingDuration(ListingDurationCodeType.DAYS_10.value());	 
		      else if (ei.getDurataInserzione()==30)
		    	  item.setListingDuration(ListingDurationCodeType.DAYS_30.value());
		        
		      	      
		      // LUOGO IN CUI SI TROVA L'OGGETTO
		      item.setLocation("Santa Severa, RM");
		      item.setCountry(CountryCodeType.IT);
	
		      // CATEGORIE EBAY 
		      if (ei.getIdCategoria1()!=null && !ei.getIdCategoria1().trim().isEmpty()){
		    	  CategoryType cat1 = new CategoryType();
		    	  cat1.setCategoryID(String.valueOf(ei.getIdCategoria1()));
		    	  item.setPrimaryCategory(cat1);
		      }
		      if (ei.getIdCategoria2()!=null && !ei.getIdCategoria2().trim().isEmpty()){
		    	  CategoryType cat2 = new CategoryType();
		    	  cat2.setCategoryID(String.valueOf(ei.getIdCategoria2()));
		    	  item.setSecondaryCategory(cat2);
		      }
	//	      CategoryType cat1 = new CategoryType();
	//	      CategoryType cat2 = new CategoryType();
	//
	//	      cat1.setCategoryID(String.valueOf(ei.getIdCategoria1()));
	//	      cat2.setCategoryID(String.valueOf(ei.getIdCategoria2()));
	//	      
	//	      item.setPrimaryCategory(cat1);
	//	      item.setSecondaryCategory(cat2);
		      
		      // CATEGORIA INTERNA DEL NEGOZIO
		      StorefrontType sf = new StorefrontType();
		      sf.setStoreCategoryID(art.getCategoria().getIdCategoriaEbay());
		      sf.setStoreURL("http://stores.ebay.it/ZELDABOMBONIERE");
		      item.setStorefront(sf);
		      
		      // QUANTITA' TOTALE
		      item.setQuantity(art.getQuantitaMagazzino());
		      item.setQuantityAvailable(art.getQuantitaMagazzino());
		      
		      // METODO PAGAMENTO
		      // TODO rivedere tariffa contrassegno
		      if (ei.isContrassegno())
		      item.setPaymentMethods(new BuyerPaymentMethodCodeType[] 
			                            {BuyerPaymentMethodCodeType.PAY_PAL,BuyerPaymentMethodCodeType.MONEY_XFER_ACCEPTED,
		    		  BuyerPaymentMethodCodeType.MONEY_XFER_ACCEPTED_IN_CHECKOUT,BuyerPaymentMethodCodeType.MOCC,BuyerPaymentMethodCodeType.COD});
		      else 
		    	  item.setPaymentMethods(new BuyerPaymentMethodCodeType[] 
	                  {BuyerPaymentMethodCodeType.PAY_PAL,BuyerPaymentMethodCodeType.MONEY_XFER_ACCEPTED,
		    			  BuyerPaymentMethodCodeType.MONEY_XFER_ACCEPTED_IN_CHECKOUT,BuyerPaymentMethodCodeType.MOCC});
		      
		      // email is required if paypal is used as payment method
		      item.setPayPalEmailAddress("zeldabomboniere@gmail.com");
		      
	
		      // CONDIZIONI OGGETTO (1000=NUOVO)
		      // TODO rivedere
		      boolean specificareCategoria = false;
		      
		      if (!specificheCategoria(ei.getIdCategoria1(),ei.getAmbiente()))
		    	  specificareCategoria = true;
		      
		      if (specificareCategoria){
		    	  item.setConditionID(1000); 		      
			      item.setConditionDefinition("Nuovo");
			      item.setConditionDescription("Nuovo");
			      item.setConditionDisplayName("Nuovo");
		      }
		      // handling time is required
		      item.setDispatchTimeMax(Integer.valueOf(1));
	
		      // DETTAGLI SPEDIZIONE
		      item.setShippingDetails(buildShippingDetails(art.getCostoSpedizione(),ei.isContrassegno(),art.getAliquotaIva()));
		      item.setGetItFast(true);
		      
		      //IMMAGINI
		      PictureDetailsType pdt = new PictureDetailsType();
		      
		      pdt.setExternalPictureURL(new String[]{Costanti.percorsoImmaginiRemoto+art.getImmagine1()});
		      item.setPictureDetails(pdt);
		      
		      // return policy
		      ReturnPolicyType returnPolicy = new ReturnPolicyType();
		      returnPolicy.setReturnsAcceptedOption("ReturnsAccepted");
		      returnPolicy.setRefundOption("MoneyBack");
		      item.setReturnPolicy(returnPolicy);
		      
		      //VARIANTI
		      if (art.getVarianti()!=null && !art.getVarianti().isEmpty()){
		    	  item.setVariations(impostaVarianti(art.getCodice(),art.getVarianti(), prezzo));
		  	  }
		  }
		  catch(Exception e){
			  Log.error(e.getMessage());
			  e.printStackTrace();
		  }
		      return item;
	  }
	  
	  
	  private static ShippingDetailsType buildShippingDetails(double costo_spedizione, boolean contrassegno, float iva)
	  {
	     // Shipping details.
	     ShippingDetailsType sd = new ShippingDetailsType();
	     
	     if (contrassegno){
	    	 AmountType amount = new AmountType();
		     amount.setValue(3);
	    	 sd.setCODCost(amount);
	     }
	     
	     //SPEDIZIONE IMMEDIATA
	     sd.setGetItFast(true);
	     
	     //IL VENDITORE PUò CAMBIARE ISTRUZIONI DI PAGAMENTO DURANTE IL CHECKOUT
	     sd.setChangePaymentInstructions(true);
	     
	     
	     //TASSE (IVA)
	     /*
	     SalesTaxType stt = new SalesTaxType(); * US site only *
	     stt.setSalesTaxState("Italia");
	     stt.setShippingIncludedInTax(true);
	     stt.setSalesTaxPercent((float)iva);
	     sd.setSalesTax(stt);
	     */
	     
	     TaxJurisdictionType tj = new TaxJurisdictionType();
	     tj.setJurisdictionID("IT");
	     tj.setSalesTaxPercent(iva);
	     tj.setShippingIncludedInTax(true);
	     
	     TaxTableType tt = new TaxTableType();
	     tt.setTaxJurisdiction(new TaxJurisdictionType[]{ tj });
	     sd.setTaxTable(tt);
	     
	     //REGOLE
/*	     CalculatedShippingDiscountType csd = new CalculatedShippingDiscountType();
	     csd.setDiscountName(DiscountNameCodeType.EACH_ADDITIONAL_AMOUNT);
	     
	     DiscountProfileType dpt1 = new DiscountProfileType();
	     dpt1.setDiscountProfileID("55551026");
	     dpt1.setDiscountProfileName("CORRIERE ITALIA");
	     AmountType am = new AmountType();
	     am.setValue(0);
	     dpt1.setEachAdditionalAmount(am);
	     dpt1.setEachAdditionalAmountOff(am);
	     dpt1.setMappedDiscountProfileID("55551026");
	     
	     //DiscountProfileType dpt2 = new DiscountProfileType();
	     //dpt2.setDiscountProfileID("215124026");
	     
	     csd.setDiscountProfile(new DiscountProfileType[]{dpt1});
	     sd.setCalculatedShippingDiscount(csd);
*/
	     sd.setShippingDiscountProfileID("55551026"); //CORRIERE ITALIA
	     sd.setInternationalShippingDiscountProfileID("215124026"); //SPEDIZIONE ESTERA
	     
	   //Applica la mia regola per la spedizione promozionale
	     sd.setPromotionalShippingDiscount(true);
	     //sd.setInternationalPromotionalShippingDiscount(true); questa no
	     
	     
	     sd.setApplyShippingDiscount(new Boolean(true));
	     AmountType amount;
	     sd.setPaymentInstructions("");

	     // Shipping type and shipping service options
	     sd.setShippingType(ShippingTypeCodeType.FLAT);
	     sd.setShippingServiceUsed(ShippingServiceCodeType.IT_EXPRESS_COURIER.value());
	     
	     
	     ShippingServiceOptionsType shippingOptions = new ShippingServiceOptionsType();
	     
	     //SERVIZIO DI SPEDIZIONE NAZIONALE
	     shippingOptions.setShippingService(ShippingServiceCodeType.IT_EXPRESS_COURIER.value());
	     
	     //Indica se il servizio è un servizio di trasporto accellerato
	     shippingOptions.setExpeditedService(true);
	     
	     shippingOptions.setShippingTimeMax(1);
	     shippingOptions.setShippingTimeMin(1);
	     
	     //COSTI AGGIUNTIVI
	     amount = new AmountType();
	     amount.setValue(0.0);
	     shippingOptions.setShippingServiceAdditionalCost(amount);
	     
	     //COSTO SPEDIZIONE
	     amount = new AmountType();
	     amount.setValue(costo_spedizione);
	     shippingOptions.setShippingServiceCost(amount);
	     
	     if (costo_spedizione==0)
	    	 shippingOptions.setFreeShipping(true);
	     
	     //PRIORITà CON CUI IL SERVIZIO DI SPEDIZIONE APPARE NELLA PAGINA
	     shippingOptions.setShippingServicePriority(new Integer(1));
	     
	     //ASSICURAZIONE
	     amount = new AmountType();
	     amount.setValue(2.0);
	     shippingOptions.setShippingInsuranceCost(amount);	     
	     sd.setInsuranceFee(amount);
	     sd.setInsuranceOption(InsuranceOptionCodeType.OPTIONAL);
	     
	     
	     //SERVIZIO DI SPEDIZIONE INTERNAZIONALE
	     InternationalShippingServiceOptionsType inShipOpt = new InternationalShippingServiceOptionsType();
	     inShipOpt.setShippingService(ShippingServiceCodeType.IT_EXPEDITED_INTERNATIONAL.value());
	     inShipOpt.setShipToLocation(new String[]{ShippingRegionCodeType.EUROPE.value(),
	    		 									ShippingRegionCodeType.EUROPEAN_UNION.value()
	    		 									//,CountryCodeType.FR.value(),
	    		 									//CountryCodeType.DE.value(),
	    		 									//CountryCodeType.GB.value()
	    		 									});
	     
	     //COSTI AGGIUNTIVI
	     amount = new AmountType();
	     amount.setValue(0.0);
	     inShipOpt.setShippingServiceAdditionalCost(amount);
	     
	     //COSTO SPEDIZIONE
	     amount = new AmountType();
	     amount.setValue(25);
	     inShipOpt.setShippingServiceCost(amount);
	     
	     //PRIORITà CON CUI IL SERVIZIO DI SPEDIZIONE APPARE NELLA PAGINA
	     inShipOpt.setShippingServicePriority(new Integer(1));
	     
	     //ASSICURAZIONE
	     amount = new AmountType();
	     amount.setValue(5.0);
	     inShipOpt.setShippingInsuranceCost(amount);	     

	     sd.setShippingServiceOptions(new ShippingServiceOptionsType[]{shippingOptions});
	     sd.setInternationalShippingServiceOption(new InternationalShippingServiceOptionsType[]{inShipOpt});

	     return sd;
	  }
	  
	  
	  private static VariationsType impostaVarianti(String codice_articolo, List<Variante_Articolo> varianti, double prezzo){
		  
		  VariationType[] variants  = new VariationType[varianti.size()]; 
		  		  		  
		  NameValueListType typeVariation = new NameValueListType(); 		  
		  String[] valoriVarianti = new String[varianti.size()];
		  
		  //oggetto che contiene l'array per le immagini
		  PicturesType pic = new PicturesType();
		  //array per le immagini (contiene oggetti immagini-valore variante)
		  VariationSpecificPictureSetType[] vspSet = new VariationSpecificPictureSetType[varianti.size()];
		  
		  int i = 0;    	  
		  
		  for (Variante_Articolo v : varianti){
			  //creo l'oggetto variante
			  VariationType variation = new VariationType(); 
			  
			  //Definisco le specifiche della variante (il tipo di variante e il valore)
			  
		      if (i==0) {
		    	  typeVariation.setName(v.getTipo()); 
		    	  pic.setVariationSpecificName(v.getTipo());
		      }
		      
		      valoriVarianti[i] =  v.getValore();
		      
		      //Log.debug("Variante: "+v.getIdVariante()+", "+v.getTipo()+", "+v.getValore()+", "+v.getImmagine());
		      
		      NameValueListType nvlt = new NameValueListType(); 
		      nvlt.setName(v.getTipo());
		      nvlt.setValue(new String[]{ v.getValore() });
		      NameValueListArrayType nvListArraySpec = new NameValueListArrayType(); 
		      nvListArraySpec.setNameValueList(new NameValueListType[] {nvlt}); 
		      
		   // Aggiungo le specifiche della variante all'oggetto variante
		      variation.setVariationSpecifics(nvListArraySpec);
		      
		      AmountType amt = new AmountType(); 
		      amt.setCurrencyID(CurrencyCodeType.EUR); 
		      amt.setValue(prezzo); 
		       
		      variation.setStartPrice(amt); 
		      variation.setQuantity(v.getQuantita());
		      variation.setSKU(codice_articolo+"-"+v.getValore());
		      
		      VariationSpecificPictureSetType vsp = new VariationSpecificPictureSetType();
	    	  vsp.setVariationSpecificValue(v.getValore());
	    	  vsp.setPictureURL(new String[]{Costanti.percorsoImmaginiRemoto+v.getImmagine()});	
	    	      	  
	    	  vspSet[i] = vsp;
		      variants[i] = variation;
		      
		      i++;
		  }
		  
		  typeVariation.setValue(valoriVarianti); 
	      
	      NameValueListArrayType nvListArray = new NameValueListArrayType(); 
	      nvListArray.setNameValueList(new NameValueListType[] {typeVariation});       	      
	     
//		  NameValueListType[] NVLtype = new NameValueListType[varianti.size()]; 
		  		  
		  VariationsType variations  = new VariationsType(); 
          
          variations.setVariation(variants); 
          
          //setto qui l'array con le immagini per le varie varianti    	  
          pic.setVariationSpecificPictureSet(vspSet);
    	  variations.setPictures(new PicturesType[]{pic});
    	  
    	  variations.setVariationSpecificsSet(nvListArray);	    
    	  
    	  return variations; 
	  }
	  
	  
	  
		private static boolean specificheCategoria(String id_categoria, String ambiente) {
			GetCategoryFeaturesCall cf;
			boolean enabled = true;
			try {
				cf = getCatFeature(id_categoria,ambiente);
				CategoryFeatureType feature = cf.getReturnedCategory()[0];
				if (feature!=null)
					enabled = feature.getConditionEnabled().equals(ConditionEnabledCodeType.ENABLED);
			} catch (Exception e) {			
				//e.printStackTrace();
				Log.error("Specifiche Categoria, "+e.getMessage());
			}
//			FeatureDefinitionsType fdt = cf.getReturnedFeatureDefinitions();
//			
//			ListingDurationDefinitionsType lddts = fdt.getListingDurations();
//			
//			ListingDurationDefinitionType[] lddt = lddts.getListingDuration();
//			
//			for (int i=0;i< lddt.length;i++){
//				System.out.println(lddt[i].getDurationSetID());
//			}		
			/*
			 * If condition Enabled is disabled, then DO NOT pass conditionID in
			 * AddItem or ReviseItem for this category
			 */
			return enabled;			
		}
		
		
		private static GetCategoryFeaturesCall getCatFeature(String categoryId, String ambiente)	throws IOException {
			GetCategoryFeaturesCall request = new GetCategoryFeaturesCall(EbayApiUtility.getApiContext(ambiente));
			request.setSite(SiteCodeType.ITALY);
			request.setDetailLevel(new DetailLevelCodeType[] { DetailLevelCodeType.RETURN_ALL });
			request.setCategoryID(categoryId);
			request.setOutputSelector(new String[] { "Category.ConditionEnabled",
					"Category.ConditionValues.Condition.ID",
					"Category.ConditionValues.Condition.DisplayName" });
			try {
				request.getCategoryFeatures();
			} catch (Exception e) {
				e.printStackTrace();
				Log.info(e);
			}
			return request;
		}
		
		
		
		public static void assegnaRegoleDiProgrammazione(String itemID, String ambiente){
			try{		
				ApiCall call = new ApiCall( EbayApiUtility.getApiContext(ambiente) );
				
				SetSellingManagerItemAutomationRuleRequestType arrequest = new SetSellingManagerItemAutomationRuleRequestType();
				
				arrequest.setItemID(itemID);
				
				SellingManagerAutoRelistType art = new SellingManagerAutoRelistType();
				art.setType(SellingManagerAutoRelistTypeCodeType.RELIST_CONTINUOUSLY_UNTIL_SOLD);
				art.setRelistCondition(SellingManagerAutoRelistOptionCodeType.RELIST_IMMEDIATELY);
				arrequest.setAutomatedRelistingRule(art);

				//AbstractResponseType response = call.executeByApiName("SetSellingManagerItemAutomationRule", arrequest);
				call.executeByApiName("SetSellingManagerItemAutomationRule", arrequest);
				
				//System.out.println(response.getMessage());			
			}
			catch (Exception e){
				e.printStackTrace();
				Log.info(e);
			}
		}

}
