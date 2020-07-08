package com.incomm.scheduler.model;

public class AutoReplenishmentInventory {
	
private static final long serialVersionUID = 1L;
	
	private String merchantId;		

	private String locationId;
	
	private Long productId;
	
	private String merchantName;		
	
	private String locationName;
	
	private long productName;
	
	private String autoReplenish;

	private Long initialOrder;
	
	private Long reorderLevel;
	
	private String reorderValue;
	
	
	private String currInventory;
	
	private String maxInventory;
	
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

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public long getProductName() {
		return productName;
	}

	public void setProductName(long productName) {
		this.productName = productName;
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

	public String getReorderValue() {
		return reorderValue;
	}

	public void setReorderValue(String reorderValue) {
		this.reorderValue = reorderValue;
	}

	public String getCurrInventory() {
		return currInventory;
	}

	public void setCurrInventory(String currInventory) {
		this.currInventory = currInventory;
	}

	public String getMaxInventory() {
		return maxInventory;
	}

	public void setMaxInventory(String maxInventory) {
		this.maxInventory = maxInventory;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	      

	
}
