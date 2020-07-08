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
@Table(name="ROLE_PERMISSION")
@AssociationOverride(name = "primaryKey.role", joinColumns = @JoinColumn(name = "ROLE_ID"))
@AssociationOverride(name = "primaryKey.permission", joinColumns = @JoinColumn(name = "PERMISSION_ID"))
public class RolePermission implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	RolePermissionTempId  primaryKey = new RolePermissionTempId();
	
	private long insUser;

	@Temporal(TemporalType.DATE)
	@Column(name = "INS_DATE", updatable = false)
	private Date insDate =  new java.sql.Date(new java.util.Date().getTime());
	
	private long lastUpdUser;

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_UPD_DATE")
	private Date lastUpdDate =  new java.sql.Date(new java.util.Date().getTime());


	@EmbeddedId
	public RolePermissionTempId getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(RolePermissionTempId primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Column(name = "INS_USER", updatable = false)
	public long getInsUser() {
		return insUser;
	}

	public void setInsUser(long insUser) {
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

	public void setLastUpdDate(Date lastUpdDate) {
		this.lastUpdDate = lastUpdDate;
	}

	@Column(name = "LAST_UPD_USER")
	public long getLastUpdUser() {
		return lastUpdUser;
	}

	public void setLastUpdUser(long lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_UPD_DATE")
	public Date getLastUpdDate() {
		return lastUpdDate;
	}

	@Transient
	public Role getRole() {
		return getPrimaryKey().getRole();
	}

	public void setRole(Role product) {
		getPrimaryKey().setRole(product);
	}
	
	@Transient
	public Permission getPermission() {
		return getPrimaryKey().getPermission();
	}
	
	public void setPermission(Permission permission) {
		getPrimaryKey().setPermission(permission);
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		else if (getClass() != obj.getClass())
			return false;
		else {
			RolePermissionTemp that = (RolePermissionTemp) obj;
			return Objects.equals(getPrimaryKey(), that.getPrimaryKey());
		}
	}

	@Override
	public int hashCode() {
		return (getPrimaryKey() != null ? getPrimaryKey().hashCode() : 0);
	}

}
