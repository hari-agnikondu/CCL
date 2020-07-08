package com.incomm.cclp.dto;

import java.util.Map;

public class ValueDTO {

	private Map<String, String> valueObj;

	private Map<String, Map<String, Object>> cardAttributes;

	private Map<String, Object> usageLimit;

	private Map<String, Object> usageFee;

	private Map<String, Map<String, Object>> productAttributes;

	public AccountPurseDTO accountPurseDto;

	public AccountPurseDTO getAccountPurseDto() {
		return accountPurseDto;
	}

	public void setAccountPurseDto(AccountPurseDTO accountPurseDto) {
		this.accountPurseDto = accountPurseDto;
	}

	public Map<String, Map<String, Object>> getCardAttributes() {
		return cardAttributes;
	}

	public void setCardAttributes(Map<String, Map<String, Object>> cardAttributes) {
		this.cardAttributes = cardAttributes;
	}

	public Map<String, Object> getUsageLimit() {
		return usageLimit;
	}

	public void setUsageLimit(Map<String, Object> usageLimit) {
		this.usageLimit = usageLimit;
	}

	public Map<String, Object> getUsageFee() {
		return usageFee;
	}

	public void setUsageFee(Map<String, Object> usageFee) {
		this.usageFee = usageFee;
	}

	public Map<String, Map<String, Object>> getProductAttributes() {
		return productAttributes;
	}

	public void setProductAttributes(Map<String, Map<String, Object>> productAttributes) {
		this.productAttributes = productAttributes;
	}

	public Map<String, String> getValueObj() {
		return valueObj;
	}

	public void setValueObj(Map<String, String> valueObj) {
		this.valueObj = valueObj;
	}

	public ValueDTO() {
		// constructor
	}

}
