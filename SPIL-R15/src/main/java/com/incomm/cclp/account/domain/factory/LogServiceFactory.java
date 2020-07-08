package com.incomm.cclp.account.domain.factory;

import static com.incomm.cclp.account.util.CodeUtil.hasOneElementOnly;
import static com.incomm.cclp.account.util.CodeUtil.isEmpty;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.account.application.command.AccountTransactionCommand;
import com.incomm.cclp.account.domain.model.AccountAggregateUpdate;
import com.incomm.cclp.account.domain.model.AccountPurseGroupUpdate;
import com.incomm.cclp.account.domain.model.AccountPurseUpdate;
import com.incomm.cclp.account.domain.model.AccountPurseUpdateNew;
import com.incomm.cclp.account.domain.model.CardEntity;
import com.incomm.cclp.account.domain.model.StatementsLogBuilderHelper;
import com.incomm.cclp.account.domain.model.TransactionLogBuilderHelper;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.dao.service.SequenceService;
import com.incomm.cclp.domain.StatementsLog;
import com.incomm.cclp.domain.StatementsLogPK;
import com.incomm.cclp.domain.TransactionLog;
import com.incomm.cclp.dto.RequestInfo;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.transaction.constants.GeneralConstants;
import com.incomm.cclp.transaction.constants.TransactionConstant;

@Service
public class LogServiceFactory {

	@Autowired
	private SequenceService sequenceService;

	public List<StatementsLog> getStatementLogList(RequestInfo req) {
		List<StatementsLog> stmtLog = new ArrayList<>();
		if (req.isFinancial()) {
			RequestInfo reqStatementLogInfo = req.getClone();
			double closingBalance = 0;
			double closingAvailBalance = 0;
			switch (req.getCreditDebitFlg()) {
			case TransactionConstant.DEBIT_CARD:
				closingBalance = req.getOpeningLedgerBalance() - req.getTxnAmount();
				closingAvailBalance = req.getOpeningAvailBalance() - req.getTxnAmount();
				break;
			case TransactionConstant.CREDIT_CARD:
				closingBalance = req.getOpeningLedgerBalance() + req.getTxnAmount();
				closingAvailBalance = req.getOpeningAvailBalance() + req.getTxnAmount();
				break;
			}
			if (req.getTxnAmount() > 0) {
				generateReqLogObj(reqStatementLogInfo, req.getOpeningLedgerBalance(), closingBalance, req.getTxnAmount(),
						req.getCreditDebitFlg(), GeneralConstants.NO, req.getTxnDesc(), req.getOpeningAvailBalance(), closingAvailBalance);
				stmtLog.add(getStatementLogObj(reqStatementLogInfo));
			}

			if ("A".equals(req.getProdFeeCondition())) {
				closingBalance = reqStatementLogInfo.getClosingBalance() - req.getProdFlatFeeAmount();
				closingAvailBalance = reqStatementLogInfo.getAvailClosingBalance() - req.getProdFlatFeeAmount();
				generateReqLogObj(reqStatementLogInfo, reqStatementLogInfo.getClosingBalance(), closingBalance, req.getProdFlatFeeAmount(),
						TransactionConstant.DEBIT_CARD, GeneralConstants.YES, req.getTxnDesc() + " Fee",
						reqStatementLogInfo.getAvailClosingBalance(), closingAvailBalance);
				stmtLog.add(getStatementLogObj(reqStatementLogInfo));

				closingBalance = reqStatementLogInfo.getClosingBalance() - req.getProdPercentFeeAmount();
				closingAvailBalance = reqStatementLogInfo.getAvailClosingBalance() - req.getProdPercentFeeAmount();
				generateReqLogObj(reqStatementLogInfo, reqStatementLogInfo.getClosingBalance(), closingBalance,
						req.getProdPercentFeeAmount(), TransactionConstant.DEBIT_CARD, GeneralConstants.YES,
						req.getTxnDesc() + " Percent Fee", reqStatementLogInfo.getAvailClosingBalance(), closingAvailBalance);
				stmtLog.add(getStatementLogObj(reqStatementLogInfo));

			} else if (req.getTxnFee() > 0) {
				closingBalance = reqStatementLogInfo.getClosingBalance() - req.getTxnFee();
				closingAvailBalance = reqStatementLogInfo.getAvailClosingBalance() - req.getTxnFee();
				generateReqLogObj(reqStatementLogInfo, reqStatementLogInfo.getClosingBalance(), closingBalance, req.getTxnFee(),
						TransactionConstant.DEBIT_CARD, GeneralConstants.YES, req.getTxnDesc() + " Fee",
						reqStatementLogInfo.getAvailClosingBalance(), closingAvailBalance);
				stmtLog.add(getStatementLogObj(reqStatementLogInfo));
			}
		}
		return stmtLog;
	}


