package com.incomm.scheduler.model;

import org.springframework.core.io.Resource;


public class CnShipmentFile extends BaseResourceAware
{
	String magicNumber;
	String status;
	String carrier;
	String date;
	String trackingNumber;
	String merchantID;
	String merchantName;
	String storelocationID;
	String batchNumber;
	String caseNumber;
	String palletNumber;
	String serialNumber;
	String shipTo;
	String streetAddress1;
	String streetAddress2;
	String city;
	String state;
	String zip;
	String dCMSID;
	String prodID;
	String orderID;
	String parentSerialNumber;
	String fileName;
	
	public String getFileName() {
		return getResource().getFilename();
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public CnShipmentFile()
	{
		
	}
	
	public CnShipmentFile(Resource resource){
		this.setResource(resource);
	}
	

	/**
	 * @return the magic_Number
	 */
	public String getMagicNumber() {
		return magicNumber;
	}
	/**
	 * @param magic_Number the magic_Number to set
	 */
	public void setMagicNumber(String magicNumber) {
		this.magicNumber = magicNumber;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the carrier
	 */
	public String getCarrier() {
		return carrier;
	}
	/**
	 * @param carrier the carrier to set
	 */
	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}
	/**
	 * @return the tracking_Number
	 */
	public String getTrackingNumber() {
		return trackingNumber;
	}
	/**
	 * @param tracking_Number the tracking_Number to set
	 */
	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}
	/**
	 * @return the merchant_ID
	 */
	public String getMerchantID() {
		return merchantID;
	}
	/**
	 * @param merchant_ID the merchant_ID to set
	 */
	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}
	/**
	 * @return the merchant_Name
	 */
	public String getMerchantName() {
		return merchantName;
	}
	/**
	 * @param merchant_Name the merchant_Name to set
	 */
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	/**
	 * @return the storelocationID
	 */
	public String getStorelocationID() {
		return storelocationID;
	}
	/**
	 * @param storelocationID the storelocationID to set
	 */
	public void setStorelocationID(String storelocationID) {
		this.storelocationID = storelocationID;
	}
	/**
	 * @return the batch_Number
	 */
	public String getBatchNumber() {
		return batchNumber;
	}
	/**
	 * @param batch_Number the batch_Number to set
	 */
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}
	/**
	 * @return the case_Number
	 */
	public String getCaseNumber() {
		return caseNumber;
	}
	/**
	 * @param case_Number the case_Number to set
	 */
	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}
	/**
	 * @return the pallet_Number
	 */
	public String getPalletNumber() {
		return palletNumber;
	}
	/**
	 * @param pallet_Number the pallet_Number to set
	 */
	public void setPalletNumber(String palletNumber) {
		this.palletNumber = palletNumber;
	}
	/**
	 * @return the serial_Number
	 */
	public String getSerialNumber() {
		return serialNumber;
	}
	/**
	 * @param serial_Number the serial_Number to set
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	/**
	 * @return the ship_To
	 */
	public String getShipTo() {
		return shipTo;
	}
	/**
	 * @param ship_To the ship_To to set
	 */
	public void setShipTo(String shipTo) {
		this.shipTo = shipTo;
	}
	/**
	 * @return the street_Address1
	 */
	public String getStreetAddress1() {
		return streetAddress1;
	}
	/**
	 * @param street_Address1 the street_Address1 to set
	 */
	public void setStreetAddress1(String streetAddress1) {
		this.streetAddress1 = streetAddress1;
	}
	/**
	 * @return the street_Address2
	 */
	public String getStreetAddress2() {
		return streetAddress2;
	}
	/**
	 * @param street_Address2 the street_Address2 to set
	 */
	public void setStreetAddress2(String streetAddress2) {
		this.streetAddress2 = streetAddress2;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}
	/**
	 * @param zip the zip to set
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}
	/**
	 * @return the dCMS_ID
	 */
	public String getdCMSID() {
		return dCMSID;
	}
	/**
	 * @param dCMS_ID the dCMS_ID to set
	 */
	public void setdCMSID(String dCMSID) {
		this.dCMSID = dCMSID;
	}
	/**
	 * @return the prod_ID
	 */
	public String getProdID() {
		return prodID;
	}
	/**
	 * @param prod_ID the prod_ID to set
	 */
	public void setProdID(String prodID) {
		this.prodID = prodID;
	}
	/**
	 * @return the order_ID
	 */
	public String getOrderID() {
		return orderID;
	}
	/**
	 * @param order_ID the order_ID to set
	 */
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	/**
	 * @return the parent_Serial_Number
	 */
	public String getParentSerialNumber() {
		return parentSerialNumber;
	}
	/**
	 * @param parent_Serial_Number the parent_Serial_Number to set
	 */
	public void setParentSerialNumber(String parentSerialNumber) {
		this.parentSerialNumber = parentSerialNumber;
	}
	
	public String getFileRecode(){
		StringBuilder builder=new StringBuilder();
		builder.append(getMagicNumber());
		builder.append(",");
		builder.append(getStatus());
		builder.append(",");
		builder.append(getCarrier());
		builder.append(",");
		builder.append(getDate());
		builder.append(",");
		builder.append(getTrackingNumber());
		builder.append(",");
		builder.append(getMerchantID());
		builder.append(",");
		builder.append(getMerchantName());
		builder.append(",");
		builder.append(getStorelocationID());
		builder.append(",");
		builder.append(getBatchNumber());
		builder.append(",");
		builder.append(getCaseNumber());
		builder.append(",");
		builder.append(getPalletNumber());
		builder.append(",");
		builder.append(getSerialNumber());
		builder.append(",");
		builder.append(getShipTo());
		builder.append(",");
		builder.append(getStreetAddress1());
		builder.append(",");
		builder.append(getStreetAddress2());
		builder.append(",");
		builder.append(getCity());
		builder.append(",");
		builder.append(getState());
		builder.append(",");
		builder.append(getZip());
		builder.append(",");
		builder.append(getdCMSID());
		builder.append(",");
		builder.append(getProdID());
		builder.append(",");
		builder.append(getOrderID());
		builder.append(",");
		builder.append(getParentSerialNumber());
		return builder.toString();
	}
	
	  @Override
	    public String toString() {
	        StringBuilder builder = new StringBuilder();
	        builder.append("CN File Process [magic_Number=");
	        builder.append(magicNumber);
	        builder.append(", status=");
	        builder.append(status);
	        builder.append(", carrier=");
	        builder.append(carrier);
	        builder.append(", date=");
	        builder.append(date);
	        builder.append(", tracking_Number=");
	        builder.append(trackingNumber);
	        builder.append(", merchant_ID=");
	        builder.append(merchantID);
	        builder.append(", merchant_Name=");
	        builder.append(merchantName);
	        builder.append(", storelocationID=");
	        builder.append(storelocationID);
	        builder.append(", batch_Number=");
	        builder.append(batchNumber);
	        builder.append(", case_Number=");
	        builder.append(caseNumber);
	        builder.append(", pallet_Number=");
	        builder.append(palletNumber);
	        builder.append(", serial_Number=");
	        builder.append(serialNumber);
	        builder.append(", ship_To=");
	        builder.append(shipTo);
	        builder.append(", street_Address1=");
	        builder.append(streetAddress1);
	        builder.append(", street_Address2=");
	        builder.append(streetAddress2);
	        builder.append(", city=");
	        builder.append(city);
	        builder.append(", state=");
	        builder.append(state);
	        builder.append(", zip=");
	        builder.append(zip);
	        builder.append(", dCMS_ID=");
	        builder.append(dCMSID);
	        builder.append(", prod_ID=");
	        builder.append(prodID);
	        builder.append(", order_ID=");
	        builder.append(orderID);
	        builder.append(", parent_Serial_Number=");
	        builder.append(parentSerialNumber);
	        builder.append(", fileName=");
	        builder.append(getFileName());
	        builder.append("]");
	        return builder.toString();
	    }
	
}
