package it.swb.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBCzf {
	
	private static DBCzf istanza = null;
	public Connection con = null;
	
	private DBCzf(){
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager
					.getConnection("jdbc:mysql://mysql.gloriamoraldisnc.mondoserver.com:3306/gloriamoraldisnc_db2?"
							+ "user=gloriamoraldi&password=sZ!c81Kp");
			
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

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
	
    public static synchronized DBCzf getInstance() {
        if (istanza == null) 
            istanza = new DBCzf();
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
			con = DriverManager
					.getConnection("jdbc:mysql://mysql.gloriamoraldisnc.mondoserver.com:3306/gloriamoraldisnc_db2?"
							+ "user=gloriamoraldi&password=sZ!c81Kp");
			
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

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
