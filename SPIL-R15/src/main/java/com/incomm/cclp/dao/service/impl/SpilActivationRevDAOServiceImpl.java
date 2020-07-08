package com.incomm.cclp.dao.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

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
import com.incomm.cclp.dao.service.SpilActivationRevDAOService;
import com.incomm.cclp.domain.AccountPurseBalance;
import com.incomm.cclp.domain.CardInfo;
import com.incomm.cclp.domain.StatementsLogInfo;
import com.incomm.cclp.domain.TransactionLog;
import com.incomm.cclp.domain.TransactionLogInfo;
import com.incomm.cclp.dto.AccountPurseDTO;
import com.incomm.cclp.dto.RequestInfo;
import com.incomm.cclp.dto.ResponseInfo;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.repos.GenericRepo;
import com.incomm.cclp.transaction.constants.GeneralConstants;
import com.incomm.cclp.transaction.constants.TransactionConstant;

@Service
public class SpilActivationRevDAOServiceImpl implements SpilActivationRevDAOService {

	@Autowired
	private SequenceService sequenceService;

	@Autowired
	private GenericRepo genericRepo;

	@Autowired
	private LogService logService;

	@Autowired
	private AuthChecksService authService;

	@Autowired
	AccountPurseDAOService accountPurseService;

	private static final Logger logger = LogManager.getLogger(SpilActivationRevDAOServiceImpl.class);

	@Transactional
	@Override
	public ResponseInfo doActivationRev(RequestInfo req) {
		logger.info("doActivationRev START >>>");
		req.setTxnDesc(TransactionConstant.ACTIVATION_REVERSAL_TXN_DESC);

		req.setTxnSeqId(sequenceService.getNextTxnSeqId());

		TransactionLogInfo txnLog = getTransactionLogDetails(req);

		accountPurseService.getAccountPurseDetails(req);

//		TransactionLogInfo txnLog = genericRepo.getLastSuccessPurseTxn(req.getChannel(), req.getCardNumHash(), req.getTxnCode(),
//				ResponseCodes.SUCCESS, MsgTypeConstants.MSG_TYPE_NORMAL, req.getRrn(), req.getTxnDate(),req.getPurseId().longValue());
		List<StatementsLogInfo> stmtLog = genericRepo.getLastFeeTransactions(req.getRrn(), TransactionConstant.DEBIT_CARD, req.getTxnDate(),
				req.getTxnCode());
		setOriginalValues(req, txnLog);

		if (GeneralConstants.YES.equalsIgnoreCase(txnLog.getTranReverseFlag())) {
			logger.error(SpilExceptionMessages.CARD_REVERSAL_ALREADY_DONE);
			throw new ServiceException(SpilExceptionMessages.CARD_REVERSAL_ALREADY_DONE, ResponseCodes.NOT_REVERSIBLE_OR_ALREADY_REVERSED);
		}

		authService.authorizeTransaction(req);
		genericRepo.updateLastTxnDate(req.getAccountId(), req.getPurseId());
		CardInfo replacedCardDetails = genericRepo.getReplacedCardStatus(req.getAccountId(), CardStatusConstants.CLOSED);

		req.setFirstTimeTopupFlag(txnLog.getProcessFlag());

		int count = genericRepo.updateActivationDateCardStatus(req.getCardNumHash(), req.getOldCardStatus());
		if (count == 0) {
			logger.error(SpilExceptionMessages.INVALID_CARD_STATE);
			throw new ServiceException(SpilExceptionMessages.INVALID_CARD_STATE, ResponseCodes.INVALID_CARD_STATE);
		}

		if (req.isFirstTimeTopUp()) {
			doFirstTimeTopup(req);
		}

		if (CardStatusConstants.DAMAGE.equalsIgnoreCase(replacedCardDetails.getOldCardStatus())) {
			genericRepo.updateCurrentCardStatus(replacedCardDetails.getCardNumHash(), CardStatusConstants.DAMAGE);
		}

		req.setAuthId(sequenceService.getNextAuthSeqId());

		logService.addReversalStatementlog(req, stmtLog);

		Map<String, String> inventoryDetails = genericRepo.getLocationMerchanntID(req.getCardNumHash());
		req.setLocationId(inventoryDetails.get("LOCATION_ID"));
		req.setMerchantId(inventoryDetails.get("MERCHANT_ID"));
		logger.debug("LocationId: {}, MerchantId: {}", req.getLocationId(), req.getMerchantId());
		if (req.getLocationId() != null && req.getMerchantId() != null) {
			genericRepo.updateInventory(req.getProductId(), req.getMerchantId(), req.getLocationId());
		}

		genericRepo.updateTransactionReversalFlag(req.getCardNumHash(), req.getTxnCode(), req.getRrn(), "Y");

		doTransactionLogs(req);

		AccountPurseDTO purAuthResp = req.getValueDTO()
			.getAccountPurseDto();
		if (purAuthResp != null) {
			purAuthResp.setAuthAmount(BigDecimal.valueOf(req.getClosingAvailBalance()));
			purAuthResp.setAccountPurseId(req.getAccountPurseId());
		}
		logger.info("doActivationRev END <<<");

		return ResponseInfo.builder()
			.respCode(ResponseCodes.SUCCESS)
			.errMsg(GeneralConstants.OK)
			.authId(req.getAuthId())
			.authorizedAmt(req.getAuthorizedAmount())
			.accountPurse(purAuthResp)
			.build();
	}

