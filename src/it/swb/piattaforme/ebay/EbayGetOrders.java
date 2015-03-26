package it.swb.piattaforme.ebay;
import com.ebay.sdk.*;
import com.ebay.sdk.call.CompleteSaleCall;
import com.ebay.sdk.call.GetOrderTransactionsCall;
import com.ebay.sdk.call.GetOrdersCall;
import com.ebay.soap.eBLBaseComponents.*;

import it.swb.business.ArticoloBusiness;
import it.swb.log.Log;
import it.swb.model.Articolo;
import it.swb.model.Cliente;
import it.swb.model.Indirizzo;
import it.swb.model.InfoEbay;
import it.swb.model.Ordine;
import it.swb.utility.DateMethods;
import it.swb.utility.Methods;

import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.log4j.Logger;

/**
* 1. Create an ApiContext Object
* 2. Set the auth token and target api url (Webservice endpoint)
* 3. Create a GetOrders object.
* 4. Set the CreateTimeFrom  and CreateTimeTo filter (just an example)
* 5. Set the DetailLevel flag to ReturnAll
* 6. Call GetOrdersCall.getOrders() to retrieve the orders
*/
public class EbayGetOrders {

    final static Logger logger = Logger.getLogger(EbayGetOrders.class);

    public static void getOrders() throws ApiException, SdkException, Exception {

        ApiContext apiContext = EbayApiUtility.getApiContext("produzione");

        // set detail level to ReturnAll
        DetailLevelCodeType[] detailLevels = new DetailLevelCodeType[]{DetailLevelCodeType.RETURN_ALL};

        // define CreateTimeFrom and CreateTimeTo filters
        // set "To" to the current time 
        Calendar to = new GregorianCalendar();         
        Calendar from = (GregorianCalendar) to.clone();

        // In this example we set "From" to 30 mins from current time - you need to set it to the last time you made the call
        from.add(Calendar.MINUTE,-1000);        

        GetOrdersCall getOrders = new GetOrdersCall(apiContext);
        getOrders.setDetailLevel(detailLevels);
        getOrders.setCreateTimeFrom(from);
        getOrders.setCreateTimeTo(to);
        
        OrderType[] orders = getOrders.getOrders();
        outputOrderInfo (orders);
    }
    
    
    public static void getOrderTransaction(String idOrdine) {
    	System.out.println("Start");
        ApiContext apiContext = EbayApiUtility.getApiContext("produzione");

        // set detail level to ReturnAll
        DetailLevelCodeType[] detailLevels = new DetailLevelCodeType[]{DetailLevelCodeType.RETURN_ALL,DetailLevelCodeType.ITEM_RETURN_ATTRIBUTES,
        																	DetailLevelCodeType.ITEM_RETURN_DESCRIPTION};

        GetOrderTransactionsCall orderCall = new GetOrderTransactionsCall(apiContext);
        orderCall.setDetailLevel(detailLevels);
        
        OrderIDArrayType oiat = new OrderIDArrayType();
        String[] st = new String[1];
        st[0] = idOrdine;
        oiat.setOrderID(st);
        
        orderCall.setOrderIDArray(oiat);
        
        try{
	        OrderArrayType orderArray = orderCall.getOrderTransactions();
	        
	        OrderType[] order = orderArray.getOrder();
	        
	        if(order.length!=0){
		        TransactionArrayType transAryType = order[0].getTransactionArray();
		        TransactionType[] transArray = transAryType.getTransaction();
		        
		        for (TransactionType tran : transArray) {
		
		            // get the OrderLineItemID, Quantity, buyer's email and SKU
		            System.out.println("   Transaction -> OrderLineItemID  : " + tran.getOrderLineItemID());
		            System.out.println("   Transaction -> Quantity Purchased  : " + tran.getQuantityPurchased());
		            System.out.println("   Transaction -> Buyer email  : " + tran.getBuyer().getEmail());
		            ItemType item = tran.getItem();
		            String sku = item.getSKU();
		            if (sku != null) {
		                System.out.println("   Transaction -> SKU  : " + sku);
		            } else {
		            	String desc = item.getDescription();
		            	
		            	if (desc!=null && desc.contains("<CODICE>")){
		            		int start = desc.indexOf("<CODICE>")+8;
		            		int end = desc.indexOf("</CODICE>");
		            		
		            		sku = desc.substring(start, end);
		            		item.setSKU(sku);
		            		System.out.println("Personal Method -> SKU : "+sku);
		            	}
		            }
		            System.out.println(" ");
		            
		            System.out.println(tran.getItem().getTitle());
		
		            // if the item is listed with variations, get the variation SKU
		            VariationType variation = tran.getVariation();
		            if (variation != null) {
		                System.out.println("   Transaction ->  Variation SKU()" + variation.getSKU());
		            }
		        }
	        }
        }
        catch (Exception e){
        	e.printStackTrace();
        }
        System.out.println("end");
    }
    
    
    public static List<Ordine> getOrdini(Date dataDa, Date dataA) {
    	Log.info("Download ordini eBay dal "+DateMethods.formattaData2(dataDa)+" al "+DateMethods.formattaData2(dataA));
    	List<Ordine> ordini = null;
    	try{
    	
	        ApiContext apiContext = EbayApiUtility.getApiContext("produzione");
	
	        // set detail level to ReturnAll
	        DetailLevelCodeType[] detailLevels = new DetailLevelCodeType[]{DetailLevelCodeType.RETURN_ALL};
	
	        // define CreateTimeFrom and CreateTimeTo filters
	        // set "To" to the current time 
//	        Calendar to = new GregorianCalendar();         
//	        Calendar from = (GregorianCalendar) to.clone();
	                
	        Calendar from = new GregorianCalendar();
	        from.setTime(dataDa);
	        
	        Calendar to = new GregorianCalendar(); 
	        to.setTime(dataA);
	
	        // In this example we set "From" to 30 mins from current time - you need to set it to the last time you made the call
//	        from.add(Calendar.MINUTE,-1440*giorni);        
	
	        GetOrdersCall getOrders = new GetOrdersCall(apiContext);
	        getOrders.setDetailLevel(detailLevels);
	        getOrders.setCreateTimeFrom(from);
	        getOrders.setCreateTimeTo(to);
	        
	        OrderType[] orders = getOrders.getOrders();
	        ordini = ottieniOrdini(orders);
	        
	        Log.info(ordini.size()+" ordini ottenuti.");
    	}catch (Exception e){
	    	Log.error(e.getMessage());
	    	e.printStackTrace();
	    }   	
        return ordini;
    }

