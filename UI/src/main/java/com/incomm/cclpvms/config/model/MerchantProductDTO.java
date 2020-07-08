package com.incomm.cclpvms.config.model;

import java.util.Date;

public class MerchantProductDTO {

	private Long merchantId;
	private Long productId;
	private String merchantName;
	private String productName;
	
	private Long insUser;
	
	private Date insDate;
	
	private Long lastUpdUser;
	
	private Date lastUpdDate;
	
	
	

	
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
	
	
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
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
	

	public MerchantProductDTO() {


	}
	public MerchantProductDTO(Long merchantId, Long productId, String merchantName, String productName, Long insUser,
			Date insDate, Long lastUpdUser, Date lastUpdDate) {

		this.merchantId = merchantId;
		this.productId = productId;
		this.merchantName = merchantName;
		this.productName = productName;
		this.insUser = insUser;
		this.insDate = insDate;
		this.lastUpdUser = lastUpdUser;
		this.lastUpdDate = lastUpdDate;
	}
	@Override
	public String toString() {
		return "MerchantProductDTO [merchantId=" + merchantId + ", productId=" + productId + ", merchantName="
				+ merchantName + ", productName=" + productName + ", insUser=" + insUser + ", insDate=" + insDate
				+ ", lastUpdUser=" + lastUpdUser + ", lastUpdDate=" + lastUpdDate + "]";
	}
	
	
	

	
}
