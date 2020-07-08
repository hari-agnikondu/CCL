package com.incomm.scheduler.dao;

import java.util.List;

public interface SerialNumberRequestDAO {

	void getSerialNumber(Long productId, String upc, Long serialNumberQuantity);

	List<Long> getAllProductIds();

	void getSerialNumber(Long productId);

}
