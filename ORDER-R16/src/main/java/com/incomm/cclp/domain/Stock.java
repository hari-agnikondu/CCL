package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="LOCATION_INVENTORY")
public class Stock  implements Serializable{

	private static final long serialVersionUID = 1L;
	

	@Id
	@Column(name="MERCHANT_ID")
	private String merchantId;		
	
	@Id
	@Column(name="LOCATION_ID")
	private String locationId;
	
	@Id
	@Column(name="PRODUCT_ID")
	private long productId;
	
	@Column(name="AUTO_REPLENISH")
	private String autoReplenish;

	@Column(name="INITIAL_ORDER")
	private Long initialOrder;
	
	@Column(name="REORDER_LEVEL")
	private Long reorderLevel;
	
	@Column(name="REORDER_VALUE")
	private Long reorderValue;
	
	
	@Column(name="CURR_INVENTORY")
	private Long currInventory;
	
	@Column(name="MAX_INVENTORY")
	private Long maxInventory;
	
	
	@Column(name="INS_USER",updatable = false )
	private long insUser;
	
	@Temporal(TemporalType.DATE)
	@Column(name="INS_DATE",updatable = false)
	private Date insDate;
	
	
	@Column(name="LAST_UPD_USER")
	private long lastUpdUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPD_DATE")
	private Date lastUpdDate;

	
	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getAutoReplenish() {
		return autoReplenish;
	}

	public void setAutoReplenish(String autoReplenish) {
		this.autoReplenish = autoReplenish;
	}

	public Long getInitialOrder() {
		return initialOrder;
	}

	public void setInitialOrder(Long initialOrder) {
		this.initialOrder = initialOrder;
	}

	public Long getReorderLevel() {
		return reorderLevel;
	}

	public void setReorderLevel(Long reorderLevel) {
		this.reorderLevel = reorderLevel;
	}


	
	public Long getReorderValue() {
		return reorderValue;
	}

	public void setReorderValue(Long reorderValue) {
		this.reorderValue = reorderValue;
	}

	public Long getCurrInventory() {
		return currInventory;
	}

	public void setCurrInventory(Long currInventory) {
		this.currInventory = currInventory;
	}

	public Long getMaxInventory() {
		return maxInventory;
	}

	public void setMaxInventory(Long maxInventory) {
		this.maxInventory = maxInventory;
	}

	public long getInsUser() {
		return insUser;
	}

	public void setInsUser() {
		this.insUser = 1;
	}

	public Date getInsDate() {
		return insDate;
	}

	public void setInsDate() {
		this.insDate = new java.sql.Date(new java.util.Date().getTime());
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

	public void setLastUpdDate() {
		this.lastUpdDate = new java.sql.Date(new java.util.Date().getTime());
	}

	@Override
	public String toString() {
		return "Stock [merchantId=" + merchantId + ", locationId=" + locationId + ", productId=" + productId
				+ ", autoReplenish=" + autoReplenish + ", initialOrder=" + initialOrder + ", reorderLevel="
				+ reorderLevel + ", reorderValue=" + reorderValue + ", currInventory=" + currInventory
				+ ", maxInventory=" + maxInventory + ", insUser=" + insUser + ", insDate=" + insDate + ", lastUpdUser="
				+ lastUpdUser + ", lastUpdDate=" + lastUpdDate + "]";
	}
	
}