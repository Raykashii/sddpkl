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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "MUSER")
@NamedQuery(name = "Muser.findAll", query = "SELECT m FROM Muser m")
public class Muser implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer muserpk;
	private Date lastupdated;
	private String password;
	private String updatedby;
	private String userid;
	private String username;
	private Musergroup musergroup;
	private String email;
	private Date lastlogin;
	private String createdby;
	private Date createdtime;
	private Integer islogin;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	public Integer getMuserpk() {
		return this.muserpk;
	}

	public void setMuserpk(Integer muserpk) {
		this.muserpk = muserpk;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastupdated() {
		return this.lastupdated;
	}

	public void setLastupdated(Date lastupdated) {
		this.lastupdated = lastupdated;
	}

	@Column(length = 70)
	@Type(type = "com.projectSta.utils.usertype.TrimUserType")
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(length = 15)
	@Type(type = "com.projectSta.utils.usertype.TrimUserType")
	public String getUpdatedby() {
		return this.updatedby;
	}

	public void setUpdatedby(String updatedby) {
		this.updatedby = updatedby;
	}

	@Column(length = 50)
	@Type(type = "com.projectSta.utils.usertype.TrimUserType")
	public String getUserid() {
		return this.userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@Column(length = 40)
	@Type(type = "com.projectSta.utils.usertype.TrimUserType")
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	// bi-directional many-to-one association to Musergroup
	@ManyToOne
	@JoinColumn(name = "MUSERGROUPFK")
	public Musergroup getMusergroup() {
		return this.musergroup;
	}

	public void setMusergroup(Musergroup musergroup) {
		this.musergroup = musergroup;
	}

	@Column(length = 50)
	@Type(type = "com.projectSta.utils.usertype.TrimUserType")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getLastlogin() {
		return lastlogin;
	}

	public void setLastlogin(Date lastlogin) {
		this.lastlogin = lastlogin;
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

	public Integer getIslogin() {
		return islogin;
	}

	public void setIslogin(Integer islogin) {
		this.islogin = islogin;
	}

}