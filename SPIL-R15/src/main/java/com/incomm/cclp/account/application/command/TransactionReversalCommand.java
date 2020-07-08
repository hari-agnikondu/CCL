package com.incomm.cclp.account.application.command;

import java.util.List;

import lombok.Value;

@Value
public class TransactionReversalCommand {

	private AccountTransactionCommand transactionCommand;

	List<?> accountUpdate;

}
