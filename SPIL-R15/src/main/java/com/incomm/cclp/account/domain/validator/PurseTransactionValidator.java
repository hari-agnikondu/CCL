package com.incomm.cclp.account.domain.validator;

import static com.incomm.cclp.account.util.CodeUtil.isNotEqual;
import static com.incomm.cclp.account.util.CodeUtil.isNotEqualIgnoreCase;
import static com.incomm.cclp.account.util.CodeUtil.isNull;
import static com.incomm.cclp.account.util.CodeUtil.isNullOrEmpty;
import static com.incomm.cclp.account.util.CodeUtil.not;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.incomm.cclp.account.application.command.TransactionInfo;
import com.incomm.cclp.account.application.command.UpdateAccountPurseCommand;
import com.incomm.cclp.account.application.command.UpdatePurseStatusCommand;
import com.incomm.cclp.account.application.command.UpdateRolloverAccountPurseCommand;
import com.incomm.cclp.account.domain.exception.DomainExceptionFactory;
import com.incomm.cclp.account.domain.exception.DomainExceptionType;
import com.incomm.cclp.account.domain.model.AccountAggregate;
import com.incomm.cclp.account.domain.model.AccountPurse;
import com.incomm.cclp.account.domain.model.ProductPurse;
import com.incomm.cclp.account.domain.model.PurseStatusUpdateTransactionInfo;
import com.incomm.cclp.account.domain.model.PurseUpdateActionType;
import com.incomm.cclp.account.domain.model.PurseUpdateTransactionInfo;
import com.incomm.cclp.account.domain.model.SpNumberType;
import com.incomm.cclp.account.domain.view.AccountPurseGroupView;
import com.incomm.cclp.account.util.DateTimeUtil;
import com.incomm.cclp.account.util.JsonUtil;
import com.incomm.cclp.cache.AppCacheService;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.transaction.dao.TransactionDAO;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
class PurseTransactionValidator {

	private static final int RETRY_REQUEST_VALIDITY_TIME_IN_HOURS = 72;

	@Autowired
	private ProductValidator productValidator;

	@Autowired
	private PurseValidator purseValidator;

	@Autowired
	private TransactionDAO transactionDAO;

	public boolean isValidRetry(UpdateAccountPurseCommand command, PurseUpdateTransactionInfo previousTransactionInfo) {

		log.debug("ENTER");

		if (isNull(previousTransactionInfo)) {
			log.debug("null previous transaction found for given correlation id.");
			return false;
		}

		if (not(areRequestParamtersMatching(command, previousTransactionInfo))) {
			throw DomainExceptionFactory.from(DomainExceptionType.DUPLICATE_REQUEST, "Duplicate RRN/correlationId.");
		}

		log.info("Request parameters matched with previous transaction for purseStatusUpdate");
		return true;

	}

	public boolean isValidRetry(UpdatePurseStatusCommand command, PurseStatusUpdateTransactionInfo previousTransactionInfo) {

		log.debug("ENTER");

		if (isNull(previousTransactionInfo)) {
			log.debug("null previous transaction found for given correlation id.");
			return false;
		}

		if (not(areRequestParamtersMatching(command, previousTransactionInfo))) {
			throw DomainExceptionFactory.from(DomainExceptionType.DUPLICATE_REQUEST, "Duplicate RRN/correlationId.");
		}

		log.info("Request parameters matched with previous transaction for purseStatusUpdate");
		return true;

	}

	public void validate(UpdatePurseStatusCommand command, AccountAggregate accountAggregate) {

		this.productValidator.validateProductExpiryDate(accountAggregate.getProductId(), AppCacheService.from(accountAggregate.getValueDto()
			.getProductAttributes()));

	}

