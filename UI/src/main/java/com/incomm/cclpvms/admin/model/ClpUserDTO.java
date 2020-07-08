package com.incomm.cclpvms.admin.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * The DTO class for the ClpUser.
 * 
 */

public class ClpUserDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	
	
	private Long userId;
	
	private String userLoginId;
	
	private String lastLoginTime;

	private String userName;

	private String userEmail;

	private Long userContactNumber;

	private String userStatus;

	private String checkerRemarks;

	private Long insUser;

	private Date insDate;

	private long lastUpdUser;

	private Date lastUpdDate;

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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserLoginId() {
		return userLoginId;
	}

	public void setUserLoginId(String userLoginId) {
		this.userLoginId = userLoginId;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
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

	public Long getInsUser() {
		return insUser;
	}

	public void setInsUser(Long insUser) {
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

	public List<GroupDTO> getGroups() {
		return groups;
	}

	public void setGroups(List<GroupDTO> groups) {
		this.groups = groups;
	}

	

	@Override
	public String toString() {
		return "ClpUserDTO [userId=" + userId + ", userLoginId=" + userLoginId + ", lastLoginTime=" + lastLoginTime
				+ ", userName=" + userName + ", userEmail=" + userEmail + ", userContactNumber=" + userContactNumber
				+ ", userStatus=" + userStatus + ", checkerRemarks=" + checkerRemarks + ", insUser=" + insUser
				+ ", insDate=" + insDate + ", lastUpdUser=" + lastUpdUser + ", lastUpdDate=" + lastUpdDate + ", groups="
				+ groups + ", accessStatus=" + accessStatus + "]";
	}

	public ClpUserDTO() {
	
	}
	
	

	public ClpUserDTO(Long userId, String userLoginId, String lastLoginTime, String userName, String userEmail,
			Long userContactNumber, String userStatus, String checkerRemarks, Long insUser, Date insDate,
			long lastUpdUser, Date lastUpdDate, List<GroupDTO> groups) {
		super();
		this.userId = userId;
		this.userLoginId = userLoginId;
		this.lastLoginTime = lastLoginTime;
		this.userName = userName;
		this.userEmail = userEmail;
		this.userContactNumber = userContactNumber;
		this.userStatus = userStatus;
		this.checkerRemarks = checkerRemarks;
		this.insUser = insUser;
		this.insDate = insDate;
		this.lastUpdUser = lastUpdUser;
		this.lastUpdDate = lastUpdDate;
		this.groups = groups;
	}

	public ClpUserDTO(Long userId, String userLoginId, String lastLoginTime, String userName, String userEmail,
			Long userContactNumber, String userStatus, String checkerRemarks, Long insUser, Date insDate,
			long lastUpdUser, Date lastUpdDate, List<GroupDTO> groups, String accessStatus) {
		super();
		this.userId = userId;
		this.userLoginId = userLoginId;
		this.lastLoginTime = lastLoginTime;
		this.userName = userName;
		this.userEmail = userEmail;
		this.userContactNumber = userContactNumber;
		this.userStatus = userStatus;
		this.checkerRemarks = checkerRemarks;
		this.insUser = insUser;
		this.insDate = insDate;
		this.lastUpdUser = lastUpdUser;
		this.lastUpdDate = lastUpdDate;
		this.groups = groups;
		this.accessStatus = accessStatus;
	}

	
	
	
	
	
}