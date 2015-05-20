package it.swb.piattaforme.zelda;

import it.swb.database.DataSource;
import it.swb.log.Log;
import it.swb.model.ArticoloAcquistato;
import it.swb.model.Cliente;
import it.swb.model.Indirizzo;
import it.swb.model.Ordine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrdiniZelda {
	
	public static List<Ordine> getOrdini(Date dataDa, Date dataA){
		Log.debug("Cerco di ottenere la lista degli ordini di ZeldaBomboniere.it");
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		List<Ordine> ordini = null;

		try {	
			con = DataSource.getZBConnection();
			
			ps = con.prepareStatement("SELECT o.*,os.name as stato_ordine " +
														"FROM fmrmlfhq_zeldabomboniere.order as o " +
														"INNER JOIN order_status as os ON o.order_status_id = os.order_status_id and os.language_id=2 " +
														//TODO !!! inserire dataDa e dataA nella query download ordini zb
														//"WHERE date_modified between ? AND ? " +
														"order by date_added desc limit 30");
			
			//ps.setDate(1, new java.sql.Date(dataDa.getTime()));
			//ps.setDate(2, new java.sql.Date(dataA.getTime()));
			
			rs = ps.executeQuery();
			
			ps1 = con.prepareStatement("SELECT opr.* ,opt.value as variante, p.tax_class_id " +
															"FROM order_product as opr " +
															"LEFT JOIN order_option as opt ON opr.order_product_id = opt.order_product_id " +
															"INNER JOIN product as p ON opr.product_id = p.product_id " +
															"WHERE opr.order_id=?");
			
//			ClienteBusiness.getInstance().reloadMappaClientiZeldaCompleta();
//			Map<String,Cliente> mapclienti = ClienteBusiness.getInstance().getMappaClientiZeldaCompleta();
//			Map<String,List<Articolo>> maparticoli = getMappaOrdiniConListaArticoli(con,ps,rs);
			
			ps2 =  con.prepareStatement("SELECT code,title,value " +
															"FROM order_total  " +
															"WHERE order_id=?");
			
			ordini = new ArrayList<Ordine>();
			
			int i = 0;
			
			while (rs.next()){
				i++;
				Ordine o = new Ordine();
				
				o.setIdOrdinePiattaforma("ZB_"+rs.getString("order_id"));
				o.setPiattaforma("ZeldaBomboniere.it");
				
				o.setDataAcquisto(rs.getTimestamp("date_added"));
				
				o.setMetodoPagamento(rs.getString("payment_method"));
				o.setMetodoSpedizione(rs.getString("shipping_method"));
				String commento = rs.getString("comment");
				if (commento!=null && !commento.isEmpty())
				o.setCommento(commento);
				//o.setTotale(rs.getDouble("total")); viene messo dopo su rs2
				o.setValuta(rs.getString("currency_code"));
				o.setStato(rs.getString("stato_ordine"));
				
				o.setIdCliente(rs.getInt("customer_id"));
				
				o.setEmail(rs.getString("email"));
				
				o.setUsername(rs.getString("customer_id"));
				o.setNomeAcquirente(rs.getString("firstname")+" "+rs.getString("lastname"));
				
				Cliente c = new Cliente();
				c.setPiattaforma("ZeldaBomboniere.it");
				c.setUsername(rs.getString("customer_id"));
				c.setNome(rs.getString("firstname"));
				c.setCognome(rs.getString("lastname"));
				c.setNomeCompleto(rs.getString("firstname")+" "+rs.getString("lastname"));
				c.setTelefono(rs.getString("telephone"));
				c.setFax(rs.getString("fax"));
				c.setEmail(rs.getString("email"));
				
				o.setCliente(c);
				
				Indirizzo indSped = new Indirizzo();
				indSped.setNome(rs.getString("shipping_firstname"));
				indSped.setCognome(rs.getString("shipping_lastname"));
				indSped.setNomeCompleto(rs.getString("shipping_firstname")+" "+rs.getString("shipping_lastname"));
				indSped.setAzienda(rs.getString("shipping_company"));
				indSped.setIndirizzo1(rs.getString("shipping_address_1"));
				indSped.setIndirizzo2(rs.getString("shipping_address_2"));
				indSped.setComune(rs.getString("shipping_city"));
				indSped.setProvincia(rs.getString("shipping_zone"));
				indSped.setCap(rs.getString("shipping_postcode"));
				indSped.setNazione(rs.getString("shipping_country"));
				
				c.setIndirizzoSpedizione(indSped);
				o.setIndirizzoSpedizione(indSped);
				
				Indirizzo indFatt = new Indirizzo();
				indFatt.setNome(rs.getString("payment_firstname"));
				indFatt.setCognome(rs.getString("payment_lastname"));
				indFatt.setNomeCompleto(rs.getString("payment_firstname")+" "+rs.getString("payment_lastname"));
				indFatt.setAzienda(rs.getString("payment_company"));
				indFatt.setIndirizzo1(rs.getString("payment_address_1"));
				indFatt.setIndirizzo2(rs.getString("payment_address_2"));
				indFatt.setComune(rs.getString("payment_city"));
				indFatt.setProvincia(rs.getString("payment_zone"));
				indFatt.setCap(rs.getString("payment_postcode"));
				indFatt.setNazione(rs.getString("payment_country"));
				
				c.setIndirizzoFatturazione(indFatt);
				o.setIndirizzoFatturazione(indFatt);
				
				ps1.setInt(1, rs.getInt("order_id"));
				
				rs1 = ps1.executeQuery();
				
				List<ArticoloAcquistato> articoli = new ArrayList<ArticoloAcquistato>();
				
				int quantita_totale = 0;
				
				o.setBomboniere(false);
				
				while (rs1.next()){
					ArticoloAcquistato a = new ArticoloAcquistato();
					
					a.setPiattaforma("ZeldaBomboniere.it");
					a.setIdArticolo(rs1.getInt("product_id"));
					a.setNome(rs1.getString("name"));
					a.setTitoloInserzione(rs1.getString("name"));
					a.setCodice(rs1.getString("model"));
					a.setQuantitaAcquistata(rs1.getInt("quantity"));
					int classe_iva = rs1.getInt("tax_class_id");
					int iva=22;
					if (classe_iva==12) iva=10;
					else if (classe_iva==13) iva=4;
					a.setIva(iva);
					a.setPrezzoUnitario(rs1.getDouble("price"));
					a.setPrezzoTotale(rs1.getDouble("total"));
					a.setTasse(rs1.getDouble("tax"));
					a.setVariante(rs1.getString("variante"));
					
					a.setIdOrdinePiattaforma(String.valueOf(o.getIdOrdinePiattaforma()));
					String transazione = o.getIdOrdinePiattaforma()+"_"+a.getCodice();
					
					if(a.getVariante()!=null && !a.getVariante().isEmpty())
						transazione+="_"+a.getVariante();
						
					a.setIdTransazione(transazione);
					
					quantita_totale+=a.getQuantitaAcquistata();
					
					if (a.getCodice() != null && (a.getCodice().contains("ZELDA") || a.getCodice().contains("TORTA"))){
		            	o.setBomboniere(true);
		            }
					
					articoli.add(a);
				}
				
				o.setQuantitaAcquistata(quantita_totale);
				o.setElencoArticoli(articoli);
				
				ps2.setInt(1, rs.getInt("order_id"));
				
				rs2 = ps2.executeQuery();
				
				while (rs2.next()){
					if (rs2.getString("code").equals("coupon")){
						o.setSconto(true);
						o.setNomeBuonoSconto(rs2.getString("title"));
						o.setValoreBuonoSconto(rs2.getDouble("value"));
					}
					else if (rs2.getString("code").equals("shipping")){
						o.setCostoSpedizione(rs2.getDouble("value"));
					}
					else if (rs2.getString("code").equals("total")){
						o.setTotale(rs2.getDouble("value"));
					}
				}
				
//				if (mapclienti.containsKey(o.getEmailAcquirente()))
//					o.setCliente(mapclienti.get(o.getEmailAcquirente()));
//				else if (mapclienti.containsKey(o.getIdAcquirente()))
//					o.setCliente(mapclienti.get(o.getIdAcquirente()));
//				
//				
//				if (maparticoli.containsKey(o.getIdOrdine()))
//					o.setArticoli(maparticoli.get(o.getIdOrdine()));
				
				ordini.add(o);
				
			}
			Log.debug("Lista degli ordini zb.it ottenuta, occorrenze:"+ i);

		} catch (Exception ex) {
			Log.info(ex); 
			ex.printStackTrace();
		}
		 finally {			 
				 DataSource.closeConnections(con,null,ps,rs);	
				 DataSource.closeConnections(null,null,ps1,rs1);	
				 DataSource.closeConnections(null,null,ps2,rs2);	
		}
		return ordini;
	}

}
