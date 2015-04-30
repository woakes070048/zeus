package it.swb.piattaforme.amazon;

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

    /**
     * The endpoint for region service and version.
     * ex: serviceURL = MWSEndpoint.NA_PROD.toString();
     */
    private static final String serviceURL = MWSEndpoint.IT_PROD.toString();

    /** The client, lazy initialized. Async client is also a sync client. */
    private static MarketplaceWebServiceOrdersAsyncClient client = null;

    /**
     * Get a client connection ready to use.
     *
     * @return A ready to use client connection.
     */
    public static MarketplaceWebServiceOrdersClient getClient() {
        return getAsyncClient();
    }

    /**
     * Get an async client connection ready to use.
     *
     * @return A ready to use client connection.
     */
    public static synchronized MarketplaceWebServiceOrdersAsyncClient getAsyncClient() {
        if (client==null) {
            MarketplaceWebServiceOrdersConfig config = new MarketplaceWebServiceOrdersConfig();
            config.setServiceURL(serviceURL);
            // Set other client connection configurations here.
            client = new MarketplaceWebServiceOrdersAsyncClient(accessKey, secretKey, 
                    appName, appVersion, config, null);
        }
        return client;
    }
    
}
