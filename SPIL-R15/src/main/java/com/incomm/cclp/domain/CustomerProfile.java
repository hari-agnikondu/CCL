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
import javax.persistence.Lob;
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
@Table(name = "CUSTOMER_PROFILE")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "CustomerProfile.findAll", query = "SELECT c FROM CustomerProfile c"),
		@NamedQuery(name = "CustomerProfile.findByCustomerCode", query = "SELECT c FROM CustomerProfile c WHERE c.customerCode = :customerCode"),
		@NamedQuery(name = "CustomerProfile.findByCustomerType", query = "SELECT c FROM CustomerProfile c WHERE c.customerType = :customerType"),
		@NamedQuery(name = "CustomerProfile.findByFirstName", query = "SELECT c FROM CustomerProfile c WHERE c.firstName = :firstName"),
		@NamedQuery(name = "CustomerProfile.findByMiddleName", query = "SELECT c FROM CustomerProfile c WHERE c.middleName = :middleName"),
		@NamedQuery(name = "CustomerProfile.findByLastName", query = "SELECT c FROM CustomerProfile c WHERE c.lastName = :lastName"),
		@NamedQuery(name = "CustomerProfile.findByGenderType", query = "SELECT c FROM CustomerProfile c WHERE c.genderType = :genderType"),
		@NamedQuery(name = "CustomerProfile.findByMaritalStatus", query = "SELECT c FROM CustomerProfile c WHERE c.maritalStatus = :maritalStatus"),
		@NamedQuery(name = "CustomerProfile.findByPermId", query = "SELECT c FROM CustomerProfile c WHERE c.permId = :permId"),
		@NamedQuery(name = "CustomerProfile.findByEmailOne", query = "SELECT c FROM CustomerProfile c WHERE c.emailOne = :emailOne"),
		@NamedQuery(name = "CustomerProfile.findByEmailTwo", query = "SELECT c FROM CustomerProfile c WHERE c.emailTwo = :emailTwo"),
		@NamedQuery(name = "CustomerProfile.findByMobileOne", query = "SELECT c FROM CustomerProfile c WHERE c.mobileOne = :mobileOne"),
		@NamedQuery(name = "CustomerProfile.findByMobileTwo", query = "SELECT c FROM CustomerProfile c WHERE c.mobileTwo = :mobileTwo"),
		@NamedQuery(name = "CustomerProfile.findByCustomerId", query = "SELECT c FROM CustomerProfile c WHERE c.customerId = :customerId"),
		@NamedQuery(name = "CustomerProfile.findBySsn", query = "SELECT c FROM CustomerProfile c WHERE c.ssn = :ssn"),
		@NamedQuery(name = "CustomerProfile.findByHobbies", query = "SELECT c FROM CustomerProfile c WHERE c.hobbies = :hobbies"),
		@NamedQuery(name = "CustomerProfile.findByUserName", query = "SELECT c FROM CustomerProfile c WHERE c.userName = :userName"),
		@NamedQuery(name = "CustomerProfile.findByPasswordHash", query = "SELECT c FROM CustomerProfile c WHERE c.passwordHash = :passwordHash"),
		@NamedQuery(name = "CustomerProfile.findByWrongLogincnt", query = "SELECT c FROM CustomerProfile c WHERE c.wrongLogincnt = :wrongLogincnt"),
		@NamedQuery(name = "CustomerProfile.findByAcctLockFlag", query = "SELECT c FROM CustomerProfile c WHERE c.acctLockFlag = :acctLockFlag"),
		@NamedQuery(name = "CustomerProfile.findByAcctunlockUser", query = "SELECT c FROM CustomerProfile c WHERE c.acctunlockUser = :acctunlockUser"),
		@NamedQuery(name = "CustomerProfile.findByAcctUnlockDate", query = "SELECT c FROM CustomerProfile c WHERE c.acctUnlockDate = :acctUnlockDate"),
		@NamedQuery(name = "CustomerProfile.findByLastLoginDate", query = "SELECT c FROM CustomerProfile c WHERE c.lastLoginDate = :lastLoginDate"),
		@NamedQuery(name = "CustomerProfile.findByIsActive", query = "SELECT c FROM CustomerProfile c WHERE c.isActive = :isActive"),
		@NamedQuery(name = "CustomerProfile.findByProductId", query = "SELECT c FROM CustomerProfile c WHERE c.productId = :productId"),
		@NamedQuery(name = "CustomerProfile.findByPartnerId", query = "SELECT c FROM CustomerProfile c WHERE c.partnerId = :partnerId"),
		@NamedQuery(name = "CustomerProfile.findByOptinoptoutStatus", query = "SELECT c FROM CustomerProfile c WHERE c.optinoptoutStatus = :optinoptoutStatus"),
		@NamedQuery(name = "CustomerProfile.findByAddressId", query = "SELECT c FROM CustomerProfile c WHERE c.addressId = :addressId"),
		@NamedQuery(name = "CustomerProfile.findByAlertLanguage", query = "SELECT c FROM CustomerProfile c WHERE c.alertLanguage = :alertLanguage"),
		@NamedQuery(name = "CustomerProfile.findByBirthDate", query = "SELECT c FROM CustomerProfile c WHERE c.birthDate = :birthDate"),
		@NamedQuery(name = "CustomerProfile.findByAlertLevel", query = "SELECT c FROM CustomerProfile c WHERE c.alertLevel = :alertLevel") })
