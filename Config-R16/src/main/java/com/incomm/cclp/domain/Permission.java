package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;


/**
 * The persistent class for the PERMISSION database table.
 * 
 */
@Entity
@Audited
@Table(name="PERMISSION")
public class Permission implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PERMISSION_SEQ_GEN", sequenceName="PERMISSION_PERMISSION_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PERMISSION_SEQ_GEN")
	@Column(name="PERMISSION_ID")
	private long permissionId;

	@Column(name="DESCRIPTION")
	private String description;

	@Temporal(TemporalType.DATE)
	@Column(name="INS_DATE")
	private Date insDate;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPD_DATE")
	private Date lastUpdDate;

	@Column(name="PERMISSION_NAME")
	private String permissionName;
	
	
	@Column(name = "INS_USER" ,updatable = false)
	private Long insUser;
	
	@Column(name = "LAST_UPD_USER")
	private Long lastUpdUser;

	public Long getInsUser() {
		return insUser;
	}
	
	@Column(name="ENTITY")
	private String entity;
	
	@Column(name="OPERATION")
	private String operation;


	public void setInsUser(Long insUser) {
		this.insUser = insUser;
	}


	public Long getLastUpdUser() {
		return lastUpdUser;
	}


	public void setLastUpdUser(Long lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	public Permission() {
	}


	public Permission(long permissionId) {
		super();
		this.permissionId = permissionId;
	}


	public long getPermissionId() {
		return permissionId;
	}


	public void setPermissionId(long permissionId) {
		this.permissionId = permissionId;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
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


	public String getPermissionName() {
		return permissionName;
	}


	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}


	public String getEntity() {
		return entity;
	}


	public void setEntity(String entity) {
		this.entity = entity;
	}


	public String getOperation() {
		return operation;
	}


	public void setOperation(String operation) {
		this.operation = operation;
	}
  
	
}