	public void validate(UpdateAccountPurseCommand command, AccountAggregate accountAggregate) {

		AppCacheService cacheService = AppCacheService.from(accountAggregate.getValueDto()
			.getProductAttributes());

		this.productValidator.validateProductExpiryDate(accountAggregate.getProductId(), cacheService);

		this.purseValidator.validatePurseForAction(command.getPurseName(), command.getActionType(),
				this.getMatchingPurse(command, accountAggregate));

		this.purseValidator.validatePurseTypeForAction(cacheService, accountAggregate.getProductPurseByPurseName(command.getPurseName()));

		if (command.getActionType() != PurseUpdateActionType.AUTO_TOP_UP) {
			this.validatePurseBalance(cacheService, command, accountAggregate);
		}

	}

	private void validatePurseBalance(AppCacheService cacheService, UpdateAccountPurseCommand command, AccountAggregate accountAggregate) {

		Optional<AccountPurse> accountPurseOpt = getMatchingPurse(command, accountAggregate);
		double currentPurseBalance = accountPurseOpt.isPresent() ? accountPurseOpt.get()
			.getAvailableBalance()
			.doubleValue() : 0.0;

		ProductPurse productPurse = accountAggregate.getProductPurseByPurseName(command.getPurseName());

		purseValidator.validateMinAndMaxPurseBalance(cacheService, accountAggregate.getProductId(), productPurse.getPurseId(),
				command.getActionType(), currentPurseBalance, command.getTransactionAmount()
					.doubleValue());

	}

	private boolean areRequestParamtersMatching(UpdateAccountPurseCommand command, PurseUpdateTransactionInfo previousTransactionInfo) {

		TransactionInfo currentTransactionInfo = command.getTransactionInfo();

		SpNumberType spNumberType = currentTransactionInfo.getSpNumberType()
			.isPresent()
					? currentTransactionInfo.getSpNumberType()
						.get()
					: null;

		if (isNotEqual(spNumberType, previousTransactionInfo.getSpNumberType())) {
			// spNumberType check
			return false;
		}

		if (isNotEqual(currentTransactionInfo.getSpNumber(), previousTransactionInfo.getSpNumber())) {
			// spNumber check
			return false;
		}

		if (isNotEqual(command.getActionType(), previousTransactionInfo.getActionType())) {
			// actionType check
			return false;
		}

		if (isNotEqualIgnoreCase(command.getPurseName(), previousTransactionInfo.getPurseName())) {
			// purseName check
			return false;
		}

		if (isNotEqual(command.getAccountPurseId(), previousTransactionInfo.getInputAccountPurseId())) {
			// spNumber check
			return false;
		}

		if (not(command.getTransactionAmount()
			.compareTo(previousTransactionInfo.getTransactionAmount()) == 0)) {
			// spNumber check
			return false;
		}

		if (not(DateTimeUtil.isEqual(command.getEffectiveDate(), previousTransactionInfo.getEffectiveDate()))) {
			// spNumber check
			return false;
		}

		if (not(DateTimeUtil.isEqual(command.getExpiryDate(), previousTransactionInfo.getExpiryDate()))) {
			// spNumber check
			return false;
		}

		if (isNotEqual(command.getSkuCode(), previousTransactionInfo.getSkuCode())) {
			// spNumber check
			return false;
		}

		if (isNotEqual(command.getCurrency(), previousTransactionInfo.getCurrency())) {
			// spNumber check
			return false;
		}

		if (isNull(previousTransactionInfo.getTransactionDateTime())) {
			return false;
		}

		if (previousTransactionInfo.getTransactionDateTime()
			.isBefore(LocalDateTime.now()
				.minusHours(RETRY_REQUEST_VALIDITY_TIME_IN_HOURS))) {
			return false;
		}

		return true;
	}

