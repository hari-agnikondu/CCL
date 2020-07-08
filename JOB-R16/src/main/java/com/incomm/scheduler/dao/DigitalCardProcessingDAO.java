package com.incomm.scheduler.dao;

import java.util.List;
import java.util.Map;

public interface DigitalCardProcessingDAO {

	int getAvailableCardCountForProd(String productId);

	int getIssuedCardCountForProd(String productId);

	String processOrder(String orderId);

	List<Map<String, Object>> getAllDigitalProducts();

}
