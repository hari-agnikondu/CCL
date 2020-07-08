package com.incomm.cclp.fsapi.service.impl;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.fsapi.bean.CSSResponseCode;
import com.incomm.cclp.fsapi.constants.B2BResponseCode;
import com.incomm.cclp.fsapi.constants.B2BResponseMessage;
import com.incomm.cclp.fsapi.constants.FSAPIConstants;
import com.incomm.cclp.fsapi.dao.OrderCancelDAO;
import com.incomm.cclp.fsapi.dao.OrderProcessDAO;
import com.incomm.cclp.fsapi.dao.SerialNumberActivationDAO;
import com.incomm.cclp.fsapi.dao.SpilReloadDAO;
import com.incomm.cclp.fsapi.helper.APIHelper;
import com.incomm.cclp.fsapi.helper.FSAPIUtils;
import com.incomm.cclp.fsapi.helper.JsonHelper;
import com.incomm.cclp.fsapi.helper.OrderValidator;
import com.incomm.cclp.fsapi.helper.ReqResLogger;
import com.incomm.cclp.fsapi.response.ResponseBuilder;
import com.incomm.cclp.fsapi.service.CommonService;
import com.incomm.cclp.fsapi.service.OrderProcessService;
import com.incomm.cclp.fsapi.service.PostBackService;
import com.incomm.cclp.fsapi.service.SerialNumberRangeActivationService;
import com.incomm.cclp.fsapi.service.TransactionService;
import com.incomm.cclp.util.Util;

@Service
public class PostBackServiceImpl extends JdbcDaoSupport implements PostBackService{

	@Autowired
	APIHelper apiHelper;

	@Autowired
	CommonService commonService;

	@Autowired
	ResponseBuilder responseBuilder;
	@Autowired
	public FSAPIUtils fsapiUtils;

	@Autowired
	OrderProcessDAO orderProcessDAO;

	@Autowired
	ReqResLogger reqResLogger;

	@Autowired
	OrderProcessService orderProcessService;

	@Autowired
	TransactionService transactionService;
	
	@Autowired
	OrderCancelDAO orderCancelDao;
	
	@Autowired
	OrderValidator orderValidator;

	@Autowired
	SerialNumberRangeActivationService serialNumberRangeActivationService;
	
	@Autowired
	FSAPIUtils  utils;
	
	@Autowired
	SpilReloadDAO spilReloadDAO;
	
	@Autowired
	SerialNumberActivationDAO serialNumberDAO;

	@Autowired
	PostBackProcessImpl postBackProcessImpl;
	
	@Autowired
    public void setDs(@Qualifier("orderDs") DataSource dataSource) {
         setDataSource(dataSource);
    }
	
	private final Logger log = LogManager.getLogger(this.getClass());