	private boolean areRequestParamtersMatching(UpdatePurseStatusCommand command,
			PurseStatusUpdateTransactionInfo previousTransactionInfo) {

		TransactionInfo currentTransactionInfo = command.getTransactionInfo();

		SpNumberType spNumberType = currentTransactionInfo.getSpNumberType()
			.isPresent()
					? currentTransactionInfo.getSpNumberType()
						.get()
					: null;

		if (isNotEqual(spNumberType, previousTransactionInfo.getSpNumberType())) {
			// spNumberType check
			return false;
		}

		if (isNotEqual(currentTransactionInfo.getSpNumber(), previousTransactionInfo.getSpNumber())) {
			// spNumber check
			return false;
		}

		if (isNotEqualIgnoreCase(command.getPurseName(), previousTransactionInfo.getPurseName())) {
			// purseName check
			return false;
		}

		if (isNotEqual(command.getPurseStatus(), previousTransactionInfo.getPurseStatus())) {
			// purseStatus check
			return false;
		}

		if (isNull(previousTransactionInfo.getTransactionDateTime())) {
			return false;
		}

		if (previousTransactionInfo.getTransactionDateTime()
			.isBefore(LocalDateTime.now()
				.minusHours(RETRY_REQUEST_VALIDITY_TIME_IN_HOURS))) {
			return false;
		}

		if (not(DateTimeUtil.isEqual(DateTimeUtil.convert(command.getStartDate()), previousTransactionInfo.getStartDate()))) {
			// spNumber check
			return false;
		}

		if (not(DateTimeUtil.isEqual(DateTimeUtil.convert(command.getEndDate()), previousTransactionInfo.getEndDate()))) {
			// spNumber check
			return false;
		}

		return true;
	}

	private Optional<AccountPurse> getMatchingPurse(UpdateAccountPurseCommand command, AccountAggregate accountAggregate) {
		return accountAggregate.getMatchingAccountPurse(command.getActionType(), command.getPurseName(),
				DateTimeUtil.convert(command.getEffectiveDate()), DateTimeUtil.convert(command.getExpiryDate()), command.getSkuCode(),
				command.getAccountPurseId());
	}

	public <T> Optional<T> getPreviousTransactionInfo(TransactionInfo transactionInfo, Class<T> classType) {

		String previousTransactionInfoString = this.transactionDAO.getTransactionLogRequestInfo( //
				transactionInfo.getCorrelationId(), //
				transactionInfo.getDeliveryChannelType()
					.getChannelCode(), //
				transactionInfo.getMessageType()
					.getMessageTypeCode(), //
				transactionInfo.getMdmId(), //
				ResponseCodes.SUCCESS);

		if (isNullOrEmpty(previousTransactionInfoString)) {
			return Optional.empty();
		}

		T previousTransaction = JsonUtil.mapToObject(previousTransactionInfoString, classType);

		return Optional.ofNullable(previousTransaction);
	}

	public void validate(UpdateRolloverAccountPurseCommand command, AccountAggregate accountAggregate) {

		AppCacheService cacheService = AppCacheService.from(accountAggregate.getValueDto()
			.getProductAttributes());

		this.productValidator.validateProductExpiryDate(accountAggregate.getProductId(), cacheService);

		this.purseValidator.validatePurseForAction(command.getPurseName(), command.getActionType(),
				this.getMatchingPurse(command, accountAggregate));

		this.purseValidator.validatePurseTypeForAction(cacheService, accountAggregate.getProductPurseByPurseName(command.getPurseName()));

		if (command.getActionType() != PurseUpdateActionType.AUTO_TOP_UP) {
			this.validatePurseBalance(cacheService, command, accountAggregate);
		}

	}

	private void validatePurseBalance(AppCacheService cacheService, UpdateRolloverAccountPurseCommand command,
			AccountAggregate accountAggregate) {
		Optional<AccountPurse> accountPurseOpt = getMatchingPurse(command, accountAggregate);
		double currentPurseBalance = accountPurseOpt.isPresent() ? accountPurseOpt.get()
			.getAvailableBalance()
			.doubleValue() : 0.0;

		ProductPurse productPurse = accountAggregate.getProductPurseByPurseName(command.getPurseName());

		purseValidator.validateMinAndMaxPurseBalance(cacheService, accountAggregate.getProductId(), productPurse.getPurseId(),
				command.getActionType(), currentPurseBalance, command.getTransactionAmount()
					.doubleValue());

	}

