package com.incomm.scheduler.model;

public class SerialNumberFile extends BaseResourceAware {
	
	private String fileName;
	private Long productId;
	private String serialNumber;
	private String van16;
	public String getFileName() {
		return getResource().getFilename();
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getVan16() {
		return van16;
	}
	public void setVan16(String van16) {
		this.van16 = van16;
	}
	@Override
	public String toString() {
		return "SerialNumberFile [file_Name=" + fileName + ", product_Id="
				+ productId + ", serial_Number=" + serialNumber + ", van16="
				+ van16 + "]";
	}
	
	public String getFileRecode() {
		StringBuilder builder = new StringBuilder();
		builder.append(getProductId());
		builder.append(",");
		builder.append(getSerialNumber());
		builder.append(",");
		builder.append(getVan16());
		builder.append(",");
		
		return builder.toString();
	} 

}