public class CustomerProfile implements Serializable {

	private static final long serialVersionUID = 1L;
	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce
	// field validation
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "CUSTOMER_CODE")
	private BigDecimal customerCode;
	@Size(max = 20)
	@Column(name = "CUSTOMER_TYPE")
	private String customerType;
	@Size(max = 2000)
	@Column(name = "FIRST_NAME")
	private String firstName;
	@Size(max = 2000)
	@Column(name = "MIDDLE_NAME")
	private String middleName;
	@Size(max = 2000)
	@Column(name = "LAST_NAME")
	private String lastName;
	@Size(max = 1)
	@Column(name = "GENDER_TYPE")
	private String genderType;
	@Size(max = 1)
	@Column(name = "MARITAL_STATUS")
	private String maritalStatus;
	@Size(max = 20)
	@Column(name = "PERM_ID")
	private String permId;
	@Size(max = 2000)
	@Column(name = "EMAIL_ONE")
	private String emailOne;
	@Size(max = 2000)
	@Column(name = "EMAIL_TWO")
	private String emailTwo;
	@Size(max = 200)
	@Column(name = "MOBILE_ONE")
	private String mobileOne;
	@Size(max = 200)
	@Column(name = "MOBILE_TWO")
	private String mobileTwo;
	@Column(name = "CUSTOMER_ID")
	private BigInteger customerId;
	@Size(max = 100)
	@Column(name = "SSN")
	private String ssn;
	@Size(max = 50)
	@Column(name = "HOBBIES")
	private String hobbies;
	@Size(max = 2000)
	@Column(name = "USER_NAME")
	private String userName;
	@Size(max = 100)
	@Column(name = "PASSWORD_HASH")
	private String passwordHash;
	@Column(name = "WRONG_LOGINCNT")
	private Short wrongLogincnt;
	@Size(max = 1)
	@Column(name = "ACCT_LOCK_FLAG")
	private String acctLockFlag;
	@Column(name = "ACCTUNLOCK_USER")
	private Integer acctunlockUser;
	@Column(name = "ACCT_UNLOCK_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date acctUnlockDate;
	@Column(name = "LAST_LOGIN_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLoginDate;
	@Size(max = 1)
	@Column(name = "IS_ACTIVE")
	private String isActive;
	@Basic(optional = false)
	@NotNull
	@Column(name = "PRODUCT_ID")
	private BigInteger productId;
	@Basic(optional = false)
	@NotNull
	@Column(name = "PARTNER_ID")
	private BigInteger partnerId;
	@Size(max = 1)
	@Column(name = "OPTINOPTOUT_STATUS")
	private String optinoptoutStatus;
	@Column(name = "ADDRESS_ID")
	private BigInteger addressId;
	@Size(max = 20)
	@Column(name = "ALERT_LANGUAGE")
	private String alertLanguage;
	@Size(max = 200)
	@Column(name = "BIRTH_DATE")
	private String birthDate;
	@Size(max = 20)
	@Column(name = "ALERT_LEVEL")
	private String alertLevel;
	@Lob
	@Column(name = "ALERT_SETTING")
	private String alertSetting;
	@OneToMany(mappedBy = "customerCode")
	private Collection<Address> addressCollection;
	@OneToMany(mappedBy = "customerCode")
	private Collection<Card> cardCollection;

	public CustomerProfile() {
	}

	public CustomerProfile(BigDecimal customerCode) {
		this.customerCode = customerCode;
	}

	public CustomerProfile(BigDecimal customerCode, BigInteger productId, BigInteger partnerId) {
		this.customerCode = customerCode;
		this.productId = productId;
		this.partnerId = partnerId;
	}

	public BigDecimal getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(BigDecimal customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGenderType() {
		return genderType;
	}

	public void setGenderType(String genderType) {
		this.genderType = genderType;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getPermId() {
		return permId;
	}

	public void setPermId(String permId) {
		this.permId = permId;
	}

	public String getEmailOne() {
		return emailOne;
	}

	public void setEmailOne(String emailOne) {
		this.emailOne = emailOne;
	}

	public String getEmailTwo() {
		return emailTwo;
	}

	public void setEmailTwo(String emailTwo) {
		this.emailTwo = emailTwo;
	}

	public String getMobileOne() {
		return mobileOne;
	}

	public void setMobileOne(String mobileOne) {
		this.mobileOne = mobileOne;
	}

	public String getMobileTwo() {
		return mobileTwo;
	}

	public void setMobileTwo(String mobileTwo) {
		this.mobileTwo = mobileTwo;
	}

	public BigInteger getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigInteger customerId) {
		this.customerId = customerId;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getHobbies() {
		return hobbies;
	}

	public void setHobbies(String hobbies) {
		this.hobbies = hobbies;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public Short getWrongLogincnt() {
		return wrongLogincnt;
	}

	public void setWrongLogincnt(Short wrongLogincnt) {
		this.wrongLogincnt = wrongLogincnt;
	}

	public String getAcctLockFlag() {
		return acctLockFlag;
	}

	public void setAcctLockFlag(String acctLockFlag) {
		this.acctLockFlag = acctLockFlag;
	}

	public Integer getAcctunlockUser() {
		return acctunlockUser;
	}

	public void setAcctunlockUser(Integer acctunlockUser) {
		this.acctunlockUser = acctunlockUser;
	}

	public Date getAcctUnlockDate() {
		return acctUnlockDate;
	}

	public void setAcctUnlockDate(Date acctUnlockDate) {
		this.acctUnlockDate = acctUnlockDate;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public BigInteger getProductId() {
		return productId;
	}

	public void setProductId(BigInteger productId) {
		this.productId = productId;
	}

	public BigInteger getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(BigInteger partnerId) {
		this.partnerId = partnerId;
	}

	public String getOptinoptoutStatus() {
		return optinoptoutStatus;
	}

	public void setOptinoptoutStatus(String optinoptoutStatus) {
		this.optinoptoutStatus = optinoptoutStatus;
	}

	public BigInteger getAddressId() {
		return addressId;
	}

	public void setAddressId(BigInteger addressId) {
		this.addressId = addressId;
	}

	public String getAlertLanguage() {
		return alertLanguage;
	}

	public void setAlertLanguage(String alertLanguage) {
		this.alertLanguage = alertLanguage;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getAlertLevel() {
		return alertLevel;
	}

	public void setAlertLevel(String alertLevel) {
		this.alertLevel = alertLevel;
	}

	public String getAlertSetting() {
		return alertSetting;
	}

	public void setAlertSetting(String alertSetting) {
		this.alertSetting = alertSetting;
	}

	@XmlTransient
	public Collection<Address> getAddressCollection() {
		return addressCollection;
	}

	public void setAddressCollection(Collection<Address> addressCollection) {
		this.addressCollection = addressCollection;
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
		hash += (customerCode != null ? customerCode.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof CustomerProfile)) {
			return false;
		}
		CustomerProfile other = (CustomerProfile) object;
		if ((this.customerCode == null && other.customerCode != null)
				|| (this.customerCode != null && !this.customerCode.equals(other.customerCode))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.incomm.fs.cclp_spil.CustomerProfile[ customerCode=" + customerCode + " ]";
	}

}
