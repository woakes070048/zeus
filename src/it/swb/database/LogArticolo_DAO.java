package it.swb.database;

import it.swb.log.Log;
import it.swb.model.LogArticolo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogArticolo_DAO {
	
	public static List<LogArticolo> getLogArticoli(){
		Log.info("Cerco di ottenere la lista di tutti i logs per gli articoli");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		List<LogArticolo> logs = null;

		try {			
			con = DataSource.getLocalConnection();
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM LOG_ARTICOLI ORDER BY DATA DESC");
			
			logs = new ArrayList<LogArticolo>();
			
			while (rs.next()){
				LogArticolo l = new LogArticolo();
				
				l.setIdLog(rs.getInt("id_log"));
				l.setCodiceArticolo(rs.getString("codice_articolo"));
				l.setData(rs.getTimestamp("data"));
				l.setAzione(rs.getString("azione"));
				l.setNote(rs.getString("note"));						
								
				logs.add(l);
			}
			Log.info("Ottenuti "+logs.size()+" log.");

		} catch (Exception ex) {
			Log.info(ex);
			ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,st,null,rs);
		}
		return logs;
	}
	
	/** Per ottenere tutti i log di una certa azione, ad esempio solo le vendite */
	public static List<LogArticolo> getLogArticoli(String azione){
		Log.info("Cerco di ottenere la lista di tutti i logs per gli articoli");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<LogArticolo> logs = null;

		try {			
			con = DataSource.getLocalConnection();
			ps = con.prepareStatement("SELECT * FROM LOG_ARTICOLI WHERE AZIONE=? ORDER BY DATA DESC");
			ps.setString(1, azione);
			rs = ps.executeQuery();
			
			logs = new ArrayList<LogArticolo>();
			
			while (rs.next()){
				LogArticolo l = new LogArticolo();
				
				l.setIdLog(rs.getInt("id_log"));
				l.setCodiceArticolo(rs.getString("codice_articolo"));
				l.setData(rs.getTimestamp("data"));
				l.setAzione(rs.getString("azione"));
				//l.setNote(rs.getString("note"));	
				
				String note = rs.getString("note");
				
				int x = 0;
				int y = 0;
				String ordine = "";
				int quantita = 0;
				
				if (note.contains("eBay") || note.contains("Amazon") || note.contains("Yatego")){
					//x = note.indexOf("");
					y = note.indexOf(",");
					
					ordine = note.substring(7,y);
					
					x = note.indexOf("scalate ")+8;
					y = note.indexOf(" giacenze");
					
					quantita = Integer.valueOf(note.substring(x,y));
				} else {
					x = note.indexOf("di ")+3;
					y = note.indexOf(" unit");
					
					quantita = Integer.valueOf(note.substring(x,y));
				}
				
				
				
				l.setNote(ordine);
				l.setExtra_1(quantita);
								
				logs.add(l);
			}
			Log.info("Ottenuti "+logs.size()+" report log.");

		} catch (Exception ex) {
			Log.info(ex);
			ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,rs);
		}
		return logs;
	}
	
	/** Per ottenere i log di uno specifico articolo */
	public static List<LogArticolo> getLogArticolo(String codice_articolo){
		Log.info("Cerco di ottenere la lista di log per l'articolo"+codice_articolo);
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		List<LogArticolo> logs = null;

		try {			
			con = DataSource.getLocalConnection();
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM LOG_ARTICOLI WHERE CODICE_ARTICOLO = ? ORDER BY DATA ASC");
			
			logs = new ArrayList<LogArticolo>();
			
			while (rs.next()){
				LogArticolo l = new LogArticolo();
				
				l.setIdLog(rs.getInt("id_log"));
				l.setCodiceArticolo(rs.getString("CODICE_Articolo"));
				l.setData(rs.getTimestamp("data"));
				l.setAzione(rs.getString("azione"));
				l.setNote(rs.getString("note"));						
								
				logs.add(l);
			}
			Log.info("Ottenuti "+logs.size()+" log.");

		} catch (Exception ex) {
			Log.info(ex);
			ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,st,null,rs);
		}
		return logs;
	}
	
	
	public static Map<String,List<LogArticolo>> getMappaLogArticoli(DbTool dbt){
		Log.info("Caricamento mappa log...");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		Map<String,List<LogArticolo>> map = null;

		try {			
			if (dbt==null){
				con = DataSource.getLocalConnection();
				st = con.createStatement();
			}else {
				con = dbt.getConnection();
				st = dbt.getStatement();
				rs = dbt.getResultSet();
			}
			
			rs = st.executeQuery("SELECT * FROM LOG_ARTICOLI ORDER BY CODICE_ARTICOLO ASC, DATA ASC");
			
			map = new HashMap<String,List<LogArticolo>>();
			
			while (rs.next()){
				LogArticolo l = new LogArticolo();
				
				l.setIdLog(rs.getInt("id_log"));
				l.setCodiceArticolo(rs.getString("CODICE_Articolo"));
				l.setData(rs.getTimestamp("data"));
				l.setAzione(rs.getString("azione"));
				l.setNote(rs.getString("note"));						
								
				if (map.containsKey(l.getCodiceArticolo()))
					map.get(l.getCodiceArticolo()).add(l);
				else {
					List<LogArticolo> list = new ArrayList<LogArticolo>();
					list.add(l);
					map.put(l.getCodiceArticolo(),list);
				} 
			}
			Log.info("Mappa log caricata.");
		} catch (Exception ex) {
			Log.info(ex);
			ex.printStackTrace();
		}
		 finally {
			 if (dbt==null) DataSource.closeConnections(con,st,null,rs);
		}
		return map;
	}
	
	
	public static void inserisciLogArticolo(LogArticolo l,Connection con,PreparedStatement ps){
		//Log.info("Inserimento Log per Articolo "+l.getCodiceArticolo()+" nel database locale...");
//		Connection con = null;
//		PreparedStatement ps = null;
		try {			
			//con = DataSource.getLocalConnection();
			String query = "INSERT INTO LOG_ARTICOLI(`codice_articolo`,`azione`,`data`,`note`) VALUES (?,?,?,?)"; 
			ps = con.prepareStatement(query);
			
			ps.setString(1, l.getCodiceArticolo());			
			ps.setString(2, l.getAzione());	
			if (l.getData()==null)
				l.setData(new Date());
			ps.setTimestamp(3, new Timestamp(l.getData().getTime()));	
			ps.setString(4, l.getNote());	

			ps.executeUpdate();
			
			con.commit();
			//Log.info("Inserimento riuscito.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { con.rollback();
			} catch (SQLException e) { e.printStackTrace();	}
		}
