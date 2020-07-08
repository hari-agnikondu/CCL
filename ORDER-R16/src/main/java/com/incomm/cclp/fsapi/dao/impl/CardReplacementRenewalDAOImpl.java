package com.incomm.cclp.fsapi.dao.impl;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.fsapi.bean.ErrorMsgBean;
import com.incomm.cclp.fsapi.constants.B2BResponseCode;
import com.incomm.cclp.fsapi.constants.B2BResponseMessage;
import com.incomm.cclp.fsapi.constants.FSAPIConstants;
import com.incomm.cclp.fsapi.dao.CardReplacementRenewalDAO;
/**
 * 
 * @author sampathkumarl
 *
 */
@Repository
public class CardReplacementRenewalDAOImpl extends JdbcDaoSupport implements CardReplacementRenewalDAO {

	private final Logger log = LogManager.getLogger(this.getClass());

	@Autowired
	public void setDs(@Qualifier("orderDs") DataSource dataSource) {
		setDataSource(dataSource);
	}
	@Autowired
	public JdbcTemplate jdbctemplate;


	/**
	 * saveCardReplacementOrder REPLACERENEWAL_DTLStable
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void saveCardReplacementOrder(Map<String, Object> valuHashMap) throws ServiceException {

		try{

			LinkedList<Map<String,Object>> cardlist = (LinkedList<Map<String, Object>>) valuHashMap.get(FSAPIConstants.CARDS);
			log.debug("cardlist " + cardlist);
			Map<String,Object> cardDetlsMap=new HashMap<>();
			String replacementOrderId=getCardReplacementOrderID();
			valuHashMap.put(FSAPIConstants.ORDERID, replacementOrderId);

			getJdbcTemplate().batchUpdate(QueryConstants.REPLACERENEWAL_DTLS, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement repalcestmt, int i) throws SQLException {

					Map<String,Object>	mapCards= cardlist.get(i);

					for (Entry<String, Object> cardTemp : mapCards.entrySet()) {
						cardDetlsMap.put(cardTemp.getKey(), cardTemp.getValue());
					}


					repalcestmt.setString(1,replacementOrderId);
					repalcestmt.setString(2, (String)cardDetlsMap.get(FSAPIConstants.REPLACEMENT_TYPE));
					repalcestmt.setString(3, (String)cardDetlsMap.get(FSAPIConstants.REPLACEMENT_VALUE));
					repalcestmt.setString(4, (String)cardDetlsMap.get(FSAPIConstants.REPLACEMENT_ACTION));
					repalcestmt.setString(5, (String)cardDetlsMap.get(FSAPIConstants.REPLACEMENT_SHIPPINGMETHOD));
					repalcestmt.setString(6, (String)cardDetlsMap.get(FSAPIConstants.REPLACEMENT_REQUESTREASON));
					repalcestmt.setString(7, String.valueOf(cardDetlsMap.get(FSAPIConstants.REPLACEMENT_ISFEEWAIVED)));
					repalcestmt.setString(8, (String)cardDetlsMap.get(FSAPIConstants.REPLACEMENT_COMMENTS));
					repalcestmt.setString(9, (String)cardDetlsMap.get(FSAPIConstants.REPLACEMENT_SHIPTOCOMPANYNAME));
					repalcestmt.setString(10, (String)cardDetlsMap.get(FSAPIConstants.REPLACEMENT_FIRSTNAME));
					repalcestmt.setString(11, (String)cardDetlsMap.get(FSAPIConstants.REPLACEMENT_MIDDLEINITIAL));
					repalcestmt.setString(12, (String)cardDetlsMap.get(FSAPIConstants.REPLACEMENT_LASTNAME));
					repalcestmt.setString(13, (String)cardDetlsMap.get(FSAPIConstants.REPLACEMENT_PHONE));
					repalcestmt.setString(14, (String)cardDetlsMap.get(FSAPIConstants.REPLACEMENT_EMAIL));

					final Map<String, String> addrMap = (Map<String, String>) cardDetlsMap.get(FSAPIConstants.ORDER_SHIP_ADDRESS);
					if (addrMap != null) {
						repalcestmt.setString(15, (String)addrMap.get(FSAPIConstants.REPLACEMENT_ADDRESSLINE1));
						repalcestmt.setString(16, (String)addrMap.get(FSAPIConstants.REPLACEMENT_ADDRESSLINE2));
						repalcestmt.setString(17, (String)addrMap.get(FSAPIConstants.REPLACEMENT_ADDRESSLINE3));
						repalcestmt.setString(18, (String)addrMap.get(FSAPIConstants.REPLACEMENT_CITY));
						repalcestmt.setString(19, (String)addrMap.get(FSAPIConstants.REPLACEMENT_STATE));
						repalcestmt.setString(20, (String)addrMap.get(FSAPIConstants.REPLACEMENT_POSTALCODE));
						repalcestmt.setString(21, (String)addrMap.get(FSAPIConstants.REPLACEMENT_COUNTRY));
					} else {
						repalcestmt.setString(15, null);
						repalcestmt.setString(16, null);
						repalcestmt.setString(17, null);
						repalcestmt.setString(18, null);
						repalcestmt.setString(19, null);
						repalcestmt.setString(20, null);
						repalcestmt.setString(21, null);
					}

				}
				@Override
				public int getBatchSize() {
					return cardlist.size();
				}
			});

		}
		catch(Exception exp){
			log.info("expception while inserting replacement or renewal details..",exp);
			log.error("expception while inserting replacement or renewal details.."+exp);
		}
	}



	/**
	 * validating ProxySerial number and partner 
	 */

