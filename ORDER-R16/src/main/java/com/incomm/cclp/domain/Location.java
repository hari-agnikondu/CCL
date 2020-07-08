package com.incomm.cclp.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="clp_configuration.LOCATION")//added by Hari, because we are accessing the table from other schema without synonym
@NamedQuery(name="Location.findAll", query="SELECT i FROM Location i")
public class Location {
	private static final long serialVersionUID = 1L;

	@Id	
	//@SequenceGenerator(name="ISSUER_SEQ_GEN", sequenceName="seq_issuer_id", allocationSize=1)
	/*@SequenceGenerator(name="ISSUER_SEQ_GEN", sequenceName="ISSUER_ISSUER_ID_SEQ", allocationSize=1)//Kalaivani changed 26-02-2018 as per ER Diagram
	
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ISSUER_SEQ_GEN")*/
	@Column(name="LOCATION_ID")
	private long locationId;
	
	@Column(name="LOCATION_NAME")
	private String locationName;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="MERCHANT_ID" ,referencedColumnName="MERCHANT_ID")
	private Merchant merchant;

	@Column(name="ADDRESS_TWO")
	private String addressTwo;
	
	@Column(name="ADDRESS_ONE")
	private String addressOne;
	
	@Column(name="CITY")
	private String city;
	
	@Column(name="STATE")
	private String state;
	
	@Column(name="COUNTRY")
	private String country;
	
	@Column(name="ZIP")
	private String zip;
	
	@Column(name="INS_USER")
	private long insUser;
	
	@Temporal(TemporalType.DATE)
	@Column(name="INS_DATE")
	private Date insDate;
	
	@Column(name="LAST_UPD_USER")
	private long lastUpdUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPD_DATE")
	private Date lastUpdDate;


	public Date getInsDate() {
		return this.insDate;
	}

	public void setInsDate(Date insDate) {
		this.insDate = new java.sql.Date(new java.util.Date().getTime());
	}


	public Date getLastUpdDate() {
		return this.lastUpdDate;
	}

	public void setLastUpdDate(Date lastUpdDate) {
		this.lastUpdDate = lastUpdDate;
	}


public long getLocationId() {
		return locationId;
	}

	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}

	public String getAddressTwo() {
		return addressTwo;
	}

	public void setAddressTwo(String addressTwo) {
		this.addressTwo = addressTwo;
	}

	public String getAddressOne() {
		return addressOne;
	}

	public void setAddressOne(String addressOne) {
		this.addressOne = addressOne;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
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
}
