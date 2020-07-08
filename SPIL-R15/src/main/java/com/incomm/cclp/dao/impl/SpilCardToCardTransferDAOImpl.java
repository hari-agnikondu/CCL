package com.incomm.cclp.dao.impl;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.SpilCardToCardTransferDAO;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.transaction.constants.GeneralConstants;
import com.incomm.cclp.util.Util;

/**
 * 
 * @author sampathkumarl
 *
 */
@Repository
public class SpilCardToCardTransferDAOImpl implements SpilCardToCardTransferDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;

	private static final Logger logger = LogManager.getLogger(SpilCardToCardTransferDAOImpl.class);

	@Override
	public String[] spilCardToCardTransfer(ValueDTO valueDto) throws ServiceException {
		String[] respFields = new String[7];
		Map<String, String> valueObj = valueDto.getValueObj();
		try {

			List<SqlParameter> params = new ArrayList<>();
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.BIGINT));
			params.add(new SqlParameter(Types.BIGINT));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.DATE));
			params.add(new SqlParameter(Types.DATE));
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
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
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
			params.add(new SqlParameter(Types.VARCHAR));

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

			params.add(new SqlOutParameter("54", Types.VARCHAR));
			params.add(new SqlOutParameter("55", Types.VARCHAR));
			params.add(new SqlOutParameter("56", Types.VARCHAR));
			params.add(new SqlOutParameter("57", Types.VARCHAR));
			params.add(new SqlOutParameter("58", Types.VARCHAR));
			params.add(new SqlOutParameter("59", Types.VARCHAR));
			params.add(new SqlOutParameter("60", Types.VARCHAR));

			Map<String, Object> resultMap = jdbcTemplate.call(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = null;
					if (GeneralConstants.MSG_TYPE.equals(valueObj.get(ValueObjectKeys.MSGTYPE))) {
						cs = con.prepareCall(QueryConstants.SP_CARD_TO_CARD_TRANSFER);
						logger.info(QueryConstants.SP_CARD_TO_CARD_TRANSFER);
					} else {
						cs = con.prepareCall(QueryConstants.SP_CARD_TO_CARD_TRANSFER_RVSL);
						logger.info(QueryConstants.SP_CARD_TO_CARD_TRANSFER_RVSL);
					}

					cs.setString(1, valueObj.get(ValueObjectKeys.CARD_NUMBER));
					cs.setInt(2, Integer.parseInt(valueObj.get(ValueObjectKeys.SPIL_PROD_ID)));
					logger.info(ValueObjectKeys.SPIL_PROD_ID + ":" + valueObj.get(ValueObjectKeys.SPIL_PROD_ID));
					cs.setBigDecimal(3, new BigDecimal(valueObj.get(ValueObjectKeys.CARD_ACCOUNT_ID)));
					logger.info(ValueObjectKeys.CARD_ACCOUNT_ID + ":" + valueObj.get(ValueObjectKeys.CARD_ACCOUNT_ID));
					cs.setString(4, valueObj.get(ValueObjectKeys.CARD_CARDSTAT));
					logger.info(ValueObjectKeys.CARD_CARDSTAT + ":" + valueObj.get(ValueObjectKeys.CARD_CARDSTAT));
					cs.setString(5, valueObj.get(ValueObjectKeys.CARD_CARDSTAT));
					logger.info(ValueObjectKeys.CARD_CARDSTAT + ":" + valueObj.get(ValueObjectKeys.CARD_CARDSTAT));
					cs.setString(6, valueObj.get(ValueObjectKeys.PROXY_NUMBER));
					logger.info(ValueObjectKeys.PROXY_NUMBER + ":" + valueObj.get(ValueObjectKeys.PROXY_NUMBER));
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
					java.sql.Date activationdate = null;
					java.sql.Date lasttxndate = null;

					if (valueObj.get(ValueObjectKeys.CARD_ACTIVATION_DATE) != null) {
						java.util.Date date = null;
						try {
							date = sdf1.parse(valueObj.get(ValueObjectKeys.CARD_ACTIVATION_DATE));
						} catch (ParseException e) {
							logger.warn("Error Occured while parsing Card Activation Date {} : error message {}",
									valueObj.get(ValueObjectKeys.CARD_ACTIVATION_DATE), e.getMessage());

						}
						if (date != null)
							activationdate = new java.sql.Date(date.getTime());
					}
					if (valueObj.get(ValueObjectKeys.LAST_TXN_DATE) != null) {
						java.util.Date date1 = null;
						try {
							date1 = sdf1.parse(valueObj.get(ValueObjectKeys.LAST_TXN_DATE));
						} catch (ParseException e) {
							logger.warn("Error Occured while parsing Last Transaction Date {} : error message {}",
									valueObj.get(ValueObjectKeys.LAST_TXN_DATE), e.getMessage());

						}
						if (date1 != null)
							lasttxndate = new java.sql.Date(date1.getTime());
					}
					cs.setDate(7, activationdate);
					logger.info("ActivationDate:" + activationdate);

					cs.setDate(8, lasttxndate);
					logger.info("Last txn date:" + lasttxndate);
					cs.setString(9, valueObj.get(ValueObjectKeys.FIRST_TIMETOPUP_FLAG));
					logger.info(ValueObjectKeys.FIRST_TIMETOPUP_FLAG + ":" + valueObj.get(ValueObjectKeys.FIRST_TIMETOPUP_FLAG));
					cs.setString(10, valueObj.get(ValueObjectKeys.SPIL_UPC));
					logger.info(ValueObjectKeys.SPIL_UPC + ":" + valueObj.get(ValueObjectKeys.SPIL_UPC));
					cs.setString(11, valueObj.get(ValueObjectKeys.SP_NUM_TYPE));
					logger.info(ValueObjectKeys.SP_NUM_TYPE + ":" + valueObj.get(ValueObjectKeys.SP_NUM_TYPE));
					cs.setString(12, valueObj.get(ValueObjectKeys.MSGTYPE));
					logger.info(ValueObjectKeys.MSGTYPE + ":" + valueObj.get(ValueObjectKeys.MSGTYPE));
					cs.setString(13, valueObj.get(ValueObjectKeys.DELIVERY_CHANNEL_CODE));
					logger.info(ValueObjectKeys.DELIVERY_CHANNEL_CODE + ":" + valueObj.get(ValueObjectKeys.DELIVERY_CHANNEL_CODE));
					cs.setString(14, valueObj.get(ValueObjectKeys.TRANS_CODE));
					logger.info(ValueObjectKeys.TRANS_CODE + ":" + valueObj.get(ValueObjectKeys.TRANS_CODE));
					cs.setString(15, valueObj.get(ValueObjectKeys.SPIL_TRAN_DATE));
					logger.info(ValueObjectKeys.SPIL_TRAN_DATE + ":" + valueObj.get(ValueObjectKeys.SPIL_TRAN_DATE));
					cs.setString(16, valueObj.get(ValueObjectKeys.SPIL_TRAN_TIME));
					logger.info(ValueObjectKeys.SPIL_TRAN_TIME + ":" + valueObj.get(ValueObjectKeys.SPIL_TRAN_TIME));
					cs.setString(17, valueObj.get(ValueObjectKeys.SPIL_REQTIME_ZONE));
					logger.info(ValueObjectKeys.SPIL_REQTIME_ZONE + ":" + valueObj.get(ValueObjectKeys.SPIL_REQTIME_ZONE));
					cs.setString(18, valueObj.get(ValueObjectKeys.INCOM_REF_NUMBER));
					logger.info(ValueObjectKeys.INCOM_REF_NUMBER + ":" + valueObj.get(ValueObjectKeys.INCOM_REF_NUMBER));
					cs.setString(19, valueObj.get(ValueObjectKeys.SPIL_MERCHANT_NAME));
					logger.info(ValueObjectKeys.SPIL_MERCHANT_NAME + ":" + valueObj.get(ValueObjectKeys.SPIL_MERCHANT_NAME));
					cs.setString(20, valueObj.get(ValueObjectKeys.SPIL_STORE_ID));
					logger.info(ValueObjectKeys.SPIL_STORE_ID + ":" + valueObj.get(ValueObjectKeys.SPIL_STORE_ID));
					cs.setString(21, valueObj.get(ValueObjectKeys.SPIL_TERM_ID));
					logger.info(ValueObjectKeys.SPIL_TERM_ID + ":" + valueObj.get(ValueObjectKeys.SPIL_TERM_ID));
					cs.setString(22, valueObj.get(ValueObjectKeys.SPIL_ADDRESS_1));
					cs.setString(23, valueObj.get(ValueObjectKeys.SPIL_ADDRESS_2));
					cs.setString(24, valueObj.get(ValueObjectKeys.SPIL_CITY));
					logger.info(ValueObjectKeys.SPIL_CITY + ":" + valueObj.get(ValueObjectKeys.SPIL_CITY));
					cs.setString(25, valueObj.get(ValueObjectKeys.SPIL_STATE));
					logger.info(ValueObjectKeys.SPIL_STATE + ":" + valueObj.get(ValueObjectKeys.SPIL_STATE));
					cs.setString(26, valueObj.get(ValueObjectKeys.SPIL_MERCREF_NUMBER));
					logger.info(ValueObjectKeys.SPIL_MERCREF_NUMBER + ":" + valueObj.get(ValueObjectKeys.SPIL_MERCREF_NUMBER));
					cs.setString(27, valueObj.get(ValueObjectKeys.SPIL_LOCALE_CNTRY));
					logger.info(ValueObjectKeys.SPIL_LOCALE_CNTRY + ":" + valueObj.get(ValueObjectKeys.SPIL_LOCALE_CNTRY));
					cs.setString(28, valueObj.get(ValueObjectKeys.SPIL_LOCALE_CURRENCY));
					logger.info(ValueObjectKeys.SPIL_LOCALE_CURRENCY + ":" + valueObj.get(ValueObjectKeys.SPIL_LOCALE_CURRENCY));
					cs.setString(29, valueObj.get(ValueObjectKeys.SPIL_LOCALE_LANGUAGE));
					logger.info(ValueObjectKeys.SPIL_LOCALE_LANGUAGE + ":" + valueObj.get(ValueObjectKeys.SPIL_LOCALE_LANGUAGE));
					cs.setString(30, valueObj.get(ValueObjectKeys.SPIL_POS_ENTRYMODE));
					logger.info(ValueObjectKeys.SPIL_POS_ENTRYMODE + ":" + valueObj.get(ValueObjectKeys.SPIL_POS_ENTRYMODE));
					cs.setString(31, valueObj.get(ValueObjectKeys.SPIL_POS_CONDCODE));
					logger.info(ValueObjectKeys.SPIL_POS_CONDCODE + ":" + valueObj.get(ValueObjectKeys.SPIL_POS_CONDCODE));
					cs.setString(32, valueObj.get(ValueObjectKeys.SOURCE_INFO));
					logger.info(ValueObjectKeys.SOURCE_INFO + ":" + valueObj.get(ValueObjectKeys.SOURCE_INFO));
					cs.setBigDecimal(33, new BigDecimal(
							valueObj.get(ValueObjectKeys.SPIL_FEE_AMT) != null ? valueObj.get(ValueObjectKeys.SPIL_FEE_AMT) : "0.0"));
					logger.info(ValueObjectKeys.SPIL_FEE_AMT + ":"
							+ (valueObj.get(ValueObjectKeys.SPIL_FEE_AMT) != null ? valueObj.get(ValueObjectKeys.SPIL_FEE_AMT) : "0.0"));
					cs.setBigDecimal(34, new BigDecimal(valueObj.get(ValueObjectKeys.SPIL_TRAN_AMT)));
					logger.info(ValueObjectKeys.SPIL_TRAN_AMT + ":" + valueObj.get(ValueObjectKeys.SPIL_TRAN_AMT));
					cs.setString(35, valueObj.get(ValueObjectKeys.SPIL_ORGNL_TRAN_CURR));
					logger.info(ValueObjectKeys.SPIL_ORGNL_TRAN_CURR + ":" + valueObj.get(ValueObjectKeys.SPIL_ORGNL_TRAN_CURR));
					cs.setString(36, valueObj.get(ValueObjectKeys.PARTY_SUPPORTED));
					logger.info(ValueObjectKeys.PARTY_SUPPORTED + ":" + valueObj.get(ValueObjectKeys.PARTY_SUPPORTED));
					cs.setString(37, ValueObjectKeys.FLAG_NO);
					logger.info(ValueObjectKeys.PARTIAL_PREAUTH_INDICATOR + ":" + ValueObjectKeys.FLAG_NO);
					cs.setString(38, valueObj.get(ValueObjectKeys.MDM_ID));
					logger.info(ValueObjectKeys.MDM_ID + ":" + valueObj.get(ValueObjectKeys.MDM_ID));
					cs.setString(39, valueObj.get(ValueObjectKeys.SPIL_TARGET_CARDNUM));
					logger.info(ValueObjectKeys.SPIL_TARGET_CARDNUM + ":" + valueObj.get(ValueObjectKeys.SPIL_TARGET_CARDNUM));
					cs.setString(40, valueObj.get(ValueObjectKeys.IS_REDEEMED));
					logger.info(ValueObjectKeys.IS_REDEEMED + ":" + valueObj.get(ValueObjectKeys.IS_REDEEMED));
					cs.setString(41, valueObj.get(ValueObjectKeys.SPIL_ZIP));
					logger.info(ValueObjectKeys.SPIL_ZIP + ":" + valueObj.get(ValueObjectKeys.SPIL_ZIP));
					// 13.06.2018 added
					cs.setString(42, valueObj.get(ValueObjectKeys.CASHIER_ID));
					logger.info(ValueObjectKeys.CASHIER_ID + ":" + valueObj.get(ValueObjectKeys.CASHIER_ID));
					cs.setString(43, valueObj.get(ValueObjectKeys.EXP_DATE));
					logger.info(ValueObjectKeys.EXP_DATE + ":" + valueObj.get(ValueObjectKeys.EXP_DATE));

					if (valueObj.get(ValueObjectKeys.CURRENCY_CONV_RATE) != null) {
						cs.setBigDecimal(44, new BigDecimal(valueObj.get(ValueObjectKeys.CURRENCY_CONV_RATE)));
						logger.info(ValueObjectKeys.CURRENCY_CONV_RATE + ":" + valueObj.get(ValueObjectKeys.CURRENCY_CONV_RATE));
					} else {
						cs.setBigDecimal(44, null);
						logger.info(ValueObjectKeys.CURRENCY_CONV_RATE + ":" + "null");
					}
					cs.setString(45, valueObj.get(ValueObjectKeys.TXN_CURRENCY_CODE));
					logger.info(ValueObjectKeys.TXN_CURRENCY_CODE + ":" + valueObj.get(ValueObjectKeys.TXN_CURRENCY_CODE));
					cs.setBigDecimal(46, new BigDecimal(valueObj.get(ValueObjectKeys.ORGNL_TRAN_AMOUNT)));
					logger.info(ValueObjectKeys.ORGNL_TRAN_AMOUNT + ":" + valueObj.get(ValueObjectKeys.ORGNL_TRAN_AMOUNT));
					if (!Util.isEmpty(valueObj.get(ValueObjectKeys.PURAUTHREQ_ACCOUNT_PURSE_ID))) {
						cs.setInt(47, Integer.parseInt(valueObj.get(ValueObjectKeys.PURAUTHREQ_ACCOUNT_PURSE_ID)));
						logger.info(ValueObjectKeys.PURAUTHREQ_ACCOUNT_PURSE_ID + ":"
								+ valueObj.get(ValueObjectKeys.PURAUTHREQ_ACCOUNT_PURSE_ID));
					} else {
						cs.setInt(47, 0);
						logger.info(ValueObjectKeys.PURAUTHREQ_ACCOUNT_PURSE_ID + ":" + "null");
					}
					cs.setString(48, valueObj.get(ValueObjectKeys.PURAUTHREQ_PURSE_NAME));
					logger.info(ValueObjectKeys.PURAUTHREQ_PURSE_NAME + ":" + valueObj.get(ValueObjectKeys.PURAUTHREQ_PURSE_NAME));

					if (valueObj.get(ValueObjectKeys.PURAUTHREQ_TRAN_AMT) != null) {
						cs.setBigDecimal(49, new BigDecimal(valueObj.get(ValueObjectKeys.PURAUTHREQ_TRAN_AMT)));
						logger.info(ValueObjectKeys.PURAUTHREQ_TRAN_AMT + ":" + valueObj.get(ValueObjectKeys.PURAUTHREQ_TRAN_AMT));
					} else {
						cs.setBigDecimal(49, null);
						logger.info(ValueObjectKeys.PURAUTHREQ_TRAN_AMT + ":" + "null");
					}

					cs.setString(50, valueObj.get(ValueObjectKeys.PURAUTHREQ_PURSE_CURRENCY));
					logger.info(ValueObjectKeys.PURAUTHREQ_PURSE_CURRENCY + ":" + valueObj.get(ValueObjectKeys.PURAUTHREQ_PURSE_CURRENCY));
					cs.setString(51, valueObj.get(ValueObjectKeys.PURAUTHREQ_SKUCODE));
					logger.info(ValueObjectKeys.PURAUTHREQ_SKUCODE + ":" + valueObj.get(ValueObjectKeys.PURAUTHREQ_SKUCODE));
					cs.setInt(52, Integer.parseInt(valueObj.get(ValueObjectKeys.PURSE_ID)));
					logger.info(ValueObjectKeys.PURSE_ID + ":" + valueObj.get(ValueObjectKeys.PURSE_ID));

					cs.setString(53, valueObj.get(ValueObjectKeys.STORE_ID));
					logger.info(ValueObjectKeys.STORE_ID + ":" + valueObj.get(ValueObjectKeys.STORE_ID));

					cs.registerOutParameter(54, Types.VARCHAR);// p_resp_code
					cs.registerOutParameter(55, Types.VARCHAR);// p_err_msg
					cs.registerOutParameter(56, Types.VARCHAR);// p_auth_id
					cs.registerOutParameter(57, Types.VARCHAR);// source card balance
					cs.registerOutParameter(58, Types.VARCHAR);// Target card balance
					cs.registerOutParameter(59, Types.VARCHAR);// source card status
					cs.registerOutParameter(60, Types.VARCHAR);// Target card status

					return cs;
				}
			}, params);
			respFields[0] = (String) resultMap.get("54");// Response Code
			respFields[1] = (String) resultMap.get("55");// Response Msg
			respFields[2] = (String) resultMap.get("56");// AuthId
			respFields[3] = (String) resultMap.get("57");// source card balance
			respFields[4] = (String) resultMap.get("58");// Target card balance
			respFields[5] = (String) resultMap.get("59");// source card status
			respFields[6] = (String) resultMap.get("60");// Target card status
			logger.debug("Response message : " + respFields[1] + "Response Code : " + respFields[0] + "Auth Id :" + respFields[2]
					+ "  source card balance:" + respFields[3] + " Target card balance:" + respFields[4] + " source card status:"
					+ respFields[5] + " Target card status:" + respFields[6]);

		} catch (Exception e) {

			logger.error("Exception in Spil Card to Card Transfer Dao: " + e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
		}
		return respFields;

	}

}