	private Optional<AccountPurse> getMatchingPurse(UpdateRolloverAccountPurseCommand command, AccountAggregate accountAggregate) {
		return accountAggregate.getMatchingAccountPurse(command.getActionType(), command.getPurseName(),
				DateTimeUtil.convert(command.getEffectiveDate()), DateTimeUtil.convert(command.getExpiryDate()), command.getSkuCode(),
				command.getAccountPurseId());

	}

	public boolean isValidRetry(UpdateRolloverAccountPurseCommand command, PurseUpdateTransactionInfo previousTransactionInfo) {
		log.debug("ENTER");

		if (isNull(previousTransactionInfo)) {
			log.debug("null previous transaction found for given correlation id.");
			return false;
		}

		if (not(areRequestParamtersMatching(command, previousTransactionInfo))) {
			throw DomainExceptionFactory.from(DomainExceptionType.DUPLICATE_REQUEST, "Duplicate RRN/correlationId.");
		}

		log.info("Request parameters matched with previous transaction for purseStatusUpdate");
		return true;

	}

	private boolean areRequestParamtersMatching(UpdateRolloverAccountPurseCommand command,
			PurseUpdateTransactionInfo previousTransactionInfo) {
		TransactionInfo currentTransactionInfo = command.getTransactionInfo();

		SpNumberType spNumberType = currentTransactionInfo.getSpNumberType()
			.isPresent()
					? currentTransactionInfo.getSpNumberType()
						.get()
					: null;

		if (isNotEqual(spNumberType, previousTransactionInfo.getSpNumberType())) {
			// spNumberType check
			return false;
		}

		if (isNotEqual(currentTransactionInfo.getSpNumber(), previousTransactionInfo.getSpNumber())) {
			// spNumber check
			return false;
		}

		if (isNotEqual(command.getActionType(), previousTransactionInfo.getActionType())) {
			// actionType check
			return false;
		}

		if (isNotEqualIgnoreCase(command.getPurseName(), previousTransactionInfo.getPurseName())) {
			// purseName check
			return false;
		}

		if (isNotEqual(command.getAccountPurseId(), previousTransactionInfo.getInputAccountPurseId())) {
			// spNumber check
			return false;
		}

		if (not(command.getTransactionAmount()
			.compareTo(previousTransactionInfo.getTransactionAmount()) == 0)) {
			// spNumber check
			return false;
		}

		if (not(DateTimeUtil.isEqual(command.getEffectiveDate(), previousTransactionInfo.getEffectiveDate()))) {
			// spNumber check
			return false;
		}

		if (not(DateTimeUtil.isEqual(command.getExpiryDate(), previousTransactionInfo.getExpiryDate()))) {
			// spNumber check
			return false;
		}

		if (isNotEqual(command.getSkuCode(), previousTransactionInfo.getSkuCode())) {
			// spNumber check
			return false;
		}

		if (isNotEqual(command.getCurrency(), previousTransactionInfo.getCurrency())) {
			// spNumber check
			return false;
		}

		if (isNull(previousTransactionInfo.getTransactionDateTime())) {
			return false;
		}

		if (previousTransactionInfo.getTransactionDateTime()
			.isBefore(LocalDateTime.now()
				.minusHours(RETRY_REQUEST_VALIDITY_TIME_IN_HOURS))) {
			return false;
		}

		return true;
	}

	public void validateTransactionAmount(BigDecimal amount) {
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			log.info("Invalid transaction amount: {}", amount);
			throw new ServiceException(SpilExceptionMessages.INVALID_AMOUNT, ResponseCodes.INVALID_AMOUNT);
		}
	}

	public void validateForLockedBalance(List<AccountPurseGroupView> groupViews) {
		boolean balanceMismatch = groupViews.stream()
			.anyMatch(view -> view.getAccountPurses()
				.stream()
				.anyMatch(accountPurse -> accountPurse.getAvailableBalance()
					.compareTo(accountPurse.getLedgerBalance()) != 0));

		if (balanceMismatch) {
			log.info("available balance and ledger balance does not match for atleast one account purse.");
			throw new ServiceException(SpilExceptionMessages.CARD_BALANCE_LOCKED, ResponseCodes.CARD_BALANCE_LOCKED);
		}
	}

}
