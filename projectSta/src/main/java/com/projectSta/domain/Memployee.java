package com.projectSta.domain;

import java.io.Serializable;
import org.hibernate.annotations.Type;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.lang.Integer;


@Entity
@Table(name = "Memployee")
@NamedQuery(name = "Memployee.findAll", query = "SELECT m FROM Memployee m")
public class Memployee implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer memployeepk;
	private String nama;
	private String jabatan;
	private String no_telp;
	private String alamat;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	public Integer getMemployeepk() {
		return memployeepk;
	}
	public void setMemployeepk(Integer memployeepk) {
		this.memployeepk = memployeepk;
	}
	
	
	
	@Column(length = 40)
	@Type(type = "com.projectSta.utils.usertype.TrimUserType")
	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}
	
	

	public String getJabatan() {
		return jabatan;
	}
	public void setJabatan(String jabatan) {
		this.jabatan = jabatan;
	}
	
	

	public String getNo_telp() {
		return no_telp;
	}
	public void setNo_telp(String no_telp) {
		this.no_telp = no_telp;
	}
	
	

	public String getAlamat() {
		return alamat;
	}
	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}
	


	
}