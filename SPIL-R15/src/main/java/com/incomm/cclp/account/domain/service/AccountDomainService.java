package com.incomm.cclp.account.domain.service;

import static com.incomm.cclp.account.util.CodeUtil.isNull;
import static com.incomm.cclp.account.util.CodeUtil.isPositive;
import static com.incomm.cclp.account.util.CodeUtil.mapToOptionalLong;
import static com.incomm.cclp.account.util.CodeUtil.mapYNToBoolean;
import static com.incomm.cclp.account.util.CodeUtil.mapZeroOneToBoolean;
import static com.incomm.cclp.account.util.CodeUtil.not;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.incomm.cclp.account.application.command.AccountTransactionCommand;
import com.incomm.cclp.account.application.command.SpNumber;
import com.incomm.cclp.account.application.command.TransactionInfo;
import com.incomm.cclp.account.application.persistence.AccountTransactionPersistenceHandler;
import com.incomm.cclp.account.application.persistence.AccountTransactionUpdate;
import com.incomm.cclp.account.domain.factory.AccountAggregateRepository;
import com.incomm.cclp.account.domain.factory.AccountPurseAggregateStateType;
import com.incomm.cclp.account.domain.factory.AccountPurseStateType;
import com.incomm.cclp.account.domain.factory.AccountPurseUsageStateType;
import com.incomm.cclp.account.domain.factory.LogServiceFactory;
import com.incomm.cclp.account.domain.model.AccountAggregateUpdate;
import com.incomm.cclp.account.domain.model.AccountEntity;
import com.incomm.cclp.account.domain.model.AccountPurseAggregateNew;
import com.incomm.cclp.account.domain.model.AccountPurseGroupUpdate;
import com.incomm.cclp.account.domain.model.AccountPurseKey;
import com.incomm.cclp.account.domain.model.AccountPurseOperationPartial;
import com.incomm.cclp.account.domain.model.AccountUpdate;
import com.incomm.cclp.account.domain.model.CardEntity;
import com.incomm.cclp.account.domain.model.CardStatusType;
import com.incomm.cclp.account.domain.model.CardUpdate;
import com.incomm.cclp.account.domain.model.LedgerEntry;
import com.incomm.cclp.account.domain.model.LedgerEntryType;
import com.incomm.cclp.account.domain.model.MessageType;
import com.incomm.cclp.account.domain.model.OperationType;
import com.incomm.cclp.account.domain.model.PurseBalanceType;
import com.incomm.cclp.account.domain.model.RedemptionLockAggregate;
import com.incomm.cclp.account.domain.model.RedemptionLockEntity;
import com.incomm.cclp.account.domain.model.RedemptionLockUpdate;
import com.incomm.cclp.account.domain.model.TransactionLogUpdate;
import com.incomm.cclp.account.domain.validator.TransactionValidator;
import com.incomm.cclp.account.domain.view.AccountPurseGroupView;
import com.incomm.cclp.account.domain.view.AccountPurseViewNew;
import com.incomm.cclp.account.domain.view.AccountSummaryView;
import com.incomm.cclp.account.util.CodeUtil;
import com.incomm.cclp.account.util.DateTimeFormatType;
import com.incomm.cclp.account.util.DateTimeUtil;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.service.SequenceService;
import com.incomm.cclp.domain.StatementsLog;
import com.incomm.cclp.domain.TransactionLog;
import com.incomm.cclp.domain.TransactionLogInfo;
import com.incomm.cclp.dto.PurseAuthResponse;
import com.incomm.cclp.dto.StatementLog;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.repos.GenericRepo;
import com.incomm.cclp.transaction.constants.GeneralConstants;
import com.incomm.cclp.util.Util;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class AccountDomainService {

	@Autowired
	private AccountAggregateRepository repository;

	@Autowired
	private LogServiceFactory logService;

	@Autowired
	private SequenceService sequenceService;

	@Autowired
	private TransactionValidator transactionValidator;

	@Autowired
	private GenericRepo genericRepository;

	@Autowired
	private AccountTransactionPersistenceHandler persistenceHandler;

	public AccountSummaryView executeCommand(AccountTransactionCommand command) {

		Optional<TransactionConfigType> transactionConfigType = TransactionConfigType.byTransactionType(command.getTransactionInfo()
			.getTransactionType());

		if (transactionConfigType.isPresent()) {
			log.info("Processing transaction {}", transactionConfigType.get()
				.getTransactionType()
				.getTransactionShortName());
			return transactionConfigType.get()
				.getFunction()
				.apply(command);
		}

		log.error("Transaction configuration not found for : {} ", command.getTransactionInfo()
			.getTransactionType()
			.getTransactionDescription());
		throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
	}

	public AccountSummaryView processBalanceInquiry(AccountTransactionCommand command) {

		ValueDTO valueDto = command.getValueDTO();
		long purseId = getPurseId(valueDto.getValueObj()
			.get(ValueObjectKeys.PURSE_ID));
		CardEntity cardEntity = this.repository.loadCardEntity(valueDto.getValueObj());
		boolean allowPartialAuth = isPartialAuthAllowed(Util.getProductPartialIndicator(valueDto));

		AccountPurseAggregateNew accountPurseAggregate = this.repository.loadAccountPurseAggregate(cardEntity.getAccountId(),
				cardEntity.getProductId(), purseId, AccountPurseAggregateStateType.from(AccountPurseStateType.ALL_UNEXPIRED));
		Map<LedgerEntryType, BigDecimal> transactionFees = getTransactionFees(valueDto.getValueObj());

		AccountPurseGroupUpdate accountPurseGroupUpdate = accountPurseAggregate.debitPurseBalance(purseId, BigDecimal.ZERO, transactionFees,
				PurseBalanceType.BOTH, allowPartialAuth);

		AccountAggregateUpdate accountAggregateUpdate = createAggregateUpdate(accountPurseGroupUpdate, Optional.empty(), Optional.empty());
		long authId = saveTransaction(command, cardEntity, accountAggregateUpdate, Collections.emptyList(), Optional.empty());

		List<AccountPurseGroupView> accountPurseGroupViews = accountPurseAggregate.getAccountPurseBalances(Boolean.TRUE, Boolean.TRUE);

		BigDecimal authorizedAmount = isNull(accountPurseGroupUpdate) ? BigDecimal.ZERO
				: getAuthorizedAmount(accountPurseGroupUpdate.getLedgerEntries());

		List<PurseAuthResponse> purseAuthResponses = getPurseAuthResponses(command.getDefaultPurseId(), purseId, accountPurseGroupUpdate,
				command.getPurseCurrency(), command.getPurseName(), command.getSkuCode());

		return getSuccessResponseView(command, authId, accountPurseGroupViews, accountPurseAggregate.getDefaultPurseAvailableBalance(),
				cardEntity.getCardStatus(), Optional.empty(), Optional.empty(), authorizedAmount, purseAuthResponses);
	}

	public AccountSummaryView processRedemption(AccountTransactionCommand command) {

		ValueDTO valueDto = command.getValueDTO();
		Optional<Long> accountPurseId = command.getAccountPurseId();
		long purseId = getPurseId(valueDto.getValueObj()
			.get(ValueObjectKeys.PURSE_ID));
		boolean allowPartialAuth = isPartialAuthAllowed(Util.getProductPartialIndicator(valueDto));

		this.transactionValidator.validateTransactionAmount(command.getTransactionAmount());

		CardEntity cardEntity = this.repository.loadCardEntity(valueDto.getValueObj());
		AccountPurseAggregateNew accountPurseAggregate = loadAccountPurseAggregate(cardEntity.getAccountId(), cardEntity.getProductId(),
				purseId, command.getPurseName(), AccountPurseStateType.ALL_UNEXPIRED);

		accountPurseAggregate.updateLimitsAndFee(purseId, valueDto.getUsageFee(), valueDto.getUsageLimit());
		Map<LedgerEntryType, BigDecimal> transactionFees = getTransactionFees(valueDto.getValueObj());

		AccountPurseGroupUpdate accountPurseGroupUpdate;
		Optional<CardUpdate> cardUpdate = Optional.empty();
		Optional<AccountUpdate> accountUpdate = Optional.empty();

		if (accountPurseId.isPresent()) {
			log.info("Debit amount from single purse with accountPurseId: {}", accountPurseId.get());
			accountPurseGroupUpdate = accountPurseAggregate.debitPurseBalance(purseId, accountPurseId.get(), command.getTransactionAmount(),
					transactionFees, PurseBalanceType.BOTH, Boolean.TRUE);
		} else {
			log.info("Debit amount from default Purse/multiple purses with purseId: {}", purseId);
			accountPurseGroupUpdate = accountPurseAggregate.debitPurseBalance(purseId, command.getTransactionAmount(), transactionFees,
					PurseBalanceType.BOTH, allowPartialAuth);
		}

		AccountAggregateUpdate accountAggregateUpdate = createAggregateUpdate(accountPurseGroupUpdate, cardUpdate, accountUpdate);
		long authId = saveTransaction(command, cardEntity, accountAggregateUpdate, Collections.emptyList(), Optional.empty());
		boolean includeFutureEffectivePurses = true;
		boolean includeZeroBalancePurses = true;
		List<AccountPurseGroupView> accountPurseGroupViews = accountPurseAggregate.getAccountPurseBalances(includeFutureEffectivePurses,
				includeZeroBalancePurses);

		BigDecimal authorizedAmount = isNull(accountPurseGroupUpdate) ? BigDecimal.ZERO
				: getAuthorizedAmount(accountPurseGroupUpdate.getLedgerEntries());
		List<PurseAuthResponse> purseAuthResponses = getPurseAuthResponses(command.getDefaultPurseId(), purseId, accountPurseGroupUpdate,
				command.getPurseCurrency(), command.getPurseName(), command.getSkuCode());

		return getSuccessResponseView(command, authId, accountPurseGroupViews, accountPurseAggregate.getDefaultPurseAvailableBalance(),
				cardEntity.getCardStatus(), Optional.empty(), Optional.empty(), authorizedAmount, purseAuthResponses);
	}

	public AccountSummaryView processRedeemReversal(AccountTransactionCommand command) {
		Optional<CardUpdate> cardUpdate = Optional.empty();
		Optional<AccountUpdate> accountUpdate = Optional.empty();
		return processReversal(command, cardUpdate, accountUpdate);
	}

	public AccountSummaryView processStoreCredit(AccountTransactionCommand command) {

		ValueDTO valueDto = command.getValueDTO();
		Optional<Long> accountPurseId = command.getAccountPurseId();
		long purseId = getPurseId(valueDto.getValueObj()
			.get(ValueObjectKeys.PURSE_ID));

		CardEntity cardEntity = this.repository.loadCardEntity(valueDto.getValueObj());
		AccountPurseAggregateNew accountPurseAggregate = loadAccountPurseAggregate(cardEntity.getAccountId(), cardEntity.getProductId(),
				purseId, command.getPurseName(), AccountPurseStateType.ALL_UNEXPIRED);

		accountPurseAggregate.setMaxPurseBalance(purseId, getMaxPurseBalance(valueDto, purseId, command.getDefaultPurseId()));

		Map<LedgerEntryType, BigDecimal> transactionFees = getTransactionFees(valueDto.getValueObj());

		AccountPurseGroupUpdate accountPurseGroupUpdate;

		if (accountPurseId.isPresent()) {
			log.info("Credit to the single purse");
			accountPurseGroupUpdate = accountPurseAggregate.creditPurseBalance(purseId, accountPurseId.get(),
					command.getTransactionAmount(), transactionFees);
		} else {
			log.info("Credit amount to last expiring purse");
			accountPurseGroupUpdate = accountPurseAggregate.creditPurseBalance(purseId, command.getTransactionAmount(), transactionFees,
					false);
		}

		AccountAggregateUpdate accountAggregateUpdate;
		Optional<CardUpdate> cardUpdate = Optional.empty();
		Optional<AccountUpdate> accountUpdate = Optional.empty();

		accountAggregateUpdate = createAggregateUpdate(accountPurseGroupUpdate, cardUpdate, accountUpdate);
		long authId = saveTransaction(command, cardEntity, accountAggregateUpdate, null, Optional.empty());
		boolean includeFutureEffectivePurses = true;
		boolean includeZeroBalancePurses = true;
		List<AccountPurseGroupView> accountPurseGroupViews = accountPurseAggregate.getAccountPurseBalances(includeFutureEffectivePurses,
				includeZeroBalancePurses);

		BigDecimal authorizedAmount = isNull(accountPurseGroupUpdate) ? BigDecimal.ZERO
				: getAuthorizedAmount(accountPurseGroupUpdate.getLedgerEntries());
		List<PurseAuthResponse> purseAuthResponses = getPurseAuthResponses(command.getDefaultPurseId(), purseId, accountPurseGroupUpdate,
				command.getPurseCurrency(), command.getPurseName(), command.getSkuCode());

		return getSuccessResponseView(command, authId, accountPurseGroupViews, accountPurseAggregate.getDefaultPurseAvailableBalance(),
				cardEntity.getCardStatus(), Optional.empty(), Optional.empty(), authorizedAmount, purseAuthResponses);
	}

	public AccountSummaryView processStoreCreditReversal(AccountTransactionCommand command) {
		Optional<CardUpdate> cardUpdate = Optional.empty();
		Optional<AccountUpdate> accountUpdate = Optional.empty();
		return processReversal(command, cardUpdate, accountUpdate);
	}

	public AccountSummaryView processValueInsertion(AccountTransactionCommand command) {

		ValueDTO valueDto = command.getValueDTO();
		Optional<Long> accountPurseId = command.getAccountPurseId();
		long purseId = getPurseId(valueDto.getValueObj()
			.get(ValueObjectKeys.PURSE_ID));

		if (command.getTransactionAmount()
			.compareTo(BigDecimal.ZERO) <= 0) {
			log.info("Invalid transaction amount {}. Insertion value can not be -ve or zero", command.getTransactionAmount());
			throw new ServiceException(SpilExceptionMessages.INVALID_AMOUNT, ResponseCodes.INVALID_AMOUNT);
		}

		CardEntity cardEntity = this.repository.loadCardEntity(valueDto.getValueObj());
		AccountEntity accountEntity = this.repository.loadAccountEntity(cardEntity.getAccountId());
		AccountPurseAggregateNew accountPurseAggregate = loadAccountPurseAggregate(cardEntity.getAccountId(), cardEntity.getProductId(),
				purseId, command.getPurseName(), AccountPurseStateType.ALL_UNEXPIRED);

		// Get Fees from Limit Check value dto
		accountPurseAggregate.updateLimitsAndFee(purseId, valueDto.getUsageFee(), valueDto.getUsageLimit());
		accountPurseAggregate.setMaxPurseBalance(purseId, getMaxPurseBalance(valueDto, purseId, command.getDefaultPurseId()));

		// Get Fees from Fee Check value dto
		Map<LedgerEntryType, BigDecimal> transactionFees = getTransactionFees(valueDto.getValueObj());

		AccountPurseGroupUpdate accountPurseGroupUpdate;

		Optional<CardUpdate> cardUpdate = Optional.empty();
		Optional<AccountUpdate> accountUpdate = Optional.empty();

		if (not(cardEntity.isFirstTimeTopUpCompleted())) {
			log.info("Set first time top up flag to TRUE and Intitial Load amount to {}", command.getTransactionAmount());
			cardUpdate = Optional.of(cardEntity.updateFirstTimeTopUp(false));
			accountUpdate = Optional.of(accountEntity.updateInitialLoadAmount(command.getTransactionAmount()));
		}

		if (accountPurseId.isPresent()) {
			log.info("Credit to the account purse id {} ", accountPurseId.isPresent());
			accountPurseGroupUpdate = accountPurseAggregate.creditPurseBalance(purseId, accountPurseId.get(),
					command.getTransactionAmount(), transactionFees);
		} else {
			log.info("Credit amount to last expiring purse");
			accountPurseGroupUpdate = accountPurseAggregate.creditPurseBalance(purseId, command.getTransactionAmount(), transactionFees,
					true);
		}

		AccountAggregateUpdate accountAggregateUpdate = createAggregateUpdate(accountPurseGroupUpdate, cardUpdate, accountUpdate);
		long authId = saveTransaction(command, cardEntity, accountAggregateUpdate, null, Optional.empty());
		boolean includeFutureEffectivePurses = true;
		boolean includeZeroBalancePurses = true;
		List<AccountPurseGroupView> accountPurseGroupViews = accountPurseAggregate.getAccountPurseBalances(includeFutureEffectivePurses,
				includeZeroBalancePurses);
		BigDecimal authorizedAmount = isNull(accountPurseGroupUpdate) ? BigDecimal.ZERO
				: getAuthorizedAmount(accountPurseGroupUpdate.getLedgerEntries());
		List<PurseAuthResponse> purseAuthResponses = getPurseAuthResponses(command.getDefaultPurseId(), purseId, accountPurseGroupUpdate,
				command.getPurseCurrency(), command.getPurseName(), command.getSkuCode());

		return getSuccessResponseView(command, authId, accountPurseGroupViews, accountPurseAggregate.getDefaultPurseAvailableBalance(),
				cardEntity.getCardStatus(), Optional.empty(), Optional.empty(), authorizedAmount, purseAuthResponses);

	}

	public AccountSummaryView processValueInsertionReversal(AccountTransactionCommand command) {

		TransactionInfo transactionInfo = command.getTransactionInfo();
		ValueDTO valueDto = command.getValueDTO();
		String transactionDate = DateTimeUtil.convert(transactionInfo.getTransactionDateTime(), DateTimeFormatType.YYYYMMDD);
		Long purseId = Long.valueOf(valueDto.getValueObj()
			.get(ValueObjectKeys.PURSE_ID));

		CardEntity cardEntity = this.repository.loadCardEntity(command.getValueDTO()
			.getValueObj());
		AccountEntity accountEntity = this.repository.loadAccountEntity(cardEntity.getAccountId());

		// Load transactionLog
		TransactionLogInfo transactionLogInfo = this.repository.loadTransactionInfo(transactionInfo.getDeliveryChannelType(),
				MessageType.REVERSE, transactionInfo.getCorrelationId(), cardEntity.getCardNumberHash(), purseId.longValue(),
				transactionInfo.getTransactionDateTime()
					.toLocalDate(),
				transactionInfo.getTransactionType()
					.getTransactionCode());

		Boolean isLogFirstTimeTopUpFlag = CodeUtil.mapYNToBoolean(transactionLogInfo.getProcessFlag());

		Optional<CardUpdate> cardUpdate = Optional.empty();
		Optional<AccountUpdate> accountUpdate = Optional.empty();

		if (isLogFirstTimeTopUpFlag) {
			log.info("Set first time top up flag to FALSE and intitial Load amount to NULL");
			cardUpdate = Optional.of(cardEntity.updateFirstTimeTopUp(false));
			accountUpdate = Optional.of(accountEntity.updateInitialLoadAmount(null));
		}

		AccountPurseAggregateNew accountPurseAggregate = loadAccountPurseAggregate(cardEntity.getAccountId(), cardEntity.getProductId(),
				purseId, command.getPurseName(), AccountPurseStateType.ALL_UNEXPIRED);

		accountPurseAggregate.setMaxPurseBalance(purseId, getMaxPurseBalance(valueDto, purseId, command.getDefaultPurseId()));

		// reversal are system call, there will be no fee
		Map<LedgerEntryType, BigDecimal> transactionFees = new EnumMap<>(LedgerEntryType.class);

		// load ALL Statement Logs
		List<StatementLog> listStatementLogs = this.repository.loadStatementLogs(transactionInfo.getDeliveryChannelType(),
				transactionInfo.getCorrelationId(), cardEntity.getCardNumberHash(), transactionDate, transactionInfo.getTransactionType()
					.getTransactionCode());

		List<LedgerEntry> ledgerEntries = createLedgerEntries(listStatementLogs, cardEntity.getAccountId());

		List<RedemptionLockUpdate> redemptionLockUpdates = Collections.emptyList();
		PurseBalanceType purseBalanceType = PurseBalanceType.BOTH;

		// Perform Business operation
		AccountPurseGroupUpdate accountPurseGroupUpdate = accountPurseAggregate.revertPurseBalance(purseId, ledgerEntries, transactionFees,
				purseBalanceType);

		Optional<TransactionLogUpdate> transactionLogUpdate = Optional.of(TransactionLogUpdate.builder()
			.cardNumberHash(cardEntity.getCardNumberHash())
			.correlationId(transactionInfo.getCorrelationId())
			.isReversedFlag(GeneralConstants.YES)
			.transactionType(transactionInfo.getTransactionType())
			.build());

		AccountAggregateUpdate accountAggregateUpdate = createAggregateUpdate(accountPurseGroupUpdate, cardUpdate, accountUpdate);
		long authId = saveTransaction(command, cardEntity, accountAggregateUpdate, redemptionLockUpdates, transactionLogUpdate);
		boolean includeFutureEffectivePurses = true;
		boolean includeZeroBalancePurses = true;
		List<AccountPurseGroupView> accountPurseGroupViews = accountPurseAggregate.getAccountPurseBalances(includeFutureEffectivePurses,
				includeZeroBalancePurses);

		BigDecimal authorizedAmount = isNull(accountPurseGroupUpdate) ? BigDecimal.ZERO
				: getAuthorizedAmount(accountPurseGroupUpdate.getLedgerEntries());
		List<PurseAuthResponse> purseAuthResponses = getPurseAuthResponses(command.getDefaultPurseId(), purseId, accountPurseGroupUpdate,
				command.getPurseCurrency(), command.getPurseName(), command.getSkuCode());

		return getSuccessResponseView(command, authId, accountPurseGroupViews, accountPurseAggregate.getDefaultPurseAvailableBalance(),
				cardEntity.getCardStatus(), Optional.empty(), Optional.empty(), authorizedAmount, purseAuthResponses);
	}

	public AccountSummaryView processRedemptionWithLock(AccountTransactionCommand command) {
		ValueDTO valueDto = command.getValueDTO();
		Optional<Long> accountPurseId = command.getAccountPurseId();
		long purseId = getPurseId(valueDto.getValueObj()
			.get(ValueObjectKeys.PURSE_ID));
		boolean allowPartialAuth = isPartialAuthAllowed(Util.getProductPartialIndicator(command.getValueDTO()));

		this.transactionValidator.validateTransactionAmount(command.getTransactionAmount());

		CardEntity cardEntity = this.repository.loadCardEntity(command.getValueDTO()
			.getValueObj());
		AccountPurseAggregateNew accountPurseAggregate = loadAccountPurseAggregate(cardEntity.getAccountId(), cardEntity.getProductId(),
				purseId, command.getPurseName(), AccountPurseStateType.ALL_UNEXPIRED);
		RedemptionLockAggregate redemptionLockAggregate = loadRedemptionLockAggregate(cardEntity, command.getTransactionInfo());

		Map<LedgerEntryType, BigDecimal> transactionFees = getTransactionFees(command.getValueDTO()
			.getValueObj());
		accountPurseAggregate.updateLimitsAndFee(purseId, valueDto.getUsageFee(), valueDto.getUsageLimit());

		AccountPurseGroupUpdate accountPurseGroupUpdate;
		if (accountPurseId.isPresent()) {
			log.info("Debit amount from single purse");
			accountPurseGroupUpdate = accountPurseAggregate.debitPurseBalance(purseId, accountPurseId.get(), command.getTransactionAmount(),
					transactionFees, PurseBalanceType.AVAILABLE_BALANCE, Boolean.TRUE);
		} else {
			log.info("Debit amount from default Purse/ multiple purses");
			accountPurseGroupUpdate = accountPurseAggregate.debitPurseBalance(purseId, command.getTransactionAmount(), transactionFees,
					PurseBalanceType.AVAILABLE_BALANCE, allowPartialAuth);
		}

		List<RedemptionLockUpdate> redemptionLockUpdates = getRedemptionLockUpdates(accountPurseGroupUpdate.getLedgerEntries(),
				redemptionLockAggregate, purseId);
		Optional<CardUpdate> cardUpdate = Optional.empty();
		Optional<AccountUpdate> accountUpdate = Optional.empty();

		AccountAggregateUpdate accountAggregateUpdate = createAggregateUpdate(accountPurseGroupUpdate, cardUpdate, accountUpdate);
		long authId = saveTransaction(command, cardEntity, accountAggregateUpdate, redemptionLockUpdates, Optional.empty());
		boolean includeFutureEffectivePurses = true;
		boolean includeZeroBalancePurses = true;
		List<AccountPurseGroupView> accountPurseGroupViews = accountPurseAggregate.getAccountPurseBalances(includeFutureEffectivePurses,
				includeZeroBalancePurses);

		BigDecimal authorizedAmount = isNull(accountPurseGroupUpdate) ? BigDecimal.ZERO
				: getAuthorizedAmount(accountPurseGroupUpdate.getLedgerEntries());
		List<PurseAuthResponse> purseAuthResponses = getPurseAuthResponses(command.getDefaultPurseId(), purseId, accountPurseGroupUpdate,
				command.getPurseCurrency(), command.getPurseName(), command.getSkuCode());

		return getSuccessResponseView(command, authId, accountPurseGroupViews, accountPurseAggregate.getDefaultPurseAvailableBalance(),
				cardEntity.getCardStatus(), Optional.empty(), Optional.empty(), authorizedAmount, purseAuthResponses);
	}

	public AccountSummaryView processRedemptionWithLockReversal(AccountTransactionCommand command) {
		ValueDTO valueDto = command.getValueDTO();
		TransactionInfo transactionInfo = command.getTransactionInfo();
		String transactionDate = DateTimeUtil.convert(transactionInfo.getTransactionDateTime(), DateTimeFormatType.YYYYMMDD);
		Long purseId = Long.valueOf(valueDto.getValueObj()
			.get(ValueObjectKeys.PURSE_ID));

		CardEntity cardEntity = this.repository.loadCardEntity(valueDto.getValueObj());
		AccountPurseAggregateNew accountPurseAggregate = loadAccountPurseAggregate(cardEntity.getAccountId(), cardEntity.getProductId(),
				purseId, command.getPurseName(), AccountPurseStateType.ALL_UNEXPIRED);
		RedemptionLockAggregate redemptionLockAggregate = loadRedemptionLockAggregate(cardEntity, command.getTransactionInfo());

		accountPurseAggregate.updateLimitsAndFee(purseId, valueDto.getUsageFee(), valueDto.getUsageLimit());
		accountPurseAggregate.setMaxPurseBalance(purseId, getMaxPurseBalance(valueDto, purseId, command.getDefaultPurseId()));
		Map<LedgerEntryType, BigDecimal> transactionFees = new EnumMap<>(LedgerEntryType.class);

		List<StatementLog> listStatementLogs = this.repository.loadStatementLogs(transactionInfo.getDeliveryChannelType(),
				transactionInfo.getCorrelationId(), cardEntity.getCardNumberHash(), transactionDate, transactionInfo.getTransactionType()
					.getTransactionCode());
		List<LedgerEntry> ledgerEntries = createLedgerEntries(listStatementLogs, cardEntity.getAccountId());

		AccountPurseGroupUpdate accountPurseGroupUpdate = accountPurseAggregate.revertPurseBalance(purseId, ledgerEntries, transactionFees,
				PurseBalanceType.AVAILABLE_BALANCE);
		Optional<CardUpdate> cardUpdate = Optional.empty();
		Optional<AccountUpdate> accountUpdate = Optional.empty();

		List<RedemptionLockUpdate> redemptionLockUpdates = redemptionLockAggregate.revertRedemptionLockFlag(command.getTransactionInfo()
			.getCorrelationId(), purseId);
		Optional<TransactionLogUpdate> transactionLogUpdate = Optional.of(TransactionLogUpdate.builder()
			.cardNumberHash(cardEntity.getCardNumberHash())
			.correlationId(transactionInfo.getCorrelationId())
			.isReversedFlag(GeneralConstants.YES)
			.transactionType(transactionInfo.getTransactionType())
			.build());
		AccountAggregateUpdate accountAggregateUpdate = createAggregateUpdate(accountPurseGroupUpdate, cardUpdate, accountUpdate);
		long authId = saveTransaction(command, cardEntity, accountAggregateUpdate, redemptionLockUpdates, transactionLogUpdate);
		boolean includeFutureEffectivePurses = true;
		boolean includeZeroBalancePurses = true;
		List<AccountPurseGroupView> accountPurseGroupViews = accountPurseAggregate.getAccountPurseBalances(includeFutureEffectivePurses,
				includeZeroBalancePurses);

		BigDecimal authorizedAmount = isNull(accountPurseGroupUpdate) ? BigDecimal.ZERO
				: getAuthorizedAmount(accountPurseGroupUpdate.getLedgerEntries());
		List<PurseAuthResponse> purseAuthResponses = getPurseAuthResponses(command.getDefaultPurseId(), purseId, accountPurseGroupUpdate,
				command.getPurseCurrency(), command.getPurseName(), command.getSkuCode());

		return getSuccessResponseView(command, authId, accountPurseGroupViews, accountPurseAggregate.getDefaultPurseAvailableBalance(),
				cardEntity.getCardStatus(), Optional.empty(), Optional.empty(), authorizedAmount, purseAuthResponses);
	}

	public AccountSummaryView processRedemptionWithUnLock(AccountTransactionCommand command) {
		ValueDTO valueDto = command.getValueDTO();
		Optional<Long> accountPurseId = command.getAccountPurseId();
		Long purseId = Long.valueOf(valueDto.getValueObj()
			.get(ValueObjectKeys.PURSE_ID));
		boolean allowPartialAuth = isPartialAuthAllowed(Util.getProductPartialIndicator(command.getValueDTO()));

		// Load State
		CardEntity cardEntity = this.repository.loadCardEntity(command.getValueDTO()
			.getValueObj());

		AccountPurseAggregateNew accountPurseAggregate = loadAccountPurseAggregate(cardEntity.getAccountId(), cardEntity.getProductId(),
				purseId, command.getPurseName(), AccountPurseStateType.ALL_UNEXPIRED);

		RedemptionLockAggregate redemptionLockAggregate = loadRedemptionLockAggregate(cardEntity, command.getTransactionInfo());
		Map<LedgerEntryType, BigDecimal> transactionFees = getTransactionFees(command.getValueDTO()
			.getValueObj());

		// Perform the business operation
		AccountPurseGroupUpdate accountPurseGroupUpdate = null;
		if (accountPurseId.isPresent()) {
			log.info("Debit amount from single purse");
			accountPurseGroupUpdate = accountPurseAggregate.debitPurseBalance(purseId, accountPurseId.get(), command.getTransactionAmount(),
					transactionFees, PurseBalanceType.AVAILABLE_BALANCE, Boolean.TRUE);
		} else {
			log.info("Debit amount from default Purse/ multiple purses");
			accountPurseGroupUpdate = accountPurseAggregate.debitPurseBalance(purseId, command.getTransactionAmount(), transactionFees,
					PurseBalanceType.AVAILABLE_BALANCE, allowPartialAuth);
		}
		List<RedemptionLockUpdate> redemptionLockUpdates = getRedemptionLockUpdates(accountPurseGroupUpdate.getLedgerEntries(),
				redemptionLockAggregate, purseId);
		Optional<CardUpdate> cardUpdate = Optional.empty();
		Optional<AccountUpdate> accountUpdate = Optional.empty();

		// Create the update object and save it
		AccountAggregateUpdate accountAggregateUpdate = createAggregateUpdate(accountPurseGroupUpdate, cardUpdate, accountUpdate);
		long authId = saveTransaction(command, cardEntity, accountAggregateUpdate, redemptionLockUpdates, Optional.empty());

		BigDecimal authorizedAmount = isNull(accountPurseGroupUpdate) ? BigDecimal.ZERO
				: getAuthorizedAmount(accountPurseGroupUpdate.getLedgerEntries());
		List<PurseAuthResponse> purseAuthResponses = getPurseAuthResponses(command.getDefaultPurseId(), purseId, accountPurseGroupUpdate,
				command.getPurseCurrency(), command.getPurseName(), command.getSkuCode());
		return null;
	}

	public AccountSummaryView processRedemptionWithUnLockReversal(AccountTransactionCommand command) {
		return null;
	}

	public AccountSummaryView processCardSwap(AccountTransactionCommand command) {

		final ValueDTO valueDto = command.getValueDTO();
		final SpNumber targetSpNumber = getTargetSpNumber(command);
		final Long purseId = Long.valueOf(valueDto.getValueObj()
			.get(ValueObjectKeys.PURSE_ID));

		CardEntity sourceCardEntity = this.repository.loadCardEntity(valueDto.getValueObj());
		CardEntity targetCardEntity = this.repository.loadCardEntity(targetSpNumber.getSpNumberType(), targetSpNumber.getSpNumber());

		transactionValidator.validateForCardSwap(targetCardEntity.getCardStatus(), sourceCardEntity.getProductId(),
				targetCardEntity.getProductId());

		AccountEntity accountEntity = this.repository.loadAccountEntity(targetCardEntity.getAccountId());

		CardUpdate sourceCardUpdate = sourceCardEntity.updateCardStatus(CardStatusType.CLOSED);
		CardUpdate targetCardUpdate = targetCardEntity.updateCardStatusToActive(false);

		AccountPurseAggregateNew source = this.repository.loadAccountPurseAggregate(sourceCardEntity.getAccountId(),
				sourceCardEntity.getProductId(), command.getDefaultPurseId(),
				AccountPurseAggregateStateType.from(AccountPurseUsageStateType.ALL, AccountPurseStateType.ALL_UNEXPIRED));

		AccountPurseAggregateNew target = this.repository.loadAccountPurseAggregate(targetCardEntity.getAccountId(),
				targetCardEntity.getProductId(), command.getDefaultPurseId(),
				AccountPurseAggregateStateType.from(AccountPurseUsageStateType.ALL, AccountPurseStateType.ALL_UNEXPIRED));

		Map<LedgerEntryType, BigDecimal> transactionFees = getTransactionFees(valueDto.getValueObj());
		source.updateLimitsAndFee(purseId, valueDto.getUsageFee(), valueDto.getUsageLimit());
// rename getAccountPurseBalances
		List<AccountPurseGroupView> groupViews = source.getAccountPurseBalances(true, true);

		this.transactionValidator.validateForLockedBalance(groupViews);

		List<AccountPurseGroupUpdate> sourceGroupUpdates = new ArrayList<>();
		List<AccountPurseGroupUpdate> targetGroupUpdates = new ArrayList<>();

		groupViews.forEach(group -> {
			List<AccountPurseOperationPartial> partials = this.map(group.getAccountPurses(), LedgerEntryType.TRANSACTION_AMOUNT);

			List<AccountPurseOperationPartial> partialsWithBalance = partials.stream()
				.filter(partial -> isPositive(partial.getAmount()))
				.collect(Collectors.toList());

			source.updateAccountPurseGroup(group.getAccountPurseGroupKey()
				.getPurseId(), partialsWithBalance, transactionFees, PurseBalanceType.BOTH, OperationType.DEBIT, false, Optional.empty())
				.ifPresent(sourceGroupUpdates::add);

			target.updateAccountPurseGroup(group.getAccountPurseGroupKey()
				.getPurseId(), partials, Collections.emptyMap(), PurseBalanceType.BOTH, OperationType.CREDIT, false,
					Optional.of(group.getGroupStatus()))
				.ifPresent(targetGroupUpdates::add);

		});

		AccountUpdate accountUpdate = accountEntity.updateInitialLoadAmount(target.getDefaultPurseAvailableBalance());

		AccountAggregateUpdate sourceAggregateUpdate = createAggregateUpdate(sourceGroupUpdates, Optional.of(sourceCardUpdate),
				Optional.empty());
		AccountAggregateUpdate targetAggregateUpdate = createAggregateUpdate(targetGroupUpdates, Optional.of(targetCardUpdate),
				Optional.of(accountUpdate));

		long authId = saveTransaction(command, sourceCardEntity, Optional.of(targetCardEntity), sourceAggregateUpdate,
				Optional.of(targetAggregateUpdate), null, Optional.empty());

		// card table - check if we need to update first time topup.

		boolean includeFutureEffectivePurses = true;
		boolean includeZeroBalancePurses = true;
		List<AccountPurseGroupView> accountPurseGroupViews = target.getAccountPurseBalances(includeFutureEffectivePurses,
				includeZeroBalancePurses);

		BigDecimal authorizedAmount = BigDecimal.ZERO;

		List<PurseAuthResponse> purseAuthResponses = Collections.emptyList();
		return getSuccessResponseView(command, authId, accountPurseGroupViews, source.getDefaultPurseAvailableBalance(),
				sourceCardUpdate.getNewCardStatus(), Optional.of(targetCardUpdate.getNewCardStatus()),
				Optional.of(target.getDefaultPurseAvailableBalance()), authorizedAmount, purseAuthResponses);

	}

	private SpNumber getTargetSpNumber(AccountTransactionCommand command) {
		final Optional<SpNumber> targetSpNumber = command.getTargetSpNumber();

		return targetSpNumber.orElseThrow(() -> {
			log.warn("Unable to find the target sp number: {}", targetSpNumber);
			return new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
		});

	}

	public AccountSummaryView processCardSwapReversal(AccountTransactionCommand command) {
		return null;
	}

	// Only for Redeem Credit
	private AccountSummaryView processReversal(AccountTransactionCommand command, Optional<CardUpdate> cardUpdate,
			Optional<AccountUpdate> accountUpdate) {

		TransactionInfo transactionInfo = command.getTransactionInfo();
		ValueDTO valueDto = command.getValueDTO();
		String transactionDate = DateTimeUtil.convert(transactionInfo.getTransactionDateTime(), DateTimeFormatType.YYYYMMDD);
		Long purseId = Long.valueOf(valueDto.getValueObj()
			.get(ValueObjectKeys.PURSE_ID));

		CardEntity cardEntity = this.repository.loadCardEntity(valueDto.getValueObj());
		AccountPurseAggregateNew accountPurseAggregate = loadAccountPurseAggregate(cardEntity.getAccountId(), cardEntity.getProductId(),
				purseId, command.getPurseName(), AccountPurseStateType.ALL_UNEXPIRED);

		accountPurseAggregate.setMaxPurseBalance(purseId, getMaxPurseBalance(valueDto, purseId, command.getDefaultPurseId()));

		// reversal are system call, there will be no fee
		Map<LedgerEntryType, BigDecimal> transactionFees = new EnumMap<>(LedgerEntryType.class);

		// load ALL Statement Logs
		List<StatementLog> listStatementLogs = this.repository.loadStatementLogs(transactionInfo.getDeliveryChannelType(),
				transactionInfo.getCorrelationId(), cardEntity.getCardNumberHash(), transactionDate, transactionInfo.getTransactionType()
					.getTransactionCode());
		List<LedgerEntry> ledgerEntries = createLedgerEntries(listStatementLogs, cardEntity.getAccountId());

		List<RedemptionLockUpdate> redemptionLockUpdates = Collections.emptyList();
		PurseBalanceType purseBalanceType = PurseBalanceType.BOTH;

		// Perform Business operation
		AccountPurseGroupUpdate accountPurseGroupUpdate = accountPurseAggregate.revertPurseBalance(purseId, ledgerEntries, transactionFees,
				purseBalanceType);

		Optional<TransactionLogUpdate> transactionLogUpdate = Optional.of(TransactionLogUpdate.builder()
			.cardNumberHash(cardEntity.getCardNumberHash())
			.correlationId(transactionInfo.getCorrelationId())
			.isReversedFlag(GeneralConstants.YES)
			.transactionType(transactionInfo.getTransactionType())
			.build());

		AccountAggregateUpdate accountAggregateUpdate = createAggregateUpdate(accountPurseGroupUpdate, cardUpdate, accountUpdate);
		long authId = saveTransaction(command, cardEntity, accountAggregateUpdate, redemptionLockUpdates, transactionLogUpdate);
		boolean includeFutureEffectivePurses = true;
		boolean includeZeroBalancePurses = true;
		List<AccountPurseGroupView> accountPurseGroupViews = accountPurseAggregate.getAccountPurseBalances(includeFutureEffectivePurses,
				includeZeroBalancePurses);

		BigDecimal authorizedAmount = isNull(accountPurseGroupUpdate) ? BigDecimal.ZERO
				: getAuthorizedAmount(accountPurseGroupUpdate.getLedgerEntries());
		List<PurseAuthResponse> purseAuthResponses = getPurseAuthResponses(command.getDefaultPurseId(), purseId, accountPurseGroupUpdate,
				command.getPurseCurrency(), command.getPurseName(), command.getSkuCode());

		return getSuccessResponseView(command, authId, accountPurseGroupViews, accountPurseAggregate.getDefaultPurseAvailableBalance(),
				cardEntity.getCardStatus(), Optional.empty(), Optional.empty(), authorizedAmount, purseAuthResponses);
	}

	private boolean isPartialAuthAllowed(String productPartialIndicator) {
		return mapYNToBoolean(productPartialIndicator) || mapZeroOneToBoolean(productPartialIndicator);
	}

	private AccountAggregateUpdate createAggregateUpdate(AccountPurseGroupUpdate accountPurseGroupUpdate, Optional<CardUpdate> cardUpdate,
			Optional<AccountUpdate> accountUpdate) {
		return createAggregateUpdate(Collections.singletonList(accountPurseGroupUpdate), cardUpdate, accountUpdate);
	}

	private AccountAggregateUpdate createAggregateUpdate(List<AccountPurseGroupUpdate> accountPurseGroupUpdates,
			Optional<CardUpdate> cardUpdate, Optional<AccountUpdate> accountUpdate) {
		return AccountAggregateUpdate.builder()
			.accountPurseGroupUpdate(accountPurseGroupUpdates)
			.cardUpdate(cardUpdate)
			.accountUpdate(accountUpdate)
			.build();
	}

	private AccountSummaryView getSuccessResponseView(AccountTransactionCommand command, long authId,
			List<AccountPurseGroupView> accountPurseGroupViews, BigDecimal sourceCardBalance, CardStatusType sourceCardStatus,
			Optional<CardStatusType> targetCardStatus, Optional<BigDecimal> targetCardBalance, BigDecimal authorizedAmount,
			List<PurseAuthResponse> purseAuthResponses) {

		return AccountSummaryView.builder()
			.responseCode(ResponseCodes.SUCCESS)
			.responseMessage(GeneralConstants.OK)
			.authId(authId)
			.authorizedAmount(authorizedAmount)
			.currencyCode(command.getPurseCurrency()
				.orElse(""))
			.sourceCardBalance(sourceCardBalance)
			.sourceCardStatus(sourceCardStatus)
			.targetCardBalance(targetCardBalance)
			.targetCardStatus(targetCardStatus)
			.accountPurseGroupViews(accountPurseGroupViews)
			.purseAuthResponses(purseAuthResponses)
			.command(command)
			.build();
	}

	private Map<LedgerEntryType, BigDecimal> getTransactionFees(Map<String, String> valueObject) {

		Map<LedgerEntryType, BigDecimal> transactionFees = new EnumMap<>(LedgerEntryType.class);
		String feeCondition = valueObject.get(ValueObjectKeys.PROD_FEE_CONDITION);
		String flatFee = valueObject.get(ValueObjectKeys.PROD_FLAT_FEE_AMT);
		String percentFee = valueObject.get(ValueObjectKeys.PROD_PERCENT_FEE_AMT);
		String transactionFee = valueObject.get(ValueObjectKeys.PROD_TXN_FEE_AMT);

		log.info("Fee Condition: {}", feeCondition);
		if (GeneralConstants.BOTH_FEE.equals(feeCondition)) {
			log.info("Flat Fee: {}, Percent Fee: {}", flatFee, percentFee);
			transactionFees.put(LedgerEntryType.FLAT_FEE, new BigDecimal(flatFee));
			transactionFees.put(LedgerEntryType.PERCENT_FEE, new BigDecimal(percentFee));
		} else if (transactionFee != null) {
			log.info("Flat Fee: {}", transactionFee);
			transactionFees.put(LedgerEntryType.FLAT_FEE, new BigDecimal(transactionFee));
		}

		return transactionFees;
	}

	private long saveTransaction( //
			AccountTransactionCommand command, //
			CardEntity cardEntity, //
			AccountAggregateUpdate accountAggregateUpdate, //
			List<RedemptionLockUpdate> redemptionLockUpdates, //
			Optional<TransactionLogUpdate> transactionLogUpdate) {
		return this.saveTransaction(command, cardEntity, Optional.empty(), accountAggregateUpdate, Optional.empty(), redemptionLockUpdates,
				transactionLogUpdate);
	}

	private long saveTransaction(AccountTransactionCommand command, //
			CardEntity cardEntity, //
			Optional<CardEntity> targetCardEntity, //
			AccountAggregateUpdate accountAggregateUpdate, //
			Optional<AccountAggregateUpdate> targetAccountAggregateUpdate, //
			List<RedemptionLockUpdate> redemptionLockUpdates, //
			Optional<TransactionLogUpdate> transactionLogUpdate) {

		TransactionLog transactionLog = logService.getTransactionLog(command, cardEntity, accountAggregateUpdate, null);

		List<StatementsLog> statementLogs = logService.getStatementLog(command, cardEntity, accountAggregateUpdate,
				transactionLog.getTransactionSqid(), transactionLog.getAuthId());

		if (targetCardEntity.isPresent() && targetAccountAggregateUpdate.isPresent()) {

			transactionLog.setTopupCardNbrEncr(targetCardEntity.get()
				.getCardNumberEncrypted());

			statementLogs.addAll(logService.getStatementLog(command, targetCardEntity.get(), targetAccountAggregateUpdate.get(),
					transactionLog.getTransactionSqid(), transactionLog.getAuthId()));
		}

		List<AccountAggregateUpdate> accountAggregateUpdates = targetAccountAggregateUpdate.isPresent()
				? Arrays.asList(accountAggregateUpdate, targetAccountAggregateUpdate.get())
				: Collections.singletonList(accountAggregateUpdate);

		AccountTransactionUpdate accountTransactionUpdate = AccountTransactionUpdate.builder()
			.accountAggregateUpdates(accountAggregateUpdates)
			.transactionLog(transactionLog)
			.statementLogs(statementLogs)
			.transactionLogUpdate(transactionLogUpdate)
			.redemptionLocks(redemptionLockUpdates)
			.transactionDate(command.getTransactionInfo()
				.getTransactionDateTime())
			.build();

		long authId = Long.parseLong(transactionLog.getAuthId());
		this.persistenceHandler.persist(accountTransactionUpdate);
		return authId;
	}

	private List<PurseAuthResponse> getPurseAuthResponses(long defaultPurseId, long purseId,
			AccountPurseGroupUpdate accountPurseGroupUpdate, Optional<String> currencyCode, Optional<String> purseName,
			Optional<String> skuCode) {
		List<PurseAuthResponse> purseAuthResponses = new ArrayList<>();

		if (defaultPurseId == purseId) {
			return purseAuthResponses;
		}

		String sku = skuCode.isPresent() ? skuCode.get() : "";
		String currency = currencyCode.isPresent() ? currencyCode.get() : "";

		if (accountPurseGroupUpdate != null && not(CollectionUtils.isEmpty(accountPurseGroupUpdate.getLedgerEntries()))) {
			accountPurseGroupUpdate.getLedgerEntries()
				.forEach(ledgerEntry -> {
					PurseAuthResponse purseAuthResponse = PurseAuthResponse.builder()
						.accountPurseId(ledgerEntry.getAccountPurseKey()
							.getAccountPurseId())
						.purseName(purseName.get())
						.availableBalance(ledgerEntry.getNewAvailableBalance())
						.transactionAmount(ledgerEntry.getTransactionAmount())
						.skuCode(sku)
						.currencyCode(currency)
						.build();
					purseAuthResponses.add(purseAuthResponse);
				});

		}

		return purseAuthResponses;
	}

	private BigDecimal getAuthorizedAmount(List<LedgerEntry> ledgerEntries) {
		return ledgerEntries.stream()
			.filter(p -> p.getLedgerEntryType() == LedgerEntryType.TRANSACTION_AMOUNT)
			.map(LedgerEntry::getAuthorizedAmount)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private BigDecimal getMaxPurseBalance(ValueDTO valueDto, long purseId, long defaultPurseId) {
		if (defaultPurseId == purseId) {
			return new BigDecimal(valueDto.getProductAttributes()
				.get(ValueObjectKeys.GENERAL)
				.get(ValueObjectKeys.MAXCARDBAL)
				.toString());
		} else {
			return valueDto.getProductAttributes()
				.get(ValueObjectKeys.PURSE)
				.get(ValueObjectKeys.MAX_PURSE_BALANCE) != null ? new BigDecimal(
						valueDto.getProductAttributes()
							.get(ValueObjectKeys.PURSE)
							.get(ValueObjectKeys.MAX_PURSE_BALANCE)
							.toString())
						: BigDecimal.ZERO;
		}
	}

	private RedemptionLockAggregate loadRedemptionLockAggregate(CardEntity cardEntity, TransactionInfo transactionInfo) {
		List<RedemptionLockEntity> redemptionLocks = this.repository.loadRedemptionLocks(cardEntity.getCardNumberHash(), "Y");
		if (redemptionLocks == null) {
			redemptionLocks = Collections.emptyList();
		}
		return RedemptionLockAggregate.builder()
			.cardEntity(cardEntity)
			.transactionInfo(transactionInfo)
			.redemptionLocks(redemptionLocks)
			.build();
	}

	private List<LedgerEntry> createLedgerEntries(List<StatementLog> statementLogInfo, long accountId) {
		List<LedgerEntry> ledgerEntries = new ArrayList<>();

		statementLogInfo.forEach(statementLog -> {
			LedgerEntry ledgerEntry = LedgerEntry.builder()
				.accountPurseKey(AccountPurseKey.from(accountId, statementLog.getPurseId(), statementLog.getAccountPurseId()))
				.ledgerEntryType(LedgerEntryType.byLogDescripton(statementLog.getTransactionDescription()))
				.operationType(OperationType.byFlag(statementLog.getOperationType()))
				.transactionAmount(statementLog.getTransactionAmount())
				.build();
			ledgerEntries.add(ledgerEntry);
		});
		return ledgerEntries;
	}

	private List<RedemptionLockUpdate> getRedemptionLockUpdates(List<LedgerEntry> ledgerEntries,
			RedemptionLockAggregate redemptionLockAggregate, long purseId) {
		List<RedemptionLockUpdate> redemptionLockUpdates = new ArrayList<>();

		Map<Long, List<LedgerEntry>> result = ledgerEntries.stream()
			.collect(Collectors.groupingBy(ledgerEntry -> ledgerEntry.getAccountPurseKey()
				.getAccountPurseId()));

		result.forEach((accountPurseId, ledgerEntryList) -> {
			long sequenceId = sequenceService.getNextTxnSeqId()
				.longValue();
			RedemptionLockUpdate redemptionLockUpdate = redemptionLockAggregate.getRedemptionLockUpdate(ledgerEntryList, accountPurseId,
					sequenceId, purseId);
			redemptionLockUpdates.add(redemptionLockUpdate);
		});
		return redemptionLockUpdates;
	}

	/*
	 * private Optional<AccountUpdate> loadAccountUpdate(CardEntity cardEntity, AccountEntity accountEntity, BigDecimal
	 * initialLoadAmount, Boolean transactionLogFirstTimeTopUpFlag) {
	 * 
	 * Optional<AccountUpdate> accountUpdate = Optional.empty();
	 * 
	 * if (cardEntity.getFirstTimeTopUp() && transactionLogFirstTimeTopUpFlag) { accountUpdate =
	 * Optional.ofNullable(accountEntity.updateInitialLoadAmount(null)); } else if (!cardEntity.getFirstTimeTopUp()) {
	 * log.debug("First time top up reversal"); accountUpdate =
	 * Optional.ofNullable(accountEntity.updateInitialLoadAmount(initialLoadAmount)); } return accountUpdate; }
	 * 
	 * private Optional<CardUpdate> loadCardUpdate(CardEntity cardEntity, Boolean transactionLogFirstTimeTopUpFlag) {
	 * 
	 * Optional<CardUpdate> cardUpdate = Optional.empty(); if (cardEntity.getFirstTimeTopUp() &&
	 * transactionLogFirstTimeTopUpFlag) { cardUpdate =
	 * Optional.ofNullable(cardEntity.updateFirstTimeTopUp(Boolean.FALSE)); } else if (!cardEntity.getFirstTimeTopUp())
	 * { log.debug("First time top up"); cardUpdate =
	 * Optional.ofNullable(cardEntity.updateFirstTimeTopUp(Boolean.TRUE)); } return cardUpdate; }
	 */
	private AccountPurseAggregateNew loadAccountPurseAggregate(long accountId, long productId, long purseId, Optional<String> purseName,
			AccountPurseStateType stateType) {

		if (purseName.isPresent()) {
			return this.repository.loadAccountPurseAggregate(accountId, productId, purseName.get(),
					AccountPurseAggregateStateType.from(stateType));
		} else {
			return this.repository.loadAccountPurseAggregate(accountId, productId, purseId, AccountPurseAggregateStateType.from(stateType));
		}
	}

	private long getPurseId(String purseId) {
		log.debug("mapping purseId: {}", purseId);
		return mapToOptionalLong(purseId).orElseThrow(() -> {
			log.error("Purse Id not present: {}", purseId);
			return new ServiceException(SpilExceptionMessages.INVALID_PURSE, ResponseCodes.INVALID_PURSE);
		});
	}

	private List<AccountPurseOperationPartial> map(List<AccountPurseViewNew> accountPurseViews, LedgerEntryType ledgerEntryType) {
		return accountPurseViews.stream()
			.map(view -> this.map(view, ledgerEntryType))
			.collect(Collectors.toList());
	}

	private AccountPurseOperationPartial map(AccountPurseViewNew accountPurseView, LedgerEntryType ledgerEntryType) {
		return AccountPurseOperationPartial.from(accountPurseView.getAccountPurseKey()
			.getAccountPurseId(), accountPurseView.getAccountPurseAltKeyAttributes(), accountPurseView.getAvailableBalance(),
				ledgerEntryType);
	}

	private AccountPurseOperationPartial map(LedgerEntry ledgerEntry, LedgerEntryType ledgerEntryType) {
		return AccountPurseOperationPartial.from(ledgerEntry.getAccountPurseKey()
			.getAccountPurseId(), null, ledgerEntry.getTransactionAmount(), ledgerEntryType);
	}
}