	@Async
	public void callPostBackProcess(Map<String, Object> valuHashMap,final String apiName, long timeTaken, String reqHeaders)  {
		String orderId = String.valueOf(valuHashMap.get(FSAPIConstants.ORDER_ORDER_ID));
		String partnerId = String.valueOf(valuHashMap.get(FSAPIConstants.ORDER_PARTNERID));
		String respCde = String.valueOf(valuHashMap.get(FSAPIConstants.ORDER_RESPONSE_CODE)) != null ? String
				.valueOf(valuHashMap.get(FSAPIConstants.ORDER_RESPONSE_CODE)): String.valueOf(valuHashMap.get(FSAPIConstants.SUCCESS_RESP_CODE));
				String postBackRes = String.valueOf(valuHashMap.get(FSAPIConstants.POST_BACK_RESP));
				if (orderId != null && partnerId != null
						&& B2BResponseCode.SUCCESS.equalsIgnoreCase(respCde)) {
					log.debug(" -callPostBackProcess for bulk order activation && serial range activation------");
					try {
						String orderStatus = String.valueOf(valuHashMap.get(FSAPIConstants.ORDER_RESPONSE_STATUS));

						@SuppressWarnings("unchecked")
						List<Map<String, Object>> lineItemStatus = (LinkedList<Map<String, Object>>) valuHashMap
 						.get(FSAPIConstants.ORDER_LINE_ITEMDTLS);

						if(lineItemStatus!=null && !lineItemStatus.isEmpty()){
							for (final Map<String, Object> temp : lineItemStatus) {

								@SuppressWarnings("unchecked")
								List<Map<String, String>> tempCardArry = (List<Map<String, String>>) temp.get(FSAPIConstants.CARDS);

								for (Map<String, String> tempCrd : tempCardArry) {

									valuHashMap.putAll(tempCrd);

							

										if (apiName != null && apiName.equalsIgnoreCase(FSAPIConstants.ORDER_ACTIVATION_API)) {
											orderProcessService.updateCardInfo(valuHashMap);
											
											tempCrd.put(FSAPIConstants.ORDER_RESPONSE_STATUS,transactionService.getAllCardStatus().get((String) valuHashMap.get(ValueObjectKeys.CARDSTATUS)));
										}
										else if(apiName != null && apiName.equalsIgnoreCase(FSAPIConstants.SERIALNUMBER_RANGE_VALIDATION_API)){
											serialNumberRangeActivationService.updateCardInfo(valuHashMap);
											
											tempCrd.put(FSAPIConstants.ORDER_RESPONSE_STATUS,transactionService.getAllCardStatus().get((String) valuHashMap.get(ValueObjectKeys.CARDSTATUS)));


										}
									
								}

							}
							apiHelper.getCardArrayDtls(valuHashMap);
							valuHashMap.put(FSAPIConstants.ORDER_RESPONSE_STATUS,orderStatus);

							if (FSAPIConstants.ORDER_FSAPI_TRUE.equalsIgnoreCase(postBackRes)
									|| FSAPIConstants.ORDER_FSAPI_TRUE1.equals(postBackRes)) {

								valuHashMap.put(FSAPIConstants.ORDER_LINE_ITEM_ID,new HashSet<String>());
								valuHashMap.put(FSAPIConstants.POSTBACK_APINAME,FSAPIConstants.ORDER_POSTBACK_APINAME);
								String channelCode = valuHashMap.get(ValueObjectKeys.DELIVERYCHNL)+"";
								log.info("ORDER_RESPONSE_CODE:"+valuHashMap.get(FSAPIConstants.ORDER_RESPONSE_CODE)+"ORDER_RESPONSE_MSG:"+valuHashMap.get(FSAPIConstants.ORDER_RESPONSE_MSG)+"");
								CSSResponseCode respCode = responseBuilder.getCSSResponseCodeByRespId(channelCode,String.valueOf(valuHashMap.get(FSAPIConstants.ORDER_RESPONSE_CODE)));
								valuHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,respCode.getChannelResponseCode());
								valuHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, respCode.getResponseDescription());	
								valuHashMap.put("status","Received");
								Map<String, Object> valueResMap = apiHelper.setResValue(FSAPIConstants.ORDER_STATUS_API	+ FSAPIConstants.GET, valuHashMap);
								valueResMap.put(FSAPIConstants.POSTBACK_URL, valuHashMap.get(FSAPIConstants.POSTBACK_URL));	
								Map<String,Object> postBackReqMap=new HashMap<>();
								postBackReqMap.put("order", valueResMap);
								JSONObject resJsonObj = JsonHelper.getJsonFromMap(postBackReqMap);
								String res = FSAPIConstants.ORDER_EMPTY_STRING;
								if (resJsonObj != null) {
									res = resJsonObj.toString();
								}
								
							logger.info("Post Back Request message"+res);
					
									reqResLogger.postBackLogger(valuHashMap, res,timeTaken,String.valueOf(new JSONObject(reqHeaders)),apiName);
								
							}
						}
						else{
							logger.error(" Line Item Details not found ------");
						}
						
					}  catch (UnknownHostException e) {
						log.error("UnknownHostException while logging response message "+ e);
					} catch (Exception e) {
						log.error(" -callPostBackProcess calling  ------", e);
					}
				} else {
					log.debug(" -callPostBackProcess calling Skipped------");
				}
	}
	@Override
	@Async
	public void cancelOrderProcess(Map<String, Object> valueHashMap) {
		
		try {
			Map<String,Object> cancelOrderMap =orderCancelDao.cancelOrderProcess(valueHashMap);
			
			if(!cancelOrderMap.isEmpty())
			{
				valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,cancelOrderMap.get("P_RESP_CODE_OUT"));
				valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, cancelOrderMap.get("P_RESP_MSG_OUT"));
				valueHashMap.put(FSAPIConstants.POSTBACK_URL, cancelOrderMap.get("P_POSTBACK_URL_OUT"));
			}
			String postBackRes = String.valueOf(valueHashMap.get(FSAPIConstants.POSTBACK_URL));
			log.debug(" -cancelOrderProcess calling  --postBackRes---"+postBackRes);
			if (APIHelper.emptyCheck(postBackRes)) {
				sendPostBackForFile(valueHashMap, FSAPIConstants.CANCEL_ORDER_API,FSAPIConstants.ORDER_POSTBACK_APINAME);
			}
		} catch (Exception e) {
			log.error(" -cancelOrderProcess calling  ------", e);
		}
	}
	
	
	public void sendPostBackForFile(Map<String, Object> valuHashMap,String apiName,String postBackAPIName) {
		@SuppressWarnings("unchecked")
		Set<String> lineItemSet = (Set<String>) valuHashMap.get(FSAPIConstants.ORDER_LINE_ITEM_ID);
		try {
			if(lineItemSet==null || lineItemSet.isEmpty()){
				valuHashMap.put(FSAPIConstants.ORDER_LINE_ITEM_ID, new HashSet<String>());			
			}
			valuHashMap.put(FSAPIConstants.POSTBACK_APINAME, postBackAPIName);
			log.debug(" -sendPostBackForFile calling ------"+apiName);
			orderValidator.getOrderStatus(valuHashMap);
			orderValidator.getlineItemDtls(valuHashMap, apiName);
			Map<String, Object> valueResMap = apiHelper.setResValue(FSAPIConstants.ORDER_STATUS_API+FSAPIConstants.GET, valuHashMap);
			
			JSONObject resJsonObj = JsonHelper.getJsonFromMap(valueResMap);
			valuHashMap.put(FSAPIConstants.POST_BACK_RESP,FSAPIConstants.ORDER_FSAPI_TRUE);
			String res = FSAPIConstants.ORDER_EMPTY_STRING;
			if (resJsonObj != null) {
				res = resJsonObj.toString();
			}
			log.info("Post Back response:"+res);
			
			
				reqResLogger.postBackLogger(valuHashMap, res,(Long)valuHashMap.get("timeTaken"),String.valueOf(valuHashMap.get("headers")),apiName);
			
			
		}  catch (UnknownHostException e) {
			log.error("UnknownHostException while logging response message "+ e);
		} catch (Exception e) {
			log.error("Exception in sendPostBackForFile",e);
		}
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Async
	@Override
	public  void reloadPostBack(Map<String, Object> valuHashMap){
		HashMap<String,String> tempValueMap= new HashMap<>();
		try{
			valuHashMap.remove(ValueObjectKeys.RESPONSEMESSAGE);
			valuHashMap.remove(ValueObjectKeys.FSAPIRESPONSECODE);
			
			tempValueMap.put(ValueObjectKeys.INSTCODE,FSAPIConstants.INST_CODE);
			tempValueMap.put(ValueObjectKeys.MSGTYPE,FSAPIConstants.FINANCIAL_MESSAGE_TYPE);			
			tempValueMap.put(ValueObjectKeys.MEMBERNO, FSAPIConstants.MEMBER_NUMBER);
			tempValueMap.put(ValueObjectKeys.REVERSAL_CODE,FSAPIConstants.REVERSAL_CODE);
			tempValueMap.put(ValueObjectKeys.TXN_MODE,FSAPIConstants.TRANS_MODE);
			tempValueMap.put(ValueObjectKeys.DELIVERYCHNL,(String)valuHashMap.get(ValueObjectKeys.DELIVERYCHNL));
			tempValueMap.put(ValueObjectKeys.TRANS_CODE,(String)valuHashMap.get(ValueObjectKeys.TRANS_CODE));

			tempValueMap.put(ValueObjectKeys.MERCHANT_NAME, (String)valuHashMap.get(FSAPIConstants.RELOAD_MERCHANTID));
			tempValueMap.put(ValueObjectKeys.POSTBACKRESPONSE, String.valueOf(valuHashMap.get(FSAPIConstants.RELOAD_POST_BACK_RESPONSE)));
			tempValueMap.put(ValueObjectKeys.POSTBACKURL, (String)valuHashMap.get(FSAPIConstants.RESP_HERDER_POSTBACKURL));
			tempValueMap.put(ValueObjectKeys.X_INCFS_CORRELATIONID, (String)valuHashMap.get(ValueObjectKeys.X_INCFS_CORRELATIONID));
			
			LinkedList  cardlist=(LinkedList)valuHashMap.get(FSAPIConstants.CARDS);
			ListIterator list=cardlist.listIterator();
			List<Map<String,String>> cardDetails=new ArrayList<>();
			while(list.hasNext()){
				Map<String,String> cardlst=(Map<String,String>)list.next();
				
					for(Entry<String, String> cardTemp : cardlst.entrySet()){
						tempValueMap.put(cardTemp.getKey(), cardTemp.getValue());
					}
					utils.getRRN(valuHashMap,FSAPIConstants.RELOADRRNSEQ);
					utils.getyyyyMMdd(valuHashMap, new java.sql.Date(new java.util.Date().getTime()));
					utils.gethhmmss(valuHashMap, new java.util.Date());
					tempValueMap.put(ValueObjectKeys.RRN, (String)valuHashMap.get(ValueObjectKeys.RRN));
					tempValueMap.put(ValueObjectKeys.BUSINESS_DATE, (String)valuHashMap.get(ValueObjectKeys.BUSINESS_DATE));
					tempValueMap.put(ValueObjectKeys.BUSINESS_TIME, (String)valuHashMap.get(ValueObjectKeys.BUSINESS_TIME));
					tempValueMap.put(ValueObjectKeys.DELIVERYCHNL, (String)valuHashMap.get(ValueObjectKeys.DELIVERYCHNL));
					tempValueMap.put(ValueObjectKeys.TRANS_CODE, (String)valuHashMap.get(ValueObjectKeys.TRANS_CODE));
					tempValueMap.put(ValueObjectKeys.TRANS_SHORT_NAME, (String)valuHashMap.get(ValueObjectKeys.TRANS_SHORT_NAME));
					tempValueMap.put(ValueObjectKeys.IS_FINANCIAL, (String)valuHashMap.get(ValueObjectKeys.IS_FINANCIAL));
					tempValueMap.put(ValueObjectKeys.CREDIT_DEBIT_INDICATOR, (String)valuHashMap.get(ValueObjectKeys.CREDIT_DEBIT_INDICATOR));
					tempValueMap.put(ValueObjectKeys.TRANSACTIONDESC, (String)valuHashMap.get(ValueObjectKeys.TRANSACTIONDESC));
					tempValueMap.put(ValueObjectKeys.X_INCFS_CHANNEL,(String)valuHashMap.get(ValueObjectKeys.X_INCFS_CHANNEL));
					String tranAmount=tempValueMap.get(FSAPIConstants.DENOMINATION);
					tempValueMap.put(ValueObjectKeys.TRANAMOUNT, tranAmount);
					utils.getCardDetls(tempValueMap);
					utils.checkDenomination(tempValueMap);
					utils.checkReloadableFlag(tempValueMap);
					
					if(tempValueMap.get(ValueObjectKeys.FSAPIRESPONSECODE)!=null 
							&& !B2BResponseCode.TOP_UP_NOT_SUPPORTED.equals(tempValueMap.get(ValueObjectKeys.FSAPIRESPONSECODE)) && !B2BResponseCode.INVALID_DENOMINATION.equals(tempValueMap.get(ValueObjectKeys.FSAPIRESPONSECODE))){
						Map<String,Object> checkCardStatus=new HashMap<>();
						checkCardStatus.put(ValueObjectKeys.CARDSTATUS,tempValueMap.get(ValueObjectKeys.CARDSTATUS));
						checkCardStatus.put(ValueObjectKeys.PROD_CODE,tempValueMap.get(ValueObjectKeys.PROD_CODE));
						checkCardStatus.put(ValueObjectKeys.TRANS_SHORT_NAME,valuHashMap.get(ValueObjectKeys.TRANS_SHORT_NAME));
						checkCardStatus.put(ValueObjectKeys.X_INCFS_CHANNEL,valuHashMap.get(ValueObjectKeys.X_INCFS_CHANNEL));
						checkCardStatus.put(ValueObjectKeys.HASH_CARDNO,tempValueMap.get(ValueObjectKeys.HASH_CARDNO));
						
						
						String cardStatusRespCode = utils.validateCardStatusForFinancialTxn(checkCardStatus);
						
						//check product validity
						if(!CCLPConstants.EXPIRED_PRODUCT_CARD_STATUS.equals(checkCardStatus.get(ValueObjectKeys.CARDSTATUS))
								&& B2BResponseCode.SUCCESS.equals(cardStatusRespCode)){
							utils.checkProductValidity(checkCardStatus);
							cardStatusRespCode = String.valueOf(checkCardStatus.get(ValueObjectKeys.FSAPIRESPONSECODE));
						}
						
						if(cardStatusRespCode != null && B2BResponseCode.SUCCESS.equals(cardStatusRespCode)){
							String[] respFields=spilReloadDAO.invokeReload(tempValueMap);
							populatePostBackVal(tempValueMap,valuHashMap,cardlst,respFields[0],respFields[3]);	
						}
						else{
							Map<String,Object> valObj=new HashMap<>();
							for(Entry<String, String> cardTemp : tempValueMap.entrySet()){
								valObj.put(cardTemp.getKey(), cardTemp.getValue());
							}
							valObj.put(ValueObjectKeys.FSAPIRESPONSECODE, cardStatusRespCode);
							populatePostBackVal(tempValueMap,valuHashMap,cardlst,cardStatusRespCode,"0.0");
							utils.logTxnDtls(valObj, FSAPIConstants.INVALID_CARD_STATE, cardStatusRespCode);
						}
					}
					else{
						Map<String,Object> valObj=new HashMap<>();
						for(Entry<String, String> cardTemp : tempValueMap.entrySet()){
							valObj.put(cardTemp.getKey(), cardTemp.getValue());
						}
						populatePostBackVal(tempValueMap,valuHashMap,cardlst,tempValueMap.get(ValueObjectKeys.FSAPIRESPONSECODE),"0.0");
						utils.logTxnDtls(valObj, tempValueMap.get(ValueObjectKeys.RESPONSEMESSAGE), tempValueMap.get(ValueObjectKeys.FSAPIRESPONSECODE));
					}
				
				
				cardDetails.add(cardlst);
				clearMap(tempValueMap,valuHashMap);
			} 
			
			Map<String,Object> postBackMap=new HashMap<>();
			postBackMap.put(FSAPIConstants.CARDS, cardDetails);
			String postBackResponse=String.valueOf(valuHashMap.get(ValueObjectKeys.POSTBACKRESPONSE));
			if(FSAPIConstants.ORDER_FSAPI_TRUE.equalsIgnoreCase(postBackResponse) || FSAPIConstants.ORDER_FSAPI_TRUE1.equals(postBackResponse)){
			Map<String, Object> valueResMap = apiHelper.setResValue(FSAPIConstants.RELOAD_POSTBACK_API+FSAPIConstants.POST, postBackMap);
			
			valueResMap.put(FSAPIConstants.POSTBACK_URL, String.valueOf(valuHashMap.get(FSAPIConstants.POSTBACK_URL)));
			JSONObject resJsonObj = JsonHelper.getJsonFromMap(valueResMap);
			
			String res = FSAPIConstants.ORDER_EMPTY_STRING;
			if (resJsonObj != null) {
				res = resJsonObj.toString();
			}
			logger.info("postback Request message "+res);
			valuHashMap.put(FSAPIConstants.POSTBACK_APINAME, FSAPIConstants.RELOAD_POSTBACK_API); 
			valuHashMap.put(ValueObjectKeys.FSAPIRESPONSECODE, B2BResponseCode.SUCCESS);
			valuHashMap.put(ValueObjectKeys.RESPONSEMESSAGE, B2BResponseMessage.SUCCESS);
			Map<String,Object> reqHeader=(Map<String,Object>)valuHashMap.get(ValueObjectKeys.ORIGINAL_HEADER);
			reqResLogger.postBackLogger(valuHashMap, res,0,String.valueOf(new JSONObject(reqHeader)),FSAPIConstants.RELOAD_POSTBACK_API);
			
			}else{
				log.debug("Postback response is ::"+postBackResponse);
			}
			
		} catch (UnknownHostException e) {
			log.error("Error while logging response message "+ e);
			}
		catch(ServiceException srvExp){
			log.error("ServiceException while processing",srvExp);	
		}
		catch(Exception exp){
			log.error("Exception while processing",exp);	
		}
	}
	
	public void clearMap(Map<String,String> tempValueMap,Map<String, Object> valuHashMap){
		
		tempValueMap.remove(ValueObjectKeys.CARDNO);
		tempValueMap.remove(ValueObjectKeys.RRN);
		tempValueMap.remove(ValueObjectKeys.TRANAMOUNT);
		tempValueMap.remove(ValueObjectKeys.TYPE);
		tempValueMap.remove(ValueObjectKeys.VALUE);
		tempValueMap.remove(ValueObjectKeys.BUSINESSDATE);
		tempValueMap.remove(ValueObjectKeys.BUSINESSTIME);
		tempValueMap.remove(ValueObjectKeys.RRN);
		valuHashMap.remove(ValueObjectKeys.RRN);
		valuHashMap.remove(ValueObjectKeys.BUSINESSDATE);
		valuHashMap.remove(ValueObjectKeys.BUSINESSTIME);
	}
	
	
	public List<Map<String, Object>> fetchReplaceRenew (Map<String, Object> valProcessMap)
	{
		
		
		
		List<Map<String, Object>> cardTypeValueList =getJdbcTemplate().queryForList(QueryConstants.QRYFORPROCESS,(String)valProcessMap.get(FSAPIConstants.ORDERID));
		
		ListIterator list=cardTypeValueList.listIterator();
		
		List<Map<String,Object>> cardDetails=new ArrayList<>();
		
		while(list.hasNext()){
			Map<String,Object> rsProcess=(Map<String,Object>)list.next();
			
			Map<String,Object> replaceRenew = new HashMap<>();

				
				replaceRenew.put(ValueObjectKeys.TYPE, String.valueOf(rsProcess.get("REPLACERENEW_TYPE")));
				replaceRenew.put(ValueObjectKeys.VALUE, String.valueOf(rsProcess.get("REPLACERENEW_VALUE")));
				
				replaceRenew.put(ValueObjectKeys.P_FIRST_NAME_IN, String.valueOf(rsProcess.get("FIRST_NAME")));
				replaceRenew.put(ValueObjectKeys.P_MIDDLEINITIAL_IN, String.valueOf(rsProcess.get("MIDDLE_NAME")));
				replaceRenew.put(ValueObjectKeys.P_LAST_NAME_IN, String.valueOf(rsProcess.get("LAST_NAME")));
				replaceRenew.put(ValueObjectKeys.P_EMAIL_IN, String.valueOf(rsProcess.get("EMAIL")));
				replaceRenew.put(ValueObjectKeys.P_PHONE_IN, String.valueOf(rsProcess.get("PHONE")));
				replaceRenew.put(ValueObjectKeys.P_ADDRESSLINE_ONE_IN, String.valueOf(rsProcess.get("ADDRESS_LINEONE")));
				replaceRenew.put(ValueObjectKeys.P_ADDRESSLINE_TWO_IN, String.valueOf(rsProcess.get("ADDRESS_LINETWO")));
				replaceRenew.put(ValueObjectKeys.P_ADDRESSLINE_THREE_IN, String.valueOf(rsProcess.get("ADDRESS_LINETHREE")));
				replaceRenew.put(ValueObjectKeys.P_STATE_IN, String.valueOf(rsProcess.get("STATE")));
				replaceRenew.put(ValueObjectKeys.P_CITY_IN, String.valueOf(rsProcess.get("CITY")));
				replaceRenew.put(ValueObjectKeys.P_COUNTRY_IN, String.valueOf(rsProcess.get("COUNTRY")));
				replaceRenew.put(ValueObjectKeys.P_POSTAL_CODE_IN, String.valueOf(rsProcess.get("POSTAL_CODE")));
				replaceRenew.put(ValueObjectKeys.P_COMMENTS_IN, String.valueOf(rsProcess.get("COMMENTS")));
				replaceRenew.put(ValueObjectKeys.P_REQUEST_REASON_IN, String.valueOf(rsProcess.get("REQUEST_REASON")));
				replaceRenew.put(ValueObjectKeys.P_SHIPPINGMETHOD_IN, String.valueOf(rsProcess.get("SHIPING_METHOD")));
				replaceRenew.put(ValueObjectKeys.P_ISFEEWAIVED_IN, String.valueOf(rsProcess.get("FEE_WAIVED"))); 
				replaceRenew.put(ValueObjectKeys.P_SHIP_COMPANYNAME_IN, String.valueOf(rsProcess.get("SHIP_COMPANYNAME")));
				
				cardDetails.add(replaceRenew);
				
				}
		
		return cardDetails;
				
	}
			

	
	@Async
	public void cardReplaceRenewal(Map<String, Object> valProcessMap, Map<String, String> reqHeaders) {

		Map<String, String> valuHashMap=new HashMap<>();

		List<Map<String,String>>   responseList=new ArrayList<>();

		Map<String,Object> postBackResMap=new HashMap<>();
		try{
	
			List<Map<String, Object>> list = fetchReplaceRenew(valProcessMap);
			
			ListIterator replaceRenewList =list.listIterator();
			
			
			
			while(replaceRenewList.hasNext()){
				Map<String,String> replaceRenew= (Map<String, String>) replaceRenewList.next();
				Map<String,String>  resposnseMap=new HashMap<>();
						try{

							valuHashMap.put(ValueObjectKeys.TYPE, String.valueOf(replaceRenew.get(ValueObjectKeys.TYPE)));
							valuHashMap.put(ValueObjectKeys.VALUE, String.valueOf(replaceRenew.get(ValueObjectKeys.VALUE)));
							valuHashMap.put(ValueObjectKeys.DELIVERYCHNL, String.valueOf(valProcessMap.get(ValueObjectKeys.DELIVERYCHNL)));
							valuHashMap.put(ValueObjectKeys.TRANS_CODE, String.valueOf(valProcessMap.get(ValueObjectKeys.TRANS_CODE)));
							valuHashMap.put(ValueObjectKeys.IS_FINANCIAL, String.valueOf(valProcessMap.get(ValueObjectKeys.IS_FINANCIAL)));
							valuHashMap.put(ValueObjectKeys.CREDIT_DEBIT_INDICATOR, String.valueOf(valProcessMap.get(ValueObjectKeys.CREDIT_DEBIT_INDICATOR)));
							valuHashMap.put(ValueObjectKeys.X_INCFS_PARTNERID, String.valueOf(valProcessMap.get(ValueObjectKeys.X_INCFS_PARTNERID)));
							valuHashMap.put(ValueObjectKeys.TRANSACTIONDESC, String.valueOf(valProcessMap.get(ValueObjectKeys.TRANSACTIONDESC)));
							valuHashMap.put(ValueObjectKeys.BUSINESS_DATE, (String)valuHashMap.get(ValueObjectKeys.BUSINESS_DATE));
							valuHashMap.put(ValueObjectKeys.BUSINESS_TIME, (String)valuHashMap.get(ValueObjectKeys.BUSINESS_TIME));
							valuHashMap.put(FSAPIConstants.CORRELATIONID, String.valueOf(valProcessMap.get(ValueObjectKeys.X_INCFS_CORRELATIONID)));
							
							utils.getCardDetls(valuHashMap);
							
							Map<String,Object> checkCardStatus=new HashMap<>();
							checkCardStatus.put(ValueObjectKeys.CARDSTATUS,valuHashMap.get(ValueObjectKeys.CARDSTATUS));
							checkCardStatus.put(ValueObjectKeys.PROD_CODE,valuHashMap.get(ValueObjectKeys.PROD_CODE));
							checkCardStatus.put(ValueObjectKeys.TRANS_SHORT_NAME,valProcessMap.get(ValueObjectKeys.TRANS_SHORT_NAME));
							checkCardStatus.put(ValueObjectKeys.X_INCFS_CHANNEL,valProcessMap.get(ValueObjectKeys.X_INCFS_CHANNEL));
							checkCardStatus.put(ValueObjectKeys.HASH_CARDNO,valuHashMap.get(ValueObjectKeys.HASH_CARDNO));
							
						
							
							String cardStatusRespCode =fsapiUtils.validateCardStatus(checkCardStatus);
							valuHashMap.put(ValueObjectKeys.FSAPIRESPONSECODE,cardStatusRespCode);
							
							//check product validity
							if(!CCLPConstants.EXPIRED_PRODUCT_CARD_STATUS.equals(checkCardStatus.get(ValueObjectKeys.CARDSTATUS)) 
									&& B2BResponseCode.SUCCESS.equals(valuHashMap.get(ValueObjectKeys.FSAPIRESPONSECODE)) ){
								utils.checkProductValidity(checkCardStatus);
								valuHashMap.put(ValueObjectKeys.FSAPIRESPONSECODE,String.valueOf(checkCardStatus.get(ValueObjectKeys.FSAPIRESPONSECODE)));
							}
							

							Map<String,Object> respMap = new HashMap<>();
							if(B2BResponseCode.SUCCESS.equals(valuHashMap.get(ValueObjectKeys.FSAPIRESPONSECODE))) {
					
							SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(getJdbcTemplate()).withSchemaName("CLP_ORDER").withCatalogName("B2BAPI")
									.withProcedureName("CARD_REPLACERENEWAL");
							Map<String, Object> inParamMap = new HashMap<>();

							inParamMap.put("p_card_no_in",valuHashMap.get(FSAPIConstants.REPLACEMENT_CARDNUMBER));  // Customer Card Number
							inParamMap.put("p_msg_in",FSAPIConstants.FINANCIAL_MESSAGE_TYPE);//FSAPIConstants.NON_FINANCIAL_MSGTYPE  // Message Type
							inParamMap.put("p_txn_mode_in",FSAPIConstants.TRANS_MODE); // TRANS_MODE
							inParamMap.put("p_curr_code_in",valuHashMap.get(ValueObjectKeys.CURRENCY_CODE)); // Currency Code
							inParamMap.put("p_first_name_in", replaceRenew.get(ValueObjectKeys.P_FIRST_NAME_IN));
							inParamMap.put("p_middleinitial_in", replaceRenew.get(ValueObjectKeys.P_MIDDLEINITIAL_IN));
							inParamMap.put("p_last_name_in", replaceRenew.get(ValueObjectKeys.P_LAST_NAME_IN));
							inParamMap.put("p_email_in", replaceRenew.get(ValueObjectKeys.P_EMAIL_IN));
							inParamMap.put("p_phone_in", replaceRenew.get(ValueObjectKeys.P_PHONE_IN));
							inParamMap.put("p_addressline_one_in", replaceRenew.get(ValueObjectKeys.P_ADDRESSLINE_ONE_IN));
							inParamMap.put("p_addressline_two_in", replaceRenew.get(ValueObjectKeys.P_ADDRESSLINE_TWO_IN));
							inParamMap.put("p_addressline_three_in", replaceRenew.get(ValueObjectKeys.P_ADDRESSLINE_THREE_IN));
							inParamMap.put("p_state_in", replaceRenew.get(ValueObjectKeys.P_STATE_IN));
							inParamMap.put("p_city_in", replaceRenew.get(ValueObjectKeys.P_CITY_IN));
							inParamMap.put("p_country_in", replaceRenew.get(ValueObjectKeys.P_COUNTRY_IN));
							inParamMap.put("p_postal_code_in", replaceRenew.get(ValueObjectKeys.P_POSTAL_CODE_IN));
							inParamMap.put("p_comments_in", replaceRenew.get(ValueObjectKeys.P_COMMENTS_IN));
							inParamMap.put("p_request_reason_in", replaceRenew.get(ValueObjectKeys.P_REQUEST_REASON_IN));
							inParamMap.put("p_shippingmethod_in", replaceRenew.get(ValueObjectKeys.P_SHIPPINGMETHOD_IN));
							inParamMap.put("p_isfeewaived_in", replaceRenew.get(ValueObjectKeys.P_ISFEEWAIVED_IN)); 
							inParamMap.put("p_fsapi_channel_in", (String)(valProcessMap.get(FSAPIConstants.ORDER_CHNL_ID))); // FSAPI channel
							inParamMap.put("p_stan_in","1"); //(String)(valProcessMap.get(FSAPIConstants.STAN))  // System Trace Audit Number
							inParamMap.put("p_mbr_numb_in",FSAPIConstants.MEMBER_NUMBER);  //Member Number
							inParamMap.put("p_rvsl_code_in",FSAPIConstants.REVERSAL_CODE);  //
							inParamMap.put("p_ship_companyname_in", replaceRenew.get(ValueObjectKeys.P_SHIP_COMPANYNAME_IN));
							inParamMap.put("p_correlation_id_in", valuHashMap.get(FSAPIConstants.CORRELATIONID));


							SqlParameterSource in = new MapSqlParameterSource(inParamMap);
								respMap =  simpleJdbcCall.execute(in);	
							log.info("CARD_REPLACERENEWAL resp: "+respMap);


							log.debug("expiry date ::"+respMap.get(ValueObjectKeys.P_CARD_EXPIRTY_DATE_OUT));
							log.debug("balance  ::"+respMap.get(ValueObjectKeys.P_AVAILABLE_BALANCE_OUT));
							log.debug("last 4digits of card ::"+respMap.get(ValueObjectKeys.P_LAST4DIGITS_PAN_OUT));
							log.debug("card fee ::"+respMap.get(ValueObjectKeys.P_CARD_FEE_OUT));
							log.debug(" Response Code ::"+respMap.get(ValueObjectKeys.P_RESP_CODE_OUT));
							log.debug("Response Message ::"+respMap.get(ValueObjectKeys.P_RESP_MESSGE_OUT));


							log.info("expiry date ::"+respMap.get(ValueObjectKeys.P_CARD_EXPIRTY_DATE_OUT));
							log.info("balance  ::"+respMap.get(ValueObjectKeys.P_AVAILABLE_BALANCE_OUT));
							log.info("last 4digits of card ::"+respMap.get(ValueObjectKeys.P_LAST4DIGITS_PAN_OUT));
							log.info("card fee ::"+respMap.get(ValueObjectKeys.P_CARD_FEE_OUT));
							log.info(" Response Code ::"+respMap.get(ValueObjectKeys.P_RESP_CODE_OUT));
							log.info("Response Message ::"+respMap.get(ValueObjectKeys.P_RESP_MESSGE_OUT));


							if(FSAPIConstants.PROXYNUMBER.equalsIgnoreCase(String.valueOf(replaceRenew.get(ValueObjectKeys.TYPE)))){
								resposnseMap.put(FSAPIConstants.PROXYNUMBER, String.valueOf(replaceRenew.get(ValueObjectKeys.VALUE)));
								resposnseMap.put(FSAPIConstants.SERIALNUMBER, (String)valuHashMap.get(FSAPIConstants.RESP_SERIAL_NUMBER));
							}else if(FSAPIConstants.SERIALNUMBER.equalsIgnoreCase(String.valueOf(replaceRenew.get(ValueObjectKeys.TYPE)))){
								resposnseMap.put(FSAPIConstants.SERIALNUMBER,String.valueOf(replaceRenew.get(ValueObjectKeys.VALUE)));
								resposnseMap.put(FSAPIConstants.PROXYNUMBER, (String)valuHashMap.get(FSAPIConstants.RESP_PRXOY_NUMBER));
							}
							if(FSAPIConstants.SUCCESS_RESPONSE.equals(String.valueOf(respMap.get(ValueObjectKeys.P_RESP_CODE_OUT)))){
								resposnseMap.put(FSAPIConstants.REPLACEMENT_PANLASTFOUR, String.valueOf(respMap.get(ValueObjectKeys.P_LAST4DIGITS_PAN_OUT)));
								resposnseMap.put(FSAPIConstants.REPLACEMENT_EXPDATE, String.valueOf(respMap.get(ValueObjectKeys.P_CARD_EXPIRTY_DATE_OUT)));
								resposnseMap.put(ValueObjectKeys.FSAPIRESPONSECODE, String.valueOf(respMap.get(ValueObjectKeys.P_RESP_CODE_OUT)));
								resposnseMap.put(ValueObjectKeys.RESPONSEMESSAGE, FSAPIConstants.SUCCESS_MSG);
							}else{
								resposnseMap.put(ValueObjectKeys.FSAPIRESPONSECODE, String.valueOf(respMap.get(ValueObjectKeys.P_RESP_CODE_OUT)));
								resposnseMap.put(ValueObjectKeys.RESPONSEMESSAGE, String.valueOf(respMap.get(ValueObjectKeys.P_RESP_MESSGE_OUT)));
							}
							resposnseMap.put(FSAPIConstants.REPLACEMENT_FEE, Util.isEmpty(respMap.get(ValueObjectKeys.P_CARD_FEE_OUT)+"")?"0.0":respMap.get(ValueObjectKeys.P_CARD_FEE_OUT)+"");
							resposnseMap.put(ValueObjectKeys.AVAILABLEBALANCE, Util.isEmpty(respMap.get(ValueObjectKeys.P_AVAILABLE_BALANCE_OUT)+"")?"0.0":respMap.get(ValueObjectKeys.P_AVAILABLE_BALANCE_OUT)+"");
							}
							
					else {
						
							Map<String,Object> valObj=new HashMap<>();
							for(Entry<String, String> cardTemp : valuHashMap.entrySet()){
								valObj.put(cardTemp.getKey(), cardTemp.getValue());
							}
							valObj.put(ValueObjectKeys.FSAPIRESPONSECODE, cardStatusRespCode);
							utils.getRRN(valObj,  FSAPIConstants.RELOADRRNSEQ);
							utils.logTxnDtls(valObj, FSAPIConstants.INVALID_CARD_STATE, cardStatusRespCode);
							
							if(FSAPIConstants.PROXYNUMBER.equalsIgnoreCase(replaceRenew.get(ValueObjectKeys.TYPE))){
								resposnseMap.put(FSAPIConstants.PROXYNUMBER, replaceRenew.get(ValueObjectKeys.VALUE));
								resposnseMap.put(FSAPIConstants.SERIALNUMBER, (String)valuHashMap.get(FSAPIConstants.RESP_SERIAL_NUMBER));
							}else if(FSAPIConstants.SERIALNUMBER.equalsIgnoreCase(replaceRenew.get(ValueObjectKeys.TYPE))){
								resposnseMap.put(FSAPIConstants.SERIALNUMBER, replaceRenew.get(ValueObjectKeys.VALUE));
								resposnseMap.put(FSAPIConstants.PROXYNUMBER, (String)valuHashMap.get(FSAPIConstants.RESP_PRXOY_NUMBER));
							}
							resposnseMap.put(ValueObjectKeys.AVAILABLEBALANCE, Util.isEmpty(respMap.get(ValueObjectKeys.P_AVAILABLE_BALANCE_OUT)+"")?"0.0":respMap.get(ValueObjectKeys.P_AVAILABLE_BALANCE_OUT)+"");
							resposnseMap.put(ValueObjectKeys.FSAPIRESPONSECODE, B2BResponseCode.INVALID_CARD_STATE);
							resposnseMap.put(ValueObjectKeys.RESPONSEMESSAGE, FSAPIConstants.INVALID_CARD_STATE);
							
													
							}
						}catch(Exception exp){
							log.debug(" exp in while loop"+exp);
							if(FSAPIConstants.PROXYNUMBER.equalsIgnoreCase(replaceRenew.get(ValueObjectKeys.TYPE))){
								resposnseMap.put(FSAPIConstants.PROXYNUMBER, replaceRenew.get(ValueObjectKeys.VALUE));
								resposnseMap.put(FSAPIConstants.SERIALNUMBER, (String)valuHashMap.get(FSAPIConstants.RESP_SERIAL_NUMBER));
							}else if(FSAPIConstants.SERIALNUMBER.equalsIgnoreCase(replaceRenew.get(ValueObjectKeys.TYPE))){
								resposnseMap.put(FSAPIConstants.SERIALNUMBER, replaceRenew.get(ValueObjectKeys.VALUE));
								resposnseMap.put(FSAPIConstants.PROXYNUMBER, (String)valuHashMap.get(FSAPIConstants.RESP_PRXOY_NUMBER));
							}
							resposnseMap.put(ValueObjectKeys.FSAPIRESPONSECODE, FSAPIConstants.FAILURE_RESP_CODE);
							resposnseMap.put(ValueObjectKeys.RESPONSEMESSAGE, FSAPIConstants.SYSTEM_ERROR);

						}
						 responseList.add(resposnseMap);
			}
			postBackResMap.put(FSAPIConstants.CARDS, responseList);
			String postBackResponse=String.valueOf(valProcessMap.get(ValueObjectKeys.POSTBACKRESPONSE));
			if(FSAPIConstants.ORDER_FSAPI_TRUE.equalsIgnoreCase(postBackResponse) || FSAPIConstants.ORDER_FSAPI_TRUE1.equals(postBackResponse)){
				Map<String, Object> valueResMap = apiHelper.setResValue(FSAPIConstants.REPLACERELOAD_PTBACK+FSAPIConstants.POST, postBackResMap);
				valueResMap.put(FSAPIConstants.POSTBACK_URL, String.valueOf(valProcessMap.get(FSAPIConstants.POSTBACK_URL)));

				
				
				JSONObject resJsonObj = JsonHelper.getJsonFromMap(valueResMap);

				String res = FSAPIConstants.ORDER_EMPTY_STRING;
				if (resJsonObj != null) {
					res = resJsonObj.toString();
				}
				valProcessMap.put(FSAPIConstants.POSTBACK_APINAME, FSAPIConstants.REPLACERELOAD_PTBACK);
				logger.info("postback Request sent Started "+valProcessMap);
					reqResLogger.postBackLogger(valProcessMap, res,0,String.valueOf(new JSONObject(reqHeaders)),FSAPIConstants.REPLACERELOAD_PTBACK);
				
			}
		}catch(Exception exp){
			log.info("Exception in card replacement",exp);	
			log.error("Exception while calling card replacement"+exp);	

		}
	}
	
	@SuppressWarnings("unchecked")
	@Async
	public  void loggingCardStatusInquiry(Map<String,Object> valueObject){
		
		try{
		List<Map<String, Object>> tempItemList = (List<Map<String, Object>>) valueObject.get("cards");
		
		 for(Map<String, Object> serialOuterMap : tempItemList) {
		

			
			Map<String,Object> serialMap = new HashMap<>();
			
			utils.getSerialDtlsForCardStatus(serialOuterMap,serialMap);
			
			//Product expiry check
			if(!CCLPConstants.EXPIRED_PRODUCT_CARD_STATUS.equals(serialMap.get(ValueObjectKeys.CARDSTATUS))){
				utils.checkProductValidity(serialMap);
				if(!B2BResponseCode.SUCCESS.equalsIgnoreCase(String.valueOf(serialMap.get(ValueObjectKeys.FSAPIRESPONSECODE)))){
					valueObject.put(ValueObjectKeys.CARDSTATUS, CCLPConstants.EXPIRED_PRODUCT_CARD_STATUS);
					valueObject.put(ValueObjectKeys.FSAPIRESPONSECODE,B2BResponseCode.INVALID_CARD_STATE);
					valueObject.put(ValueObjectKeys.RESPONSEMESSAGE, FSAPIConstants.EXPIRED_PRODUCT);
				}
			}
			
			
			if(!serialMap.isEmpty()) {
			serialMap.put(ValueObjectKeys.DELIVERYCHNL, valueObject.get(ValueObjectKeys.DELIVERYCHNL));
			serialMap.put(ValueObjectKeys.TRANS_CODE, valueObject.get(ValueObjectKeys.TRANS_CODE));
			serialMap.put(ValueObjectKeys.MSGTYPE, valueObject.get(ValueObjectKeys.MSGTYPE));
			serialMap.put(FSAPIConstants.ORDER_AMOUNT, valueObject.get(FSAPIConstants.ORDER_AMOUNT));
			serialMap.put(ValueObjectKeys.X_INCFS_CORRELATIONID, valueObject.get(ValueObjectKeys.X_INCFS_CORRELATIONID));
			
			
			serialMap.put(ValueObjectKeys.X_INCFS_PARTNERID, valueObject.get(ValueObjectKeys.X_INCFS_PARTNERID));
			serialMap.put(ValueObjectKeys.IS_FINANCIAL, valueObject.get(ValueObjectKeys.IS_FINANCIAL));
			serialMap.put(ValueObjectKeys.CREDIT_DEBIT_INDICATOR, valueObject.get(ValueObjectKeys.CREDIT_DEBIT_INDICATOR));
			serialMap.put(ValueObjectKeys.TRANSACTIONDESC, valueObject.get(ValueObjectKeys.TRANSACTIONDESC));
			serialMap.put(ValueObjectKeys.BUSINESS_DATE, valueObject.get(ValueObjectKeys.BUSINESS_DATE));
			serialMap.put(ValueObjectKeys.BUSINESS_TIME, valueObject.get(ValueObjectKeys.BUSINESS_TIME));
			
			
			
			utils.getRRN(serialMap, FSAPIConstants.RELOADRRNSEQ);
			utils.logTxnDtls(serialMap, valueObject.get(ValueObjectKeys.RESPONSEMESSAGE)+"", valueObject.get(ValueObjectKeys.FSAPIRESPONSECODE)+"");
			}
		}
		 
		}catch(ServiceException e){
			log.error("ServiceException in loggingCardStatusInquiry",e);
		}
		catch (Exception e) {
			log.error("Exception in loggingCardStatusInquiry",e);
		}
	}
	
	public void populatePostBackVal(Map<String,String> tempValueMap,Map<String, Object> valuHashMap,Map<String,String> cardlst,String responseCode,String availableBalance) throws ServiceException{
		CSSResponseCode respCode = responseBuilder.getCSSResponseCodeByRespId(valuHashMap.get(ValueObjectKeys.DELIVERYCHNL)+"",responseCode);
		cardlst.put(ValueObjectKeys.FSAPIRESPONSECODE, respCode.getChannelResponseCode());
		cardlst.put(ValueObjectKeys.RESPONSEMESSAGE, respCode.getResponseDescription());
		if(FSAPIConstants.PROXYNUMBER.equalsIgnoreCase(tempValueMap.get(ValueObjectKeys.TYPE))){
		cardlst.put(tempValueMap.get(ValueObjectKeys.TYPE), tempValueMap.get(ValueObjectKeys.VALUE));
		cardlst.put(FSAPIConstants.SERIALNUMBER, (String)tempValueMap.get(FSAPIConstants.RESP_SERIAL_NUMBER));
		}else if(FSAPIConstants.SERIALNUMBER.equalsIgnoreCase(tempValueMap.get(ValueObjectKeys.TYPE))){
			cardlst.put(tempValueMap.get(ValueObjectKeys.TYPE), tempValueMap.get(ValueObjectKeys.VALUE));
			cardlst.put(FSAPIConstants.PROXYNUMBER, (String)tempValueMap.get(FSAPIConstants.RESP_PRXOY_NUMBER));
		 }
		cardlst.put(ValueObjectKeys.AVAILABLEBALANCE, availableBalance);
	}
	
	@Override
	@Async
	public ResponseEntity<Object> postBackStatus(String payload,Map<String, String> reqHeaders){
		
		Map<String,Object> valuHashMap = new HashMap<>();
		
		logger.info("PostBack process Started");
		ResponseEntity<Object> resp = null;
		long startTime = System.currentTimeMillis();
		
		String reqMsg="";
		String res="";
		String responseCode = "";
		String orderID="";
		String reqHeader="";
		
		Map<String,Object> postBackReqMap=new HashMap<>();
		try {
			reqHeader=new JSONObject(reqHeaders).toString();
			JSONObject jsonObj = JsonHelper.isJSONValid(payload);
			if (jsonObj != null) {
				valuHashMap = JsonHelper.jsonToMap(jsonObj);
			}
			Thread.sleep(4000);
			orderID = String.valueOf(valuHashMap.get("orderID"));
			String partnerId= String.valueOf(valuHashMap.get("partnerID"));
			valuHashMap.put(FSAPIConstants.ORDERID, orderID);
			valuHashMap.put(FSAPIConstants.ORDER_PARTNERID, partnerId);
			logger.info("Post back trigger methodenabled");
			valuHashMap.put(ValueObjectKeys.DELIVERYCHNL,"17");
			orderValidator.getLineItemOrderStatus(valuHashMap);
			orderValidator.getLineItemDtls(valuHashMap, FSAPIConstants.ORDER_STATUS_API);
			if(Util.isEmpty(valuHashMap.get(FSAPIConstants.ORDER_RESPONSE_CODE)+"")){
				valuHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
			}
			CSSResponseCode respCode = responseBuilder.getCSSResponseCodeByRespId(valuHashMap.get(ValueObjectKeys.DELIVERYCHNL)+"",String.valueOf(valuHashMap.get(FSAPIConstants.ORDER_RESPONSE_CODE)));
			valuHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,respCode.getChannelResponseCode());
			valuHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, respCode.getResponseDescription());	
			Map<String, Object> valueResMap = apiHelper.setResValue(FSAPIConstants.ORDER_STATUS_API	+ FSAPIConstants.GET, valuHashMap);
			valueResMap.put(FSAPIConstants.POSTBACK_URL, valuHashMap.get(FSAPIConstants.POSTBACK_URL));	
			postBackReqMap.put("order", valueResMap);
			JSONObject resJsonObj = JsonHelper.getJsonFromMap(postBackReqMap);
			responseCode = String.valueOf(valuHashMap.get(FSAPIConstants.ORDER_RESPONSE_CODE));
			reqMsg=String.valueOf(postBackReqMap.toString());
			 res = FSAPIConstants.ORDER_EMPTY_STRING;
			if (resJsonObj != null) {
				res = resJsonObj.toString();
			}
			
			logger.info("resJsonObj*****:"+resJsonObj);
		} catch (ServiceException se) {
			logger.debug("ServiceException" + se);
			valuHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, se.getCode());
			valuHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, se.getMessage());
		} catch (Exception e) {
			logger.debug("Exception" + e);
			valuHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,
					B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
			valuHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG,
					B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
		}
		
		finally {
			final long endTime = System.currentTimeMillis();
			final long timeTaken = endTime - startTime;
			logger.debug("Total Time Taken......" + timeTaken);
			logger.info("responsePostBackLogger called from postBackStatus method");
			orderProcessDAO.responsePostBackLogger(FSAPIConstants.ORDER_API, orderID, reqHeader, res, responseCode, null, null, null, timeTaken, null);
			logger.info("ended postBackStatus method");
		}
		
		return resp;
		
		
	}
	
	

	@Override
	public void loadPostBackUrl(String pstbkOrderActStatus,String postBackPropKey){
		orderProcessDAO.deletePostBackUrl();
		orderProcessDAO.updatePostBackUrl(postBackPropKey, pstbkOrderActStatus);
	}

}
