package com.incomm.cclp.fsapi.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.fsapi.bean.ErrorMsgBean;
import com.incomm.cclp.fsapi.constants.B2BResponseCode;
import com.incomm.cclp.fsapi.constants.B2BResponseMessage;
import com.incomm.cclp.fsapi.constants.FSAPIConstants;
import com.incomm.cclp.fsapi.dao.CardStatusInquiryDAO;
import com.incomm.cclp.fsapi.service.CommonService;
import com.incomm.cclp.fsapi.service.TransactionService;

@Repository
public class CardStatusInquiryDAOImpl extends JdbcDaoSupport implements CardStatusInquiryDAO{

	@Autowired
	@Qualifier("transactionalDs")
	private DataSource dataSource;
	
	@Autowired
    public void setDs(@Qualifier("orderDs") DataSource dataSource) {
         setDataSource(dataSource);
    }
	
	@Autowired
	public JdbcTemplate jdbctemplate;
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	CommonService commonService;
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@PostConstruct
	private void postConstruct() {
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
		
		@Override
		public void getyyyyMMdd(Map<String,Object> tempValueMap,java.sql.Date date)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			tempValueMap.put(ValueObjectKeys.BUSINESS_DATE, sdf.format(date)) ;
		}
		
		@Override
		public void gethhmmss(Map<String,Object> tempValueMap,java.util.Date date)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("kkmmss");
			tempValueMap.put(ValueObjectKeys.BUSINESS_TIME, sdf.format(date)) ;
		}
		
		@Override
		public void checkOrderId(Map<String, Object> valuHashMap,
				List<ErrorMsgBean> errorList) {
			
			String quryforPrxy="";
			
			logger.debug("checkOrderId  start");
			
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			
			if(FSAPIConstants.PROXYNUMBER.equalsIgnoreCase((String)valuHashMap.get(FSAPIConstants.TYPE))){
				quryforPrxy =" c.proxy_number=:proxy_number ";
				parameters.addValue("proxy_number", (String) valuHashMap.get(FSAPIConstants.CARDSTAT_START));
			}else if (FSAPIConstants.SERIALNUMBER.equalsIgnoreCase((String)valuHashMap.get(FSAPIConstants.TYPE))){
				quryforPrxy=" c.serial_number=:serial_number";
				parameters.addValue("serial_number", (String) valuHashMap.get(FSAPIConstants.CARDSTAT_START));
			}
			
			String startTypeOrderIDqry = QueryConstants.GET_ORDER_ID +quryforPrxy;
			
			String startOrderId = namedParameterJdbcTemplate.queryForObject(startTypeOrderIDqry,parameters, String.class);
			
			if(FSAPIConstants.PROXYNUMBER.equalsIgnoreCase((String)valuHashMap.get(FSAPIConstants.TYPE))){
				quryforPrxy =" c.proxy_number=:proxy_number ";
				parameters.addValue(FSAPIConstants.PROXY_NUMBER, (String) valuHashMap.get(FSAPIConstants.CARDSTAT_END));
			}else if (FSAPIConstants.SERIALNUMBER.equalsIgnoreCase((String)valuHashMap.get(FSAPIConstants.TYPE))){
				quryforPrxy=" c.serial_number=:serial_number";
				parameters.addValue(FSAPIConstants.SERIAL_NUMBER, (String) valuHashMap.get(FSAPIConstants.CARDSTAT_END));
			}
			
			String endTypeOrderIDqry = QueryConstants.GET_ORDER_ID +quryforPrxy;
			
			String endOrderId = namedParameterJdbcTemplate.queryForObject(endTypeOrderIDqry,parameters, String.class);
			
			if(!startOrderId.equalsIgnoreCase(endOrderId)){
				logger.error(B2BResponseMessage.START_AND_END_SERIAL_OR_PROXY_NUMBERS_NOT_BELONGS_TO_SAME_ORDER);
	       	 ErrorMsgBean errorbean=new ErrorMsgBean();
	    			errorbean.setErrorMsg(B2BResponseMessage.START_AND_END_SERIAL_OR_PROXY_NUMBERS_NOT_BELONGS_TO_SAME_ORDER);
	    			errorbean.setRespCode(B2BResponseCode.START_AND_END_SERIAL_OR_PROXY_NUMBERS_NOT_BELONGS_TO_SAME_ORDER);
	    			errorList.add(errorbean);
	           }
		}
		
		@Override
		public void processCardStatusInquiry(Map<String, Object> valuHashMap,List<ErrorMsgBean> errorList) throws ServiceException{
			
			final List<Map<String, Object>> tempItemList = new LinkedList<>();
			
			try {
			List<String> listOfProxyorSerial = getListOfProxyOrSerial(valuHashMap);
			for(String proxyOrSerial:listOfProxyorSerial)
			{
				final Map<String, Object> tempLineItemMap = new HashMap<>();
				
				valuHashMap.put(ValueObjectKeys.BUSINESS_DATE, (String)valuHashMap.get(ValueObjectKeys.BUSINESS_DATE));
				valuHashMap.put(ValueObjectKeys.BUSINESS_TIME, (String)valuHashMap.get(ValueObjectKeys.BUSINESS_TIME));
				valuHashMap.put(ValueObjectKeys.API_NAME, (String)valuHashMap.get(ValueObjectKeys.API_NAME));
				valuHashMap.put(ValueObjectKeys.REQUEST_METHOD_TYPE, (String)valuHashMap.get(ValueObjectKeys.REQUEST_METHOD_TYPE));
				valuHashMap.put(FSAPIConstants.CARDSTAT_VALUE, proxyOrSerial);
				getCardDetls(valuHashMap);
				
				if(FSAPIConstants.PROXYNUMBER.equalsIgnoreCase((String) valuHashMap.get(FSAPIConstants.TYPE)))
				{
					tempLineItemMap.put(FSAPIConstants.PROXYNUMBER, valuHashMap.get(FSAPIConstants.CARDSTAT_VALUE));
					tempLineItemMap.put(FSAPIConstants.SERIALNUMBER, valuHashMap.get(FSAPIConstants.SERIALNUMBER));
				}else if(FSAPIConstants.SERIALNUMBER.equalsIgnoreCase((String) valuHashMap.get(FSAPIConstants.TYPE))){
						tempLineItemMap.put(FSAPIConstants.SERIALNUMBER, valuHashMap.get(FSAPIConstants.CARDSTAT_VALUE));
						tempLineItemMap.put(FSAPIConstants.PROXYNUMBER, valuHashMap.get(FSAPIConstants.PROXYNUMBER));
				}
				 
				tempLineItemMap.put(FSAPIConstants.STATUS, transactionService.getAllCardStatus().get(valuHashMap.get(FSAPIConstants.CARD_STATUS)));
				String cardNo=(String) valuHashMap.get(ValueObjectKeys.CARDNO);
				tempLineItemMap.put(FSAPIConstants.PANLASTFOUR, cardNo.substring(cardNo.length()-4));
				tempLineItemMap.put(ValueObjectKeys.AVAILABLE_BALACE,  valuHashMap.get(ValueObjectKeys.AVAILABLE_BALACE));
				tempLineItemMap.put(FSAPIConstants.EXPDATE, valuHashMap.get(FSAPIConstants.EXPDATE));
				tempItemList.add(tempLineItemMap);
			
			}
			}catch(Exception e) {
				throw new ServiceException(B2BResponseMessage.SYSTEM_ERROR,B2BResponseCode.SYSTEM_ERROR);
			}
			valuHashMap.put("cards", tempItemList);
		}
		
		 @Override
		 public  Map<String,Object>  getCardDetls(Map<String, Object> valuHashMap) throws SQLException{

			 String quryforCardPrxy="";
			 Map<String,Object> cardMap;

			 if(FSAPIConstants.PROXYNUMBER.equalsIgnoreCase((String) valuHashMap.get(FSAPIConstants.TYPE))){
				 quryforCardPrxy ="proxy_number=?";
			 }else if (FSAPIConstants.SERIALNUMBER.equalsIgnoreCase((String) valuHashMap.get(FSAPIConstants.TYPE))){
				 quryforCardPrxy="serial_number=?";
			 }

			 //Get the card number for  card status is other then 0 and 9
			 String queryCardNo1=" SELECT fn_dmaps_main(c.card_num_encr) AS card_no,c.proxy_number as proxy, c.serial_number as serial, to_char(c.expiry_date,'MM/YY') as expdate,"+
					 " c.card_status as cardStatus,ap.available_balance FROM card c,account_purse ap WHERE "+quryforCardPrxy+" and c.account_id=ap.account_id and card_status not in( '0','9') order by DATE_OF_ACTIVATION desc ";

			 //Get the card number for 0 card stats
			 String queryCardNo2=" SELECT fn_dmaps_main(c.card_num_encr)  AS card_no,c.proxy_number as proxy, c.serial_number as serial , to_char(c.expiry_date,'MM/YY') as expdate,"+
					 " c.card_status as cardStatus,ap.available_balance FROM card c,account_purse ap WHERE "+quryforCardPrxy+" and c.account_id=ap.account_id and c.card_status = 0 order by c.ins_date desc ";

			 //Get the card number for 9 card stats
			 String queryCardNo3=" SELECT fn_dmaps_main(c.card_num_encr) AS card_no,c.proxy_number as proxy, c.serial_number as serial,to_char(c.expiry_date,'MM/YY') as expdate, "+
					 " c.card_status as cardStatus,ap.available_balance FROM card c,account_purse ap WHERE "+quryforCardPrxy+" and c.account_id=ap.account_id AND c.card_status = 9 order by c.ins_date desc ";

			cardMap = getCardDetails(queryCardNo1,valuHashMap.get(FSAPIConstants.CARDSTAT_VALUE));
			
			if(cardMap.isEmpty())
			{
				cardMap = getCardDetails(queryCardNo2,valuHashMap.get(FSAPIConstants.CARDSTAT_VALUE));	
				if(cardMap.isEmpty())
				{
					cardMap = getCardDetails(queryCardNo3,valuHashMap.get(FSAPIConstants.CARDSTAT_VALUE));
				}
			}
			valuHashMap.putAll(cardMap);
			return cardMap;
		 }
		 
		public Map<String,Object> getCardDetails(String query, Object value)
		{
			Map<String,Object> cardMap = new HashMap<>();
			
			return getJdbcTemplate().query(query, new Object[]{value},  new ResultSetExtractor<Map<String,Object>>(){

				 @Override
				 public Map<String, Object> extractData(ResultSet rs)
						 throws SQLException {

					 if(rs.next()){
						 cardMap.put(ValueObjectKeys.CARDNO, rs.getString(1));
						 cardMap.put(FSAPIConstants.PROXYNUMBER, rs.getString(2));
						 cardMap.put(FSAPIConstants.SERIALNUMBER	, rs.getString(3));
						 cardMap.put(FSAPIConstants.EXPDATE, rs.getString(4));
						 cardMap.put(FSAPIConstants.CARD_STATUS, rs.getString(5));
						 cardMap.put(ValueObjectKeys.AVAILABLE_BALACE, rs.getString(6));
					 }
					return cardMap;
					
				 }
			 });
		}
		 
		
		@Override
		public List<String> getListOfProxyOrSerial(Map<String, Object> valuHashMap) {
			
			String quryforPrxy="";
			
			logger.debug("getListofProxyOrSerial  start");
			
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			
			if(FSAPIConstants.PROXYNUMBER.equalsIgnoreCase((String)valuHashMap.get(FSAPIConstants.TYPE))){
				quryforPrxy =" proxy_number";
			}else if (FSAPIConstants.SERIALNUMBER.equalsIgnoreCase((String)valuHashMap.get(FSAPIConstants.TYPE))){
				quryforPrxy=" serial_number";
			}
			
			String getListOfProxyOrSerial = "select distinct "+quryforPrxy+" from card where to_number("+quryforPrxy+") between to_number(:1) and to_number(:2)";
			
			parameters.addValue("1", (String) valuHashMap.get(FSAPIConstants.CARDSTAT_START));
			parameters.addValue("2", (String) valuHashMap.get(FSAPIConstants.CARDSTAT_END));
			
			return namedParameterJdbcTemplate.queryForList(getListOfProxyOrSerial,parameters, String.class);
			
		}
		
		public String getRRN(String seqName) {
			final StringBuilder rrnQuery = new StringBuilder();
			rrnQuery
					.append("SELECT TO_CHAR (TO_CHAR (SYSDATE, 'YYMMDDHH24MISS')|| LPAD ( ");
			rrnQuery.append(seqName);
			rrnQuery.append(".NEXTVAL, 3, '0')) RRN FROM DUAL");
			
			return (getJdbcTemplate().queryForObject(rrnQuery.toString(), new Object[] {}, String.class));
		}


		@Override
		public void checkPartnerId(Map<String, Object> valueHashMap,
			List<ErrorMsgBean> errorList) {
			String quryforPrxy="";
			
			logger.debug("checkPartnerId  start");
			
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			
			if(FSAPIConstants.PROXYNUMBER.equalsIgnoreCase((String)valueHashMap.get(FSAPIConstants.TYPE))){
				quryforPrxy =" proxy_number";
			}else if (FSAPIConstants.SERIALNUMBER.equalsIgnoreCase((String)valueHashMap.get(FSAPIConstants.TYPE))){
				quryforPrxy=" serial_number";
			}
			
			String partnerIdCheck = "select count(1) as cnt from product p ,card c  where p.product_id=c.product_id and p.partner_id=:1 and c."+quryforPrxy+" between to_number(:2) and to_number(:3)";
			
			parameters.addValue("1", (String) valueHashMap.get(FSAPIConstants.ORDER_PARTNERID));
			parameters.addValue("2", (String) valueHashMap.get(FSAPIConstants.CARDSTAT_START));
			parameters.addValue("3", (String) valueHashMap.get(FSAPIConstants.CARDSTAT_END));
			
			Integer count = namedParameterJdbcTemplate.queryForObject(partnerIdCheck,parameters, Integer.class);
			
			if(count==0)
			{
				logger.error(B2BResponseMessage.INVALID_PARTNER_ID);
				ErrorMsgBean errorbean=new ErrorMsgBean();
    			errorbean.setErrorMsg(B2BResponseMessage.INVALID_PARTNER_ID);
    			errorbean.setRespCode(B2BResponseCode.INVALID_PARTNER_ID);
    			errorList.add(errorbean);
			}
			
		}


		@Override
		public void processCardStatus(Map<String, Object> valueHashMap,
				List<ErrorMsgBean> errorList) {

			StringBuilder qry=new StringBuilder();
			
			final List<Map<String, Object>> tempItemList = new LinkedList<>();
			
			qry.append("SELECT fn_dmaps_main(c.card_num_encr) AS card_no,c.proxy_number as proxy, c.serial_number as serial,to_char(c.expiry_date,'MM/YY') as expdate, "
					+ "c.card_status as cardStatus,ap.available_balance FROM card c,account_purse ap,product p where ap.account_id=c.account_id and c.product_id=p.product_id and p.partner_id=? and ");
			
			if (FSAPIConstants.CARDSTAT_EXPIRES_IN.equalsIgnoreCase((String) valueHashMap.get(FSAPIConstants.CARDSTAT_CRITERIA))) {
				qry.append(" TO_CHAR(c.expiry_date,'mmyy')= ? ");
			} else {
				qry.append(" c.expiry_date between sysdate and sysdate+? ");
			}
			
			 getJdbcTemplate().query(qry.toString(), new Object[]{valueHashMap
					.get(FSAPIConstants.ORDER_PARTNERID), valueHashMap.get(FSAPIConstants.CARDSTAT_VALUE)}, new ResultSetExtractor<List<Map<String,Object>>>(){

				@Override
				public List<Map<String,Object>> extractData(ResultSet rs)
						throws SQLException {
					while(rs.next())
					{
						final Map<String, Object> cardMap = new HashMap<>();
						
						String cardNo=rs.getString(1);
						cardMap.put(FSAPIConstants.PANLASTFOUR, cardNo.substring(cardNo.length()-4));
						 cardMap.put(FSAPIConstants.PROXYNUMBER, rs.getString(2));
						 cardMap.put(FSAPIConstants.SERIALNUMBER	, rs.getString(3));
						 cardMap.put(FSAPIConstants.EXPDATE, rs.getString(4));
						 try {
							cardMap.put(FSAPIConstants.STATUS, transactionService.getAllCardStatus().get(rs.getString(5)));
						} catch (ServiceException e) {
							logger.info("Exception occured while get card status definitions: " ,e);
						}
						 cardMap.put(ValueObjectKeys.AVAILABLE_BALACE, rs.getString(6));
						 tempItemList.add(cardMap);
					}
					
					valueHashMap.put("cards", tempItemList);
					return tempItemList;
				} 
			}); 
		}
		
		@Override
		public void processCardStatusList(Map<String, Object> valueHashMap,
				List<ErrorMsgBean> errorList) {
			
			StringBuilder qry=new StringBuilder();
			
			final List<Map<String, Object>> tempItemList = new LinkedList<>();
			Map<String, String> serialHash = new HashMap<>();
			
			qry.append("SELECT fn_dmaps_main(c.card_num_encr) AS card_no,c.proxy_number as proxy, c.serial_number as serial,to_char(c.expiry_date,'MM/YY') as expdate, "
					+ "c.card_status as cardStatus,ap.available_balance,c.card_num_hash cardNoHash FROM card c,account_purse ap,product p where ap.account_id=c.account_id and c.product_id=p.product_id and p.partner_id=? and ");

			if(FSAPIConstants.PROXYNUMBER.equalsIgnoreCase((String)valueHashMap.get(FSAPIConstants.TYPE))){
				qry.append(" proxy_number in(  ");
			} else {
				qry.append(" serial_number in ( ");
			}
			final Set<String> cardSet =  new LinkedHashSet<>( Arrays.asList(String .valueOf(valueHashMap.get(FSAPIConstants.CARDSTAT_VALUE)).split(",")));

			qry.append(String.join(",", cardSet));
			qry.append(")");
			
			 getJdbcTemplate().query(qry.toString(), new Object[]{valueHashMap.get(FSAPIConstants.ORDER_PARTNERID)},new ResultSetExtractor<List<Map<String,Object>>>(){

				@Override
				public List<Map<String,Object>> extractData(ResultSet rs)
						throws SQLException {
					while(rs.next())
					{
						final Map<String, Object> cardMap = new HashMap<>();
						String cardNo=rs.getString(1);
						cardMap.put(FSAPIConstants.PANLASTFOUR, cardNo.substring(cardNo.length()-4));
						 cardMap.put(FSAPIConstants.PROXYNUMBER, rs.getString(2));
						 cardMap.put(FSAPIConstants.SERIALNUMBER	, rs.getString(3));
						 cardMap.put(FSAPIConstants.EXPDATE, rs.getString(4));
						 try {
								cardMap.put(FSAPIConstants.STATUS, transactionService.getAllCardStatus().get(rs.getString(5)));
							} catch (ServiceException e) {
								logger.info("Exception occured while get card status definitions: " ,e);
							}
						 cardMap.put(ValueObjectKeys.AVAILABLE_BALACE, rs.getString(6));
						 serialHash.put(rs.getString(7), rs.getString(3));
						 tempItemList.add(cardMap);
					}
					
					if(tempItemList.isEmpty())
					{
							logger.error(B2BResponseMessage.INVALID_PARTNER_ID);
							ErrorMsgBean errorbean=new ErrorMsgBean();
			    			errorbean.setErrorMsg(B2BResponseMessage.INVALID_PARTNER_ID);
			    			errorbean.setRespCode(B2BResponseCode.INVALID_PARTNER_ID);
			    			errorList.add(errorbean);
					}
					valueHashMap.put("cards", tempItemList);
					valueHashMap.put("serialHashMap", serialHash);
					//valueHashMap.put(FSAPIConstants.ORDER_RESPONSE, tempItemList );
					return tempItemList;
				} 
			}); 
		}

}
