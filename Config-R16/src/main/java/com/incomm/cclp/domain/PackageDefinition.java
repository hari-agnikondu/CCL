package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "package_definition",uniqueConstraints = { @UniqueConstraint(columnNames = "PACKAGE_ID")})
public class PackageDefinition implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="PACKAGE_ID")
	private String packageId;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="REPLACMENT_PACKAGE_ID")
	private String replacementPackageId;
	
	@Column(name="FULFILLMENT_VENDOR_ID")
	private String fulfillmentVendorId;
	
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
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "primaryKey.packageId", orphanRemoval = true)
	private List<PackageAttributes> packageKeyAttributes;

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReplacementPackageId() {
		return replacementPackageId;
	}

	public void setReplacementPackageId(String replacementPackageId) {
		this.replacementPackageId = replacementPackageId;
	}

	public String getFulfillmentVendorId() {
		return fulfillmentVendorId;
	}

	public void setFulfillmentVendorId(String fulfillmentVendorId) {
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
	
	public List<PackageAttributes> getPackageKeyAttributes() {
		return packageKeyAttributes;
	}

	public void setPackageKeyAttributes(List<PackageAttributes> packageKeyAttributes) {
		this.packageKeyAttributes = packageKeyAttributes;
	}
	
	@Override
	public String toString() {
		return "PackageDefinition [packageId=" + packageId + ", description=" + description + ", replacementPackageId="
				+ replacementPackageId + ", fulfillmentVendorId=" + fulfillmentVendorId + ", isActive=" + isActive
				+ ", insUser=" + insUser + ", insDate=" + insDate + ", lastUpdUser=" + lastUpdUser + ", lastUpdDate="
				+ lastUpdDate + ", packageKeyAttributes=" + packageKeyAttributes + "]";
	}
	

}
