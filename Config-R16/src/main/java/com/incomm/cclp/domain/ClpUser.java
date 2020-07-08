package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "CLP_USER")
@NamedQuery(name = "ClpUser.findAll", query = "SELECT c FROM ClpUser c")
public class ClpUser implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "USER_ID")
	@SequenceGenerator(name = "CLP_USER_SEQ_GEN", sequenceName = "CLP_USER_USER_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CLP_USER_SEQ_GEN")
	private Long userId;

	@Column(name = "USER_LOGIN_ID")
	private String userLoginId;

	@Column(name = "LAST_LOGIN_TIME", updatable = false)
	private String lastLoginTime;

	@Column(name = "INS_USER", updatable = false)
	private Long insUser;

	@Temporal(TemporalType.DATE)
	@Column(name = "INS_DATE", updatable = false)
	private Date insDate;

	@Column(name = "LAST_UPD_USER")
	private Long lastUpdUser;

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_UPD_DATE")
	private Date lastUpdDate;

	@Column(name = "STATUS")
	private String userStatus;

	@Column(name = "CHECKER_REMARKS")
	private String checkerRemarks;

	@Column(name = "USER_NAME")
	private String userName;

	@Column(name = "USER_EMAIL")
	private String userEmail;

	@Column(name = "USER_CONTACT_NO")
	private Long userContactNumber;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "primaryKey.user", cascade = { CascadeType.ALL}, orphanRemoval = true)
	private List<UserGroupTemp> listUserGroupTemp;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "primaryKey.user", cascade = { CascadeType.ALL}, orphanRemoval = true)
	private List<UserGroup> listUserGroup;	

	@Column(name = "ACCESS_STATUS")
	private String accessStatus;
	
	
	public String getAccessStatus() {
		return accessStatus;
	}

	public void setAccessStatus(String accessStatus) {
		this.accessStatus = accessStatus;
	}

	public List<UserGroup> getListUserGroup() {
		return listUserGroup;
	}

	public void setListUserGroup(List<UserGroup> listUserGroup) {
		this.listUserGroup = listUserGroup;
	}

	public List<UserGroupTemp> getListUserGroupTemp() {
		return listUserGroupTemp;
	}

	public void setListUserGroupTemp(List<UserGroupTemp> listUserGroupTemp) {
		this.listUserGroupTemp = listUserGroupTemp;
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
		this.insDate = new java.sql.Date(new java.util.Date().getTime());
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

}
