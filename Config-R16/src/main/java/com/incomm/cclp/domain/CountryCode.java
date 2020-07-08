package com.incomm.cclp.domain;


import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

	@Entity
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@Table (name="COUNTRY_CODE")
	public class CountryCode implements Serializable{

		private static final long serialVersionUID = 1L;

		@Id
		@Column(name = "COUNTRY_CODE")
		private Long countryCodeID;
		
		

		@OneToOne
		@JoinColumn(name="CURRENCY_ID")
		private CurrencyCode currencyCode;
		
		@Column(name = "COUNTRY_NAME")
		private String countryName;
		
		@Column(name = "SWITCH_COUNTRY_CODE")
		private String switchCountryCode;
		
		@Column(name = "ISO_COUNTRY_CODE")
		private String isoCountryCode;
		
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
		
		@OneToMany
		@JoinColumn(name="COUNTRY_CODE")
		private Set<StateCode> statecodes;
		
		public Set<StateCode> getStatecodes() {
			return statecodes;
		}

		public void setStatecodes(Set<StateCode> statecodes) {
			this.statecodes = statecodes;
		}
		public Long getCountryCodeID() {
			return countryCodeID;
		}

		public void setCountryCodeID(Long countryCodeID) {
			this.countryCodeID = countryCodeID;
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

		

		public CurrencyCode getCurrencyCode() {
			return currencyCode;
		}

		public void setCurrencyCode(CurrencyCode currencyCode) {
			this.currencyCode = currencyCode;
		}

		public String getCountryName() {
			return countryName;
		}

		public void setCountryName(String countryName) {
			this.countryName = countryName;
		}

		public String getSwitchCountryCode() {
			return switchCountryCode;
		}

		public void setSwitchCountryCode(String switchCountryCode) {
			this.switchCountryCode = switchCountryCode;
		}

		public String getIsoCountryCode() {
			return isoCountryCode;
		}

		public void setIsoCountryCode(String isoCountryCode) {
			this.isoCountryCode = isoCountryCode;
		}

		public String getAlphaCountryCode() {
			return alphaCountryCode;
		}

		public void setAlphaCountryCode(String alphaCountryCode) {
			this.alphaCountryCode = alphaCountryCode;
		}
		
		@Override
		public String toString() {
			return "CountryCode [countryCodeID=" + countryCodeID + ", currencyCode=" + currencyCode + ", countryName="
					+ countryName + ", switchCountryCode=" + switchCountryCode + ", isoCountryCode=" + isoCountryCode
					+ ", alphaCountryCode=" + alphaCountryCode + ", insUser=" + insUser + ", insDate=" + insDate
					+ ", lastUpdUser=" + lastUpdUser + ", lastUpdDate=" + lastUpdDate + ", statecodes=" + statecodes
					+ "]";
		}
	}



