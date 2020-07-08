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
import com.incomm.scheduler.dao.ShipmentFileDao;
import com.incomm.scheduler.model.ShipmentFile;
import com.incomm.scheduler.utils.GenUtils;
import com.incomm.scheduler.utils.ScriptUtils;

@Repository
public class ShipmentFileDaoImpl extends JdbcDaoSupport implements ShipmentFileDao {
	private static final Logger logger = LogManager.getLogger(ShipmentFileDaoImpl.class);
	
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
					cs.setString(2, "SHIPMENT_FILE");
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
		
		logger.info(CCLPConstants.ENTER);
		int result = 0;
		String sql = ScriptUtils.CHECK_SHIPMENT_FILE_DUPLICATE_FILE;
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
	//SerialNumber File Upload
	
	@Override
	public void insertShipmentFile(List<? extends ShipmentFile> shipmentFiles) {
		
		logger.info(CCLPConstants.ENTER);
		getJdbcTemplate().batchUpdate(ScriptUtils.SHIPMENT_FILE_INSERT_TEMP, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ShipmentFile shipmentFile = shipmentFiles.get(i);
				ps.setString(1, shipmentFile.getFileName());
				ps.setString(2, shipmentFile.getCustomerDesc());
				ps.setString(3, shipmentFile.getSourceOneBatch());
				ps.setString(4, shipmentFile.getParentOrderId());
				ps.setString(5, shipmentFile.getChildOrderId());
				ps.setDate(6, GenUtils.converToDate(shipmentFile.getFileDate()));
				ps.setString(7, shipmentFile.getSerialNumber());
				ps.setString(8, shipmentFile.getCards());
				ps.setString(9, shipmentFile.getPackageId());
				ps.setString(10, shipmentFile.getCardType());
				ps.setString(11, shipmentFile.getContactName());
				ps.setString(12, shipmentFile.getShipto());
				ps.setString(13, shipmentFile.getAddress1());
				ps.setString(14, shipmentFile.getAddress2());
				ps.setString(15, shipmentFile.getCity());
				ps.setString(16, shipmentFile.getState());
				ps.setString(17, shipmentFile.getZip());
				ps.setString(18, shipmentFile.getTrackingNumber());
				ps.setDate(19, GenUtils.converToDate(shipmentFile.getShipDate()));
				ps.setString(20, shipmentFile.getShipmentId());
				ps.setString(21, shipmentFile.getShipMethod());
				ps.setString(22, "Y");
				ps.setInt(23, 1);
				ps.setString(24, "COMPLETED");
			}

			public int getBatchSize() {
				return shipmentFiles.size();
			}
		});
		logger.info(CCLPConstants.EXIT);
	}
	
	@Override
	public void shipmentFileProcCall() {
	
		logger.info(CCLPConstants.ENTER);
		List<SqlParameter> params = new ArrayList<>();
		Map<String,Object> result = null;
		params.add(new SqlParameter( Types.NUMERIC));
		params.add(new SqlOutParameter("P_ERR_MSG", Types.VARCHAR));
		try {
			result = getJdbcTemplate().call(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(ScriptUtils.SHIPMENT_FILE_PROC_CALL);
					cs.setInt(1, 1);
					cs.registerOutParameter(2, Types.VARCHAR);
					return cs;
				}
			}, params);
			
			logger.info("Result of shipment file process Procedure {}",result);
		}catch(Exception e) {
			logger.error("Error occured in shipment file process "+ e.getMessage());
		}
		logger.info(CCLPConstants.EXIT);
	}


	@Override
	public void updateShipmentFileStatus(String fileName, String status,
			String errorMsg) {
		String sql = ScriptUtils.SHIPMENT_FILE_STATUS_UPDATE;
		getJdbcTemplate().update(sql, status,errorMsg,fileName);
	}


	@Override
	public void insertShipmentFileStatus(String fileName, String status,
			String errorMsg) {
		String sql = ScriptUtils.SHIPMENT_FILE_STATUS_INSERT;
		getJdbcTemplate().update(sql,fileName,status,errorMsg );
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
	public String getShipmentFileFormat() {
		String sql = "select attribute_value  from ATTRIBUTE_DEFINITION where ATTRIBUTE_GROUP='Global Parameters' and ATTRIBUTE_NAME='cnFileName'";
		List<Map<String, Object>> shipmentFormat= getJdbcTemplate().queryForList(sql);
		return Optional.ofNullable( String.valueOf(shipmentFormat.get(0).get("attribute_value"))).orElse("No data");
	}


	@Override
	public void truncateDataFromTemp(String tableName, List<String> fileNames) {

		int result = 0;
		
		String params = fileNames.toString().replace("[","'").replace("]","'").replace(", ", "','" );
		
		String sql = "delete FROM " + tableName + " where file_name in ("+params+") " ;
	result = 	getJdbcTemplate().update(sql);
	logger.info("result of truncate :{} is {} ",tableName,result);
	}

}
