package com.incomm.cclpvms.admin.model;

import java.io.Serializable;



/**
 * The DTO class for the Permission.
 * 
 */

public class PermissionDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private long permissionId;

	private String description;

	private String permissionName;

	private Long insUser;
	
	public Long getInsUser() {
		return insUser;
	}

	public void setInsUser(Long insUser) {
		this.insUser = insUser;
	}

	public Long getLastUpdUser() {
		return lastUpdUser;
	}

	public void setLastUpdUser(Long lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	private Long lastUpdUser;
	
	public long getPermissionId() {
		return permissionId;
	}


	public void setPermissionId(long permissionId) {
		this.permissionId = permissionId;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}

	public String getPermissionName() {
		return permissionName;
	}


	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

}