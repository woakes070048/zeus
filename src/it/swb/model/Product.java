package it.swb.model;

import java.sql.Date;
import java.util.List;

public class Product {
	long product_id;
	String product_ean;
	long product_quantity;
	int product_availability;
	Date product_date_added;
	Date date_modify;
	int product_publish;
	int product_tax_id;
	int currency_id;
	String product_template;
	double product_old_price;
	double product_buy_price;
	double product_price;
	double min_price;
	int different_prices;
	double product_weight;
	String product_thumb_image;
	String product_name_image;
	String product_full_image;
	int product_manufacturer_id;
	int product_is_add_price;
	int add_price_unit_id;
	double average_rating;
	int reviews_count;
	int delivery_times_id;
	long hits;
	double weight_volume_units;
	int basic_price_unit_id;
	int label_id;
	int vendor_id;
	int access;	
	String name_it_IT;
	String alias_it_IT;
	String short_description_it_IT;
	String description_it_IT;
	String meta_title_it_IT;
	String meta_description_it_IT;
	String meta_keyword_it_IT;	
	String name_en_GB;
	String alias_en_GB;
	String short_description_en_GB;
	String description_en_GB;
	String meta_title_en_GB;
	String meta_description_en_GB;
	String meta_keyword_en_GB;
	String extra_field_1;
	String extra_field_2;
	
//Campi aggiuntivi (cioè che non stanno nella tabella sul sito, ma che servono per altre cose qui
	int category_id;
	int product_ordering;
	String codiceArticolo;
	String immagine;
	String immagine2;
	String immagine3;
	String immagine4;
	String immagine5;
	String dimensioni;
	String ambitoUtilizzo;
	String quantitaInserzione;
	String titoloBreve;
	long itemId;
	List<Variante_Articolo> varianti;
	

	public Product() {
		super();
	}
	
