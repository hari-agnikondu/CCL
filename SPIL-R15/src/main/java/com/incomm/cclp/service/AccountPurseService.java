package com.incomm.cclp.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.incomm.cclp.account.domain.factory.AccountPurseStateType;
import com.incomm.cclp.account.domain.model.AccountPurseGroupStatus;
import com.incomm.cclp.account.domain.model.PurseStatusType;
import com.incomm.cclp.domain.AccountPurseBalance;
import com.incomm.cclp.domain.AccountPurseInfo;
import com.incomm.cclp.dto.AccountBalance;
import com.incomm.cclp.dto.AccountPurseUsageDto;

public interface AccountPurseService {

	public AccountPurseInfo getAccountPurse(long accountPurseId);

	public List<AccountPurseBalance> getAccountPurse(long accountId, long accountPurseId);

	public List<AccountPurseBalance> getAccountPurses(long accountId, long purseId, String skuCode);

	public List<AccountPurseBalance> getAccountPurses(long accountId, long purseId, boolean purseStatus);

	public List<AccountPurseBalance> getAccountPurseBalances(long accountId, long productId, long purseId);

	public AccountPurseBalance getAccountPurseBalance(long accountPurseId);

	public AccountPurseUsageDto getAccountPurseUsage(long accountId, long purseId);

	public List<AccountPurseUsageDto> getAccountPurseUsage(long accountId);

	public BigDecimal getCumulativePurseBalance(BigInteger accountId, BigInteger productId, BigInteger purseId);

	public int updateAccountPurse(long accountId, long purseId, long accountPurseId, BigDecimal closingLedgerBalance,
			BigDecimal closingAvailBalance, BigDecimal previousLedgerBalance, BigDecimal previousAvailableBalance, String topupStatus);

	public int updateAccountPurseUsage(long accountId, long purseId, Map<String, Object> usageFees, Map<String, Object> usageLimits,
			AccountPurseGroupStatus accountPurseGroupStatus, LocalDateTime lastTransactionDate);

	public int addAccountPurse(AccountPurseBalance accountPurse);

	public int addAccountPurseUsage(long accountId, long purseId, Map<String, Object> usageFees, Map<String, Object> usageLimits,
			AccountPurseGroupStatus accountPurseGroupStatus, LocalDateTime lastTransactionDate);

	public List<AccountBalance> getActiveAccountPurses(long accountId);

	public List<AccountPurseBalance> getAccountPurseBalances(long accountId, Optional<Long> purseId, AccountPurseStateType purseStateType);

	public int updatePurseStatus(long accountId, long purseId, PurseStatusType purseStatus, LocalDateTime lastTransactionDate,
			LocalDateTime startDate, LocalDateTime endDate);

	List<AccountPurseBalance> getExpiredAccountPurseBalances(long accountId, long productId, long purseId);

}
