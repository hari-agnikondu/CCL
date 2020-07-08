package com.incomm.scheduler.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import com.incomm.scheduler.dao.BatchLoadAccountPurseDAO;
import com.incomm.scheduler.model.BatchLoadAccountPurse;
import com.incomm.scheduler.model.BatchLoadAccountPurseLog;
import com.incomm.scheduler.utils.DateTimeUtil;

@Component
public class BatchLoadAccountPurseDAOImpl extends JdbcDaoSupport implements BatchLoadAccountPurseDAO {

	private static final String QUERY_GET_PENDING_REQUESTS = "select batch_id, partner_id, product_id, purse_name, transaction_amount, effective_date, expiry_date, "
			+ "sku_code, action_type, mdm_id, status, override_card_status, percentage_amount from batch_load_account_purse where status = 'NEW'";

	private static final String QUERY_UPDATE_BATCH_REQUEST = "update batch_load_account_purse set status= :status, last_upd_date = sysdate  where batch_id = :batchId";

	private static final String QUERY_INSERT_REQUEST_LOG = "insert into batch_load_account_purse_log "
			+ "(BATCH_ID, REQUEST_ID, correlation_id, ACCOUNT_NUMBER, INS_DATE, LAST_UPD_DATE) "
			+ "values (:batchId, seq_load_acct_batch_req_id.nextval, :correlationId, :accountNumber, sysdate, sysdate )";

	private static final String QUERY_UPDATE_REQUEST_LOG = "update batch_load_account_purse_log "
			+ "set INS_USER = ?, response_code=?, response_message=? , available_balance=?, auth_amount = ?, LAST_UPD_DATE=sysdate "
			+ "where batch_id = ? and request_id=? and correlation_id =?";

	private static final String INSERT_BATCH_LOAD_ACCOUNT_PURSE = "Insert into batch_load_account_purse (BATCH_ID,PARTNER_ID,PRODUCT_ID, "
			+ "PURSE_NAME,TRANSACTION_AMOUNT,EFFECTIVE_DATE,EXPIRY_DATE,SKU_CODE,ACTION_TYPE,MDM_ID,STATUS,OVERRIDE_CARD_STATUS,NEXT_RUN_DATE,PREEMPT_DAYS,PERCENTAGE_AMOUNT,"
			+ "INS_USER,INS_DATE,LAST_UPD_USER,LAST_UPD_DATE) values (:batchId,:partnerId,:productId,:purseName,:transactionAmount, "
			+ ":effectiveDate,:expiryDate,:skuCode,:actionType,:mdmId, 'NEW',:overrideCardStatus,:nextRunDate,:preemptDays,:percentageAmount,1,sysdate,1,sysdate) ";

	private static final String QUERY_GET_RECORD_COUNT_BY_BATCH_ID = "select count(1) from batch_load_account_purse_log where batch_id= ? ";

	private static final String QUERY_GET_NEXT_BATCH_ID = "select seq_load_acct_batch_id.nextval from dual";

	
	
	private RowMapper<BatchLoadAccountPurse> accountPurseUpdateRequestRowMapper = this.getAccountPurseUpdateRequestRowMapper();

	@Autowired
	public void setTransactionalDataSource(@Qualifier("transactionalDs") DataSource dataSource) {
		setDataSource(dataSource);
	}

	@Override
	public List<BatchLoadAccountPurse> getPendingAccountPurseUpdateRequests() {
		return getJdbcTemplate().query(QUERY_GET_PENDING_REQUESTS, accountPurseUpdateRequestRowMapper);
	}

