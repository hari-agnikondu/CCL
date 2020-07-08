/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.incomm.cclp.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author skocherla
 */
@Entity
@Table(name = "ADDRESS")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Address.findAll", query = "SELECT a FROM Address a"),
		@NamedQuery(name = "Address.findByAddressId", query = "SELECT a FROM Address a WHERE a.addressId = :addressId"),
		@NamedQuery(name = "Address.findByAddressOne", query = "SELECT a FROM Address a WHERE a.addressOne = :addressOne"),
		@NamedQuery(name = "Address.findByAddressTwo", query = "SELECT a FROM Address a WHERE a.addressTwo = :addressTwo"),
		@NamedQuery(name = "Address.findByAddressThree", query = "SELECT a FROM Address a WHERE a.addressThree = :addressThree"),
		@NamedQuery(name = "Address.findByAddressFour", query = "SELECT a FROM Address a WHERE a.addressFour = :addressFour"),
		@NamedQuery(name = "Address.findByPinCode", query = "SELECT a FROM Address a WHERE a.pinCode = :pinCode"),
		@NamedQuery(name = "Address.findByPhoneOne", query = "SELECT a FROM Address a WHERE a.phoneOne = :phoneOne"),
		@NamedQuery(name = "Address.findByPhoneTwo", query = "SELECT a FROM Address a WHERE a.phoneTwo = :phoneTwo"),
		@NamedQuery(name = "Address.findByCountryCode", query = "SELECT a FROM Address a WHERE a.countryCode = :countryCode"),
		@NamedQuery(name = "Address.findByCityName", query = "SELECT a FROM Address a WHERE a.cityName = :cityName"),
		@NamedQuery(name = "Address.findByFaxOne", query = "SELECT a FROM Address a WHERE a.faxOne = :faxOne"),
		@NamedQuery(name = "Address.findByAddrFlag", query = "SELECT a FROM Address a WHERE a.addrFlag = :addrFlag"),
		@NamedQuery(name = "Address.findByStateSwitch", query = "SELECT a FROM Address a WHERE a.stateSwitch = :stateSwitch"),
		@NamedQuery(name = "Address.findByInsUser", query = "SELECT a FROM Address a WHERE a.insUser = :insUser"),
		@NamedQuery(name = "Address.findByInsDate", query = "SELECT a FROM Address a WHERE a.insDate = :insDate"),
		@NamedQuery(name = "Address.findByLastUpdUser", query = "SELECT a FROM Address a WHERE a.lastUpdUser = :lastUpdUser"),
		@NamedQuery(name = "Address.findByLastUpdDate", query = "SELECT a FROM Address a WHERE a.lastUpdDate = :lastUpdDate"),
		@NamedQuery(name = "Address.findByEmail", query = "SELECT a FROM Address a WHERE a.email = :email"),
		@NamedQuery(name = "Address.findByMoblOne", query = "SELECT a FROM Address a WHERE a.moblOne = :moblOne"),
		@NamedQuery(name = "Address.findByStateCode", query = "SELECT a FROM Address a WHERE a.stateCode = :stateCode"),
		@NamedQuery(name = "Address.findByCommType", query = "SELECT a FROM Address a WHERE a.commType = :commType") })
public class Address implements Serializable {