	public void doFirstTimeTopup(RequestInfo req) {
		logger.info("doFirstTimeTopup START >>>");
		genericRepo.updateTopup(req.getCardNumHash(), GeneralConstants.NO);
		genericRepo.updateInitialLoadBalance(req.getAccountId());
		genericRepo.updateFirstLoadDatetoNull(req.getAccountId());
		logger.info("doFirstTimeTopup END <<<");
	}

	public void doTransactionLogs(RequestInfo req) {
		logger.info("doTransactionLogs START >>>");
		RequestInfo reqTransactionLogInfo = req.getClone();

		String cardStatus = CardStatusConstants.CONSUMED.equalsIgnoreCase(req.getOldCardStatus()) ? CardStatusConstants.INACTIVE
				: req.getOldCardStatus();
		generateTransactionLog(reqTransactionLogInfo, req.getTxnCode(), req.getTxnType(), req.getCreditDebitFlg(), req.getTxnDesc(),
				req.getPartialPreAuthInd(), req.getOpeningLedgerBalance(), req.getOpeningAvailBalance(), cardStatus);
		logService.doTransactionLog(reqTransactionLogInfo);

		if (CardStatusConstants.CONSUMED.equalsIgnoreCase(req.getOldCardStatus())) {
			reqTransactionLogInfo.setTxnAmount(0.0);
			reqTransactionLogInfo.setAuthorizedAmount(0.0);
			generateTransactionLog(reqTransactionLogInfo, req.getTxnCode(), req.getTxnType(), req.getCreditDebitFlg(), req.getTxnDesc(),
					req.getPartialPreAuthInd(), req.getOpeningLedgerBalance(), req.getOpeningAvailBalance(), req.getOldCardStatus());
			logService.doTransactionLog(reqTransactionLogInfo);
		}
		logger.info("doTransactionLogs END <<<");
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

	public void setOriginalValues(RequestInfo req, TransactionLogInfo txnLog) {
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
	}

	public TransactionLogInfo getTransactionLogDetails(RequestInfo req) {
		TransactionLogInfo txnLog = null;

		if (req.isExtPurse()) {
			txnLog = genericRepo.getLastSuccessPurseTxn(req.getChannel(), req.getCardNumHash(), req.getTxnCode(), ResponseCodes.SUCCESS,
					MsgTypeConstants.MSG_TYPE_NORMAL, req.getRrn(), req.getTxnDate(), req.getPurseId()
						.longValue());
		} else {
			TransactionLog txn = genericRepo.getAllLastSuccessPurseTxn(req.getChannel(), req.getCardNumHash(), req.getTxnCode(),
					ResponseCodes.SUCCESS, MsgTypeConstants.MSG_TYPE_NORMAL, req.getRrn(), req.getTxnDate(), req.getPurseId()
						.longValue());

			if (txn == null) {
				txnLog = genericRepo.getLastSuccessPurseTxn(req.getChannel(), req.getCardNumHash(), req.getTxnCode(), ResponseCodes.SUCCESS,
						MsgTypeConstants.MSG_TYPE_NORMAL, req.getRrn(), req.getTxnDate(), req.getPurseId()
							.longValue());
			} else {
				txnLog = new TransactionLogInfo();
				txnLog.setTransactionAmount(txn.getTransactionAmount());
				txnLog.setTranfeeAmount(txn.getTranfeeAmount());
				txnLog.setTranReverseFlag(txn.getTranReverseFlag());
				txnLog.setProcessFlag(txn.getProcessFlag());
				txnLog.setTransactionDate(txn.getTransactionDate());
				txnLog.setTransactionTime(txn.getTransactionTime());
				txnLog.setTerminalId(txn.getTerminalId());
				txnLog.setOriginalRrn(txn.getOrgnlRrn());
				txnLog.setAccountPurseId(BigInteger.valueOf(txn.getAccountPurseId()));
				txnLog.setAuthAmount(txn.getAuthAmount());
				txnLog.setClosingAvailableBalance(txn.getAccountBalance());
				txnLog.setOpeningLedgerBalance(txn.getOpeningLedgerBalance());
				txnLog.setOpeningAvailableBalance(txn.getOpeningAvailableBalance());
				txnLog.setClosingLedgerBalance(txn.getLedgerBalance());

				AccountPurseBalance purse = AccountPurseBalance.builder()
					.accountId(req.getAccountId()
						.longValue())
					.purseId(txn.getPurseId()
						.longValue())
					.productId(req.getProductId()
						.longValue())
					.effectiveDate(null)
					.expiryDate(null)
					.purseType(txn.getPurseCurrency())
					.skuCode(txn.getSkuCode())
					.build();

				req.setPurAuthReq(purse);
				req.setPurseId(txn.getPurseId());
			}
		}

		return txnLog;
	}
}
