package it.swb.piattaforme.gm;

import it.swb.database.Categoria_DAO;
import it.swb.database.DataSource;
import it.swb.log.Log;
import it.swb.model.Articolo;
import it.swb.model.Product;
import it.swb.model.Variante_Articolo;
import it.swb.utility.Methods;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class GloriaMoraldiRemoto_DAO {

	public static int insertIntoProducts(Product p){
		Log.debug("Inserimento articolo su gloriamoraldi.it remoto...");
		Connection con = null;
		PreparedStatement ps = null;
		int risultato = 0;
		
		try {			
			con = DataSource.getRemoteConnection();
			
							
				if (p.getProduct_id()!=0 && !checkIfProductExist(p.getProduct_id(),con)){
				
					String query = "INSERT INTO `pmqbpiom_jshopping_products` (`product_id`, `parent_id`, `product_ean`, `product_quantity`, `unlimited`," +
						" `product_availability`, `product_date_added`, `date_modify`, `product_publish`, `product_tax_id`, `currency_id`, `product_template`," +
						" `product_url`, `product_old_price`, `product_buy_price`, `product_price`, `min_price`, `different_prices`, `product_weight`," +
						" `product_thumb_image`, `product_name_image`, `product_full_image`, `product_manufacturer_id`, `product_is_add_price`," +
						" `add_price_unit_id`, `average_rating`, `reviews_count`, `delivery_times_id`, `hits`, `weight_volume_units`, `basic_price_unit_id`," +
						" `label_id`, `vendor_id`, `access`, `name_en-GB`, `alias_en-GB`, `short_description_en-GB`, `description_en-GB`, `meta_title_en-GB`," +
						" `meta_description_en-GB`, `meta_keyword_en-GB`, `name_it-IT`, `alias_it-IT`, `short_description_it-IT`, `description_it-IT`," +
						" `meta_title_it-IT`, `meta_description_it-IT`, `meta_keyword_it-IT`) VALUES " +
						"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				
					ps = con.prepareStatement(query);
					
					ps.setLong(1, p.getProduct_id());
					ps.setLong(2, 0);
					if (p.getProduct_ean()==null) ps.setString(3, "");
					else ps.setString(3, p.getProduct_ean());
					ps.setLong(4, p.getProduct_quantity());
					ps.setLong(5, 0);
					ps.setString(6, "");
					if (p.getProduct_date_added()!=null)
						ps.setDate(7, p.getProduct_date_added());
					else ps.setDate(7, new Date(new java.util.Date().getTime()));
					ps.setDate(8, p.getDate_modify());
					ps.setLong(9, 1);
					ps.setLong(10, p.getProduct_tax_id());
					ps.setLong(11, 1);
					ps.setString(12, "default");
					ps.setString(13, "");
					ps.setLong(14, 0);
					ps.setDouble(15, p.getProduct_buy_price());				//buy price
					ps.setDouble(16, p.getProduct_price());					//product price
					ps.setDouble(17, 0);									//min price
					ps.setLong(18, 0);										//different prices
					ps.setDouble(19, 0);									//product weight
					ps.setString(20, p.getProduct_thumb_image());			//thumb image
					ps.setString(21, p.getProduct_name_image());			//name image (medium)
					ps.setString(22, p.getProduct_full_image());			//full image
					ps.setLong(23, 0);
					ps.setLong(24, 0);
					ps.setLong(25, 0);
					ps.setDouble(26, 0);									//average rating
					ps.setLong(27, 0);										//reviews count
					ps.setLong(28, 0);										//delivery_times_id
					ps.setLong(29, 0);										//hits
					ps.setLong(30, 0);
					ps.setLong(31, 0);
					ps.setLong(32, 0);
					ps.setLong(33, 0);
					ps.setLong(34, 1);
					ps.setString(35, p.getName_en_GB());
					
					String aliasEN = p.getAlias_en_GB();
					if (checkIfAliasExist(aliasEN,con))
						aliasEN = Methods.creaAliasRandom(p.getName_en_GB());
					
					ps.setString(36, aliasEN);
					ps.setString(37, p.getShort_description_en_GB());
					ps.setString(38, p.getDescription_en_GB());
					ps.setString(39, p.getMeta_title_en_GB());
					ps.setString(40, p.getMeta_description_en_GB());
					ps.setString(41, p.getMeta_keyword_en_GB());
					
					ps.setString(42, p.getName_it_IT());								//name_it-IT	
					
					String aliasIT = p.getAlias_it_IT();
					if (checkIfAliasExist(aliasIT,con))
						aliasIT = Methods.creaAliasRandom(p.getName_it_IT());
					
					ps.setString(43, aliasIT);											//alias_it-IT
					ps.setString(44, p.getShort_description_it_IT());			//Short_description_it_IT
					ps.setString(45, p.getDescription_it_IT());					//Description_it_IT
					ps.setString(46, p.getMeta_title_it_IT());					//Meta_title_it_IT
					ps.setString(47, p.getMeta_description_it_IT());				//Meta_description_it_IT
					ps.setString(48, p.getMeta_keyword_it_IT());					//Meta_keyword_it_IT
					
//					ps.setString(49, p.getExtra_field_1());						//Extra_field_1
//					ps.setString(50, p.getExtra_field_2());						//Extra_field_2
	
					risultato = ps.executeUpdate();				
					
					insertIntoProductToCategories(p,con);
					insertIntoProductImages(p,con);
					
					if (p.getVarianti()!=null && !p.getVarianti().isEmpty())
						{
						for (Variante_Articolo v : p.getVarianti()){
							v.setId_articolo(p.getProduct_id());
						}
						
						insertIntoProductAttr2(p.getVarianti(),con);
						}
					con.commit();						
					
					Log.debug("Inserimento riuscito.");					
				}
				else { Log.debug("Non inserito, ID articolo già esistente: "+p.getProduct_id());	}

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
	
	
	public static int insertIntoProducts(Product p, Connection con){
		Log.debug("Inserimento articolo su gloriamoraldi.it remoto...");
		
		PreparedStatement ps = null;
		int risultato = 0;
		
		try {			
							
				if (p.getProduct_id()!=0 && !checkIfProductExist(p.getProduct_id(),con)){
				
					String query = "INSERT INTO `pmqbpiom_jshopping_products` (`product_id`, `parent_id`, `product_ean`, `product_quantity`, `unlimited`," +
						" `product_availability`, `product_date_added`, `date_modify`, `product_publish`, `product_tax_id`, `currency_id`, `product_template`," +
						" `product_url`, `product_old_price`, `product_buy_price`, `product_price`, `min_price`, `different_prices`, `product_weight`," +
						" `product_thumb_image`, `product_name_image`, `product_full_image`, `product_manufacturer_id`, `product_is_add_price`," +
						" `add_price_unit_id`, `average_rating`, `reviews_count`, `delivery_times_id`, `hits`, `weight_volume_units`, `basic_price_unit_id`," +
						" `label_id`, `vendor_id`, `access`, `name_en-GB`, `alias_en-GB`, `short_description_en-GB`, `description_en-GB`, `meta_title_en-GB`," +
						" `meta_description_en-GB`, `meta_keyword_en-GB`, `name_it-IT`, `alias_it-IT`, `short_description_it-IT`, `description_it-IT`," +
						" `meta_title_it-IT`, `meta_description_it-IT`, `meta_keyword_it-IT`) VALUES " +
						"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				
					ps = con.prepareStatement(query);
					
					ps.setLong(1, p.getProduct_id());
					ps.setLong(2, 0);
					if (p.getProduct_ean()==null) ps.setString(3, "");
					else ps.setString(3, p.getProduct_ean());
					ps.setLong(4, p.getProduct_quantity());
					ps.setLong(5, 0);
					ps.setString(6, "");
					if (p.getProduct_date_added()!=null)
						ps.setDate(7, p.getProduct_date_added());
					else ps.setDate(7, new Date(new java.util.Date().getTime()));
					ps.setDate(8, p.getDate_modify());
					ps.setLong(9, 1);
					ps.setLong(10, 1);
					ps.setLong(11, 1);
					ps.setString(12, "default");
					ps.setString(13, "");
					ps.setLong(14, 0);
					ps.setDouble(15, p.getProduct_buy_price());				//buy price
					ps.setDouble(16, p.getProduct_price());					//product price
					ps.setDouble(17, 0);									//min price
					ps.setLong(18, 0);										//different prices
					ps.setDouble(19, 0);									//product weight
					ps.setString(20, p.getProduct_thumb_image());			//thumb image
					ps.setString(21, p.getProduct_name_image());			//name image (medium)
					ps.setString(22, p.getProduct_full_image());			//full image
					ps.setLong(23, 0);
					ps.setLong(24, 0);
					ps.setLong(25, 0);
					ps.setDouble(26, 0);									//average rating
					ps.setLong(27, 0);										//reviews count
					ps.setLong(28, 0);										//delivery_times_id
					ps.setLong(29, 0);										//hits
					ps.setLong(30, 0);
					ps.setLong(31, 0);
					ps.setLong(32, 0);
					ps.setLong(33, 0);
					ps.setLong(34, 1);
					ps.setString(35, p.getName_en_GB());
					
					String aliasEN = p.getAlias_en_GB();
					if (checkIfAliasExist(aliasEN,con))
						aliasEN = Methods.creaAliasRandom(p.getName_en_GB());
					
					ps.setString(36, aliasEN);
					ps.setString(37, p.getShort_description_en_GB());
					ps.setString(38, p.getDescription_en_GB());
					ps.setString(39, p.getMeta_title_en_GB());
					ps.setString(40, p.getMeta_description_en_GB());
					ps.setString(41, p.getMeta_keyword_en_GB());
					
					ps.setString(42, p.getName_it_IT());								//name_it-IT	
					
					String aliasIT = p.getAlias_it_IT();
					if (checkIfAliasExist(aliasIT,con))
						aliasIT = Methods.creaAliasRandom(p.getName_it_IT());
					
					ps.setString(43, aliasIT);											//alias_it-IT
					ps.setString(44, p.getShort_description_it_IT());			//Short_description_it_IT
					ps.setString(45, p.getDescription_it_IT());					//Description_it_IT
					ps.setString(46, p.getMeta_title_it_IT());					//Meta_title_it_IT
					ps.setString(47, p.getMeta_description_it_IT());				//Meta_description_it_IT
					ps.setString(48, p.getMeta_keyword_it_IT());					//Meta_keyword_it_IT
					
//					ps.setString(49, p.getExtra_field_1());						//Extra_field_1
//					ps.setString(50, p.getExtra_field_2());						//Extra_field_2
	
					risultato = ps.executeUpdate();				
					
					insertIntoProductToCategories(p,con);
					insertIntoProductImages(p,con);
					
					if (p.getVarianti()!=null && !p.getVarianti().isEmpty())
						{
						for (Variante_Articolo v : p.getVarianti()){
							v.setId_articolo(p.getProduct_id());
						}
						
						insertIntoProductAttr2(p.getVarianti(),con);
						}					
					
					Log.debug("Inserimento riuscito.");					
				}
				else { Log.debug("Non inserito, ID articolo già esistente: "+p.getProduct_id());	}

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { 
				con.rollback();
			} catch (SQLException e) { Log.info(e); e.printStackTrace();	}
		}
		 finally {
			 DataSource.closeStatements(null,ps,null);
		}
		return risultato;
	}
	
	
	public static void insertIntoProductToCategories(Product p, Connection con) {
//		Connection con = null;
		PreparedStatement ps = null;
		
		try {
//			con = DataSource.getRemoteConnection();
			String query_insert="INSERT INTO `pmqbpiom_jshopping_products_to_categories` " +
						"(`product_id`, `category_id`, `product_ordering`) VALUES (?,?,?)";
			ps = con.prepareStatement(query_insert);
				
			ps.setLong(1, p.getProduct_id());
			ps.setInt(2, p.getCategory_id());
			ps.setInt(3, Categoria_DAO.checkLastCategoriesOrdering(p.getCategory_id(),con));
			
			ps.executeUpdate();
		
			//il commit viene fatto alla fine del metodo principale, sulla connessione che qui viene passata come parametro
			//con.commit();		
			
		} catch (Exception ex) {
			Log.error("L'articolo NON è stato associato ad una categoria.");
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 DataSource.closeStatements(null,ps,null);
		}
	}
	
	public static void insertIntoProductImages(Product p, Connection con){
//		Connection con = null;
		PreparedStatement ps = null;
		int x = 0;
		String cartella = "";
		String nome = "";
		int ordering = 1;

		try {				
//			con = DataSource.getRemoteConnection();
			String query = "INSERT INTO pmqbpiom_jshopping_products_images(product_id,image_thumb,image_name,image_full,name,ordering)" +
					" VALUES (?,?,?,?,?,?)";
			
			if (p.getImmagine()!=null && !p.getImmagine().equals("")) {				
				
				ps = con.prepareStatement(query);
				
				ps.setLong(1, p.getProduct_id());
				
				
				cartella = Methods.getNomeCartella(p.getImmagine());
				nome = Methods.getNomeImmagine(p.getImmagine());
				
				ps.setString(2, "_thumbnails/piccole/"+cartella+"/piccola_"+nome);
				ps.setString(3, "_thumbnails/medie/"+cartella+"/media_"+nome);
				ps.setString(4, p.getImmagine());
				ps.setString(5, "");
				
				ordering = checkLastImagesOrdering(p.getProduct_id(),con);
				ps.setInt(6, ordering);
				
				ps.executeUpdate();
				
//				if (cartella.contains("/"))	cartella = cartella.replace("/", "\\");
//				Methods.ScaricaImmagineECreaThumbnails(nome, cartella);
			}
			if (p.getImmagine2()!=null && !p.getImmagine2().equals("")) {				
			
				ps = con.prepareStatement(query);
				
				ps.setLong(1, p.getProduct_id());
				
				x = p.getImmagine2().lastIndexOf("/");
				cartella = p.getImmagine2().substring(0,x);
				nome = p.getImmagine2().substring(x+1,p.getImmagine2().length());
				
				ps.setString(2, "_thumbnails/piccole/"+cartella+"/piccola_"+nome);
				ps.setString(3, "_thumbnails/medie/"+cartella+"/media_"+nome);
				ps.setString(4, p.getImmagine2());
				ps.setString(5, "");
				ordering++;			
				ps.setInt(6, ordering);
				
				ps.executeUpdate();
				
//				if (cartella.contains("/"))	cartella = cartella.replace("/", "\\");
//				Methods.ScaricaImmagineECreaThumbnails(nome, cartella);
			}
			if (p.getImmagine3()!=null && !p.getImmagine3().equals("")) {				
				
				ps = con.prepareStatement(query);
				
				ps.setLong(1, p.getProduct_id());
				
				x = p.getImmagine3().lastIndexOf("/");
				cartella = p.getImmagine3().substring(0,x);
				nome = p.getImmagine3().substring(x+1,p.getImmagine3().length());
				
				ps.setString(2, "_thumbnails/piccole/"+cartella+"/piccola_"+nome);
				ps.setString(3, "_thumbnails/medie/"+cartella+"/media_"+nome);
				ps.setString(4, p.getImmagine3());
				ps.setString(5, "");
				ordering++;					
				ps.setInt(6, ordering);
				
				ps.executeUpdate();
				
//				if (cartella.contains("/"))	cartella = cartella.replace("/", "\\");
//				Methods.ScaricaImmagineECreaThumbnails(nome, cartella);
			}
			if (p.getImmagine4()!=null && !p.getImmagine4().equals("")) {				
			
				ps = con.prepareStatement(query);
				
				ps.setLong(1, p.getProduct_id());
				
				x = p.getImmagine4().lastIndexOf("/");
				cartella = p.getImmagine4().substring(0,x);
				nome = p.getImmagine4().substring(x+1,p.getImmagine4().length());
				
				ps.setString(2, "_thumbnails/piccole/"+cartella+"/piccola_"+nome);
				ps.setString(3, "_thumbnails/medie/"+cartella+"/media_"+nome);
				ps.setString(4, p.getImmagine4());
				ps.setString(5, "");
				ordering++;					
				ps.setInt(6, ordering);
				
				ps.executeUpdate();
				
//				if (cartella.contains("/"))	cartella = cartella.replace("/", "\\");
//				Methods.ScaricaImmagineECreaThumbnails(nome, cartella);
			}
			if (p.getImmagine5()!=null && !p.getImmagine5().equals("")) {				
				
				ps = con.prepareStatement(query);
				
				ps.setLong(1, p.getProduct_id());
				
				x = p.getImmagine5().lastIndexOf("/");
				cartella = p.getImmagine5().substring(0,x);
				nome = p.getImmagine5().substring(x+1,p.getImmagine5().length());
				
				ps.setString(2, "_thumbnails/piccole/"+cartella+"/piccola_"+nome);
				ps.setString(3, "_thumbnails/medie/"+cartella+"/media_"+nome);
				ps.setString(4, p.getImmagine5());
				ps.setString(5, "");
				ordering++;					
				ps.setInt(6, ordering);
				
				ps.executeUpdate();
				
//				if (cartella.contains("/"))	cartella = cartella.replace("/", "\\");
//				Methods.ScaricaImmagineECreaThumbnails(nome, cartella);
			}			
			//il commit viene fatto alla fine del metodo principale, sulla connessione che qui viene passata come parametro
			//con.commit();
			
		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}finally {
			DataSource.closeStatements(null,ps,null);
		}	 	
	}
	
	public static void insertIntoProductAttr2(List<Variante_Articolo> varianti, Connection con) {
//		Connection con = null;
		PreparedStatement ps = null;
		
		try {				
//			con = DataSource.getRemoteConnection();
			
			for (Variante_Articolo v : varianti)
			{
				int id_valore_variante = 0;
				
				//controllo se già esiste				
				if (!checkIfVariantExist(v,con)){
					String query_insert="insert into `pmqbpiom_jshopping_products_attr2`" +
							"(product_id,attr_id,attr_value_id,price_mod,addprice)" +
							"VALUES (?,?,?,?,?)";
					ps = con.prepareStatement(query_insert);
									
					ps.setLong(1, v.getId_articolo());
					ps.setInt(2, v.getAttr_id());	//1 corrisponde a variante colore
					id_valore_variante = checkIfVariantValueExist(v,con);
					ps.setInt(3, id_valore_variante);					
					ps.setString(4, "+");
					ps.setDouble(5, 0.00);
		
					ps.executeUpdate();
					
					insertIntoProductImages(v.getId_articolo(),v.getImmagine(),v.getValore(),con);
				} 					
			}					
			//il commit viene fatto alla fine del metodo principale, sulla connessione che qui viene passata come parametro
			//con.commit();					
		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 DataSource.closeStatements(null,ps,null);
		}
	}
	
	
	public static void insertIntoProductImages(long id_prodotto, String immagine, String name, Connection con){
//		Connection con = null;
		PreparedStatement ps = null;
		String cartella = "";
		String nome = "";
		int ordering = 1;

		try {				
//			con = DataSource.getRemoteConnection();
			String query = "INSERT INTO pmqbpiom_jshopping_products_images(product_id,image_thumb,image_name,image_full,name,ordering)" +
					" VALUES (?,?,?,?,?,?)";
			
			if (immagine!=null && !immagine.trim().isEmpty()) {				
				
				ps = con.prepareStatement(query);
				
				ps.setLong(1, id_prodotto);
				
				cartella = Methods.getNomeCartella(immagine);
				nome = Methods.getNomeImmagine(immagine);
				
				ps.setString(2, "_thumbnails/piccole/"+cartella+"/piccola_"+nome);
				ps.setString(3, "_thumbnails/medie/"+cartella+"/media_"+nome);
				ps.setString(4, immagine);
				ps.setString(5, name);
				
				ordering = checkLastImagesOrdering(id_prodotto,con);
				ps.setInt(6, ordering);
				
				ps.executeUpdate();
				
//				if (cartella.contains("/"))	cartella = cartella.replace("/", "\\");
//				Methods.ScaricaImmagineECreaThumbnails(nome, cartella);
			}
			//il commit viene fatto alla fine del metodo principale, sulla connessione che qui viene passata come parametro
			//con.commit();
			
		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}finally {
			DataSource.closeStatements(null,ps,null);
		}	 	
	}
	
	
	public static int deleteProduct(Articolo a){	
		PreparedStatement ps = null;
		Connection con = null;
		int res = 0;
		
		try {		
			con = DataSource.getRemoteConnection();
			
			String query = "delete from `pmqbpiom_jshopping_products` where `product_id` = ? ";
			
			ps = con.prepareStatement(query);
			ps.setLong(1, a.getIdArticolo());
			
			ps.executeUpdate();
			
			query = "delete from `pmqbpiom_jshopping_products_to_categories` where `product_id` = ? ";
			
			ps = con.prepareStatement(query);
			ps.setLong(1, a.getIdArticolo());
			
			ps.executeUpdate();
			
			query = "delete from `pmqbpiom_jshopping_products_images` where `product_id` = ? ";
			
			ps = con.prepareStatement(query);
			ps.setLong(1, a.getIdArticolo());
			
			ps.executeUpdate();
			
			query = "delete from `pmqbpiom_jshopping_products_attr2` where `product_id` = ? ";
			
			ps = con.prepareStatement(query);
			ps.setLong(1, a.getIdArticolo());
			
			ps.executeUpdate();
			
			con.commit();
			
			res=1;							
		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();	
			try { 
				con.rollback();
			} catch (SQLException e) { Log.info(e); e.printStackTrace();	}
		}
		finally {
			DataSource.closeConnections(con, null, ps, null);
		}		
		return res;
	}
	
	
	
	private static int checkLastImagesOrdering(long product_id, Connection con){	
		PreparedStatement ps = null;
		ResultSet rs = null;
//		Connection con = null;
		int order = 1;
		
		try {		
//			con = DataSource.getRemoteConnection();
			ps = con.prepareStatement("select max(ordering) ordine from `pmqbpiom_jshopping_products_images` where `product_id`=?");
			ps.setLong(1, product_id);
			
			rs = ps.executeQuery();
			
			while (rs.next()){
				order = rs.getInt("ordine")+1;
			}
		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();	}
		finally {
			DataSource.closeStatements(null,ps,rs);
		}		
		return order;
	}
	
	private static boolean checkIfVariantExist(Variante_Articolo v, Connection con){	
		PreparedStatement ps = null;
		ResultSet rs = null;
//		Connection con = null;
		boolean exist = false;
		
		try {		
//			con = DataSource.getRemoteConnection();
			ps = con.prepareStatement("select * from `pmqbpiom_jshopping_products_attr2` where `product_id`=? and `attr_id`=? and `attr_value_id`=?");
			
			ps.setLong(1, v.getId_articolo());										
			ps.setLong(2, v.getAttr_id());
			ps.setLong(3, checkIfVariantValueExist(v,con));
						
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
	
	//controlla se il valore di una variante esiste già, e ne restituisce l'id. Se non esiste lo crea e restituisce l'id creato
	private static int checkIfVariantValueExist(Variante_Articolo v, Connection con){	
		PreparedStatement ps = null;
		ResultSet rs = null;
//		Connection con = null;
		int id = 0;
		
		try {					
//			con = DataSource.getRemoteConnection();
			//cerco l'id del valore della variante
			ps = con.prepareStatement("select value_id from `pmqbpiom_jshopping_attr_values` where LOWER(`name_it-IT`) like ? and `attr_id`=?");
			
			ps.setString(1, v.getValore().toLowerCase());
			ps.setInt(2, v.getAttr_id());			
						
			rs = ps.executeQuery();
			
			while (rs.next()){
				id = rs.getInt("value_id");
			}
			
			//il valore non esiste, devo crearlo
			if (id==0)
			{
				String query_insert = "insert into `pmqbpiom_jshopping_attr_values`" +
						"(`attr_id`,`value_ordering`,`name_en-GB`,`name_it-IT`,`image`) values (?,?,?,?,?) ";
				ps = con.prepareStatement(query_insert);					
								
				ps.setInt(1, v.getAttr_id());		
				ps.setInt(2, checkLastAttrValuesOrdering(v.getAttr_id(),con));
				ps.setString(3, v.getValore());
				ps.setString(4, v.getValore());
				ps.setString(5, "");
				
				ps.execute();
				
				//ripesco l'id del valore appena creato
				ps = con.prepareStatement("select value_id from `pmqbpiom_jshopping_attr_values` where LOWER(`name_it-IT`) like ? and `attr_id`=?");
				ps.setString(1, v.getValore().toLowerCase());
				ps.setInt(2, v.getAttr_id());
							
				rs = ps.executeQuery();
				
				while (rs.next()){
					id = rs.getInt("value_id");
				}
			}
			
		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();	}
		finally {
			DataSource.closeStatements(null,ps,rs);
		}		
		return id;
	}
	
	
	private static int checkLastAttrValuesOrdering(long attr_id, Connection con){	
		PreparedStatement ps = null;
		ResultSet rs = null;
//		Connection con = null;
		int order = 1;
		
		try {		
//			con = DataSource.getRemoteConnection();
			ps = con.prepareStatement("select max(value_ordering) ordine from `pmqbpiom_jshopping_attr_values` where `attr_id` = ?");
			ps.setLong(1, attr_id);
			
			rs = ps.executeQuery();
			
			while (rs.next()){
				order = rs.getInt("ordine")+1;
			}
		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();	}
		finally {
			DataSource.closeStatements(null,ps,rs);
		}		
		return order;
	}
	
	
	private static boolean checkIfProductExist(long id_prodotto, Connection con){	
		PreparedStatement ps = null;
		ResultSet rs = null;
//		Connection con = null;
		boolean exist = false;
		
		try {		
//			con = DataSource.getRemoteConnection();

			ps = con.prepareStatement("select * from `pmqbpiom_jshopping_products` where `product_id` = ? ");
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
	
	
	private static boolean checkIfAliasExist(String alias, Connection con){	
		PreparedStatement ps = null;
		ResultSet rs = null;
//		Connection con = null;
		boolean exist = false;
		
		try {		
//			con = DataSource.getRemoteConnection();

			ps = con.prepareStatement("select * from `pmqbpiom_jshopping_products` where `alias_it-IT` like ?");
			ps.setString(1, alias);
			
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
