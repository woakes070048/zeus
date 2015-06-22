package it.swb.database;

import it.swb.log.Log;
import it.swb.model.LogArticolo;
import it.swb.model.Variante_Articolo;
import it.swb.utility.Methods;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Variante_Articolo_DAO {
	
	public static void inserisciOModificaVarianti(List<Variante_Articolo> varianti, String codice_articolo, DbTool dbt){
		inserisciOModificaVarianti(varianti, codice_articolo, dbt.getConnection(), dbt.getPreparedStatement());
	}

	public static void inserisciOModificaVarianti(List<Variante_Articolo> varianti, String codice_articolo, Connection con, PreparedStatement ps){
		boolean closeCon = false;
		if (con==null){
			con = DataSource.getLocalConnection();
			closeCon=true;
		}	
		for (Variante_Articolo v : varianti){
			v.setCodiceArticolo(codice_articolo);
			int id_variante = controllaSeVarianteEsiste(v, con, ps);
			if (id_variante==-1)
				inserisciVariante(v, codice_articolo, con, ps);
			else {
				v.setIdVariante(id_variante);
				modificaVariante(v, codice_articolo, con, ps);
			}
		}
		if (closeCon)
			try {
				con.commit();
				DataSource.closeConnections(con,null,ps,null);
			} catch (Exception e) {
				Log.info(e);
				e.printStackTrace();
			}
	}
	
	public static void inserisciVariante(Variante_Articolo v, String codice_articolo, Connection con, PreparedStatement ps){
		Log.debug("Inserimento variante nel database locale, valore: "+v.getValore()+", Codice articolo: "+codice_articolo);
		boolean closeCon = false;
		try {			
			if (con==null){
				con = DataSource.getLocalConnection();
				closeCon=true;
			}	
	
			String query = "INSERT INTO VARIANTI_ARTICOLO(`codice_articolo`,`tipo`,`valore`,`immagine`,`quantita`,`codice_barre`,`tipo_codice_barre`,`dimensioni`)" +
								" VALUES (?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE codice_articolo=codice_articolo";
			ps = con.prepareStatement(query);
			ps.setString(1, codice_articolo);	
			ps.setString(2, v.getTipo());	
			ps.setString(3, v.getValore());	
			ps.setString(4, Methods.trimAndToLower(v.getImmagine()));	
			ps.setInt(5, v.getQuantita());	
			ps.setString(6, v.getCodiceBarre());
			ps.setString(7, v.getTipoCodiceBarre());
			ps.setString(8, v.getDimensioni());
			ps.executeUpdate();
			
			LogArticolo l = new LogArticolo();
			l.setCodiceArticolo(codice_articolo);
			l.setAzione("Creazione Variante");
			l.setNote("Inserita la variante "+v.getValore());
			LogArticolo_DAO.inserisciLogArticolo(l, con, ps);
			
			if (closeCon) con.commit();
			
			Log.debug("Inserimento variante riuscito.");
						
		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { con.rollback();
			} catch (SQLException e) { Log.info(e); e.printStackTrace();	}
		}
		finally {
			 if(closeCon) DataSource.closeConnections(con,null,ps,null);
		}
	}
	
	public static void modificaQuantitaVarianti(String codice_articolo, Connection con, PreparedStatement ps) throws Exception{
		
		String query = "UPDATE `varianti_articolo` " +
								"SET quantita = 0 " +
								"WHERE codice_articolo = ? ";
		
		ps = con.prepareStatement(query);
		
		ps.setString(1, codice_articolo);	
		
		ps.executeUpdate();
		
	}
	
	
	public static void modificaVariante(Variante_Articolo v, String codice_articolo, Connection con, PreparedStatement ps){
		Log.debug("Modifica Variante: "+v.getValore()+", barcode: "+v.getCodiceBarre());
		boolean closeCon = false;
		try {			
			if (con==null){
				con = DataSource.getLocalConnection();
				closeCon=true;
			}
			String query = "UPDATE `varianti_articolo` " +
									"SET `codice_articolo`= ? ,`tipo`= ? ,`valore`= ? ,`immagine`= ? ,`quantita`= ?,`dimensioni`= ?,`codice_barre`= ?,`tipo_codice_barre`= ? " +
									"WHERE `id_variante`= ? ";
			
			ps = con.prepareStatement(query);
			
			ps.setString(1, codice_articolo);	
			ps.setString(2, v.getTipo());	
			ps.setString(3, v.getValore());	
			ps.setString(4, Methods.trimAndToLower(v.getImmagine()));	
			ps.setInt(5, v.getQuantita());	
			ps.setString(6, v.getDimensioni());
			ps.setString(7, v.getCodiceBarre());
			ps.setString(8, v.getTipoCodiceBarre());
			ps.setInt(9, v.getIdVariante());	
			
			ps.executeUpdate();
			
			LogArticolo l = new LogArticolo();
			l.setCodiceArticolo(codice_articolo);
			l.setAzione("Modifica Variante");
			l.setNote("Modificata la variante "+v.getValore());
			LogArticolo_DAO.inserisciLogArticolo(l, con, ps);
			
			if (closeCon) con.commit();
			
			Log.debug("Modifica variante riuscita.");
						
		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { con.rollback();
			} catch (SQLException e) { Log.info(e); e.printStackTrace();	}
		}
		 finally {
			 if(closeCon) DataSource.closeConnections(con,null,ps,null);
		}
	}
	
	
	
	public static Map<String, String> getVariantiMap(Connection con){
		Log.debug("Cerco di ottenere la mappa delle varianti...");
		Statement st = null;
		ResultSet rs = null;
		Map<String,String> varianti = null;

		try {			
			st = con.createStatement();
			rs = st.executeQuery("SELECT distinct(codice_articolo) FROM varianti_articolo order by codice_articolo asc;");
			
			varianti = new HashMap<String,String>();
			
			while (rs.next()){
				varianti.put(rs.getString("codice_articolo"),rs.getString("codice_articolo"));
			}
			Log.debug("Mappa delle varianti creata.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { con.rollback();
			} catch (SQLException e) { Log.info(e); e.printStackTrace();	}
		}
		 finally {
			 DataSource.closeStatements(st,null,rs);
		}
		return varianti;
	}
	
	public static Map<String, List<Variante_Articolo>> getMappaVariantiCompleta(DbTool dbt){
		Log.info("Caricamento mappa delle varianti...");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		Map<String,List<Variante_Articolo>> varianti = null;

		try {			
			if (dbt==null){
				con = DataSource.getLocalConnection();
				st = con.createStatement();
			}else {
				con = dbt.getConnection();
				st = dbt.getStatement();
				rs = dbt.getResultSet();
			}
			
			rs = st.executeQuery("SELECT * FROM varianti_articolo order by codice_articolo asc;");
			
			varianti = new HashMap<String,List<Variante_Articolo>>();
			
			while (rs.next()){
				Variante_Articolo v = new Variante_Articolo();
				
				String cod = rs.getString("CODICE_ARTICOLO");
				
				v.setCodiceArticolo(cod);
				v.setIdVariante(rs.getInt("id_variante"));
				v.setImmagine(rs.getString("immagine"));
				v.setTipo(rs.getString("tipo"));
				v.setQuantita(rs.getInt("quantita"));
				v.setValore(rs.getString("valore"));
				v.setCodiceBarre(rs.getString("codice_barre"));
				v.setTipoCodiceBarre(rs.getString("tipo_codice_barre"));		
				v.setDimensioni(rs.getString("dimensioni"));
				
				if (varianti.containsKey(cod))
					varianti.get(cod).add(v);
				else {
					List<Variante_Articolo> l = new ArrayList<Variante_Articolo>();
					l.add(v);
					varianti.put(cod,l);
				} 
				
			}
			Log.info("Mappa delle varianti caricata.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { con.rollback();
			} catch (SQLException e) { Log.info(e); e.printStackTrace();	}
		}
		 finally {
			 if (dbt==null) DataSource.closeConnections(con, st, null, rs);
		}
		return varianti;
	}
	
	
	public static List<Variante_Articolo> getVarianti(String codice_articolo){
		//Log.debug("");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Variante_Articolo> varianti = null;

		try {			
			con = DataSource.getLocalConnection();
			ps = con.prepareStatement("SELECT * FROM VARIANTI_ARTICOLO WHERE CODICE_ARTICOLO = ?");
			ps.setString(1, codice_articolo);
			rs = ps.executeQuery();
			
			varianti = new ArrayList<Variante_Articolo>();
			
			while (rs.next()){
				Variante_Articolo v = new Variante_Articolo();
				v.setCodiceArticolo(codice_articolo);
				v.setIdVariante(rs.getInt("id_variante"));
				v.setImmagine(rs.getString("immagine"));
				v.setTipo(rs.getString("tipo"));
				v.setQuantita(rs.getInt("quantita"));
				v.setValore(rs.getString("valore"));
				v.setCodiceBarre(rs.getString("codice_barre"));
				v.setTipoCodiceBarre(rs.getString("tipo_codice_barre"));
				v.setDimensioni(rs.getString("dimensioni"));
			
				varianti.add(v);
			}
			
			//Log.info("");
		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con, null,ps,rs);
		}
		return varianti;
	}
	
	
	public static Variante_Articolo getVariante(long id_variante){
		//Log.debug("");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Variante_Articolo v = null;

		try {			
			con = DataSource.getLocalConnection();
			ps = con.prepareStatement("SELECT * FROM VARIANTI_ARTICOLO WHERE ID_VARIANTE = ?");
			ps.setLong(1, id_variante);
			rs = ps.executeQuery();
			
			v = new Variante_Articolo();
			
			while (rs.next()){
				v.setCodiceArticolo(rs.getString("CODICE_ARTICOLO"));
				v.setIdVariante(rs.getInt("id_variante"));
				v.setImmagine(rs.getString("immagine"));
				v.setTipo(rs.getString("tipo"));
				v.setQuantita(rs.getInt("quantita"));
				v.setValore(rs.getString("valore"));
				v.setCodiceBarre(rs.getString("codice_barre"));
				v.setTipoCodiceBarre(rs.getString("tipo_codice_barre"));		
				v.setDimensioni(rs.getString("dimensioni"));
			}
			
			//Log.info("");
		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con, null,ps,rs);
		}
		return v;
	}
	
	
	public static void eliminaVariante(long id_variante){
		Log.info("Cancellazione variante con id: "+id_variante);
		Connection con = null;
		PreparedStatement ps = null;

		try {			
			con = DataSource.getLocalConnection();
			ps = con.prepareStatement("DELETE FROM VARIANTI_ARTICOLO WHERE ID_VARIANTE = ? ");
			ps.setLong(1, id_variante);
			ps.executeUpdate();
			
			Log.info("Variante con id "+id_variante+" eliminata.");
			
			con.commit();

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,null);
		}
	}
	
	
	public static void eliminaVarianti(String codice_articolo){
		Log.info("Eliminazione varianti dell'articolo: "+codice_articolo+" dal database locale...");
		Connection con = null;
		PreparedStatement ps = null;

		try {			
			con = DataSource.getLocalConnection();
			ps = con.prepareStatement("DELETE FROM VARIANTI_ARTICOLO WHERE CODICE_ARTICOLO = ? ");
			ps.setString(1, codice_articolo);
			ps.executeUpdate();
			
			LogArticolo l = new LogArticolo();
			l.setCodiceArticolo(codice_articolo);
			l.setAzione("Eliminazione Varianti");
			l.setNote("eliminate tutte le varianti ");
			LogArticolo_DAO.inserisciLogArticolo(l, con, ps);
			
			con.commit();
			
			Log.info("Varianti eliminate.");
			
		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,null);
		}
	}
	
	
	/* Controlla se una variante già esiste, e se esiste restituisce l'id */
	private static int controllaSeVarianteEsiste(Variante_Articolo v, Connection con, PreparedStatement ps){
		//Log.debug("");
		ResultSet rs = null;
		int id_variante = -1;

		try {			
			ps = con.prepareStatement("SELECT ID_VARIANTE FROM VARIANTI_ARTICOLO WHERE UPPER(CODICE_ARTICOLO) = ? AND UPPER(IMMAGINE) = ?");
			ps.setString(1, v.getCodiceArticolo().toLowerCase().trim());
			if (v.getImmagine()!=null)
				ps.setString(2, v.getImmagine().toLowerCase().trim());
			else ps.setNull(2, Types.NULL);
			rs = ps.executeQuery();
			
			while (rs.next()){
				id_variante = rs.getInt("ID_VARIANTE");
				
				Log.debug("Questa variante esiste, id: "+id_variante+", immagine: "+v.getImmagine());
			}
			
			
		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 DataSource.closeStatements(null,null,rs);
		}
		return id_variante;
	}
	
	

}
