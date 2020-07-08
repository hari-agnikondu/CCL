package com.incomm.cclp.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.dao.CurrencyConversionDAO;
import com.incomm.cclp.dto.CurrencyConversionRespDetails;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.util.Util;

import io.micrometer.core.instrument.util.StringUtils;

@Repository
public class CurrencyConversionDAOImpl extends JdbcDaoSupport implements CurrencyConversionDAO {

	@Autowired
	DataSource dataSource;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}

	@Autowired
	JdbcTemplate jdbcTemplate;

	private static final Logger logger = LogManager.getLogger(CurrencyConversionDAOImpl.class);
	private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final String REQUIRED_DATETIME_FORMAT = "dd-MMM-yyyy HH:mm:ss";

	@Override
	public String getConversionRate(String mdmId, String txnCurrency, String issuingCurrency) {
		try {
			logger.debug("MDM_id: {}, Transaction_currency: {}, Issuing_Currency: {}", mdmId, txnCurrency, issuingCurrency);
			return getJdbcTemplate().queryForObject(QueryConstants.GET_CONVERSION_RATE, String.class, mdmId, txnCurrency, issuingCurrency);
		} catch (Exception e) {
			logger.warn(SpilExceptionMessages.CONVERSION_RATE_NOT_FOUND + " " + e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.CONVERSION_RATE_NOT_FOUND, ResponseCodes.SYSTEM_ERROR);
		}

	}

	@Override
	public Map<String, Object> addCurrencyRates(String mdmId, String channel, String user, CurrencyConversionRespDetails currConvRespDtls)
			throws Exception {

		logger.info("addCurrencyRates START>>");
		logger.info("MdmId:: {}, IssuingCurrency:: {},TransactionCurrency:: {},COnversionRate:: {}, EffectiveDate:: {}", mdmId,
				currConvRespDtls.getIssuingCurrency(), currConvRespDtls.getTransactionCurrency(), currConvRespDtls.getExchangeRate(),
				currConvRespDtls.getEffectiveDateTime());
		Map<String, Object> resultMap = null;
		try {

			List<SqlParameter> params = new ArrayList<>();
			params.add(new SqlParameter(Types.VARCHAR));// mdmId
			params.add(new SqlParameter(Types.VARCHAR));// issuingCurrency
			params.add(new SqlParameter(Types.VARCHAR));// txnCurrency
			params.add(new SqlParameter(Types.DOUBLE));// conversionRate
			params.add(new SqlParameter(Types.VARCHAR));// effectiveDate
			params.add(new SqlParameter(Types.VARCHAR));// channel
			params.add(new SqlParameter(Types.VARCHAR));// external_userName

			params.add(new SqlOutParameter("8", Types.VARCHAR));// responseCode
			params.add(new SqlOutParameter("9", Types.VARCHAR));// responseMessage

			resultMap = jdbcTemplate.call(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(QueryConstants.ADD_CONVERSION_RATE);
					logger.info("Query:" + QueryConstants.ADD_CONVERSION_RATE);
					cs.setString(1, mdmId);

					cs.setString(2, currConvRespDtls.getIssuingCurrency());

					cs.setString(3, currConvRespDtls.getTransactionCurrency());

					cs.setDouble(4, currConvRespDtls.getExchangeRate());

					try {
						if (StringUtils.isEmpty(currConvRespDtls.getEffectiveDateTime())
								|| new SimpleDateFormat(DATETIME_FORMAT).parse(currConvRespDtls.getEffectiveDateTime())
									.before(new Date())) {
							currConvRespDtls.setEffectiveDateTime(new SimpleDateFormat(DATETIME_FORMAT).format(new Date()));
						}
					} catch (ParseException e) {
						logger.warn("Error in parsing effective date::" + e.getMessage());
					}
					cs.setString(5, Util.formatDt(currConvRespDtls.getEffectiveDateTime(), REQUIRED_DATETIME_FORMAT, DATETIME_FORMAT));

					cs.setString(6, channel);

					cs.setString(7, user);

					cs.registerOutParameter(8, Types.VARCHAR);
					cs.registerOutParameter(9, Types.VARCHAR);

					return cs;
				}
			}, params);

			logger.info("RESPONSE CODE -->" + resultMap.get("8"));
			logger.info("RESPONSE MESSAGE -->" + resultMap.get("9"));

		}

		catch (Exception e) {
			logger.warn("Exception occured in method addCurrencyRates()::" + e.getMessage(), e);
			throw new Exception();

		}
		logger.info("addCurrencyRates END<<<");
		return resultMap;
	}

	@Override
	public Map<String, Object> delCurrencyRates(String mdmId, String channel, String user, CurrencyConversionRespDetails currConvRespDtls)
			throws Exception {

		logger.info("delCurrencyRates DAO START>>");
		logger.info("MdmId:: {}, IssuingCurrency:: {},TransactionCurrency:: {}, EffectiveDate:: {}", mdmId,
				currConvRespDtls.getIssuingCurrency(), currConvRespDtls.getTransactionCurrency(), currConvRespDtls.getEffectiveDateTime());
		Map<String, Object> resultMap = null;
		try {

			List<SqlParameter> params = new ArrayList<>();
			params.add(new SqlParameter(Types.VARCHAR));// mdmId
			params.add(new SqlParameter(Types.VARCHAR));// issuingCurrency
			params.add(new SqlParameter(Types.VARCHAR));// txnCurrency
			params.add(new SqlParameter(Types.VARCHAR));// effectiveDate
			params.add(new SqlParameter(Types.VARCHAR));// channel
			params.add(new SqlParameter(Types.VARCHAR));// external_userName

			params.add(new SqlOutParameter("7", Types.VARCHAR));// responseCode
			params.add(new SqlOutParameter("8", Types.VARCHAR));// responseMessage

			resultMap = jdbcTemplate.call(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(QueryConstants.DEL_CONVERSION_RATE);
					logger.info("Query:" + QueryConstants.DEL_CONVERSION_RATE);

					cs.setString(1, mdmId);

					cs.setString(2, currConvRespDtls.getIssuingCurrency());

					cs.setString(3, currConvRespDtls.getTransactionCurrency());

					if (currConvRespDtls.getEffectiveDateTime() != null && !currConvRespDtls.getEffectiveDateTime()
						.isEmpty()) {
						cs.setString(4, Util.formatDt(currConvRespDtls.getEffectiveDateTime(), REQUIRED_DATETIME_FORMAT, DATETIME_FORMAT));
					} else {
						cs.setString(4, currConvRespDtls.getEffectiveDateTime());
					}

					cs.setString(5, channel);

					cs.setString(6, user);

					cs.registerOutParameter(7, Types.VARCHAR);
					cs.registerOutParameter(8, Types.VARCHAR);

					return cs;
				}
			}, params);
			logger.info("RESPONSE CODE -->" + resultMap.get("7"));
			logger.info("RESPONSE MESSAGE -->" + resultMap.get("8"));

		}

		catch (Exception e) {
			logger.warn("Exception occured in method delCurrencyRates()::" + e.getMessage(), e);
			throw new Exception();

		}
		logger.info("delCurrencyRates DAO END<<<");
		return resultMap;
	}

	@Override
	public List<CurrencyConversionRespDetails> getAllCurrencyConversions(String mdmId) {
		logger.debug("getAllCurrencyConversions ENTER>>>");
		logger.debug("MDM_id: {}", mdmId);
		List<CurrencyConversionRespDetails> currencyRates = new ArrayList<CurrencyConversionRespDetails>();
		List<Map<String, Object>> rates = getJdbcTemplate().queryForList(QueryConstants.GET_ALL_CONVERSION_RATE, mdmId);
		for (Map rate : rates) {
			CurrencyConversionRespDetails currencyRate = new CurrencyConversionRespDetails();
			currencyRate.setTransactionCurrency(rate.get("TRANSACTION_CURRENCY")
				.toString());
			currencyRate.setIssuingCurrency(rate.get("ISSUING_CURRENCY")
				.toString());
			currencyRate.setExchangeRate(Double.valueOf(rate.get("CONVERSION_RATE")
				.toString()));
			currencyRate.setEffectiveDateTime(rate.get("EFFECTIVE_DATE")
				.toString());
			currencyRates.add(currencyRate);
		}
		logger.debug("getAllCurrencyConversions EXIT<<<");
		return currencyRates;
	}

	@Override
	public boolean isValidMdmId(String mdmId) {
		return getJdbcTemplate().queryForObject(QueryConstants.CHECK_MDMID, String.class, mdmId)
			.equals("0") ? false : true;
	}

}
