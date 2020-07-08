package com.incomm.cclp.fsapi.dao.impl;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.fsapi.constants.B2BResponseCode;
import com.incomm.cclp.fsapi.constants.B2BResponseMessage;
import com.incomm.cclp.fsapi.dao.ProductDAO;


@Repository
public class ProductDAOImpl extends JdbcDaoSupport implements ProductDAO {

	@Autowired
	@Qualifier("transactionalDs")
	private DataSource dataSource;
	
	@Autowired
    public void setDs() {
         setDataSource(dataSource);
    }
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	
	/*@Autowired
	@Qualifier("transactionalDs")*/
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@PostConstruct
	private void postConstruct() {
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	@Override
	public int checkProductByProductId(String productId) {
		return (getJdbcTemplate().queryForObject(QueryConstants.CHECK_PRODUCT, new Object[] { productId }, Integer.class));
	}

	@Override
	public String getProductAttributesByProductId(String productId) {
		return getJdbcTemplate().queryForObject(QueryConstants.GET_PRODUCT_ATTRIBUTES, new Object[] { productId },
				String.class);
	}

	
	public int checkProductPackageId(String productId, String packageId) {
		return (getJdbcTemplate().queryForObject(QueryConstants.CHECK_PRODUCT_PACKAGEID, new Object[] { productId,packageId }, Integer.class));
		
	}

	@Override
	public int checkPartnerId(String productId, String partnerId) {
		int count;
		count = (getJdbcTemplate().queryForObject(QueryConstants.CHECK_PARTNERID, new Object[] { productId,partnerId }, Integer.class));
		
		if(count < 0) {
			count = (getJdbcTemplate().queryForObject(QueryConstants.CHECK_PARTNERID_GROUP_ACCESS, new Object[] { productId,partnerId }, Integer.class));
		}
		return count;
	}

	@Override
	public List<String> checkFulFillmentVendor(Set<String> packageIdList) {
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("packageIdList", packageIdList);
		
		return namedParameterJdbcTemplate.queryForList(QueryConstants.ORDER_FULFILLMENT_VENDOR,
				parameters, String.class);
		
	}
	@Override
	public List<Map<String, Object>> checkFulFillmentVendorWithkeypair(Set<String> packageIdList) {
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("packageIdList", packageIdList);
		
		return namedParameterJdbcTemplate.queryForList(QueryConstants.ORDER_FULFILLMENT_VENDOR_KEYPAIR,
				parameters);
		
	}

	@Override
	public List<String> checkVirtualProduct(Set<String> productIdSet) {
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("productIdSet", productIdSet);
		return namedParameterJdbcTemplate.queryForList(QueryConstants.GET_CARD_TYPE,
				parameters, String.class);
		
		
	}

	@Override
	public String getChannels() {
		
		return getJdbcTemplate().queryForObject(QueryConstants.GET_CHNL_FROM_GLOBAL_PARAM, String.class);
	}

	@Override
	public List<Map<String, Object>> getPackageAttributes(String packageId) {
		
		return getJdbcTemplate().queryForList(QueryConstants.GET_PACKAGE_ATTRIBUTES,packageId);
	}

	
	/**
	 * Business date logic.
	 */
	
	
	@Override
	public String getBusinessDate(String productid) throws ServiceException {
		String[] result = new String[3];

		try {
			List<SqlParameter> params = new ArrayList<>();

			params.add(new SqlParameter(Types.VARCHAR));

			params.add(new SqlOutParameter("businessdate", Types.VARCHAR));
			params.add(new SqlOutParameter("respcode", Types.VARCHAR));
			params.add(new SqlOutParameter("respmsg", Types.VARCHAR));


			Map<String, Object> resultMap = jdbcTemplate.call(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall("{call clp_transactional.SP_GET_CUTOFF(?,?,?,?)}");

					cs.setString(1, productid);

					cs.registerOutParameter(2, Types.VARCHAR);
					cs.registerOutParameter(3, Types.VARCHAR);
					cs.registerOutParameter(4, Types.VARCHAR);

					return cs;
				}
			}, params);

			result[0] = (String) resultMap.get("businessdate");
			result[1] = (String) resultMap.get("respcode");
			result[2] = (String) resultMap.get("respmsg");
			logger.error("partnerId validation response: responseCode: {}, responseMsg: {}", result[1], result[2]);
			return result[0];

		} catch (Exception e) {
			logger.error(e);
			throw new ServiceException(B2BResponseMessage.SYSTEM_ERROR, B2BResponseCode.SYSTEM_ERROR);
		}

	}
	@Override
	public int checkProductvalidity(String productId) {
		
		return jdbcTemplate.queryForObject(QueryConstants.CHECK_PRODUCT_VALIDITY, new Object[] { productId },
				Integer.class);
		
	}
	
	
	@Override
	public void updateCardStatus(String cardNumHash, String expiredProductCardStatus) {
		jdbcTemplate.update(QueryConstants.UPDATE_CARD_STATUS, expiredProductCardStatus, cardNumHash);
		
	}


}
