/*
Copyright (c) 2006 eBay, Inc.

This program is licensed under the terms of the eBay Common Development and 
Distribution License (CDDL) Version 1.0 (the "License") and any subsequent 
version thereof released by eBay.  The then-current version of the License 
can be found at https://www.codebase.ebay.com/Licenses.html and in the 
eBaySDKLicense file that is under the eBay SDK install directory.
*/

package it.swb.ebay;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


import com.ebay.sdk.ApiAccount;
import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.ApiLogging;
import com.ebay.sdk.call.AddItemCall;
import com.ebay.sdk.util.eBayUtil;
import com.ebay.soap.eBLBaseComponents.AmountType;
import com.ebay.soap.eBLBaseComponents.BuyerPaymentMethodCodeType;
import com.ebay.soap.eBLBaseComponents.CategoryType;
import com.ebay.soap.eBLBaseComponents.CountryCodeType;
import com.ebay.soap.eBLBaseComponents.CurrencyCodeType;
import com.ebay.soap.eBLBaseComponents.FeesType;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.ebay.soap.eBLBaseComponents.ListingDurationCodeType;
import com.ebay.soap.eBLBaseComponents.ListingTypeCodeType;
import com.ebay.soap.eBLBaseComponents.NameValueListArrayType;
import com.ebay.soap.eBLBaseComponents.NameValueListType;
import com.ebay.soap.eBLBaseComponents.SiteCodeType;
/**
 * 
 * A simple item listing sample,
 * show basic flow to list an item to eBay Site using eBay SDK.
 * 
 * @author boyang
 *
 */
public class TestEbay {
	
	
	

	
  public static void main(String[] args) {

    try {

      System.out.print("\n");
      System.out.print("+++++++++++++++++++++++++++++++++++++++\n");
      System.out.print("+ Welcome to eBay SDK for Java Sample +\n");
      System.out.print("+  - ConsoleAddItem                   +\n");
      System.out.print("+++++++++++++++++++++++++++++++++++++++\n");
      System.out.print("\n");

      // [Step 1] Initialize eBay ApiContext object
	  System.out.println("===== [1] Account Information ====");
      ApiContext apiContext = getApiContext();
      
      // [Step 2] Create a new item object.
      System.out.println("===== [2] Item Information ====");
      ItemType item = buildItem();
      
      // [Step 3] Create call object and execute the call.
      System.out.println("===== [3] Execute the API call ====");
      System.out.println("Begin to call eBay API, please wait ...");
      AddItemCall api = new AddItemCall(apiContext);
      api.setSite(SiteCodeType.ITALY);
      api.setItem(item);
      
      FeesType fees = api.addItem();
      System.out.println("End to call eBay API, show call result ...");
      System.out.println();

      // [Setp 4] Display results.
      System.out.println("The list was listed successfully!");

      double listingFee = eBayUtil.findFeeByName(fees.getFee(), "ListingFee").getFee().getValue();
      System.out.println("Listing fee is: " + new Double(listingFee).toString());
      System.out.println("Listed Item ID: " + item.getItemID());
    }
    catch(Exception e) {
      System.out.println("Fail to list the item.");
      e.printStackTrace();
    }
  }
  
  // sample category ids supporting custom item specifics
  private static Set<String> sampleCatIDSet = new HashSet<String>();
  static {
	  sampleCatIDSet.add("162140");
  }
  /**
   * Build a sample item
   * @return ItemType object
   */
  private static ItemType buildItem() throws IOException {

	  ItemType item = new ItemType();

      // item title
      item.setTitle("Test titolo");
      // item description
      item.setDescription("Test descrizione");
      
      // listing type
      item.setListingType(ListingTypeCodeType.FIXED_PRICE_ITEM);
      // listing price
      item.setCurrency(CurrencyCodeType.EUR);
      //input = ConsoleUtil.readString("Start Price: ");
      AmountType amount = new AmountType();
      amount.setValue(Double.valueOf("10"));
      item.setStartPrice(amount);
      
      // listing duration
      item.setListingDuration(ListingDurationCodeType.DAYS_7.value());
      
      // item location and country
      item.setLocation("Santa Severa");
      item.setCountry(CountryCodeType.IT);
      

      // listing category
      CategoryType cat = new CategoryType();

      String catID = "38232";
      cat.setCategoryID(catID);
      item.setPrimaryCategory(cat);
      item.setConditionID(1000); 
      
      // item quantity
      item.setQuantity(new Integer(1));
      
      // item condition, New
      // item.setConditionID(1000);  
 
      // item specifics
      if(sampleCatIDSet.contains(catID)) {
    	  item.setItemSpecifics(buildItemSpecifics());
      }
      
      item.setPaymentMethods(new BuyerPaymentMethodCodeType[] 
              {BuyerPaymentMethodCodeType.PAY_PAL,BuyerPaymentMethodCodeType.MONEY_XFER_ACCEPTED,
    		  BuyerPaymentMethodCodeType.MOCC});
      // email is required if paypal is used as payment method
      item.setPayPalEmailAddress("zeldabomboniere@gmail.com");
      /*
		 * The Business Policies API and related Trading API fields are
		 * available in sandbox. It will be available in production for a
		 * limited number of sellers with Version 775. 100 percent of sellers
		 * will be ramped up to use Business Polcies in July 2012
		 */
      
//      //Create Seller Profile container
//      SellerProfilesType sellerProfile=new SellerProfilesType();
//      
//      //Set Payment ProfileId
//      input = ConsoleUtil.readString("Enter your Seller Policy Payment ProfileId : ");
//      SellerPaymentProfileType sellerPaymentProfile=new SellerPaymentProfileType();
//      sellerPaymentProfile.setPaymentProfileID(Long.valueOf(input));
//      sellerProfile.setSellerPaymentProfile(sellerPaymentProfile);
//
//      //Set Shipping ProfileId
//      SellerShippingProfileType sellerShippingProfile=new SellerShippingProfileType();
//      input = ConsoleUtil.readString("Enter your Seller Policy Shipping ProfileId : ");
//      sellerShippingProfile.setShippingProfileID(Long.valueOf(input));
//      sellerProfile.setSellerShippingProfile(sellerShippingProfile);
//      
//      //Set Return Policy ProfileId
//      SellerReturnProfileType sellerReturnProfile=new SellerReturnProfileType();
//	  input = ConsoleUtil.readString("Enter your Seller Policy Return ProfileId : ");
//	  sellerReturnProfile.setReturnProfileID(Long.valueOf(input));
//	  sellerProfile.setSellerReturnProfile(sellerReturnProfile);
//      
//	  //Add Seller Profile to Item
//      item.setSellerProfiles(sellerProfile);
      return item;
  }
  
