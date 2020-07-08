package com.incomm.cclp.dao.impl;

import static com.incomm.cclp.account.util.CodeUtil.not;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.incomm.ccl.common.util.Mapper;
import com.incomm.cclp.account.domain.model.CardStatusType;
import com.incomm.cclp.account.domain.model.SpNumberType;
import com.incomm.cclp.account.util.CodeUtil;
import com.incomm.cclp.account.util.DateTimeUtil;
import com.incomm.cclp.constants.LoggerConstants;
import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.dao.CardDetailsDAO;
import com.incomm.cclp.domain.CardDetail;
import com.incomm.cclp.exception.ServiceException;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Repository
public class CardDetailsDAOImpl extends JdbcDaoSupport implements CardDetailsDAO {

	@Autowired
	DataSource dataSource;

	RowMapper<CardDetail> cardDetailRowMapper;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
		this.cardDetailRowMapper = this.getCardDetailRowMapper();
	}

	@Override
	public Map<String, Object> getCardDetailsBySPNumber(String spNumber) {
		String query = "";
		Map<String, Object> cardDetails = null;
		List<Map<String, Object>> cardDet = null;
		query = QueryConstants.GET_CARD_DETAILS + "AND CARD_NUM_HASH = fn_hash(?) ";
		cardDet = getJdbcTemplate().queryForList(query, spNumber);
		if (CollectionUtils.isEmpty(cardDet)) {

			query = QueryConstants.GET_CARD_DETAILS + QueryConstants.GET_CARDBYACTIVE;
			cardDet = getJdbcTemplate().queryForList(query, spNumber, spNumber, spNumber, spNumber);

			if (CollectionUtils.isEmpty(cardDet)) {
				query = QueryConstants.GET_CARD_DETAILS + QueryConstants.GET_CARDBYINACTIVE;
				cardDet = getJdbcTemplate().queryForList(query, spNumber, spNumber, spNumber, spNumber);

				if (CollectionUtils.isEmpty(cardDet)) {
					query = "Select * from( " + QueryConstants.GET_CARD_DETAILS + QueryConstants.GET_CARDBYCLOSE;
					cardDet = getJdbcTemplate().queryForList(query, spNumber, spNumber, spNumber, spNumber);

				}
			}

		}
		if (!CollectionUtils.isEmpty(cardDet)) {
			cardDetails = cardDet.get(0);
		}

		return cardDetails;
	}

	@Override
	public Map<String, Object> getCardDetailsByCustomerPref(String spNumber, String targetCardNumber) {
		String query = "";
		Map<String, Object> cardDetails = null;
		List<Map<String, Object>> cardDet = null;
		query = QueryConstants.GET_CARD_DETAILS + "AND CARD_NUM_HASH = fn_hash(?) AND CARD_NUM_HASH <> fn_hash(?)";

		cardDet = getJdbcTemplate().queryForList(query, spNumber, targetCardNumber);
		if (CollectionUtils.isEmpty(cardDet)) {

			query = QueryConstants.GET_CARD_DETAILS + QueryConstants.GET_CARDBYACTIVE + " AND CARD_NUM_HASH <>  fn_hash(?) ";
			cardDet = getJdbcTemplate().queryForList(query, spNumber, spNumber, spNumber, spNumber, targetCardNumber);

			if (CollectionUtils.isEmpty(cardDet)) {
				query = QueryConstants.GET_CARD_DETAILS + QueryConstants.GET_CARDBYINACTIVE + " AND CARD_NUM_HASH <> fn_hash(?)";
				cardDet = getJdbcTemplate().queryForList(query, spNumber, spNumber, spNumber, spNumber, targetCardNumber);

				if (CollectionUtils.isEmpty(cardDet)) {
					query = "Select * from (" + QueryConstants.GET_CARD_DETAILS + " AND CARD_NUM_HASH <> fn_hash(?)"
							+ QueryConstants.GET_CARDBYCLOSE;
					cardDet = getJdbcTemplate().queryForList(query, targetCardNumber, spNumber, spNumber, spNumber, spNumber);

				}
			}

		}

		if (!CollectionUtils.isEmpty(cardDet)) {
			cardDetails = cardDet.get(0);
		}
		return cardDetails;
	}

	@Override
	public String getCardNoByProductId(String productId) {
		return getJdbcTemplate().queryForObject(QueryConstants.CARD_NUM_BY_PRODUCTID, String.class, productId);
	}

	@Override
	public String getCardNoByRefNum(String rrn, String txnCode, String txnDate) {
		return getJdbcTemplate().queryForObject(QueryConstants.CARDNO_BY_RRN, String.class, rrn, txnCode, txnDate);
	}

	@Override
	public String getCardNumberByCustId(String customerId) {
		return getJdbcTemplate().queryForObject(QueryConstants.CARDNO_BY_CUSTOMERID, String.class, customerId);
	}

	@Override
	public void updateCardStatus(String spNumber, String cardStatusDigitalInProcess) {
		log.debug(LoggerConstants.ENTER);
		try {
			log.info("update Digital in process card status query: {}, parameters: cardStatus: {}, ",
					QueryConstants.UPDATE_CARD_STATUS_DIG_IN_PROCESS, cardStatusDigitalInProcess);
			getJdbcTemplate().update(QueryConstants.UPDATE_CARD_STATUS_DIG_IN_PROCESS, cardStatusDigitalInProcess, spNumber);
			getJdbcTemplate().update(QueryConstants.UPDATE_OLDCARD_STATUS_DIG_NULL, spNumber);
		} catch (DataAccessException e) {
			log.error("DataAccessException Occured while updating card status: " + e);
			throw new ServiceException("DataAccessException Occured while updating card status", ResponseCodes.SYSTEM_ERROR);
		} catch (Exception e) {
			log.error("Exception Occured while updating  card status: " + e);
			throw new ServiceException("Exception Occured while updating card status", ResponseCodes.SYSTEM_ERROR);
		}
		log.debug(LoggerConstants.EXIT);

	}

	@Override
	public CardDetail getCardDetails(SpNumberType spNumberType, String spNumber) {

		if (CodeUtil.isNotNull(spNumberType)) {
			List<CardDetail> cardDetails = getJdbcTemplate().query(getQueryForSpNumberType(spNumberType), this.cardDetailRowMapper,
					spNumber);
			return CollectionUtils.isEmpty(cardDetails) ? null : filterBasedOnCardStatus(cardDetails);
		} else {
			log.info("spNumerType is null");
			return getCardDetails(spNumber);
		}

	}

	private CardDetail filterBasedOnCardStatus(List<CardDetail> cardDetails) {
		if (cardDetails.size() == 1) {
			return cardDetails.get(0);
		}

		log.info("removing card details with card status Closed");
		Optional<CardDetail> cardDetailOptional = cardDetails.stream()
			.filter(cardDetail -> not(cardDetail.getCardStatus()
				.equals(CardStatusType.CLOSED.getStatusCode())))
			.findAny();

		log.info("removed {} records with cardStatus closed", cardDetails.size());
		return cardDetailOptional.isPresent() ? cardDetailOptional.get() : cardDetails.get(0);
	}

	private String getQueryForSpNumberType(SpNumberType spNumberType) {

		switch (spNumberType) {
		case CUSTOMER_ID:
			log.info("get card details by customer ID");
			return QueryConstants.GET_CARD_DETAILS_BY_CUSTOMER_ID;
		case ACCOUNT_NUMBER:
			log.info("get card details by accountNumber");
			return QueryConstants.GET_CARD_DETAILS + " AND a.account_number = ? ";
		case PROXY_NUMBER:
			log.info("get card details by proxyNumber");
			return QueryConstants.GET_CARD_DETAILS + " AND proxy_number = ? ";
		case SERIAL_NUMBER:
			log.info("get card details by serialNumber");
			return QueryConstants.GET_CARD_DETAILS + " AND serial_number = ? ";
		case CARD_NUMBER:
		default:
			log.info("get card details by cardNumberHash");
			return QueryConstants.GET_CARD_DETAILS + " AND CARD_NUM_HASH = fn_hash(?) ";
		}
	}

	private CardDetail getCardDetails(String spNumber) {
		List<CardDetail> cardDetails = null;

		// Search by card number encrypted
		String query = QueryConstants.GET_CARD_DETAILS + "AND CARD_NUM_HASH = fn_hash(?) ";
		cardDetails = getJdbcTemplate().query(query, this.cardDetailRowMapper, spNumber);
		if (not(CollectionUtils.isEmpty(cardDetails))) {
			return cardDetails.get(0);
		}

		// search by serial number, proxy number, account number and card status not 0, 9
		query = QueryConstants.GET_CARD_DETAILS + QueryConstants.GET_CARDBYACTIVE;
		cardDetails = getJdbcTemplate().query(query, this.cardDetailRowMapper, spNumber, spNumber, spNumber);
		if (not(CollectionUtils.isEmpty(cardDetails))) {
			return cardDetails.get(0);
		}

		// search by serial number, proxy number, account number and card status is 0
		query = QueryConstants.GET_CARD_DETAILS + QueryConstants.GET_CARDBYINACTIVE;
		cardDetails = getJdbcTemplate().query(query, this.cardDetailRowMapper, spNumber, spNumber, spNumber);
		if (not(CollectionUtils.isEmpty(cardDetails))) {
			return cardDetails.get(0);
		}

		// search by serial number, proxy number, account number and card status is 9
		query = "Select * from( " + QueryConstants.GET_CARD_DETAILS + QueryConstants.GET_CARDBYCLOSE;
		cardDetails = getJdbcTemplate().query(query, this.cardDetailRowMapper, spNumber, spNumber, spNumber);

		return CollectionUtils.isEmpty(cardDetails) ? null : cardDetails.get(0);

	}

	private RowMapper<CardDetail> getCardDetailRowMapper() {
		return (rs, rowNum) -> CardDetail.builder()
			.cardNumberHash(rs.getString("card_num_hash"))
			.cardNumber(rs.getString("card_number"))
			.serialNumber(rs.getString("serial_number"))
			.accountId(rs.getLong("account_id"))
			.customerCode(rs.getLong("customer_code"))
			.productId(rs.getLong("product_id"))
			.proxyNumber(rs.getString("proxy_number"))
			.cardStatus(rs.getString("card_status"))
			.expiryDate(rs.getString("expiry_date"))
			.cardNumberEncrypted(rs.getString("card_num_encr"))
			.lastFourDigit(rs.getString("last4digit"))
			.dateOfActication(DateTimeUtil.map(rs.getTimestamp("date_of_activation")))
			.firstTimeTopUp(rs.getString("firsttime_topup"))
			.starterCardFlag(rs.getString("startercard_flag"))
			.oldCardStatus(rs.getString("old_cardstat"))
			.lastTransactionDate(DateTimeUtil.map(rs.getTimestamp("last_txndate")))
			.isRedeemed(rs.getString("is_redeemed"))
			.profileCode(rs.getString("PRFL_CODE"))
			.digitalPin(rs.getString("digital_pin"))
			.issuerId(rs.getLong("issuer_id"))
			.partnerId(rs.getLong("partner_id"))
			.replFlag(rs.getObject("replflag", Integer.class))
			.accountNumber(rs.getString("account_number"))
			.dbSysDate(rs.getTimestamp("db_sysdate")
				.toLocalDateTime())
			.build();

	}

	@Override
	public Map<String, Object> getAccountPurseUsageDetails(String accountId, String purseId) {
		log.debug(LoggerConstants.ENTER);
		String query = "";
		Map<String, Object> accountUsageDetails = null;
		List<Map<String, Object>> accountDet = null;
		query = QueryConstants.GET_ACCOUNT_PURSE_USAGE_DETAILS;

		accountDet = getJdbcTemplate().queryForList(query, accountId, purseId);

		if (!CollectionUtils.isEmpty(accountDet)) {
			accountUsageDetails = accountDet.get(0);
		}
		log.debug(LoggerConstants.EXIT);
		return accountUsageDetails;
	}

	@Override
	public Map<String, Object> getCardNumberFromBalTransferDetails(String rrn, String targetCardNumberInRequest) {
		Map<String, Object> cardDetails = null;
		List<Map<String, Object>> cardDetailsList = null;
		String query = QueryConstants.CARDNO_FROM_BAL_TRANSFER_DETAILS;

		cardDetailsList = getJdbcTemplate().queryForList(query, rrn, targetCardNumberInRequest);

		if (!CollectionUtils.isEmpty(cardDetailsList)) {
			cardDetails = cardDetailsList.get(0);
		}

		return cardDetails;

	}

	@Override
	public int updateCard(String cardNumberHash, String newCardStatus, String oldCardStatus, LocalDateTime activationDate,
			Boolean firstTimeTopUp) {
		return getJdbcTemplate().update(QueryConstants.UPDATE_CARD, newCardStatus, Mapper.mapToYN(firstTimeTopUp),
				activationDate == null ? null : Timestamp.valueOf(activationDate), cardNumberHash, oldCardStatus);
	}

}