    private static void outputOrderInfo(OrderType[] orders) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss.SSS");
        if (orders ==null ){
              System.out.println(" No order found ");
            return;
        }
        for (OrderType order : orders) {
            System.out.println("");
            System.out.println("Order Information ");

            // get the OrderID
            System.out.println("  Order  -> OrderID: " + order.getOrderID());
            CheckoutStatusType checkoutStatus = order.getCheckoutStatus();
            CompleteStatusCodeType completeStatus = checkoutStatus.getStatus();
            System.out.println("  Order  -> Checkout Status: " + completeStatus.toString());
           
            // if checkout is complete, then the order is ready for fulfillment
            if (completeStatus.equals(CompleteStatusCodeType.COMPLETE)) {

                // get the amount paid
                System.out.println(" Order  -> AmountPaid: " + order.getAmountPaid().getValue() + " " + order.getAmountPaid().getCurrencyID());
                
                // get the payment method 
                BuyerPaymentMethodCodeType paymentMethod = order.getCheckoutStatus().getPaymentMethod();
                System.out.println("  Order  -> Payment Method : " + paymentMethod.value());

                // get the checkout message left by the buyer, if any
                String buyerCheckoutMsg = order.getBuyerCheckoutMessage();
                if (buyerCheckoutMsg != null) {
                    System.out.println("  Order  -> Buyer CheckoutMsg : " + buyerCheckoutMsg);
                }

                // get the sales tax, if any 
                double salesTax = order.getShippingDetails().getSalesTax().getSalesTaxAmount() == null ? 0.0 : order.getShippingDetails().getSalesTax().getSalesTaxAmount().getValue();
                System.out.println("  Order -> SalesTax " + salesTax);

                // get the external transaction information - if payment is made via PayPal, then this is the PayPal transaction info
                ExternalTransactionType[] externalTransactions = order.getExternalTransaction();
                for (ExternalTransactionType et : externalTransactions) {
                    System.out.println("  Order -> External TransactionID  : " + et.getExternalTransactionID());
                    System.out.println("  Order -> External Transaction Time  : " + dateFormatter.format(et.getExternalTransactionTime().getTime()));
                    System.out.println("  Order -> External FeeOrCreditAmount  : " + et.getFeeOrCreditAmount().getValue() + " " + et.getFeeOrCreditAmount().getCurrencyID());
                    System.out.println("  Order -> External Transaction PaymentOrRefundAmount   : " + et.getPaymentOrRefundAmount().getValue() + " " + et.getPaymentOrRefundAmount().getCurrencyID());
                }

                // get the shipping service selected by the buyer
                System.out.println("  Order -> Shipping Service Selected  : " + order.getShippingServiceSelected().getShippingService());
                System.out.println("  Order -> Shipping Service Selected ShippingServiceCost  : " + order.getShippingServiceSelected().getShippingServiceCost().getValue() + " " + order.getShippingServiceSelected().getShippingServiceCost().getCurrencyID());

                // get the buyer's shipping address 
                AddressType shippingAddress = order.getShippingAddress();
                StringBuffer address = new StringBuffer();
                address.append(shippingAddress.getName());
                if (shippingAddress.getStreet() != null) {
                    address.append(",").append(shippingAddress.getStreet());
                }
                if (shippingAddress.getStreet1() != null) {
                    address.append(",").append(shippingAddress.getStreet1());
                }
                if (shippingAddress.getStreet2() != null) {
                    address.append(",").append(shippingAddress.getStreet2());
                }
                if (shippingAddress.getCityName() != null) {
                    address.append(",").append(
                            shippingAddress.getCityName());
                }
                if (shippingAddress.getStateOrProvince() != null) {
                    address.append(",").append(
                            shippingAddress.getStateOrProvince());
                }
                if (shippingAddress.getPostalCode() != null) {
                    address.append(",").append(
                            shippingAddress.getPostalCode());
                }
                if (shippingAddress.getCountryName() != null) {
                    address.append(",").append(
                            shippingAddress.getCountryName());
                }
                if (shippingAddress.getPhone() != null) {
                    address.append(",").append(shippingAddress.getPhone());
                }

                System.out.println("  Order -> Shipping Address: " + address);

                TransactionArrayType transAryType = order.getTransactionArray();
                TransactionType[] transArray = transAryType.getTransaction();
                System.out.println("  Order -> Transaction Array  ");

                 // iterate through each transaction for the order
                for (TransactionType tran : transArray) {

                    // get the OrderLineItemID, Quantity, buyer's email and SKU
                    System.out.println("   Transaction -> OrderLineItemID  : " + tran.getOrderLineItemID());
                    System.out.println("   Transaction -> Quantity Purchased  : " + tran.getQuantityPurchased());
                    System.out.println("   Transaction -> Buyer email  : " + tran.getBuyer().getEmail());
                    ItemType item = tran.getItem();
		            String sku = item.getSKU();
		            if (sku != null) {
		                System.out.println("   Transaction -> SKU  : " + sku);
		            } else {
		            	String desc = item.getDescription();
		            	
		            	if (desc!=null && desc.contains("<CODICE>")){
		            		int start = desc.indexOf("<CODICE>")+8;
		            		int end = desc.indexOf("</CODICE>");
		            		
		            		sku = desc.substring(start, end);
		            		item.setSKU(sku);
		            		System.out.println("Personal Method -> SKU : "+sku);
		            	}
		            }
                    System.out.println(" ");

                    // if the item is listed with variations, get the variation SKU
                    VariationType variation = tran.getVariation();
                    if (variation != null) {
                        System.out.println("   Transaction ->  Variation SKU()" + variation.getSKU());
                    }
                }
            }
        }
    }
    
    private static List<Ordine> ottieniOrdini(OrderType[] orders) {
    	List<Ordine> ordini = null;
    	
    	Map<String, Articolo> mappaArticoli = ArticoloBusiness.getInstance().getMappaArticoli();
    	
        //SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        if (orders ==null ){
              System.out.println(" Nessun ordine ");
            return null;
        }
        
        ordini = new ArrayList<Ordine>();
        
        for (OrderType order : orders) {
 
           //CheckoutStatusType checkoutStatus = order.getCheckoutStatus();
           //CompleteStatusCodeType completeStatus = checkoutStatus.getStatus();
           
            // if checkout is complete, then the order is ready for fulfillment
  //          if (completeStatus.equals(CompleteStatusCodeType.COMPLETE)) {
            	Ordine o = new Ordine();
            	o.setPiattaforma("eBay");
            	
            	 if (order.getShippedTime()!=null){
                 	o.setDataSpedizione(order.getShippedTime().getTime());
                 	o.setStato("Spedito");
                 } else {
                	 
	                OrderStatusCodeType osct = order.getOrderStatus();
	             	
	             	String stato = osct.value();
	             	
	             	if (stato.equals("Active"))
	             		stato = "In Attesa";
	             	else if (stato.equals("Cancelled"))
	             		stato = "Cancellato";
	             	else if (stato.equals("Completed"))
	             		stato = "Pagato";
	             	
	             	o.setStato(stato);
                 }
            	 
            	
            	
            	//o.setIdCliente(order.getBuyerUserID());
            	
            	o.setCommento(order.getBuyerCheckoutMessage());
            	
               // get the OrderID
               o.setIdOrdinePiattaforma(order.getOrderID());

                // get the amount paid
                o.setTotale(order.getAmountPaid().getValue());
                o.setValuta(order.getAmountPaid().getCurrencyID().value());
                
                // get the payment method 
                BuyerPaymentMethodCodeType paymentMethod = order.getCheckoutStatus().getPaymentMethod();
                if (paymentMethod.value().equals("MoneyXferAcceptedInCheckout"))
                	o.setMetodoPagamento("Bonifico");
                if (paymentMethod.value().equals("MOCC"))
                	o.setMetodoPagamento("Vaglia Postale");
                else o.setMetodoPagamento(paymentMethod.value());

                // get the checkout message left by the buyer, if any
                String buyerCheckoutMsg = order.getBuyerCheckoutMessage();
                if (buyerCheckoutMsg != null) {
                    o.setCommento(buyerCheckoutMsg);
                }

                // get the sales tax, if any 
                //double salesTax = order.getShippingDetails().getSalesTax().getSalesTaxAmount() == null ? 0.0 : order.getShippingDetails().getSalesTax().getSalesTaxAmount().getValue();
                //o.setSalesTax(salesTax);

//                // get the external transaction information - if payment is made via PayPal, then this is the PayPal transaction info
//                ExternalTransactionType[] externalTransactions = order.getExternalTransaction();
//                for (ExternalTransactionType et : externalTransactions) {
////                    System.out.println("  Order -> External TransactionID  : " + et.getExternalTransactionID());
//                    o.setData(Methods.formattaDataConOra(et.getExternalTransactionTime().getTime()));
////                    System.out.println("  Order -> External FeeOrCreditAmount  : " + et.getFeeOrCreditAmount().getValue() + " " + et.getFeeOrCreditAmount().getCurrencyID());
////                    System.out.println("  Order -> External Transaction PaymentOrRefundAmount   : " + et.getPaymentOrRefundAmount().getValue() + " " + et.getPaymentOrRefundAmount().getCurrencyID());
//                }
                
                
                if (order.getPaidTime()!=null){
                	o.setDataPagamento(order.getPaidTime().getTime());
                }
                
                o.setDataAcquisto(order.getCreatedTime().getTime());

                // get the shipping service selected by the buyer
                o.setMetodoSpedizione(order.getShippingServiceSelected().getShippingService());
                if (order.getShippingServiceSelected().getShippingServiceCost()!=null)
                	o.setCostoSpedizione(order.getShippingServiceSelected().getShippingServiceCost().getValue());
                
                ShippingDetailsType sdt = order.getShippingDetails();

                if (sdt!=null){
                	ShipmentTrackingDetailsType[] stdt = sdt.getShipmentTrackingDetails();
                	if (stdt.length!=0 && stdt[0]!=null)
                		o.setNumeroTracciamento(stdt[0].getShipmentTrackingNumber());
                }
                
                // get the buyer's shipping address 
                AddressType shippingAddress = order.getShippingAddress();
                
                Indirizzo ind = new Indirizzo();                
                
                ind.setNomeCompleto(shippingAddress.getName());
                
//                if (shippingAddress.getStreet() != null) {
//                    ind.setVia(shippingAddress.getStreet());
//                }
                if (shippingAddress.getStreet1() != null) {
                    ind.setIndirizzo1(shippingAddress.getStreet1());
                }
                if (shippingAddress.getStreet2() != null) {
                    ind.setIndirizzo2(shippingAddress.getStreet2());
                }
                if (shippingAddress.getCityName() != null) {
                    ind.setComune(shippingAddress.getCityName());
                }
                if (shippingAddress.getStateOrProvince() != null) {
                    ind.setProvincia(shippingAddress.getStateOrProvince());
                }
                if (shippingAddress.getPostalCode() != null) {
                    ind.setCap(shippingAddress.getPostalCode());
                }
                if (shippingAddress.getCountryName() != null) {
                    ind.setNazione(shippingAddress.getCountryName());
                }
                if (shippingAddress.getPhone2() != null) {
                    ind.setCellulare(shippingAddress.getPhone2());
                }
                if (shippingAddress.getPhone() != null) {
                    ind.setTelefono(shippingAddress.getPhone());
                }
                if (shippingAddress.getCompanyName() != null) {
                    ind.setAzienda(shippingAddress.getCompanyName());
                }
                
                Cliente c = new Cliente();
                
                c.setPiattaforma("eBay");
                c.setUsername(order.getBuyerUserID());
                c.setNomeCompleto(shippingAddress.getName());
                c.setNome(shippingAddress.getFirstName());
                c.setCognome(shippingAddress.getLastName());
                c.setTelefono(shippingAddress.getPhone());
                c.setCellulare(shippingAddress.getPhone2());
                c.setAzienda(shippingAddress.getCompanyName());
                c.setIndirizzoSpedizione(ind);
                
                o.setCliente(c);
                o.setIndirizzoSpedizione(ind);
                
                TransactionArrayType transAryType = order.getTransactionArray();
                TransactionType[] transArray = transAryType.getTransaction();
                
                int quantita = 0;
                
                /* sku-id_ins-variante */
                List<Articolo> articoli = new ArrayList<Articolo>();
                
                int y=1;
                
                String email = null;
                
             // iterate through each transaction for the order
                for (TransactionType tran : transArray) {
                	Articolo a = new Articolo();
                	
                	a.setIdArticolo(y);
                	y++;
                	
                	a.setNote2(tran.getTransactionID());
                	
                    // get the OrderLineItemID, Quantity, buyer's email and SKU
                	ItemType it = tran.getItem();
                	
		            String sku = it.getSKU();
		            if (sku == null) {
		            	String desc = it.getDescription();
		            	
		            	if (desc!=null && desc.contains("<CODICE>")){
		            		int start = desc.indexOf("<CODICE>")+8;
		            		int end = desc.indexOf("</CODICE>");
		            		
		            		sku = desc.substring(start, end);
		            	}
		            }
                	
                	a.setCodice(sku);
                	a.setNome(it.getTitle());
                	a.setQuantitaMagazzino(tran.getQuantityPurchased());
                	
                	if (mappaArticoli.containsKey(sku)){
                		Articolo art = mappaArticoli.get(sku);
                		a.setAliquotaIva(art.getAliquotaIva());
                	}
                	
                	a.setTitoloInserzione(it.getItemID());
                	
                	if (it.getSKU()==null || it.getSKU().trim().isEmpty()){
                		InfoEbay ie = new InfoEbay();
                		
                		ie.setTitoloInserzione(it.getTitle());
                    	ie.setDescrizioneEbay(it.getDescription());
                    	
                    	if (it.getPrimaryCategory()!=null)
                    	ie.setIdCategoria1(it.getPrimaryCategory().getCategoryID());
                    	
                    	if (it.getSecondaryCategory()!=null)
                    	ie.setIdCategoria2(it.getSecondaryCategory().getCategoryID());
                    	
                	}                	
                	
                	AmountType at = tran.getTransactionPrice();
                	if (at!=null)
                		a.setPrezzoDettaglio(at.getValue());
                	
//                	a.setPrezzoPiattaforme(a.getPrezzoDettaglio()*tran.getQuantityPurchased());
//                	String abc = String.valueOf(a.getPrezzoPiattaforme());
//                	if (abc.length()>=6){
//	                	int x = abc.indexOf(".");
//	        	    	String def = abc.substring(0, x+3);
//	        	    	a.setPrezzoPiattaforme(Double.valueOf(def));
//                	} SOSTITUITO DA:
                	a.setPrezzoPiattaforme(Methods.round(a.getPrezzoDettaglio()*tran.getQuantityPurchased(), 2));
                	
 //                   System.out.println("   Transaction -> OrderLineItemID  : " + tran.getOrderLineItemID());

                    quantita = quantita + (int) tran.getQuantityPurchased();
                    
                    
                    
                    if (tran.getBuyer()!=null)
                    	email = tran.getBuyer().getEmail();
                    
                    // if the item is listed with variations, get the variation SKU
                    VariationType variation = tran.getVariation();
                    
                    if (variation != null) {
                        a.setNote(variation.getSKU());
                    }
                    articoli.add(a);
                }
                c.setEmail(email);
                o.setEmail(email);
                o.setArticoli(articoli);
                o.setQuantitaAcquistata(quantita);
                
                ordini.add(o);
 //           }
        }
        return ordini;
    }
    
    
    
    public static boolean completeSale(String id_ordine, String tracking_number){ 
    	boolean spedito = false;
    	
    	ApiContext apiContext = EbayApiUtility.getApiContext("produzione");
         
        CompleteSaleCall completeSaleApi = new CompleteSaleCall(apiContext); 
         
        completeSaleApi.setOrderID(id_ordine);
//        completeSaleApi.setItemID("110049243019"); 
//        completeSaleApi.setTransactionID("25291804001"); 
        completeSaleApi.setShipped(true); 
         
        ShipmentType shipType = new ShipmentType(); 
         
        ShipmentTrackingDetailsType shpmnt = new ShipmentTrackingDetailsType(); 
        
        if (tracking_number!=null && !tracking_number.trim().isEmpty())
        	shpmnt.setShipmentTrackingNumber(tracking_number); 
        
        shpmnt.setShippingCarrierUsed("SDA"); 
         
        shipType.setShipmentTrackingDetails(new ShipmentTrackingDetailsType[]{shpmnt}); 
         
        completeSaleApi.setShipment(shipType); 
         
        try { 
        	
             completeSaleApi.completeSale(); 
             spedito = true;
             
        } catch (ApiException e) {  
             e.printStackTrace(); 
             Log.error(e.getMessage());
        } catch (SdkException e) {  
              e.printStackTrace(); 
              Log.error(e.getMessage());
        } catch (Exception e) {  
             e.printStackTrace(); 
             Log.error(e.getMessage());
        } 
        return spedito;
    }
    
    
    public static void main(String[] args) {
        try {
            getOrders();
            //getOrderTransaction("171049019951-1042300895007");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}