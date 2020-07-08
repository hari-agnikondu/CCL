package com.incomm.cclp.account.domain.event.subscriber;

import com.incomm.cclp.account.domain.event.TransactionFailedEvent;
import com.incomm.cclp.account.domain.model.AccountAggregateUpdate;

public interface PersistenceHandlerInterface {

	public void persist(AccountAggregateUpdate accountAggregateUpdate);

	public void persist(TransactionFailedEvent transactionFailed);

}
