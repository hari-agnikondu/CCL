package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;




/**
 * The persistent class for the GROUP_ACCESS_PRODUCT database table.
 * 
 */
@Entity
@Audited
@Table(name = "GROUP_ACCESS_PRODUCT")
public class GroupAccessProduct implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@ManyToOne
	@JoinColumn(name="GROUP_ACCESS_ID")
	private GroupAccess groupAccess;	
	@Id
	@ManyToOne
	@JoinColumn(name="PRODUCT_ID")
	private Product product;
	@Id
	@ManyToOne
	@JoinColumn(name="PARTNER_ID")
	private Partner partner;
	@Id
	@Column(name="PARTNER_PARTY_TYPE")
	private String partnerPartyType;
	
	@Column(name="INS_USER",updatable=false)
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

	public GroupAccess getGroupAccess() {
		return groupAccess;
	}

	public void setGroupAccess(GroupAccess groupAccess) {
		this.groupAccess = groupAccess;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}

	public String getPartnerPartyType() {
		return partnerPartyType;
	}

	public void setPartnerPartyType(String partnerPartyType) {
		this.partnerPartyType = partnerPartyType;
	}

	@Override
	public String toString() {
		return "GroupAccessProduct [groupAccess=" + groupAccess + ", product="
				+ product + ", partner=" + partner + ", partnerPartyType="
				+ partnerPartyType + ", insUser=" + insUser + ", insDate="
				+ insDate + ", lastUpdUser=" + lastUpdUser + ", lastUpdDate="
				+ lastUpdDate + "]";
	}

}