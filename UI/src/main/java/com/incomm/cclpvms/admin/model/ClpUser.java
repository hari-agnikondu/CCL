package com.incomm.cclpvms.admin.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;




public class ClpUser implements Serializable {

	private static final long serialVersionUID = 1L;


	private Long userId;

	private String userLoginId;

	private String lastLoginTime;

	private Long insUser;

	private Date insDate;

	private Long lastUpdUser;

	private Date lastUpdDate;

	private String userStatus;

	private String checkerRemarks;

	private String userName;

	private String userEmail;

	private Long userContactNumber;
	
	private List<GroupDTO> groups;
	
	private List<String> groupNames;

	private String accessStatus;
	
	public String getAccessStatus() {
		return accessStatus;
	}

	public void setAccessStatus(String accessStatus) {
		this.accessStatus = accessStatus;
	}

	public List<String> getGroupNames() {
		return groupNames;
	}

	public void setGroupNames(List<String> groupNames) {
		this.groupNames = groupNames;
	}

	public 	List<GroupDTO> getGroups() {
		return groups;
	}

	public void setGroups(List<GroupDTO> groups) {
		this.groups = groups;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserLoginId() {
		return userLoginId;
	}

	public void setUserLoginId(String userLoginId) {
		this.userLoginId = userLoginId;
	}


	public Long getInsUser() {
		return insUser;
	}


	public void setInsUser(Long insUser) {
		this.insUser = insUser;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Date getInsDate() {
		return insDate;
	}

	public void setInsDate(Date insDate) {
		this.insDate = insDate;
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

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public String getCheckerRemarks() {
		return checkerRemarks;
	}

	public void setCheckerRemarks(String checkerRemarks) {
		this.checkerRemarks = checkerRemarks;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public Long getUserContactNumber() {
		return userContactNumber;
	}

	public void setUserContactNumber(Long userContactNumber) {
		this.userContactNumber = userContactNumber;
	}



	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public ClpUser() {
	}

	public ClpUser(Long userId, String userLoginId, String userStatus, String userName, String userEmail,
			Long userContactNumber,List<GroupDTO> groups) {

		this.userId = userId;
		this.userLoginId = userLoginId;
		this.userStatus = userStatus;
		this.userName = userName;
		this.userEmail = userEmail;
		this.userContactNumber = userContactNumber;
		this.groups=groups;
	}
	
	public ClpUser(Long userId, String userLoginId, String userStatus, String userName, String userEmail,
			Long userContactNumber,List<GroupDTO> groups,String accessStatus) {

		this.userId = userId;
		this.userLoginId = userLoginId;
		this.userStatus = userStatus;
		this.userName = userName;
		this.userEmail = userEmail;
		this.userContactNumber = userContactNumber;
		this.groups=groups;
		this.accessStatus = accessStatus;
	}
	
	
	
	
	
	
}
