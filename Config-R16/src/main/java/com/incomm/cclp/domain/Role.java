package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;


/**
 * The persistent class for the "ROLE" database table.
 * 
 */
@Entity
@Audited
@Table(name="ROLE")
public class Role implements Serializable {


	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ROLE_SEQ_GEN", sequenceName="ROLE_ROLE_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ROLE_SEQ_GEN")
	@Column(name="ROLE_ID")
	private long roleId;
    
	@Column(name="DESCRIPTION")
	private String roleDesc;
	
	@Column(name = "INS_USER" ,updatable = false)
	private Long insUser;
	
	@Column(name = "LAST_UPD_USER")
	private Long lastUpdUser;

	@Temporal(TemporalType.DATE)
	@Column(name="INS_DATE",updatable = false )
	private Date insDate = new java.sql.Date(new java.util.Date().getTime());

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPD_DATE")
	private Date lastUpdDate = new java.sql.Date(new java.util.Date().getTime());

	@Column(name="ROLE_NAME")
	private String roleName;

	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "primaryKey.role", cascade = { CascadeType.PERSIST, CascadeType.REMOVE,
			CascadeType.MERGE }, orphanRemoval = true)
	private Set<RolePermission> rolePermissions;	
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "primaryKey.role", cascade = {CascadeType.PERSIST,
			CascadeType.MERGE }, orphanRemoval = true)
	private Set<RolePermissionTemp> rolePermissionsTemp;
	

	
	public Set<RolePermissionTemp> getRolePermissionsTemp() {
		return rolePermissionsTemp;
	}

	public void setRolePermissionsTemp(Set<RolePermissionTemp> rolePermissionsTemp) {
		this.rolePermissionsTemp = rolePermissionsTemp;
	}
	
	public Set<RolePermission> getRolePermissions() {
		return rolePermissions;
	}

	public void setRolePermissions(Set<RolePermission> rolePermissions) {
		this.rolePermissions = rolePermissions;
	}

	@Column(name="STATUS")
	private String status;
	
	@Column(name="CHECKER_REMARKS")
	private String checkerRemarks;


	public String getCheckerRemarks() {
		return checkerRemarks;
	}

	public void setCheckerRemarks(String checkerRemarks) {
		this.checkerRemarks = checkerRemarks;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public Long getLastUpdUser() {
		return lastUpdUser;
	}

	public void setLastUpdUser(Long lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public Date getInsDate() {
		return insDate;
	}

	public void setInsDate(Date insDate) {
		this.insDate =  new java.sql.Date(new java.util.Date().getTime());
	}

	public Date getLastUpdDate() {
		return lastUpdDate;
	}

	public void setLastUpdDate(Date lastUpdDate) {
		this.lastUpdDate =  new java.sql.Date(new java.util.Date().getTime());
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	public Long getInsUser() {
		return insUser;
	}

	public void setInsUser(Long insUser) {
		this.insUser = insUser;
	}

	@Override
	public String toString() {
		return "Role [roleDesc=" + roleDesc + ", roleName=" + roleName + "]";
	}


}