	public void validateProxySerial(Map<String,Object> valueMap,List<ErrorMsgBean> errorList)throws SQLException{
		String prxyserialflag="";
		ErrorMsgBean   errorbean=new ErrorMsgBean();

		try{  
			Map<String,Object> mapForProxySerialCnt=new HashMap<>();
			Map<String,Object> mapForpartnerIdCNT=new HashMap<>();

			log.debug("Reload id "+(String)valueMap.get(FSAPIConstants.ORDERID));
			
			List<Map<String, Object>> cardTypeValueList =getJdbcTemplate().queryForList(QueryConstants.QUERYORDERDETAILS,(String) valueMap.get(FSAPIConstants.ORDERID));
			ListIterator<Map<String, Object>> list=cardTypeValueList.listIterator();
			
			while(list.hasNext()){
				Map<String,Object> cardTypeValueMap=(Map<String,Object>)list.next();
				
			if(cardTypeValueMap.containsKey(ValueObjectKeys.REPLACERENEWTYPE)){

				if(FSAPIConstants.PROXYNUMBER.equalsIgnoreCase(String.valueOf(cardTypeValueMap.get(ValueObjectKeys.REPLACERENEWTYPE)))){
					mapForProxySerialCnt= getJdbcTemplate().queryForMap(QueryConstants.QUERYPROXYCOUNT,String.valueOf(cardTypeValueMap.get(ValueObjectKeys.REPLACERENEWVALUE)));
					prxyserialflag="P";
				}else if (FSAPIConstants.SERIALNUMBER.equalsIgnoreCase(String.valueOf(cardTypeValueMap.get(ValueObjectKeys.REPLACERENEWTYPE)))){
					mapForProxySerialCnt=getJdbcTemplate().queryForMap(QueryConstants.QUERYSERIALCOUNT,String.valueOf(cardTypeValueMap.get(ValueObjectKeys.REPLACERENEWVALUE)));
					prxyserialflag="S";
				}else{
					logger.error("Invalid  Field type PROXYNUMBER/SERIALNUMBER");
					errorbean.setErrorMsg(FSAPIConstants.INVALID_TYPE);
					errorbean.setRespCode(B2BResponseCode.INVALID_FIELD);
					errorList.add(errorbean);

				} 


				log.debug("Proxy/Serial Number "+cardTypeValueMap.get(ValueObjectKeys.REPLACERENEWVALUE));

				int	pryselcount=Integer.parseInt(String.valueOf(mapForProxySerialCnt.get("COUNT(1)")));
				log.debug("proxy/serial count   :: "+pryselcount);
				if( pryselcount==0){
					if("P".equals(prxyserialflag)){
						logger.error(FSAPIConstants.INVALID_PRXOY_NUMBER);
						errorbean.setErrorMsg(FSAPIConstants.INVALID_PRXOY_NUMBER);
						errorbean.setRespCode(B2BResponseCode.INVALID_PROXY_NUMBER);
						errorList.add(errorbean);
					}else{
						logger.error(FSAPIConstants.INVALID_SERIAL_NUMBER);
						errorbean.setErrorMsg(FSAPIConstants.INVALID_SERIAL_NUMBER);
						errorbean.setRespCode(B2BResponseCode.INVALID_SERIAL_NUMBER);
						errorList.add(errorbean);
					}
				}else{

					if(FSAPIConstants.PROXYNUMBER.equalsIgnoreCase(String.valueOf(cardTypeValueMap.get(ValueObjectKeys.REPLACERENEWTYPE)))){
						String qryPartnerId=QueryConstants.QUERYFORPARTNERID;
						qryPartnerId =qryPartnerId+" and  PROXY_NUMBER=? ";
						mapForpartnerIdCNT=getJdbcTemplate().queryForMap(qryPartnerId,((String)valueMap.get(FSAPIConstants.ORDER_PARTNERID)).trim(),String.valueOf(cardTypeValueMap.get(ValueObjectKeys.REPLACERENEWVALUE)));
					}else if (FSAPIConstants.SERIALNUMBER.equalsIgnoreCase(String.valueOf(cardTypeValueMap.get(ValueObjectKeys.REPLACERENEWTYPE)))){
						String qryPartnerId=QueryConstants.QUERYFORPARTNERID;
						qryPartnerId =qryPartnerId+" and  SERIAL_NUMBER=? ";
						mapForpartnerIdCNT=getJdbcTemplate().queryForMap(qryPartnerId,((String)valueMap.get(FSAPIConstants.ORDER_PARTNERID)).trim(),String.valueOf(cardTypeValueMap.get(ValueObjectKeys.REPLACERENEWVALUE)));
					}

					int	partnerIdcount=Integer.parseInt(String.valueOf(mapForpartnerIdCNT.get("partnerIdCNT")));
					log.debug("partnerIdcount   :: "+partnerIdcount);
					if( partnerIdcount==0){
						logger.error(FSAPIConstants.ORDER_PARTNERID_ERRMSG);
						errorbean.setErrorMsg(B2BResponseMessage.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
						errorbean.setRespCode(B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
						errorList.add(errorbean);
					}


				}

			}
			}

		}catch(Exception exp){
			log.error("Exception while  getting product code and cattype..."+exp);	
			log.error("Excpetion while validating proxy/serial number "+exp);
			errorbean.setErrorMsg(B2BResponseMessage.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
			errorbean.setRespCode(B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
			errorList.add(errorbean);
		}

	}


/**
 * getCardReplacementOrderID dynamically
 * @return
 * @throws Exception
 */
	public String getCardReplacementOrderID(){ 

		return getJdbcTemplate().query(QueryConstants.CARDREPLACEORDERID,  new ResultSetExtractor<String>(){

			@Override
			public String extractData(ResultSet rs) throws SQLException {
				rs.next();
				return rs.getString(1);
			}
		});
	}



}
