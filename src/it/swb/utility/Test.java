package it.swb.utility;

import it.swb.business.ArticoloBusiness;
import it.swb.business.CategorieBusiness;
import it.swb.database.Articolo_DAO;
import it.swb.database.DataSource;
import it.swb.database.GM_IT_DAO;
import it.swb.database.LogArticolo_DAO;
import it.swb.database.Ordine_DAO;
import it.swb.database.Variante_Articolo_DAO;
import it.swb.dbf.DbfUtil;
import it.swb.images.ImageUtil;
import it.swb.java.SdaUtility;
import it.swb.log.Log;
import it.swb.model.Articolo;
import it.swb.model.Categoria;
import it.swb.model.InfoAmazon;
import it.swb.model.InfoEbay;
import it.swb.model.LogArticolo;
import it.swb.model.Ordine;
import it.swb.model.Variante_Articolo;
import it.swb.piattaforme.amazon.EditorModelliAmazon;
import it.swb.piattaforme.ebay.EbayController;
import it.swb.piattaforme.ebay.EbayGetItem;
import it.swb.piattaforme.ebay.EbayRelistItem;
import it.swb.piattaforme.ebay.EbayStuff;
import it.swb.piattaforme.zelda.OrdiniZelda;
import it.swb.piattaforme.zelda.ZB_IT_DAO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import au.com.bytecode.opencsv.CSVReader;


public class Test {
	
	
	public static void main(String[] args){
				
		//operazioneZeroCodiciFase1();
		
		//operazioneZeroCodiciFase2();
		
		//operazioneZeroCodiciFase2eMezzo(); //ok
		
		//operazioneZeroCodiciFase3(); //ok
		
		//operazioneZeroCodiciFase3eMezzo(); //ok
		
		//operazioneZeroCodiciFase4(); //ok
		
		//operazioneZeroCodiciFase5(); //ok
		
		//operazioneZeroCodiciFase5eMezzo();
		
//		List<Articolo> art = Articolo_DAO.getArticoli("select * from articoli where codice in " +
//				"('NT030','NT85','GE04','GE07','GE11','GE12','GE13','GE15','GE01','GE02','GECGL1','GECO5','GEC01','GEC08','GE05','GE10','FA81','GEAS300','GEAF','GECPG250'" +
//				",'GEFA1','GEGE5','GERI','BP18','FA042')");
//		
//		for (Articolo a : art){
//			Articolo_DAO.setPresenzaSu(a.getCodice(),"zb",1,null);
//			Articolo_DAO.setPresenzaSu(a.getCodice(),"gm",1,null);
//		}
		
		
		
		//ricreaThumb(art);
		
		//inserisciSuGm();
		
		//trasferimentoUtentiGM();
		
//		TimerOrdini trd1 = new TimerOrdini();
//		Timer orologio = new Timer();
//		orologio.schedule(trd1, 0, 60 * 1000);
		
		
//		ordiniZelda();
		
//		String sku = "AF17-GIALLO";
//		
//		if (sku.contains("-")){
//			int x = sku.indexOf("-");
//			sku = sku.substring(0,x);
//		}
//		
//		System.out.println(sku);
		
//		ottieniEmail();
		
//		double x = 9.223372036854776E16;
//		String s = String.valueOf(x);
//		s = s.substring(0,9);
//		x = Double.valueOf(s);
//		
//		System.out.println(x);
//		
//		System.out.println(Methods.round(x,2));
		
//		ArticoloBusiness.getInstance().elaboraCodaInserzioni();
		
		
		
//		String data = "23/03/2015";
//		
//		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//		Date date;
//		try {
//			date = format.parse(data);
//			date = DateMethods.setDataConOra(date, 17, 00);
//			Timestamp t1 = new Timestamp(date.getTime());
//			
//			System.out.println(t1);
//			
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		//segnaNumeriTracciamento();
		
		//generaFileConfermaSpedizioniAmazon();
		
		//xxx();
		
		//SdaUtility.generaModelloConfermaSpedizioniAmazon(new Date());
		
		//ArticoloBusiness.getInstance().elaboraCodaInserzioni();
		
		
	
	}
	
	public static void xxx(){
		Map<String,String> m = new HashMap<String,String>();
		
		String x = "BP040 BP042-Cuore BP042-Cuore BP042-Cuore BP042-Cuore BP042-Cuore BP042-Cuore BP039 BP039 BP039 BP039 BP039 BP030-Verde_-_Estate BP030-Verde_-_Estate BP030-Verde_-_Estate BP030-Verde_-_Estate BP030-Verde_-_Estate BP030-Verde_-_Estate BP030-Verde_-_Estate BP030-Misti_-_Fantasia BP030-Misti_-_Fantasia BP030-Misti_-_Fantasia BP030-Misti_-_Fantasia BP030-Misti_-_Fantasia BP030-Misti_-_Fantasia BP030-Misti_-_Fantasia BP030-Blu-Celeste_-_Mare BP030-Blu-Celeste_-_Mare BP030-Blu-Celeste_-_Mare BP030-Blu-Celeste_-_Mare BP030-Blu-Celeste_-_Mare BP030-Blu-Celeste_-_Mare BP030-Blu-Celeste_-_Mare BP030-Fuxia BP030-Fuxia BP030-Fuxia BP030-Fuxia BP030-Fuxia BP030-Fuxia BP030-Fuxia BP030-Lilla_-_Primavera BP030-Lilla_-_Primavera BP030-Lilla_-_Primavera BP030-Lilla_-_Primavera BP030-Lilla_-_Primavera BP030-Lilla_-_Primavera BP030-Lilla_-_Primavera BP030-Rosso-_Giallo_-_Autunno BP030-Rosso-_Giallo_-_Autunno BP030-Rosso-_Giallo_-_Autunno BP030-Rosso-_Giallo_-_Autunno BP030-Rosso-_Giallo_-_Autunno BP030-Rosso-_Giallo_-_Autunno BP030-Rosso-_Giallo_-_Autunno BP030-Mix_-_Inverno BP030-Mix_-_Inverno BP030-Mix_-_Inverno BP030-Mix_-_Inverno BP030-Mix_-_Inverno BP030-Mix_-_Inverno BP030-Mix_-_Inverno ZELDA1232-Argento ZELDA1232-Argento ZELDA1232-Argento ZELDA1232-Argento ZELDA1232-Argento ZELDA1232-Argento ZELDA1232-Argento BP07-Oro BP07-Oro BP07-Oro BP07-Oro BP07-Oro BP07-Oro ZELDA1818-Argento ZELDA1818-Argento ZELDA1818-Argento ZELDA1818-Argento ZELDA1818-Argento ZELDA1818-Argento ZELDA1818-Argento ZELDA1818-Oro ZELDA1818-Oro ZELDA1818-Oro ZELDA1818-Oro ZELDA1818-Oro ZELDA1818-Oro ZELDA1818-Oro TORTA_BOMBONIERE_24 TORTA_BOMBONIERE_25 TORTA_BOMBONIERE_26 ZELDA636 ZELDA1815-Argento ZELDA1815-Argento ZELDA1815-Argento ZELDA1815-Argento ZELDA1815-Argento ZELDA1815-Argento ZELDA1815-Argento BP07-Argento BP07-Argento BP07-Argento BP07-Argento BP07-Argento BP07-Argento ZELDA1815-Oro ZELDA1815-Oro ZELDA1815-Oro ZELDA1815-Oro ZELDA1815-Oro ZELDA1815-Oro ZELDA1815-Oro ZELDA1817-Argento ZELDA1817-Argento ZELDA1817-Argento ZELDA1817-Argento ZELDA1817-Argento ZELDA1817-Argento ZELDA1817-Argento ZELDA1817-Oro ZELDA1817-Oro ZELDA1817-Oro ZELDA1817-Oro ZELDA1817-Oro ZELDA1817-Oro ZELDA1817-Oro BP007 BP036 BP036 BP036 BP036 BP036 TORTA-BOMBONIERE-104 TORTA-BOMBONIERE-102 TORTA-BOMBONIERE-103 ZELDA1796-Argento ZELDA1796-Argento ZELDA1796-Argento ZELDA1796-Argento ZELDA1796-Argento ZELDA1796-Argento ZELDA1796-Argento ZELDA1796-Oro ZELDA1796-Oro ZELDA1796-Oro ZELDA1796-Oro ZELDA1796-Oro ZELDA1796-Oro ZELDA1796-Oro ZELDA445 BP043-Ricotta_e_pera BP043-Ricotta_e_pera BP043-Ricotta_e_pera BP043-Ricotta_e_pera BP043-Ricotta_e_pera BP043-Ricotta_e_pera BP043-Ricotta_e_pera ZELDA1806-Rosa ZELDA1806-Rosa ZELDA1806-Rosa ZELDA1806-Rosa ZELDA1806-Rosa ZELDA1806-Rosa ZELDA1806-Rosa ZELDA1806-Celeste ZELDA1806-Celeste ZELDA1806-Celeste ZELDA1806-Celeste ZELDA1806-Celeste ZELDA1806-Celeste ZELDA1806-Celeste BP043-Sorbetto_di_limone BP043-Sorbetto_di_limone BP043-Sorbetto_di_limone BP043-Sorbetto_di_limone BP043-Sorbetto_di_limone BP043-Sorbetto_di_limone BP043-Sorbetto_di_limone ZELDA1800-Argento ZELDA1800-Argento ZELDA1800-Argento ZELDA1800-Argento ZELDA1800-Argento ZELDA1800-Argento ZELDA1800-Argento ZELDA1800-Oro ZELDA1800-Oro ZELDA1800-Oro ZELDA1800-Oro ZELDA1800-Oro ZELDA1800-Oro ZELDA1800-Oro ZELDA1781-Rosa ZELDA1781-Rosa ZELDA1781-Rosa ZELDA1781-Rosa ZELDA1781-Rosa ZELDA1781-Rosa ZELDA1781-Rosa BP043-Wafer BP043-Wafer BP043-Wafer BP043-Wafer BP043-Wafer BP043-Wafer BP043-Wafer ZELDA1781-Celeste ZELDA1781-Celeste ZELDA1781-Celeste ZELDA1781-Celeste ZELDA1781-Celeste ZELDA1781-Celeste ZELDA1781-Celeste ZELDA1625-Angelo ZELDA1625-Angelo ZELDA1625-Angelo ZELDA1625-Angelo ZELDA1625-Angelo ZELDA1625-Angelo ZELDA1625-Angelo ZELDA1625-Cuore ZELDA1625-Cuore ZELDA1625-Cuore ZELDA1625-Cuore ZELDA1625-Cuore ZELDA1625-Cuore ZELDA1625-Cuore BP043-Nut BP043-Nut BP043-Nut BP043-Nut BP043-Nut BP043-Nut BP043-Nut BP034-Nut_(Nocciole) BP034-Nut_(Nocciole) BP034-Nut_(Nocciole) BP034-Nut_(Nocciole) BP034-Nut_(Nocciole) BP034-Nut_(Nocciole) BP034-Nut_(Nocciole) BP034-Cocco BP034-Cocco BP034-Cocco BP034-Cocco BP034-Cocco BP034-Cocco BP034-Cocco BP034-Mandorla BP034-Mandorla BP034-Mandorla BP034-Mandorla BP034-Mandorla BP034-Mandorla BP034-Mandorla BP034-Tiramisxf9 BP034-Tiramisù BP034-Tiramisù BP034-Tiramisù BP034-Tiramisù BP034-Tiramisù BP034-Tiramisù BP19-Cuori_Bianchi BP19-Cuori_Bianchi BP19-Cuori_Bianchi BP19-Cuori_Bianchi BP19-Cuori_Bianchi BP19-Cuori_Bianchi BP19-Lenti_Colorate BP19-Lenti_Colorate BP19-Lenti_Colorate BP19-Lenti_Colorate BP19-Lenti_Colorate BP19-Lenti_Colorate BP020-Rosso BP020-Rosso BP020-Rosso BP020-Rosso BP020-Rosso BP020-Rosso BP020-Rosso BP020-Rosa BP020-Rosa BP020-Rosa BP020-Rosa BP020-Rosa BP020-Rosa BP020-Rosa BP043-Sfumato_verde BP043-Sfumato_verde BP043-Sfumato_verde BP043-Sfumato_verde BP043-Sfumato_verde BP043-Sfumato_verde BP043-Sfumato_verde BP020-Celeste BP020-Celeste BP020-Celeste BP020-Celeste BP020-Celeste BP020-Celeste BP020-Celeste BP020-Bianco BP020-Bianco BP020-Bianco BP020-Bianco BP020-Bianco BP020-Bianco BP020-Bianco BP13 BP002 BP002 BP002 BP002 BP002 BP043-Sfumato_azzurro BP043-Sfumato_azzurro BP043-Sfumato_azzurro BP043-Sfumato_azzurro BP043-Sfumato_azzurro BP043-Sfumato_azzurro BP043-Sfumato_azzurro BP008 BP008 BP008 BP008 BP008 BP009 BP009 BP009 BP009 BP009 BP011-Banana BP011-Banana BP011-Banana BP011-Banana BP011-Banana BP011-Banana BP011-Cocco BP011-Cocco BP011-Cocco BP011-Cocco BP011-Cocco BP011-Cocco BP011-Cioccolato_al_Latte BP011-Cioccolato_al_Latte BP011-Cioccolato_al_Latte BP011-Cioccolato_al_Latte BP011-Cioccolato_al_Latte BP011-Cioccolato_al_Latte BP011-Liquirizia_e_Menta BP011-Liquirizia_e_Menta BP011-Liquirizia_e_Menta BP011-Liquirizia_e_Menta BP011-Liquirizia_e_Menta BP011-Liquirizia_e_Menta BP011-Pistacchio BP011-Pistacchio BP011-Pistacchio BP011-Pistacchio BP011-Pistacchio BP011-Pistacchio BP011-Ricotta_e_Pera BP011-Ricotta_e_Pera BP011-Ricotta_e_Pera BP011-Ricotta_e_Pera BP011-Ricotta_e_Pera BP011-Ricotta_e_Pera BP043-Sfumato_rosa BP043-Sfumato_rosa BP043-Sfumato_rosa BP043-Sfumato_rosa BP043-Sfumato_rosa BP043-Sfumato_rosa BP043-Sfumato_rosa BP011-Wafer BP011-Wafer BP011-Wafer BP011-Wafer BP011-Wafer BP011-Wafer BP011-Nut BP011-Nut BP011-Nut BP011-Nut BP011-Nut BP011-Nut BP011-Panna_e_Cioccolato BP011-Panna_e_Cioccolato BP011-Panna_e_Cioccolato BP011-Panna_e_Cioccolato BP011-Panna_e_Cioccolato BP011-Panna_e_Cioccolato BP011-Mix_Colorato BP011-Mix_Colorato BP011-Mix_Colorato BP011-Mix_Colorato BP011-Mix_Colorato BP011-Mix_Colorato BP011-Tiramisxf9 BP011-Tiramisù BP011-Tiramisù BP011-Tiramisù BP011-Tiramisù BP011-Tiramisù BP012 BP012 BP012 BP012 BP012 BP043-Sfumato_arancione BP043-Sfumato_arancione BP043-Sfumato_arancione BP043-Sfumato_arancione BP043-Sfumato_arancione BP043-Sfumato_arancione BP043-Sfumato_arancione BP015-Celeste BP015-Celeste BP015-Celeste BP015-Celeste BP015-Celeste BP015-Celeste BP015-Celeste BP015-Rosa BP015-Rosa BP015-Rosa BP015-Rosa BP015-Rosa BP015-Rosa BP015-Rosa BP015-Rosso BP015-Rosso BP015-Rosso BP015-Rosso BP015-Rosso BP015-Rosso BP015-Rosso BP015-Tiffany BP015-Tiffany BP015-Tiffany BP015-Tiffany BP015-Tiffany BP015-Tiffany BP015-Tiffany BP017 ZELDA1777-Lilla ZELDA1777-Lilla ZELDA1777-Lilla ZELDA1777-Lilla ZELDA1777-Lilla ZELDA1777-Lilla ZELDA1777-Lilla ZELDA1777-Panna ZELDA1777-Panna ZELDA1777-Panna ZELDA1777-Panna ZELDA1777-Panna ZELDA1777-Panna ZELDA1777-Panna ZELDA1622 ZELDA1683-Rosa ZELDA1683-Rosa ZELDA1683-Rosa ZELDA1683-Rosa ZELDA1683-Rosa ZELDA1683-Rosa ZELDA1683-Rosa ZELDA1683-Celeste ZELDA1683-Celeste ZELDA1683-Celeste ZELDA1683-Celeste ZELDA1683-Celeste ZELDA1683-Celeste ZELDA1683-Celeste TORTA-BOMBONIERE-101 TORTA-BOMBONIERE-100 ZELDA1681-Rosso ZELDA1681-Rosso ZELDA1681-Rosso ZELDA1681-Rosso ZELDA1681-Rosso ZELDA1681-Rosso ZELDA1681-Rosso ZELDA1681-Blu ZELDA1681-Blu ZELDA1681-Blu ZELDA1681-Blu ZELDA1681-Blu ZELDA1681-Blu ZELDA1681-Blu ZELDA1255-Celeste ZELDA1255-Celeste ZELDA1255-Celeste ZELDA1255-Celeste ZELDA1255-Celeste ZELDA1255-Bianco ZELDA1255-Bianco ZELDA1255-Bianco ZELDA1255-Bianco ZELDA1255-Bianco ZELDA1255-Rosa ZELDA1255-Rosa ZELDA1255-Rosa ZELDA1255-Rosa ZELDA1255-Rosa ZELDA1647 ZELDA1632 ZELDA1637 ZELDA1646 TORTA-BOMBONIERE-99 ZELDA1614 ZELDA1624 ZELDA1627 ZELDA1568 ZELDA1577 ZELDA1586 ZELDA1561 ZELDA1570 ZELDA1571 ZELDA1558 TORTA-BOMBONIERE-94 TORTA-BOMBONIERE-96 TORTA-BOMBONIERE-95 TORTA-BOMBONIERE-91 TORTA-BOMBONIERE-92 TORTA-BOMBONIERE-93 ZELDA1270 ZELDA1520 ZELDA1521 ZELDA1353 ZELDA1357 ZELDA1489 ZELDA1231 ZELDA1251 TORTA-BOMBONIERE-56 TORTA-BOMBONIERE-36 TORTA-BOMBONIERE-27 ZELDA1051 ZELDA1262 ZELDA1896";
		
		String[] xx = x.split(" ");
		
		for (int i=0; i<xx.length;i++){
			String c = xx[i];
			if (c.contains("-")){
				int a = c.indexOf("-", 0);
				c = c.substring(0,a);
			}
			m.put(c, c);
		}
		
		Set<String> set = m.keySet();
		
		for (String s : set)
			System.out.println("'"+s+"',");
	}
	

	
	public static void ottieniEmail(){
		String filePath = "D:\\estrazione3_vivai_lazio.xls";
		int i = 0;
		int j = 0;
		
		try{
			File file = new File(filePath); 
			FileInputStream fis;
			
			fis = new FileInputStream(file);

			HSSFWorkbook wb = new HSSFWorkbook(fis); 
			HSSFSheet st = wb.getSheet("Indirizzi"); 
			
			Map<String,String> indirizzo;
			
			List<Map<String,String>> indirizzi = new ArrayList<Map<String,String>>();
			
			
			for (i=1; i<=st.getLastRowNum()-1; i++){
				
				HSSFRow row = st.getRow(i); 
				
				indirizzo = new HashMap<String,String>();
				
				for (j=1; j<=row.getLastCellNum()-1; j++){
					
					HSSFCell cell = row.getCell(j);
					
					String value = cell.getStringCellValue();
					
					switch (j) {
					case 1:
						indirizzo.put("categoria", value);
						break;
					case 2:
						indirizzo.put("nominativo", value);
						break;
					case 3:
						indirizzo.put("indirizzo", value);
						break;
					case 4:
						indirizzo.put("cap", value);
						break;
					case 5:
						indirizzo.put("localita", value);
						break;
					case 6:
						indirizzo.put("provincia", value);
						break;
					case 7:
						indirizzo.put("telefono", value);
						break;
					case 8:
						indirizzo.put("fax", value);
						break;
					case 9:
						indirizzo.put("email", value.toLowerCase());
						break;
					case 10:
						indirizzo.put("sito", value.toLowerCase());
						break;	
					
					}
				}
				
				indirizzi.add(indirizzo);
			}
			
			System.out.println(indirizzi.size());
			
			salvaEmail(indirizzi);
			
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("i = "+i);
			System.out.println("j = "+j);
		}
		
	}
	
