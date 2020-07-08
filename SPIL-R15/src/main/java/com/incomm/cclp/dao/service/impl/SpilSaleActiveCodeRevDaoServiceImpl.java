package com.incomm.cclp.dao.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incomm.cclp.constants.CardStatusConstants;
import com.incomm.cclp.constants.MsgTypeConstants;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.dao.service.AccountPurseDAOService;
import com.incomm.cclp.dao.service.AuthChecksService;
import com.incomm.cclp.dao.service.LogService;
import com.incomm.cclp.dao.service.SequenceService;
import com.incomm.cclp.dao.service.SpilSaleActiveCodeRevDaoService;
import com.incomm.cclp.domain.AccountPurseInfo;
import com.incomm.cclp.domain.CardInfo;
import com.incomm.cclp.domain.StatementsLogInfo;
import com.incomm.cclp.domain.TransactionLogInfo;
import com.incomm.cclp.dto.RequestInfo;
import com.incomm.cclp.dto.ResponseInfo;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.repos.GenericRepo;
import com.incomm.cclp.transaction.constants.GeneralConstants;
import com.incomm.cclp.transaction.constants.TransactionConstant;

@Service
public class SpilSaleActiveCodeRevDaoServiceImpl implements SpilSaleActiveCodeRevDaoService {

	@Autowired
	private SequenceService sequenceService;

	@Autowired
	private GenericRepo genericRepo;

	@Autowired
	private LogService logService;

	@Autowired
	private AuthChecksService authService;
	@Autowired
	private AccountPurseDAOService accountPurseDaoService;

	private static final Logger logger = LogManager.getLogger(SpilSaleActiveCodeRevDaoServiceImpl.class);

	@Transactional
	@Override
	public ResponseInfo doSaleActiveCodeRev(RequestInfo req) {
		logger.info("doSaleActiveCodeRev START >>>");
		req.setTxnDesc("Sale Active Code Reversal");

		req.setTxnSeqId(sequenceService.getNextTxnSeqId());

		accountPurseDaoService.getAccountPurseDetails(req);

		TransactionLogInfo txnLog = genericRepo.getLastSuccessTxn(req.getChannel(), req.getCardNumHash(), req.getTxnCode(),
				ResponseCodes.SUCCESS, MsgTypeConstants.MSG_TYPE_NORMAL, req.getRrn(), req.getTxnDate());
		List<StatementsLogInfo> stmtLog = genericRepo.getLastFeeTransactions(req.getRrn(), TransactionConstant.DEBIT_CARD, req.getTxnDate(),
				req.getTxnCode());
		setOriginalValues(req, txnLog);

		if (GeneralConstants.YES.equalsIgnoreCase(txnLog.getTranReverseFlag())) {
			logger.error(SpilExceptionMessages.CARD_REVERSAL_ALREADY_DONE);
			throw new ServiceException(SpilExceptionMessages.CARD_REVERSAL_ALREADY_DONE, ResponseCodes.NOT_REVERSIBLE_OR_ALREADY_REVERSED);
		}

		authService.authorizeTransaction(req);

		CardInfo replacedCardDetails = genericRepo.getReplacedCardStatus(req.getAccountId(), CardStatusConstants.CLOSED);
		genericRepo.updateLastTxnDate(req.getAccountId(), req.getPurseId());
		req.setFirstTimeTopupFlag(txnLog.getProcessFlag());

		int count = genericRepo.updateActivationCardStatus(req.getCardNumHash(), null, req.getOldCardStatus());
		if (count == 0) {
			logger.error(SpilExceptionMessages.INVALID_CARD_STATE);
			throw new ServiceException(SpilExceptionMessages.INVALID_CARD_STATE, ResponseCodes.INVALID_CARD_STATE);
		}
		req.setCardStatus(TransactionConstant.PRINTER_SENT);
		if (req.isFirstTimeTopUp()) {
			doFirstTimeTopup(req);
		}

		if (CardStatusConstants.DAMAGE.equalsIgnoreCase(replacedCardDetails.getOldCardStatus())) {
			genericRepo.updateCurrentCardStatus(replacedCardDetails.getCardNumHash(), CardStatusConstants.DAMAGE);
		}

		req.setAuthId(sequenceService.getNextAuthSeqId());

		doStatementLog(req, stmtLog);

		logger.debug("LocationId: {}, MerchantId: {}", req.getLocationId(), req.getMerchantId());
		if (req.getLocationId() != null && req.getMerchantId() != null) {
			genericRepo.updateInventory(req.getProductId(), req.getMerchantId(), req.getLocationId());
		}

		req.setAuthorizedAmount(req.getClosingAvailBalance());

		genericRepo.updateTransactionReversalFlag(req.getCardNumHash(), req.getTxnCode(), req.getRrn(), "Y");

		doTransactionLogs(req);

		logger.info("doSaleActiveCodeRev END <<<");

		return ResponseInfo.builder()
			.respCode(ResponseCodes.SUCCESS)
			.errMsg(GeneralConstants.OK)
			.authId(req.getAuthId())
			.authorizedAmt(req.getAuthorizedAmount())
			.cardStatus(req.getCardStatus())
			.serialNumber(new BigDecimal(req.getSerialNumber()))
			.digitalPin(req.getDigitalPin())
			.currCode(req.getTranCurr())
			.cardNumber(req.getCardNum())
			.build();
	}

