/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.incomm.cclp.domain;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author skocherla
 */
@Embeddable
public class AccountPursePK implements Serializable {

	@Basic(optional = false)
	@NotNull
	@Column(name = "ACCOUNT_ID")
	private BigInteger accountId;
	@Basic(optional = false)
	@NotNull
	@Column(name = "PRODUCT_ID")
	private BigInteger productId;
	@Basic(optional = false)
	@NotNull
	@Column(name = "PURSE_ID")
	private BigInteger purseId;

	public AccountPursePK() {
	}

	public AccountPursePK(BigInteger accountId, BigInteger productId, BigInteger purseId) {
		this.accountId = accountId;
		this.productId = productId;
		this.purseId = purseId;
	}

	public BigInteger getAccountId() {
		return accountId;
	}

	public void setAccountId(BigInteger accountId) {
		this.accountId = accountId;
	}

	public BigInteger getProductId() {
		return productId;
	}

	public void setProductId(BigInteger productId) {
		this.productId = productId;
	}

	public BigInteger getPurseId() {
		return purseId;
	}

	public void setPurseId(BigInteger purseId) {
		this.purseId = purseId;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (accountId != null ? accountId.hashCode() : 0);
		hash += (productId != null ? productId.hashCode() : 0);
		hash += (purseId != null ? purseId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof AccountPursePK)) {
			return false;
		}
		AccountPursePK other = (AccountPursePK) object;
		if ((this.accountId == null && other.accountId != null) || (this.accountId != null && !this.accountId.equals(other.accountId))) {
			return false;
		}
		if ((this.productId == null && other.productId != null) || (this.productId != null && !this.productId.equals(other.productId))) {
			return false;
		}
		if ((this.purseId == null && other.purseId != null) || (this.purseId != null && !this.purseId.equals(other.purseId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.incomm.fs.cclp_spil.AccountPursePK[ accountId=" + accountId + ", productId=" + productId + ", purseId=" + purseId
				+ " ]";
	}

}
