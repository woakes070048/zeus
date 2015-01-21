package it.swb.model;

import java.util.Date;

public class LogArticolo {
	
	int idLog;
	String codiceArticolo;
	Date data;
	String azione;
	String note;
	int extra_1;
	
	public LogArticolo(){
		super();
	}

	public int getIdLog() {
		return idLog;
	}

	public void setIdLog(int idLog) {
		this.idLog = idLog;
	}

	public String getCodiceArticolo() {
		return codiceArticolo;
	}

	public void setCodiceArticolo(String codiceArticolo) {
		this.codiceArticolo = codiceArticolo;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getAzione() {
		return azione;
	}

	public void setAzione(String azione) {
		this.azione = azione;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getExtra_1() {
		return extra_1;
	}

	public void setExtra_1(int extra_1) {
		this.extra_1 = extra_1;
	}
	
	
	
	
}