	public long getProduct_id() {
		return product_id;
	}
	public void setProduct_id(long product_id) {
		this.product_id = product_id;
	}
	public String getProduct_ean() {
		return product_ean;
	}
	public void setProduct_ean(String product_ean) {
		this.product_ean = product_ean;
	}
	public long getProduct_quantity() {
		return product_quantity;
	}
	public void setProduct_quantity(long product_quantity) {
		this.product_quantity = product_quantity;
	}
	public int getProduct_availability() {
		return product_availability;
	}
	public void setProduct_availability(int product_availability) {
		this.product_availability = product_availability;
	}
	public Date getProduct_date_added() {
		return product_date_added;
	}
	public void setProduct_date_added(Date product_date_added) {
		this.product_date_added = product_date_added;
	}
	public Date getDate_modify() {
		return date_modify;
	}
	public void setDate_modify(Date date_modify) {
		this.date_modify = date_modify;
	}
	public int getProduct_publish() {
		return product_publish;
	}
	public void setProduct_publish(int product_publish) {
		this.product_publish = product_publish;
	}
	public int getProduct_tax_id() {
		return product_tax_id;
	}
	public void setProduct_tax_id(int product_tax_id) {
		this.product_tax_id = product_tax_id;
	}
	public int getCurrency_id() {
		return currency_id;
	}
	public void setCurrency_id(int currency_id) {
		this.currency_id = currency_id;
	}
	public String getProduct_template() {
		return product_template;
	}
	public void setProduct_template(String product_template) {
		this.product_template = product_template;
	}
	public double getProduct_old_price() {
		return product_old_price;
	}
	public void setProduct_old_price(double product_old_price) {
		this.product_old_price = product_old_price;
	}
	public double getProduct_buy_price() {
		return product_buy_price;
	}
	public void setProduct_buy_price(double product_buy_price) {
		this.product_buy_price = product_buy_price;
	}
	public double getProduct_price() {
		return product_price;
	}
	public void setProduct_price(double product_price) {
		this.product_price = product_price;
	}
	public double getMin_price() {
		return min_price;
	}
	public void setMin_price(double min_price) {
		this.min_price = min_price;
	}
	public int getDifferent_prices() {
		return different_prices;
	}
	public void setDifferent_prices(int different_prices) {
		this.different_prices = different_prices;
	}
	public double getProduct_weight() {
		return product_weight;
	}
	public void setProduct_weight(double product_weight) {
		this.product_weight = product_weight;
	}
	public String getProduct_thumb_image() {
		return product_thumb_image;
	}
	public void setProduct_thumb_image(String product_thumb_image) {
		this.product_thumb_image = product_thumb_image;
	}
	public String getProduct_name_image() {
		return product_name_image;
	}
	public void setProduct_name_image(String product_name_image) {
		this.product_name_image = product_name_image;
	}
	public String getProduct_full_image() {
		return product_full_image;
	}
	public void setProduct_full_image(String product_full_image) {
		this.product_full_image = product_full_image;
	}
	public int getProduct_manufacturer_id() {
		return product_manufacturer_id;
	}
	public void setProduct_manufacturer_id(int product_manufacturer_id) {
		this.product_manufacturer_id = product_manufacturer_id;
	}
	public int getProduct_is_add_price() {
		return product_is_add_price;
	}
	public void setProduct_is_add_price(int product_is_add_price) {
		this.product_is_add_price = product_is_add_price;
	}
	public int getAdd_price_unit_id() {
		return add_price_unit_id;
	}
	public void setAdd_price_unit_id(int add_price_unit_id) {
		this.add_price_unit_id = add_price_unit_id;
	}
	public double getAverage_rating() {
		return average_rating;
	}
	public void setAverage_rating(double average_rating) {
		this.average_rating = average_rating;
	}
	public int getReviews_count() {
		return reviews_count;
	}
	public void setReviews_count(int reviews_count) {
		this.reviews_count = reviews_count;
	}
	public int getDelivery_times_id() {
		return delivery_times_id;
	}
	public void setDelivery_times_id(int delivery_times_id) {
		this.delivery_times_id = delivery_times_id;
	}
	public long getHits() {
		return hits;
	}
	public void setHits(long hits) {
		this.hits = hits;
	}
	public double getWeight_volume_units() {
		return weight_volume_units;
	}
	public void setWeight_volume_units(double weight_volume_units) {
		this.weight_volume_units = weight_volume_units;
	}
	public int getBasic_price_unit_id() {
		return basic_price_unit_id;
	}
	public void setBasic_price_unit_id(int basic_price_unit_id) {
		this.basic_price_unit_id = basic_price_unit_id;
	}
	public int getLabel_id() {
		return label_id;
	}
	public void setLabel_id(int label_id) {
		this.label_id = label_id;
	}
	public int getVendor_id() {
		return vendor_id;
	}
	public void setVendor_id(int vendor_id) {
		this.vendor_id = vendor_id;
	}
	public int getAccess() {
		return access;
	}
	public void setAccess(int access) {
		this.access = access;
	}
	public String getName_it_IT() {
		return name_it_IT;
	}
	public void setName_it_IT(String name_it_IT) {
		this.name_it_IT = name_it_IT;
	}
	public String getAlias_it_IT() {
		return alias_it_IT;
	}
	public void setAlias_it_IT(String alias_it_IT) {
		this.alias_it_IT = alias_it_IT;
	}
	public String getShort_description_it_IT() {
		return short_description_it_IT;
	}
	public void setShort_description_it_IT(String short_description_it_IT) {
		this.short_description_it_IT = short_description_it_IT;
	}
	public String getDescription_it_IT() {
		return description_it_IT;
	}
	public void setDescription_it_IT(String description_it_IT) {
		this.description_it_IT = description_it_IT;
	}
	public String getMeta_title_it_IT() {
		return meta_title_it_IT;
	}
	public void setMeta_title_it_IT(String meta_title_it_IT) {
		this.meta_title_it_IT = meta_title_it_IT;
	}
	public String getMeta_description_it_IT() {
		return meta_description_it_IT;
	}
	public void setMeta_description_it_IT(String meta_description_it_IT) {
		this.meta_description_it_IT = meta_description_it_IT;
	}
	public String getMeta_keyword_it_IT() {
		return meta_keyword_it_IT;
	}
	public void setMeta_keyword_it_IT(String meta_keyword_it_IT) {
		this.meta_keyword_it_IT = meta_keyword_it_IT;
	}
	public String getExtra_field_1() {
		return extra_field_1;
	}
	public void setExtra_field_1(String extra_field_1) {
		this.extra_field_1 = extra_field_1;
	}
	public String getExtra_field_2() {
		return extra_field_2;
	}
	public void setExtra_field_2(String extra_field_2) {
		this.extra_field_2 = extra_field_2;
	}
	
	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public int getProduct_ordering() {
		return product_ordering;
	}

