package com.incomm.cclp.fsapi.controller;

import java.util.HashMap;
import java.util.Map;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.fsapi.constants.FSAPIConstants;
import com.incomm.cclp.fsapi.service.B2BService;
import com.incomm.cclp.fsapi.service.CardReplacementRenewalService;
import com.incomm.cclp.fsapi.service.CardStatusInquiryService;
import com.incomm.cclp.fsapi.service.ProxyNumberActivationService;
import com.incomm.cclp.fsapi.service.SerialNumberActivationService;
import com.incomm.cclp.fsapi.service.VirtualCardValidationService;

@RestController
@RequestMapping("/b2b/cards")
public class B2BCardsController {
	
	@Autowired
	VirtualCardValidationService virtualCardValidationService;
	
	@Autowired
	SerialNumberActivationService serialNumberActivationService;
	
	@Autowired
	CardReplacementRenewalService cardReplacementRenewalService;
	
	@Autowired
	B2BService b2bService;
	
	@Autowired
	ProxyNumberActivationService proxyNumberActivationService;
	@Autowired
	CardStatusInquiryService cardStatusInquiryService;
	
	private final Logger logger = LogManager.getLogger(this.getClass());

	@RequestMapping(value ="/status", method = RequestMethod.GET)
	public ResponseEntity<Object> cardStatus(@RequestHeader Map<String, String> headers ,final @RequestParam("criteria") String criteria,
			final @RequestParam("value") String value, final @RequestParam("type") String type,
			final @RequestParam("start") String start,final @RequestParam("end") String end) {

		Map<String, Object> valueObj = new HashMap<>();

		valueObj.put(FSAPIConstants.TYPE, type);
		valueObj.put(FSAPIConstants.CARDSTAT_START, start);
		valueObj.put(FSAPIConstants.CARDSTAT_END, end);
		valueObj.put(FSAPIConstants.CARDSTAT_CRITERIA, criteria);
		valueObj.put(FSAPIConstants.CARDSTAT_VALUE, value);
		valueObj.putAll(headers);
		valueObj.put(ValueObjectKeys.API_NAME, FSAPIConstants.FSAPI_CARDSTATUS_INQUIRY);
		valueObj.put(ValueObjectKeys.REQUEST_METHOD_TYPE, FSAPIConstants.GET);
		ResponseEntity<Object> responseMsg = cardStatusInquiryService.cardStatusInquiry(valueObj);

		logger.debug("Response from Order fdetails {}" , responseMsg);
		return responseMsg;

	}
	
	
	@RequestMapping(value ="/{serialnumber}/serialnumberactivation", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> serialNumberActivation(@RequestHeader Map<String, String> headers,final @PathVariable("serialnumber") String serialnumber, @RequestBody String orderReq) throws ServiceException{

		Map<String, Object> valueObj = new HashMap<>();
		
		valueObj.put("serialnumber", serialnumber);
		valueObj.put(ValueObjectKeys.REQUEST, orderReq);
		valueObj.put(ValueObjectKeys.API_NAME, FSAPIConstants.SERIALNUMBER_VALIDATION_API);
		valueObj.put(ValueObjectKeys.REQUEST_METHOD_TYPE, FSAPIConstants.PUT);
	
		ResponseEntity<Object> responseMsg = serialNumberActivationService.serialNumberActivationProcess(headers,valueObj);

		logger.debug("Response from serial number details {}" , responseMsg);
		return responseMsg;

	}
	
	

	@RequestMapping(value ="/{proxynumber}/proxynumberactivation", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> proxyNumberActivation(@RequestHeader Map<String, String> headers,final @PathVariable("proxynumber") String proxynumber, @RequestBody String orderReq) throws ServiceException{

		Map<String, Object> valueObj = new HashMap<>();
		
		valueObj.put("proxynumber", proxynumber);
		valueObj.put(ValueObjectKeys.REQUEST, orderReq);
		valueObj.put(ValueObjectKeys.API_NAME, FSAPIConstants.PROXYNUMBER_VALIDATION_API);
		valueObj.put(ValueObjectKeys.REQUEST_METHOD_TYPE, FSAPIConstants.PUT);
		ResponseEntity<Object> responseMsg = proxyNumberActivationService.proxyNumberActivationProcess(headers, valueObj);

		logger.debug("Response from proxy number details {}" , responseMsg);
		return responseMsg;

	}
	
	@RequestMapping(value =FSAPIConstants.SERIALNUMBER_RANGE_ACTIVATION_API_URL, method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> serialNumberRangeActivation(@RequestHeader Map<String, String> headers,@RequestBody String payLoad) throws ServiceException 
	{
		Map<String, Object> valueObj = new HashMap<>();
		valueObj.putAll(headers);
		valueObj.put(ValueObjectKeys.API_NAME, FSAPIConstants.SERIALNUMBER_RANGE_VALIDATION_API);
		valueObj.put(ValueObjectKeys.REQUEST, payLoad);
		valueObj.put(ValueObjectKeys.REQUEST_METHOD_TYPE, RequestMethod.PUT);
		ResponseEntity<Object> responseMsg = b2bService.serialNumberRangeActivation(valueObj,headers);
		logger.debug("Response from serialNumberRangeActivation {}" , responseMsg);
		return responseMsg;
	}
	
	@RequestMapping(value =FSAPIConstants.FSAPI_RELOAD_URI, method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> reload(@RequestHeader Map<String, String> headers,@RequestBody String payLoad) throws ServiceException 
	{
		Map<String, Object> valueObj = new HashMap<>();
		valueObj.putAll(headers);
		valueObj.put(ValueObjectKeys.API_NAME, FSAPIConstants.RELOAD_API);
		valueObj.put(ValueObjectKeys.REQUEST, payLoad);
		valueObj.put(ValueObjectKeys.REQUEST_METHOD_TYPE, RequestMethod.PUT);
		valueObj.put(ValueObjectKeys.ORIGINAL_HEADER, headers);
		ResponseEntity<Object> responseMsg = b2bService.reloadProcess(valueObj,headers);

		logger.debug("Response from reload {}" , responseMsg);
		return responseMsg;
	}
	
	
	
	@RequestMapping(value ="/replacement", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> replacement(@RequestHeader Map<String, String> headers, @RequestBody String replacementReq) throws ServiceException{

		Map<String, Object> valueObj = new HashMap<>();
		valueObj.put(ValueObjectKeys.API_NAME, FSAPIConstants.REPLACEMENTAPI);
		valueObj.put(ValueObjectKeys.REQUEST_METHOD_TYPE, FSAPIConstants.POST);
		ResponseEntity<Object> responseMsg = cardReplacementRenewalService.cardReplacementRenewalProcess(headers,replacementReq,valueObj);

		logger.debug("Response from replacement details {}" , responseMsg);
		return responseMsg;

	}
	
	@GetMapping(value ="/validation")
	public ResponseEntity<Object> validation(@RequestHeader Map<String, String> headers, @RequestParam("encryptedString") String encryptedString) throws ServiceException{

		Map<String, Object> valueObj = new HashMap<>();
		valueObj.put(ValueObjectKeys.API_NAME, FSAPIConstants.VIRTUALCARD_VALIDATION_API);
		valueObj.put(ValueObjectKeys.REQUEST_METHOD_TYPE, FSAPIConstants.GET);
		
		
		ResponseEntity<Object> responseMsg = virtualCardValidationService.virtualCardValidationProcess(headers, encryptedString,valueObj);
		logger.debug("Response from VCvalidation details {}" , responseMsg);
		return responseMsg;

	}

	
	

}
