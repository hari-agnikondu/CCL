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
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.dao.ResponseFileDao;
import com.incomm.scheduler.model.ResponseFile;
import com.incomm.scheduler.utils.GenUtils;
import com.incomm.scheduler.utils.ScriptUtils;

@Repository
public class ResponseFileDaoImpl extends JdbcDaoSupport implements ResponseFileDao {
	private static final Logger logger = LogManager.getLogger(ResponseFileDaoImpl.class);

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
					cs.setString(2, "RESPONSE_FILE");
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
		String sql = ScriptUtils.CHECK_RESPONSE_FILE_DUPLICATE_FILE;
		if(jobStatus){
			sql+=" and FILE_STATUS=?";
			result = getJdbcTemplate().queryForObject(sql, new Object[] { fileName, "COMPLETED"}, Integer.class);
		}
		else{
			result = getJdbcTemplate().queryForObject(sql, new Object[] { fileName}, Integer.class);
		}
		return result;
	}

	@Override
	public void insertResponseFile(List<? extends ResponseFile> responseFiles) {
		logger.info(CCLPConstants.ENTER);
		 try {
			  getJdbcTemplate().batchUpdate(ScriptUtils.RESPONSE_FILE_INSERT_TEMP, new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						ResponseFile responseFile = responseFiles.get(i);
						ps.setString(1, responseFile.getMagicNumber());
						ps.setString(2, responseFile.getFileName());
						ps.setString(3, responseFile.getStatus());
						ps.setString(4, responseFile.getCarrier());
						ps.setDate(5, GenUtils.converToDate(responseFile.getDate()));
						ps.setString(6, responseFile.getTrackingNumber());
						ps.setString(7, responseFile.getMerchantID());
						ps.setString(8, responseFile.getMerchantName());
						ps.setString(9, responseFile.getStorelocationID());
						ps.setString(10, responseFile.getBatchNumber());
						ps.setString(11, responseFile.getCaseNumber());
						ps.setString(12, responseFile.getPalletNumber());
						ps.setString(13, responseFile.getSerialNumber());
						ps.setString(14, responseFile.getShipTo());
						ps.setString(15, responseFile.getStreetAddress1());
						ps.setString(16, responseFile.getStreetAddress2());
						ps.setString(17, responseFile.getCity());
						ps.setString(18, responseFile.getState());
						ps.setString(19, responseFile.getZip());
						ps.setString(20, responseFile.getdCMSID());
						ps.setString(21, responseFile.getProdID());
						ps.setString(22, responseFile.getOrderID());
						ps.setString(23, responseFile.getSerialNumber());
						ps.setString(24, "Y");
						ps.setString(25, "1");
						ps.setString(26, "COMPLETED");
						
					}

					public int getBatchSize() {
						logger.info("Size:"+responseFiles.size());
						return responseFiles.size();
						
						
					}
				});
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		 logger.info(CCLPConstants.EXIT);
		
	}

	@Override
	public void updateResponseFileStatus(String fileName, String status,
			String errorMsg) {
		String sql = ScriptUtils.RESPONSE_FILE_STATUS_UPDATE;
		getJdbcTemplate().update(sql, status,errorMsg,fileName );
		
	}

	@Override
	public void insertResponseFileStatus(String fileName, String status,
			String errorMsg) {
		String sql = ScriptUtils.RESPONSE_FILE_STATUS_INSERT;
		getJdbcTemplate().update(sql,  fileName,status,errorMsg );
		
	}

	@Override
	public void responseFileProcCall() {
		
		logger.info(CCLPConstants.ENTER);
		
		List<SqlParameter> params = new ArrayList<>();
		Map<String,Object> result = null;
		params.add(new SqlParameter( Types.NUMERIC));
		params.add(new SqlOutParameter("P_ERR_MSG", Types.VARCHAR));
		try {
			result = getJdbcTemplate().call(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(ScriptUtils.RESPONSE_FILE_PROC_CALL);
					cs.setInt(1, 1);
					cs.registerOutParameter(2, Types.VARCHAR);
					return cs;
				}
			}, params);
			
			logger.info("Result Response file Procedure {}",result);
		}catch(Exception e) {
			logger.error("Error occured in response file process "+ e.getMessage());
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
	public String getResponseFileFormat() {
		String sql = "select attribute_value  from ATTRIBUTE_DEFINITION where ATTRIBUTE_GROUP='Global Parameters' and ATTRIBUTE_NAME='cnFileName'";
		List<Map<String, Object>> shipmentFormat= getJdbcTemplate().queryForList(sql);
		return Optional.ofNullable( String.valueOf(shipmentFormat.get(0).get("attribute_value"))).orElse("No data");
	}

	@Override
	public void trauncteDataFromTemp(String tableName, List<String> fileNames) {
			int result = 0;
		
		String params = fileNames.toString().replace("[","'").replace("]","'").replace(", ", "','" );
		
		String sql = "delete FROM " + tableName + " where file_name in ("+params+") " ;
	result = 	getJdbcTemplate().update(sql);
	logger.debug("result of truncate :{} is {} ",tableName,result);
	}

}
