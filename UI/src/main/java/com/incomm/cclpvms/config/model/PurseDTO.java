package com.incomm.cclpvms.config.model;

import java.util.Date;

public class PurseDTO{

	private String currencyTypeID;
	private String currCodeAlpha;
	private Long purseId;
	private String purseType;
	private String description;
	private String upc;
	private Long insUser;
	private Date insDate;
	private Long lastUpdUser;
	private Date lastUpdDate;
	private Long purseTypeId;
	private String purseTypeName;
	private String currencyDesc;
	private String extPurseId;
	private Long hotCardThreshold;
	
	public Long getPurseTypeId() {
		return purseTypeId;
	}
	public void setPurseTypeId(Long purseTypeId) {
		this.purseTypeId = purseTypeId;
	}

	
	public Long getPurseId() {
		return purseId;
	}
	public void setPurseId(Long purseId) {
		this.purseId = purseId;
	}
	public String getPurseType() {
		return purseType;
	}
	public void setPurseType(String purseType) {
		this.purseType = purseType;
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
		this.insDate = insDate;
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
	
	public PurseDTO() {
		
	}
	
	public String getPurseTypeName() {
		return purseTypeName;
	}
	public void setPurseTypeName(String purseTypeName) {
		this.purseTypeName = purseTypeName;
	}
	public String getCurrencyDesc() {
		return currencyDesc;
	}
	public void setCurrencyDesc(String currencyDesc) {
		this.currencyDesc = currencyDesc;
	}
	public String getCurrCodeAlpha() {
		return currCodeAlpha;
	}
	public void setCurrCodeAlpha(String currCodeAlpha) {
		this.currCodeAlpha = currCodeAlpha;
	}
	public String getCurrencyTypeID() {
		return currencyTypeID;
	}
	public void setCurrencyTypeID(String currencyTypeID) {
		this.currencyTypeID = currencyTypeID;
	}
	public String getExtPurseId() {
		return extPurseId;
	}
	public void setExtPurseId(String extPurseId) {
		this.extPurseId = extPurseId;
	}
	public long getHotCardThreshold() {
		return hotCardThreshold;
	}
	public void setHotCardThreshold(long hotCardThreshold) {
		this.hotCardThreshold = hotCardThreshold;
	}

	@Override
	public String toString() {
		return "PurseDTO [currencyTypeID=" + currencyTypeID + ", currCodeAlpha=" + currCodeAlpha + ", purseId="
				+ purseId + ", purseType=" + purseType + ", description=" + description + ", upc=" + upc + ", insUser="
				+ insUser + ", insDate=" + insDate + ", lastUpdUser=" + lastUpdUser + ", lastUpdDate=" + lastUpdDate
				+ ", purseTypeId=" + purseTypeId + ", purseTypeName=" + purseTypeName + ", currencyDesc=" + currencyDesc
				+ ", extPurseId=" + extPurseId + "]";
	}
	
}

