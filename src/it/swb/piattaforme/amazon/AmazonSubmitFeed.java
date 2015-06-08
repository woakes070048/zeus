package it.swb.piattaforme.amazon;

import it.swb.log.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.amazonaws.mws.*;
import com.amazonaws.mws.model.*;

/**
 * 
 * Submit Feed Samples
 * 
 * 
 */
public class AmazonSubmitFeed {

    public static void main(String[] args) {
    	
    	//String percorso = "D:\\zeus\\spedizioni\\numeri_tracciamento_(2015-05-18_18.09.43).txt";
    	
    	//inviaModelloNumeriTracciamento(percorso);
    	
    	//SdaUtility.generaModelloConfermaSpedizioniAmazon(new Date());
    	
    	String percorso = "D:\\zeus\\mcd\\mcd_amazon_(2015-05-26_16.58.57).txt";
    	
    	inviaModelloCaricamentoArticoli(percorso);
    }
    
    
    public static void inviaModelloCaricamentoArticoli(String percorso){
    	Log.info("Invio ad amazon il file con i nuovi articoli: "+percorso);
    	
    	MarketplaceWebServiceClient webServiceClient = AmazonConfig.getWebServiceClient();
    	
    	SubmitFeedRequest request = new SubmitFeedRequest();
        
        request.setMerchant(AmazonConfig.getMerchantid());
        
        request.setFeedType("_POST_FLAT_FILE_LISTINGS_DATA_");
        
		try {
		        	
        	FileInputStream fis = new FileInputStream(percorso);
        	
        	request.setContentMD5(computeContentMD5HeaderValue(fis));
        	
			request.setFeedContent(fis);
			String risultato = invokeSubmitFeed(webServiceClient, request);
			
			Log.info("Il file è stato inviato con risultato: "+risultato);
			 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    
    public static void inviaModelloNumeriTracciamento(String percorso) {
    	
    	Log.info("Invio ad amazon il file con i numeri di tracciamento delle spedizioni: "+percorso);

    	MarketplaceWebServiceClient webServiceClient = AmazonConfig.getWebServiceClient();
    	
    	
 //       final String sellerDevAuthToken = "<Merchant Developer MWS Auth Token>";
        // marketplaces to which this feed will be submitted; look at the
        // API reference document on the MWS website to see which marketplaces are
        // included if you do not specify the list yourself

        SubmitFeedRequest request = new SubmitFeedRequest();
        
        request.setMerchant(AmazonConfig.getMerchantid());
        
        //request.setMWSAuthToken(sellerDevAuthToken);
        //final IdList marketplaces = new IdList(Arrays.asList(MWSEndpoint.IT_PROD.toString()));
        //request.setMarketplaceIdList(marketplaces);

        request.setFeedType("_POST_FLAT_FILE_FULFILLMENT_DATA_");

        // MWS exclusively offers a streaming interface for uploading your
        // feeds. This is because
        // feed sizes can grow to the 1GB+ range - and as your business grows
        // you could otherwise
        // silently reach the feed size where your in-memory solution will no
        // longer work, leaving you
        // puzzled as to why a solution that worked for a long time suddenly
        // stopped working though
        // you made no changes. For the same reason, we strongly encourage you
        // to generate your feeds to
        // local disk then upload them directly from disk to MWS via Java -
        // without buffering them in Java
        // memory in their entirety.
        // Note: MarketplaceWebServiceClient will not retry a submit feed request
        // because there is no way to reset the InputStream from our client. 
        // To enable retry, recreate the InputStream and resubmit the feed
        // with the new InputStream. 
        //
        // request.setFeedContent( new FileInputStream("my-feed.xml" /*or
        // "my-flat-file.txt" if you use flat files*/);
        
        try {
        	
        	FileInputStream fis = new FileInputStream(percorso);
        	
        	request.setContentMD5(computeContentMD5HeaderValue(fis));
        	
			request.setFeedContent(fis);
			 invokeSubmitFeed(webServiceClient, request);
			 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

    }

    /**
     * Submit Feed request sample Uploads a file for processing together with
     * the necessary metadata to process the file, such as which type of feed it
     * is. PurgeAndReplace if true means that your existing e.g. inventory is
     * wiped out and replace with the contents of this feed - use with caution
     * (the default is false).
     * 
     * @param service
     *            instance of MarketplaceWebService service
     * @param request
     *            Action to invoke
     */
    public static String invokeSubmitFeed(MarketplaceWebService service,
            SubmitFeedRequest request) {
    	
    	String risultato = "";
        try {

            SubmitFeedResponse response = service.submitFeed(request);

            System.out.println("SubmitFeed Action Response");
            System.out
            .println("=============================================================================");
            System.out.println();

            System.out.print("    SubmitFeedResponse");
            System.out.println();
            if (response.isSetSubmitFeedResult()) {
                System.out.print("        SubmitFeedResult");
                System.out.println();
                SubmitFeedResult submitFeedResult = response
                .getSubmitFeedResult();
                if (submitFeedResult.isSetFeedSubmissionInfo()) {
                    System.out.print("            FeedSubmissionInfo");
                    System.out.println();
                    FeedSubmissionInfo feedSubmissionInfo = submitFeedResult
                    .getFeedSubmissionInfo();
                    if (feedSubmissionInfo.isSetFeedSubmissionId()) {
                        System.out.print("                FeedSubmissionId");
                        System.out.println();
                        System.out.print("                    "
                                + feedSubmissionInfo.getFeedSubmissionId());
                        System.out.println();
                    }
                    if (feedSubmissionInfo.isSetFeedType()) {
                        System.out.print("                FeedType");
                        System.out.println();
                        System.out.print("                    "
                                + feedSubmissionInfo.getFeedType());
                        System.out.println();
                    }
                    if (feedSubmissionInfo.isSetSubmittedDate()) {
                        System.out.print("                SubmittedDate");
                        System.out.println();
                        System.out.print("                    "
                                + feedSubmissionInfo.getSubmittedDate());
                        System.out.println();
                    }
                    if (feedSubmissionInfo.isSetFeedProcessingStatus()) {
                    	risultato = feedSubmissionInfo.getFeedProcessingStatus();
                        System.out
                        .print("                FeedProcessingStatus");
                        System.out.println();
                        System.out.print("                    "
                                + feedSubmissionInfo.getFeedProcessingStatus());
                        System.out.println();
                    }
                    if (feedSubmissionInfo.isSetStartedProcessingDate()) {
                        System.out
                        .print("                StartedProcessingDate");
                        System.out.println();
                        System.out
                        .print("                    "
                                + feedSubmissionInfo
                                .getStartedProcessingDate());
                        System.out.println();
                    }
                    if (feedSubmissionInfo.isSetCompletedProcessingDate()) {
                        System.out
                        .print("                CompletedProcessingDate");
                        System.out.println();
                        System.out.print("                    "
                                + feedSubmissionInfo
                                .getCompletedProcessingDate());
                        System.out.println();
                    }
                }
            }
            if (response.isSetResponseMetadata()) {
                System.out.print("        ResponseMetadata");
                System.out.println();
                ResponseMetadata responseMetadata = response
                .getResponseMetadata();
                if (responseMetadata.isSetRequestId()) {
                    System.out.print("            RequestId");
                    System.out.println();
                    System.out.print("                "
                            + responseMetadata.getRequestId());
                    System.out.println();
                }
            }
            System.out.println(response.getResponseHeaderMetadata());
            System.out.println();
            System.out.println();

        } catch (MarketplaceWebServiceException ex) {

            System.out.println("Caught Exception: " + ex.getMessage());
            System.out.println("Response Status Code: " + ex.getStatusCode());
            System.out.println("Error Code: " + ex.getErrorCode());
            System.out.println("Error Type: " + ex.getErrorType());
            System.out.println("Request ID: " + ex.getRequestId());
            System.out.print("XML: " + ex.getXML());
            System.out.println("ResponseHeaderMetadata: " + ex.getResponseHeaderMetadata());
        }
        return risultato;
    }
    
    
    /**
     * Calculate content MD5 header values for feeds stored on disk.
     */
    public static String computeContentMD5HeaderValue( FileInputStream fis ) 
        throws IOException, NoSuchAlgorithmException {

        DigestInputStream dis = new DigestInputStream( fis,
            MessageDigest.getInstance( "MD5" ));

        byte[] buffer = new byte[8192];
        while( dis.read( buffer ) > 0 );

        String md5Content = new String(
            org.apache.commons.codec.binary.Base64.encodeBase64(
                dis.getMessageDigest().digest()) ); 

        // Effectively resets the stream to be beginning of the file
        // via a FileChannel.
        fis.getChannel().position( 0 );

        return md5Content;
    }

}