  /**
   * Populate eBay SDK ApiContext object with data input from user
   * @return ApiContext object
   */
  private static ApiContext getApiContext() throws IOException {
	  
	    ApiAccount apiAccount =  new ApiAccount();
	    apiAccount.setDeveloper("7f9aa601-aa36-4695-b14c-8ec7707fc2ca");
	    apiAccount.setApplication("GloriaMo-b14d-4ee9-98b2-6db15ca66607");
	    apiAccount.setCertificate("4bde473c-295e-4bd1-a728-2f399d42105c");

	    ApiCredential apiCredential = new ApiCredential();
	    apiCredential.setApiAccount(apiAccount);
	    apiCredential.seteBayToken("AgAAAA**AQAAAA**aAAAAA**WKB2UA**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6wMmIegDpCCqQydj6x9nY+seQ**wpwBAA**AAMAAA**j6hOnny0q6AOmBCSNmGY/2qKyPDLqmfGX21hZpeK42Kv9FqdpM69lv6CrrVBfwvA7kHjJoqPgTfhLFSOqO40wFMO+oBx+s67GQPsEQUIRrlm+stO9P0t5YA7xIpfJEqzhm9u9MXpMJg+SPCNbVjRv01hYnSh9gjxJ/+vRDlTVyoUQ0b25UrIqkV441Y8uC6wc9DvLdWJBTJODW9mtX/y4pHT5sey3CKxqJDD7up+oVEwQ5K1ntRmRJ+V01LSuF4F5sL01SLgQtxUrI1iyYohjcHedyETwQdJmDOHqxOYwmYxhTLzfD5ldC/hY8iUEgCsKeo2mhZKxqwdaarrLsxcF0q7NIFEVl16P0jT5BR4v+a4QRIPVS05TtNlvJmzh3isU2pzbRKnYiiiiuCQXFY025VevNRK8V5D6uv/Tb2nrGLm8f48mte/2F0HRcz77GCmBsOOW4wjeE4HQpMrb91UshAp/Iu2ePlzVYX2RZTPgXw4AIxB+U59Sgw9tRblXWsAfe6TVwB+yrgU1Er/YvEU8Me2eWtfVRVOgNsqjLn28TX+lE+BeH+Qxsgruq7skMhsqjtSS9pKlqk8LMbTsPze08JKDOmR9SmvCSPjvs8vABMV/0jlgrgjF8jqJhWjlO6Zs4jSZBc1S75yfY9rYhJUEtss1ROQtqkjAaKtZCvQUeKwfUBh+q1WsGiQvmkGWq+UjXrJJi0jALku4PBFXBmuha7p9MV9+ciImKn6DlCyZU/a6fdAjCqT3Aa5VT/2d8sf");

	    ApiContext apiContext = new ApiContext();
	    
	    apiContext.setApiCredential(apiCredential);
	    apiContext.setApiServerUrl("https://api.ebay.com/wsapi");  
	    
	    ApiLogging apiLogging = new ApiLogging(); 
	    apiContext.setApiLogging(apiLogging); 
      
      return apiContext;
  }
  
  // build sample item specifics
  public static NameValueListArrayType buildItemSpecifics(){
	  
	  //create the content of item specifics
	  NameValueListArrayType nvArray = new NameValueListArrayType();
	  NameValueListType nv1 = new NameValueListType();
	  nv1.setName("Origin");
	  nv1.setValue(new String[]{"IT"});
	  NameValueListType nv2 = new NameValueListType();
	  nv2.setName("Year");
	  nv2.setValue(new String[]{"2010"});
	  nvArray.setNameValueList(new NameValueListType[]{nv1,nv2});
	  	  
	  return nvArray;
  }
  
}
