package com.incomm.cclp.account.domain.model;

import static com.incomm.cclp.account.util.CodeUtil.isNotEmpty;
import static com.incomm.cclp.account.util.CodeUtil.isNotNull;
import static com.incomm.cclp.account.util.CodeUtil.isNull;
import static com.incomm.cclp.account.util.CodeUtil.isZero;
import static com.incomm.cclp.account.util.CodeUtil.not;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.incomm.cclp.account.domain.exception.DomainExceptionFactory;
import com.incomm.cclp.account.domain.exception.DomainExceptionType;
import com.incomm.cclp.account.domain.factory.AccountAggregateRepository;
import com.incomm.cclp.account.domain.view.AccountPurseGroupView;
import com.incomm.cclp.account.domain.view.AccountPurseViewNew;
import com.incomm.cclp.config.SpringContext;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.exception.ServiceException;

import lombok.Builder;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class AccountPurseGroup implements AccountPurseGroupInterface {

	private final AccountPurseGroupKey accountPurseGroupKey;

	private final ProductPurse productPurse;

	private final List<AccountPurse> accountPurses;

	private Map<String, Object> usageLimits;
	private Map<String, Object> usageFees;

	private AccountPurseGroupStatus previousGroupStatus;
	private AccountPurseGroupStatus groupStatus;

	private Map<String, Object> newUsageLimits;
	private Map<String, Object> newUsageFees;

	@SuppressWarnings("unused")
	private LocalDateTime lastTransactionDate;

	private boolean isNew;
	private boolean accountPurseGroupUpdated = false;

	private BigDecimal maxPurseBalance;

	@Builder
	public AccountPurseGroup(AccountPurseGroupKey accountPurseGroupKey, //
			ProductPurse productPurse, //
			Map<String, Object> usageLimits, //
			Map<String, Object> usageFees, //
			AccountPurseGroupStatus groupStatus, //
			LocalDateTime lastTransactionDate, //
			List<AccountPurse> accountPurses, //
			boolean isNew) {
		super();
		this.accountPurseGroupKey = accountPurseGroupKey;
		this.productPurse = productPurse;
		this.lastTransactionDate = lastTransactionDate;
		this.accountPurses = accountPurses;
		this.isNew = isNew;

		this.groupStatus = groupStatus;
		this.previousGroupStatus = groupStatus;

		this.usageFees = usageFees;
		this.newUsageFees = usageFees;

		this.usageLimits = usageLimits;
		this.newUsageLimits = usageLimits;
	}

	@Override
	public long getPurseId() {
		return this.accountPurseGroupKey.getPurseId();
	}

	@Override
	public String getPurseName() {
		return productPurse.getPurseName();
	}

	@Override
	public void updateLimitsAndFee(Map<String, Object> fees, Map<String, Object> limits) {
		this.newUsageFees = fees;
		this.newUsageLimits = limits;
		this.accountPurseGroupUpdated = true;
	}

	@Override
	public void setMaxBalance(BigDecimal balance) {
		log.info("Max Purse balance is assigned with amount of {}", balance);
		this.maxPurseBalance = balance;
	}

	@Override
	public void validatePurseStatus() {
		if (not(groupStatus.isActive())) {
			log.info("purse status is inactive for purseId:{}", this.accountPurseGroupKey.getPurseId());
			throw new ServiceException(SpilExceptionMessages.ACCOUNTPURSENOTACTIVE, ResponseCodes.ACCOUNT_PURSE_NOT_ACTIVE);
		}
	}

	////////////////////
	// CREDIT
	////////////////////

	@Override
	public AccountPurseGroupUpdate creditPurseBalance( //
			BigDecimal transactionAmount, //
			Map<LedgerEntryType, BigDecimal> transactionFees, //
			PurseBalanceType purseBalanceType, //
			boolean createPurseIfNoneExist) {

		AccountPurse accountPurse = getFarthestExpiringEffectivePurse(createPurseIfNoneExist);

		log.info("Credit amount of {} to farthest expiring purse {} ", transactionAmount, accountPurse.getPurseName());
		return creditPurseBalance(accountPurse.getAccountPurseKey()
			.getAccountPurseId(), transactionAmount, transactionFees, purseBalanceType);

	}

	@Override
	public AccountPurseGroupUpdate creditPurseBalance( //
			AccountPurseAltKeyAttributes attributes, //
			BigDecimal transactionAmount, //
			Map<LedgerEntryType, BigDecimal> transactionFees, //
			PurseBalanceType purseBalanceType, //
			boolean createPurseIfNoneExist) {

		AccountPurse accountPurse = getMatchingAccountPurse(attributes, createPurseIfNoneExist);

		return creditPurseBalance(accountPurse.getAccountPurseKey()
			.getAccountPurseId(), transactionAmount, transactionFees, purseBalanceType);
	}

	@Override
	public AccountPurseGroupUpdate creditPurseBalance( //
			long accountPurseId, //
			BigDecimal transactionAmount, //
			Map<LedgerEntryType, BigDecimal> transactionFees, //
			PurseBalanceType purseBalanceType) {

		boolean deductFeeFromMultipleAccountPurses = false;
		List<LedgerEntry> ledgerEntries = new ArrayList<>();

		BigDecimal fee = calculateCumulativeFee(transactionFees);
		validateCreditAmountWithFee(transactionAmount, fee);
		validateCreditAmountWithMaxBalance(transactionAmount.add(getMaxActiveAccountPurseBalance()));

		AccountPurse accountPurse = getMatchingAccountPurse(accountPurseId);
		log.info("Credit {} for accountPurse {}", transactionAmount, accountPurse.getPurseName());
		ledgerEntries.add(accountPurse.creditPurseBalance(transactionAmount, LedgerEntryType.TRANSACTION_AMOUNT, purseBalanceType));

		if (hasFee(transactionFees)) {
			validatePurseBalanceForDebit(accountPurse.getAvailableBalance(), BigDecimal.ZERO, fee, deductFeeFromMultipleAccountPurses);
			log.info("Fee applicable. Fee amount of {} applied", transactionFees);
			ledgerEntries.addAll(debitFees(accountPurse, transactionFees, purseBalanceType));
		}

		return this.getAccountPurseGroupUpdate(ledgerEntries);

	}

	private void validateCreditAmountWithFee(BigDecimal amount, BigDecimal fee) {
		if (isBalanceLessThan(amount, fee)) {
			log.warn("Credit operation reducing the purse balance.");
		}
	}

	private void validateCreditAmountWithMaxBalance(BigDecimal amount) {
		if (isBalanceLessThan(this.maxPurseBalance, amount)) {
			log.error("Credit amount exceding allowed max purse balance");
			throw new ServiceException(SpilExceptionMessages.ERROR_MAX_BALANCE_EXCEED, ResponseCodes.EXCEEDING_MAXIMUM_CARD_BALANCE);
		}
	}

	////////////////////
	// DEBIT
	////////////////////

	@Override
	public AccountPurseGroupUpdate debitPurseBalance( //
			AccountPurseAltKeyAttributes attributes, //
			BigDecimal transactionAmount, //
			Map<LedgerEntryType, BigDecimal> transactionFees, //
			PurseBalanceType purseBalanceType, //
			boolean allowPartialAuth) {

		AccountPurse accountPurse = getMatchingAccountPurse(attributes, false);

		return debitPurseBalance(accountPurse.getAccountPurseKey()
			.getAccountPurseId(), transactionAmount, transactionFees, purseBalanceType, allowPartialAuth);
	}

	@Override
	public AccountPurseGroupUpdate debitPurseBalance( //
			long accountPurseId, //
			BigDecimal transactionAmount, //
			Map<LedgerEntryType, BigDecimal> transactionFees, //
			PurseBalanceType purseBalanceType, //
			boolean allowPartialAuth) {

		List<LedgerEntry> ledgerEntries = new ArrayList<>();
		AccountPurse accountPurse = getMatchingAccountPurse(accountPurseId);
		validatePurseBalanceForDebit(accountPurse.getAvailableBalance(), transactionAmount, calculateCumulativeFee(transactionFees),
				allowPartialAuth);

		if (hasFee(transactionFees)) {
			log.info("Fee applicable. Fee amount of {} applied", transactionFees);
			ledgerEntries.addAll(debitFees(accountPurse, transactionFees, PurseBalanceType.BOTH));
		}

		ledgerEntries.add(accountPurse.debitPurseBalance(transactionAmount, LedgerEntryType.TRANSACTION_AMOUNT, purseBalanceType));
		return this.getAccountPurseGroupUpdate(ledgerEntries);
	}

	@Override
	public AccountPurseGroupUpdate debitPurseBalance( //
			BigDecimal transactionAmount, //
			Map<LedgerEntryType, BigDecimal> transactionFees, //
			PurseBalanceType purseBalanceType, //
			boolean allowPartialAuth) {

		List<LedgerEntry> ledgerEntries = new ArrayList<>();

		if (not(hasBalanceForDebit(transactionAmount, transactionFees) || allowPartialAuth)) {
			log.error("Error Cause: Insufficient Balance for {} accountPurse", this.productPurse.getPurseName());
			throw new ServiceException(SpilExceptionMessages.ERROR_INSUFFICIENT_BALANCE, ResponseCodes.INSUFFICIENT_FUNDS);
		}

		if (hasFee(transactionFees)) {
			log.info("Fee applicable. Fee amount of {} applied", transactionFees);
			ledgerEntries.addAll(debitFees(transactionFees, PurseBalanceType.BOTH));
		}

		ledgerEntries.addAll(debitPurseBalances(transactionAmount, LedgerEntryType.TRANSACTION_AMOUNT, purseBalanceType));

		return this.getAccountPurseGroupUpdate(ledgerEntries);
	}

	//////////////////////
	// Redemption Unlock
	//////////////////////

	@Override
	public AccountPurseGroupUpdate debitLockedPurseBalance(List<RedemptionLockEntity> redemptionLocks, long accountPurseId,
			Map<LedgerEntryType, BigDecimal> transactionFees, BigDecimal unlockTransactionAmount, BigDecimal lockTransactionAmount) {
		return null;
	}

	@Override
	public AccountPurseGroupUpdate debitLockedPurseBalance(List<RedemptionLockEntity> redemptionLocks,
			Map<LedgerEntryType, BigDecimal> transactionFees, BigDecimal transactionAmount, BigDecimal lockTransactionAmount) {

		List<LedgerEntry> ledgerEntries = new ArrayList<>();
		BigDecimal flatFee = transactionFees.get(LedgerEntryType.FLAT_FEE);
		BigDecimal percentFee = transactionFees.get(LedgerEntryType.PERCENT_FEE);

		for (RedemptionLockEntity lock : redemptionLocks) {
			AccountPurse accountPurse = getMatchingAccountPurse(lock.getRedemptionLockKey()
				.getAccountPurseId());

			BigDecimal purseLockedAmount = lock.getAuthorizedAmount();

			if (isBalanceGreaterThanOrEqual(transactionAmount, purseLockedAmount)) {
				ledgerEntries.add(accountPurse.debitPurseBalanceForUnlock(purseLockedAmount, purseLockedAmount,
						LedgerEntryType.TRANSACTION_AMOUNT, PurseBalanceType.BOTH));
				transactionAmount = transactionAmount.subtract(purseLockedAmount);
			} else {
				ledgerEntries.add(accountPurse.debitPurseBalanceForUnlock(transactionAmount, purseLockedAmount,
						LedgerEntryType.TRANSACTION_AMOUNT, PurseBalanceType.BOTH));
				transactionAmount = transactionAmount.subtract(transactionAmount);
			}

			flatFee = debitAmount(accountPurse, LedgerEntryType.FLAT_FEE, flatFee, PurseBalanceType.BOTH, ledgerEntries);
			percentFee = debitAmount(accountPurse, LedgerEntryType.PERCENT_FEE, percentFee, PurseBalanceType.BOTH, ledgerEntries);
			transactionAmount = debitAmount(accountPurse, LedgerEntryType.TRANSACTION_AMOUNT, transactionAmount, PurseBalanceType.BOTH,
					ledgerEntries);
		}

		return this.getAccountPurseGroupUpdate(ledgerEntries);
	}

	////////////////////
	// Reversal
	////////////////////

	@Override
	public AccountPurseGroupUpdate revertPurseBalance(//
			List<LedgerEntry> logLedgerEntries, //
			Map<LedgerEntryType, BigDecimal> transactionFees, //
			PurseBalanceType purseBalanceType) {

		List<LedgerEntry> updateLedgerEntries = new ArrayList<>();

		final boolean allowPartialAuth = false;

		logLedgerEntries.forEach(ledgerEntry -> {

			AccountPurse accountPurse = getMatchingAccountPurse(ledgerEntry.getAccountPurseKey()
				.getAccountPurseId());
			OperationType operationType = ledgerEntry.getOperationType()
				.getReversalOperationType();

			if (operationType == OperationType.CREDIT) {
				log.info("Credit for reversal. Transaction amount is : {}", ledgerEntry.getTransactionAmount());
				LedgerEntry ledgerEntryUpdate = accountPurse.creditPurseBalance(ledgerEntry.getTransactionAmount(),
						ledgerEntry.getLedgerEntryType(), purseBalanceType);
				updateLedgerEntries.add(ledgerEntryUpdate);
			}

			if (operationType == OperationType.DEBIT) {
				log.info("Dedit for reversal. Transaction amount is : {}", operationType.toString(), ledgerEntry.getTransactionAmount());
				validatePurseBalanceForDebit(accountPurse.getAvailableBalance(), ledgerEntry.getTransactionAmount(),
						calculateCumulativeFee(transactionFees), allowPartialAuth);
				LedgerEntry ledgerEntryUpdate = accountPurse.debitPurseBalance(ledgerEntry.getTransactionAmount(),
						ledgerEntry.getLedgerEntryType(), purseBalanceType);
				updateLedgerEntries.add(ledgerEntryUpdate);
			}

		});
		return this.getAccountPurseGroupUpdate(updateLedgerEntries);
	}

	public Optional<AccountPurseGroupUpdate> updateAccountPurseGroup(//
			List<AccountPurseOperationPartial> partials, //
			Map<LedgerEntryType, BigDecimal> transactionFees, //
			PurseBalanceType purseBalanceType, //
			OperationType operationType, //
			boolean allowPartialAuth, Optional<AccountPurseGroupStatus> groupStatus) {

		if (groupStatus.isPresent() && not(this.groupStatus.equals(groupStatus.get()))) {
			log.info("Purse status update. Status set to {}", groupStatus.get());
			this.updatePurseStatusOnly(groupStatus.get());
		}

		List<LedgerEntry> updateLedgerEntries = new ArrayList<>();

		if (hasFee(transactionFees)) {
			log.info("Fee applicable. Fee amount of {} applied", transactionFees);
			updateLedgerEntries.addAll(debitFees(transactionFees, PurseBalanceType.BOTH));
		}

		partials.forEach(partial -> processPurseOperationPartial(partial, purseBalanceType, operationType, allowPartialAuth)
			.ifPresent(updateLedgerEntries::add));

		return isNotEmpty(updateLedgerEntries) || this.areAccountPurseUpdated() || this.accountPurseGroupUpdated
				? Optional.of(this.getAccountPurseGroupUpdate(updateLedgerEntries))
				: Optional.empty();
	}

	private Optional<LedgerEntry> processPurseOperationPartial(AccountPurseOperationPartial partial, PurseBalanceType purseBalanceType,
			OperationType operationType, boolean allowPartialAuth) {

		if (operationType == OperationType.CREDIT) {
			log.debug("Partial credit on account purse id: {}. Credit amount: {}", partial.getAccountPurseId(), partial.getAmount());
			return processPurseOperationPartialCredit(partial, purseBalanceType);
		}

		if (operationType == OperationType.DEBIT) {
			log.debug("Partial debit on account purse id: {}. Debit amount: {}", partial.getAccountPurseId(), partial.getAmount());
			return processPurseOperationPartialDebit(partial, purseBalanceType, allowPartialAuth);
		}
		return Optional.empty();
	}

	private Optional<LedgerEntry> processPurseOperationPartialCredit(AccountPurseOperationPartial partial,
			PurseBalanceType purseBalanceType) {

		AccountPurse accountPurse = getMatchingAccountPurse(partial, true);

		if (not(accountPurse.isNew()) && //
				isZero(accountPurse.getAvailableBalance()) && //
				isZero(accountPurse.getLedgerBalance()) && //
				isZero(partial.getAmount())) {
			return Optional.empty();
		}

		return Optional.of(accountPurse.creditPurseBalance(partial.getAmount(), partial.getLedgerEntryType(), purseBalanceType));
	}

	private Optional<LedgerEntry> processPurseOperationPartialDebit(AccountPurseOperationPartial partial, PurseBalanceType purseBalanceType,
			boolean allowPartialAuth) {

		final AccountPurse accountPurse = getMatchingAccountPurse(partial, false);
		final BigDecimal fees = BigDecimal.ZERO;

		if (isZero(partial.getAmount())) {
			return Optional.empty();
		}

		validatePurseBalanceForDebit(accountPurse.getAvailableBalance(), partial.getAmount(), fees, allowPartialAuth);
		return Optional.of(accountPurse.debitPurseBalance(partial.getAmount(), partial.getLedgerEntryType(), purseBalanceType));
	}

	public AccountPurseGroupView getAccountPurseBalances(boolean includeFutureEffectivePurses, boolean includeZeroBalancePurses) {

		log.info("Get Account Purse Group View for pursename:{}", this.productPurse.getPurseName());
		Predicate<AccountPurse> includefutureEffectivePredicate = accountPurse -> includeFutureEffectivePurses
				|| accountPurse.isEffective();
		Predicate<AccountPurse> includeZeroBalancePredicate = accountPurse -> includeZeroBalancePurses
				|| not(accountPurse.isZeroBalancePurse());

		List<AccountPurseViewNew> purseViews = this.accountPurses.stream()
			.filter(accountPurse -> !accountPurse.isExpired())
			.filter(includefutureEffectivePredicate)
			.filter(includeZeroBalancePredicate)
			.map(this::mapToAccountPurseView)
			.collect(Collectors.toList());

		return AccountPurseGroupView.builder()
			.accountPurseGroupKey(accountPurseGroupKey)
			.lastTransactionDate(this.lastTransactionDate)
			.productPurse(productPurse)
			.usageFees(this.usageFees)
			.usageLimits(this.usageLimits)
			.accountPurses(purseViews)
			.groupStatus(this.groupStatus)
			.build();
	}

	private void validatePurseBalanceForDebit(BigDecimal balance, BigDecimal transactionAmount, BigDecimal fee, boolean allowPartialAuth) {

		BigDecimal amount = transactionAmount.add(fee);
		log.info("validateBalance: Balance: {}, debit amount: {}, allowPartialAuth: {}", balance, amount, allowPartialAuth);

		if (isBalanceLessThan(balance, amount) && !allowPartialAuth) {
			log.info("Insufficient funds for debit. Transaction amount is : {} and Avaialable balance: {}", amount, balance);
			throw new ServiceException(SpilExceptionMessages.ERROR_INSUFFICIENT_BALANCE, ResponseCodes.INSUFFICIENT_FUNDS);
		}
	}

	private boolean hasBalanceForDebit(BigDecimal transactionAmount, Map<LedgerEntryType, BigDecimal> transactionFees) {
		BigDecimal totalFee = calculateCumulativeFee(transactionFees);
		BigDecimal maxActiveAccountPurseBalance = getMaxActiveAccountPurseBalance();

		BigDecimal amount = transactionAmount.add(totalFee);
		log.info("Balance check for debit,  Active account purse balance: {}  and transaction amount: {}", maxActiveAccountPurseBalance,
				amount);
		return maxActiveAccountPurseBalance.compareTo(amount) >= 0;
	}

	private boolean hasBalanceToDebitFees(Map<LedgerEntryType, BigDecimal> transactionFees) {
		BigDecimal totalFee = calculateCumulativeFee(transactionFees);
		BigDecimal maxActiveAccountPurseBalance = getMaxActiveAccountPurseBalance();

		log.info("Balance check for fee deduction. Active account purse balance: {} and total fee is {}", maxActiveAccountPurseBalance,
				totalFee);
		return maxActiveAccountPurseBalance.compareTo(totalFee) >= 0;
	}

	private List<LedgerEntry> debitPurseBalances(BigDecimal transactionAmount, LedgerEntryType ledgerEntryType,
			PurseBalanceType purseBalanceType) {

		log.info("Get ledger entries for debit");
		final List<LedgerEntry> ledgerEntries = new ArrayList<>();
		BigDecimal remainingTransactionAmount = transactionAmount;

		List<AccountPurse> sortedAccountPurses = getSortedDebitEligiblePurses();

		for (AccountPurse accountPurse : sortedAccountPurses) {
			if (accountPurse.hasBalanceForDebit(remainingTransactionAmount)) {
				if (isBalanceGreaterThanZero(remainingTransactionAmount)) {
					ledgerEntries.add(accountPurse.debitPurseBalance(remainingTransactionAmount, ledgerEntryType, purseBalanceType));
				}
				break;
			} else {
				BigDecimal amount = accountPurse.getAvailableBalance();
				ledgerEntries.add(accountPurse.debitPurseBalance(amount, ledgerEntryType, purseBalanceType));
				remainingTransactionAmount = remainingTransactionAmount.subtract(amount);
			}
		}
		return ledgerEntries;
	}

	private List<LedgerEntry> debitFees(Map<LedgerEntryType, BigDecimal> transactionFees, PurseBalanceType purseBalanceType) {
		List<LedgerEntry> ledgerEntries = new ArrayList<>();

		log.info("Debit fee if any");
		if (not(hasBalanceToDebitFees(transactionFees))) {
			log.error("Error Cause: Insufficient Balance to Debit Fees for {} accountPurse", this.productPurse.getPurseName());
			throw new ServiceException(SpilExceptionMessages.ERROR_INSUFFICIENT_BALANCE, ResponseCodes.INSUFFICIENT_FUNDS);
		}

		if (transactionFees.containsKey(LedgerEntryType.FLAT_FEE)) {
			log.info("Flat fee: {}", transactionFees.get(LedgerEntryType.FLAT_FEE));
			ledgerEntries
				.addAll(debitPurseBalances(transactionFees.get(LedgerEntryType.FLAT_FEE), LedgerEntryType.FLAT_FEE, purseBalanceType));
		}

		if (transactionFees.containsKey(LedgerEntryType.PERCENT_FEE)) {
			log.info("Percentage fee: {}", transactionFees.get(LedgerEntryType.PERCENT_FEE));
			ledgerEntries.addAll(
					debitPurseBalances(transactionFees.get(LedgerEntryType.PERCENT_FEE), LedgerEntryType.PERCENT_FEE, purseBalanceType));
		}

		return ledgerEntries;
	}

	private List<LedgerEntry> debitFees(AccountPurse accountPurse, Map<LedgerEntryType, BigDecimal> transactionFees,
			PurseBalanceType purseBalanceType) {
		List<LedgerEntry> ledgerEntries = new ArrayList<>();

		log.info("Debit fee if any");
		if (transactionFees.containsKey(LedgerEntryType.FLAT_FEE)) {
			log.info("Flat fee: {}", transactionFees.get(LedgerEntryType.FLAT_FEE));
			ledgerEntries.add(accountPurse.debitPurseBalance(transactionFees.get(LedgerEntryType.FLAT_FEE), LedgerEntryType.FLAT_FEE,
					purseBalanceType));
		}

		if (transactionFees.containsKey(LedgerEntryType.PERCENT_FEE)) {
			log.info("Percentage fee:{}", transactionFees.get(LedgerEntryType.PERCENT_FEE));
			ledgerEntries.add(accountPurse.debitPurseBalance(transactionFees.get(LedgerEntryType.PERCENT_FEE), LedgerEntryType.PERCENT_FEE,
					purseBalanceType));
		}

		return ledgerEntries;
	}

	private Optional<AccountPurse> getMatchingAccountPurse(AccountPurseAltKeyAttributes attributes) {
		log.info("Look for matching account purse with matching attributes of {}", attributes.toString());
		return this.accountPurses.stream()
			.filter(accountPurse -> accountPurse.getAccountPurseAltKeyAttributes()
				.equals(attributes))
			.findFirst();
	}

	private AccountPurse getMatchingAccountPurse(AccountPurseOperationPartial partial, boolean createPurseIfNoneExist) {

		if (partial.hasAccountPurseId() && hasMatchingAccountPurse(partial.getAccountPurseId()
			.get())) {
			log.info("Look for matching account purse with matching account purse id {}", partial.getAccountPurseId());
			return this.getMatchingAccountPurse(partial.getAccountPurseId()
				.get());
		}

		if (partial.getAttributes()
			.isPresent()) {
			log.info("Look for matching account purse with matching attributes of {}", partial.getAttributes()
				.toString());
			return this.getMatchingAccountPurse(partial.getAttributes()
				.get(), createPurseIfNoneExist);
		}

		log.warn("encountered AccountPurseOperationPartial with no account purse id or attributes:{}", partial.toString());
		throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);

	}

	private AccountPurse createAccountPurse(AccountPurseAltKeyAttributes attributes) {
		log.info("Create new account purse {}", productPurse.getPurseName());
		AccountAggregateRepository repository = SpringContext.getBean(AccountAggregateRepository.class);

		AccountPurse accountPurse = AccountPurse.builder()
			.accountPurseKey(AccountPurseKey.from(accountPurseGroupKey.getAccountId(), accountPurseGroupKey.getPurseId(),
					repository.getNextAccountPurseId()))
			.accountPurseAltKeyAttributes(attributes)
			.purseTypeId(productPurse.getPurseType()
				.getPurseTypeId())
			.purseName(productPurse.getPurseName())
			.firstLoadDate(LocalDateTime.now())
			.minorUnits(productPurse.getCurrencyMinorUnits())
			.availableBalance(BigDecimal.ZERO)
			.ledgerBalance(BigDecimal.ZERO)
			.isNew(true)
			.build();

		accountPurse.setNew(true);

		this.accountPurses.add(accountPurse);
		return accountPurse;
	}

	private boolean hasFee(Map<LedgerEntryType, BigDecimal> transactionFee) {
		return not(transactionFee.isEmpty());
	}

	private boolean hasMatchingAccountPurse(long accountPurseId) {
		log.info("Check if account purse exists with id {}", accountPurseId);
		return this.accountPurses.stream()
			.anyMatch(accountPurse -> accountPurse.getAccountPurseKey()
				.getAccountPurseId() == accountPurseId);
	}

	private AccountPurse getMatchingAccountPurse(long accountPurseId) {
		log.info("Look for account purse with id {}", accountPurseId);
		Optional<AccountPurse> matchingAccountPurse = this.accountPurses.stream()
			.filter(p -> p.getAccountPurseKey()
				.getAccountPurseId() == accountPurseId)
			.findFirst();

		log.info("AccountPurse with accountPurseId {} is present.", accountPurseId);
		return matchingAccountPurse.orElseThrow(() -> {
			log.error("Unable to locate the account purse with accountPurseId: " + accountPurseId);
			return new ServiceException(SpilExceptionMessages.INVALID_PURSE, ResponseCodes.INVALID_PURSE);
		});
	}

	private List<AccountPurseUpdateNew> getUpdatedAccountPurses() {
		return this.accountPurses.stream()
			.filter(AccountPurse::isDirty)
			.map(this::map)
			.collect(Collectors.toList());
	}

	private boolean areAccountPurseUpdated() {
		return this.accountPurses.stream()
			.filter(AccountPurse::isDirty)
			.count() > 0;
	}

	private AccountPurseUpdateNew map(AccountPurse accountPurse) {
		return AccountPurseUpdateNew.builder()
			.accountPurseKey(accountPurse.getAccountPurseKey())
			.attributes(accountPurse.getAccountPurseAltKeyAttributes())
			.previousAvailableBalance(accountPurse.getOriginalAvailableBalance())
			.previousLedgerBalance(accountPurse.getOriginalLedgerBalance())
			.newAvailableBalance(accountPurse.getAvailableBalance())
			.newLedgerBalance(accountPurse.getLedgerBalance())
			.isNew(accountPurse.isNew())
			.productId(this.productPurse.getProductId())
			.build();
	}

	public Optional<AccountPurse> getFarthestExpiringEffectivePurse() {
		return this.accountPurses.stream()
			.filter(AccountPurse::isEffective)
			.sorted(EXPPIRY_DATE_COMPARATOR_DESC)
			.findFirst();
	}

	private AccountPurse getMatchingAccountPurse(AccountPurseAltKeyAttributes attributes, boolean createPurseIfNoneExist) {
		Optional<AccountPurse> matchingAccountPurse = getMatchingAccountPurse(attributes);

		if (matchingAccountPurse.isPresent()) {
			return matchingAccountPurse.get();
		}

		if (createPurseIfNoneExist) {
			return this.createAccountPurse(attributes);

		}

		log.info("Unable to locate the account purse with attributes: {}", attributes.toString());
		throw new ServiceException(SpilExceptionMessages.INVALID_PURSE, ResponseCodes.INVALID_PURSE);

	}

	public List<AccountPurse> getSortedDebitEligiblePurses() {
		return this.accountPurses.stream()
			.filter(AccountPurse::isUnexpiredEffectivePurse)
			.filter(p -> p.getAvailableBalance()
				.compareTo(BigDecimal.ZERO) > 0)
			.sorted(EXPPIRY_DATE_COMPARATOR_ASC)
			.collect(Collectors.toList());
	}

	public AccountPurseGroupUpdate updatePurseStatus(AccountPurseGroupStatus groupStatus) {

		if (this.groupStatus.equals(groupStatus)) {
			log.error("No update required on purse status: {}", groupStatus.toString());
			throw DomainExceptionFactory.from(DomainExceptionType.DOMAIN_VALIDATION_FAILED,
					"No update required on purse status: " + groupStatus.toString());
		}

		this.updatePurseStatusOnly(groupStatus);

		return this.getAccountPurseGroupUpdate(Collections.emptyList(), Collections.emptyList());

	}

	private void updatePurseStatusOnly(AccountPurseGroupStatus newGroupStatus) {

		this.groupStatus = newGroupStatus;
		this.accountPurseGroupUpdated = true;
	}

	private AccountPurseGroupUpdate getAccountPurseGroupUpdate(List<LedgerEntry> ledgerEntries) {
		return this.getAccountPurseGroupUpdate(ledgerEntries, this.getUpdatedAccountPurses());
	}

	private AccountPurseGroupUpdate getAccountPurseGroupUpdate(List<LedgerEntry> ledgerEntries, List<AccountPurseUpdateNew> purseUpdates) {

		log.info("update Account purse group using updated ledger entries and purses updates");
		return AccountPurseGroupUpdate.builder()
			.accountPurseGroupKey(accountPurseGroupKey)
			.accountPurseUpdates(purseUpdates)
			.ledgerEntries(ledgerEntries)
			.isNew(this.isNew)
			.previousUsageFee(this.usageFees)
			.previousUsageLimit(this.usageLimits)
			.newUsageLimit(this.newUsageLimits)
			.newUsageFee(this.newUsageFees)
			.previousStatus(this.previousGroupStatus)
			.newStatus(this.groupStatus)
			.purseType(this.productPurse.getPurseType())
			.isDefault(this.productPurse.isDefault())
			.currencyCode(this.productPurse.getCurrencyId())
			.build();

	}

	private AccountPurse getFarthestExpiringEffectivePurse(boolean createPurseIfNoneExist) {
		Optional<AccountPurse> accountPurse = getFarthestExpiringEffectivePurse();

		if (accountPurse.isPresent()) {
			log.info("Farthest expiring purse is {} with account purse id {} ", accountPurse.get()
				.getPurseName(),
					accountPurse.get()
						.getAccountPurseKey()
						.getAccountPurseId());
			return accountPurse.get();
		}

		if (createPurseIfNoneExist) {
			log.info("Create new account purse {}", productPurse.getPurseName());
			return createAccountPurse(AccountPurseAltKeyAttributes.DEFAULT);
		}
		log.error("Unable to locate the account purse");
		throw new ServiceException(SpilExceptionMessages.INVALID_PURSE, ResponseCodes.INVALID_PURSE);
	}

	public BigDecimal getMaxActiveAccountPurseBalance() {
		if (this.groupStatus.isActive()) {
			log.info("Get max balance for active account purses for the purse {}", productPurse.getPurseName());
			return this.accountPurses.stream()
				.filter(p -> p.getExpiryDate() == null || p.getExpiryDate()
					.isAfter(LocalDateTime.now()))
				.map(AccountPurse::getAvailableBalance)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		} else {
			log.error("Purse is inactive");
			throw new ServiceException(SpilExceptionMessages.ACCOUNTPURSENOTACTIVE, ResponseCodes.ACCOUNT_PURSE_NOT_ACTIVE);
		}
	}

	private BigDecimal calculateCumulativeFee(Map<LedgerEntryType, BigDecimal> transactionFees) {
		return transactionFees.values()
			.stream()
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private boolean isBalanceLessThan(BigDecimal balance, BigDecimal transactionAmount) {
		return balance.subtract(transactionAmount)
			.compareTo(BigDecimal.ZERO) < 0;
	}

	private boolean isBalanceGreaterThanOrEqual(BigDecimal balance, BigDecimal transactionAmount) {
		return balance.subtract(transactionAmount)
			.compareTo(BigDecimal.ZERO) >= 0;
	}

	private boolean isBalanceGreaterThanZero(BigDecimal balance) {
		return balance.compareTo(BigDecimal.ZERO) > 0;
	}

	private BigDecimal debitAmount(AccountPurse accountPurse, LedgerEntryType ledgerEntryType, BigDecimal amount,
			PurseBalanceType purseBalanceType, List<LedgerEntry> ledgerEntries) {
		if (isBalanceGreaterThanZero(accountPurse.getAvailableBalance()) && isBalanceGreaterThanZero(amount)) {
			// TODO @Prakash why to return Zero !! Need attention
			if (accountPurse.hasBalanceForDebit(amount)) {
				log.info("Debit amount of {} from account purse id ", amount, accountPurse.getAccountPurseKey()
					.getAccountPurseId());
				ledgerEntries.add(accountPurse.debitPurseBalance(amount, ledgerEntryType, purseBalanceType));
				return BigDecimal.ZERO;
			} else {
				log.info("Debit partial amount of {} from account purse id ", accountPurse.getAvailableBalance(),
						accountPurse.getAccountPurseKey()
							.getAccountPurseId());
				ledgerEntries.add(accountPurse.debitPurseBalance(accountPurse.getAvailableBalance(), ledgerEntryType, purseBalanceType));
				return amount.subtract(accountPurse.getAvailableBalance());
			}
		}

		return amount;
	}

	private AccountPurseViewNew mapToAccountPurseView(AccountPurse accountPurse) {
		return AccountPurseViewNew.builder()
			.accountPurseKey(accountPurse.getAccountPurseKey())
			.accountPurseAltKeyAttributes(accountPurse.getAccountPurseAltKeyAttributes())
			.ledgerBalance(accountPurse.getLedgerBalance())
			.availableBalance(accountPurse.getAvailableBalance())
			.firstLoadDate(accountPurse.getFirstLoadDate())
			.build();
	}

	private static final Comparator<AccountPurse> EXPPIRY_DATE_COMPARATOR_DESC = (ap1, ap2) -> {
		if (isNull(ap1.getExpiryDate()) && isNull(ap2.getExpiryDate())) {
			return 0;
		}

		if (isNull(ap1.getExpiryDate()) && isNotNull(ap2.getExpiryDate())) {
			return -1;
		}

		if (isNotNull(ap1.getExpiryDate()) && isNull(ap2.getExpiryDate())) {
			return 1;
		}

		return ap2.getExpiryDate()
			.compareTo(ap1.getExpiryDate());
	};

	private static final Comparator<AccountPurse> EXPPIRY_DATE_COMPARATOR_ASC = (ap1, ap2) -> {

		if (isNull(ap1.getExpiryDate()) && isNull(ap2.getExpiryDate())) {
			return 0;
		}

		if (isNull(ap1.getExpiryDate()) && isNotNull(ap2.getExpiryDate())) {
			return 1;
		}

		if (isNotNull(ap1.getExpiryDate()) && isNull(ap2.getExpiryDate())) {
			return -1;
		}

		return ap1.getExpiryDate()
			.compareTo(ap2.getExpiryDate());

	};

	@Override
	public BigDecimal getAvailablePurseBalance() {
		return this.accountPurses.stream()
			.map(AccountPurse::getAvailableBalance)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	@Override
	public boolean isDefaultPurse() {
		return this.productPurse.isDefault();
	}
}
