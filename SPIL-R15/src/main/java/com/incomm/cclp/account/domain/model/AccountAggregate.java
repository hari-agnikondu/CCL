package com.incomm.cclp.account.domain.model;

import static com.incomm.cclp.account.util.CodeUtil.isNull;
import static com.incomm.cclp.account.util.CodeUtil.wrapIfNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.util.CollectionUtils;

import com.incomm.cclp.account.application.command.TransactionInfo;
import com.incomm.cclp.account.application.command.UpdateAccountPurseCommand;
import com.incomm.cclp.account.application.command.UpdatePurseStatusCommand;
import com.incomm.cclp.account.application.command.UpdateRolloverAccountPurseCommand;
import com.incomm.cclp.account.domain.event.AccountPurseUpdatedEvent;
import com.incomm.cclp.account.domain.event.PurseStatusUpdatedEvent;
import com.incomm.cclp.account.domain.event.TransactionFailedEvent;
import com.incomm.cclp.account.domain.event.publisher.AccountDomainEventPublisher;
import com.incomm.cclp.account.domain.exception.DomainException;
import com.incomm.cclp.account.domain.exception.DomainExceptionFactory;
import com.incomm.cclp.account.domain.exception.DomainExceptionType;
import com.incomm.cclp.account.domain.factory.LogServiceFactory;
import com.incomm.cclp.account.domain.validator.PurseValidator;
import com.incomm.cclp.account.domain.validator.TransactionValidator;
import com.incomm.cclp.account.domain.view.AccountPurseView;
import com.incomm.cclp.account.domain.view.PurseView;
import com.incomm.cclp.account.util.DateTimeUtil;
import com.incomm.cclp.account.util.JsonUtil;
import com.incomm.cclp.config.SpringContext;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.service.SequenceService;
import com.incomm.cclp.domain.TransactionLog;
import com.incomm.cclp.dto.AccountPurseUsageDto;
import com.incomm.cclp.dto.RequestInfo;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.transaction.bean.SpilStartupMsgTypeBean;
import com.incomm.cclp.transaction.constants.TransactionConstant;
import com.incomm.cclp.util.RequestInfoService;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class AccountAggregate {

	// @formatter:off
	private TransactionValidator transactionValidator;
	private AccountDomainEventPublisher domainEventPublisher;
	private SequenceService sequenceService;
	private LogServiceFactory logService;
	private RequestInfoService requestInfoService;
	private ValueDtoMapper valueDtoMapper;

	@Getter private final  String accountNumber;
	@Getter private final long accountId;
	@Getter private final long productId;
	@Getter private final CardEntity cardEntity;
	private final AccountPurseAggregate accountPurseAggregate;
	private final AccountPurseAggregateNew accountPurseAggregate1;
	
	// Replace the SpilStartupMsgTypeBean with a corresponding domain class. Put this class at appropriate level. 
	// Transaction configuration should not be part of domain but part of request processing.
	@Getter @Deprecated private SpilStartupMsgTypeBean transactionConfiguration;
	// @formatter:on

	@Getter
	@Deprecated
	private ValueDTO valueDto;

	@Builder
	public AccountAggregate( //
			String accountNumber, //
			long accountId, //
			long productId, //
			CardEntity cardEntity, //
			AccountPurseAggregate accountPurseAggregate, //
			AccountPurseAggregateNew accountPurseAggregate1, //
			SpilStartupMsgTypeBean transactionConfiguration) {

		log.info("Building aggregate for account number: {}", accountNumber);

		this.accountNumber = accountNumber;
		this.accountId = accountId;
		this.productId = productId;
		this.cardEntity = cardEntity;
		this.accountPurseAggregate = accountPurseAggregate;
		this.accountPurseAggregate1 = accountPurseAggregate1;
		this.transactionConfiguration = transactionConfiguration;

		this.transactionValidator = SpringContext.getBean(TransactionValidator.class);
		this.domainEventPublisher = SpringContext.getBean(AccountDomainEventPublisher.class);
		this.sequenceService = SpringContext.getBean(SequenceService.class);
		this.logService = SpringContext.getBean(LogServiceFactory.class);
		this.requestInfoService = SpringContext.getBean(RequestInfoService.class);

	}

	private void populateValueDto(String purseName) {

		AccountPurseUsageDto purseUsageDto = this.accountPurseAggregate.findMatchAccountPurseUsage(purseName);

		ProductPurse productPurse = this.getProductPurseByPurseName(purseName);
		this.valueDtoMapper = SpringContext.getBean(ValueDtoMapper.class);
		this.valueDto = valueDtoMapper.createValueDto(this.productId, productPurse.getPurseId(), purseUsageDto);

		valueDtoMapper.populateCardInfo(this.valueDto.getValueObj(), cardEntity);
		valueDtoMapper.populateMessageTypeBeanInfo(this.valueDto.getValueObj(), this.transactionConfiguration);
		valueDtoMapper.populateProductPurseInfo(this.valueDto, productPurse);
	}

	public AccountPurseView execute(UpdateAccountPurseCommand command) {
		AccountPurseView accountPurseView = null;

		log.debug("Enter Process method");
		long timeBeforeTransaction = System.currentTimeMillis();
		log.info("UpdateAccountPurseCommand process transaction start time: {}", timeBeforeTransaction);

		BigDecimal nextTransactionId = null;
		String nextAuthorizationId = null;
		TransactionInfo transactionInfo = command.getTransactionInfo();

		try {
			this.populateValueDto(command.getPurseName());
			this.valueDtoMapper.populateTransactionInfo(this.valueDto.getValueObj(), command);

			nextTransactionId = this.getNextTransactionId();

			Optional<PurseUpdateTransactionInfo> previousTransactionInfoOpt = transactionValidator
				.getPreviousTransactionInfo(transactionInfo, PurseUpdateTransactionInfo.class);
			if (previousTransactionInfoOpt.isPresent() && transactionValidator.isValidRetry(command, previousTransactionInfoOpt.get())) {

				PurseUpdateTransactionInfo previousTransactionInfo = previousTransactionInfoOpt.get();
				accountPurseView = this.createAccountPurseView( //
						DomainExceptionType.RETRY_REQUEST.getResponseCode(), //
						DomainExceptionType.RETRY_REQUEST.getDefaultMessage(), //
						nextTransactionId, //
						command, //
						previousTransactionInfo.getAccountPurseId(), //
						DateTimeUtil.toDefaultTimeZone(previousTransactionInfo.getEffectiveDate()), //
						DateTimeUtil.toDefaultTimeZone(previousTransactionInfo.getExpiryDate()), //
						previousTransactionInfo.getAvailablePurseBalance(), //
						previousTransactionInfo.getAuthorizedAmount(), //
						previousTransactionInfo.getCardStatus());

				TransactionLog transactionLog = TransactionLogBuilderHelper.from(nextTransactionId)
					.accountAggregate(this)
					.command(command)
					.purseUpdateTransactionInfo(previousTransactionInfo)
					.response(DomainExceptionType.RETRY_REQUEST.getResponseCode(), DomainExceptionType.RETRY_REQUEST.getDefaultMessage(),
							"F")
					.build();

				domainEventPublisher.raise(TransactionFailedEvent.from(transactionLog));

				return accountPurseView;
			}

			transactionValidator.validate(command, this);

			nextAuthorizationId = this.getNextAuthorizationId();

			BigDecimal maxPurseBalance = this.getValueDto()
				.getProductAttributes()
				.get(ValueObjectKeys.PURSE)
				.containsKey(ValueObjectKeys.MAX_PURSE_BALANCE)
						? new BigDecimal(String.valueOf(this.getValueDto()
							.getProductAttributes()
							.get(ValueObjectKeys.PURSE)
							.get(ValueObjectKeys.MAX_PURSE_BALANCE)))
						: BigDecimal.valueOf(PurseValidator.MAX_BALANCE);

			BigDecimal transactionFee = this.getTransactionFee(this.valueDto.getValueObj()
				.get(ValueObjectKeys.PROD_TXN_FEE_AMT));

			AccountPurseUpdate accountPurseUpdate = this.accountPurseAggregate.updateAccountPurse( //
					command.getActionType(), //
					command.getPurseName(), //
					command.getAccountPurseId(), //
					command.getTransactionAmount(), //
					command.getEffectiveDate(), //
					command.getExpiryDate(), //
					command.getSkuCode(), //
					transactionFee, //
					valueDto.getUsageFee(), //
					valueDto.getUsageLimit(), maxPurseBalance);

			RequestInfo req = requestInfoService.getRequestInfo(valueDto);
			req.setTxnSeqId(nextTransactionId);
			req.setAuthId(nextAuthorizationId);
			req.setAccountId(BigInteger.valueOf(accountPurseUpdate.getAccountPurseKey()
				.getAccountId()));
			req.setAuthorizedAmount(Double.valueOf(String.valueOf(accountPurseUpdate.getAuthorizedAmount())));
			req.setAccountPurseId(accountPurseUpdate.getAccountPurseKey()
				.getAccountPurseId());
			req.setOpeningAvailBalance(Double.valueOf(String.valueOf(accountPurseUpdate.getPreviousAvailableBalance())));
			req.setOpeningLedgerBalance(Double.valueOf(String.valueOf(accountPurseUpdate.getPreviousLedgerBalance())));
			req.setTxnAmount(Double.valueOf(String.valueOf(accountPurseUpdate.getAuthorizedAmount()
				.add(transactionFee))));

			PurseUpdateTransactionInfo purseUpdateTransactionInfo = PurseUpdateTransactionInfo.builder()
				.spNumberType(transactionInfo.getSpNumberType()
					.isPresent()
							? transactionInfo.getSpNumberType()
								.get()
							: null)
				.spNumber(transactionInfo.getSpNumber())
				.purseName(command.getPurseName())
				.actionType(command.getActionType())
				.transactionAmount(command.getTransactionAmount())
				.effectiveDate(command.getEffectiveDate())
				.expiryDate(command.getExpiryDate())
				.skuCode(command.getSkuCode())
				.currency(command.getCurrency())
				.transactionDateTime(LocalDateTime.now())
				.inputAccountPurseId(command.getAccountPurseId())
				.accountPurseId(accountPurseUpdate.getAccountPurseKey()
					.getAccountPurseId())
				.authorizedAmount(accountPurseUpdate.getAuthorizedAmount())
				.availablePurseBalance(accountPurseUpdate.getNewAvailableBalance())
				.cardStatus(this.cardEntity.getCardStatus())
				.build();

			TransactionLog transactionLog = TransactionLogBuilderHelper.from(nextTransactionId, nextAuthorizationId)
				.accountAggregate(this)
				.cardEntity(cardEntity)
				.command(command)
				.requestXml(JsonUtil.mapToJson(purseUpdateTransactionInfo))
				.accountPurseUpdate(accountPurseUpdate, transactionFee)
				.response(ResponseCodes.SUCCESS, "OK", "C")
				.businessDateField(logService.cutOverTime(this.valueDto.getProductAttributes()))
				.build();

			AccountPurseUpdatedEvent event = AccountPurseUpdatedEvent.builder()
				.accountPurseUpdate(accountPurseUpdate)
				.statementLogs(logService.getStatementLogList(req))
				.transactionLog(transactionLog)
				.transactionDate(LocalDateTime.now())
				.transactionInfo(purseUpdateTransactionInfo)
				.build();

			accountPurseView = this.createAccountPurseView( //
					ResponseCodes.SUCCESS, //
					"success", nextTransactionId, //
					command, //
					accountPurseUpdate.getAccountPurseKey()
						.getAccountPurseId(), //
					DateTimeUtil.convert(accountPurseUpdate.getEffectiveDate()), //
					DateTimeUtil.convert(accountPurseUpdate.getExpiryDate()), //
					accountPurseUpdate.getNewAvailableBalance(), //
					accountPurseUpdate.getAuthorizedAmount(), //
					this.cardEntity.getCardStatus() //

			);
			domainEventPublisher.raise(event);

		} catch (ServiceException exception) {
			log.info("ResponseCode:" + exception.getResponseID() + " Message:" + exception.getMessage() + ", Reason:"
					+ exception.getReason());
			if (exception.getCause() != null && !(exception.getCause() instanceof ServiceException)) {
				log.error(exception.getCause()
					.getMessage(), exception.getCause());
			}

			this.domainEventPublisher.raise(TransactionFailedEvent
				.from(this.getFailedTransaction(nextTransactionId, command, exception.getResponseID(), exception.getMessage())));

			throw exception;
		} catch (DomainException exception) {
			log.info("ResponseCode:" + exception.getExceptionType()
				.getResponseCode() + " Message:" + exception.getMessage() + ", System Message:" + exception.getSystemMessage());

			this.domainEventPublisher
				.raise(TransactionFailedEvent.from(this.getFailedTransaction(nextTransactionId, command, exception.getExceptionType()
					.getResponseCode(), exception.getMessage())));

			throw exception;
		} catch (Exception exception) {
			log.error("Process Terminated Due to Exception :" + exception.getMessage(), exception);
			this.domainEventPublisher.raise(TransactionFailedEvent.from(this.getFailedTransaction(nextTransactionId, command,
					DomainExceptionType.SYSTEM_ERROR.getResponseCode(), exception.getMessage())));
			throw DomainExceptionFactory.from(DomainExceptionType.SYSTEM_ERROR, exception.getMessage());
		} finally {
			long timeTaken = System.currentTimeMillis() - timeBeforeTransaction;
			log.info("Transaction response Time:{}ms", timeTaken);

		}

		log.info("Transaction response {}", accountPurseView == null ? null : accountPurseView.toString());
		return accountPurseView;

	}

	private TransactionLog getFailedTransaction(BigDecimal transactionId, UpdateAccountPurseCommand command, String responseCode,
			String responseMessage) {

		return TransactionLogBuilderHelper.from(transactionId)
			.accountAggregate(this)
			.command(command)
			.response(responseCode, responseMessage, "F")
			.build();
	}

	private BigDecimal getTransactionFee(String transactionFeeString) {

		if (isNull(transactionFeeString)) {
			return BigDecimal.ZERO;
		}

		return new BigDecimal(transactionFeeString);
	}

	private AccountPurseView createAccountPurseView( //
			String responseCode, //
			String responseMessage, //
			BigDecimal nextTransactionId, //
			UpdateAccountPurseCommand command, //
			long accountPurseId, //
			ZonedDateTime effectiveDate, //
			ZonedDateTime expiryDate, //
			BigDecimal availablePurseBalance, //
			BigDecimal authorizedAmount, CardStatusType cardStatus) {

		return AccountPurseView.builder()
			.responseCode(responseCode)
			.responseMessage(responseMessage)
			.transactionId(nextTransactionId.toString())
			.transactionInfo(command.getTransactionInfo())
			.purseName(command.getPurseName())
			.accountPurseId(Long.toString(accountPurseId))
			.availablePurseBalance(availablePurseBalance)
			.authorizedAmount(authorizedAmount)
			.cardStatus(cardStatus.getStatusDescription())
			.effectiveDate(effectiveDate)
			.expiryDate(expiryDate)
			.transactionAmount(command.getTransactionAmount()) //
			.currency(command.getCurrency())
			.skuCode(command.getSkuCode())
			.action(command.getActionType())
			.date(command.getTransactionInfo()
				.getTransactionDateTime())
			.build();
	}

	private BigDecimal getNextTransactionId() {
		return sequenceService.getNextTxnSeqId();
	}

	private String getNextAuthorizationId() {
		return sequenceService.getNextAuthSeqId();
	}

	public Optional<AccountPurse> getMatchingAccountPurse(PurseUpdateActionType actionType, String purseName, LocalDateTime effectiveDate,
			LocalDateTime expiryDate, String skuCode, Long accountPurseId) {
		return this.accountPurseAggregate.getMatchingAccountPurse(actionType, purseName, effectiveDate, expiryDate, skuCode,
				accountPurseId);
	}

	public ProductPurse getProductPurseByPurseName(String purseName) {
		return this.accountPurseAggregate.getProductPurseByPurseName(purseName);
	}

	public PurseView execute(UpdatePurseStatusCommand command) {
		PurseView purseView = null;

		log.debug("Enter Process method");
		long timeBeforeTransaction = System.currentTimeMillis();
		log.info("UpdatePurseStatusCommand process transaction start time: {}", timeBeforeTransaction);

		BigDecimal nextTransactionId = null;
		String nextAuthorizationId = null;
		TransactionInfo transactionInfo = command.getTransactionInfo();

		try {
			this.populateValueDto(command.getPurseName());
			this.valueDtoMapper.populateTransactionInfo(this.valueDto.getValueObj(), command);

			nextTransactionId = this.getNextTransactionId();
			nextAuthorizationId = this.getNextAuthorizationId();

			Optional<PurseStatusUpdateTransactionInfo> previousTransactionInfoOpt = transactionValidator
				.getPreviousTransactionInfo(transactionInfo, PurseStatusUpdateTransactionInfo.class);
			if (previousTransactionInfoOpt.isPresent() && transactionValidator.isValidRetry(command, previousTransactionInfoOpt.get())) {

				PurseStatusUpdateTransactionInfo previousTransactionInfo = previousTransactionInfoOpt.get();
				purseView = PurseView.builder()
					.responseCode(DomainExceptionType.RETRY_REQUEST.getResponseCode())
					.responseMessage(DomainExceptionType.RETRY_REQUEST.getDefaultMessage())
					.transactionId(nextTransactionId.toString())
					.transactionInfo(command.getTransactionInfo())
					.purseName(command.getPurseName())
					.purseStatus(previousTransactionInfo.getPurseStatus())
					.purseStartDate(DateTimeUtil.toDefaultTimeZone(command.getStartDate()))
					.purseEndDate(DateTimeUtil.toDefaultTimeZone(command.getEndDate()))
					.build();

				TransactionLog transactionLog = TransactionLogBuilderHelper.from(nextTransactionId)
					.accountAggregate(this)
					.command(command)
					.response(DomainExceptionType.RETRY_REQUEST.getResponseCode(), DomainExceptionType.RETRY_REQUEST.getDefaultMessage(),
							"F")
					.build();

				domainEventPublisher.raise(TransactionFailedEvent.from(transactionLog));

				return purseView;

			}

			transactionValidator.validate(command, this);

			BigDecimal transactionFee = this.getTransactionFee(this.valueDto.getValueObj()
				.get(ValueObjectKeys.PROD_TXN_FEE_AMT));

			PurseUpdate purseUpdate = this.accountPurseAggregate.updatePurseStatus(command.getPurseName(), command.getPurseStatus(),
					command.getStartDate(), command.getEndDate());

			RequestInfo req = requestInfoService.getRequestInfo(valueDto);
			req.setTxnSeqId(nextTransactionId);
			req.setAuthId(nextAuthorizationId);

			PurseStatusUpdateTransactionInfo statusUpdateTransactionInfo = PurseStatusUpdateTransactionInfo.builder()
				.spNumberType(transactionInfo.getSpNumberType()
					.isPresent()
							? transactionInfo.getSpNumberType()
								.get()
							: null)
				.spNumber(transactionInfo.getSpNumber())
				.purseName(command.getPurseName())
				.purseStatus(command.getPurseStatus())
				.transactionDateTime(LocalDateTime.now())
				.startDate(purseUpdate.getNewStartDate())
				.endDate(purseUpdate.getNewEndDate())
				.build();

//			TransactionLog transactionLog = logService.getTransactionLog(req);
//			transactionLog.setRequestXml(JsonUtil.mapToJson(statusUpdateTransactionInfo));

			TransactionLog transactionLog = TransactionLogBuilderHelper.from(nextTransactionId, nextAuthorizationId)
				.accountAggregate(this)
				.command(command)
				.requestXml(JsonUtil.mapToJson(statusUpdateTransactionInfo))
				.purseUpdate(purseUpdate, transactionFee)
				.response(ResponseCodes.SUCCESS, "OK", "C")
				.build();

			PurseStatusUpdatedEvent event = PurseStatusUpdatedEvent.builder()
				.accountId(this.accountId)
				.rrn(command.getTransactionInfo()
					.getCorrelationId())
				.purseUpdate(purseUpdate)
				.statementLogs(logService.getStatementLogList(req))
				.transactionLog(transactionLog)
				.transactionDate(LocalDateTime.now())
				.transactionInfo(statusUpdateTransactionInfo)
				.build();

			purseView = PurseView.builder()
				.responseCode("0")
				.responseMessage("success")
				.transactionId(nextTransactionId.toString())
				.transactionInfo(command.getTransactionInfo())
				.purseName(command.getPurseName())
				.purseStatus(purseUpdate.getNewStatus())
				.purseStartDate(DateTimeUtil.convert(purseUpdate.getNewStartDate()))
				.purseEndDate(DateTimeUtil.convert(purseUpdate.getNewEndDate()))
				.build();

			domainEventPublisher.raise(event);

		} catch (ServiceException exception) {
			log.info("ResponseCode:" + exception.getResponseID() + " Message:" + exception.getMessage() + ", Reason:"
					+ exception.getReason());
			if (exception.getCause() != null && !(exception.getCause() instanceof ServiceException)) {
				log.error(exception.getCause()
					.getMessage(), exception.getCause());
			}
			this.domainEventPublisher.raise(TransactionFailedEvent
				.from(this.getFailedTransaction(nextTransactionId, command, exception.getResponseID(), exception.getMessage())));

			throw exception;
		} catch (DomainException exception) {
			log.info("ResponseCode:" + exception.getExceptionType()
				.getResponseCode() + " Message:" + exception.getMessage() + ", System Message:" + exception.getSystemMessage());

			this.domainEventPublisher
				.raise(TransactionFailedEvent.from(this.getFailedTransaction(nextTransactionId, command, exception.getExceptionType()
					.getResponseCode(), exception.getMessage())));

			throw exception;
		} catch (Exception exception) {
			log.error("Process Terminated Due to Exception :" + exception.getMessage(), exception);

			this.domainEventPublisher.raise(TransactionFailedEvent.from(this.getFailedTransaction(nextTransactionId, command,
					DomainExceptionType.SYSTEM_ERROR.getResponseCode(), exception.getMessage())));

			throw DomainExceptionFactory.from(DomainExceptionType.SYSTEM_ERROR, exception.getMessage());
		} finally {
			long timeTaken = System.currentTimeMillis() - timeBeforeTransaction;
			log.info("Transaction response Time:{}ms", timeTaken);

		}

		log.info("Transaction response {}", purseView == null ? null : purseView.toString());
		return purseView;

	}

	private TransactionLog getFailedTransaction(BigDecimal transactionId, UpdatePurseStatusCommand command, String responseCode,
			String responseMessage) {

		return TransactionLogBuilderHelper.from(transactionId)
			.accountAggregate(this)
			.command(command)
			.response(responseCode, responseMessage, "F")
			.build();
	}

	public AccountPurseView execute(UpdateRolloverAccountPurseCommand command) {
		AccountPurseView accountPurseView = null;

		log.debug("Enter Process method");
		long timeBeforeTransaction = System.currentTimeMillis();
		log.info("UpdateAccountPurseCommand process transaction start time: {}", timeBeforeTransaction);

		BigDecimal nextTransactionId = null;
		String nextAuthorizationId = null;
		TransactionInfo transactionInfo = command.getTransactionInfo();

		try {
			this.populateValueDto(command.getPurseName());
			this.valueDtoMapper.populateTransactionInfo(this.valueDto.getValueObj(), command);

			Optional<PurseUpdateTransactionInfo> previousTransactionInfoOpt = transactionValidator
				.getPreviousTransactionInfo(transactionInfo, PurseUpdateTransactionInfo.class);
			if (previousTransactionInfoOpt.isPresent() && transactionValidator.isValidRetry(command, previousTransactionInfoOpt.get())) {
				nextTransactionId = this.getNextTransactionId();
				PurseUpdateTransactionInfo previousTransactionInfo = previousTransactionInfoOpt.get();
				accountPurseView = this.createAccountPurseView( //
						DomainExceptionType.RETRY_REQUEST.getResponseCode(), //
						DomainExceptionType.RETRY_REQUEST.getDefaultMessage(), //
						nextTransactionId, //
						command, //
						previousTransactionInfo.getAccountPurseId(), //
						DateTimeUtil.toDefaultTimeZone(previousTransactionInfo.getEffectiveDate()), //
						DateTimeUtil.toDefaultTimeZone(previousTransactionInfo.getExpiryDate()), //
						previousTransactionInfo.getAvailablePurseBalance(), //
						previousTransactionInfo.getAuthorizedAmount(), //
						previousTransactionInfo.getCardStatus());

				TransactionLog transactionLog = TransactionLogBuilderHelper.from(nextTransactionId)
					.accountAggregate(this)
					.command(command)
					.purseUpdateTransactionInfo(previousTransactionInfo)
					.response(DomainExceptionType.RETRY_REQUEST.getResponseCode(), DomainExceptionType.RETRY_REQUEST.getDefaultMessage(),
							"F")
					.build();

				domainEventPublisher.raise(TransactionFailedEvent.from(transactionLog));

				return accountPurseView;
			}

			transactionValidator.validate(command, this);

			BigDecimal maxPurseBalance = this.getValueDto()
				.getProductAttributes()
				.get(ValueObjectKeys.PURSE)
				.containsKey(ValueObjectKeys.MAX_PURSE_BALANCE)
						? new BigDecimal(String.valueOf(this.getValueDto()
							.getProductAttributes()
							.get(ValueObjectKeys.PURSE)
							.get(ValueObjectKeys.MAX_PURSE_BALANCE)))
						: BigDecimal.valueOf(PurseValidator.MAX_BALANCE);

			BigDecimal transactionFee = this.getTransactionFee(this.valueDto.getValueObj()
				.get(ValueObjectKeys.PROD_TXN_FEE_AMT));

			List<AccountPurse> expiredAccountPurses = wrapIfNull(this.accountPurseAggregate.expiredAccountPursesByPurseName);
			if (CollectionUtils.isEmpty(expiredAccountPurses)) {
				nextTransactionId = this.getNextTransactionId();
				throw DomainExceptionFactory.from(DomainExceptionType.INPUT_VALIDATION_FAILED,
						"Unable to locate the expired account purse with purse name:" + command.getPurseName());
			}

			AccountPurseUpdate accountPurseUpdate1 = null;
			AccountPurseUpdate accountPurseUpdate2 = null;

			for (AccountPurse expiredAccountPurse : expiredAccountPurses) {
				nextAuthorizationId = this.getNextAuthorizationId();
				nextTransactionId = this.getNextTransactionId();
				BigDecimal rolloverTxnAmt;
				if (command.getPercentageAmount()
					.compareTo(BigDecimal.ZERO) > 0) {
					rolloverTxnAmt = expiredAccountPurse.getAvailableBalance()
						.multiply(command.getPercentageAmount())
						.divide(new BigDecimal(100));
				} else {
					rolloverTxnAmt = command.getTransactionAmount();
				}

				accountPurseUpdate1 = this.accountPurseAggregate.updateAccountPurse( //
						PurseUpdateActionType.valueOf("AUTO_ROLLOVER_DEBIT"), //
						command.getPurseName(), //
						expiredAccountPurse.getAccountPurseKey()
							.getAccountPurseId(), //
						rolloverTxnAmt, //
						command.getEffectiveDate(), //
						command.getExpiryDate(), //
						command.getSkuCode(), //
						BigDecimal.ZERO, //
						valueDto.getUsageFee(), //
						valueDto.getUsageLimit(), maxPurseBalance);

				RequestInfo req1 = requestInfoService.getRequestInfo(valueDto);
				req1.setTxnSeqId(nextTransactionId);
				req1.setAuthId(nextAuthorizationId);
				req1.setAccountId(BigInteger.valueOf(accountPurseUpdate1.getAccountPurseKey()
					.getAccountId()));
				req1.setAuthorizedAmount(Double.valueOf(String.valueOf(accountPurseUpdate1.getAuthorizedAmount())));
				req1.setAccountPurseId(accountPurseUpdate1.getAccountPurseKey()
					.getAccountPurseId());
				req1.setOpeningAvailBalance(Double.valueOf(String.valueOf(accountPurseUpdate1.getPreviousAvailableBalance())));
				req1.setOpeningLedgerBalance(Double.valueOf(String.valueOf(accountPurseUpdate1.getPreviousLedgerBalance())));
				req1.setTxnAmount(Double.valueOf(String.valueOf(accountPurseUpdate1.getAuthorizedAmount()
					.add(BigDecimal.ZERO))));
				req1.setCreditDebitFlg(TransactionConstant.DEBIT_CARD);

				PurseUpdateTransactionInfo purseUpdateTransactionInfo1 = PurseUpdateTransactionInfo.builder()
					.spNumberType(transactionInfo.getSpNumberType()
						.isPresent()
								? transactionInfo.getSpNumberType()
									.get()
								: null)
					.spNumber(transactionInfo.getSpNumber())
					.purseName(command.getPurseName())
					.actionType(command.getActionType())
					.transactionAmount(rolloverTxnAmt)
					.effectiveDate(command.getEffectiveDate())
					.expiryDate(command.getExpiryDate())
					.skuCode(command.getSkuCode())
					.currency(command.getCurrency())
					.transactionDateTime(LocalDateTime.now())
					.inputAccountPurseId(command.getAccountPurseId())
					.accountPurseId(accountPurseUpdate1.getAccountPurseKey()
						.getAccountPurseId())
					.authorizedAmount(accountPurseUpdate1.getAuthorizedAmount())
					.availablePurseBalance(accountPurseUpdate1.getNewAvailableBalance())
					.cardStatus(this.cardEntity.getCardStatus())
					.build();

				accountPurseUpdate2 = this.accountPurseAggregate.updateAccountPurse( //
						PurseUpdateActionType.valueOf("AUTO_ROLLOVER_CREDIT"), //
						command.getPurseName(), //
						command.getAccountPurseId(), //
						accountPurseUpdate1.getAuthorizedAmount(), //
						command.getEffectiveDate(), //
						command.getExpiryDate(), //
						command.getSkuCode(), //
						transactionFee, //
						valueDto.getUsageFee(), //
						valueDto.getUsageLimit(), maxPurseBalance);

				RequestInfo req2 = requestInfoService.getRequestInfo(valueDto);
				req2.setTxnSeqId(nextTransactionId);
				req2.setAuthId(nextAuthorizationId);
				req2.setAccountId(BigInteger.valueOf(accountPurseUpdate2.getAccountPurseKey()
					.getAccountId()));
				req2.setAuthorizedAmount(Double.valueOf(String.valueOf(accountPurseUpdate2.getAuthorizedAmount())));
				req2.setAccountPurseId(accountPurseUpdate2.getAccountPurseKey()
					.getAccountPurseId());
				req2.setOpeningAvailBalance(Double.valueOf(String.valueOf(accountPurseUpdate2.getPreviousAvailableBalance())));
				req2.setOpeningLedgerBalance(Double.valueOf(String.valueOf(accountPurseUpdate2.getPreviousLedgerBalance())));
				req2.setTxnAmount(Double.valueOf(String.valueOf(accountPurseUpdate2.getAuthorizedAmount()
					.add(transactionFee))));
				req2.setTxnDesc(this.valueDto.getValueObj()
					.get(ValueObjectKeys.TRANSACTIONDESC) + " "
						+ accountPurseUpdate1.getAccountPurseKey()
							.getAccountPurseId());

				PurseUpdateTransactionInfo purseUpdateTransactionInfo2 = PurseUpdateTransactionInfo.builder()
					.spNumberType(transactionInfo.getSpNumberType()
						.isPresent()
								? transactionInfo.getSpNumberType()
									.get()
								: null)
					.spNumber(transactionInfo.getSpNumber())
					.purseName(command.getPurseName())
					.actionType(command.getActionType())
					.transactionAmount(accountPurseUpdate1.getAuthorizedAmount())
					.effectiveDate(command.getEffectiveDate())
					.expiryDate(command.getExpiryDate())
					.skuCode(command.getSkuCode())
					.currency(command.getCurrency())
					.transactionDateTime(LocalDateTime.now())
					.inputAccountPurseId(command.getAccountPurseId())
					.accountPurseId(accountPurseUpdate2.getAccountPurseKey()
						.getAccountPurseId())
					.authorizedAmount(accountPurseUpdate2.getAuthorizedAmount())
					.availablePurseBalance(accountPurseUpdate2.getNewAvailableBalance())
					.cardStatus(this.cardEntity.getCardStatus())
					.build();

				TransactionLog transactionLog = TransactionLogBuilderHelper.from(nextTransactionId, nextAuthorizationId)
					.accountAggregate(this)
					.command(command)
					.requestXml(JsonUtil.mapToJson(purseUpdateTransactionInfo1))
					.accountPurseUpdate(accountPurseUpdate2, transactionFee)
					.response(ResponseCodes.SUCCESS, "OK", "C")
					.businessDateField(logService.cutOverTime(this.valueDto.getProductAttributes()))
					.build();

				AccountPurseUpdatedEvent event = AccountPurseUpdatedEvent.builder()
					.accountPurseUpdate(accountPurseUpdate1)
					.accountPurseUpdate(accountPurseUpdate2)
					.statementLogs(logService.getStatementLogList(req1))
					.statementLogs(logService.getStatementLogList(req2))
					.transactionLog(transactionLog)
					.transactionDate(LocalDateTime.now())
					.transactionInfo(purseUpdateTransactionInfo1)
					.transactionInfo(purseUpdateTransactionInfo2)
					.build();

				domainEventPublisher.raise(event);
				transactionFee = BigDecimal.ZERO;
			}

			accountPurseView = this.createAccountPurseView( //
					ResponseCodes.SUCCESS, //
					"success", nextTransactionId, //
					command, //
					accountPurseUpdate2.getAccountPurseKey()
						.getAccountPurseId(), //
					DateTimeUtil.convert(accountPurseUpdate2.getEffectiveDate()), //
					DateTimeUtil.convert(accountPurseUpdate2.getExpiryDate()), //
					accountPurseUpdate2.getNewAvailableBalance(), //
					accountPurseUpdate2.getAuthorizedAmount(), //
					this.cardEntity.getCardStatus() //
			);

		} catch (ServiceException exception) {
			log.info("ResponseCode:" + exception.getResponseID() + " Message:" + exception.getMessage() + ", Reason:"
					+ exception.getReason());
			if (exception.getCause() != null && !(exception.getCause() instanceof ServiceException)) {
				log.error(exception.getCause()
					.getMessage(), exception.getCause());
			}

			this.domainEventPublisher.raise(TransactionFailedEvent
				.from(this.getFailedTransaction(nextTransactionId, command, exception.getResponseID(), exception.getMessage())));

			throw exception;
		} catch (DomainException exception) {
			log.info("ResponseCode:" + exception.getExceptionType()
				.getResponseCode() + " Message:" + exception.getMessage() + ", System Message:" + exception.getSystemMessage());

			this.domainEventPublisher
				.raise(TransactionFailedEvent.from(this.getFailedTransaction(nextTransactionId, command, exception.getExceptionType()
					.getResponseCode(), exception.getMessage())));

			throw exception;
		} catch (Exception exception) {
			log.error("Process Terminated Due to Exception :" + exception.getMessage(), exception);
			this.domainEventPublisher.raise(TransactionFailedEvent.from(this.getFailedTransaction(nextTransactionId, command,
					DomainExceptionType.SYSTEM_ERROR.getResponseCode(), exception.getMessage())));
			throw DomainExceptionFactory.from(DomainExceptionType.SYSTEM_ERROR, exception.getMessage());
		} finally {
			long timeTaken = System.currentTimeMillis() - timeBeforeTransaction;
			log.info("Transaction response Time:{}ms", timeTaken);

		}

		log.info("Transaction response {}", accountPurseView == null ? null : accountPurseView.toString());
		return accountPurseView;

	}

	private AccountPurseView createAccountPurseView( //
			String responseCode, //
			String responseMessage, //
			BigDecimal nextTransactionId, //
			UpdateRolloverAccountPurseCommand command, //
			long accountPurseId, //
			ZonedDateTime effectiveDate, //
			ZonedDateTime expiryDate, //
			BigDecimal availablePurseBalance, //
			BigDecimal authorizedAmount, CardStatusType cardStatus) {

		return AccountPurseView.builder()
			.responseCode(responseCode)
			.responseMessage(responseMessage)
			.transactionId(nextTransactionId.toString())
			.transactionInfo(command.getTransactionInfo())
			.purseName(command.getPurseName())
			.accountPurseId(Long.toString(accountPurseId))
			.availablePurseBalance(availablePurseBalance)
			.authorizedAmount(authorizedAmount)
			.cardStatus(cardStatus.getStatusDescription())
			.effectiveDate(effectiveDate)
			.expiryDate(expiryDate)
			.transactionAmount(command.getTransactionAmount()) //
			.currency(command.getCurrency())
			.skuCode(command.getSkuCode())
			.action(command.getActionType())
			.date(command.getTransactionInfo()
				.getTransactionDateTime())
			.build();
	}

	private TransactionLog getFailedTransaction(BigDecimal transactionId, UpdateRolloverAccountPurseCommand command, String responseCode,
			String responseMessage) {

		return TransactionLogBuilderHelper.from(transactionId)
			.accountAggregate(this)
			.command(command)
			.response(responseCode, responseMessage, "F")
			.build();
	}

}
