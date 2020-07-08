package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Entity
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Table(name = "PURSE")
public class Purse implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "PURSE_SEQ_GEN", sequenceName = "PURSE_PURSE_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PURSE_SEQ_GEN")
	@Column(name = "PURSE_ID")
	private Long purseId;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="PURSE_TYPE_ID")
	private PurseType purseType;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "CURRENCY_CODE")
	private CurrencyCode currencyCode;
	
	@Column(name = "UPC")
	private String upc;

	@Column(name = "INS_USER" ,updatable = false)
	private Long insUser;

	@CreationTimestamp
	@Column(name = "INS_DATE" ,updatable = false )
	private Date insDate;

	@Column(name = "LAST_UPD_USER")
	private Long lastUpdUser;

	@UpdateTimestamp
	@Column(name = "LAST_UPD_DATE")
	private Date lastUpdDate;
	

	@Column(name = "EXT_PURSE_ID")
	private String extPurseId;
	
	@Column(name = "HOT_CARD_THRESHOLD")
	private Long hotCardThreshold;
	
	

	public Purse(Long purseId) {
		super();
		this.purseId = purseId;
	}

	public Purse(Long purseId,PurseType purseType,String description,CurrencyCode currencyCode,String upc,String extPurseId, Long hotCardThreshold) {
		this.purseId=purseId;
		this.purseType=purseType;
		this.description=description;
		this.currencyCode=currencyCode!=null?currencyCode:null ;
		this.upc=upc;
		this.extPurseId=extPurseId;
		this.hotCardThreshold=hotCardThreshold;

		}
	
	public Purse() {
		
	}
	
	public Long getPurseId() {
		return purseId;
	}

	public void setPurseId(Long purseId) {
		this.purseId = purseId;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	public String getUpc() {
		return upc;
	}

	public void setUpc(String upc) {
		this.upc = upc;
	}

	public Long getInsUser() {
		return insUser;
	}

	public void setInsUser(Long insUser) {
		this.insUser = insUser;
	}

	public Date getInsDate() {
		return insDate;
	}

	public void setInsDate(Date insDate) {
		this.insDate = new java.sql.Date(new java.util.Date().getTime());
	}

	public Long getLastUpdUser() {
		return lastUpdUser;
	}

	public void setLastUpdUser(Long lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	public Date getLastUpdDate() {
		return lastUpdDate;
	}

	public void setLastUpdDate(Date lastUpdDate) {
		this.lastUpdDate = lastUpdDate;
	}

	public PurseType getPurseType() {
		return purseType;
	}

	public void setPurseType(PurseType purseType) {
		this.purseType = purseType;
	}

	public CurrencyCode getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(CurrencyCode currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getExtPurseId() {
		return extPurseId;
	}

	public void setExtPurseId(String extPurseId) {
		this.extPurseId = extPurseId;
	}

	public Long getHotCardThreshold() {
		return hotCardThreshold;
	}

	public void setHotCardThreshold(Long hotCardThreshold) {
		this.hotCardThreshold = hotCardThreshold;
	}

	@Override
	public String toString() {
		return "Purse [purseId=" + purseId + ", purseType=" + purseType
				+ ", description=" + description + ", currencyCode="
				+ currencyCode + ", upc=" + upc + ", insUser=" + insUser
				+ ", insDate=" + insDate + ", lastUpdUser=" + lastUpdUser
				+ ", lastUpdDate=" + lastUpdDate + ", extPurseId=" + extPurseId
				+  ", hotCardThreshold=" + hotCardThreshold +"]";
	}


}



