package it.swb.database;

import it.swb.log.Log;
import it.swb.model.Fornitore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Fornitore_DAO {
	
	public static List<Fornitore> getFornitori(){
		Log.info("getFornitori(): Cerco di ottenere la lista di fornitori...");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		List<Fornitore> fornitori = null;

		try {			
			con = DataSource.getLocalConnection();
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM FORNITORI ORDER BY RAGIONE_SOCIALE ASC");
			
			fornitori = new ArrayList<Fornitore>();
			int i = 0;
			
			while (rs.next()){
				Fornitore f = new Fornitore();
				
				f.setCodiceFornitore(rs.getString("CODICE_FORNITORE"));
				f.setRagioneSociale(rs.getString("RAGIONE_SOCIALE"));
				f.setProprietario(rs.getString("PROPRIETARIO"));
				
				f.setIndirizzoSedeLegale(rs.getString("INDIRIZZO_SEDE_LEGALE"));
				f.setCapSedeLegale(rs.getString("CAP_SEDE_LEGALE"));						
				f.setLocalitaSedeLegale(rs.getString("LOCALITA_SEDE_LEGALE"));
				f.setProvinciaSedeLegale(rs.getString("PROVINCIA_SEDE_LEGALE"));
				f.setIndirizzoUffici(rs.getString("INDIRIZZO_UFFICI"));
				f.setCapUffici(rs.getString("CAP_UFFICI"));						
				f.setLocalitaUffici(rs.getString("LOCALITA_UFFICI"));
				f.setProvinciaUffici(rs.getString("PROVINCIA_UFFICI"));
				
				f.setCodiceFiscale(rs.getString("CODICE_FISCALE"));
				f.setPartitaIva(rs.getString("PARTITA_IVA"));
				f.setCodicePagamento(rs.getString("CODICE_PAGAMENTO"));
				f.setTelefono1(rs.getString("TELEFONO1"));
				f.setTelefono2(rs.getString("TELEFONO2"));
				f.setFax(rs.getString("FAX"));
				f.setEmail(rs.getString("EMAIL"));
				f.setResponsabileRappresentante(rs.getString("RESPONSABILE_RAPPRESENTANTE"));
				f.setTipoAttivita(rs.getString("TIPO_ATTIVITA"));
				f.setCodiceTipoAttivita(rs.getString("CODICE_TIPO_ATTIVITA"));
								
				fornitori.add(f);
				i++;
			}
			Log.info("getFornitori(): "+i+" fornitori ottenuti.");

		} catch (Exception ex) {
			Log.info(ex);
			ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,st,null,rs);
		}
		return fornitori;
	}
	
	
	public static int inserisciFornitore(Fornitore f){
		Log.info("Inserimento fornitore "+f.getCodiceFornitore()+" nel database locale...");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int result = 0;

		try {			
			con = DataSource.getLocalConnection();
			String query = "INSERT INTO FORNITORI(`codice_fornitore`,`ragione_sociale`,`proprietario`,`indirizzo_sede_legale`," + /*4*/
												"`cap_sede_legale`,`localita_sede_legale`,`provincia_sede_legale`," +			/*7*/
												"`indirizzo_uffici`,`cap_uffici`,`localita_uffici`,`provincia_uffici`," +		/*11*/
												"`codice_fiscale`,`partita_iva`,`telefono1`,`telefono2`,`fax`,`email`," +		/*17*/
												"`responsabile_rappresentante`,`codice_pagamento`,`tipo_attivita`," +			/*20*/
												"`codice_tipo_attivita`)" +														/*21*/
												" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; 
			ps = con.prepareStatement(query);
			
			ps.setString(1, f.getCodiceFornitore());			
			ps.setString(2, f.getRagioneSociale());	
			ps.setString(3, f.getProprietario());	
			ps.setString(4, f.getIndirizzoSedeLegale());			
			ps.setString(5, f.getCapSedeLegale());	
			ps.setString(6, f.getLocalitaSedeLegale());	
			ps.setString(7, f.getProvinciaSedeLegale());			
			ps.setString(8, f.getIndirizzoUffici());	
			ps.setString(9, f.getCapUffici());	
			ps.setString(10, f.getLocalitaUffici());			
			ps.setString(11, f.getProvinciaUffici());	
			ps.setString(12, f.getCodiceFiscale());	
			ps.setString(13, f.getPartitaIva());			
			ps.setString(14, f.getTelefono1());	
			ps.setString(15, f.getTelefono2());	
			ps.setString(16, f.getFax());			
			ps.setString(17, f.getEmail());	
			ps.setString(18, f.getResponsabileRappresentante());	
			ps.setString(19, f.getCodicePagamento());			
			ps.setString(20, f.getTipoAttivita());	
			ps.setString(21, f.getCodiceTipoAttivita());	
			
			ps.executeUpdate();
			
			con.commit();

			Log.info("Inserimento riuscito.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { con.rollback();
			} catch (SQLException e) { e.printStackTrace();	}
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,rs);
		}
		return result;
	}
	
	
	public static int modificaFornitore(Fornitore f){
		Log.info("Modifica fornitore "+f.getCodiceFornitore()+" nel database locale...");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int result = 0;

		try {			
			con = DataSource.getLocalConnection();
			String query = "UPDATE FORNITORI SET `ragione_sociale`= ?, `proprietario`= ?, `indirizzo_sede_legale`= ?, " + 					/*3*/
												"`cap_sede_legale`= ?, `localita_sede_legale`= ?, `provincia_sede_legale`= ?, " +			/*6*/
												"`indirizzo_uffici`= ?, `cap_uffici`= ?, `localita_uffici`= ?, `provincia_uffici`= ?, " +		/*10*/
												"`codice_fiscale`= ?, `partita_iva`= ?, `telefono1`= ?, `telefono2`= ?, `fax`= ?, `email`= ?, " +	/*16*/
												"`responsabile_rappresentante`= ?, `codice_pagamento`= ?, `tipo_attivita`= ?, " +			/*19*/
												"`codice_tipo_attivita`=? WHERE `codice_fornitore` = ?"; 									/*21*/

			ps = con.prepareStatement(query);
			ps.setString(1, f.getRagioneSociale());				
			ps.setString(2, f.getProprietario());	
			ps.setString(3, f.getIndirizzoSedeLegale());			
			ps.setString(4, f.getCapSedeLegale());	
			ps.setString(5, f.getLocalitaSedeLegale());	
			ps.setString(6, f.getProvinciaSedeLegale());			
			ps.setString(7, f.getIndirizzoUffici());	
			ps.setString(8, f.getCapUffici());	
			ps.setString(9, f.getLocalitaUffici());			
			ps.setString(10, f.getProvinciaUffici());	
			ps.setString(11, f.getCodiceFiscale());	
			ps.setString(12, f.getPartitaIva());			
			ps.setString(13, f.getTelefono1());	
			ps.setString(14, f.getTelefono2());	
			ps.setString(15, f.getFax());			
			ps.setString(16, f.getEmail());	
			ps.setString(17, f.getResponsabileRappresentante());	
			ps.setString(18, f.getCodicePagamento());			
			ps.setString(19, f.getTipoAttivita());	
			ps.setString(20, f.getCodiceTipoAttivita());
			ps.setString(21, f.getCodiceFornitore());
			
			result = ps.executeUpdate();
			
			con.commit();
			
			
			Log.info("Risultato modifica: "+result);

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { con.rollback();
			} catch (SQLException e) { e.printStackTrace();	}
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,rs);
		}
		return result;
	}
	
	
	
	
	public static boolean checkIfFornitoreExist(String codiceFornitore){
		Log.info("checkIfFornitoreExist("+codiceFornitore+")...");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean exist = false;

		try {			
			con = DataSource.getLocalConnection();
			ps = con.prepareStatement("SELECT * FROM FORNITORI WHERE CODICE_FORNITORE LIKE ? ");
			ps.setString(1, codiceFornitore);
			rs = ps.executeQuery();
			
			while (rs.next()){
				exist = true;
				Log.info("checkIfFornitoreExist("+codiceFornitore+"): esiste gia' un fornitore con questo codice");
			}
			

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,rs);
		}
		return exist;
	}
	
	
	public static void eliminaFornitore(String codiceFornitore){
		Log.info("eliminaFornitore("+codiceFornitore+")...");
		Connection con = null;
		PreparedStatement ps = null;

		try {			
			con = DataSource.getLocalConnection();
			ps = con.prepareStatement("DELETE FROM FORNITORI WHERE CODICE_FORNITORE = ? ");
			ps.setString(1, codiceFornitore);
			ps.executeUpdate();
			
			Log.info("eliminaFornitore("+codiceFornitore+"): eliminato");
			
			con.commit();

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,null);
		}
	}

}
