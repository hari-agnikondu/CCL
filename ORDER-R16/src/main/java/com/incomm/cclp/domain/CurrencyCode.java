package com.incomm.cclp.domain;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

	@Entity
	@Table (name="clp_transactional.CURRENCY_CODE")
	public class CurrencyCode implements Serializable{
		
		private static final long serialVersionUID = 1L;

		@Id
		@Column(name = "CURRENCY_ID")
		private String currencyTypeID;
		
		@Column(name = "CURRENCY_CODE")
		private String currCodeAlpha;
		
		@Column(name = "CURRENCY_DESCRIPTION")
		private String currencyDesc;
		
		@Column(name = "MINOR_UNITS")
		private String minorUnits;


		public String getMinorUnits() {
			return minorUnits;
		}

		public void setMinorUnits(String minorUnits) {
			this.minorUnits = minorUnits;
		}

		public String getCurrencyDesc() {
			return currencyDesc;
		}

		public void setCurrencyDesc(String currencyDesc) {
			this.currencyDesc = currencyDesc;
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
			return "CurrencyCode [currencyTypeID=" + currencyTypeID
					+ ", currCodeAlpha=" + currCodeAlpha + ", currencyDesc="
					+ currencyDesc + ", minorUnits=" + minorUnits + "]";
		}



	}



