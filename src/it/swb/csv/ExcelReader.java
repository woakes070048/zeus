package it.swb.csv;

import java.sql.*;
import java.util.*;

public class ExcelReader {
	static String foglio = "mcd_yatego_2013-07.csv";
	static String filename = "D:\\zeus\\mcd\\mcd_yatego_2013-07.csv";

	@SuppressWarnings("rawtypes")
	static Vector vettore = new Vector();

	public static void main(String[] args) {
		try {
			ExcelReader e = new ExcelReader(filename);
			vettore = e.leggidati(foglio);
			System.out.println(vettore);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("static-access")
	public ExcelReader(String filename) throws ClassNotFoundException {
		this.filename = filename;
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
	}


	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	public Vector leggidati(String foglio) throws SQLException {
		String query = "select * from [" + foglio.trim() + "$]";
		String database = "jdbc:odbc:Driver={Microsoft Excel Driver (*.csv)};DBQ="
				+ filename.trim() + ";DriverID=22;READONLY=true}";
		Connection con = java.sql.DriverManager.getConnection(database, "", "");
		Statement state = con.createStatement();
		ResultSet rs = state.executeQuery(query);
		int maxWidth = 0;
		Vector rows = new Vector(); // righe
		Vector columnHeader = new Vector(); // colonne
		ResultSetMetaData rsmd = rs.getMetaData();
		int cols = rsmd.getColumnCount(); // numero colonne
		for (int i = 1; i <= cols; i++)
			columnHeader.add(rsmd.getColumnName(i)); // prende il nome delle
														// colonne
		rows.add(columnHeader); // e li aggiunge al vettore
		while (rs.next()) {
			Vector thisRow = new Vector(); // vettore temporaneo per i dati
			for (int i = 1; i <= cols; i++)
				thisRow.add(rs.getString(i)); // aggiunge i dati al vettore
			rows.add(thisRow); // aggiunge il vettore temporaneo al vettore
								// sopra
		}
		rs.close();
		state.close();
		con.close();
		return rows;
	}
}