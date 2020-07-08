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
import com.incomm.cclp.fsapi.dao.ProxyNumberActivationDAO;
import com.incomm.cclp.fsapi.helper.FSAPIUtils;
import com.incomm.cclp.fsapi.helper.OrderValidator;
import com.incomm.cclp.fsapi.helper.ReqResLogger;
import com.incomm.cclp.fsapi.response.ResponseBuilder;
import com.incomm.cclp.fsapi.service.CommonService;
import com.incomm.cclp.fsapi.service.TransactionService;
@Repository
public class ProxyNumberActivationDAOImpl extends JdbcDaoSupport  implements ProxyNumberActivationDAO{
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

	HashMap<String, String> pramMap = null;
	/**
	 * proxyNumber Activation DB Process
	 */
	@Override
	public Map<String, Object> proxyNumberActivation(Map<String, Object> valueHashMap,List<ErrorMsgBean> errorList) throws ServiceException {
		String respMsg = null;
		boolean cardStatusChanged = false;
		try {
			valueHashMap.put(ValueObjectKeys.MSGTYPE,FSAPIConstants.NON_FINANCIAL_MSGTYPE);			
			valueHashMap.put(ValueObjectKeys.MEMBERNO, FSAPIConstants.MEMBER_NUMBER);
			valueHashMap.put(ValueObjectKeys.REVERSAL_CODE,FSAPIConstants.REVERSAL_CODE);
			valueHashMap.put(ValueObjectKeys.TXN_MODE,FSAPIConstants.TRANS_MODE);
			fsapiUtils.getDelchannelTranCode(valueHashMap,FSAPIConstants.ACTIVATION_API);
			fsapiUtils.setDefaultValues(valueHashMap);
			fsapiUtils.getyyyyMMdd(valueHashMap,FSAPIUtils.getCurrentDate());
			fsapiUtils.gethhmmss(valueHashMap,new java.util.Date());
			getProxyDtls(valueHashMap);
			fsapiUtils.getCardInfoWithCard(valueHashMap);
			fsapiUtils.checkPartnerIdForCardAct(valueHashMap,errorList);
			//Check product validity
			
			
			if (errorList.isEmpty()) {
				fsapiUtils.checkActivationCodeForCardActivation(valueHashMap);
				if(B2BResponseCode.SUCCESS.equalsIgnoreCase(valueHashMap.get(ValueObjectKeys.FSAPIRESPONSECODE)+"")){
					valueHashMap.put(ValueObjectKeys.FSAPIRESPONSECODE,fsapiUtils.validateCardStatusForFinancialTxn(valueHashMap));
					
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
						else if(valueHashMap.get(ValueObjectKeys.CARDSTATUS)!= null && B2BResponseCode.SUCCESS.equals(valueHashMap.get(ValueObjectKeys.FSAPIRESPONSECODE))){
							fsapiUtils.updateCardInfo(valueHashMap);
							fsapiUtils.checkFundingOption(valueHashMap);
							fsapiUtils.genCVV(valueHashMap,FSAPIConstants.CVV2);
							cardStatusChanged = true;
							
						}
						
						
						
						if(cardStatusChanged){
							valueHashMap.put(ValueObjectKeys.CARDSTATUS, FSAPIConstants.ACTIVE_CARDSTAT);
						}
						Map<String,String> cardStatusDesc = transactionService.getAllCardStatus();
						valueHashMap.put("status", cardStatusDesc.get(valueHashMap.get(ValueObjectKeys.CARDSTATUS)));
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
			throw serviceException;
		} catch (Exception e) {
			log.info("exception occured"+e);
			throw new ServiceException(
					B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR,
					B2BResponseMessage.RRN_GENERATING_EXCEPTION_MSG);
		}
		finally {
			logAPIRequestDtls(valueHashMap,respMsg);
		}
		return valueHashMap;
	}

	/**
	 * getting ProxyDtls
	 * @param valueHashMap
	 * @throws ServiceException
	 */
	public void getProxyDtls(Map<String, Object> valueHashMap)throws ServiceException {

			Map<String,Object> testMap=getJdbcTemplate().query(QueryConstants.GET_PROXY_DETAILS,  new ResultSetExtractor<Map<String,Object>>(){
				@Override
				public Map<String,Object> extractData(ResultSet rs) throws SQLException {
					Map<String,Object> proxyMap=new HashMap<>();
					if(rs.next()){
						proxyMap.put(ValueObjectKeys.CARD_TYPE, rs.getString("CardType"));	
						proxyMap.put(ValueObjectKeys.PROD_CODE, rs.getString("PRODUCT_ID"));	
						proxyMap.put(ValueObjectKeys.CUST_CODE, rs.getString("CUSTOMER_CODE"));	
						proxyMap.put(ValueObjectKeys.HASH_CARDNO, rs.getString("CARD_NUM_HASH"));	
						proxyMap.put(ValueObjectKeys.CARD_EXPIRY_DATE, rs.getString("EXPDATE"));
						proxyMap.put(ValueObjectKeys.EXPIRATIONDATE, rs.getString("EXPIRATIONDATE"));	
						proxyMap.put(ValueObjectKeys.EXPIRY_DATE, rs.getString("EXPIRY_DATE"));
						proxyMap.put(ValueObjectKeys.CARDSTATUS, rs.getString("CARD_STATUS"));	
						proxyMap.put(ValueObjectKeys.CARDNUMBER, rs.getString("cardNo"));
						proxyMap.put(ValueObjectKeys.ENCR_CARDNO, rs.getString("CARD_NUM_ENCR"));
						proxyMap.put(ValueObjectKeys.ACTIVATION_CODE, rs.getString("ACTIVATION_CODE"));
					}
					return proxyMap;
				}


			},(String) valueHashMap.get("proxynumber"));

			if(testMap.isEmpty()) {
				
				throw new ServiceException(FSAPIConstants.INVALID_PRXOY_NUMBER,
						B2BResponseCode.INVALID_PROXY_NUMBER);		
			}
			else
			{
				valueHashMap.putAll(testMap);
			}

		}


	
/**
 * logging into the FSAPI_PROXY_ACTIVATION_LOG table
 * @param valObj
 * @param respMsg
 */
	private void logAPIRequestDtls(Map<String, Object> valObj,String respMsg) {
		int count = 0;

		try {
			log.info("--------responseLogger---------");

			count = getJdbcTemplate().update(QueryConstants.FSAPI_PROXY_ACTIVATION_LOG,  valObj.get(ValueObjectKeys.RRN),
					valObj.get("proxynumber"),valObj.get(ValueObjectKeys.ACTIVATION_CODE),respMsg);

			if (count > 0) {
				log.debug("Records Inserted in FSAPI_PROXY_ACTIVATION  table : " + count);
			}
		} catch (Exception e) {
			log.error("Exception when inserting into FSAPI_PROXY_ACTIVATION : " ,e); 	
		} 


	}

	public static java.sql.Date getCurrentDate(){
		return new java.sql.Date(new java.util.Date().getTime());
	}

	

	
	private int getReplacedCardOldStatus(String proxyNumber) {
		return getJdbcTemplate().queryForObject(QueryConstants.CHECK_DAMAGED_CARD_PROXY, new Object[] { proxyNumber }, Integer.class);
	}
}
