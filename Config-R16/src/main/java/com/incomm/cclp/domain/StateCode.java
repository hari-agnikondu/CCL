package com.incomm.cclp.domain;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;

	@Entity
	@Audited
	@Table (name="STATE_CODE")
	public class StateCode implements Serializable{
		
		private static final long serialVersionUID = 1L;

		
		@Column(name = "COUNTRY_CODE")
		private Long countryCode;
		
		@Id
		@Column(name = "STATE_CODE")
		private Long stateCodeID;
		

		@Column(name = "STATE_NAME")
		private String stateName;
		
		@Column(name = "SWITCH_STATE_CODE")
		private String switchStateCode;
		
		@Column(name = "ALPHA_COUNTRY_CODE")
		private String alphaCountryCode;
		
		@Column(name = "INS_USER" ,updatable = false)
		private Long insUser;

		@CreationTimestamp
		@Column(name = "INS_DATE" ,updatable = false )
		private Date insDate;

		@Column(name = "LAST_UPD_USER")
		private Long lastUpdUser;

		@UpdateTimestamp
		@Column(name = "LAST_UPD_DATE")
		private Date lastUpdDate;
		

		public Long getCountryCode() {
			return countryCode;
		}

		public void setCountryCode(Long countryCode) {
			this.countryCode = countryCode;
		}

		public Long getStateCodeID() {
			return stateCodeID;
		}

		public void setStateCodeID(Long stateCodeID) {
			this.stateCodeID = stateCodeID;
		}

		

		public String getStateName() {
			return stateName;
		}

		public void setStateName(String stateName) {
			this.stateName = stateName;
		}

		public String getSwitchStateCode() {
			return switchStateCode;
		}

		public void setSwitchStateCode(String switchStateCode) {
			this.switchStateCode = switchStateCode;
		}

		public String getAlphaCountryCode() {
			return alphaCountryCode;
		}

		public void setAlphaCountryCode(String alphaCountryCode) {
			this.alphaCountryCode = alphaCountryCode;
		}

		public Long getInsUser() {
			return insUser;
		}

		public void setInsUser(Long insUser) {
			this.insUser = insUser;
		}

		public Date getInsDate() {
			return insDate;
		}

		public void setInsDate(Date insDate) {
			this.insDate = new java.sql.Date(new java.util.Date().getTime());
		}

		public Long getLastUpdUser() {
			return lastUpdUser;
		}

		public void setLastUpdUser(Long lastUpdUser) {
			this.lastUpdUser = lastUpdUser;
		}

		public Date getLastUpdDate() {
			return lastUpdDate;
		}

		public void setLastUpdDate(Date lastUpdDate) {
			this.lastUpdDate = lastUpdDate;
		}

		@Override
		public String toString() {
			return "StateCode [countryCode=" + countryCode + ", stateCodeID=" + stateCodeID + ", stateName=" + stateName
					+ ", switchStateCode=" + switchStateCode + ", alphaCountryCode=" + alphaCountryCode + ", insUser="
					+ insUser + ", insDate=" + insDate + ", lastUpdUser=" + lastUpdUser + ", lastUpdDate=" + lastUpdDate
					+ "]";
		}


	
	}



