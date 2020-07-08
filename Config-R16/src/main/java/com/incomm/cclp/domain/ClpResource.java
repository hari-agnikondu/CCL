package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;


/**
 * The persistent class for the CLP_RESOURCE database table.
 * 
 */
@Entity
@Audited
@Table(name="CLP_RESOURCE")
public class ClpResource implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="RESOURCE_ID")
	private Long id;
	
	@Column(name="RESOURCE_NAME")
	private String name;
	
	@Column(name="RESOURCE_URL")
	private String url;
	
	@Column(name="RESOURCE_TAB_TYPE")
	private String tabType;
	
	@Column(name="RESOURCE_KEY")
	private String key;
	
	@Column(name="RESOURCE_ORDER")
	private String order;
	
	//bi-directional many-to-one association to Permission
	@ManyToOne
	@JoinColumn(name="PERMISSION_ID")
	private Permission permission;
	
	@Column(name="PARENT_RESOURCE_ID")
	private Long parentResourceId;

	@Column(name="INS_USER",updatable = false )
	private Long insUser;
	@CreationTimestamp
	@Column(name="INS_DATE",updatable = false )
	private Date insDate;
	
	@Column(name="LAST_UPD_USER")
	private Long lastUpdUser;
	
	@UpdateTimestamp
	@Column(name="LAST_UPD_DATE")
	private Date lastUpdDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTabType() {
		return tabType;
	}

	public void setTabType(String tabType) {
		this.tabType = tabType;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public Permission getPermission() {
		return permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}

	public Long getParentResourceId() {
		return parentResourceId;
	}

	public void setParentResourceId(Long parentResourceId) {
		this.parentResourceId = parentResourceId;
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

	@Override
	public String toString() {
		return "ClpResource [id=" + id + ", name=" + name + ", url=" + url
				+ ", tabType=" + tabType + ", key=" + key + ", order=" + order
				+ ", permission=" + permission + ", parentResourceId="
				+ parentResourceId + ", insUser=" + insUser + ", insDate="
				+ insDate + ", lastUpdUser=" + lastUpdUser + ", lastUpdDate="
				+ lastUpdDate + "]";
	}

	
}