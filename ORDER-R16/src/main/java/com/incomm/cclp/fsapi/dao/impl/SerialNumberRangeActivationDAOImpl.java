package com.incomm.cclp.fsapi.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.fsapi.constants.B2BResponseCode;
import com.incomm.cclp.fsapi.constants.B2BResponseMessage;
import com.incomm.cclp.fsapi.constants.FSAPIConstants;
import com.incomm.cclp.fsapi.dao.SerialNumberRangeActivationDAO;
import com.incomm.cclp.fsapi.helper.APIHelper;
import com.incomm.cclp.fsapi.helper.FSAPIUtils;
import com.incomm.cclp.fsapi.helper.OrderValidator;
import com.incomm.cclp.fsapi.service.OrderProcessService;
@Repository
public class SerialNumberRangeActivationDAOImpl extends JdbcDaoSupport  implements SerialNumberRangeActivationDAO {

	private final Logger logUtil = LogManager.getLogger(this.getClass());
	@Autowired
	public void setDs(@Qualifier("orderDs") DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Autowired
	FSAPIUtils utils;
	
	@Autowired
	APIHelper apiHelper;
	
	@Autowired
	OrderValidator orderValidator;
	
	@Autowired
	OrderProcessService orderProcessService;
	
	@Override
	public Map<String, Object> serialNumberRangeActivation(Map<String, Object> valueMap)  {
		Map<String, Object> startObj = new HashMap<>();
		Map<String, Object> endObj = new HashMap<>();
		String headerPartnerID = null;
		String startSerialNum = null;
		String endSerialNum = null;
		try {
			logUtil.debug("inside Serial number range activation");

			startSerialNum = (String) valueMap.get("startSerialNumber");
			logUtil.debug("startSerialNum :::::::::::::: "+startSerialNum);
			endSerialNum = (String) valueMap.get("endSerialNumber");
			logUtil.debug("endSerialNum :::::::::::::::::::::: "+endSerialNum);
			startObj.put(ValueObjectKeys.SERLNUMBER, startSerialNum);
			endObj.put(ValueObjectKeys.SERLNUMBER, endSerialNum);
			
			startObj.put(ValueObjectKeys.INSTCODE,FSAPIConstants.INST_CODE);
			endObj.put(ValueObjectKeys.INSTCODE,FSAPIConstants.INST_CODE);
			valueMap.put(ValueObjectKeys.MSGTYPE,FSAPIConstants.NON_FINANCIAL_MSGTYPE);			
			valueMap.put(ValueObjectKeys.MEMBERNO, FSAPIConstants.MEMBER_NUMBER);
			valueMap.put(ValueObjectKeys.REVERSAL_CODE,FSAPIConstants.REVERSAL_CODE);
			valueMap.put(ValueObjectKeys.TXN_MODE,FSAPIConstants.TRANS_MODE);
			
			headerPartnerID =(String) valueMap.get(FSAPIConstants.ORDER_PARTNERID);
			logUtil.info("headerPartnerID :::::::::::::::::::::: "+headerPartnerID);
			
			if(startSerialNum != null && endSerialNum != null)
			{
				
			if (startSerialNum.equals(endSerialNum)) {
				logUtil.info("Start and end serial numbers are same....");
				startObj = getPanCode(startObj);
				startObj = verifyOrderDetails(startObj,headerPartnerID);
				valueMap.put(FSAPIConstants.ORDER_ORDER_ID, startObj.get(FSAPIConstants.ORDER_ORDER_ID));
				valueMap.put(FSAPIConstants.ORDER_PARTNERID, startObj.get(FSAPIConstants.ORDER_PARTNERID));
				startObj = verifyProductID(startObj);
			} else {
				logUtil.info("Start and end serial numbers are not same....");
				startObj = getPanCode(startObj);
				endObj   = getPanCode(endObj);

				startObj = verifyOrderDetails(startObj,headerPartnerID);
				valueMap.put(FSAPIConstants.ORDER_ORDER_ID, startObj.get(FSAPIConstants.ORDER_ORDER_ID));
				valueMap.put(FSAPIConstants.ORDER_PARTNERID, startObj.get(FSAPIConstants.ORDER_PARTNERID));
				valueMap.put(FSAPIConstants.ORDER_LINE_ITEM_ID, endObj.get(FSAPIConstants.ORDER_LINE_ITEM_ID));
				
				endObj   = verifyOrderDetails(endObj,headerPartnerID);
				valueMap.put(FSAPIConstants.ORDER_ORDER_ID, endObj.get(FSAPIConstants.ORDER_ORDER_ID));
				valueMap.put(FSAPIConstants.ORDER_PARTNERID, endObj.get(FSAPIConstants.ORDER_PARTNERID));
				valueMap.put(FSAPIConstants.ORDER_LINE_ITEM_ID, endObj.get(FSAPIConstants.ORDER_LINE_ITEM_ID));
				
				if (startObj.get(FSAPIConstants.ORDER_ORDER_ID).equals(endObj.get(FSAPIConstants.ORDER_ORDER_ID))
						&& startObj.get(FSAPIConstants.ORDER_PARTNERID).equals(endObj.get(FSAPIConstants.ORDER_PARTNERID))) 
				{
					startObj = verifyProductID(startObj);
					endObj   = verifyProductID(endObj);
				
				if (startObj.get(FSAPIConstants.PRODUCTID) != null && endObj.get(FSAPIConstants.PRODUCTID)!= null
						&& ! startObj.get(FSAPIConstants.PRODUCTID).equals(endObj.get(FSAPIConstants.PRODUCTID))) 
					{
					//  Start and end serial numbers doesn't fall under same product Id
					throw new ServiceException(B2BResponseMessage.START_AND_END_SERIAL_NUMBERS_NOT_BELONGS_TO_SAME_PRODUCT, 
							B2BResponseCode.START_AND_END_SERIAL_NUMBERS_NOT_BELONGS_TO_SAME_PRODUCT);
					}
				}else{
					throw new ServiceException(B2BResponseMessage.START_AND_END_SERIAL_OR_PROXY_NUMBERS_NOT_BELONGS_TO_SAME_ORDER, 
							B2BResponseCode.START_AND_END_SERIAL_OR_PROXY_NUMBERS_NOT_BELONGS_TO_SAME_ORDER);
				}
			}
				getLineItemDtls(startObj,endObj,valueMap);
			}

		} catch (ServiceException servException) {
			logUtil.error("ServiceException in serialNumberRangeActivation:"+servException);
			valueMap.put(ValueObjectKeys.RESPONSEMESSAGE, servException.getMessage());
			valueMap.put(ValueObjectKeys.FSAPIRESPONSECODE, servException.getCode());
		} catch (Exception e) {
			logUtil.error("Exception in serialNumberRangeActivation:"+e);
			valueMap.put(ValueObjectKeys.RESPONSEMESSAGE, e.getMessage());
			valueMap.put(ValueObjectKeys.FSAPIRESPONSECODE, B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
		} finally {
			try {
				if(valueMap.get("responseCode") == null &&  valueMap.get("responseMessage") == null) {
					valueMap.put(ValueObjectKeys.RESPONSEMESSAGE, B2BResponseMessage.SUCCESS);
					valueMap.put(ValueObjectKeys.FSAPIRESPONSECODE, B2BResponseCode.SUCCESS);
					logUtil.info("respCode :"+valueMap.get(ValueObjectKeys.FSAPIRESPONSECODE) + "respMsg :"+valueMap.get(ValueObjectKeys.RESPONSEMESSAGE));
				} 
				
			} catch (Exception e) {
				logUtil.error("Exception Occured while setting the response.." + e);
			}
		}
		return valueMap;
	}

	public Map<String, Object> verifyOrderDetails(Map<String, Object> valObj,String headerPartnerID) throws ServiceException {
		try {
			Map<String,Object> valueMap=getJdbcTemplate().query(QueryConstants.B2B_GET_ORDER_DETAILS_BY_CARD,  new ResultSetExtractor<Map<String,Object>>(){
				Map<String,Object> valueMap=new HashMap<>();
				@Override
				public Map<String,Object> extractData(ResultSet rs) throws SQLException {
					if(rs.next()){
						valueMap.put(FSAPIConstants.ORDER_ORDER_ID, rs.getString("ORDER_ID"));
						valueMap.put(FSAPIConstants.ORDER_PARTNERID, rs.getString("PARTNER_ID"));
						valueMap.put(FSAPIConstants.ORDER_LINE_ITEM_ID, rs.getString("ORDER_LINE_ITEM_ID"));
					}
					return valueMap;
				}

			}, valObj.get(ValueObjectKeys.HASH_CARDNO));
			
			if(valueMap.isEmpty()){
				throw new ServiceException(B2BResponseMessage.INVALID_CARD_NUMBER, B2BResponseCode.INVALID_CARD_NUMBER);
			}
			else{
				valObj.putAll(valueMap);
			}
			
			if(headerPartnerID != null && valObj.get(FSAPIConstants.ORDER_PARTNERID) != null && !headerPartnerID.equalsIgnoreCase(valObj.get(FSAPIConstants.ORDER_PARTNERID)+""))
			{
				
					throw new ServiceException(B2BResponseMessage.INVALID_PARTNER_ID, B2BResponseCode.INVALID_PARTNER_ID);

			}
			Boolean outFlag=getJdbcTemplate().query(QueryConstants.B2B_CHECK_ORDERID_PARTNERID,  new ResultSetExtractor<Boolean>(){
				Boolean flag=false;
				@Override
				public Boolean extractData(ResultSet rs) throws SQLException {
					if(!rs.next()){
						flag=true;
					}
					return flag;
				}

			},(String) valObj.get(FSAPIConstants.ORDER_ORDER_ID),(String) valObj.get(FSAPIConstants.ORDER_PARTNERID));
			
			if(outFlag){
				throw new ServiceException(B2BResponseMessage.INVALID_ORDER_ID, B2BResponseCode.INVALID_ORDER_ID);
			}
			
			
		} 
	
		catch (ServiceException e) {
			logUtil.error("ServiceException in verifyOrderDetails()..." + e);
			throw e;
		} 
		
		catch (Exception e) {
			logUtil.error("Exception in verifyOrderDetails()..." + e);
			throw new ServiceException(B2BResponseMessage.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR, B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
		} 

		return valObj;

	}

	public Map<String, Object> getPanCode(Map<String, Object> valObj) throws ServiceException {
		try {
			String cardNumberHash = getJdbcTemplate().queryForObject(QueryConstants.B2B_GET_CARD_NUMBER_BY_SERIAL_NUMBER, new Object[] { (String) valObj.get(ValueObjectKeys.SERLNUMBER)}, String.class);
			if(cardNumberHash!=null){
				valObj.put(ValueObjectKeys.HASH_CARDNO, cardNumberHash);
			}
		}
		catch (EmptyResultDataAccessException e) {
			logUtil.error("EmptyResultDataAccessException in getPanCode:"+e);
			throw new ServiceException(B2BResponseMessage.INVALID_SERIAL_NUMBER, B2BResponseCode.INVALID_SERIAL_NUMBER);
		} 
		catch (Exception e) {
			logUtil.error("Exception in getPanCode:"+e);
		} 
		return valObj;
	}

	public Map<String, Object> verifyProductID(Map<String, Object> valObj) {
		try {
			String productId = getJdbcTemplate().
					queryForObject(QueryConstants.B2B_GET_PRODUCTID_BY_ORDERID,
							new Object[] { (String) valObj.get(FSAPIConstants.ORDER_ORDER_ID),(String) valObj.get(FSAPIConstants.ORDER_PARTNERID),
							(String) valObj.get(FSAPIConstants.ORDER_LINE_ITEM_ID)}, String.class);
			if(productId!=null){
				valObj.put(FSAPIConstants.PRODUCTID, productId);
			}
		}
		catch (EmptyResultDataAccessException e) {
			logUtil.error("EmptyResultDataAccessException in verifyProductID:"+e);
		} 
		catch (Exception e) {
			logUtil.error("Exception in verifyProductID:"+e);
		} 
		return valObj;
	}

	public void getSerialNumberRange(Map<String, Object> startObj, Map<String, Object> endObj,Map<String, Object> lineItemMap, String orderId, String partnerId, String lineItemId) {
		try {
			List<Map<String, String>> lineItemList =
					getJdbcTemplate().query(QueryConstants.B2B_GET_CARD_DETAILS_BY_SERIAL_NUMBER,  
							new ResultSetExtractor<List<Map<String, String>>>(){
				List<Map<String, String>> tempList = new LinkedList<>();
				@Override
				public List<Map<String, String>> extractData(ResultSet rs) throws SQLException {
					while(rs.next()){
					Map<String, String> temp = new HashMap<>();
					temp.put("proxyNumber", rs.getString("PROXY_NUMBER"));
					temp.put("pin", rs.getString("PIN"));
					temp.put("encryptedString", rs.getString("PROXY_PIN_ENCR"));
					temp.put("serialNumber", rs.getString("SERIAL_NUMBER"));
					temp.put("status", rs.getString("CARD_STATUS"));
					temp.put("trackingNumber", rs.getString("TRACKING_NO"));
					temp.put("shippingDateTime", rs.getString("SHIPPING_DATETIME"));
					temp.put("cardNumber", rs.getString("CARDNUMBER"));
					temp.put("hashCardNumber", rs.getString("CARD_NUM_HASH"));
					temp.put("expirationDate", rs.getString("EXPIRATIONDATE"));
					temp.put("custcode", rs.getString("CUSTOMER_CODE"));
					temp.put("prodcode", rs.getString("PRODUCT_ID"));
					temp.put("encryptCardNumber", rs.getString("CARD_NUM_ENCR"));
					tempList.add(temp);
					}
					return tempList;
				}
				

			},orderId,partnerId,lineItemId,(String) startObj.get(ValueObjectKeys.SERLNUMBER),(String) endObj.get(ValueObjectKeys.SERLNUMBER));
			
			lineItemMap.put(FSAPIConstants.CARDS, lineItemList);
			
		
		}  catch (Exception e) {
			logUtil.error("Exception while loading getSerialNumberRange()" + e);
		} 
	}

	
	public void updateCardInfo(Map<String, Object> valObj)  {
		String cardStatusRespCode = null;
		try 
		{
			
			utils.setDefaultValues(valObj);
			utils.getCardInfoWithCard(valObj);
			utils.getDelchannelTranCode(valObj,FSAPIConstants.ACTIVATION_API);
			
			utils.getRRN(valObj,FSAPIConstants.FSAPI_SERIALNUMBERRANGE_ACTIVATION_SEQ_NAME);
			utils.getyyyyMMdd(valObj, new java.sql.Date(new java.util.Date().getTime()));
			utils.gethhmmss(valObj, new java.util.Date());
			cardStatusRespCode =  utils.validateCardStatusForFinancialTxn(valObj);
			logUtil.info("cardStatusRespCode :::::::::"+cardStatusRespCode);
			
			//check product validity
			if(!CCLPConstants.EXPIRED_PRODUCT_CARD_STATUS.equals(valObj.get(ValueObjectKeys.CARDSTATUS))
					&& cardStatusRespCode != null && B2BResponseCode.SUCCESS.equals(cardStatusRespCode)){
				utils.checkProductValidity(valObj);
				cardStatusRespCode = String.valueOf(valObj.get(ValueObjectKeys.FSAPIRESPONSECODE));
			}
			String errormsg="";
			String responseCode="";
			if(cardStatusRespCode != null && B2BResponseCode.SUCCESS.equals(cardStatusRespCode) ) 
			{
				if((String)valObj.get(ValueObjectKeys.CARDSTATUS)!= null && "1".equals((String)valObj.get(ValueObjectKeys.CARDSTATUS))){
					errormsg=FSAPIConstants.CARD_ACTIVATION_ALREADY_DONE;
					responseCode=B2BResponseCode.ACTIVATION_ALEREDY_DONE;
				}else if((String)valObj.get(ValueObjectKeys.CARDSTATUS)!= null && !"0".equals((String)valObj.get(ValueObjectKeys.CARDSTATUS))){
					errormsg=FSAPIConstants.INVALID_CARD_STATE;
		    		responseCode=B2BResponseCode.INVALID_CARD_STATE;
				} 
				else if((String)valObj.get(ValueObjectKeys.CARDSTATUS)!= null  && B2BResponseCode.SUCCESS.equals(cardStatusRespCode)) {
					utils.updateCardInfo(valObj);
					valObj.put(ValueObjectKeys.CARDSTATUS,FSAPIConstants.ACTIVE_CARDSTAT);
					errormsg=FSAPIConstants.SUCCESS_MSG;
					responseCode=FSAPIConstants.SUCCESS_RESPONSE1;
					utils.genCVV(valObj, "cvv2");
					utils.checkFundingOption(valObj);

				}
			}else{
				valObj.put(ValueObjectKeys.FSAPIRESPONSECODE, cardStatusRespCode);
				errormsg=FSAPIConstants.INVALID_CARD_STATE;
	    		responseCode=B2BResponseCode.INVALID_CARD_STATE;
				
				logUtil.error("Valid Card Status Check Failed..." );
			}
			if(!valObj.containsKey(ValueObjectKeys.TRAN_LOG_UPDATE)){
				utils.logTxnDtls(valObj, errormsg, responseCode);
			}
			
		} catch(Exception e) {
			
			logUtil.error("Exception Occured while updating card status details..." + e);
		}
	
	}


	public void logAPIRequestDtls(Map<String, Object> valObj,String respMsg)  {
		try { 
			utils.getRRN(valObj,FSAPIConstants.FSAPI_SERIALNUMBERRANGE_ACTIVATION_SEQ_NAME);
			int updateCount=getJdbcTemplate().update(QueryConstants.FSAPI_SERIAL_RANGE_INFO,  (String) valObj.get(ValueObjectKeys.RRN),(String) valObj.get(FSAPIConstants.STARTSERIALNUMBER)
					,(String) valObj.get(FSAPIConstants.ENDSERIALNUMBER),String.valueOf(valObj.get(FSAPIConstants.RESP_HERDER_POSTBACKURL)),String.valueOf(valObj.get(FSAPIConstants.POST_BACK_RESP))
					,respMsg);
			if (updateCount > 0) 
			{
				logUtil.debug("Records Inserted in VMS_FSAPI_RANGE_ACT_INFO  table : " + updateCount);
			} 
			}  catch(ServiceException cae) 
			{	
				logUtil.info("ServiceException when insertingin to VMS_FSAPI_RANGE_ACT_INFO : " ,cae);
				logUtil.error("ServiceException when inserting in to VMS_FSAPI_RANGE_ACT_INFO : " +cae.getMessage()); 

			}
	}
	
	public void getLineItemDtls(Map<String, Object> startObj, Map<String, Object> endObj,Map<String, Object> valueMap) throws ServiceException {

		String  orderId= (String) startObj.get(FSAPIConstants.ORDER_ORDER_ID);
		String partnerId =(String) startObj.get(FSAPIConstants.ORDER_PARTNERID);
		try {
			List<Map<String, Object>> lineItemList =
					getJdbcTemplate().query(QueryConstants.B2B_GET_LINE_ITEM_DETAILS_BY_SERIAL_NUMBER,  new ResultSetExtractor<List<Map<String, Object>>>(){
				String orderStatus = null;
				List<Map<String, Object>> lineItemList = new LinkedList<>();
				@Override
				public List<Map<String, Object>> extractData(ResultSet rs) throws SQLException {
					while(rs.next()){
						Map<String,Object> lineItemMap = new HashMap<>();
						String lineItemId = rs.getString("lineItemID");
						lineItemMap.put(FSAPIConstants.ORDER_LINE_ITEM_ID,lineItemId );
						lineItemMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, FSAPIConstants.SUCCESS_RESP_CODE);
						lineItemMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, FSAPIConstants.SUCCESS_MSG);
						lineItemMap.put(FSAPIConstants.ORDER_RESPONSE_STATUS, rs.getString("OLI_ORDER_STATUS"));
						getSerialNumberRange(startObj,endObj,lineItemMap,orderId,partnerId,lineItemId);
						lineItemList.add(lineItemMap);
						if(orderStatus == null){
							orderStatus = rs.getString("OD_ORDER_STATUS");
							valueMap.put(FSAPIConstants.ORDER_RESPONSE_STATUS, orderStatus);
						}
					}
					return lineItemList;
				}
				

			},orderId,partnerId,(String) startObj.get(ValueObjectKeys.SERLNUMBER),(String) endObj.get(ValueObjectKeys.SERLNUMBER));
			
			if(lineItemList.isEmpty()){
				throw new ServiceException(B2BResponseMessage.INVALID_SERIAL_NUMBER, 
						B2BResponseCode.INVALID_SERIAL_NUMBER);
			}
			valueMap.put(FSAPIConstants.ORDER_LINE_ITEMDTLS, lineItemList);
			
			logUtil.info("--------getLineItemDtls---------");
		}
		catch (ServiceException s) {
			logUtil.error("ServiceException Occured while loading getLineItemDtls()..." , s);
			throw s;
		}
		catch (Exception e) {
			logUtil.error("Exception while loading getLineItemDtls()" + e);
		}
	}
	
	

}
