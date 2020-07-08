package com.incomm.cclp.fsapi.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.fsapi.bean.ErrorMsgBean;
import com.incomm.cclp.fsapi.constants.B2BResponseCode;
import com.incomm.cclp.fsapi.constants.B2BResponseMessage;
import com.incomm.cclp.fsapi.constants.FSAPIConstants;
import com.incomm.cclp.fsapi.dao.OrderProcessDAO;
import com.incomm.cclp.fsapi.dao.SerialNumberActivationDAO;
import com.incomm.cclp.fsapi.helper.FSAPIUtils;
import com.incomm.cclp.fsapi.helper.OrderValidator;
import com.incomm.cclp.fsapi.helper.ReqResLogger;
import com.incomm.cclp.fsapi.response.ResponseBuilder;
import com.incomm.cclp.fsapi.service.CommonService;
import com.incomm.cclp.fsapi.service.TransactionService;
@Repository
public class SerialNumberActivationDAOImpl extends JdbcDaoSupport implements SerialNumberActivationDAO{
	
	@Autowired
	OrderProcessDAO orderProcessDAO;
	@Autowired
	TransactionService transactionService;
	@Autowired
	CommonService commonService;
	@Autowired
	ResponseBuilder responseBuilder;
	@Autowired
	ReqResLogger reqResLogger;
	
	@Autowired
	public OrderValidator orderValidator;
	
	@Autowired
    public void setDs(@Qualifier("orderDs") DataSource dataSource) {
         setDataSource(dataSource);
    }
	
	private final Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	public JdbcTemplate jdbctemplate;
	
	@Autowired
	public FSAPIUtils fsapiUtils;
	
	@Override
	public Map<String, Object> serialNumberActivation(Map<String, Object> valueHashMap,List<ErrorMsgBean> errorList) throws ServiceException {

		String respMsg = null;
		boolean cardStatusChanged = false;
		try {
			valueHashMap.put(ValueObjectKeys.MSGTYPE,FSAPIConstants.NON_FINANCIAL_MSGTYPE);			
			valueHashMap.put(ValueObjectKeys.MEMBERNO, FSAPIConstants.MEMBER_NUMBER);
			valueHashMap.put(ValueObjectKeys.REVERSAL_CODE,FSAPIConstants.REVERSAL_CODE);
			valueHashMap.put(ValueObjectKeys.TXN_MODE,FSAPIConstants.TRANS_MODE);

			fsapiUtils.getDelchannelTranCode(valueHashMap,FSAPIConstants.ACTIVATION_API);
			fsapiUtils.setDefaultValues(valueHashMap);
			
			fsapiUtils.getyyyyMMdd(valueHashMap,getCurrentDate());
			fsapiUtils.gethhmmss(valueHashMap,new java.util.Date());
			getSerialDtls(valueHashMap);
			fsapiUtils.getCardInfoWithCard(valueHashMap);
			fsapiUtils.checkPartnerIdForCardAct(valueHashMap,errorList);
			
			
			if (errorList.isEmpty()) {
				fsapiUtils.checkActivationCodeForCardActivation(valueHashMap);
				if(B2BResponseCode.SUCCESS.equalsIgnoreCase(valueHashMap.get(ValueObjectKeys.FSAPIRESPONSECODE)+"")){
					valueHashMap.put(ValueObjectKeys.FSAPIRESPONSECODE,fsapiUtils.validateCardStatusForFinancialTxn(valueHashMap));
					//Check product validity
					if(!CCLPConstants.EXPIRED_PRODUCT_CARD_STATUS.equals(valueHashMap.get(ValueObjectKeys.CARDSTATUS)) 
							&& B2BResponseCode.SUCCESS.equals(valueHashMap.get(ValueObjectKeys.FSAPIRESPONSECODE))){
						fsapiUtils.checkProductValidity(valueHashMap);
					}
					
					if(B2BResponseCode.SUCCESS.equals(valueHashMap.get(ValueObjectKeys.FSAPIRESPONSECODE))){
						
						if(valueHashMap.get(ValueObjectKeys.CARDSTATUS)!= null  && "1".equals(valueHashMap.get(ValueObjectKeys.CARDSTATUS))){

							valueHashMap.put(ValueObjectKeys.FSAPIRESPONSECODE, B2BResponseCode.ACTIVATION_ALEREDY_DONE);
							valueHashMap.put(ValueObjectKeys.RESPONSEMESSAGE, FSAPIConstants.CARD_ACTIVATION_ALREADY_DONE);
						}else if(valueHashMap.get(ValueObjectKeys.CARDSTATUS)!= null  && !"0".equals(valueHashMap.get(ValueObjectKeys.CARDSTATUS))){
							valueHashMap.put(ValueObjectKeys.FSAPIRESPONSECODE, B2BResponseCode.INVALID_CARD_STATE);
							valueHashMap.put(ValueObjectKeys.RESPONSEMESSAGE, FSAPIConstants.INVALID_CARD_STATE);
						}
						else if(valueHashMap.get(ValueObjectKeys.CARDSTATUS)!= null && B2BResponseCode.SUCCESS.equals(valueHashMap.get(ValueObjectKeys.FSAPIRESPONSECODE)) ){
							fsapiUtils.updateCardInfo(valueHashMap);
							fsapiUtils.genCVV(valueHashMap,FSAPIConstants.CVV2);
							fsapiUtils.checkFundingOption(valueHashMap);
							cardStatusChanged = true;
						}

						if(cardStatusChanged){
							valueHashMap.put(ValueObjectKeys.CARDSTATUS, FSAPIConstants.ACTIVE_CARDSTAT);
							Map<String,String> cardStatusDesc = transactionService.getAllCardStatus();
							valueHashMap.put("status", cardStatusDesc.get(valueHashMap.get(ValueObjectKeys.CARDSTATUS)));
						}
						
						
					} else{
						valueHashMap.put(ValueObjectKeys.FSAPIRESPONSECODE, B2BResponseCode.INVALID_CARD_STATE);
						valueHashMap.put(ValueObjectKeys.RESPONSEMESSAGE, FSAPIConstants.INVALID_CARD_STATE);
					}
				}
			}else {
				valueHashMap.put(ValueObjectKeys.FSAPIRESPONSECODE, errorList.get(0).getRespCode());
				valueHashMap.put(ValueObjectKeys.RESPONSEMESSAGE, errorList.get(0).getErrorMsg());
			}


		}
		
		catch (ServiceException serviceException) {
			log.info("ServiceException while activating the card using serial number ",serviceException);
			throw new ServiceException(FSAPIConstants.INVALID_SERIAL_NUMBER,
					B2BResponseCode.INVALID_SERIAL_NUMBER);			}
		catch (Exception e) {
			log.info("ecxeption while activating the card using serial number "+e);
			throw new ServiceException(
					B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR,
					B2BResponseMessage.RRN_GENERATING_EXCEPTION_MSG);
		}finally{
		logAPIRequestDtls(valueHashMap,respMsg);
		
		}
		return valueHashMap;

	}
	