	public static void salvaEmail(List<Map<String,String>> indirizzi){
		Connection con = null;
		PreparedStatement ps = null;
		
		try{
			con = DataSource.getTestConnection();
			
			String query = "insert into estrazioni (nominativo,categoria,indirizzo,localita,cap,provincia,telefono,fax,email,sito) " +
					"values (?,?,?,?,?,?,?,?,?,?) on duplicate key update email = email;";
			
			for (Map<String,String> indirizzo : indirizzi) {
			
				ps = con.prepareStatement(query);
				ps.setString(1, indirizzo.get("nominativo"));
				ps.setString(2, indirizzo.get("categoria"));
				ps.setString(3, indirizzo.get("indirizzo"));
				ps.setString(4, indirizzo.get("localita"));
				ps.setString(5, indirizzo.get("cap"));
				ps.setString(6, indirizzo.get("provincia"));
				ps.setString(7, indirizzo.get("telefono"));
				ps.setString(8, indirizzo.get("fax"));
				ps.setString(9, indirizzo.get("email"));
				ps.setString(10, indirizzo.get("sito"));
			
				ps.executeUpdate();
			}
			
			con.commit();
			
		}catch(Exception e){
			e.printStackTrace();
		} finally { 
			DataSource.closeConnections(con, null, ps, null); 
		}
		
	}
	
	public static void ordiniZelda(){
		
		Date dataDa = DateMethods.sottraiGiorniAData(DateMethods.oraDelleStreghe(new Date()), 2);
		Date dataA = DateMethods.ventitreCinquantanove(new Date());
		
		List<Ordine> ordini = OrdiniZelda.getOrdini(dataDa,dataA,null);
		
		Ordine_DAO.elaboraOrdini(ordini);
	}
	
	public static void trasferimentoUtentiGM(){
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			con = DataSource.getGMConnection();
			String query = "select * from customer_ajshop";
			ps = con.prepareStatement(query);
			
			rs = ps.executeQuery();
			
			query = "INSERT INTO `customer` (`customer_id`, `store_id`, `firstname`, `lastname`, `email`, `telephone`, `fax`, `password`," +
					" `salt`, `cart`, `wishlist`, `newsletter`, `address_id`, `customer_group_id`, `ip`, `status`, `approved`, `token`, `date_added`)" +
					" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE customer_id=customer_id;";
			
			String query2 = "INSERT INTO `address` (`address_id`, `customer_id`, `firstname`, `lastname`, `company`, `company_id`, `tax_id`," +
					" `address_1`, `address_2`, `city`, `postcode`, `country_id`, `zone_id`)" +
					" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE customer_id=customer_id;  ";
			
			
			while (rs.next()){
				ps = con.prepareStatement(query);
				
				ps.setLong(1, rs.getInt("user_id")); //customer_id
				ps.setLong(2, 0); //store_id
				ps.setString(3, rs.getString("f_name"));
				ps.setString(4, rs.getString("l_name"));
				ps.setString(5, rs.getString("email"));
				ps.setString(6, rs.getString("phone"));
				ps.setString(7, rs.getString("fax"));
				ps.setString(8, "password");
				ps.setString(9, "salt");
				ps.setString(10, "a:0:{}");
				ps.setString(11, "");
				ps.setInt(12, 1); 			//newsletter
				
				ps.setLong(13, rs.getInt("user_id")); //address_id
				
				int customer_group_id = 0;
				if (rs.getInt("usergroup_id")==1) customer_group_id = 6;
				if (rs.getInt("usergroup_id")==2) customer_group_id = 2;
				if (rs.getInt("usergroup_id")==3) customer_group_id = 5;
				if (rs.getInt("usergroup_id")==4) customer_group_id = 6;
				if (rs.getInt("usergroup_id")==5) customer_group_id = 1;
				if (rs.getInt("usergroup_id")==6) customer_group_id = 4;
				if (rs.getInt("usergroup_id")==7) customer_group_id = 3;
				
				ps.setInt(14, customer_group_id); //customer_groud_id
				ps.setString(15, ""); 				//ip
				ps.setInt(16, 1); 					//status
				ps.setInt(17, 1);				//approved
				ps.setString(18, "");			//token
				ps.setDate(19, new java.sql.Date(Calendar.getInstance().getTimeInMillis()));			//date_added
				
				ps.executeUpdate();
				
				ps = con.prepareStatement(query2);
				
				ps.setLong(1, rs.getInt("user_id")); //address_id
				ps.setLong(2, rs.getInt("user_id")); //customer_id
				ps.setString(3, rs.getString("f_name")); //firstname
				ps.setString(4, rs.getString("l_name")); //lastname
				String company = rs.getString("firma_name");
				if (company.length()>32) company = company.substring(0,32);
				ps.setString(5, company); //company
				ps.setString(6, rs.getString("tax_number")); //company_id
				ps.setString(7, rs.getString("ext_field_1")); //tax_id
				ps.setString(8, rs.getString("street")); //address_1
				ps.setString(9, ""); //address_2
				ps.setString(10, rs.getString("city")); //city
				String zip = rs.getString("zip");
				if (zip.length()>10) zip = zip.substring(0,10);
				ps.setString(11, zip); //postcode
				String country = rs.getString("country");
				if (country==null) country = "105";
				ps.setString(12, country); 	 //country_id			
				ps.setInt(13, 0); //zone_id
				
				ps.executeUpdate();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		} finally { DataSource.closeConnections(con, null, ps, rs); }
	}
	
