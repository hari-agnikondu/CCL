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
import com.incomm.scheduler.dao.OrderProcessingDAO;
import com.incomm.scheduler.utils.JobConstants;
import com.incomm.scheduler.utils.ScriptUtils;

@Repository
public class OrderProcessingDAOImpl extends JdbcDaoSupport implements OrderProcessingDAO {

	private static final Logger logger = LogManager.getLogger(OrderProcessingDAOImpl.class);
	
	@Autowired
    public void setDs(@Qualifier("orderDs") DataSource dataSource) {
         setDataSource(dataSource);
    }

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getOrderIdList() {
		List orderlist = null;
		orderlist=getJdbcTemplate().queryForList(ScriptUtils.GET_APPROVED_ORDER_LIST);
		return orderlist;
	}


	@Override
	public String callOrderProcedure(String orderId, String orderLineItemId) {
		
		logger.info(CCLPConstants.ENTER);
		List<SqlParameter> params = new ArrayList<>();
		params.add(new SqlParameter( Types.VARCHAR));
		params.add(new SqlParameter(Types.VARCHAR));
		params.add(new SqlOutParameter(JobConstants.CCF_ERROR, Types.VARCHAR)); //commented because there is no out paramenter
			Map<String, Object> resultMap = getJdbcTemplate().call(new CallableStatementCreator() {
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement cs = con.prepareCall(ScriptUtils.ORDER_PROC_CALL);
				cs.setString(1, orderId);
				cs.setString(2, orderLineItemId);
				cs.registerOutParameter(3, Types.VARCHAR); 
				
				return cs;
			}
		}, params);
	
		logger.info("Result of the procedure :"+resultMap.get(JobConstants.CCF_ERROR));
		logger.info(CCLPConstants.EXIT);
		return (String) resultMap.get(JobConstants.CCF_ERROR);
	}


	@Override
	public String generateManualOrder(String orderId, String lineItemId,Long partnerId) {

		logger.info(CCLPConstants.ENTER);
			List<SqlParameter> params = new ArrayList<>();
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlOutParameter(JobConstants.CCF_ERROR, Types.VARCHAR));
			
				Map<String, Object> resultMap = getJdbcTemplate().call(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(ScriptUtils.ORDER_PROC_CALL);
					cs.setString(1, orderId);
					cs.setString(2, lineItemId);

					cs.registerOutParameter(3, Types.VARCHAR);
					
					return cs;
				}
			}, params);
	
			logger.info("Result of the procedure :"+resultMap.get(JobConstants.CCF_ERROR).toString());
			logger.info(CCLPConstants.EXIT);
			return resultMap.get(JobConstants.CCF_ERROR).toString();
	}


	@Override
	public void updateOrderStatus(String orderId, String lineItemId, Long partnerId) {

		getJdbcTemplate().update(ScriptUtils.UPDATE_ORDER_STATUS_IN_ORDER_DETAILS,orderId,partnerId);
		getJdbcTemplate().update(ScriptUtils.UPDATE_ORDER_STATUS_IN_ORDER_LINE_ITEM,orderId,partnerId,lineItemId);
	
	
	}

}
