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

import com.incomm.scheduler.constants.QueryConstants;
import com.incomm.scheduler.dao.DigitalCardProcessingDAO;
import com.incomm.scheduler.utils.ScriptUtils;

@Repository
public class DigitalCardProcessingDAOImpl extends JdbcDaoSupport implements DigitalCardProcessingDAO{

	private static final Logger log = LogManager.getLogger(DigitalCardProcessingDAOImpl.class);
	
	@Autowired
	public void setDs(@Qualifier("orderDs") DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int getAvailableCardCountForProd(String productId) {
		return getJdbcTemplate().queryForObject(QueryConstants.GET_AVAILABLE_INVENTORY, Integer.class, productId);
		 
		}

	@Override
	public int getIssuedCardCountForProd(String productId) {
		return  getJdbcTemplate().queryForObject(QueryConstants.GET_ISSUED_INVENTORY, Integer.class, productId);
	
	}

	@Override
	public String processOrder(String orderId) {
		String response = "";
		try {
			List<SqlParameter> params = new ArrayList<>();
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlOutParameter("error", Types.VARCHAR));

			log.info("Calling the Procedure : {}", ScriptUtils.SP_DIGITAL_CARD_PROCESSING);

			Map<String, Object> resultMap = getJdbcTemplate().call(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					log.debug("Processing order: {}", orderId);

					CallableStatement cs = con.prepareCall(ScriptUtils.SP_DIGITAL_CARD_PROCESSING);
					cs.setString(1, orderId);
					cs.setString(2, "1");
					cs.registerOutParameter(3, Types.VARCHAR);
					return cs;
				}
			}, params);
			if (resultMap != null) {
				response = (String) resultMap.get("error");
				log.info("Result of the Procedure : {} ", resultMap.get("error"));
			} else {
				log.info("Error in procedure call");
			}

		} catch (Exception e) {
			log.error("Error in procedure call : {}", e.getMessage(), e);
		}
		return response;
	}

	@Override
	public List<Map<String, Object>> getAllDigitalProducts() {
		
		return getJdbcTemplate().queryForList(ScriptUtils.GET_ALL_DIGITAL_PRODUCTS);
		
	}

}
