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

import com.incomm.scheduler.dao.YearlyFeeDAO;
import com.incomm.scheduler.utils.ScriptUtils;

@Repository
public class YearlyFeeDAOImpl extends JdbcDaoSupport implements YearlyFeeDAO {

	private static final Logger log = LogManager.getLogger(YearlyFeeDAOImpl.class);
	
	@Autowired
	public void setDs(@Qualifier("transactionalDs") DataSource dataSource) {
		setDataSource(dataSource);
	}
	@Override
	public String callYearlyFeeProcedure() {
		try {
			List<SqlParameter> params = new ArrayList<>();
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlOutParameter("error", Types.VARCHAR));

			log.info("Calling the Procedure : {}", ScriptUtils.SP_CALC_ANNUAL_FEES);
			Map<String, Object> resultMap = getJdbcTemplate().call(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(ScriptUtils.SP_MAINTANENANCE_FEE_SCHEDULER);
					cs.setString(1, "203");
					cs.registerOutParameter(2, Types.VARCHAR);
					return cs;
				}
			}, params);
			if (resultMap != null)
				log.info("Result of the Procedure : {} ", resultMap.get("error"));

		} catch (Exception e) {
			log.info("Error in procedure call : {}", e.getMessage());
		}
		return "FINISHED";
	}

}