	private static final long serialVersionUID = 1L;
	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce
	// field validation
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "ADDRESS_ID")
	private BigDecimal addressId;
	@Size(max = 2000)
	@Column(name = "ADDRESS_ONE")
	private String addressOne;
	@Size(max = 2000)
	@Column(name = "ADDRESS_TWO")
	private String addressTwo;
	@Size(max = 2000)
	@Column(name = "ADDRESS_THREE")
	private String addressThree;
	@Size(max = 200)
	@Column(name = "ADDRESS_FOUR")
	private String addressFour;
	@Size(max = 2000)
	@Column(name = "PIN_CODE")
	private String pinCode;
	@Size(max = 2000)
	@Column(name = "PHONE_ONE")
	private String phoneOne;
	@Size(max = 1000)
	@Column(name = "PHONE_TWO")
	private String phoneTwo;
	@Column(name = "COUNTRY_CODE")
	private Short countryCode;
	@Size(max = 2000)
	@Column(name = "CITY_NAME")
	private String cityName;
	@Size(max = 20)
	@Column(name = "FAX_ONE")
	private String faxOne;
	@Column(name = "ADDR_FLAG")
	private Character addrFlag;
	@Size(max = 3)
	@Column(name = "STATE_SWITCH")
	private String stateSwitch;
	@Column(name = "INS_USER")
	private BigInteger insUser;
	@Column(name = "INS_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date insDate;
	@Column(name = "LAST_UPD_USER")
	private BigInteger lastUpdUser;
	@Column(name = "LAST_UPD_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdDate;
	// @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
	// message="Invalid email")//if the field contains email address consider using this annotation to enforce field
	// validation
	@Size(max = 2000)
	@Column(name = "EMAIL")
	private String email;
	@Size(max = 2000)
	@Column(name = "MOBL_ONE")
	private String moblOne;
	@Column(name = "STATE_CODE")
	private Short stateCode;
	@Column(name = "COMM_TYPE")
	private Character commType;
	@JoinColumn(name = "CUSTOMER_CODE", referencedColumnName = "CUSTOMER_CODE")
	@ManyToOne
	private CustomerProfile customerCode;
	@OneToMany(mappedBy = "addressId")
	private Collection<Card> cardCollection;

	public Address() {
	}

	public Address(BigDecimal addressId) {
		this.addressId = addressId;
	}

	public BigDecimal getAddressId() {
		return addressId;
	}

	public void setAddressId(BigDecimal addressId) {
		this.addressId = addressId;
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

	public String getAddressThree() {
		return addressThree;
	}

	public void setAddressThree(String addressThree) {
		this.addressThree = addressThree;
	}

	public String getAddressFour() {
		return addressFour;
	}

	public void setAddressFour(String addressFour) {
		this.addressFour = addressFour;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public String getPhoneOne() {
		return phoneOne;
	}

	public void setPhoneOne(String phoneOne) {
		this.phoneOne = phoneOne;
	}

	public String getPhoneTwo() {
		return phoneTwo;
	}

	public void setPhoneTwo(String phoneTwo) {
		this.phoneTwo = phoneTwo;
	}

	public Short getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(Short countryCode) {
		this.countryCode = countryCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getFaxOne() {
		return faxOne;
	}

	public void setFaxOne(String faxOne) {
		this.faxOne = faxOne;
	}

	public Character getAddrFlag() {
		return addrFlag;
	}

	public void setAddrFlag(Character addrFlag) {
		this.addrFlag = addrFlag;
	}

	public String getStateSwitch() {
		return stateSwitch;
	}

	public void setStateSwitch(String stateSwitch) {
		this.stateSwitch = stateSwitch;
	}

	public BigInteger getInsUser() {
		return insUser;
	}

	public void setInsUser(BigInteger insUser) {
		this.insUser = insUser;
	}

	public Date getInsDate() {
		return insDate;
	}

	public void setInsDate(Date insDate) {
		this.insDate = insDate;
	}

	public BigInteger getLastUpdUser() {
		return lastUpdUser;
	}

	public void setLastUpdUser(BigInteger lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	public Date getLastUpdDate() {
		return lastUpdDate;
	}

	public void setLastUpdDate(Date lastUpdDate) {
		this.lastUpdDate = lastUpdDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMoblOne() {
		return moblOne;
	}

	public void setMoblOne(String moblOne) {
		this.moblOne = moblOne;
	}

	public Short getStateCode() {
		return stateCode;
	}

	public void setStateCode(Short stateCode) {
		this.stateCode = stateCode;
	}

	public Character getCommType() {
		return commType;
	}

	public void setCommType(Character commType) {
		this.commType = commType;
	}

	public CustomerProfile getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(CustomerProfile customerCode) {
		this.customerCode = customerCode;
	}

	@XmlTransient
	public Collection<Card> getCardCollection() {
		return cardCollection;
	}

	public void setCardCollection(Collection<Card> cardCollection) {
		this.cardCollection = cardCollection;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (addressId != null ? addressId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Address)) {
			return false;
		}
		Address other = (Address) object;
		if ((this.addressId == null && other.addressId != null) || (this.addressId != null && !this.addressId.equals(other.addressId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.incomm.fs.cclp_spil.Address[ addressId=" + addressId + " ]";
	}

}
