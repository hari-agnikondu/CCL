package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="clp_configuration.MERCHANT")//added by Hari, because we are accessing the table from other schema without synonym
@NamedQuery(name="Merchant.findAll", query="SELECT i FROM Merchant i")
public class Merchant implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id	
	//@SequenceGenerator(name="ISSUER_SEQ_GEN", sequenceName="seq_issuer_id", allocationSize=1)
/*	@SequenceGenerator(name="ISSUER_SEQ_GEN", sequenceName="ISSUER_ISSUER_ID_SEQ", allocationSize=1)//Kalaivani changed 26-02-2018 as per ER Diagram
	
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ISSUER_SEQ_GEN")*/
	@Column(name="MERCHANT_ID")
	private String merchantId;
	
	@Column(name="MERCHANT_NAME")
	private String merchantName;

	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="MDM_ID")
	private String mdmId;
	
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


	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public Date getInsDate() {
		return this.insDate;
	}

	public void setInsDate(Date insDate) {
		this.insDate = new java.sql.Date(new java.util.Date().getTime());
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
