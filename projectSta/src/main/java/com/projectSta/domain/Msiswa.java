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
@Table(name = "MSISWA")
@NamedQuery(name = "Msiswa.findAll", query = "SELECT m FROM Msiswa m")
public class Msiswa implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer msiswapk;
	private Mkelas mkelasfk;
	private String nisn;
	private String namasiswa;
	private String jeniskelamin;
	private Integer notelepon;
	private String alamat;
	private Date createdtime;
	private String createdby;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	public Integer getMsiswapk() {
		return msiswapk;
	}
	public void setMsiswapk(Integer msiswapk) {
		this.msiswapk = msiswapk;
	}
	
	@ManyToOne
	@JoinColumn(name = "MKELASFK")
	public Mkelas getMkelasfk() {
		return mkelasfk;
	}
	public void setMkelasfk(Mkelas mkelasfk) {
		this.mkelasfk = mkelasfk;
	}
	
	@Column(length = 50)
	@Type(type = "com.projectSta.utils.usertype.TrimUserType")
	public String getNisn() {
		return nisn;
	}
	public void setNisn(String nisn) {
		this.nisn = nisn;
	}
	
	
	@Column(length = 40)
	@Type(type = "com.projectSta.utils.usertype.TrimUserType")
	public String getNamasiswa() {
		return namasiswa;
	}
	public void setNamasiswa(String namasiswa) {
		this.namasiswa = namasiswa;
	}
	
	@Column(length = 50)
	@Type(type = "com.projectSta.utils.usertype.TrimUserType")
	public String getJeniskelamin() {
		return jeniskelamin;
	}
	public void setJeniskelamin(String jeniskelamin) {
		this.jeniskelamin = jeniskelamin;
	}
	

	public Integer getNotelepon() {
		return notelepon;
	}
	public void setNotelepon(Integer notelepon) {
		this.notelepon = notelepon;
	}
	
	@Column(length = 50)
	@Type(type = "com.projectSta.utils.usertype.TrimUserType")
	public String getAlamat() {
		return alamat;
	}
	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}
	public Date getCreatedtime() {
		return createdtime;
	}
	public void setCreatedtime(Date createdtime) {
		this.createdtime = createdtime;
	}
	public String getCreatedby() {
		return createdby;
	}
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	
	
	
}
