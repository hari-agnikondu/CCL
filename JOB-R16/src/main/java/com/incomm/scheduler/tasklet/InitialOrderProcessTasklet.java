package com.incomm.scheduler.tasklet;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.incomm.scheduler.dao.OrderProcessingDAO;

public class InitialOrderProcessTasklet {
	
	private static final Logger logger = LogManager.getLogger(InitialOrderProcessTasklet.class);
	private final OrderProcessingDAO orderprocessingdao;

	public InitialOrderProcessTasklet(OrderProcessingDAO orderprocessingdao) {
		this.orderprocessingdao = orderprocessingdao;
	}

	public void getOrderID() {
		
		List orderlist= orderprocessingdao.getOrderIdList();
		logger.debug(orderlist);

		Iterator iterator = orderlist.iterator();
	
		while(iterator.hasNext()) {
			 Map map = (Map)iterator.next();
			String orderId =  (String) map.get("ORDER_ID"); 
			String orderLineItemId =   (String) map.get("LINE_ITEM_ID"); 
			orderprocessingdao.callOrderProcedure(orderId,orderLineItemId);
		}
			
		
		
	}

}
