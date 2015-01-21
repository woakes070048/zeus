package it.swb.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.swb.log.Log;

public class Mcd_DAO {
	
	public static int aggiungiAMcd(String codiceArticolo, String piattaforma){
		Connection con = null;
		PreparedStatement ps = null;
		
		int res = 0;
		
		try {
			
			String query = "INSERT INTO mcd(`piattaforma`,`codice_articolo`,`data`,`elaborato`) VALUES (?,?,?,0) "+
					" ON DUPLICATE KEY UPDATE `data`=?";
			
			con = DataSource.getLocalConnection();

			ps = con.prepareStatement(query);
			ps.setString(1, piattaforma);			
			ps.setString(2, codiceArticolo);
			ps.setTimestamp(3, new Timestamp(new Date().getTime()));	
			ps.setTimestamp(4, new Timestamp(new Date().getTime()));	
			
			res = ps.executeUpdate();
			
			con.commit();
			
		}
		catch (Exception ex){
			Log.info(ex);
			ex.printStackTrace();
			try { 
				con.rollback();
			} catch (SQLException e) { 
				Log.info(ex); 
				e.printStackTrace();	
			}
		} finally {
			DataSource.closeConnections(con, null, ps, null);
		}
		return res;
	}
	
	public static int segnaComeElaborati(String piattaforma){
		Connection con = null;
		PreparedStatement ps = null;
		
		int res = 0;
		
		try {
			
			String query = "UPDATE mcd SET `elaborato`=1 "+
					" WHERE `piattaforma`=?";
			
			con = DataSource.getLocalConnection();

			ps = con.prepareStatement(query);
			ps.setString(1, piattaforma);			
			
			res = ps.executeUpdate();
			
			con.commit();
		}
		catch (Exception ex){
			Log.info(ex);
			ex.printStackTrace();
			try { 
				con.rollback();
			} catch (SQLException e) { 
				Log.info(ex); 
				e.printStackTrace();	
			}
		} finally {
			DataSource.closeConnections(con, null, ps, null);
		}
		return res;
	}
	
	public static List<String> getMcd(String piattaforma){
		Log.info("Cerco di ottenere la lista di tutti gli articoli da caricare per la piattaforma "+piattaforma);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> mcd = null;

		try {			
			con = DataSource.getLocalConnection();
			ps = con.prepareStatement("SELECT * FROM mcd WHERE piattaforma=? ORDER BY DATA DESC");
			ps.setString(1, piattaforma);
			rs = ps.executeQuery();
			
			mcd = new ArrayList<String>();
			
			while (rs.next()){
								
				mcd.add(rs.getString("codice_articolo"));
			}
			Log.info("Ottenuti "+mcd.size()+" articoli da caricare.");

		} catch (Exception ex) {
			Log.info(ex);
			ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,rs);
		}
		return mcd;
	}
	
	public static int getNumeroArticoliInAttesa(String piattaforma){
		Log.info("Cerco di ottenere il numero di articoli da caricare per la piattaforma "+piattaforma);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int num = 0;
		
		try {			
			con = DataSource.getLocalConnection();
			ps = con.prepareStatement("SELECT COUNT(*) as num FROM mcd WHERE piattaforma=? ");
			ps.setString(1, piattaforma);
			rs = ps.executeQuery();
			
			while (rs.next()) num = rs.getInt("num");
			
			Log.info("Ottenuto: "+num);

		} catch (Exception ex) {
			Log.info(ex);
			ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,rs);
		}
		return num;
	}


}
