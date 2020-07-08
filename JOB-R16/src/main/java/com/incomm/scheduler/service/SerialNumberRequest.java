package com.incomm.scheduler.service;

public interface SerialNumberRequest {

	public String getSerialNumber(Long productId, String upc, Long serialNumberQuantity);

}
