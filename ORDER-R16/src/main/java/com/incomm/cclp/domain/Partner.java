package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="clp_configuration.PARTNER")//added by Hari, because we are accessing the table from other schema without synonym
@NamedQuery(name="Partner.findAll", query="SELECT i FROM Partner i")
public class Partner implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PARTNER_SEQ_GEN", sequenceName="PARTNER_PARTNER_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PARTNER_SEQ_GEN")
	@Column(name="PARTNER_ID")
	private long partnerId;
	
	@Column(name="PARTNER_NAME")
	private String partnerName;

	@Column(name="DESCRIPTION")
	private String partnerDesc;
	
	@Column(name="MDM_ID")
	private String mdmId;

	@Column(name="IS_ACTIVE")
	private String isActive;
	
	@Column(name="INS_USER",updatable = false )
	private long insUser;
	
	@Temporal(TemporalType.DATE)
	@Column(name="INS_DATE",updatable = false)
	private Date insDate;
	
	@Column(name="LAST_UPD_USER")
	private long lastUpdUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPD_DATE")
	private Date lastUpdDate;


	public long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(long partnerId) {
		this.partnerId = partnerId;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	

	public String getPartnerDesc() {
		return partnerDesc;
	}

	public void setPartnerDesc(String partnerDesc) {
		this.partnerDesc = partnerDesc;
	}

	public String getMdmId() {
		return mdmId;
	}

	public void setMdmId(String mdmId) {
		this.mdmId = mdmId;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public long getInsUser() {
		return insUser;
	}

	public void setInsUser() {
		this.insUser = 1;
	}

	public Date getInsDate() {
		return insDate;
	}

	public void setInsDate() {
		this.insDate = new java.sql.Date(new java.util.Date().getTime());
	}

	public long getLastUpdUser() {
		return lastUpdUser;
	}

	public void setLastUpdUser() {
		this.lastUpdUser = 1;
	}

	public Date getLastUpdDate() {
		return lastUpdDate;
	}

	public void setLastUpdDate() {
		this.lastUpdDate = new java.sql.Date(new java.util.Date().getTime());
	}
	
}
