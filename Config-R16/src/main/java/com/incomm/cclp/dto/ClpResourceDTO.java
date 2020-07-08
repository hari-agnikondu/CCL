package com.incomm.cclp.dto;

public class ClpResourceDTO {
	
	private Long id;
	
	private String name;
	
	private String url;
	
	private String tabType;
	
	private String key;
	
	private String order;
	
	private long permissionId;

	private String description;
	
	private String permissionName;
	
	
	private String entity;
	
	private String operation;
	
	private Long parentResourceId;
	
	private Long lastUpdUser;

	private Long insUser;
	
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
	public Long getLastUpdUser() {
		return lastUpdUser;
	}
	public void setLastUpdUser(Long lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
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
	@Override
	public String toString() {
		return "ClpResourceDTO [id=" + id + ", name=" + name + ", url=" + url
				+ ", tabType=" + tabType + ", key=" + key + ", order=" + order
				+ ", permissionId=" + permissionId + ", description="
				+ description + ", permissionName=" + permissionName
				+ ", entity=" + entity + ", operation=" + operation
				+ ", parentResourceId=" + parentResourceId + ", lastUpdUser="
				+ lastUpdUser + ", insUser=" + insUser + "]";
	}
	
}
