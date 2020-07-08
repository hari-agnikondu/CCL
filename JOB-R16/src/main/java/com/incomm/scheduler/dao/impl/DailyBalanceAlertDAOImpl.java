package com.incomm.scheduler.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.incomm.scheduler.constants.QueryConstants;
import com.incomm.scheduler.dao.DailyBalanceAlertDAO;
import com.incomm.scheduler.model.DailyBalance;

@Repository
public class DailyBalanceAlertDAOImpl extends JdbcDaoSupport implements DailyBalanceAlertDAO {

	private static final Logger log = LogManager.getLogger(DailyBalanceAlertDAOImpl.class);
	
	@Autowired
	public void setDs(@Qualifier("orderDs") DataSource dataSource) {
		setDataSource(dataSource);
	}
	/*
	 * to fetch all table data which is inserted by cursor
	 * @see com.incomm.scheduler.dao.DailyBalanceAlertDAO#getDailyBalanceDetails()
	 */

	@Override
	public List<DailyBalance> getDailyBalanceDetails() {
		return getJdbcTemplate().query(QueryConstants.GET_DAILY_BALANCE_ALERT_QUERY,  new ResultSetExtractor<List<DailyBalance>>(){

			@Override
			public List<DailyBalance> extractData(ResultSet rs) throws SQLException{
				List<DailyBalance> dailyBalOptList = new ArrayList<>();

				while(rs.next()){
					//SELECT FN_DMAPS_MAIN(CARD_NUM_ENCR),MOBILE_NUMBER,EMAIL,CARD_NUM_HASH,PROCESS_STATUS,CURRENCY_CODE FROM SMS_EMAIL_DAILY_ALERT_MSG
					DailyBalance db = new DailyBalance();
					db.setCardNumber(rs.getString(1));//FN_DMAPS_MAIN(CARD_NUM_ENCR)
					db.setMoblieNo(rs.getString(2));//MOBILE_NUMBER
					db.setEmail(rs.getString(3));//EMAIL
					db.setCardHash(rs.getString(4));//CARD_NUM_HASH
					db.setAccountId(rs.getString(5));//ACCOUNT_ID
					db.setProcessStatus(rs.getString(6));//PROCESS_STATUS
					db.setCurrencyCode(rs.getString(7));//CURRENCY_CODE
					dailyBalOptList.add(db);
				}
				return dailyBalOptList;
			}

		});
	}
	/*
	 * update status of alert status on table
	 * @see com.incomm.scheduler.dao.DailyBalanceAlertDAO#updateDailyBal_alert_msg(java.util.Map)
	 */

	@Override
	public void updateDailyBalalertmsg(Map<String, String> valueObj) {
		getJdbcTemplate().update(QueryConstants.DAILY_BALANCE_ALERT_MSG, valueObj.get("process_msg"),valueObj.get("CardNumber"));
		log.debug("Process_status value is" +valueObj.get("process_msg"));
	}
	
	/*
	 * Calling procedure updae table
	 * @see com.incomm.scheduler.dao.DailyBalanceAlertDAO#callProcedure(java.lang.Long)
	 * @param issuer id feature it will use for multi insttituion
	 */

	@Override
	public String callProcedure(Long issuerId) {

		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(getJdbcTemplate())
				.withProcedureName("SP_SMSEMAIL_DAILY_ALTMSG");
		Map<String, Object> inParamMap = new HashMap<>();
		inParamMap.put("P_ISSUER", 1);
		SqlParameterSource in = new MapSqlParameterSource(inParamMap);
		Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);
		return simpleJdbcCallResult.get("P_ERR_MSG").toString();
	}

	/**/

}
