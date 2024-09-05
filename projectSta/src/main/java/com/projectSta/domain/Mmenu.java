package com.projectSta.domain;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "MMENU")
@NamedQuery(name = "Mmenu.findAll", query = "SELECT m FROM Mmenu m")
public class Mmenu implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer mmenupk;
	private Integer menuorderno;
	private String menugroup;
	private String menusubgroup;
	private String menuname;
	private String menupath;
	private String menuparamname;
	private String menuparamvalue;
	private String menugroupicon;
	private String menusubgroupicon;
	private String menuicon;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	public Integer getMmenupk() {
		return this.mmenupk;
	}

	public void setMmenupk(Integer mmenupk) {
		this.mmenupk = mmenupk;
	}

	@Column(length = 20)
	@Type(type = "com.projectSta.utils.usertype.TrimUserType")
	public String getMenugroup() {
		return this.menugroup;
	}

	public void setMenugroup(String menugroup) {
		this.menugroup = menugroup;
	}

	@Column(length = 50)
	@Type(type = "com.projectSta.utils.usertype.TrimUserType")
	public String getMenugroupicon() {
		return menugroupicon;
	}

	public void setMenugroupicon(String menugroupicon) {
		this.menugroupicon = menugroupicon;
	}

	@Column(length = 40)
	@Type(type = "com.projectSta.utils.usertype.TrimUserType")
	public String getMenuname() {
		return this.menuname;
	}

	public void setMenuname(String menuname) {
		this.menuname = menuname;
	}

	public Integer getMenuorderno() {
		return this.menuorderno;
	}

	public void setMenuorderno(Integer menuorderno) {
		this.menuorderno = menuorderno;
	}

	@Column(length = 10)
	@Type(type = "com.projectSta.utils.usertype.TrimUserType")
	public String getMenuparamname() {
		return this.menuparamname;
	}

	public void setMenuparamname(String menuparamname) {
		this.menuparamname = menuparamname;
	}

	@Column(length = 20)
	@Type(type = "com.projectSta.utils.usertype.TrimUserType")
	public String getMenuparamvalue() {
		return this.menuparamvalue;
	}

	public void setMenuparamvalue(String menuparamvalue) {
		this.menuparamvalue = menuparamvalue;
	}

	@Column(length = 40)
	@Type(type = "com.projectSta.utils.usertype.TrimUserType")
	public String getMenupath() {
		return this.menupath;
	}

	public void setMenupath(String menupath) {
		this.menupath = menupath;
	}

	@Column(length = 20)
	@Type(type = "com.projectSta.utils.usertype.TrimUserType")
	public String getMenusubgroup() {
		return this.menusubgroup;
	}

	public void setMenusubgroup(String menusubgroup) {
		this.menusubgroup = menusubgroup;
	}

	@Column(length = 50)
	@Type(type = "com.projectSta.utils.usertype.TrimUserType")
	public String getMenusubgroupicon() {
		return menusubgroupicon;
	}

	public void setMenusubgroupicon(String menusubgroupicon) {
		this.menusubgroupicon = menusubgroupicon;
	}

	@Column(length = 50)
	@Type(type = "com.projectSta.utils.usertype.TrimUserType")
	public String getMenuicon() {
		return menuicon;
	}

	public void setMenuicon(String menuicon) {
		this.menuicon = menuicon;
	}

}