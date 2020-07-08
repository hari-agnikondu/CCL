package com.incomm.cclp.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.account.domain.factory.AccountPurseStateType;
import com.incomm.cclp.account.domain.model.AccountPurseGroupStatus;
import com.incomm.cclp.account.domain.model.PurseStatusType;
import com.incomm.cclp.dao.AccountPurseDAO;
import com.incomm.cclp.domain.AccountPurseBalance;
import com.incomm.cclp.domain.AccountPurseInfo;
import com.incomm.cclp.dto.AccountBalance;
import com.incomm.cclp.dto.AccountPurseUsageDto;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.AccountPurseService;
import com.incomm.cclp.util.Util;

@Service
public class AccountPurseServiceImpl implements AccountPurseService {

	@Autowired
	AccountPurseDAO accountPurseDAO;

	@Override
	public AccountPurseInfo getAccountPurse(long accountPurseId) throws ServiceException {
		return accountPurseDAO.getAccountPurse(accountPurseId);

	}

	@Override
	public List<AccountPurseBalance> getAccountPurse(long accountId, long accountPurseId) throws ServiceException {
		return accountPurseDAO.getActiveAccountPurseBalance(accountId, accountPurseId);

	}

	@Override
	public List<AccountPurseBalance> getAccountPurses(long accountId, long purseId, String skuCode) throws ServiceException {
		return accountPurseDAO.getAccountPurseBalances(accountId, purseId, skuCode);
	}

	@Override
	public List<AccountPurseBalance> getAccountPurses(long accountId, long purseId, boolean purseStatus) {
		return accountPurseDAO.getAccountPurseBalances(accountId, purseId, purseStatus);
	}

	@Override
	public int updateAccountPurse(long accountId, long purseId, long accountPurseId, BigDecimal closingLedgBal, BigDecimal closingAvailBal,
			BigDecimal previousLedgerBalance, BigDecimal previousAvailableBalance, String topupStatus) {

		return accountPurseDAO.updateAccountPurse(accountId, purseId, accountPurseId, closingLedgBal, closingAvailBal,
				previousLedgerBalance, previousAvailableBalance, topupStatus);
	}

	@Override
	public List<AccountPurseBalance> getAccountPurseBalances(long accountId, long productId, long purseId) {
		return this.accountPurseDAO.getAccountPurseBalances(accountId, productId, purseId);
	}

	@Override
	public AccountPurseUsageDto getAccountPurseUsage(long accountId, long purseId) {
		return this.accountPurseDAO.getAccountPurseUsage(accountId, purseId);
	}

	@Override
	public List<AccountPurseUsageDto> getAccountPurseUsage(long accountId) {
		return this.accountPurseDAO.getAccountPurseUsage(accountId);
	}

	@Override
	public AccountPurseBalance getAccountPurseBalance(long accountPurseId) {
		return accountPurseDAO.getAccountBalance(accountPurseId);
	}

	@Override
	public int updateAccountPurseUsage(long accountId, long purseId, Map<String, Object> usageFees, Map<String, Object> usageLimits,
			AccountPurseGroupStatus accountPurseGroupStatus, LocalDateTime lastTransactionDate) {

		String fees = null;
		String limits = null;

		fees = Util.convertMapToJson(usageFees);
		limits = Util.convertMapToJson(usageLimits);

		return accountPurseDAO.updateAccountPurseUsage(accountId, purseId, fees, limits, accountPurseGroupStatus, lastTransactionDate);
	}

	@Override
	public int addAccountPurseUsage(long accountId, long purseId, Map<String, Object> usageFees, Map<String, Object> usageLimits,
			AccountPurseGroupStatus accountPurseGroupStatus, LocalDateTime lastTransactionDate) {

		String fees = null;
		String limits = null;
		AccountPurseUsageDto purseUsage = getAccountPurseUsage(accountId, purseId);
		if (purseUsage == null) {

			fees = Util.convertMapToJson(usageFees);
			limits = Util.convertMapToJson(usageLimits);

			return accountPurseDAO.addAccountPurseUsage(accountId, purseId, fees, limits, accountPurseGroupStatus, lastTransactionDate);
		}
		return 0;
	}

	@Override
	public int addAccountPurse(AccountPurseBalance accountPurse) {
		return accountPurseDAO.addAccountPurse(accountPurse);
	}

	@Override
	public BigDecimal getCumulativePurseBalance(BigInteger accountId, BigInteger productId, BigInteger purseId) {
		List<AccountPurseBalance> accountPurses = accountPurseDAO.getActiveAccountPurseBalances(accountId.longValue(),
				productId.longValue(), purseId.longValue());
		BigDecimal cumulativeBalance = BigDecimal.ZERO;
		for (AccountPurseBalance purse : accountPurses) {
			cumulativeBalance = cumulativeBalance.add(purse.getAvailableBalance());
		}
		return cumulativeBalance;
	}

	@Override
	public int updatePurseStatus(long accountId, long purseId, PurseStatusType purseStatus, LocalDateTime lastTransactionDate,
			LocalDateTime startDate, LocalDateTime endDate) {
		return accountPurseDAO.updatePurseStatus(accountId, purseId, purseStatus.getStatusCode(), lastTransactionDate, startDate, endDate);
	}

	@Override
	public List<AccountBalance> getActiveAccountPurses(long accountId) {
		return accountPurseDAO.getActiveAccountPurses(accountId);
	}

	@Override
	public List<AccountPurseBalance> getAccountPurseBalances(long accountId, Optional<Long> purseId, AccountPurseStateType purseStateType) {

		return accountPurseDAO.getAccountPurseBalances(accountId, purseId, purseStateType);
	}

	@Override
	public List<AccountPurseBalance> getExpiredAccountPurseBalances(long accountId, long productId, long purseId) {
		return this.accountPurseDAO.getExpiredAccountPurseBalances(accountId, productId, purseId);
	}

}
