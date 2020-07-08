package com.incomm.cclp.account.domain.model;

import static com.incomm.cclp.account.util.CodeUtil.isNotNull;
import static com.incomm.cclp.account.util.CodeUtil.isNull;
import static com.incomm.cclp.account.util.CodeUtil.not;
import static com.incomm.cclp.account.util.CodeUtil.setScale;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.function.BiFunction;

import com.incomm.cclp.account.util.DateTimeUtil;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.exception.ServiceException;

import lombok.Builder;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

// TODO Later @Pankaj remove data annotation from this class.
@Data
@Log4j2
public class AccountPurse implements AccountPurseInterface {

	private final AccountPurseKey accountPurseKey;
	private final AccountPurseAltKeyAttributes accountPurseAltKeyAttributes;

	private final int purseTypeId;
	private final String purseName;

	private final LocalDateTime effectiveDate;
	private final LocalDateTime expiryDate;
	private final String skuCode;

	private final BigDecimal originalLedgerBalance;
	private final BigDecimal originalAvailableBalance;

	private LocalDateTime firstLoadDate;

	private BigDecimal ledgerBalance;
	private BigDecimal availableBalance;

	private int minorUnits;

	private boolean isNew = false;

	private String topUpStatus;
	private boolean dirty = false;

	@Builder
	public AccountPurse(AccountPurseKey accountPurseKey, //
			AccountPurseAltKeyAttributes accountPurseAltKeyAttributes, //
			int purseTypeId, //
			String purseName, //
			LocalDateTime firstLoadDate, //
			BigDecimal ledgerBalance, //
			BigDecimal availableBalance, //
			int minorUnits, //
			String topUpStatus, //
			boolean isNew) {
		super();
		this.accountPurseKey = accountPurseKey;
		this.accountPurseAltKeyAttributes = accountPurseAltKeyAttributes;
		this.purseTypeId = purseTypeId;
		this.purseName = purseName;
		this.effectiveDate = accountPurseAltKeyAttributes.getEffectiveDate();
		this.expiryDate = accountPurseAltKeyAttributes.getExpiryDate();
		this.skuCode = accountPurseAltKeyAttributes.getSkuCode();
		this.firstLoadDate = firstLoadDate;
		this.originalAvailableBalance = availableBalance;
		this.originalLedgerBalance = ledgerBalance;
		this.minorUnits = minorUnits;
		this.ledgerBalance = ledgerBalance;
		this.availableBalance = availableBalance;
		this.topUpStatus = topUpStatus;
		this.isNew = isNew;
		this.dirty = isNew;
	}

	@Override
	public LedgerEntry creditPurseBalance(BigDecimal transactionAmount, LedgerEntryType ledgerEntryType,
			PurseBalanceType purseBalanceType) {
		return this.updateAccountPurse(transactionAmount, ledgerEntryType, purseBalanceType, OperationType.CREDIT);
	}

	@Override
	public LedgerEntry debitPurseBalance(BigDecimal transactionAmount, LedgerEntryType ledgerEntryType, PurseBalanceType purseBalanceType) {
		if (not(hasBalanceForDebit(transactionAmount))) {
			log.info("Insufficient funds for debit. Transaction amount is : {} and Avaialable balance: {}", transactionAmount,
					this.availableBalance);
			throw new ServiceException(SpilExceptionMessages.ERROR_INSUFFICIENT_BALANCE, ResponseCodes.INSUFFICIENT_FUNDS);
		}

		return this.updateAccountPurse(transactionAmount, ledgerEntryType, purseBalanceType, OperationType.DEBIT);
	}

	@Override
	public LedgerEntry debitPurseBalanceAllowNegative(BigDecimal transactionAmount, LedgerEntryType ledgerEntryType,
			PurseBalanceType purseBalanceType) {
		return this.updateAccountPurse(transactionAmount, ledgerEntryType, purseBalanceType, OperationType.DEBIT);
	}

	@Override
	public LedgerEntry debitPurseBalanceForUnlock(BigDecimal transactionAmount, BigDecimal lockedAmount, LedgerEntryType ledgerEntryType,
			PurseBalanceType purseBalanceType) {
		this.availableBalance = this.availableBalance.add(lockedAmount);
		return this.updateAccountPurse(transactionAmount, ledgerEntryType, purseBalanceType, OperationType.DEBIT);
	}

