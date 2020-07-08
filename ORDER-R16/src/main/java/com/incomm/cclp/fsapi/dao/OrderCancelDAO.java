package com.incomm.cclp.fsapi.dao;

import java.util.Map;

public interface OrderCancelDAO {

	Map<String, Object> cancelOrder(Map<String, Object> valueHashMap);

	Map<String, Object> cancelOrderProcess(Map<String, Object> valueHashMap);

	void getOrderStatus(Map<String, Object> valueHashMap);

}
