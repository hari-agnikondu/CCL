package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "LOCATION")
public class Location implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "location_id_seq", sequenceName = "location_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "location_id_seq")
	@Column(name = "LOCATION_ID")
	private Long locationId;
	
	@ManyToOne
	@JoinColumn(name="MERCHANT_ID")
	private Merchant merchant;

	@Column(name = "LOCATION_NAME")
	private String locationName;
	
	@Column(name = "ADDRESS_ONE")
	private String addressOne;
	
	@Column(name = "ADDRESS_TWO")
	private String addressTwo;
	
	@Column(name = "CITY")
	private String city;
	
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "STATE")
	private StateCode state;
	
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "COUNTRY")
	private CountryCode country;
	
	@Column(name = "ZIP")
	private String zip;

	@Column(name = "INS_USER" ,updatable = false)
	private Long insUser;

	@CreationTimestamp
	@Column(name = "INS_DATE" ,updatable = false )
	private Date insDate;

	@Column(name = "LAST_UPD_USER")
	private Long lastUpdUser;

	@UpdateTimestamp
	@Column(name = "LAST_UPD_DATE")
	private Date lastUpdDate;

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
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


	public StateCode getState() {
		return state;
	}

	public void setState(StateCode state) {
		this.state = state;
	}

	public CountryCode getCountry() {
		return country;
	}

	public void setCountry(CountryCode country) {
		this.country = country;
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
		this.insDate = new java.sql.Date(new java.util.Date().getTime());
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
		return "Location [locationId=" + locationId + ", merchant=" + merchant
				+ ", locationName=" + locationName + ", addressOne="
				+ addressOne + ", addressTwo=" + addressTwo + ", city=" + city
				+ ", state=" + state + ", country=" + country + ", zip=" + zip
				+ ", insUser=" + insUser + ", insDate=" + insDate
				+ ", lastUpdUser=" + lastUpdUser + ", lastUpdDate="
				+ lastUpdDate + "]";
	}


}
