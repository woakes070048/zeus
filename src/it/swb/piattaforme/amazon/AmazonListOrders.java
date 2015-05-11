package it.swb.piattaforme.amazon;

import it.swb.business.ArticoloBusiness;
import it.swb.log.Log;
import it.swb.model.Articolo;
import it.swb.model.ArticoloAcquistato;
import it.swb.model.Cliente;
import it.swb.model.Indirizzo;
import it.swb.model.Ordine;
import it.swb.utility.DateMethods;
import it.swb.utility.Methods;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrders;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersAsync;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersAsyncClient;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersClient;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersException;
import com.amazonservices.mws.orders._2013_09_01.model.Address;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrderItemsRequest;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrderItemsResponse;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrderItemsResult;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrdersRequest;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrdersResponse;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrdersResult;
import com.amazonservices.mws.orders._2013_09_01.model.Money;
import com.amazonservices.mws.orders._2013_09_01.model.Order;
import com.amazonservices.mws.orders._2013_09_01.model.OrderItem;
import com.amazonservices.mws.orders._2013_09_01.model.ResponseHeaderMetadata;

public class AmazonListOrders {
	
    /**
     * Call the service, log response and exceptions.
     *
     * @param client
     * @param request
     *
     * @return The response.
     */
    public static ListOrdersResponse invokeListOrders(MarketplaceWebServiceOrders client, ListOrdersRequest request) {
        try {
            // Call the service.
            ListOrdersResponse response = client.listOrders(request);
//            ResponseHeaderMetadata rhmd = response.getResponseHeaderMetadata();
//            // We recommend logging every the request id and timestamp of every call.
//            System.out.println("Request:");
//            System.out.println(request.toXML());
//            System.out.println("Response:");
//            //System.out.println("RequestId: "+rhmd.getRequestId());
//            System.out.println("Timestamp: "+rhmd.getTimestamp());
//            System.out.println(response.toXML());
            return response;
        } catch (MarketplaceWebServiceOrdersException ex) {
            // Exception properties are important for diagnostics.
        	System.out.println("Request:");
        	System.out.println(request.toXML());
            System.out.println("Service Exception:");
            ResponseHeaderMetadata rhmd = ex.getResponseHeaderMetadata();
            if(rhmd != null) {
                System.out.println("RequestId: "+rhmd.getRequestId());
                System.out.println("Timestamp: "+rhmd.getTimestamp());
            }
            System.out.println("Message: "+ex.getMessage());
            System.out.println("StatusCode: "+ex.getStatusCode());
            System.out.println("ErrorCode: "+ex.getErrorCode());
            System.out.println("ErrorType: "+ex.getErrorType());
            throw ex;
        }
    }
    
    public static List<Object> invokeListOrderItems(MarketplaceWebServiceOrdersAsync client, List<ListOrderItemsRequest> requestList) {
        // Call the service async.
    	 List<Object> responseList = new ArrayList<Object>();
    	
        //List<Future<ListOrderItemsResponse>> futureList = new ArrayList<Future<ListOrderItemsResponse>>();
        
        int i = 0;
        
        for (ListOrderItemsRequest request : requestList) {
            Future<ListOrderItemsResponse> future = client.listOrderItemsAsync(request);
 //           futureList.add(future);
  //      }
        
       
//        for (Future<ListOrderItemsResponse> future : futureList) {
            
            Object xresponse = null;
            
            try {
            	ListOrderItemsResponse response = null;
            	
            	i++;
            	
            	if (i%30!=0)
            		response = future.get(30,TimeUnit.SECONDS);
            	else{
           			Log.info("Attesa di 40 secondi per non intasare amazon...");
           			int secondi = 40;
           		    Thread.sleep(secondi*1000);                 //1000 milliseconds is one second.
           		    Log.info("Download ripreso");
           		    
           		    response = future.get(30,TimeUnit.SECONDS);
           		}
            		
//                ResponseHeaderMetadata rhmd = response.getResponseHeaderMetadata();
//                // We recommend logging every the request id and timestamp of every call.
//                System.out.println("Response:");
//                System.out.println("RequestId: "+rhmd.getRequestId());
//                System.out.println("Timestamp: "+rhmd.getTimestamp());
//                String responseXml = response.toXML();
//                System.out.println(responseXml);
            	
                xresponse = response;
       		} catch(InterruptedException ex) {
       		    Thread.currentThread().interrupt();
            } catch (ExecutionException ee) {
            	Log.info(ee);
                Throwable cause = ee.getCause();
                if (cause instanceof MarketplaceWebServiceOrdersException) {
                    // Exception properties are important for diagnostics.
                    MarketplaceWebServiceOrdersException ex = 
                        (MarketplaceWebServiceOrdersException)cause;
                    //ResponseHeaderMetadata rhmd = ex.getResponseHeaderMetadata();
                    //System.out.println("Service Exception:");
                    //System.out.println("RequestId: "+rhmd.getRequestId());
                    //System.out.println("Timestamp: "+rhmd.getTimestamp());
                    Log.info("ERRORE: id ordine "+request.getAmazonOrderId()+", Message: "+ex.getMessage()+", StatusCode: "+ex.getStatusCode()+", ErrorCode: "+ex.getErrorCode());
                    //System.out.println("ErrorType: "+ex.getErrorType());
                    //xresponse = ex;
                } else {
                    //xresponse = cause;
                }
            } catch (Exception e) {
            	Log.info(e);
                //xresponse = e;
            }
            responseList.add(xresponse);
        }
        return responseList;
    }
    
