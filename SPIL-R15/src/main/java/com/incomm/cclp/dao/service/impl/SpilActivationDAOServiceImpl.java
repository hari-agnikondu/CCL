package com.incomm.cclp.dao.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incomm.cclp.constants.LoggerConstants;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.AccountPurseDAO;
import com.incomm.cclp.dao.service.AccountPurseDAOService;
import com.incomm.cclp.dao.service.AuthChecksService;
import com.incomm.cclp.dao.service.LogService;
import com.incomm.cclp.dao.service.SequenceService;
import com.incomm.cclp.dao.service.SpilActivationDAOService;
import com.incomm.cclp.dto.AccountPurseDTO;
import com.incomm.cclp.dto.RequestInfo;
import com.incomm.cclp.dto.ResponseInfo;
import com.incomm.cclp.repos.GenericRepo;
import com.incomm.cclp.transaction.constants.GeneralConstants;
import com.incomm.cclp.transaction.constants.TransactionConstant;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class SpilActivationDAOServiceImpl implements SpilActivationDAOService {

	@Autowired
	private GenericRepo genericRepo;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private LogService logService;
	@Autowired
	private AuthChecksService authService;
	@Autowired
	private AccountPurseDAOService accountPurseDaoService;
	@Autowired
	private AccountPurseDAO accountPurseDAO;

	@Transactional
	@Override
	public ResponseInfo doActivation(RequestInfo req) {
		log.info(LoggerConstants.ENTER);

		double originalTxnAmount = req.getTxnAmount();
		req.setTxnSeqId(sequenceService.getNextTxnSeqId());
		req.setAuthId(sequenceService.getNextAuthSeqId());
		req.setTxnReversalFlag(GeneralConstants.NO);

		accountPurseDaoService.getAccountPurseDetails(req);

		RequestInfo reqTransactionLogInfo = req.getClone();
		if (req.isCardStatusConsumed()) {
			log.debug("Logging in transactionLog for consumed card status");
			generateTransactionLog(reqTransactionLogInfo, TransactionConstant.TXN_UPDATE_INACTIVE_CODE, GeneralConstants.NO,
					TransactionConstant.NONFINANCIAL_CARD, "Card Status update to InActive", 0, 0);
			logService.doTransactionLog(reqTransactionLogInfo);
		}

		if (req.isOrderFulfillment()) {
			log.debug("Setting transaction amount to 0");
			req.setTxnAmount(0);
		}

		authService.authorizeTransaction(req);

		genericRepo.updateLastTxnDate(req.getAccountId(), req.getPurseId());
		genericRepo.updateCurrentCardStatus(req.getCardNumHash(), TransactionConstant.ACTIVE_CARD);
		req.setCardStatus(TransactionConstant.ACTIVE_CARD);

		int count = genericRepo.getDamagedCardCount(req.getAccountId(), TransactionConstant.DAMAGE);
		if (count != 0) {
			genericRepo.updateCardStatus(new BigDecimal(req.getAccountId()), TransactionConstant.DAMAGE, TransactionConstant.CLOSED);
		}

		Date activationDate = req.getSysDate();
		genericRepo.updateActivationStatus(req.getCardNumHash(), activationDate);

		if (req.isFirstTimeTopUp() && req.getTxnAmount() > 0) {
			doFirstTimeTopup(req);
		}

		req.setFeeFlg(GeneralConstants.YES);

		logService.addStatementLog(req);

		log.debug("LocationId: " + req.getLocationId() + ", MerchantId: " + req.getMerchantId());
		if (req.getLocationId() != null) {
			genericRepo.removeFromInventory(req.getProductId(), req.getMerchantId(), req.getLocationId());
		}

		req.setTxnAmount(originalTxnAmount);

		if (req.isExtPurse()) {
			log.debug("Logging in transactionLog for default purse");
			String defaultPurseId = (String) req.getValueDTO()
				.getProductAttributes()
				.get(ValueObjectKeys.PRODUCT)
				.get(ValueObjectKeys.DEFAULT_PURSE);
			Map<String, Object> defaultPurse = genericRepo.getDefaultPurseDetails(req.getAccountId()
				.longValue(), Long.valueOf(defaultPurseId));
			generateTransactionLog(reqTransactionLogInfo, req.getTxnCode(), req.getTxnType(), req.getCreditDebitFlg(), req.getTxnDesc(), 0,
					0);
			setDefaultPurse(reqTransactionLogInfo, req, defaultPurse);

			logService.doTransactionLog(reqTransactionLogInfo);
		}

		logService.doTransactionLog(req);

		AccountPurseDTO purAuthResp = req.getValueDTO()
			.getAccountPurseDto();
		if (purAuthResp != null) {
			purAuthResp.setAuthAmount(BigDecimal.valueOf(req.getClosingAvailBalance()));
			purAuthResp.setAccountPurseId(req.getAccountPurseId());
		}

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
			.accountPurse(purAuthResp)
			.build();
	}

	public void generateTransactionLog(RequestInfo req, String transactionCode, String isFinancial, String CreditDebitFlag,
			String transactionDesc, double openingLedgerBalance, double openingAvailableBalance) {
		log.info(LoggerConstants.ENTER);

		req.setTxnCode(transactionCode);
		req.setTxnType(isFinancial);
		req.setCreditDebitFlg(CreditDebitFlag);
		req.setTxnDesc(transactionDesc);
		req.setOpeningLedgerBalance(openingLedgerBalance);
		req.setOpeningAvailBalance(openingAvailableBalance);

		log.info(LoggerConstants.EXIT);
	}

	public void doFirstTimeTopup(RequestInfo req) {
		log.info(LoggerConstants.ENTER);

		genericRepo.updateTopup(req.getCardNumHash(), GeneralConstants.YES);
		genericRepo.updateInitialLoadBalance(req.getAccountId(), req.getTxnAmount(), req.getTxnAmount());
		genericRepo.updateFirstLoadDate(req.getAccountPurseId(), req.getSysDate());

		log.info(LoggerConstants.EXIT);
	}

	public void setDefaultPurse(RequestInfo reqTransactionLogInfo, RequestInfo req, Map<String, Object> purseDetails) {
		reqTransactionLogInfo.setPurAuthReq(null);
		reqTransactionLogInfo.setTxnSeqId(sequenceService.getNextTxnSeqId());
		reqTransactionLogInfo.setCardStatus(req.getCardStatus());
		reqTransactionLogInfo.setTxnAmount(0);
		reqTransactionLogInfo.setTxnFee(0);
		reqTransactionLogInfo.setPurseType(purseDetails.get("PURSE_TYPE")
			.toString());
		reqTransactionLogInfo.setPurseTypeId(Integer.valueOf(purseDetails.get("PURSE_TYPE_ID")
			.toString()));
		reqTransactionLogInfo.setAccountPurseId(Long.parseLong(purseDetails.get("ACCOUNT_PURSE_ID")
			.toString()));
		reqTransactionLogInfo.setPurseId(new BigInteger(purseDetails.get("PURSE_ID")
			.toString()));
	}
}