	public void setProduct_ordering(int product_ordering) {
		this.product_ordering = product_ordering;
	}

	public String getCodiceArticolo() {
		return codiceArticolo;
	}

	public void setCodiceArticolo(String codiceArticolo) {
		this.codiceArticolo = codiceArticolo;
	}

	public String getImmagine() {
		return immagine;
	}

	public void setImmagine(String immagine) {
		this.immagine = immagine;
	}

	public String getImmagine2() {
		return immagine2;
	}

	public void setImmagine2(String immagine2) {
		this.immagine2 = immagine2;
	}

	public String getImmagine3() {
		return immagine3;
	}

	public void setImmagine3(String immagine3) {
		this.immagine3 = immagine3;
	}

	public String getImmagine4() {
		return immagine4;
	}

	public void setImmagine4(String immagine4) {
		this.immagine4 = immagine4;
	}

	public String getImmagine5() {
		return immagine5;
	}

	public void setImmagine5(String immagine5) {
		this.immagine5 = immagine5;
	}

	public String getDimensioni() {
		return dimensioni;
	}

	public void setDimensioni(String dimensioni) {
		this.dimensioni = dimensioni;
	}

	public String getAmbitoUtilizzo() {
		return ambitoUtilizzo;
	}

	public void setAmbitoUtilizzo(String ambitoUtilizzo) {
		this.ambitoUtilizzo = ambitoUtilizzo;
	}

	public String getQuantitaInserzione() {
		return quantitaInserzione;
	}

	public void setQuantitaInserzione(String quantitaInserzione) {
		this.quantitaInserzione = quantitaInserzione;
	}

	public String getTitoloBreve() {
		return titoloBreve;
	}

	public void setTitoloBreve(String titoloBreve) {
		this.titoloBreve = titoloBreve;
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public List<Variante_Articolo> getVarianti() {
		return varianti;
	}

	public void setVarianti(List<Variante_Articolo> varianti) {
		this.varianti = varianti;
	}

	public String getName_en_GB() {
		return name_en_GB;
	}

	public void setName_en_GB(String name_en_GB) {
		this.name_en_GB = name_en_GB;
	}

	public String getAlias_en_GB() {
		return alias_en_GB;
	}

	public void setAlias_en_GB(String alias_en_GB) {
		this.alias_en_GB = alias_en_GB;
	}

	public String getShort_description_en_GB() {
		return short_description_en_GB;
	}

	public void setShort_description_en_GB(String short_description_en_GB) {
		this.short_description_en_GB = short_description_en_GB;
	}

	public String getDescription_en_GB() {
		return description_en_GB;
	}

	public void setDescription_en_GB(String description_en_GB) {
		this.description_en_GB = description_en_GB;
	}

	public String getMeta_title_en_GB() {
		return meta_title_en_GB;
	}

	public void setMeta_title_en_GB(String meta_title_en_GB) {
		this.meta_title_en_GB = meta_title_en_GB;
	}

	public String getMeta_description_en_GB() {
		return meta_description_en_GB;
	}

	public void setMeta_description_en_GB(String meta_description_en_GB) {
		this.meta_description_en_GB = meta_description_en_GB;
	}

	public String getMeta_keyword_en_GB() {
		return meta_keyword_en_GB;
	}

	public void setMeta_keyword_en_GB(String meta_keyword_en_GB) {
		this.meta_keyword_en_GB = meta_keyword_en_GB;
	}

}