	/**
	 * 
	 * @param valueHashMap
	 * @throws ServiceException
	 */

	public void getSerialDtls(Map<String, Object> valueHashMap)throws ServiceException {

			Map<String, Object> testMap=getJdbcTemplate().query(QueryConstants.GET_SERIAL_DETAILS,  new ResultSetExtractor<Map<String,Object>>(){
				@Override
				public Map<String,Object> extractData(ResultSet rs) throws SQLException {
					
					Map<String,Object> serialMap = new HashMap<>();
					
					if(rs.next()){
						serialMap.put(ValueObjectKeys.CARD_TYPE, rs.getString("CardType"));	
						serialMap.put(ValueObjectKeys.PROD_CODE, rs.getString("PRODUCT_ID"));	
						serialMap.put(ValueObjectKeys.CUST_CODE, rs.getString("CUSTOMER_CODE"));	
						serialMap.put(ValueObjectKeys.HASH_CARDNO, rs.getString("CARD_NUM_HASH"));	
						serialMap.put(ValueObjectKeys.CARD_EXPIRY_DATE, rs.getString("EXPDATE"));
						serialMap.put(ValueObjectKeys.EXPIRATIONDATE, rs.getString("EXPIRATIONDATE"));	
						serialMap.put(ValueObjectKeys.EXPIRY_DATE, rs.getString("EXPIRY_DATE"));
						serialMap.put(ValueObjectKeys.CARDSTATUS, rs.getString("CARD_STATUS"));	
						serialMap.put(ValueObjectKeys.CARDNUMBER, rs.getString("cardNo"));
						serialMap.put(ValueObjectKeys.ENCR_CARDNO, rs.getString("CARD_NUM_ENCR"));
						serialMap.put(ValueObjectKeys.ACTIVATION_CODE, rs.getString("ACTIVATION_CODE"));
					}
					
					return serialMap;
				}

			},(String) valueHashMap.get("serialnumber"));
			
			
			if(testMap.isEmpty()) {
				throw new ServiceException(FSAPIConstants.INVALID_SERIAL_NUMBER,
						B2BResponseCode.INVALID_SERIAL_NUMBER);		
			}
			else
			{
				valueHashMap.putAll(testMap);
			}

		
	}
	

	

	
	private void logAPIRequestDtls(Map<String, Object> valObj,String respMsg){
			int count = 0;
			try {
				log.info("--------responseLogger---------");

				count = getJdbcTemplate().update(QueryConstants.FSAPI_SERIAL_NUMBER_ACTIVATION_LOG,   valObj.get(ValueObjectKeys.RRN),
						valObj.get("serialnumber"),valObj.get(ValueObjectKeys.ACTIVATION_CODE),respMsg);

				if (count > 0) {
					log.debug("Records Inserted in FSAPI_SERIAL_ACTIVATION  table : " + count);
				}
			} catch (Exception e) {
				log.info("Exception when inserting in to FSAPI_SERIAL_ACTIVATION : " ,e); 
				
			} 
		
			
	}
	
	public static java.sql.Date getCurrentDate(){
		return new java.sql.Date(new java.util.Date().getTime());
	}

	
	private int getReplacedCardOldStatus(String serialNumber) {
		return getJdbcTemplate().queryForObject(QueryConstants.CHECK_DAMAGED_CARD_SERIAL, new Object[] { serialNumber }, Integer.class);
	}

}

