package com.incomm.cclp.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.incomm.cclp.domain.CardRange;

/**
 * The DTO class for the ISSUER.
 * 
 */
public class IssuerDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private long issuerId;
	
	private String issuerName;

	private String description;
	
	private String mdmId;
	
	private String isActive;
	
	private long insUser;

	private Date insDate;
	
	private long lastUpdUser;

	private Date lastUpdDate;

	private List<CardRange> cardRanges;

	public long getIssuerId() {
		return issuerId;
	}


	public void setIssuerId(long issuerId) {
		this.issuerId = issuerId;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Date getInsDate() {
		return insDate;
	}


	public void setInsDate(Date insDate) {
		this.insDate = insDate;
	}


	public String getIsActive() {
		return isActive;
	}


	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}


	public String getIssuerName() {
		return issuerName;
	}


	public void setIssuerName(String issuerName) {
		this.issuerName = issuerName;
	}


	public Date getLastUpdDate() {
		return lastUpdDate;
	}


	public void setLastUpdDate(Date lastUpdDate) {
		this.lastUpdDate = lastUpdDate;
	}


	public String getMdmId() {
		return mdmId;
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


	public List<CardRange> getCardRanges() {
		return cardRanges;
	}


	public void setCardRanges(List<CardRange> cardRanges) {
		this.cardRanges = cardRanges;
	}


	public String toString()
	{
		return "IssuerDTO [issuerId: " + this.issuerId + 
				", issuerName: " + this.issuerName + 
				", description: " + this.description + 
				", mdmId: " + this.mdmId + 
				", insUser: " + this.insUser + 
				", lastUpdUser: " + this.lastUpdUser + 
				", isActive: " + this.isActive + 
				", cardRanges: " + (this.cardRanges == null ? "{}" : 
					this.cardRanges.toString()) + "]";
	}

}