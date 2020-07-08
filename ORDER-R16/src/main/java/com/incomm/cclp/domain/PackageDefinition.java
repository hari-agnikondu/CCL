package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "clp_configuration.package_definition")
public class PackageDefinition implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="PACKAGE_DEFINITION_PACKAGE_SEQ_GEN", sequenceName="PACKAGE_DEFINITION_PACKAGE_ID", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PACKAGE_DEFINITION_PACKAGE_SEQ_GEN")
	@Column(name="PACKAGE_ID")
	private Long packageId;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="REPLACMENT_PACKAGE_ID")
	private Long replacmentPackageId;
	
	@Column(name="FULFILLMENT_VENDOR_ID")
	private Long fulfillmentVendorId;
	
	@Column(name="IS_ACTIVE")
	private String isActive;
	
	@Column(name="INS_USER",updatable = false )
	private long insUser;
	
	@Temporal(TemporalType.DATE)
	@Column(name="INS_DATE",updatable = false)
	private Date insDate;
	
	@Column(name="LAST_UPD_USER")
	private long lastUpdUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPD_DATE")
	private Date lastUpdDate;

	public Long getPackageId() {
		return packageId;
	}

	public void setPackageId(Long packageId) {
		this.packageId = packageId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getReplacmentPackageId() {
		return replacmentPackageId;
	}

	public void setReplacmentPackageId(Long replacmentPackageId) {
		this.replacmentPackageId = replacmentPackageId;
	}

	public Long getFulfillmentVendorId() {
		return fulfillmentVendorId;
	}

	public void setFulfillmentVendorId(Long fulfillmentVendorId) {
		this.fulfillmentVendorId = fulfillmentVendorId;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public long getInsUser() {
		return insUser;
	}

	public void setInsUser(long insUser) {
		this.insUser = insUser;
	}

	public Date getInsDate() {
		return insDate;
	}

	public void setInsDate(Date insDate) {
		this.insDate = new java.sql.Date(new java.util.Date().getTime());
	}

	public long getLastUpdUser() {
		return lastUpdUser;
	}

	public void setLastUpdUser(long lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	public Date getLastUpdDate() {
		return lastUpdDate;
	}

	public void setLastUpdDate(Date lastUpdDate) {
		this.lastUpdDate = lastUpdDate;
	}

	public PackageDefinition(Long packageId, long insUser, Date insDate, long lastUpdUser, Date lastUpdDate) {
		
		this.packageId = packageId;
		this.insUser = insUser;
		this.insDate = insDate;
		this.lastUpdUser = lastUpdUser;
		this.lastUpdDate = lastUpdDate;
	}

	public PackageDefinition() {
		
	}

	
	

}
