package com.incomm.cclp.transaction.bean;

import java.io.Serializable;

public class SupportedPurse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String productId;
	private String purseId;
	private String isDefault;
	private String currencyId;

	private String currencyCode;
	private String upc;
	private String minorUnits;

	public String getMinorUnits() {
		return minorUnits;
	}

	public void setMinorUnits(String minorUnits) {
		this.minorUnits = minorUnits;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getPurseId() {
		return purseId;
	}

	public void setPurseId(String purseId) {
		this.purseId = purseId;
	}

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getUpc() {
		return upc;
	}

	public void setUpc(String upc) {
		this.upc = upc;
	}

	public String getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(String currencyId) {
		this.currencyId = currencyId;
	}

	@Override
	public String toString() {
		return "SupportedPurse [productId=" + productId + ", purseId=" + purseId + ", isDefault=" + isDefault + ", currencyId=" + currencyId
				+ ", currencyCode=" + currencyCode + ", upc=" + upc + ",minorUnits=" + minorUnits + " ]";
	}

}