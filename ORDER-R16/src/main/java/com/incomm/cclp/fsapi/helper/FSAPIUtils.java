package com.incomm.cclp.fsapi.helper;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.fsapi.bean.ErrorMsgBean;
import com.incomm.cclp.fsapi.bean.FsApiMaster;
import com.incomm.cclp.fsapi.constants.APIConstants;
import com.incomm.cclp.fsapi.constants.B2BResponseCode;
import com.incomm.cclp.fsapi.constants.B2BResponseMessage;
import com.incomm.cclp.fsapi.constants.FSAPIConstants;
import com.incomm.cclp.fsapi.dao.ProductDAO;
import com.incomm.cclp.fsapi.dao.SpilReloadDAO;
import com.incomm.cclp.fsapi.service.CommonService;
import com.incomm.cclp.fsapi.service.ProductService;
import com.incomm.cclp.fsapi.service.TransactionService;
import com.incomm.cclp.fsapi.token.security.AES;
import com.incomm.cclp.fsapi.token.security.exception.APISecurityException;
import com.incomm.cclp.util.HSMCommandBuilder;
import com.incomm.cclp.util.Util;

@Repository
public class FSAPIUtils extends JdbcDaoSupport  {



	@Autowired
	HSMCommandBuilder hsmbuilder;

	@Autowired 
	TransactionService transactionServ;
	@Autowired 
	ProductDAO productDao;
	@Autowired
	ProductService productService;
	
	@Autowired
	CommonService  commonService;


	@Value("${CCF_HSM_IPADDRESS}") String hsmIpAddress;
	@Value("${CCF_HSM_PORT}") int hsmPort;

	@Value("${FSAPIKEY}")
	private String fsapiKey;


	@Autowired
	SpilReloadDAO spilReloadDAO;

	@Autowired
	public void setDs(@Qualifier("orderDs") DataSource dataSource) {
		setDataSource(dataSource);
	}

	private final Logger log = LogManager.getLogger(this.getClass());

	/**
	 * 
	 * @param tempValueMap
	 * @param api
	 * @throws ServiceException
	 */
	public void getDelchannelTranCode(Map<String,Object> tempValueMap,String api) {
		log.debug("start of getDelchannelTranCode");
		try{
			getJdbcTemplate().query(QueryConstants.B2B_QUERY_DELIVERY_CHANNEL,  new ResultSetExtractor<Map<String,Object>>(){
				@Override
				public Map<String,Object> extractData(ResultSet rs) throws SQLException {

					if(rs.next()){
						tempValueMap.put(ValueObjectKeys.DELIVERYCHNL, rs.getString(1));
						tempValueMap.put(ValueObjectKeys.TRANS_CODE, rs.getString(2));
						tempValueMap.put(ValueObjectKeys.TRANS_SHORT_NAME, rs.getString(3));
						tempValueMap.put(ValueObjectKeys.IS_FINANCIAL, rs.getString(4));
						tempValueMap.put(ValueObjectKeys.CREDIT_DEBIT_INDICATOR, rs.getString(5));
						tempValueMap.put(ValueObjectKeys.TRANSACTIONDESC, rs.getString(6));
						log.info("DELIVERY CHANNEL AND TRAN CODE DESC : "+tempValueMap.get(ValueObjectKeys.DELIVERYCHNL) + " : "+tempValueMap.get(ValueObjectKeys.TRANS_CODE));
					}
					return tempValueMap;
				}

			}, (String) tempValueMap.get(ValueObjectKeys.X_INCFS_CHANNEL),api);
		}catch(Exception exp){
			log.error("Exception occured while getting delivery chanel and txn code : ",exp);
		}
	}
	/**
	 * 
	 * @param tempValueMap
	 * @param date
	 */
	public void getyyyyMMdd(Map<String,Object> tempValueMap,java.sql.Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		tempValueMap.put(ValueObjectKeys.BUSINESS_DATE, sdf.format(date)) ;
	}
	/**
	 * 
	 * @param tempValueMap
	 * @param date
	 */
	public void gethhmmss(Map<String,Object> tempValueMap,java.util.Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("kkmmss");
		tempValueMap.put(ValueObjectKeys.BUSINESS_TIME, sdf.format(date)) ;
	}


