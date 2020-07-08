package com.incomm.cclp.dao.impl;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.SpilDAO;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.util.Util;

@Repository
public class SpilDAOImpl implements SpilDAO {
	@Autowired
	JdbcTemplate jdbcTemplate;

	private static final Logger logger = LogManager.getLogger(SpilDAOImpl.class);

	@Override
	@Transactional
	public String[] transactionLogEntry(Map<String, String> valueObj, String respCode, String respMsg) throws ServiceException {
		String[] result = new String[2];

		try {
			List<SqlParameter> params = new ArrayList<>();
			params.add(new SqlParameter(Types.BIGINT));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.FLOAT));
			params.add(new SqlParameter(Types.FLOAT));
			params.add(new SqlParameter(Types.FLOAT));
			params.add(new SqlParameter(Types.BIGINT));
			params.add(new SqlParameter(Types.DATE));
			params.add(new SqlParameter(Types.DATE));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.BIGINT));
			params.add(new SqlParameter(Types.BIGINT));
			params.add(new SqlParameter(Types.BIGINT));
			params.add(new SqlParameter(Types.BIGINT));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.FLOAT));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));

			// for issuer id and partner id

			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));

			params.add(new SqlParameter(Types.FLOAT));
			params.add(new SqlParameter(Types.FLOAT));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.FLOAT));

			params.add(new SqlParameter(Types.BIGINT));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.FLOAT));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.BIGINT));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));

			params.add(new SqlOutParameter("71", Types.VARCHAR));
			params.add(new SqlOutParameter("72", Types.VARCHAR));

			Map<String, Object> resultMap = jdbcTemplate.call(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(QueryConstants.TRANSACTION_LOG_ENTRY);

					logger.info(QueryConstants.TRANSACTION_LOG_ENTRY);
					cs.setInt(1, Util.isEmpty(valueObj.get(ValueObjectKeys.PRODUCT_ID)) ? 0
							: Integer.parseInt(valueObj.get(ValueObjectKeys.PRODUCT_ID)));
					logger.info(ValueObjectKeys.PRODUCT_ID + ":" + (Util.isEmpty(valueObj.get(ValueObjectKeys.PRODUCT_ID)) ? 0
							: Integer.parseInt(valueObj.get(ValueObjectKeys.PRODUCT_ID))));
					cs.setString(2, valueObj.get(ValueObjectKeys.DELIVERY_CHANNEL_CODE));
					logger.info(ValueObjectKeys.DELIVERY_CHANNEL_CODE + ":" + valueObj.get(ValueObjectKeys.DELIVERY_CHANNEL_CODE));
					cs.setString(3, valueObj.get(ValueObjectKeys.TRANS_CODE));
					logger.info(ValueObjectKeys.TRANS_CODE + ":" + valueObj.get(ValueObjectKeys.TRANS_CODE));
					cs.setString(4, valueObj.get(ValueObjectKeys.MSG_TYPE));
					logger.info(ValueObjectKeys.MSG_TYPE + ":" + valueObj.get(ValueObjectKeys.MSG_TYPE));
					cs.setString(5, valueObj.get(ValueObjectKeys.IS_FINANCIAL));
					logger.info(ValueObjectKeys.IS_FINANCIAL + ":" + valueObj.get(ValueObjectKeys.IS_FINANCIAL));
					cs.setString(6, valueObj.get(ValueObjectKeys.CREDIT_DEBIT_INDICATOR));
					logger.info(ValueObjectKeys.CREDIT_DEBIT_INDICATOR + ":" + valueObj.get(ValueObjectKeys.CREDIT_DEBIT_INDICATOR));
					cs.setString(7, valueObj.get(ValueObjectKeys.TRANSACTIONDESC));
					logger.info(ValueObjectKeys.TRANSACTIONDESC + ":" + valueObj.get(ValueObjectKeys.TRANSACTIONDESC));
					cs.setString(8, respCode);
					logger.info("responseCode:" + respCode);
					cs.setString(9, respMsg);
					logger.info("ResponseMsg:" + respMsg);
					// Usage Limit
					cs.setString(10, (String) ValueObjectKeys.STATUS_FAILED);
					logger.info("Status:" + ValueObjectKeys.STATUS_FAILED);
					cs.setString(11, valueObj.get(ValueObjectKeys.SPIL_TERM_ID));
					logger.info(ValueObjectKeys.SPIL_TERM_ID + ":" + valueObj.get(ValueObjectKeys.SPIL_TERM_ID));
					cs.setString(12, valueObj.get(ValueObjectKeys.INCOM_REF_NUMBER));
					logger.info(ValueObjectKeys.INCOM_REF_NUMBER + ":" + valueObj.get(ValueObjectKeys.INCOM_REF_NUMBER));
					cs.setString(13, valueObj.get(ValueObjectKeys.CARD_NUM_HASH));
					logger.info(ValueObjectKeys.CARD_NUM_HASH + ":" + valueObj.get(ValueObjectKeys.CARD_NUM_HASH));
					cs.setString(14, valueObj.get(ValueObjectKeys.CARD_NUMBER));
					cs.setString(15, valueObj.get(ValueObjectKeys.PROXY_NUMBER));
					logger.info(ValueObjectKeys.PROXY_NUMBER + ":" + valueObj.get(ValueObjectKeys.PROXY_NUMBER));
					cs.setString(16, valueObj.get(ValueObjectKeys.SPIL_TRAN_DATE));
					logger.info(ValueObjectKeys.SPIL_TRAN_DATE + ":" + valueObj.get(ValueObjectKeys.SPIL_TRAN_DATE));
					cs.setString(17, valueObj.get(ValueObjectKeys.SPIL_TRAN_TIME));
					logger.info(ValueObjectKeys.SPIL_TRAN_TIME + ":" + valueObj.get(ValueObjectKeys.SPIL_TRAN_TIME));
					cs.setString(18, valueObj.get(ValueObjectKeys.SPIL_REQTIME_ZONE));
					logger.info(ValueObjectKeys.SPIL_REQTIME_ZONE + ":" + valueObj.get(ValueObjectKeys.SPIL_REQTIME_ZONE));
					cs.setString(19, (String) ValueObjectKeys.STATUS_YES);
					logger.info("Status:" + ValueObjectKeys.STATUS_YES);
					cs.setString(20, "0"); // Auth_id
					logger.info("Auth Id: 0");
					cs.setFloat(21, Util.isEmpty(valueObj.get(ValueObjectKeys.SPIL_TRAN_AMT)) ? 0
							: Float.parseFloat(valueObj.get(ValueObjectKeys.SPIL_TRAN_AMT)));
					logger.info(ValueObjectKeys.SPIL_TRAN_AMT + ":" + valueObj.get(ValueObjectKeys.SPIL_TRAN_AMT));
					cs.setFloat(22, 0); // Authorized amount
					logger.info(ValueObjectKeys.SPIL_TRAN_AMT + ":" + 0);
					cs.setFloat(23, 0); // transaction fee amount
					logger.info("Transaction fee amt: 0");
					cs.setInt(24, 00); // reversal code
					logger.info("Reversal code: 00");
					cs.setDate(25, new java.sql.Date(new java.util.Date().getTime()));
					cs.setDate(26, new java.sql.Date(new java.util.Date().getTime()));
					cs.setString(27, valueObj.get(ValueObjectKeys.SPIL_ORGNL_TRAN_CURR));
					logger.info(ValueObjectKeys.SPIL_ORGNL_TRAN_CURR + ":" + valueObj.get(ValueObjectKeys.SPIL_ORGNL_TRAN_CURR));
					cs.setString(28, valueObj.get(ValueObjectKeys.CARD_NUM_HASH));
					logger.info(ValueObjectKeys.CARD_NUM_HASH + ":" + valueObj.get(ValueObjectKeys.CARD_NUM_HASH));
					cs.setString(29, valueObj.get(ValueObjectKeys.INCOM_REF_NUMBER));
					logger.info(ValueObjectKeys.INCOM_REF_NUMBER + ":" + valueObj.get(ValueObjectKeys.INCOM_REF_NUMBER));
					cs.setString(30, valueObj.get(ValueObjectKeys.SPIL_TRAN_DATE));
					logger.info(ValueObjectKeys.SPIL_TRAN_DATE + ":" + valueObj.get(ValueObjectKeys.SPIL_TRAN_DATE));
					cs.setString(31, valueObj.get(ValueObjectKeys.SPIL_TRAN_TIME));
					logger.info(ValueObjectKeys.SPIL_TRAN_TIME + ":" + valueObj.get(ValueObjectKeys.SPIL_TRAN_TIME));
					cs.setString(32, valueObj.get(ValueObjectKeys.SPIL_TERM_ID));
					logger.info(ValueObjectKeys.SPIL_TERM_ID + ":" + valueObj.get(ValueObjectKeys.SPIL_TERM_ID));
					cs.setInt(33, 0); // ledger balance
					logger.info("Ledger balance: 0");
					cs.setInt(34, 0); // account balance
					logger.info("Account balance: 0");
					cs.setInt(35, 0); // opening ledger balance
					logger.info("opening ledger balance:0");
					cs.setInt(36, 0); // opening available balance
					logger.info("opening available balance:0");
					cs.setString(37, valueObj.get(ValueObjectKeys.CARD_CARDSTAT));
					logger.info(ValueObjectKeys.CARD_CARDSTAT + ":" + valueObj.get(ValueObjectKeys.CARD_CARDSTAT));
					cs.setString(38, valueObj.get(ValueObjectKeys.COUNTRYCODE));
					logger.info(ValueObjectKeys.COUNTRYCODE + ":" + valueObj.get(ValueObjectKeys.COUNTRYCODE));
					cs.setString(39, valueObj.get(ValueObjectKeys.SPIL_MERCHANT_NAME));
					logger.info(ValueObjectKeys.SPIL_MERCHANT_NAME + ":" + valueObj.get(ValueObjectKeys.SPIL_MERCHANT_NAME));
					cs.setString(40, valueObj.get(ValueObjectKeys.SPIL_STORE_ID));
					logger.info(ValueObjectKeys.SPIL_STORE_ID + ":" + valueObj.get(ValueObjectKeys.SPIL_STORE_ID));
					cs.setString(41, valueObj.get(ValueObjectKeys.SPIL_ADDRESS_1));
					cs.setString(42, valueObj.get(ValueObjectKeys.SPIL_ADDRESS_2));
					cs.setString(43, valueObj.get(ValueObjectKeys.SPIL_CITY));
					logger.info(ValueObjectKeys.SPIL_CITY + ":" + valueObj.get(ValueObjectKeys.SPIL_CITY));
					cs.setString(44, valueObj.get(ValueObjectKeys.SPIL_STATE));
					logger.info(ValueObjectKeys.SPIL_STATE + ":" + valueObj.get(ValueObjectKeys.SPIL_STATE));
					cs.setFloat(45, Util.isEmpty(valueObj.get(ValueObjectKeys.SPIL_FEE_AMT)) ? 0
							: Float.parseFloat(valueObj.get(ValueObjectKeys.SPIL_FEE_AMT)));
					logger.info(ValueObjectKeys.SPIL_FEE_AMT + ":" + valueObj.get(ValueObjectKeys.SPIL_FEE_AMT));
					cs.setString(46, valueObj.get(ValueObjectKeys.SPIL_UPC));
					logger.info(ValueObjectKeys.SPIL_UPC + ":" + valueObj.get(ValueObjectKeys.SPIL_UPC));
					cs.setString(47, valueObj.get(ValueObjectKeys.SPIL_MERCREF_NUMBER));
					logger.info(ValueObjectKeys.SPIL_MERCREF_NUMBER + ":" + valueObj.get(ValueObjectKeys.SPIL_MERCREF_NUMBER));
					cs.setString(48, valueObj.get(ValueObjectKeys.SPIL_LOCALE_CNTRY));
					logger.info(ValueObjectKeys.SPIL_LOCALE_CNTRY + ":" + valueObj.get(ValueObjectKeys.SPIL_LOCALE_CNTRY));
					cs.setString(49, valueObj.get(ValueObjectKeys.SPIL_LOCALE_CURRENCY));
					logger.info(ValueObjectKeys.SPIL_LOCALE_CURRENCY + ":" + valueObj.get(ValueObjectKeys.SPIL_LOCALE_CURRENCY));
					cs.setString(50, valueObj.get(ValueObjectKeys.SPIL_LOCALE_LANGUAGE));
					logger.info(ValueObjectKeys.SPIL_LOCALE_LANGUAGE + ":" + valueObj.get(ValueObjectKeys.SPIL_LOCALE_LANGUAGE));
					cs.setString(51, valueObj.get(ValueObjectKeys.SPIL_POS_ENTRYMODE));
					logger.info(ValueObjectKeys.SPIL_POS_ENTRYMODE + ":" + valueObj.get(ValueObjectKeys.SPIL_POS_ENTRYMODE));
					cs.setString(52, valueObj.get(ValueObjectKeys.SPIL_POS_CONDCODE));
					logger.info(ValueObjectKeys.SPIL_POS_CONDCODE + ":" + valueObj.get(ValueObjectKeys.SPIL_POS_CONDCODE));

					cs.setString(53, valueObj.get(ValueObjectKeys.CARD_ACCOUNT_ID));
					logger.info(ValueObjectKeys.CARD_ACCOUNT_ID + ":" + valueObj.get(ValueObjectKeys.CARD_ACCOUNT_ID));
					cs.setString(54, valueObj.get(ValueObjectKeys.CASHIER_ID));
					logger.info(ValueObjectKeys.CASHIER_ID + ":" + valueObj.get(ValueObjectKeys.CASHIER_ID));
					cs.setString(55, valueObj.get(ValueObjectKeys.EXPDATE));
					cs.setString(56, valueObj.get(ValueObjectKeys.MDM_ID));
					logger.info(ValueObjectKeys.MDM_ID + ":" + valueObj.get(ValueObjectKeys.MDM_ID));

					cs.setString(57, valueObj.get(ValueObjectKeys.ISSUER_ID));
					logger.info(ValueObjectKeys.ISSUER_ID + ":" + valueObj.get(ValueObjectKeys.ISSUER_ID));
					cs.setString(58, valueObj.get(ValueObjectKeys.PARTNER_ID));
					logger.info(ValueObjectKeys.PARTNER_ID + ":" + valueObj.get(ValueObjectKeys.PARTNER_ID));

					cs.setFloat(59, Util.isEmpty(valueObj.get(ValueObjectKeys.ORGNL_TRAN_AMOUNT)) ? 0
							: Float.parseFloat(valueObj.get(ValueObjectKeys.ORGNL_TRAN_AMOUNT)));
					logger.info(ValueObjectKeys.ORGNL_TRAN_AMOUNT + ":" + valueObj.get(ValueObjectKeys.ORGNL_TRAN_AMOUNT));
					cs.setFloat(60, Util.isEmpty(valueObj.get(ValueObjectKeys.ORGNL_TRAN_AMOUNT)) ? 0
							: Float.parseFloat(valueObj.get(ValueObjectKeys.ORGNL_TRAN_AMOUNT))); // Authorized
					// amount
					logger.info(ValueObjectKeys.ORGNL_TRAN_AMOUNT + ":" + valueObj.get(ValueObjectKeys.ORGNL_TRAN_AMOUNT));
					cs.setString(61, valueObj.get(ValueObjectKeys.TXN_CURRENCY_CODE));
					logger.info(ValueObjectKeys.TXN_CURRENCY_CODE + ":" + valueObj.get(ValueObjectKeys.TXN_CURRENCY_CODE));

					if (valueObj.get(ValueObjectKeys.CURRENCY_CONV_RATE) != null) {
						cs.setFloat(62, Util.isEmpty(valueObj.get(ValueObjectKeys.CURRENCY_CONV_RATE)) ? 0
								: Float.parseFloat(valueObj.get(ValueObjectKeys.CURRENCY_CONV_RATE)));
						logger.info(ValueObjectKeys.CURRENCY_CONV_RATE + ":" + valueObj.get(ValueObjectKeys.CURRENCY_CONV_RATE));
					} else {
						cs.setBigDecimal(62, null);
						logger.info(ValueObjectKeys.CURRENCY_CONV_RATE + ":" + "null");
					}

					if (!Util.isEmpty(valueObj.get(ValueObjectKeys.PURAUTHREQ_ACCOUNT_PURSE_ID))) {
						cs.setInt(63, Integer.parseInt(valueObj.get(ValueObjectKeys.PURAUTHREQ_ACCOUNT_PURSE_ID)));
						logger.info(ValueObjectKeys.PURAUTHREQ_ACCOUNT_PURSE_ID + ":"
								+ valueObj.get(ValueObjectKeys.PURAUTHREQ_ACCOUNT_PURSE_ID));
					} else {
						cs.setInt(63, 0);
						logger.info(ValueObjectKeys.PURAUTHREQ_ACCOUNT_PURSE_ID + ":" + "null");
					}

					cs.setString(64, valueObj.get(ValueObjectKeys.PURAUTHREQ_PURSE_NAME));
					logger.info(ValueObjectKeys.PURAUTHREQ_PURSE_NAME + ":" + valueObj.get(ValueObjectKeys.PURAUTHREQ_PURSE_NAME));

					if (valueObj.get(ValueObjectKeys.PURAUTHREQ_TRAN_AMT) != null) {
						cs.setBigDecimal(65, new BigDecimal(valueObj.get(ValueObjectKeys.PURAUTHREQ_TRAN_AMT)));
						logger.info(ValueObjectKeys.PURAUTHREQ_TRAN_AMT + ":" + valueObj.get(ValueObjectKeys.PURAUTHREQ_TRAN_AMT));
					} else {
						cs.setBigDecimal(65, null);
						logger.info(ValueObjectKeys.PURAUTHREQ_TRAN_AMT + ":" + "null");
					}

					cs.setString(66, valueObj.get(ValueObjectKeys.PURAUTHREQ_PURSE_CURRENCY));
					logger.info(ValueObjectKeys.PURAUTHREQ_PURSE_CURRENCY + ":" + valueObj.get(ValueObjectKeys.PURAUTHREQ_PURSE_CURRENCY));
					cs.setString(67, valueObj.get(ValueObjectKeys.PURAUTHREQ_SKUCODE));
					logger.info(ValueObjectKeys.PURAUTHREQ_SKUCODE + ":" + valueObj.get(ValueObjectKeys.PURAUTHREQ_SKUCODE));
					logger.info(ValueObjectKeys.PURSE_ID + ":" + valueObj.get(ValueObjectKeys.PURSE_ID));
					String purseId = valueObj.get(ValueObjectKeys.PURSE_ID);
					if (Util.isEmpty(valueObj.get(ValueObjectKeys.PURSE_ID)) && purseId != null) {
						cs.setInt(68, Integer.parseInt(purseId));
					} else {
						cs.setInt(68, 0);
					}

					cs.setString(69, valueObj.get(ValueObjectKeys.RETAIL_PATRNER_REFERENCE_NUMBER));
					logger.info(ValueObjectKeys.RETAIL_PATRNER_REFERENCE_NUMBER + ":"
							+ valueObj.get(ValueObjectKeys.RETAIL_PATRNER_REFERENCE_NUMBER));

					cs.setString(70, valueObj.get(ValueObjectKeys.STORE_ID));
					logger.info(ValueObjectKeys.STORE_ID + ":" + valueObj.get(ValueObjectKeys.STORE_ID));

					cs.registerOutParameter(71, Types.VARCHAR);
					cs.registerOutParameter(72, Types.VARCHAR);

					return cs;
				}
			}, params);

			result[0] = (String) resultMap.get("71");
			result[1] = (String) resultMap.get("72");
			logger.info("Transaction log entry: responsemsg: {}, responseCode:{}", result[0], result[1]);

			return result;

		}

		catch (Exception e) {
			logger.error("Error in Transaction log entry: " + e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
		}

	}

	@Override
	public void updateUsageLimits(ValueDTO valueDto) throws ServiceException {

		Map<String, String> valueObj = valueDto.getValueObj();

		try {
			logger.info("Update usage Limits: Query-{}", QueryConstants.UPDATE_CARD_USAGE_LIMITS);
			jdbcTemplate.update(QueryConstants.UPDATE_CARD_USAGE_LIMITS, valueObj.get(ValueObjectKeys.CARD_USAGE_LIMIT),
					Double.parseDouble(valueObj.get(ValueObjectKeys.CARD_ACCOUNT_ID)), valueObj.get(ValueObjectKeys.PURSE_ID));
		} catch (Exception e) {
			logger.error("Exception Occured while updating Usage Limits of Card: " + e.getMessage(), e);
			throw new ServiceException("Exception Occured while updating Usage Card status", ResponseCodes.SYSTEM_ERROR);
		}
	}

	@Override
	@Transactional
	public void updateCardStatus(String cardNumHash, String oldCardStatus, String cardStatus) throws ServiceException {

		try {
			logger.info("Update card status: query:{}, parameters(cardStatus: {}, oldcardStatus:{}, cardNumber:{} )", cardStatus,
					oldCardStatus, cardNumHash);
			jdbcTemplate.update(QueryConstants.UPDATE_CARDSTATUS, cardStatus, oldCardStatus, cardNumHash);
		} catch (DataAccessException e) {
			logger.error("DataAccessException Occured while updating Usage card status: " + e.getMessage(), e);
			throw new ServiceException("DataAccessException Occured while updating Usage Consumed card status", ResponseCodes.SYSTEM_ERROR);
		} catch (Exception e) {
			logger.error("Exception Occured while updating Usage Card status: " + e.getMessage(), e);
			throw new ServiceException("Exception Occured while updating Usage Card status", ResponseCodes.SYSTEM_ERROR);
		}

	}

	@Override
	@Transactional
	public void updateExpiryDate(String expiryDate, String cardNumber) {
		try {
			logger.info("updateExpiryDate Start>>>" + "-->" + expiryDate + "-->" + cardNumber.substring(cardNumber.length() - 4));
			jdbcTemplate.update(QueryConstants.UPDATE_EXPIRY_DATE, expiryDate, cardNumber);
		} catch (DataAccessException e) {
			logger.error("DataAccessException Occured while updating expiry date: " + e.getMessage(), e);
			throw new ServiceException("DataAccessException Occured while updating updating expiry date", ResponseCodes.SYSTEM_ERROR);
		} catch (Exception e) {
			logger.error("Exception Occured while updating updating expiry date: " + e.getMessage(), e);
			throw new ServiceException("Exception Occured while updating expiry date", ResponseCodes.SYSTEM_ERROR);
		}

	}

	@Override
	public Map<String, String> getMonthlyFeeCapDetails(Map<String, String> valueObj, java.sql.Date calLastTxnDate) {
		return jdbcTemplate.query(QueryConstants.GET_MONTHLY_FEE_CAP_DETAILS_QRY, new ResultSetExtractor<Map<String, String>>() {
			Map<String, String> valMap = new HashMap<>();

			@Override
			public Map<String, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					valMap.put(ValueObjectKeys.FEE_CAP, rs.getString(1));
					valMap.put(ValueObjectKeys.FEE_ACCRUED, rs.getString(2));
				}
				return valMap;
			}
		}, valueObj.get(ValueObjectKeys.ACCOUNT_NUMBER), calLastTxnDate);

	}

	@Override
	public void newInsertMonthlyFeeCap(Map<String, String> valueObj, java.sql.Date calLastTxnDate, Double feeCap) throws ServiceException {
		jdbcTemplate.update(QueryConstants.MONTHLY_FEE_CAP_DETAILS_INSERT_QRY, valueObj.get(ValueObjectKeys.ACCOUNT_NUMBER), calLastTxnDate,
				feeCap, "0.0", "0.0", "1");
	}

	@Override
	public void updateMonthlyFeeCap(Map<String, String> valueObj) throws ServiceException {
		java.sql.Date calLastTxnDate;
		try {
			calLastTxnDate = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(valueObj.get(ValueObjectKeys.USAGE_FEE_PERIOD))
				.getTime());
			jdbcTemplate.update(QueryConstants.MONTHLY_FEE_CAP_DETAILS_UPDATE_QRY,
					Double.valueOf(valueObj.get(ValueObjectKeys.CURRENT_FEE_ACCRUED)),
					Double.valueOf(valueObj.get(ValueObjectKeys.PRODUCT_MONTHLY_FEE_CAP)), valueObj.get(ValueObjectKeys.ACCOUNT_NUMBER),
					calLastTxnDate);
		} catch (ParseException e) {
			logger.error("ParseException in updateMonthlyFeeCap:" + e.getMessage(), e);
			throw new ServiceException("Exception Occured while updating updateMonthlyFeeCap", ResponseCodes.SYSTEM_ERROR);
		}
	}

	@Override
	public void updateUsageFee(ValueDTO valueDto) throws ServiceException {
		Map<String, String> valueObj = valueDto.getValueObj();
		try {
			Map<String, Object> usageFee = valueDto.getUsageFee();
			if (usageFee != null && !Util.isEmpty(valueObj.get(ValueObjectKeys.UPDATE_USAGE_FEE))) {
				String cardAttributesJsonString = Util.convertMapToJson(usageFee);
				jdbcTemplate.update(QueryConstants.UPDATE_CARD_USAGE_FEE, cardAttributesJsonString,
						Double.parseDouble(valueObj.get(ValueObjectKeys.CARD_ACCOUNT_ID)), valueObj.get(ValueObjectKeys.PURSE_ID));
			}
		} catch (Exception e) {
			logger.error("Exception Occured while updating Usage Fee of Card" + e.getMessage(), e);
			throw new ServiceException("Exception Occured while updating updateUsageFee", ResponseCodes.SYSTEM_ERROR);
		}
	}

	@Override
	public String getPurseBalance(String accountId) {
		return jdbcTemplate.queryForObject(QueryConstants.GET_PURSE_BAL, new Object[] { accountId }, String.class);
	}

	@Override
	public int getCountInStatementLog(String cardNumber) {
		logger.info("Get count of records in Statement Log {})", QueryConstants.CHECK_STMTLOG_FOR_DEACTIVATION);
		return jdbcTemplate.queryForObject(QueryConstants.CHECK_STMTLOG_FOR_DEACTIVATION, new Object[] { cardNumber }, Integer.class);

	}

	@Override
	public int getCountInTransactiontLog(String cardNumber) {
		logger.info("Get count of records in Transaction Log {})", QueryConstants.CHECK_TXNLOG_FOR_DEACTIVATION);
		return jdbcTemplate.queryForObject(QueryConstants.CHECK_TXNLOG_FOR_DEACTIVATION, new Object[] { cardNumber }, Integer.class);
	}

	@Override
	public Map<String, String> getAcccountBalanceAndCurrency(Map<String, String> valObj) {
		String accountId = valObj.get(ValueObjectKeys.CARD_ACCOUNT_ID);
		return jdbcTemplate.query(QueryConstants.GET_ACCOUNT_DEFAULTPURSE_AVAILABLE_BALANCE, new ResultSetExtractor<Map<String, String>>() {

			@Override
			public Map<String, String> extractData(ResultSet rs) throws SQLException {
				if (rs.next()) {
					valObj.put(ValueObjectKeys.AVAIL_BALANCE, rs.getString(1));
					valObj.put(ValueObjectKeys.DEFAULT_PURSE_CURRENCY, rs.getString(2));
				}
				return valObj;
			}
		}, accountId);

	}

	@Override
	public String getServerDate() {
		return jdbcTemplate.queryForObject(QueryConstants.GET_SERVER_DATE, String.class);
	}

}
