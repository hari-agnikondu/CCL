package com.incomm.cclp.account.application.service;

import static com.incomm.cclp.account.util.CodeUtil.convertListToString;
import static com.incomm.cclp.account.util.CodeUtil.convertToEmptyStringIfNull;
import static com.incomm.cclp.account.util.CodeUtil.isNotNull;
import static com.incomm.cclp.account.util.CodeUtil.not;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.account.application.command.AccountTransactionCommand;
import com.incomm.cclp.account.application.command.UpdateAccountPurseCommand;
import com.incomm.cclp.account.application.command.UpdatePurseStatusCommand;
import com.incomm.cclp.account.application.command.UpdateRolloverAccountPurseCommand;
import com.incomm.cclp.account.application.persistence.AccountTransactionPersistenceHandler;
import com.incomm.cclp.account.domain.event.TransactionFailedEvent;
import com.incomm.cclp.account.domain.event.publisher.AccountDomainEventPublisher;
import com.incomm.cclp.account.domain.event.subscriber.AccountDomainPersistenceHandler;
import com.incomm.cclp.account.domain.exception.DomainException;
import com.incomm.cclp.account.domain.exception.DomainExceptionFactory;
import com.incomm.cclp.account.domain.exception.DomainExceptionType;
import com.incomm.cclp.account.domain.factory.AccountAggregateParams;
import com.incomm.cclp.account.domain.factory.AccountAggregateRepository;
import com.incomm.cclp.account.domain.factory.AccountStateType;
import com.incomm.cclp.account.domain.model.AccountAggregate;
import com.incomm.cclp.account.domain.model.ProductPurse;
import com.incomm.cclp.account.domain.model.TransactionResponseInfo;
import com.incomm.cclp.account.domain.service.AccountDomainService;
import com.incomm.cclp.account.domain.view.AccountPurseGroupView;
import com.incomm.cclp.account.domain.view.AccountPurseView;
import com.incomm.cclp.account.domain.view.AccountPurseViewNew;
import com.incomm.cclp.account.domain.view.AccountSummaryView;
import com.incomm.cclp.account.domain.view.PurseView;
import com.incomm.cclp.account.util.DateTimeFormatType;
import com.incomm.cclp.account.util.DateTimeUtil;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.domain.TransactionLog;
import com.incomm.cclp.dto.PurseAuthResponse;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class AccountApplicationService {

	@Autowired
	private AccountAggregateRepository aggregateRepository;

	@Autowired
	private AccountDomainService accountDomainService;

	@Autowired
	private AccountTransactionPersistenceHandler persistenceHandler;

	private AccountDomainEventPublisher domainEventPublisher;

	@Autowired
	public AccountApplicationService(final AccountDomainEventPublisher domainEventPublisher,
			final AccountDomainPersistenceHandler domainPersistenceHandler) {
		this.domainEventPublisher = domainEventPublisher;
		this.domainEventPublisher.addSubscriber(domainPersistenceHandler);
	}

	@SuppressWarnings("unused")
	private <T, R> R execute(T command, Function<T, R> executerFunction,
			BiFunction<T, TransactionResponseInfo, TransactionLog> failureHandler) {

		long timeBeforeTransaction = System.currentTimeMillis();

		boolean isError = false;
		TransactionLog transactionLog = null;

		try {
			log.info("execute method start time: {}", timeBeforeTransaction);

			return executerFunction.apply(command);

		} catch (ServiceException exception) {
			log.info("ResponseCode:" + exception.getResponseID() + " Message:" + exception.getMessage() + ", Reason:"
					+ exception.getReason());
			if (exception.getCause() != null && !(exception.getCause() instanceof ServiceException)) {
				log.error(exception.getCause()
					.getMessage(), exception.getCause());
			}

			isError = true;
			transactionLog = failureHandler.apply(command,
					TransactionResponseInfo.from(exception.getResponseID(), exception.getMessage(), null));

			throw exception;
		} catch (DomainException exception) {
			log.info("ResponseCode:" + exception.getExceptionType()
				.getResponseCode() + " Message:" + exception.getMessage() + ", System Message:" + exception.getSystemMessage());

			isError = true;
			transactionLog = failureHandler.apply(command, TransactionResponseInfo.from(exception.getExceptionType()
				.getResponseCode(), exception.getMessage(), null));

			throw exception;
		} catch (Exception exception) {
			log.error("Process Terminated Due to Exception:" + exception.getMessage(), exception);

			isError = true;
			transactionLog = failureHandler.apply(command,
					TransactionResponseInfo.from(DomainExceptionType.SYSTEM_ERROR.getResponseCode(), exception.getMessage(), null));

			throw DomainExceptionFactory.from(DomainExceptionType.SYSTEM_ERROR, exception.getMessage());
		} finally {

			if (isError) {
				this.persistenceHandler.persist(TransactionFailedEvent.from(transactionLog));
			}

			long timeTaken = System.currentTimeMillis() - timeBeforeTransaction;
			log.info("Transaction response Time:{} ms", timeTaken);

		}
	}

	public AccountPurseView execute(UpdateAccountPurseCommand command) {

		AccountAggregateParams params = AccountAggregateParams.builder()
			.spNumber(command.getTransactionInfo()
				.getSpNumber())
			.spNumberType(command.getTransactionInfo()
				.getSpNumberType())
			.deliveryChannelType(command.getTransactionInfo()
				.getDeliveryChannelType())
			.transactionShortCode(command.getActionType()
				.getTransactionShortName())
			.purseName(command.getPurseName())
			.accountPurseId(command.getAccountPurseId())
			.state(AccountStateType.CARD)
			.state(AccountStateType.ACCOUNT_PURSE_SELECTED)
			.state(AccountStateType.ACCOUNT_PURSE_USAGE_SELECTED)
			.build();

		AccountAggregate aggregate = this.aggregateRepository.load(params);
		return aggregate.execute(command);

	}

	public PurseView execute(UpdatePurseStatusCommand command) {

		AccountAggregateParams params = AccountAggregateParams.builder()
			.spNumber(command.getTransactionInfo()
				.getSpNumber())
			.spNumberType(command.getTransactionInfo()
				.getSpNumberType())
			.deliveryChannelType(command.getTransactionInfo()
				.getDeliveryChannelType())
			.transactionShortCode(UpdatePurseStatusCommand.TRANSACTION_SHORT_CODE)
			.purseName(command.getPurseName())
			.state(AccountStateType.CARD)
			.state(AccountStateType.ACCOUNT_PURSE_USAGE_SELECTED)
			.build();

		AccountAggregate aggregate = this.aggregateRepository.load(params);
		return aggregate.execute(command);

	}

	public AccountPurseView execute(UpdateRolloverAccountPurseCommand command) {

		AccountAggregateParams params = AccountAggregateParams.builder()
			.spNumber(command.getTransactionInfo()
				.getSpNumber())
			.spNumberType(command.getTransactionInfo()
				.getSpNumberType())
			.deliveryChannelType(command.getTransactionInfo()
				.getDeliveryChannelType())
			.transactionShortCode(command.getActionType()
				.getTransactionShortName())
			.purseName(command.getPurseName())
			.accountPurseId(command.getAccountPurseId())
			.state(AccountStateType.CARD)
			.state(AccountStateType.ACCOUNT_PURSE_SELECTED)
			.state(AccountStateType.ACCOUNT_PURSE_USAGE_SELECTED)
			.build();

		AccountAggregate aggregate = this.aggregateRepository.load(params);
		return aggregate.execute(command);

	}

	public void processTransactionFailedEvent(TransactionFailedEvent event) {
		this.domainEventPublisher.raise(event);
	}

	public String[] processSpilTransaction(ValueDTO valueDto) {

		AccountTransactionCommand command = CommandMapper.map(valueDto);

		try {
			AccountSummaryView update = accountDomainService.executeCommand(command);
			valueDto.getValueObj()
				.put(ValueObjectKeys.PUR_BAL, buildPurseBalanceResponse(update.getAccountPurseGroupViews()));
			return buildResponse(update);
		} catch (ServiceException exception) {
			log.info("Caught exception in account application service:", exception.getMessage());
			throw exception;
		}
	}

	private String[] buildResponse(AccountSummaryView update) {

		String targetCardBalance = update.getTargetCardBalance()
			.isPresent()
					? update.getTargetCardBalance()
						.get()
						.toString()
					: "";

		String targetCardStatus = update.getTargetCardStatus()
			.isPresent()
					? update.getTargetCardStatus()
						.get()
						.getStatusDescription()
					: "";

		String[] result = new String[10];
		result[0] = update.getResponseCode();
		result[1] = update.getResponseMessage();
		result[2] = Long.toString(update.getAuthId());
		result[3] = update.getSourceCardBalance()
			.toString();
		result[4] = update.getAuthorizedAmount()
			.toString();
		result[5] = update.getCurrencyCode();
		result[6] = buildPurseAuthResponse(update.getPurseAuthResponses());
		result[7] = targetCardBalance;
		result[8] = targetCardStatus;
		result[9] = update.getSourceCardStatus()
			.getStatusDescription();

		return result;
	}

	private String buildPurseAuthResponse(List<PurseAuthResponse> accountbalances) {
		List<String> response = accountbalances.stream()
			.map(this::convertPurseauthRespToString)
			.collect(Collectors.toList());
		return convertListToString(response);
	}

	private String convertPurseauthRespToString(PurseAuthResponse purseResponse) {
		return purseResponse.getAccountPurseId() + "|" + purseResponse.getPurseName() + "|" + purseResponse.getTransactionAmount() + "|"
				+ purseResponse.getAvailableBalance() + "|" + purseResponse.getCurrencyCode() + "|" + purseResponse.getSkuCode();
	}

	private String buildPurseBalanceResponse(List<AccountPurseGroupView> accountPurseGroupViews) {
		List<String> response = accountPurseGroupViews.stream()
			.filter(p -> not(p.getProductPurse()
				.isDefault()))
			.map(this::convertAccountPurseGroupViewToString)
			.collect(Collectors.toList());
		return convertListToString(response);
	}

	private String convertAccountPurseGroupViewToString(AccountPurseGroupView accountPurseGroupView) {
		List<String> result = new ArrayList<>();
		ProductPurse productPurse = accountPurseGroupView.getProductPurse();

		accountPurseGroupView.getAccountPurses()
			.forEach(accountPurseView -> {
				if (isNotNull(accountPurseView) && isNotNull(productPurse)) {
					log.info("Converting AccountPurseView to String for accountPurseId: {}", accountPurseView.getAccountPurseKey()
						.getAccountPurseId());
					result.add(
							convertAccountPurseViewToString(accountPurseView, productPurse.getPurseName(), productPurse.getCurrencyCode()));
				}
			});
		return convertListToString(result);
	}

	private String convertAccountPurseViewToString(AccountPurseViewNew accountPurseView, String purseName, String currencyCode) {

		return accountPurseView.getAccountPurseKey()
			.getAccountPurseId()
				+ "|" + purseName + "|" + accountPurseView.getAvailableBalance() + "|" + currencyCode + "|"
				+ convertToEmptyStringIfNull(DateTimeUtil.convert(accountPurseView.getAccountPurseAltKeyAttributes()
					.getExpiryDate(), DateTimeFormatType.DD_MMM_YY_WITH_HYPHEN))
				+ "|" + convertToEmptyStringIfNull(accountPurseView.getAccountPurseAltKeyAttributes()
					.getSkuCode());
	}
}
