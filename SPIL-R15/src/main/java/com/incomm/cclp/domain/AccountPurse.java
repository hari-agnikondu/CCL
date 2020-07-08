/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.incomm.cclp.domain;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import com.incomm.cclp.constants.QueryConstants;

/**
 *
 * @author skocherla
 */
@Entity
@Table(name = "ACCOUNT_PURSE")
@XmlRootElement

@NamedNativeQueries({
		@NamedNativeQuery(name = "AccountPurse.getAccountPurse", query = QueryConstants.GET_ACCOUNT_PURSE_INFO, resultSetMapping = "AccountPurseInfoResult") })
@SqlResultSetMapping(name = "AccountPurseInfoResult", classes = { @ConstructorResult(targetClass = AccountPurseInfo.class, columns = {
		@ColumnResult(name = "avlbl", type = Double.class), @ColumnResult(name = "ledbl", type = Double.class),
		@ColumnResult(name = "pid", type = BigInteger.class), @ColumnResult(name = "currencyCode", type = String.class) }) })

@NamedQueries({ @NamedQuery(name = "AccountPurse.findAll", query = "SELECT a FROM AccountPurse a"),
		@NamedQuery(name = "AccountPurse.findByAccountId", query = "SELECT a FROM AccountPurse a WHERE a.accountPursePK.accountId = :accountId"),
		@NamedQuery(name = "AccountPurse.findByProductId", query = "SELECT a FROM AccountPurse a WHERE a.accountPursePK.productId = :productId"),
		@NamedQuery(name = "AccountPurse.findByPurseId", query = "SELECT a FROM AccountPurse a WHERE a.accountPursePK.purseId = :purseId"),
		@NamedQuery(name = "AccountPurse.findByLedgerBalance", query = "SELECT a FROM AccountPurse a WHERE a.ledgerBalance = :ledgerBalance"),
		@NamedQuery(name = "AccountPurse.findByAvailableBalance", query = "SELECT a FROM AccountPurse a WHERE a.availableBalance = :availableBalance"),
		@NamedQuery(name = "AccountPurse.findByPurseType", query = "SELECT a FROM AccountPurse a WHERE a.purseType = :purseType"),
		@NamedQuery(name = "AccountPurse.findByCurrencyCode", query = "SELECT a FROM AccountPurse a WHERE a.currencyCode = :currencyCode"),
		@NamedQuery(name = "AccountPurse.findByUpc", query = "SELECT a FROM AccountPurse a WHERE a.upc = :upc"),
		@NamedQuery(name = "AccountPurse.findByPurseTypeId", query = "SELECT a FROM AccountPurse a WHERE a.purseTypeId = :purseTypeId"),
		@NamedQuery(name = "AccountPurse.findByInsUser", query = "SELECT a FROM AccountPurse a WHERE a.insUser = :insUser"),
		@NamedQuery(name = "AccountPurse.findByInsDate", query = "SELECT a FROM AccountPurse a WHERE a.insDate = :insDate"),
		@NamedQuery(name = "AccountPurse.findByLastUpdUser", query = "SELECT a FROM AccountPurse a WHERE a.lastUpdUser = :lastUpdUser"),
		@NamedQuery(name = "AccountPurse.findByLastUpdDate", query = "SELECT a FROM AccountPurse a WHERE a.lastUpdDate = :lastUpdDate"),
		@NamedQuery(name = "AccountPurse.findByMonthlyfeeCounter", query = "SELECT a FROM AccountPurse a WHERE a.monthlyfeeCounter = :monthlyfeeCounter"),
		@NamedQuery(name = "AccountPurse.findByWeeklyfeeCounter", query = "SELECT a FROM AccountPurse a WHERE a.weeklyfeeCounter = :weeklyfeeCounter"),
		@NamedQuery(name = "AccountPurse.findByFirstLoadDate", query = "SELECT a FROM AccountPurse a WHERE a.firstLoadDate = :firstLoadDate"),
		@NamedQuery(name = "AccountPurse.findByAnnualfeeCounter", query = "SELECT a FROM AccountPurse a WHERE a.annualfeeCounter = :annualfeeCounter"),
		@NamedQuery(name = "AccountPurse.findByDormancyfeeCounter", query = "SELECT a FROM AccountPurse a WHERE a.dormancyfeeCounter = :dormancyfeeCounter"),
		@NamedQuery(name = "AccountPurse.getAccountPurseByPurseId", query = QueryConstants.GET_ACCOUNTPURSE_LIST),
		@NamedQuery(name = "AccountPurse.getAccountPurseByAccountPurseId", query = QueryConstants.GET_ACCOUNTPURSE_DETAILS) })