    public static ListOrderItemsResponse invokeListOrderItems(MarketplaceWebServiceOrders client, ListOrderItemsRequest request) {
        try {
            // Call the service.
            ListOrderItemsResponse response = client.listOrderItems(request);
            //ResponseHeaderMetadata rhmd = response.getResponseHeaderMetadata();
            // We recommend logging every the request id and timestamp of every call.
            //System.out.println("Response:");
            //System.out.println("RequestId: "+rhmd.getRequestId());
            //System.out.println("Timestamp: "+rhmd.getTimestamp());
            //String responseXml = response.toXML();
            //System.out.println(responseXml);
            return response;
        } catch (MarketplaceWebServiceOrdersException ex) {
            // Exception properties are important for diagnostics.
            System.out.println("Service Exception:");
            ResponseHeaderMetadata rhmd = ex.getResponseHeaderMetadata();
            if(rhmd != null) {
                System.out.println("RequestId: "+rhmd.getRequestId());
                System.out.println("Timestamp: "+rhmd.getTimestamp());
            }
            System.out.println("Message: "+ex.getMessage());
            System.out.println("StatusCode: "+ex.getStatusCode());
            System.out.println("ErrorCode: "+ex.getErrorCode());
            System.out.println("ErrorType: "+ex.getErrorType());
            throw ex;
        }
    }
    
    public static List<Ordine> scaricaOrdini(Date dataDa, Date dataA){
    	Date now = new Date();
    	
    	String s1 = DateMethods.formattaData1(dataA);
    	String s2 = DateMethods.formattaData1(now);
    	
    	if (s2.equals(s1)) {
    		dataA = DateMethods.sottraiOreAData(now, 2);
    	}
    	
    	Log.info("Download ordini Amazon dal "+DateMethods.formattaData2(dataDa)+" al "+DateMethods.formattaData2(dataA));
    	List<Ordine> ordini = null;
    	
//    	Calendar date1 =Calendar.getInstance();
//    	date1.setTime(dataDa);
//
//    	Calendar date2 =Calendar.getInstance();
//    	date2.setTime(dataA);
    	
    	try{
    		List<Order> orders = effettuaChiamataAmazon(dataDa,dataA);
    		ordini = elaboraETrasformaOrdini(orders);
    		
    		Log.info(ordini.size()+" ordini amazon ottenuti.");
    	}catch (Exception e){
	    	Log.error(e.getMessage());
	    	e.printStackTrace();
	    }   	
        return ordini;
    }
    
    
    public static List<Order> effettuaChiamataAmazon(Date dataDa, Date dataA) throws Exception{
    	
  		// Get a client connection.
		MarketplaceWebServiceOrdersClient client = AmazonConfig.getClient();
		
		// Create a request.
        ListOrdersRequest request = new ListOrdersRequest();
        String sellerId = "A1L3HFHFCQOIJK";
        request.setSellerId(sellerId);
        
       // String mwsAuthToken = "example";
        //request.setMWSAuthToken(mwsAuthToken);
 
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(dataDa.getTime());
        gc.add(Calendar.HOUR, 2);
        
        GregorianCalendar gc1 = new GregorianCalendar();
        gc1.setTimeInMillis(dataA.getTime());
        gc1.add(Calendar.HOUR, 2);
        
        XMLGregorianCalendar createdAfter = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc).normalize(); 
        
        
//        MwsUtl.getDTF().newXMLGregorianCalendar();
//        createdAfter.setYear(dataDa.get(Calendar.YEAR));
//        createdAfter.setMonth(dataDa.get(Calendar.MONTH));
//        createdAfter.setDay(dataDa.get(Calendar.DAY_OF_MONTH));
//        createdAfter.setTime(dataDa.get(Calendar.HOUR), dataDa.get(Calendar.MINUTE), dataDa.get(Calendar.SECOND));
        request.setCreatedAfter(createdAfter);
        

