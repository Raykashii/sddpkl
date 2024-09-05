package com.projectSta.domain;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.Type;

import java.util.Date;

@Entity
@Table(name = "MUSERGROUP")
@NamedQuery(name = "Musergroup.findAll", query = "SELECT m FROM Musergroup m")
public class Musergroup implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer musergrouppk;
	private String usergroupcode;
	private String usergroupname;
	private String usergroupdesc;
	private Date createdtime;
	private String createdby;
	private Date lastupdated;
	private String updatedby;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	public Integer getMusergrouppk() {
		return this.musergrouppk;
	}

	public void setMusergrouppk(Integer musergrouppk) {
		this.musergrouppk = musergrouppk;
	}

	@Column(length = 20)
	@Type(type = "com.projectSta.utils.usertype.TrimUserType")
	public String getUsergroupcode() {
		return this.usergroupcode;
	}

	public void setUsergroupcode(String usergroupcode) {
		this.usergroupcode = usergroupcode;
	}

	@Column(length = 40)
	@Type(type = "com.projectSta.utils.usertype.TrimUserType")
	public String getUsergroupname() {
		return this.usergroupname;
	}

	public void setUsergroupname(String usergroupname) {
		this.usergroupname = usergroupname;
	}

	@Column(length = 100)
	@Type(type = "com.projectSta.utils.usertype.TrimUserType")
	public String getUsergroupdesc() {
		return this.usergroupdesc;
	}

	public void setUsergroupdesc(String usergroupdesc) {
		this.usergroupdesc = usergroupdesc;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatedtime() {
		return createdtime;
	}

	public void setCreatedtime(Date createdtime) {
		this.createdtime = createdtime;
	}

	@Column(length = 15)
	@Type(type = "com.projectSta.utils.usertype.TrimUserType")
	public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastupdated() {
		return this.lastupdated;
	}

	public void setLastupdated(Date lastupdated) {
		this.lastupdated = lastupdated;
	}

	@Column(length = 15)
	@Type(type = "com.projectSta.utils.usertype.TrimUserType")
	public String getUpdatedby() {
		return this.updatedby;
	}

	public void setUpdatedby(String updatedby) {
		this.updatedby = updatedby;
	}
}