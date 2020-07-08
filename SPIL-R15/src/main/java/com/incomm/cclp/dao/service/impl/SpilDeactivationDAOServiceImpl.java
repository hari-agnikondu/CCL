package com.incomm.cclp.dao.service.impl;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incomm.cclp.constants.LoggerConstants;
import com.incomm.cclp.constants.MsgTypeConstants;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.dao.service.AccountPurseDAOService;
import com.incomm.cclp.dao.service.AuthChecksService;
import com.incomm.cclp.dao.service.LogService;
import com.incomm.cclp.dao.service.SequenceService;
import com.incomm.cclp.dao.service.SpilDeactivationDAOService;
import com.incomm.cclp.domain.TransactionLogInfo;
import com.incomm.cclp.dto.RequestInfo;
import com.incomm.cclp.dto.ResponseInfo;
import com.incomm.cclp.repos.GenericRepo;
import com.incomm.cclp.transaction.constants.GeneralConstants;
import com.incomm.cclp.transaction.constants.TransactionConstant;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class SpilDeactivationDAOServiceImpl implements SpilDeactivationDAOService {

	@Autowired
	private GenericRepo genericRepo;

	@Autowired
	private SequenceService sequenceService;

	@Autowired
	private LogService logService;

	@Autowired
	private AuthChecksService authService;

	@Autowired
	AccountPurseDAOService accountPurseService;

	@Transactional
	@Override
	public ResponseInfo doDeactivation(RequestInfo req) {
		log.info(LoggerConstants.ENTER);

		req.setTxnSeqId(sequenceService.getNextTxnSeqId());
		req.setAuthId(sequenceService.getNextAuthSeqId());
		req.setTxnReversalFlag(GeneralConstants.NO);

		accountPurseService.getAccountPurseDetails(req);

		log.debug("defaultPurseId-->" + req.getDefaultPurseId());
		// get last successful activation txnAmount
		TransactionLogInfo txnLog = genericRepo.getLastSuccessfulActTxn(req.getChannel(), req.getCardNumHash(),
				TransactionConstant.TXN_ACTIVATION_CODE, ResponseCodes.SUCCESS, MsgTypeConstants.MSG_TYPE_NORMAL, req.getDefaultPurseId());

		// Set transaction amount to with which activation was done ignoring txn amount in request.
		log.debug("txnLogInfo-->" + txnLog);
		// setOriginalValues(req, txnLog);
		req.setTxnAmount(txnLog.getClosingAvailableBalance());
		req.setClosingAvailBalance(txnLog.getClosingAvailableBalance());
		req.setClosingLedgerBalance(txnLog.getClosingLedgerBalance());

		authService.authorizeTransaction(req);

		// update balances to zero
		genericRepo.updateBalancesByAccountPurseId(req.getAccountPurseId(), 0.0, 0.0);

		// update first_load_date to null
		genericRepo.updateFirstLoadDatetoNull(req.getAccountId(), req.getPurseId(), req.getProductId());

		// Update card status to inactive
		genericRepo.updateCardToInactiveStatus(req.getCardNumHash(), TransactionConstant.INACTIVE_CARD, req.getCardStatus());

		// Update account purse usage table with last txn date
		genericRepo.updateLastTxnDate(req.getAccountId(), req.getPurseId());

		req.setCardStatus(TransactionConstant.INACTIVE_CARD);

		logService.addStatementLog(req);

		Map<String, String> inventoryDetails = genericRepo.getLocationMerchanntID(req.getCardNumHash());
		req.setLocationId(inventoryDetails.get("LOCATION_ID"));
		req.setMerchantId(inventoryDetails.get("MERCHANT_ID"));
		log.debug("LocationId: " + req.getLocationId() + ", MerchantId: " + req.getMerchantId());
		if (req.getLocationId() != null) {
			genericRepo.addToInventory(req.getProductId(), req.getMerchantId(), req.getLocationId());
		}

		// update txnReversal flag
		genericRepo.updateTransactionReversalFlag(req.getCardNumHash(), req.getTxnCode(), req.getRrn(), "Y");

		// Update initialloadamt to null
		genericRepo.updateInitialLoadBalance(req.getAccountId(), req.getTxnAmount(), req.getTxnAmount());

		logService.doTransactionLog(req);

		log.info(LoggerConstants.EXIT);

		return ResponseInfo.builder()
			.respCode(ResponseCodes.SUCCESS)
			.errMsg(GeneralConstants.OK)
			.authId(req.getAuthId())
			.authorizedAmt(req.getAuthorizedAmount())
			.cardStatus(req.getCardStatus())
			.tranSeqId(req.getTxnSeqId())
			.serialNumber(new BigDecimal(req.getSerialNumber()))
			.currCode(req.getLocaleCurrency())
			.cardNumber(req.getCardNum())
			.build();
	}

	public void generateTransactionLog(RequestInfo req, String transaction_code, String is_financial, String cr_dr_flag,
			String transaction_desc, double opening_ledger_balance, double opening_available_balance) {
		log.info(LoggerConstants.ENTER);

		req.setTxnCode(transaction_code);
		req.setTxnType(is_financial);
		req.setCreditDebitFlg(cr_dr_flag);
		req.setTxnDesc(transaction_desc);
		req.setOpeningLedgerBalance(opening_ledger_balance);
		req.setOpeningAvailBalance(opening_available_balance);

		log.info(LoggerConstants.EXIT);
	}

	public void doFirstTimeTopup(RequestInfo req) {
		log.info(LoggerConstants.ENTER);

		genericRepo.updateTopup(req.getCardNumHash(), GeneralConstants.YES);
		genericRepo.updateInitialLoadBalance(req.getAccountId(), req.getTxnAmount(), req.getTxnAmount());
		// update for default purse
		genericRepo.updateFirstLoadDate(req.getAccountPurseId(), req.getSysDate());

		log.info(LoggerConstants.EXIT);
	}

}
