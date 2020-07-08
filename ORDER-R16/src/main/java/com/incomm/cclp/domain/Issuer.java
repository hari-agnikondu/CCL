package com.incomm.cclp.domain;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the ISSUER database table.
 * 
 */
@Entity
@Table(name="clp_configuration.ISSUER")//added by Hari, because we are accessing the table from other schema without synonym
@NamedQuery(name="Issuer.findAll", query="SELECT i FROM Issuer i")
public class Issuer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	//@SequenceGenerator(name="ISSUER_SEQ_GEN", sequenceName="seq_issuer_id", allocationSize=1)
	@SequenceGenerator(name="ISSUER_SEQ_GEN", sequenceName="ISSUER_ISSUER_ID_SEQ", allocationSize=1)//Kalaivani changed 26-02-2018 as per ER Diagram
	
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ISSUER_SEQ_GEN")
	@Column(name="ISSUER_ID")
	private long issuerId;
	
	@Column(name="ISSUER_NAME")
	private String issuerName;

	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="MDM_ID")
	private String mdmId;

	@Column(name="IS_ACTIVE")
	private String isActive;
	
	@Column(name="INS_USER")
	private long insUser;
	
	@Temporal(TemporalType.DATE)
	@Column(name="INS_DATE")
	private Date insDate;
	
	@Column(name="LAST_UPD_USER")
	private long lastUpdUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPD_DATE")
	private Date lastUpdDate;


	public long getIssuerId() {
		return this.issuerId;
	}

	public void setIssuerId(long issuerId) {
		this.issuerId = issuerId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getInsDate() {
		return this.insDate;
	}

	public void setInsDate(Date insDate) {
		this.insDate = new java.sql.Date(new java.util.Date().getTime());
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getIssuerName() {
		return this.issuerName;
	}

	public void setIssuerName(String issuerName) {
		this.issuerName = issuerName;
	}

	public Date getLastUpdDate() {
		return this.lastUpdDate;
	}

	public void setLastUpdDate(Date lastUpdDate) {
		this.lastUpdDate = lastUpdDate;
	}

	public String getMdmId() {
		return this.mdmId;
	}

	public void setMdmId(String mdmId) {
		this.mdmId = mdmId;
	}

	public long getInsUser() {
		return insUser;
	}

	public void setInsUser(long insUser) {
		this.insUser = insUser;
	}

	public long getLastUpdUser() {
		return lastUpdUser;
	}

	public void setLastUpdUser(long lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

}