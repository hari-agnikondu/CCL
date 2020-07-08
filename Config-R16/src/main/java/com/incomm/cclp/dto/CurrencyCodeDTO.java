package com.incomm.cclp.dto;


public class CurrencyCodeDTO {
	
	
	private String currencyTypeID;
	
	private String currCodeAlpha;
	
	private String currencyDesc;
	
	private String minorUnits;
		

	public CurrencyCodeDTO() {
		super();
	}

	public CurrencyCodeDTO(String currencyTypeID, String currCodeAlpha, String currencyDesc, String minorUnits) {
		this.currencyTypeID = currencyTypeID;
		this.currCodeAlpha = currCodeAlpha;
		this.currencyDesc = currencyDesc;
		this.minorUnits = minorUnits;
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

	@Override
	public String toString() {
		return "CurrencyCodeDTO [currencyTypeID=" + currencyTypeID
				+ ", currCodeAlpha=" + currCodeAlpha + ", currencyDesc="
				+ currencyDesc + ", minorUnits=" + minorUnits + "]";
	}

}
