package com.incomm.cclp.account.domain.model;

import static com.incomm.cclp.account.util.CodeUtil.isNotNull;
import static com.incomm.cclp.account.util.CodeUtil.isNull;
import static com.incomm.cclp.account.util.CodeUtil.not;
import static com.incomm.cclp.account.util.CodeUtil.setScale;
import static com.incomm.cclp.account.util.CodeUtil.wrapIfNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.incomm.cclp.account.domain.exception.DomainExceptionFactory;
import com.incomm.cclp.account.domain.exception.DomainExceptionType;
import com.incomm.cclp.account.util.CodeUtil;
import com.incomm.cclp.account.util.DateTimeUtil;
import com.incomm.cclp.config.SpringContext;
import com.incomm.cclp.dao.service.SequenceService;
import com.incomm.cclp.dto.AccountPurseUsageDto;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class AccountPurseAggregate {

	final long accountId;
	final Map<String, List<AccountPurse>> accountPursesByPurseName;
	final Map<Long, AccountPurseUsageDto> accountPurseUsageByPurseId;
	final Map<String, ProductPurse> productPurseByPurseName;
	final Map<Long, ProductPurse> productPurseByPurseId;
	final List<AccountPurse> expiredAccountPursesByPurseName;

	private AccountPurseAggregate(long accountId, List<ProductPurse> productPurses, List<AccountPurseUsageDto> accountPurseUsageDtos,
			List<AccountPurse> accountPurses, List<AccountPurse> expiredAccountPurses) {

		this.accountId = accountId;

		this.accountPursesByPurseName = wrapIfNull(accountPurses).stream()
			.collect(Collectors.groupingBy(accountPurse -> accountPurse.getPurseName()
				.toUpperCase()));

		this.productPurseByPurseName = wrapIfNull(productPurses).stream()
			.collect(Collectors.toMap(productPurse -> productPurse.getPurseName()
				.toUpperCase(), Function.identity()));

		this.productPurseByPurseId = wrapIfNull(productPurses).stream()
			.collect(Collectors.toMap(ProductPurse::getPurseId, Function.identity()));

		this.accountPurseUsageByPurseId = wrapIfNull(accountPurseUsageDtos).stream()
			.collect(Collectors.toMap(AccountPurseUsageDto::getPurseId, Function.identity()));

		this.expiredAccountPursesByPurseName = expiredAccountPurses;
	}

	public static AccountPurseAggregate from(long accountId, List<ProductPurse> productPurses,
			List<AccountPurseUsageDto> accountPurseUsageDtos, List<AccountPurse> accountPurses, List<AccountPurse> expiredAccountPurses) {
		return new AccountPurseAggregate(accountId, productPurses, accountPurseUsageDtos, accountPurses, expiredAccountPurses);
	}

	public PurseUpdate updatePurseStatus(final String purseName, final PurseStatusType purseStatus, final ZonedDateTime startDate,
			final ZonedDateTime endDate) {

		final LocalDateTime startDateLocal = DateTimeUtil.convert(startDate);
		final LocalDateTime endDateLocal = DateTimeUtil.convert(endDate);

		ProductPurse productPurse = this.getProductPurseByPurseName(purseName);
		AccountPurseUsageDto usageDto = this.findMatchAccountPurseUsage(purseName);

		if (usageDto == null) {
			throw DomainExceptionFactory.from(DomainExceptionType.DOMAIN_VALIDATION_FAILED,
					"Unable to locate the purse with given purseName:" + purseName);
		}

		PurseStatusType previousStatus = usageDto.getPurseStatus() == null ? null
				: PurseStatusType.byStatusCode(usageDto.getPurseStatus())
					.orElse(null);

		final LocalDateTime previousStartDate = usageDto.getStartDate();
		final LocalDateTime previousEndDate = usageDto.getEndDate();

		// do not update only if all 3 (status,startDate,endDate) are same
		if (CodeUtil.isEqual(previousStatus, purseStatus) && CodeUtil.isEqual(previousStartDate, startDateLocal)
				&& CodeUtil.isEqual(previousEndDate, endDateLocal)) {
			throw DomainExceptionFactory.from(DomainExceptionType.DOMAIN_VALIDATION_FAILED, "No update required on purse " + purseName);
		}

		return PurseUpdate.builder()
			.purseKey(PurseKey.from(this.accountId, productPurse.getPurseId()))
			.newStatus(purseStatus)
			.previousStatus(previousStatus)
			.newStartDate(startDateLocal)
			.previousStartDate(previousStartDate)
			.newEndDate(endDateLocal)
			.previousEndDate(previousEndDate)
			.build();
	}

	public AccountPurseUpdate updateAccountPurse( //
			final PurseUpdateActionType actionType, //
			final String purseName, //
			final Long accountPurseId, //
			final BigDecimal transactionAmount, //
			final ZonedDateTime effectiveDate, //
			final ZonedDateTime expiryDate, //
			final String skuCode, //
			final BigDecimal transactionFee, //
			final Map<String, Object> fees, // remove limits and fee once the logic is implemented
			final Map<String, Object> limits, // within the domain.
			final BigDecimal maxPurseBalance

	) {
		BigDecimal transactionAmountFeeAccounted = transactionAmount;
		LocalDateTime effectiveDateLocal = DateTimeUtil.convert(effectiveDate);
		LocalDateTime expiryDateLocal = DateTimeUtil.convert(expiryDate);

		BigDecimal cumulativePurseBalance = this.getCumulativePurseBalance(purseName);
		Optional<AccountPurse> accountPurseOptional = null;
		if (actionType == PurseUpdateActionType.valueOf("AUTO_ROLLOVER_DEBIT")) {
			accountPurseOptional = this.getMatchingExpiredAccountPurse(purseName, effectiveDateLocal, expiryDateLocal, skuCode,
					accountPurseId);
		} else {
			accountPurseOptional = this.getMatchingAccountPurse(purseName, effectiveDateLocal, expiryDateLocal, skuCode, accountPurseId);
		}

		AccountPurseKey accountPurseKey = null;
		ProductPurse productPurse = this.getProductPurseByPurseName(purseName);
		int minorUnits = productPurse.getCurrencyMinorUnits();
		String topupStatus = null;

		BigDecimal previousLedgerBalance = BigDecimal.ZERO;
		BigDecimal previousAvailableBalance = BigDecimal.ZERO;
		BigDecimal newLedgerBalance = BigDecimal.ZERO;
		BigDecimal newAvailableBalance = BigDecimal.ZERO;
		BigDecimal authorizedAmount = BigDecimal.ZERO;
		boolean isNewAccountPurse = false;
		boolean isNewAccountPurseUsage = false;

		AccountPurseUsageDto accountPurseUsage = findMatchAccountPurseUsage(purseName);
		isNewAccountPurseUsage = accountPurseUsage == null;
		AccountPurseGroupStatus groupStatus = getGroupStatus(accountPurseUsage);

		if (not(transactionFee.doubleValue() == 0)) {
			if (transactionAmount.compareTo(transactionFee) <= 0) {
				throw DomainExceptionFactory.from(DomainExceptionType.DOMAIN_VALIDATION_FAILED, "Insufficient Funds.");
			}
			transactionAmountFeeAccounted = transactionAmount.subtract(transactionFee);
		}

		if (accountPurseOptional.isPresent()) {
			AccountPurse accountPurse = accountPurseOptional.get();
			log.info("Found matching account purse with accountPurseKey:{}", accountPurse.getAccountPurseKey());

			accountPurseKey = accountPurse.getAccountPurseKey();
			previousLedgerBalance = accountPurse.getLedgerBalance();
			previousAvailableBalance = accountPurse.getAvailableBalance();
			effectiveDateLocal = accountPurse.getEffectiveDate();
			expiryDateLocal = accountPurse.getExpiryDate();

			switch (actionType) {
			case LOAD:
				newLedgerBalance = accountPurse.getLedgerBalance()
					.add(transactionAmountFeeAccounted);
				newAvailableBalance = accountPurse.getAvailableBalance()
					.add(transactionAmountFeeAccounted);
				authorizedAmount = transactionAmount;
				topupStatus = accountPurse.getTopUpStatus();
				break;
			case UNLOAD:
				newLedgerBalance = accountPurse.getLedgerBalance()
					.subtract(transactionAmountFeeAccounted);
				newAvailableBalance = accountPurse.getAvailableBalance()
					.subtract(transactionAmountFeeAccounted);
				authorizedAmount = transactionAmount;
				topupStatus = accountPurse.getTopUpStatus();
				break;
			case AUTORELOAD: // Added By Hari for AutoReload
				if (effectiveDateLocal.isAfter(LocalDateTime.now()))
					throw DomainExceptionFactory.from(DomainExceptionType.DOMAIN_VALIDATION_FAILED,
							"Unable to Reload the purse with Future EffectiveDate :" + purseName);
				if (cumulativePurseBalance.add(transactionAmountFeeAccounted)
					.compareTo(maxPurseBalance) > 0) {
					if (cumulativePurseBalance.compareTo(maxPurseBalance) >= 0)
						throw DomainExceptionFactory.from(DomainExceptionType.DOMAIN_VALIDATION_FAILED,
								"Cumulative Purse Balance is more than the maxPurseBalance : " + purseName);
					BigDecimal authAmount = maxPurseBalance.subtract(cumulativePurseBalance);
					newLedgerBalance = accountPurse.getLedgerBalance()
						.add(authAmount);
					newAvailableBalance = accountPurse.getAvailableBalance()
						.add(authAmount);
					authorizedAmount = authAmount;
				} else {
					newLedgerBalance = accountPurse.getLedgerBalance()
						.add(transactionAmountFeeAccounted);
					newAvailableBalance = accountPurse.getAvailableBalance()
						.add(transactionAmountFeeAccounted);
					authorizedAmount = transactionAmountFeeAccounted;
				}

				topupStatus = accountPurse.getTopUpStatus();
				break;

			case TOP_UP:
				if (accountPurse.getAvailableBalance()
					.compareTo(transactionAmountFeeAccounted) >= 1) {
					throw DomainExceptionFactory.from(DomainExceptionType.DOMAIN_VALIDATION_FAILED,
							"accountPurse balance is more than or equal to top-up transaction amount.");
				}
				authorizedAmount = transactionAmount.subtract(accountPurse.getLedgerBalance());
				newLedgerBalance = transactionAmountFeeAccounted;
				newAvailableBalance = accountPurse.getAvailableBalance()
					.add(authorizedAmount);
				topupStatus = accountPurse.getTopUpStatus();
				break;
			case AUTO_ROLLOVER_DEBIT:
				if (previousAvailableBalance.compareTo(transactionAmountFeeAccounted) >= 0) {
					newLedgerBalance = accountPurse.getLedgerBalance()
						.subtract(transactionAmountFeeAccounted);
					newAvailableBalance = accountPurse.getAvailableBalance()
						.subtract(transactionAmountFeeAccounted);
					authorizedAmount = transactionAmount;
				} else {
					newLedgerBalance = accountPurse.getLedgerBalance()
						.subtract(accountPurse.getLedgerBalance());
					newAvailableBalance = accountPurse.getAvailableBalance()
						.subtract(accountPurse.getAvailableBalance());
					authorizedAmount = accountPurse.getAvailableBalance();
				}
				topupStatus = "AUTOROLLOVER";
				break;

			case AUTO_ROLLOVER_CREDIT:
				if (cumulativePurseBalance.add(transactionAmountFeeAccounted)
					.compareTo(maxPurseBalance) > 0) {
					if (cumulativePurseBalance.compareTo(maxPurseBalance) >= 0)
						throw DomainExceptionFactory.from(DomainExceptionType.DOMAIN_VALIDATION_FAILED,
								"Cumulative Purse Balance is more than the maxPurseBalance : " + purseName);
					BigDecimal authAmount = maxPurseBalance.subtract(cumulativePurseBalance);
					newLedgerBalance = accountPurse.getLedgerBalance()
						.add(authAmount);
					newAvailableBalance = accountPurse.getAvailableBalance()
						.add(authAmount);
					authorizedAmount = authAmount;
				} else {
					newLedgerBalance = accountPurse.getLedgerBalance()
						.add(transactionAmountFeeAccounted);
					newAvailableBalance = accountPurse.getAvailableBalance()
						.add(transactionAmountFeeAccounted);
					authorizedAmount = transactionAmountFeeAccounted;
				}

				this.updateAccoutPurseBal(newLedgerBalance, newAvailableBalance, accountPurseKey, effectiveDateLocal, expiryDateLocal,
						accountPurse.getPurseTypeId(), accountPurse.getPurseName(), accountPurse.getSkuCode(),
						accountPurse.getTopUpStatus(), accountPurse);
				topupStatus = accountPurse.getTopUpStatus();
				break;
			default:
				break;
			}

		} else {
			accountPurseKey = AccountPurseKey.from(this.accountId, productPurse.getPurseId(), this.getNextAccountPurseId());
			isNewAccountPurse = true;
			BigDecimal creditAmount = transactionAmountFeeAccounted;
			BigDecimal authAmount = transactionAmount;

			if (actionType == PurseUpdateActionType.AUTO_TOP_UP) {
				if (maxPurseBalance.compareTo(cumulativePurseBalance) <= 0) {
					throw DomainExceptionFactory.from(DomainExceptionType.DOMAIN_VALIDATION_FAILED,
							"accountPurse balance is more than or equal to auto top-up transaction amount.");
				} else if (maxPurseBalance.compareTo(cumulativePurseBalance.add(transactionAmountFeeAccounted)) < 0) {
					creditAmount = transactionAmountFeeAccounted.subtract(cumulativePurseBalance.add(transactionAmountFeeAccounted)
						.subtract(maxPurseBalance));
					authAmount = creditAmount.add(transactionFee);
				}
				topupStatus = "AUTOTOPUP";
			}

			newLedgerBalance = creditAmount;
			newAvailableBalance = creditAmount;
			authorizedAmount = authAmount;

			log.info("Creating a new account purse with accountPurseKey:{}", accountPurseKey);
		}

		// The below condition should not happen as this is already validated in the common validators.
		if (newAvailableBalance.signum() == -1 || newLedgerBalance.signum() == -1) {
			log.error("Invalid domain state, negative available or ledger balance encountered:newAvailableBalance" + newAvailableBalance
					+ ", newLedgerBalance" + newLedgerBalance);
			throw DomainExceptionFactory.from(DomainExceptionType.SYSTEM_ERROR,
					"Invalid domain state, negative available or ledger balance encountered:newAvailableBalance" + newAvailableBalance
							+ ", newLedgerBalance" + newLedgerBalance);
		}

		return AccountPurseUpdate.builder()
			.accountPurseKey(accountPurseKey)
			.productId(productPurse.getProductId())
			.isNewAccountPurse(isNewAccountPurse)
			.previousLedgerBalance(setScale(previousLedgerBalance, minorUnits))
			.previousAvailableBalance(setScale(previousAvailableBalance, minorUnits))
			.newLedgerBalance(setScale(newLedgerBalance, minorUnits))
			.newAvailableBalance(setScale(newAvailableBalance, minorUnits))
			.authorizedAmount(setScale(authorizedAmount, minorUnits))
			.pursetype(productPurse.getPurseType())
			.currencyCode(productPurse.getCurrencyId())
			.skuCode(skuCode)
			.effectiveDate(effectiveDateLocal)
			.expiryDate(expiryDateLocal)
			.previousUsageFee(null)
			.previousUsageLimit(null)
			.isNewPurseUsage(isNewAccountPurseUsage)
			.newUsageFee(fees)
			.newUsageLimit(limits)
			.transactionAmount(transactionAmount)
			.newGroupStatus(groupStatus)
			.topupStatus(topupStatus)
			.build();

	}

	private void updateAccoutPurseBal(BigDecimal newLedgerBalance, BigDecimal newAvailableBalance, AccountPurseKey accountPurseKey,
			LocalDateTime effectiveDateLocal, LocalDateTime expiryDateLocal, int purseTypeId, String purseName, String skuCode,
			String topupStatus, AccountPurse accountPurse) {
		for (Entry<String, List<AccountPurse>> ap : this.accountPursesByPurseName.entrySet()) {
			if (ap.getValue()
				.contains(accountPurse))
				ap.getValue()
					.remove(accountPurse);
		}
		;
		this.accountPursesByPurseName.get(purseName)
			.add(AccountPurse.builder()
				.accountPurseKey(accountPurseKey)
				.availableBalance(newAvailableBalance)
				.ledgerBalance(newLedgerBalance)
				.accountPurseAltKeyAttributes(AccountPurseAltKeyAttributes.from(effectiveDateLocal, expiryDateLocal, skuCode))
				.purseName(purseName)
				.purseTypeId(purseTypeId)
				.topUpStatus(topupStatus)
				.build());
	}

	AccountPurseGroupStatus getGroupStatus(AccountPurseUsageDto accountPurseUsage) {
		if (accountPurseUsage == null) {
			return AccountPurseGroupStatus.from(PurseStatusType.ACTIVE);
		}

		Optional<PurseStatusType> purseStausOptional = PurseStatusType.byStatusCode(accountPurseUsage.getPurseStatus());

		return AccountPurseGroupStatus.from(purseStausOptional.orElse(PurseStatusType.ACTIVE));
	}

	Optional<AccountPurse> getMatchingAccountPurse(PurseUpdateActionType actionType, String purseName, LocalDateTime effectiveDate,
			LocalDateTime expiryDate, String skuCode, Long accountPurseId) {

		Optional<AccountPurse> accountPurse = getMatchingAccountPurse(purseName, effectiveDate, expiryDate, skuCode, accountPurseId);

		if (actionType == PurseUpdateActionType.LOAD || actionType == PurseUpdateActionType.UNLOAD
				|| actionType == PurseUpdateActionType.TOP_UP) {
			return accountPurse;
		} else {
			if (not(accountPurse.isPresent())) {
				return getFurtherestExpiringAccountPurse(purseName);
			}

		}
		return accountPurse;
	}

	private Optional<AccountPurse> getMatchingAccountPurse(String purseName, LocalDateTime effectiveDate, LocalDateTime expiryDate,
			String skuCode, Long accountPurseId) {

		List<AccountPurse> pursesWithMatchingName = wrapIfNull(this.accountPursesByPurseName.get(purseName.toUpperCase()));
		ProductPurse productPurse = getProductPurseByPurseName(purseName);

		if (CollectionUtils.isEmpty(pursesWithMatchingName)) {
			return Optional.empty();
		}

		if (isNotNull(accountPurseId)) {
			Optional<AccountPurse> matchingAccountPurse = pursesWithMatchingName.stream()
				.filter(accountPurse -> accountPurse.getAccountPurseKey()
					.getAccountPurseId() == accountPurseId)
				.findFirst();

			if (not(matchingAccountPurse.isPresent())) {
				throw DomainExceptionFactory.from(DomainExceptionType.INPUT_VALIDATION_FAILED,
						"Unable to locate the account purse with accountPurseId:" + accountPurseId);
			} else {
				if (productPurse.getPurseType() == PurseType.SKU && isNotNull(skuCode) && not(CodeUtil.isEqual(matchingAccountPurse.get()
					.getSkuCode(), skuCode))) {
					throw DomainExceptionFactory.from(DomainExceptionType.INPUT_VALIDATION_FAILED,
							"skuCode did not match with account purse skuCode");
				}
			}
			return matchingAccountPurse;
		}

		return pursesWithMatchingName.stream()
			.filter(accountPurse -> isEqual(accountPurse.getEffectiveDate(), effectiveDate)
					&& isEqual(accountPurse.getExpiryDate(), expiryDate) && CodeUtil.isEqual(accountPurse.getSkuCode(), skuCode))
			.findFirst();

	}

	Optional<AccountPurse> getFurtherestExpiringAccountPurse(String purseName) {
		List<AccountPurse> pursesWithMatchingName = wrapIfNull(this.accountPursesByPurseName.get(purseName.toUpperCase()));

//		if ((effectiveDate != null && expiryDate != null) || (effectiveDate != null && effectiveDate.isAfter(LocalDateTime.now()))
//				|| (expiryDate != null)) {
//			return pursesWithMatchingName.stream()
//				.filter(accountPurse -> isEqual(accountPurse.getEffectiveDate(), effectiveDate)
//						&& isEqual(accountPurse.getExpiryDate(), expiryDate) && CodeUtil.isEqual(accountPurse.getSkuCode(), skuCode))
//				.findFirst();
//		} else {
//			return pursesWithMatchingName.stream()
//				.filter(accountPurse -> accountPurse.getExpiryDate() == null)
//				.count() > 0 ? pursesWithMatchingName.stream()
//					.filter(accountPurse -> accountPurse.getExpiryDate() == null)
//					.findFirst()
//						: pursesWithMatchingName.stream()
//							.filter(accountPurse -> accountPurse.getExpiryDate() != null)
//							.max(Comparator.comparing(AccountPurse::getExpiryDate))
//							.map(Function.identity());
//		}

		return pursesWithMatchingName.stream()
			.filter(accountPurse -> accountPurse.getExpiryDate() == null)
			.count() > 0 ? pursesWithMatchingName.stream()
				.filter(accountPurse -> accountPurse.getExpiryDate() == null)
				.findFirst()
					: pursesWithMatchingName.stream()
						.filter(accountPurse -> accountPurse.getExpiryDate() != null)
						.max(Comparator.comparing(AccountPurse::getExpiryDate))
						.map(Function.identity());
	}

	Optional<AccountPurse> getMatchingExpiredAccountPurse(String purseName, LocalDateTime effectiveDate, LocalDateTime expiryDate,
			String skuCode, Long accountPurseId) {

		List<AccountPurse> pursesWithMatchingName = this.expiredAccountPursesByPurseName;
		ProductPurse productPurse = getProductPurseByPurseName(purseName);

		if (isNotNull(accountPurseId)) {
			Optional<AccountPurse> matchingAccountPurse = pursesWithMatchingName.stream()
				.filter(accountPurse -> accountPurse.getAccountPurseKey()
					.getAccountPurseId() == accountPurseId)
				.findFirst();

			if (not(matchingAccountPurse.isPresent())) {
				throw DomainExceptionFactory.from(DomainExceptionType.INPUT_VALIDATION_FAILED,
						"Unable to locate the account purse with accountPurseId:" + accountPurseId);
			} else {
				if (productPurse.getPurseType() == PurseType.SKU && isNotNull(skuCode) && not(CodeUtil.isEqual(matchingAccountPurse.get()
					.getSkuCode(), skuCode))) {
					throw DomainExceptionFactory.from(DomainExceptionType.INPUT_VALIDATION_FAILED,
							"skuCode did not match with account purse skuCode");
				}
			}
			return matchingAccountPurse;
		}

		if (CollectionUtils.isEmpty(pursesWithMatchingName)) {
			return Optional.empty();
		}

		if (effectiveDate != null && expiryDate != null)
			return pursesWithMatchingName.stream()
				.filter(accountPurse -> isEqual(accountPurse.getEffectiveDate(), effectiveDate)
						&& isEqual(accountPurse.getExpiryDate(), expiryDate) && CodeUtil.isEqual(accountPurse.getSkuCode(), skuCode))
				.findFirst();
		else {
			return pursesWithMatchingName.stream()
				.filter(accountPurse -> accountPurse.getExpiryDate() == null)
				.count() > 0 ? pursesWithMatchingName.stream()
					.filter(accountPurse -> accountPurse.getExpiryDate() == null)
					.findFirst()
						: pursesWithMatchingName.stream()
							.filter(accountPurse -> accountPurse.getExpiryDate() != null)
							.max(Comparator.comparing(AccountPurse::getExpiryDate))
							.map(Function.identity());

		}

	}

	private long getNextAccountPurseId() {
		SequenceService sequenceService = SpringContext.getBean(SequenceService.class);
		return sequenceService.getNextAccountPurseId();
	}

	boolean isEqual(LocalDateTime date1, LocalDateTime date2) {
		if (isNull(date1) && isNull(date2)) {
			return true;
		}
		if (isNull(date1) && isNotNull(date2) || isNotNull(date1) && isNull(date2)) {
			return false;
		}
		return date1.compareTo(date2) == 0;
	}

	ProductPurse getProductPurseByPurseName(String purseName) {
		return this.productPurseByPurseName.get(purseName == null ? null : purseName.toUpperCase());
	}

	public AccountPurseUsageDto findMatchAccountPurseUsage(String purseName) {
		ProductPurse productPurse = this.getProductPurseByPurseName(purseName);
		return this.accountPurseUsageByPurseId.get(productPurse.getPurseId());
	}

	public BigDecimal getCumulativePurseBalance(String purseName) {

		List<AccountPurse> pursesWithMatchingName = wrapIfNull(this.accountPursesByPurseName.get(purseName.toUpperCase()));
		if (!CollectionUtils.isEmpty(pursesWithMatchingName)) {
			return BigDecimal.valueOf(pursesWithMatchingName.stream()
				.mapToDouble(accountPurse -> accountPurse.getLedgerBalance()
					.doubleValue())
				.sum());
		}
		return BigDecimal.ZERO;
	}

}