	private LedgerEntry updateAccountPurse(BigDecimal transactionAmount, LedgerEntryType ledgerEntryType, PurseBalanceType purseBalanceType,
			OperationType operationType) {

		log.info(
				"Updating account purse, operation:{}, amount:{}, fields:{}, ledgerEntry:{}, on accountPurseId:{}, ledger:{}, available:{}",
				operationType, transactionAmount, purseBalanceType, ledgerEntryType, this.accountPurseKey.getAccountPurseId(),
				this.ledgerBalance, this.availableBalance);

		BigDecimal authAmount = transactionAmount;
		BigDecimal previousAvailableBalance = this.availableBalance;
		BigDecimal previousLedgerBalance = this.ledgerBalance;

		BigDecimal newAvailableBalance = this.availableBalance;
		BigDecimal newLedgerBalance = this.ledgerBalance;

		if (this.isAvailableBalanceUpdatedRequired(purseBalanceType)) {
			newAvailableBalance = this.updateAvailableBalance(operationType, transactionAmount);
		}
		if (this.isLedgerBalanceUpdateRequired(purseBalanceType)) {
			newLedgerBalance = this.updateLedgerBalance(operationType, transactionAmount);
		}

		return apply(LedgerEntry.builder()
			.accountPurseKey(this.accountPurseKey)
			.ledgerEntryType(ledgerEntryType)
			.operationType(operationType)
			.previousLedgerBalance(previousLedgerBalance)
			.previousAvailableBalance(previousAvailableBalance)
			.newLedgerBalance(newLedgerBalance)
			.newAvailableBalance(newAvailableBalance)
			.authorizedAmount(authAmount)
			.transactionAmount(transactionAmount)
			.build());

	}

	private BigDecimal updateAvailableBalance(OperationType operationType, BigDecimal transactionAmount) {
		return setScale(getOperationFunction(operationType).apply(this.ledgerBalance, transactionAmount), minorUnits);
	}

	private boolean isAvailableBalanceUpdatedRequired(PurseBalanceType purseBalanceType) {
		return purseBalanceType == PurseBalanceType.AVAILABLE_BALANCE || purseBalanceType == PurseBalanceType.BOTH;
	}

	private BigDecimal updateLedgerBalance(OperationType operationType, BigDecimal transactionAmount) {
		return setScale(getOperationFunction(operationType).apply(this.ledgerBalance, transactionAmount), minorUnits);
	}

	private boolean isLedgerBalanceUpdateRequired(PurseBalanceType purseBalanceType) {
		return purseBalanceType == PurseBalanceType.LEDGER_BALANCE || purseBalanceType == PurseBalanceType.BOTH;
	}

	private BiFunction<BigDecimal, BigDecimal, BigDecimal> getOperationFunction(OperationType operationType) {
		return operationType == OperationType.CREDIT ? BigDecimal::add : BigDecimal::subtract;
	}

	// find max purse balance check for credit authAmount
	private BigDecimal calculateCreditAuthAmount(BigDecimal transactionAmount, BigDecimal previousAvailableBalance,
			BigDecimal maxPurseBalance) {
		boolean bValidCredit = (maxPurseBalance.compareTo(previousAvailableBalance.add(transactionAmount)) >= 0) ? true : false;
		if (bValidCredit) {
			log.info("Transaction amount is under valid/authorized limit");
			return transactionAmount;
		} else {
			log.warn("Transaction amount exceeds than auth amount");
			return maxPurseBalance.subtract(previousAvailableBalance);
		}
	}

	@Override
	public boolean hasBalanceForDebit(BigDecimal transactionAmount) {
		log.debug("Check for available balance to support transaction ampount of {}", transactionAmount);
		return this.availableBalance.compareTo(transactionAmount) >= 0;
	}

	public boolean isZeroBalancePurse() {
		return this.availableBalance.compareTo(BigDecimal.ZERO) == 0;
	}

	private LedgerEntry apply(LedgerEntry ledgerEntry) {
		this.dirty = true;
		this.ledgerBalance = ledgerEntry.getNewLedgerBalance();
		this.availableBalance = ledgerEntry.getNewAvailableBalance();
		return ledgerEntry;
	}

	@Override
	public boolean isUnexpiredEffectivePurse() {
		return not(isExpired()) && isEffective();
	}

	public boolean isExpired() {
		return isNotNull(this.accountPurseAltKeyAttributes.getExpiryDate())
				&& DateTimeUtil.isPastDate(this.accountPurseAltKeyAttributes.getExpiryDate());
	}

	public boolean isEffective() {
		return isNull(this.accountPurseAltKeyAttributes.getEffectiveDate())
				|| DateTimeUtil.isPastDate(this.accountPurseAltKeyAttributes.getEffectiveDate());
	}
}
