package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.AssociationOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "GROUP_ROLE")
@AssociationOverride(name = "primaryKey.role", joinColumns = @JoinColumn(name = "ROLE_ID"))
@AssociationOverride(name = "primaryKey.group", joinColumns = @JoinColumn(name = "GROUP_ID")) 
public class GroupRole implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private GroupRoleId primaryKey = new GroupRoleId();

	private Long insUser;

	private Date insDate;

	private Long lastUpdUser;

	private Date lastUpdDate;

	@EmbeddedId
	public GroupRoleId getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(GroupRoleId primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Column(name = "INS_USER", updatable = false)
	public Long getInsUser() {
		return insUser;
	}

	public void setInsUser(Long insUser) {
		this.insUser = insUser;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "INS_DATE", updatable = false)
	public Date getInsDate() {
		return insDate;
	}

	public void setInsDate(Date insDate) {
		this.insDate = new java.sql.Date(new java.util.Date().getTime());
	}

	@Column(name = "LAST_UPD_USER")
	public Long getLastUpdUser() {
		return lastUpdUser;
	}

	public void setLastUpdUser(Long lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_UPD_DATE")
	public Date getLastUpdDate() {
		return lastUpdDate;
	}

	public void setLastUpdDate(Date lastUpdDate) {
		this.lastUpdDate = lastUpdDate;
	}

	@Transient
	public Role getRole() {
		return getPrimaryKey().getRole();
	}

	public void setRole(Role user) {
		getPrimaryKey().setRole(user);
	}

	@Transient
	public Group getGroup() {
		return getPrimaryKey().getGroup();
	}

	public void setGroup(Group group) {
		getPrimaryKey().setGroup(group);
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		UserGroupTemp that = (UserGroupTemp) obj;

		return Objects.equals(getPrimaryKey(), that.getPrimaryKey());
	}

	@Override
	public int hashCode() {
		return (getPrimaryKey() != null ? getPrimaryKey().hashCode() : 0);
	}

	@Override
	public String toString() {
		return "GroupRole [primaryKey=" + primaryKey + ", insUser=" + insUser + ", insDate=" + insDate
				+ ", lastUpdUser=" + lastUpdUser + ", lastUpdDate=" + lastUpdDate + "]";
	}


}
