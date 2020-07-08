package com.incomm.cclp.account.domain.factory;

import static com.incomm.cclp.account.util.CodeUtil.mapYNToBoolean;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.incomm.ccl.common.util.DateTimeUtil;
import com.incomm.cclp.account.application.command.TransactionInfo;
import com.incomm.cclp.account.domain.exception.DomainExceptionFactory;
import com.incomm.cclp.account.domain.exception.DomainExceptionType;
import com.incomm.cclp.account.domain.model.AccountAggregate;
import com.incomm.cclp.account.domain.model.AccountEntity;
import com.incomm.cclp.account.domain.model.AccountPurseAggregate;
import com.incomm.cclp.account.domain.model.AccountPurseAggregateNew;
import com.incomm.cclp.account.domain.model.AccountPurseGroup;
import com.incomm.cclp.account.domain.model.AccountPurseGroupKey;
import com.incomm.cclp.account.domain.model.AccountPurseGroupStatus;
import com.incomm.cclp.account.domain.model.CardEntity;
import com.incomm.cclp.account.domain.model.CardStatusType;
import com.incomm.cclp.account.domain.model.DeliveryChannelType;
import com.incomm.cclp.account.domain.model.MessageType;
import com.incomm.cclp.account.domain.model.OperationType;
import com.incomm.cclp.account.domain.model.ProductPurse;
import com.incomm.cclp.account.domain.model.PurseStatusType;
import com.incomm.cclp.account.domain.model.RedemptionLockEntity;
import com.incomm.cclp.account.domain.model.SpNumberType;
import com.incomm.cclp.account.util.CodeUtil;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.ProductDAO;
import com.incomm.cclp.dao.service.SequenceService;
import com.incomm.cclp.domain.Account;
import com.incomm.cclp.domain.AccountPurseBalance;
import com.incomm.cclp.domain.CardDetail;
import com.incomm.cclp.domain.StatementsLogInfo;
import com.incomm.cclp.domain.TransactionLogInfo;
import com.incomm.cclp.dto.AccountBalance;
import com.incomm.cclp.dto.AccountPurseUsageDto;
import com.incomm.cclp.dto.StatementLog;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.repos.GenericRepo;
import com.incomm.cclp.service.AccountPurseService;
import com.incomm.cclp.service.CardDetailsService;
import com.incomm.cclp.service.RedemptionLockService;
import com.incomm.cclp.transaction.bean.SpilStartupMsgTypeBean;
import com.incomm.cclp.transaction.service.TransactionService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class AccountAggregateRepository implements AccountAggregateRepositoryInterface {

	// @formatter:off
	@Autowired private CardDetailsService cardDetailsService;
	@Autowired private AccountPurseService accountPurseService;
	@Autowired private TransactionService transactionService;
	@Autowired private RedemptionLockService redemptionLockService;
	@Autowired private ProductDAO productDao;
	@Autowired private GenericRepo genericRepository;
	@Autowired private SequenceService sequenceService;
	// @formatter:on

	public AccountAggregate load(AccountAggregateParams params) {

		SpilStartupMsgTypeBean transactionConfiguration = this.getTransactionConfiguration(params.getTransactionShortCode(),
				params.getDeliveryChannelType());

		CardDetail cardDetail = getCardDetails(params.getSpNumberType(), params.getSpNumber());
		CardEntity cardEntity = AggregateMapper.map(cardDetail);

		AccountPurseAggregate accountPurseAggregate = this.getAccountPurseAggregate(cardDetail.getAccountId(), cardDetail.getProductId(),
				params);

		return AccountAggregate.builder()
			.productId(cardDetail.getProductId())
			.accountId(cardDetail.getAccountId())
			.accountNumber(cardDetail.getAccountNumber())
			.cardEntity(cardEntity)
			.accountPurseAggregate(accountPurseAggregate)
			.transactionConfiguration(transactionConfiguration)
			.build();

	}

	public AccountAggregate loadAccountAggregate(TransactionInfo transactionInfo, String purseName, AccountPurseStateType stateType) {

		CardEntity cardEntity = loadCardEntity(transactionInfo.getSpNumberType(), transactionInfo.getSpNumber());

		AccountPurseAggregateNew accountPurseAggregate = loadAccountPurseAggregate(cardEntity.getAccountId(), cardEntity.getProductId(),
				purseName, AccountPurseAggregateStateType.from(stateType));

		return AccountAggregate.builder()
			.accountId(cardEntity.getAccountId())
			.accountNumber(cardEntity.getAccountNumber())
			.productId(cardEntity.getProductId())
			.cardEntity(cardEntity)
			.accountPurseAggregate1(accountPurseAggregate)
			.build();
	}

	private AccountPurseAggregate getAccountPurseAggregate(long accountId, long productId, AccountAggregateParams params) {

		Map<Long, ProductPurse> productPurseMap = this.getProductPurses(productId);

		ProductPurse selectedProductPurse = getProductPurseByPurseName(productPurseMap.values(), params.getPurseName());

		List<AccountPurseUsageDto> purseUsageList = Collections.emptyList();
		List<AccountPurseBalance> accountPurseBalances = Collections.emptyList();
		List<AccountPurseBalance> expiredAccountPurseBalances = Collections.emptyList();

		if (params.getStates()
			.contains(AccountStateType.ACCOUNT_PURSE_SELECTED)) {
			accountPurseBalances = this.accountPurseService.getAccountPurseBalances(accountId, productId,
					selectedProductPurse.getPurseId());
			expiredAccountPurseBalances = this.accountPurseService.getExpiredAccountPurseBalances(accountId, productId,
					selectedProductPurse.getPurseId());
		}

		if (params.getStates()
			.contains(AccountStateType.ACCOUNT_PURSE_USAGE_SELECTED)) {
			AccountPurseUsageDto accountPurseUsageDto = this.accountPurseService.getAccountPurseUsage(accountId,
					selectedProductPurse.getPurseId());
			if (accountPurseUsageDto != null) {
				purseUsageList = Collections.singletonList(accountPurseUsageDto);
			}
		}

		return AccountPurseAggregate.from(accountId, new ArrayList<ProductPurse>(productPurseMap.values()), purseUsageList,
				AggregateMapper.map(accountPurseBalances, productPurseMap),
				AggregateMapper.map(expiredAccountPurseBalances, productPurseMap));
	}

	@Override
	public AccountPurseAggregateNew loadAccountPurseAggregate(long accountId, long productId, String purseName,
			AccountPurseAggregateStateType stateType) {
		log.debug("accountId: {}, productId: {}, purseName: {}", accountId, productId, purseName);
		Map<Long, ProductPurse> productPurseMap = this.getProductPurses(productId);
		ProductPurse selectedProductPurse = getProductPurseByPurseName(productPurseMap.values(), purseName);

		return this.loadAccountPurseAggregate(accountId, productId, selectedProductPurse.getPurseId(), productPurseMap, stateType);
	}

	@Override
	public AccountPurseAggregateNew loadAccountPurseAggregate(long accountId, long productId, long purseId,
			AccountPurseAggregateStateType stateType) {
		log.debug("accountId: {}, productId: {}, purseId: {}", accountId, productId, purseId);
		Map<Long, ProductPurse> productPurseMap = this.getProductPurses(productId);
		ProductPurse selectedProductPurse = getProductPurseByPurseId(productPurseMap.values(), purseId);

		return this.loadAccountPurseAggregate(accountId, productId, selectedProductPurse.getPurseId(), productPurseMap, stateType);
	}

	private AccountPurseAggregateNew loadAccountPurseAggregate(long accountId, long productId, long selectedPurseId,
			Map<Long, ProductPurse> productPurseMap, AccountPurseAggregateStateType stateType) {

		ProductPurse selectedProductPurse = getProductPurseByPurseId(productPurseMap.values(), selectedPurseId);

		Map<Long, AccountPurseUsageDto> usageMap = loadAccountPurseUsage(accountId, selectedProductPurse.getPurseId(),
				stateType.getPurseUsageStateType());

		Map<Long, List<AccountPurseBalance>> accountPurseMap = loadAccountPurse(accountId, selectedProductPurse.getPurseId(),
				stateType.getPurseStateType());

		return createAggregate(accountId, productId, selectedProductPurse.getPurseId(), productPurseMap, usageMap, accountPurseMap,
				stateType.getPurseUsageStateType());

	}

	private ProductPurse getProductPurseByPurseName(Collection<ProductPurse> productPurses, String purseName) {
		Optional<ProductPurse> productPurseOpt = productPurses.stream()
			.filter(productPurse -> productPurse.getPurseName()
				.equalsIgnoreCase(purseName))
			.findFirst();

		return productPurseOpt.orElseThrow(
				() -> DomainExceptionFactory.from(DomainExceptionType.DOMAIN_VALIDATION_FAILED, SpilExceptionMessages.INVALID_PURSE));
	}

	private ProductPurse getProductPurseByPurseId(Collection<ProductPurse> productPurses, long purseId) {
		Optional<ProductPurse> productPurseOpt = productPurses.stream()
			.filter(productPurse -> productPurse.getPurseId() == purseId)
			.findFirst();

		return productPurseOpt.orElseThrow(
				() -> DomainExceptionFactory.from(DomainExceptionType.DOMAIN_VALIDATION_FAILED, SpilExceptionMessages.INVALID_PURSE));
	}

	private AccountPurseAggregateNew createAggregate(long accountId, long productId, long selectedPurseId,
			Map<Long, ProductPurse> productPurseMap, Map<Long, AccountPurseUsageDto> usageMap,
			Map<Long, List<AccountPurseBalance>> accountPurseMap, AccountPurseUsageStateType purseUsageStateType) {
		Set<Long> purseIds = new HashSet<>();
		purseIds.addAll(productPurseMap.keySet());

		Map<Long, AccountPurseGroup> accountPurseGroupMap = purseIds.stream()
			.map(nextPurseId -> this.createAccountPurseGroup(accountId, nextPurseId, selectedPurseId, productPurseMap,
					Optional.ofNullable(usageMap.get(nextPurseId)), accountPurseMap.get(nextPurseId), purseUsageStateType))
			.collect(Collectors.toMap(AccountPurseGroup::getPurseId, Function.identity()));

		return AccountPurseAggregateNew.builder()
			.accountId(accountId)
			.productId(productId)
			.accountPurseGroupByPurseId(accountPurseGroupMap)
			.purseIds(purseIds)
			.build();
	}

	private AccountPurseGroup createAccountPurseGroup(long accountId, long purseId, long selectedPurseId,
			Map<Long, ProductPurse> productPurseMap, Optional<AccountPurseUsageDto> accountPurseUsageDto,
			List<AccountPurseBalance> accountPurseBalances, AccountPurseUsageStateType purseUsageStateType) {

		AccountPurseGroupStatus groupStatus = this.mapAccountPurseGroupStatus(accountPurseUsageDto);

		boolean isNewAccountPurseUsage = false;

		if (CodeUtil.not(accountPurseUsageDto.isPresent())) {
			isNewAccountPurseUsage = purseUsageStateType == AccountPurseUsageStateType.ALL || purseId == selectedPurseId;
		}

		ProductPurse productPurse = productPurseMap.get(purseId);

		return AccountPurseGroup.builder()
			.accountPurseGroupKey(AccountPurseGroupKey.from(accountId, purseId))
			.productPurse(productPurse)
			.usageFees(accountPurseUsageDto.isPresent() ? accountPurseUsageDto.get()
				.getUsageFees() : Collections.emptyMap())
			.usageLimits(accountPurseUsageDto.isPresent() ? accountPurseUsageDto.get()
				.getUsageLimits() : Collections.emptyMap())
			.lastTransactionDate(accountPurseUsageDto.isPresent() ? accountPurseUsageDto.get()
				.getLastTransactionDate() : null)
			.groupStatus(groupStatus)
			.accountPurses(AggregateMapper.map(accountPurseBalances, productPurseMap))
			.isNew(isNewAccountPurseUsage)
			.build();
	}

	private AccountPurseGroupStatus mapAccountPurseGroupStatus(Optional<AccountPurseUsageDto> accountPurseUsageDto) {
		if (accountPurseUsageDto.isPresent()) {

			AccountPurseUsageDto dto = accountPurseUsageDto.get();

			Optional<PurseStatusType> status = accountPurseUsageDto.isPresent() ? PurseStatusType.byStatusCode(dto.getPurseStatus())
					: Optional.empty();

			PurseStatusType purseStatus = status.orElse(PurseStatusType.ACTIVE);

			return AccountPurseGroupStatus.from(purseStatus, dto.getStartDate(), dto.getEndDate());
		}

		return AccountPurseGroupStatus.from(PurseStatusType.ACTIVE);
	}

	private Map<Long, ProductPurse> getProductPurses(long productId) {
		List<ProductPurse> productPurses = this.productDao.getPursesByProductId(productId);

		return CollectionUtils.isEmpty(productPurses) ? Collections.emptyMap()
				: productPurses.stream()
					.collect(Collectors.toMap(ProductPurse::getPurseId, Function.identity()));
	}

	private Map<Long, List<AccountPurseBalance>> loadAccountPurse(long accountId, long purseId, AccountPurseStateType purseStateType) {

		if (purseStateType.isSkipAccountPurseLoad()) {
			log.info(" 0 account Purse with purseId {} and status: {} loaded for accountId: {}", purseId, purseStateType.name(), accountId);
			return Collections.emptyMap();
		}

		List<AccountPurseBalance> accountPurseBalances = this.accountPurseService.getAccountPurseBalances(accountId, Optional.of(purseId),
				purseStateType);

		log.info("{} account Purse with purseId {} and status: {} loaded for accountId: {}", accountPurseBalances.size(), purseId,
				purseStateType.name(), accountId);

		return accountPurseBalances.stream()
			.collect(Collectors.groupingBy(AccountPurseBalance::getPurseId));

	}

	private Map<Long, AccountPurseUsageDto> loadAccountPurseUsage(long accountId, long purseId, AccountPurseUsageStateType usageStateType) {

		if (usageStateType == AccountPurseUsageStateType.ALL) {
			return this.accountPurseService.getAccountPurseUsage(accountId)
				.stream()
				.collect(Collectors.toMap(AccountPurseUsageDto::getPurseId, Function.identity()));
		} else if (usageStateType == AccountPurseUsageStateType.SELECTED) {
			AccountPurseUsageDto usageDto = this.accountPurseService.getAccountPurseUsage(accountId, purseId);
			return Collections.singletonMap(purseId, usageDto);
		}

		return Collections.emptyMap();
	}

	private CardDetail getCardDetails(Optional<SpNumberType> spNumberType, String spNumber) {
		CardDetail cardDetail = cardDetailsService.getCardDetails(spNumberType.isPresent() ? spNumberType.get() : null, spNumber);
		if (cardDetail == null) {
			log.error("Card Number {} is not found as PAN, proxy_number and serial number", spNumber);
			throw new ServiceException(SpilExceptionMessages.INVALID_CARD, ResponseCodes.INVALID_CARD);
		}
		return cardDetail;
	}

	private SpilStartupMsgTypeBean getTransactionConfiguration(String transactionShortName, DeliveryChannelType deliveryChannelType) {

		SpilStartupMsgTypeBean transactionConfiguration = transactionService
			.getSpilMessageTypeBean(transactionShortName + deliveryChannelType.getChannelCode());

		if (transactionConfiguration == null) {

			log.error("Unable to find SpilStartupMsgTypeBean for transactionCode:{}, deliveryChannel:{}", transactionShortName,
					deliveryChannelType);
			throw DomainExceptionFactory.from(DomainExceptionType.INPUT_VALIDATION_FAILED, SpilExceptionMessages.SPIL_INVALID_REQUEST,
					"Unable to find SpilStartupMsgTypeBean for transactionCode:" + transactionShortName + " , deliveryChannel:"
							+ deliveryChannelType);
		}
		return transactionConfiguration;
	}

	@Override
	public CardEntity loadCardEntity(Map<String, String> valueObject) {
		Optional<CardStatusType> status = CardStatusType.byStatusCode(valueObject.get(ValueObjectKeys.CARD_CARDSTAT));
		CardStatusType cardStatus = status.isPresent() ? status.get() : null;

		return CardEntity.builder()
			.accountId(Long.parseLong(valueObject.get(ValueObjectKeys.CARD_ACCOUNT_ID)))
			.productId(Long.parseLong(valueObject.get(ValueObjectKeys.PRODUCT_ID)))
			.partnerId(Long.parseLong(valueObject.get(ValueObjectKeys.PARTNER_ID)))
			.customerCode(Long.parseLong(valueObject.get(ValueObjectKeys.CUSTOMER_CODE)))
			.issuerId(Long.parseLong(valueObject.get(ValueObjectKeys.ISSUER_ID)))
			.cardNumberEncrypted(valueObject.get(ValueObjectKeys.CARD_NUM_ENCR))
			.cardNumberHash(valueObject.get(ValueObjectKeys.CARD_NUM_HASH))
			.cardNumber(valueObject.get(ValueObjectKeys.CARD_NUMBER))
			.serialNumber(valueObject.get(ValueObjectKeys.CARD_SERIAL_NUMBER))
			.proxyNumber(valueObject.get(ValueObjectKeys.PROXY_NUMBER))
			.lastFourDigit(valueObject.get(ValueObjectKeys.LAST_4DIGIT))
			// TODO source of this field?
			// .dateOfActication(valueObject.get(ValueObjectKeys.CARD_ACTIVATION_DATE))
			.expiryDate(valueObject.get(ValueObjectKeys.EXPIRY_DATE))
			.oldCardStatus(CardStatusType.byStatusCode(valueObject.get(ValueObjectKeys.OLD_CARD_STATUS)))
			.cardStatus(cardStatus)
			.isRedeemed(valueObject.get(ValueObjectKeys.IS_REDEEMED))
			.digitalPin(valueObject.get(ValueObjectKeys.DIGITAL_PIN))
			// .lastTransactionDate(valueObject.get(ValueObjectKeys.LAST_TXN_DATE))
			.firstTimeTopUp(mapYNToBoolean((valueObject.get(ValueObjectKeys.FIRST_TIMETOPUP_FLAG))))
			// .starterCardFlag(valueObject.get(ValueObjectKeys.LAST_4DIGIT))
			// .profileCode(valueObject.get(ValueObjectKeys.profile))
			.replFlag(Integer.parseInt(valueObject.get(ValueObjectKeys.REPL_FLAG)))
			.build();
	}

	@Override
	public AccountEntity loadAccountEntity(long accountId) {
		Optional<Account> accountOptional = genericRepository.getAccountByAccountId(BigDecimal.valueOf(accountId));
		Account account = accountOptional.orElseThrow(() -> {
			log.error("Unable to find account with accountId {}", accountId);
			return new ServiceException(SpilExceptionMessages.ACCOUNTNUMNOTFOUND, ResponseCodes.SYSTEM_ERROR);
		});

		return AccountEntity.builder()
			.accountId(account.getAccountId()
				.longValue())
			.accountNumber(account.getAccountNumber())
			.accountStatus(account.getAccountStatus())
			.productId(account.getProductId())
			.initialLoadAmount(account.getInitialloadAmt())
			.newInitialLoadAmount(account.getNewInitialloadAmt())
			.lastUpdateDate(DateTimeUtil.map((Timestamp) account.getLastUpdDate()))
			.typeCode(account.getTypeCode())
			.statCode(account.getStatCode())
			.activeFlag(account.getActiveFlag())
			.redemptionDelayFlag(account.getRedemptionDelayFlag())
			.build();

	}

	@Override
	public List<TransactionLogInfo> loadTransactionInfo(DeliveryChannelType channelType, MessageType messageType, String correlationId,
			String cardNumbeHash, LocalDateTime transactionDate, String responseCode) {

		return genericRepository.getLastSuccessfulTransactions(channelType.getChannelCode(), cardNumbeHash,
				messageType.getMessageTypeCode(), responseCode, messageType.getMessageTypeCode(), correlationId,
				transactionDate.toString());

	}

	@Override
	public TransactionLogInfo loadTransactionInfo(DeliveryChannelType channelType, MessageType messageType, String correlationId,
			String cardNumbeHash, long purseId, LocalDate transactionDate, String responseCode) {

		return genericRepository.getLastSuccessPurseTxn(channelType.getChannelCode(), cardNumbeHash, messageType.getMessageTypeCode(),
				responseCode, messageType.getMessageTypeCode(), correlationId, transactionDate.toString(), purseId);
	}

	@Override
	public List<StatementsLogInfo> loadFeeStatementLogs(DeliveryChannelType channelType, String correlationId, OperationType operationType,
			String cardNumberHash, String transactionDate, String transactionCode) {

		return genericRepository.getLastFeeTransactions(correlationId, operationType.getFlag(), transactionDate, transactionCode);
	}

	@Override
	public List<StatementLog> loadStatementLogs(DeliveryChannelType channelType, String correlationId, String cardNumberHash,
			String transactionDate, String transactionCode) {

		return genericRepository.getAllStatementLogs(correlationId, transactionDate, transactionCode);
	}

	@Override
	public List<RedemptionLockEntity> loadRedemptionLocks(String cardNumberHash, String lockFlag) {

		return redemptionLockService.getRedemptionLocks(cardNumberHash, lockFlag);
	}

	@Override
	public CardEntity loadCardEntity(Optional<SpNumberType> spNumberType, String spNumber) {
		CardDetail cardDetail = getCardDetails(spNumberType, spNumber);
		return AggregateMapper.map(cardDetail);
	}

	@Override
	public List<AccountBalance> loadAllActiveAccountPurses(long accountId) {
		return accountPurseService.getActiveAccountPurses(accountId);
	}

	@Override
	public long getNextAuthId() {
		return Long.valueOf(sequenceService.getNextAuthSeqId());
	}

	@Override
	public long getNextAccountPurseId() {
		return Long.valueOf(sequenceService.getNextAccountPurseId());
	}

	@Override
	public long getNextTransactionId() {
		return this.sequenceService.getNextTxnSeqId()
			.longValue();
	}

}
