package it.swb.piattaforme.zelda;

import it.swb.database.DataSource;
import it.swb.log.Log;
import it.swb.model.Articolo;
import it.swb.model.Variante_Articolo;
import it.swb.utility.Methods;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ZeldaBomboniereIT_DAO_backup {
	
	public static int insertIntoProduct(Articolo a){
		Log.debug("Inserimento articolo su zeldabomboniere.it ...");
		Connection con = null;
		PreparedStatement ps = null;
		int risultato = 0;
		
		try {			
			con = DataSource.getZBConnection();
									
			if (a.getIdArticolo()!=0 && !checkIfProductExist(a.getIdArticolo(),con)){
			
				String query = "INSERT INTO `product` (`product_id`, `model`, `sku`, `ean`, `quantity`," +		/* 5 */
								" `stock_status_id`, `image`, `manufacturer_id`, `shipping`, `price`," +		/* 10 */
								" `points`, `tax_class_id`, `date_available`, `weight`, `weight_class_id`, " +	/* 15 */
								"`length`, `width`, `height`, `length_class_id`, `subtract`, " +				/* 20 */
								"`minimum`, `sort_order`, `status`, `date_added`, `date_modified`, " +			/* 25 */
								"`viewed`,`upc`,`jan`,`isbn`,`mpn`,`location`) " +								/* 31 */
								"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";	/* sono 26 */
			
				ps = con.prepareStatement(query);
				
				java.sql.Date data = new java.sql.Date(new java.util.Date().getTime());
				
				ps.setLong(1, a.getIdArticolo());						/* product_id */
				ps.setString(2, a.getCodice());							/* model */
				ps.setString(3, a.getCodice());							/* sku */
				if (a.getCodiceBarre()==null) ps.setString(4, "");		/* ean */
				else ps.setString(4, a.getCodiceBarre());		
				ps.setLong(5, a.getQuantitaMagazzino());				/* quantity */
				
				ps.setInt(6, 6);										/* stock_status_id */
				ps.setString(7, "articoli/"+a.getImmagine1());		/* image */
				ps.setInt(8, 0);										/* manufacturer_id */
				ps.setInt(9, 1);										/* shipping */
				ps.setDouble(10, a.getPrezzoDettaglio());				/* price */
				
				ps.setInt(11,0);										/* points */
				ps.setInt(12,11);										/* tax_class_id */
				ps.setDate(13, data);									/* date_available */
				ps.setDouble(14, 0);									/* weight */
				ps.setInt(15,1);										/* weight_clas_id */
				
				
				ps.setDouble(16, 0);									/* length */
				ps.setDouble(17, 0);									/* width */
				ps.setDouble(18, 0);									/* height */
				ps.setInt(19, 1);										/* length_class_id */
				ps.setInt(20, 1);										/* subtract */
				
				ps.setInt(21, 1);										/* minimum */
				ps.setInt(22, 0);										/* sort_order */
				ps.setInt(23, 1);										/* status */
				ps.setDate(24, data);													/* date_added */
				ps.setDate(25, data);									/* date_modified */
				ps.setInt(26,1);										/* viewed */
				ps.setString(27, "");
				ps.setString(28, "");
				ps.setString(29, "");
				ps.setString(30, "");
				ps.setString(31, "");

				risultato = ps.executeUpdate();			
				
				insertIntoProductDescription(a, con);
				insertIntoProductImage(a, con);
				insertIntoProductToCategory(a, con);
				insertIntoProductToStore(a, con);
				
				if (a.getVarianti()!=null && !a.getVarianti().isEmpty()){
					insertIntoProductOption(a,con);
				}
				
				con.commit();						
				
				Log.debug("Inserimento riuscito.");					
			}
			else { Log.debug("Non inserito, ID articolo già esistente: "+a.getIdArticolo());	}

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { 
				con.rollback();
			} catch (SQLException e) { Log.info(e); e.printStackTrace();	}
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,null);
		}
		return risultato;
	}
	
	
	
	public static void insertIntoProductDescription(Articolo a,Connection con){
		PreparedStatement ps = null;
		
		try {					
			String query = "INSERT INTO `product_description` (`product_id`, `language_id`, `name`, `description`, `meta_description`, `meta_keyword`, `tag`) " +																	/* 26 */
							"VALUES (?,?,?,?,?,?,?)";	/* sono 7 */
		
			ps = con.prepareStatement(query);
			
			ps.setLong(1, a.getIdArticolo());		/* product_id */
			ps.setInt(2, 2);						/* language_id */
			ps.setString(3, Methods.primeLettereMaiuscole(a.getNome()));			/* name */	
			
			String descrizione = "<p>"+Methods.MaiuscolaDopoPunto(a.getDescrizione())+"<br/><br/><b>Quantit&agrave; inserzione:</b> "
								+Methods.MaiuscolaDopoPunto(a.getQuantitaInserzione())+"<br/><br/><b>Dimensioni:</b> "
								+Methods.MaiuscolaDopoPunto(a.getDimensioni())+"</p>";
			ps.setString(4, descrizione);			/* description */
			ps.setString(5, "");					/* meta_description */
			ps.setString(6, "");					/* meta_keyword */
			ps.setString(7, "");					/* tag */

			ps.executeUpdate();		
			
			
			ps.setInt(2, 1);						/* language_id */
					
			ps.executeUpdate();
						
			/* il commit viene fatto alla fine del metodo principale */
			//con.commit();						
			
		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { 
				con.rollback();
			} catch (SQLException e) { Log.info(e); e.printStackTrace();	}
		}
		 finally {
			 DataSource.closeStatements(null, ps, null);
		}
	}
	
	public static void insertIntoProductImage(long idArticolo, String immagine, int ordinamento, Connection con){
		PreparedStatement ps = null;
		
		try {			
			if (immagine!=null && !immagine.trim().isEmpty()){
				if (ordinamento==-1){
					String query = "select max(sort_order) as max from product_image where product_id=?";
					
					ps = con.prepareStatement(query);
					ps.setLong(1, idArticolo);		
					
					ResultSet rs = ps.executeQuery();
					
					if (rs.next()) ordinamento = rs.getInt("max");
				}
				
				//funzione che controlla se esiste già la stessa immagine associata allo stesso product id
				
				String query = "SELECT * FROM `product_image` WHERE `product_id`= ? AND `image` = ?";
				
				ps = con.prepareStatement(query);
				
				ps.setLong(1, idArticolo);		
				ps.setString(2, "articoli/"+immagine);		
				
				ResultSet rs = ps.executeQuery();
				
				//se l'immagine non è presente la inserisce
				if (!rs.next()){
					query = "INSERT INTO `product_image` (`product_id`, `image`, `sort_order`) VALUES (?,?,?)";
					
					ps = con.prepareStatement(query);
				
				
					ps.setLong(1, idArticolo);		
					ps.setString(2, "articoli/"+immagine);		
					ps.setInt(3, ordinamento);	
					
					ps.executeUpdate();	
				}
				
			}
			/* il commit viene fatto alla fine del metodo principale */
			//con.commit();							
			
		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { 
				con.rollback();
			} catch (SQLException e) { Log.info(e); e.printStackTrace();	}
		}
		 finally {
			 DataSource.closeStatements(null, ps, null);
		}
	}
	
	
	public static void insertIntoProductImage(Articolo a,Connection con){
		
		try {					
			//la prima immagine è già presente nella descrizione del prodotto
			//insertIntoProductImage(a.getIdArticolo(),a.getImmagine1(),1,con);
			insertIntoProductImage(a.getIdArticolo(),a.getImmagine2(),2,con);
			insertIntoProductImage(a.getIdArticolo(),a.getImmagine3(),3,con);
			insertIntoProductImage(a.getIdArticolo(),a.getImmagine4(),4,con);
			insertIntoProductImage(a.getIdArticolo(),a.getImmagine5(),5,con);
			
			/* il commit viene fatto alla fine del metodo principale */
			//con.commit();							
			
		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { 
				con.rollback();
			} catch (SQLException e) { Log.info(e); e.printStackTrace();	}
		}
	}
	
	public static void insertIntoProductToCategory(Articolo a,Connection con){
		PreparedStatement ps = null;
		
		try {			
			String query = "INSERT INTO `product_to_category` (`product_id`, `category_id`) VALUES (?,?)";	
		
			ps = con.prepareStatement(query);
			
			ps.setLong(1, a.getIdArticolo());	
			ps.setLong(2, a.getIdCategoria());	
			
			ps.executeUpdate();		
			
			if (a.getIdCategoria2()>0){
				ps.setLong(1, a.getIdArticolo());	
				ps.setLong(2, a.getIdCategoria2());	
				
				ps.executeUpdate();		
			}
			
			/* il commit viene fatto alla fine del metodo principale */
			//con.commit();							
			
		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { 
				con.rollback();
			} catch (SQLException e) { Log.info(e); e.printStackTrace();	}
		}
		 finally {
			 DataSource.closeStatements(null, ps, null);
		}
	}
	
	public static void insertIntoProductToStore(Articolo a,Connection con){
		PreparedStatement ps = null;
		
		try {			
			String query = "INSERT INTO `product_to_store` (`product_id`, `store_id`) VALUES (?,?)";	
		
			ps = con.prepareStatement(query);
			
			ps.setLong(1, a.getIdArticolo());	
			ps.setInt(2, 0);	
			
			ps.executeUpdate();				
			
			/* il commit viene fatto alla fine del metodo principale */
			//con.commit();							
			
		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { 
				con.rollback();
			} catch (SQLException e) { Log.info(e); e.printStackTrace();	}
		}
		 finally {
			 DataSource.closeStatements(null, ps, null);
		}
	}
	
