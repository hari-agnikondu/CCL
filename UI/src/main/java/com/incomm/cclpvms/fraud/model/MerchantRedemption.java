package com.incomm.cclpvms.fraud.model;

import java.util.Date;

import com.incomm.cclpvms.config.validator.FieldValidation;

public class MerchantRedemption {
	

	@FieldValidation(notEmpty = true, pattern = "^[A-Za-z0-9]+$",min=2,max = 100,
			messageNotEmpty = "{messageNotEmpty.merchant.merchantRedeemId}", 
			messageLength = "{messageLength.merchant.merchantRedeemId}",
			startsWithSpace = "{startsWithSpace.merchant.merchantRedeemId}",
			messagePattern = "{messagepattern.merchant.merchantRedeemId}", groups = ValidationStepOneMerchantRedeem.class)
	private String merchantId;


	@FieldValidation(notEmpty = true, pattern = "^[A-Za-z0-9 ,&.;'_-]+$",min=2,max = 100,
			messageNotEmpty = "{messageNotEmpty.merchant.merchantRedeemName}", 
			messageLength = "{messageLength.merchant.merchantRedeemName}",
			startsWithSpace = "{startsWithSpace.merchant.merchantRedeemName}",
			messagePattern = "{messagepattern.merchant.merchantRedeemName}", groups = ValidationStepOneMerchantRedeem.class)
	private String merchantName;

	private long insUser;

	private Date insDate;

	private long lastUpdUser;

	private Date lastUpdDate;

	public MerchantRedemption() {

	}

	public MerchantRedemption(String merchantId, String merchantName, long insUser, Date insDate, long lastUpdUser,
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

	@Override
	public String toString() {
		return "MerchantRedemption [merchantId=" + merchantId + ", merchantName=" + merchantName + ", insUser="
				+ insUser + ", insDate=" + insDate + ", lastUpdUser=" + lastUpdUser + ", lastUpdDate=" + lastUpdDate
				+ "]";
	}
	public interface ValidationStepOneMerchantRedeem {

	}

}
