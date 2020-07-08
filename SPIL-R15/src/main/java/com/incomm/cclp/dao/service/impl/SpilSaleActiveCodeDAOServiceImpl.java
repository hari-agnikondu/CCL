package com.incomm.cclp.dao.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.dao.service.AccountPurseDAOService;
import com.incomm.cclp.dao.service.AuthChecksService;
import com.incomm.cclp.dao.service.LogService;
import com.incomm.cclp.dao.service.SequenceService;
import com.incomm.cclp.dao.service.SpilSaleActiveCodeDAOService;
import com.incomm.cclp.domain.AccountPurseInfo;
import com.incomm.cclp.dto.RequestInfo;
import com.incomm.cclp.dto.ResponseInfo;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.repos.GenericRepo;
import com.incomm.cclp.transaction.constants.GeneralConstants;
import com.incomm.cclp.transaction.constants.TransactionConstant;

@Service
public class SpilSaleActiveCodeDAOServiceImpl implements SpilSaleActiveCodeDAOService {

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

	private static final Logger logger = LogManager.getLogger(SpilSaleActiveCodeDAOServiceImpl.class);

	@Transactional
	@Override
	public ResponseInfo doSaleActiveCode(RequestInfo req) throws ServiceException, ParseException {
		logger.info("doSaleActiveCode START >>>");

		double original_txn_amount = req.getTxnAmount();
		String currentCardStatus = req.getCardStatus();

		req.setTxnSeqId(sequenceService.getNextTxnSeqId());

		accountPurseDaoService.getAccountPurseDetails(req);

		RequestInfo reqTransactionLogInfo = req.getClone();
		if (req.isCardStatusConsumed()) {
			generateTransactionLog(reqTransactionLogInfo, TransactionConstant.TXN_UPDATE_INACTIVE_CODE, GeneralConstants.NO,
					TransactionConstant.NONFINANCIAL_CARD, "Card Status update to InActive", req.getPartialPreAuthInd(), 0, 0);
			logService.doTransactionLog(reqTransactionLogInfo);
		}
		genericRepo.updateLastTxnDate(req.getAccountId(), req.getPurseId());
		genericRepo.updateCardStatus(TransactionConstant.ACTIVE_CARD, req.getCardNumHash());
		genericRepo.updateOldCardStatus(req.getCardNumHash(), currentCardStatus);
		currentCardStatus = TransactionConstant.ACTIVE_CARD;
		int count = genericRepo.getDamagedCardCount(req.getAccountId(), TransactionConstant.DAMAGE);
		if (count != 0) {
			genericRepo.updateCardStatus(new BigDecimal(req.getAccountId()), TransactionConstant.DAMAGE, TransactionConstant.CLOSED);
		}

		if (req.isOrderFulfillment()) {
			req.setTxnAmount(0);
		}

		authService.authorizeTransaction(req);

		Date activationDate = req.getSysDate();
		genericRepo.updateActivationStatus(req.getCardNumHash(), activationDate);

		if (req.isFirstTimeTopUp() && req.getTxnAmount() > 0) {
			doFirstTimeTopup(req);
		}

		req.setAuthId(sequenceService.getNextAuthSeqId());
		req.setFeeFlg(GeneralConstants.YES);

		doStatementLog(req);

		logger.debug("LocationId: {}, MerchantId: {}", req.getLocationId(), req.getMerchantId());
		if (req.getLocationId() != null) {
			genericRepo.removeFromInventory(req.getProductId(), req.getMerchantId(), req.getLocationId());
		}

		req.setAuthorizedAmount(req.getClosingAvailBalance());
		req.setCardStatus(currentCardStatus);
		req.setTxnAmount(original_txn_amount);

		reqTransactionLogInfo = req.getClone();
		generateTransactionLog(reqTransactionLogInfo, req.getTxnCode(), req.getTxnType(), req.getCreditDebitFlg(), req.getTxnDesc(),
				req.getPartialPreAuthInd(), req.getOpeningLedgerBalance(), req.getOpeningAvailBalance());
		logService.doTransactionLog(reqTransactionLogInfo);

		logger.info("doSaleActiveCode END <<<");
		return ResponseInfo.builder()
			.respCode(ResponseCodes.SUCCESS)
			.errMsg(GeneralConstants.OK)
			.authId(req.getAuthId())
			.authorizedAmt(req.getAuthorizedAmount())
			.cardStatus(currentCardStatus)
			.tranSeqId(req.getTxnSeqId())
			.serialNumber(new BigDecimal(req.getSerialNumber()))
			.digitalPin(req.getDigitalPin())
			.currCode(req.getTranCurr())
			.cardNumber(req.getCardNum())
			.build();
	}

