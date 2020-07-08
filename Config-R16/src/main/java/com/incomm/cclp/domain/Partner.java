package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "PARTNER")
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
	@CreationTimestamp
	@Column(name="INS_DATE",updatable = false)
	private Date insDate;
	
	@Column(name="LAST_UPD_USER")
	private long lastUpdUser;

	@Temporal(TemporalType.DATE)
	@UpdateTimestamp
	@Column(name="LAST_UPD_DATE")
	private Date lastUpdDate;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "partnerCurrencyID.partner", cascade = { CascadeType.PERSIST,
			CascadeType.MERGE }, orphanRemoval = true)
	private List<PartnerCurrency> partnerCurrencyList; 
	

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "partnerPurseID.partner", cascade = {CascadeType.PERSIST,
			CascadeType.MERGE}, orphanRemoval = true)
	private List<PartnerPurse> partnerPurseList;

	public List<PartnerCurrency> getPartnerCurrencyList() {
		return partnerCurrencyList;
	}
	
	public void setPartnerCurrencyList(List<PartnerCurrency> partnerCurrencyList) {
		this.partnerCurrencyList = partnerCurrencyList;
	}

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

	public void setInsUser(long insUser) {
		this.insUser = 1;
	}

	public Date getInsDate() {
		return insDate;
	}

	public void setInsDate(Date insDate) {
		this.insDate = new java.sql.Date(new java.util.Date().getTime());
	}

	public long getLastUpdUser() {
		return lastUpdUser;
	}

	public void setLastUpdUser(long lastUpdUser) {
		this.lastUpdUser = 1;
	}

	public Date getLastUpdDate() {
		return lastUpdDate;
	}

	public void setLastUpdDate(Date lastUpdDate) {
		this.lastUpdDate = new java.sql.Date(new java.util.Date().getTime());
	}

	public List<PartnerPurse> getPartnerPurseList() {
		return partnerPurseList;
	}

	public void setPartnerPurseList(List<PartnerPurse> partnerPurseList) {
		this.partnerPurseList = partnerPurseList;
	}

	@Override
	public String toString() {
		return "Partner [partnerId=" + partnerId + ", partnerName=" + partnerName + ", mdmId=" + mdmId + "]";
	}
	
}
