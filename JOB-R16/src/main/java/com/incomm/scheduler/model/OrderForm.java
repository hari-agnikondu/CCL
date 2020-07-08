package com.incomm.scheduler.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.Arrays;


public class OrderForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long partnerId;
	private String partnerName;
	
	private Long productId;
	private String productName;
	
	private String packageId;
	
	private Long issuerId;
	private String issuerName;
	
	private Long merchantId;
	private String merchantName;
	
	private Long locationId;
	private String locationName;
	
	private String defaultCardStatus;
	
	private String orderId;

	private String quantity;
	
	private long insUser;
	
	private String[] orderPartnerId; 
	
	private String checkerRemarks;
	private String lineItemId;
	
	private String status;
	
	private Date insDate;
	
	private String orderType;
	
	private String userName;
	
	
	public OrderForm() {
		// constructor
	}
	
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	
	public long getInsUser() {
		return insUser;
	}

	public void setInsUser(long insUser) {
		this.insUser = insUser;
	}

	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Long getIssuerId() {
		return issuerId;
	}

	public void setIssuerId(Long issuerId) {
		this.issuerId = issuerId;
	}

	public String getIssuerName() {
		return issuerName;
	}

	public void setIssuerName(String issuerName) {
		this.issuerName = issuerName;
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

	public String getDefaultCardStatus() {
		return defaultCardStatus;
	}

	public void setDefaultCardStatus(String defaultCardStatus) {
		this.defaultCardStatus = defaultCardStatus;
	}

	public String[] getOrderPartnerId() {
		return orderPartnerId;
	}

	public void setOrderPartnerId(String[] orderPartnerId) {
		this.orderPartnerId = orderPartnerId;
	}

	public String getCheckerRemarks() {
		return checkerRemarks;
	}

	public void setCheckerRemarks(String checkerRemarks) {
		this.checkerRemarks = checkerRemarks;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getInsDate() {
		return insDate;
	}

	public void setInsDate(Date insDate) {
		this.insDate = new java.sql.Date(new java.util.Date().getTime());
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	@Override
	public String toString() {
		return "OrderForm [partnerId=" + partnerId + ", partnerName=" + partnerName + ", productId=" + productId
				+ ", productName=" + productName + ", packageId=" + packageId + ", issuerId=" + issuerId
				+ ", issuerName=" + issuerName + ", merchantId=" + merchantId + ", merchantName=" + merchantName
				+ ", locationId=" + locationId + ", locationName=" + locationName + ", defaultCardStatus="
				+ defaultCardStatus + ", orderId=" + orderId + ", quantity=" + quantity + ", insUser=" + insUser
				+ ", orderPartnerId=" + Arrays.toString(orderPartnerId) + ", checkerRemarks=" + checkerRemarks
				+ ", status=" + status + ", date=" + insDate + "]";
	}

	public String getLineItemId() {
		return lineItemId;
	}

	public void setLineItemId(String lineItemId) {
		this.lineItemId = lineItemId;
	}
	
	
	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	//added for 'Check Order Status screen'	
    String lineItemIdStatus;
    String ccfFileName;
    String startSerialNo;
    String endSerialNo;


	public String getLineItemIdStatus() {
		return lineItemIdStatus;
	}

	public void setLineItemIdStatus(String lineItemIdStatus) {
		this.lineItemIdStatus = lineItemIdStatus;
	}

	public String getCcfFileName() {
		return ccfFileName;
	}

	public void setCcfFileName(String ccfFileName) {
		this.ccfFileName = ccfFileName;
	}

	public String getStartSerialNo() {
		return startSerialNo;
	}

	public void setStartSerialNo(String startSerialNo) {
		this.startSerialNo = startSerialNo;
	}

	public String getEndSerialNo() {
		return endSerialNo;
	}

	public void setEndSerialNo(String endSerialNo) {
		this.endSerialNo = endSerialNo;
	}

    
    
    
}
