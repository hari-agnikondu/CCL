package com.incomm.cclp.dao.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incomm.cclp.account.domain.model.AccountPurseGroupStatus;
import com.incomm.cclp.account.domain.model.PurseStatusType;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.constants.SpilTranConstants;
import com.incomm.cclp.dao.CommonValidationsDAO;
import com.incomm.cclp.dao.service.AuthChecksService;
import com.incomm.cclp.domain.AccountPurseBalance;
import com.incomm.cclp.domain.AuthCheck;
import com.incomm.cclp.dto.RequestInfo;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.exception.SuspendCardException;
import com.incomm.cclp.repos.GenericRepo;
import com.incomm.cclp.service.AccountPurseService;
import com.incomm.cclp.transaction.constants.TransactionConstant;
import com.incomm.cclp.util.AuthCheckComparator;

@Service
public class AuthChecksServiceImpl implements AuthChecksService {

	@Autowired
	private GenericRepo genericRepo;

	@Autowired
	private AccountPurseService accountPurseService;

	@Autowired
	CommonValidationsDAO commonValidationsDao;

	private static final Logger logger = LogManager.getLogger(AuthChecksServiceImpl.class);

	@Transactional
	@Override
	public void authorizeTransaction(RequestInfo req) {
		logger.info("authorizeTransaction start >>>");
		doAuthChecks(req);
		logger.info("authorizeTransaction end <<<");
	}

	@Transactional
	public void doAuthChecks(RequestInfo req) {
		logger.debug("doAuthChecks: ");
		List<AuthCheck> authChecks = getAuthChecks(req.getTxnCode(), req.getChannel(), req.getMsgType());
		logger.debug("Authchecks count: " + authChecks.size());

		Collections.sort(authChecks, new AuthCheckComparator());
		for (AuthCheck authCheck : authChecks) {
			switch (authCheck.getAuthCheckPK()
				.getCheckName()) {
			case "BALANCE_DEBIT":
				doBalanceAuthDebit(req);
				break;
			case "BALANCE_LOCK":
				doBalanceAuthLock(req);
				break;
			case "BALANCE_UNLOCK":
				doBalanceAuthUnlock();
				break;
			case "BALANCE_CREDIT":
				doBalanceAuthCredit(req);
				break;
			case "SUSPENDED_CREDIT_DEBIT":
				doHandleSupendedCreditDebit(req);
				break;
			case "BALANCE_CREDIT_REVERSAL":
				doBalanceCreditReversal(req);
				break;
			case "BALANCE_DEBIT_REVERSAL":
				balanceAuthDebitReversal(req);
				break;
			default:
				break;
			}
		}
	}

	@Transactional
	public List<AuthCheck> getAuthChecks(String txnCode, String channel, String msgType) {
		logger.debug("txnCode: {},channel: {},msgType: {}", txnCode, channel, msgType);
		return genericRepo.findAuthChecks(txnCode, channel, msgType);
	}

	@Transactional
	public void doBalanceAuthDebit(RequestInfo req) {

		double closingAvailableBalance = req.getClosingAvailBalance();
		double closingLedgerBalance = req.getClosingLedgerBalance();
		double txnAmount = req.getTxnAmount();
		double txnFee = req.getTxnFee();
		double totalAmount = 0;
		double authAmount = 0;
		double delayedAmount = commonValidationsDao.getDelayedAmount(req.getAccountId()
			.toString());
		if (delayedAmount > 0) {
			commonValidationsDao.updateDelayedAmount(req.getAccountId()
				.toString());
		}

		if (req.getTxnCode()
			.equals(SpilTranConstants.SPIL_DEACTIVATION)) {
			authAmount = req.getTxnAmount();
			totalAmount = txnAmount - txnFee;
			logger.debug("totalAmount: {}", totalAmount);

			closingAvailableBalance = closingAvailableBalance - totalAmount;
			logger.debug("closingAvailableBalance: {}", closingAvailableBalance);

			closingLedgerBalance = closingLedgerBalance - totalAmount;
			logger.debug("closingLedgerBalance: {}", closingLedgerBalance);
		}

		if (closingLedgerBalance >= 0 && closingAvailableBalance >= 0) {
			genericRepo.updateBalancesByAccountPurseId(req.getAccountPurseId(), closingLedgerBalance, closingAvailableBalance);
		} else {
			if (!req.getTxnCode()
				.equals(SpilTranConstants.SPIL_BALANCE_INQUIRY)
					|| !req.getTxnCode()
						.equals(SpilTranConstants.SPIL_TRAN_HISTORY)) {
				logger.error(SpilExceptionMessages.ERROR_INSUFFICIENT_BALANCE);
				throw new ServiceException(SpilExceptionMessages.ERROR_INSUFFICIENT_BALANCE, ResponseCodes.INSUFFICIENT_FUNDS);
			}
		}

		req.setClosingAvailBalance(closingAvailableBalance);
		req.setClosingLedgerBalance(closingLedgerBalance);
		req.setTotalAmount(authAmount);
		req.setReqAuthAmt(req.getReqTxnAmt());
		req.setAuthorizedAmount(closingAvailableBalance);

	}