	public void doFirstTimeTopup(RequestInfo req) {
		logger.info("doFirstTimeTopup START >>>");
		genericRepo.updateTopup(req.getCardNumHash(), GeneralConstants.NO);
		genericRepo.updateInitialLoadBalance(req.getAccountId(), 0.0, 0.0);
		genericRepo.updateFirstLoadDatetoNull(req.getAccountId());
		logger.info("doFirstTimeTopup END <<<");
	}

	public void doStatementLog(RequestInfo req, List<StatementsLogInfo> stmtLog) {
		logger.info("START >>>");
		if (req.isFinancial()) {
			RequestInfo reqStatementLogInfo = req.getClone();
			reqStatementLogInfo.setClosingBalance(req.getOpeningLedgerBalance());
			double closing_balance = 0;
			for (StatementsLogInfo stmt : stmtLog) {
				closing_balance = reqStatementLogInfo.getClosingBalance() + stmt.getTxnAmount();
				String txnDesc = stmt.getTxnDesc()
					.split("Sale Active Code")[1];
				generateStatementLogObj(reqStatementLogInfo, reqStatementLogInfo.getClosingBalance(), closing_balance, stmt.getTxnAmount(),
						TransactionConstant.CREDIT_CARD, GeneralConstants.YES, "Sale Active Code" + " " + txnDesc + " " + "Reversal");
				logService.doStatementsLog(reqStatementLogInfo);
			}

			if (req.getTxnAmount() > 0) {
				switch (req.getCreditDebitFlg()) {
				case TransactionConstant.DEBIT_CARD:
					closing_balance = reqStatementLogInfo.getClosingBalance() - req.getTxnAmount();
					break;
				case TransactionConstant.CREDIT_CARD:
					closing_balance = reqStatementLogInfo.getClosingBalance() + req.getTxnAmount();
					break;
				}
				generateStatementLogObj(reqStatementLogInfo, reqStatementLogInfo.getClosingBalance(), closing_balance, req.getTxnAmount(),
						req.getCreditDebitFlg(), GeneralConstants.NO, "Sale Active Code Reversal");
				logService.doStatementsLog(reqStatementLogInfo);
			}
		}
		logger.info("END <<<");
	}

