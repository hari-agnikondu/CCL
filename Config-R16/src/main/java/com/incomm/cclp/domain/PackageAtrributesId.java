package com.incomm.cclp.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PackageAtrributesId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	@Column(name="PACKAGE_ID")
	private String packageId;
	
	@Column(name="ATTRIBUTE_NAME")
	private String attributeName;

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

	public PackageAtrributesId()
	{
		
	}
	
	public PackageAtrributesId(String packageId, String attributeName) {
		this.packageId = packageId;
		this.attributeName = attributeName;
	}
	
	
}
