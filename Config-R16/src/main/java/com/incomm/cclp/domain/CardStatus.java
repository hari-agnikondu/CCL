package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;




/**
 * The persistent class for the CARD_STATUS database table.
 * 
 */
@Entity
@Audited
@Table(name = "CARD_STATUS",uniqueConstraints=@UniqueConstraint(columnNames={"STATUS_CODE","STATUS_DESC"}))
public class CardStatus implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="STATUS_CODE")
	private String statusCode;		
	@Id
	@Column(name="STATUS_DESC")
	private String statusDesc;

	@Column(name="INS_USER")
	private Long insUser;

	@CreationTimestamp
	@Column(name="INS_DATE",updatable=false)
	private Date insDate;

	@Column(name="LAST_UPD_USER")
	private Long lastUpdUser;

	@UpdateTimestamp
	@Column(name="LAST_UPD_DATE")
	private Date lastUpdDate;

	@Column(name="SCREEN_CONFIG")
	private String screenConfig;
	
	public String getScreenConfig() {
		return screenConfig;
	}

	public void setScreenConfig(String screenConfig) {
		this.screenConfig = screenConfig;
	}
	
	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
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

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	@Override
	public String toString() {
		return "CardStatus [statusCode=" + statusCode + ", statusDesc=" + statusDesc + "]";
	}


}