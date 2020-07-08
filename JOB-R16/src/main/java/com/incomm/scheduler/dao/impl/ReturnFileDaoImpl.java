package com.incomm.scheduler.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.dao.ReturnFileDao;
import com.incomm.scheduler.model.ReturnFile;
import com.incomm.scheduler.utils.GenUtils;
import com.incomm.scheduler.utils.ScriptUtils;

@Repository
public class ReturnFileDaoImpl extends JdbcDaoSupport implements ReturnFileDao {
	private static final Logger logger = LogManager.getLogger(ReturnFileDaoImpl.class);
	
	@Autowired
    public void setDs(@Qualifier("orderDs") DataSource dataSource) {
         setDataSource(dataSource);
    }
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public int chkDuplicateFiles(String fileName, String tableName, String whereCondition) {
		int result = 0;
		String sql = "SELECT COUNT(1) FROM " + tableName + " " + whereCondition;
		result = getJdbcTemplate().queryForObject(sql, new Object[] { fileName }, Integer.class);
		return result;
	}

	@Override
	public void trauncteDataFromTemp(String tableName){
		
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
					cs.setString(2, "RETURN_FILE");
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
	public void trauncteDataFromTemp(String tableName,List<String> fileNames){
		
		int result = 0;
		
		String params = fileNames.toString().replace("[","'").replace("]","'").replace(", ", "','" );
		
		String sql = "delete FROM " + tableName + " where file_name in ("+params+") " ;
		result = 	getJdbcTemplate().update(sql);
		logger.info("result of truncate :{} is {} ",tableName,result);
		
		
	}

	@Override
	public int chkDuplicateFileStatus(String fileName,boolean jobStatus) {
		
		logger.info(CCLPConstants.ENTER);
		int result = 0;
		String sql = ScriptUtils.CHECK_RETURN_FILE_DUPLICATE_FILE;
		if(jobStatus){
			sql+=" and FILE_STATUS=?";
			result = getJdbcTemplate().queryForObject(sql, new Object[] { fileName, "COMPLETED"}, Integer.class);
		}
		else{
			result = getJdbcTemplate().queryForObject(sql, new Object[] { fileName}, Integer.class);
		}
		logger.info(CCLPConstants.EXIT);
		return result;
	}

	
	//Return File Upload
	
	@Override
	public void insertReturnFile(List<? extends ReturnFile> returnFiles) {
		
		logger.info(CCLPConstants.ENTER);
		getJdbcTemplate().batchUpdate(ScriptUtils.RETURN_FILE_INSERT_TEMP, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ReturnFile returnFile = returnFiles.get(i);
				ps.setString(1, returnFile.getFileName());
				ps.setString(2, returnFile.getCustomerDesc());
				ps.setString(3, returnFile.getShipSuffixnum());
				ps.setString(4, returnFile.getParentOrderId());
				ps.setString(5, returnFile.getClientOrderId());
				ps.setString(6, returnFile.getSerialNumber());
				ps.setString(7, returnFile.getRejectCode());
				ps.setString(8, returnFile.getRejectReason());
				ps.setDate(9,  GenUtils.converToDate(returnFile.getFileDate()));
				ps.setString(10, returnFile.getCardType());
				ps.setString(11, returnFile.getClientOrderId());
				ps.setString(12, "Y");
				ps.setInt(13, 1);
				ps.setString(14, "COMPLETED");
				ps.setInt(15, 1);
				ps.setInt(16, 1);
				
			}

			public int getBatchSize() {
				return returnFiles.size();
			}
		});
		logger.info(CCLPConstants.EXIT);
		
	}
	
	@Override
	public void updateReturnFileStatus(String fileName, String status, String errorMsg) {
		
		String sql = ScriptUtils.RETURN_FILE_STATUS_UPDATE;
		getJdbcTemplate().update(sql, status,errorMsg,fileName);
		
	}
	
	@Override
	public void insertReturnFileStatus(String fileName, String status, String errorMsg) {
		
		String sql = ScriptUtils.RETURN_FILE_STATUS_INSERT;
		getJdbcTemplate().update(sql, fileName,status,errorMsg);
	}
	
	@Override
	public void returnFileProcCall() {
		
		logger.info(CCLPConstants.ENTER);
		List<SqlParameter> params = new ArrayList<>();
		Map<String,Object> result = null;
		params.add(new SqlParameter( Types.NUMERIC));
		params.add(new SqlOutParameter("P_ERR_MSG", Types.VARCHAR));
		try {
			result = getJdbcTemplate().call(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(ScriptUtils.RETURN_FILE_PROC_CALL);
					cs.setInt(1, 1);
					cs.registerOutParameter(2, Types.VARCHAR);
					return cs;
				}
			}, params);
			
			logger.info("Result Return file Procedure {}",result);
		}catch(Exception e) {
			logger.error("Error In return file procedure "+ e.getMessage());
		}
		logger.info(CCLPConstants.EXIT);
		
	}
	@Override
	public int chkDestinationId(String fileName) {
		int result = 0;
		String sql = "SELECT COUNT(1) FROM FULFILLMENT_VENDOR where FULFILLMENT_VENDOR_ID=?";
		result = getJdbcTemplate().queryForObject(sql, new Object[] { fileName }, Integer.class);
		return result;
	}


	@Override
	public int chkSrcName(String srcName) {
		int result = 0;
		String sql = " select COUNT(1) from ATTRIBUTE_DEFINITION where ATTRIBUTE_GROUP='Global Parameters' and ATTRIBUTE_NAME='srcPlatform' and attribute_value=? ";
		result = getJdbcTemplate().queryForObject(sql, new Object[] { srcName }, Integer.class);
		return result;
	}
	
	
	@Override
	public String getReturnFileFormat() {
		String sql = "select attribute_value  from ATTRIBUTE_DEFINITION where ATTRIBUTE_GROUP='Global Parameters' and ATTRIBUTE_NAME='returnFileName'";
		List<Map<String, Object>> shipmentFormat= getJdbcTemplate().queryForList(sql);
		return Optional.ofNullable( String.valueOf(shipmentFormat.get(0).get("attribute_value"))).orElse("No data");
	}

}
