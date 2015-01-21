package it.swb.database;

import it.swb.log.Log;
import it.swb.utility.Methods;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Report_DAO {
	
	public static List<Object[]> getReportOrdini(String groupBy, boolean dividiXpiattaforma){
		Log.info("Richiesto il report sugli ordini, raggruppati per "+groupBy+", divisi per piattaforma: "+dividiXpiattaforma);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Object[]> list = null;

		try {			
			con = DataSource.getLocalConnection();
			
			String raggruppamento = "substring(data_acquisto,1,10)";
			String piattaforma = "";
			
			if (groupBy.equals("Mese"))	raggruppamento = "substring(data_acquisto,1,7)";
			
			if (dividiXpiattaforma) piattaforma = ",piattaforma ";
			
			
			String query = "SELECT "+raggruppamento+" as data, count(*) as numero_ordini, sum(totale) as fatturato " +piattaforma+
							"FROM ordini " +
							"where stato <> 'Cancellato' and stato <> 'Inactive' and stato <> 'Annullato' " +
							"group by "+raggruppamento+piattaforma+
							" order by data desc";
			
			ps = con.prepareStatement(query);
			
			rs = ps.executeQuery();
			
			list = new ArrayList<Object[]>();
			int i = 0;
			
			while (rs.next()){
				Object[] s = new Object[5];
				
				s[0] = i;
				s[1] = rs.getString("data");
				s[2] = rs.getInt("numero_ordini");
				s[3] = rs.getDouble("fatturato");
				if (dividiXpiattaforma) s[4] = rs.getString("piattaforma");
				else s[4] = null;
				
				i++;
				list.add(s);
			}
			
			Log.info("Ottenuti "+list.size()+" report ordini.");

		} catch (Exception ex) {
			Log.info(ex);
			ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,rs);
		}
		return list;
	}
	
	
	public static List<Object[]> getReportVendite(String dataDa, String dataA, String statoOrdine, boolean raggruppaArticoli){
		Log.info("Richiesto il report sulle vendite da "+dataDa+" a "+dataA+", per articoli: "+statoOrdine+", raggruppamento: "+raggruppaArticoli);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Object[]> list = null;

		try {			
			con = DataSource.getLocalConnection();
			
			String raggruppamento1 = "sum(ova.quantita) as quantita ";
			String raggruppamento2 = "group by codice_articolo ";
			String stOr = "and o.stato='"+statoOrdine+"' ";
			String ordinamento = "codice_articolo asc";
			
			if (!raggruppaArticoli) {
				raggruppamento1 = "ova.quantita, data_acquisto, piattaforma, o.id_ordine as id_ordine ";
				raggruppamento2 = "";
				ordinamento = "data_acquisto desc";
			}
			
			if (statoOrdine.equals("Tutti"))	stOr = "";
			
			String query = "SELECT codice_articolo, a.nome as nome, " +raggruppamento1+ 
							"FROM ordini_vs_articoli AS ova " +
							"INNER JOIN ordini AS o ON ova.id_ordine = o.id_ordine " +
							"INNER JOIN articoli AS a ON ova.codice_articolo = a.codice "+
							"WHERE codice_articolo IS NOT NULL "+stOr+" " +
							"AND data_acquisto BETWEEN '"+dataDa+"' AND '"+dataA+"' " +
							raggruppamento2 +
							"ORDER BY "+ordinamento;
			
			//System.out.println(query);
			
			ps = con.prepareStatement(query);
			
			rs = ps.executeQuery();
			
			list = new ArrayList<Object[]>();
			int i = 0;
			
			while (rs.next()){
				Object[] s = new Object[7];
				
				s[0] = i;
				s[1] = rs.getString("codice_articolo");
				s[6] = rs.getString("nome");
				s[2] = rs.getInt("quantita");
				if (!raggruppaArticoli){
					s[3] = rs.getString("data_acquisto");
					s[4] = rs.getString("piattaforma");
					s[5] = rs.getString("id_ordine");
				}
				else {
					s[3] = "dal "+Methods.cambiaFormatoData(dataDa)+" al "+Methods.cambiaFormatoData(dataA);
					s[4] = null;
					s[5] = null;
				}
				
				i++;
				list.add(s);
			}
			
			Log.info("Ottenuti "+list.size()+" report vendite.");

		} catch (Exception ex) {
			Log.info(ex);
			ex.printStackTrace();
		}
		 finally {
			 DataSource.closeConnections(con,null,ps,rs);
		}
		return list;
	}

}
