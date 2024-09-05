package com.projectSta.domain;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@NamedQuery(name = "Musergroupmenu.findAll", query = "SELECT m FROM Musergroupmenu m")
public class Musergroupmenu implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer musergroupmenupk;
	private Mmenu mmenu;
	private Musergroup musergroup;
		
	



	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	public Integer getMusergroupmenupk() {
		return this.musergroupmenupk;
	}

	public void setMusergroupmenupk(Integer musergroupmenupk) {
		this.musergroupmenupk = musergroupmenupk;
	}

	// bi-directional many-to-one association to Mmenu
	@ManyToOne
	@JoinColumn(name = "mmenufk")
	public Mmenu getMmenu() {
		return this.mmenu;
	}

	public void setMmenu(Mmenu mmenu) {
		this.mmenu = mmenu;
	}

	// bi-directional many-to-one association to Musergroup
	@ManyToOne
	@JoinColumn(name = "musergroupfk")
	public Musergroup getMusergroup() {
		return this.musergroup;
	}

	public void setMusergroup(Musergroup musergroup) {
		this.musergroup = musergroup;
	}

	


}