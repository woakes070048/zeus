package it.swb.piattaforme.amazon;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.mws.*;
import com.amazonaws.mws.model.*;

/**
 *
 * Get Report List  Samples
 *
 *
 */
public class AmazonGetReportList {

    /**
     * Just add a few required parameters, and try the service
     * Get Report List functionality
     *
     * @param args unused
     */
	
	
	
    public static void main(String... args) {
    	
    	String id[] = richiediListaReportOrdini();
    	
    	AmazonGetReport.scaricaReportOrdini(id[0], id[1]);
    }

    public static String[] richiediListaReportOrdini(){
    	
    	MarketplaceWebServiceClient webServiceClient = AmazonConfig.getWebServiceClient();

        /************************************************************************
         * Uncomment to try out Mock Service that simulates Marketplace Web Service 
         * responses without calling Marketplace Web Service  service.
         *
         * Responses are loaded from local XML files. You can tweak XML files to
         * experiment with various outputs during development
         *
         * XML files available under com/amazonaws/mws/mock tree
         *
         ***********************************************************************/
//        MarketplaceWebService service = new MarketplaceWebServiceMock();

        /************************************************************************
         * Marketplace and Merchant IDs are required parameters for all 
         * Marketplace Web Service calls.
         ***********************************************************************/

        GetReportListRequest request = new GetReportListRequest();
        request.setMerchant( AmazonConfig.getMerchantid() );
        
        TypeList t = new TypeList();
        List<String> list = new ArrayList<String>();
        list.add("_GET_FLAT_FILE_ORDERS_DATA_");
        t.setType(list);
        request.setReportTypeList(t);

        List<String[]> ids = invokeGetReportList(webServiceClient, request);

        return ids.get(0);
    }



    /**
     * Get Report List  request sample
     * returns a list of reports; by default the most recent ten reports,
     * regardless of their acknowledgement status
     *   
     * @param service instance of MarketplaceWebService service
     * @param request Action to invoke
     */
    public static List<String[]> invokeGetReportList(MarketplaceWebService service, GetReportListRequest request) {
        List<String[]> reportIds = new ArrayList<String[]>();
    	
    	try {

            GetReportListResponse response = service.getReportList(request);


//            System.out.println ("GetReportList Action Response");
//            System.out.println ("=============================================================================");
//            System.out.println ();

//            System.out.print("    GetReportListResponse");
//            System.out.println();
            
            if (response.isSetGetReportListResult()) {
                System.out.print("        GetReportListResult");
                System.out.println();
                GetReportListResult  getReportListResult = response.getGetReportListResult();
                
//                if (getReportListResult.isSetNextToken()) {
//                    System.out.print("            NextToken: "+ getReportListResult.getNextToken());
//                    System.out.println();
//                }
//                if (getReportListResult.isSetHasNext()) {
//                    System.out.print("            HasNext: " + getReportListResult.isHasNext());
//                    System.out.println();
//                }
                
                java.util.List<ReportInfo> reportInfoListList = getReportListResult.getReportInfoList();
                
                for (ReportInfo reportInfoList : reportInfoListList) {
                	
//                    System.out.print("            ReportInfoList");
//                    System.out.println();
                	
                	String s[] = new String[2];
                	
                    if (reportInfoList.isSetReportId()) {
                        System.out.print("                ReportId: "+ reportInfoList.getReportId());
                        s[0] = reportInfoList.getReportId();
                    }

                    if (reportInfoList.isSetReportRequestId()) {
                        System.out.println("                ReportRequestId: " + reportInfoList.getReportRequestId());
                        s[1] = reportInfoList.getReportRequestId();
                    }
                    
                    reportIds.add(s);
                    
//                  if (reportInfoList.isSetReportType()) {
//                  System.out.print("                ReportType");
//                  System.out.println();
//                  System.out.print("                    " + reportInfoList.getReportType());
//                  System.out.println();
//              }
//                    if (reportInfoList.isSetAvailableDate()) {
//                        System.out.print("                AvailableDate");
//                        System.out.println();
//                        System.out.print("                    " + reportInfoList.getAvailableDate());
//                        System.out.println();
//                    }
//                    if (reportInfoList.isSetAcknowledged()) {
//                        System.out.print("                Acknowledged");
//                        System.out.println();
//                        System.out.print("                    " + reportInfoList.isAcknowledged());
//                        System.out.println();
//                    }
//                    if (reportInfoList.isSetAcknowledgedDate()) {
//                        System.out.print("                AcknowledgedDate");
//                        System.out.println();
//                        System.out.print("                    " + reportInfoList.getAcknowledgedDate());
//                        System.out.println();
//                    }
                }
            } 
//            if (response.isSetResponseMetadata()) {
//                System.out.print("        ResponseMetadata");
//                System.out.println();
//                ResponseMetadata  responseMetadata = response.getResponseMetadata();
//                if (responseMetadata.isSetRequestId()) {
//                    System.out.print("            RequestId");
//                    System.out.println();
//                    System.out.print("                " + responseMetadata.getRequestId());
//                    System.out.println();
//                }
//            } 
//            System.out.println();
//            System.out.println(response.getResponseHeaderMetadata());
//            System.out.println();


        } catch (MarketplaceWebServiceException ex) {

            System.out.println("Caught Exception: " + ex.getMessage());
            System.out.println("Response Status Code: " + ex.getStatusCode());
            System.out.println("Error Code: " + ex.getErrorCode());
            System.out.println("Error Type: " + ex.getErrorType());
            System.out.println("Request ID: " + ex.getRequestId());
            System.out.print("XML: " + ex.getXML());
            System.out.println("ResponseHeaderMetadata: " + ex.getResponseHeaderMetadata());
        }
    	
    	return reportIds;
    }

}

