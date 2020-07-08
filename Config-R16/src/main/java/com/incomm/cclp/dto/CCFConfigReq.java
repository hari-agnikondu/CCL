package com.incomm.cclp.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CCFConfigReq implements Serializable {

	private static final long serialVersionUID = 1L;

	private String versionID;
	private String addEdit;
	private String versionName;
	private long insUser;
	private Date insDate;
	private long lastUpdUser;
	private Date lastUpdDate;
	private List<CCFConfDetailDTO> rowData;

	public String getVersionID() {
		return versionID;
	}

	public void setVersionID(String versionID) {
		this.versionID = versionID;
	}

	public String getAddEdit() {
		return addEdit;
	}

	public void setAddEdit(String addEdit) {
		this.addEdit = addEdit;
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
		this.insDate = insDate;
	}

	public long getLastUpdUser() {
		return lastUpdUser;
	}

	public void setLastUpdUser(long lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	public Date getLastUpdDate() {
		return lastUpdDate;
	}

	public void setLastUpdDate(Date lastUpdDate) {
		this.lastUpdDate = lastUpdDate;
	}

	public List<CCFConfDetailDTO> getRowData() {
		return rowData;
	}

	public void setRowData(List<CCFConfDetailDTO> rowData) {
		this.rowData = rowData;
	}

	@Override
	public String toString() {
		return "CCFConfigReq [versionID=" + versionID + ", addEdit=" + addEdit + ", versionName=" + versionName
				+ ", insUser=" + insUser + ", insDate=" + insDate + ", lastUpdUser=" + lastUpdUser + ", lastUpdDate="
				+ lastUpdDate + ", rowData=" + rowData + "]";
	}

}
