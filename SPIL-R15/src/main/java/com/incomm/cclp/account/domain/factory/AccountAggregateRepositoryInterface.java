package com.incomm.cclp.account.domain.factory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.incomm.cclp.account.domain.model.AccountEntity;
import com.incomm.cclp.account.domain.model.AccountPurseAggregateNew;
import com.incomm.cclp.account.domain.model.CardEntity;
import com.incomm.cclp.account.domain.model.DeliveryChannelType;
import com.incomm.cclp.account.domain.model.MessageType;
import com.incomm.cclp.account.domain.model.OperationType;
import com.incomm.cclp.account.domain.model.RedemptionLockEntity;
import com.incomm.cclp.account.domain.model.SpNumberType;
import com.incomm.cclp.domain.StatementsLogInfo;
import com.incomm.cclp.domain.TransactionLogInfo;
import com.incomm.cclp.dto.AccountBalance;
import com.incomm.cclp.dto.StatementLog;

public interface AccountAggregateRepositoryInterface {

	public CardEntity loadCardEntity(Optional<SpNumberType> spNumberType, String spNumber);

	public CardEntity loadCardEntity(Map<String, String> valueObject);

	public AccountPurseAggregateNew loadAccountPurseAggregate(long accountId, long productId, long purseId,
			AccountPurseAggregateStateType stateType);

	public AccountPurseAggregateNew loadAccountPurseAggregate(long accountId, long productId, String purseName,
			AccountPurseAggregateStateType stateType);

	public AccountEntity loadAccountEntity(long accountId);

	public List<TransactionLogInfo> loadTransactionInfo( //
			DeliveryChannelType channelType, //
			MessageType messageType, //
			String correlationId, //
			String cardNumbeHash, //
			LocalDateTime transactionDate, //
			String responseCode);

	public TransactionLogInfo loadTransactionInfo(//
			DeliveryChannelType channelType, //
			MessageType messageType, //
			String correlationId, //
			String cardNumberHash, //
			long purseId, //
			LocalDate transactionDate, //
			String responseCode);

	public List<StatementLog> loadStatementLogs(//
			DeliveryChannelType channelType, //
			String correlationId, //
			String cardNumberHash, //
			String transactionDate, //
			String transactionCode);

	public List<StatementsLogInfo> loadFeeStatementLogs(DeliveryChannelType channelType, String correlationId, OperationType operationType,
			String cardNumberHash, String transactionDate, String transactionCode);

	public List<RedemptionLockEntity> loadRedemptionLocks(String cardNumberHash, String lockFlag);

	public List<AccountBalance> loadAllActiveAccountPurses(long accountId);

	public long getNextAuthId();

	public long getNextTransactionId();

	public long getNextAccountPurseId();

}
