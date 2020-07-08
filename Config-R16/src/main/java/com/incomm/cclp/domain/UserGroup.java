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
@Table(name = "USER_GROUP")
@AssociationOverride(name = "primaryKey.user", joinColumns = @JoinColumn(name = "USER_ID"))
@AssociationOverride(name = "primaryKey.group", joinColumns = @JoinColumn(name = "GROUP_ID")) 
public class UserGroup implements Serializable {

	private static final long serialVersionUID = 1L;

	private UserGroupTempId primaryKey = new UserGroupTempId();

	private Long insUser;

	private Date insDate;

	private Long lastUpdUser;

	private Date lastUpdDate;

	@EmbeddedId
	public UserGroupTempId getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(UserGroupTempId primaryKey) {
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
	public ClpUser getClpUser() {
		return getPrimaryKey().getUser();
	}

	public void setClpUser(ClpUser user) {
		getPrimaryKey().setUser(user);
	}

	@Transient
	public Group getGroup() {
		return getPrimaryKey().getGroup();
	}

	public void setGroup(Group group) {
		getPrimaryKey().setGroup(group);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		UserGroup that = (UserGroup) obj;

		return Objects.equals(getPrimaryKey(), that.getPrimaryKey());
	}

	@Override
	public int hashCode() {
		return (getPrimaryKey() != null ? getPrimaryKey().hashCode() : 0);
	}

	@Override
	public String toString() {
		return "UserGroupTemp [primaryKey=" + primaryKey + ", insUser=" + insUser + ", insDate=" + insDate
				+ ", lastUpdUser=" + lastUpdUser + ", lastUpdDate=" + lastUpdDate + "]";
	}

}