	private void generateReqLogObj(RequestInfo req, double openingBal, double closingBal, double txnAmt, String creditDebitFlag,
			String feeFlag, String txnDesc, double openingAvailBalance, double closingAvailBalance) {
		req.setOpeningBalance(openingBal);
		req.setClosingBalance(closingBal);
		req.setTxnAmount(txnAmt);
		req.setFeeFlg(feeFlag);
		req.setCreditDebitFlg(creditDebitFlag);
		req.setMerchantCity(req.getStoreCity());
		req.setMerchantState(req.getStoreState());
		req.setTxnDesc(txnDesc);
		req.setAccountPurseId(req.getAccountPurseId());
		req.setAvailOpeningBalance(openingAvailBalance);
		req.setAvailClosingBalance(closingAvailBalance);
	}

	private StatementsLog getStatementLogObj(RequestInfo req) {
		SimpleDateFormat newFormat = new SimpleDateFormat("dd-MMM-yy");
		String finalString = newFormat.format(new Date());
		long time = System.currentTimeMillis();
		StatementsLog stmtLog = new StatementsLog();
		StatementsLogPK stmtLogPK = new StatementsLogPK();
		stmtLog.setCardNumHash(req.getCardNumHash());
		stmtLog.setCardNumEncr(req.getCardNumEncr());
		stmtLog.setOpeningBalance(req.getOpeningBalance());
		stmtLog.setClosingBalance(req.getClosingBalance());
		stmtLog.setTransactionAmount(req.getTxnAmount());
		stmtLog.setCreditDebitFlag(req.getCreditDebitFlg());
		stmtLog.setTransactionNarration(req.getTxnDesc());
		stmtLog.setLastUpdDate(finalString);
		stmtLog.setInsDate(finalString);
		stmtLog.setInsTimeStamp(new Timestamp(time));
		stmtLog.setRrn(req.getRrn());
		stmtLog.setAuthId(req.getAuthId());
		stmtLog.setTransactionDate(req.getTxnDate());
		stmtLog.setTransactionTime(req.getTxnTime());
		stmtLog.setFeeFlag(req.getFeeFlg());
		stmtLog.setDeliveryChannel(req.getChannel());
		stmtLog.setTransactionCode(req.getTxnCode());
		stmtLog.setAccountId(req.getAccountId());
		stmtLog.setToAccountId(req.getAccountId());
		stmtLog.setMerchantName(req.getMerchantName());
		stmtLog.setMerchantCity(req.getMerchantCity());
		stmtLog.setMerchantState(req.getMerchantState());
		stmtLog.setCardLast4digit(req.getCardLast4digits());
		stmtLog.setProductId(req.getProductId());
		stmtLogPK.setRecordSeq(sequenceService.getNextRecordSeqId()
			.toBigInteger());
		stmtLog.setPurseId(req.getPurseId());
		stmtLog.setToPurseId(req.getPurseId());
		stmtLogPK.setTransactionSqid(req.getTxnSeqId());
		stmtLog.setStatementsLogPK(stmtLogPK);
		stmtLog.setBusinessDate(cutOverTime(req.getValueDTO()
			.getProductAttributes()));
		stmtLog.setStoreId(req.getStoreId());
		stmtLog.setAccountPurseId(req.getAccountPurseId());
		stmtLog.setAvailOpenBal(req.getAvailOpeningBalance());
		stmtLog.setAvailcloseBal(req.getAvailClosingBalance());
		stmtLog.setSourceDescription("CCLP");
		return stmtLog;
	}

	public String cutOverTime(Map<String, Map<String, Object>> productAttributes) {
		try {
			Date date = Calendar.getInstance()
				.getTime();
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-YY");
			String cutOffDate = dateFormat.format(date);

			DateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyhh:mm:ss");
			Date cutOffDateTime = dateTimeFormat.parse(cutOffDate.concat(productAttributes.get("Product")
				.get("cutOverTime")
				.toString()));

			Date businessDate = new Date();
			date = dateTimeFormat.parse(dateTimeFormat.format(date));

			if (date.compareTo(cutOffDateTime) < 0)
				businessDate = cutOffDateTime;
			else {
				Calendar c = Calendar.getInstance();
				c.setTime(cutOffDateTime);
				c.add(Calendar.DATE, 1);
				businessDate = c.getTime();
			}

			return new SimpleDateFormat("dd-MMM-yy").format(businessDate)
				.toUpperCase();
		} catch (Exception e) {
			throw new ServiceException(SpilExceptionMessages.ERROR_PARSING_DATE);
		}
	}