	public static void operazioneZeroCodiciFase5eMezzo(){
		Connection con = null;
		PreparedStatement ps = null;
		
		try{
			con = DataSource.getTestConnection();
			
			String query = "update inserzioni_ebay set id_ebay=?, id_categoria=? where titolo_inserzione=? and prezzo_dettaglio=?";
			ps = con.prepareStatement(query);
			
			List<Articolo> art = leggiCsvEbay();
			
			int i = 0; //senza sku
			
			Map<String,Boolean> cosaScaricare = new HashMap<String,Boolean>();
			cosaScaricare.put("descrizione", false);
			cosaScaricare.put("titolo", false);
			cosaScaricare.put("categorie", false);
			cosaScaricare.put("categorie_negozio", true);
			cosaScaricare.put("prezzo", false);
			cosaScaricare.put("varianti", false);
			
			for (Articolo a : art){
				if (a.getCodice() ==null || a.getCodice().isEmpty()){
					
					Map<String,Object> map = EbayGetItem.scaricaInserzione(a.getIdEbay(), cosaScaricare);
					
					InfoEbay ie = (InfoEbay) map.get("info_ebay");
					
					System.out.println(ie.getIdCategoriaNegozio1());
					
					i++;
					
					System.out.println(i);
					ps.setString(1, a.getIdEbay());
					ps.setLong(2, ie.getIdCategoriaNegozio1());
					ps.setString(3, a.getTitoloInserzione());
					ps.setDouble(4, a.getPrezzoDettaglio());
					
					try{
						System.out.println("Aggiornato: "+ps.executeUpdate());	
						con.commit();
					} catch(SQLException se){ System.out.println(se.getMessage()); }
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	/*** Elaborazione dati nel DB Test
	*
	*	Cancello le inserzioni elaborate nella fase 3 e 4
	*
	*/
	public static void operazioneZeroCodiciFase5(){
		Connection con = null;		
		PreparedStatement ps = null;
		ResultSet rs = null;
	
		try{
			con = DataSource.getTestConnection();
			
			String query = "select * from inserzioni_ebay where id_video=1";
			ps = con.prepareStatement(query);
			
			rs = ps.executeQuery();
			
			query = "delete from inserzioni_ebay where id_articolo=?";
			ps = con.prepareStatement(query);
			
			while (rs.next()){
				ps.setInt(1, rs.getInt("id_articolo"));
				ps.executeUpdate();
			}
			
			con.commit();
			
		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,null);
		}		
	}
	
	
	/*** Elaborazione dati nel DB Test
	*
	*	Qui chiudo le inserzioni elaborate nella fase 3 e le riapro con template nuovo e senza scadenza
	*
	*/
	public static void operazioneZeroCodiciFase4(){
		Connection con = null;		
		PreparedStatement ps = null;
		Connection con1 = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
	
		try{
			con = DataSource.getTestConnection();
			con1 = DataSource.getLocalConnection();
			
			//ottengo l'elenco di codici
			String query = "select *,count(*) as tot from inserzioni_ebay where id_articolo>=14500 and id_articolo <15000 group by codice having tot=1 order by id_articolo";
			ps = con.prepareStatement(query);
			
			Map<String, Articolo> mappaArticoli = Articolo_DAO.getMappaArticoliCompleta();
			
			rs = ps.executeQuery();
			
			query = "update inserzioni_ebay set id_video=1 where codice=?";
			ps = con.prepareStatement(query);
			
			String query1 = "update articoli set id_ebay=? where codice=?";
			ps1 = con1.prepareStatement(query1);
			
			int i = 1;
			
			while (rs.next()){
				
				System.out.println(i);
				i++;
				
				if (mappaArticoli.containsKey(rs.getString("codice"))){
					
					System.out.println("Aggiorno "+rs.getString("codice")+ " - ID: " + rs.getString("id_articolo")+" - EB: "+rs.getString("id_ebay"));

					Articolo a = mappaArticoli.get(rs.getString("codice"));
					
					int quantita = a.getQuantitaMagazzino(); 
					
//					if (a.getVarianti()!=null && a.getVarianti().isEmpty()){
//						
//					}
					boolean chiusa = false;
					boolean template = false;
					
					//se ottengo l'errore qui evito di chiudere l'inserzione
					String desc = EditorDescrizioni.creaDescrizioneEbay(a);
					if (desc!=null && !desc.isEmpty()){
						a.getInfoEbay().setDescrizioneEbay(desc);
						template = true;
						chiusa = EbayRelistItem.endItem(rs.getString("id_ebay"));
					}
					
					String query2 = "update articoli set id_ebay='', presente_su_ebay=0 where codice=?";
					ps2 = con1.prepareStatement(query2);
					
					if (!template) System.out.println("Non è possibile creare l'inserzione per mancanza di informazioni");
					else if (!chiusa && quantita<=0){
						System.out.println("L'inserzione era già chiusa e con quantità<=0");
						ps2.setString(1, a.getCodice());						
						ps2.executeUpdate();
						
						con1.commit();
					}
					else {
						try{
							if (a.getQuantitaMagazzino()<=0) a.setQuantitaMagazzino(20);
							
							String x[] = EbayController.creaInserzione(a);
							
							String nuovoid = x[1];
							
							if (nuovoid.length()<15){
								
								System.out.println("Vecchia inserzione: www.ebay.it/itm/"+a.getIdEbay());
								System.out.println("Nuova inserzione: www.ebay.it/itm/"+nuovoid);
							
								ps1.setString(1, nuovoid);
								ps1.setString(2, a.getCodice());
								
								ps1.executeUpdate();
								
								ps.setString(1, a.getCodice());
								ps.executeUpdate();
								
								con1.commit();
								con.commit();
							}
						}
						catch(Exception ex){
							ex.printStackTrace();
						}
						
					}
				}				
				
				System.out.println();
			}
			
//			con1.commit();
//			con.commit();
			
		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,null);
			 //DataSource.closeConnections(con1,null,ps1,null);
		}		
	}
	
	/*** 
	*
	*	Devo aggiornare gli id delle inserzioni perché sono scaduti
	*
	*/
	public static void operazioneZeroCodiciFase3eMezzo(){
		Connection con = null;
		PreparedStatement ps = null;
		
		try{
			con = DataSource.getTestConnection();
			
			String query = "update inserzioni_ebay set id_ebay=? where titolo_inserzione=? and prezzo_dettaglio=?";
			ps = con.prepareStatement(query);
			
			List<Articolo> art = leggiCsvEbay();
			
			int i = 0; //senza sku
			
			for (Articolo a : art){
				if (a.getCodice() ==null || a.getCodice().isEmpty()){
					i++;
					System.out.println(i);
					ps.setString(1, a.getIdEbay());
					ps.setString(2, a.getTitoloInserzione());
					ps.setDouble(3, a.getPrezzoDettaglio());
					//ps.setInt(4, a.getQuantitaEffettiva());
					
					try{
						System.out.println("Aggiornato: "+ps.executeUpdate());		
					} catch(SQLException se){ System.out.println(se.getMessage()); }
				}
			}
			con.commit();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/*** Elaborazione dati nel DB Test
	*
	*	Qui aggiorno le informazioni mancanti degli articoli con quelle ottenute dalle inserzioni (solo inserzioni non multiple)
	*
	*/
	public static void operazioneZeroCodiciFase3(){
		Connection con = null;		
		PreparedStatement ps = null;
		Connection con1 = null;
		PreparedStatement ps1 = null;
		ResultSet rs = null;
	
		try{
			con = DataSource.getTestConnection();
			con1 = DataSource.getLocalConnection();
			
			//ottengo l'elenco di codici
			String query = "select *,count(*) as tot from inserzioni_ebay group by codice having tot=1";
			ps = con.prepareStatement(query);
			
			Map<String, Articolo> mappaArticoli = Articolo_DAO.getMappaArticoliCompleta();
			
			rs = ps.executeQuery();
			
			query = "update articoli set descrizione=?,quantita_inserzione=?,dimensioni=?," +
					"immagine1=?,immagine2=?,immagine3=?,immagine4=?,immagine5=?," +
					"titolo_inserzione=?,id_categoria_ebay_1=?,id_categoria_ebay_2=? where codice=?";
			ps1 = con1.prepareStatement(query);
			
			int i = 1;
			
			Map<String,List<Variante_Articolo>> mapvar = getVariantiInserzioniMap();
			
			while (rs.next()){
				
				if (mappaArticoli.containsKey(rs.getString("codice"))){
					boolean aggiornare = false;
					
					Articolo a = mappaArticoli.get(rs.getString("codice"));
					
					if (a.getDescrizione()==null || a.getDescrizione().isEmpty()) {
						a.setDescrizione(rs.getString("descrizione"));
						aggiornare = true;
					}
					
					if (a.getQuantitaInserzione()==null || a.getQuantitaInserzione().isEmpty()) {
						a.setQuantitaInserzione(rs.getString("quantita_inserzione"));
						aggiornare = true;
					}
					
					if (a.getDimensioni()==null || a.getDimensioni().isEmpty()) {
						a.setDimensioni(rs.getString("dimensioni"));
						aggiornare = true;
					}
					
					if (a.getImmagine1()==null || a.getImmagine1().isEmpty()) {
						a.setImmagine1(rs.getString("immagine1"));
						aggiornare = true;
					}
					
					if (a.getImmagine2()==null || a.getImmagine2().isEmpty()) {
						a.setImmagine2(rs.getString("immagine2"));
						aggiornare = true;
					}
					
					if (a.getImmagine3()==null || a.getImmagine3().isEmpty()) {
						a.setImmagine3(rs.getString("immagine3"));
						aggiornare = true;
					}
					
					if (a.getImmagine4()==null || a.getImmagine4().isEmpty()) {
						a.setImmagine4(rs.getString("immagine4"));
						aggiornare = true;
					}
					
					if (a.getImmagine5()==null || a.getImmagine5().isEmpty()) {
						a.setImmagine5(rs.getString("immagine5"));
						aggiornare = true;
					}
					
					if (a.getTitoloInserzione()==null || a.getTitoloInserzione().isEmpty()) {
						a.setTitoloInserzione(rs.getString("titolo_inserzione"));
						aggiornare = true;
					}
					
					if (a.getInfoEbay()==null){
						aggiornare = true;
						InfoEbay ie = new InfoEbay();
						
						ie.setTitoloInserzione(rs.getString("titolo_inserzione"));
						ie.setIdCategoria1(rs.getString("ID_CATEGORIA_EBAY_1"));
						if(rs.getString("ID_CATEGORIA_EBAY_2")!=null)
							ie.setIdCategoria2(rs.getString("ID_CATEGORIA_EBAY_2"));
						ie.setDurataInserzione(999);
						
						a.setInfoEbay(ie);
					}
					
					//se l'inserzione ha varianti
					if (rs.getBoolean("ha_varianti")){
						System.out.println("Aggiornamento Varianti");
						
						//ottengo la lista delle varianti per quella inserzione
						List<Variante_Articolo> l = mapvar.get(a.getCodice());
						
						//se l'articolo ha varianti
						if (a.getHaVarianti()==1){
							//ottengo la lista delle varianti per quell'articolo
							List<Variante_Articolo> varianti = a.getVarianti();
							
							//costuisco una mappa di varianti per quella lista
							Map<String,Variante_Articolo> mapv = new HashMap<String,Variante_Articolo>();
							for (Variante_Articolo v : varianti){
								mapv.put(v.getValore().toLowerCase(), v);
							}
							
							//confronto le varianti dell'inserzione con quelle dell'articolo
							for (Variante_Articolo v : l){
								//se la variante è contenuta
								if (mapv.containsKey(v.getValore().toLowerCase())){
									boolean aggiorna = false;
									Variante_Articolo v1 = mapv.get(v.getValore().toLowerCase());
									
									//controllo se c'è qualche campo da aggiornare
									if (!(v1.getValore().equals(v.getValore()))){
										v1.setValore(v.getValore());
										aggiorna = true;
									}
									if (!(v1.getImmagine().equals(v.getImmagine()))){
										v1.setImmagine(v.getImmagine());
										aggiorna = true;
									}
									if (v1.getQuantita()!=(v.getQuantita())){
										v1.setQuantita(v.getQuantita());
										aggiorna = true;
									}
									
									//se c'era qualche campo diverso allora aggiorno
									if (aggiorna){
										Variante_Articolo_DAO.modificaVariante(v1, a.getCodice(), con1, null);
									}
										
								}
								//altrimenti se quella variante mancava la salvo come nuova
								else Variante_Articolo_DAO.inserisciVariante(v, a.getCodice(), con1, null);
							}
						} 
						//altrimenti se l'articolo non aveva varianti salvo direttamente quelle dell'inserzione
						else {
							for (Variante_Articolo v : l){
								Variante_Articolo_DAO.inserisciVariante(v, a.getCodice(), con1, null);
							}
						}
					}
					
					if(aggiornare){
					
						ps1.setString(1, a.getDescrizione());
						ps1.setString(2, a.getQuantitaInserzione());
						ps1.setString(3, a.getDimensioni());
						ps1.setString(4, a.getImmagine1());
						ps1.setString(5, a.getImmagine2());
						ps1.setString(6, a.getImmagine3());
						ps1.setString(7, a.getImmagine4());
						ps1.setString(8, a.getImmagine5());
						ps1.setString(9, a.getTitoloInserzione());
						ps1.setString(10, a.getInfoEbay().getIdCategoria1());
						ps1.setString(11, a.getInfoEbay().getIdCategoria2());
						
						ps1.setString(12, a.getCodice());
						
						ps1.executeUpdate();
					}
					
				}				
				
				System.out.println(i);
				i++;
			}
			
			con1.commit();
			
		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,null);
			 //DataSource.closeConnections(con1,null,ps1,null);
		}		
	}
	
	
	/*** Scarico le informazioni delle varianti, che mi ero scordato di fare all'inizio */
	public static void operazioneZeroCodiciFase2eMezzo(){
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		ResultSet rs = null;
	
		try{
			con = DataSource.getTestConnection();
			
			String query = "select id_ebay,codice from inserzioni_ebay";
			ps = con.prepareStatement(query);
			
			rs = ps.executeQuery();
			
			int i = 1;
			
			String query1 = "update inserzioni_ebay set ha_varianti=1 where id_ebay=?";
			ps = con.prepareStatement(query1);
			
			String query2 = "insert into inserzioni_ebay_varianti(codice_articolo,id_ebay,tipo,valore,immagine,quantita) values (?,?,?,?,?,?)";
			ps1 = con.prepareStatement(query2);
			
			Map<String,Boolean> cosaScaricare = new HashMap<String,Boolean>();
			cosaScaricare.put("descrizione", false);
			cosaScaricare.put("titolo", false);
			cosaScaricare.put("categorie", false);
			cosaScaricare.put("prezzo", false);
			cosaScaricare.put("varianti", true);
			
			while (rs.next()){
				
				Map<String,Object> map = EbayGetItem.scaricaInserzione(rs.getString("id_ebay"), cosaScaricare);
				
				if (map.get("varianti")!=null){
					System.out.println("Varianti trovate");
					
					@SuppressWarnings("unchecked")
					List<Variante_Articolo> varianti = (List<Variante_Articolo>)map.get("varianti");
					
					if(!varianti.isEmpty()){
						ps.setString(1, rs.getString("id_ebay"));
						ps.executeUpdate();
						
						for (Variante_Articolo v : varianti){
						
							ps1.setString(1, rs.getString("codice"));
							ps1.setString(2, rs.getString("id_ebay"));
							ps1.setString(3, v.getTipo());
							ps1.setString(4, v.getValore());
							ps1.setString(5, v.getImmagine());
							ps1.setInt(6, v.getQuantita());
							ps1.executeUpdate();
						}
						con.commit();
					}
				}
				
				System.out.println(i);
				i++;
			}
			
			//con.commit();
			
		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,rs);
		}		
	}
	
	
	
	/*** Elaborazione dati nel DB Test
	*
	*	Qui spunto la presenza su ebay delle inserzioni che hanno un codice che matcha con qualche articolo
	*
	*/
	public static void operazioneZeroCodiciFase2(){
		Connection con = null;
		Connection con1 = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		ResultSet rs = null;
	
		try{
			con = DataSource.getTestConnection();
			con1 = DataSource.getLocalConnection();
			
			//ottengo l'elenco di codici
			String query = "select codice,id_ebay from inserzioni_ebay group by codice";
			ps = con.prepareStatement(query);
			
			//Map<String, Articolo> mappaArticoli = Articolo_DAO.getMappaArticoliCompleta();
			
			rs = ps.executeQuery();
			
			int i = 1;
			
			query = "update articoli set presente_su_ebay=1, id_ebay=? where codice=?";
			ps1 = con1.prepareStatement(query);
			
			while (rs.next()){
				
				//se il codice è presente nella lista degli articoli, spunto la presenza su ebay e intanto salvo l'id inserzione attuale
								
				ps1.setString(1, rs.getString("id_ebay"));
				ps1.setString(2, rs.getString("codice"));
				
				ps1.executeUpdate();
				
				System.out.println(i);
				i++;
			}
			
			con1.commit();
			
		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,rs);
			 DataSource.closeConnections(con1,null,ps1,null);
		}		
	}
	
	/*** Scorrimento file csv, recupero informazioni da ebay e salvataggio nel DB Test */
	public static void operazioneZeroCodiciFase1(){
	
		List<Articolo> art = leggiCsvEbay();
		
		List<Articolo> consku = new ArrayList<Articolo>();
		List<Articolo> senzasku = new ArrayList<Articolo>();
		
		int i = 0; //senza sku
		int j = 0; //con sku
		
		for (Articolo a : art){
			if (a.getCodice() !=null && !a.getCodice().isEmpty()){
				consku.add(a);
				j++;
			}
			else {
				senzasku.add(a);
				i++;
			}
		}
		System.out.println(art.size());
		System.out.println("Inserzioni con SKU: "+j+"\n Inserzioni senza SKU: "+i);
		
		//spuntaSuZeusInserzioniConSku(consku); 
		
		salvaInserzioniSenzaSku(senzasku);
	}
	
	public static void salvaInserzioniSenzaSku(List<Articolo> art){
		Connection con = null;
		PreparedStatement ps = null;
	
		try{
			con = DataSource.getTestConnection();
			String query = "INSERT INTO INSERZIONI_EBAY(`codice`,`titolo_inserzione`,`id_categoria_ebay_1`,`id_categoria_ebay_2`,`prezzo_dettaglio`," +	/*5*/
							"`descrizione`,`dimensioni`,`id_ebay`,`quantita`,`quantita_inserzione`,`nome`," + /*6*/
							"`immagine1`,`immagine2`,`immagine3`,`immagine4`,`immagine5`,`ha_varianti`)"+ /*6*/
							" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";  /*sono 17*/
			ps = con.prepareStatement(query);
		/*
			 * devo scaricare la descrizione dell'inserzione. Posso farlo in due modi:
			 * 1. EbayStuff.leggiDaUrl(String urlebay);   <---- ricordarsi di ritagliare solo la porzione di codice che mi interessa
			 * 2. EbayGetItem.getItem( );				<---- magari creare un nuovo metodo con solo le cose che mi serve che faccia
			 * 
			 *  controllare quale di questi due è più veloce. 
			 *  P.S. questi inizialmente mi servono soltanto per ottenere il codice dell'articolo, se poi devo anche ricostruirlo dovrò usare getItem
			*/
			
			Map<String,Boolean> cosaScaricare = new HashMap<String,Boolean>();
			cosaScaricare.put("descrizione", true);
			cosaScaricare.put("titolo", true);
			cosaScaricare.put("categorie", true);
			cosaScaricare.put("categorie_negozio", true);
			cosaScaricare.put("prezzo", true);
			cosaScaricare.put("varianti", false);
			
//			int i = 0; //codice trovato, caso 1
//			int j = 0; //codice non trovato, caso 2
			
			int n = 1; //contatore
			
			for (Articolo a : art){		
				System.out.println(n);
				n++;
				Map<String,Object> map = EbayGetItem.scaricaInserzione(a.getIdEbay(), cosaScaricare);
				
				InfoEbay ie = (InfoEbay) map.get("info_ebay");
				
				a.setInfoEbay(ie);
				a.setQuantitaMagazzino((Integer)map.get("quantita_residua"));
				
				EbayStuff.elaboraDescrizione(a);
				
				System.out.println(a.getCodice());
				
				salvaInserzioniSuDbTest(a,con,ps);
			}
			
			
			
		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
			try { con.rollback();
			} catch (SQLException e) { 
				Log.info(ex); e.printStackTrace();	
			}
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,null);
		}		
	}
	