public class AccountPurse implements Serializable {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	protected AccountPursePK accountPursePK;
	@Basic // (optional = false)
	@NotNull
	@Column(name = "LEDGER_BALANCE")
	private double ledgerBalance;
	@Basic // (optional = false)
	@NotNull
	@Column(name = "AVAILABLE_BALANCE")
	private double availableBalance;
	@Basic // (optional = false)
	@NotNull
	@Size(min = 1, max = 20)
	@Column(name = "PURSE_TYPE")
	private String purseType;
	@Size(max = 3)
	@Column(name = "CURRENCY_CODE")
	private String currencyCode;
	@Size(max = 20)
	@Column(name = "UPC")
	private String upc;
	@Basic // (optional = false)
	@NotNull
	@Column(name = "PURSE_TYPE_ID")
	private BigInteger purseTypeId;
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
	@Column(name = "MONTHLYFEE_COUNTER")
	private Long monthlyfeeCounter;
	@Column(name = "WEEKLYFEE_COUNTER")
	private Long weeklyfeeCounter;
	@Column(name = "FIRST_LOAD_DATE", nullable = true)
//    @Temporal(TemporalType.TIMESTAMP)
	private Date firstLoadDate;
	@Column(name = "ANNUALFEE_COUNTER")
	private Long annualfeeCounter;
	@Column(name = "DORMANCYFEE_COUNTER")
	private Long dormancyfeeCounter;

	@JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "ACCOUNT_ID", insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private Account account;

	@Column(name = "EXPIRY_DATE")
	private Date expiryDate;

	@Column(name = "EFFECTIVE_DATE")
	private Date effectiveDate;

	@Column(name = "ACCOUNT_PURSE_ID")
	private BigInteger accountPurseId;

	public AccountPurse() {
	}

	public AccountPurse(AccountPursePK accountPursePK) {
		this.accountPursePK = accountPursePK;
	}

	public AccountPurse(AccountPursePK accountPursePK, double ledgerBalance, double availableBalance, String purseType,
			BigInteger purseTypeId) {
		this.accountPursePK = accountPursePK;
		this.ledgerBalance = ledgerBalance;
		this.availableBalance = availableBalance;
		this.purseType = purseType;
		this.purseTypeId = purseTypeId;
	}

	public AccountPurse(BigInteger accountId, BigInteger productId, BigInteger purseId) {
		this.accountPursePK = new AccountPursePK(accountId, productId, purseId);
	}

	public AccountPursePK getAccountPursePK() {
		return accountPursePK;
	}

	public void setAccountPursePK(AccountPursePK accountPursePK) {
		this.accountPursePK = accountPursePK;
	}

	public double getLedgerBalance() {
		return ledgerBalance;
	}

	public void setLedgerBalance(double ledgerBalance) {
		this.ledgerBalance = ledgerBalance;
	}

	public double getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(double availableBalance) {
		this.availableBalance = availableBalance;
	}

	public String getPurseType() {
		return purseType;
	}

	public void setPurseType(String purseType) {
		this.purseType = purseType;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getUpc() {
		return upc;
	}

	public void setUpc(String upc) {
		this.upc = upc;
	}

	public BigInteger getPurseTypeId() {
		return purseTypeId;
	}

	public void setPurseTypeId(BigInteger purseTypeId) {
		this.purseTypeId = purseTypeId;
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

	public Long getMonthlyfeeCounter() {
		return monthlyfeeCounter;
	}

	public void setMonthlyfeeCounter(Long monthlyfeeCounter) {
		this.monthlyfeeCounter = monthlyfeeCounter;
	}

	public Long getWeeklyfeeCounter() {
		return weeklyfeeCounter;
	}

	public void setWeeklyfeeCounter(Long weeklyfeeCounter) {
		this.weeklyfeeCounter = weeklyfeeCounter;
	}

	public Date getFirstLoadDate() {
		return firstLoadDate;
	}

	public void setFirstLoadDate(Date firstLoadDate) {
		this.firstLoadDate = firstLoadDate;
	}

	public Long getAnnualfeeCounter() {
		return annualfeeCounter;
	}

	public void setAnnualfeeCounter(Long annualfeeCounter) {
		this.annualfeeCounter = annualfeeCounter;
	}

	public Long getDormancyfeeCounter() {
		return dormancyfeeCounter;
	}

	public void setDormancyfeeCounter(Long dormancyfeeCounter) {
		this.dormancyfeeCounter = dormancyfeeCounter;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public BigInteger getAccountPurseId() {
		return accountPurseId;
	}

	public void setAccountPurseId(BigInteger accountPurseId) {
		this.accountPurseId = accountPurseId;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (accountPursePK != null ? accountPursePK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof AccountPurse)) {
			return false;
		}
		AccountPurse other = (AccountPurse) object;
		if ((this.accountPursePK == null && other.accountPursePK != null)
				|| (this.accountPursePK != null && !this.accountPursePK.equals(other.accountPursePK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.incomm.fs.cclp_spil.AccountPurse[ accountPursePK=" + accountPursePK + " ]";
	}

}
