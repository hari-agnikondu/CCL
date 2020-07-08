package com.incomm.cclp.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

public class OrderDTO implements Serializable {
	private static final long serialVersionUID = 1L;

		private String orderId;		

		private long partnerId;
		
		private String partnerName;
		
		private Long issuerId;
		
		private String issuerName;

		private String merchantId;
		
		private String merchantName;
		
		private Long locationId;
		
		private String locationName;
		
		private Long productId;
		
		private String productName;
		
		private Date insDate;

		private String defaultCardStatus;
		
		
		private String postBackResponse;
		
		
		private String postBackUrl;
		
		
		private String activationCode;
		
		private String checkerRemarks;
		
		private String shippingMethod;
		
		
		private String status;
		
		
		private String addressLine1;
		
		
		private String addressLine2;
		
		
		private String addressLine3;
		
		
		private String city;
		
		
		private String state;
		
		
		private String postalCode;
		
		
		private String country;
		
		
		private String firstname;
		
		
		private String middleInitial;
		
		
		private String lastName;
		
		
		private String phone;
		
		
		private String email;
		
		
		private String shipToCompanyName;
		
		
		private String shippingFee;
		
		
		private String errorMsg;
		
		
		private String channelId;
		
		
		private String acceptPartial;
		
		
		private String orderType;
		
		
		private String parentOid;
		
		
		private String orderSource;
		
		
		private String fulfillmentType;
		
		private String packageId;
		
		private long insUser;
		
		private String[] orderPartnerId;
		
		private String quantity;
		
		private String userName;

		public OrderDTO() {
			
		}


		public long getInsUser() {
			return insUser;
		}


		public void setInsUser(long insUser) {
			this.insUser = insUser;
		}


		public String getCheckerRemarks() {
			return checkerRemarks;
		}


		public void setCheckerRemarks(String checkerRemarks) {
			this.checkerRemarks = checkerRemarks;
		}


		public String[] getOrderPartnerId() {
			return orderPartnerId;
		}


		public void setOrderPartnerId(String[] orderPartnerId) {
			this.orderPartnerId = orderPartnerId;
		}


		public void setPartnerId(long partnerId) {
			this.partnerId = partnerId;
		}


		
		
		
		public String getPartnerName() {
			return partnerName;
		}


		public void setPartnerName(String partnerName) {
			this.partnerName = partnerName;
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


		public String getProductName() {
			return productName;
		}


		public void setProductName(String productName) {
			this.productName = productName;
		}


		public String getPackageId() {
			return packageId;
		}


		public void setPackageId(String packageId) {
			this.packageId = packageId;
		}


		private String lineItemId;

		
		public Long getProductId() {
			return productId;
		}


		public void setProductId(Long productId) {
			this.productId = productId;
		}


		public String getOrderId() {
			return orderId;
		}


		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}


		public Long getPartnerId() {
			return partnerId;
		}


		public Long getIssuerId() {
			return issuerId;
		}


		public void setIssuerId(Long issuerId) {
			this.issuerId = issuerId;
		}


		public String getMerchantId() {
			return merchantId;
		}


		public void setMerchantId(String merchantId) {
			this.merchantId = merchantId;
		}


		public Long getLocationId() {
			return locationId;
		}


		public void setLocationId(Long locationId) {
			this.locationId = locationId;
		}


		public Date getInsDate() {
			return insDate;
		}


		public void setInsDate(Date insDate) {
			this.insDate = insDate;
		}


		public String getDefaultCardStatus() {
			return defaultCardStatus;
		}


		public void setDefaultCardStatus(String defaultCardStatus) {
			this.defaultCardStatus = defaultCardStatus;
		}


		public String getPostBackResponse() {
			return postBackResponse;
		}


		public void setPostBackResponse(String postBackResponse) {
			this.postBackResponse = postBackResponse;
		}


		public String getPostBackUrl() {
			return postBackUrl;
		}


		public void setPostBackUrl(String postBackUrl) {
			this.postBackUrl = postBackUrl;
		}


		public String getActivationCode() {
			return activationCode;
		}


		public void setActivationCode(String activationCode) {
			this.activationCode = activationCode;
		}


		public String getShippingMethod() {
			return shippingMethod;
		}


		public void setShippingMethod(String shippingMethod) {
			this.shippingMethod = shippingMethod;
		}


		public String getStatus() {
			return status;
		}


		public void setStatus(String status) {
			this.status = status;
		}


		public String getAddressLine1() {
			return addressLine1;
		}


		public void setAddressLine1(String addressLine1) {
			this.addressLine1 = addressLine1;
		}