	public static void salvaInserzioniSuDbTest(Articolo art,Connection con,PreparedStatement ps){
		try {			
			
			ps.setString(1, art.getCodice());			
			ps.setString(2, art.getInfoEbay().getTitoloInserzione());
			ps.setString(3, art.getInfoEbay().getIdCategoria1());
			ps.setString(4, art.getInfoEbay().getIdCategoria2());
			ps.setDouble(5, art.getPrezzoDettaglio());
			
			ps.setString(6, art.getDescrizione());
			ps.setString(7, art.getDimensioni());				
			ps.setString(8, art.getIdEbay());
			ps.setInt(9, art.getQuantitaMagazzino());
			ps.setString(10, art.getQuantitaInserzione());
			
			ps.setString(11, art.getNome());
			
			
			ps.setString(12, art.getImmagine1());
			ps.setString(13, art.getImmagine2());
			ps.setString(14, art.getImmagine3());
			ps.setString(15, art.getImmagine4());
			ps.setString(16, art.getImmagine5());
			if (art.getVarianti()!=null && !art.getVarianti().isEmpty())
				ps.setInt(17, 1);
			else ps.setInt(17, 0);
			
			ps.executeUpdate();
			
			con.commit();
					
			Log.info("Salvataggio riuscito.");

		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
			try { con.rollback();
			} catch (SQLException e) { 
				Log.info(ex); e.printStackTrace();	
			}
		}
	}
	