        XMLGregorianCalendar createdBefore = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc1).normalize(); 
//			MwsUtl.getDTF().newXMLGregorianCalendar();
//        createdBefore.setYear(dataA.get(Calendar.YEAR));
//        createdBefore.setMonth(dataA.get(Calendar.MONTH));
//        createdBefore.setDay(dataA.get(Calendar.DAY_OF_MONTH));
//        createdBefore.setTime(dataA.get(Calendar.HOUR), dataA.get(Calendar.MINUTE), dataA.get(Calendar.SECOND));
        request.setCreatedBefore(createdBefore);

/*
        XMLGregorianCalendar lastUpdatedAfter = MwsUtl.getDTF().newXMLGregorianCalendar();
        lastUpdatedAfter.setYear(dataDa.get(Calendar.YEAR));
        lastUpdatedAfter.setMonth(dataDa.get(Calendar.MONTH));
        lastUpdatedAfter.setDay(dataDa.get(Calendar.DAY_OF_MONTH));
        lastUpdatedAfter.setTime(dataDa.get(Calendar.HOUR), dataDa.get(Calendar.MINUTE), dataDa.get(Calendar.SECOND));
        request.setLastUpdatedAfter(lastUpdatedAfter);

        
        XMLGregorianCalendar lastUpdatedBefore = MwsUtl.getDTF().newXMLGregorianCalendar();
        lastUpdatedBefore.setYear(dataA.get(Calendar.YEAR));
        lastUpdatedBefore.setMonth(dataA.get(Calendar.MONTH));
        lastUpdatedBefore.setDay(dataA.get(Calendar.DAY_OF_MONTH));
        lastUpdatedBefore.setTime(dataA.get(Calendar.HOUR), dataA.get(Calendar.MINUTE), dataA.get(Calendar.SECOND));
        request.setLastUpdatedBefore(lastUpdatedBefore);
*/
        
        List<String> orderStatus = new ArrayList<String>();
        orderStatus.add("Canceled");
        orderStatus.add("Unshipped");
        orderStatus.add("PartiallyShipped");
        orderStatus.add("Shipped");
        request.setOrderStatus(orderStatus);
        
        	List<String> marketplaceId = new ArrayList<String>();
        	marketplaceId.add("APJ6JRA9NG5V4");
        	request.setMarketplaceId(marketplaceId);
        	
