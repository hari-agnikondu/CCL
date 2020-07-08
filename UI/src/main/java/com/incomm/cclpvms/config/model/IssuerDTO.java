package com.incomm.cclpvms.config.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.incomm.cclpvms.config.model.CardRange;


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

	private transient List<CardRange> cardRanges;

	

	private transient Object data;
	
	private String result;
	
	private String message;

	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
	
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
				", isActive: " + this.isActive + 
				", cardRanges: " + (this.cardRanges == null ? "{}" : 
					this.cardRanges.toString()) + "]";
	}

}