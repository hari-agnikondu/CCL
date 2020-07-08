package com.incomm.cclpvms.admin.model;

import java.util.Date;
import java.util.Set;

import com.incomm.cclpvms.config.model.CardRange.ValidateSearch;
import com.incomm.cclpvms.config.validator.CheckForSearch;

public class Group {

	private Long groupId;
	@CheckForSearch(pattern="^[A-Za-z _]+$",max = 100, messageLength="{messageLength.groupSearch.groupName}",messagepattern="{messagepattern.groupSearch.groupName}",groups=ValidateSearch.class)
	private String groupName;

	private Date insDate;

	private Date lastUpdDate;
	
	private Long roleId;
	
	private Set<Role> roles;
	
	private String groupStatus;
	
	private String groupCheckerRemarks;
	
	private Long insUser;
	
	private Long lpdUptUser;
	
	
  private Set<String> selectedRoleList;
	
	private Set<String> roleList;
	

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
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

	public Long getInsUser() {
		return insUser;
	}

	public void setInsUser(Long insUser) {
		this.insUser = insUser;
	}

	public Long getLpdUptUser() {
		return lpdUptUser;
	}

	public void setLpdUptUser(Long lpdUptUser) {
		this.lpdUptUser = lpdUptUser;
	}

	

	
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Set<String> getSelectedRoleList() {
		return selectedRoleList;
	}

	public void setSelectedRoleList(Set<String> selectedRoleList) {
		this.selectedRoleList = selectedRoleList;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<String> getRoleList() {
		return roleList;
	}

	public void setRoleList(Set<String> roleList) {
		this.roleList = roleList;
	}

	@Override
	public String toString() {
		return "Group [groupId=" + groupId + ", groupName=" + groupName
				+ ", insDate=" + insDate + ", lastUpdDate=" + lastUpdDate
				+ ", roleId=" + roleId + ", roles=" + roles + ", groupStatus="
				+ groupStatus + ", groupCheckerRemarks=" + groupCheckerRemarks
				+ ", insUser=" + insUser + ", lpdUptUser=" + lpdUptUser
				+ ", selectedRoleList=" + selectedRoleList + ", roleList="
				+ roleList + "]";
	}

	
	

}
