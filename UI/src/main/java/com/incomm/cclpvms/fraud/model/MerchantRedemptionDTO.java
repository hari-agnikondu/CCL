package com.incomm.cclpvms.fraud.model;

import java.util.Date;

public class MerchantRedemptionDTO {
	
	private String merchantId;

	private String merchantName;

	private long insUser;

	private Date insDate;

	private long lastUpdUser;

	private Date lastUpdDate;
	
	
	public MerchantRedemptionDTO() {
		
	}

	public MerchantRedemptionDTO(String merchantId, String merchantName, long insUser, Date insDate, long lastUpdUser,
			Date lastUpdDate) {
		super();
		this.merchantId = merchantId;
		this.merchantName = merchantName;
		this.insUser = insUser;
		this.insDate = insDate;
		this.lastUpdUser = lastUpdUser;
		this.lastUpdDate = lastUpdDate;
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
	
	


}
