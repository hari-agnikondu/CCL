package com.incomm.cclp.dto;


public class LocationDTO {

	private Long locationId;
	private Long merchantId;
	private String merchantName; 
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	private String locationName;
	private String addressOne;
	private String addressTwo;
	private String city;
	private Long stateCodeID;
	private String stateName;
	private String countryName;
	private Long countryCodeID;
	private String zip;
	private Long insUser;
	private Long lastUpdUser;
	
	public Long getLocationId() {
		return locationId;
	}
	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getAddressOne() {
		return addressOne;
	}
	public void setAddressOne(String addressOne) {
		this.addressOne = addressOne;
	}
	public String getAddressTwo() {
		return addressTwo;
	}
	public void setAddressTwo(String addressTwo) {
		this.addressTwo = addressTwo;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}

	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
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
	public Long getCountryCodeID() {
		return countryCodeID;
	}
	public void setCountryCodeID(Long countryCodeID) {
		this.countryCodeID = countryCodeID;
	}
	public Long getStateCodeID() {
		return stateCodeID;
	}
	public void setStateCodeID(Long stateCodeID) {
		this.stateCodeID = stateCodeID;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	@Override
	public String toString() {
		return "LocationDTO [locationId=" + locationId + ", merchantId="
				+ merchantId + ", merchantName=" + merchantName
				+ ", locationName=" + locationName + ", addressOne="
				+ addressOne + ", addressTwo=" + addressTwo + ", city=" + city
				+ ", stateCodeID=" + stateCodeID + ", stateName=" + stateName
				+ ", countryName=" + countryName + ", countryCodeID="
				+ countryCodeID + ", zip=" + zip + ", insUser=" + insUser
				+ ", lastUpdUser=" + lastUpdUser + "]";
	}

	
}

