package com.incomm.scheduler.service;

import java.util.Map;

public interface ManualOrderService {

	public String getManualOrder(String orderId,String lineItemId,Long partnerId);

	public void processMailForOrder(String payLoad, Map<String, String> headers) ;
}
