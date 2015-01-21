package it.swb.database;

import it.swb.log.Log;
import it.swb.model.Categoria;
import it.swb.model.CategoriaAmazon;
import it.swb.model.CategoriaEbay;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;



public class Categoria_DAO {
	
	
	public static List<Categoria> getCategorie(){
		Log.info("Caricamento categorie in corso...");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		List<Categoria> categorie = null;

		try {			
			con = DataSource.getLocalConnection();
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM CATEGORIE order by nome_categoria_principale, nome_categoria");
			
			categorie = new ArrayList<Categoria>();
			
			/* brutto ma necessario, deve rimanere */
			Categoria d = new Categoria();
			d.setIdCategoria(-1);
			d.setNomeCategoria("- Seleziona -");
			categorie.add(d);		
			
			while (rs.next()){
				Categoria c = new Categoria();
				
				//TODO 
				c.setIdCategoria(rs.getInt("ID_CATEGORIA"));
				c.setPrincipale(rs.getBoolean("IS_PRINCIPALE"));
				//if (c.isPrincipale()){
				//	c.setNomeCategoria(rs.getString("NOME_CATEGORIA_PRINCIPALE"));			
				//} else {
				//	c.setNomeCategoria(" -> "+rs.getString("NOME_CATEGORIA"));			
				//}
				if (rs.getString("NOME_CATEGORIA")!=null)
					c.setNomeCategoria(" -> "+rs.getString("NOME_CATEGORIA"));
				else c.setNomeCategoria("");
				c.setIdCategoriaPrincipale(rs.getInt("ID_CATEGORIA_PRINCIPALE"));
				c.setNomeCategoriaPrincipale(rs.getString("NOME_CATEGORIA_PRINCIPALE"));
				c.setOrdinamento(rs.getInt("ORDINAMENTO"));
				c.setIdCategoriaEbay(rs.getLong("ID_CATEGORIA_EBAY"));
				c.setIdCategoriaYatego(rs.getString("ID_CATEGORIA_YATEGO"));
				c.setIdCategoriaAmazon(rs.getLong("ID_CATEGORIA_AMAZON"));
				c.setIdCategoriaGestionale(rs.getInt("ID_CATEGORIA_GESTIONALE"));
				c.setSoloDettaglio(rs.getInt("SOLO_DETTAGLIO"));
								
				categorie.add(c);		
				
			}
			Log.info("Categorie caricate.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { con.rollback();
			} catch (SQLException e) { Log.info(e); e.printStackTrace();	}
		}
		 finally {
			 DataSource.closeConnections(con,st,null,rs);
		}
		return categorie;
	}
	
	
	public static Map<Long, Categoria> getMappaCategorie(DbTool dbt){
		Log.info("Caricamento mappa delle categorie...");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		Map<Long,Categoria> categorie = null;

		try {	
			if (dbt==null){
				con = DataSource.getLocalConnection();
				st = con.createStatement();
			}else {
				con = dbt.getConnection();
				st = dbt.getStatement();
				rs = dbt.getResultSet();
			}
				
			rs = st.executeQuery("SELECT * FROM CATEGORIE order by nome_categoria_principale, nome_categoria");
			
			categorie = new HashMap<Long,Categoria>();
			
			while (rs.next()){
				Categoria c = new Categoria();
				
				c.setIdCategoria(rs.getLong("ID_CATEGORIA"));
				c.setPrincipale(rs.getBoolean("IS_PRINCIPALE"));
				c.setNomeCategoria(rs.getString("NOME_CATEGORIA"));			
				c.setIdCategoriaPrincipale(rs.getLong("ID_CATEGORIA_PRINCIPALE"));
				c.setNomeCategoriaPrincipale(rs.getString("NOME_CATEGORIA_PRINCIPALE"));
				c.setOrdinamento(rs.getInt("ORDINAMENTO"));
				c.setIdCategoriaEbay(rs.getLong("ID_CATEGORIA_EBAY"));
				c.setIdCategoriaYatego(rs.getString("ID_CATEGORIA_YATEGO"));
				c.setIdCategoriaAmazon(rs.getLong("ID_CATEGORIA_AMAZON"));
				c.setIdCategoriaGestionale(rs.getInt("ID_CATEGORIA_GESTIONALE"));
				c.setSoloDettaglio(rs.getInt("SOLO_DETTAGLIO"));
								
				categorie.put(c.getIdCategoria(),c);			
			}
			Log.info("Mappa delle categorie caricata.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 if (dbt==null) DataSource.closeConnections(con,st,null,rs);
		}
		return categorie;
	}
	
	public static Map<Long, Categoria> getMappaCategorieNegozioEbay(){
		Log.info("Caricamento mappa delle categorie...");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		Map<Long,Categoria> categorie = null;

		try {			
			con = DataSource.getLocalConnection();
			st = con.createStatement();
			
				
			rs = st.executeQuery("SELECT * FROM CATEGORIE order by nome_categoria_principale, nome_categoria");
			
			categorie = new HashMap<Long,Categoria>();
			
			while (rs.next()){
				Categoria c = new Categoria();
				
				c.setIdCategoria(rs.getLong("ID_CATEGORIA"));
				c.setPrincipale(rs.getBoolean("IS_PRINCIPALE"));
				c.setNomeCategoria(rs.getString("NOME_CATEGORIA"));			
				c.setIdCategoriaPrincipale(rs.getLong("ID_CATEGORIA_PRINCIPALE"));
				c.setNomeCategoriaPrincipale(rs.getString("NOME_CATEGORIA_PRINCIPALE"));
				c.setOrdinamento(rs.getInt("ORDINAMENTO"));
				c.setIdCategoriaEbay(rs.getLong("ID_CATEGORIA_EBAY"));
				c.setIdCategoriaYatego(rs.getString("ID_CATEGORIA_YATEGO"));
				c.setIdCategoriaAmazon(rs.getLong("ID_CATEGORIA_AMAZON"));
				c.setIdCategoriaGestionale(rs.getInt("ID_CATEGORIA_GESTIONALE"));
				c.setSoloDettaglio(rs.getInt("SOLO_DETTAGLIO"));
								
				categorie.put(c.getIdCategoriaEbay(),c);			
			}
			Log.info("Mappa delle categorie caricata.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,st,null,rs);
		}
		return categorie;
	}
	
	public static Map<Integer, Categoria> getMappaCategorieGestionale(){
		Log.debug("Caricamento mappa delle categorie del gestionale...");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		Map<Integer,Categoria> categorie = null;

		try {			
			con = DataSource.getLocalConnection();
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM CATEGORIE order by nome_categoria_principale, nome_categoria");
			
			categorie = new HashMap<Integer,Categoria>();
			
			while (rs.next()){
				Categoria c = new Categoria();
				
				c.setIdCategoria(rs.getLong("ID_CATEGORIA"));
				c.setPrincipale(rs.getBoolean("IS_PRINCIPALE"));
				c.setNomeCategoria(rs.getString("NOME_CATEGORIA"));			
				c.setIdCategoriaPrincipale(rs.getLong("ID_CATEGORIA_PRINCIPALE"));
				c.setNomeCategoriaPrincipale(rs.getString("NOME_CATEGORIA_PRINCIPALE"));
				c.setOrdinamento(rs.getInt("ORDINAMENTO"));
				c.setIdCategoriaEbay(rs.getLong("ID_CATEGORIA_EBAY"));
				c.setIdCategoriaYatego(rs.getString("ID_CATEGORIA_YATEGO"));
				c.setIdCategoriaAmazon(rs.getLong("ID_CATEGORIA_AMAZON"));
				c.setIdCategoriaGestionale(rs.getInt("ID_CATEGORIA_GESTIONALE"));
				c.setSoloDettaglio(rs.getInt("SOLO_DETTAGLIO"));
								
				categorie.put(c.getIdCategoriaGestionale(),c);			
			}
			Log.debug("Mappa delle categorie del gestionale caricata.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,st,null,rs);
		}
		return categorie;
	}
	
	
	public static List<Categoria> getCategoriePrincipali(){
		Log.debug("Categorie_DAO.getCategoriePrincipali(): Cerco di ottenere la lista di categorie principali...");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		List<Categoria> categorie = null;

		try {			
			con = DataSource.getLocalConnection();
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM CATEGORIE WHERE IS_PRINCIPALE=1");
			
			categorie = new ArrayList<Categoria>();
			int i = 0;
			
			while (rs.next()){
				Categoria c = new Categoria();
				
				c.setIdCategoria(rs.getLong("ID_CATEGORIA"));
				c.setNomeCategoria(rs.getString("NOME_CATEGORIA"));
				c.setPrincipale(rs.getBoolean("IS_PRINCIPALE"));
				c.setIdCategoriaPrincipale(rs.getLong("ID_CATEGORIA_PRINCIPALE"));
				c.setNomeCategoriaPrincipale(rs.getString("NOME_CATEGORIA_PRINCIPALE"));
				c.setOrdinamento(rs.getInt("ORDINAMENTO"));
				c.setIdCategoriaEbay(rs.getLong("ID_CATEGORIA_EBAY"));
				c.setIdCategoriaYatego(rs.getString("ID_CATEGORIA_YATEGO"));
				c.setIdCategoriaAmazon(rs.getLong("ID_CATEGORIA_AMAZON"));
				c.setIdCategoriaGestionale(rs.getInt("ID_CATEGORIA_GESTIONALE"));
				c.setSoloDettaglio(rs.getInt("SOLO_DETTAGLIO"));
								
				categorie.add(c);			
				i++;
			}
			Log.debug("Categorie_DAO.getCategoriePrincipali(): "+i+" categorie principali create.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,st,null,rs);
		}
		return categorie;
	}
	
	
//	public static List<Categoria> getSottoCategorieByIdCategoriaPrincipale(int id_categoria_principale){
//		Log.debug("Categorie_DAO.getSottoCategorieByIdCategoriaPrincipale("+id_categoria_principale+"):" +
//				" Cerco di ottenere la lista di sotto-categorie...");
//		Connection con = null;
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		List<Categoria> categorie = null;
//
//		try {			
//			con = DataSource.getLocalConnection();
//			ps = con.prepareStatement("SELECT * FROM CATEGORIE WHERE ID_CATEGORIA_PRINCIPALE = ? ");
//			ps.setInt(1, id_categoria_principale);
//			
//			rs = ps.executeQuery();
//			
//			categorie = new ArrayList<Categoria>();
//			int i = 0;
//			
//			while (rs.next()){
//				Categoria c = new Categoria();
//				
//				c.setIdCategoria(rs.getInt("ID_CATEGORIA"));
//				c.setNomeCategoria(rs.getString("NOME_CATEGORIA"));
//				c.setPrincipale(rs.getBoolean("IS_PRINCIPALE"));
//				c.setIdCategoriaPrincipale(rs.getInt("ID_CATEGORIA_PRINCIPALE"));
//				c.setNomeCategoriaPrincipale(rs.getString("NOME_CATEGORIA_PRINCIPALE"));
//				c.setOrdinamento(rs.getInt("ORDINAMENTO"));
//				c.setIdCategoriaEbay(rs.getLong("ID_CATEGORIA_EBAY"));
//				c.setIdCategoriaYatego(rs.getString("ID_CATEGORIA_YATEGO"));
//				c.setIdCategoriaAmazon(rs.getLong("ID_CATEGORIA_AMAZON"));
//				c.setIdCategoriaGestionale(rs.getInt("ID_CATEGORIA_GESTIONALE"));
//				c.setSoloDettaglio(rs.getInt("SOLO_DETTAGLIO"));
//								
//				categorie.add(c);			
//				i++;
//			}
//			Log.debug("Categorie_DAO.getSottoCategorieByIdCategoriaPrincipale("+id_categoria_principale+"): "+i+" sotto-categorie create.");
//
//		} catch (Exception ex) {
//			Log.info(ex); ex.printStackTrace();
//		}
//		 finally {
//			 DataSource.closeConnections(con,null,ps,rs);
//		}
//		return categorie;
//	}
	
	public static List<Categoria> getSottoCategorieByIdCategoriaPrincipale(Connection con, long id_categoria_principale){
//		Log.debug("Categorie_DAO.getSottoCategorieByIdCategoriaPrincipale("+id_categoria_principale+"):" +
//				" Cerco di ottenere la lista di sotto-categorie...");
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Categoria> categorie = null;

		try {			
			
			ps = con.prepareStatement("SELECT * FROM CATEGORIE WHERE ID_CATEGORIA_PRINCIPALE = ? ");
			ps.setLong(1, id_categoria_principale);
			
			rs = ps.executeQuery();
			
			categorie = new ArrayList<Categoria>();
			
			while (rs.next()){
				Categoria c = new Categoria();
				
				c.setIdCategoria(rs.getLong("ID_CATEGORIA"));
				c.setNomeCategoria(rs.getString("NOME_CATEGORIA"));
				c.setPrincipale(rs.getBoolean("IS_PRINCIPALE"));
				c.setIdCategoriaPrincipale(rs.getLong("ID_CATEGORIA_PRINCIPALE"));
				c.setNomeCategoriaPrincipale(rs.getString("NOME_CATEGORIA_PRINCIPALE"));
				c.setOrdinamento(rs.getInt("ORDINAMENTO"));
				c.setIdCategoriaEbay(rs.getLong("ID_CATEGORIA_EBAY"));
				c.setIdCategoriaYatego(rs.getString("ID_CATEGORIA_YATEGO"));
				c.setIdCategoriaAmazon(rs.getLong("ID_CATEGORIA_AMAZON"));
				c.setIdCategoriaGestionale(rs.getInt("ID_CATEGORIA_GESTIONALE"));
				c.setSoloDettaglio(rs.getInt("SOLO_DETTAGLIO"));
								
				categorie.add(c);			
			}
//			Log.debug("Categorie_DAO.getSottoCategorieByIdCategoriaPrincipale("+id_categoria_principale+"): "+i+" sotto-categorie create.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 DataSource.closeStatements(null,ps,rs);
		}
		return categorie;
	}
	
	
	public static int checkLastCategoriesOrdering(long category_id, Connection con){	
		PreparedStatement ps = null;
		ResultSet rs = null;
//		Connection con = null;
		
		int order = 1;
		
		try {
//			con = DataSource.getLocalConnection();
			ps = con.prepareStatement("select max(product_ordering) ordine from pmqbpiom_jshopping_products_to_categories where category_id=?");
			ps.setLong(1, category_id);
			
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
	
	
	public static List<CategoriaEbay> getCategorieEbay(){
		Log.debug("Categorie_DAO.getCategorieEbay(): Cerco di ottenere la lista di categorie ebay...");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		List<CategoriaEbay> categorie = null;

		try {			
			con = DataSource.getLocalConnection();
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM CATEGORIE_EBAY order by level_1,level_2,level_3,level_4,level_5,level_6");
			
			categorie = new ArrayList<CategoriaEbay>();
			int i = 0;
			
			while (rs.next()){
				CategoriaEbay c = new CategoriaEbay();
				
				c.setCategory_id(rs.getLong("category_id"));
				c.setLevel_1(rs.getString("level_1"));
				if (rs.getString("level_2")!=null && !rs.getString("level_2").isEmpty()) 
					c.setLevel_2(" -> "+rs.getString("level_2"));
				else c.setLevel_2("");
				if (rs.getString("level_3")!=null && !rs.getString("level_3").isEmpty()) 
					c.setLevel_3(" -> "+rs.getString("level_3"));
				else c.setLevel_3("");
				if (rs.getString("level_4")!=null && !rs.getString("level_4").isEmpty()) 
					c.setLevel_4(" -> "+rs.getString("level_4"));
				else c.setLevel_4("");
				if (rs.getString("level_5")!=null && !rs.getString("level_5").isEmpty()) 
					c.setLevel_5(" -> "+rs.getString("level_5"));
				else c.setLevel_5("");
				if (rs.getString("level_6")!=null && !rs.getString("level_6").isEmpty()) 
					c.setLevel_6(" -> "+rs.getString("level_6"));
				else c.setLevel_6("");
				c.setParent_id(rs.getLong("parent_id"));
								
				categorie.add(c);			
				i++;
			}
			Log.debug("Caricate "+i+" categorie ebay.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,st,null,rs);
		}
		return categorie;
	}
	
	public static Map<Long, CategoriaEbay> getMappaCategorieEbay(DbTool dbt){
		Log.info("Caricamento mappa delle categorie eBay...");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		Map<Long,CategoriaEbay> categorie = null;

		try {			
			if (dbt==null){
				con = DataSource.getLocalConnection();				
			}else {
				con = dbt.getConnection();				
			}
			
			st = con.createStatement();
			
			rs = st.executeQuery("SELECT * FROM CATEGORIE_EBAY order by level_1,level_2,level_3,level_4,level_5,level_6");
			
			categorie = new HashMap<Long,CategoriaEbay>();
			
			while (rs.next()){
				CategoriaEbay c = new CategoriaEbay();
				
				c.setCategory_id(rs.getLong("category_id"));
				c.setLevel_1(rs.getString("level_1"));
				if (rs.getString("level_2")!=null && !rs.getString("level_2").isEmpty()) 
					c.setLevel_2(" -> "+rs.getString("level_2"));
				else c.setLevel_2("");
				if (rs.getString("level_3")!=null && !rs.getString("level_3").isEmpty()) 
					c.setLevel_3(" -> "+rs.getString("level_3"));
				else c.setLevel_3("");
				if (rs.getString("level_4")!=null && !rs.getString("level_4").isEmpty()) 
					c.setLevel_4(" -> "+rs.getString("level_4"));
				else c.setLevel_4("");
				if (rs.getString("level_5")!=null && !rs.getString("level_5").isEmpty()) 
					c.setLevel_5(" -> "+rs.getString("level_5"));
				else c.setLevel_5("");
				if (rs.getString("level_6")!=null && !rs.getString("level_6").isEmpty()) 
					c.setLevel_6(" -> "+rs.getString("level_6"));
				else c.setLevel_6("");
				c.setParent_id(rs.getLong("parent_id"));
								
				categorie.put(c.getCategory_id(),c);			
			}
			Log.info("Mappa delle categorie eBay caricata.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 if (dbt==null) DataSource.closeConnections(con,st,null,rs);
		}
		return categorie;
	}

	
	public static List<CategoriaEbay> getCategorieEbayLevel_1(){
		Log.debug("Cerco di ottenere la lista di categorie ebay level 1...");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		List<CategoriaEbay> categorie = null;

		try {			
			con = DataSource.getLocalConnection();
			st = con.createStatement();
			rs = st.executeQuery("SELECT level_1, category_id FROM CATEGORIE_EBAY where level_2='' order by level_1;");
			
			categorie = new ArrayList<CategoriaEbay>();
			int i = 0;
			
			while (rs.next()){
				CategoriaEbay c = new CategoriaEbay();
				
				c.setCategory_id(rs.getLong("category_id"));
				c.setLevel_1(rs.getString("level_1"));
								
				categorie.add(c);			
				i++;
			}
			Log.debug(i+" categorie ebay level 1 create.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,st,null,rs);
		}
		return categorie;
	}
	
	
	public static List<CategoriaEbay> getCategorieEbayLevel_2(long parent_id){
		Log.debug("Cerco di ottenere la lista di categorie ebay level 2...");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CategoriaEbay> categorie = null;

		try {			
			con = DataSource.getLocalConnection();
			String sql = "SELECT level_1,level_2, category_id FROM CATEGORIE_EBAY where parent_id= ? order by level_1,level_2";
			ps = con.prepareStatement(sql);
			ps.setLong(1, parent_id);
			rs = ps.executeQuery();
			
			categorie = new ArrayList<CategoriaEbay>();
			int i = 0;
			
			while (rs.next()){
				CategoriaEbay c = new CategoriaEbay();
				
				c.setCategory_id(rs.getLong("category_id"));
				c.setLevel_1(rs.getString("level_1"));
				c.setLevel_2(rs.getString("level_2"));
				c.setParent_id(parent_id);
								
				categorie.add(c);			
				i++;
			}
			Log.debug(i+" categorie ebay level 2 create.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,rs);
		}
		return categorie;
	}
	
	public static List<CategoriaEbay> getCategorieEbayLevel_3(long parent_id){
		Log.debug("Cerco di ottenere la lista di categorie ebay level 3...");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CategoriaEbay> categorie = null;

		try {			
			con = DataSource.getLocalConnection();
			String sql = "SELECT level_1,level_2,level_3,category_id FROM CATEGORIE_EBAY where parent_id= ? order by level_1,level_2,level_3";
			ps = con.prepareStatement(sql);
			ps.setLong(1, parent_id);
			rs = ps.executeQuery();
			
			categorie = new ArrayList<CategoriaEbay>();
			int i = 0;
			
			while (rs.next()){
				CategoriaEbay c = new CategoriaEbay();
				
				c.setCategory_id(rs.getLong("category_id"));
				c.setLevel_1(rs.getString("level_1"));
				c.setLevel_2(rs.getString("level_2"));
				c.setLevel_3(rs.getString("level_3"));
				c.setParent_id(parent_id);
								
				categorie.add(c);			
				i++;
			}
			Log.debug(i+" categorie ebay level 3 create.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,rs);
		}
		return categorie;
	}
	
	public static List<CategoriaEbay> getCategorieEbayLevel_4(long parent_id){
		Log.debug("Cerco di ottenere la lista di categorie ebay level 4...");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CategoriaEbay> categorie = null;

		try {			
			con = DataSource.getLocalConnection();
			String sql = "SELECT level_1,level_2,level_3,level_4,category_id FROM CATEGORIE_EBAY where parent_id= ? order by level_1,level_2,level_3,level_4";
			ps = con.prepareStatement(sql);
			ps.setLong(1, parent_id);
			rs = ps.executeQuery();
			
			categorie = new ArrayList<CategoriaEbay>();
			int i = 0;
			
			while (rs.next()){
				CategoriaEbay c = new CategoriaEbay();
				
				c.setCategory_id(rs.getLong("category_id"));
				c.setLevel_1(rs.getString("level_1"));
				c.setLevel_2(rs.getString("level_2"));
				c.setLevel_3(rs.getString("level_3"));
				c.setLevel_4(rs.getString("level_4"));
				c.setParent_id(parent_id);
								
				categorie.add(c);			
				i++;
			}
			Log.debug(i+" categorie ebay level 4 create.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,rs);
		}
		return categorie;
	}
	
	
	public static void salvaCategorieAmazon(String[][] nodi){
		Log.debug("Inizio salvataggio...");
		Connection con = null;
		PreparedStatement ps = null;

		try {			
			con = DataSource.getLocalConnection();
			
			for(int i=1;i<nodi.length;i++){
			
				String sql = "REPLACE into categorie_amazon(`id_categoria`,`nome_categoria`) values (?,?)";
				ps = con.prepareStatement(sql);
							
				if (nodi[i][0] != null){
					//if (nodi[i][0].length()>11) System.out.println("-------------> "+nodi[i][0]);
					ps.setString(1, nodi[i][0]);
					ps.setString(2, nodi[i][1]);				
					ps.executeUpdate();			
				}			
			}
			
			con.commit();
			
			Log.debug("Fine salvataggio.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { con.rollback();
			} catch (SQLException e) { Log.info(e); e.printStackTrace();	}
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,null);
		}
	}
	
	
	public static TreeNode getAlberoCategorie(){
		Log.debug("Categorie_DAO.getAlberoCategorie(): Cerco di ottenere l'albero delle categorie...");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		TreeNode root = null;

		try {			
			con = DataSource.getLocalConnection();
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM categorie where is_principale = 1 order by nome_categoria_principale asc");
			
			root = new DefaultTreeNode("root", null);  
			
			while (rs.next()){
				Categoria c = new Categoria();
				
				c.setIdCategoria(rs.getLong("ID_CATEGORIA"));
				c.setNomeCategoria(rs.getString("NOME_CATEGORIA_PRINCIPALE"));
				c.setPrincipale(rs.getBoolean("IS_PRINCIPALE"));
				c.setIdCategoriaPrincipale(rs.getLong("ID_CATEGORIA_PRINCIPALE"));
				c.setNomeCategoriaPrincipale(rs.getString("NOME_CATEGORIA_PRINCIPALE"));
				c.setOrdinamento(rs.getInt("ORDINAMENTO"));
				c.setIdCategoriaEbay(rs.getLong("ID_CATEGORIA_EBAY"));
				c.setIdCategoriaYatego(rs.getString("ID_CATEGORIA_YATEGO"));
				c.setIdCategoriaAmazon(rs.getLong("ID_CATEGORIA_AMAZON"));
				c.setIdCategoriaGestionale(rs.getInt("ID_CATEGORIA_GESTIONALE"));
				c.setSoloDettaglio(rs.getInt("SOLO_DETTAGLIO"));
				
				TreeNode tncat = new DefaultTreeNode(c, root);  
				
				List<Categoria> subCats = getSottoCategorieByIdCategoriaPrincipale(con,c.getIdCategoria());
				
				for (Categoria sc : subCats){
					
					@SuppressWarnings("unused")
					TreeNode tnsubcat = new DefaultTreeNode(sc, tncat);  
					
				}
				
			}
			Log.debug("Categorie_DAO.getNodiAmazon(): albero creato.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,st,null,rs);
		}
		return root;
	}
	
	
	
	public static List<CategoriaAmazon> getCategorieAmazon(){
		Log.debug("Cerco di ottenere la lista di categorie amazon...");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		List<CategoriaAmazon> nodi = null;

		try {			
			con = DataSource.getLocalConnection();
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM categorie_amazon order by nome_categoria asc");
			
			nodi = new ArrayList<CategoriaAmazon>();
			int i = 0;
			
			while (rs.next()){
				nodi.add(new CategoriaAmazon(rs.getLong("id_categoria"),rs.getString("nome_categoria")));
				
				i++;
			}
			Log.debug("Caricate "+i+" categorie Amazon.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,st,null,rs);
		}
		return nodi;
	}
	
	
	public static Map<Long, String> getMappaCategorieAmazon(DbTool dbt){
		Log.info("Caricamento mappa delle categorie Amazon...");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		Map<Long,String> categorie = null;

		try {			
			if (dbt==null){
				con = DataSource.getLocalConnection();				
			}else {
				con = dbt.getConnection();
			}
			
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM categorie_amazon order by nome_categoria asc");
			
			categorie = new HashMap<Long,String>();
			
			while (rs.next()){
				categorie.put(rs.getLong("id_categoria"),rs.getString("nome_categoria"));			
			}
			Log.info("Mappa delle categorie Amazon caricata.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {
			 if (dbt==null) DataSource.closeConnections(con,st,null,rs);
		}
		return categorie;
	}
	
	public static void assegnaCategoriaAmazon(Categoria c){
		Log.debug("Provo ad assegnare la categoria amazon...");
		Connection con = null;
		PreparedStatement ps = null;

		try {			
			con = DataSource.getLocalConnection();
		
			String sql = "UPDATE CATEGORIE SET `id_categoria_amazon`= ? WHERE `id_categoria`= ?";
			ps = con.prepareStatement(sql);
									
			ps.setLong(1, c.getIdCategoriaAmazon());
			ps.setLong(2, c.getIdCategoria());
			
			ps.executeUpdate();			
		
			con.commit();
			
			Log.debug("Categoria amazon assegnata.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { con.rollback();
			} catch (SQLException e) { Log.info(e); e.printStackTrace();	}
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,null);
		}
	}
	
}
