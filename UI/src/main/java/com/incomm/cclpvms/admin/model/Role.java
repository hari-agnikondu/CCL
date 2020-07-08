package com.incomm.cclpvms.admin.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.incomm.cclpvms.config.validator.FieldValidation;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Role implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long roleId;
	
	public Role(){
		
	}
	public Role(String roleName){
		this.roleName=roleName;
	}
	
	
	public Role(long roleId, String roleName, String roleDesc, long[] permissionID, String status,Set<PermissionDTO> permissions ,long insUser) {
		
		this.roleId = roleId;
		this.roleName = roleName;
		this.roleDesc = roleDesc;
		this.permissionID = permissionID;
		this.status = status;
		this.permissions = permissions;
		this.insUser=insUser;
	}
public Role(long roleId, String roleName, String roleDesc, long[] permissionID, String status,Set<PermissionDTO> permissions) {
		
		this.roleId = roleId;
		this.roleName = roleName;
		this.roleDesc = roleDesc;
		this.permissionID = permissionID;
		this.status = status;
		this.permissions = permissions;
		
	}
	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	
	@FieldValidation(notEmpty = true,pattern="^[A-Za-z0-9 ,&.;'_-]+$", min = 2, max = 100,  messageNotEmpty="{messageNotEmpty.role.roleName}",
			messageLength="{messageLength.role.roleName}",messagePattern="{messagepattern.role.roleName}")
	private String roleName;
	
	private String roleDesc;
	
	
	private long[] permissionID;
	
	private Set<PermissionDTO> permissions;

	private String status;
	private String action;
	
	private String checkerRemarks;
	private long insUser;
	private long lastUpdUser;
	
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public Set<PermissionDTO> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<PermissionDTO> permissions) {
		this.permissions = permissions;
	}

	
	public long[] getPermissionID() {
		return permissionID;
	}

	public void setPermissionID(long[] permissionID) {
		this.permissionID = permissionID;
	}

	public long getInsUser() {
		return insUser;
	}

	public void setInsUser(long insUser) {
		this.insUser = insUser;
	}

	public long getLastUpdUser() {
		return lastUpdUser;
	}

	public void setLastUpdUser(long lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}
	
	public String getCheckerRemarks() {
		return checkerRemarks;
	}
	public void setCheckerRemarks(String checkerRemarks) {
		this.checkerRemarks = checkerRemarks;
	}
	@Override
	public String toString() {
		return "Role [roleId=" + roleId + ", roleName=" + roleName + ", roleDesc=" + roleDesc + ", permissionID="
				+ Arrays.toString(permissionID) + ", permissions=" + permissions + ", status=" + status + ", action="
				+ action + ", checkerRemarks=" + checkerRemarks + ", insUser=" + insUser + ", lastUpdUser="
				+ lastUpdUser + "]";
	}
	
	

	

}