	@Transactional
	public void doBalanceAuthCredit(RequestInfo req) {
		logger.info("doBalanceAuthCredit start >>>");

		double authAmount = req.getTxnAmount();
		double totalAmount = req.getTxnAmount() - req.getTxnFee();

		double closingAvailBalanceOut = req.getOpeningAvailBalance() + totalAmount;
		logger.debug("closingAvailBalanceOut: {}", closingAvailBalanceOut);

		double closingLedBalanceOut = req.getOpeningLedgerBalance() + totalAmount;
		logger.debug("closingLedBalanceOut: {}", closingLedBalanceOut);

		if ((closingAvailBalanceOut < 0) || (closingLedBalanceOut < 0)) {
			logger.error(SpilExceptionMessages.ERROR_INSUFFICIENT_BALANCE);
			throw new ServiceException(SpilExceptionMessages.ERROR_INSUFFICIENT_BALANCE, ResponseCodes.INSUFFICIENT_FUNDS);
		}

		double maxPurseBal = req.getMaxPurseBalance();

		if (req.isNewPurse()) {
			req.setPurseId(BigInteger.valueOf(req.getPurAuthReq()
				.getPurseId()));

			if (!Double.isNaN(maxPurseBal))
				validatePurseMaxBalanace(req.getAccountId(), req.getProductId(), BigInteger.valueOf(req.getPurAuthReq()
					.getPurseId()), closingAvailBalanceOut, maxPurseBal);

			AccountPurseBalance accountPurse = getAccountPurseBalnceObj(req.getPurAuthReq(), closingLedBalanceOut, closingAvailBalanceOut);

			accountPurseService.addAccountPurse(accountPurse);

			Map<String, Object> usageLimits = req.getValueDTO()
				.getUsageLimit();
			Map<String, Object> usageFees = req.getValueDTO()
				.getUsageFee();

			accountPurseService.addAccountPurseUsage(req.getAccountId()
				.longValue(),
					req.getPurseId()
						.longValue(),
					usageFees, usageLimits, AccountPurseGroupStatus.from(PurseStatusType.ACTIVE), new Timestamp(req.getSysDate()
						.getTime()).toLocalDateTime());
		} else {
			if (!Double.isNaN(maxPurseBal))
				validatePurseMaxBalanace(req.getAccountId(), req.getProductId(), req.getPurseId(), closingAvailBalanceOut, maxPurseBal);

			genericRepo.updateBalancesByAccountPurseId(req.getAccountPurseId(), closingLedBalanceOut, closingAvailBalanceOut);
			req.setAuthorizedAmount(closingAvailBalanceOut);
		}

		req.setClosingAvailBalance(closingAvailBalanceOut);
		req.setClosingLedgerBalance(closingLedBalanceOut);
		req.setTotalAmount(authAmount);
		req.setReqAuthAmt(req.getReqTxnAmt());

		logger.info("doBalanceAuthCredit end <<<");
	}

	@Transactional
	public void doBalanceAuthLock(RequestInfo req) {

	}

	@Transactional
	public void doBalanceAuthUnlock() {

	}