//	public static void insertIntoProductOption(Articolo a,Connection con){
//		PreparedStatement ps = null;
//		
//		try {			
//			String query = "INSERT INTO `product_option` (`product_id`, `option_id`, `option_value`,`required`) VALUES (?,?,?,?)";	
//		
//			ps = con.prepareStatement(query);
//			
//			int option_id=16;
//			
//			Variante_Articolo v = a.getVarianti().get(1);
//			if (v.getTipo().equals("Colore")) option_id=13;
//			else if (v.getTipo().equals("Gusto")) option_id=15;
//			else if (v.getTipo().equals("Tema")) option_id=14;
//			else if (v.getTipo().equals("Misura")) option_id=17;
//		
//			ps.setLong(1, a.getIdArticolo());	
//			ps.setLong(2, option_id);	
//			ps.setString(3, "");	
//			ps.setInt(4, 1);	
//			
//			ps.executeUpdate();		
//			
//			/* il commit viene fatto alla fine del metodo principale */
//			//con.commit();							
//			
//		} catch (Exception ex) {
//			Log.info(ex); ex.printStackTrace();
//			try { 
//				con.rollback();
//			} catch (SQLException e) { Log.info(e); e.printStackTrace();	}
//		}
//		 finally {
//			 DataSource.closeStatements(null, ps, null);
//		}
//	}
	
	public static void insertIntoProductOption(Articolo a,Connection con){
		PreparedStatement ps = null;
		
		try {			
			String query = "INSERT INTO `product_option` (`product_id`, `option_id`, `option_value`,`required`) VALUES (?,?,?,?)";	
			
			ps = con.prepareStatement(query);
			
			int option_id=16;
			
			Variante_Articolo v1 = a.getVarianti().get(0);
			if (v1.getTipo().equals("Colore")) option_id=13;
			else if (v1.getTipo().equals("Gusto")) option_id=15;
			else if (v1.getTipo().equals("Tema")) option_id=14;
			else if (v1.getTipo().equals("Misura")) option_id=17;
		
			ps.setLong(1, a.getIdArticolo());	
			ps.setLong(2, option_id);	
			ps.setString(3, "");	
			ps.setInt(4, 1);	
			
			ps.executeUpdate();		
			
			for (Variante_Articolo v : a.getVarianti()){
			
				query = "INSERT INTO `option_value` (`option_id`, `image`,`sort_order`) VALUES (?,?,?)";
				
				ps = con.prepareStatement(query);
				
				ps.setInt(1, option_id);	
				ps.setString(2, "no_image.jpg");	
				ps.setInt(3, 0);	
				
				ps.executeUpdate();	
				
				query = "INSERT INTO `option_value_description` (`option_value_id`,`language_id` ,`option_id`, `name`) VALUES (LAST_INSERT_ID(),?,?,?)";
				
				ps = con.prepareStatement(query);
				
				ps.setInt(1, 2);	
				ps.setInt(2, option_id);	
				ps.setString(3, v.getValore());	
				
				ps.executeUpdate();		
				
				query = "INSERT INTO `product_option_value` (`product_option_id`,`product_id` ,`option_id`, `option_value_id`," +
															"`quantity`,`subtract`,`price`,`price_prefix`,`points`,`points_prefix`,`weight`,`weight_prefix`)" +
															" VALUES ((select product_option_id from product_option where product_id=?),?,?,LAST_INSERT_ID(),?,?,?,?,?,?,?,?)";
			
				ps = con.prepareStatement(query);
				
				ps.setLong(1, a.getIdArticolo());		//product_option_id ottenuto tramite product_id
				ps.setLong(2, a.getIdArticolo());		//product_id
				ps.setInt(3, option_id);		//option_id
				//auto-generato 				option_value_id
				ps.setInt(4, v.getQuantita());	//quantity
				ps.setInt(5, 1);				//subtract
				ps.setDouble(6, 0);				//price
				ps.setString(7, "+");				//price_prefix
				ps.setInt(8, 0);				//points
				ps.setString(9, "+");				//points_prefix
				ps.setDouble(10, 0);				//weight
				ps.setString(11, "+	");			//weight_prefix
				
				ps.executeUpdate();		
				
				/* il commit viene fatto alla fine del metodo principale */
				//con.commit();			
				
				insertIntoProductImage(a.getIdArticolo(),v.getImmagine(),-1,con);
			}
			
		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { 
				con.rollback();
			} catch (SQLException e) { Log.info(e); e.printStackTrace();	}
		}
		 finally {
			 DataSource.closeStatements(null, ps, null);
		}
	}
	
	public static void insertIntoOptionValue(Articolo a,Connection con){
		PreparedStatement ps = null;
		
		try {			
			String query = "INSERT INTO `product_option` (`product_id`, `option_id`, `option_value`,`required`) VALUES (?,?,?,?)";	
		
			ps = con.prepareStatement(query);
			
			int option_id=16;
			
			Variante_Articolo v = a.getVarianti().get(1);
			if (v.getTipo().equals("Colore")) option_id=13;
			else if (v.getTipo().equals("Gusto")) option_id=15;
			else if (v.getTipo().equals("Tema")) option_id=14;
			else if (v.getTipo().equals("Misura")) option_id=17;
		
			ps.setLong(1, a.getIdArticolo());	
			ps.setLong(2, option_id);	
			ps.setString(3, "");	
			ps.setInt(4, 1);	
			
			ps.executeUpdate();		
			
			/* il commit viene fatto alla fine del metodo principale */
			//con.commit();							
			
		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { 
				con.rollback();
			} catch (SQLException e) { Log.info(e); e.printStackTrace();	}
		}
		 finally {
			 DataSource.closeStatements(null, ps, null);
		}
	}
	
	public static void insertIntoOptionValueDescription(Articolo a,Connection con){
		PreparedStatement ps = null;
		
		try {			
			String query = "INSERT INTO `product_option` (`product_id`, `option_id`, `option_value`,`required`) VALUES (?,?,?,?)";	
		
			ps = con.prepareStatement(query);
			
			int option_id=16;
			
			Variante_Articolo v = a.getVarianti().get(1);
			if (v.getTipo().equals("Colore")) option_id=13;
			else if (v.getTipo().equals("Gusto")) option_id=15;
			else if (v.getTipo().equals("Tema")) option_id=14;
			else if (v.getTipo().equals("Misura")) option_id=17;
		
			ps.setLong(1, a.getIdArticolo());	
			ps.setLong(2, option_id);	
			ps.setString(3, "");	
			ps.setInt(4, 1);	
			
			ps.executeUpdate();		
			
			/* il commit viene fatto alla fine del metodo principale */
			//con.commit();							
			
		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { 
				con.rollback();
			} catch (SQLException e) { Log.info(e); e.printStackTrace();	}
		}
		 finally {
			 DataSource.closeStatements(null, ps, null);
		}
	}
	
	public static int deleteProduct(Articolo a){	
		PreparedStatement ps = null;
		Connection con = null;
		int res = 0;
		
		try {		
			con = DataSource.getZBConnection();
			
			String query = "delete from `product` where `product_id` = ? ";
			
			ps = con.prepareStatement(query);
			ps.setLong(1, a.getIdArticolo());
			
			ps.executeUpdate();
			
			query = "delete from `product_description` where `product_id` = ? ";
			
			ps = con.prepareStatement(query);
			ps.setLong(1, a.getIdArticolo());
			
			ps.executeUpdate();
			
			query = "delete from `product_image` where `product_id` = ? ";
			
			ps = con.prepareStatement(query);
			ps.setLong(1, a.getIdArticolo());
			
			ps.executeUpdate();
			
			query = "delete from `product_to_category` where `product_id` = ? ";
			
			ps = con.prepareStatement(query);
			ps.setLong(1, a.getIdArticolo());
			
			ps.executeUpdate();
			
			query = "delete from `product_to_store` where `product_id` = ? ";
			
			ps = con.prepareStatement(query);
			ps.setLong(1, a.getIdArticolo());
			
			ps.executeUpdate();
			
			con.commit();
			
			res=1;							
		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();	
			try { 
				con.rollback();
			} catch (SQLException e) { Log.info(e); e.printStackTrace();	}
		}
		finally {
			DataSource.closeConnections(con, null, ps, null);
		}		
		return res;
	}
	
	private static boolean checkIfProductExist(long id_prodotto, Connection con){	
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean exist = false;
		
		try {		
			ps = con.prepareStatement("select * from `product` where `product_id` = ? ");
			ps.setLong(1, id_prodotto);
			
			rs = ps.executeQuery();
			
			while (rs.next()){
				exist = true;
			}			
		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();	}
		finally {
			DataSource.closeStatements(null,ps,rs);
		}		
		return exist;
	}
}
