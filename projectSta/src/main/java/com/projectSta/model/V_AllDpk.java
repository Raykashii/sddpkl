package com.projectSta.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

@Entity
public class V_AllDpk implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer bulan;
	@Id
	private String keterangan;
	@Id
	private BigDecimal nominal;
	
	public Integer getBulan() {
		return bulan;
	}
	public void setBulan(Integer bulan) {
		this.bulan = bulan;
	}
	@Type(type = "com.projectSta.utils.usertype.TrimUserType")
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public BigDecimal getNominal() {
		return nominal;
	}
	public void setNominal(BigDecimal nominal) {
		this.nominal = nominal;
	}
	
	
	
	

}
