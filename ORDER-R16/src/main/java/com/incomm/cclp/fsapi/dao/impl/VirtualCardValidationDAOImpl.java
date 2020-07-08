package com.incomm.cclp.fsapi.dao.impl;

import java.sql.Connection;
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
import com.incomm.cclp.fsapi.constants.FSAPIConstants;
import com.incomm.cclp.fsapi.dao.VirtualCardValidationDAO;
import com.incomm.cclp.fsapi.helper.FSAPIUtils;
import com.incomm.cclp.fsapi.service.TransactionService;

/**
 * 
 * @author sampathkumarl
 *
 */

@Repository
public class VirtualCardValidationDAOImpl extends JdbcDaoSupport implements VirtualCardValidationDAO {
	@Autowired
	TransactionService transactionService;

	@Autowired
	public JdbcTemplate jdbctemplate;

	@Autowired
	public FSAPIUtils fsapiUtils;

	@Autowired
	public void setDs(@Qualifier("orderDs") DataSource dataSource) {
		setDataSource(dataSource);
	}


	private final Logger log = LogManager.getLogger(this.getClass());
	Connection conn;
	HashMap<String, String> pramMap = null;
	String profileCode = null;
	String prodCode = null;
	String prodCatg = null;
	String expryChkFlag = null;
	String sweepFlag = null;
	
	/**
	 * virtualCardValidation 
	 */

