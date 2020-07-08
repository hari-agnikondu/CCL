package com.incomm.cclp.account.domain.model;

import static com.incomm.cclp.account.util.CodeUtil.isNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.incomm.cclp.account.domain.exception.DomainExceptionFactory;
import com.incomm.cclp.account.domain.exception.DomainExceptionType;
import com.incomm.cclp.account.domain.view.AccountPurseGroupView;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.exception.ServiceException;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class AccountPurseAggregateNew implements AccountPurseAggregateInterface {

	@Getter
	private final long accountId;

	@Getter
	private final long productId;

	private final Map<Long, AccountPurseGroup> accountPurseGroupByPurseId;

	@Getter
	private final Set<Long> purseIds;

	@Builder
	public AccountPurseAggregateNew(long accountId, long productId, Map<Long, AccountPurseGroup> accountPurseGroupByPurseId,
			Set<Long> purseIds) {
		this.accountId = accountId;
		this.productId = productId;
		this.accountPurseGroupByPurseId = accountPurseGroupByPurseId;
		this.purseIds = purseIds;
	}

	@Override
	public AccountPurseGroupUpdate creditPurseBalance(long purseId, BigDecimal transactionAmount,
			Map<LedgerEntryType, BigDecimal> transactionFees, boolean createPurseIfNoneExist) {

		return getAccountPurseGroup(purseId).creditPurseBalance(transactionAmount, transactionFees, PurseBalanceType.BOTH,
				createPurseIfNoneExist);
	}

	@Override
	public AccountPurseGroupUpdate creditPurseBalance(long purseId, AccountPurseAltKeyAttributes attributes, BigDecimal transactionAmount,
			Map<LedgerEntryType, BigDecimal> transactionFees, boolean createPurseIfNoneExist) {
		return null;
	}

	@Override
	public AccountPurseGroupUpdate creditPurseBalance(long purseId, long accountPurseId, BigDecimal transactionAmount,
			Map<LedgerEntryType, BigDecimal> transactionFees) {
		return getAccountPurseGroup(purseId).creditPurseBalance(accountPurseId, transactionAmount, transactionFees, PurseBalanceType.BOTH);
	}

	@Override
	public AccountPurseGroupUpdate debitPurseBalance(long purseId, AccountPurseAltKeyAttributes attributes, BigDecimal transactionAmount,
			Map<LedgerEntryType, BigDecimal> transactionFees, PurseBalanceType purseBalanceType, boolean allowPartialAuth) {

		return getAccountPurseGroup(purseId).debitPurseBalance(attributes, transactionAmount, transactionFees, purseBalanceType,
				allowPartialAuth);
	}

	/*
	 * Partial allowPartialAuth should be always false for all the transactions other than redeem For redeem
	 * allowPartialAuth value is pulled from the produtc_purse attributes
	 */
	@Override
	public AccountPurseGroupUpdate debitPurseBalance(long purseId, long accountPurseId, BigDecimal transactionAmount,
			Map<LedgerEntryType, BigDecimal> transactionFees, PurseBalanceType purseBalanceType, boolean allowPartialAuth) {

		return getAccountPurseGroup(purseId).debitPurseBalance(accountPurseId, transactionAmount, transactionFees, purseBalanceType,
				allowPartialAuth);
	}

	/*
	 * Partial allowPartialAuth should be always false for all the transactions other than redeem For redeem
	 * allowPartialAuth value is pulled from the produtc_purse attributes
	 */
	@Override
	public AccountPurseGroupUpdate debitPurseBalance(long purseId, BigDecimal transactionAmount,
			Map<LedgerEntryType, BigDecimal> transactionFees, PurseBalanceType purseBalanceType, boolean allowPartialAuth) {

		return getAccountPurseGroup(purseId).debitPurseBalance(transactionAmount, transactionFees, purseBalanceType, allowPartialAuth);
	}

	@Override
	public void updateLimitsAndFee(long purseId, Map<String, Object> fees, Map<String, Object> limits) {
		getAccountPurseGroup(purseId).updateLimitsAndFee(fees, limits);
	}

	@Override
	public AccountPurseGroupUpdate updatePurseStatus(long purseId, AccountPurseGroupStatus groupStatus) {
		AccountPurseGroup accountPurseGroup = this.accountPurseGroupByPurseId.get(purseId);

		if (isNull(accountPurseGroup)) {
			throw DomainExceptionFactory.from(DomainExceptionType.DOMAIN_VALIDATION_FAILED,
					"Unable to locate account purse group for given purseName.");
		}

		return accountPurseGroup.updatePurseStatus(groupStatus);
	}

	@Override
	public AccountPurseGroupUpdate revertPurseBalance(long purseId, List<LedgerEntry> ledgerEntries,
			Map<LedgerEntryType, BigDecimal> transactionFees, PurseBalanceType purseBalanceType) {

		return getAccountPurseGroup(purseId).revertPurseBalance(ledgerEntries, transactionFees, purseBalanceType);

	}

	@Override
	public void setMaxPurseBalance(long purseId, BigDecimal balance) {
		getAccountPurseGroup(purseId).setMaxBalance(balance);
	}

	@Override
	public AccountPurseGroupUpdate debitLockedPurseBalance(List<RedemptionLockEntity> redemptionLocks,
			Map<LedgerEntryType, BigDecimal> transactionFees, long purseId, BigDecimal unlockTransactionAmount,
			BigDecimal lockTransactionAmount) {
		return getAccountPurseGroup(purseId).debitLockedPurseBalance(redemptionLocks, transactionFees, unlockTransactionAmount,
				lockTransactionAmount);
	}

	private AccountPurseGroup getAccountPurseGroup(long purseId) {
		AccountPurseGroup accountPurseGroup = this.accountPurseGroupByPurseId.get(purseId);
		if (isNull(accountPurseGroup)) {
			log.info("Unable to locate account purse group for given purseId", purseId);
			throw new ServiceException(SpilExceptionMessages.ACCOUNT_PURSE_GROUP_NOT_FOUND, ResponseCodes.INVALID_PURSE);
		}
		return accountPurseGroup;
	}

	public List<AccountPurseGroupView> getAccountPurseBalances(boolean includeFutureEffectivePurses, boolean includeZeroBalancePurses) {
		return this.purseIds.stream()
			.map(purseId -> getAccountPurseGroup(purseId).getAccountPurseBalances(includeFutureEffectivePurses, includeZeroBalancePurses))
			.collect(Collectors.toList());
	}

	public Optional<AccountPurseGroupUpdate> updateAccountPurseGroup(long purseId, //
			List<AccountPurseOperationPartial> partials, //
			Map<LedgerEntryType, BigDecimal> transactionFees, PurseBalanceType purseBalanceType, //
			OperationType operationType, //
			boolean allowPartialAuth, Optional<AccountPurseGroupStatus> groupStatus) {

		return this.accountPurseGroupByPurseId.get(purseId)
			.updateAccountPurseGroup(partials, transactionFees, purseBalanceType, operationType, allowPartialAuth, groupStatus);
	}

	@Override
	public BigDecimal getDefaultPurseAvailableBalance() {

		Optional<AccountPurseGroup> optionalAccountPurseGroup = this.accountPurseGroupByPurseId.values()
			.stream()
			.filter(AccountPurseGroup::isDefaultPurse)
			.findFirst();

		if (optionalAccountPurseGroup.isPresent()) {
			return optionalAccountPurseGroup.get()
				.getAvailablePurseBalance();
		} else {
			log.info("Unable to locate default account Purse Group");
			throw new ServiceException(SpilExceptionMessages.ACCOUNT_PURSE_GROUP_NOT_FOUND, ResponseCodes.INVALID_PURSE);
		}
	}

}