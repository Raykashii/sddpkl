package com.projectSta.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class VDataTotal {

	@Id
	private BigDecimal nominal;
	

	public BigDecimal getNominal() {
		return nominal;
	}

	public void setNominal(BigDecimal nominal) {
		this.nominal = nominal;
	}
	
	
}
