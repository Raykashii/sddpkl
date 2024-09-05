package com.projectSta.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "MPDPKPRODUK")
@NamedQuery(name = "MPdpkproduk.findAll", query = "SELECT m FROM MPdpkproduk m")
public class MPdpkproduk {
	private Integer mdpkprodukpk;
	private String keterangan;
	private String label;
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	public Integer getMdpkprodukpk() {
		return mdpkprodukpk;
	}

	public void setMdpkprodukpk(Integer mdpkprodukpk) {
		this.mdpkprodukpk = mdpkprodukpk;
	}

	@Type(type = "com.projectSta.utils.usertype.TrimUserType")
	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	@Type(type = "com.projectSta.utils.usertype.TrimUserType")
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}


}