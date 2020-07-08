package com.incomm.cclp.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table (name="PURSE_TYPE")
public class PurseType implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "PURSE_TYPE_SEQ_GEN", sequenceName = "PURSE_TYPE_NAME", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PURSE_TYPE_SEQ_GEN")
	@Column(name = "PURSE_TYPE_ID")
	private Long purseTypeId;
	
	@Column(name = "PURSE_TYPE_NAME")
	private String purseTypeName;

	

	public String getPurseTypeName() {
		return purseTypeName;
	}

	public void setPurseTypeName(String purseTypeName) {
		this.purseTypeName = purseTypeName;
	}

	public Long getPurseTypeId() {
		return purseTypeId;
	}

	public void setPurseTypeId(Long purseTypeId) {
		this.purseTypeId = purseTypeId;
	}

	@Override
	public String toString() {
		return "PurseType [purseTypeId=" + purseTypeId + ", purseTypeName="
				+ purseTypeName + "]";
	}
	
	

}
