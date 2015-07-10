package it.swb.piattaforme.zelda;

import it.swb.business.CategorieBusiness;
import it.swb.database.Articolo_DAO;
import it.swb.database.DataSource;
import it.swb.java.Email;
import it.swb.log.Log;
import it.swb.model.Articolo;
import it.swb.model.Variante_Articolo;
import it.swb.utility.Methods;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ZB_IT_DAO {
	
	public static int insertIntoProduct(Articolo a){
		Log.debug("Inserimento articolo su zeldabomboniere.it ...");
		Connection con = null;
		PreparedStatement ps = null;
		int risultato = 0;
		
		try {			
			con = DataSource.getZBConnection();
									
			if (a.getIdArticolo()!=0 /* && !checkIfProductExist(a.getIdArticolo(),con,ps) */){
			
				String query = "INSERT INTO `product` (`product_id`, `model`, `sku`, `ean`, `quantity`," +		/* 5 */
								" `stock_status_id`, `image`, `manufacturer_id`, `shipping`, `price`," +		/* 10 */
								" `points`, `tax_class_id`, `date_available`, `weight`, `weight_class_id`, " +	/* 15 */
								"`length`, `width`, `height`, `length_class_id`, `subtract`, " +				/* 20 */
								"`minimum`, `sort_order`, `status`, `date_added`, `date_modified`, " +			/* 25 */
								"`viewed`,`upc`,`jan`,`isbn`,`mpn`,`location`) " +								/* 31 */
								"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) " +		/* sono 31 */
								"ON DUPLICATE KEY UPDATE " +
								"model=?,sku=?,ean=?,quantity=?,image=?,price=?,tax_class_id=?,date_modified=? ";	/* altri 8 */
			
				ps = con.prepareStatement(query);
				
				java.sql.Date data = new java.sql.Date(new java.util.Date().getTime());
				
				ps.setLong(1, a.getIdArticolo());						/* product_id */
				ps.setString(2, a.getCodice());							/* model */
				ps.setString(3, a.getCodice());							/* sku */
				String codiceBarre = a.getCodiceBarre();
				if (codiceBarre==null) codiceBarre = "";		/* ean */
				ps.setString(4, codiceBarre);		
				ps.setLong(5, a.getQuantitaMagazzino());				/* quantity */
				
				ps.setInt(6, 6);										/* stock_status_id */
				ps.setString(7, "articoli/"+a.getImmagine1());		/* image */
				ps.setInt(8, 0);										/* manufacturer_id */
				ps.setInt(9, 1);										/* shipping */
				double price = a.getPrezzoPiattaforme();
				if (price==0) price = a.getPrezzoDettaglio();
				ps.setDouble(10, price);				/* price */
				
				ps.setInt(11,0);										/* points */
				
				int id_iva = 11; /* 22% */ 
				if (a.getAliquotaIva()==10) id_iva=12; /* 10% */ 
				else if (a.getAliquotaIva()==4) id_iva=13; /* 4% */ 
				
				ps.setInt(12,id_iva);										/* tax_class_id */
				ps.setDate(13, data);									/* date_available */
				ps.setDouble(14, 0);									/* weight */
				ps.setInt(15,1);										/* weight_clasS_id */
				
				ps.setDouble(16, 0);									/* length */
				ps.setDouble(17, 0);									/* width */
				ps.setDouble(18, 0);									/* height */
				ps.setInt(19, 1);										/* length_class_id */
				ps.setInt(20, 1);										/* subtract */
				
				ps.setInt(21, 1);										/* minimum */
				ps.setInt(22, 0);										/* sort_order */
				ps.setInt(23, 1);										/* status */
				ps.setDate(24, data);									/* date_added */
				ps.setDate(25, data);									/* date_modified */
				ps.setInt(26,1);										/* viewed */
				ps.setString(27, "");
				ps.setString(28, "");
				ps.setString(29, "");
				ps.setString(30, "");
				ps.setString(31, "");
				
				
				ps.setString(32, a.getCodice());					/* model */
				ps.setString(33, a.getCodice());					/* sku */
				ps.setString(34, codiceBarre);						/* ean */
				ps.setInt(35, a.getQuantitaMagazzino());			/* quantity */
				ps.setString(36, "articoli/"+a.getImmagine1());		/* image */
				ps.setDouble(37, price);							/* price */
				ps.setInt(38, id_iva);								/* tax_class_id */
				ps.setDate(39, data);								/* date_modified */
				//ps.setLong(40, a.getIdArticolo());					/* product_id */

				ps.executeUpdate();			
				
				insertIntoProductDescription(a, con, ps);
				insertIntoProductImage(a, con, ps);
				insertIntoProductToCategory(a, con, ps);
				insertIntoProductToStore(a, con, ps);
				
				if (a.getPrezzoScontato()!=0) {
					insertIntoProductSpecial(a.getIdArticolo(), a.getPrezzoScontato(), con, ps);
				}
				
				insertIntoUrlAlias(a, con, ps);
				insertIntoProductRelated(a, con, ps);
				
				if (a.getVarianti()!=null && !a.getVarianti().isEmpty()){
					insertIntoProductOption(a,con,ps);
				}
				
				con.commit();			
				
				risultato = 1;
				
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
	
	
	
	private static void insertIntoProductDescription(Articolo a,Connection con, PreparedStatement ps) throws SQLException{
		
		String query = "INSERT INTO `product_description` (`product_id`, `language_id`, `name`, `description`, `meta_title`, `meta_description`, `meta_keyword`, `tag`) " +																	/* 26 */
						"VALUES (?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE product_id=product_id";	/* sono 8 */
		
		
		String nome = Methods.primeLettereMaiuscole(a.getNome());
		
		String descrizione = "<p>"+Methods.MaiuscolaDopoPunto(a.getDescrizione())+"<br/><br/><b>Quantit&agrave; inserzione:</b> "
				+Methods.MaiuscolaDopoPunto(a.getQuantitaInserzione())+"<br/><br/><b>Dimensioni:</b> "
				+Methods.MaiuscolaDopoPunto(a.getDimensioni())+"</p>";
		
		nome = Methods.escapeQuotes(nome);
		descrizione = Methods.escapeQuotes(descrizione);
		
		String metaDescription = metaDescriptionGenerator(a);
		String keyword = keywordGenerator(a);
		String tag = tagGenerator(a);
	
		ps = con.prepareStatement(query);
		
		ps.setLong(1, a.getIdArticolo());		/* product_id */
		ps.setInt(2, 2);						/* language_id */
		ps.setString(3, nome);					/* name */	
		ps.setString(4, descrizione);			/* description */
		ps.setString(5, nome);					/* meta_title */	
		ps.setString(6, metaDescription);		/* meta_description */
		ps.setString(7, keyword);				/* meta_keyword */
		ps.setString(8, tag);					/* tag */

		ps.executeUpdate();		
		
		
		ps.setInt(2, 1);						/* language_id */
				
		ps.executeUpdate();
					
		/* il commit viene fatto alla fine del metodo principale */
		//con.commit();						
	}
	
	private static void insertIntoProductImage(long idArticolo, String immagine, int ordinamento, Connection con, PreparedStatement ps) throws SQLException{
		
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
				query = "INSERT INTO `product_image` (`product_id`, `image`, `sort_order`) VALUES (?,?,?) ON DUPLICATE KEY UPDATE product_id=product_id";
				
				ps = con.prepareStatement(query);
			
			
				ps.setLong(1, idArticolo);		
				ps.setString(2, "articoli/"+immagine);		
				ps.setInt(3, ordinamento);	
				
				ps.executeUpdate();	
			}
			
		}
		/* il commit viene fatto alla fine del metodo principale */
		//con.commit();							
	}
	
	
	private static void insertIntoProductImage(Articolo a,Connection con, PreparedStatement ps) throws SQLException{
		
		//la prima immagine è già presente nella descrizione del prodotto
		//insertIntoProductImage(a.getIdArticolo(),a.getImmagine1(),1,con);
		insertIntoProductImage(a.getIdArticolo(),a.getImmagine2(),2,con, ps);
		insertIntoProductImage(a.getIdArticolo(),a.getImmagine3(),3,con, ps);
		insertIntoProductImage(a.getIdArticolo(),a.getImmagine4(),4,con, ps);
		insertIntoProductImage(a.getIdArticolo(),a.getImmagine5(),5,con, ps);
		
		/* il commit viene fatto alla fine del metodo principale */
		//con.commit();							
	}
	
	private static void insertIntoProductToCategory(Articolo a,Connection con, PreparedStatement ps) throws SQLException{
		
		String query = "INSERT INTO `product_to_category` (`product_id`, `category_id`) VALUES (?,?) ON DUPLICATE KEY UPDATE product_id=product_id";	
	
		ps = con.prepareStatement(query);
		
		ps.setLong(1, a.getIdArticolo());	
		ps.setLong(2, a.getIdCategoria());	
		
		ps.executeUpdate();		
		
		if (a.getIdCategoria2()>0){
			ps.setLong(1, a.getIdArticolo());	
			ps.setLong(2, a.getIdCategoria2());	
			
			ps.executeUpdate();		
		}
		
		if (a.getPrezzoScontato()!=0){
			ps.setLong(1, a.getIdArticolo());	
			ps.setLong(2, 115); //OUTLET	
			
			ps.executeUpdate();		
		}
		
		/* il commit viene fatto alla fine del metodo principale */
		//con.commit();							
	}
	
	private static void insertIntoProductToStore(Articolo a,Connection con, PreparedStatement ps) throws SQLException{
		
		String query = "INSERT INTO `product_to_store` (`product_id`, `store_id`) VALUES (?,?) ON DUPLICATE KEY UPDATE product_id=product_id";	
	
		ps = con.prepareStatement(query);
		
		ps.setLong(1, a.getIdArticolo());	
		ps.setInt(2, 0);	
		
		ps.executeUpdate();				
		
		/* il commit viene fatto alla fine del metodo principale */
		//con.commit();							
	}
	
	private static void insertIntoUrlAlias(Articolo a,Connection con, PreparedStatement ps) throws SQLException{
	
		String query = "INSERT INTO `url_alias` (`query`, `keyword`, `language_id`) VALUES (?,?,?) ON DUPLICATE KEY UPDATE query=query";	
	
		ps = con.prepareStatement(query);
		
		String url = a.getNome().toLowerCase();
		url = url.replace("\"", " ");
		url = url.replace("  ", " ");
		url = url.replace("  ", " ");
		url = url.replace(" ", "-");
		url = url.replace("/", "-");
		
		
		ps.setString(1, "product_id="+a.getIdArticolo());	
		ps.setString(2, url);	
		ps.setInt(3, 2);	
		
		ps.executeUpdate();				
		
		/* il commit viene fatto alla fine del metodo principale */
		//con.commit();							
	}
	
	private static void insertIntoProductRelated(Articolo a,Connection con, PreparedStatement ps) throws SQLException{
		
		List<Long> correlati = relatedProductsGenerator(a, ps);
	
		if (correlati!=null && !correlati.isEmpty()){
			ps = con.prepareStatement("INSERT INTO `product_related` (`product_id`, `related_id`) VALUES (?,?) ON DUPLICATE KEY UPDATE product_id=product_id");
			
			for (long c : correlati){
				ps.setLong(1, a.getIdArticolo());	
				ps.setLong(2, c);	
				
				ps.executeUpdate();	
			}
		}
		/* il commit viene fatto alla fine del metodo principale */
		//con.commit();							
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
	
	private static void insertIntoProductOption(Articolo a,Connection con, PreparedStatement ps) throws SQLException{
		
		String query = "INSERT INTO `product_option` (`product_id`, `option_id`, `value`,`required`) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE product_id=product_id";	
		
		ps = con.prepareStatement(query);
		
		int option_id=4;
		
		Variante_Articolo v1 = a.getVarianti().get(0);
		if (v1.getTipo()!=null && !v1.getTipo().isEmpty()){
			if (v1.getTipo().equals("Colore")) option_id=1;
			else if (v1.getTipo().equals("Gusto")) option_id=3;
			else if (v1.getTipo().equals("Tema")) option_id=2;
			else if (v1.getTipo().equals("Variante")) option_id=4;
			else if (v1.getTipo().equals("Misura")) option_id=5;
		}
	
		ps.setLong(1, a.getIdArticolo());	
		ps.setLong(2, option_id);	
		ps.setString(3, "");	
		ps.setInt(4, 1);	
		
		ps.executeUpdate();		
		
		for (Variante_Articolo v : a.getVarianti()){
		
			query = "INSERT INTO `option_value` (`option_id`, `image`,`sort_order`) VALUES (?,?,?) ON DUPLICATE KEY UPDATE option_id=option_id";
			
			ps = con.prepareStatement(query);
			
			ps.setInt(1, option_id);	
			ps.setString(2, "articoli/"+Methods.trimAndToLower(v.getImmagine()));	
			ps.setInt(3, 0);	
			
			ps.executeUpdate();	
			
			query = "INSERT INTO `option_value_description` (`option_value_id`,`language_id` ,`option_id`, `name`) VALUES (LAST_INSERT_ID(),?,?,?) ON DUPLICATE KEY UPDATE option_value_id=option_value_id";
			
			ps = con.prepareStatement(query);
			
			ps.setInt(1, 2);	
			ps.setInt(2, option_id);	
			ps.setString(3, Methods.escapeQuotes(v.getValore()));	
			
			ps.executeUpdate();		
			
			query = "INSERT INTO `product_option_value` (`product_option_id`,`product_id` ,`option_id`, `option_value_id`," +
						"`quantity`,`subtract`,`price`,`price_prefix`,`points`,`points_prefix`,`weight`,`weight_prefix`)" +
						" VALUES ((select product_option_id from product_option where product_id=?),?,?,LAST_INSERT_ID(),?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE product_id=product_id";
		
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
			
			insertIntoProductImage(a.getIdArticolo(),v.getImmagine(),0,con,ps);
		}
	}
	
	private static void insertIntoProductSpecial(long idArticolo, double prezzoScontato, Connection con, PreparedStatement ps) throws SQLException{
		
		String q = "INSERT INTO product_special(product_id, customer_group_id, priority, price, date_start, date_end) " +
						"VALUES (?,?,?,?,?,?);";
		
		ps = con.prepareStatement(q);
		
		ps.setLong(1, idArticolo);
		ps.setInt(2, 1);
		ps.setInt(3, 0);
		ps.setDouble(4, prezzoScontato);
		ps.setString(5, "0000-00-00");
		ps.setString(6, "0000-00-00");
			
		ps.executeUpdate();
				
	}
	
	public static int deleteProduct(Articolo a){	
		Log.info("Eliminazione da ZeldaBomboniere.it dell'inserzione di "+a.getCodice());
		
		PreparedStatement ps = null;
		Connection con = null;
		int res = 0;
		
		try {		
			con = DataSource.getZBConnection();
			
			String query = "delete from `product` where `product_id` = ? ";
			ps = con.prepareStatement(query);
			ps.setLong(1, a.getIdArticolo());
			res = ps.executeUpdate();
			
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
			
			query = "delete from `url_alias` where `query` = ? ";
			ps = con.prepareStatement(query);
			ps.setString(1, "product_id="+a.getIdArticolo());
			ps.executeUpdate();
			
			query = "delete from `product_related` where `product_id` = ? ";
			ps = con.prepareStatement(query);
			ps.setLong(1, a.getIdArticolo());
			ps.executeUpdate();
			
			query = "delete from `product_option` where `product_id` = ? ";
			ps = con.prepareStatement(query);
			ps.setLong(1, a.getIdArticolo());
			ps.executeUpdate();
			
			query = "delete from `product_option_value` where `product_id` = ? ";
			ps = con.prepareStatement(query);
			ps.setLong(1, a.getIdArticolo());
			ps.executeUpdate();
			
			con.commit();
			
		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();	
			res = 0;
			try { 
				con.rollback();
			} catch (SQLException e) { Log.info(e); e.printStackTrace();	}
		}
		finally {
			DataSource.closeConnections(con, null, ps, null);
		}		
		return res;
	}
	
//	private static boolean checkIfProductExist(long id_prodotto, Connection con, PreparedStatement ps){	
//		ResultSet rs = null;
//		boolean exist = false;
//		
//		try {		
//			ps = con.prepareStatement("select * from `product` where `product_id` = ? ");
//			ps.setLong(1, id_prodotto);
//			
//			rs = ps.executeQuery();
//			
//			while (rs.next()){
//				exist = true;
//			}			
//		} catch (Exception ex) {
//			Log.info(ex); ex.printStackTrace();	}
//		finally {
//			DataSource.closeStatements(null,null,rs);
//		}		
//		return exist;
//	}
	
	private static String keywordGenerator(Articolo a){
		
		String nomeCategoria = CategorieBusiness.getInstance().getMappaCategorie().get(a.getIdCategoria()).getNomeCategoria();
		
		String keyword = a.getParoleChiave1();
		if (a.getParoleChiave2()!=null && !a.getParoleChiave2().isEmpty()) keyword+=" "+a.getParoleChiave2();
		if (a.getParoleChiave3()!=null && !a.getParoleChiave3().isEmpty()) keyword+=" "+a.getParoleChiave3();
		if (a.getParoleChiave4()!=null && !a.getParoleChiave4().isEmpty()) keyword+=" "+a.getParoleChiave4();
		if (a.getParoleChiave5()!=null && !a.getParoleChiave5().isEmpty()) keyword+=" "+a.getParoleChiave5();
		
		if (keyword==null || keyword.isEmpty()){
			if (a.getTitoloInserzione()!=null && !a.getTitoloInserzione().isEmpty()) keyword = a.getTitoloInserzione();
			else keyword = a.getNome();
		}
		keyword+=" "+nomeCategoria;
		
		keyword = Methods.rimuoviApiciSlash(keyword);
		keyword = keyword.toLowerCase();
		
		if (keyword.length()>255) keyword = keyword.substring(0, 255);
		
		return keyword;
	}
	
	private static String tagGenerator(Articolo a){
		String nomeCategoria = CategorieBusiness.getInstance().getMappaCategorie().get(a.getIdCategoria()).getNomeCategoria();
		
		String tag = a.getNome();
		tag+=" "+nomeCategoria;
		tag = Methods.rimuoviApiciSlash(tag);
		tag = tag.toLowerCase();
		tag = tag.replace(" cm ", " ");
		tag = tag.replace(" mm ", " ");
		tag = tag.replace(" pz ", " ");
		tag = tag.replace(" gr ", " ");
		tag = tag.replace(" x ", " ");
		tag = tag.replace(" da ", " ");
		tag = tag.replace(" di ", " ");
		tag = tag.replace(" e ", " ");
		tag = tag.replace(" in ", " ");
		tag = tag.replace(" con ", " ");
		tag = tag.replace(" per ", " ");
		tag = tag.replace(" ", ", ");
		tag = tag.trim();
		
		return tag;
	}
	
	private static String metaDescriptionGenerator(Articolo a){
		String metaDescription = a.getNome()+" - "+a.getDescrizione().toLowerCase();
		int pos = metaDescription.indexOf(".");
		if (pos!=-1) metaDescription = metaDescription.substring(0, pos);
		if (metaDescription.length()>255) metaDescription = metaDescription.substring(0, 255);		
		
		return metaDescription;
	}
	
	private static List<Long> relatedProductsGenerator(Articolo a, PreparedStatement ps){
		Connection con = null;
		ResultSet rs = null;
		List<Long> related = null;
		try{
			con = DataSource.getLocalConnection();
			ps = con.prepareStatement("select distinct a.id_articolo, a.nome, " +
				" max(2 / (case a.prezzo_dettaglio >= "+a.getPrezzoDettaglio()+" when 0 then ("+a.getPrezzoDettaglio()+" / a.prezzo_dettaglio) else (a.prezzo_dettaglio / "+a.getPrezzoDettaglio()+") end) * " +
				" (case id_categoria when "+a.getIdCategoria()+" then 2 else 1 end) + (1 + rel.rlv) )  as relevance, rel.rlv " +
				" from articoli a " +
				" inner join (SELECT id_articolo, MATCH(nome, descrizione) AGAINST ('"+Methods.escapeQuotes(metaDescriptionGenerator(a))+"') as rlv " +
				" FROM articoli) as rel on rel.id_articolo = a.id_articolo " +
				" group by a.id_articolo" +
				" having a.id_articolo <> "+a.getIdArticolo()+" and relevance >= 8 " +
					//	"and a.id_articolo not in (select related_id from product_related where product_id = "+a.getIdArticolo()+")" +
				" order by relevance desc" +
				" limit 0,5");
		
		rs = ps.executeQuery();
		related = new ArrayList<Long>();
		
		while (rs.next()){
			related.add(rs.getLong("id_articolo"));
		}			
		
		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();	}
		finally {
			DataSource.closeConnections(con,null,null,rs);
		}		
		return related;
	}
	
	public static void updateProduct(){
		
	}
	
	public static void aggiornaStatoOrdine(){
		
	}
	
	public static boolean confirmShipment(String orderId, String trackingNumber, String courier){
		Connection con = null;
		PreparedStatement ps = null;
		boolean ok = true;
		
		try {			
			con = DataSource.getZBConnection();
			
			String query = "UPDATE `order` " +
									"SET `order_status_id` = ? " +
									"WHERE `order_id` = ?";
			
			ps = con.prepareStatement(query);
			ps.setInt(1, 3); //3 = spedito
			ps.setInt(2, Integer.valueOf(orderId));
			
			ps.executeUpdate();			
			
			query = "INSERT INTO `order_history` (`order_id`,`order_status_id`,`notify`,`comment`,`date_added`) " +
							"VALUES (?,?,?,?,?);";
			
			ps = con.prepareStatement(query);
			ps.setInt(1, Integer.valueOf(orderId));
			ps.setInt(2, 3); //3 = spedito
			ps.setInt(3, 0);
			ps.setString(4, "Codice per il tracciamento della spedizione (con corriere "+courier+"): "+trackingNumber);
			ps.setDate(5, new java.sql.Date(new Date().getTime()));
			
			ps.executeUpdate();			
			
			con.commit();
			
		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
			ok = false;
			try { 
				con.rollback();
			} catch (SQLException e) { Log.info(e); e.printStackTrace();	}
		}
		finally {
			DataSource.closeConnections(con,null,ps,null);
		}		
		return ok;
	}
	
	public static int confirmShipments(List<Map<String,String>> orders){
		Connection con = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		int spediti = 0;
		
		try {			
			con = DataSource.getZBConnection();
			
			String query1 = "UPDATE `order` " +
									"SET `order_status_id` = ? " +
									"WHERE `order_id` = ?";
			
			String query2 = "INSERT INTO `order_history` (`order_id`,`order_status_id`,`notify`,`comment`,`date_added`) " +
					"VALUES (?,?,?,?,?);";
			
			ps1 = con.prepareStatement(query1);
			ps2 = con.prepareStatement(query2);
			
			for (Map<String,String> ord : orders){
				ps1.setInt(1, 3); //3 = spedito
				ps1.setInt(2, Integer.valueOf(ord.get("id_ordine_piattaforma").replace("ZB_", "")));
				
				spediti = spediti + ps1.executeUpdate();			
				
				ps2.setInt(1, Integer.valueOf(ord.get("id_ordine_piattaforma").replace("ZB_", "")));
				ps2.setInt(2, 3); //3 = spedito
				ps2.setInt(3, 0);
				ps2.setString(4, "Codice per il tracciamento della spedizione (con corriere "+ord.get("nome_corriere")+"): "+ord.get("numero_tracciamento"));
				ps2.setDate(5, new java.sql.Date(new Date().getTime()));
				
				ps2.executeUpdate();			
				
				Email.inviaNumeroTracciamentoOrdine(ord.get("email"), ord.get("numero_tracciamento"), ord.get("nome_corriere"));
			}
			con.commit();
			
			
			
		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
			try { 
				con.rollback();
			} catch (SQLException e) { Log.info(e); e.printStackTrace();	}
		}
		finally {
			DataSource.closeConnections(con,null,ps1,null);
			DataSource.closeStatements(null,ps2,null);
		}		
		return spediti;
	}
	
	
	public static void main(String[] args){
		
		List<Articolo> l = Articolo_DAO.getArticoli("select * from articoli where codice='CC091'");
		
		for (Articolo a : l){
			System.out.println("NOME: "+a.getNome());
			System.out.println("DESCRIZIONE: "+a.getDescrizione());
			System.out.println("KEYWORD: "+keywordGenerator(a));
			System.out.println("TAG: "+tagGenerator(a));
			System.out.println("META_DESCRIPTION: "+metaDescriptionGenerator(a));
		}
		
	}
	
}