		public String getAddressLine2() {
			return addressLine2;
		}


		public void setAddressLine2(String addressLine2) {
			this.addressLine2 = addressLine2;
		}


		public String getAddressLine3() {
			return addressLine3;
		}


		public void setAddressLine3(String addressLine3) {
			this.addressLine3 = addressLine3;
		}


		public String getCity() {
			return city;
		}


		public void setCity(String city) {
			this.city = city;
		}


		public String getState() {
			return state;
		}


		public void setState(String state) {
			this.state = state;
		}


		public String getPostalCode() {
			return postalCode;
		}


		public void setPostalCode(String postalCode) {
			this.postalCode = postalCode;
		}


		public String getCountry() {
			return country;
		}


		public void setCountry(String country) {
			this.country = country;
		}


		public String getFirstname() {
			return firstname;
		}


		public void setFirstname(String firstname) {
			this.firstname = firstname;
		}


		public String getMiddleInitial() {
			return middleInitial;
		}


		public void setMiddleInitial(String middleInitial) {
			this.middleInitial = middleInitial;
		}


		public String getLastName() {
			return lastName;
		}


		public void setLastName(String lastName) {
			this.lastName = lastName;
		}


		public String getPhone() {
			return phone;
		}


		public void setPhone(String phone) {
			this.phone = phone;
		}


		public String getEmail() {
			return email;
		}


		public void setEmail(String email) {
			this.email = email;
		}


		public String getShipToCompanyName() {
			return shipToCompanyName;
		}


		public void setShipToCompanyName(String shipToCompanyName) {
			this.shipToCompanyName = shipToCompanyName;
		}


		public String getShippingFee() {
			return shippingFee;
		}


		public void setShippingFee(String shippingFee) {
			this.shippingFee = shippingFee;
		}


		public String getErrorMsg() {
			return errorMsg;
		}


		public void setErrorMsg(String errorMsg) {
			this.errorMsg = errorMsg;
		}


		public String getChannelId() {
			return channelId;
		}


		public void setChannelId(String channelId) {
			this.channelId = channelId;
		}


		public String getAcceptPartial() {
			return acceptPartial;
		}


		public void setAcceptPartial(String acceptPartial) {
			this.acceptPartial = acceptPartial;
		}


		public String getOrderType() {
			return orderType;
		}


		public void setOrderType(String orderType) {
			this.orderType = orderType;
		}


		public String getParentOid() {
			return parentOid;
		}


		public void setParentOid(String parentOid) {
			this.parentOid = parentOid;
		}


		public String getOrderSource() {
			return orderSource;
		}


		public void setOrderSource(String orderSource) {
			this.orderSource = orderSource;
		}


		public String getFulfillmentType() {
			return fulfillmentType;
		}


		public void setFulfillmentType(String fulfillmentType) {
			this.fulfillmentType = fulfillmentType;
		}


		public String getQuantity() {
			return quantity;
		}


		public void setQuantity(String quantity) {
			this.quantity = quantity;
		}


		public String getLineItemId() {
			return lineItemId;
		}


		public void setLineItemId(String lineItemId) {
			this.lineItemId = lineItemId;
		}


	
		
		


		@Override
		public String toString() {
			return "OrderDTO [orderId=" + orderId + ", partnerId=" + partnerId + ", partnerName=" + partnerName
					+ ", issuerId=" + issuerId + ", issuerName=" + issuerName + ", merchantId=" + merchantId
					+ ", merchantName=" + merchantName + ", locationId=" + locationId + ", locationName=" + locationName
					+ ", productId=" + productId + ", productName=" + productName + ", insDate=" + insDate
					+ ", defaultCardStatus=" + defaultCardStatus + ", postBackResponse=" + postBackResponse
					+ ", postBackUrl=" + postBackUrl + ", activationCode=" + activationCode + ", checkerRemarks="
					+ checkerRemarks + ", shippingMethod=" + shippingMethod + ", status=" + status + ", addressLine1="
					+ addressLine1 + ", addressLine2=" + addressLine2 + ", addressLine3=" + addressLine3 + ", city="
					+ city + ", state=" + state + ", postalCode=" + postalCode + ", country=" + country + ", firstname="
					+ firstname + ", middleInitial=" + middleInitial + ", lastName=" + lastName + ", phone=" + phone
					+ ", email=" + email + ", shipToCompanyName=" + shipToCompanyName + ", shippingFee=" + shippingFee
					+ ", errorMsg=" + errorMsg + ", channelId=" + channelId + ", acceptPartial=" + acceptPartial
					+ ", orderType=" + orderType + ", parentOid=" + parentOid + ", orderSource=" + orderSource
					+ ", fulfillmentType=" + fulfillmentType + ", packageId=" + packageId + ", insUser=" + insUser
					+ ", orderPartnerId=" + Arrays.toString(orderPartnerId) + ", quantity=" + quantity + ", userName="
					+ userName + ", lineItemId=" + lineItemId + "]";
		}


