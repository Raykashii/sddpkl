package com.projectSta.domain;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;

@Entity
@Table(name = "Tletter")
@NamedQuery(name = "Mtletter.findAll", query = "SELECT m FROM Tletter m")
public class Mtletter implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer tletterpk;
	private String memployeefk;
	private Date tgl_berangkat;
	private Date tgl_pulang;
	private String tujuan;
	private String keterangan;
	
	public Integer getTletterpk() {
		return tletterpk;
	}
	public void setTletterpk(Integer tletterpk) {
		this.tletterpk = tletterpk;
	}
	public String getMemployeefk() {
		return memployeefk;
	}
	public void setMemployeefk(String memployeefk) {
		this.memployeefk = memployeefk;
	}
	public Date getTgl_berangkat() {
		return tgl_berangkat;
	}
	public void setTgl_berangkat(Date tgl_berangkat) {
		this.tgl_berangkat = tgl_berangkat;
	}
	public Date getTgl_pulang() {
		return tgl_pulang;
	}
	public void setTgl_pulang(Date tgl_pulang) {
		this.tgl_pulang = tgl_pulang;
	}
	public String getTujuan() {
		return tujuan;
	}
	public void setTujuan(String tujuan) {
		this.tujuan = tujuan;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	
}