	public static void spuntaSuZeusInserzioniConSku(List<Articolo> art){
		Connection con = null;
		PreparedStatement ps = null;
		System.out.println("Spunto su Zeus la presenza su ebay delle inserzioni con sku");
		try {			
			con = DataSource.getLocalConnection();
			
			String query = "UPDATE  `articoli` SET `presente_su_ebay`=1 WHERE `codice`=? ";
			int i = 1;
			
			for (Articolo a : art){				
					ps = con.prepareStatement(query);					
					ps.setString(1, a.getCodice());						
					ps.executeUpdate();
					System.out.println(i);
					i++;
			}
					
			con.commit();						

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { 
				con.rollback();
			} catch (SQLException e) { Log.info(e); e.printStackTrace();	}
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,null);
		}
		System.out.println("Fine spunte");
	}
	
	public static List<Articolo> leggiCsvEbay(){
		
		String filename = "D:\\FileExchange_Response_23166044.csv";
		
		CSVReader reader;
		
		List<Articolo> articoli = null;
		
//		System.out.println("start");
		try{
			
			articoli = new ArrayList<Articolo>();
			
			reader = new CSVReader(new FileReader(filename), ';');
			
			String [] nextLine = reader.readNext();
			
		    while ((nextLine = reader.readNext()) != null) {
		    	
		    	Articolo a = new Articolo();
		    	a.setIdEbay(nextLine[0].trim());
		    	a.setCodice(nextLine[1]);
		    	a.setTitoloInserzione(nextLine[13]);
		    	a.setPrezzoDettaglio(Double.valueOf(nextLine[8].replace("EUR ", "").replace(",", ".")));
		    	a.setQuantitaEffettiva(Integer.valueOf(nextLine[5]));
		    	InfoEbay ie = new InfoEbay();
		    	ie.setIdCategoria1(nextLine[15]);
		    	a.setInfoEbay(ie);
		    	
		    	articoli.add(a);		    	
		    }
		    
		    reader.close();			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
//		System.out.println("end");
		return articoli;
	}
		
	
	public static Map<String,String> getInserzioniMap(){
		Log.debug("Caricamento mappa delle inserzioni...");
		Statement st = null;
		ResultSet rs = null;
		Map<String,String> mapart = null;
		Connection con = null;

		try {	
			
			con = DataSource.getTestConnection();
			
			st = con.createStatement();
			rs = st.executeQuery("SELECT id_ebay,codice FROM inserzioni_ebay order by codice asc");
			
			mapart = new HashMap<String,String>();
			
			while (rs.next()){
				mapart.put(rs.getString("id_ebay"), rs.getString("codice"));
			}
			Log.debug("Mappa delle inserzioni caricata.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {			 
				 DataSource.closeConnections(con,st,null,rs);			 
		}
		return mapart;
	
	}
	
	public static Map<String,List<Variante_Articolo>> getVariantiInserzioniMap(){
		Log.debug("Caricamento mappa delle varianti delle inserzioni...");
		Statement st = null;
		ResultSet rs = null;
		Map<String,List<Variante_Articolo>> mapvar = null;
		Connection con = null;

		try {	
			
			con = DataSource.getTestConnection();
			
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM inserzioni_ebay_varianti");
			
			mapvar = new HashMap<String,List<Variante_Articolo>>();
			
			while (rs.next()){
				Variante_Articolo v = new Variante_Articolo();
				
				String cod = rs.getString("CODICE_ARTICOLO");
				
				v.setCodiceArticolo(cod);
				v.setIdVariante(rs.getInt("id_variante"));
				v.setImmagine(rs.getString("immagine"));
				
				String tipo = rs.getString("tipo");
				if (!tipo.equals("Colore") &&
					!tipo.equals("Tema") &&
					!tipo.equals("Gusto") &&
					!tipo.equals("Variante")){
					tipo = "Variante";
				}
					
				v.setTipo(tipo);
				v.setQuantita(rs.getInt("quantita"));
				v.setValore(rs.getString("valore"));
				v.setCodiceBarre(rs.getString("id_ebay"));
				
				if (mapvar.containsKey(cod))
					mapvar.get(cod).add(v);
				else {
					List<Variante_Articolo> l = new ArrayList<Variante_Articolo>();
					l.add(v);
					mapvar.put(cod,l);
				} 
				
			}
			
			Log.debug("Mappa delle varianti delle inserzioni caricata.");

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
		}
		 finally {			 
				 DataSource.closeConnections(con,st,null,rs);			 
		}
		return mapvar;
	
	}
	
	
	public static void inserisciVariante(Variante_Articolo v, String codice_articolo, Connection con, PreparedStatement ps){
		Log.debug("Inserimento variante nel database locale, valore: "+v.getValore()+", Codice articolo: "+codice_articolo);
		boolean closeCon = false;
		try {			
			if (con==null){
				con = DataSource.getLocalConnection();
				closeCon=true;
			}	
	
			String query = "INSERT INTO VARIANTI_ARTICOLO(`codice_articolo`,`tipo`,`valore`,`immagine`,`quantita`)" +
								" VALUES (?,?,?,?,?) ON KEY DUPLICATE UPDATE ";
			ps = con.prepareStatement(query);
			ps.setString(1, codice_articolo);	
			ps.setString(2, v.getTipo());	
			ps.setString(3, v.getValore());	
			ps.setString(4, Methods.trimAndToLower(v.getImmagine()));	
			ps.setInt(5, v.getQuantita());	
			ps.setString(6, v.getCodiceBarre());
			ps.setString(7, v.getTipoCodiceBarre());
			ps.setString(8, v.getDimensioni());
			ps.executeUpdate();
			
			LogArticolo l = new LogArticolo();
			l.setCodiceArticolo(codice_articolo);
			l.setAzione("Creazione Variante");
			l.setNote("Inserita la variante "+v.getValore());
			LogArticolo_DAO.inserisciLogArticolo(l, con, ps);
			
			if (closeCon) con.commit();
			
			Log.debug("Inserimento variante riuscito.");
						
		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { con.rollback();
			} catch (SQLException e) { Log.info(e); e.printStackTrace();	}
		}
		finally {
			 if(closeCon) DataSource.closeConnections(con,null,ps,null);
		}
	}
	
	public static void rinominaImmaginiLocali(){
		try{
			String pre = Costanti.percorsoImmaginiMedieLocale;
			File cartellaImmagini = new File(pre);
			String files[] = cartellaImmagini.list(); 
			
			for (String nome_file : files){
				//String nome_file = Costanti.percorsoImmaginiLocale+"ACCESSORI\\3ROCKS.jpg";
				File file = new File(pre+nome_file);
		
				System.out.println("Rinomino: "+nome_file);
				String new_name = nome_file.toLowerCase();
				File fileLower = new File(pre+new_name);
				file.renameTo(fileLower);
				
				if (fileLower.isDirectory()){
					System.out.println("Entro nella cartella: "+fileLower.getName());
					
					String subfiles[] = fileLower.list(); 
					
					for (String nome_subfile : subfiles){
						File subfile = new File(pre+fileLower.getName()+"\\"+nome_subfile);
						
						System.out.println("--> Rinomino: "+nome_subfile);
						String new_name_subfile = nome_subfile.toLowerCase();
						File subfileLower = new File(pre+fileLower.getName()+"\\"+new_name_subfile);
						subfile.renameTo(subfileLower);
						
						if (subfileLower.isDirectory()){
							System.out.println("--> Entro nella sottocartella: "+fileLower.getName());
							
							String subsubfiles[] = subfileLower.list(); 
							
							for (String nome_subsubfile : subsubfiles){
								File subsubfile = new File(pre+fileLower.getName()+"\\"+subfileLower.getName()+"\\"+nome_subsubfile);
								
								System.out.println("------> Rinomino: "+nome_subsubfile);
								String new_name_subsubfile = nome_subsubfile.toLowerCase();
								File subsubfileLower = new File(pre+fileLower.getName()+"\\"+subfileLower.getName()+"\\"+new_name_subsubfile);
								subsubfile.renameTo(subsubfileLower);
							}
						}
					}
				}
			}
			
		} catch (Exception e){
			e.printStackTrace();
		}

	}
	
	public static void inserisciSuZb(){
		List<Articolo> l = Articolo_DAO.getArticoli("select * from articoli where presente_su_ebay=1 and id_categoria=58");
		
		System.out.println(l.size());

		for (Articolo a : l){
			
			
			int r = ZB_IT_DAO.insertIntoProduct(a);

			if (r != 0) {				
				if (a.getImmagine1() != null && !a.getImmagine1().trim().isEmpty()){
					Methods.copiaImmaginePerZeldaBomboniere(a.getImmagine1());
				}
				if (a.getImmagine2() != null && !a.getImmagine2().trim().isEmpty()){
					Methods.copiaImmaginePerZeldaBomboniere(a.getImmagine2());
				}
				if (a.getImmagine3() != null && !a.getImmagine3().trim().isEmpty()){
					Methods.copiaImmaginePerZeldaBomboniere(a.getImmagine3());
				}
				if (a.getImmagine4() != null && !a.getImmagine4().trim().isEmpty()){
					Methods.copiaImmaginePerZeldaBomboniere(a.getImmagine4());
				}
				if (a.getImmagine5() != null && !a.getImmagine5().trim().isEmpty()){
					Methods.copiaImmaginePerZeldaBomboniere(a.getImmagine5());				
				}
				
				if (a.getVarianti()!=null && !a.getVarianti().isEmpty()){
					for (Variante_Articolo v : a.getVarianti()){
						Methods.copiaImmaginePerZeldaBomboniere(v.getImmagine());
					}
				}
		
			}
		}
	}
	
	public static void inserisciSu(List<Articolo> l){
		int i=1;
		for (Articolo a : l){
			
			System.out.println(i);
			i++;
			GM_IT_DAO.insertIntoProduct(a);
			ZB_IT_DAO.insertIntoProduct(a);

		}
	}
	
//	public static void ricreaThumb(List<Articolo> articoli){
//		FTPClient f = FTPutil.getConnection();
//		int i=1;
//		
//		for (Articolo a : articoli){
//			System.out.println(i+". ID_Articolo="+a.getIdArticolo()+", Codice: "+a.getCodice());
//			if (controlloSintassiImmagine(a.getImmagine1()))
//				FTPmethods.creaThumbnailsNew(Methods.getNomeImmagine(a.getImmagine1()),Methods.getNomeCartella(a.getImmagine1()),f);
//			if (controlloSintassiImmagine(a.getImmagine2()))
//				FTPmethods.creaThumbnailsNew(Methods.getNomeImmagine(a.getImmagine2()),Methods.getNomeCartella(a.getImmagine2()),f);
//			if (controlloSintassiImmagine(a.getImmagine3()))
//				FTPmethods.creaThumbnailsNew(Methods.getNomeImmagine(a.getImmagine3()),Methods.getNomeCartella(a.getImmagine3()),f);
//			if (controlloSintassiImmagine(a.getImmagine4()))
//				FTPmethods.creaThumbnailsNew(Methods.getNomeImmagine(a.getImmagine4()),Methods.getNomeCartella(a.getImmagine4()),f);
//			if (controlloSintassiImmagine(a.getImmagine5()))
//				FTPmethods.creaThumbnailsNew(Methods.getNomeImmagine(a.getImmagine5()),Methods.getNomeCartella(a.getImmagine5()),f);
//			
//			if (a.getVarianti()!=null && !a.getVarianti().isEmpty()){
//				for (Variante_Articolo v : a.getVarianti()){
//					if (controlloSintassiImmagine(v.getImmagine()))
//						FTPmethods.creaThumbnailsNew(Methods.getNomeImmagine(v.getImmagine()), Methods.getNomeCartella(v.getImmagine()),f);
//				}
//			}
//			i++;
//		}
//		
//		System.out.println("Fine");
//		
//		FTPutil.closeConnection(f);
//	}
	
	@SuppressWarnings("unused")
	private static boolean controlloSintassiImmagine(String s){
		if (s!=null && s.trim().length()!=0 && s.contains("/") && s.toLowerCase().contains(".jpg"))
			return true;
		else return false;
	}
	
	public static void abc(List<Articolo> list){
		Connection con = null;
		PreparedStatement ps = null;
		
		try {			
			con = DataSource.getRemoteConnection();
			
			int i = 1;
			
			for (Articolo a : list){
				
					String query = "UPDATE  `pmqbpiom_jshopping_products` SET `description_it-IT`=? WHERE `product_id`=? ";
				
					ps = con.prepareStatement(query);
					
					ps.setString(1, EditorDescrizioni.creaDescrizioneSitoGM(a));
					ps.setLong(2, a.getIdArticolo());	
					
					ps.executeUpdate();
					
					System.out.println(i);
					i++;
			}
					
			con.commit();						
					

		} catch (Exception ex) {
			Log.info(ex); ex.printStackTrace();
			try { 
				con.rollback();
			} catch (SQLException e) { Log.info(e); e.printStackTrace();	}
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,null);
		}
	}
	
	public static void testLeggiCodiciBarreVarianti(){
		Map<String,List<String>> m = DbfUtil.syncCodiciBarreVarianti("Z:\\DB\\001\\ARTBAR.DBF");		
		
		//Articolo_DAO.salvaCodiciBarreVarianti(m);
		
		
		List<String> l = new ArrayList<String>(m.keySet());		
		int nvar = 0;
		int max = 0;
		String cmax = "";
		
		for (String s : l){
			//System.out.print(s+": ");
			
			int att = 0;			
			
			for (String d : m.get(s)){
				att++;
				nvar++;
				d = d+"";
			}
			
			if (att>max){
				max=att;
				cmax = s;
			}
			att = 0;
		}
		
		System.out.println(l.size()+", "+nvar+", numero max varianti: "+max+" per "+cmax);
	}
	
	
	public static void stampaOrdine(Ordine o){
		
		System.out.println("Ordine: "+o.getPiattaforma()+" "+o.getIdOrdinePiattaforma()+" del "+o.getDataAcquisto());
		System.out.println("Totale: "+o.getTotale()+" "+o.getValuta()+" per "+o.getQuantitaAcquistata()+" articoli");
		System.out.println("Spese di spedizione: "+o.getCostoSpedizione());
		
		System.out.println("Cliente: "+o.getCliente().getNome());
		
		for (Articolo a : o.getArticoli()){
			System.out.println("SKU: "+a.getCodice());
			System.out.println("Inserzione: "+a.getNome());
			if (a.getNote()!=null && !a.getNote().isEmpty()) System.out.println("Variante: "+a.getNote());
			System.out.println("Quantità: "+a.getQuantitaMagazzino());
			System.out.println("Costo totale: "+a.getPrezzoDettaglio());
			
		}
		
		System.out.println("");
		System.out.println("");
		
	}
	
	
	@SuppressWarnings("unused")
	private static void creaThumb(String nomeImmagine){
		Methods.controllaSeCartellaEsiste("D:\\dev\\EasyPHP-12.1\\www\\opencart\\image\\cache\\articoli\\"+nomeImmagine);
		
		ImageUtil.creaThumb(74, 74, "D:\\immagini\\articoli\\"+nomeImmagine, 
				"D:\\dev\\EasyPHP-12.1\\www\\opencart\\image\\cache\\articoli\\"+nomeImmagine.toUpperCase().replace(".JPG", "")+"-74x74.JPG");
		ImageUtil.creaThumb(80, 80, "D:\\immagini\\articoli\\"+nomeImmagine, 
				"D:\\dev\\EasyPHP-12.1\\www\\opencart\\image\\cache\\articoli\\"+nomeImmagine.toUpperCase().replace(".JPG", "")+"-80x80.JPG");
		ImageUtil.creaThumb(228, 228, "D:\\immagini\\articoli\\"+nomeImmagine, 
				"D:\\dev\\EasyPHP-12.1\\www\\opencart\\image\\cache\\articoli\\"+nomeImmagine.toUpperCase().replace(".JPG", "")+"-228x228.JPG");
		ImageUtil.creaThumb(700, 700, "D:\\immagini\\articoli\\"+nomeImmagine, 
				"D:\\dev\\EasyPHP-12.1\\www\\opencart\\image\\cache\\articoli\\"+nomeImmagine.toUpperCase().replace(".JPG", "")+"-700x700.JPG");	
	}
	
	
//	public static void test1(){
//		
//		List<Categoria> cats = Categoria_DAO.getCategorie();
//		
//		Connection con = null;
//		PreparedStatement ps = null;
//		
//		try{
//			
//			con = getOpenCartConnection();
//			
//			for (Categoria c : cats){
//				ps = con.prepareStatement("INSERT INTO category values (?,?,?,?,?,?,?,?,?)");
//				
//				ps.setInt(1, c.getIdCategoria());
//				ps.setString(2, "data/demo/");
//				ps.setInt(3, c.getIdCategoriaPrincipale());
//				ps.setInt(4, 0);
//				ps.setInt(5, 1);
//				ps.setInt(6, c.getOrdinamento());
//				ps.setInt(7, 1);
//				ps.setDate(8, new java.sql.Date(new java.util.Date().getTime()));
//				ps.setDate(9, new java.sql.Date(new java.util.Date().getTime()));
//				
//				ps.executeUpdate();
//				
//				ps = con.prepareStatement("insert into category_description values (?,?,?,?,?,?)");
//				
//				ps.setInt(1, c.getIdCategoria());
//				ps.setInt(2, 1);
//				ps.setString(3, c.getNomeCategoria().replace("---", ""));
//				ps.setString(4, "");
//				ps.setString(5, c.getNomeCategoria().replace("---", ""));
//				ps.setString(6, c.getNomeCategoria().replace("---", ""));
//				
//				ps.executeUpdate();
//				
//				ps = con.prepareStatement("insert into category_description values (?,?,?,?,?,?)");
//				
//				ps.setInt(1, c.getIdCategoria());
//				ps.setInt(2, 2);
//				ps.setString(3, c.getNomeCategoria().replace("---", ""));
//				ps.setString(4, "");
//				ps.setString(5, "");
//				ps.setString(6, "");
//				
//				ps.executeUpdate();
//				
//				ps = con.prepareStatement("insert into category_to_store values (?,?)");
//				
//				ps.setInt(1, c.getIdCategoria());
//				ps.setInt(2, 0);
//				
//				ps.executeUpdate();
//				
//				ps = con.prepareStatement("insert into url_alias(query,keyword) values (?,?)");
//				
//				ps.setString(1, "category_id="+c.getIdCategoria());
//				ps.setString(2, c.getNomeCategoria().toLowerCase().replace("/ ", "").replace(" ", "-").replace("---", ""));
//				
//				ps.executeUpdate();
//					
//			}
//			
//		} catch(Exception e){
//			e.printStackTrace();
//		}
//		
//	}
	
	
	public static Connection getOpenCartConnection() {
		Connection con = null;
		try {		
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Properties config = new Properties();	        
			config.load(DataSource.class.getResourceAsStream("/database.properties"));
			
			String server = config.getProperty("Local_DB_Server");
	        String port = config.getProperty("Local_DB_Port");
	        String database = "zeldabomboniere";
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
	
	
	public static void aggiungiVariante(Articolo a,Variante_Articolo v, PrintWriter pw){
		//System.out.println("Inizio a scrivere sul file");
		
		/* inizio product basic information */
		
		//(A) SKU 
		pw.print(a.getCodice()+"-"+v.getValore().trim().replace(" ", "_"));
		pw.print("	");
	    
	    //(B) CODICE BARRE
		if (a.getCodiceBarre()!=null && !a.getCodiceBarre().trim().isEmpty())
			pw.print(a.getCodiceBarre().toUpperCase().trim());
		pw.print("	");
	    
	    //(C) TIPO CODICE (EAN) 
		pw.print("EAN");
		pw.print("	");
	    
	    //(D) NOME ARTICOLO 
		pw.print(Methods.primeLettereMaiuscole(a.getNome())+" Gusto "+v.getValore());
		pw.print("	");

	    //(E) MARCA 
		pw.print("Maxtris");
		pw.print("	");

	    //(F) PRODUTTORE 
		pw.print("Prisco");
		pw.print("	");

	    //(G) DESCRIZIONE 
		pw.print(Methods.MaiuscolaDopoPunto(a.getDescrizione()));
		pw.print("	");

	    //(H) PUNTO ELENCO 1 
	    if (a.getQuantitaInserzione()!=null && !a.getQuantitaInserzione().trim().isEmpty())
	    	pw.print("Quantità inserzione: "+Methods.primeLettereMaiuscole(a.getQuantitaInserzione()));  
	    pw.print("	");

	    //(I) PUNTO ELENCO 2 
	    if (a.getDimensioni()!=null && !a.getDimensioni().trim().isEmpty()){
	    	pw.print("Dimensioni: "+Methods.primeLettereMaiuscole(a.getDimensioni()).replace("Cm", "cm")); 
		}
	    pw.print("	");

	    //(J) PUNTO ELENCO 3 
	    if (a.getInfoAmazon().getPuntoElenco3()!=null && !a.getInfoAmazon().getPuntoElenco3().trim().isEmpty()){
	    	pw.print(a.getInfoAmazon().getPuntoElenco3()); 
		}
	    pw.print("	");

	    //(K) PUNTO ELENCO 4 
	    if (a.getInfoAmazon().getPuntoElenco4()!=null && !a.getInfoAmazon().getPuntoElenco4().trim().isEmpty()){
	    	pw.print(a.getInfoAmazon().getPuntoElenco4()); 
		}
	    pw.print("	");

	    //(L) PUNTO ELENCO 5 
	    if (a.getInfoAmazon().getPuntoElenco5()!=null && !a.getInfoAmazon().getPuntoElenco5().trim().isEmpty()){
	    	pw.print(a.getInfoAmazon().getPuntoElenco5()); 
		}
	    pw.print("	");

	    //(M) NODO NAVIGAZIONE 1
//	    if (a.getInfoAmazon().getIdCategoria1()!=0)
//	    	pw.print(a.getInfoAmazon().getIdCategoria1());  
//	    else pw.print(a.getCategoria().getIdCategoriaAmazon());
//	    pw.print("	");

	    //(N) NODO NAVIGAZIONE 2 
//	    if (a.getInfoAmazon().getIdCategoria2()!=0)
//	    	pw.print(a.getInfoAmazon().getIdCategoria2());  
//	    pw.print("	");

	    //(O) TIPO PRODOTTO 
	    pw.print("FurnitureAndDecor"); 
	    pw.print("	");
	    
	    /* fine product basic information */
	    
	    /* inizio informazioni sull'immagine */
	    
	    //(BB) URL IMMAGINE PRINCIPALE
	    pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine1()); 
	    pw.print("	");
	    
	    //(BC) URL ALTRA IMMAGINE 1
	    if (a.getImmagine2()!=null && !a.getImmagine2().trim().isEmpty()){
	    	pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine2()+"");  
	    }
	    pw.print("	");
	    
	    //(BD) URL ALTRA IMMAGINE 2
	    if (a.getImmagine3()!=null && !a.getImmagine3().trim().isEmpty()){
	    	pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine3());  
	    }
	    pw.print("	");
	    
	    //(BE) URL ALTRA IMMAGINE 3
	    if (a.getImmagine4()!=null && !a.getImmagine4().trim().isEmpty()){
	    	pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine4());  
	    }
	    pw.print("	");
	    
	    //(BF) URL ALTRA IMMAGINE 4
	    if (a.getImmagine5()!=null && !a.getImmagine5().trim().isEmpty()){
	    	pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine5());  
	    }
	    pw.print("	");
	    
	    //(BG) URL ALTRA IMMAGINE 5	
	    pw.print("	");
		//(BH) URL ALTRA IMMAGINE 6
	    pw.print("	");
		//(BI) URL ALTRA IMMAGINE 7
	    pw.print("	");
		//(BJ) URL ALTRA IMMAGINE 8
	    pw.print("	");
	    
	    //(BK) URL IMMAGINE SWITCH
	    pw.print("	");
	    
	    /* fine informazioni sull'immagine */
	    
	    /* inizio informazioni legali */
	    
	    //(BL) ESCLUSIONE RESPOSABILITA'
	    if (a.getInfoAmazon().getEsclusioneResponsabilita()!=null && !a.getInfoAmazon().getEsclusioneResponsabilita().trim().isEmpty())
	    	pw.print(a.getInfoAmazon().getEsclusioneResponsabilita());
	    pw.print("	");
	    
	    //(BM) DESCRIZIONE GARANZIA VENDITORE
	    if (a.getInfoAmazon().getDescrizioneGaranziaVenditore()!=null && !a.getInfoAmazon().getDescrizioneGaranziaVenditore().trim().isEmpty())
	   	 	pw.print(a.getInfoAmazon().getDescrizioneGaranziaVenditore());
	    pw.print("	");
	    
	    //(BN) AVVERTENZE SICUREZZA
	    if (a.getInfoAmazon().getAvvertenzeSicurezza()!=null && !a.getInfoAmazon().getAvvertenzeSicurezza().trim().isEmpty())
	    	pw.print(a.getInfoAmazon().getAvvertenzeSicurezza());
	    pw.print("	");
	    
	    /* fine informazioni legali */
	    
	    //(BO) AGGIORNA O RIMUOVI
	    pw.print("	");
	    
	    /* inizio informazioni sull'offerta */
	    
	    //(BP) PREZZO
	    pw.print(String.valueOf(a.getPrezzoDettaglio()).replace(",", ".")); 
	    pw.print("	");
	    
	    //(BQ) VALUTA
	    pw.print("EUR"); 
	    pw.print("	");
	    
	    //(BR) QUANTITA'
	    if (a.getQuantitaMagazzino()>0)
	    	pw.print(a.getQuantitaMagazzino());  
	    else pw.print(20);  
	    pw.print("	");
	    
	    //(BS) CONDIZIONI
	    pw.print("New"); 
	    pw.print("	");
	    
	    //(BT) NOTA CONDIZIONI
	    if (a.getInfoAmazon().getNotaCondizioni()!=null && !a.getInfoAmazon().getNotaCondizioni().trim().isEmpty())
	    	pw.print(a.getInfoAmazon().getNotaCondizioni());
	    pw.print("	");
	    
	    //(BU) VOCE PACCHETTO QUANTITA'
	    if (a.getInfoAmazon().getVocePacchettoQuantita()!=0)
	    	pw.print(a.getInfoAmazon().getVocePacchettoQuantita());
	    pw.print("	");
	    
	    //(BV) NUMERO PEZZI
	    if (a.getInfoAmazon().getNumeroPezzi()!=0)
	    	pw.print(a.getInfoAmazon().getNumeroPezzi());
	    pw.print("	");
	    
	    //(BW) DATA LANCIO
	    pw.print("	");
	    
	    //(BX) DATA RILASCIO
	    pw.print("	");
	    
	    //(BY) TEMPI ESECUZIONE SPEDIZIONE
	    pw.print("	");
	    
	    //(BZ) DATA RIFORNIMENTO
	    pw.print("	");
	    
	    //(CA) QUANTITA' MASSIMA SPEDIZIONE CUMULATIVA
	    pw.print("100");
	    pw.print("	");
	    
	    //(CB) PAESE DI ORIGINE
	    if (a.getInfoAmazon().getPaeseOrigine()!=null && !a.getInfoAmazon().getPaeseOrigine().trim().isEmpty())
	    	pw.print(a.getInfoAmazon().getPaeseOrigine());
	    pw.print("	");
	    
	    /* fine informazioni sull'offerta */
	    
	    /* inizio dimensioni prodotto */
	    
	    //(GG) PESO SPEDIZIONE
	    pw.print("	");
	    
	    //(GH) UNITA' MISURA PESO SPEDIZIONE
	    pw.print("	");
	    
	    //(GI) LUNGHEZZA ARTICOLO
	    if (a.getInfoAmazon().getLunghezzaArticolo()!=0)
	    	pw.print(a.getInfoAmazon().getLunghezzaArticolo()); 
	    pw.print("	");
	    
	    //(GJ) ALTEZZA ARTICOLO
	    if (a.getInfoAmazon().getAltezzaArticolo()!=0)
	    	pw.print(a.getInfoAmazon().getAltezzaArticolo()); 
	    pw.print("	");
	    
	    //(GK) PESO ARTICOLO
	    if (a.getInfoAmazon().getPesoArticolo()!=0)
	    	pw.print(a.getInfoAmazon().getPesoArticolo()); 
	    pw.print("	");
	    
	    //(GL) UNITA' MISURA PESO ARTICOLO
	    if (a.getInfoAmazon().getUnitaMisuraPesoArticolo()!=null && !a.getInfoAmazon().getUnitaMisuraPesoArticolo().trim().isEmpty())
	    	pw.print(a.getInfoAmazon().getUnitaMisuraPesoArticolo()); 
	    pw.print("	");
	    
	    /* fine dimensioni prodotto */
	    
	    /* inizio informazioni sulla scoperta del prodotto */
	    
	    //(GM) CODICE ARTICOLO DEL PRODUTTORE (MFR, MANIFACTURER PART NUMBER)
	    if (a.getCodiceArticoloFornitore()!=null && !a.getCodiceArticoloFornitore().trim().isEmpty())
	    	pw.print(a.getCodiceArticoloFornitore());
	    else pw.print(a.getCodice());
	    pw.print("	");
	    
	    //(GN) PAROLE CHIAVE 1
	    if (a.getParoleChiave1()!=null && !a.getParoleChiave1().trim().isEmpty())
	    	pw.print(a.getParoleChiave1());
	    pw.print("	");
	    
	    //(GO) PAROLE CHIAVE 2
	    if (a.getParoleChiave2()!=null && !a.getParoleChiave2().trim().isEmpty())
	    	pw.print(a.getParoleChiave2());
	    pw.print("	");
	    
	    //(GP) PAROLE CHIAVE 3
	    if (a.getParoleChiave3()!=null && !a.getParoleChiave3().trim().isEmpty())
	    	pw.print(a.getParoleChiave3());
	    pw.print("	");
	    
	    //(GQ) PAROLE CHIAVE 4
	    if (a.getParoleChiave4()!=null && !a.getParoleChiave4().trim().isEmpty())
	    	pw.print(a.getParoleChiave4());
	    pw.print("	");
	    
	    //(GR) PAROLE CHIAVE 5
	    if (a.getParoleChiave5()!=null && !a.getParoleChiave5().trim().isEmpty())
	    	pw.print(a.getParoleChiave5());
	    pw.print("	");
	    
	    /* fine informazioni sulla scoperta del prodotto */
	    
	    /* inizio informazioni prezzo saldo e ribasso */
	    
	    //(GS) PREZZO DI VENDITA
	    pw.print("	");
	    
	    //(GT) DATA FINE SALDO
	    pw.print("	");
	    
	    //(GU) DATA INIZIO SALDO
	    pw.print("	");
	    
	    /* fine informazioni prezzo saldo e ribasso */
	    
	    /* inizio informazioni varianti */
	    
	  	//(GV) FILIAZIONE (parent, child)
	    pw.print("	");
	    
	  	//(GW) PARENT SKU
	    pw.print("	");
	    
	  	//(GX) TIPO RELAZIONE (variante, accessorio)
	    pw.print("	");
	    
	  	//(GY) TEMA VARIANTE (colore, taglia, taglia-colore,fragranza, taglia-fragranza)
	    pw.print("	");
	    
	  	//(GZ) DIMENSIONI (SE IL TEMA è TAGLIA, ES. VALORE VALIDO: S)
	    pw.print("	");
	    
	  	//(HA) MAPPA DIMENSIONI
	    pw.print("	");
	    
	  	//(HB) COLORE
	    pw.print("	");
	    
	  	//(HC) MAPPA COLORE
	    pw.print("	");
	    
	    /* fine informazioni varianti */
	    
	    /* fine !!! */
	    
	    pw.println();
		
	}
		
