package com.incomm.cclp.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "CCF_CONF_VERSION", uniqueConstraints = { @UniqueConstraint(columnNames = "VERSION_NAME") })
public class CCFConfVersion implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "VERSION_NAME", unique = true, nullable = false)
	private String versionName;

	@Column(name = "INS_USER")
	private long insUser;

	@Temporal(TemporalType.DATE)
	@Column(name = "INS_DATE")
	private Date insDate;

	@Column(name = "LAST_UPD_USER")
	private long lupdUser;

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_UPD_DATE")
	private Date lupdDate;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "ccfConfVersion", orphanRemoval = true)
	private List<CCFConfDetail> ccfConfigRecords;

	public CCFConfVersion() {

	}

	public List<CCFConfDetail> getCcfConfigRecords() {
		return ccfConfigRecords;
	}

	public void setCcfConfigRecords(List<CCFConfDetail> ccfConfigRecords) {
		this.ccfConfigRecords = ccfConfigRecords;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public long getInsUser() {
		return insUser;
	}

	public void setInsUser(long insUser) {
		this.insUser = insUser;
	}

	public Date getInsDate() {
		return insDate;
	}

	public void setInsDate(Date insDate) {
		this.insDate = new java.sql.Date(new java.util.Date().getTime());
	}

	public long getLupdUser() {
		return lupdUser;
	}

	public void setLupdUser(long lupdUser) {
		this.lupdUser = lupdUser;
	}

	public Date getLupdDate() {
		return lupdDate;
	}

	public void setLupdDate(Date lupdDate) {
		this.lupdDate = lupdDate;
	}

	public CCFConfVersion(String versionName, long insUser, Date insDate, long lupdUser, Date lupdDate) {
		super();
		this.versionName = versionName;
		this.insUser = insUser;
		this.insDate = insDate;
		this.lupdUser = lupdUser;
		this.lupdDate = lupdDate;
	}

	public CCFConfVersion(String versionName, long insUser, Date insDate, long lupdUser, Date lupdDate,
			List<CCFConfDetail> ccfConfigRecords) {
		super();
		this.versionName = versionName;
		this.insUser = insUser;
		this.insDate = insDate;
		this.lupdUser = lupdUser;
		this.lupdDate = lupdDate;
		this.ccfConfigRecords = ccfConfigRecords;
	}

	@Override
	public String toString() {
		return "CCFVersion [versionName=" + versionName + ", insUser=" + insUser + ", insDate=" + insDate
				+ ", lupdUser=" + lupdUser + ", lupdDate=" + lupdDate + "]";
	}

}
