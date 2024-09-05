package com.projectSta.domain;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.Type;

import java.util.Date;

@Entity
@Table(name = "MKELAS")
@NamedQuery(name = "Mkelas.findAll", query = "SELECT m FROM Mkelas m")
public class Mkelas implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer mkelaspk;
	private String kodekelas;
	private String namakelas;
	private String kelasdesc;
	private Date createdtime;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	public Integer getMkelaspk() {
		return mkelaspk;
	}
	public void setMkelaspk(Integer mkelaspk) {
		this.mkelaspk = mkelaspk;
	}
	
	@Column(length = 20)
	@Type(type = "com.projectSta.utils.usertype.TrimUserType")
	public String getKodekelas() {
		return kodekelas;
	}
	public void setKodekelas(String kodekelas) {
		this.kodekelas = kodekelas;
	}
	
	@Column(length = 40)
	@Type(type = "com.projectSta.utils.usertype.TrimUserType")
	public String getNamakelas() {
		return namakelas;
	}
	public void setNamakelas(String namakelas) {
		this.namakelas = namakelas;
	}
	
	@Column(length = 100)
	@Type(type = "com.projectSta.utils.usertype.TrimUserType")
	public String getKelasdesc() {
		return kelasdesc;
	}
	public void setKelasdesc(String kelasdesc) {
		this.kelasdesc = kelasdesc;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatedtime() {
		return createdtime;
	}
	public void setCreatedtime(Date createdtime) {
		this.createdtime = createdtime;
	}


	
}