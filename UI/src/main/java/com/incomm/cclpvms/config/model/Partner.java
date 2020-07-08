package com.incomm.cclpvms.config.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.incomm.cclpvms.config.validator.EmptyValidation;
import com.incomm.cclpvms.config.validator.FieldValidation;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Partner implements Serializable {

	private static final long serialVersionUID = 1L;
	private long partnerId;
	@EmptyValidation(notEmpty = false, pattern = "^[A-Za-z0-9 ,&.;'_-]+$", min = 0, max = 100, messageNotEmpty = "{messageNotEmpty.partner.partnerName}", messageLength = "{messageLength.partner.partnerName}", messagePattern = "{messagepattern.partner.partnerName}", groups = searchPartner.class)

	@FieldValidation(notEmpty = true, pattern = "^[A-Za-z0-9 ,&.;'_-]+$", min = 2, max = 100, messageNotEmpty = "{messageNotEmpty.partner.partnerName}", messageLength = "{messageLength.partner.partnerName}", messagePattern = "{messagepattern.partner.partnerName}")

	private String partnerName;

	@FieldValidation(notEmpty = false, pattern = "", max = 255, messageNotEmpty = "{messageNotEmpty.partner.partnerDesc}", messageLength = "{messageLength.partner.partnerDesc}", messagePattern = "{messagepattern.partner.partnerDesc}")
	private String partnerDesc;

	@FieldValidation(notEmpty = true, pattern = "^[0-9]+$", min = 1, max = 20, messageNotEmpty = "{messageNotEmpty.partner.mdmId}", messageLength = "{messageLength.partner.mdmId}", messagePattern = "{messagepattern.partner.mdmId}")

	private String mdmId;

	private String isActive;
	private long insUser;
	private long lastUpdUser;
	
	private List<String> supportedCurrency;
	private List<String> supportedCurrencyUpdate;
	private List<Object> supportedCurrencyObject;

	private List<String> supportedPurse;
	private List<String> supportedPurseUpdate;
	private List<Object> supportedPurseObject;

	public Partner() {
	}

	public Partner(long partnerID, String partnerName, String partnerDesc, String mdmId, String partnerStatus) {
		super();
		this.partnerId = partnerID;
		this.partnerName = partnerName;
		this.partnerDesc = partnerDesc;
		this.mdmId = mdmId;
		this.isActive = partnerStatus;
	}

	public Partner(Long id, String partnerName2, String partnerDesc2, String mdmId2, String isActive2, long insUser2,
			long lastUpdUser2, List<String> currencyList, List<Object> currencyListObj,List<String> purseList,List<Object> purseListObj) {
		this.partnerId = id;
		this.partnerName = partnerName2;
		this.partnerDesc = partnerDesc2;
		this.mdmId = mdmId2;
		this.isActive = isActive2;
		this.insUser = insUser2;
		this.lastUpdUser = lastUpdUser2;
		this.supportedCurrency = currencyList;
		this.supportedCurrencyObject = currencyListObj;
		this.supportedPurse = purseList;
		this.supportedPurseObject = purseListObj;
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

	public long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(long partnerId) {
		this.partnerId = partnerId;
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


	public List<Object> getSupportedCurrencyObject() {
		return supportedCurrencyObject;
	}

	public void setSupportedCurrencyObject(List<Object> supportedCurrencyObject) {
		this.supportedCurrencyObject = supportedCurrencyObject;
	}

	public List<String> getSupportedCurrency() {
		return supportedCurrency;
	}

	public void setSupportedCurrency(List<String> supportedCurrency) {
		this.supportedCurrency = supportedCurrency;
	}

	public List<String> getSupportedCurrencyUpdate() {
		return supportedCurrencyUpdate;
	}

	public void setSupportedCurrencyUpdate(List<String> supportedCurrencyUpdate) {
		this.supportedCurrencyUpdate = supportedCurrencyUpdate;
	}

	public List<String> getSupportedPurse() {
		return supportedPurse;
	}

	public void setSupportedPurse(List<String> supportedPurse) {
		this.supportedPurse = supportedPurse;
	}

	public List<String> getSupportedPurseUpdate() {
		return supportedPurseUpdate;
	}

	public void setSupportedPurseUpdate(List<String> supportedPurseUpdate) {
		this.supportedPurseUpdate = supportedPurseUpdate;
	}

	public List<Object> getSupportedPurseObject() {
		return supportedPurseObject;
	}

	public void setSupportedPurseObject(List<Object> supportedPurseObject) {
		this.supportedPurseObject = supportedPurseObject;
	}

	@Override
	public String toString() {
		return "Partner [partnerId=" + partnerId + ", partnerName=" + partnerName + ", partnerDesc=" + partnerDesc
				+ ", mdmId=" + mdmId + ", isActive=" + isActive + ", insUser=" + insUser + ", lastUpdUser="
				+ lastUpdUser + ", supportedPurse=" + supportedPurse + ", supportedPurseUpdate=" + supportedPurseUpdate
				+ ", supportedPurseObject=" + supportedPurseObject + ", supportedCurrency=" + supportedCurrency
				+ ", supportedCurrencyUpdate=" + supportedCurrencyUpdate + ", supportedCurrencyObject="
				+ supportedCurrencyObject + "]";
	}

}

