package com.incomm.cclp.dto;

import java.util.Date;

public class PurseDTO {

	private String currencyTypeID;
	private String currCodeAlpha;
	private Long purseId;
	private String purseType;
	private String description;
	private String upc;
	private long insUser;
	private Date insDate;
	private long lastUpdUser;
	private Date lastUpdDate;
	private long purseTypeId;
	private String purseTypeName;
	private String currencyDesc;
	private String minorUnits;

	private String extPurseId;
	private Long hotCardThreshold;

	
	public long getPurseTypeId() {
		return purseTypeId;
	}
	public void setPurseTypeId(long purseTypeId) {
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

	public long getInsUser() {
		return insUser;
	}

	public void setInsUser(long insUser) {
		this.insUser = insUser;
	}

	public Date getInsDate() {
		return insDate;
	}

	public void setInsDate(Date insDate) {
		this.insDate = insDate;
	}

	public long getLastUpdUser() {
		return lastUpdUser;
	}

	public void setLastUpdUser(long lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	public Date getLastUpdDate() {
		return lastUpdDate;
	}

	public void setLastUpdDate(Date lastUpdDate) {
		this.lastUpdDate = lastUpdDate;
	}
	public String getCurrencyDesc() {
		return currencyDesc;
	}
	public void setCurrencyDesc(String currencyDesc) {
		this.currencyDesc = currencyDesc;
	}
	
	public String getPurseTypeName() {
		return purseTypeName;
	}
	public void setPurseTypeName(String purseTypeName) {
		this.purseTypeName = purseTypeName;
	}
	
	public PurseDTO() {
		
	}

	public String getMinorUnits() {
		return minorUnits;
	}
	public void setMinorUnits(String minorUnits) {
		this.minorUnits = minorUnits;
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
				+ ", minorUnits=" + minorUnits + ", extPurseId=" + extPurseId + "]";
	}
	
	
}

