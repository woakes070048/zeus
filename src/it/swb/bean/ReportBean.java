package it.swb.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import it.swb.business.ReportBusiness;
import it.swb.utility.Methods;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "reportBean")
@ViewScoped
public class ReportBean  implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private List<Object[]> reportVendite;  
    
    private List<Object[]> reportOrdini;
    
    private List<Object[]> reportOrdiniFiltrati;
    
    private List<Object[]> reportVenditeFiltrati;  
    
    private Object[] reportSelezionato;
    
    private Date dataDa = Methods.sottraiGiorniAData(Methods.oraDelleStreghe(new Date()), 90);
    private Date dataA = Methods.ventitreCinquantanove(new Date());
    
    private Date dataOggi = new Date();
    
    private Boolean raggruppaArticoli = true;
    private String statoOrdine = "Spedito";
    
    private String groupBy = "Giorno";
    private Boolean dividiX = false;
    
    public void getVendite(){
    	String da = Methods.formattaData2(Methods.oraDelleStreghe(dataDa)); 
		String a = Methods.formattaData2(Methods.ventitreCinquantanove(dataA)); 
		reportVendite = ReportBusiness.getInstance().reloadReportVendite(da,a,statoOrdine,raggruppaArticoli);
    }
    
    public void getOrdini(){
    	reportOrdini = ReportBusiness.getInstance().reloadReportOrdini(groupBy,dividiX);
    }
    
    public int getTotaleOrdini() {  
        int total = 0; 
        
        List<Object[]> l = getReportOrdini();
        if (getReportOrdiniFiltrati()!=null) l = getReportOrdiniFiltrati();
  
        for(Object[] s : l) {  
            total += (int) s[2];  
        }  
        return total;  
    }  
  
    public double getTotaleFatturato() {  
    	double total = 0;  
    	
    	List<Object[]> l = getReportOrdini();
        if (getReportOrdiniFiltrati()!=null) l = getReportOrdiniFiltrati();
    	
        for(Object[] s : l) {  
            total += (double) s[3];  
        }  
        
        return total;
    }  
    
	public List<Object[]> getReportVendite() {
		if (reportVendite==null){
			String da = Methods.formattaData2(Methods.oraDelleStreghe(dataDa)); 
			String a = Methods.formattaData2(Methods.ventitreCinquantanove(dataA)); 
			reportVendite = ReportBusiness.getInstance().getReportVendite(da,a,statoOrdine,raggruppaArticoli);
		}
		return reportVendite;
	}
	
	public void reloadReportVendite() {
		String da = Methods.formattaData2(Methods.oraDelleStreghe(dataDa)); 
		String a = Methods.formattaData2(Methods.ventitreCinquantanove(dataA)); 
		reportVendite = ReportBusiness.getInstance().reloadReportVendite(da,a,statoOrdine,raggruppaArticoli);
	}

	public void setReportVendite(List<Object[]> reportVendite) {
		this.reportVendite = reportVendite;
	}

	public List<Object[]> getReportVenditeFiltrati() {
		return reportVenditeFiltrati;
	}

	public void setReportVenditeFiltrati(List<Object[]> reportVenditeFiltrati) {
		this.reportVenditeFiltrati = reportVenditeFiltrati;
	}

	public Object[] getReportSelezionato() {
		return reportSelezionato;
	}

	public void setReportSelezionato(Object[] reportSelezionato) {
		this.reportSelezionato = reportSelezionato;
	}

	public List<Object[]> getReportOrdini() {
		if (reportOrdini==null)
			reportOrdini = ReportBusiness.getInstance().getReportOrdini(groupBy,dividiX);
		return reportOrdini;
	}

	public void setReportOrdini(List<Object[]> reportOrdini) {
		this.reportOrdini = reportOrdini;
	}

	public String getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	public Boolean getDividiX() {
		return dividiX;
	}

	public void setDividiX(Boolean dividiX) {
		this.dividiX = dividiX;
	}

	public List<Object[]> getReportOrdiniFiltrati() {
		return reportOrdiniFiltrati;
	}

	public void setReportOrdiniFiltrati(List<Object[]> reportOrdiniFiltrati) {
		this.reportOrdiniFiltrati = reportOrdiniFiltrati;
	}

	public Date getDataDa() {
		return dataDa;
	}

	public void setDataDa(Date dataDa) {
		this.dataDa = dataDa;
	}

	public Date getDataA() {
		return dataA;
	}

	public void setDataA(Date dataA) {
		this.dataA = dataA;
	}

	public Date getDataOggi() {
		return dataOggi;
	}

	public void setDataOggi(Date dataOggi) {
		this.dataOggi = dataOggi;
	}

	public String getStatoOrdine() {
		return statoOrdine;
	}

	public void setStatoOrdine(String statoOrdine) {
		this.statoOrdine = statoOrdine;
	}

	public Boolean getRaggruppaArticoli() {
		return raggruppaArticoli;
	}

	public void setRaggruppaArticoli(Boolean raggruppaArticoli) {
		this.raggruppaArticoli = raggruppaArticoli;
	}
    

  }
