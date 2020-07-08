package com.incomm.cclp.dao.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.CardStatusConstants;
import com.incomm.cclp.constants.MsgTypeConstants;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.dao.service.AccountPurseDAOService;
import com.incomm.cclp.dao.service.AuthChecksService;
import com.incomm.cclp.dao.service.LogService;
import com.incomm.cclp.dao.service.SequenceService;
import com.incomm.cclp.dao.service.SpilDeactivationRevDAOService;
import com.incomm.cclp.domain.StatementsLogInfo;
import com.incomm.cclp.domain.TransactionLogInfo;
import com.incomm.cclp.dto.RequestInfo;
import com.incomm.cclp.dto.ResponseInfo;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.repos.GenericRepo;
import com.incomm.cclp.transaction.constants.GeneralConstants;
import com.incomm.cclp.transaction.constants.TransactionConstant;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class SpilDeactivationRevDAOServiceImpl implements SpilDeactivationRevDAOService {
	@Autowired
	private AuthChecksService authService;
	@Autowired
	private GenericRepo genericRepo;
	@Autowired
	AccountPurseDAOService accountPurseService;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private LogService logService;

	@Override
	public ResponseInfo doDeactivationRev(RequestInfo req) throws ServiceException {
		req.setTxnSeqId(sequenceService.getNextTxnSeqId());
		accountPurseService.getAccountPurseDetails(req);
		req.setAuthId(sequenceService.getNextAuthSeqId());

		TransactionLogInfo txnLog = genericRepo.getLastSuccessTxn(req.getChannel(), req.getCardNumHash(), req.getTxnCode(),
				ResponseCodes.SUCCESS, MsgTypeConstants.MSG_TYPE_NORMAL, req.getRrn(), req.getTxnDate());
		setOriginalValues(req, txnLog);

		int count = genericRepo.updateCardDetails(req.getCardNumHash(), req.getOldCardStatus(), req.getFirstTimeTopupFlag());
		if (count == 0) {
			log.error(SpilExceptionMessages.INVALID_CARD_STATE);
			throw new ServiceException(SpilExceptionMessages.INVALID_CARD_STATE, ResponseCodes.INVALID_CARD_STATE);
		}

		List<StatementsLogInfo> stmtLog = genericRepo.getLastFeeTransactions(req.getRrn(), TransactionConstant.CREDIT_CARD,
				req.getTxnDate(), req.getTxnCode());

		authService.authorizeTransaction(req);

		genericRepo.updateLastTxnDate(req.getAccountId(), req.getPurseId());

		logService.addReversalStatementlog(req, stmtLog);

		Map<String, String> inventoryDetails = genericRepo.getLocationMerchanntID(req.getCardNumHash());
		req.setLocationId(inventoryDetails.get("LOCATION_ID"));
		req.setMerchantId(inventoryDetails.get("MERCHANT_ID"));
		log.debug("LocationId: {}, MerchantId: {}", req.getLocationId(), req.getMerchantId());
		if (req.getLocationId() != null && req.getMerchantId() != null) {
			genericRepo.updateInventory(req.getProductId(), req.getMerchantId(), req.getLocationId());
		}

		genericRepo.updateTransactionReversalFlag(req.getCardNumHash(), req.getTxnCode(), req.getRrn(), "Y");

		genericRepo.updateTransactionReversalFlag(req.getCardNumHash(), "002", txnLog.getOriginalRrn(), "N");

		genericRepo.updateInitialLoadBalance(req.getAccountId(), req.getClosingAvailBalance(), req.getClosingAvailBalance());

		req.setAuthorizedAmount(req.getClosingAvailBalance());

		doTransactionLogs(req);

		return ResponseInfo.builder()
			.respCode(ResponseCodes.SUCCESS)
			.errMsg(GeneralConstants.OK)
			.authId(req.getAuthId())
			.authorizedAmt(req.getAuthorizedAmount())
			.currCode(req.getLocaleCurrency())
			.build();
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

	public void doTransactionLogs(RequestInfo req) {
		log.info("doTransactionLogs START >>>");
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
		log.info("doTransactionLogs END <<<");
	}

	public void generateTransactionLog(RequestInfo req, String transactionCode, String isFinancial, String creditDebitFlag,
			String transactionDesc, String partialPreauthFlag, double openingLedgerBalance, double openingAvailableBalance,
			String cardStatus) {
		log.info("generateTransactionLog START >>>");
		req.setCardStatus(cardStatus);
		req.setTxnCode(transactionCode);
		req.setTxnType(isFinancial);
		req.setCreditDebitFlg(creditDebitFlag);
		req.setTxnDesc(transactionDesc);
		req.setPartialPreAuthInd(partialPreauthFlag);
		req.setOpeningLedgerBalance(openingLedgerBalance);
		req.setOpeningAvailBalance(openingAvailableBalance);
		req.setTxnReversalFlag(GeneralConstants.YES);
		req.setTxnFee(0);
		req.setReversalCode("0");
		log.info("generateTransactionLog END <<<");
	}

}
