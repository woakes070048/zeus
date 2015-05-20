package it.swb.piattaforme.amazon;

import com.amazonaws.mws.MarketplaceWebServiceClient;
import com.amazonaws.mws.MarketplaceWebServiceConfig;
import com.amazonservices.mws.orders._2013_09_01.MWSEndpoint;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersAsyncClient;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersClient;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersConfig;

public class AmazonConfig {
	
	
	
    /** Developer AWS access key. */
    private static final String accessKey = "AKIAI44QMMQLB3RM2C3Q";

    /** Developer AWS secret key. */
    private static final String secretKey = "s8iIFQsLIQPCr/jK2JaqqhSbYlPV3/WzwHwi/aZP";

    /** The client application name. */
    private static final String appName = "Zeus";

    /** The client application version. */
    private static final String appVersion = "0.1";
    
    private static final String merchantId = "A1L3HFHFCQOIJK";

    /**
     * The endpoint for region service and version.
     * ex: serviceURL = MWSEndpoint.NA_PROD.toString();
     */
    private static final String serviceURL = MWSEndpoint.IT_PROD.toString();

    /** The client, lazy initialized. Async client is also a sync client. */
    private static MarketplaceWebServiceOrdersAsyncClient ordersClient = null;
    
    private static MarketplaceWebServiceClient webServiceClient = null;

    /**
     * Get a client connection ready to use.
     *
     * @return A ready to use client connection.
     */
    public static MarketplaceWebServiceOrdersClient getClient() {
        return getOrdersAsyncClient();
    }

    /**
     * Get an async client connection ready to use.
     *
     * @return A ready to use client connection.
     */
    public static synchronized MarketplaceWebServiceOrdersAsyncClient getOrdersAsyncClient() {
        if (ordersClient==null) {
            MarketplaceWebServiceOrdersConfig config = new MarketplaceWebServiceOrdersConfig();
            config.setServiceURL(serviceURL);
            // Set other client connection configurations here.
            ordersClient = new MarketplaceWebServiceOrdersAsyncClient(accessKey, secretKey, 
                    appName, appVersion, config, null);
        }
        return ordersClient;
    }
    
    public static synchronized MarketplaceWebServiceClient getWebServiceClient() {
    	if (webServiceClient==null) {
	    	MarketplaceWebServiceConfig config = new MarketplaceWebServiceConfig();
	    	config.setServiceURL(serviceURL);
	    	
	    	webServiceClient = new MarketplaceWebServiceClient(accessKey, secretKey, 
	            appName, appVersion, config);
    	}
    	return webServiceClient;
    }

	public static String getMerchantid() {
		return merchantId;
	}
    
}