	@Transactional
	public void doHandleSupendedCreditDebit(RequestInfo req) {
		logger.info("doHandleSupendedCreditDebit start >>>");
		double maxCardBal = req.getMaxPurseBalance();
		if (req.isCredit()) {
			if (!TransactionConstant.SUSPENDED_CREDIT.equalsIgnoreCase(req.getCardStatus()) && req.getClosingAvailBalance() > maxCardBal) {
				genericRepo.updateOldCurrentCardStatus(req.getCardNumHash(), TransactionConstant.SUSPENDED_CREDIT, req.getCardStatus());
			}

			if (TransactionConstant.SUSPENDED_DEBIT.equalsIgnoreCase(req.getCardStatus())
					&& (req.getClosingAvailBalance() > 0 && req.getClosingAvailBalance() < maxCardBal)) {
				genericRepo.updateOldCurrentCardStatus(req.getCardNumHash(), req.getCardStatus(), TransactionConstant.SUSPENDED_DEBIT);
			}
		} else if (req.isDebit()
				&& (!TransactionConstant.SUSPENDED_DEBIT.equalsIgnoreCase(req.getCardStatus()) && req.getClosingAvailBalance() < 0)) {
			genericRepo.updateOldCurrentCardStatus(req.getCardNumHash(), TransactionConstant.SUSPENDED_DEBIT, req.getCardStatus());
		}
		logger.info("doHandleSupendedCreditDebit end <<<");
	}

	@Transactional
	public void doBalanceCreditReversal(RequestInfo req) {
		logger.info("doBalanceCreditReversal start >>>");
		double totalAmount = req.getOrgnAuthAmt() - req.getOrgnlTxnFee();

		double closingAvailBalance = req.getOpeningAvailBalance() - totalAmount;
		logger.debug("closingAvailBalance: {}", closingAvailBalance);

		double closingLedBalance = req.getOpeningLedgerBalance() - totalAmount;
		logger.debug("closingLedBalance: {}", closingLedBalance);

		req.setClosingAvailBalance(closingAvailBalance);
		req.setClosingLedgerBalance(closingLedBalance);
		req.setTotalAmount(req.getOrgnAuthAmt());
		req.setReqAuthAmt(req.getReqTxnAmt());
		if (req.isExtPurse()) {
			req.setPurseId(BigInteger.valueOf(req.getPurAuthReq()
				.getPurseId()));
			genericRepo.updateBalanceExpDate(req.getAccountPurseId(), closingLedBalance, closingAvailBalance);
		} else {
			genericRepo.updateBalancesByAccountPurseId(req.getAccountPurseId(), closingLedBalance, closingAvailBalance);
			req.setAuthorizedAmount(closingAvailBalance);
		}

		logger.info("doBalanceCreditReversal end <<<");

	}

	@Transactional
	public void balanceAuthDebitReversal(RequestInfo req) {
		double totalAmount = req.getOrgnAuthAmt() + req.getOrgnlTxnFee();
		double closingAvailBalance = req.getOpeningAvailBalance() + totalAmount;
		double closingLedBalance = req.getOpeningLedgerBalance() + totalAmount;

		genericRepo.updateBalancesByAccountPurseId(req.getAccountPurseId(), closingLedBalance, closingAvailBalance);

		req.setClosingAvailBalance(closingAvailBalance);
		req.setClosingLedgerBalance(closingLedBalance);
		req.setTotalAmount(req.getOrgnAuthAmt());
		req.setReqAuthAmt(req.getReqTxnAmt());

		logger.debug("totalAmount: {}, closingAvailBalance: {}, closingLedBalance: {}", totalAmount, closingAvailBalance,
				closingLedBalance);
	}

	public void validatePurseMaxBalanace(BigInteger accountId, BigInteger productId, BigInteger purseId, double closingAvailableBalance,
			double maxPurseBalance) {
		BigDecimal cumulativeBalance = accountPurseService.getCumulativePurseBalance(accountId, productId, purseId);
		if ((closingAvailableBalance + cumulativeBalance.doubleValue()) > maxPurseBalance) {
			logger.error(SpilExceptionMessages.ERROR_MAX_BALANCE_EXCEED);
			throw new SuspendCardException(SpilExceptionMessages.ERROR_MAX_BALANCE_EXCEED, ResponseCodes.EXCEEDING_MAXIMUM_CARD_BALANCE);
		}
	}

	public AccountPurseBalance getAccountPurseBalnceObj(AccountPurseBalance reqPurse, double closingLedBalanceOut,
			double closingAvailBalanceOut) {
		reqPurse.setLedgerBalance(BigDecimal.valueOf(closingLedBalanceOut));
		reqPurse.setAvailableBalance(BigDecimal.valueOf(closingAvailBalanceOut));
		return reqPurse;
	}

}