		public String getUserName() {
			return userName;
		}


		public void setUserName(String userName) {
			this.userName = userName;
		}


		public OrderDTO(String orderId, Long partnerId, String partnerName, Long issuerId, String issuerName,
				String merchantId, String merchantName, Long locationId, String locationName, Long productId,
				String productName, Date insDate, String defaultCardStatus, String status, String orderType,
				String packageId, String quantity, long insUser,String lineItemId) {
			super();
			this.orderId = orderId;
			this.partnerId = partnerId;
			this.partnerName = partnerName;
			this.issuerId = issuerId;
			this.issuerName = issuerName;
			this.merchantId = merchantId;
			this.merchantName = merchantName;
			this.locationId = locationId;
			this.locationName = locationName;
			this.productId = productId;
			this.productName = productName;
			this.insDate = insDate;
			this.defaultCardStatus = defaultCardStatus;
			this.status = status;
			this.orderType = orderType;
			this.packageId = packageId;
			this.quantity = quantity;
			this.insUser = insUser;
			this.lineItemId = lineItemId;
		}
		
		
		public OrderDTO(String orderId, Long partnerId, String partnerName, Long issuerId, String issuerName,
				String merchantId, String merchantName, Long locationId, String locationName, Long productId,
				String productName, Date insDate, String defaultCardStatus, String status, String orderType,
				String packageId, String quantity, String userName,String lineItemId) {
			super();
			this.orderId = orderId;
			this.partnerId = partnerId;
			this.partnerName = partnerName;
			this.issuerId = issuerId;
			this.issuerName = issuerName;
			this.merchantId = merchantId;
			this.merchantName = merchantName;
			this.locationId = locationId;
			this.locationName = locationName;
			this.productId = productId;
			this.productName = productName;
			this.insDate = insDate;
			this.defaultCardStatus = defaultCardStatus;
			this.status = status;
			this.orderType = orderType;
			this.packageId = packageId;
			this.quantity = quantity;
			this.userName = userName;
			this.lineItemId = lineItemId;
			
		}
		public OrderDTO(String orderId, Long partnerId, String partnerName, Long issuerId, String issuerName,
				String merchantId, String merchantName, Long locationId, String locationName, Long productId,
				String productName, Date insDate, String defaultCardStatus, String status, String orderType,
				String packageId, String quantity, String userName,String lineItemId,Long insUser) {
			super();
			this.orderId = orderId;
			this.partnerId = partnerId;
			this.partnerName = partnerName;
			this.issuerId = issuerId;
			this.issuerName = issuerName;
			this.merchantId = merchantId;
			this.merchantName = merchantName;
			this.locationId = locationId;
			this.locationName = locationName;
			this.productId = productId;
			this.productName = productName;
			this.insDate = insDate;
			this.defaultCardStatus = defaultCardStatus;
			this.status = status;
			this.orderType = orderType;
			this.packageId = packageId;
			this.quantity = quantity;
			this.userName = userName;
			this.lineItemId = lineItemId;
			this.insUser = insUser;
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
     
	public OrderDTO(String orderId, Long partnerId, String partnerName, Long issuerId, String issuerName,
			String merchantId, String merchantName, Long locationId, String locationName, Long productId,
			String productName, Date insDate, String defaultCardStatus, String status, String orderType,
			String packageId, String quantity, long insUser,String lineItemId,String lineItemIdStatus,String ccfFileName,
			String startSerialNo,String endSerialNo) {
		super();
		this.orderId = orderId;
		this.partnerId = partnerId;
		this.partnerName = partnerName;
		this.issuerId = issuerId;
		this.issuerName = issuerName;
		this.merchantId = merchantId;
		this.merchantName = merchantName;
		this.locationId = locationId;
		this.locationName = locationName;
		this.productId = productId;
		this.productName = productName;
		this.insDate = insDate;
		this.defaultCardStatus = defaultCardStatus;
		this.status = status;
		this.orderType = orderType;
		this.packageId = packageId;
		this.quantity = quantity;
		this.insUser = insUser;
		this.lineItemId = lineItemId;
		this.lineItemIdStatus = lineItemIdStatus;
		this.ccfFileName =ccfFileName;
		this.startSerialNo = startSerialNo;
		this.endSerialNo = endSerialNo;
		}
     

}
