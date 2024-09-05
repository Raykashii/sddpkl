package com.projectSta.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class V_SumDPK {
	
	@Id
	private Integer tsummarydpkpk;
	private String bulan;
	private String keterangan;
	private BigDecimal nominal;

}
