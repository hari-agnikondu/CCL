package com.incomm.cclpvms.config.model;


import java.util.Date;

import javax.validation.constraints.NotNull;

import com.incomm.cclpvms.config.model.Merchant.SearchMerchantScreen;
import com.incomm.cclpvms.config.model.Product.ValidationStepOne;
import com.incomm.cclpvms.config.validator.EmptyValidation;
import com.incomm.cclpvms.config.validator.FieldValidation;

public class Location {

	private Long locationId;
	@NotNull(message = "{message.location.merchantId}")
	private Long merchantId;
	@EmptyValidation(notEmpty = false, pattern = "^[A-Za-z0-9 ,&.;'_-]+$", min = 0, max = 100,  messageLength = "{messageLength.merchant.merchantName}",
            messagePattern = "{messagepattern.merchant.merchantName}", 
           groups = SearchMerchantScreen.class)
	
	private String merchantName;
	
	@FieldValidation(notEmpty =true , pattern = "^[A-Za-z0-9 #]+$", min=1,max = 50, messageNotEmpty = "{messageNotEmpty.location.locationName}", messageLength = "{messageLength.location.locationName}", messagePattern = "{messagepattern.location.locationName}")
	private String locationName;
	
	@FieldValidation(notEmpty = true,  max = 255, messageNotEmpty="{messageNotEmpty.location.addressOne}",
			messageLength="{messageLength.location.addressOne}")
	private String addressOne;
	
	@FieldValidation(notEmpty = false,  max = 255, messageNotEmpty="{messageNotEmpty.location.addressTwo}",
			messageLength="{messageLength.location.addressTwo}")
	private String addressTwo;
	
	@FieldValidation(notEmpty = true, min=1, max = 20, messageNotEmpty = "{messageNotEmpty.location.city}", messageLength = "{messageLength.location.city}")
	private String city;
	private Long stateCodeID;
	private String stateName;
	private String countryName;
	private Long countryCodeID;

	@FieldValidation(notEmpty = true, pattern = "^[A-Za-z0-9 ]+$", max = 10, messageNotEmpty = "{messageNotEmpty.location.zip}", messageLength = "{messageLength.location.zip}", messagePattern = "{messagepattern.location.zip}")
	private String zip;
	private Long insUser;
	private Date insDate;
	private Long lastUpdUser;
	private Date lastUpdDate;
	
	public Long getLocationId() {
		return locationId;
	}
	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

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
		return "Location [locationId=" + locationId + ", merchantId="
				+ merchantId + ", merchantName=" + merchantName
				+ ", locationName=" + locationName + ", addressOne="
				+ addressOne + ", addressTwo=" + addressTwo + ", city=" + city
				+ ", stateCodeID=" + stateCodeID + ", stateName=" + stateName
				+ ", countryName=" + countryName + ", countryCodeID="
				+ countryCodeID + ", zip=" + zip + ", insUser=" + insUser
				+ ", insDate=" + insDate + ", lastUpdUser=" + lastUpdUser
				+ ", lastUpdDate=" + lastUpdDate + "]";
	}
	
}

