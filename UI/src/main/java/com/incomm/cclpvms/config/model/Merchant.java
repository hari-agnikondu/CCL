package com.incomm.cclpvms.config.model;

import java.util.Date;


import com.incomm.cclpvms.config.validator.EmptyValidation;
import com.incomm.cclpvms.config.validator.FieldValidation;

public class Merchant {
	
	@EmptyValidation(notEmpty = false, pattern = "^[A-Za-z0-9 ,&.;'_-]+$", min = 0, max = 100,  messageLength = "{messageLength.merchant.merchantName}",
            messagePattern = "{messagepattern.merchant.merchantName}", 
           groups = SearchMerchantScreen.class)

	@FieldValidation(notEmpty = true, pattern = "^[A-Za-z0-9 ,&.;'_-]+$",min=2,max = 100,
			messageNotEmpty = "{messageNotEmpty.merchant.merchantName}", 
			messageLength = "{messageLength.merchant.merchantName}",
			startsWithSpace = "{startsWithSpace.merchant.merchantName}",
			messagePattern = "{messagepattern.merchant.merchantName}", groups = ValidationStepOneMerchant.class)
	private String merchantName;

	@FieldValidation(notEmpty = false, max = 255, 
			messageLength = "{messageLength.merchant.merchantDesc}", groups = ValidationStepTwoMerchant.class)
	private String description;
	@FieldValidation(notEmpty = false, pattern = "^[0-9]+$",  max = 20, 
			messageNotEmpty = "{messageNotEmpty.merchant.merchantMdmId}", 
			messageLength = "{messageLength.merchant.merchantId}", 
			messagePattern = "{messagepattern.merchant.merchantMdmId}", groups = ValidationStepTwoMerchant.class)
	private String mdmId;
	
	private Long merchantId;
	
	private long insUser;
	
	private Date insDate;
	
	private long lastUpdUser;

	private Date lastUpdDate;
	
	
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


	public Merchant(String merchantName, String description, String mdmId) {
		 super();
		this.merchantName = merchantName;
		this.description = description;
		this.mdmId = mdmId;
	}


	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMdmId() {
		return mdmId;
	}
	public void setMdmId(String mdmId) {
		this.mdmId = mdmId;
	}
	
	public Long getMerchantId() {
		return merchantId;
	}


	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}


	
	public interface ValidationStepOneMerchant {

	}

	public interface ValidationStepTwoMerchant {

	}
	public interface SearchMerchantScreen {

	}
	
	
	public Merchant(String merchantName, String description, String mdmId, Long merchantId) {

		this.merchantName = merchantName;
		this.description = description;
		this.mdmId = mdmId;
		this.merchantId = merchantId;
	}


	public Merchant() {

	}
	


	
	
	
	
}
