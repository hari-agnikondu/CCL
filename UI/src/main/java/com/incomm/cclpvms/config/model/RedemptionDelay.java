package com.incomm.cclpvms.config.model;

public class RedemptionDelay {
	private String merchantId;
	private Long productId;
	private String merchantName;
	private String productName;
	private String startTimeDisplay;
	private String endTimeDisplay;
	private Long redemptionDelayTime;
	
	

	public RedemptionDelay() {
		
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getStartTimeDisplay() {
		return startTimeDisplay;
	}

	public void setStartTimeDisplay(String startTimeDisplay) {
		this.startTimeDisplay = startTimeDisplay;
	}

	public String getEndTimeDisplay() {
		return endTimeDisplay;
	}

	public void setEndTimeDisplay(String endTimeDisplay) {
		this.endTimeDisplay = endTimeDisplay;
	}

	public Long getRedemptionDelayTime() {
		return redemptionDelayTime;
	}

	public void setRedemptionDelayTime(Long redemptionDelayTime) {
		this.redemptionDelayTime = redemptionDelayTime;
	}

	public RedemptionDelay(String merchantId, Long productId, String merchantName, String productName,
			String startTimeDisplay, String endTimeDisplay, Long redemptionDelayTime) {
		super();
		this.merchantId = merchantId;
		this.productId = productId;
		this.merchantName = merchantName;
		this.productName = productName;
		this.startTimeDisplay = startTimeDisplay;
		this.endTimeDisplay = endTimeDisplay;
		this.redemptionDelayTime = redemptionDelayTime;
	}

	@Override
	public String toString() {
		return "RedemptionDelay [merchantId=" + merchantId + ", productId=" + productId + ", merchantName="
				+ merchantName + ", productName=" + productName + ", startTimeDisplay=" + startTimeDisplay
				+ ", endTimeDisplay=" + endTimeDisplay + ", redemptionDelayTime=" + redemptionDelayTime + "]";
	}

		
	
	
	

}