//		List<Articolo> bomboniere = caricaBomboniereSuAmazon();
//		
//		for (Articolo a : bomboniere){
//			EditorModelliCaricamento.aggiungiProdottoAModelloAmazon(a);
//		}
 
//		testAmazonVarianti();
	
//	public static void aggiungiAdAmazon(Articolo articoloSelezionato){
//		
//		@SuppressWarnings("rawtypes")
//		Map categorie = Categoria_DAO.getMappaCategorie();
//		Categoria c = (Categoria) categorie.get(articoloSelezionato.getIdCategoria());
//		long id_nodo = c.getIdCategoriaAmazon();
//		
//		InfoAmazon ia = new InfoAmazon();
//		ia.setPuntoElenco1(Methods.primeLettereMaiuscole(articoloSelezionato.getQuantitaInserzione()));
//		ia.setPuntoElenco2(articoloSelezionato.getDimensioni());
////		ia.setPuntoElenco3(punto_elenco_3);
////		ia.setPuntoElenco4(punto_elenco_4);
////		ia.setPuntoElenco5(punto_elenco_5);
////		ia.setEsclusioneResponsabilita(esclusione_responsabilita);
////		ia.setDescrizioneGaranziaVenditore(descrizione_garanzia_venditore);
////		ia.setAvvertenzeSicurezza(avvertenze_sicurezza);
////		ia.setNotaCondizioni(nota_condizioni);
////		ia.setVocePacchettoQuantita(articoloSelezionato.getQuantitaInserzione());
////		ia.setNumeroPezzi();
//		ia.setQuantitaMassimaSpedizioneCumulativa(articoloSelezionato.getQuantitaMagazzino());
////		ia.setPaeseOrigine(paese_origine);
////		ia.setLunghezzaArticolo(lunghezza_articolo);
////		ia.setAltezzaArticolo(altezza_articolo);
////		ia.setPesoArticolo(peso_articolo);
////		ia.setUnitaMisuraPesoArticolo(unita_misura_peso_articolo);
//		ia.setParoleChiave1("Bomboniere");
//		ia.setParoleChiave2("Zelda Bomboniere");
//		ia.setParoleChiave3("Bomboniere confezionate");
////		ia.setParoleChiave4(criteri_di_ricerca_4);
////		ia.setParoleChiave5(criteri_di_ricerca_5);
//		ia.setCategoria1(id_nodo);
////		ia.setCategoria2(nodoSelezionato2);
//		
//		articoloSelezionato.setInfoAmazon(ia);
//		
//		int risultato_inserimento_amazon = EditorModelliAmazon.aggiungiProdottoAModelloAmazon(articoloSelezionato);
//			
//		//Articolo_DAO.setPresenteSu(a.getCodice(), "amazon");
//			
//		Log.info(risultato_inserimento_amazon);
//	}
	
	
	

	
	
	public static void testPrimeLettereMaiuscole(){
		String s = "PIANTA ARTIFICIALE BUSH CADENTE FOGLIE GERANIO 60 CM";
		System.out.println(s);
		s = Methods.primeLettereMaiuscole(s.toLowerCase());
		System.out.println(s);
	}
	
	
	
	public static List<Articolo> caricaBomboniereSuAmazon(){
		Log.debug("caricaBomboniereSuAmazon(): Cerco di ottenere la lista di articoli...");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		List<Articolo> articoli = null;

		try {			
			con = DataSource.getLocalConnection();
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM ARTICOLI WHERE CODICE LIKE '%ZELDA%' ORDER BY DATA_ULTIMA_MODIFICA DESC, DATA_INSERIMENTO DESC");
			
			articoli = new ArrayList<Articolo>();
			int i = 0;
			
			Map<Long,Categoria> catMap = CategorieBusiness.getInstance().getMappaCategorie();
			
			while (rs.next()){
				Articolo a = new Articolo();
				
				a.setIdArticolo(rs.getLong("ID_ARTICOLO"));
				a.setCodice(rs.getString("CODICE"));
				a.setCodiceFornitore(rs.getString("CODICE_FORNITORE"));
				a.setCodiceArticoloFornitore(rs.getString("CODICE_ARTICOLO_FORNITORE"));
				a.setNome(rs.getString("NOME"));
				a.setNote(rs.getString("NOTE"));
				a.setIdCategoria(rs.getInt("ID_CATEGORIA"));
				a.setCategoria(catMap.get(a.getIdCategoria()));
				a.setPrezzoDettaglio(rs.getDouble("PREZZO_DETTAGLIO"));
				a.setPrezzoIngrosso(rs.getDouble("PREZZO_INGROSSO"));
				a.setCostoAcquisto(rs.getDouble("COSTO_ACQUISTO"));
				a.setAliquotaIva(rs.getInt("ALIQUOTA_IVA"));
				a.setTitoloInserzione(rs.getString("TITOLO_EBAY"));
				if (rs.getString("TITOLO_EBAY")!=null && !rs.getString("TITOLO_EBAY").trim().isEmpty()){				
					InfoEbay ei = new InfoEbay();
					ei.setTitoloInserzione(rs.getString("TITOLO_EBAY"));
					a.setInfoEbay(ei);
				}
				a.setDimensioni(rs.getString("DIMENSIONI"));
				a.setQuantitaMagazzino(rs.getInt("QUANTITA"));
				a.setQuantitaInserzione(rs.getString("QUANTITA_INSERZIONE"));
				a.setDescrizioneBreve(rs.getString("DESCRIZIONE_BREVE"));
				a.setDescrizione(rs.getString("DESCRIZIONE"));
				a.setCodiceBarre(rs.getString("CODICE_BARRE"));
				a.setTipoCodiceBarre(rs.getString("TIPO_CODICE_BARRE"));
				a.setDataInserimento(rs.getDate("DATA_INSERIMENTO"));
				a.setDataUltimaModifica(rs.getDate("DATA_ULTIMA_MODIFICA"));
				a.setPresente_su_ebay(rs.getInt("PRESENTE_SU_EBAY"));
				a.setPresente_su_gm(rs.getInt("PRESENTE_SU_GM"));
				a.setPresente_su_amazon(rs.getInt("PRESENTE_SU_AMAZON"));
				a.setImmagine1(rs.getString("IMMAGINE1"));
				a.setImmagine2(rs.getString("IMMAGINE2"));
				a.setImmagine3(rs.getString("IMMAGINE3"));
				a.setImmagine4(rs.getString("IMMAGINE4"));
				a.setImmagine5(rs.getString("IMMAGINE5"));
				
				InfoAmazon ia = new InfoAmazon();
				ia.setIdCategoria1("0");
				a.setInfoAmazon(ia);
				
				articoli.add(a);
				i++;
			}
			Log.debug("caricaBomboniereSuAmazon(): "+i+" articoli ottenuti.");

		} catch (Exception ex) {
			Log.info(ex);
			ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,st,null,rs);
		}
		return articoli;
	}
	
	
