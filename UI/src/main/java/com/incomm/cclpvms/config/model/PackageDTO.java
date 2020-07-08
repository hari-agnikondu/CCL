package com.incomm.cclpvms.config.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class PackageDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String packageId;
	private String description;
	private String replacementPackageId;
	private String fulfillmentId;
	private String isActive;
	private long insUser;
	private Date insDate;
	private long lastUpdUser;
	private Date lastUpdDate;
	private Map<String, String> packageAttributes;

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

	public String getFulfillmentId() {
		return fulfillmentId;
	}

	public void setFulfillmentId(String fulfillmentId) {
		this.fulfillmentId = fulfillmentId;
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
		this.insDate = insDate;
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

	public Map<String, String> getPackageAttributes() {
		return packageAttributes;
	}

	public void setPackageAttributes(Map<String, String> packageAttributes) {
		this.packageAttributes = packageAttributes;
	}

}
