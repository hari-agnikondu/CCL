package com.incomm.cclpvms.config.model;

import java.io.Serializable;

public class CurrencyCode implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String currencyTypeID;

	private String currCodeAlpha;
	
	private String currencyDesc;
	
	private String minorUnits;

	public String getCurrencyTypeID() {
		return currencyTypeID;
	}

	public void setCurrencyTypeID(String currencyTypeID) {
		this.currencyTypeID = currencyTypeID;
	}

	public String getCurrCodeAlpha() {
		return currCodeAlpha;
	}

	public void setCurrCodeAlpha(String currCodeAlpha) {
		this.currCodeAlpha = currCodeAlpha;
	}

	public String getCurrencyDesc() {
		return currencyDesc;
	}

	public void setCurrencyDesc(String currencyDesc) {
		this.currencyDesc = currencyDesc;
	}

	public String getMinorUnits() {
		return minorUnits;
	}

	public void setMinorUnits(String minorUnits) {
		this.minorUnits = minorUnits;
	}

	@Override
	public String toString() {
		return "CurrencyCode [currencyTypeID=" + currencyTypeID + ", currCodeAlpha=" + currCodeAlpha + ", currencyDesc="
				+ currencyDesc + ", minorUnits=" + minorUnits + "]";
	}
	
	

}
