package com.incomm.scheduler.model;

public class ReturnFile extends BaseResourceAware{
	String customerDesc;
	String shipSuffixnum;
	String parentOrderId;
	String childOrderId;
	String serialNumber;
	String rejectCode;
	String rejectReason;
	String fileDate;
	String cardType;
	String clientOrderId;
	String fileName;
	String date;
	
	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getFileName() {
		return getResource().getFilename();
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	


	public String getChildOrderId() {
		return childOrderId;
	}


	public void setChildOrderId(String childOrderId) {
		this.childOrderId = childOrderId;
	}


	public ReturnFile() {
		//constructor
	}

	public String getCustomerDesc() {
		return customerDesc;
	}

	public void setCustomerDesc(String customerDesc) {
		this.customerDesc = customerDesc;
	}

	public String getShipSuffixnum() {
		return shipSuffixnum;
	}

	public void setShipSuffixnum(String shipSuffixnum) {
		this.shipSuffixnum = shipSuffixnum;
	}

	public String getParentOrderId() {
		return parentOrderId;
	}

	public void setParentOrderId(String parentOrderId) {
		this.parentOrderId = parentOrderId;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getRejectCode() {
		return rejectCode;
	}

	public void setRejectCode(String rejectCode) {
		this.rejectCode = rejectCode;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getFileDate() {
		return fileDate;
	}

	public void setFileDate(String fileDate) {
		this.fileDate = fileDate;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getClientOrderId() {
		return clientOrderId;
	}

	public void setClientOrderId(String clientOrderId) {
		this.clientOrderId = clientOrderId;
	}


	@Override
	public String toString() {
		return "ReturnFile [customer_Desc=" + customerDesc
				+ ", ship_Suffix_num=" + shipSuffixnum + ", parent_Order_Id="
				+ parentOrderId + ", child_Order_Id=" + childOrderId
				+ ", serial_Number=" + serialNumber + ", reject_Code="
				+ rejectCode + ", reject_Reason=" + rejectReason
				+ ", file_Date=" + fileDate + ", card_Type=" + cardType
				+ ", client_Order_Id=" + clientOrderId + ", fileName="
				+ fileName + ", date=" + date + "]";
	}

	public String getFileRecode(){
		StringBuilder builder=new StringBuilder();
		builder.append(getCustomerDesc());
		builder.append(",");
		builder.append(getShipSuffixnum());
		builder.append(",");
		builder.append(getParentOrderId());
		builder.append(",");
		builder.append(getChildOrderId());
		builder.append(",");
		builder.append(getSerialNumber());
		builder.append(",");
		builder.append(getRejectCode());
		builder.append(",");
		builder.append(getRejectReason());
		builder.append(",");
		builder.append(getFileDate());
		builder.append(",");
		builder.append(getCardType());
		builder.append(",");
		builder.append(getClientOrderId());
		builder.append(",");
		builder.append(getFileName());
		builder.append(",");
		return builder.toString();
	}
	
	

}