	private RowMapper<BatchLoadAccountPurse> getAccountPurseUpdateRequestRowMapper() {
		return (rs, rowNum) -> BatchLoadAccountPurse.builder()
			.batchId(rs.getLong("batch_id"))
			.partnerId(rs.getLong("partner_id"))
			.productId(rs.getLong("product_id"))
			.purseName(rs.getString("purse_name"))
			.mdmId(rs.getString("mdm_id"))
			.transactionAmount(rs.getBigDecimal("transaction_amount"))
			.effectiveDate(DateTimeUtil.map(rs.getTimestamp("effective_date")))
			.expiryDate(DateTimeUtil.map(rs.getTimestamp("expiry_date")))
			.skuCode(rs.getString("sku_code"))
			.actionType(rs.getString("action_type"))
			.status(rs.getString("status"))
			.overrideCardStatus(rs.getString("OVERRIDE_CARD_STATUS"))
			.percentageAmount(rs.getBigDecimal("percentage_amount"))
			.build();
	}

	@Override
	public int updateAccountPurseUpdateRequest(long batchId, String status) {
		return getJdbcTemplate().update(QUERY_UPDATE_BATCH_REQUEST, status, Long.valueOf(batchId));
	}

	@Override
	public int[] addAccountPurseBatchLog(List<? extends BatchLoadAccountPurseLog> items) {

		return getJdbcTemplate().batchUpdate(QUERY_INSERT_REQUEST_LOG, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				BatchLoadAccountPurseLog item = items.get(i);
				ps.setLong(1, item.getBatchId());
				ps.setString(2, item.getCorrelationId());

				// Set null-able values using setobject, this does not work for primitive fields.
				ps.setObject(3, item.getAccountNumber(), java.sql.Types.VARCHAR);

			}

			@Override
			public int getBatchSize() {
				return items.size();
			}

		});
	}

	@Override
	public int[] updateAccountPurseBatchLog(List<? extends BatchLoadAccountPurseLog> items) {

		return getJdbcTemplate().batchUpdate(QUERY_UPDATE_REQUEST_LOG, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				BatchLoadAccountPurseLog item = items.get(i);

//				if (item.getAccountPurseId() == null) {
//					ps.setNull(1, java.sql.Types.NUMERIC);
//				} else {
//					ps.setLong(1, item.getAccountPurseId());
//				}
				
				ps.setNull(1, java.sql.Types.NUMERIC);

				ps.setString(2, item.getResponseCode());
				ps.setString(3, item.getResponseMessage());
				ps.setBigDecimal(4, item.getAvailableBalance());
				ps.setBigDecimal(5, item.getAuthAmount());
				ps.setLong(6, item.getBatchId());
				ps.setLong(7, item.getRequestId());
				ps.setString(8, item.getCorrelationId());
			}

			@Override
			public int getBatchSize() {
				return items.size();
			}

		});

	}

	@Override
	public int getRecordCountByBatchId(long batchId) {
		return getJdbcTemplate().queryForObject(QUERY_GET_RECORD_COUNT_BY_BATCH_ID, new Object[] { batchId }, Integer.class);
	}

	@Override
	public long getNextPurseLoadBatchId() {

		return getJdbcTemplate().queryForObject(QUERY_GET_NEXT_BATCH_ID, new SingleColumnRowMapper<Long>(Long.class));

	}

	@Override
	public int addBatchLoadAccountPurseRequest(BatchLoadAccountPurse request) {

		return getJdbcTemplate().update(INSERT_BATCH_LOAD_ACCOUNT_PURSE, //
				request.getBatchId(), //
				request.getPartnerId(), //
				request.getProductId(), //
				request.getPurseName(), //
				request.getTransactionAmount(), //
				map(request.getEffectiveDate()), //
				map(request.getExpiryDate()), //
				request.getSkuCode(), //
				request.getActionType(), //
				request.getMdmId(), //
				request.getOverrideCardStatus(),
				map(request.getNextRunDate()),
				request.getPreemptDays(),
				request.getPercentageAmount());
	}
	
	
	

	public static LocalDateTime map(java.sql.Timestamp timestamp) {
		return timestamp == null ? null : timestamp.toLocalDateTime();
	}

	public static java.sql.Timestamp map(LocalDateTime localDateTime) {
		return localDateTime == null ? null : java.sql.Timestamp.valueOf(localDateTime);
	}
	
	public static java.sql.Date map(LocalDate localDate) {
		return localDate == null ? null : java.sql.Date.valueOf(localDate);
	}

}