	public void doFirstTimeTopup(RequestInfo req) {
		logger.info("doFirstTimeTopup START >>>");
		genericRepo.updateTopup(req.getCardNumHash(), GeneralConstants.YES);
		genericRepo.updateInitialLoadBalance(req.getAccountId(), req.getTxnAmount(), req.getTxnAmount());
		genericRepo.updateFirstLoadDate(req.getAccountPurseId(), req.getSysDate());
		logger.info("doFirstTimeTopup END <<<");
	}

	public void doStatementLog(RequestInfo req) {
		if (req.isFinancial()) {
			RequestInfo reqStatementLogInfo = req.getClone();
			double closing_balance = 0;
			switch (req.getCreditDebitFlg()) {
			case TransactionConstant.DEBIT_CARD:
				closing_balance = req.getOpeningLedgerBalance() - req.getTxnAmount();
				break;
			case TransactionConstant.CREDIT_CARD:
				closing_balance = req.getOpeningLedgerBalance() + req.getTxnAmount();
				break;
			}
			if (req.getTxnAmount() > 0) {
				generateStatementLogObj(reqStatementLogInfo, req.getOpeningLedgerBalance(), closing_balance, req.getTxnAmount(),
						req.getCreditDebitFlg(), GeneralConstants.NO, "Sale Active Code");
				logService.doStatementsLog(reqStatementLogInfo);
			}

			if ("A".equals(req.getProdFeeCondition())) {
				closing_balance = reqStatementLogInfo.getClosingBalance() - req.getProdFlatFeeAmount();
				generateStatementLogObj(reqStatementLogInfo, reqStatementLogInfo.getClosingBalance(), closing_balance,
						req.getProdFlatFeeAmount(), TransactionConstant.DEBIT_CARD, GeneralConstants.YES, "Sale Active Code Fee");
				logService.doStatementsLog(reqStatementLogInfo);

				closing_balance = reqStatementLogInfo.getClosingBalance() - req.getProdPercentFeeAmount();
				generateStatementLogObj(reqStatementLogInfo, reqStatementLogInfo.getClosingBalance(), closing_balance,
						req.getProdPercentFeeAmount(), TransactionConstant.DEBIT_CARD, GeneralConstants.YES,
						"Sale Active Code Percent Fee");
				logService.doStatementsLog(reqStatementLogInfo);

			} else if (req.getTxnFee() > 0) {
				closing_balance = reqStatementLogInfo.getClosingBalance() - req.getTxnFee();
				generateStatementLogObj(reqStatementLogInfo, reqStatementLogInfo.getClosingBalance(), closing_balance, req.getTxnFee(),
						TransactionConstant.DEBIT_CARD, GeneralConstants.YES, "Sale Active Code Fee");
				logService.doStatementsLog(reqStatementLogInfo);
			}
		}
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
			String transaction_desc, String partial_preauth_ind, double opening_ledger_balance, double opening_available_balance) {
		logger.info("generateTransactionLog START >>>");
		req.setTxnCode(transaction_code);
		req.setTxnType(is_financial);
		req.setCreditDebitFlg(cr_dr_flag);
		req.setTxnDesc(transaction_desc);
		req.setPartialPreAuthInd(partial_preauth_ind);
		req.setOpeningLedgerBalance(opening_ledger_balance);
		req.setOpeningAvailBalance(opening_available_balance);
		req.setTxnReversalFlag(GeneralConstants.NO);
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

}