	public Map<String, Object> virtualCardValidation(Map<String, Object> valueHashMap,List<ErrorMsgBean> errorList){

		boolean expryDateChanged = false;
		boolean cardStatusChanged = false;

		try {

			valueHashMap.put(ValueObjectKeys.MSGTYPE,FSAPIConstants.FINANCIAL_MESSAGE_TYPE);			
			valueHashMap.put(ValueObjectKeys.MEMBERNO, FSAPIConstants.MEMBER_NUMBER);
			valueHashMap.put(ValueObjectKeys.REVERSAL_CODE,FSAPIConstants.REVERSAL_CODE);
			valueHashMap.put(ValueObjectKeys.TXN_MODE,FSAPIConstants.TRANS_MODE);
			fsapiUtils.getyyyyMMdd(valueHashMap,FSAPIUtils.getCurrentDate());
			fsapiUtils.gethhmmss(valueHashMap,new java.util.Date());

			getVirtualCardNo(valueHashMap);
			getVirtualCardDtls(valueHashMap);

			prodCode =  (String) valueHashMap.get(ValueObjectKeys.PROD_CODE);
			prodCatg =  (String) valueHashMap.get(ValueObjectKeys.PROD_CATG);

			expryChkFlag = String.valueOf(valueHashMap.get(ValueObjectKeys.EXPIREDFLAG));

			fsapiUtils.checkPartnerIdForCardAct(valueHashMap,errorList);
			
			if (errorList.isEmpty()) {
				valueHashMap.put(ValueObjectKeys.FSAPIRESPONSECODE,fsapiUtils.validateCardStatus(valueHashMap));
				
				//check product validity
				if(!CCLPConstants.EXPIRED_PRODUCT_CARD_STATUS.equals(valueHashMap.get(ValueObjectKeys.CARDSTATUS))
						&& B2BResponseCode.SUCCESS.equals(valueHashMap.get(ValueObjectKeys.FSAPIRESPONSECODE))){
					fsapiUtils.checkProductValidity(valueHashMap);
				}
				
				if(B2BResponseCode.SUCCESS.equals(valueHashMap.get(ValueObjectKeys.FSAPIRESPONSECODE))){
					if(valueHashMap.get(ValueObjectKeys.CARDSTATUS)!= null && "0".equals(valueHashMap.get(ValueObjectKeys.CARDSTATUS))){
						if(expryChkFlag != null && expryChkFlag.equalsIgnoreCase(ValueObjectKeys.EXPRYFLAGTRUE)){
							fsapiUtils.getNewExpiryDate(valueHashMap);

							updateCardInfo(valueHashMap);
							cardStatusChanged = true;
							expryDateChanged = true;
						}
						else if(expryChkFlag != null && expryChkFlag.equalsIgnoreCase(ValueObjectKeys.EXPRYFLAGFALSE)){ 
							updateCardInfo(valueHashMap);
							cardStatusChanged = true;
						}

					}

					if(expryDateChanged){
						valueHashMap.put(ValueObjectKeys.EXPIRYDATE, valueHashMap.get(ValueObjectKeys.CARD_NEW_EXPIRYDATE));
					} 

					fsapiUtils.getPackageId(valueHashMap);
					fsapiUtils.genCVV(valueHashMap,FSAPIConstants.CVV2);

					if(cardStatusChanged){
						valueHashMap.put(ValueObjectKeys.CARDSTATUS, FSAPIConstants.ACTIVE_CARDSTAT);
					}
					Map<String,String> cardStatusDesc = transactionService.getAllCardStatus();
					valueHashMap.put("status", cardStatusDesc.get(valueHashMap.get(ValueObjectKeys.CARDSTATUS)));

				}else{
					valueHashMap.put(ValueObjectKeys.FSAPIRESPONSECODE, B2BResponseCode.INVALID_CARD_STATE);
					valueHashMap.put(ValueObjectKeys.RESPONSEMESSAGE, FSAPIConstants.INVALID_CARD_STATE);

				}
			}
			else {
				valueHashMap.put(ValueObjectKeys.FSAPIRESPONSECODE, errorList.get(0).getRespCode());
				valueHashMap.put(ValueObjectKeys.RESPONSEMESSAGE, errorList.get(0).getErrorMsg());
			}

		}
		catch (ServiceException servException) {
			log.error("ServiceException in VC Validation:"+servException);
			valueHashMap.put(ValueObjectKeys.RESPONSEMESSAGE, servException.getMessage());
			valueHashMap.put(ValueObjectKeys.FSAPIRESPONSECODE, servException.getCode());
		} catch (Exception e) {
			log.error("Exception in VC Validation:"+e);
			valueHashMap.put(ValueObjectKeys.RESPONSEMESSAGE, e.getMessage());
			valueHashMap.put(ValueObjectKeys.FSAPIRESPONSECODE, B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
		}
		return valueHashMap;

	}

/**
 * 
 * @param valueHashMap
 * @throws ServiceException
 */

	public void getVirtualCardNo(Map<String, Object> valueHashMap)throws ServiceException {

		try {
			getJdbcTemplate().query(QueryConstants.HASHCARDQUERYFORCARDNO,  new ResultSetExtractor<Map<String,Object>>(){
				@Override
				public Map<String,Object> extractData(ResultSet rs) throws SQLException {

					if(rs.next()){
						valueHashMap.put(ValueObjectKeys.HASH_CARDNO, rs.getString("CARD_NUM_HASH"));
						valueHashMap.put(FSAPIConstants.ORDER_ORDER_ID, rs.getString("ORDER_ID"));
						valueHashMap.put(FSAPIConstants.ORDER_LINE_ITEM_ID, rs.getString("ORDER_LINE_ITEM_ID"));
					}
					return valueHashMap;
				}


			},(String) valueHashMap.get("encryptedString"));


		}

		catch (Exception e) {
			log.info("Exception when getting details based on proxy number: " +e); 	
			throw new ServiceException(FSAPIConstants.CARDNOTFOUND,
					B2BResponseCode.INVALID_CARD_NUMBER);

		}

	}
/**
 * geting the VirtualCardDtls
 * @param valueHashMap
 * @throws ServiceException
 */

	public void getVirtualCardDtls(Map<String, Object> valueHashMap)throws ServiceException {

		try {
			getJdbcTemplate().query(QueryConstants.HASHCARDQUERYFORCARDDTLS,  new ResultSetExtractor<Map<String,Object>>(){
				@Override
				public Map<String,Object> extractData(ResultSet rs) throws SQLException {

					if(rs.next()){
						valueHashMap.put(ValueObjectKeys.PROD_CODE, rs.getString("PRODUCT_ID"));	
						valueHashMap.put(ValueObjectKeys.CUST_CODE, rs.getString("CUSTOMER_CODE"));		
						valueHashMap.put(ValueObjectKeys.EXPIREDFLAG, rs.getString("EXPIREDFLAG"));	
						valueHashMap.put(ValueObjectKeys.CARDSTATUS, rs.getString("CARD_STATUS"));	
						valueHashMap.put(ValueObjectKeys.CARDNUMBER, rs.getString("cardno"));
						valueHashMap.put(ValueObjectKeys.ENCR_CARDNO, rs.getString("CARD_NUM_ENCR"));
						valueHashMap.put(ValueObjectKeys.EXPIRATIONDATE, rs.getString("EXPIRATIONDATE"));
						valueHashMap.put(ValueObjectKeys.EXPIRYDATE, rs.getString("EXPIRY_DATE"));
					}
					return valueHashMap;
				}


			},(String) valueHashMap.get(ValueObjectKeys.HASH_CARDNO));

		}

		catch (Exception e) {
			log.info("Exception when getting details based on virtual card number: " +e); 	
			throw new ServiceException(FSAPIConstants.CARDNOTFOUND,
					B2BResponseCode.INVALID_CARD_NUMBER);

		}




	}

/**
 * updateCardInfo with EXPIRY_DATE based on CARD_NUM_HASH
 * @param valObj
 * @throws ServiceException
 */


	public void updateCardInfo(Map<String, Object> valObj) throws ServiceException {

		String updDateQuery = null;

		try {
			if((String)valObj.get(ValueObjectKeys.CARD_NEW_EXPIRYDATE) != null && ((String)valObj.get(ValueObjectKeys.CARDSTATUS)!= null && "0".equals((String)valObj.get(ValueObjectKeys.CARDSTATUS)))){
				updDateQuery = "CARD_STATUS=?,DATE_OF_ACTIVATION=sysdate,EXPIRY_DATE=to_date(?,'yyyy-MM-dd')";
			} else if((String)valObj.get(ValueObjectKeys.CARD_NEW_EXPIRYDATE) == null && ((String)valObj.get(ValueObjectKeys.CARDSTATUS)!= null && "0".equals((String)valObj.get(ValueObjectKeys.CARDSTATUS)))){
				updDateQuery = "CARD_STATUS=?,DATE_OF_ACTIVATION=sysdate";
			} else if((String)valObj.get(ValueObjectKeys.CARD_NEW_EXPIRYDATE) != null && ((String)valObj.get(ValueObjectKeys.CARDSTATUS)!= null && !"0".equals((String)valObj.get(ValueObjectKeys.CARDSTATUS)))){
				updDateQuery = "EXPIRY_DATE=to_date(?,'yyyy-MM-dd')";
			}
			int updateCount=0;
			if(updDateQuery != null){
				String updCardStatus="update CARD set "+updDateQuery+" where " +
						"CARD_NUM_HASH=?";

				
				if((String)valObj.get(ValueObjectKeys.CARD_NEW_EXPIRYDATE) != null && ((String)valObj.get(ValueObjectKeys.CARDSTATUS)!= null && "0".equals((String)valObj.get(ValueObjectKeys.CARDSTATUS)))){

					 updateCount=getJdbcTemplate().update(updCardStatus, FSAPIConstants.ACTIVE_CARDSTAT,(String) valObj.get(ValueObjectKeys.CARD_NEW_EXPIRYDATE),(String) valObj.get(ValueObjectKeys.HASH_CARDNO));	

				} else if((String)valObj.get(ValueObjectKeys.CARD_NEW_EXPIRYDATE) == null && ((String)valObj.get(ValueObjectKeys.CARDSTATUS)!= null && "0".equals((String)valObj.get(ValueObjectKeys.CARDSTATUS)))){
					 updateCount=getJdbcTemplate().update(updCardStatus, FSAPIConstants.ACTIVE_CARDSTAT,(String) valObj.get(ValueObjectKeys.HASH_CARDNO));
				} else if((String)valObj.get(ValueObjectKeys.CARD_NEW_EXPIRYDATE) != null && ((String)valObj.get(ValueObjectKeys.CARDSTATUS)!= null && !"0".equals((String)valObj.get(ValueObjectKeys.CARDSTATUS)))){
					 updateCount=getJdbcTemplate().update(updCardStatus,(String) valObj.get(ValueObjectKeys.CARD_NEW_EXPIRYDATE),(String) valObj.get(ValueObjectKeys.HASH_CARDNO));
				}

			}
			log.info("Updated Count"+updateCount);

		}
		 catch(Exception e){
			log.error("Exception in updating the card status and expiry date"+e);
			throw new ServiceException(FSAPIConstants.DAO_SQL_EXEC ,FSAPIConstants.SQLEXCEPTION_DAO);
		} 
	}


	/**
	 * logAPIRequestDtls 
	 */

	public void logAPIRequestDtls(Map<String, Object> tempValuHashMap,String respMsg) {

		int updateCount=getJdbcTemplate().update(QueryConstants.AUDITQRY, (String) tempValuHashMap.get(ValueObjectKeys.RRN),(String) tempValuHashMap.get("encryptedString"),respMsg);
		log.debug("auditQry ::::"+QueryConstants.AUDITQRY);

		if (updateCount > 0) 
		{
			log.debug("Records Inserted in FSAPI_VIRTUALCARD_INFO  table : " + updateCount);
		}
	}



}
