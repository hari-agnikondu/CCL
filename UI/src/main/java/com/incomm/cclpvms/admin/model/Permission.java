package com.incomm.cclpvms.admin.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;


/**
 * The persistent class for the PERMISSION database table.
 * 
 */

public class Permission implements Serializable {
	private static final long serialVersionUID = 1L;

	private long permissionId;

	private String description;


	private Date insDate;


	private Date lastUpdDate;

	private String permissionName;
	
	private Set<Role> roles;


	public Permission() {
	}


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


	public String getPermissionName() {
		return permissionName;
	}


	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}


	public Set<Role> getRoles() {
		return roles;
	}


	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}


}