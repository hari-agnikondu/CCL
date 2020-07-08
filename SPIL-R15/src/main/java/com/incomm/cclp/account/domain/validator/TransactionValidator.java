package com.incomm.cclp.account.domain.validator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.incomm.cclp.account.application.command.TransactionInfo;
import com.incomm.cclp.account.application.command.UpdateAccountPurseCommand;
import com.incomm.cclp.account.application.command.UpdatePurseStatusCommand;
import com.incomm.cclp.account.application.command.UpdateRolloverAccountPurseCommand;
import com.incomm.cclp.account.domain.model.AccountAggregate;
import com.incomm.cclp.account.domain.model.CardStatusType;
import com.incomm.cclp.account.domain.model.PurseStatusUpdateTransactionInfo;
import com.incomm.cclp.account.domain.model.PurseUpdateTransactionInfo;
import com.incomm.cclp.account.domain.view.AccountPurseGroupView;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.transaction.validation.AuthorizationValidation;

@Component
public class TransactionValidator {

	@Autowired
	private PurseTransactionValidator purseTransactionValidator;

	@Autowired
	private AuthorizationValidation authorizationValidation;

	@Autowired
	private CardValidator cardValidator;

	public boolean isValidRetry(UpdateAccountPurseCommand command, PurseUpdateTransactionInfo previousTransactionInfo) {
		return this.purseTransactionValidator.isValidRetry(command, previousTransactionInfo);
	}

	public boolean isValidRetry(UpdatePurseStatusCommand command, PurseStatusUpdateTransactionInfo previousTransactionInfo) {
		return this.purseTransactionValidator.isValidRetry(command, previousTransactionInfo);
	}

	public void validate(UpdateAccountPurseCommand command, AccountAggregate accountAggregate) {
		this.purseTransactionValidator.validate(command, accountAggregate);
		this.performCommonValidation(accountAggregate.getValueDto());
	}

	public void validate(UpdatePurseStatusCommand command, AccountAggregate accountAggregate) {
		this.purseTransactionValidator.validate(command, accountAggregate);
		this.performCommonValidation(accountAggregate.getValueDto());
	}

	private void performCommonValidation(ValueDTO valueDto) {
		this.authorizationValidation.validate(valueDto);
	}

	public <T> Optional<T> getPreviousTransactionInfo(TransactionInfo transactionInfo, Class<T> classType) {
		return this.purseTransactionValidator.getPreviousTransactionInfo(transactionInfo, classType);
	}

	public void validate(UpdateRolloverAccountPurseCommand command, AccountAggregate accountAggregate) {
		this.purseTransactionValidator.validate(command, accountAggregate);
		this.performCommonValidation(accountAggregate.getValueDto());

	}

	public boolean isValidRetry(UpdateRolloverAccountPurseCommand command, PurseUpdateTransactionInfo previousTransactionInfo) {
		return this.purseTransactionValidator.isValidRetry(command, previousTransactionInfo);
	}

	public void validateTransactionAmount(BigDecimal amount) {
		this.purseTransactionValidator.validateTransactionAmount(amount);
	}

	public void validateForCardSwap(CardStatusType targetCardStatus, long sourceCardProductId, long targetCardProductId) {

		this.cardValidator.validateProductIds(sourceCardProductId, targetCardProductId);
		this.cardValidator.validateTargetCardStatusForCardSwap(targetCardStatus);

	}

	public void validateForLockedBalance(List<AccountPurseGroupView> groupViews) {
		this.purseTransactionValidator.validateForLockedBalance(groupViews);
	}

}
