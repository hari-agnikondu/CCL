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
import com.incomm.scheduler.dao.CnShipmentFileDao;
import com.incomm.scheduler.model.CnShipmentFile;
import com.incomm.scheduler.utils.GenUtils;
import com.incomm.scheduler.utils.ScriptUtils;

@Repository
public class CnShipmentFileDaoImpl extends JdbcDaoSupport implements CnShipmentFileDao {
	private static final Logger log = LogManager.getLogger(CnShipmentFileDaoImpl.class);
	
	@Autowired
    public void setDs(@Qualifier("orderDs") DataSource dataSource) {
         setDataSource(dataSource);
    }
	

	@Override
	public void insert(List<? extends CnShipmentFile> cnShipmentFiles) {
		
		log.info(CCLPConstants.ENTER);  
		try {
			  getJdbcTemplate().batchUpdate(ScriptUtils.RETAIL_SHIPMENT_INSERT_TEMP, new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						CnShipmentFile cnShipmentFile = cnShipmentFiles.get(i);
						ps.setInt(1, 1);
						ps.setString(2, cnShipmentFile.getMagicNumber());
						ps.setString(3, cnShipmentFile.getFileName());
						ps.setString(4, cnShipmentFile.getStatus());
						ps.setString(5, cnShipmentFile.getCarrier());
						ps.setDate(6, GenUtils.converToDate(cnShipmentFile.getDate()));
						ps.setString(7, cnShipmentFile.getTrackingNumber());
						ps.setString(8, cnShipmentFile.getMerchantID());
						ps.setString(9, cnShipmentFile.getMerchantName());
						ps.setString(10, cnShipmentFile.getStorelocationID());
						ps.setString(11, cnShipmentFile.getBatchNumber());
						ps.setString(12, cnShipmentFile.getCaseNumber());
						ps.setString(13, cnShipmentFile.getPalletNumber());
						ps.setString(14, cnShipmentFile.getSerialNumber());
						ps.setString(15, cnShipmentFile.getShipTo());
						ps.setString(16, cnShipmentFile.getStreetAddress1());
						ps.setString(17, cnShipmentFile.getStreetAddress2());
						ps.setString(18, cnShipmentFile.getCity());
						ps.setString(19, cnShipmentFile.getState());
						ps.setString(20, cnShipmentFile.getZip());
						ps.setString(21, cnShipmentFile.getdCMSID());
						ps.setString(22, cnShipmentFile.getProdID());
						ps.setString(23, "Raja");
						ps.setString(24, "45454545");
						ps.setString(25, "Y");
						ps.setString(26, "1");
						ps.setString(27, "1");
					}

					public int getBatchSize() {
						log.info("Size:"+cnShipmentFiles.size());
						return cnShipmentFiles.size();
					}
				});
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		  log.info(CCLPConstants.EXIT);

	}

	@Override
	public void makeProcCall() {
		
		log.info(CCLPConstants.ENTER);
		List<SqlParameter> params = new ArrayList<>();
		params.add(new SqlOutParameter("error", Types.VARCHAR));
		params.add(new SqlOutParameter("count", Types.NUMERIC));
		 getJdbcTemplate().call(new CallableStatementCreator() {
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement cs = con.prepareCall(ScriptUtils.RETAIL_SHIPMENT_PROC_CALL);
				cs.registerOutParameter(1, Types.VARCHAR);
				cs.registerOutParameter(2, Types.NUMERIC);

				return cs;
			}
		}, params);

		log.info(CCLPConstants.EXIT);
	}

	@Override
	public int chkDuplicateFiles(String fileName, String tableName, String whereCondition) {
		int result = 0;
		String sql = "SELECT COUNT(1) FROM " + tableName + " " + whereCondition;
		result = getJdbcTemplate().queryForObject(sql, new Object[] { fileName }, Integer.class);
		return result;
	}

	@Override
	public void trauncteDataFromTemp(String tableName) {

		log.info(CCLPConstants.ENTER);
		List<SqlParameter> params = new ArrayList<>();
		Map<String, Object> result = null;
		params.add(new SqlParameter(Types.VARCHAR));
		params.add(new SqlParameter(Types.VARCHAR));
		params.add(new SqlOutParameter("error", Types.VARCHAR));
		try {
			result = getJdbcTemplate().call(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(ScriptUtils.TRUNCATE);
					cs.setString(1, tableName);
					cs.setString(2, "CN_FILE");
					cs.registerOutParameter(3, Types.VARCHAR);
					return cs;
				}
			}, params);

			log.info("Result of the Truncate Procedure {}", result);
		} catch (Exception e) {
			logger.error("Error occured while truncating table " + e.getMessage());
		}
		log.info(CCLPConstants.EXIT);

	}

	@Override
	public void insertCNFileStatus(String fileName, String status, String errorMsg) { 
		
		String sql = ScriptUtils.CN_FILE_STATUS_INSERT;
		getJdbcTemplate().update(sql, fileName,status,errorMsg);
	}

	@Override
	public int chkDuplicateFileStatus(String fileName,boolean jobStatus) {
		int result = 0;
		String sql = ScriptUtils.CHECK_DUPLICATE_FILE;
		if(jobStatus){
			sql+=" and STATUS=?";
			result = getJdbcTemplate().queryForObject(sql, new Object[] { fileName, "COMPLETED"}, Integer.class);
		}
		else{
			result = getJdbcTemplate().queryForObject(sql, new Object[] { fileName}, Integer.class);
		}
		return result;
	}

	@Override
	public void updateCNFileStatus(String fileName, String status, String errorMsg) {
		
		String sql = ScriptUtils.CN_FILE_STATUS_UPDATE;
		getJdbcTemplate().update(sql, status,errorMsg,fileName );
		
	}

}
