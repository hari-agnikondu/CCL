package com.incomm.cclp.account.application.persistence;

import com.incomm.cclp.account.domain.event.TransactionFailedEvent;

public interface AccountTransactionPersistenceHandlerInterface {

	public void persist(AccountTransactionUpdate accountTransactionUpdate);

	public void persist(TransactionFailedEvent event);

}
