package com.incomm.scheduler.serviceimpl;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.controller.OrderProcessController;
import com.incomm.scheduler.dao.OrderProcessingDAO;
import com.incomm.scheduler.exception.ServiceException;
import com.incomm.scheduler.notification.NotificationService;
import com.incomm.scheduler.service.ManualOrderService;
import com.incomm.scheduler.utils.JsonHelper;
import com.incomm.scheduler.utils.Util;

@Service
public class ManualOrderServiceImpl implements ManualOrderService {

	@Autowired
	OrderProcessingDAO orderDAO;

	@Autowired
	NotificationService notificationService;

	// the Logger
	private static final Logger logger = LogManager.getLogger(OrderProcessController.class);

	public String getManualOrder(String orderId, String lineItemId, Long partnerId) {

		logger.info(CCLPConstants.ENTER);
		String result = orderDAO.generateManualOrder(orderId, lineItemId, partnerId);
		logger.info("Generate Mannual Order Result: {}", result);
		if (!Util.isEmpty(result) && result.equalsIgnoreCase("ok")) {
			logger.info("updating status for orderId: {}, lineItemId: {} and partnerId: {}", orderId, lineItemId,
					partnerId);
			orderDAO.updateOrderStatus(orderId, lineItemId, partnerId);
		}

		logger.info(CCLPConstants.EXIT);
		return result;

	}

	@Override
	public void processMailForOrder(String payLoad, Map<String, String> reqHeaders) {

		logger.info("processMailForOrder process Started");
		Map<String, Object> valuHashMap = new HashMap<>();
		String orderID = "";
		String userId = "";
		String reqHeader = "";
		String subject = "";
		String message = "";

		try {
			reqHeader = new JSONObject(reqHeaders).toString();
			logger.info("reqHeader" + reqHeader);
			JSONObject jsonObj = JsonHelper.isJSONValid(payLoad);
			if (jsonObj != null) {
				valuHashMap = JsonHelper.jsonToMap(jsonObj);
			}
			orderID = String.valueOf(valuHashMap.get("orderID"));
			logger.info("orderID" + orderID);
			userId = String.valueOf(valuHashMap.get("insUser"));
			subject = String.valueOf(valuHashMap.get("subject"));
			message = String.valueOf(valuHashMap.get("message"));

			logger.info("Entered in to notification Service" + orderID + subject + message);
			notificationService.sendEmailNotification(userId, subject, message);
			logger.info("Completed notification Service" + orderID + subject + message);

		} catch (ServiceException e) {
			logger.error("ServiceException occured while process ORDER Generation:  {} ", e.getMessage());
			notificationService.sendEmailNotification(userId, subject,
					"Order Generation Failed for OrderID -" + orderID);
		}
		logger.info("processMailForOrder process Ended");
	}

}
