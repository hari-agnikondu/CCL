package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "MERCHANT_PRODUCT")
public class MerchantProduct implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	MerchantProductId primaryKey = new MerchantProductId();
	
	@Column(name="INS_USER")
	private Long insUser;
	
	@Temporal(TemporalType.DATE)
	@Column(name="INS_DATE")
	private Date insDate;
	
	@Column(name="LAST_UPD_USER")
	private Long lastUpdUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPD_DATE")
	private Date lastUpdDate;

	public MerchantProductId getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(MerchantProductId primaryKey) {
		this.primaryKey = primaryKey;
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
	
	@Transient
	public Merchant getMerchant() {
		return getPrimaryKey().getMerchant();
	}

	public void setMerchant(Merchant merchant) {
		this.getPrimaryKey().setMerchant(merchant);
	}

	@Transient
	public Product getProduct() {
		return getPrimaryKey().getProduct();
	}

	public void setProduct(Product product) {
		this.getPrimaryKey().setProduct(product);
	}

}
