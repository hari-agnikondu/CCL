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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "ACCOUNT")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a"),
		@NamedQuery(name = "Account.findByAccountId", query = "SELECT a FROM Account a WHERE a.accountId = :accountId"),
		@NamedQuery(name = "Account.findByAccountNumber", query = "SELECT a FROM Account a WHERE a.accountNumber = :accountNumber"),
		@NamedQuery(name = "Account.findByAccountStatus", query = "SELECT a FROM Account a WHERE a.accountStatus = :accountStatus"),
		@NamedQuery(name = "Account.findByInsUser", query = "SELECT a FROM Account a WHERE a.insUser = :insUser"),
		@NamedQuery(name = "Account.findByInsDate", query = "SELECT a FROM Account a WHERE a.insDate = :insDate"),
		@NamedQuery(name = "Account.findByLastUpdUser", query = "SELECT a FROM Account a WHERE a.lastUpdUser = :lastUpdUser"),
		@NamedQuery(name = "Account.findByLastUpdDate", query = "SELECT a FROM Account a WHERE a.lastUpdDate = :lastUpdDate"),
		@NamedQuery(name = "Account.findByProductId", query = "SELECT a FROM Account a WHERE a.productId = :productId"),
		@NamedQuery(name = "Account.findByTypeCode", query = "SELECT a FROM Account a WHERE a.typeCode = :typeCode"),
		@NamedQuery(name = "Account.findByStatCode", query = "SELECT a FROM Account a WHERE a.statCode = :statCode"),
		@NamedQuery(name = "Account.findByInitialloadAmt", query = "SELECT a FROM Account a WHERE a.initialloadAmt = :initialloadAmt"),
		@NamedQuery(name = "Account.findByActiveFlag", query = "SELECT a FROM Account a WHERE a.activeFlag = :activeFlag"),
		@NamedQuery(name = "Account.findByRedemptionDelayFlag", query = "SELECT a FROM Account a WHERE a.redemptionDelayFlag = :redemptionDelayFlag"),
		@NamedQuery(name = "Account.findByNewInitialloadAmt", query = "SELECT a FROM Account a WHERE a.newInitialloadAmt = :newInitialloadAmt") })
public class Account implements Serializable {

	private static final long serialVersionUID = 1L;
	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce
	// field validation
	@Id
	@Basic(optional = false)
	// @NotNull
	@Column(name = "ACCOUNT_ID")
	private BigDecimal accountId;
	@Size(max = 20)
	@Column(name = "ACCOUNT_NUMBER")
	private String accountNumber;
	@Size(max = 2)
	@Column(name = "ACCOUNT_STATUS")
	private String accountStatus;
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
	@Basic(optional = false)
	@NotNull
	@Column(name = "PRODUCT_ID")
	private BigInteger productId;
	@Column(name = "TYPE_CODE")
	private BigInteger typeCode;
	@Column(name = "STAT_CODE")
	private Short statCode;
	@Column(name = "INITIALLOAD_AMT")
	private BigDecimal initialloadAmt;
	@Size(max = 20)
	@Column(name = "ACTIVE_FLAG")
	private String activeFlag;
	@Size(max = 20)
	@Column(name = "REDEMPTION_DELAY_FLAG")
	private String redemptionDelayFlag;
	@Column(name = "NEW_INITIALLOAD_AMT")
	private BigDecimal newInitialloadAmt;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
	private Collection<AccountPurse> accountPurseCollection;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "accountId")
	private Collection<Card> cardCollection;

	public Account() {
	}

	public Account(BigDecimal accountId) {
		this.accountId = accountId;
	}

	public Account(BigDecimal accountId, BigInteger productId) {
		this.accountId = accountId;
		this.productId = productId;
	}

	public BigDecimal getAccountId() {
		return accountId;
	}

	public void setAccountId(BigDecimal accountId) {
		this.accountId = accountId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
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

	public BigInteger getProductId() {
		return productId;
	}

	public void setProductId(BigInteger productId) {
		this.productId = productId;
	}

	public BigInteger getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(BigInteger typeCode) {
		this.typeCode = typeCode;
	}

	public Short getStatCode() {
		return statCode;
	}

	public void setStatCode(Short statCode) {
		this.statCode = statCode;
	}

	public BigDecimal getInitialloadAmt() {
		return initialloadAmt;
	}

	public void setInitialloadAmt(BigDecimal initialloadAmt) {
		this.initialloadAmt = initialloadAmt;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public String getRedemptionDelayFlag() {
		return redemptionDelayFlag;
	}

	public void setRedemptionDelayFlag(String redemptionDelayFlag) {
		this.redemptionDelayFlag = redemptionDelayFlag;
	}

	public BigDecimal getNewInitialloadAmt() {
		return newInitialloadAmt;
	}

	public void setNewInitialloadAmt(BigDecimal newInitialloadAmt) {
		this.newInitialloadAmt = newInitialloadAmt;
	}

	@XmlTransient
	public Collection<AccountPurse> getAccountPurseCollection() {
		return accountPurseCollection;
	}

	public void setAccountPurseCollection(Collection<AccountPurse> accountPurseCollection) {
		this.accountPurseCollection = accountPurseCollection;
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
		hash += (accountId != null ? accountId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Account)) {
			return false;
		}
		Account other = (Account) object;
		if ((this.accountId == null && other.accountId != null) || (this.accountId != null && !this.accountId.equals(other.accountId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.incomm.fs.cclp_spil.Account[ accountId=" + accountId + " ]";
	}

}
