package com.incomm.cclp.dto;

import java.io.Serializable;
import java.util.Set;


/**
 * The DTO class for the Role.
 * 
 */

public class RoleDTO implements Serializable {

	
	private static final long serialVersionUID = 1L;

	private long roleId;

	private String roleDesc;

	private long insUser;

	private Long lastUpdUser;

	public Long getLastUpdUser() {
		return lastUpdUser;
	}

	public void setLastUpdUser(Long lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	private String roleName;

	private Set<PermissionDTO> permissions;
   
	private String status;
	
	private String checkerRemarks;

	public String getCheckerRemarks() {
		return checkerRemarks;
	}

	public void setCheckerRemarks(String checkerRemarks) {
		this.checkerRemarks = checkerRemarks;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}


	public long getInsUser() {
		return insUser;
	}

	public void setInsUser(long insUser) {
		this.insUser = insUser;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Set<PermissionDTO> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<PermissionDTO> permissionDtos) {
		this.permissions = permissionDtos;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	@Override
	public String toString() {
		return "RoleDTO [roleId=" + roleId + ", roleDesc=" + roleDesc + ", insUser=" + insUser + ", lastUpdUser="
				+ lastUpdUser + ", roleName=" + roleName + ", permissions=" + permissions + ", status=" + status
				+ ", checkerRemarks=" + checkerRemarks + "]";
	}

}