package com.incomm.cclp.dto;

import java.io.Serializable;
import java.util.Set;

import com.incomm.cclp.domain.CurrencyCode;
import com.incomm.cclp.domain.StateCode;

public class CountryCodeDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long countryCodeID;
	
	private CurrencyCode currencyCode;
	private String countryName;
	private String switchCountryCode;
	private String isoCountryCode;
	private String alphaCountryCode;
	private Long insUser;
	
	private Long lastUpdUser;
	private Set<StateCode> statecodes;
	
	public Long getCountryCodeID() {
		return countryCodeID;
	}
	public void setCountryCodeID(Long countryCodeID) {
		this.countryCodeID = countryCodeID;
	}
	
	
	public CurrencyCode getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(CurrencyCode currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getSwitchCountryCode() {
		return switchCountryCode;
	}
	public void setSwitchCountryCode(String switchCountryCode) {
		this.switchCountryCode = switchCountryCode;
	}
	public String getIsoCountryCode() {
		return isoCountryCode;
	}
	public void setIsoCountryCode(String isoCountryCode) {
		this.isoCountryCode = isoCountryCode;
	}
	public String getAlphaCountryCode() {
		return alphaCountryCode;
	}
	public void setAlphaCountryCode(String alphaCountryCode) {
		this.alphaCountryCode = alphaCountryCode;
	}
	public Long getInsUser() {
		return insUser;
	}
	public void setInsUser(Long insUser) {
		this.insUser = insUser;
	}
	public Long getLastUpdUser() {
		return lastUpdUser;
	}
	public void setLastUpdUser(Long lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}
	public Set<StateCode> getStatecodes() {
		return statecodes;
	}
	public void setStatecodes(Set<StateCode> statecodes) {
		this.statecodes = statecodes;
	}


	@Override
	public String toString() {
		return "CountryCodeDTO [countryCodeID=" + countryCodeID + ", currencyCode=" + currencyCode + ", countryName="
				+ countryName + ", switchCountryCode=" + switchCountryCode + ", isoCountryCode=" + isoCountryCode
				+ ", alphaCountryCode=" + alphaCountryCode + ", insUser=" + insUser + ", lastUpdUser=" + lastUpdUser
				+ ", statecodes=" + statecodes + "]";
	}
}
