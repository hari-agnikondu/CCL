package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="ORDER_DETAILS")
//@NamedNativeQuery(name="Order.findAll", query="SELECT o FROM order o")
@SecondaryTable(name="ORDER_LINE_ITEM", pkJoinColumns= {
@PrimaryKeyJoinColumn(name="ORDER_ID", referencedColumnName="ORDER_ID"),
@PrimaryKeyJoinColumn(name="PARTNER_ID", referencedColumnName="PARTNER_ID")/*,
@PrimaryKeyJoinColumn(name="LINE_ITEM_ID", referencedColumnName="LINE_ITEM_ID")*/}
)
public class Order implements Serializable{


	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ORDER_ID")
	private String orderId;		
	
	
	@Column(name="PARTNER_ID",table="ORDER_DETAILS",updatable=false,insertable=false)
	private long partnerId;
	
	@Id
	@Column(name="PARTNER_ID", table="ORDER_LINE_ITEM")
	private long lineItempartnerId;
	
	@Column(name="ISSUER_ID")
	private long issuerId;

	@Column(name="MERCHANT_ID")
	private String merchantId;
	
	@Column(name="LOCATION_ID")
	private Long locationId;
	
	@Column(name="ORDER_DEFAULT_CARD_STATUS")
	private String defaultCardStatus;
	
	@Column(name="POSTBACK_RESPONSE")
	private String postBackResponse;
	
	@Column(name="POSTBACK_URL")
	private String postBackUrl;
	
	@Column(name="ACTIVATION_CODE")
	private String activationCode;
	
	@Column(name="SHIPPING_METHOD")
	private String shippingMethod;
	
	@Column(name="ORDER_STATUS")
	private String orderStatus;
	
	@Column(name="ADDRESS_LINE1")
	private String addressLine1;
	
	@Column(name="ADDRESS_LINE2")
	private String addressLine2;
	
	@Column(name="ADDRESS_LINE3")
	private String addressLine3;
	
	@Column(name="CITY")
	private String city;
	
	@Column(name="STATE")
	private String state;
	
	@Column(name="POSTAL_CODE")
	private String postalCode;
	
	@Column(name="COUNTRY")
	private String country;
	
	@Column(name="FIRST_NAME")
	private String firstname;
	
	@Column(name="MIDDLE_INITIAL")
	private String middleInitial;
	
	@Column(name="LAST_NAME")
	private String lastName;
	
	@Column(name="PHONE")
	private String phone;
	
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="SHIP_TO_COMPANY_NAME")
	private String shipToCompanyName;
	
	@Column(name="SHIPPING_FEE")
	private String shippingFee;
	
	@Column(name="ERROR_MSG")
	private String errorMsg;
	
	@Column(name="CHANNEL_ID")
	private String channelId;
	
	@Column(name="ACCEPT_PARTIAL")
	private String acceptPartial;
	
	@Column(name="ORDER_TYPE")
	private String orderType;
	
	@Column(name="PARENT_OID")
	private String parentOid;
	
	@Column(name="ORDER_SOURCE")
	private String orderSource;
	
	@Column(name="FULFILLMENT_TYPE")
	private String fulfillmentType;
	
	@Column(name="PACKAGE_ID", table="ORDER_LINE_ITEM")
	private String packageId;


	@Column(name="QUANTITY", table="ORDER_LINE_ITEM")
	private String quantity;
	
	@Column(name="LINE_ITEM_ID", table="ORDER_LINE_ITEM")
	private String lineItemId;
	
	@Column(name="ORDER_STATUS", table="ORDER_LINE_ITEM")
	private String status;
	
	@Column(name="PRODUCT_ID", table="ORDER_LINE_ITEM")
	private Long productId;
	
	@Column(name="INS_USER",updatable = false )
	private long insUser;
	
	@Temporal(TemporalType.DATE)
	@Column(name="INS_DATE",updatable = false)
	private Date insDate;
	
	
	@Column(name="LAST_UPD_USER")
	private long lastUpdUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPD_DATE")
	private Date lastUpdDate;
	
	@Column(name="CHECKER_REMARKS")
	private String checkerRemarks;
	
	
	public String getCheckerRemarks() {
		return checkerRemarks;
	}

	public void setCheckerRemarks(String checkerRemarks) {
		this.checkerRemarks = checkerRemarks;
	}

	public long getIssuerId() {
		return issuerId;
	}

	public void setIssuerId(long issuerId) {
		this.issuerId = issuerId;
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

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public long getLineItempartnerId() {
		return lineItempartnerId;
	}

	public void setLineItempartnerId(long lineItempartnerId) {
		this.lineItempartnerId = lineItempartnerId;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(long partnerId) {
		this.partnerId = partnerId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
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

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
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
	
	public long getInsUser() {
		return insUser;
	}

	public Date getInsDate() {
		return insDate;
	}

	public void setInsDate() {
		this.insDate = new java.sql.Date(new java.util.Date().getTime());
	}

	public long getLastUpdUser() {
		return lastUpdUser;
	}

	public Date getLastUpdDate() {
		return lastUpdDate;
	}

	public void setLastUpdDate() {
		this.lastUpdDate = new java.sql.Date(new java.util.Date().getTime());
	}

	public void setInsUser(long insUser) {
		this.insUser = insUser;
	}

	public void setLastUpdUser(long lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}
	
}