//	public static Articolo creaArticoloDiEsempio(){
//		Map<Integer,Categoria> categorie = Categoria_DAO.getMappaCategorie();
//		
//		Articolo a = new Articolo();
//		
//    	a.setCodice("CB02");
//    	a.setCodiceFornitore("ZATTI");
//    	a.setCodiceArticoloFornitore("XXX");
//    	a.setCodiceBarre("8003507121201");
//    	a.setTipoCodiceBarre("EAN-13");
//    	a.setNome("SCATOLINA PER BOMBONIERA IN COTTO GRANDE");
//    	a.setTitoloInserzione("SCATOLINA PER BOMBONIERA IN COTTO GRANDE");
//    	a.setPrezzoDettaglio(5.06);
//    	a.setAliquotaIva(21);
//    	a.setQuantitaMagazzino(123);
//    	a.setQuantitaInserzione("UN PEZZO");
//    	a.setDimensioni("RETTANGOLARE/OVALE 12X7X4.5 CM; QUADRA 8X5 CM; PIATTA 12X4.5 CM; TONDA 10X4.5 CM");
//    	a.setDescrizioneBreve("SCATOLINA PER BOMBONIERA");
//    	a.setDescrizione("SCATOLINA PER BOMBONIERA IN COTTO GRANDE IN VARIE MISURE E COLORI");
//    	a.setCategoria(categorie.get(80));
//    	a.setImmagine1("CONTENITORI/CB02.JPG");
//    	a.setImmagine2("CONTENITORI/CB02-OVALEARANCIO.JPG");
//    	a.setImmagine3("CONTENITORI/CB02-QUADROVERDE.JPG");
//    	a.setImmagine4("CONTENITORI/CB02-RETTANGOLAREBLU.JPG");
//    	a.setImmagine5("CONTENITORI/CB02-TONDOARANCIO.JPG");
//    	
//    	return a;
//	}
	
//	public static void testAmazonVarianti(){
//		Map<Integer,Categoria> categorie = Categoria_DAO.getMappaCategorie();
//		
//		Articolo a = new Articolo();
//		
//    	a.setCodice("CB02");
//    	a.setCodiceFornitore("ZATTI");
//    	a.setCodiceArticoloFornitore("XXX");
//    	a.setCodiceBarre("8003507121201");
//    	a.setTipoCodiceBarre("EAN-13");
//    	a.setNome("SCATOLINA PER BOMBONIERA IN COTTO GRANDE");
//    	a.setPrezzoDettaglio(5.06);
//    	a.setAliquotaIva(21);
//    	a.setQuantitaMagazzino(1100);
//    	a.setQuantitaInserzione("UN PEZZO");
//    	a.setDimensioni("RETTANGOLARE/OVALE 12X7X4.5 CM; QUADRA 8X5 CM; PIATTA 12X4.5 CM; TONDA 10X4.5 CM");
//    	a.setDescrizioneBreve("SCATOLINA PER BOMBONIERA");
//    	a.setDescrizione("SCATOLINA PER BOMBONIERA IN COTTO GRANDE IN VARIE MISURE E COLORI");
//    	a.setCategoria(categorie.get(80));
//    	a.setImmagine1("CONTENITORI/CB02.JPG");
////    	a.setImmagine2("CONTENITORI/CB02-OVALEARANCIO.JPG");
////    	a.setImmagine3("CONTENITORI/CB02-QUADROVERDE.JPG");
////    	a.setImmagine4("CONTENITORI/CB02-RETTANGOLAREBLU.JPG");
////    	a.setImmagine5("CONTENITORI/CB02-TONDOARANCIO.JPG");
//    	
//    	InfoAmazon ia = new InfoAmazon();
//    	
//    	ia.setParoleChiave1("bomboniere");
//    	ia.setCategoria1(6543);
//    	ia.setCategoria2(3466);
//    	
//    	a.setInfoAmazon(ia);
//    	
//    	Variante_Articolo v1 = new Variante_Articolo();
//    	Variante_Articolo v2 = new Variante_Articolo();
//    	Variante_Articolo v3 = new Variante_Articolo();
//    	v1.setCodiceArticolo("CB02");
//    	v2.setCodiceArticolo("CB02");
//    	v3.setCodiceArticolo("CB02");
//    	v1.setImmagine("CONTENITORI/CB02-OVALEARANCIO.JPG");
//    	v2.setImmagine("CONTENITORI/CB02-QUADROVERDE.JPG");
//    	v3.setImmagine("CONTENITORI/CB02-RETTANGOLAREBLU.JPG");
//    	v1.setValore("Ovale Arancio");
//    	v2.setValore("Quadro Verde");
//    	v3.setValore("Rettangolare Blu");
//    	v1.setQuantita(100);
//    	v2.setQuantita(100);
//    	v3.setQuantita(100);
//    	
//    	List<Variante_Articolo> vars = new ArrayList<Variante_Articolo>();
//    	
//    	vars.add(v1);
//    	vars.add(v2);
//    	vars.add(v3);
//    	
//    	a.setVarianti(vars);
//    	
////    	InfoEbay ie = new InfoEbay();
////    	
////    	a.setEbayInfo(ie);
////    	
////    	ie.setTitoloInserzione("DUE ANGELI STUDIOSI PROVA TITOLO INSERZIONE");
////    	
////    	System.out.println(EditorDescrizioni.creaDescrizioneEbay(a));
//    	
//    	EditorModelliAmazon.aggiungiProdottoAModelloAmazon(a);
//	}
	
	public static void leggiFiles() {
		File name = new File("D:\\zeus\\mcd\\mcd_amazon_2013-07.txt");
		if (name.isFile()) {
			try {
				BufferedReader input = new BufferedReader(new FileReader(name));
				StringBuffer buffer = new StringBuffer();
				String text;
				int i = 0;
				
				while ((text = input.readLine()) != null){
					if (i>=2){
						String[] riga = text.split("	");	
						//System.out.println (riga[0]+" "+riga.length);
						if (riga.length<=54 || !riga[57].equals("child"))
							System.out.println(riga[0]);
					}
					i++;
					//buffer.append(text + "\n");
				}
				
				input.close();

				System.out.println(buffer.toString());
			} catch (IOException ioException) {
			}
		}
	}
	
/*	
	public static void spostaVarianti(){
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			con = DataSourceOLD.getInstance().getLocalConnection();
			st = con.createStatement();
			rs = st.executeQuery("select * from varianti_artiolo");
			String tipo = "colore";
			String valore = "";
			
			while (rs.next()){
				if (rs.getString("colore")==null){
					tipo = "Tema";
					valore = rs.getString("tema");
				}
				else if (rs.getString("tema")==null){
					tipo = "Colore";
					valore = rs.getString("colore");
				}
				else {
					tipo = "Variante";
					valore = rs.getString("colore")+" "+rs.getString("tema");
				}
				
				System.out.println("insert into `varianti_articolo`(`codice_articolo`,`tipo`,`valore`,`immagine`) " +
						"values ('"+rs.getString("codice_articolo")+"'," +
								"'"+tipo+"'," +
								"'"+valore+"'," +
								"'"+rs.getString("immagine")+"');");
			}
			
		}
		catch (Exception e){
			Log.info(e);
			e.printStackTrace();
		}
		
	}
*/	
	
