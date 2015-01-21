package it.swb.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBClocalgm {
	
	private static DBClocalgm istanza = null;
	public Connection con = null;
	
	private DBClocalgm(){
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Properties config = new Properties();	        
			config.load(DBClocal.class.getResourceAsStream("/database.properties"));
			
	        String server = config.getProperty("Local_GM_DB_Server");
	        String port = config.getProperty("Local_GM_DB_Port");
	        String database = config.getProperty("Local_GM_DB_Name");
	        String user = config.getProperty("Local_GM_DB_User");
	        String password = config.getProperty("Local_GM_DB_Password");
			
			con = DriverManager
					.getConnection("jdbc:mysql://"+server+":"+port+"/"+database+"?user="+user+"&password="+password);
			
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

		}catch(IOException e) {
            e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
    public static synchronized DBClocalgm getInstance() {
        if (istanza == null) 
            istanza = new DBClocalgm();
        return istanza;
    }
    
    public Connection getConnection(){
    	if (con==null) 
    		con = createConnection();
   		return con;
    }
    
	public void closeConnection(){
		if (con != null) {
			try {
				con.close();
			} catch (SQLException sqlEx) {
				sqlEx.printStackTrace();
			}
		}
	}

	public Connection createConnection() {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Properties config = new Properties();	        
			config.load(DBClocal.class.getResourceAsStream("/database.properties"));
			
	        String server = config.getProperty("Local_GM_DB_Server");
	        String port = config.getProperty("Local_GM_DB_Port");
	        String database = config.getProperty("Local_GM_DB_Name");
	        String user = config.getProperty("Local_GM_DB_User");
	        String password = config.getProperty("Local_GM_DB_Password");
			
			con = DriverManager
					.getConnection("jdbc:mysql://"+server+":"+port+"/"+database+"?user="+user+"&password="+password);
			
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

		}catch(IOException e) {
            e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	
	public void closeConnections(Connection con, Statement st, PreparedStatement ps, ResultSet rs){
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException sqlEx) {
				sqlEx.printStackTrace();
			}
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException sqlEx) {
				sqlEx.printStackTrace();
			}
		}
		if (st != null) {
			try {
				st.close();
			} catch (SQLException sqlEx) {
				sqlEx.printStackTrace();
			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (SQLException sqlEx) {
				sqlEx.printStackTrace();
			}
		}
	}
	
}
