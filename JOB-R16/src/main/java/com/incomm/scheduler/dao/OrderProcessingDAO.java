package com.incomm.scheduler.dao;

import java.util.List;

public interface OrderProcessingDAO {

	public List<Object> getOrderIdList();
	public String callOrderProcedure(String orderId, String orderLineItemId);
	public String generateManualOrder(String orderId, String lineItemId,Long partnerId);
	public void updateOrderStatus(String orderId, String lineItemId, Long partnerId);

}
