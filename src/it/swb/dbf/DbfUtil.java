package it.swb.dbf;

import it.swb.business.ArticoloBusiness;
import it.swb.database.Articolo_DAO;
import it.swb.database.Categoria_DAO;
import it.swb.log.Log;
import it.swb.model.Articolo;
import it.swb.model.Categoria;
import it.swb.model.Cliente;
import it.swb.model.Fornitore;
import it.swb.model.Indirizzo;
import it.swb.utility.DateMethods;
import it.swb.utility.Methods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.sql.Date;
import java.util.List;

import org.xBaseJ.DBF;
import org.xBaseJ.fields.CharField;
import org.xBaseJ.fields.DateField;
import org.xBaseJ.fields.NumField;


public class DbfUtil {
	
	public static void main (String[] args){
		
//		List<Cliente> clienti = getClienti("D:\\DB\\001\\ANACF.DBF");
//		
//		for (Cliente c : clienti){
//			if (Cliente_DAO.checkIfClienteExist(c.getCodiceCliente()))
//				Cliente_DAO.modificaCliente(c);
//			else Cliente_DAO.inserisciCliente(c);
//		}
		
		//getArticoliFromDbf("D:\\BACKUP\\ARTICOLI.DBF",true);
		
		
	}
	
	public static List<Fornitore> getFornitori(String percorso){	
		System.out.println("Inizio letture articoli da "+percorso+" ...");
		List<Fornitore> list = null;
		try{
			//Open dbf file
			DBF classDB=new DBF(percorso);

			//Define fields
			CharField fornitore  = (CharField) classDB.getField("CLFR");
			CharField codice_fornitore  = (CharField) classDB.getField("CODCF");
			CharField nome_fornitore  = (CharField) classDB.getField("RAGSOC");
			CharField proprietario  = (CharField) classDB.getField("RAGSOC1");
			
			CharField indirizzo_sede_legale  = (CharField) classDB.getField("INDIR");
			CharField cap_sede_legale  = (CharField) classDB.getField("CAP");
			CharField localita_sede_legale  = (CharField) classDB.getField("LOCAL");
			CharField provincia_sede_legale  = (CharField) classDB.getField("PROV");
			CharField indirizzo_uffici  = (CharField) classDB.getField("INDIRA");
			CharField cap_uffici  = (CharField) classDB.getField("CAPA");
			CharField localita_uffici  = (CharField) classDB.getField("LOCALA");
			CharField provincia_uffici  = (CharField) classDB.getField("PROVA");
			
			CharField codice_fiscale  = (CharField) classDB.getField("CODFISC");
			CharField partita_iva  = (CharField) classDB.getField("PARTIVA");
			CharField codice_pagamento  = (CharField) classDB.getField("CODPAG");
			CharField telefono1  = (CharField) classDB.getField("TEL");
			CharField telefono2  = (CharField) classDB.getField("TEL2");
			CharField fax  = (CharField) classDB.getField("FAX");
			CharField email  = (CharField) classDB.getField("EMAIL");
			CharField responsabile_rappresentante  = (CharField) classDB.getField("RESPRAP");
			CharField tipo_attivita  = (CharField) classDB.getField("PROTRAT");
			CharField codice_tipo_attivita  = (CharField) classDB.getField("RAGCLI");


			list = new ArrayList<Fornitore>();
			int j = 0;
			
			for (int i = 1; i <= classDB.getRecordCount(); i++)
			{
				classDB.read();
				
				if (fornitore.get().trim().equals("F"))
				{
					Fornitore f = new Fornitore();
					
					f.setCodiceFornitore(codice_fornitore.get().trim());
					f.setRagioneSociale(nome_fornitore.get().trim());
					f.setProprietario(proprietario.get().trim());
					
					f.setIndirizzoSedeLegale(indirizzo_sede_legale.get().trim());
					f.setCapSedeLegale(cap_sede_legale.get().trim());						
					f.setLocalitaSedeLegale(localita_sede_legale.get().trim());
					f.setProvinciaSedeLegale(provincia_sede_legale.get().trim());
					f.setIndirizzoUffici(indirizzo_uffici.get().trim());
					f.setCapUffici(cap_uffici.get().trim());						
					f.setLocalitaUffici(localita_uffici.get().trim());
					f.setProvinciaUffici(provincia_uffici.get().trim());
					
					f.setCodiceFiscale(codice_fiscale.get().trim());
					f.setPartitaIva(partita_iva.get().trim());
					f.setCodicePagamento(codice_pagamento.get().trim());
					f.setTelefono1(telefono1.get().trim());
					f.setTelefono2(telefono2.get().trim());
					f.setFax(fax.get().trim());
					f.setEmail(email.get().trim());
					f.setResponsabileRappresentante(responsabile_rappresentante.get().trim());
					f.setTipoAttivita(tipo_attivita.get().trim());
					f.setCodiceTipoAttivita(codice_tipo_attivita.get().trim());
									
					list.add(f);
				}
				j++;
			}
			System.out.println("Fine lettura dbf... "+j+" occorrenze");
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	
	public static List<Cliente> getClienti(String percorso){	
		System.out.println("Inizio letture articoli da "+percorso+" ...");
		List<Cliente> list = null;
		try{
			//Open dbf file
			DBF classDB=new DBF(percorso);

			//Define fields
			CharField cliente  = (CharField) classDB.getField("CLFR");
			CharField codice_cliente  = (CharField) classDB.getField("CODCF");
			CharField nome_cliente  = (CharField) classDB.getField("RAGSOC");
			CharField proprietario  = (CharField) classDB.getField("RAGSOC1");
			
			CharField indirizzo_sede_legale  = (CharField) classDB.getField("INDIR");
			CharField cap_sede_legale  = (CharField) classDB.getField("CAP");
			CharField localita_sede_legale  = (CharField) classDB.getField("LOCAL");
			CharField provincia_sede_legale  = (CharField) classDB.getField("PROV");
			CharField indirizzo_uffici  = (CharField) classDB.getField("INDIRA");
			CharField cap_uffici  = (CharField) classDB.getField("CAPA");
			CharField localita_uffici  = (CharField) classDB.getField("LOCALA");
			CharField provincia_uffici  = (CharField) classDB.getField("PROVA");
			
			CharField codice_fiscale  = (CharField) classDB.getField("CODFISC");
			CharField partita_iva  = (CharField) classDB.getField("PARTIVA");
			CharField codice_pagamento  = (CharField) classDB.getField("CODPAG");
			CharField telefono1  = (CharField) classDB.getField("TEL");
			CharField telefono2  = (CharField) classDB.getField("TEL2");
			CharField fax  = (CharField) classDB.getField("FAX");
			CharField email  = (CharField) classDB.getField("EMAIL");
			CharField responsabile_rappresentante  = (CharField) classDB.getField("RESPRAP");
			CharField tipo_attivita  = (CharField) classDB.getField("PROTRAT");
			CharField codice_tipo_attivita  = (CharField) classDB.getField("RAGCLI");


			list = new ArrayList<Cliente>();
			int j = 0;
			
			for (int i = 1; i <= classDB.getRecordCount(); i++)
			{
				classDB.read();
				
				if (cliente.get().trim().equals("C"))
				{
					Cliente f = new Cliente();
					
					f.setCodiceCliente(codice_cliente.get().trim());
					f.setRagioneSociale(nome_cliente.get().trim());
					f.setProprietario(proprietario.get().trim());
					
					Indirizzo isd = new Indirizzo();
					isd.setIndirizzo1(indirizzo_sede_legale.get().trim());
					isd.setCap(cap_sede_legale.get().trim());
					isd.setComune(localita_sede_legale.get().trim());
					isd.setProvincia(provincia_sede_legale.get().trim());
					
					
					Indirizzo iu = new Indirizzo();
					iu.setIndirizzo1(indirizzo_uffici.get().trim());
					iu.setCap(cap_uffici.get().trim());
					iu.setComune(localita_uffici.get().trim());
					iu.setProvincia(provincia_uffici.get().trim());
					
					f.setIndirizzoSedeLegale(isd);
					f.setIndirizzoUffici(iu);
					
					f.setCodiceFiscale(codice_fiscale.get().trim());
					f.setPartitaIva(partita_iva.get().trim());
					f.setCodicePagamento(codice_pagamento.get().trim());
					f.setTelefono1(telefono1.get().trim());
					f.setTelefono2(telefono2.get().trim());
					f.setFax(fax.get().trim());
					f.setEmail(email.get().trim());
					f.setResponsabileRappresentante(responsabile_rappresentante.get().trim());
					f.setTipoAttivita(tipo_attivita.get().trim());
					f.setCodiceTipoAttivita(codice_tipo_attivita.get().trim());
									
					list.add(f);
				}
				j++;
			}
			System.out.println("Fine lettura dbf... "+j+" occorrenze");
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	
	public static List<Articolo> getGiacenze(String percorso){	
		System.out.println("Inizio letture articoli da "+percorso+" ...");
		List<Articolo> mapqt = null;
		
		try{
			//Open dbf file
			DBF classDB=new DBF(percorso);

			//Define fields
			CharField anno  = (CharField) classDB.getField("ANNO");
			CharField codart  = (CharField) classDB.getField("CODART");
			NumField giaini = (NumField) classDB.getField("GIAINI");
			NumField procar  = (NumField) classDB.getField("PROCAR");			
			NumField prosca  = (NumField) classDB.getField("PROSCA");
			
			
			Map<String,String> mapart = Articolo_DAO.getArticoliMap();
			
			mapqt = new ArrayList<Articolo>();
			
			for (int i = 1; i <= classDB.getRecordCount(); i++)
			{
				classDB.read();
//				if (anno.get().equals("2013") 
//					&& codart.get()!=null && !codart.get().trim().isEmpty()
//					&& giaini.get()!=null && !giaini.get().trim().isEmpty()
//					&& procar.get()!=null && !procar.get().trim().isEmpty()
//					&& prosca.get()!=null && !prosca.get().trim().isEmpty()){
				if (anno.get().equals("2013") && codart.get()!=null && !codart.get().trim().isEmpty()
						&& ((giaini.get()!=null && !giaini.get().trim().isEmpty())
						|| (procar.get()!=null && !procar.get().trim().isEmpty())
						|| (prosca.get()!=null && !prosca.get().trim().isEmpty()))){
				
					String cod = codart.get().trim();
					
					if (mapart.containsKey(cod)){
//						double ini = Double.valueOf(giaini.get().trim());
//						double car = Double.valueOf(procar.get().trim());
//						double sca = Double.valueOf(prosca.get().trim());
						double ini = 0;
						double car = 0;
						double sca = 0;
						
						if (giaini.get()!=null && !giaini.get().trim().isEmpty())
							ini = Double.valueOf(giaini.get().trim());
						if (procar.get()!=null && !procar.get().trim().isEmpty())
							car = Double.valueOf(procar.get().trim());
						if (prosca.get()!=null && !prosca.get().trim().isEmpty())
							sca = Double.valueOf(prosca.get().trim());
						double tot = ini+car-sca;
					
						Articolo a = new Articolo();
						a.setCodice(cod);
						a.setQuantitaMagazzino((int)Math.round(tot));
						a.setQuantitaEffettiva((int)Math.round(tot));
						mapqt.add(a);
					}
						//System.out.println(cod+"  TOT: "+tot+". Zeus: "+mapart.get(cod));
				}		
			}
			System.out.println("Fine lettura dbf...");
		}catch(Exception e){
			e.printStackTrace();
		}
		return mapqt;
	}
	
	public static int getGiacenzaArticolo(String percorso, String codice_articolo){	
		System.out.println("Inizio lettura giacenze da "+percorso+" ...");
		int giacenza = 0;
		
		try{
			//Open dbf file
			DBF classDB=new DBF(percorso);

			//Define fields
			CharField anno  = (CharField) classDB.getField("ANNO");
			CharField codart  = (CharField) classDB.getField("CODART");
			NumField giaini = (NumField) classDB.getField("GIAINI");
			NumField procar  = (NumField) classDB.getField("PROCAR");			
			NumField prosca  = (NumField) classDB.getField("PROSCA");
			
			boolean trovato = false;
			
			int i = 1;
			
			while (i <= classDB.getRecordCount() && !trovato){
				
				classDB.read();
				
				if (anno.get().equals("2013") && codart.get()!=null && !codart.get().trim().isEmpty()
					&& ((giaini.get()!=null && !giaini.get().trim().isEmpty())
					|| (procar.get()!=null && !procar.get().trim().isEmpty())
					|| (prosca.get()!=null && !prosca.get().trim().isEmpty()))){
					
					String cod = codart.get().trim().replace("/", "_");;
					
					if (codice_articolo.equals(cod)){
						trovato = true;
						double ini = 0;
						double car = 0;
						double sca = 0;
						
						if (giaini.get()!=null && !giaini.get().trim().isEmpty())
							ini = Double.valueOf(giaini.get().trim());
						if (procar.get()!=null && !procar.get().trim().isEmpty())
							car = Double.valueOf(procar.get().trim());
						if (prosca.get()!=null && !prosca.get().trim().isEmpty())
							sca = Double.valueOf(prosca.get().trim());
						
						double tot = ini+car-sca;
					
					
						giacenza = ((int)Math.round(tot));
						
						//System.out.println(cod+"  TOT: "+tot);
					}
						
				}
				i++;
			}
			System.out.println("Fine lettura dbf...");
		}catch(Exception e){
			e.printStackTrace();
		}
		return giacenza;
	}
	
	@SuppressWarnings("unused")
	public static List<Articolo> getArticoliFromDbf(String percorso, boolean soloNuovi){	
		System.out.println("Inizio lettura articoli da "+percorso+" ...");
		List<Articolo> list = null;
		try{
			//Open dbf file
			DBF classDB=new DBF(percorso);

			//Define fields
			CharField codart  = (CharField) classDB.getField("CODART");
			CharField desart  = (CharField) classDB.getField("DESART");
			CharField unmis  = (CharField) classDB.getField("UNMIS");
			CharField aliva  = (CharField) classDB.getField("ALIVA");
			NumField prezzo1  = (NumField) classDB.getField("PREZZO1");
			NumField prezzo2  = (NumField) classDB.getField("PREZZO2");
			NumField costost  = (NumField) classDB.getField("COSTOST");
			NumField provv  = (NumField) classDB.getField("PROVV");
//			CharField vendi  = (CharField) classDB.getField("VENDI");
//			CharField costo  = (CharField) classDB.getField("COSTO");
			NumField sconto  = (NumField) classDB.getField("SCONTO");
			NumField sconto2  = (NumField) classDB.getField("SCONTO2");
			NumField sconto3  = (NumField) classDB.getField("SCONTO3");
			CharField merce1  = (CharField) classDB.getField("MERCE1");
			DateField uldtvar  = (DateField) classDB.getField("ULDTVAR");
//			CharField artaltern  = (CharField) classDB.getField("ARTALTERN");
			CharField codfor  = (CharField) classDB.getField("CODFOR");
			CharField usafor  = (CharField) classDB.getField("USAFOR");
			CharField desrid  = (CharField) classDB.getField("DESRID");
//			CharField ragfis  = (CharField) classDB.getField("RAGFIS");
			DateField datins  = (DateField) classDB.getField("DATINS");
			CharField codean  = (CharField) classDB.getField("CODEAN");
			CharField tipbar  = (CharField) classDB.getField("TIPBAR");
//			CharField pesvar  = (CharField) classDB.getField("PESVAR");
//			CharField proazi  = (CharField) classDB.getField("PROAZI");
//			CharField db  = (CharField) classDB.getField("DB");
//			CharField gm  = (CharField) classDB.getField("GM");
//			CharField lifo  = (CharField) classDB.getField("LIFO");
//			CharField tipcon  = (CharField) classDB.getField("TIPCON");
//			CharField tipimb  = (CharField) classDB.getField("TIPIMB");
//			NumField pesuni  = (NumField) classDB.getField("PESUNI");
//			NumField pezcon  = (NumField) classDB.getField("PEZCON");
//			NumField pescon  = (NumField) classDB.getField("PESCON");
//			NumField pezban  = (NumField) classDB.getField("PEZBAN");
//			NumField volume  = (NumField) classDB.getField("VOLUME");
//			NumField temapp  = (NumField) classDB.getField("TEMAPP");
//			CharField dimart  = (CharField) classDB.getField("DIMART");
//			CharField ragsta  = (CharField) classDB.getField("RAGSTA");
//			CharField divacq  = (CharField) classDB.getField("DIVACQ");
//			CharField divven  = (CharField) classDB.getField("DIVVEN");
//			CharField conart  = (CharField) classDB.getField("CONART");
//			CharField claric  = (CharField) classDB.getField("CLARIC");
//			CharField ubiart  = (CharField) classDB.getField("UBIART");
//			MemoField memoart  = (MemoField) classDB.getField("MEMOART");
//			CharField tipsco  = (CharField) classDB.getField("TIPSCO");
//			CharField artwe  = (CharField) classDB.getField("ARTWE");
//			CharField checkwe  = (CharField) classDB.getField("CHECKWE");
//			CharField ultmod  = (CharField) classDB.getField("ULTMOD");
//			CharField categana  = (CharField) classDB.getField("CATEGANA");
//			CharField obsole  = (CharField) classDB.getField("OBSOLE");
//			CharField desart2  = (CharField) classDB.getField("DESART2");
//			CharField tipdiba  = (CharField) classDB.getField("TIPDIBA");
//			CharField gestlotti  = (CharField) classDB.getField("GESTLOTTI");
//			CharField tipolotto  = (CharField) classDB.getField("TIPOLOTTO");
//			CharField alias  = (CharField) classDB.getField("ALIAS");
//			CharField nomcomb  = (CharField) classDB.getField("NOMCOMB");
//			NumField moltipl  = (NumField) classDB.getField("MOLTIPL");
//			NumField massanet  = (NumField) classDB.getField("MASSANET");
//			CharField servizio  = (CharField) classDB.getField("SERVIZIO");

			list = new ArrayList<Articolo>();
			int j = 0;
			
			Map<Integer,Categoria> mapcat = Categoria_DAO.getMappaCategorieGestionale();
			
			for (int i = 1; i <= classDB.getRecordCount(); i++)
			{
				classDB.read();
				
					
				
				Date dataIns = Date.valueOf(DateMethods.modificaDate(datins.get().trim()));
				Date d = new Date(DateMethods.calcolaMesePrecedente(new java.util.Date()).getTime());
				
				Date dataUltVar = Date.valueOf(DateMethods.modificaDate(uldtvar.get().trim()));
				Date d1 = new Date(DateMethods.calcolaMesePrecedente(new java.util.Date()).getTime());
				
				if (soloNuovi /* && (dataIns.after(d) || dataUltVar.after(d1)) */ || !desart.get().trim().isEmpty()){
				
					Articolo a = new Articolo();
					a.setCodice(codart.get().trim().replace("/", "_"));
					a.setNome(desart.get().trim().toUpperCase());
					if(merce1.get()!=null && !merce1.get().trim().equals(""))
						a.setIdCategoriaGestionale(Integer.valueOf(merce1.get().trim()));
					Categoria c = mapcat.get(a.getIdCategoriaGestionale());
					if (c!=null)	a.setIdCategoria(c.getIdCategoria());
					a.setUnitaMisura(unmis.get().trim());
					if(aliva.get()!=null && !aliva.get().trim().equals(""))
						a.setAliquotaIva(Integer.valueOf(aliva.get().trim()));
					if(prezzo1.get()!=null && !prezzo1.get().trim().equals(""))	
						a.setPrezzoIngrosso(Double.valueOf(prezzo1.get().trim()));
					if(prezzo2.get()!=null && !prezzo2.get().trim().equals(""))
						a.setPrezzoDettaglio(Double.valueOf(prezzo2.get().trim()));
					if(costost.get()!=null && !costost.get().trim().equals(""))
						a.setCostoAcquisto(Double.valueOf(costost.get().trim()));
					
					a.setCodiceFornitore(codfor.get().trim());
					a.setCodiceArticoloFornitore(usafor.get().trim());
					a.setDescrizioneCategoria(desrid.get().trim());
					if(codean.get()!=null && !codean.get().trim().equals(""))
						a.setCodiceBarre(codean.get().replace("'", "").trim());
					if(tipbar.get()!=null && !tipbar.get().trim().equals("")){
						String tipcodbar = "";
						if(tipbar.get().trim().equals("3"))
							tipcodbar = "EAN-13";
						else if (tipbar.get().trim().equals("4"))	tipcodbar = "EAN-8";
						else if (tipbar.get().trim().equals("7"))	tipcodbar = "UPC-E";
						a.setTipoCodiceBarre(tipcodbar);
					}
					if(datins.get()!=null && !datins.get().trim().equals(""))						
						a.setDataInserimento(Date.valueOf(DateMethods.modificaDate(datins.get().trim())));
					
					if(uldtvar.get()!=null && !uldtvar.get().trim().equals(""))
						a.setDataUltimaModifica(Date.valueOf(DateMethods.modificaDate(uldtvar.get().trim())));
					
					list.add(a);
					j++;
				}
			}
			System.out.println("Fine lettura dbf... "+j+" occorrenze valide");
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	
	public static List<Articolo> getArticoliFromDbf(String percorso, boolean soloNuovi, boolean nome,java.util.Date syncData,boolean iva,boolean prezzoDettaglio,boolean prezzoIngrosso,
			boolean costoAcquisto, boolean categoria, boolean codiceFornitore,boolean codiceArticoloFornitore,boolean codiceBarre,boolean tipoCodiceBarre){	
		System.out.println("Inizio letture articoli da "+percorso+" ...");
		List<Articolo> list = null;
		try{
			//Open dbf file
			DBF classDB=new DBF(percorso);

			//Define fields
			CharField codart  = (CharField) classDB.getField("CODART");
			CharField desart  = (CharField) classDB.getField("DESART");
			CharField unmis  = (CharField) classDB.getField("UNMIS");
			CharField aliva  = (CharField) classDB.getField("ALIVA");
			NumField prezzo1  = (NumField) classDB.getField("PREZZO1");
			NumField prezzo2  = (NumField) classDB.getField("PREZZO2");
			NumField costost  = (NumField) classDB.getField("COSTOST");
//			NumField provv  = (NumField) classDB.getField("PROVV");
//			CharField vendi  = (CharField) classDB.getField("VENDI");
//			CharField costo  = (CharField) classDB.getField("COSTO");
//			NumField sconto  = (NumField) classDB.getField("SCONTO");
//			NumField sconto2  = (NumField) classDB.getField("SCONTO2");
//			NumField sconto3  = (NumField) classDB.getField("SCONTO3");
			CharField merce1  = (CharField) classDB.getField("MERCE1");
			DateField uldtvar  = (DateField) classDB.getField("ULDTVAR");
//			CharField artaltern  = (CharField) classDB.getField("ARTALTERN");
			CharField codfor  = (CharField) classDB.getField("CODFOR");
			CharField usafor  = (CharField) classDB.getField("USAFOR");
			CharField desrid  = (CharField) classDB.getField("DESRID");
//			CharField ragfis  = (CharField) classDB.getField("RAGFIS");
			DateField datins  = (DateField) classDB.getField("DATINS");
			CharField codean  = (CharField) classDB.getField("CODEAN");
			CharField tipbar  = (CharField) classDB.getField("TIPBAR");
//			CharField pesvar  = (CharField) classDB.getField("PESVAR");
//			CharField proazi  = (CharField) classDB.getField("PROAZI");
//			CharField db  = (CharField) classDB.getField("DB");
//			CharField gm  = (CharField) classDB.getField("GM");
//			CharField lifo  = (CharField) classDB.getField("LIFO");
//			CharField tipcon  = (CharField) classDB.getField("TIPCON");
//			CharField tipimb  = (CharField) classDB.getField("TIPIMB");
//			NumField pesuni  = (NumField) classDB.getField("PESUNI");
//			NumField pezcon  = (NumField) classDB.getField("PEZCON");
//			NumField pescon  = (NumField) classDB.getField("PESCON");
//			NumField pezban  = (NumField) classDB.getField("PEZBAN");
//			NumField volume  = (NumField) classDB.getField("VOLUME");
//			NumField temapp  = (NumField) classDB.getField("TEMAPP");
//			CharField dimart  = (CharField) classDB.getField("DIMART");
//			CharField ragsta  = (CharField) classDB.getField("RAGSTA");
//			CharField divacq  = (CharField) classDB.getField("DIVACQ");
//			CharField divven  = (CharField) classDB.getField("DIVVEN");
//			CharField conart  = (CharField) classDB.getField("CONART");
//			CharField claric  = (CharField) classDB.getField("CLARIC");
//			CharField ubiart  = (CharField) classDB.getField("UBIART");
//			MemoField memoart  = (MemoField) classDB.getField("MEMOART");
//			CharField tipsco  = (CharField) classDB.getField("TIPSCO");
//			CharField artwe  = (CharField) classDB.getField("ARTWE");
//			CharField checkwe  = (CharField) classDB.getField("CHECKWE");
//			CharField ultmod  = (CharField) classDB.getField("ULTMOD");
//			CharField categana  = (CharField) classDB.getField("CATEGANA");
//			CharField obsole  = (CharField) classDB.getField("OBSOLE");
//			CharField desart2  = (CharField) classDB.getField("DESART2");
//			CharField tipdiba  = (CharField) classDB.getField("TIPDIBA");
//			CharField gestlotti  = (CharField) classDB.getField("GESTLOTTI");
//			CharField tipolotto  = (CharField) classDB.getField("TIPOLOTTO");
//			CharField alias  = (CharField) classDB.getField("ALIAS");
//			CharField nomcomb  = (CharField) classDB.getField("NOMCOMB");
//			NumField moltipl  = (NumField) classDB.getField("MOLTIPL");
//			NumField massanet  = (NumField) classDB.getField("MASSANET");
//			CharField servizio  = (CharField) classDB.getField("SERVIZIO");

			list = new ArrayList<Articolo>();
			int j = 0;
			
			Map<Integer,Categoria> mapcat = Categoria_DAO.getMappaCategorieGestionale();
			Map<String,String> mapart = Articolo_DAO.getArticoliMap();
			
			for (int i = 1; i <= classDB.getRecordCount(); i++)
			{
				classDB.read();
				
					
				Date dataIns = Date.valueOf(DateMethods.modificaDate(datins.get().trim()));
				//Date d = new Date(DateMethods.calcolaMesePrecedente(new java.util.Date()).getTime());
				
				Date dataUltVar = Date.valueOf(DateMethods.modificaDate(uldtvar.get().trim()));
				//Date d1 = new Date(DateMethods.calcolaMesePrecedente(new java.util.Date()).getTime());
				
				if ((syncData!=null && (dataIns.after(syncData) || dataUltVar.after(syncData)))  && !desart.get().trim().isEmpty()){
				
					
					String cod = codart.get().trim().replace("/", "_");
					
					if (soloNuovi && mapart.containsKey(cod)){}
					else {
						
						Articolo a = new Articolo();
						a.setCodice(cod);
						if (nome) a.setNome(desart.get().trim().toUpperCase());
						if (categoria){
							if(merce1.get()!=null && !merce1.get().trim().equals(""))
								a.setIdCategoriaGestionale(Integer.valueOf(merce1.get().trim()));
							Categoria c = mapcat.get(a.getIdCategoriaGestionale());
							if (c!=null)	a.setIdCategoria(c.getIdCategoria()); 
						}
						a.setUnitaMisura(unmis.get().trim());
						
						if (iva){
							if(aliva.get()!=null && !aliva.get().trim().equals(""))
								a.setAliquotaIva(Integer.valueOf(aliva.get().trim()));
						}
						if (prezzoIngrosso){
							if(prezzo1.get()!=null && !prezzo1.get().trim().equals(""))	
								a.setPrezzoIngrosso(Double.valueOf(prezzo1.get().trim()));
						}
						if (prezzoDettaglio){
							if(prezzo2.get()!=null && !prezzo2.get().trim().equals(""))
								a.setPrezzoDettaglio(Double.valueOf(prezzo2.get().trim()));
						}
						if (costoAcquisto){
							if(costost.get()!=null && !costost.get().trim().equals(""))
								a.setCostoAcquisto(Double.valueOf(costost.get().trim()));
						}
						
						
						if (codiceFornitore) a.setCodiceFornitore(codfor.get().trim());
						if (codiceArticoloFornitore) a.setCodiceArticoloFornitore(usafor.get().trim());
						a.setDescrizioneCategoria(desrid.get().trim());
						
						if (codiceBarre){
							if(codean.get()!=null && !codean.get().trim().equals(""))
								a.setCodiceBarre(codean.get().replace("'", "").trim());
						}
						if (tipoCodiceBarre){
							if(tipbar.get()!=null && !tipbar.get().trim().equals("")){
								String tipcodbar = "";
								if(tipbar.get().trim().equals("3"))
									tipcodbar = "EAN-13";
								else if (tipbar.get().trim().equals("4"))	tipcodbar = "EAN-8";
								else if (tipbar.get().trim().equals("7"))	tipcodbar = "UPC-E";
								a.setTipoCodiceBarre(tipcodbar);
							}
						}
						if(datins.get()!=null && !datins.get().trim().equals(""))						
							a.setDataInserimento(Date.valueOf(DateMethods.modificaDate(datins.get().trim())));
						
						if(uldtvar.get()!=null && !uldtvar.get().trim().equals(""))
							a.setDataUltimaModifica(Date.valueOf(DateMethods.modificaDate(uldtvar.get().trim())));
						
						list.add(a);
						j++;
					}
				}
			}
			System.out.println("Fine lettura dbf... "+j+" occorrenze valide");
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	
	
	public static Articolo getArticoloFromDbf(String percorso, String codice_articolo){	
		System.out.println("Inizio lettura da "+percorso+" ...");
		Articolo a = null;
		try{
			//Open dbf file
			DBF classDB=new DBF(percorso);

			//Define fields
			CharField codart  = (CharField) classDB.getField("CODART");
			CharField desart  = (CharField) classDB.getField("DESART");
			CharField unmis  = (CharField) classDB.getField("UNMIS");
			CharField aliva  = (CharField) classDB.getField("ALIVA");
			NumField prezzo1  = (NumField) classDB.getField("PREZZO1");
			NumField prezzo2  = (NumField) classDB.getField("PREZZO2");
			NumField costost  = (NumField) classDB.getField("COSTOST");
//			NumField provv  = (NumField) classDB.getField("PROVV");
//			NumField sconto  = (NumField) classDB.getField("SCONTO");
//			NumField sconto2  = (NumField) classDB.getField("SCONTO2");
//			NumField sconto3  = (NumField) classDB.getField("SCONTO3");
			CharField merce1  = (CharField) classDB.getField("MERCE1");
			DateField uldtvar  = (DateField) classDB.getField("ULDTVAR");
			CharField codfor  = (CharField) classDB.getField("CODFOR");
			CharField usafor  = (CharField) classDB.getField("USAFOR");
			CharField desrid  = (CharField) classDB.getField("DESRID");
			DateField datins  = (DateField) classDB.getField("DATINS");
			CharField codean  = (CharField) classDB.getField("CODEAN");
			CharField tipbar  = (CharField) classDB.getField("TIPBAR");

			a = new Articolo();
			boolean trovato = false;
			
			Map<Integer,Categoria> mapcat = Categoria_DAO.getMappaCategorieGestionale();
			
			for (int i = 1; i <= classDB.getRecordCount(); i++)
			{
				classDB.read();				
				
				if (codice_articolo.equals(codart.get().trim().replace("/", "_")) && !desart.get().trim().isEmpty()){
					
					trovato = true;
					a.setCodice(codart.get().trim().replace("/", "_"));
					a.setNome(desart.get().trim().toUpperCase());
					if(merce1.get()!=null && !merce1.get().trim().equals(""))
						a.setIdCategoriaGestionale(Integer.valueOf(merce1.get().trim()));
					Categoria c = mapcat.get(a.getIdCategoriaGestionale());
					if (c!=null)	a.setIdCategoria(c.getIdCategoria());
					a.setUnitaMisura(unmis.get().trim());
					if(aliva.get()!=null && !aliva.get().trim().equals(""))
						a.setAliquotaIva(Integer.valueOf(aliva.get().trim()));
					if(prezzo1.get()!=null && !prezzo1.get().trim().equals(""))	
						a.setPrezzoIngrosso(Double.valueOf(prezzo1.get().trim()));
					if(prezzo2.get()!=null && !prezzo2.get().trim().equals(""))
						a.setPrezzoDettaglio(Double.valueOf(prezzo2.get().trim()));
					if(costost.get()!=null && !costost.get().trim().equals(""))
						a.setCostoAcquisto(Double.valueOf(costost.get().trim()));
					
					a.setCodiceFornitore(codfor.get().trim());
					a.setCodiceArticoloFornitore(usafor.get().trim());
					a.setDescrizioneCategoria(desrid.get().trim());
					if(codean.get()!=null && !codean.get().trim().equals(""))
						a.setCodiceBarre(codean.get().replace("'", "").trim());
					if(tipbar.get()!=null && !tipbar.get().trim().equals("")){
						String tipcodbar = "";
						if(tipbar.get().trim().equals("3"))
							tipcodbar = "EAN-13";
						else if (tipbar.get().trim().equals("4"))	tipcodbar = "EAN-8";
						else if (tipbar.get().trim().equals("7"))	tipcodbar = "UPC-E";
						a.setTipoCodiceBarre(tipcodbar);
					}
					
					if(datins.get()!=null && !datins.get().trim().equals(""))						
						a.setDataInserimento(Date.valueOf(DateMethods.modificaDate(datins.get().trim())));
					
					if(uldtvar.get()!=null && !uldtvar.get().trim().equals(""))
						a.setDataUltimaModifica(Date.valueOf(DateMethods.modificaDate(uldtvar.get().trim())));										
				}				
			}
			if (trovato) System.out.println("Articolo trovato: "+a.getNome());
			else System.out.println("Articolo non trovato.");
		}catch(Exception e){
			e.printStackTrace();
		}
		return a;
	}
	
	
	
	public static List<Articolo> syncArticoli(String percorso, Map<String,Boolean> whatToSync,java.util.Date syncData){	
		Log.debug("Inizio lettura file "+percorso+" ...");
		List<Articolo> list = null;
		try{
			//Open dbf file
			DBF classDB=new DBF(percorso);

			//Define fields
			CharField codart  = (CharField) classDB.getField("CODART");
			CharField desart  = (CharField) classDB.getField("DESART");
			CharField desart2  = (CharField) classDB.getField("DESART2");
			CharField unmis  = (CharField) classDB.getField("UNMIS");
			CharField aliva  = (CharField) classDB.getField("ALIVA");
			NumField prezzo1  = (NumField) classDB.getField("PREZZO1");
			NumField prezzo2  = (NumField) classDB.getField("PREZZO2");
			NumField costost  = (NumField) classDB.getField("COSTOST");
//			NumField provv  = (NumField) classDB.getField("PROVV");
//			NumField sconto  = (NumField) classDB.getField("SCONTO");
//			NumField sconto2  = (NumField) classDB.getField("SCONTO2");
//			NumField sconto3  = (NumField) classDB.getField("SCONTO3");
			CharField merce1  = (CharField) classDB.getField("MERCE1");
			DateField uldtvar  = (DateField) classDB.getField("ULDTVAR");
			CharField codfor  = (CharField) classDB.getField("CODFOR");
			CharField usafor  = (CharField) classDB.getField("USAFOR");
			CharField desrid  = (CharField) classDB.getField("DESRID");
			DateField datins  = (DateField) classDB.getField("DATINS");
			CharField codean  = (CharField) classDB.getField("CODEAN");
			CharField tipbar  = (CharField) classDB.getField("TIPBAR");


			list = new ArrayList<Articolo>();
			int x = 0;	/* articoli modificati */
			int y = 0;	/* articoli nuovi */
			
			Map<Integer,Categoria> mapcat = Categoria_DAO.getMappaCategorieGestionale();
			Map<String,Articolo> mapart = ArticoloBusiness.getInstance().getMappaArticoli();
			
			for (int i = 1; i <= classDB.getRecordCount(); i++)
			{
				classDB.read();
									
				Date dataIns = Date.valueOf(DateMethods.modificaDate(datins.get().trim()));				
				Date dataUltVar = Date.valueOf(DateMethods.modificaDate(uldtvar.get().trim()));
				
				if ((syncData!=null && (dataIns.after(syncData) || dataUltVar.after(syncData)))  && !desart.get().trim().isEmpty()){
								
					String cod = codart.get().trim().replace("/", "_");
					boolean qualcosaEcambiato = false;
					
					if (mapart.containsKey(cod)){
						if (!whatToSync.get("soloNuovi")){
							Articolo a = mapart.get(cod);
							
							String note = "";
							
							//String riga = cod+"	";
							
							if (whatToSync.get("nome") && desart.get()!=null && !a.getNome().toUpperCase().equals(desart.get().trim().toUpperCase())){
								//scrivere su file testo che è cambiato il nome
								//riga+=a.getNome()+"	"+desart.get().trim().toUpperCase()+"	";
								note+="Nome da \""+a.getNome()+"\" a \""+desart.get().trim().toUpperCase()+"\". ";
								a.setNome(desart.get().trim().toUpperCase().replace("'", "\'"));
								qualcosaEcambiato = true;
							} //else riga+=a.getNome()+"		";
							
							if (whatToSync.get("dimensioni") && desart2.get()!=null && !a.getDimensioni().toUpperCase().equals(desart2.get().trim().toUpperCase())){
								//scrivere su file testo che è cambiato il nome
								//riga+=a.getNome()+"	"+desart.get().trim().toUpperCase()+"	";
								note+="Dimensioni da \""+a.getDimensioni()+"\" a \""+desart2.get().trim().toUpperCase()+"\". ";
								a.setDimensioni(desart2.get().trim().toUpperCase().replace("'", "\'"));
								qualcosaEcambiato = true;
							}
							
							if (whatToSync.get("codiceBarre") && codean.get()!=null && a.getCodiceBarre()!=null && (!codean.get().trim().equals("") && !a.getCodiceBarre().equals(codean.get().replace("'", "").trim()))){
								//cambiato codean
								//riga+=a.getCodiceBarre()+"	"+codean.get().trim()+"	";
								note+="Codice a barre da \""+a.getCodiceBarre()+"\" a \""+codean.get().replace("'", "").trim()+"\". ";
								a.setCodiceBarre(codean.get().replace("'", "").trim());
								qualcosaEcambiato = true;
							} else if (whatToSync.get("codiceBarre") && codean.get()!=null && a.getCodiceBarre()==null){
								note+="Codice a barre da NULLO a \""+codean.get().replace("'", "").trim()+"\". ";
								a.setCodiceBarre(codean.get().replace("'", "").trim());
								qualcosaEcambiato = true;
							}
							
							if (whatToSync.get("prezzoIngrosso") && prezzo1.get()!=null && !prezzo1.get().trim().equals("") && a.getPrezzoIngrosso()!=Double.valueOf(prezzo1.get().trim())){	
								//scrivere che è cambiato questo prezzo
								//riga+=a.getPrezzoIngrosso()+"	"+prezzo1.get().trim()+"	";
								note+="Prezzo ingrosso da \""+a.getPrezzoIngrosso()+"\" a \""+prezzo1.get().trim()+"\". ";
								a.setPrezzoIngrosso(Double.valueOf(prezzo1.get().trim()));
								qualcosaEcambiato = true;
							} //else riga+="		";
							
							if (whatToSync.get("prezzoDettaglio") && prezzo2.get()!=null && !prezzo2.get().trim().equals("") && a.getPrezzoDettaglio()!=Double.valueOf(prezzo2.get().trim())){
								//scrivere che è cambiato questo prezzo
								//riga+=a.getPrezzoDettaglio()+"	"+prezzo2.get().trim()+"	";
								note+="Prezzo dettaglio da \""+a.getPrezzoDettaglio()+"\" a \""+prezzo2.get().trim()+"\". ";
								a.setPrezzoDettaglio(Double.valueOf(prezzo2.get().trim()));
								qualcosaEcambiato = true;
							} //else riga+="		";
							
							if (whatToSync.get("costoAcquisto") && costost.get()!=null && !costost.get().trim().equals("") && a.getCostoAcquisto()!=Double.valueOf(costost.get().trim())){
								//scrivere che è cambiato questo prezzo
								//riga+=a.getCostoAcquisto()+"	"+costost.get().trim()+"	";
								note+="Costo acquisto da \""+a.getCostoAcquisto()+"\" a \""+costost.get().trim()+"\". ";
								a.setCostoAcquisto(Double.valueOf(costost.get().trim()));
								qualcosaEcambiato = true;
							} //else riga+="		";
							
							if (whatToSync.get("categoria") && merce1.get()!=null && !merce1.get().trim().equals("") && a.getIdCategoriaGestionale()!=Integer.valueOf(merce1.get().trim())){
								note+="Categoria Gestionale da \""+a.getIdCategoriaGestionale()+"\" a \""+Integer.valueOf(merce1.get().trim())+"\". ";
								a.setIdCategoriaGestionale(Integer.valueOf(merce1.get().trim()));
								Categoria c = mapcat.get(a.getIdCategoriaGestionale());
								if (c!=null)	a.setIdCategoria(c.getIdCategoria()); 
								qualcosaEcambiato = true;
							}
														
							if (whatToSync.get("iva") && aliva.get()!=null && !aliva.get().trim().equals("") && a.getAliquotaIva()!=Integer.valueOf(aliva.get().trim())){
								//scrivere che è cambiata iva
								//riga+=a.getAliquotaIva()+"	"+aliva.get().trim()+"	";
								note+="IVA da \""+a.getAliquotaIva()+"\" a \""+Integer.valueOf(aliva.get().trim())+"\". ";
								a.setAliquotaIva(Integer.valueOf(aliva.get().trim()));
								qualcosaEcambiato = true;
							}							
							
							if (whatToSync.get("codiceFornitore") && codfor.get()!=null && !a.getCodiceFornitore().equals(codfor.get().trim()))  {
								//scrivere che è cambiato cod fornitore
								note+="Codice Fornitore da \""+a.getCodiceFornitore()+"\" a \""+codfor.get().trim()+"\". ";
								a.setCodiceFornitore(codfor.get().trim());
								qualcosaEcambiato = true;
							}
							if (whatToSync.get("codiceArticoloFornitore") && usafor.get()!=null && !a.getCodiceArticoloFornitore().equals(usafor.get().trim())) {
								//scrivere che è cambiato cod art fornitore
								note+="Codice Articolo Fornitore da \""+a.getCodiceArticoloFornitore()+"\" a \""+usafor.get().trim()+"\". ";
								a.setCodiceArticoloFornitore(usafor.get().trim());
								qualcosaEcambiato = true;
							}													
							
							
							if (whatToSync.get("tipoCodiceBarre") && tipbar.get()!=null && !tipbar.get().trim().equals("")){
								String tipcodbar = "";
								if(tipbar.get().trim().equals("3"))
									tipcodbar = "EAN-13";
								else if (tipbar.get().trim().equals("4"))	tipcodbar = "EAN-8";
								else if (tipbar.get().trim().equals("7"))	tipcodbar = "UPC-E";
								a.setTipoCodiceBarre(tipcodbar);
								note+="Tipo Codice a barre.";
								qualcosaEcambiato=true;
							}
							
							a.setUnitaMisura(unmis.get().trim());
							a.setDescrizioneCategoria(desrid.get().trim());
							
							a.setDataInserimento(dataIns);
							
							a.setDataUltimaModifica(dataUltVar);
							
							if (qualcosaEcambiato){
								a.setNote(note);
								list.add(a);
							}
							x++;					
						}
						else {/* caso in cui l'articolo già esiste ma devo sincronizzare solo quelli nuovi: non fare nulla */}
					}
					
					else {
						//caso in cui l'articolo non è contenuto nella mappa
						String riga = "";
						
						riga+=cod+"		";
						riga+=desart.get().trim().toUpperCase()+"		";
						riga+=codean.get().replace("'", "").trim()+"		";
						riga+=prezzo1.get().trim();
						riga+=prezzo2.get().trim()+"		";
						riga+=costost.get().trim()+"		";
						riga+=aliva.get().trim();
						Methods.scriviSuReport(riga);
						
						Articolo a = new Articolo();
						
						a.setCodice(cod);
						a.setNome(desart.get().trim().toUpperCase());
						if(merce1.get()!=null && !merce1.get().trim().equals(""))
							a.setIdCategoriaGestionale(Integer.valueOf(merce1.get().trim()));
						Categoria c = mapcat.get(a.getIdCategoriaGestionale());
						if (c!=null)	a.setIdCategoria(c.getIdCategoria());
						a.setUnitaMisura(unmis.get().trim());
						if(aliva.get()!=null && !aliva.get().trim().equals(""))
							a.setAliquotaIva(Integer.valueOf(aliva.get().trim()));
						if(prezzo1.get()!=null && !prezzo1.get().trim().equals(""))	
							a.setPrezzoIngrosso(Double.valueOf(prezzo1.get().trim()));
						if(prezzo2.get()!=null && !prezzo2.get().trim().equals(""))
							a.setPrezzoDettaglio(Double.valueOf(prezzo2.get().trim()));
						if(costost.get()!=null && !costost.get().trim().equals(""))
							a.setCostoAcquisto(Double.valueOf(costost.get().trim()));
						
						a.setCodiceFornitore(codfor.get().trim());
						a.setCodiceArticoloFornitore(usafor.get().trim());
						a.setDescrizioneCategoria(desrid.get().trim());
						if(codean.get()!=null && !codean.get().trim().equals(""))
							a.setCodiceBarre(codean.get().replace("'", "").trim());
						if(tipbar.get()!=null && !tipbar.get().trim().equals("")){
							String tipcodbar = "";
							if(tipbar.get().trim().equals("3"))
								tipcodbar = "EAN-13";
							else if (tipbar.get().trim().equals("4"))	tipcodbar = "EAN-8";
							else if (tipbar.get().trim().equals("7"))	tipcodbar = "UPC-E";
							a.setTipoCodiceBarre(tipcodbar);
						}
						
						a.setDataInserimento(dataIns);
						
						a.setDataUltimaModifica(dataUltVar);	
						
						list.add(a);
						y++;
					}					

				}
			}
			Log.debug("Fine lettura file, "+y+" nuovi articoli e "+x+" da modificare.");
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	
	
	
	public static Map<String,List<String>> syncCodiciBarreVarianti(String percorso){	
		Log.debug("Sincronizzazione codici a barre varianti, inizio lettura file "+percorso+" ...");
		
		Map<String,List<String>> values = null;
		
		try{
			//Open dbf file
			DBF classDB=new DBF(percorso);

			//Define fields
			CharField codart  = (CharField) classDB.getField("CODART");
			CharField codean  = (CharField) classDB.getField("CODEAN");

			values = new HashMap<String,List<String>>();
			
			for (int i = 1; i <= classDB.getRecordCount(); i++)
			{
				classDB.read();
				
								
				String codice_articolo = codart.get().trim().replace("/", "_");
				String codice_barre = codean.get().trim();
				
				if (values.containsKey(codice_articolo)){
					values.get(codice_articolo).add(codice_barre);
				} else {
					List<String> l = new ArrayList<String>();
					l.add(codice_barre);
					values.put(codice_articolo, l);
				}				
			}
			Log.debug("Fine lettura file.");
		}catch(Exception e){
			e.printStackTrace();
		}
		return values;
	}
}
