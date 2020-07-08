package com.incomm.cclp.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.incomm.cclp.account.domain.factory.AccountPurseStateType;
import com.incomm.cclp.account.domain.model.AccountPurseGroupStatus;
import com.incomm.cclp.domain.AccountPurseBalance;
import com.incomm.cclp.domain.AccountPurseInfo;
import com.incomm.cclp.dto.AccountBalance;
import com.incomm.cclp.dto.AccountPurseUsageDto;

public interface AccountPurseDAO {

	public AccountPurseInfo getAccountPurse(long accountPurseId);

	public int updateAccountPurse(long accountId, long purseId, long accountPurseId, BigDecimal closingLedgBalance,
			BigDecimal closingAvailBalance, BigDecimal previousLedgBalance, BigDecimal previousAvailBalance, String topupStatus);

	public List<AccountPurseBalance> getAccountPurseBalances(long accountId, long productId, long purseId);

	public AccountPurseUsageDto getAccountPurseUsage(long accountId, long purseId);

	public List<AccountPurseUsageDto> getAccountPurseUsage(long accountId);

	public List<AccountPurseBalance> getActiveAccountPurseBalances(long accountId, long productId, long purseId);

	public List<AccountPurseBalance> getActiveAccountPurseBalance(long accountId, long accountPurseId);

	public List<AccountPurseBalance> getAccountPurseBalances(long accountId, long purseId, String skuCode);

	public List<AccountPurseBalance> getAccountPurseBalances(long accountId, long purseId, boolean purseStatus);

	public int updateAccountPurseUsage(long accountId, long purseId, String usageFees, String usageLimits,
			AccountPurseGroupStatus accountPurseGroupStatus, LocalDateTime lastTransactionDate);

	public AccountPurseBalance getAccountBalance(long accountPurseId);

	public int addAccountPurse(AccountPurseBalance accountPurse);

	public List<AccountPurseBalance> getAllAccountPurseBalances(BigInteger accountId, BigInteger productId);

	public int addAccountPurseUsage(long accountId, long purseId, String usageFees, String usageLimits,
			AccountPurseGroupStatus accountPurseGroupStatus, LocalDateTime lastTransactionDate);

	public int updatePurseStatus(long accountId, long purseId, String statusCode, LocalDateTime lastTransactionDate,
			LocalDateTime startDate, LocalDateTime endDate);

	List<AccountPurseBalance> getExpiredAccountPurseBalances(long accountId, long productId, long purseId);

	public List<AccountBalance> getActiveAccountPurses(long accountId);

	public List<AccountPurseBalance> getAccountPurseBalances(long accountId, Optional<Long> purseId, AccountPurseStateType purseStateType);
}
