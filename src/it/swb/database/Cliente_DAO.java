package it.swb.database;

import it.swb.log.Log;
import it.swb.model.Cliente;
import it.swb.model.Indirizzo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cliente_DAO {
	

	
	public static List<Cliente> getClienti(){
		Log.info("getClienti(): Cerco di ottenere la lista di clienti...");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		List<Cliente> clienti = null;

		try {			
			con = DataSource.getLocalConnection();
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM CLIENTI ORDER BY RAGIONE_SOCIALE ASC");
			
			clienti = new ArrayList<Cliente>();
			int i = 1;
			
			while (rs.next()){
				Cliente c = new Cliente();
				
				c.setCodiceCliente(rs.getString("CODICE_CLIENTE"));
				c.setRagioneSociale(rs.getString("RAGIONE_SOCIALE"));
				c.setProprietario(rs.getString("PROPRIETARIO"));
				
				Indirizzo isd = new Indirizzo();
				isd.setIndirizzo1(rs.getString("INDIRIZZO_SEDE_LEGALE"));
				isd.setCap(rs.getString("CAP_SEDE_LEGALE"));
				isd.setComune(rs.getString("LOCALITA_SEDE_LEGALE"));
				isd.setProvincia(rs.getString("PROVINCIA_SEDE_LEGALE"));
				
				c.setIndirizzoSedeLegale(isd);
				
				Indirizzo iu = new Indirizzo();
				iu.setIndirizzo1(rs.getString("INDIRIZZO_UFFICI"));
				iu.setCap(rs.getString("CAP_UFFICI"));
				iu.setComune(rs.getString("LOCALITA_UFFICI"));
				iu.setProvincia(rs.getString("PROVINCIA_UFFICI"));
				
				c.setIndirizzoUffici(iu);
				
				c.setCodiceFiscale(rs.getString("CODICE_FISCALE"));
				c.setPartitaIva(rs.getString("PARTITA_IVA"));
				c.setCodicePagamento(rs.getString("CODICE_PAGAMENTO"));
				c.setTelefono1(rs.getString("TELEFONO1"));
				c.setTelefono2(rs.getString("TELEFONO2"));
				c.setFax(rs.getString("FAX"));
				c.setEmail(rs.getString("EMAIL"));
				c.setResponsabileRappresentante(rs.getString("RESPONSABILE_RAPPRESENTANTE"));
				c.setTipoAttivita(rs.getString("TIPO_ATTIVITA"));
				c.setCodiceTipoAttivita(rs.getString("CODICE_TIPO_ATTIVITA"));
								
				clienti.add(c);
				i++;
			}
			Log.info("getClienti(): "+i+" clienti ottenuti.");

		} catch (Exception ex) {
			Log.info(ex);
			ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,st,null,rs);
		}
		return clienti;
	}
	
	public static List<Cliente> getClientiZelda(){
		Log.info("Cerco di ottenere la lista di clienti Zelda...");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		List<Cliente> clienti = null;

		try {			
			con = DataSource.getLocalConnection();
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM CLIENTI_ZELDA ORDER BY NOME_COMPLETO");
			
			clienti = new ArrayList<Cliente>();
			int i = 0;
			
			while (rs.next()){
				Cliente c = new Cliente();
				
				c.setIdCliente(rs.getInt("ID_CLIENTE"));
				c.setUsername(rs.getString("USERNAME"));
				c.setPiattaforma(rs.getString("PIATTAFORMA"));
				c.setTitolo(rs.getString("TITOLO"));
				c.setNomeCompleto(rs.getString("NOME_COMPLETO"));
				c.setAzienda(rs.getString("AZIENDA"));
				c.setCodiceFiscale(rs.getString("CODICE_FISCALE"));
				c.setPartitaIva(rs.getString("PARTITA_IVA"));
				c.setEmail(rs.getString("EMAIL"));
				c.setTelefono(rs.getString("TELEFONO"));
				c.setCellulare(rs.getString("CELLULARE"));
				c.setFax(rs.getString("FAX"));
				
				Indirizzo is = new Indirizzo();
				is.setIndirizzo1(rs.getString("INDIRIZZO_1"));
				is.setCap(rs.getString("CAP"));
				is.setComune(rs.getString("COMUNE"));
				is.setProvincia(rs.getString("PROVINCIA"));
				is.setNazione(rs.getString("NAZIONE"));
				is.setIndirizzo2(rs.getString("INDIRIZZO_2"));
				
				c.setIndirizzoSpedizione(is);
				
				clienti.add(c);
				i++;
			}
			Log.info(i+" clienti Zelda ottenuti.");

		} catch (Exception ex) {
			Log.info(ex);
			ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,st,null,rs);
		}
		return clienti;
	}
	
	
	public static Map<Integer,Cliente> getMappaClientiZelda(){
		Log.info("Cerco di ottenere la mappa di clienti Zelda...");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		Map<Integer,Cliente> clienti = null;

		try {			
			con = DataSource.getLocalConnection();
			st = con.createStatement();
			rs = st.executeQuery("SELECT id_cliente,username,telefono,nome_completo FROM CLIENTI_ZELDA");
			
			clienti = new HashMap<Integer,Cliente>();
			
			while (rs.next()){
				Cliente c = new Cliente();
				c.setIdCliente(rs.getInt("id_cliente"));
				c.setUsername(rs.getString("username"));
				c.setCellulare(rs.getString("telefono"));
				c.setNomeCompleto(rs.getString("nome_completo"));
				clienti.put(rs.getInt("id_cliente"),c);
			}
			Log.info("Mappa clienti Zelda ottenuta.");

		} catch (Exception ex) {
			Log.info(ex);
			ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,st,null,rs);
		}
		return clienti;
	}
	
	public static Map<String,Cliente> getMappaClientiZeldaCompleta(){
		Log.info("Cerco di ottenere la mappa di clienti Zelda...");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		Map<String,Cliente> clienti = null;

		try {			
			con = DataSource.getLocalConnection();
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM CLIENTI_ZELDA");
			
			clienti = new HashMap<String,Cliente>();
			
			while (rs.next()){
				Cliente c = new Cliente();
				
				c.setIdCliente(rs.getInt("id_cliente"));
				c.setUsername(rs.getString("USERNAME"));
				c.setNomeCompleto(rs.getString("NOME_COMPLETO"));
				c.setAzienda(rs.getString("azienda"));
				c.setCellulare(rs.getString("cellulare"));
				c.setTelefono(rs.getString("telefono"));
				c.setEmail(rs.getString("email"));
				c.setPiattaforma(rs.getString("piattaforma"));
				c.setPartitaIva(rs.getString("partita_iva"));
				c.setCodiceFiscale(rs.getString("codice_fiscale"));
				c.setFax(rs.getString("fax"));
				
				Indirizzo is = new Indirizzo();
				is.setAzienda(rs.getString("azienda"));
				is.setCap(rs.getString("cap"));
				is.setIndirizzo1(rs.getString("indirizzo_1"));
				is.setComune(rs.getString("comune"));
				is.setProvincia(rs.getString("provincia"));
				is.setNazione(rs.getString("nazione"));
				is.setIndirizzo2(rs.getString("indirizzo_2"));
				
				c.setIndirizzoSpedizione(is);
				
				if (c.getPiattaforma().equals("eBay"))
					clienti.put(c.getUsername(),c);
				else clienti.put(c.getEmail(), c);
			}
			Log.info("Mappa clienti Zelda ottenuta.");

		} catch (Exception ex) {
			Log.info(ex);
			ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,st,null,rs);
		}
		return clienti;
	}
	
	public static Map<Integer,Cliente> getMappaClientiZeldaCompletaByID(){
		Log.info("Cerco di ottenere la mappa di clienti Zelda...");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		Map<Integer,Cliente> clienti = null;

		try {			
			con = DataSource.getLocalConnection();
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM CLIENTI_ZELDA");
			
			clienti = new HashMap<Integer,Cliente>();
			
			while (rs.next()){
				Cliente c = new Cliente();
				
				c.setIdCliente(rs.getInt("id_cliente"));
				c.setUsername(rs.getString("USERNAME"));
				c.setNomeCompleto(rs.getString("NOME_COMPLETO"));
				c.setAzienda(rs.getString("azienda"));
				c.setCellulare(rs.getString("cellulare"));
				c.setTelefono(rs.getString("telefono"));
				c.setEmail(rs.getString("email"));
				c.setPiattaforma(rs.getString("piattaforma"));
				c.setPartitaIva(rs.getString("partita_iva"));
				c.setCodiceFiscale(rs.getString("codice_fiscale"));
				c.setFax(rs.getString("fax"));
				
				Indirizzo is = new Indirizzo();
				is.setAzienda(rs.getString("azienda"));
				is.setCap(rs.getString("cap"));
				is.setIndirizzo1(rs.getString("indirizzo_1"));
				is.setComune(rs.getString("comune"));
				is.setProvincia(rs.getString("provincia"));
				is.setNazione(rs.getString("nazione"));
				is.setIndirizzo2(rs.getString("indirizzo_2"));
				
				c.setIndirizzoSpedizione(is);
				
				clienti.put(c.getIdCliente(), c);
			}
			Log.info("Mappa clienti Zelda ottenuta.");

		} catch (Exception ex) {
			Log.info(ex);
			ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,st,null,rs);
		}
		return clienti;
	}
	
	
	public static int inserisciCliente(Cliente f){
//		Log.info("Inserimento cliente "+f.getCodiceCliente()+" nel database locale...");
		Connection con = null;
		PreparedStatement ps = null;
		int result = 0;

		try {			
			con = DataSource.getLocalConnection();
			String query = "INSERT INTO CLIENTI(`codice_cliente`,`ragione_sociale`,`proprietario`,`indirizzo_sede_legale`," + /*4*/
												"`cap_sede_legale`,`localita_sede_legale`,`provincia_sede_legale`," +			/*7*/
												"`indirizzo_uffici`,`cap_uffici`,`localita_uffici`,`provincia_uffici`," +		/*11*/
												"`codice_fiscale`,`partita_iva`,`telefono1`,`telefono2`,`fax`,`email`," +		/*17*/
												"`responsabile_rappresentante`,`codice_pagamento`,`tipo_attivita`," +			/*20*/
												"`codice_tipo_attivita`)" +														/*21*/
												" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; 
			ps = con.prepareStatement(query);
			
			ps.setString(1, f.getCodiceCliente());			
			ps.setString(2, f.getRagioneSociale());	
			ps.setString(3, f.getProprietario());	
			ps.setString(4, f.getIndirizzoSedeLegale().getIndirizzo1());			
			ps.setString(5, f.getIndirizzoSedeLegale().getCap());	
			ps.setString(6, f.getIndirizzoSedeLegale().getComune());	
			ps.setString(7, f.getIndirizzoSedeLegale().getProvincia());			
			ps.setString(8, f.getIndirizzoUffici().getIndirizzo1());	
			ps.setString(9, f.getIndirizzoUffici().getCap());	
			ps.setString(10, f.getIndirizzoUffici().getComune());			
			ps.setString(11, f.getIndirizzoUffici().getProvincia());	
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

//			Log.info("Inserimento riuscito.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { con.rollback();
			} catch (SQLException e) { e.printStackTrace();	}
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,null);
		}
		return result;
	}
	
	public static int inserisciClienteZelda(Cliente c, Connection con, PreparedStatement ps){
//		Log.info("Inserimento cliente zelda "+c.getUsername()+" nel database locale...");
		int id_cliente = 0;
		boolean closeCon = false;
		ResultSet rs = null;

		try {			
			if (con == null){
				con = DataSource.getLocalConnection();
				closeCon = true;
			}
			String query = "INSERT INTO `clienti_zelda`(`username`,`piattaforma`,`titolo`,`nome_completo`,`azienda`,`nazione`," +
					"`cap`,`provincia`,`comune`,`indirizzo_1`,`indirizzo_2`,`codice_fiscale`,`partita_iva`," +
					"`email`,`telefono`,`cellulare`,`fax`,`nome`,`cognome`)  VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
			
			ps = con.prepareStatement(query);
			
			ps.setString(1, c.getUsername());			
			ps.setString(2, c.getPiattaforma());	
			ps.setString(3, c.getTitolo());	
			ps.setString(4, c.getNomeCompleto());			
			ps.setString(5, c.getAzienda());	
			ps.setString(6, c.getIndirizzoSpedizione().getNazione());	
			ps.setString(7, c.getIndirizzoSpedizione().getCap());			
			ps.setString(8, c.getIndirizzoSpedizione().getProvincia());	
			ps.setString(9, c.getIndirizzoSpedizione().getComune());	
			ps.setString(10, c.getIndirizzoSpedizione().getIndirizzo1());			
			ps.setString(11, c.getIndirizzoSpedizione().getIndirizzo2());	
			ps.setString(12, c.getCodiceFiscale());	
			ps.setString(13, c.getPartitaIva());			
			ps.setString(14, c.getEmail());	
			ps.setString(15, c.getTelefono());	
			ps.setString(16, c.getCellulare());			
			ps.setString(17, c.getFax());	
			ps.setString(18, c.getNome());	
			ps.setString(19, c.getCognome());	
			
			ps.executeUpdate();
			
			con.commit();
			
			ps = con.prepareStatement("SELECT LAST_INSERT_ID() as id_cliente");
			
			rs = ps.executeQuery();
			
			while (rs.next()) id_cliente =  rs.getInt("id_cliente");

//			Log.info("Inserimento riuscito.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { con.rollback();
			} catch (SQLException e) { e.printStackTrace();	}
		}
		 finally {
			 if (closeCon)
				 DataSource.closeConnections(con,null,ps,rs);
		}
		return id_cliente;
	}
	
	
	public static int modificaCliente(Cliente f){
//		Log.info("Modifica cliente "+f.getCodiceCliente()+" nel database locale...");
		Connection con = null;
		PreparedStatement ps = null;
		int result = 0;

		try {			
			con = DataSource.getLocalConnection();
			String query = "UPDATE CLIENTI SET `ragione_sociale`= ?, `proprietario`= ?, `indirizzo_sede_legale`= ?, " + 					/*3*/
												"`cap_sede_legale`= ?, `localita_sede_legale`= ?, `provincia_sede_legale`= ?, " +			/*6*/
												"`indirizzo_uffici`= ?, `cap_uffici`= ?, `localita_uffici`= ?, `provincia_uffici`= ?, " +		/*10*/
												"`codice_fiscale`= ?, `partita_iva`= ?, `telefono1`= ?, `telefono2`= ?, `fax`= ?, `email`= ?, " +	/*16*/
												"`responsabile_rappresentante`= ?, `codice_pagamento`= ?, `tipo_attivita`= ?, " +			/*19*/
												"`codice_tipo_attivita`=? WHERE `codice_cliente` = ?"; 									/*21*/

			ps = con.prepareStatement(query);
			ps.setString(1, f.getRagioneSociale());				
			ps.setString(2, f.getProprietario());	
			ps.setString(3, f.getIndirizzoSedeLegale().getIndirizzo1());			
			ps.setString(4, f.getIndirizzoSedeLegale().getCap());	
			ps.setString(5, f.getIndirizzoSedeLegale().getComune());	
			ps.setString(6, f.getIndirizzoSedeLegale().getProvincia());			
			ps.setString(7, f.getIndirizzoUffici().getIndirizzo1());	
			ps.setString(8, f.getIndirizzoUffici().getCap());	
			ps.setString(9, f.getIndirizzoUffici().getComune());			
			ps.setString(10, f.getIndirizzoUffici().getProvincia());	
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
			ps.setString(21, f.getCodiceCliente());
			
			result = ps.executeUpdate();
			
			con.commit();
			
			
//			Log.info("Risultato modifica: "+result);

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { con.rollback();
			} catch (SQLException e) { e.printStackTrace();	}
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,null);
		}
		return result;
	}
	
	
	public static int modificaClienteZelda(Cliente c, int id_cliente, Connection con, PreparedStatement ps){
//		Log.info("Modifica cliente zelda "+c.getUsername()+" nel database locale...");
		int result = 0;
		boolean closeCon = false;

		try {	if (con == null){
			con = DataSource.getLocalConnection();
			closeCon = true;
		}
			String query = "UPDATE `clienti_zelda` SET `titolo` = ?,`nome_completo` = ?,`nome` = ?,`cognome` = ?,`azienda` = ?," +
							" `codice_fiscale` = ?, `partita_iva` = ?,`email` = ?,`telefono` = ?,`cellulare` = ?, `fax` = ?," +
							" `nazione` = ?, `cap` = ?,`provincia` = ?,`comune` = ?, `indirizzo_1` = ?,`indirizzo_2` = ? " +
							" WHERE `id_cliente` = ? "; 
			
			ps = con.prepareStatement(query);
			ps.setString(1, c.getTitolo());				
			ps.setString(2, c.getNomeCompleto());	
			ps.setString(3, c.getNome());	
			ps.setString(4, c.getCognome());	
			ps.setString(5, c.getAzienda());	
			ps.setString(6, c.getCodiceFiscale());	
			ps.setString(7, c.getPartitaIva());			
			ps.setString(8, c.getEmail());	
			ps.setString(9, c.getTelefono());	
			ps.setString(10, c.getCellulare());			
			ps.setString(11, c.getFax());	
			ps.setString(12, c.getIndirizzoSpedizione().getNazione());	
			ps.setString(13, c.getIndirizzoSpedizione().getCap());			
			ps.setString(14, c.getIndirizzoSpedizione().getProvincia());	
			ps.setString(15, c.getIndirizzoSpedizione().getComune());	
			ps.setString(16, c.getIndirizzoSpedizione().getIndirizzo1());			
			ps.setString(17, c.getIndirizzoSpedizione().getIndirizzo2());
			ps.setInt(18, id_cliente);
			
			result = ps.executeUpdate();
			
			if (closeCon) con.commit();

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { con.rollback();
			} catch (SQLException e) { e.printStackTrace();	}
		}
		 finally {
			 if (closeCon)
				 DataSource.closeConnections(con,null,ps,null);
		}
		return result;
	}
	
	

	
	public static boolean checkIfClienteExist(String codiceCliente){
		Log.info("checkIfClienteExist("+codiceCliente+")...");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean exist = false;

		try {			
			con = DataSource.getLocalConnection();
			ps = con.prepareStatement("SELECT * FROM CLIENTI WHERE CODICE_CLIENTE LIKE ? ");
			ps.setString(1, codiceCliente);
			rs = ps.executeQuery();
			
			while (rs.next()){
				exist = true;
				Log.info("checkIfClienteExist("+codiceCliente+"): esiste gia' un cliente con questo codice");
			}
			

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,rs);
		}
		return exist;
	}
	
	
	public static void eliminaCliente(String codiceCliente){
		Log.info("eliminaCliente("+codiceCliente+")...");
		Connection con = null;
		PreparedStatement ps = null;

		try {			
			con = DataSource.getLocalConnection();
			ps = con.prepareStatement("DELETE FROM CLIENTI WHERE CODICE_CLIENTE = ? ");
			ps.setString(1, codiceCliente);
			ps.executeUpdate();
			
			Log.info("eliminaCliente("+codiceCliente+"): eliminato");
			
			con.commit();

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,null);
		}
	}
	
	public static void eliminaClienteZelda(String username){
		Log.info("eliminaClienteZelda("+username+")...");
		Connection con = null;
		PreparedStatement ps = null;

		try {			
			con = DataSource.getLocalConnection();
			ps = con.prepareStatement("DELETE FROM CLIENTI_ZELDA WHERE USERNAME = ? ");
			ps.setString(1, username);
			ps.executeUpdate();
			
			Log.info("eliminaClienteZelda("+username+"): eliminato");
			
			con.commit();

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,null);
		}
	}

}
