package com.incomm.cclp.dto;

import java.util.List;
import java.io.Serializable;
import java.util.Date;

public class RedemptionDelayDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long productId;
	
	private String merchantId;
	
	private String productName;

	private String merchantName;
	
	private String startTimeDisplay;
	
	private String endTimeDisplay;
	
	private Long redemptionDelayTime;
	
	private Long insUser;
	
	private Date insDate;

	private Long lastUpdUser;

	private Date lastUpdDate;
	
	private List<String> operationsList;
	
	
	
	public RedemptionDelayDTO() {
		
	}

	public RedemptionDelayDTO(Long productId, String merchantId, String productName, String merchantName,
			String startTimeDisplay, String endTimeDisplay, Long redemptionDelayTime, Long insUser, Date insDate,
			Long lastUpdUser, Date lastUpdDate) {
		
		this.productId = productId;
		this.merchantId = merchantId;
		this.productName = productName;
		this.merchantName = merchantName;
		this.startTimeDisplay = startTimeDisplay;
		this.endTimeDisplay = endTimeDisplay;
		this.redemptionDelayTime = redemptionDelayTime;
		this.insUser = insUser;
		this.insDate = insDate;
		this.lastUpdUser = lastUpdUser;
		this.lastUpdDate = lastUpdDate;
		
	}

	public List<String> getOperationsList() {
		return operationsList;
	}

	public void setOperationsList(List<String> operationsList) {
		this.operationsList = operationsList;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
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
		this.insDate = insDate;
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
		return "MerchantDTO [merchantId=" + merchantId + ", productId=" + productId +", startTimeDisplay=" + startTimeDisplay
				+", endTimeDisplay"+ endTimeDisplay +", redemptionDelayTime"+ redemptionDelayTime +", insUser="+ insUser + ", insDate=" + insDate + ", lastUpdUser=" + lastUpdUser + ", lastUpdDate=" + lastUpdDate
				+ "]";
	}

}