//	public static void appendi(Exception e){
//		FileOutputStream fos;
//		try {
//			fos = new FileOutputStream ("D:\\log\\gebis.txt", true);
//	
//			PrintWriter pw = new PrintWriter (fos);
//			
//			DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH.mm.ss");
//			String st = df.format(new Date());
//			
//			pw.println();
//			pw.println();
//			pw.println();
//			pw.println();
//			pw.println(st+": "+e.toString());
//			pw.println();
//			
//			for (StackTraceElement s : e.getStackTrace()){
//				pw.println(s.toString());
//			}
//			
//			pw.close();
//			
//		} catch (FileNotFoundException f) {
//			f.printStackTrace();
//		} 
//	}
	
	
	public static  void editorModelloAmazon(Articolo a){				
		 Properties config = new Properties();	        
	     try {
			config.load(Log.class.getResourceAsStream("/zeus.properties"));
									
			String percorso = config.getProperty("percorso_modello_caricamento_dati_amazon");		
			
			//File f = new File(percorso);
			
			FileOutputStream fos = new FileOutputStream (percorso, true);
			
			PrintWriter pw = new PrintWriter (fos);
			
			//pw.println();
			//pw.println("");
			aggiungiProdotto(a,pw);
			
			pw.close();
	
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	
	
	
	public static void aggiungiProdotto(Articolo a, PrintWriter pw){
		
		/* inizio product basic information */
		
		//(A) SKU 
		pw.print(a.getCodice());
		pw.print("	");
	    
	    //(B) CODICE BARRE 
		pw.print(a.getCodiceBarre());
		pw.print("	");
	    
	    //(C) TIPO CODICE (EAN) 
		pw.print("EAN");
		pw.print("	");
	    
	    //(D) NOME ARTICOLO 
		pw.print(a.getNome());
		pw.print("	");

	    //(E) MARCA 
		pw.print("");
		pw.print("	");

	    //(F) PRODUTTORE 
		pw.print("Zelda Bomboniere");
		pw.print("	");

	    //(G) DESCRIZIONE 
		pw.print(a.getDescrizione());
		pw.print("	");

	    //(H) PUNTO ELENCO 1 
	    if (a.getQuantitaInserzione()!=null && !a.getQuantitaInserzione().trim().isEmpty()){
	    	pw.print("Quantità inserzione: "+a.getQuantitaInserzione());  
	    }
	    pw.print("	");

	    //(I) PUNTO ELENCO 2 
	    if (a.getDimensioni()!=null && !a.getDimensioni().trim().isEmpty()){
	    	pw.print("Dimensioni: "+a.getDimensioni()); 
		}
	    pw.print("	");

	    //(J) PUNTO ELENCO 3 
	    pw.print("");
	    pw.print("	");

	    //(K) PUNTO ELENCO 4 
	    pw.print("");
	    pw.print("	");

	    //(L) PUNTO ELENCO 5 
	    pw.print("");
	    pw.print("	");

	    //(M) NODO NAVIGAZIONE 1 
	    pw.print(a.getInfoAmazon().getIdCategoria1());  
	    pw.print("	");

	    //(N) NODO NAVIGAZIONE 2 
	    pw.print(a.getInfoAmazon().getIdCategoria2());
	    pw.print("	");

	    //(O) TIPO PRODOTTO 
	    pw.print("FurnitureAndDecor"); 
	    pw.print("	");
	    
	    /* fine product basic information */
	    
	    /* inizio informazioni sull'immagine */
	    
	    //(BB) URL IMMAGINE PRINCIPALE
	    pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine1()); 
	    pw.print("	");
	    
	    //(BC) URL ALTRA IMMAGINE 1
	    if (a.getImmagine2()!=null && !a.getImmagine2().trim().isEmpty()){
	    	pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine2()+"");  
	    }
	    pw.print("	");
	    
	    //(BD) URL ALTRA IMMAGINE 2
	    if (a.getImmagine3()!=null && !a.getImmagine3().trim().isEmpty()){
	    	pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine3());  
	    }
	    pw.print("	");
	    
	    //(BE) URL ALTRA IMMAGINE 3
	    if (a.getImmagine4()!=null && !a.getImmagine4().trim().isEmpty()){
	    	pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine4());  
	    }
	    pw.print("	");
	    
	    //(BF) URL ALTRA IMMAGINE 4
	    if (a.getImmagine5()!=null && !a.getImmagine5().trim().isEmpty()){
	    	pw.print(Costanti.percorsoImmaginiRemoto+a.getImmagine5());  
	    }
	    pw.print("	");
	    
	    //(BG) URL ALTRA IMMAGINE 5	
	    pw.print("	");
		//(BH) URL ALTRA IMMAGINE 6
	    pw.print("	");
		//(BI) URL ALTRA IMMAGINE 7
	    pw.print("	");
		//(BJ) URL ALTRA IMMAGINE 8
	    pw.print("	");
	    
	    //(BK) URL IMMAGINE SWITCH
	    pw.print("	");
	    
	    /* fine informazioni sull'immagine */
	    
	    /* inizio informazioni legali */
	    
	    //(BL) ESCLUSIONE RESPOSABILITA'
	    if (a.getInfoAmazon().getEsclusioneResponsabilita()!=null && !a.getInfoAmazon().getEsclusioneResponsabilita().trim().isEmpty())
	    	pw.print(a.getInfoAmazon().getEsclusioneResponsabilita());
	    pw.print("	");
	    
	    //(BM) DESCRIZIONE GARANZIA VENDITORE
	    if (a.getInfoAmazon().getDescrizioneGaranziaVenditore()!=null && !a.getInfoAmazon().getDescrizioneGaranziaVenditore().trim().isEmpty())
	   	 	pw.print(a.getInfoAmazon().getDescrizioneGaranziaVenditore());
	    pw.print("	");
	    
	    //(BN) AVVERTENZE SICUREZZA
	    if (a.getInfoAmazon().getAvvertenzeSicurezza()!=null && !a.getInfoAmazon().getAvvertenzeSicurezza().trim().isEmpty())
	    	pw.print(a.getInfoAmazon().getAvvertenzeSicurezza());
	    pw.print("	");
	    
	    /* fine informazioni legali */
	    
	    //(BO) AGGIORNA O RIMUOVI
	    pw.print("	");
	    
	    /* inizio informazioni sull'offerta */
	    
	    //(BP) PREZZO
	    pw.print(String.valueOf(a.getPrezzoDettaglio()).replace(",", ".")); 
	    pw.print("	");
	    
	    //(BQ) VALUTA
	    pw.print("EUR"); 
	    pw.print("	");
	    
	    //(BR) QUANTITA'
	    if (a.getQuantitaMagazzino()>0)
	    	pw.print(a.getQuantitaMagazzino());  
	    else pw.print(20);  
	    pw.print("	");
	    
	    //(BS) CONDIZIONI
	    pw.print("New"); 
	    pw.print("	");
	    
	    //(BT) NOTA CONDIZIONI
	    if (a.getInfoAmazon().getNotaCondizioni()!=null && !a.getInfoAmazon().getNotaCondizioni().trim().isEmpty())
	    	pw.print(a.getInfoAmazon().getNotaCondizioni());
	    pw.print("	");
	    
	    //(BU) VOCE PACCHETTO QUANTITA'
	    if (a.getInfoAmazon().getVocePacchettoQuantita()!=0)
	    	pw.print(a.getInfoAmazon().getVocePacchettoQuantita());
	    pw.print("	");
	    
	    //(BV) NUMERO PEZZI
	    if (a.getInfoAmazon().getNumeroPezzi()!=0)
	    	pw.print(a.getInfoAmazon().getNumeroPezzi());
	    pw.print("	");
	    
	    //(BW) DATA LANCIO
	    pw.print("	");
	    
	    //(BX) DATA RILASCIO
	    pw.print("	");
	    
	    //(BY) TEMPI ESECUZIONE SPEDIZIONE
	    pw.print("	");
	    
	    //(BZ) DATA RIFORNIMENTO
	    pw.print("	");
	    
	    //(CA) QUANTITA' MASSIMA SPEDIZIONE CUMULATIVA
	    if (a.getInfoAmazon().getNumeroPezzi()!=0)
	    	pw.print(a.getInfoAmazon().getQuantitaMassimaSpedizioneCumulativa());
	    pw.print("	");
	    
	    //(CB) PAESE DI ORIGINE
	    if (a.getInfoAmazon().getPaeseOrigine()!=null && !a.getInfoAmazon().getPaeseOrigine().trim().isEmpty())
	    	pw.print(a.getInfoAmazon().getPaeseOrigine());
	    pw.print("	");
	    
	    /* fine informazioni sull'offerta */
	    
	    /* inizio dimensioni prodotto */
	    
	    //(GG) PESO SPEDIZIONE
	    pw.print("	");
	    
	    //(GH) UNITA' MISURA PESO SPEDIZIONE
	    pw.print("	");
	    
	    //(GI) LUNGHEZZA ARTICOLO
	    if (a.getInfoAmazon().getLunghezzaArticolo()!=0)
	    	pw.print(a.getInfoAmazon().getLunghezzaArticolo()); 
	    pw.print("	");
	    
	    //(GJ) ALTEZZA ARTICOLO
	    if (a.getInfoAmazon().getAltezzaArticolo()!=0)
	    	pw.print(a.getInfoAmazon().getAltezzaArticolo()); 
	    pw.print("	");
	    
	    //(GK) PESO ARTICOLO
	    if (a.getInfoAmazon().getPesoArticolo()!=0)
	    	pw.print(a.getInfoAmazon().getPesoArticolo()); 
	    pw.print("	");
	    
	    //(GL) UNITA' MISURA PESO ARTICOLO
	    if (a.getInfoAmazon().getUnitaMisuraPesoArticolo()!=null && !a.getInfoAmazon().getUnitaMisuraPesoArticolo().trim().isEmpty())
	    	pw.print(a.getInfoAmazon().getUnitaMisuraPesoArticolo()); 
	    pw.print("	");
	    
	    /* fine dimensioni prodotto */
	    
	    /* inizio informazioni sulla scoperta del prodotto */
	    
	    //(GM) CODICE ARTICOLO DEL PRODUTTORE
	    if (a.getCodiceArticoloFornitore()!=null && !a.getCodiceArticoloFornitore().trim().isEmpty())
	    	pw.print(a.getCodiceArticoloFornitore());
	    pw.print("	");
	    
	    //(GN) PAROLE CHIAVE 1
	    if (a.getParoleChiave1()!=null && !a.getParoleChiave1().trim().isEmpty())
	    	pw.print(a.getParoleChiave1());
	    pw.print("	");
	    
	    //(GO) PAROLE CHIAVE 2
	    if (a.getParoleChiave2()!=null && !a.getParoleChiave2().trim().isEmpty())
	    	pw.print(a.getParoleChiave2());
	    pw.print("	");
	    
	    //(GP) PAROLE CHIAVE 3
	    if (a.getParoleChiave3()!=null && !a.getParoleChiave3().trim().isEmpty())
	    	pw.print(a.getParoleChiave3());
	    pw.print("	");
	    
	    //(GQ) PAROLE CHIAVE 4
	    if (a.getParoleChiave4()!=null && !a.getParoleChiave4().trim().isEmpty())
	    	pw.print(a.getParoleChiave4());
	    pw.print("	");
	    
	    //(GR) PAROLE CHIAVE 5
	    if (a.getParoleChiave5()!=null && !a.getParoleChiave5().trim().isEmpty())
	    	pw.print(a.getParoleChiave5());
	    pw.print("	");
	    
	    /* fine informazioni sulla scoperta del prodotto */
	    
	    /* inizio informazioni prezzo saldo e ribasso */
	    
	    //(GS) PREZZO DI VENDITA
	    pw.print("	");
	    
	    //(GT) DATA FINE SALDO
	    pw.print("	");
	    
	    //(GU) DATA INIZIO SALDO
	    pw.print("	");
	    
	    /* fine informazioni prezzo saldo e ribasso */
	    
	    /* inizio informazioni varianti */
	    
	  	//(GV) FILIAZIONE (parent, child)
	    pw.print("	");
	    
	  	//(GW) PARENT SKU
	    pw.print("	");
	    
	  	//(GX) TIPO RELAZIONE (variante, accessorio)
	    pw.print("	");
	    
	  	//(GY) TEMA VARIANTE (colore, taglia, taglia-colore,fragranza, taglia-fragranza)
	    pw.print("	");
	    
	  	//(GZ) DIMENSIONI (SE IL TEMA è TAGLIA, ES. VALORE VALIDO: S)
	    pw.print("	");
	    
	  	//(HA) MAPPA DIMENSIONI
	    pw.print("	");
	    
	  	//(HB) COLORE
	    pw.print("	");
	    
	  	//(HC) MAPPA COLORE
	    pw.print("	");
	    
	    /* fine informazioni varianti */
	    
	    /* fine !!! */
	    
	    pw.println();
		
	}


}
