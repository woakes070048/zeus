package it.swb.business;

import it.swb.database.Report_DAO;

import java.util.List;

public class ReportBusiness {
	
    private static ReportBusiness instance = new ReportBusiness();
    
    /* Costruttore privato della classe */
    private ReportBusiness() {}

    /* Metodo che permette di ottenere l'istanza della classe */
    public static ReportBusiness getInstance() {
        return instance;
    }
    
    private List<Object[]> reportVendite;
    private List<Object[]> reportOrdini;
	
	public List<Object[]> getReportVendite(String dataDa, String dataA, String statoOrdine, boolean raggruppaArticoli) {
		if (reportVendite==null)
			reportVendite = Report_DAO.getReportVendite(dataDa, dataA, statoOrdine, raggruppaArticoli);
		return reportVendite;
	}
	
	public List<Object[]> getReportOrdini(String groupBy, boolean dividiX) {
		if (reportOrdini==null)
			reportOrdini = Report_DAO.getReportOrdini(groupBy,dividiX);
		return reportOrdini;
	}
	
	public List<Object[]> reloadReportVendite(String dataDa, String dataA, String statoOrdine, boolean raggruppaArticoli) {
		reportVendite=null;
		return getReportVendite(dataDa, dataA, statoOrdine, raggruppaArticoli);
	}
	
	public List<Object[]> reloadReportOrdini(String groupBy, boolean dividiX) {
		reportOrdini=null;
		return getReportOrdini(groupBy,dividiX);
	}
}
