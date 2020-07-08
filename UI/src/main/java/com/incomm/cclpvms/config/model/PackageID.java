package com.incomm.cclpvms.config.model;

import java.io.Serializable;
import java.util.Map;

import com.incomm.cclpvms.config.validator.FieldValidation;

public class PackageID implements Serializable {

	private static final long serialVersionUID = 1L;

	//@FieldValidation(notEmpty = true, pattern = "^[0-9]+$", min = 2, max = 10, messageNotEmpty = "{messageNotEmpty.package.packageID}", messageLength = "{messageLength.package.packageID}", messagePattern = "{messageLength.package.packageID}", groups = ValidationStepOne.class)
	private String packageId;

	@FieldValidation(notEmpty = true, max = 100, messageNotEmpty = "{messageNotEmpty.package.description}", messageLength = "{messageLength.package.description}", messagePattern = "{messagepattern.package.description}", groups = ValidationStepTwo.class)
	private String description;

	private String replacementPackageId;
	private String fulfillmentId;
	private String isActive;
	
	private String parentPackageId;
	
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

	public Map<String, String> getPackageAttributes() {
		return packageAttributes;
	}

	public void setPackageAttributes(Map<String, String> packageAttributes) {
		this.packageAttributes = packageAttributes;
	}
	
	

	public String getParentPackageId() {
		return parentPackageId;
	}

	public void setParentPackageId(String parentPackageId) {
		this.parentPackageId = parentPackageId;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public interface ValidationStepOne {


	}

	public interface ValidationStepTwo {

	}
}
