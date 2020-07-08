package com.incomm.cclpvms.config.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;


public class CountryCodeDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long countryCodeID;
	private CurrencyCodeDTO currencyCode;
	private String countryName;
	private String switchCountryCode;
	private String isoCountryCode;
	private String alphaCountryCode;
	private Long insUser;
	private Long lastUpdUser;
	private Set<StateCode> statecodes;

	public CountryCodeDTO(Long countryCodeID,String countryName){
		this.countryCodeID=countryCodeID;
		this.countryName=countryName;
		
	}
	
	public CountryCodeDTO(){
		
	}
	
	public CountryCodeDTO(Long countryCodeID, CurrencyCodeDTO currencyCode, String countryName,
			String switchCountryCode, String isoCountryCode, String alphaCountryCode, Long insUser, Long lastUpdUser,
			Set<StateCode> statecodes) {
		super();
		this.countryCodeID = countryCodeID;
		this.currencyCode = currencyCode;
		this.countryName = countryName;
		this.switchCountryCode = switchCountryCode;
		this.isoCountryCode = isoCountryCode;
		this.alphaCountryCode = alphaCountryCode;
		this.insUser = insUser;
		this.lastUpdUser = lastUpdUser;
		this.statecodes = statecodes;
	}
	public Long getCountryCodeID() {
		return countryCodeID;
	}
	public void setCountryCodeID(Long countryCodeID) {
		this.countryCodeID = countryCodeID;
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
