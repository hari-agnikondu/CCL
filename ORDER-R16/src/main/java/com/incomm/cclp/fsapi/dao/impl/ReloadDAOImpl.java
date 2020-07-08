package com.incomm.cclp.fsapi.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
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

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.fsapi.bean.ErrorMsgBean;
import com.incomm.cclp.fsapi.constants.B2BResponseCode;
import com.incomm.cclp.fsapi.constants.B2BResponseMessage;
import com.incomm.cclp.fsapi.constants.FSAPIConstants;
import com.incomm.cclp.fsapi.dao.ReloadDAO;
@Repository
public class ReloadDAOImpl extends JdbcDaoSupport implements ReloadDAO {
	
	private final Logger logUtil = LogManager.getLogger(this.getClass());
	
	@Autowired
	public void setDs(@Qualifier("orderDs") DataSource dataSource) {
		setDataSource(dataSource);
	}
	@Override
	public void getOrderID(Map<String, Object> valuHashMap){
		try{
			getJdbcTemplate().query(QueryConstants.B2B_ORDER_ID_SEQUENCE_QRY,  new ResultSetExtractor<Map<String,Object>>(){
				@Override
				public Map<String,Object> extractData(ResultSet rs) throws SQLException {
					if(rs.next()){
						valuHashMap.put(ValueObjectKeys.RELOAD_ID,rs.getString("orderid"));
					}
					return null;
				}

			});
			
	
		}catch(Exception exp){
			logUtil.error("Exception while getting order sequence id "+exp);
		}
	}

	public void insertReloadDtls(Map<String,Object> tempValueMap) throws ServiceException{
		try{
			int updateCount=getJdbcTemplate().update(QueryConstants.B2B_RELOAD_ORDER_DETAILS_INSERT, 
					(String)tempValueMap.get(ValueObjectKeys.RELOAD_ID),(String)tempValueMap.get(ValueObjectKeys.MERCHANT_ID)
					,tempValueMap.get(ValueObjectKeys.POSTBACKRESPONSE)+"",(String)tempValueMap.get(ValueObjectKeys.POSTBACKURL),Integer.valueOf("1"),(String)tempValueMap.get("x-incfs-channel"));
			logUtil.info("insertReloadDtls:"+updateCount);

		}catch(Exception exp){
			logUtil.error("Exception while inserting into vms_reload_order dtls table::"+exp);
		}
	}
	@Override
	public void insertReloadmast(Map<String,Object> tempValueMap) throws ServiceException{
		try{
			int updateCount=getJdbcTemplate().update(QueryConstants.B2B_RELOAD_ORDER_INSERT,  
					(String)tempValueMap.get(ValueObjectKeys.RELOAD_ID),(String)tempValueMap.get(ValueObjectKeys.TYPE)
					,(String)tempValueMap.get(ValueObjectKeys.VALUE),(String)tempValueMap.get(ValueObjectKeys.DENOM),
					(String)tempValueMap.get(ValueObjectKeys.COMMENTS),"1");
			logUtil.info("insertReloadmast:"+updateCount);
			
		}catch(Exception exp)
		{
			logUtil.error("Exception while inserting into vms_reload_order table."+exp);
		}
	}
	