	public void doTransactionLogs(RequestInfo req) {
		logger.info("START >>>");
		RequestInfo reqTransactionLogInfo = req.getClone();

		String cardStatus = CardStatusConstants.CONSUMED.equalsIgnoreCase(req.getOldCardStatus()) ? CardStatusConstants.INACTIVE
				: req.getOldCardStatus();
		generateTransactionLog(reqTransactionLogInfo, req.getTxnCode(), req.getTxnType(), req.getCreditDebitFlg(),
				"Sale Active Code Reversal", req.getPartialPreAuthInd(), req.getOpeningLedgerBalance(), req.getOpeningAvailBalance(),
				cardStatus);
		logService.doTransactionLog(reqTransactionLogInfo);

		if (CardStatusConstants.CONSUMED.equalsIgnoreCase(req.getOldCardStatus())) {
			reqTransactionLogInfo.setTxnAmount(0.0);
			reqTransactionLogInfo.setAuthorizedAmount(0.0);
			generateTransactionLog(reqTransactionLogInfo, req.getTxnCode(), req.getTxnType(), req.getCreditDebitFlg(),
					"Sale Active Code Reversal", req.getPartialPreAuthInd(), req.getOpeningLedgerBalance(), req.getOpeningAvailBalance(),
					req.getOldCardStatus());
			logService.doTransactionLog(reqTransactionLogInfo);
		}
		logger.info("END <<<");
	}

	public void generateStatementLogObj(RequestInfo req, double openingBal, double closingBal, double txnAmt, String cr_dr_flag,
			String feeFlag, String txnDesc) {
		logger.info("generateStatementLogObj START >>>");
		req.setOpeningBalance(openingBal);
		req.setClosingBalance(closingBal);
		req.setTxnAmount(txnAmt);
		req.setFeeFlg(feeFlag);
		req.setCreditDebitFlg(cr_dr_flag);
		req.setMerchantCity(req.getStoreCity());
		req.setMerchantState(req.getStoreState());
		req.setTxnDesc(txnDesc);
		logger.info("generateStatementLogObj END <<<");
	}

	public void generateTransactionLog(RequestInfo req, String transaction_code, String is_financial, String cr_dr_flag,
			String transaction_desc, String partial_preauth_ind, double opening_ledger_balance, double opening_available_balance,
			String cardStatus) {
		logger.info("generateTransactionLog START >>>");
		req.setCardStatus(cardStatus);
		req.setTxnCode(transaction_code);
		req.setTxnType(is_financial);
		req.setCreditDebitFlg(cr_dr_flag);
		req.setTxnDesc(transaction_desc);
		req.setPartialPreAuthInd(partial_preauth_ind);
		req.setOpeningLedgerBalance(opening_ledger_balance);
		req.setOpeningAvailBalance(opening_available_balance);
		req.setTxnReversalFlag(GeneralConstants.YES);
		req.setTxnFee(0);
		req.setReversalCode("0");
		logger.info("generateTransactionLog END <<<");
	}

	public void getAccountPurseDetails(RequestInfo req) {
		logger.info("getAccountPurseDetails START >>>");
		AccountPurseInfo purseInfo = genericRepo.getAccountPurse(req.getProductId(), req.getAccountId(), req.getTranCurr(),
				req.getCardNumHash());
		req.setOpeningBalance(purseInfo.getAvlbl());
		req.setOpeningAvailBalance(purseInfo.getAvlbl());
		req.setOpeningLedgerBalance(purseInfo.getLedbl());
		req.setPurseId(purseInfo.getPurseId());
		// req.setCurrCode(purseInfo.getCurrencyCode());
		logger.info("getAccountPurseDetails END <<<");
	}

	public void setOriginalValues(RequestInfo req, TransactionLogInfo txnLog) {
		logger.info("setOriginalValues START >>>");
		req.setTxnAmount(txnLog.getTransactionAmount());
		req.setOrgnlTxnAmt(txnLog.getTransactionAmount());
		req.setOrgnAuthAmt(txnLog.getAuthAmount());
		req.setOrgnlTxnFee(txnLog.getTranfeeAmount());
		req.setTxnFee(txnLog.getTranfeeAmount());
		req.setOrgnlCardNumHash(req.getCardNumHash());
		req.setOrgnlRrn(req.getRrn());
		req.setOrgnlTxnDate(txnLog.getTransactionDate());
		req.setOrgnlTxnTime(txnLog.getTransactionTime());
		req.setOrgnlTerminalId(txnLog.getTerminalId());
		logger.info("setOriginalValues END <<<");
	}
}
