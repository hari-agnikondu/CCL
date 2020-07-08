package com.incomm.cclp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.incomm.cclp.account.domain.model.ProductPurse;
import com.incomm.cclp.account.domain.model.PurseType;
import com.incomm.cclp.account.util.CodeUtil;
import com.incomm.cclp.constants.LoggerConstants;
import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.dao.ProductDAO;
import com.incomm.cclp.dto.PurseDTO;
import com.incomm.cclp.exception.ServiceException;

@Repository
public class ProductDAOImpl extends JdbcDaoSupport implements ProductDAO {

	@Autowired
	DataSource dataSource;

	public final RowMapper<ProductPurse> productPurseRowMapper = getProductPurseRowMapper();

	private final Logger logger = LogManager.getLogger(this.getClass());

	private static final String GET_PRODUCT_PURSE_LIST_QUERY = "SELECT pp.product_id, p.purse_id , p.ext_purse_id , p.purse_type_id , "
			+ "p.description, cc.currency_id , cc.currency_code , cc.minor_units , pp.is_default FROM clp_configuration.purse p, "
			+ "clp_configuration.purse_type pt, currency_code cc, clp_configuration.product_purse pp WHERE p.purse_type_id = pt.purse_type_id "
			+ "AND cc.currency_id = p.currency_code AND pp.purse_id = p.purse_id AND pp.product_id = :productId";

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}

	@Override
	public int checkProductByProductId(String productId) {
		return (getJdbcTemplate().queryForObject(QueryConstants.CHECK_PRODUCT, new Object[] { productId }, Integer.class));
	}

	@Override
	public String getProductAttributesByProductId(String productId) {
		return getJdbcTemplate().queryForObject(QueryConstants.GET_PRODUCT_ATTRIBUTES, new Object[] { productId }, String.class);
	}

	public int checkRuleSetById(String ruleSetId) {
		return (getJdbcTemplate().queryForObject(QueryConstants.CHECK_RULESET, new Object[] { ruleSetId }, Integer.class));
	}

	@Override
	public List<Map<String, Object>> getRuleSetById(String ruleSetId) {
		return getJdbcTemplate().queryForList(QueryConstants.GET_RULES_FOR_RULESETID, ruleSetId);
	}

	@Override
	public List<Map<String, Object>> getRuleDetailsById(int ruleId) {
		return getJdbcTemplate().queryForList(QueryConstants.GET_RULES_DETAILS_FOR_RULEID, ruleId);
	}

	@Override
	public String getTransactionIdentifier(String deliveryChannelCode, String transCode, String msgType) {
		return getJdbcTemplate().queryForObject(QueryConstants.GET_TRANSACTION_IDENTIFIER,
				new Object[] { deliveryChannelCode, transCode, msgType }, String.class);
	}

	@Override
	public int getProductRuleSet(String productId) {
		int ruleSetId;
		try {
			ruleSetId = getJdbcTemplate().queryForObject(QueryConstants.GET_RULE_SET_ID, new Object[] { productId }, Integer.class);
		} catch (EmptyResultDataAccessException e) {
			return 0;
		}
		return ruleSetId;
	}

	@Override
	public int checkProductValidity(String productId) {

		return getJdbcTemplate().queryForObject(QueryConstants.CHECK_PRODUCT_VALIDITY, new Object[] { productId }, Integer.class);

	}

	@Override
	public void updateCardStatus(String cardNumHash, String cardStatus) throws ServiceException {
		logger.debug(LoggerConstants.ENTER);
		try {
			logger.info("update Expired product card status query: {}, parameters: cardStatus: {}, cardNumHash: {}",
					QueryConstants.UPDATE_CARD_STATUS, cardStatus, cardNumHash);
			getJdbcTemplate().update(QueryConstants.UPDATE_CARD_STATUS, cardStatus, cardNumHash);
		} catch (DataAccessException e) {
			logger.error("DataAccessException Occured while updating card status: " + e.getMessage(), e);
			throw new ServiceException("DataAccessException Occured while updating card status", ResponseCodes.SYSTEM_ERROR);
		} catch (Exception e) {
			logger.error("Exception Occured while updating  card status: " + e.getMessage(), e);
			throw new ServiceException("Exception Occured while updating card status", ResponseCodes.SYSTEM_ERROR);
		}
		logger.debug(LoggerConstants.EXIT);
	}

	@Override
	public String getPartnerDetails(String productId, String partnerName) {

		return getJdbcTemplate().queryForObject(QueryConstants.GET_PARTNER_DETAILS, new Object[] { productId, partnerName }, String.class);

	}

	@Override
	public String getMdmId(String partnerId) {

		return getJdbcTemplate().queryForObject(QueryConstants.GET_MDMID, new Object[] { partnerId }, String.class);
	}

	@Override
	public String getProductDefaultCurr(String purseId) {

		return getJdbcTemplate().queryForObject(QueryConstants.GET_CURRENCY_CODE, new Object[] { purseId }, String.class);

	}

	@Override
	public PurseDTO getpurseDto(String purseName) {
		String query = "SELECT p.purse_type_id AS pursetype, p.ext_purse_id AS pursename, p.description, p.purse_id AS purseid, "
				+ "p.currency_code AS currencycode, pp.purse_type_name, cc.minor_units as minorUnits FROM clp_configuration.purse p, "
				+ "clp_configuration.purse_type pp, currency_code cc WHERE p.purse_type_id = pp.purse_type_id and "
				+ "cc.currency_id = p.currency_code AND upper(ext_purse_id) =  upper(:purseName)";
		List<PurseDTO> purseDtos = getJdbcTemplate().query(query, new RowMapper<PurseDTO>() {
			@Override
			public PurseDTO mapRow(ResultSet rs, int rownumber) throws SQLException {
				PurseDTO purseDto = new PurseDTO();
				purseDto.setPurseTypeId(rs.getLong(1));
				purseDto.setPurseName(rs.getString(2));
				purseDto.setDescription(rs.getString(3));
				purseDto.setPurseId(rs.getLong(4));
				purseDto.setCurrencyCode(rs.getString(5));
				purseDto.setPurseType(rs.getString(6));
				purseDto.setMinorUnits(rs.getObject(7, Integer.class));

				return purseDto;
			}
		}, purseName);

		return CollectionUtils.isEmpty(purseDtos) ? null : purseDtos.get(0);
	}

	@Override
	public List<ProductPurse> getPursesByProductId(long productId) {
		return getJdbcTemplate().query(GET_PRODUCT_PURSE_LIST_QUERY, this.productPurseRowMapper, productId);
	}

	private RowMapper<ProductPurse> getProductPurseRowMapper() {
		return (rs, nowNum) -> ProductPurse.builder()
			.productId(rs.getLong("product_id"))
			.purseId(rs.getLong("purse_id"))
			.purseName(rs.getString("ext_purse_id"))
			.purseType(PurseType.byPurseTypeId(rs.getInt("purse_type_id"))
				.orElse(null))
			.description(rs.getString("description"))
			.currencyId(rs.getString("currency_id"))
			.currencyCode(rs.getString("currency_code"))
			.currencyMinorUnits(rs.getInt("minor_units"))
			.isDefault(CodeUtil.mapYNToBoolean(rs.getString("is_default")))
			.build();
	}

}
