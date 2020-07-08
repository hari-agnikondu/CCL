package com.incomm.cclpvms.config.model;


public class Package {

	
	private String packageId;
	private String description;
	private String replacementPackageId;
	private String fulfillmentVendorId;
	private String isActive;
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
	public Package(String packageId, String description, String replacementPackageId, String fulfillmentVendorId,
			String isActive) {
		super();
		this.packageId = packageId;
		this.description = description;
		this.replacementPackageId = replacementPackageId;
		this.fulfillmentVendorId = fulfillmentVendorId;
		this.isActive = isActive;
	}
	public Package() {
	
	}
	
	
	
	
	
	
}

