package com.incomm.cclp.transaction.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.incomm.cclp.constants.MsgTypeConstants;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.transaction.bean.CardStatus;
import com.incomm.cclp.transaction.bean.RuleParameter;
import com.incomm.cclp.transaction.bean.RuleParameterMapping;
import com.incomm.cclp.transaction.bean.SpilResponseCode;
import com.incomm.cclp.transaction.bean.SpilStartupMsgTypeBean;
import com.incomm.cclp.transaction.bean.TransactionAuthDefinition;
import com.incomm.cclp.transaction.bean.TransactionInputValidation;
import com.incomm.cclp.transaction.bean.Transactions;
import com.incomm.cclp.transaction.constants.TransactionQueryConstants;
import com.incomm.cclp.transaction.dao.TransactionDAO;
import com.incomm.cclp.transaction.mapper.CardStatusMapper;
import com.incomm.cclp.transaction.mapper.RuleParameterMapper;
import com.incomm.cclp.transaction.mapper.RuleParameterMappingMapper;
import com.incomm.cclp.transaction.mapper.SpilResponseCodesMapper;
import com.incomm.cclp.transaction.mapper.SpilStartupMsgTypeBeanMapper;
import com.incomm.cclp.transaction.mapper.TransactionAuthDefinitionMapper;
import com.incomm.cclp.transaction.mapper.TransactionInputValidationMapper;

/**
 * Transaction DAO provides all the DAO operations for Transactions.
 * 
 * @author venkateshgaddam
 */

@Repository
public class TransationDAOImpl implements TransactionDAO {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * Getting Transaction Input validations based on delivery channel return List<TransactionInputValidation>
	 */
	public List<TransactionInputValidation> getTransactionInputValidations(String deliveryChannel) {
		Map<String, String> parameters = new HashMap<>();
		parameters.put(ValueObjectKeys.CHANNEL_CODE, deliveryChannel);
		return namedParameterJdbcTemplate.query(TransactionQueryConstants.TRANSACTION_INPUT_VALIDATION, parameters,
				new TransactionInputValidationMapper());

	}

	/**
	 * Getting spil message type return List<SpilStartupMsgTypeBean>
	 */
	public List<SpilStartupMsgTypeBean> getSpilMessageType(String deliveryChannel) {
		Map<String, String> parameters = new HashMap<>();
		parameters.put(ValueObjectKeys.CHANNEL_CODE, deliveryChannel);
		return namedParameterJdbcTemplate.query(TransactionQueryConstants.TRANSACTION_SPIL_MESSAGE_TYPE, parameters,
				new SpilStartupMsgTypeBeanMapper());

	}

	@Override
	public List<TransactionAuthDefinition> getAllTransactionAuthDefinitions() {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("channelCode", "01");

		return namedParameterJdbcTemplate.query(TransactionQueryConstants.TRANSACTION_AUTH_DEFINITION, parameters,
				new TransactionAuthDefinitionMapper());
	}

	@Override
	public List<SpilResponseCode> getSpilResponseCodes(String deliveryChannel) {
		Map<String, String> parameters = new HashMap<>();
		parameters.put(ValueObjectKeys.CHANNEL_CODE, deliveryChannel);
		return namedParameterJdbcTemplate.query(TransactionQueryConstants.SPIL_RESPONSE_CODE, parameters, new SpilResponseCodesMapper());

	}

	@Override
	public List<CardStatus> getAllCardStatus() {
		return namedParameterJdbcTemplate.query(TransactionQueryConstants.CARD_STATUS, new CardStatusMapper());
	}

	@Override
	public List<RuleParameter> getAllRuleParameters() {
		return namedParameterJdbcTemplate.query(TransactionQueryConstants.RULE_PARAMETERS, new RuleParameterMapper());
	}

	@Override
	public List<RuleParameterMapping> getAllRuleParametersMapping() {
		return namedParameterJdbcTemplate.query(TransactionQueryConstants.RULE_PARAMETERS_MAPPING, new RuleParameterMappingMapper());
	}

	@Override
	public Map<String, String> getTxnLogDtls(Map<String, String> valueobj) {
		return jdbcTemplate.query(TransactionQueryConstants.DUPLICATE_CHECK_QRY, new ResultSetExtractor<Map<String, String>>() {
			Map<String, String> valMap = new HashMap<>();

			@Override
			public Map<String, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					valMap.put(ValueObjectKeys.REVERSAL_FLAG, rs.getString(1));
					valMap.put(ValueObjectKeys.ORGNL_TRAN_REQ_AMNT, rs.getString(2));
					valMap.put(ValueObjectKeys.ORGNL_AUTH_AMNT, rs.getString(3));
					valMap.put(ValueObjectKeys.ORGNL_TRAN_FEE_AMNT, rs.getString(4));
					valMap.put(ValueObjectKeys.ORGNL_DR_CR_FLAG, rs.getString(5));
					valMap.put(ValueObjectKeys.ORGNL_MAX_FEE_FLAG, rs.getString(6));
					valMap.put(ValueObjectKeys.ORGNL_FREE_FEE_FLAG, rs.getString(7));
					valMap.put(ValueObjectKeys.ORGNL_CURRENCY_CONV_RATE, rs.getString(8));
					valMap.put(ValueObjectKeys.ORGNL_TRAN_AMNT, rs.getString(9));
				}
				return valMap;
			}
		}, valueobj.get(ValueObjectKeys.INCOM_REF_NUMBER), valueobj.get(ValueObjectKeys.DELIVERYCHNL), MsgTypeConstants.MSG_TYPE_NORMAL,
				ResponseCodes.SUCCESS, valueobj.get(ValueObjectKeys.TRANS_CODE), valueobj.get(ValueObjectKeys.CARD_NUM_HASH));
	}

	@Override
	public List<Transactions> getSpilTransactionCodes(String deliveryChannel) {

		return jdbcTemplate.query(TransactionQueryConstants.SPIL_TRANSACTIONS, new ResultSetExtractor<List<Transactions>>() {
			List<Transactions> txnCodeList = new ArrayList<>();

			@Override
			public List<Transactions> extractData(ResultSet rs) throws SQLException, DataAccessException {
				while (rs.next()) {
					Transactions transactions = new Transactions();
					transactions.setTransactionCode(rs.getString(1));
					transactions.setTransactionDescription(rs.getString(2));
					transactions.setTransactionShortName(rs.getString(3));
					transactions.setIsFinancial(rs.getString(4));
					transactions.setCrDrIndicator(rs.getString(5));
					txnCodeList.add(transactions);
				}
				return txnCodeList;
			}
		});

	}

	@Override
	public String getTransactionLogRequestInfo(String rrn, String deliveryChannel, String messageType, String mdmId, String responseCode) {

		List<String> requests = jdbcTemplate.query(TransactionQueryConstants.TRANSACION_INFO_BY_RRN_DELIVERY_CHANNEL_MSG_TYPE_MDM_ID,
				new SingleColumnRowMapper<String>(), rrn, deliveryChannel, messageType, mdmId, responseCode);

		return CollectionUtils.isEmpty(requests) ? null : requests.get(0);
	}

}
