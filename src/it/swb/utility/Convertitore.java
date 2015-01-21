package it.swb.utility;

import java.util.Map;

import it.swb.business.CategorieBusiness;
import it.swb.log.Log;
import it.swb.model.Articolo;
import it.swb.model.Categoria;
import it.swb.model.InfoGM;
import it.swb.model.InfoYatego;
import it.swb.model.Product;
import it.swb.model.Yatego_Articolo;
import it.swb.utility.Methods;

public class Convertitore {
	
	public static Product convertiArticoloInProduct(Articolo a){
		Product p = null;
		
		try {		
			InfoGM gmInfo = a.getGmInfo();
			
			p = new Product();
			
			p.setProduct_id(a.getIdArticolo());
			
			p.setProduct_ean(a.getCodice());
			p.setProduct_date_added(a.getDataInserimento());
			if (a.getDataUltimaModifica()!=null)
				p.setDate_modify(a.getDataUltimaModifica());
			else p.setDate_modify(new java.sql.Date(new java.util.Date().getTime()));
			p.setProduct_buy_price(a.getCostoAcquisto());
			p.setProduct_price(a.getPrezzoDettaglio());
	//		p.setMin_price(rs.getDouble("prezzo_ingrosso"));
	//		if (rs.getDouble("prezzo_dettaglio")-rs.getDouble("prezzo_ingrosso")!=0)
	//			p.setDifferent_prices(1);
	//		else 
			p.setDifferent_prices(0);
			p.setProduct_quantity(a.getQuantitaMagazzino());
			
			if (a.getAliquotaIva()==10) p.setProduct_tax_id(3);
			else if (a.getAliquotaIva()==4) p.setProduct_tax_id(4);
			else p.setProduct_tax_id(1);
			
			//TODO DA RIVEDERE
			String xxx[] = Methods.dividiCartellaEImmagine(a.getImmagine1());
			p.setProduct_name_image("_thumbnails/medie/"+xxx[0]+"/media_"+xxx[1]);
			p.setProduct_full_image(a.getImmagine1());
			p.setProduct_thumb_image("_thumbnails/piccole/"+xxx[0]+"/piccola_"+xxx[1]);
			
			
			p.setProduct_template("default");
			p.setProduct_manufacturer_id(0);
			p.setProduct_is_add_price(0);
			p.setAdd_price_unit_id(0);
			p.setAverage_rating(0);
			p.setReviews_count(0);
			p.setDelivery_times_id(0);
			p.setHits(0);
			p.setWeight_volume_units(0);
			p.setBasic_price_unit_id(0);
			p.setLabel_id(0);
			p.setVendor_id(0);
			p.setAccess(1);
			p.setName_it_IT(Methods.primeLettereMaiuscole(a.getNome()));
			p.setAlias_it_IT(Methods.creaAlias(a.getNome()));
			p.setShort_description_it_IT(Methods.primeLettereMaiuscole(a.getDescrizioneBreve()));
			if (a.getDescrizioneBreve()==null)
				p.setShort_description_it_IT("");
			p.setDescription_it_IT(EditorDescrizioni.creaDescrizioneSitoGM(a));
			
			p.setMeta_title_it_IT(gmInfo.getMetaTitolo());
			p.setMeta_description_it_IT(gmInfo.getMetaDescrizione());
			p.setMeta_keyword_it_IT(gmInfo.getMetaKeywords());
			
			p.setName_en_GB(gmInfo.getTitoloInglese());
			p.setAlias_en_GB(Methods.creaAlias(gmInfo.getTitoloInglese()));
			p.setShort_description_en_GB(gmInfo.getDescrizioneBreveInglese());
			if (gmInfo.getDescrizioneBreveInglese()==null)
				p.setShort_description_en_GB("");
			p.setDescription_en_GB(EditorDescrizioni.creaDescrizioneSitoGM(a));
			p.setMeta_title_en_GB(gmInfo.getMetaTitoloInglese());
			p.setMeta_description_en_GB(gmInfo.getMetaDescrizioneInglese());
			p.setMeta_keyword_en_GB(gmInfo.getMetaKeywordsInglese());
			
			p.setExtra_field_1(String.valueOf(a.getIdCategoria()));
			p.setExtra_field_2(a.getCodice());
			
			
		//campi aggiuntivi	
			p.setCodiceArticolo(a.getCodice());		
			p.setCategory_id((int)a.getIdCategoria());
			p.setImmagine(a.getImmagine1());
			p.setImmagine2(a.getImmagine2());
			p.setImmagine3(a.getImmagine3());
			p.setImmagine4(a.getImmagine4());
			p.setImmagine5(a.getImmagine5());
			p.setDimensioni(a.getDimensioni());
			p.setQuantitaInserzione(a.getQuantitaInserzione());			
			p.setVarianti(a.getVarianti());
			
			
		}
		catch (Exception e){
			Log.info(e);
			e.printStackTrace();
		}
		return p;
	}
	
	
	public static Yatego_Articolo convertiArticoloInYatego_Articolo(Articolo a){
		Yatego_Articolo y = new Yatego_Articolo();
		Map<Long, Categoria> categorie = CategorieBusiness.getInstance().getMappaCategorie();
		
		try{
			InfoYatego iy = a.getInfoYatego();
			
			y.setForeign_id(a.getCodice());
			y.setArticle_nr(a.getCodice());
			if (a.getTitoloInserzione()!=null && !a.getTitoloInserzione().isEmpty())
				y.setTitle(Methods.primeLettereMaiuscole(a.getTitoloInserzione()));
			else y.setTitle(Methods.primeLettereMaiuscole(a.getNome()));
			y.setTax(a.getAliquotaIva());
			double prezzo = a.getPrezzoDettaglio();
	  	  	if (a.getPrezzoPiattaforme()>0) prezzo = a.getPrezzoPiattaforme();
			y.setPrice(prezzo);
//			y.setPrice_uvp(price_uvp);
//			y.setPrice_purchase(price_purchase);
//			y.setTax_differtial(tax_differtial);
//			y.setUnits(units);
//			y.setDelivery_surcharge(delivery_surcharge);
//			y.setDelivery_calc_once(delivery_calc_once);
			String desc_breve;
			if (a.getDescrizioneBreve()!=null && !a.getDescrizioneBreve().isEmpty()) desc_breve = Methods.MaiuscolaDopoPunto(a.getDescrizioneBreve());
			else if (a.getTitoloInserzione()!=null && !a.getTitoloInserzione().isEmpty()) desc_breve = Methods.primeLettereMaiuscole(a.getTitoloInserzione());
			else desc_breve = Methods.primeLettereMaiuscole(a.getNome());
			y.setShort_desc(desc_breve);
			y.setLong_desc(EditorDescrizioni.creaDescrizioneYatego(a.getNome(), a.getQuantitaInserzione(), a.getDimensioni(), a.getDescrizione()));
//			y.setUrl(url);
			y.setAuto_linefeet(1);
			y.setPicture(Costanti.percorsoImmaginiRemoto+a.getImmagine1());
			if (a.getImmagine2()!=null && !a.getImmagine2().isEmpty()) y.setPicture2(Costanti.percorsoImmaginiRemoto+a.getImmagine2());
			if (a.getImmagine3()!=null && !a.getImmagine3().isEmpty()) y.setPicture3(Costanti.percorsoImmaginiRemoto+a.getImmagine3());
			if (a.getImmagine4()!=null && !a.getImmagine4().isEmpty()) y.setPicture4(Costanti.percorsoImmaginiRemoto+a.getImmagine4());
			if (a.getImmagine5()!=null && !a.getImmagine5().isEmpty()) y.setPicture5(Costanti.percorsoImmaginiRemoto+a.getImmagine5());
			
			/*  inserire in formato TESTO le Vostre categorie proprie. Possono essere di tre livelli 
			 * ad esempio: “cat1>cat2>cat3”. Il divisore deve essere il maggiore e non deve contenere spazi. 
			 * La mappatura con le categorie Yatego, viene fatta successivamente */
			Categoria c = categorie.get(a.getIdCategoria());
			y.setCategories(c.getNomeCategoriaPrincipale()+">"+c.getNomeCategoria());
			
//			
//			if (iy!=null && iy.getCategorie()!=null && !iy.getCategorie().isEmpty())
//	    		y.setCategories(a.getIdCategoria()+","+iy.getCategorie());
//			else {
//				Categoria c = categorie.get(a.getIdCategoria());
//				y.setCategories(a.getIdCategoria()+","+c.getIdCategoriaYatego());
//			}
			
			
			if (a.getVarianti()!=null && !a.getVarianti().isEmpty()){
//				String varianti = "";
//				boolean prima = true;
//				
//				for(Variante_Articolo v : a.getVarianti()){
//					String nomeVariante = v.getCodiceArticolo()+"-"+v.getValore().replace(" ", "_");
//					
//					if(nomeVariante.length()>30)
//						nomeVariante = nomeVariante.substring(0, 30);
//					
//					if (prima) {
//						varianti = nomeVariante;
//						prima = false;
//					} else varianti = varianti+","+nomeVariante;
//				}				
//				y.setVariants(varianti);	
				y.setVariants(a.getCodice());
			} 
			else y.setVariants("");
//	    	y.setDiscount_set_id(discount_set_id);
			if (a.getQuantitaMagazzino()!=0) y.setStock(a.getQuantitaMagazzino());
			else y.setStock(10);
	    	
	    	y.setDelivery_date("Gli ordini vengono evasi in 24-48 ore dalla ricezione del pagamento, i tempi di consegna con corriere espresso sono normalmente di 1-2 giorni " +
	    			"lavorativi, nel caso vi troviate in Sicilia, Calabria, Sardegna o altri CAP disagiati i tempi potrebbero aumentare a 2-3 giorni lavorativi.");
//	    	y.setQuantity_unit();
//	    	y.setPackage_size(package_size);
	    	if (iy!=null && iy.getCrossSelling()!=null && !iy.getCrossSelling().isEmpty())
	    		y.setCross_selling(iy.getCrossSelling());
	    	if (a.getCodiceBarre()!=null && !a.getCodiceBarre().isEmpty())
	    		y.setEan(a.getCodiceBarre());
//	    	y.setIsbn(isbn);
//	    	y.setManufacturer(manufacturer);
	    	if (a.getCodiceArticoloFornitore()!=null && !a.getCodiceArticoloFornitore().isEmpty())
	    		y.setMpn(a.getCodiceArticoloFornitore());
//	    	y.setDelitem(delitem);
	    	y.setStatus(1);
//	    	y.setTop_offer(top_offer);
	    	
			
		}
		catch (Exception e){
			Log.info(e);
			e.printStackTrace();
		}
		return y;
	}

}