//        List<String> fulfillmentChannel = new ArrayList<String>();
//        request.setFulfillmentChannel(fulfillmentChannel);
//        List<String> paymentMethod = new ArrayList<String>();
//        request.setPaymentMethod(paymentMethod);
//        String buyerEmail = "example";
//        request.setBuyerEmail(buyerEmail);
//        String sellerOrderId = "example";
//        request.setSellerOrderId(sellerOrderId);
//        Integer maxResultsPerPage = 10;
//        request.setMaxResultsPerPage(maxResultsPerPage);
//           List<String> tfmShipmentStatus = new ArrayList<String>();
//           request.setTFMShipmentStatus(tfmShipmentStatus);

        // Make the call.
        	ListOrdersResponse resp = invokeListOrders(client, request);
        	ListOrdersResult res = resp.getListOrdersResult();
        	List<Order> orders = res.getOrders();
        	
        	return orders;
    }
    
    public static List<ArticoloAcquistato> ottieniArticoliDellOrdine(String order_id){
    	
    	List<ArticoloAcquistato> articoli = new ArrayList<ArticoloAcquistato>();
    	
    	MarketplaceWebServiceOrdersClient client = AmazonConfig.getOrdersAsyncClient();

        // Create a request.
        ListOrderItemsRequest request = new ListOrderItemsRequest();
        String sellerId = "A1L3HFHFCQOIJK";
        request.setSellerId(sellerId);
        //String mwsAuthToken = "example";
        //request.setMWSAuthToken(mwsAuthToken);
        //String amazonOrderId = "example";
        request.setAmazonOrderId(order_id);

        // Make the call.
        ListOrderItemsResponse resp = invokeListOrderItems(client, request);
        
        ListOrderItemsResult list = resp.getListOrderItemsResult();
        List<OrderItem> items = list.getOrderItems();
        
        for (OrderItem it : items){
        	ArticoloAcquistato a = new ArticoloAcquistato();
        	
        	a.setPiattaforma("Amazon");
        	a.setIdOrdinePiattaforma(order_id);
        	
        	a.setAsin(it.getASIN());
        	a.setCodice(it.getSellerSKU());
        	String nome = it.getTitle();
        	a.setNome(nome);
        	
        	if (nome.contains("(")){
        		int st = nome.indexOf("(")+1;
        		int en = nome.indexOf(")");
        		String variante = nome.substring(st,en);
        		a.setVariante(variante);
        	}
        	
        	int quantita = it.getQuantityOrdered();
        	a.setQuantitaAcquistata(quantita);
        	
        	Money mon = it.getItemPrice();
			 double prezzoTotale = 0;
			 if (mon!=null){
				 String tot = mon.getAmount();	 
				 prezzoTotale = Methods.veryRound(Double.valueOf(tot)); 
			 }
        	a.setPrezzoTotale(prezzoTotale);
        	
        	mon = it.getShippingPrice();
			 double costoSpedizione = 0;
			 if (mon!=null){
				 String tot = mon.getAmount();	 
				 costoSpedizione = Methods.veryRound(Double.valueOf(tot)); 
			 }
			 a.setCostoSpedizione(costoSpedizione);
			 
			 double prezzoUnitario =  0;
			 if (quantita!=0) prezzoUnitario = prezzoTotale/quantita;
			 a.setPrezzoUnitario(Methods.veryRound(prezzoUnitario));
			 
        	mon = it.getItemTax();
			 double tax = 0;
			 if (mon!=null){
				 String tot = mon.getAmount();	 
				 tax = Double.valueOf(tot); 
			 }
			 a.setIva(tax);
        	
        	articoli.add(a);
        	
        }
    	
    	return articoli;
    	
    }
    
    public static Map<String,List<ArticoloAcquistato>> ottieniElencoArticoli(List<ListOrderItemsRequest> requestList){
    	
    	Map<String, Articolo> mappaArticoli = ArticoloBusiness.getInstance().getMappaArticoliPerOrdini();
    	
    	Map<String,List<ArticoloAcquistato>> map = new HashMap<String,List<ArticoloAcquistato>>();
    	
    	MarketplaceWebServiceOrdersAsyncClient client = AmazonConfig.getOrdersAsyncClient();
    	
    	List<Object> listaRisposte = invokeListOrderItems(client, requestList);
    	
    	for (Object o : listaRisposte){
    		if (o!=null){
	    		
	    		ListOrderItemsResponse resp = (ListOrderItemsResponse) o;
	            
	            ListOrderItemsResult list = resp.getListOrderItemsResult();
	            List<OrderItem> items = list.getOrderItems();
	            
	            String order_id = list.getAmazonOrderId();
	            
	            List<ArticoloAcquistato> articoli = new ArrayList<ArticoloAcquistato>();
	            
	            for (OrderItem it : items){
	            	ArticoloAcquistato a = new ArticoloAcquistato();
	            	
	            	String sku = it.getSellerSKU();
	            	
					if (sku.contains("-")){
						int x = sku.indexOf("-");
						sku = sku.substring(0,x);
					}
					
					a.setCodice(sku);
					
					if (mappaArticoli.containsKey(sku))
						a.setIdArticolo(mappaArticoli.get(sku).getIdArticolo());
	            	
	            	a.setPiattaforma("Amazon");
	            	a.setIdOrdinePiattaforma(order_id);
	            	
	            	a.setAsin(it.getASIN());
	            	
	            	String nome = it.getTitle();
	            	a.setNome(nome);
	            	
	            	a.setIdTransazione(it.getOrderItemId());
	            	
	            	if (nome.contains("(")){
	            		int st = nome.indexOf("(")+1;
	            		int en = nome.indexOf(")");
	            		String variante = nome.substring(st,en);
	            		a.setVariante(variante);
	            	}
	            	
	            	int quantita = it.getQuantityOrdered();
	            	a.setQuantitaAcquistata(quantita);
	            	
	            	Money mon = it.getItemPrice();
	    			 double prezzoTotale = 0;
	    			 if (mon!=null){
	    				 String tot = mon.getAmount();	 
	    				 prezzoTotale = Methods.veryRound(Double.valueOf(tot)); 
	    			 }
	            	a.setPrezzoTotale(prezzoTotale);
	            	
	            	mon = it.getShippingPrice();
	    			 double costoSpedizione = 0;
	    			 if (mon!=null){
	    				 String tot = mon.getAmount();	 
	    				 costoSpedizione = Methods.veryRound(Double.valueOf(tot)); 
	    			 }
	    			 a.setCostoSpedizione(costoSpedizione);
	    			 
	    			 double prezzoUnitario =  0;
	    			 if (quantita!=0) prezzoUnitario = prezzoTotale/quantita;
	    			 a.setPrezzoUnitario(Methods.veryRound(prezzoUnitario));
	    			 
	            	mon = it.getItemTax();
	    			 double tax = 0;
	    			 if (mon!=null){
	    				 String tot = mon.getAmount();	 
	    				 tax = Double.valueOf(tot); 
	    			 }
	    			 a.setIva(tax);
	            	
	            	articoli.add(a);
	            	
	            }
	            
	            map.put(order_id, articoli);
    		}
    	}
    	
    	
    	return map;
    	
    }
    
    
    public static List<Ordine> elaboraETrasformaOrdini(List<Order> orders) throws Exception{
    	 List<Ordine> ordini = null;
    	 
    	 if (orders!=null && !orders.isEmpty()){
    		 
    		 List<ListOrderItemsRequest> listaRichieste = new ArrayList<ListOrderItemsRequest>();
    		 
    		 ordini = new ArrayList<Ordine>();
    		 
    		 for (Order ord : orders){
    			 if (!ord.getOrderStatus().equals("Pending")){
    			 
	    			 Ordine o = new Ordine();
	    			 o.setPiattaforma("Amazon");
	    			 o.setIdOrdinePiattaforma(ord.getAmazonOrderId());    			 
	    			 
	    			 String stato = ord.getOrderStatus();
	    			 if (stato.equals("Canceled")) stato = "Cancellato";
	    			 else if (stato.equals("Unshipped")) stato = "Non Spedito";
	    			 else if (stato.equals("Shipped")) stato = "Spedito";
	    			 else if (stato.equals("Pending")) stato = "In Attesa";
	    				 
	    			 o.setStato(stato);
	    			 
	    			XMLGregorianCalendar pd = ord.getPurchaseDate();
	    			//System.out.println("purchase date: "+pd.toString());
	    			//Date dataAcquisto = DateMethods.creaData(pd.getYear(),pd.getMonth(),pd.getDay(),pd.getHour(),pd.getDay());
	    			Date dataAcquisto = DateMethods.trasformaDataAmazon(pd.toString());	    			
	    			o.setDataAcquisto(dataAcquisto);
	    			
	    			if (stato.equals("Spedito") || stato.equals("Non Spedito"))
	    				o.setDataPagamento(dataAcquisto);
	    			
	    			XMLGregorianCalendar lud = ord.getLastUpdateDate();
	    			//System.out.println("last update date: "+pd.toString());
	    			//Date dataUltimaModifica = DateMethods.creaData(lud.getYear(),lud.getMonth(),lud.getDay(),lud.getHour(),lud.getDay());
	    			Date dataUltimaModifica = DateMethods.trasformaDataAmazon(lud.toString());
	    			o.setDataUltimaModifica(dataUltimaModifica);
	    			
	    			
	    			 Money mon = ord.getOrderTotal();
	    			 double totale = 0;
	    			 if (mon!=null){
	    				 String tot = mon.getAmount();	 
	    				 totale = Methods.veryRound(Double.valueOf(tot));
	    			 }
	    			 o.setTotale(totale);
	    			 o.setValuta("EUR");
	    			 
	    			 //TODO !!! trovare costo di spedizione degli ordini amazon
	    			 o.setCostoSpedizione(7);
	    			 
	    			 o.setQuantitaAcquistata(ord.getNumberOfItemsUnshipped());
	    			 
	    			 o.setEmail(ord.getBuyerEmail());
	    			 
	    			 String pagamento = ord.getPaymentMethod();
	    			 if (pagamento!=null && pagamento.equals("Other")) pagamento = "Checkout by Amazon"; 
	    			 
	    			 o.setMetodoPagamento(pagamento);
	    			 
	    			 o.setNomeAcquirente(ord.getBuyerName());
	    			 
	    			 Cliente c = new Cliente();
	     			 c.setNomeCompleto(ord.getBuyerName());
	     			 c.setEmail(ord.getBuyerEmail());
	     			 c.setPiattaforma("Amazon");
	     			
	     			Indirizzo indSpedizione = new Indirizzo();
	     			 
	    			 Address address = ord.getShippingAddress();
	    			 
	    			 if (address!=null){
		    			 
		    			 indSpedizione.setNomeCompleto(address.getName());
		    			 indSpedizione.setCap(address.getPostalCode());
		    			 indSpedizione.setTelefono(address.getPhone());
		    			 indSpedizione.setComune(address.getCity());
		    			 indSpedizione.setProvincia(address.getStateOrRegion());
		    			 indSpedizione.setIndirizzo1(address.getAddressLine1());
		    			 indSpedizione.setIndirizzo2(address.getAddressLine2() + " " + address.getAddressLine3());
		    			 //TODO !!! trovare la nazione di spedizione degli ordini amazon
		    			 indSpedizione.setNazione("IT");
		    			 
		    			 
		    			 c.setTelefono(address.getPhone());
	    			 }
	    			 
	    			 c.setIndirizzoSpedizione(indSpedizione);
	    			 o.setIndirizzoSpedizione(indSpedizione);
	    			 o.setCliente(c);
	    			 
	    			 //problema request is throttled
	    			//List<ArticoloAcquistato> articoli = ottieniArticoliDellOrdine(ord.getAmazonOrderId());
	    			//o.setElencoArticoli(articoli);
	    			
	    			ordini.add(o);
	    			
	    			
	    			ListOrderItemsRequest richiestaElencoArticoli = new ListOrderItemsRequest();
	    	        String sellerId = "A1L3HFHFCQOIJK";
	    	        richiestaElencoArticoli.setSellerId(sellerId);
	    	        richiestaElencoArticoli.setAmazonOrderId(ord.getAmazonOrderId());
	    	        
	    	        listaRichieste.add(richiestaElencoArticoli);
	    		 }
    		 }
    		 
    		 System.out.println("Ordini amazon: "+ordini.size()+" richieste di articoli");
    		 
    		 Map<String,List<ArticoloAcquistato>> mappaElencoArticoli = ottieniElencoArticoli(listaRichieste);
    		 
    		 for (Ordine o : ordini){
    			 List<ArticoloAcquistato> elencoArticoli = mappaElencoArticoli.get(o.getIdOrdinePiattaforma()); 
    			 o.setElencoArticoli(elencoArticoli);
    		 }
    	 }
    	 
    	 return ordini;
    }

    /**
     *  Command line entry point.
     */
    public static void main(String[] args) {

    	Date dataDa = DateMethods.creaData(2015, 4, 26, 0, 0); 
    	Date dataA = DateMethods.creaData(2015, 4, 30, 23, 59);
    	List<Ordine> ordini = scaricaOrdini(dataDa,dataA);
    	
    	for (Ordine o : ordini){
    		if (true){
    			
    			//List<ArticoloAcquistato> articoli = ottieniArticoliDellOrdine(o.getIdOrdinePiattaforma());
    			//o.setElencoArticoli(articoli);
    			
				Methods.stampaOrdine(o);
    		}
    	}
    	
    }

}
