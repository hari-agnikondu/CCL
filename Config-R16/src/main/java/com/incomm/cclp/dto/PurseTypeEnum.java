package com.incomm.cclp.dto;

public enum PurseTypeEnum {

	CONSUMER_FUNDED_CURRENCY(1,"CONSUMER FUNDED CURRENCY"),
	POINTS(2,"POINTS"),
	SKU(3,"SKU"),
	PARTNER_FUNDED_CURRENCY(4,"PARTNER FUNDED CURRENCY");
	
	private Long purseTypeId;
	private String purseTypeName;
	
	private PurseTypeEnum(Long purseTypeId, String purseTypeName) {
		this.purseTypeId = purseTypeId;
		this.purseTypeName = purseTypeName;
	}
	
	private PurseTypeEnum(int purseTypeId, String purseTypeName) {
		this.purseTypeId = Long.valueOf(purseTypeId);
		this.purseTypeName = purseTypeName;
	}
	
	public Long getPurseTypeId() {
		return purseTypeId;
	}
	public void setPurseTypeId(Long purseTypeId) {
		this.purseTypeId = purseTypeId;
	}
	public String getPurseTypeName() {
		return purseTypeName;
	}
	public void setPurseTypeName(String purseTypeName) {
		this.purseTypeName = purseTypeName;
	}
}
