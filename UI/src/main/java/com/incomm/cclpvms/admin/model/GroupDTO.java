package com.incomm.cclpvms.admin.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;




/**
 * The DTO class for the Group.
 * 
 */

public class GroupDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private long groupId;

	private String groupName;

	private Date insDate;

	private Date lastUpdDate;
	
	private String groupStatus;
	
	private String groupCheckerRemarks;
	
	private Set<RoleDTO> roles;
	
	private Long lastUpdateUser;
	


	public Long getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(Long lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public Set<RoleDTO> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleDTO> roles) {
		this.roles = roles;
	}

	private Set<String> selectedRoleList;
	
	public GroupDTO(){
		
	}


	public Set<String> getSelectedRoleList() {
		return selectedRoleList;
	}

	public void setSelectedRoleList(Set<String> selectedRoleList) {
		this.selectedRoleList = selectedRoleList;
	}

	
	private Long insUser;
	
	

	public Long getInsUser() {
		return insUser;
	}

	public void setInsUser(Long insUser) {
		this.insUser = insUser;
	}

	public String getGroupStatus() {
		return groupStatus;
	}

	public void setGroupStatus(String groupStatus) {
		this.groupStatus = groupStatus;
	}

	public String getGroupCheckerRemarks() {
		return groupCheckerRemarks;
	}

	public void setGroupCheckerRemarks(String groupCheckerRemarks) {
		this.groupCheckerRemarks = groupCheckerRemarks;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
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

	@Override
	public String toString() {
		return "GroupDTO [groupId=" + groupId + ", groupName=" + groupName
				+ ", insDate=" + insDate + ", lastUpdDate=" + lastUpdDate
				+ ", groupStatus=" + groupStatus + ", groupCheckerRemarks="
				+ groupCheckerRemarks + ", roles=" + roles
				+ ", lastUpdateUser=" + lastUpdateUser + ", selectedRoleList="
				+ selectedRoleList + ", insUser=" + insUser + "]";
	}



	


}