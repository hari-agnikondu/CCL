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
import com.incomm.scheduler.dao.BulkTransactionDAO;
import com.incomm.scheduler.model.BulkTransactionRequestFile;
import com.incomm.scheduler.model.BulkTransactionResponseFile;
import com.incomm.scheduler.utils.ScriptUtils;

@Repository
public class BulkTransactionDAOImpl extends JdbcDaoSupport implements BulkTransactionDAO {

	private static final Logger logger = LogManager.getLogger(BulkTransactionDAOImpl.class);

	@Autowired
	public void setDs(@Qualifier("transactionalDs") DataSource dataSource) {
		setDataSource(dataSource);
	}

	@Override
	public int chkDuplicateFiles(String fileName) {
		int result = 0;
		String sql = ScriptUtils.CHECK_DUPLICATE_BLK_TXN_FILE;

		result = getJdbcTemplate().queryForObject(sql, new Object[] { fileName }, Integer.class);

		return result;
	}

	@Override
	public int getRecordCountBatchId(Long BatchId) {
		int result = 0;
		String sql = ScriptUtils.Get_RECORD_COUNT_BATCH_ID;

		result = getJdbcTemplate().queryForObject(sql, new Object[] { BatchId }, Integer.class);

		return result;
	}

	@Override
	public void InsertBlkTxnFiles(Map<String, ExecutionContext> fileList) {
		
		logger.info(CCLPConstants.ENTER);
		Connection conn = null;
		PreparedStatement blkFileStatusListPstmt = null;
		try {

			DataSource con = getDataSource();

			conn = con.getConnection();
			blkFileStatusListPstmt = conn.prepareStatement(ScriptUtils.INSERT_BLK_TXN_FILE);
			for (Map.Entry<String, ExecutionContext> files : fileList.entrySet()) {
				logger.info("Execution context" + files);

				ExecutionContext ec = files.getValue();

				blkFileStatusListPstmt.setString(1, ec.getString("file"));
				blkFileStatusListPstmt.setString(2, ec.getString("BatchId"));
				blkFileStatusListPstmt.addBatch();
			}

			blkFileStatusListPstmt.executeBatch();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (blkFileStatusListPstmt != null) {
				try {
					blkFileStatusListPstmt.close();
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

		return getJdbcTemplate().queryForObject(ScriptUtils.GET_BATCH_ID, new Object[] {}, String.class);
	}

	@Override
	public void updateResponse(List<? extends BulkTransactionResponseFile> bulkTransactionResponseFiles) {

		logger.info(CCLPConstants.ENTER);
		try {
			getJdbcTemplate().batchUpdate(ScriptUtils.UPDATE_BLK_TXN_FILE, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					BulkTransactionResponseFile bulkTransactionResponseFile = bulkTransactionResponseFiles.get(i);
					ps.setString(1, bulkTransactionResponseFile.getResponseCode());
					ps.setString(2, bulkTransactionResponseFile.getResponseMessage());
					ps.setString(3, bulkTransactionResponseFile.getTransactionDesc());
					ps.setString(4, bulkTransactionResponseFile.getCardStatus());
					ps.setDouble(5, Double.parseDouble(bulkTransactionResponseFile.getAvailableBalance()));
					ps.setString(6, bulkTransactionResponseFile.getTransactionDate());
					ps.setString(7, bulkTransactionResponseFile.getTransactionTime());
					ps.setLong(8, Long.parseLong(bulkTransactionResponseFile.getBatchId()));
					ps.setString(9, bulkTransactionResponseFile.getRecordNum());

				}

				public int getBatchSize() {
					return bulkTransactionResponseFiles.size();
				}
			});
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		logger.info(CCLPConstants.EXIT);

	}

	@Override
	public void insertRequest(List<? extends BulkTransactionRequestFile> bulkTransactionRequestFiles) {
		logger.info(CCLPConstants.ENTER);
		try {
			getJdbcTemplate().batchUpdate(ScriptUtils.INSERT_BLK_TXN, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					BulkTransactionRequestFile BulkTransactionRequestFile = bulkTransactionRequestFiles.get(i);
					ps.setLong(1, Long.parseLong(BulkTransactionRequestFile.getBatchId()));
					ps.setString(2, BulkTransactionRequestFile.getSourceReferenceNumber());
					ps.setString(3, BulkTransactionRequestFile.getFileName());
					ps.setString(4, BulkTransactionRequestFile.getSpNumber());
					ps.setString(5, BulkTransactionRequestFile.getAction());
					ps.setDouble(6, Double.parseDouble(BulkTransactionRequestFile.getAmount()));
					ps.setString(7, BulkTransactionRequestFile.getMdmId());
					ps.setString(8, BulkTransactionRequestFile.getStoreId());
					ps.setString(9, BulkTransactionRequestFile.getTerminalId());

				}

				public int getBatchSize() {
					return bulkTransactionRequestFiles.size();
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
		String sql = ScriptUtils.GET_COMPLETED_RECORD_COUNT;
		long count = 0;
		count = getJdbcTemplate().queryForObject(sql, new Object[] { fileName }, Long.class);
		String sql1 = ScriptUtils.BLK_TXN_FILE_STATUS_UPDATE;
		if (count > 0) {
			getJdbcTemplate().update(sql1, "PARTIAL-COMPLETED", "All records are not processed :P ", fileName);

		} else {
			getJdbcTemplate().update(sql1, "COMPLETED", "OK", fileName);
		}
	}

}
