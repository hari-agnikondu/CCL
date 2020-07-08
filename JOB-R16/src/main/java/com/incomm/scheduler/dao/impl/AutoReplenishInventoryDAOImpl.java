package com.incomm.scheduler.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.constants.QueryConstants;
import com.incomm.scheduler.dao.AutoReplenishInventoryDAO;
import com.incomm.scheduler.model.AutoReplenishmentInventory;
import com.incomm.scheduler.utils.ScriptUtils;

@Repository
public class AutoReplenishInventoryDAOImpl extends JdbcDaoSupport implements AutoReplenishInventoryDAO{

	private static final Logger logger = LogManager.getLogger(AutoReplenishInventoryDAOImpl.class);
	
	@Autowired
    public void setDs(DataSource dataSource) {
         setDataSource(dataSource);
    }

	
	@Override
	public String intiateBatchAutoReplenishInventory() {

		logger.info(CCLPConstants.ENTER);
		List<SqlParameter> params = new ArrayList<>();
		Map<String, Object> resultMap = null;
		String result = " ";
		try {
			params.add(new SqlOutParameter("p_autorepl_count", Types.NUMERIC));
			params.add(new SqlOutParameter("p_errmsg", Types.VARCHAR));
			
			resultMap = getJdbcTemplate().call(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(ScriptUtils.ORDER_PROC_CALL);
					cs.registerOutParameter(1, Types.NUMERIC); //---commented because there is no out paramenter
					cs.registerOutParameter(2, Types.VARCHAR); 
					return cs;
				}
			}, params);
		
			
		}catch(Exception e) {
		logger.error(e.getMessage());
		}
		if(resultMap != null) {
			result = (String) resultMap.get("p_errmsg");
		}
		logger.info(CCLPConstants.EXIT);
		return result;

	}

	@Override
	public List<AutoReplenishmentInventory> autoReplenishCheck() {
		return getJdbcTemplate().query(QueryConstants.AUTO_REPLENISH_DETAILS, new RowMapper<AutoReplenishmentInventory>() {
	        @Override
			public AutoReplenishmentInventory mapRow(ResultSet rs, int rownum) throws SQLException {
	        	AutoReplenishmentInventory autoRepInv = new AutoReplenishmentInventory();
	        	
	        	autoRepInv.setProductId(rs.getLong("PRODUCT_ID"));
	        	autoRepInv.setLocationId(rs.getString("LOCATION_ID"));
	        	autoRepInv.setReorderLevel(rs.getLong("REORDER_LEVEL"));
	        	autoRepInv.setReorderValue(rs.getString("REORDER_VALUE"));
	        	autoRepInv.setAutoReplenish(rs.getString("AUTO_REPLENISH"));
	        	autoRepInv.setMaxInventory(rs.getString("MAX_INVENTORY"));
	        	autoRepInv.setCurrInventory(rs.getString("CURR_INVENTORY"));
	        	autoRepInv.setMerchantId(rs.getString("MERCHANT_ID"));
	        	
				return autoRepInv;
			}
		});
	}
	


	@Override
	public List getProductDetails(String productID) {
		
		return getJdbcTemplate().queryForList("select p.attributes.Product.defaultPackage default_Package,issuer_id,partner_id from product p where p.product_id=?", productID);
	}
	

}
