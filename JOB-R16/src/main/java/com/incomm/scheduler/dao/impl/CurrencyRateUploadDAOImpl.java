package com.incomm.scheduler.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.dao.CurrencyRateUploadDAO;
import com.incomm.scheduler.model.CurrencyRateRequestFile;
import com.incomm.scheduler.utils.ScriptUtils;

@Repository
public class CurrencyRateUploadDAOImpl extends JdbcDaoSupport implements CurrencyRateUploadDAO {

	private static final Logger logger = LogManager.getLogger(CurrencyRateUploadDAOImpl.class);

	@Autowired
	public void setDs(@Qualifier("transactionalDs") DataSource dataSource) {
		setDataSource(dataSource);
	}

	@Override
	public int chkDuplicateFiles(String fileName) {
		int result = 0;
		String sql = ScriptUtils.CHECK_DUPLICATE_CURRENCY_RATE_FILE;

		result = getJdbcTemplate().queryForObject(sql, new Object[] { fileName }, Integer.class);

		return result;
	}

	@Override
	public int getRecordCountBatchId(Long batchId) {
		int result = 0;
		String sql = ScriptUtils.Get_RECORD_COUNT_CURRENCY_RATE_BATCH_ID;

		result = getJdbcTemplate().queryForObject(sql, new Object[] { batchId }, Integer.class);

		return result;
	}

	@Override
	public void InsertCurrencyRateUploadFiles(Map<String, ExecutionContext> fileList) {
		
		logger.info(CCLPConstants.ENTER);
		Connection conn = null;
		PreparedStatement currencyRateFileStatusListPstmt = null;
		try {

			DataSource con = getDataSource();
			conn = con.getConnection();
			currencyRateFileStatusListPstmt = conn.prepareStatement(ScriptUtils.INSERT_CURRENCY_RATE_FILE);
			for (Map.Entry<String, ExecutionContext> files : fileList.entrySet()) {
				logger.info("Execution context" + files);

				ExecutionContext ec = files.getValue();

				currencyRateFileStatusListPstmt.setString(1, ec.getString("file"));
				currencyRateFileStatusListPstmt.setString(2, ec.getString("BatchId"));
				currencyRateFileStatusListPstmt.addBatch();
			}

			currencyRateFileStatusListPstmt.executeBatch();
		} catch (Exception e) {
			logger.error("Exception while inserting file name: "+e.getMessage());
		} finally {
			if (currencyRateFileStatusListPstmt != null) {
				try {
					currencyRateFileStatusListPstmt.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
			}
		}
		logger.info(CCLPConstants.EXIT);
	}

	@Override
	public String getBatchId() {

		return getJdbcTemplate().queryForObject(ScriptUtils.GET_CURRENCY_RATE_BATCH_ID, new Object[] {}, String.class);
	}

	
	@Override
	public void insertRequest(List<? extends CurrencyRateRequestFile> currencyRateRequestFiles) {

		logger.info(CCLPConstants.ENTER);
		try {
			getJdbcTemplate().batchUpdate(ScriptUtils.INSERT_CURRENCY_CONVERSION_STAGE, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					CurrencyRateRequestFile currencyRateRequestFile = currencyRateRequestFiles.get(i);
					ps.setString(1, currencyRateRequestFile.getFileName());
					ps.setString(2, currencyRateRequestFile.getMdmId());
					ps.setString(3, currencyRateRequestFile.getIssuingCurrency());

					ps.setString(4, currencyRateRequestFile.getTransactionCurrency());
					ps.setString(5, String.valueOf(currencyRateRequestFile.getConversionRate()));
					ps.setString(6,  currencyRateRequestFile.getEffectiveDateTime());
					ps.setString(7, currencyRateRequestFile.getAction());
					ps.setString(8, currencyRateRequestFile.getBatchId());

				}

				public int getBatchSize() {
					return currencyRateRequestFiles.size();
				}
			});
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		logger.info(CCLPConstants.EXIT);

	}

	@Override
	public void updateFileStatus(String fileName) {
		String sql = ScriptUtils.GET_COMPLETED_CURRENCY_RATE_RECORD_COUNT;
		long count = 0;
		count = getJdbcTemplate().queryForObject(sql, new Object[] { fileName }, Long.class);
		String sql1 = ScriptUtils.CURRENCY_RATE_FILE_STATUS_UPDATE;
		if (count > 0) {
			getJdbcTemplate().update(sql1, "PARTIAL-COMPLETED", "All records are not processed  ", fileName);

		} else {
			getJdbcTemplate().update(sql1, "COMPLETED", "OK", fileName);
		}
	}

	@Override
	public void updateResponse(String status, String responesMsg,String batchId,String recordNumber) {
		String sql = ScriptUtils.UPDATE_CURRENCY_CONVERSION_STAGE;
		getJdbcTemplate().update(sql, status,responesMsg,batchId,recordNumber );
				
	}

	@Override
	public List<Map<String, Object>> getRecordsToProcess(String batchId) {
		String sql = ScriptUtils.GET_UNPROCESSED_CURRENCY_RATE_RECORDS;
		return getJdbcTemplate().queryForList(sql, batchId);
	}

	@Override
	public void updateDuplicateFileStatus(String fileName,String errorMsg, String status) {
		
		String batchId = getBatchId();
		String sql = ScriptUtils.INSERT_CURRENCY_RATE_FILE_ERROR;
		getJdbcTemplate().update(sql, errorMsg,fileName,status, batchId );
		
	}

	@Override
	public int checkCurrencyCode(String txnCurrency) {
		int result = 0;
		String sql = ScriptUtils.VERIFY_CURRENCY;

		result = getJdbcTemplate().queryForObject(sql, new Object[] { txnCurrency }, Integer.class);

		return result;
	}

	@Override
	public void updateResponseByAction(String batchId, String action, String errorMsg, String status) {
		
		String sql = ScriptUtils.UPDATE_CURRENCY_CONVERSION_STAGE_RESP_BY_ACTION;
		getJdbcTemplate().update(sql, status,errorMsg,batchId, action );
		
	}


}
