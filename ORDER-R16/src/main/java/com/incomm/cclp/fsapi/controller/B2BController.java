package com.incomm.cclp.fsapi.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.cclp.constants.GeneralConstants;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.fsapi.constants.FSAPIConstants;
import com.incomm.cclp.fsapi.service.B2BService;
import com.incomm.cclp.fsapi.service.OrderCancelService;
import com.incomm.cclp.util.Util;

@RestController
@RequestMapping("/b2b")
public class B2BController {
	@Autowired
	B2BService b2bService;
	@Autowired
	OrderCancelService orderCancelService;

	private final Logger logger = LogManager.getLogger(this.getClass());

	@RequestMapping(value = "/orders", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> orderProcess(@RequestHeader Map<String, String> headers, @RequestBody String payLoad)throws ServiceException {
		Map<String, Object> valueObj = new HashMap<>();
		valueObj.put(ValueObjectKeys.REQUEST, payLoad);
		valueObj.put(ValueObjectKeys.API_NAME, GeneralConstants.ORDER);
		valueObj.put(ValueObjectKeys.REQUEST_METHOD_TYPE, GeneralConstants.POST);
		ResponseEntity<Object> responseMsg = b2bService.orderProcess(headers, valueObj);
		logger.info("Response from Order fdetails {}" , responseMsg);
		return responseMsg;
	}
	
	@RequestMapping(value ="/orders/{OrderID}/status", method = RequestMethod.GET)
	public ResponseEntity<Object> orderStatus(@RequestHeader Map<String, String> headers ,final @PathVariable("OrderID") String orderId,
			@RequestParam(value = "lineItemID", defaultValue = "null", required = false) final String lineItemIds) {
		logger.info("Header of order status{ }" + headers);
		Map<String, Object> valueObj = new HashMap<>();
		Set<String> lineItemSet = null;

		if(!Util.isEmpty(lineItemIds))
			lineItemSet = Util.csvToSet(lineItemIds);
		
		valueObj.put(FSAPIConstants.LINEITEMID, lineItemSet);
		valueObj.put(FSAPIConstants.ORDERID, orderId);
		valueObj.putAll(headers);
		valueObj.put(ValueObjectKeys.API_NAME, FSAPIConstants.ORDER_STATUS_API);
		valueObj.put(ValueObjectKeys.REQUEST_METHOD_TYPE, FSAPIConstants.GET);
		ResponseEntity<Object> responseMsg = b2bService.orderStatusProcess(valueObj);
		logger.info("Response from orderStatus {}" , responseMsg);
		return responseMsg;

	}
	
	@RequestMapping(value =FSAPIConstants.ORDER_ACTIVATION_API_URL, method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> orderActivation(@RequestHeader Map<String, String> headers,
			final @PathVariable("OrderID") String orderId,
			final @RequestBody String payLoad) throws ServiceException 
	{
		Map<String, Object> valueObj = new HashMap<>();
		valueObj.put(FSAPIConstants.ORDERID, orderId);
		valueObj.putAll(headers);
		valueObj.put(ValueObjectKeys.API_NAME, FSAPIConstants.ORDER_ACTIVATION_API);
		valueObj.put(ValueObjectKeys.REQUEST, payLoad);
		valueObj.put(ValueObjectKeys.REQUEST_METHOD_TYPE, RequestMethod.PUT);
		ResponseEntity<Object> responseMsg = b2bService.orderActivation(valueObj,headers);
		logger.info("Response from orderActivation {}" , responseMsg);
		return responseMsg;
	}
	
	@RequestMapping(value =FSAPIConstants.CANCEL_ORDER_URL, method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> orderCancellation(@RequestHeader Map<String, String> headers , @PathVariable("orderid") String orderId) {
 		Map<String, Object> valueObj = new HashMap<>();
		valueObj.put(FSAPIConstants.ORDERID, orderId);
		valueObj.putAll(headers);
		valueObj.put(ValueObjectKeys.API_NAME, FSAPIConstants.CANCEL_ORDER_API);
		valueObj.put(ValueObjectKeys.REQUEST_METHOD_TYPE, FSAPIConstants.PUT);
		ResponseEntity<Object> responseMsg = orderCancelService.orderCancelService(valueObj,headers);
		logger.info("Response from orderCancellation {}" , responseMsg);
		return responseMsg;

	}
	

}
