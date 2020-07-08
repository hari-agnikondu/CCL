package com.incomm.cclp.domain;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.envers.Audited;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the "GROUP" database table.
 * 
 */
@Entity
@Audited
@Table(name="CLP_GROUP")
public class Group implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GROUP_SEQ_GEN", sequenceName="GROUP_GROUP_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GROUP_SEQ_GEN")
	@Column(name="GROUP_ID")
	private long groupId;

	@Column(name="GROUP_NAME")
	private String groupName;

	@Temporal(TemporalType.DATE)
	@Column(name="INS_DATE",updatable = false)
	private Date insDate;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPD_DATE")
	private Date lastUpdDate;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="CHECKER_REMARKS")
	private String groupCheckerRemarks;
	
	@Column(name = "INS_USER" ,updatable = false)
	private Long insUser;
	
	
	@Column(name = "LAST_UPD_USER" ,updatable = false)
	private Long lastUpdateUser;
	
	public Long getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(Long lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public Long getInsUser() {
		return insUser;
	}

	public void setInsUser(Long insUser) {
		this.insUser = insUser;
	}

	public String getGroupCheckerRemarks() {
		return groupCheckerRemarks;
	}

	public void setGroupCheckerRemarks(String groupCheckerRemarks) {
		this.groupCheckerRemarks = groupCheckerRemarks;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
		this.insDate = new java.sql.Date(new java.util.Date().getTime());
	}

	public Date getLastUpdDate() {
		return lastUpdDate;
	}

	public void setLastUpdDate(Date lastUpdDate) {
		this.lastUpdDate = lastUpdDate;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy="primaryKey.group",cascade={CascadeType.ALL}, orphanRemoval=true)
	private List<GroupRole> groupRole;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="primaryKey.group",cascade={CascadeType.ALL}, orphanRemoval=true)
	private List<GroupRoleTemp> roletemp;

	public List<GroupRole> getGroupRole() {
		return groupRole;
	}

	public void setGroupRole(List<GroupRole> groupRole) {
		this.groupRole = groupRole;
	}

	public List<GroupRoleTemp> getRoletemp() {
		return roletemp;
	}

	public void setRoletemp(List<GroupRoleTemp> roletemp) {
		this.roletemp = roletemp;
	}

	@Override
	public String toString() {
		return "Group [groupId=" + groupId + ", groupName=" + groupName + ", insDate=" + insDate + ", lastUpdDate="
				+ lastUpdDate + ", status=" + status + ", groupCheckerRemarks=" + groupCheckerRemarks + ", insUser="
				+ insUser + ", lastUpdateUser=" + lastUpdateUser + ", groupRole=" + groupRole + ", roletemp=" + roletemp
				+ "]";
	}

	
	
}