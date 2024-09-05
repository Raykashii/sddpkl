package com.projectSta.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Mtable {

	@Id
	private String table_name;

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}
}