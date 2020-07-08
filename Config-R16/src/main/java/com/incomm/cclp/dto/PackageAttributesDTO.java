package com.incomm.cclp.dto;

import java.util.Date;

public class PackageAttributesDTO {

	private String packageId;
	private String attributeName;
	private String attributeValue;
	private long insUser;
	private Date insDate;
	private long lupdUser;
	private Date lupdDate;

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
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

	public long getLupdUser() {
		return lupdUser;
	}

	public void setLupdUser(long lupdUser) {
		this.lupdUser = lupdUser;
	}

	public Date getLupdDate() {
		return lupdDate;
	}

	public void setLupdDate(Date lupdDate) {
		this.lupdDate = lupdDate;
	}

}