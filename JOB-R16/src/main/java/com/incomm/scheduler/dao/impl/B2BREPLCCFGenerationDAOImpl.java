package com.incomm.scheduler.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.dao.B2BREPLCCFGenerationDAO;
import com.incomm.scheduler.utils.JobConstants;
import com.incomm.scheduler.utils.ScriptUtils;

@Repository
public class B2BREPLCCFGenerationDAOImpl extends JdbcDaoSupport implements B2BREPLCCFGenerationDAO {

	private static final Logger logger = LogManager.getLogger(B2BREPLCCFGenerationDAOImpl.class);
	
	@Autowired
    public void setDs(@Qualifier("orderDs") DataSource dataSource) {
         setDataSource(dataSource);
    }

	@Override
	public List<Map<String, Object>> getAllOrdersToGenerateCCF(String orderId, String vendorId) {

		return getJdbcTemplate().queryForList(ScriptUtils.GET_ALL_ORDERS_TO_GENERATE_CCF,
				 vendorId, orderId);
	}

	@Override
	public List getCcfVersionDetails(String version,String recType) {
		
		return  getJdbcTemplate().queryForList(ScriptUtils.GET_CCF_VERSION_DETAILS,version,recType);
	}

	@Override
	public String getCCFFormatVersion(String productId) {
		
		return getJdbcTemplate().queryForObject(ScriptUtils.GET_CCF_FORMAT_VERSION,new Object[] {productId}, String.class);
	}

	@Override
	public String getServiceCode(String productId) {
		
		return getJdbcTemplate().queryForObject(ScriptUtils.GET_SERVICE_CODE,new Object[] {productId}, String.class);
	}



	@Override
	public void updateCardStatus(String hashPancode, String psFileName, String status) {
		
		getJdbcTemplate().update(ScriptUtils.UPDATE_CARD_STATUS,psFileName,status,hashPancode);
	}

	@Override
	public void updateCardStatus(String orderId, String orderStatus) {
		getJdbcTemplate().update(ScriptUtils.UPDATE_ORDER_STATUS_ORDER_DETAILS,orderStatus,orderId);
		getJdbcTemplate().update(ScriptUtils.UPDATE_ORDER_STATUS_ORDER_LINE_ITEM,orderStatus,orderId);
	}
	
	@Override
    public List getAddressDetails(String hashPancode) {
        
        return getJdbcTemplate().queryForList(ScriptUtils.GET_ADDRESS_DETAILS,hashPancode);
        
    }



	@Override
	public String getCVK(String msProdCode) {
		return getJdbcTemplate().queryForObject(ScriptUtils.GET_CVK,new Object[] {msProdCode}, String.class);
	}



	@Override
	public String getCvvSupportedFlag(String productId) {
		return getJdbcTemplate().queryForObject(ScriptUtils.GET_CVV_SUPPORTED_FLAG,new Object[] {productId}, String.class);
	}

	@Override
	public List<String> getAllOrdersToGenerateCCF(String orderType) {
		List orderlist = null;
		orderlist=getJdbcTemplate().queryForList(ScriptUtils.GET_ALL_COMPLETED_ORDERS,orderType);
		return orderlist;
	
	}
	
	@Override
	public List<String> getAllB2BReplOrdersToGenerateCCF(String orderType) {
		List orderlist = null;
		orderlist=getJdbcTemplate().queryForList(ScriptUtils.GET_ALL_COMPLETED_ORDERS,orderType);
		return orderlist;
	
	}


	@Override
	public List<String> getAllVendorsLinkedToOrder(String orderId) {
		List vendorList = null;
		vendorList=getJdbcTemplate().queryForList(ScriptUtils.GET_ALL_VENDOR_LINKED_TO_ORDER,orderId);
		return vendorList;
	}
	
	@Override
	public List<String> getAllVendorsLinkedToOrder(String orderId,String orderLineItem){
		List vendorList = null;
		vendorList=getJdbcTemplate().queryForList(ScriptUtils.GET_REPL_ALL_VENDOR_LINKED_TO_ORDER_LINE_ITEM,orderId,orderLineItem);
		return vendorList;
	}

	@Override
	public List<String> getAllReplacementVendorsLinkedToOrder(String orderId,String orderLineItem){
		List vendorList = null;
		
		vendorList=	 getJdbcTemplate().queryForList(ScriptUtils.GET_ALL_VENDOR_LINKED_TO_ORDER_LINE_ITEM,orderId,orderLineItem);
		
		return vendorList;
		
	}


	@Override
	public String getVendorNameForId(String vendorId) {
		return getJdbcTemplate().queryForObject(ScriptUtils.GET_VENDOR_NAME,new Object[] {vendorId}, String.class);
	}
	


