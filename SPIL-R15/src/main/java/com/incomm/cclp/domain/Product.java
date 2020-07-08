/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author skocherla
 */
@Entity
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce
	// field validation
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "PRODUCT_ID")
	private BigDecimal productId;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "PRODUCT_NAME")
	private String productName;
	@Size(max = 255)
	@Column(name = "DESCRIPTION")
	private String description;
	@Lob
	@Column(name = "ATTRIBUTES")
	private String attributes;
	@Size(max = 1)
	@Column(name = "IS_ACTIVE")
	private String isActive;
	@Column(name = "PARENT_PRODUCT_ID")
	private BigInteger parentProductId;
	@Basic(optional = false)
	@NotNull
	@Column(name = "ISSUER_ID")
	private BigInteger issuerId;
	@Basic(optional = false)
	@NotNull
	@Column(name = "PARTNER_ID")
	private BigInteger partnerId;
	@Basic(optional = false)
	@NotNull
	@Column(name = "INS_USER")
	private BigInteger insUser;
	@Basic(optional = false)
	@NotNull
	@Column(name = "INS_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date insDate;
	@Basic(optional = false)
	@NotNull
	@Column(name = "LAST_UPD_USER")
	private BigInteger lastUpdUser;
	@Basic(optional = false)
	@NotNull
	@Column(name = "LAST_UPD_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdDate;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 50)
	@Column(name = "PRODUCT_SHORT_NAME")
	private String productShortName;
	@Column(name = "PROGRAM_ID")
	private BigInteger programId;

	public Product() {
	}

	public Product(BigDecimal productId) {
		this.productId = productId;
	}

	public Product(BigDecimal productId, String productName, BigInteger issuerId, BigInteger partnerId, BigInteger insUser, Date insDate,
			BigInteger lastUpdUser, Date lastUpdDate, String productShortName) {
		this.productId = productId;
		this.productName = productName;
		this.issuerId = issuerId;
		this.partnerId = partnerId;
		this.insUser = insUser;
		this.insDate = insDate;
		this.lastUpdUser = lastUpdUser;
		this.lastUpdDate = lastUpdDate;
		this.productShortName = productShortName;
	}

	public BigDecimal getProductId() {
		return productId;
	}

	public void setProductId(BigDecimal productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public BigInteger getParentProductId() {
		return parentProductId;
	}

	public void setParentProductId(BigInteger parentProductId) {
		this.parentProductId = parentProductId;
	}

	public BigInteger getIssuerId() {
		return issuerId;
	}

	public void setIssuerId(BigInteger issuerId) {
		this.issuerId = issuerId;
	}

	public BigInteger getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(BigInteger partnerId) {
		this.partnerId = partnerId;
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

	public String getProductShortName() {
		return productShortName;
	}

	public void setProductShortName(String productShortName) {
		this.productShortName = productShortName;
	}

	public BigInteger getProgramId() {
		return programId;
	}

	public void setProgramId(BigInteger programId) {
		this.programId = programId;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (productId != null ? productId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Product)) {
			return false;
		}
		Product other = (Product) object;
		if ((this.productId == null && other.productId != null) || (this.productId != null && !this.productId.equals(other.productId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.incomm.fs.cclp_spil.Product[ productId=" + productId + " ]";
	}
}
