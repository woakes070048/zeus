package it.swb.database;

import it.swb.utility.Methods;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


public class TestDB {
	
	public static void main(String[] args) {
		
//		Connection con = DBCgm.getInstance().getConnection();
		
//		Class.forName("com.mysql.jdbc.Driver").newInstance();	
		
//      String server = "mysql11.000webhost.com";
//      String port = "3306";
//      String database = "a9931333_gloriam";
//      String user = "a9931333_gloriam";
//      String password = "moraldigl0ria";
		
//		Connection con  = DriverManager	.getConnection("jdbc:mysql://"+server+"/"+database+"?user="+user+"&password="+password);
//		
//		try {
////			System.out.println("START");
////			Connection con = DataSourceOLD.getInstance().getLocalConnection();
////			
////			System.out.println(con.getAutoCommit());
////		
////			test_query2(con);
////			System.out.println("END");		
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}

	
	
}
		
	
	
	@SuppressWarnings("unused")
	private static void ciao(){
		
		try{
			Connection con = DBCtest.getInstance().getConnection();
			String query = "select c.*,f.email, r.title from cp as c inner join fpxqguvh_users as f on c.user_id = f.id left join fpxqguvh_content as r on c.user_id = r.created_by";
			
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			File f = new File("D:\\utenti.txt");
			f.createNewFile();
			FileOutputStream fos = new FileOutputStream ("D:\\utenti.txt", true);
			
			PrintWriter pw = new PrintWriter (fos);
			
			pw.println("Nome	Cognome	Email	Titolo racconto	Località	Comune	Provincia	Indirizzo	Codice Postale	Paese	Telefono	Codice Fiscale	Istituto	Nome docente");
			pw.println();
			
			while (rs.next()){
				//System.out.println(rs.getString("firstname"));
				pw.print(Methods.primeLettereMaiuscole(rs.getString("firstname")));
				pw.print("	");
				pw.print(Methods.primeLettereMaiuscole(rs.getString("lastname")));
				pw.print("	");
				pw.print(Methods.primeLettereMaiuscole(rs.getString("email")));
				pw.print("	");
				pw.print(Methods.primeLettereMaiuscole(rs.getString("title")));
				pw.print("	");
				pw.print(Methods.primeLettereMaiuscole(rs.getString("location")));
				pw.print("	");
				pw.print(Methods.primeLettereMaiuscole(rs.getString("city")));
				pw.print("	");
				String provincia = rs.getString("state");
				if (provincia!=null)
					provincia = provincia.toUpperCase();
				pw.print(provincia);
				pw.print("	");
				pw.print(Methods.primeLettereMaiuscole(rs.getString("address")));
				pw.print("	");
				pw.print(rs.getString("zipcode"));
				pw.print("	");
				pw.print(Methods.primeLettereMaiuscole(rs.getString("country")));
				pw.print("	");
				pw.print(rs.getString("phone"));
				pw.print("	");
				String cb_codicefiscale = rs.getString("cb_codicefiscale");
				if (cb_codicefiscale!=null)
					cb_codicefiscale = cb_codicefiscale.toUpperCase();
				pw.print(cb_codicefiscale);
				pw.print("	");
				pw.print(Methods.primeLettereMaiuscole(rs.getString("cb_scuolauniv")));
				pw.print("	");
				pw.print(Methods.primeLettereMaiuscole(rs.getString("cb_nomedocente")));
				pw.print("	");
				pw.println();
			}
			
			pw.close();
			
			DBCtest.getInstance().closeConnections(con, null, ps, rs);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	
	@SuppressWarnings("unused")
	private static void test_query(Connection con) {
		
		try{
			
			Statement st = con.createStatement();
			
			System.out.println(st.execute("select 1 from dual"));
			
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	
	@SuppressWarnings("unused")
	private static void test_query2(Connection con) {
		
		try{
			
			Statement st = con.createStatement();
			
			ResultSet rs = st.executeQuery("SELECT `name_it-IT` FROM `pmqbpiom_jshopping_products` where `product_id`> 500");
			
			while (rs.next()){
				System.out.println(rs.getString("name_it-IT"));
			}
			
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	
}
