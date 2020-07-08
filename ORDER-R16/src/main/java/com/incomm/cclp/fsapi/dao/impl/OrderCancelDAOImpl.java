package com.incomm.cclp.fsapi.dao.impl;

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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.fsapi.constants.FSAPIConstants;
import com.incomm.cclp.fsapi.dao.OrderCancelDAO;
import com.incomm.cclp.fsapi.service.CommonService;
import com.incomm.cclp.fsapi.service.TransactionService;

@Repository
public class OrderCancelDAOImpl extends JdbcDaoSupport implements OrderCancelDAO {

	private final Logger logger = LogManager.getLogger(this.getClass());

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

	
	/*@Override
	public Map<String, Object> cancelOrder(Map<String, Object> valueHashMap) {

		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(getJdbcTemplate())
				.withProcedureName(QueryConstants.ORDER_CANCEL_PROC);
		Map<String, Object> inParamMap = new HashMap<>();

		inParamMap.put("p_order_id_in", valueHashMap.get(FSAPIConstants.ORDER_ORDER_ID));
		inParamMap.put("p_partner_id_in", valueHashMap.get(FSAPIConstants.ORDER_PARTNERID));

		SqlParameterSource in = new MapSqlParameterSource(inParamMap);
		return simpleJdbcCall.execute(in);
	}*/
	
	@Override
	public Map<String, Object> cancelOrder(Map<String, Object> valueHashMap) {

		logger.debug("inside cancel order");
		final String orderId = String.valueOf(valueHashMap.get(FSAPIConstants.ORDER_ORDER_ID));
		final String partnerId = String.valueOf(valueHashMap.get(FSAPIConstants.ORDER_PARTNERID));

		List<SqlParameter> params = new ArrayList<>();
		params.add(new SqlParameter(Types.VARCHAR));
		params.add(new SqlParameter(Types.VARCHAR));
		params.add(new SqlOutParameter("P_RESP_CODE_OUT", Types.VARCHAR));
		params.add(new SqlOutParameter("P_RESP_MSG_OUT", Types.VARCHAR));

		Map<String, Object> resultMap = getJdbcTemplate().call(new CallableStatementCreator() {
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement cs = con.prepareCall(QueryConstants.ORDER_CANCEL_PROC);
				cs.setString(1, orderId);
				cs.setString(2, partnerId);

				cs.registerOutParameter(3, Types.VARCHAR);
				cs.registerOutParameter(4, Types.VARCHAR);

				return cs;
			}
		}, params);

		logger.debug("order process response: " + resultMap.get("P_RESP_MSG_OUT").toString());
		logger.debug("exit processing the order");
		return resultMap;

	}

	
	/*@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> cancelOrderProcess(Map<String, Object> valueHashMap) {

		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(getJdbcTemplate())
				.withProcedureName(QueryConstants.ORDER_CANCEL_PROCESS_PROC);
		Map<String, Object> inParamMap = new HashMap<>();

		inParamMap.put("p_order_id_in", valueHashMap.get(FSAPIConstants.ORDER_ORDER_ID));
		inParamMap.put("p_partner_id_in", valueHashMap.get(FSAPIConstants.ORDER_PARTNERID));
		Map<String, String> headerMap = (Map<String, String>) valueHashMap.get("headers");
		inParamMap.put("p_correlation_id_in", headerMap.get("x-incfs-correlationid"));

		SqlParameterSource in = new MapSqlParameterSource(inParamMap);
		return simpleJdbcCall.execute(in);
	}*/
	 

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> cancelOrderProcess(Map<String, Object> valueHashMap) {

		logger.debug("inside cancel order process");
		final String orderId = String.valueOf(valueHashMap.get(FSAPIConstants.ORDER_ORDER_ID));
		final String partnerId = String.valueOf(valueHashMap.get(FSAPIConstants.ORDER_PARTNERID));
	
		Map<String, String> headerMap = (Map<String, String>) valueHashMap.get("headers");

		List<SqlParameter> params = new ArrayList<>();
		params.add(new SqlParameter(Types.VARCHAR));
		params.add(new SqlParameter(Types.VARCHAR));
		params.add(new SqlParameter(Types.VARCHAR));
		params.add(new SqlOutParameter("P_RESP_CODE_OUT", Types.VARCHAR));
		params.add(new SqlOutParameter("P_RESP_MSG_OUT", Types.VARCHAR));
		params.add(new SqlOutParameter("P_POSTBACK_URL_OUT", Types.VARCHAR));

		Map<String, Object> resultMap = getJdbcTemplate().call(new CallableStatementCreator() {
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement cs = con.prepareCall(QueryConstants.ORDER_CANCEL_PROCESS_PROC);
				cs.setString(1, orderId);
				cs.setString(2, partnerId);
				cs.setString(3, headerMap.get("x-incfs-correlationid"));

				cs.registerOutParameter(4, Types.VARCHAR);
				cs.registerOutParameter(5, Types.VARCHAR);
				cs.registerOutParameter(6, Types.VARCHAR);

				return cs;
			}
		}, params);

		logger.debug("cancel order process response: " + resultMap.get("P_RESP_MSG_OUT").toString());
		logger.debug("exit cancel order process");
		return resultMap;
	}

	@Override
	public void getOrderStatus(Map<String, Object> valueHashMap) {

	}

}
