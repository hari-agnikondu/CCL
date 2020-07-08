package com.incomm.cclp.fsapi.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.fsapi.bean.CSSResponseCode;
import com.incomm.cclp.fsapi.constants.B2BResponseCode;
import com.incomm.cclp.fsapi.constants.B2BResponseMessage;
import com.incomm.cclp.fsapi.constants.FSAPIConstants;
import com.incomm.cclp.fsapi.service.TransactionService;

@Service("fsapi")
public class ResponseBuilder {

	@Value("${FSAPIKEY}")
	private String fsapiKey;

	@Autowired
	private TransactionService transactionService;

	private final Logger logger = LogManager.getLogger(this.getClass());

	public ResponseEntity<Object> buildFailureResponse(String code, String message, Map<String, Object> valueObj) {
		Map<String, Object> response = new HashMap<>();
		HttpHeaders responseHeaders = new HttpHeaders();
		String respStr = "";
		
		String channelCode = "17";
		try {
			CSSResponseCode respCode = getCSSResponseCodeByRespId(
					/* String.valueOf(valueObj.get(FSAPIConstants.ORDER_CHNL_ID)) */channelCode,
					String.valueOf(code));
		

		responseHeaders.set(ValueObjectKeys.X_INCFS_CORRELATIONID, String.valueOf(valueObj.get(ValueObjectKeys.RRN)));
		response.put(ValueObjectKeys.FSAPIRESPONSECODE, respCode.getChannelResponseCode());
		response.put(ValueObjectKeys.RESPONSEMESSAGE, respCode.getResponseDescription());
		} catch (ServiceException e) {
			logger.error(e);
			responseHeaders.set(ValueObjectKeys.X_INCFS_CORRELATIONID, String.valueOf(valueObj.get(ValueObjectKeys.RRN)));
			response.put(ValueObjectKeys.FSAPIRESPONSECODE, FSAPIConstants.SQLEXCEPTION_DAO);
			response.put(ValueObjectKeys.RESPONSEMESSAGE, B2BResponseMessage.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
		}
		valueObj.put(FSAPIConstants.ORDER_RESPONSE, response);
		respStr = new JSONObject(response).toString();
		valueObj.put(FSAPIConstants.OUT_RESP_STR, respStr);
		return new ResponseEntity<>(response, responseHeaders, HttpStatus.BAD_REQUEST);
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> buildSuccessResponse(Map<String, Object> valueObj) throws ServiceException {
		HttpHeaders responseHeaders = new HttpHeaders();
		Map<String, Object> response = new HashMap<>();
	
		ResponseEntity<Object> respEntity = null;

		String respStr = "";
		Map<String, Object> orderMap = new HashMap<>();

		// hard coded channel code as B2B channel uses only WEB
		String channelCode = "17";
		List<Map<String, Object>> lineItemList = new ArrayList<>();
		List<Map<String, Object>> lineItemListResp = new ArrayList<>();

		try {
						List<Map<String, Object>> errorList = (List<Map<String, Object>>) valueObj.get(FSAPIConstants.ORRDER_ERROR_LIST);
			

			responseHeaders.set(ValueObjectKeys.X_INCFS_CORRELATIONID,
					String.valueOf(valueObj.get(ValueObjectKeys.INCOM_RRN_NUMBER)));

			lineItemList = (List<Map<String, Object>>) valueObj.get(FSAPIConstants.ORDER_LINE_ITEMS);

			orderMap.put(FSAPIConstants.ORDER_ORDER_ID, valueObj.get(FSAPIConstants.ORDER_ORDER_ID));
			
			
			
			/*if(!Util.isEmpty(valueObj.get(FSAPIConstants.ORDER_STATUS)+"") && FSAPIConstants.ORDER_ACCEPT_STATUS.equals(valueObj.get(FSAPIConstants.ORDER_STATUS))) {
				orderMap.put("status", "Received");
			} else {
				orderMap.put("status", valueObj.get(FSAPIConstants.ORDER_STATUS));	
			}
			
			
			orderMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, respCode.getChannelResponseCode());
			orderMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, respCode.getResponseDescription());
		*/
			CSSResponseCode respCode = getCSSResponseCodeByRespId(
					 channelCode,
					String.valueOf(valueObj.get(FSAPIConstants.ORDER_RESPONSE_CODE)));
			
			if(!errorList.isEmpty()) {
				
				
				orderMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, respCode.getChannelResponseCode());
				orderMap.put(FSAPIConstants.ORDER_RESPONSE_STATUS, FSAPIConstants.ORDER_REJECT_STATUS);
				orderMap.put(FSAPIConstants.ORDER_RESPONSE_MSG,  valueObj.get(FSAPIConstants.ORDER_RESPONSE_MSG));
			
			for (Map<String, Object> tempMap : lineItemList) {		
				Map<String, Object> tempMapResp = new HashMap<>();
				
				if("16".equals(respCode.getChannelResponseCode()) || "15".equals(respCode.getChannelResponseCode())) {
					tempMapResp.put(FSAPIConstants.ORDER_RESPONSE_CODE, respCode.getChannelResponseCode());
					tempMapResp.put(FSAPIConstants.ORDER_RESPONSE_STATUS, FSAPIConstants.ORDER_REJECT_STATUS);
					tempMapResp.put(FSAPIConstants.ORDER_RESPONSE_MSG,  valueObj.get(FSAPIConstants.ORDER_RESPONSE_MSG));	
				}else{
					tempMapResp.put(FSAPIConstants.ORDER_RESPONSE_CODE, respCode.getChannelResponseCode());
					tempMapResp.put(FSAPIConstants.ORDER_RESPONSE_STATUS, FSAPIConstants.ORDER_REJECT_STATUS);
					tempMapResp.put(FSAPIConstants.ORDER_RESPONSE_MSG,  respCode.getResponseDescription());	
				}
				lineItemListResp.add(tempMapResp);
			}
			
			} else{	
			boolean isSuccess=false;

			for (final Map<String, Object> tempMap : lineItemList) {
				logger.info("Response Code:"+String.valueOf(tempMap.get(FSAPIConstants.ORDER_RESPONSE_CODE))+"Response message:"+valueObj.get(FSAPIConstants.ORDER_RESPONSE_MSG));

				Map<String, Object> tempMapResp = new HashMap<>();
				tempMapResp.put(FSAPIConstants.ORDER_LINE_ITEM_ID, tempMap.get(FSAPIConstants.ORDER_LINE_ITEM_ID));
				CSSResponseCode respCodeLineItem = getCSSResponseCodeByRespId(
						/* String.valueOf(valueObj.get(FSAPIConstants.ORDER_CHNL_ID)) */channelCode,
						String.valueOf(tempMap.get(FSAPIConstants.ORDER_RESPONSE_CODE)));
				tempMapResp.put(FSAPIConstants.ORDER_RESPONSE_CODE, respCodeLineItem.getChannelResponseCode());
				if("16".equals(respCodeLineItem.getChannelResponseCode()) || "15".equals(respCodeLineItem.getChannelResponseCode()) ) {
					tempMapResp.put(FSAPIConstants.ORDER_RESPONSE_MSG, tempMap.get(FSAPIConstants.ORDER_RESPONSE_MSG));
				}else {
					tempMapResp.put(FSAPIConstants.ORDER_RESPONSE_MSG, respCodeLineItem.getResponseDescription());	
				}
				if(FSAPIConstants.ORDER_LINEITEM_SUSCMSG.equalsIgnoreCase(respCodeLineItem.getResponseDescription())) {
					isSuccess=true;
				}
				lineItemListResp.add(tempMapResp);
			}
			
			
			if(isSuccess) {
				orderMap.put(FSAPIConstants.ORDER_RESPONSE_STATUS, "Received");	
				orderMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, FSAPIConstants.SUCCESS_RESP_CODE);
				orderMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, FSAPIConstants.SUCCESS_MSG);
			}else if(FSAPIConstants.Y
					.equalsIgnoreCase(String.valueOf(valueObj.get(FSAPIConstants.ORDER_DUPLICATE_CHECK)))) {
				orderMap.put(FSAPIConstants.ORDER_RESPONSE_STATUS, FSAPIConstants.ORDER_REJECT_STATUS);
				orderMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, FSAPIConstants.ORDER_DUPLICATE_RESPCODE);
				orderMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, respCode.getResponseDescription());
				
			}/*else if("16".equals(respCode.getChannelResponseCode()) || "15".equals(respCode.getChannelResponseCode())) {
				orderMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, respCode.getChannelResponseCode());
				orderMap.put(FSAPIConstants.ORDER_RESPONSE_STATUS, FSAPIConstants.ORDER_REJECT_STATUS);
				orderMap.put(FSAPIConstants.ORDER_RESPONSE_MSG,  valueObj.get(FSAPIConstants.ORDER_RESPONSE_MSG));	
			}*/else {
				orderMap.put(FSAPIConstants.ORDER_RESPONSE_STATUS,FSAPIConstants.ORDER_REJECT_STATUS);
				orderMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, "02");
				orderMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, FSAPIConstants.ORDER_RESPONSE_REJSTATUS);
				
			}
				
			
			}
						

		} catch (ServiceException se) {
			logger.info("ServiceException in build Response::",se);
			orderMap.put(FSAPIConstants.ORDER_STATUS, FSAPIConstants.ORDER_REJECT_STATUS);
			CSSResponseCode respCodeExp = getCSSResponseCodeByRespId(
					/* String.valueOf(valueObj.get(FSAPIConstants.ORDER_CHNL_ID)) */channelCode,
					String.valueOf(se.getCode()));
			orderMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, respCodeExp.getChannelResponseCode());
			orderMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, respCodeExp.getResponseDescription());

			Map<String, Object> tempMapResp = new HashMap<>();
			tempMapResp.put(FSAPIConstants.ORDER_LINE_ITEM_ID, FSAPIConstants.ORDER_REJECT_STATUS);
			tempMapResp.put(FSAPIConstants.ORDER_RESPONSE_CODE,
					FSAPIConstants.SQLEXCEPTION_DAO);
			tempMapResp.put(FSAPIConstants.ORDER_RESPONSE_MSG,
					B2BResponseMessage.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
			lineItemListResp.add(tempMapResp);
		} catch (Exception e) {
			logger.info("Exception in build Response:::",e);
			orderMap.put(FSAPIConstants.ORDER_STATUS, FSAPIConstants.ORDER_REJECT_STATUS);
			orderMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, FSAPIConstants.SQLEXCEPTION_DAO);
			orderMap.put(FSAPIConstants.ORDER_RESPONSE_MSG,
					B2BResponseMessage.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);

			Map<String, Object> tempMapResp = new HashMap<>();
			tempMapResp.put(FSAPIConstants.ORDER_LINE_ITEM_ID, FSAPIConstants.ORDER_REJECT_STATUS);
			tempMapResp.put(FSAPIConstants.ORDER_RESPONSE_CODE,
					FSAPIConstants.SQLEXCEPTION_DAO);
			tempMapResp.put(FSAPIConstants.ORDER_RESPONSE_MSG,
					B2BResponseMessage.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
			lineItemListResp.add(tempMapResp);

		} finally {
			valueObj.put(FSAPIConstants.OUT_RESP_CODE, orderMap.get(FSAPIConstants.ORDER_RESPONSE_CODE));
			valueObj.put(FSAPIConstants.OUT_RESP_MSG, orderMap.get(FSAPIConstants.ORDER_RESPONSE_MSG));
		}

		orderMap.put(FSAPIConstants.ORDER_LINE_ITEMS, lineItemListResp);
		response.put(FSAPIConstants.HEADER_FIELD, orderMap);
		valueObj.put(FSAPIConstants.ORDER_RESPONSE, orderMap);
		respStr = new JSONObject(response).toString();
		valueObj.put(FSAPIConstants.OUT_RESP_STR, respStr);
		respEntity = new ResponseEntity<>(response, responseHeaders, HttpStatus.OK);
		
		return respEntity;
	}

	public ResponseEntity<Object> buildOrderStatusSuccessResponse(Map<String, Object> valueObj) {

		HttpHeaders responseHeaders = new HttpHeaders();
		Map<String, Object> response = new HashMap<>();
		ResponseEntity<Object> respEntity = null;
		Map<String, Object> orderMap = new HashMap<>();
		String channelCode = "17";
		String respStr="";
	
		responseHeaders.set(ValueObjectKeys.X_INCFS_CORRELATIONID,
				String.valueOf(valueObj.get(ValueObjectKeys.INCOM_RRN_NUMBER)));
		List<Map<String, Object>> lineItemListResp = new ArrayList<>();
		try {
	
			CSSResponseCode respCode = getCSSResponseCodeByRespId(
					/* String.valueOf(valueObj.get(FSAPIConstants.ORDER_CHNL_ID)) */channelCode,
					String.valueOf(valueObj.get(FSAPIConstants.ORDER_RESPONSE_CODE)));
		

		orderMap.put(FSAPIConstants.ORDER_ORDER_ID, valueObj.get(FSAPIConstants.ORDER_ORDER_ID));
		orderMap.put(FSAPIConstants.ORDER_STATUS, valueObj.get(FSAPIConstants.ORDER_STATUS));
		orderMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, respCode.getChannelResponseCode());
		orderMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, respCode.getResponseDescription());
		orderMap.put(FSAPIConstants.SHIPPINGMETHOD, valueObj.get(FSAPIConstants.SHIPPINGMETHOD));

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> lineItemList = (List<Map<String, Object>>) valueObj
				.get(FSAPIConstants.ORDER_LINE_ITEMDTLS);
		if (!CollectionUtils.isEmpty(lineItemList))
			for (final Map<String, Object> tempMap : lineItemList) {
				Map<String, Object> tempMapResp = new HashMap<>();
				tempMapResp.put(FSAPIConstants.ORDER_LINE_ITEM_ID, tempMap.get(FSAPIConstants.ORDER_LINE_ITEM_ID));
				
				CSSResponseCode respCodeLineItem = getCSSResponseCodeByRespId(
						/* String.valueOf(valueObj.get(FSAPIConstants.ORDER_CHNL_ID)) */channelCode,
						String.valueOf(tempMap.get(FSAPIConstants.LINEITEM_RESPONSE_CODE)));
				tempMapResp.put(FSAPIConstants.LINEITEM_RESPONSE_CODE,
						respCodeLineItem.getChannelResponseCode());
				tempMapResp.put(FSAPIConstants.LINEITEM_RESPONSE_MSG,
						respCodeLineItem.getResponseDescription());
				tempMapResp.put(FSAPIConstants.LINEITEM_STATUS, tempMap.get(FSAPIConstants.LINEITEM_STATUS));
				tempMapResp.put(FSAPIConstants.CARDS, tempMap.get(FSAPIConstants.CARDS));
				lineItemListResp.add(tempMapResp);
			}
		} catch (ServiceException e) {
			logger.error(e);
			orderMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,FSAPIConstants.SQLEXCEPTION_DAO);
			orderMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, B2BResponseMessage.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
			
		}
		orderMap.put(FSAPIConstants.ORDER_LINE_ITEMDTLS, lineItemListResp);
		response.put(FSAPIConstants.HEADER_FIELD, orderMap);
		valueObj.put(FSAPIConstants.ORDER_RESPONSE, orderMap);
		respStr = new JSONObject(response).toString();
		valueObj.put(FSAPIConstants.OUT_RESP_STR, respStr);
		respEntity = new ResponseEntity<>(response, responseHeaders, HttpStatus.OK);

		return respEntity;
	}

	public CSSResponseCode getCSSResponseCodeByRespId(String channelCode, String respId) throws ServiceException {

		return transactionService.getCSSResponseCodeByRespId(channelCode, respId);

	}

	@SuppressWarnings("unused")
	private void buildResponseBodyForOrderRequest(Object[] respObj, Map<String, Object> response) {
		response.put(ValueObjectKeys.FSAPIRESPONSECODE, respObj[0]);
		response.put(ValueObjectKeys.RESPONSEMESSAGE, respObj[1]);
	}

	public ResponseEntity<Object> buildCardStatusSuccessResponse(
			Map<String, Object> valueHashMap) {
		
		HttpHeaders responseHeaders = new HttpHeaders();
		Map<String, Object> response = new HashMap<>();
		ResponseEntity<Object> respEntity = null;
		String channelCode = valueHashMap.get(ValueObjectKeys.DELIVERYCHNL)+"";
		Map<String,Object> cardMap = new HashMap<>();
		CSSResponseCode respCode = new CSSResponseCode();
		
		try {
		respCode = getCSSResponseCodeByRespId(
					channelCode,
					String.valueOf(valueHashMap.get(FSAPIConstants.ORDER_RESPONSE_CODE)));
		
		responseHeaders.set(ValueObjectKeys.X_INCFS_CORRELATIONID,
				String.valueOf(valueHashMap.get(ValueObjectKeys.INCOM_RRN_NUMBER)));
		
		cardMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, respCode.getChannelResponseCode());
		cardMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, respCode.getResponseDescription());

		} catch (ServiceException e) {
			logger.info("ServiceException in build Response",e);
			cardMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,FSAPIConstants.SQLEXCEPTION_DAO);
			cardMap.put(FSAPIConstants.ORDER_RESPONSE_MSG,B2BResponseMessage.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
		}
		response.put(FSAPIConstants.CARDS, valueHashMap.get("cards"));
		response.put(FSAPIConstants.ORDER_RESPONSE, cardMap);
		valueHashMap.put(FSAPIConstants.ORDER_RESPONSE, cardMap);
		respEntity = new ResponseEntity<>(response, responseHeaders, HttpStatus.OK);
		
		return respEntity;
	}
	
	public ResponseEntity<Object> buildCardStatusFailureResponse(String code, String message, Map<String, Object> valueHashMap) {
		Map<String, Object> response = new HashMap<>();
		HttpHeaders responseHeaders = new HttpHeaders();
		logger.debug("code is ",code);
		String channelCode = valueHashMap.get(ValueObjectKeys.DELIVERYCHNL)+"";
		CSSResponseCode respCode = new CSSResponseCode();
		Map<String,Object> cardStatusErrorMap =  new HashMap<>();

		try {
			respCode = getCSSResponseCodeByRespId(
					channelCode,
					String.valueOf(valueHashMap.get(FSAPIConstants.ORDER_RESPONSE_CODE)));
			
		responseHeaders.set(ValueObjectKeys.X_INCFS_CORRELATIONID, String.valueOf(valueHashMap.get(ValueObjectKeys.RRN)));
		
		cardStatusErrorMap.put(ValueObjectKeys.FSAPIRESPONSECODE, respCode.getChannelResponseCode());
		cardStatusErrorMap.put(ValueObjectKeys.RESPONSEMESSAGE, message);
		
		} catch (ServiceException e) {
			logger.info("ServiceException in build Response ::",e);
			cardStatusErrorMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,FSAPIConstants.SQLEXCEPTION_DAO);
			cardStatusErrorMap.put(FSAPIConstants.ORDER_RESPONSE_MSG,B2BResponseMessage.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
		}
		catch (Exception e) {
			logger.info("Exception in build Response:",e);
			cardStatusErrorMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,FSAPIConstants.SQLEXCEPTION_DAO);
			cardStatusErrorMap.put(FSAPIConstants.ORDER_RESPONSE_MSG,B2BResponseMessage.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
		}
		
		response.put(FSAPIConstants.ORDER_RESPONSE, cardStatusErrorMap);
		valueHashMap.put(FSAPIConstants.ORDER_RESPONSE, cardStatusErrorMap);
		return new ResponseEntity<>(response, responseHeaders, HttpStatus.BAD_REQUEST);
	}
	
	

	/**
	 * 
	 * @param valueHashMap
	 * @return
	 */

	public ResponseEntity<Object> buildResponsewithCard(Map<String, Object> valueHashMap) {
		
		Map<String, Object> response = new HashMap<>();
		ResponseEntity<Object> respEntity = null;
		logger.info("ORDER_RESPONSE_CODE::"+valueHashMap.get(FSAPIConstants.ORDER_RESPONSE_CODE)+"ORDER_RESPONSE_MSG::"+valueHashMap.get(FSAPIConstants.ORDER_RESPONSE_MSG)+"");
		try{
		
			CSSResponseCode respCode = getCSSResponseCodeByRespId(String.valueOf(valueHashMap.get(ValueObjectKeys.DELIVERYCHNL)),String.valueOf(valueHashMap.get(ValueObjectKeys.FSAPIRESPONSECODE)));
			response.put(ValueObjectKeys.FSAPIRESPONSECODE,respCode.getChannelResponseCode());
			response.put(ValueObjectKeys.RESPONSEMESSAGE, respCode.getResponseDescription());	
		
			if(B2BResponseCode.SUCCESS.equalsIgnoreCase(valueHashMap.get(ValueObjectKeys.FSAPIRESPONSECODE)+"")){
				
				response.put(FSAPIConstants.CUSTOMERID, valueHashMap.get(ValueObjectKeys.CUST_CODE));
				
				if("Virtual".equalsIgnoreCase( valueHashMap.get(ValueObjectKeys.CARD_TYPE)+"")) {
					response.put("PAN", valueHashMap.get(ValueObjectKeys.CARDNUMBER));
				}else {
					response.put("PAN", "");
				}
				
				response.put(FSAPIConstants.EXPIRATIONDATE, valueHashMap.get(ValueObjectKeys.EXPIRATIONDATE));
				response.put("CVV2", valueHashMap.get(FSAPIConstants.CVV2));
				response.put(FSAPIConstants.CARD_STATUS, valueHashMap.get(ValueObjectKeys.STATUS));
			}
			
		}catch(Exception e){
			
			logger.error("Exception in build Response",e);
			response.put(FSAPIConstants.ORDER_RESPONSE_CODE,FSAPIConstants.SQLEXCEPTION_DAO);
			response.put(FSAPIConstants.ORDER_RESPONSE_MSG,B2BResponseMessage.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
			
		}
		valueHashMap.put(FSAPIConstants.OUT_RESP_STR,new JSONObject(response).toString());
		respEntity = new ResponseEntity<>(response, HttpStatus.OK);
		return respEntity;
	}
	

	public ResponseEntity<Object> buildResponse(Map<String, Object> valueObj){
		ResponseEntity<Object> respEntity = null;
		String channelCode = valueObj.get(ValueObjectKeys.DELIVERYCHNL)+"";
		Map<String,String> responseMap=new HashMap<>();
		logger.info("ORDER_RESPONSE_CODE:"+valueObj.get(FSAPIConstants.ORDER_RESPONSE_CODE)+"ORDER_RESPONSE_MSG:"+valueObj.get(FSAPIConstants.ORDER_RESPONSE_MSG)+"");
		try {
			CSSResponseCode respCode = getCSSResponseCodeByRespId(channelCode,String.valueOf(valueObj.get(FSAPIConstants.ORDER_RESPONSE_CODE)));
			responseMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,respCode.getChannelResponseCode());
			responseMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, respCode.getResponseDescription());	

		}catch(ServiceException se) {
			logger.error("ServiceException in buildResponse",se);
			responseMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,FSAPIConstants.SQLEXCEPTION_DAO);
			responseMap.put(FSAPIConstants.ORDER_RESPONSE_MSG,B2BResponseMessage.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);

		}catch(Exception e) {
			logger.error("Exception in buildResponse",e);
			responseMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,FSAPIConstants.SQLEXCEPTION_DAO);
			responseMap.put(FSAPIConstants.ORDER_RESPONSE_MSG,B2BResponseMessage.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
		}
		valueObj.put(FSAPIConstants.OUT_RESP_STR,new JSONObject(responseMap).toString());
		valueObj.put(FSAPIConstants.ORDER_RESPONSE, responseMap);
		respEntity = new ResponseEntity<>(responseMap,HttpStatus.OK);
		return respEntity;
	}
	
	
	/**
	 * 
	 * @param valueHashMap
	 * @return
	 */

	public ResponseEntity<Object> buildResponseForVCValidation(Map<String, Object> valueHashMap) {
		
		Map<String, Object> response = new HashMap<>();
		ResponseEntity<Object> respEntity = null;
		logger.info("ORDER_RESPONSE_CODE:"+valueHashMap.get(FSAPIConstants.ORDER_RESPONSE_CODE)+"ORDER_RESPONSE_MSG:"+valueHashMap.get(FSAPIConstants.ORDER_RESPONSE_MSG)+"");
		try{
		
			CSSResponseCode respCode = getCSSResponseCodeByRespId(String.valueOf(valueHashMap.get(ValueObjectKeys.DELIVERYCHNL)),String.valueOf(valueHashMap.get(ValueObjectKeys.FSAPIRESPONSECODE)));
			response.put(ValueObjectKeys.FSAPIRESPONSECODE,respCode.getChannelResponseCode());
			response.put(ValueObjectKeys.RESPONSEMESSAGE, respCode.getResponseDescription());	
		
			if(B2BResponseCode.SUCCESS.equalsIgnoreCase(valueHashMap.get(ValueObjectKeys.FSAPIRESPONSECODE)+"")){

				response.put("customerID", valueHashMap.get(ValueObjectKeys.CUST_CODE));
				response.put("PAN", valueHashMap.get(ValueObjectKeys.CARDNUMBER));
				
				response.put("expirationDate", valueHashMap.get(ValueObjectKeys.EXPIRATIONDATE));
				response.put("CVV2", valueHashMap.get(FSAPIConstants.CVV2));
				response.put("status", valueHashMap.get("status"));
				response.put("packageID", valueHashMap.get(FSAPIConstants.VIRTUAL_PACKAGE_ID));
				if("T".equals(valueHashMap.get("EXPIREDFLAG"))) {
				response.put("expiryDateUpdateFlag", valueHashMap.get("EXPIREDFLAG"));
				}else {
					response.put("expiryDateUpdateFlag", "");
				}
			}
			
		}catch(Exception e){
			
			logger.error("Exception in buildResponse",e);
			response.put(FSAPIConstants.ORDER_RESPONSE_CODE,FSAPIConstants.SQLEXCEPTION_DAO);
			response.put(FSAPIConstants.ORDER_RESPONSE_MSG,B2BResponseMessage.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
			
		}
		valueHashMap.put(FSAPIConstants.OUT_RESP_STR,new JSONObject(response).toString());
		respEntity = new ResponseEntity<>(response, HttpStatus.OK);
		return respEntity;
	}
}