	public boolean proxySerialVal(String prxyserialflag,String proxySerialNum,List<ErrorMsgBean> errorList){
		boolean partnerloopexeflag=false;
		try{
		    logUtil.info("Proxy/Serial Number "+proxySerialNum);
		    String query="";
		    if("P".equals(prxyserialflag)){
		    	query=QueryConstants.PROXY_NUMBER_CHECK;
		    }
		    else{
		    	query=QueryConstants.SERIAL_NUMBER_CHECK;
		    }
		    Integer partnerIdcount = getJdbcTemplate().queryForObject(query,
		    		new Object[] {proxySerialNum}, Integer.class);
		    if( partnerIdcount==0){
		    	ErrorMsgBean errorbean=new ErrorMsgBean();
		    	partnerloopexeflag=true;
		    	if("P".equals(prxyserialflag)){
		    		logger.error(B2BResponseMessage.INVALID_PROXY_NUMBER);
		    		errorbean.setErrorMsg(B2BResponseMessage.INVALID_PROXY_NUMBER);
		    		errorbean.setRespCode(B2BResponseCode.INVALID_PROXY_NUMBER);
		    		errorList.add(errorbean);
		    	}else{
		    		logger.error(B2BResponseMessage.INVALID_SERIAL_NUMBER);
		    		errorbean.setErrorMsg(B2BResponseMessage.INVALID_SERIAL_NUMBER);
		    		errorbean.setRespCode(B2BResponseCode.INVALID_SERIAL_NUMBER);
		    		errorList.add(errorbean);
		    	}
		    }
		}
		catch (EmptyResultDataAccessException e) {
			logUtil.error("EmptyResultDataAccessException in proxySerialVal:"+e);
			throw e;
		} 
		catch (Exception e) {
			logUtil.error("Exception in proxySerialVal:"+e);
			throw e;
		} 
		return partnerloopexeflag;
	}
	@Override
	public void validateProxySerial(Map<String,Object> valueMap,List<ErrorMsgBean> errorList)throws ServiceException{
		try{
			getJdbcTemplate().query(QueryConstants.B2B_GET_RELOAD_ORDER_BY_RELOADID,  new ResultSetExtractor<Map<String,Object>>(){
				boolean partnerloopexeflag=false;
				@Override
				public Map<String,Object> extractData(ResultSet rs) throws SQLException {
					while(rs.next()){
						if(!partnerloopexeflag){
							if(FSAPIConstants.PROXYNUMBER.equalsIgnoreCase(rs.getString(1))){
								partnerloopexeflag=proxySerialVal("P",rs.getString(2),errorList);
							}else if (FSAPIConstants.SERIALNUMBER.equalsIgnoreCase(rs.getString(1))){
								partnerloopexeflag=proxySerialVal("S",rs.getString(2),errorList);
							}else{
								logger.error(FSAPIConstants.INVALID_TYPE);
								ErrorMsgBean errorbean=new ErrorMsgBean();
								errorbean.setErrorMsg(FSAPIConstants.INVALID_TYPE);
								errorbean.setRespCode(B2BResponseCode.INVALID_FIELD);
								errorList.add(errorbean);
								break;
							} 

						}

					}
					return null;
				}

			}, (String) valueMap.get(ValueObjectKeys.RELOAD_ID));
			
		  }catch(Exception exp){
			  logUtil.error("Excpetion while validating proxy/serial number; "+exp);
			  logger.error("Excpetion while validating proxy/serial number; "+exp);
			  	ErrorMsgBean errorbean=new ErrorMsgBean();
			    errorbean.setErrorMsg(B2BResponseMessage.SYSTEM_ERROR);
				errorbean.setRespCode(B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
				errorList.add(errorbean);
		}
	}
	
	public boolean partnerIDVal(String queryToExec,String partnerId,String proxyOrSerialNum,List<ErrorMsgBean> errorList){
		boolean partnerloopexeflag=false;
		try{
		    Integer partnerIdcount = getJdbcTemplate().queryForObject(queryToExec,
		    		new Object[] {partnerId,proxyOrSerialNum}, Integer.class);
			logUtil.info("partnerIdcount   :: "+partnerIdcount);
			if( partnerIdcount==0){
				logger.error(B2BResponseMessage.INVALID_PARTNER_ID);
				ErrorMsgBean errorbean=new ErrorMsgBean();
				errorbean.setErrorMsg(B2BResponseMessage.INVALID_PARTNER_ID);
				errorbean.setRespCode(B2BResponseCode.INVALID_PARTNER_ID);
				errorList.add(errorbean);
				partnerloopexeflag=true;
			 }
		}
		catch (EmptyResultDataAccessException e) {
			logUtil.error("EmptyResultDataAccessException in partnerIDVal:"+e);
			throw e;
		} 
		catch (Exception e) {
			logUtil.error("Exception in partnerIDVal:"+e);
			throw e;
		} 
		return partnerloopexeflag;
	}
	
	
	@Override
	public void partnerIdValidation(Map<String,Object> valueMap,List<ErrorMsgBean> errorList) throws ServiceException{
		try{
			getJdbcTemplate().query(QueryConstants.B2B_GET_RELOAD_ORDER_BY_RELOADID,  new ResultSetExtractor<Map<String,Object>>(){
				boolean partnerloopexeflag=false;
				@Override
				public Map<String,Object> extractData(ResultSet rs) throws SQLException {
					while(rs.next()){
						if(!partnerloopexeflag){
							if(FSAPIConstants.PROXYNUMBER.equalsIgnoreCase(rs.getString(1))){
								partnerloopexeflag=partnerIDVal(QueryConstants.RELOAD_PARTNERID_QRY
										+" and proxy_number=? ",(String)valueMap.get(FSAPIConstants.ORDER_PARTNERID),rs.getString(2),errorList);
							}else if (FSAPIConstants.SERIALNUMBER.equalsIgnoreCase(rs.getString(1))){
								partnerloopexeflag=partnerIDVal(QueryConstants.RELOAD_PARTNERID_QRY
										+" and serial_number=? ",(String)valueMap.get(FSAPIConstants.ORDER_PARTNERID),rs.getString(2),errorList);
							}

						}else{
							break;
						}

					}
					return null;
				}

			}, (String) valueMap.get(ValueObjectKeys.RELOAD_ID));
			
		}
		catch(Exception exp){
			logUtil.error("Exception Occured while loading checkPartnerId()..." + exp);
			ErrorMsgBean errorbean=new ErrorMsgBean();
			errorbean.setErrorMsg(B2BResponseMessage.SYSTEM_ERROR);
			errorbean.setRespCode(B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
			errorList.add(errorbean);
		}
	}

}
