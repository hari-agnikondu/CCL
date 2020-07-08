package com.incomm.cclpvms.config.model;
import java.util.Date;
import java.util.Map;

public class CustomerProfileDTO {
	
	private Long profileId;
	
	private String accountNumber;

	private String proxyNumber;
	
	private String cardNumber;

	private Map<String, Map<String, Object>> attributesMap;
	
	private Long insUser;

	private Date insDate;

	private Long lastUpdUser;

	private Date lastUpdDate;

	public Long getProfileId() {
		return profileId;
	}

	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getProxyNumber() {
		return proxyNumber;
	}

	public void setProxyNumber(String proxyNumber) {
		this.proxyNumber = proxyNumber;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public Map<String, Map<String, Object>> getAttributesMap() {
		return attributesMap;
	}

	public void setAttributesMap(Map<String, Map<String, Object>> attributesMap) {
		this.attributesMap = attributesMap;
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

	@Override
	public String toString() {
		return "CustomerProfileDTO [profileId=" + profileId + ", accountNumber=" + accountNumber + ", proxyNumber="
				+ proxyNumber + ", cardNumber=" + cardNumber + ", attributesMap=" + attributesMap + ", insUser="
				+ insUser + ", insDate=" + insDate + ", lastUpdUser=" + lastUpdUser + ", lastUpdDate=" + lastUpdDate
				+ "]";
	}
	
	
}