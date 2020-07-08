package com.incomm.scheduler.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.dao.SerialNumberFileDao;
import com.incomm.scheduler.model.SerialNumberFile;
import com.incomm.scheduler.utils.ScriptUtils;

@Repository
public class SerialNumberFileDaoImpl extends JdbcDaoSupport implements SerialNumberFileDao {
	private static final Logger logger = LogManager.getLogger(SerialNumberFileDaoImpl.class);
	
	@Autowired
    public void setDs(@Qualifier("orderDs") DataSource dataSource) {
         setDataSource(dataSource);
    }
	

	@Override
	public int chkDuplicateFiles(String fileName, String tableName, String whereCondition) {
		int result = 0;
		String sql = "SELECT COUNT(1) FROM " + tableName + " " + whereCondition;
		result = getJdbcTemplate().queryForObject(sql, new Object[] { fileName }, Integer.class);
		return result;
	}

	@Override
	public void truncateDataFromTemp(String tableName){
		
		logger.info(CCLPConstants.ENTER);
		List<SqlParameter> params = new ArrayList<>();
		Map<String,Object> result = null;
		params.add(new SqlParameter( Types.VARCHAR));
		params.add(new SqlParameter(Types.VARCHAR));
		params.add(new SqlOutParameter("error", Types.VARCHAR));
		try {
			result = getJdbcTemplate().call(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(ScriptUtils.TRUNCATE);
					cs.setString(1, tableName);
					cs.setString(2, "SERIAL_NUMBER_FILE");
					cs.registerOutParameter(3, Types.VARCHAR);
					return cs;
				}
			}, params);
			
			logger.info("Result of the Truncate Procedure {}",result);
		}catch(Exception e) {
			logger.error("Error occured while truncating table "+ e.getMessage());
		}
		logger.info(CCLPConstants.EXIT);
	}


	@Override
	public int chkDuplicateFileStatus(String fileName,boolean jobStatus) {
		int result = 0;
		String sql = ScriptUtils.CHECK_SERIAL_FILE_DUPLICATE_FILE;
		if(jobStatus){
			sql+=" and FILE_STATUS=?";
			result = getJdbcTemplate().queryForObject(sql, new Object[] { fileName, "COMPLETED"}, Integer.class);
		}
		else{
			result = getJdbcTemplate().queryForObject(sql, new Object[] { fileName}, Integer.class);
		}
		return result;
	}
	//SerialNumber File Upload
	
	@Override
	public void insertSerialNumberFile(List<? extends SerialNumberFile> serialNumberFiles) {
		
		logger.info(CCLPConstants.ENTER);
		getJdbcTemplate().batchUpdate(ScriptUtils.SERIALNUMBER_FILE_INSERT_TEMP, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				SerialNumberFile serialNumberFile = serialNumberFiles.get(i);
				ps.setString(1, serialNumberFile.getFileName());
				ps.setLong(2, serialNumberFile.getProductId());
				ps.setString(3, serialNumberFile.getSerialNumber());
				ps.setString(4, serialNumberFile.getVan16());
				ps.setString(5, "Y");
				ps.setInt(6, 1);
				ps.setString(7, "COMPLETED");
			}

			public int getBatchSize() {
				return serialNumberFiles.size();
			}
		});
		logger.info(CCLPConstants.EXIT);
	}
	
	@Override
	public void updateSerialNumberFileStatus(String fileName, String status, String errorMsg) {
		
		String sql = ScriptUtils.SERIALNUMBER_FILE_STATUS_UPDATE;
		getJdbcTemplate().update(sql, status,errorMsg,fileName );
	}
	
	@Override
	public void insertSerialNumberFileStatus(String fileName, String status, String errorMsg) {
		
		String sql = ScriptUtils.SERIALNUMBER_FILE_STATUS_INSERT;
		getJdbcTemplate().update(sql, fileName,status,errorMsg );
	}
	
	@Override
	public void serialNumberFileProcCall() {
		logger.info(CCLPConstants.ENTER);
	
			List<SqlParameter> params = new ArrayList<>();
		Map<String,Object> result = null;
		params.add(new SqlParameter( Types.NUMERIC));
		params.add(new SqlOutParameter("P_ERR_MSG", Types.VARCHAR));
		try {
			result = getJdbcTemplate().call(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(ScriptUtils.SERIALNUMBER_FILE_PROC_CALL);
					cs.setInt(1, 1);
					cs.registerOutParameter(2, Types.VARCHAR);
					return cs;
				}
			}, params);
			
			logger.info("Result of serial number file process Procedure {}",result);
		}catch(Exception e) {
			logger.error("Error occured in serial number file process "+ e.getMessage());
		}
		logger.info(CCLPConstants.EXIT);
	}  

}