//		 finally {
//			 DataSource.closeConnections(con,null,ps,null);
//		}
	}
	
	
	public static void inserisciLogArticolo(LogArticolo l,DbTool dbt){
		//Log.info("Inserimento Log per Articolo "+l.getCodiceArticolo()+" nel database locale...");
//		Connection con = null;
		PreparedStatement ps = dbt.getPreparedStatement();
		try {			
			//con = DataSource.getLocalConnection();
			String query = "INSERT INTO LOG_ARTICOLI(`codice_articolo`,`azione`,`data`,`note`) VALUES (?,?,?,?)"; 
			ps = dbt.getConnection().prepareStatement(query);
			
			ps.setString(1, l.getCodiceArticolo());			
			ps.setString(2, l.getAzione());	
			if (l.getData()==null)
				l.setData(new Date());
			ps.setTimestamp(3, new Timestamp(l.getData().getTime()));	
			ps.setString(4, l.getNote());	

			ps.executeUpdate();
			
			dbt.commit();
			//Log.info("Inserimento riuscito.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { dbt.rollback();
			} catch (SQLException e) { e.printStackTrace();	}
		}
//		 finally {
//			 DataSource.closeConnections(con,null,ps,null);
//		}
	}
	
	
	public static void eliminaLogArticolo(String codiceArticolo){
		Log.info("eliminaLogArticolo("+codiceArticolo+")...");
		Connection con = null;
		PreparedStatement ps = null;

		try {			
			con = DataSource.getLocalConnection();
			ps = con.prepareStatement("DELETE FROM FORNITORI WHERE CODICE_ARTICOLO = ? ");
			ps.setString(1, codiceArticolo);
			ps.executeUpdate();
			
			con.commit();
			
			Log.info("Log per articolo: "+codiceArticolo+" eliminati.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,null);
		}
	}
	
	
	


}
