package com.incomm.cclp.fsapi.helper;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.incomm.cclp.dao.OrderDAO;
import com.incomm.cclp.fsapi.constants.FSAPIConstants;
import com.incomm.cclp.fsapi.dao.OrderProcessDAO;


@Component
public class ReqResLogger {


	@Autowired
	OrderProcessDAO orderProcessDAO;

	@Autowired
	OrderDAO orderDao;


	@SuppressWarnings("unchecked")
	public void responseLogger(String apiName,Map<String, Object> valueHashMap, String reqStr, long timeTaken, String reqHeaders) throws UnknownHostException {

		String serverID = "";
		String respCode = "";
		String respMsg = "";
		Map<String, Object> responseMap= new HashMap<>();
		String orderId = String.valueOf(valueHashMap.get(FSAPIConstants.ORDERID));
		String respStr = String.valueOf(valueHashMap.get(FSAPIConstants.OUT_RESP_STR));
		Object response =  valueHashMap.get(FSAPIConstants.ORDER_RESPONSE);
		if(response != null) {
			responseMap = (Map<String, Object>)response;
			 respCode = String.valueOf(responseMap.get(FSAPIConstants.ORDER_RESPONSE_CODE));
			 respMsg = String.valueOf(responseMap.get(FSAPIConstants.ORDER_RESPONSE_MSG));
		}else {
			 respCode = String.valueOf(valueHashMap.get(FSAPIConstants.ORDER_RESPONSE_CODE));
			 respMsg = String.valueOf(valueHashMap.get(FSAPIConstants.ORDER_RESPONSE_MSG));
		}
		String postBackUrl = String.valueOf(valueHashMap.get(FSAPIConstants.POSTBACK_URL));
		serverID = InetAddress.getLocalHost().getHostName();
		final String partnerId = String.valueOf(valueHashMap.get(FSAPIConstants.ORDER_PARTNERID));

		// validating POST BACK URL
		if (valueHashMap.get(FSAPIConstants.POST_BACK_RESP) != null && (FSAPIConstants.ORDER_FSAPI_TRUE
				.equalsIgnoreCase(valueHashMap.get(FSAPIConstants.POST_BACK_RESP).toString())
				|| FSAPIConstants.ORDER_FSAPI_TRUE1
				.equals(valueHashMap.get(FSAPIConstants.POST_BACK_RESP).toString())) && orderDao.b2bDuplicateOrderCheck(orderId,partnerId)  != 0) {
					
			/*if(!FSAPIConstants.ORDER_API.equalsIgnoreCase(valueHashMap.get(CCLPConstants.API_NAME)+"")){
				orderProcessDAO.responsePostBackLogger(GeneralConstants.ORDER,orderId,reqHeaders,respStr,respCode,respMsg,null,serverID,timeTaken,postBackUrl);
			}*/
		}

		orderProcessDAO.reqRespLogger(apiName,reqHeaders,reqStr,respCode,respMsg,respStr,serverID,timeTaken);
	}


	public void finalResponseLogger(Map<String, Object> valueHashMap, String reqStr, long timeTaken, String reqHeaders,boolean isPostBack,String apiName) throws UnknownHostException {

		String serverID = "";

		String orderId = String.valueOf(valueHashMap.get(FSAPIConstants.ORDERID));
		String respStr = String.valueOf(valueHashMap.get(FSAPIConstants.OUT_RESP_STR));
		String respCode = String.valueOf(valueHashMap.get(FSAPIConstants.ORDER_RESPONSE_CODE));
		String respMsg = String.valueOf(valueHashMap.get(FSAPIConstants.ORDER_RESPONSE_MSG));
		String postBackUrl = String.valueOf(valueHashMap.get(FSAPIConstants.POSTBACK_URL));
		serverID = InetAddress.getLocalHost().getHostName();
		final String partnerId = String.valueOf(valueHashMap.get(FSAPIConstants.ORDER_PARTNERID));

		// validating POST BACK URL
		if (isPostBack && (valueHashMap.get(FSAPIConstants.POST_BACK_RESP) != null && (FSAPIConstants.ORDER_FSAPI_TRUE
				.equalsIgnoreCase(valueHashMap.get(FSAPIConstants.POST_BACK_RESP).toString())
				|| FSAPIConstants.ORDER_FSAPI_TRUE1
				.equals(valueHashMap.get(FSAPIConstants.POST_BACK_RESP).toString())))) {

			if (orderDao.b2bDuplicateOrderCheck(orderId,partnerId)  != 0) {

				orderProcessDAO.responsePostBackLogger(apiName,orderId,reqHeaders,reqStr,respCode,respMsg,respStr,serverID,timeTaken,postBackUrl);

			}
		}
		else{
			orderProcessDAO.reqRespLogger(apiName,reqHeaders,reqStr,respCode,respMsg,respStr,serverID,timeTaken);
		}
	}


	public void postBackLogger(Map<String, Object> valueHashMap, String reqStr, long timeTaken, String reqHeaders,String apiName) throws UnknownHostException {

		if ((valueHashMap.get(FSAPIConstants.POST_BACK_RESP) != null && (FSAPIConstants.ORDER_FSAPI_TRUE
				.equalsIgnoreCase(valueHashMap.get(FSAPIConstants.POST_BACK_RESP).toString())
				|| FSAPIConstants.ORDER_FSAPI_TRUE1
				.equals(valueHashMap.get(FSAPIConstants.POST_BACK_RESP).toString())))) {		
			String serverID = "";
			String orderId = String.valueOf(valueHashMap.get(FSAPIConstants.ORDERID));
			String respCode = String.valueOf(valueHashMap.get(FSAPIConstants.ORDER_RESPONSE_CODE));
			String respMsg = String.valueOf(valueHashMap.get(FSAPIConstants.ORDER_RESPONSE_MSG));
			String postBackUrl = String.valueOf(valueHashMap.get(FSAPIConstants.POSTBACK_URL));
			serverID = InetAddress.getLocalHost().getHostName();
			orderProcessDAO.responsePostBackLogger(apiName,orderId,reqHeaders,reqStr,respCode,respMsg,null,serverID,timeTaken,postBackUrl);
		}

	}


	public void reqRespLogger(Map<String, Object> valueHashMap, String reqStr, long timeTaken, String reqHeaders,String apiName) throws UnknownHostException {

		String serverID = "";

		String respStr = String.valueOf(valueHashMap.get(FSAPIConstants.OUT_RESP_STR));
		String respCode = String.valueOf(valueHashMap.get(FSAPIConstants.ORDER_RESPONSE_CODE));
		String respMsg = String.valueOf(valueHashMap.get(FSAPIConstants.ORDER_RESPONSE_MSG));
		serverID = InetAddress.getLocalHost().getHostName();
		orderProcessDAO.reqRespLogger(apiName,reqHeaders,reqStr,respCode,respMsg,respStr,serverID,timeTaken);

	}



}
