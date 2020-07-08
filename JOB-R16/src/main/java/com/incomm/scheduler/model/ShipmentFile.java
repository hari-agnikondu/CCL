package com.incomm.scheduler.model;



public class ShipmentFile extends BaseResourceAware
{
	String customerDesc;
	String sourceOneBatch;
	String parentOrderId;
	String childOrderId;
	String fileDate;
	String serialNumber;
	String cards;
	String packageId;
	String cardType;
	String contactName;
	String shipto;
	String address1;
	String address2;
	String city;
	String state;
	String zip;
	String trackingNumber;
	String shipDate;
	String shipmentId;
	String shipMethod;
	String fileName;

     

	public String getCustomerDesc() {
		return customerDesc;
	}

	public void setCustomerDesc(String customerDesc) {
		this.customerDesc = customerDesc;
	}

	public String getSourceOneBatch() {
		return sourceOneBatch;
	}

	public void setSourceOneBatch(String sourceOneBatch) {
		this.sourceOneBatch = sourceOneBatch;
	}

	public String getParentOrderId() {
		return parentOrderId;
	}

	public void setParentOrderId(String parentOrderId) {
		this.parentOrderId = parentOrderId;
	}

	public String getChildOrderId() {
		return childOrderId;
	}

	public void setChildOrderId(String childOrderId) {
		this.childOrderId = childOrderId;
	}

	public String getFileDate() {
		return fileDate;
	}

	public void setFileDate(String fileDate) {
		this.fileDate = fileDate;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getCards() {
		return cards;
	}

	public void setCards(String cards) {
		this.cards = cards;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getShipto() {
		return shipto;
	}

	public void setShipto(String shipto) {
		this.shipto = shipto;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
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

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public String getShipDate() {
		return shipDate;
	}

	public void setShipDate(String shipDate) {
		this.shipDate = shipDate;
	}

	public String getShipmentId() {
		return shipmentId;
	}

	public void setShipmentId(String shipmentId) {
		this.shipmentId = shipmentId;
	}

	public String getShipMethod() {
		return shipMethod;
	}

	public void setShipMethod(String shipMethod) {
		this.shipMethod = shipMethod;
	}

	public String getFileName() {
		return getResource().getFilename();
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileRecode() {
		StringBuilder builder = new StringBuilder();
		builder.append(getCustomerDesc());
		builder.append(",");
		builder.append(getSourceOneBatch());
		builder.append(",");
		builder.append(getParentOrderId());
		builder.append(",");
		builder.append(getChildOrderId());
		builder.append(",");
		builder.append(getFileDate());
		builder.append(",");
		builder.append(getSerialNumber());
		builder.append(",");
		builder.append(getCards());
		builder.append(",");
		builder.append(getPackageId());
		builder.append(",");
		builder.append(getCardType());
		builder.append(",");
		builder.append(getContactName());
		builder.append(",");
		builder.append(getShipto());
		builder.append(",");
		builder.append(getAddress1());
		builder.append(",");
		builder.append(getAddress2());
		builder.append(",");
		builder.append(getCity());
		builder.append(",");
		builder.append(getState());
		builder.append(",");
		builder.append(getZip());
		builder.append(",");
		builder.append(getTrackingNumber());
		builder.append(",");
		builder.append(getShipDate());
		builder.append(",");
		builder.append(getShipmentId());
		builder.append(",");
		builder.append(getShipMethod());
		builder.append(",");
		return builder.toString();
	}

	@Override
	public String toString() {
		return "ShipmentFile [customer_Desc=" + customerDesc
				+ ", source_One_Batch=" + sourceOneBatch
				+ ", parent_Order_Id=" + parentOrderId + ", child_Order_Id="
				+ childOrderId + ", file_Date=" + fileDate
				+ ", serialNumber=" + serialNumber + ", cards=" + cards
				+ ", package_Id=" + packageId + ", card_Type=" + cardType
				+ ", contact_Name=" + contactName + ", ship_to=" + shipto
				+ ", address_1=" + address1 + ", address_2=" + address2
				+ ", city=" + city + ", state=" + state + ", zip=" + zip
				+ ", tracking_Number=" + trackingNumber + ", ship_Date="
				+ shipDate + ", shipment_Id=" + shipmentId + ", ship_Method="
				+ shipMethod + "]";
	}
}
