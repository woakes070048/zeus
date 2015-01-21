package it.swb.database;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import it.swb.log.Log;


public class DataSource {
  
    
	public static Connection getLocalConnection() {
		Connection con = null;
		try {		
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Properties config = new Properties();	        
			config.load(DataSource.class.getResourceAsStream("/database.properties"));
			
			String server = config.getProperty("Local_DB_Server");
	        String port = config.getProperty("Local_DB_Port");
	        String database = config.getProperty("Local_DB_Name");
	        String user = config.getProperty("Local_DB_User");
	        String password = config.getProperty("Local_DB_Password");
	        
	        con= DriverManager.getConnection("jdbc:mysql://"+server+":"+port+"/"+database+"?user="+user+"&password="+password);
			
	        con.setAutoCommit(false);
	        con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			

		}catch(IOException e) {
            Log.info(e); e.printStackTrace();
		} catch (SQLException e) {
			Log.info(e); e.printStackTrace();
		}  catch (InstantiationException e) {
			Log.info(e); e.printStackTrace();
		} catch (IllegalAccessException e) {
			Log.info(e); e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Log.info(e); e.printStackTrace();
		}
		return con;
	}
	
	public static Connection getTestConnection() {
		Connection con = null;
		try {		
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Properties config = new Properties();	        
			config.load(DataSource.class.getResourceAsStream("/database.properties"));
			
			String server = config.getProperty("Test_DB_Server");
	        String port = config.getProperty("Test_DB_Port");
	        String database = config.getProperty("Test_DB_Name");
	        String user = config.getProperty("Test_DB_User");
	        String password = config.getProperty("Test_DB_Password");
	        
	        con= DriverManager.getConnection("jdbc:mysql://"+server+":"+port+"/"+database+"?user="+user+"&password="+password);
			
	        con.setAutoCommit(false);
	        con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			

		}catch(IOException e) {
            Log.info(e); e.printStackTrace();
		} catch (SQLException e) {
			Log.info(e); e.printStackTrace();
		}  catch (InstantiationException e) {
			Log.info(e); e.printStackTrace();
		} catch (IllegalAccessException e) {
			Log.info(e); e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Log.info(e); e.printStackTrace();
		}
		return con;
	}
	
	
	
	public static Connection getRemoteConnection() {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Properties config = new Properties();	        
			config.load(DataSource.class.getResourceAsStream("/database.properties"));
			
			String server = config.getProperty("Remote_GM_DB_Server");
	        String port = config.getProperty("Remote_GM_DB_Port");
	        String database = config.getProperty("Remote_GM_DB_Name");
	        String user = config.getProperty("Remote_GM_DB_User");
	        String password = config.getProperty("Remote_GM_DB_Password");
	        
	        con= DriverManager.getConnection("jdbc:mysql://"+server+":"+port+"/"+database+"?user="+user+"&password="+password);
			
	        con.setAutoCommit(false);
	        con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			

		}catch(IOException e) {
            Log.info(e); e.printStackTrace();
		} catch (SQLException e) {
			Log.info(e); e.printStackTrace();
		}  catch (InstantiationException e) {
			Log.info(e); e.printStackTrace();
		} catch (IllegalAccessException e) {
			Log.info(e); e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Log.info(e); e.printStackTrace();
		}
		return con;
	}
	
	public static Connection getZBConnection() {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Properties config = new Properties();	        
			config.load(DataSource.class.getResourceAsStream("/database.properties"));
			
			String server = config.getProperty("ZB_DB_Server");
	        String port = config.getProperty("ZB_DB_Port");
	        String database = config.getProperty("ZB_DB_Name");
	        String user = config.getProperty("ZB_DB_User");
	        String password = config.getProperty("ZB_DB_Password");
	        
	        con= DriverManager.getConnection("jdbc:mysql://"+server+":"+port+"/"+database+"?user="+user+"&password="+password);
			
	        con.setAutoCommit(false);
	        con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			

		}catch(IOException e) {
            Log.info(e); e.printStackTrace();
		} catch (SQLException e) {
			Log.info(e); e.printStackTrace();
		}  catch (InstantiationException e) {
			Log.info(e); e.printStackTrace();
		} catch (IllegalAccessException e) {
			Log.info(e); e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Log.info(e); e.printStackTrace();
		}
		return con;
	}
	
	public static Connection getGMConnection() {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Properties config = new Properties();	        
			config.load(DataSource.class.getResourceAsStream("/database.properties"));
			
			String server = config.getProperty("GM_DB_Server");
	        String port = config.getProperty("GM_DB_Port");
	        String database = config.getProperty("GM_DB_Name");
	        String user = config.getProperty("GM_DB_User");
	        String password = config.getProperty("GM_DB_Password");
	        
	        con= DriverManager.getConnection("jdbc:mysql://"+server+":"+port+"/"+database+"?user="+user+"&password="+password);
			
	        con.setAutoCommit(false);
	        con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			

		}catch(IOException e) {
            Log.info(e); e.printStackTrace();
		} catch (SQLException e) {
			Log.info(e); e.printStackTrace();
		}  catch (InstantiationException e) {
			Log.info(e); e.printStackTrace();
		} catch (IllegalAccessException e) {
			Log.info(e); e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Log.info(e); e.printStackTrace();
		}
		return con;
	}
	   
   
	public static void closeConnections(Connection con, Statement st, PreparedStatement ps, ResultSet rs){		
		try {
				if (con != null) con.close();
			} catch (SQLException e) {
				Log.info(e); e.printStackTrace();
			}
		
		try
	    { if (st != null) st.close();}
		catch (Exception e)
		    { Log.info(e); e.printStackTrace();}
	    
		try
	    { if (rs != null) rs.close();}
		catch (Exception e)
		    { Log.info(e); e.printStackTrace();}
	    
		try
	    { if (ps != null) ps.close();}
		catch (Exception e)
		    { Log.info(e); e.printStackTrace();}
	 }
    
    
    
	public static void closeStatements(Statement st, PreparedStatement ps, ResultSet rs){
		try
	    { if (st != null) st.close();}
		catch (Exception e)
		    { Log.info(e); e.printStackTrace();}
	    
		try
	    { if (rs != null) rs.close();}
		catch (Exception e)
		    { Log.info(e); e.printStackTrace();}
	    
		try
	    { if (ps != null) ps.close();}
		catch (Exception e)
		    { Log.info(e); e.printStackTrace();}
	 }
	

	
	public static void closeConnection(Connection con){
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				Log.info(e);
				e.printStackTrace();
			}
		}
	}
}
