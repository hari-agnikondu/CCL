package com.incomm.cclp.dto;


import java.io.Serializable;

public class StockDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String merchantId;		
	
	private long locationId;
	
	private long productId;
	
	private String merchantName;		
	
	private String locationName;
	
	private String productName;
	
	private String autoReplenish;

	private Long initialOrder;
	
	private Long reorderLevel;
	
	private Long reorderValue;
	
	
	private Long currInventory;
	
	private Long maxInventory;
	
	private long insUser;
	

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



	public String getAutoReplenish() {
		return autoReplenish;
	}

	public void setAutoReplenish(String autoReplenish) {
		this.autoReplenish = autoReplenish;
	}



	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
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

	

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public long getLocationId() {
		return locationId;
	}

	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
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

	public void setInsUser(long insUser) {
		this.insUser = insUser;
	}

	

}