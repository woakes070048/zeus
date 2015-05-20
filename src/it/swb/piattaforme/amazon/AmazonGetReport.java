package it.swb.piattaforme.amazon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import com.amazonaws.mws.*;
import com.amazonaws.mws.model.*;

/**
 *
 * Get Report  Samples
 *
 *
 */
public class AmazonGetReport {

    /**
     * Just add a few required parameters, and try the service
     * Get Report functionality
     *
     * @param args unused
     */
    public static void main(String[] args) {
    	
    }

    public static void scaricaReportOrdini(String idReport, String nome) {
    	MarketplaceWebServiceClient webServiceClient = AmazonConfig.getWebServiceClient();
    	
        //final String sellerDevAuthToken = "<Merchant Developer MWS Auth Token>";

        GetReportRequest request = new GetReportRequest();
        request.setMerchant(AmazonConfig.getMerchantid());
        
        //request.setMWSAuthToken(sellerDevAuthToken);

        request.setReportId(idReport);

        // Note that depending on the type of report being downloaded, a report can reach 
        // sizes greater than 1GB. For this reason we recommend that you _always_ program to
        // MWS in a streaming fashion. Otherwise, as your business grows you may silently reach
        // the in-memory size limit and have to re-work your solution.
        //
        OutputStream report = null;
        
		try {
			
			String percorso = "D:\\zeus\\ordini\\"+nome+".txt";
			
			File f = new File(percorso);
			
			f.createNewFile();
			
			report = new FileOutputStream(percorso);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        request.setReportOutputStream(report);

        invokeGetReport(webServiceClient, request);

    }



    /**
     * Get Report  request sample
     * The GetReport operation returns the contents of a report. Reports can potentially be
     * very large (>100MB) which is why we only return one report at a time, and in a
     * streaming fashion.
     *   
     * @param service instance of MarketplaceWebService service
     * @param request Action to invoke
     */
    public static void invokeGetReport(MarketplaceWebService service, GetReportRequest request) {
        try {

            GetReportResponse response = service.getReport(request);


            System.out.println ("GetReport Action Response");
            System.out.println ("=============================================================================");
            System.out.println ();

            System.out.print("    GetReportResponse");
            System.out.println();
            System.out.print("    GetReportResult");
            System.out.println();
            System.out.print("            MD5Checksum");
            System.out.println();
            System.out.print("                " + response.getGetReportResult().getMD5Checksum());
            System.out.println();
            if (response.isSetResponseMetadata()) {
                System.out.print("        ResponseMetadata");
                System.out.println();
                ResponseMetadata  responseMetadata = response.getResponseMetadata();
                if (responseMetadata.isSetRequestId()) {
                    System.out.print("            RequestId");
                    System.out.println();
                    System.out.print("                " + responseMetadata.getRequestId());
                    System.out.println();
                }
            } 
            System.out.println();

            System.out.println("Report");
            System.out.println ("=============================================================================");
            System.out.println();
            System.out.println( request.getReportOutputStream().toString() );
            System.out.println();

            System.out.println(response.getResponseHeaderMetadata());
            System.out.println();


        } catch (MarketplaceWebServiceException ex) {
        	ex.printStackTrace();

            System.out.println("Caught Exception: " + ex.getMessage());
            System.out.println("Response Status Code: " + ex.getStatusCode());
            System.out.println("Error Code: " + ex.getErrorCode());
            System.out.println("Error Type: " + ex.getErrorType());
            System.out.println("Request ID: " + ex.getRequestId());
            System.out.print("XML: " + ex.getXML());
            System.out.println("ResponseHeaderMetadata: " + ex.getResponseHeaderMetadata());
        }
    }

}
