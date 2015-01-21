package it.swb.model;

import java.io.Serializable;

public class CategoriaEbay implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	long category_id;
	String level_1;
	String level_2;
	String level_3;
	String level_4;
	String level_5;
	String level_6;
	long parent_id;
	
	public CategoriaEbay(){}

	public long getCategory_id() {
		return category_id;
	}

	public void setCategory_id(long category_id) {
		this.category_id = category_id;
	}

	public String getLevel_1() {
		return level_1;
	}

	public void setLevel_1(String level_1) {
		this.level_1 = level_1;
	}

	public String getLevel_2() {
		return level_2;
	}

	public void setLevel_2(String level_2) {
		this.level_2 = level_2;
	}

	public String getLevel_3() {
		return level_3;
	}

	public void setLevel_3(String level_3) {
		this.level_3 = level_3;
	}

	public String getLevel_4() {
		return level_4;
	}

	public void setLevel_4(String level_4) {
		this.level_4 = level_4;
	}

	public String getLevel_5() {
		return level_5;
	}

	public void setLevel_5(String level_5) {
		this.level_5 = level_5;
	}

	public String getLevel_6() {
		return level_6;
	}

	public void setLevel_6(String level_6) {
		this.level_6 = level_6;
	}

	public long getParent_id() {
		return parent_id;
	}

	public void setParent_id(long parent_id) {
		this.parent_id = parent_id;
	}
	
	

}
