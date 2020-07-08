package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;




/**
 * The persistent class for the ProductRuleSet database table.
 * 
 */
@Entity
@Audited
@Table(name = "PRODUCT_RULESET")
public class ProductRuleSet implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
    private ProductRuleSetID id;

	@Column(name="INS_USER")
	private Long insUser;

	@CreationTimestamp
	@Column(name="INS_DATE",updatable=false)
	private Date insDate;

	@Column(name="LAST_UPD_USER")
	private Long lastUpdUser;

	@UpdateTimestamp
	@Column(name="LAST_UPD_DATE")
	private Date lastUpdDate;


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

	public ProductRuleSetID getId() {
		return id;
	}

	public void setId(ProductRuleSetID id) {
		this.id = id;
	}



}