	private String getBusinessDate(String cutOverTime) {
		try {

			if (cutOverTime == null) {
				return new SimpleDateFormat("dd-MMM-yy").format(new Date())
					.toUpperCase();
			}

			Date date = Calendar.getInstance()
				.getTime();
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-YY");
			String cutOffDate = dateFormat.format(date);

			DateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyhh:mm:ss");
			Date cutOffDateTime = dateTimeFormat.parse(cutOffDate.concat(cutOverTime));

			Date businessDate = new Date();
			date = dateTimeFormat.parse(dateTimeFormat.format(date));

			if (date.compareTo(cutOffDateTime) < 0)
				businessDate = cutOffDateTime;
			else {
				Calendar c = Calendar.getInstance();
				c.setTime(cutOffDateTime);
				c.add(Calendar.DATE, 1);
				businessDate = c.getTime();
			}

			return new SimpleDateFormat("dd-MMM-yy").format(businessDate)
				.toUpperCase();
		} catch (Exception e) {
			throw new ServiceException(SpilExceptionMessages.ERROR_PARSING_DATE);
		}
	}

	public TransactionLog getTransactionLog(AccountTransactionCommand command, CardEntity cardEntity,
			AccountAggregateUpdate accountAggregateUpdate, String cutOverTime) {

		List<AccountPurseUpdateNew> accountPurseUpdate = getAccountPurseUpdate(accountAggregateUpdate);

		BigDecimal transactionFee = accountAggregateUpdate.getTransactionFee() == null ? new BigDecimal(0)
				: accountAggregateUpdate.getTransactionFee();

		TransactionLogBuilderHelper builderHelper = TransactionLogBuilderHelper
			.from(sequenceService.getNextTxnSeqId(), sequenceService.getNextAuthSeqId())
			.command(command)
			.cardEntity(cardEntity)
			.accountPurseUpdates(accountPurseUpdate)
			.transactionFee(transactionFee)
			.businessDate(getBusinessDate(cutOverTime))
			.authorizedAmount(accountAggregateUpdate.getAuthorizedAmount())
			.response(ResponseCodes.SUCCESS, GeneralConstants.OK, "S");

		return builderHelper.build();
	}

	private List<AccountPurseUpdateNew> getAccountPurseUpdate(AccountAggregateUpdate accountAggregateUpdate) {
		if (isEmpty(accountAggregateUpdate.getAccountPurseGroupUpdate())) {
			return Collections.emptyList();
		}

		if (hasOneElementOnly(accountAggregateUpdate.getAccountPurseGroupUpdate())) {
			return accountAggregateUpdate.getAccountPurseGroupUpdate()
				.get(0)
				.getAccountPurseUpdates();
		}

		Optional<AccountPurseGroupUpdate> groupUpdates = getDefaultAccountPurseUpdate(accountAggregateUpdate.getAccountPurseGroupUpdate());

		if (groupUpdates.isPresent()) {
			return groupUpdates.get()
				.getAccountPurseUpdates();
		}

		return Collections.emptyList();

	}

	public Optional<AccountPurseGroupUpdate> getDefaultAccountPurseUpdate(List<AccountPurseGroupUpdate> update) {
		return update.stream()
			.filter(AccountPurseGroupUpdate::isDefault)
			.findFirst();
	}

	public List<StatementsLog> getStatementLog(AccountTransactionCommand command, CardEntity cardEntity,
			AccountAggregateUpdate accountAggregateUpdate, BigDecimal transactionLogId, String authSeqId) {
		List<StatementsLog> statementLogList = new ArrayList<>();

		StatementsLogBuilderHelper statementLogBuilderHelper = StatementsLogBuilderHelper.from()
			.accountTransactionCommand(command)
			.cardEntity(cardEntity);

		accountAggregateUpdate.getAccountPurseGroupUpdate()
			.forEach(accountGrpUpdate -> {
				accountGrpUpdate.getLedgerEntries()
					.forEach(ledgerEntry -> {
						statementLogList.add(statementLogBuilderHelper.accountPurseKey(ledgerEntry.getAccountPurseKey())
							.primaryKey(transactionLogId, authSeqId, sequenceService.getNextRecordSeqId())
							.statementLog(ledgerEntry, command)
							.build());

					});
			});

		return statementLogList;
	}
}
