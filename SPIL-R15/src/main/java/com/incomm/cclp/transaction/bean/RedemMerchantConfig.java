package com.incomm.cclp.transaction.bean;

import java.io.Serializable;

public class RedemMerchantConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String productId;
	private String merchantName;
	private String merchantId;
	private String startTime;
	private String endTime;
	private String delayMins;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getDelayMins() {
		return delayMins;
	}

	public void setDelayMins(String delayMins) {
		this.delayMins = delayMins;
	}

	@Override
	public String toString() {
		return "RedemptionDelayHazel [productId=" + productId + ", merchantName=" + merchantName + ", merchantId=" + merchantId
				+ ", startTime=" + startTime + ", endTime=" + endTime + ", delayMins=" + delayMins + "]";
	}

}
