package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AUTHENTICATION_TYPE")
public class AuthenticationType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "AUTH_TYPE_ID", updatable = false)
	private Long authTypeId;

	@Column(name = "AUTH_TYPE_DESC", updatable = false)
	private String authTypeDesc;

	@Column(name = "CHW_SUPPORT", updatable = false)
	private String chwSupport;

	@Column(name = "IVR_SUPPORT", updatable = false)
	private String ivrSupport;

	@Column(name = "AUTH_PARAMS", updatable = false)
	private String authParams;

	@Column(name = "INS_DATE", updatable = false)
	private Date insDate;

	@Column(name = "LAST_UPD_DATE", updatable = false)
	private Date lastUpdDate;

	public Long getAuthTypeId() {
		return authTypeId;
	}

	public void setAuthTypeId(Long authTypeId) {
		this.authTypeId = authTypeId;
	}

	public String getAuthTypeDesc() {
		return authTypeDesc;
	}

	public void setAuthTypeDesc(String authTypeDesc) {
		this.authTypeDesc = authTypeDesc;
	}

	public String getChwSupport() {
		return chwSupport;
	}

	public void setChwSupport(String chwSupport) {
		this.chwSupport = chwSupport;
	}

	public String getIvrSupport() {
		return ivrSupport;
	}

	public void setIvrSupport(String ivrSupport) {
		this.ivrSupport = ivrSupport;
	}

	public String getAuthParams() {
		return authParams;
	}

	public void setAuthParams(String authParams) {
		this.authParams = authParams;
	}

	public Date getInsDate() {
		return insDate;
	}

	public void setInsDate(Date insDate) {
		this.insDate = insDate;
	}

	public Date getLastUpdDate() {
		return lastUpdDate;
	}

	public void setLastUpdDate(Date lastUpdDate) {
		this.lastUpdDate = lastUpdDate;
	}

}
