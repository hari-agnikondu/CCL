package com.incomm.cclp.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author skocherla
 */
@Entity
@Table(name = "ACCOUNT_PURSE_USAGE")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "AccountPurseUsage.findAll", query = "SELECT a FROM AccountPurseUsage a"),
		@NamedQuery(name = "AccountPurseUsage.findByAccountId", query = "SELECT a FROM AccountPurseUsage a WHERE a.accountId = :accountId"),
		@NamedQuery(name = "AccountPurseUsage.findByPurseId", query = "SELECT a FROM AccountPurseUsage a WHERE a.purseId = :purseId"),
		@NamedQuery(name = "AccountPurseUsage.findBySKUCode", query = "SELECT a FROM AccountPurseUsage a WHERE a.skuCode = :skuCode"),
		@NamedQuery(name = "AccountPurseUsage.findByLastTxndate", query = "SELECT a FROM AccountPurseUsage a WHERE a.lastTxndate = :lastTxndate"),
		@NamedQuery(name = "AccountPurseUsage.findByInsUser", query = "SELECT a FROM AccountPurseUsage a WHERE a.insUser = :insUser"),
		@NamedQuery(name = "AccountPurseUsage.findByInsDate", query = "SELECT a FROM AccountPurseUsage a WHERE a.insDate = :insDate"),
		@NamedQuery(name = "AccountPurseUsage.findByLastUpdUser", query = "SELECT a FROM AccountPurseUsage a WHERE a.lastUpdUser = :lastUpdUser"),
		@NamedQuery(name = "AccountPurseUsage.findByLastUpdDate", query = "SELECT a FROM AccountPurseUsage a WHERE a.lastUpdDate = :lastUpdDate") })

public class AccountPurseUsage implements Serializable {

	private static final long serialVersionUID = 1L;
	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce
	// field validation
	@Id
	@Basic(optional = false)
	// @NotNull
	@Column(name = "ACCOUNT_ID")
	private BigDecimal accountId;
	// @Size(max = 20)
	@Column(name = "PURSE_ID")
	private BigDecimal purseId;
	@Size(max = 30)
	@Column(name = "SKU_CODE")
	private String skuCode;
	@Lob
	@Column(name = "USAGE_LIMIT")
	private String usageLimit;
	@Lob
	@Column(name = "USAGE_FEE")
	private String usageFee;
	@Column(name = "LAST_TXNDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastTxndate;
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

	public AccountPurseUsage() {
	}

	public AccountPurseUsage(BigDecimal accountId) {
		this.accountId = accountId;
	}

	public AccountPurseUsage(BigDecimal accountId, BigDecimal purseId) {
		this.accountId = accountId;
		this.purseId = purseId;
	}

	public BigDecimal getAccountId() {
		return accountId;
	}

	public void setAccountId(BigDecimal accountId) {
		this.accountId = accountId;
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

	public BigDecimal getPurseId() {
		return purseId;
	}

	public void setPurseId(BigDecimal purseId) {
		this.purseId = purseId;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getUsageLimit() {
		return usageLimit;
	}

	public void setUsageLimit(String usageLimit) {
		this.usageLimit = usageLimit;
	}

	public String getUsageFee() {
		return usageFee;
	}

	public void setUsageFee(String usageFee) {
		this.usageFee = usageFee;
	}

	public Date getLastTxndate() {
		return lastTxndate;
	}

	public void setLastTxndate(Date lastTxndate) {
		this.lastTxndate = lastTxndate;
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
		if (!(object instanceof AccountPurseUsage)) {
			return false;
		}
		AccountPurseUsage other = (AccountPurseUsage) object;
		if ((this.accountId == null && other.accountId != null) || (this.accountId != null && !this.accountId.equals(other.accountId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.incomm.fs.cclp_spil.AccountPurseUsage[ accountId=" + accountId + " ]";
	}

}
