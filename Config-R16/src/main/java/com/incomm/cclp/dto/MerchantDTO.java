package com.incomm.cclp.dto;

import java.io.Serializable;
import java.util.Date;

public class MerchantDTO implements Serializable{
	
	
	private static final long serialVersionUID = 1L;

	private Long merchantId;

	private String merchantName;
	private Long productId;	
	
	private String productName;

	private String description;

	private String mdmId;

	private Long insUser;

	private Date insDate;

	private Long lastUpdUser;

	private Date lastUpdDate;

	private Object data;
	
	private String result;
	
	private String message;

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMdmId() {
		return mdmId;
	}

	public void setMdmId(String mdmId) {
		this.mdmId = mdmId;
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

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "MerchantDTO [merchantId=" + merchantId + ", merchantName=" + merchantName + ", productId=" + productId
				+ ", productName=" + productName + ", description=" + description + ", mdmId=" + mdmId + ", insUser="
				+ insUser + ", insDate=" + insDate + ", lastUpdUser=" + lastUpdUser + ", lastUpdDate=" + lastUpdDate
				+ ", data=" + data + ", result=" + result + ", message=" + message + "]";
	}

	


	

	
	

}
