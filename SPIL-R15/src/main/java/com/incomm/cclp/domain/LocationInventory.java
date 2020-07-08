package com.incomm.cclp.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class LocationInventory implements Serializable {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	protected LocationInventoryPK locationInventoryPK;

	@Basic(optional = false)
	@Column(name = "AUTO_REPLENISH")
	private String autoReplenish;

	@Basic(optional = false)
	@Column(name = "INITIAL_ORDER")
	private BigDecimal initialOrder;

	@Basic(optional = false)
	@Column(name = "REORDER_LEVEL")
	private BigDecimal reorder_level;

	@Basic(optional = false)
	@Column(name = "REORDER_VALUE")
	private BigDecimal reorderValue;

	@Basic(optional = false)
	@Column(name = "CURR_INVENTORY")
	private BigDecimal currentInventory;

	@Basic(optional = false)
	@Column(name = "MAX_INVENTORY")
	private BigDecimal maxInventory;

	@Basic(optional = false)
	@Column(name = "INS_USER")
	private BigDecimal insUser;

	@Basic(optional = false)
	@Column(name = "INS_DATE")
	private Date insDate;

	@Basic(optional = false)
	@Column(name = "LAST_UPD_USER")
	private BigDecimal lastUpdUser;

	@Basic(optional = false)
	@Column(name = "LAST_UPD_DATE")
	private Date lastUpdDate;

	public LocationInventory() {
	}

	public LocationInventory(LocationInventoryPK locationInventoryPK) {
		this.locationInventoryPK = locationInventoryPK;
	}

	public LocationInventoryPK getLocationInventoryPK() {
		return locationInventoryPK;
	}

	public void setLocationInventoryPK(LocationInventoryPK locationInventoryPK) {
		this.locationInventoryPK = locationInventoryPK;
	}

	public String getAutoReplenish() {
		return autoReplenish;
	}

	public void setAutoReplenish(String autoReplenish) {
		this.autoReplenish = autoReplenish;
	}

	public BigDecimal getInitialOrder() {
		return initialOrder;
	}

	public void setInitialOrder(BigDecimal initialOrder) {
		this.initialOrder = initialOrder;
	}

	public BigDecimal getReorder_level() {
		return reorder_level;
	}

	public void setReorder_level(BigDecimal reorder_level) {
		this.reorder_level = reorder_level;
	}

	public BigDecimal getReorderValue() {
		return reorderValue;
	}

	public void setReorderValue(BigDecimal reorderValue) {
		this.reorderValue = reorderValue;
	}

	public BigDecimal getCurrentInventory() {
		return currentInventory;
	}

	public void setCurrentInventory(BigDecimal currentInventory) {
		this.currentInventory = currentInventory;
	}

	public BigDecimal getMaxInventory() {
		return maxInventory;
	}

	public void setMaxInventory(BigDecimal maxInventory) {
		this.maxInventory = maxInventory;
	}

	public BigDecimal getInsUser() {
		return insUser;
	}

	public void setInsUser(BigDecimal insUser) {
		this.insUser = insUser;
	}

	public Date getInsDate() {
		return insDate;
	}

	public void setInsDate(Date insDate) {
		this.insDate = insDate;
	}

	public BigDecimal getLastUpdUser() {
		return lastUpdUser;
	}

	public void setLastUpdUser(BigDecimal lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	public Date getLastUpdDate() {
		return lastUpdDate;
	}

	public void setLastUpdDate(Date lastUpdDate) {
		this.lastUpdDate = lastUpdDate;
	}

}
