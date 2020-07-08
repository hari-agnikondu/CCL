package com.incomm.cclp.dto;


import java.io.Serializable;
import java.util.Date;

	public class StateCodeDTO implements Serializable{
		
		private static final long serialVersionUID = 1L;

		
		private Long countryCode;
		private Long stateCodeID;
		private String stateName;
		
		private String switchStateCode;
		
		private String alphaCountryCode;
		
		private Long insUser;

		private Date insDate;

		private Long lastUpdUser;

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
			this.insDate = insDate;
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