	/**
	 * 
	 * @param valueObject
	 * @throws ServiceException
	 */
	public void getNewExpiryDate(Map<String,Object> valueObject){

		try{

			SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(getJdbcTemplate())
					.withProcedureName(QueryConstants.GET_EXPIRYDATE);
			Map<String, Object> inParamMap = new HashMap<>();
			inParamMap.put("p_product_id", (String) valueObject.get(ValueObjectKeys.PROD_CODE));
			SqlParameterSource in = new MapSqlParameterSource(inParamMap);
			Map<String,Object> resp =  simpleJdbcCall.execute(in);	
			if( resp != null && "OK".equalsIgnoreCase(resp.get("P_RESP_MSG_OUT")+"")){
				Timestamp ts=(Timestamp) resp.get("P_EXPIRY_DATE_OUT");
				Date date=new Date(ts.getTime());
				SimpleDateFormat sdf = new SimpleDateFormat(FSAPIConstants.DDMMYYYY);
				String stringDate = sdf.format(date );
				
				valueObject.put(ValueObjectKeys.CARD_NEW_EXPIRYDATE, stringDate);
			}else{
				valueObject.put(ValueObjectKeys.FSAPIRESPONSECODE, B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
				valueObject.put(ValueObjectKeys.RESPONSEMESSAGE, FSAPIConstants.EXPIRYDATEEXCEPTION);
			}

		} catch (Exception e) {
			log.error(""+e.toString(),e);
			valueObject.put(ValueObjectKeys.FSAPIRESPONSECODE, B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
			valueObject.put(ValueObjectKeys.RESPONSEMESSAGE, FSAPIConstants.EXPIRYDATEEXCEPTION);

		}
	}



	/**
	 * 
	 * @param valueObj
	 * @return
	 * @throws ServiceException
	 */

	public String validateCardStatus(Map<String, Object> valueObj) throws ServiceException {

		String cardStatusDesc = null;
		String cardStatusCode = null;
		String channelShortName = null;
		String txnShortName = null;
		String cardStatusAttributeKey = null;

		Map<String, Map<String, Object>> productAttributes = null;
		try {
			if (!Objects.isNull(valueObj.get(ValueObjectKeys.CARDSTATUS))) {

				cardStatusCode = valueObj.get(ValueObjectKeys.CARDSTATUS).toString();
				txnShortName = valueObj.get(ValueObjectKeys.TRANS_SHORT_NAME)+""; 
				channelShortName = (String) valueObj.get("x-incfs-channel")+"";
				String productStr=productDao.getProductAttributesByProductId(valueObj.get(ValueObjectKeys.PROD_CODE)+"");
				productAttributes = Util.jsonToMap(productStr);
				Map<String, Object> cardStatusAttributes = productAttributes.get("Card Status");
				Map<String, String> cardStatusDefs=transactionServ.getAllCardStatus();
				cardStatusDesc = cardStatusDefs.get(cardStatusCode);
				cardStatusAttributeKey = (channelShortName.toLowerCase() + "_" + txnShortName + "_" + cardStatusDesc);

				if (!CollectionUtils.isEmpty(cardStatusAttributes)
						&& !Objects.isNull(cardStatusAttributes.get(cardStatusAttributeKey))) {
					/**
					 * card status attribute value is not true throw Service Exception
					 */
					if (!cardStatusAttributes.get(cardStatusAttributeKey).toString().equalsIgnoreCase(FSAPIConstants.ORDER_FSAPI_TRUE)) {
						if ("INACTIVE".equalsIgnoreCase(cardStatusDesc)) {

							return  B2BResponseCode.INVALID_CARD_STATE;
						} else if ("ACTIVE".equalsIgnoreCase(cardStatusDesc)) {
							return  B2BResponseCode.INVALID_CARD_STATE;
						} else if ("LOST-STOLEN".equalsIgnoreCase(cardStatusDesc)) {
							return B2BResponseCode.INVALID_CARD_STATE;
						} else if ("DAMAGE".equalsIgnoreCase(cardStatusDesc)) {
							return B2BResponseCode.INVALID_CARD_STATE;
						} else if ("EXPIRED CARD".equalsIgnoreCase(cardStatusDesc)) {
							return B2BResponseCode.INVALID_CARD_STATE;
						} else if ("CLOSED".equalsIgnoreCase(cardStatusDesc)) {
							return B2BResponseCode.INVALID_CARD_STATE;
						}else {
							return B2BResponseCode.INVALID_CARD_STATE;

						}
					}
				} else {
					return B2BResponseCode.INVALID_CARD_STATE;
				}

			} else {
				return B2BResponseCode.INVALID_CARD_STATE;
			}
		} catch ( Exception e) {
			log.info("Exceptin occured validateCardStatus",e);
		}

		return B2BResponseCode.SUCCESS;
	}
	
	/**
	 * 
	 * @param valObject
	 * @param errorMsg
	 * @param reponseId
	 * @throws ServiceException
	 */
	public void logTxnDtls(Map<String, Object> valObject, String errorMsg,
			String reponseId) throws ServiceException {
		String msgtype = "";
		String rrn = "";
		String ledgerbalance="";
		String acctbalance="";
		String acctno = "";
		Double feeAmt=0d;
		String bdate="";
		try {
			if (valObject.get(ValueObjectKeys.MSGTYPE) == null){
				msgtype = "0200";
				valObject.put(ValueObjectKeys.MSGTYPE, msgtype);}
			else
				msgtype = (String) valObject.get(ValueObjectKeys.MSGTYPE);

			rrn = (String) valObject.get(ValueObjectKeys.RRN);
			if (rrn.length() > 40)
				rrn = rrn.substring(0, 40);

			String terminalID = (String) valObject.get(ValueObjectKeys.TERMINALID);

			if (terminalID != null && terminalID.length() > 15) {
					terminalID = terminalID.substring(0, 15);
			}
			
			
			String trandate = (String) valObject.get(ValueObjectKeys.BUSINESS_DATE);
			if (trandate != null&&trandate.length() > 8) {
				trandate = trandate.substring(0, 8);
			}else if(Util.isEmpty(trandate)) {
				Date date = Calendar.getInstance().getTime();  
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");  
                trandate = dateFormat.format(date);  
			}
			
			

			/*
			 * if BUSINESS_DATE length is more than 8 digits substringed for insert in
			 * transactionlog valObject.get(ValueObjectKeys.PROD_CODE)
			 */
			
			if(!Util.isEmpty((String) valObject.get(ValueObjectKeys.PROD_CODE))){
				bdate = productDao.getBusinessDate((String) valObject.get(ValueObjectKeys.PROD_CODE));
			}
			
			

			String trancde = (String) valObject.get(ValueObjectKeys.TRANS_CODE);

			String delChanel = (String) valObject.get(ValueObjectKeys.DELIVERYCHNL);
			String bussinsessTime = (String) valObject.get(ValueObjectKeys.BUSINESS_TIME);
			if (bussinsessTime != null && bussinsessTime.length() > 6 ) {
					bussinsessTime = bussinsessTime.substring(0, 6);
					valObject.put(ValueObjectKeys.BUSINESS_TIME, bussinsessTime);
			}


			if (errorMsg.length() > 100) {
				errorMsg = errorMsg.substring(0, 100);
			}


			if(B2BResponseCode.SUCCESS.equalsIgnoreCase(reponseId)){
				errorMsg = "success";

			}

			getTimeStamp(valObject);
			getHashKeyValue(valObject);
			if(valObject.get(ValueObjectKeys.HASH_CARDNO)!=null)
			{
				getCardInfoWithCard(valObject);
			}
			acctbalance=valObject.get(ValueObjectKeys.AVAILABLE_BALACE)!=null?valObject.get(ValueObjectKeys.AVAILABLE_BALACE)+"":"0";
			ledgerbalance=valObject.get(ValueObjectKeys.LEDGER_BALANCE)!=null?valObject.get(ValueObjectKeys.LEDGER_BALANCE)+"":"0";
			acctno=valObject.get(ValueObjectKeys.ACCOUNT_NUMBER)!=null?valObject.get(ValueObjectKeys.ACCOUNT_NUMBER)+"":"0";
			getAutID(valObject);
			getSeqID(valObject);
			if (valObject.get(ValueObjectKeys.RRN) != null) {
				log.info("After parsing before database hit"+ errorMsg+"RRN:"+valObject.get(ValueObjectKeys.RRN));
				String txnStatus = null;
				if(errorMsg != null &&  errorMsg.equalsIgnoreCase(FSAPIConstants.SUCCESS_MSG))
				{
					txnStatus = "C";
					valObject.put(ValueObjectKeys.ACCOUNT_NUMBER, acctno);
					valObject.put(ValueObjectKeys.AVAILABLE_BALACE, acctbalance);
					valObject.put(ValueObjectKeys.LEDGER_BALANCE, ledgerbalance);
					Map<String,Object> respMap = feecalc(valObject);
					log.info("****fee_calc - respMap:"+respMap);
					if(respMap.get(FSAPIConstants.P_TRAN_FEE)!=null && Double.valueOf(respMap.get("P_TRAN_FEE")+"")>0)
					{
						feeAmt=Double.valueOf(respMap.get("P_TRAN_FEE")+"");
						valObject.put(ValueObjectKeys.TRANAMOUNT, feeAmt);
						stmtLog(valObject);
					}

				}
				else {
					txnStatus = "F";
				}

				log.info("Query for Transaction_log"+ errorMsg+"RRN:"+QueryConstants.TRANSCTION_LOG);


				int updateCount=getJdbcTemplate().update(QueryConstants.TRANSCTION_LOG, 
						
								msgtype,//MSG_TYPE
								rrn,//RRN
								errorMsg,//ERROR_MSG
								reponseId,//REQ_RESP_CODE
								terminalID,//TERMINAL_ID
								bdate,//BUSINESS_DATE
								trancde,//TRANSACTION_CODE
								delChanel,//DELIVERY_CHANNEL
								reponseId,//RESPONSE_ID
								valObject.get(ValueObjectKeys.IS_FINANCIAL),//IS_FINANCIAL
								valObject.get(ValueObjectKeys.TRANSACTIONDESC),//TRANSACTION_DESC
						valObject.get(ValueObjectKeys.ENCR_CARDNO) != null
								? valObject.get(ValueObjectKeys.ENCR_CARDNO) + ""
								: "", // CUSTOMER_CARD_NBR_ENCR
						valObject.get(ValueObjectKeys.HASH_CARDNO) != null
								? valObject.get(ValueObjectKeys.HASH_CARDNO) + ""
								: "", // CARD_NUMBER
						valObject.get(ValueObjectKeys.IPADDRESS) != null ? valObject.get(ValueObjectKeys.IPADDRESS) + ""
								: "", // IP_ADDRESS
						"000", // TRANSACTION_TIMEZONE
						txnStatus, // TRANSACTION_STATUS
						valObject.get(ValueObjectKeys.CREDIT_DEBIT_INDICATOR), // CR_DR_FLAG
						acctno, // ACCOUNT_NUMBER
						Double.valueOf(ledgerbalance), // OPENING_LEDGER_BALANCE
						Double.valueOf(acctbalance), valObject.get(ValueObjectKeys.CURRENCY_CODE) + "", // TRAN_CURR
						00, // REVERSAL_CODE
						valObject.get(ValueObjectKeys.AUTH_ID) + "",
						valObject.get(ValueObjectKeys.PROD_CODE) != null ? valObject.get(ValueObjectKeys.PROD_CODE) + ""
								: "0", // PRODUCT_ID
						valObject.get(ValueObjectKeys.SEQ_ID) + "", valObject.get(ValueObjectKeys.HASH_KEY) + "", // HASHKEY_ID
						valObject.get(ValueObjectKeys.X_INCFS_CORRELATIONID), // CORRELATION_ID
						valObject.get(ValueObjectKeys.CARDSTATUS) != null ? valObject.get(ValueObjectKeys.CARDSTATUS)
								: "", // CARD_STATUS
						trandate, // TRANSACTION_DATE
						valObject.get(ValueObjectKeys.BUSINESS_TIME), // TRANSACTION_TIME
						valObject.get(ValueObjectKeys.ISSUER_ID) != null ? valObject.get(ValueObjectKeys.ISSUER_ID)
								: "",
						valObject.get(ValueObjectKeys.X_INCFS_PARTNERID), feeAmt,
						valObject.get(ValueObjectKeys.PROXYNUMBER) != null ? valObject.get(ValueObjectKeys.PROXYNUMBER)
								: "",
						valObject.get(ValueObjectKeys.CARD_ACCOUNT_ID) != null
								? valObject.get(ValueObjectKeys.CARD_ACCOUNT_ID)
								: "",
						valObject.get(ValueObjectKeys.PURSE_ID) != null ? valObject.get(ValueObjectKeys.PURSE_ID) : "",
						Double.valueOf(ledgerbalance),
						Double.valueOf(acctbalance)
												
						);

				log.info("Transaction log count"+updateCount);
			}

		} catch (Exception e) {
			log.error("Exception on insertlog",e);
			throw new ServiceException(FSAPIConstants.DAO_SQL_EXEC,B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);

		} 

	}
	/**
	 * 
	 * @param valObj
	 */
	public void getTimeStamp(Map<String, Object> valObj){
		try{
			getJdbcTemplate().query(QueryConstants.TIMS_STAMP_QUERY,  new ResultSetExtractor<Map<String,Object>>(){
				@Override
				public Map<String,Object> extractData(ResultSet rs) throws SQLException {

					if(rs.next()){
						valObj.put(ValueObjectKeys.TIME_STAMP, rs.getString("timestamp"));		
						log.debug("time stame after formatting ======"+rs.getString("timestamp"));
					}
					return valObj;
				}

			});

		}//try
		catch(Exception e){
			log.error("Exception in getTimeStamp",e);
		}
	}

	/**
	 * 
	 * @param valObj
	 */
	public void getHashKeyValue(Map<String, Object> valObj){

		StringBuilder data = new StringBuilder();
		if((String) valObj.get(ValueObjectKeys.DELIVERYCHNL) != null){
			data.append(valObj.get(ValueObjectKeys.DELIVERYCHNL));
		}
		if((String) valObj.get(ValueObjectKeys.TRANS_CODE) != null){
			data.append(valObj.get(ValueObjectKeys.TRANS_CODE));
		}
		if((String) valObj.get(ValueObjectKeys.CARDNUMBER) != null){
			data.append(valObj.get(ValueObjectKeys.CARDNUMBER));
		}
		if((String) valObj.get(ValueObjectKeys.RRN) != null){
			data.append(valObj.get(ValueObjectKeys.RRN));
		}
		if((String) valObj.get(ValueObjectKeys.TIME_STAMP) != null){
			data.append(valObj.get(ValueObjectKeys.TIME_STAMP));
		}
		try {
			getJdbcTemplate().query(QueryConstants.GET_HASH_VAL_QRY,  new ResultSetExtractor<Map<String,Object>>(){
				@Override
				public Map<String,Object> extractData(ResultSet rs) throws SQLException {

					if(rs.next()){
						valObj.put(ValueObjectKeys.HASH_KEY, rs.getString("hashValue"));		
					}
					return valObj;
				}

			},data);
		} catch (Exception e) {
			log.error("Exception in getHashKeyValue",e);
		}

	}
	/**
	 * 
	 * @param valueObj
	 * @param seqName
	 * @throws ServiceException
	 */
	public void getRRN(Map<String, Object> valueObj,String seqName) throws ServiceException {
		final StringBuilder rrnQuery = new StringBuilder();
		rrnQuery.append("SELECT TO_CHAR (TO_CHAR (SYSDATE, 'YYMMDDHH24MISS')|| LPAD ( ");
		rrnQuery.append(seqName);
		rrnQuery.append(".NEXTVAL, 3, '0')) RRN FROM DUAL");
		try{
			getJdbcTemplate().query(rrnQuery.toString(),  new ResultSetExtractor<Map<String,Object>>(){
				@Override
				public Map<String,Object> extractData(ResultSet rs) throws SQLException {

					if(rs.next()){
						valueObj.put(ValueObjectKeys.RRN, rs.getString(ValueObjectKeys.RRN));
					}
					return valueObj;
				}

			});


		} catch (Exception ex) {
			logger.info(ex);
			throw new ServiceException(
					FSAPIConstants.RRN_GENERATING_EXCEPTION,
					B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
		}
	}
	/**
	 * 
	 * @param valueObj
	 */
	public void getAutID(Map<String, Object> valueObj){
		try{
			getJdbcTemplate().query(QueryConstants.AUTH_ID,  new ResultSetExtractor<Map<String,Object>>(){
				@Override
				public Map<String,Object> extractData(ResultSet rs) throws SQLException {

					if(rs.next()){
						valueObj.put(ValueObjectKeys.AUTH_ID, rs.getString(1));
					}
					else{
						valueObj.put(ValueObjectKeys.AUTH_ID, 0);
					}
					return valueObj;
				}

			});
		} catch (Exception ex) {
			log.error("Exception in getAutID",ex);
		} 
	}
	/**
	 * 
	 * @param valueObj
	 */
	public void getSeqID(Map<String, Object> valueObj){
		try{
			getJdbcTemplate().query(QueryConstants.SEQ_ID,  new ResultSetExtractor<Map<String,Object>>(){
				@Override
				public Map<String,Object> extractData(ResultSet rs) throws SQLException {

					if(rs.next()){
						valueObj.put(ValueObjectKeys.SEQ_ID, rs.getString(1));
					}
					else{
						valueObj.put(ValueObjectKeys.SEQ_ID, 0);
					}
					return valueObj;
				}

			});

		} catch (Exception ex) {
			log.error("Exception in getSeqID",ex);
			valueObj.put(ValueObjectKeys.SEQ_ID, 0);
		} 
	}
	/**
	 * 
	 * @param valObj
	 * @throws ServiceException
	 */
	public void updateCardInfo(Map<String, Object> valObj) throws ServiceException {

		try{
			int updateCount=getJdbcTemplate().update(QueryConstants.B2B_ORDER_ACTIVATION_QRY, 
					 FSAPIConstants.ACTIVE_CARDSTAT,valObj.get(ValueObjectKeys.HASH_CARDNO));

			log.debug("No of updated cards "+updateCount);
		}catch(Exception e){
			log.error("Exception in updating the card status and active date"+e);
			throw new ServiceException(FSAPIConstants.DAO_SQL_EXEC ,B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
		}
	}
	
	
	public int getReplacedCardOldStatus(String cardHash) {
		return getJdbcTemplate().queryForObject(QueryConstants.CHECK_DAMAGED_CARD_CARD_HASH, new Object[] { cardHash }, Integer.class);
	}
	/**
	 * 
	 * @param valObj
	 * @throws Exception
	 */
	public void stmtLog(Map<String, Object> valObj)throws ServiceException
	{
		try {
			SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(getJdbcTemplate())
					.withProcedureName(QueryConstants.STATEMENT_LOG);
			Map<String, Object> inParamMap = new HashMap<>();

			inParamMap.put(ValueObjectKeys.P_CARD_NUM_HASH, valObj.get(ValueObjectKeys.HASH_CARDNO));
			inParamMap.put(ValueObjectKeys.P_CARD_NUM_ENCR, valObj.get(ValueObjectKeys.ENCR_CARDNO)!=null?valObj.get(ValueObjectKeys.ENCR_CARDNO):null);
			inParamMap.put(ValueObjectKeys.P_OPENING_BALANCE,  valObj.get(ValueObjectKeys.LEDGER_BALANCE));
			inParamMap.put(ValueObjectKeys.P_CLOSING_BALANCE,  valObj.get(ValueObjectKeys.AVAILABLE_BALACE));
			inParamMap.put(ValueObjectKeys.P_TRANSACTION_AMOUNT, valObj.get(ValueObjectKeys.TRANAMOUNT)!=null?valObj.get(ValueObjectKeys.TRANAMOUNT):null);
			inParamMap.put(ValueObjectKeys.P_CREDIT_DEBIT_FLAG,  valObj.get(ValueObjectKeys.CREDIT_DEBIT_INDICATOR));
			inParamMap.put(ValueObjectKeys.P_TRANSACTION_NARRATION, valObj.get(ValueObjectKeys.TRANS_SHORT_NAME));
			inParamMap.put(ValueObjectKeys.P_RRN, valObj.get(ValueObjectKeys.RRN)!=null?valObj.get(ValueObjectKeys.RRN):null);
			inParamMap.put(ValueObjectKeys.P_AUTH_ID, valObj.get(ValueObjectKeys.AUTH_ID));
			inParamMap.put(ValueObjectKeys.P_TRANSACTION_DATE, valObj.get(ValueObjectKeys.BUSINESS_DATE)!=null?valObj.get(ValueObjectKeys.BUSINESS_DATE):null);
			inParamMap.put(ValueObjectKeys.P_TRANSACTION_TIME, valObj.get(ValueObjectKeys.BUSINESS_TIME)!=null?valObj.get(ValueObjectKeys.BUSINESS_TIME):null);
			inParamMap.put(ValueObjectKeys.P_FEE_FLAG, 0);
			inParamMap.put(ValueObjectKeys.P_DELIVERY_CHANNEL_STATNENT, valObj.get(ValueObjectKeys.DELIVERYCHNL)!=null?valObj.get(ValueObjectKeys.DELIVERYCHNL):null);
			inParamMap.put(ValueObjectKeys.P_TRANSACTION_CODE_STATNENT,  valObj.get(ValueObjectKeys.TRANS_CODE)!=null?valObj.get(ValueObjectKeys.TRANS_CODE):null);
			inParamMap.put(ValueObjectKeys.P_ACCOUNT_ID,  valObj.get(ValueObjectKeys.CARD_ACCOUNT_ID));
			inParamMap.put(ValueObjectKeys.P_TO_ACCOUNT_ID, null);
			inParamMap.put(ValueObjectKeys.P_MERCHANT_NAME, null);
			inParamMap.put(ValueObjectKeys.P_MERCHANT_CITY, null);
			inParamMap.put(ValueObjectKeys.P_MERCHANT_STATE, null);
			inParamMap.put(ValueObjectKeys.P_CARD_LAST4DIGIT, null);
			inParamMap.put(ValueObjectKeys.P_PRODUCT_ID, valObj.get(ValueObjectKeys.PROD_CODE)!=null?valObj.get(ValueObjectKeys.PROD_CODE):null);
			inParamMap.put(ValueObjectKeys.P_RECORD_SEQ, null);
			inParamMap.put(ValueObjectKeys.P_PURSE_ID, null);
			inParamMap.put(ValueObjectKeys.P_TO_PURSE_ID, null);
			inParamMap.put(ValueObjectKeys.P_TRANSACTION_SQID, valObj.get(ValueObjectKeys.SEQ_ID));
			inParamMap.put(ValueObjectKeys.P_BUSINESS_DATE,new Date());
			inParamMap.put(ValueObjectKeys.P_STORE_ID, null);

			SqlParameterSource in = new MapSqlParameterSource(inParamMap);
			Map<String,Object> respMap =  simpleJdbcCall.execute(in);	
			log.info("stmt_log resp: "+respMap);
			if(!B2BResponseCode.SUCCESS.equals(respMap.get("P_RESP_CODE")))
			{
				throw new ServiceException(FSAPIConstants.DAO_SQL_EXEC ,
						B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
			}
		} catch (Exception e) {
			log.error("Exception in stmt_log",e);
		}


	}
	/**
	 * 
	 * @param valObj
	 * @return
	 * @throws Exception
	 */

	/*public Map<String, Object> feecalc(Map<String, Object> valObj)throws ServiceException
	{
		Map<String,Object> respMap =null;
		try {
			SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(getJdbcTemplate())
					.withProcedureName(QueryConstants.FEE_CALC);
			Map<String, Object> inParamMap = new HashMap<>();
			
			inParamMap.put(ValueObjectKeys.P_PRODUCTID, valObj.get(ValueObjectKeys.PROD_CODE));
			inParamMap.put(ValueObjectKeys.P_TRANSACTION_CODE, valObj.get(ValueObjectKeys.TRANS_CODE)!=null?valObj.get(ValueObjectKeys.TRANS_CODE):null);
			inParamMap.put(ValueObjectKeys.P_DELIVERY_CHANNEL, valObj.get(ValueObjectKeys.DELIVERYCHNL)!=null?valObj.get(ValueObjectKeys.DELIVERYCHNL):null);
			inParamMap.put(ValueObjectKeys.P_MSGTYPE, valObj.get(ValueObjectKeys.MSGTYPE)!=null?valObj.get(ValueObjectKeys.MSGTYPE):null);
			inParamMap.put(ValueObjectKeys.P_TXN_AMT, valObj.get(FSAPIConstants.ORDER_AMOUNT)!=null?valObj.get(FSAPIConstants.ORDER_AMOUNT):null);
			inParamMap.put(ValueObjectKeys.P_ACCOUNTID,  valObj.get(ValueObjectKeys.CARD_ACCOUNT_ID)!=null?valObj.get(ValueObjectKeys.CARD_ACCOUNT_ID):null);
			inParamMap.put(ValueObjectKeys.P_LAST_TXNDATE, new Date());

			SqlParameterSource in = new MapSqlParameterSource(inParamMap);
			respMap =  simpleJdbcCall.execute(in);	
			
		

			if(!B2BResponseCode.SUCCESS.equals(respMap.get(ValueObjectKeys.P_RESP_CODE)))
			{
				throw new ServiceException(FSAPIConstants.DAO_SQL_EXEC ,
						B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
			}
			else
			{
				return respMap;
			}
		} catch (Exception e) {
			log.error("Exception in fee_calc",e);
			throw new ServiceException(B2BResponseMessage.SYSTEM_ERROR,B2BResponseCode.SYSTEM_ERROR);
		}

	}*/
	
	public Map<String, Object> feecalc(Map<String, Object> valObj){

		java.sql.Date activationdate = null;
		
		logger.debug("inside Fee Calculation");
		final String productId = String.valueOf(valObj.get(ValueObjectKeys.PROD_CODE));
		final String transactionCode = valObj.get(ValueObjectKeys.TRANS_CODE)!=null?String.valueOf(valObj.get(ValueObjectKeys.TRANS_CODE)):null;
		final String deliveryChannel = valObj.get(ValueObjectKeys.DELIVERYCHNL)!=null?String.valueOf(valObj.get(ValueObjectKeys.DELIVERYCHNL)):null;
		final String msgtype = valObj.get(ValueObjectKeys.MSGTYPE)!=null?String.valueOf(valObj.get(ValueObjectKeys.MSGTYPE)):null;
		final Object txnAmt = valObj.get(FSAPIConstants.ORDER_AMOUNT)!=null?valObj.get(FSAPIConstants.ORDER_AMOUNT):0;
		final Object accountId = valObj.get(ValueObjectKeys.CARD_ACCOUNT_ID)!=null?valObj.get(ValueObjectKeys.CARD_ACCOUNT_ID):0;
		
		
		java.util.Date date = new Date();
			
		activationdate = new java.sql.Date(date.getTime());
		
		final Date lastTxnDate = activationdate;

		List<SqlParameter> params = new ArrayList<>();
		params.add(new SqlParameter(Types.VARCHAR));
		params.add(new SqlParameter(Types.VARCHAR));
		params.add(new SqlParameter(Types.VARCHAR));
		params.add(new SqlParameter(Types.VARCHAR));
		params.add(new SqlParameter(Types.DECIMAL));
		params.add(new SqlParameter(Types.DECIMAL));
		params.add(new SqlParameter(Types.VARCHAR));
		
		params.add(new SqlOutParameter("p_resp_code", Types.VARCHAR));
		params.add(new SqlOutParameter("p_err_msg", Types.VARCHAR));
		params.add(new SqlOutParameter("p_tran_fee", Types.VARCHAR));
		params.add(new SqlOutParameter("p_flat_fee_out", Types.DECIMAL));
		params.add(new SqlOutParameter("p_per_fee_out", Types.DECIMAL));
		params.add(new SqlOutParameter("p_min_fee_out", Types.DECIMAL));
		params.add(new SqlOutParameter("p_fee_condition_out", Types.VARCHAR));
		params.add(new SqlOutParameter("p_free_txncount_flag_out", Types.VARCHAR));
		params.add(new SqlOutParameter("p_max_txncount_flag_out", Types.VARCHAR));
		params.add(new SqlOutParameter("p_result_attributes", Types.CLOB));

		Map<String, Object> resultMap = getJdbcTemplate().call(new CallableStatementCreator() {
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement cs = con.prepareCall(QueryConstants.FEE_CALC);
				cs.setString(1, productId);
				cs.setString(2, transactionCode);
				cs.setString(3, deliveryChannel);
				cs.setString(4, msgtype);
				cs.setInt(5,  (int) txnAmt);
				cs.setString(6, (String) accountId);
				cs.setDate(7, (java.sql.Date) lastTxnDate);
			
				
				cs.registerOutParameter(8, Types.VARCHAR);
				cs.registerOutParameter(9, Types.VARCHAR);
				cs.registerOutParameter(10, Types.VARCHAR);
				cs.registerOutParameter(11, Types.DECIMAL);
				cs.registerOutParameter(12, Types.DECIMAL);
				cs.registerOutParameter(13, Types.DECIMAL);
				cs.registerOutParameter(14, Types.VARCHAR);
				cs.registerOutParameter(15, Types.VARCHAR);
				cs.registerOutParameter(16, Types.VARCHAR);
				cs.registerOutParameter(17, Types.CLOB);
				

				return cs;
			}
		}, params);

		logger.debug("Fee Calculation response: " + resultMap.get("p_err_msg").toString());
		logger.debug("exit Fee Calculation");
		return resultMap;

	}

	/**
	 * 
	 * @param tempValuHashMap
	 * @throws ServiceException
	 */
	public void validateActivationCode(Map<String, Object> tempValuHashMap) throws ServiceException {

		String activationCode = "";
		final String productId = String.valueOf(tempValuHashMap.get(ValueObjectKeys.PROD_CODE));
		final String actCode = String.valueOf(tempValuHashMap.get(ValueObjectKeys.ACTIVATION_CODE));
		Map<String, Map<String, Object>> attributes = productService.getProductAttributes(productId);

		if(!CollectionUtils.isEmpty(attributes)) {


			Map<String, Object> generalAttributes = attributes.get("General");

			if(!CollectionUtils.isEmpty(generalAttributes)) {
				activationCode = String.valueOf(generalAttributes.get("activationCode"));
			}else {
				log.error("Error while getting activation code");
			}


			if(!Util.isEmpty(activationCode) && "true".equalsIgnoreCase(activationCode)) {
				if(!Util.isEmpty(actCode)) {
					log.debug("Activation code : "+actCode);
				}else {
					log.error("Invalid Activation code field ");
					final ErrorMsgBean errorBean = new ErrorMsgBean();
					errorBean.setKey(FSAPIConstants.ORDER_ACTIVATION_CODE);
					errorBean.setErrorMsg(FSAPIConstants.ORDER_ACTIVATION_CODERESPMSG);
					errorBean.setRespCode(B2BResponseCode.INVALID_FIELD);
				}
			}
		}else {

			log.error("Product does not exist to check actinvation code");

		}

	}
	/**
	 * 
	 * @param valueHashMap
	 * @param errorList
	 */
	public void checkPartnerId(Map<String, Object> valueHashMap, List<ErrorMsgBean> errorList) {

		String partnerId = String.valueOf(valueHashMap.get(ValueObjectKeys.X_INCFS_PARTNERID));

		final String productId = String.valueOf(valueHashMap.get(ValueObjectKeys.PROD_CODE));
		int prodPartnerIdcount = productDao.checkPartnerId(productId, partnerId);

		if (prodPartnerIdcount == 0) {
			logger.error(FSAPIConstants.ORDER_PARTNERID_ERRMSG + ":"
					+ valueHashMap.get(ValueObjectKeys.X_INCFS_PARTNERID) + ":Product ID:"
					+ valueHashMap.get(ValueObjectKeys.PROD_CODE));
			final ErrorMsgBean tempBean = new ErrorMsgBean();
			tempBean.setKey(String.valueOf(valueHashMap.get(FSAPIConstants.ORDER_PARTNERID)));
			tempBean.setErrorMsg(FSAPIConstants.ORDER_PARTNERID_ERRMSG + ":"
					+ valueHashMap.get(ValueObjectKeys.X_INCFS_PARTNERID) + ":Product ID:"
					+ valueHashMap.get(ValueObjectKeys.PROD_CODE));
			tempBean.setRespCode(B2BResponseCode.INVALID_PARTNER_ID);
			errorList.add(tempBean);
		}


	}


	/**
	 * 
	 * @param set1
	 * @param set2
	 * @return
	 */
	public static boolean setComaparision(Set<?> set1, Set<?> set2) {

		if (set1 == null || set2 == null) {
			return false;
		}

		return set1.containsAll(set2);

	}
	/**
	 * 
	 * @param valuHashMap
	 * @throws SQLException
	 */

	public  void  getCardDetls(Map<String, String> valuHashMap){
		String quryforCardPrxy="";
		try{

			if(FSAPIConstants.PROXYNUMBER.equalsIgnoreCase(valuHashMap.get(ValueObjectKeys.TYPE))){
				quryforCardPrxy ="  proxy_number=?";
			}else if (FSAPIConstants.SERIALNUMBER.equalsIgnoreCase(valuHashMap.get(ValueObjectKeys.TYPE))){
				quryforCardPrxy=" serial_number=?";
			}

			//Get the card number for  card status is other then 0 and 9
			String queryCardNo1=" SELECT (SELECT  CURRENCY_CODE FROM CURRENCY_CODE WHERE CURRENCY_ID=ap.CURRENCY_CODE) as currencycode, fn_dmaps_main(c.card_num_encr) AS card_no,c.proxy_number as proxy, c.serial_number as serial, to_char(c.expiry_date,'MM/YY') as expdate,"+
					" c.card_status as cardStatus,ap.available_balance,c.CARD_NUM_HASH card_hash,c.product_id prodCode,c.ACCOUNT_ID accountId,c.expiry_date origExpDate,c.DATE_OF_ACTIVATION actDate,c.LAST_TXNDATE lastTxnDate,nvl(c.FIRSTTIME_TOPUP,'N') firstTopup FROM card c,account_purse ap WHERE "+quryforCardPrxy+" and c.account_id=ap.account_id and card_status not in( '0','9') order by DATE_OF_ACTIVATION desc ";
			//Get the card number for 0 card stats
			String queryCardNo2=" SELECT (SELECT CURRENCY_CODE FROM CURRENCY_CODE WHERE CURRENCY_ID=ap.CURRENCY_CODE) as currencycode, fn_dmaps_main(c.card_num_encr)  AS card_no,c.proxy_number as proxy, c.serial_number as serial , to_char(c.expiry_date,'MM/YY') as expdate,"+
					" c.card_status as cardStatus,ap.available_balance,c.CARD_NUM_HASH card_hash,c.product_id prodCode,c.ACCOUNT_ID accountId,c.expiry_date origExpDate,c.DATE_OF_ACTIVATION actDate,c.LAST_TXNDATE lastTxnDate,nvl(c.FIRSTTIME_TOPUP,'N') firstTopup FROM card c,account_purse ap WHERE "+quryforCardPrxy+" and c.account_id=ap.account_id and c.card_status = 0 order by c.ins_date desc ";
			String queryCardNo3=" SELECT (SELECT CURRENCY_CODE FROM CURRENCY_CODE WHERE CURRENCY_ID=ap.CURRENCY_CODE)  as currencycode,fn_dmaps_main(c.card_num_encr) AS card_no,c.proxy_number as proxy, c.serial_number as serial,to_char(c.expiry_date,'MM/YY') as expdate, "+
					" c.card_status as cardStatus,ap.available_balance,c.CARD_NUM_HASH card_hash,c.product_id prodCode,c.ACCOUNT_ID accountId,c.expiry_date origExpDate,c.DATE_OF_ACTIVATION actDate,c.LAST_TXNDATE lastTxnDate,nvl(c.FIRSTTIME_TOPUP,'N') firstTopup FROM card c,account_purse ap WHERE "+quryforCardPrxy+" and c.account_id=ap.account_id AND c.card_status = 9 order by c.ins_date desc ";
			getQueryCard(valuHashMap,queryCardNo1);

			if(ValueObjectKeys.ISHAVECARDQUERYRESULTNO.equalsIgnoreCase(valuHashMap.get(ValueObjectKeys.ISHAVEESULT))){
				getQueryCard(valuHashMap,queryCardNo2);
			}
			if(ValueObjectKeys.ISHAVECARDQUERYRESULTNO.equalsIgnoreCase(valuHashMap.get(ValueObjectKeys.ISHAVEESULT))){
				getQueryCard(valuHashMap,queryCardNo3);
			}
			if(!ValueObjectKeys.ISHAVECARDQUERYRESULTYES.equals(valuHashMap.get(ValueObjectKeys.ISHAVEESULT))){
				throw new ServiceException(FSAPIConstants.INVALID_PROXY_NUMBER,
						B2BResponseCode.INVALID_PROXY_NUMBER);	
			}



		}catch(Exception exp){
			log.error("Exception while getting card details",exp);

		}
	}
	/**
	 * 
	 * @param valueMap
	 */
	@SuppressWarnings("unchecked")
	public void getCardArrayDtls(Map<String, Object> valueMap) {

		try {
			List<Map<String, Object>> tempItemList = (LinkedList<Map<String, Object>>) valueMap.get(FSAPIConstants.ORDER_LINE_ITEMDTLS);
			Map<String, Map<String, String>> resFields = transactionServ.getFsapiDetails().get(FSAPIConstants.ORDER_STATUS_API+FSAPIConstants.GET).getResSubTagFields();
			for (final Map<String, Object> temp : tempItemList) {
				List<Map<String, String>> tempCardArry = (List<Map<String, String>>) temp.get(FSAPIConstants.CARDS);
				List<Map<String, String>> tempCardArry1 = new ArrayList<>();
				for (Map<String, String> tempCrd : tempCardArry) {
					Map<String, String> tempMap = new HashMap<>();
					if (resFields != null && resFields.get(FSAPIConstants.CARDS) != null) {
						for (Entry<String, String> resp : resFields.get(FSAPIConstants.CARDS).entrySet()) {
							tempMap.put(resp.getValue(), tempCrd.get(resp.getKey()));
						}
					}
					tempCardArry1.add(tempMap);
				}
				temp.put(FSAPIConstants.CARDS,tempCardArry1);
			}
		} catch(Exception exc) {
			log.error( "Exception occured while closing statement"+exc);

		}

	}

	/**
	 * 
	 * @param valObj
	 */
	public void getPackageId(Map<String, Object> valObj) {
		try{
			getJdbcTemplate().query(QueryConstants.GET_PACKAGE_ID,  new ResultSetExtractor<Map<String,Object>>(){
				@Override
				public Map<String,Object> extractData(ResultSet rs) throws SQLException {


					if(rs.next()){

						valObj.put(FSAPIConstants.VIRTUAL_PACKAGE_ID, rs.getString("PACKAGE_ID"));
						log.debug("Package id :::: "+ (String) valObj.get(FSAPIConstants.VIRTUAL_PACKAGE_ID));
					}
					return valObj;
				}

			},(String) valObj.get(FSAPIConstants.ORDER_ORDER_ID), (String) valObj.get(FSAPIConstants.ORDER_LINE_ITEM_ID), (String) valObj.get(FSAPIConstants.ORDER_PARTNERID));




		}catch(Exception sqle){
			log.error("Exception in Closing the ResulSet and Statment", sqle);
		}			
	}

	/** to get current date 
	 * 
	 * @return java.sql.Date object representing the current date
	 */
	public static java.sql.Date getCurrentDate(){
		return new java.sql.Date(new java.util.Date().getTime());
	}
	/**
	 * 
	 * @param valObj
	 */
	public void setDefaultValues(Map<String, Object> valObj) 
	{
		valObj.put(ValueObjectKeys.MSGTYPE,FSAPIConstants.FINANCIAL_MESSAGE_TYPE);			
		valObj.put(ValueObjectKeys.MEMBERNO, FSAPIConstants.MEMBER_NUMBER);
		valObj.put(ValueObjectKeys.REVERSAL_CODE,FSAPIConstants.REVERSAL_CODE);
		valObj.put(ValueObjectKeys.TXN_MODE,FSAPIConstants.TRANS_MODE);
		valObj.put(ValueObjectKeys.CARDNUMBER,valObj.get("cardNumber"));
		valObj.put(ValueObjectKeys.ENCR_CARDNO,valObj.get("encryptCardNumber"));
		valObj.put(ValueObjectKeys.HASH_CARDNO,valObj.get("hashCardNumber"));
		valObj.put(ValueObjectKeys.EXPIRATIONDATE,valObj.get("expirationDate"));
		valObj.put(ValueObjectKeys.CUST_CODE,valObj.get("custcode"));
		valObj.put(ValueObjectKeys.PROD_CODE,valObj.get("prodcode"));
		valObj.put(ValueObjectKeys.PROD_CATG,valObj.get("cardtype"));
		valObj.put(ValueObjectKeys.CARDSTATUS,valObj.get("status"));
	}

	/**
	 * 
	 * @param valueObject
	 * @return
	 */
	public Map<String,Object> getCardInfoWithCard(Map<String, Object> valueObject){

		return getJdbcTemplate().query(QueryConstants.GET_CARD_INFO,  new ResultSetExtractor<Map<String,Object>>(){

			@Override
			public Map<String,Object> extractData(ResultSet rs) throws SQLException {


				if(rs.next()){

					valueObject.put(ValueObjectKeys.CUST_CODE, rs.getString(1));
					valueObject.put(ValueObjectKeys.ENCR_CARDNO, rs.getString(2));
					valueObject.put(ValueObjectKeys.CARD_ACCOUNT_ID, rs.getString(3));
					valueObject.put(ValueObjectKeys.PROD_CODE, rs.getString(4));
					valueObject.put(ValueObjectKeys.ACCOUNT_NUMBER, rs.getString(5));
					valueObject.put(ValueObjectKeys.CARD_ORDER_DTL_STATUS, rs.getString(6));
					valueObject.put(ValueObjectKeys.CARDSTATUS, rs.getString(7));
					valueObject.put(ValueObjectKeys.PROXYNUMBER, rs.getString(8));
					valueObject.put(ValueObjectKeys.CURRENCY_CODE, rs.getString(9));
					valueObject.put(ValueObjectKeys.ISSUER_ID, rs.getString(10));
					valueObject.put(ValueObjectKeys.AVAILABLE_BALACE, rs.getString(11));
					valueObject.put(ValueObjectKeys.LEDGER_BALANCE, rs.getString(12));
					valueObject.put(ValueObjectKeys.CARDNUMBER, rs.getString(13));
					valueObject.put(ValueObjectKeys.PURSE_ID, rs.getString(14));

				}
				return valueObject;
			}

		}, valueObject.get(ValueObjectKeys.HASH_CARDNO));
	}

	/**
	 * 
	 * @param msProdCode
	 * @return
	 */
	public String getCVK(String msProdCode) {
		return getJdbcTemplate().queryForObject(QueryConstants.GET_CVK,new Object[] {Integer.parseInt(msProdCode)}, String.class);
	}
	/**
	 * 
	 * @param productId
	 * @return
	 */
	public String getCvvSupportedFlag(String productId) {
		return getJdbcTemplate().queryForObject(QueryConstants.GET_CVV_SUPPORTED_FLAG,new Object[] {Integer.parseInt(productId)}, String.class);
	}
	/**
	 * 
	 * @param productId
	 * @return
	 */
	public String getServiceCode(String productId) {

		return getJdbcTemplate().queryForObject(QueryConstants.GET_SERVICE_CODE,new Object[] {productId}, String.class);
	}
	/**
	 * 
	 * @param valObj
	 * @param cvvRequire
	 * @return
	 */
	public String genCVV(Map<String,Object> valObj,String cvvRequire){

		String cvv="";
		try {
			String cVK=getCVK(valObj.get(ValueObjectKeys.PROD_CODE)+"");
			SimpleDateFormat sdf1 = new SimpleDateFormat(FSAPIConstants.DDMMYYYY);
			java.util.Date date = sdf1.parse((String) valObj.get(ValueObjectKeys.EXPIRATIONDATE));
			java.sql.Date  msExpiryDate = new java.sql.Date(date.getTime());
			valObj.put(ValueObjectKeys.EXPIRATIONDATE, msExpiryDate);
			String psExpYY = padLeft(String.valueOf(getYear(msExpiryDate)), 2, '0');
			if(psExpYY != null) {
			psExpYY = psExpYY.substring(psExpYY.length() - 2);
			}
			String psExpMM = padLeft(String.valueOf(getMonth(msExpiryDate)), 2, '0');
			String msPan=valObj.get(ValueObjectKeys.CARDNUMBER)+"";
			String  srvCode = getServiceCode(valObj.get(ValueObjectKeys.PROD_CODE)+"");
			if(cvvRequire.equalsIgnoreCase(FSAPIConstants.CVV1)){ 

				cvv = hsmbuilder.genCVV(padRight(msPan, msPan.length(), FSAPIConstants.EMPTY_SPACE),  psExpYY + psExpMM,srvCode, cVK,hsmIpAddress,hsmPort);

				valObj.put(FSAPIConstants.CVV1,cvv!=null?cvv.trim():"");
			}else if(cvvRequire.equalsIgnoreCase(FSAPIConstants.CVV2)){

				cvv =hsmbuilder.genCVV(
						padRight(msPan, msPan.length(),FSAPIConstants.EMPTY_SPACE), psExpMM + psExpYY,
						FSAPIConstants.TRIPLE_ZERO, cVK,hsmIpAddress,hsmPort);

				valObj.put(FSAPIConstants.CVV2, cvv!=null?cvv.trim():"");

			}

		} catch (Exception e) {
			logger.info(e);
			valObj.put(FSAPIConstants.ERROR_CVV_GENERATION, B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);

		}

		return cvv;

	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static int getMonth(java.sql.Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return (calendar.get(Calendar.MONTH) + 1);
	}
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static int getYear(java.sql.Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}
	/**
	 * 
	 * @param str
	 * @param length
	 * @param c
	 * @return
	 */
	public static String padRight(final String str, final int length, final char c) {
		int needed;

		if (str == null) {
			needed = length;
		} else {
			needed = length - str.length();
		}
		if (needed <= 0) {
			return str;
		}
		final char[] padding = new char[needed];
		java.util.Arrays.fill(padding, c);
		StringBuilder sb = new StringBuilder(length);
		if (str != null) {
			sb.append(str);
		}
		sb.append(padding);
		return sb.toString();
	}
	/**
	 * 
	 * @param str
	 * @param length
	 * @param c
	 * @return
	 */
	public static String padLeft(final String str, final int length, final char c) {
		int needed;

		if (str == null) {
			needed = length;
		} else {
			needed = length - str.length();
		}

		if (needed <= 0) {
			return str;
		}
		final char[] padding = new char[needed];
		java.util.Arrays.fill(padding, c);
		StringBuilder sb = new StringBuilder(length);
		sb.append(padding);
		if (str != null) {
			sb.append(str);
		}
		return sb.toString();
	}




	/**
	 * checkPartnerIdForCardAct
	 * @param valueHashMap
	 * @param errorList
	 * @throws ServiceException 
	 */

	public void checkPartnerIdForCardAct(Map<String, Object> valueHashMap, List<ErrorMsgBean> errorList) {

		String partnerId = String.valueOf(valueHashMap.get(ValueObjectKeys.X_INCFS_PARTNERID));

		final String productId = String.valueOf(valueHashMap.get(ValueObjectKeys.PROD_CODE));
		int prodPartnerIdcount = productDao.checkPartnerId(productId, partnerId);
		if (prodPartnerIdcount == 0) {
			logger.error(FSAPIConstants.ORDER_PARTNERID_ERRMSG);
			ErrorMsgBean errorMsgBean=new ErrorMsgBean();
			errorMsgBean.setErrorMsg(FSAPIConstants.ORDER_PARTNERID_ERRMSG);
			errorMsgBean.setRespCode(B2BResponseCode.INVALID_PARTNER_ID);
			errorList.add(errorMsgBean);

		}


	}

	/**
	 * checkActivationCodeFor CardActivation
	 * @param valueHashMap
	 * @param errorList
	 */

	public void checkActivationCodeForCardActivation(Map<String, Object> tempValuHashMap) throws ServiceException {
		String prodactivationCode = null;
		String productId = String.valueOf(tempValuHashMap.get(ValueObjectKeys.PROD_CODE));
		String actCode = String.valueOf(tempValuHashMap.get(ValueObjectKeys.ACTIVATION_CODE));
		Map<String, Map<String, Object>> attributes = productService.getProductAttributes(productId);
		Map<String, Object> generalAttributes = attributes.get(ValueObjectKeys.PRODUCT_GENERAL);
		tempValuHashMap.put(ValueObjectKeys.FSAPIRESPONSECODE, B2BResponseCode.SUCCESS);

		prodactivationCode = String.valueOf(generalAttributes.get(ValueObjectKeys.REQUEST_ACTIVATION_CODE));
		if(!Util.isEmpty(prodactivationCode) && ValueObjectKeys.ISACTIVE.equalsIgnoreCase(prodactivationCode)) {
			if(!Util.isEmpty(tempValuHashMap.get(ValueObjectKeys.REQUEST_ACTIVATION_CODE)+"") ){
				if(!actCode.equals(tempValuHashMap.get(ValueObjectKeys.REQUEST_ACTIVATION_CODE))){
					tempValuHashMap.put(ValueObjectKeys.FSAPIRESPONSECODE, B2BResponseCode.ACTIVATION_CODE_NOT_MATCHED);
					tempValuHashMap.put(ValueObjectKeys.RESPONSEMESSAGE, FSAPIConstants.ACTIVATIONCODES_NOTMATCHED_MSG);
				}

			}else{
				tempValuHashMap.put(ValueObjectKeys.FSAPIRESPONSECODE, B2BResponseCode.INVALID_FIELD);
				tempValuHashMap.put(ValueObjectKeys.RESPONSEMESSAGE, FSAPIConstants.ORDER_ACTIVATION_CODERESPMSG);
			}

		}
	}
	/**
	 * 
	 * @param valObj
	 */
	public void logAPIRequestDtls(Map<String, Object> valObj) {

		try {
			getRRN(valObj,FSAPIConstants.FSAPI_BULKORDER_ACTIVATION_SEQ_NAME);
			int updateCount=getJdbcTemplate().update(QueryConstants.FSAPI_BULKACT_INFO, 
					(String) valObj.get(ValueObjectKeys.RRN),
							(String) valObj.get(FSAPIConstants.ORDERID),
							String.valueOf(valObj.get(FSAPIConstants.LINEITEMID)),
							String.valueOf(valObj.get(FSAPIConstants.RESP_HERDER_POSTBACKURL))
							,String.valueOf(valObj.get(FSAPIConstants.POST_BACK_RESP)),
							String.valueOf(valObj.get(FSAPIConstants.OUT_RESP_STR)));
			log.error("logAPIRequestDtls count "+updateCount);

		} catch (Exception cae) {
			log.error("Exception when inserting in to VMS_FSAPI_BULKACT_INFO : ", cae);

		} 
	}


	/**
	 * 
	 * @param valueObject
	 * @return
	 */

	public Map<String,Object> getProductFundingCheck(Map<String, Object> valueObject){

		return getJdbcTemplate().query(QueryConstants.PRODUCT_B2B_FUNDING_CHECK,  new ResultSetExtractor<Map<String,Object>>(){

			@Override
			public Map<String,Object> extractData(ResultSet rs) throws SQLException {


				while(rs.next()){
					valueObject.put(ValueObjectKeys.B2B_SOURCE_FUNDING, rs.getString(1));
					valueObject.put(ValueObjectKeys.B2B_PRODUCT_FUND, rs.getString(2));
				}
				return valueObject;
			}

		}, valueObject.get(ValueObjectKeys.PROD_CODE));
	}
	
	/**
	 * 
	 * @param valueDto
	 * @throws ServiceException
	 */

	@SuppressWarnings("unchecked")
	public void resetLimits(Map<String,Object> valueDto) throws ServiceException {

		log.debug("Reseting Limit Attributes : ENTER");
		Map<String, String> valueObj = (Map<String, String>)valueDto.get("originalMap");
		Map<String, Map<String, Object>> cardUsageLimitsMap = (Map<String, Map<String, Object>>) valueDto.get(ValueObjectKeys.CARD_USAGE_LIMIT);

		Map<String, Object> cardUsageLimits = cardUsageLimitsMap.get("Limits");
		log.info("Card Limit attrributes : {}", cardUsageLimitsMap);
		/* reseting limit attribute of card */
		reset(valueObj, cardUsageLimits);

		cardUsageLimitsMap.put("Limits", cardUsageLimits);
		log.info("Card Limit attrributes after reset : {}", cardUsageLimits);
		log.debug("Reseting Limit Attributes : EXIT");
	}
	/**
	 * 
	 * @param valueObj
	 * @param cardLimits
	 * @param delchnlTxn
	 * @throws ServiceException
	 */
	private void reset(Map<String, String> valueObj, Map<String, Object> cardLimits)
			throws ServiceException {

		java.util.Date lastTransDate = null;
		java.util.Date todayDate = null;

		cardLimits.entrySet().stream().filter(p -> p.getValue() == null).forEach(map -> map.setValue("0"));

		String date = new SimpleDateFormat(FSAPIConstants.DDMMYYYY).format(new Date());

		try {
			lastTransDate = new SimpleDateFormat(FSAPIConstants.DDMMYYYY)
					.parse(String.valueOf(valueObj.get(ValueObjectKeys.LAST_TXN_DATE)));
			todayDate = new SimpleDateFormat(FSAPIConstants.DDMMYYYY).parse(date);

		} catch (ParseException e) {
			log.error("Error Occured while try get last transaction date, {}", e.getMessage());
			throw new ServiceException(B2BResponseMessage.SYSTEM_ERROR, B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
		}

		log.info("Last transaction date" + lastTransDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(todayDate);

		Calendar calendarLastTransDate = Calendar.getInstance();
		calendarLastTransDate.setTime(lastTransDate);

		Calendar lastTransDateSunday = Calendar.getInstance();
		lastTransDateSunday.setTime(lastTransDate);
		int weekday = calendarLastTransDate.get(Calendar.DAY_OF_WEEK);
		int days = Calendar.SUNDAY - weekday;
		if (days < 0) {
			days += 7;
		}
		lastTransDateSunday.add(Calendar.DAY_OF_YEAR, days);
		if (!(lastTransDate.equals(todayDate))) {
			if (lastTransDate.before(todayDate)) {
				log.info("Last Transaction date is lesser then transaction date");

				cardLimits.entrySet().stream().filter(q -> !Util.isEmpty(String.valueOf(q.getValue())))
				.filter(p -> p.getKey().endsWith("dailyMaxCount") || p.getKey().endsWith("dailyMaxAmt"))
				.forEach(q -> cardLimits.put(q.getKey(), "0"));
			}
			if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || todayDate.after(lastTransDateSunday.getTime())) {
				log.info("Last Transaction date is lesser then (by week) transaction date");

				cardLimits.entrySet().stream().filter(q -> !Util.isEmpty(String.valueOf(q.getValue())))
				.filter(p -> p.getKey().endsWith("weeklyMaxCount") || p.getKey().endsWith("weeklyMaxAmt"))
				.forEach(q -> cardLimits.put(q.getKey(), "0"));

			}
			if ((cal.get(Calendar.DATE) == 1) || cal.get(Calendar.MONTH) > calendarLastTransDate.get(Calendar.MONTH)) {
				log.info("Last Transaction date is lesser then (by month) transaction date");

				cardLimits.entrySet().stream().filter(q -> !Util.isEmpty(String.valueOf(q.getValue())))
				.filter(p -> p.getKey().endsWith("monthlyMaxCount") || p.getKey().endsWith("monthlyMaxAmt") )
				.forEach(q -> cardLimits.put(q.getKey(), "0"));

			}

			if ((cal.get(Calendar.DATE) == 1 && cal.get(Calendar.MONTH) == Calendar.JANUARY)
					|| (cal.get(Calendar.YEAR) > calendarLastTransDate.get(Calendar.YEAR))) {
				log.info("Last Transaction date is lesser then (by year) transaction date");

				cardLimits.entrySet().stream().filter(q -> !Util.isEmpty(String.valueOf(q.getValue())))
				.filter(p -> p.getKey().endsWith("yearlyMaxCount") || p.getKey().endsWith("yearlyMaxAmt") ||
						p.getKey().endsWith("monthlyMaxCount")  || p.getKey().endsWith("monthlyMaxAmt") )
				.forEach(q -> cardLimits.put(q.getKey(), "0"));
			}

		}

	}

	/**
	 * 
	 * @param channelCode
	 * @return
	 */

	public String getDeliveryChannelShortName(String channelCode) {
		return getJdbcTemplate().queryForObject(QueryConstants.GET_DEL_CHANNEL_SHORTNAME, new Object[] { channelCode },
				String.class);
	}
	/**
	 * 
	 * @param tranCode
	 * @return
	 */
	public String getTransShortNameForTransCode(String tranCode) {
		return getJdbcTemplate().queryForObject(QueryConstants.GET_TRANSACTION_SHORT_NAME, new Object[] { tranCode },
				String.class);
	}
	/**
	 * 
	 * @param cardNumHash
	 * @param productId
	 * @return
	 */

	public String getCardLimitAttributes(String cardNumHash, String productId) {
		return getJdbcTemplate().queryForObject(QueryConstants.GET_CARD_LIMIT_ATTRIBUTES,
				new Object[] { productId, cardNumHash }, String.class);
	}

	/**
	 * 
	 * @param valObject
	 * @param apiName
	 * @param reqMethod
	 * @throws ServiceException
	 */

	public void decryptRequest(Map<String, Object> valObject, String apiName, String reqMethod)
			throws ServiceException {

		try {

			FsApiMaster fsapiBean = transactionServ
					.getFsapiMasterDetailByApiKey(apiName + APIConstants.COLON + reqMethod);
			final String reqEncrType = fsapiBean.getReqEncrypt();
			final String resEncrType = fsapiBean.getResEncrypt();
			valObject.put(ValueObjectKeys.REQUEST_ENCR_TYPE, resEncrType);
			String encrRequest = String.valueOf(valObject.get(APIConstants.ENCR_TEXT_KEY));
			if (APIConstants.Y.equalsIgnoreCase(reqEncrType) && APIHelper.emptyCheck(encrRequest)) {

				final String decReq = AES.decrypt(encrRequest, fsapiKey);

				valObject.put(ValueObjectKeys.DECRYPT_REQUEST, decReq);
				if (APIConstants.GET.equalsIgnoreCase(reqMethod)) {
					final String[] pairs = decReq.split(APIConstants.AMPERSAND);
					for (int i = 0; i < pairs.length; i++) {
						if (!pairs[i].contains(APIConstants.EQUAL)) {
							pairs[i - 1] = pairs[i - 1] + APIConstants.AMPERSAND + pairs[i];
						}
					}
					for (final String pair : pairs) {
						final String[] valuePairs = pair.split(APIConstants.EQUAL);
						if (valuePairs != null && valuePairs.length == 2) {
							valObject.put(valuePairs[0], valuePairs[1]);
						}
					}
				} else {
					JSONObject jsonObj = JsonHelper.isJSONValid(decReq);
					if (jsonObj != null) {
						Map<String, Object> tmpValuMap = JsonHelper.jsonToMap(jsonObj);
						valObject.putAll(tmpValuMap);
					}
				}

			} else if (APIConstants.N.equalsIgnoreCase(reqEncrType) && APIHelper.emptyCheck(encrRequest)
					&& (!APIConstants.GET.equalsIgnoreCase(reqMethod))) {
				JSONObject jsonObj = JsonHelper.isJSONValid(encrRequest);
				if (jsonObj != null) {
					Map<String, Object> tmpValuMap = JsonHelper.jsonToMap(jsonObj);
					valObject.putAll(tmpValuMap);
				}
			}

			valObject.put(ValueObjectKeys.REQ_PARSE, fsapiBean.getReqParse());
			valObject.put(ValueObjectKeys.REGEX_VALIDATION_BYPASS, fsapiBean.getValidationBypass());
			valObject.put(ValueObjectKeys.PARTNER_VALIDATION, fsapiBean.getPartnerValid());

		} catch (APISecurityException ae) {
			log.error("APISecurityException" + ae);
			throw new ServiceException("APISecurityException occured while decript the request",
					B2BResponseCode.SYSTEM_ERROR);
		}  catch (Exception e) {
			log.error("Exception decryptRequest" , e);
			throw new ServiceException("Exception occured while decript the request", B2BResponseCode.SYSTEM_ERROR);
		}
	}
	/**
	 * 
	 * @param valObject
	 * @param encrRequest
	 * @throws ServiceException
	 */

	public void decrypt(Map<String, Object> valObject, String encrRequest)
			throws ServiceException {

		try {

			String decReq = AES.decrypt(encrRequest, fsapiKey);
			valObject.put(ValueObjectKeys.DECRYPT_REQUEST, decReq);

		} catch (APISecurityException ae) {
			log.error("APISecurityException" + ae);
			throw new ServiceException("APISecurityException occured while decript the request",
					B2BResponseCode.SYSTEM_ERROR);
		}  catch (Exception e) {
			log.error("Exception" + e);
			throw new ServiceException("Exception occured while decript the request", B2BResponseCode.SYSTEM_ERROR);
		}
	}


	/**
	 * 
	 * @param valueObject
	 * @return
	 */

	public Map<String,Object> getFundingCheck(Map<String, Object> valueObject){

		return getJdbcTemplate().query(QueryConstants.FUNDING_CHECK_QUERY,  new ResultSetExtractor<Map<String,Object>>(){

			@Override
			public Map<String,Object> extractData(ResultSet rs) throws SQLException {


				while(rs.next()){

					valueObject.put(ValueObjectKeys.PRODUCT_FUNDING, rs.getString(1));
					valueObject.put(ValueObjectKeys.FUND_AMOUNT, rs.getString(2));
					valueObject.put(ValueObjectKeys.FUNDING_OVERRIDE, rs.getString(3));
					valueObject.put(ValueObjectKeys.DENOMINATION, rs.getString(4));
				}
				return valueObject;
			}

		}, valueObject.get(ValueObjectKeys.HASH_CARDNO));
	}
	/**
	 * 
	 * @param valueObject
	 * @return
	 */
	public  Map<String,Object>  checkFundingOption(Map<String, Object> valueObject){
		String override=getFundingCheck(valueObject).get(ValueObjectKeys.FUNDING_OVERRIDE)+"";
		Map<String,String> tempValueMap=new HashMap<>();
		valueObject.put(ValueObjectKeys.TRANAMOUNT,valueObject.get(ValueObjectKeys.DENOMINATION));
		try {
			if(!Util.isEmpty(override) && "Y".equalsIgnoreCase(override)){
				if (FSAPIConstants.CARD_ACTIVATION.equals(valueObject.get(ValueObjectKeys.PRODUCT_FUNDING))
						&&  FSAPIConstants.ORDER_AMOUNT.equals(valueObject.get(ValueObjectKeys.FUND_AMOUNT)) ) {

					for(Entry<String, Object> cardTemp : valueObject.entrySet()){
						tempValueMap.put(cardTemp.getKey(), cardTemp.getValue()+"");
					}
					valueObject.put(ValueObjectKeys.TRAN_LOG_UPDATE, ValueObjectKeys.UPDATE_YES);
					String[] respFields=spilReloadDAO.invokeReload(tempValueMap);
					log.info("ORDER_RESPONSE_CODE:"+ respFields[0]);
					if(B2BResponseCode.SUCCESS.equalsIgnoreCase(respFields[0])){
						valueObject.put(ValueObjectKeys.FSAPIRESPONSECODE, respFields[0]);
					}
				}


			}else{
					getProductFundingCheck(valueObject);

					if (FSAPIConstants.CARD_ACTIVATION.equals(valueObject.get(ValueObjectKeys.B2B_PRODUCT_FUND))
							&&  FSAPIConstants.ORDER_AMOUNT.equals(valueObject.get(ValueObjectKeys.B2B_SOURCE_FUNDING)) ) {	
					for(Entry<String, Object> cardTemp : valueObject.entrySet()){
						tempValueMap.put(cardTemp.getKey(), cardTemp.getValue()+"");
					}
					String[] respFields=spilReloadDAO.invokeReload(tempValueMap);
					log.info("ORDER_RESPONSE_CODE:"+ respFields[0]);
					valueObject.put(ValueObjectKeys.TRAN_LOG_UPDATE, ValueObjectKeys.UPDATE_YES);
					if(B2BResponseCode.SUCCESS.equalsIgnoreCase(respFields[0])){
						valueObject.put(ValueObjectKeys.FSAPIRESPONSECODE, respFields[0]);
					}

				}

			}
		} catch (Exception e) {

			log.info("Exception on funding option check",e);
		}

		return valueObject;
	}
	/**
	 * 
	 * @param temp
	 */
	public void checkDenomination(Map<String, String> temp){
		try {
			String denominationType = "";
			boolean isAmtMatched = false;

			BigDecimal txnAmount = new BigDecimal(String.valueOf(temp.get(FSAPIConstants.DENOMINATION)));

			final String productId = String.valueOf(temp.get(ValueObjectKeys.PROD_CODE));
			Map<String, Map<String, Object>> attributes;

			attributes = productService.getProductAttributes(productId);


			log.info("ENTER");
			if(!CollectionUtils.isEmpty(attributes)) {

				Map<String, Object> productAttributes = attributes.get("Product");

				if (!CollectionUtils.isEmpty(productAttributes) && productAttributes.containsKey(ValueObjectKeys.DENOMINATION_TYPE)
						&& !Objects.isNull(productAttributes.get(ValueObjectKeys.DENOMINATION_TYPE))) {

					denominationType = String.valueOf(productAttributes.get(ValueObjectKeys.DENOMINATION_TYPE));

					log.debug("Product level denomination type : {}",denominationType);
					if (denominationType.equalsIgnoreCase("Fixed")) {

						BigDecimal fixedValue = new BigDecimal(productAttributes.get("denomFixed").toString());
						if (txnAmount.compareTo(fixedValue) == 0) {
							isAmtMatched = true;
						}
					} else if ("Variable".equalsIgnoreCase(denominationType)) {
						BigDecimal minValue = new BigDecimal(productAttributes.get("denomVarMin").toString());
						BigDecimal maxValue = new BigDecimal(productAttributes.get("denomVarMax").toString());

						int res1;
						res1 = txnAmount.compareTo(minValue);
						int res2;
						res2 = txnAmount.compareTo(maxValue);

						if (res1 >=0  && res2 <= 0) {
							isAmtMatched = true;
						}
					} else if ("Select".equalsIgnoreCase(denominationType)) {
						String selAmount = productAttributes.get("denomSelect").toString();
						String[] selectedAmounts = selAmount.split(",");
						List<String> listAmount = new ArrayList<>(Arrays.asList(selectedAmounts));

						for (String strAmount : listAmount) {
							BigDecimal amountVal = new BigDecimal(strAmount);
							if (txnAmount.compareTo(amountVal) == 0) {
								isAmtMatched = true;
							}
						}
					}
					if (isAmtMatched) {
						log.info("Denomination check passed successfully");
						temp.put(ValueObjectKeys.FSAPIRESPONSECODE, B2BResponseCode.SUCCESS);
						temp.put(ValueObjectKeys.RESPONSEMESSAGE, B2BResponseMessage.SUCCESS);
					} else {
						log.error("Invalid input amount");

						temp.put(ValueObjectKeys.FSAPIRESPONSECODE, B2BResponseCode.INVALID_DENOMINATION);
						temp.put(ValueObjectKeys.RESPONSEMESSAGE, FSAPIConstants.DENOMERRMSG);

					}
				}
			}else {
				log.error("Product does not exist to check denomination");
			}
			log.info("EXIT");

		} catch (ServiceException e) {
			logger.info(e);

			temp.put(ValueObjectKeys.FSAPIRESPONSECODE, B2BResponseCode.INVALID_DENOMINATION);
			temp.put(ValueObjectKeys.RESPONSEMESSAGE, FSAPIConstants.DENOMERRMSG);
		}
	}

	/**
	 * 
	 * @param valueObject
	 * @param query
	 * @return
	 */

	public Map<String,String> getQueryCard(Map<String,String> valueObject,String query){
		valueObject.put(ValueObjectKeys.ISHAVEESULT, ValueObjectKeys.ISHAVECARDQUERYRESULTYES);
		return getJdbcTemplate().query(query,  new ResultSetExtractor<Map<String,String>>(){

			@Override
			public Map<String,String> extractData(ResultSet rsCardNo1) throws SQLException {


				if(rsCardNo1.next()){

					valueObject.put(ValueObjectKeys.CARDNO, rsCardNo1.getString("card_no"));
					valueObject.put(ValueObjectKeys.CARDNUMBER, rsCardNo1.getString("card_no"));
					valueObject.put(ValueObjectKeys.CURRENCY_CODE, rsCardNo1.getString("currencycode"));
					valueObject.put(FSAPIConstants.RESP_PRXOY_NUMBER, rsCardNo1.getString("proxy"));
					valueObject.put(FSAPIConstants.RESP_SERIAL_NUMBER, rsCardNo1.getString("serial"));
					valueObject.put(ValueObjectKeys.HASH_CARDNO, rsCardNo1.getString("card_hash"));
					valueObject.put(ValueObjectKeys.CARDSTATUS, rsCardNo1.getString("cardStatus"));
					valueObject.put(ValueObjectKeys.PROD_CODE, rsCardNo1.getString("prodCode"));
					valueObject.put(ValueObjectKeys.CARD_ACCOUNT_ID, rsCardNo1.getString("accountId"));
					valueObject.put(ValueObjectKeys.EXPIRY_DATE, rsCardNo1.getString("origExpDate"));
					valueObject.put(ValueObjectKeys.CARD_ACTIVATION_DATE, rsCardNo1.getString("actDate"));
					valueObject.put(ValueObjectKeys.LAST_TXN_DATE, rsCardNo1.getString("lastTxnDate"));
					valueObject.put(ValueObjectKeys.FIRST_TIMETOPUP_FLAG, rsCardNo1.getString("firstTopup"));
				}else{
					valueObject.put(ValueObjectKeys.ISHAVEESULT, ValueObjectKeys.ISHAVECARDQUERYRESULTNO);
				}
				return valueObject;
			}

		},valueObject.get(ValueObjectKeys.VALUE));
	}

	public void getSerialDtls(String serialNumber,String CardNumberHash,Map<String,Object> serialMap)throws ServiceException {

		try {

			getJdbcTemplate().query(QueryConstants.GET_SERIAL_DETAILS_USING_HASH,  new ResultSetExtractor<Map<String,Object>>(){
				@Override
				public Map<String,Object> extractData(ResultSet rs) throws SQLException {

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


			},serialNumber,CardNumberHash);


		}

		catch (Exception e) {
			log.info("Exception when getting details based on serialnumber: " +e); 	
			throw new ServiceException(FSAPIConstants.INVALID_SERIAL_NUMBER,
					B2BResponseCode.INVALID_SERIAL_NUMBER);
			
		}
		
	}
	
	/**
	 * Reloadable Flag check
	 */
	
	public void checkReloadableFlag(Map<String, String> temp) {

		try {
			String reloadableFlagCheck = "";

			String productId = String.valueOf(temp.get(ValueObjectKeys.PROD_CODE));
			Map<String, Map<String, Object>> attributes;

			attributes = productService.getProductAttributes(productId);
			if (!CollectionUtils.isEmpty(attributes)) {

				Map<String, Object> productAttributes = attributes.get(ValueObjectKeys.PRODUCT);
				if (!CollectionUtils.isEmpty(productAttributes)
						&& productAttributes.containsKey(ValueObjectKeys.RELOADABLE_FLAG)
						&& !Objects.isNull(productAttributes.get(ValueObjectKeys.RELOADABLE_FLAG))) {

					reloadableFlagCheck = String.valueOf(productAttributes.get(ValueObjectKeys.RELOADABLE_FLAG));
					if (reloadableFlagCheck.equalsIgnoreCase(ValueObjectKeys.DISABLE)) {
						
						log.error("Reloadable Flag is disabled, {}",B2BResponseMessage.TOP_UP_NOT_SUPPORTED);

						temp.put(ValueObjectKeys.FSAPIRESPONSECODE,
								B2BResponseCode.TOP_UP_NOT_SUPPORTED);
						temp.put(ValueObjectKeys.RESPONSEMESSAGE, B2BResponseMessage.FAILURE);
						
					} else {
					
						log.info("Relodable Flag check is Successful");
						temp.put(ValueObjectKeys.FSAPIRESPONSECODE, B2BResponseCode.SUCCESS);
						temp.put(ValueObjectKeys.RESPONSEMESSAGE, B2BResponseMessage.SUCCESS);
					}
				}
			} else {
				log.error("Product does not exist to check reloadableFlag");
			}

		} catch (ServiceException e) {
			logger.info(e);

			temp.put(ValueObjectKeys.FSAPIRESPONSECODE, B2BResponseCode.TOP_UP_NOT_SUPPORTED);
			temp.put(ValueObjectKeys.RESPONSEMESSAGE, B2BResponseMessage.TOP_UP_NOT_SUPPORTED);
		}

	}
	
	
	
	public void getSerialDtlsForCardStatus(Map<String,Object> serialOuterMap, Map<String,Object> serialMap)throws ServiceException {

		try {

			getJdbcTemplate().query(QueryConstants.GET_CARD_DETAILS_USING_SERIAL_NO,  new ResultSetExtractor<Map<String,Object>>(){
				@Override
				public Map<String,Object> extractData(ResultSet rs) throws SQLException {

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


			},serialOuterMap.get("serialNumber"));


		}

		catch (Exception e) {
			log.info("Exception when getting details based on serialnumber: " +e); 	
			throw new ServiceException(FSAPIConstants.INVALID_SERIAL_NUMBER,
					B2BResponseCode.INVALID_SERIAL_NUMBER);
			
		}
		
	}
	public void checkProductValidity(Map<String, Object> valObj) {
		boolean isValid = commonService.checkProductValidity(String.valueOf(valObj.get(ValueObjectKeys.PROD_CODE)));
		
		if(!isValid) {
			log.error("Expired product, updating card status to expired product");
			productDao.updateCardStatus(
					String.valueOf(valObj.get(ValueObjectKeys.HASH_CARDNO)),
					CCLPConstants.EXPIRED_PRODUCT_CARD_STATUS);
			valObj.put(ValueObjectKeys.CARDSTATUS, CCLPConstants.EXPIRED_PRODUCT_CARD_STATUS);
			valObj.put(ValueObjectKeys.FSAPIRESPONSECODE,B2BResponseCode.INVALID_CARD_STATE);
			valObj.put(ValueObjectKeys.RESPONSEMESSAGE, FSAPIConstants.EXPIRED_PRODUCT);
		}else {
			logger.debug("Valid Product");
			valObj.put(ValueObjectKeys.FSAPIRESPONSECODE,B2BResponseCode.SUCCESS);
		}
		
	}
	
	/**
	 * 
	 * @param valueObj
	 * @return
	 * @throws ServiceException
	 */

	public String validateCardStatusForFinancialTxn(Map<String, Object> valueObj) throws ServiceException {

		String cardStatusDesc = null;
		String cardStatusCode = null;
		String channelShortName = null;
		String txnShortName = null;
		String cardStatusAttributeKey = null;

		Map<String, Map<String, Object>> productAttributes = null;
		try {
			if (!Objects.isNull(valueObj.get(ValueObjectKeys.CARDSTATUS))) {

				cardStatusCode = valueObj.get(ValueObjectKeys.CARDSTATUS).toString();
				txnShortName = valueObj.get(ValueObjectKeys.TRANS_SHORT_NAME)+""; 
				channelShortName = (String) valueObj.get("x-incfs-channel")+"";
				String productStr=productDao.getProductAttributesByProductId(valueObj.get(ValueObjectKeys.PROD_CODE)+"");
				productAttributes = Util.jsonToMap(productStr);
				Map<String, Object> cardStatusAttributes = productAttributes.get("Card Status");
				Map<String, String> cardStatusDefs=transactionServ.getAllCardStatus();
				cardStatusDesc = cardStatusDefs.get(cardStatusCode);
				cardStatusAttributeKey = (channelShortName.toLowerCase() + "_" + txnShortName + "_" + cardStatusDesc);

				if (!CollectionUtils.isEmpty(cardStatusAttributes)
						&& !Objects.isNull(cardStatusAttributes.get(cardStatusAttributeKey)) && !("CLOSED".equalsIgnoreCase(cardStatusDesc))) {
					/**
					 * card status attribute value is not true throw Service Exception
					 */
					if (!cardStatusAttributes.get(cardStatusAttributeKey).toString().equalsIgnoreCase(FSAPIConstants.ORDER_FSAPI_TRUE)) {
						if ("INACTIVE".equalsIgnoreCase(cardStatusDesc)) {
							return  B2BResponseCode.INVALID_CARD_STATE;
						} else if ("ACTIVE".equalsIgnoreCase(cardStatusDesc)) {
							return  B2BResponseCode.INVALID_CARD_STATE;
						} else if ("LOST-STOLEN".equalsIgnoreCase(cardStatusDesc)) {
							return B2BResponseCode.INVALID_CARD_STATE;
						} else if ("DAMAGE".equalsIgnoreCase(cardStatusDesc)) {
							return B2BResponseCode.INVALID_CARD_STATE;
						} else if ("EXPIRED CARD".equalsIgnoreCase(cardStatusDesc)) {
							return B2BResponseCode.INVALID_CARD_STATE;
						} else {
							return B2BResponseCode.INVALID_CARD_STATE;
						}
					}
				} 
				
				else {
					return B2BResponseCode.INVALID_CARD_STATE;
				}

			} else {
				return B2BResponseCode.INVALID_CARD_STATE;
			}
		} catch ( Exception e) {
			log.info("Exceptin occured validateCardStatusForFinancialTxn",e);
		}

		return B2BResponseCode.SUCCESS;
	}
	
	
}