	@Override
	public Map<String, Object> getCCFFileName(String vendorId, String orderId) {
		
		logger.info(CCLPConstants.ENTER);
		Map<String, Object> resultMap = null;
		Map<String, Object> productIdAndOrderType = null;
		productIdAndOrderType = getJdbcTemplate().queryForMap(ScriptUtils.GET_PRODUCT_ID_AND_ORDER_TYPE,orderId,vendorId);
		String productIdStr = (String)productIdAndOrderType.get("productId");
		int productId = Integer.parseInt(productIdStr);
		List<SqlParameter> params = new ArrayList<>();
		int lupduser = 1;
		try {
		params.add(new SqlParameter( Types.VARCHAR));
		params.add(new SqlParameter(Types.VARCHAR));
		params.add(new SqlParameter(Types.VARCHAR));
		params.add(new SqlParameter(Types.NUMERIC));
		params.add(new SqlParameter(Types.NUMERIC));
		params.add(new SqlOutParameter("embfname", Types.VARCHAR));
		params.add(new SqlOutParameter("error", Types.VARCHAR));
			 resultMap = getJdbcTemplate().call(new CallableStatementCreator() {
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement cs = con.prepareCall(ScriptUtils.EMB_NAME);
				cs.setString(1, vendorId);
				cs.setString(2, "03");
				cs.setString(3, vendorId);
				cs.setInt(4, lupduser);
				cs.setInt(5, productId);
				cs.registerOutParameter(6, Types.VARCHAR);
				cs.registerOutParameter(7, Types.VARCHAR);
				
				return cs;
			}
		}, params);
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		logger.info(CCLPConstants.EXIT);
		return  resultMap;
				
	}
	
	
	@Override
	public Map<String, Object> getCCFFileName(String vendorId, String orderId,String orderLineItem) {
		
		logger.info(CCLPConstants.ENTER);
		Map<String, Object> resultMap = null;
		Map<String, Object> productIdAndOrderType = null;
		productIdAndOrderType = getJdbcTemplate().queryForMap(ScriptUtils.GET_PRODUCT_ID_AND_ORDER_TYPE_BY_ORDER_LINE_ITEM,orderId,vendorId,orderLineItem);
		String productIdStr = (String)productIdAndOrderType.get("productId");
		int productId = Integer.parseInt(productIdStr);
		List<SqlParameter> params = new ArrayList<>();
		int lupduser = 1;
		try {
		params.add(new SqlParameter( Types.VARCHAR));
		params.add(new SqlParameter(Types.VARCHAR));
		params.add(new SqlParameter(Types.VARCHAR));
		params.add(new SqlParameter(Types.NUMERIC));
		params.add(new SqlParameter(Types.NUMERIC));
		params.add(new SqlOutParameter("embfname", Types.VARCHAR));
		params.add(new SqlOutParameter("error", Types.VARCHAR));
			 resultMap = getJdbcTemplate().call(new CallableStatementCreator() {
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement cs = con.prepareCall(ScriptUtils.EMB_NAME);
				cs.setString(1, vendorId);
				cs.setString(2, "03");
				cs.setString(3, vendorId);
				cs.setInt(4, lupduser);
				cs.setInt(5, productId);
				cs.registerOutParameter(6, Types.VARCHAR);
				cs.registerOutParameter(7, Types.VARCHAR);
				
				return cs;
			}
		}, params);
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		logger.info(CCLPConstants.EXIT);
		return  resultMap;
				
	}





	@Override
	public Map<String, Object> getHeaderFileNumber(int insUser) {
		
		logger.info(CCLPConstants.ENTER);
		Map<String, Object> resultMap = null;
		
		List<SqlParameter> params = new ArrayList<>();
		try {

		params.add(new SqlParameter(Types.NUMERIC));
		params.add(new SqlOutParameter("headfileno", Types.VARCHAR));
		params.add(new SqlOutParameter("cardrecordcnt", Types.VARCHAR));
		params.add(new SqlOutParameter("errmsg", Types.VARCHAR));
			 resultMap = getJdbcTemplate().call(new CallableStatementCreator() {
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement cs = con.prepareCall(ScriptUtils.GET_HEADERFILE_NUMBER);
	
				cs.setInt(1, insUser);
				cs.registerOutParameter(2, Types.VARCHAR);
				cs.registerOutParameter(3, Types.VARCHAR);
				cs.registerOutParameter(4, Types.VARCHAR);
				
				return cs;
			}
		}, params);
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		logger.info(CCLPConstants.EXIT);
		return  resultMap;
	}

	@Override
	public List<Map<String, Object>> cnFileList() {
		return getJdbcTemplate().queryForList(ScriptUtils.GET_CN_FILE_STATUS_LIST);
	}

	@Override
	public void updateOrderStatus(String orderId, String orderStatus,String orderLineItem) {
		String ccfFlag="1";
		if(JobConstants.CCF_ORDER_STATUS_CCF_GENERATED.equals(orderStatus)){
			ccfFlag="2";
		}
		getJdbcTemplate().update(ScriptUtils.UPDATE_ORDER_STATUS_ORDER_DETAILS,orderStatus,orderId);
		getJdbcTemplate().update(ScriptUtils.UPDATE_ORDER_STATUS_ORDER_LINE_ITEM_ORDER_ID,orderStatus,ccfFlag,orderId,orderLineItem);
		
	}
	
	@Override
	public List<Map<String, Object>> getAllOrdersToGenerateCCF(String orderId, String vendorId,String orderLineItem) {

		return getJdbcTemplate().queryForList(ScriptUtils.GET_REPL_ALL_ORDERS_TO_GENERATE_CCF_ORDER_LINE_ITEM,
				vendorId, orderId,orderLineItem );

	}

	@Override
	public String getReplacementCCFFormatVersion(String productId) {
		return getJdbcTemplate().queryForObject(ScriptUtils.GET_REPL_CCF_FORMAT_VERSION,new Object[] {productId}, String.class);
	}

	@Override
	public void updateReplacementCard(String hashPancode, String status) {

			
			getJdbcTemplate().update(ScriptUtils.UPDATE_REPL_CARD,status,hashPancode);
	
	}
}
