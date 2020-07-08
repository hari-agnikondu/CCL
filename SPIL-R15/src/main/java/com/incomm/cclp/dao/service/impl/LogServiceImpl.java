package com.incomm.cclp.dao.service.impl;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incomm.cclp.account.util.DateTimeUtil;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.dao.service.LogService;
import com.incomm.cclp.dao.service.SequenceService;
import com.incomm.cclp.domain.AccountPurseBalance;
import com.incomm.cclp.domain.StatementsLog;
import com.incomm.cclp.domain.StatementsLogInfo;
import com.incomm.cclp.domain.StatementsLogPK;
import com.incomm.cclp.domain.TransactionLog;
import com.incomm.cclp.dto.RequestInfo;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.repos.StatementsLogRepository;
import com.incomm.cclp.repos.TransactionLogRepository;
import com.incomm.cclp.transaction.constants.GeneralConstants;
import com.incomm.cclp.transaction.constants.TransactionConstant;

@Service
public class LogServiceImpl implements LogService {

	private static final Logger logger = LogManager.getLogger(LogServiceImpl.class);

	@Autowired
	private SequenceService sequenceService;

	@Autowired
	private StatementsLogRepository stmtsLogRepo;

	@Autowired
	private TransactionLogRepository txnLogRepo;

	@Transactional
	@Override
	public void doStatementsLog(RequestInfo req) {
		logger.info("doStatementsLog START >>>");
		SimpleDateFormat newFormat = new SimpleDateFormat("dd-MMM-yy");
		String finalString = newFormat.format(new Date());
		try {
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
			stmtLog.setStoreId(req.getStoreDbId());
			stmtLog.setAccountPurseId(req.getAccountPurseId());
			stmtLog.setAvailOpenBal(req.getAvailOpeningBalance());
			stmtLog.setAvailcloseBal(req.getAvailClosingBalance());
			stmtLog.setSourceDescription("CCLP");
			logger.info("statement Log {}", stmtLog);
			stmtsLogRepo.save(stmtLog);
			logger.info("doStatementsLog END <<<");
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_STATEMENT_LOG_INSERT + " " + e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_STATEMENT_LOG_INSERT, ResponseCodes.SYSTEM_ERROR);
		}
	}

	@Transactional
	@Override
	public void doTransactionLog(RequestInfo req) {
		logger.info("doTransactionLog START >>>");

		try {
			SimpleDateFormat newFormat = new SimpleDateFormat("DD/MMM/YYHH:mm:ss");
			String finalString = newFormat.format(new Date());

			long time = System.currentTimeMillis();
			LocalDateTime currentDateTime = LocalDateTime.now();
			TransactionLog txnLog = new TransactionLog(req.getTxnSeqId(), req.getChannel(), req.getTxnCode(), req.getRrn(),
					req.getMdmId() == null ? null
							: req.getMdmId()
								.toString());
			txnLog.setProductId(req.getProductId());
			txnLog.setDeliveryChannel(req.getChannel());
			txnLog.setTransactionCode(req.getTxnCode());
			txnLog.setMsgType(req.getMsgType());
			txnLog.setIsFinancial(req.getTxnType());
			txnLog.setCrDrFlag(req.getCreditDebitFlg());
			txnLog.setTransactionDesc(req.getTxnDesc());
			txnLog.setResponseId(req.getRespCode());
			txnLog.setErrorMsg(req.getErrorMsg());
			String txnStatus = "OK".equalsIgnoreCase(req.getErrorMsg()) ? "C" : "F";
			txnLog.setTransactionStatus(txnStatus);
			txnLog.setTerminalId(req.getTerminalId());
			txnLog.setRrn(req.getRrn());
			txnLog.setCardNumber(req.getCardNumHash());
			txnLog.setCustomerCardNbrEncr(req.getCardNumEncr());
			txnLog.setProxyNumber(req.getProxyNumber());
			txnLog.setTransactionDate(req.getTxnDate());
			txnLog.setTransactionTime(req.getTxnTime());
			txnLog.setTransactionTimezone(req.getTxnTimeZone());
			txnLog.setPartialPreauthInd(req.getPartialPreAuthInd());
			txnLog.setAuthId(req.getAuthId());
			txnLog.setTransactionAmount(req.getTxnAmount());
			txnLog.setAuthAmount(req.getTotalAmount());
			txnLog.setTranfeeAmount(req.getTxnFee());
			txnLog.setTranReverseFlag(req.getTxnReversalFlag());
			txnLog.setReversalCode(req.getReversalCode());
			txnLog.setLastUpdDate(DateTimeUtil.map(currentDateTime));
			txnLog.setOrgnlTranCurr(req.getOrgnlTranCurr());
			txnLog.setLedgerBalance(req.getClosingLedgerBalance());
			txnLog.setAccountBalance(req.getClosingAvailBalance());
			txnLog.setOpeningLedgerBalance(req.getOpeningLedgerBalance());
			txnLog.setOpeningAvailableBalance(req.getOpeningAvailBalance());
			txnLog.setCardStatus(req.getCardStatus());
			txnLog.setCountryCode(req.getLocaleCountry());
			txnLog.setMerchantName(req.getMerchantName());
			txnLog.setMdmId(req.getMdmId()
				.toString());
			txnLog.setStoreDbId(req.getStoreDbId());
			txnLog.setStoreAddress1(req.getStoreAddress1());
			txnLog.setStoreAddress2(req.getStoreAddress2());
			txnLog.setStoreCity(req.getStoreCity());
			txnLog.setStoreState(req.getStoreState());
			txnLog.setSpilFee(req.getFeeAmount());
			txnLog.setSpilUpc(req.getUpc());
			txnLog.setSpilMerrefNum(req.getMerchantRefNum());
			txnLog.setSpilLocCntry(req.getLocaleCountry());
			txnLog.setSpilLocCrcy(req.getLocaleCurrency());
			txnLog.setSpilLocLang(req.getLocaleLanguage());
			txnLog.setSpilPosEntry(req.getPosEntryMode());
			txnLog.setSpilPosCond(req.getPosConditionCode());
			txnLog.setPartnerId(req.getPartnerId());
			txnLog.setIssuerId(req.getIssuerId());
			txnLog.setCashierId(req.getCashierId());
			txnLog.setExpirationDate(req.getExpiryDate());
			txnLog.setAccountId(req.getAccountId());
			txnLog.setAccountNumber(req.getAccountNumber());
			txnLog.setPurseId(req.getPurseId());
			txnLog.setOrgnlCardNbr(req.getOrgnlCardNumHash());
			txnLog.setOrgnlRrn(req.getOrgnlRrn());
			txnLog.setOrgnlTransactionDate(req.getOrgnlTxnDate());
			txnLog.setOrgnlTransactionTime(req.getOrgnlTxnTime());
			txnLog.setOrgnlTerminalId(req.getOrgnlTerminalId());
			txnLog.setMaxFeeFlag(req.getMaxFeeFlg());
			txnLog.setFreeFeeFlag(req.getFreeFeeFlg());
			txnLog.setOrgnlTranAmt(req.getReqTxnAmt());
			txnLog.setOrgnlAuthAmount(req.getReqAuthAmt());
			txnLog.setTranCurr(req.getTranCurr());
			txnLog.setConvRate(req.getConvRate());
			txnLog.setSourceName(req.getSourceName());
			txnLog.setAccountPurseId(req.getAccountPurseId());
			txnLog.setPurseCurrency(req.getPurseType());

			if (req.isExtPurse()) {
				AccountPurseBalance purse = req.getPurAuthReq();
				txnLog.setAccountPurseId(purse.getAccountPurseId());
				txnLog.setPurseName(req.getPurseName());
				txnLog.setPurseTxnAmount(req.getTxnAmount());
				txnLog.setPurseCurrency(purse.getPurseType());
				txnLog.setSkuCode(purse.getSkuCode());
			}

			if (req.getBusinessDate() == null) {
				txnLog.setBusinessDate(cutOverTime(req.getValueDTO()
					.getProductAttributes()));
			}

			txnLog.setInsDate(DateTimeUtil.map(currentDateTime));
			txnLog.setInsTimeStamp(new Timestamp(time));
			txnLog.setProcessFlag(req.getFirstTimeTopupFlag());
			txnLog.setSpilStoreDbId(req.getStoreDbId());
			txnLog.setStoreId(req.getStoreId());
			txnLogRepo.save(txnLog);
			logger.info("doTransactionLog END <<<");
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_TRANSACTION_LOG_INSERT + " " + e.getMessage());
			throw new ServiceException(SpilExceptionMessages.ERROR_TRANSACTION_LOG_INSERT);
		}
	}

	public String cutOverTime(Map<String, Map<String, Object>> productAttributes) {
		try {
			Date date = Calendar.getInstance()
				.getTime();
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-YY");
			String cutOffDate = dateFormat.format(date);

			DateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyhh:mm:ss");
			Object cutOverTime = productAttributes != null && productAttributes.get("Product") != null ? productAttributes.get("Product")
				.get("cutOverTime") : null;
			if (cutOverTime == null) {
				logger.error("Cut over time is not configured");
				throw new ServiceException(SpilExceptionMessages.ERROR_STATEMENT_LOG_INSERT, ResponseCodes.SYSTEM_ERROR);
			}
			Date cutOffDateTime = dateTimeFormat.parse(cutOffDate.concat(cutOverTime.toString()));

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
			logger.error(e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_PARSING_DATE, ResponseCodes.SYSTEM_ERROR);
		}

	}

	@Override
	public void addReversalStatementlog(RequestInfo req, List<StatementsLogInfo> stmtLog) {
		RequestInfo reqStatementLogInfo = req.getClone();
		reqStatementLogInfo.setClosingBalance(req.getOpeningLedgerBalance());
		reqStatementLogInfo.setAvailClosingBalance(req.getOpeningAvailBalance());
		double closingBalance = 0;
		double avialClosingBalance = 0;
		if (req.isFinancial()) {
			if (stmtLog != null && stmtLog.size() > 0.) {
				for (StatementsLogInfo stmt : stmtLog) {
					closingBalance = reqStatementLogInfo.getClosingBalance() + stmt.getTxnAmount();
					avialClosingBalance = reqStatementLogInfo.getAvailClosingBalance() + stmt.getTxnAmount();
					// avialOpenBalance=reqStatementLogInfo.getOpeningAvailBalance();

					generateStatementLogObj(reqStatementLogInfo, reqStatementLogInfo.getClosingBalance(), closingBalance,
							stmt.getTxnAmount(), TransactionConstant.CREDIT_CARD, GeneralConstants.YES,
							stmt.getTxnDesc() + " " + "Reversal", avialClosingBalance, reqStatementLogInfo.getOpeningAvailBalance());// check
					doStatementsLog(reqStatementLogInfo);
				}
			}
			if (req.getTxnAmount() > 0) {
				switch (req.getCreditDebitFlg()) {
				case TransactionConstant.DEBIT_CARD:
					closingBalance = reqStatementLogInfo.getClosingBalance() - req.getTxnAmount();
					avialClosingBalance = reqStatementLogInfo.getAvailClosingBalance() - req.getTxnAmount();

					break;
				case TransactionConstant.CREDIT_CARD:
					closingBalance = reqStatementLogInfo.getClosingBalance() + req.getTxnAmount();
					avialClosingBalance = reqStatementLogInfo.getAvailClosingBalance() + req.getTxnAmount();

					break;
				}
				generateStatementLogObj(reqStatementLogInfo, reqStatementLogInfo.getClosingBalance(), closingBalance, req.getTxnAmount(),
						req.getCreditDebitFlg(), GeneralConstants.NO, req.getTxnDesc(), avialClosingBalance,
						reqStatementLogInfo.getAvailClosingBalance());// check
				doStatementsLog(reqStatementLogInfo);
			}
		}

	}

	@Override
	public void addStatementLog(RequestInfo req) {

		if (req.isFinancial()) {
			RequestInfo reqStatementLogInfo = req.getClone();
			double closingBalance = 0;
			double avialClosingBalance = 0;

			switch (req.getCreditDebitFlg()) {
			case TransactionConstant.DEBIT_CARD:
				closingBalance = req.getOpeningLedgerBalance() - req.getTxnAmount();
				avialClosingBalance = req.getOpeningAvailBalance() - req.getTxnAmount();
				// avialOpenBalance=req.getOpeningAvailBalance();
				break;
			case TransactionConstant.CREDIT_CARD:
				closingBalance = req.getOpeningLedgerBalance() + req.getTxnAmount();
				avialClosingBalance = req.getOpeningAvailBalance() + req.getTxnAmount();
				// avialOpenBalance=req.getOpeningAvailBalance();
				break;
			}

			if (req.getTxnAmount() > 0) {
				generateStatementLogObj(reqStatementLogInfo, req.getOpeningLedgerBalance(), closingBalance, req.getTxnAmount(),
						req.getCreditDebitFlg(), GeneralConstants.NO, req.getTxnDesc(), avialClosingBalance, req.getOpeningAvailBalance());
				doStatementsLog(reqStatementLogInfo);
			}

			if ("A".equals(req.getProdFeeCondition())) {
				// Log flat fee amount
				closingBalance = reqStatementLogInfo.getClosingBalance() - req.getProdFlatFeeAmount();
				avialClosingBalance = reqStatementLogInfo.getAvailClosingBalance() - req.getProdFlatFeeAmount();

				generateStatementLogObj(reqStatementLogInfo, reqStatementLogInfo.getClosingBalance(), closingBalance,
						req.getProdFlatFeeAmount(), TransactionConstant.DEBIT_CARD, GeneralConstants.YES, req.getTxnDesc() + " Fee",
						avialClosingBalance, reqStatementLogInfo.getAvailClosingBalance());
				doStatementsLog(reqStatementLogInfo);

				// Log percent fee amount
				closingBalance = reqStatementLogInfo.getClosingBalance() - req.getProdPercentFeeAmount();
				avialClosingBalance = reqStatementLogInfo.getAvailClosingBalance() - req.getProdPercentFeeAmount();
				generateStatementLogObj(reqStatementLogInfo, reqStatementLogInfo.getClosingBalance(), closingBalance,
						req.getProdPercentFeeAmount(), TransactionConstant.DEBIT_CARD, GeneralConstants.YES, req.getTxnDesc() + " Fee",
						avialClosingBalance, reqStatementLogInfo.getAvailClosingBalance());
				doStatementsLog(reqStatementLogInfo);

			} else if (req.getTxnFee() > 0) {
				closingBalance = reqStatementLogInfo.getClosingBalance() - req.getTxnFee();
				avialClosingBalance = reqStatementLogInfo.getAvailClosingBalance() - req.getTxnFee();
				generateStatementLogObj(reqStatementLogInfo, reqStatementLogInfo.getClosingBalance(), closingBalance, req.getTxnFee(),
						TransactionConstant.DEBIT_CARD, GeneralConstants.YES, req.getTxnDesc() + " Fee", avialClosingBalance,
						reqStatementLogInfo.getAvailClosingBalance());
				doStatementsLog(reqStatementLogInfo);
			}
		}

	}

	public void generateStatementLogObj(RequestInfo req, double openingBal, double closingBal, double txnAmt, String creditDebitFlag,
			String feeFlag, String txnDesc, double availClosingBalance, double availOpeningBalance) {

		req.setOpeningBalance(openingBal);
		req.setClosingBalance(closingBal);
		req.setTxnAmount(txnAmt);
		req.setFeeFlg(feeFlag);
		req.setCreditDebitFlg(creditDebitFlag);
		req.setMerchantCity(req.getStoreCity());
		req.setMerchantState(req.getStoreState());
		req.setTxnDesc(txnDesc);
		req.setAvailClosingBalance(availClosingBalance);
		req.setAvailOpeningBalance(availOpeningBalance);
	}
}
