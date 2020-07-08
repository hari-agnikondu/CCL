package com.incomm.cclp.account.domain.model;

import java.util.Arrays;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionType {

	ACTIVATION("act", "002", MessageType.ORIGINAL, OperationType.CREDIT, "Activation"),
	ACTIVATION_REVERSAL("reverseact", "002", MessageType.REVERSE, OperationType.DEBIT, "Activation Reversal"),

	ACTIVATION_WITH_PREAUTHORIZATION("preauthact", "001", MessageType.ORIGINAL, OperationType.NONFINANCIAL,
			"Activation with Preauthorization"),

	BALANCE_INQUIRY("balinq", "004", MessageType.ORIGINAL, OperationType.NONFINANCIAL, "Balance Inquiry"),

	BALANCE_TRANSFER("baltransfer", "015", MessageType.ORIGINAL, OperationType.NONFINANCIAL, "Balance Transfer"),
	BALANCE_TRANSFER_REVERSAL("baltransferreversal", "015", MessageType.REVERSE, OperationType.NONFINANCIAL, "Balance Transfer Reversal"),

	CARD_REDEMPTION("redeem", "009", MessageType.ORIGINAL, OperationType.DEBIT, "Card Redemption"),
	CARD_REDEMPTION_REVERSAL("redeemreversal", "009", MessageType.REVERSE, OperationType.CREDIT, "Card Redemption Reversal"),

	CARD_TO_CARD_TRANSFER("cardcardtransfer", "016", MessageType.ORIGINAL, OperationType.DEBIT, "Card To Card Transfer"),
	CARD_TO_CARD_TRANSFER_REVERSAL("cardcardtransferrev", "016", MessageType.REVERSE, OperationType.CREDIT,
			"Card To Card Transfer Reversal"),

	CASHOUT("cashout", "005", MessageType.ORIGINAL, OperationType.DEBIT, "Cashout"),
	CASHOUT_REVERSAL("reversecashout", "005", MessageType.REVERSE, OperationType.CREDIT, "Cashout Reversal"),

	CREDIT("credit", "011", MessageType.ORIGINAL, OperationType.CREDIT, "Credit"),
	CREDIT_REVERSAL("creditreversal", "011", MessageType.REVERSE, OperationType.DEBIT, "Credit Reversal"),

	DEACTIVATION("deact", "003", MessageType.ORIGINAL, OperationType.DEBIT, "Deactivation"),
	DEACTIVATION_REVERSAL("revdeact", "003", MessageType.REVERSE, OperationType.CREDIT, "Deactivation Reversal"),

	PRE_VALUE_INSERTION("prevalins", "013", MessageType.ORIGINAL, OperationType.NONFINANCIAL, "Pre Value Insertion"),

	REDEMPTION_WITH_LOCK("redemptionauthlock", "007", MessageType.ORIGINAL, OperationType.DEBIT, "Redemption with Lock (AFD)"),
	REDEMPTION_WITH_LOCK_REVERSAL("revredemptionauthlock", "007", MessageType.REVERSE, OperationType.CREDIT,
			"Redemption with Lock (AFD) Reversal"),

	REDEMPTION_WITH_UNLOCK("redemptioncompunlock", "008", MessageType.ORIGINAL, OperationType.DEBIT, "Redemption with Unlock (AFD)"),
	REDEMPTION_WITH_UNLOCK_REVERSAL("revredemptioncompunlock", "008", MessageType.REVERSE, OperationType.CREDIT,
			"Redemption with Unlock (AFD) Reversal"),

	SALE_ACTIVE_CODE("saleactivecode", "098", MessageType.ORIGINAL, OperationType.CREDIT, "Sale Active Code"),
	SALE_ACTIVE_CODE_REVERSAL("saleactivecodereversal", "098", MessageType.REVERSE, OperationType.DEBIT, "Sale Active Code Reversal"),

	STORE_CREDIT("storecredit", "010", MessageType.ORIGINAL, OperationType.CREDIT, "Store Credit"),
	STORE_CREDIT_REVERSAL("storecreditreversal", "010", MessageType.REVERSE, OperationType.DEBIT, "Store Credit Reversal"),

	LOAD_ACCOUNT_PURSE("loadAccountPurse", "100", MessageType.ORIGINAL, OperationType.CREDIT, "Load Account Purse"),
	LOAD_ACCOUNT_PURSE_REVERSAL("loadAccountPurseReversal", "100", MessageType.REVERSE, OperationType.DEBIT, "Load Account Purse Reversal"),

	TOP_UP_PURSE("topUpAccountPurse", "102", MessageType.ORIGINAL, OperationType.CREDIT, "Top-up Purse"),
	TOP_UP_PURSE_REVERSAL("topUpAccountPurseReversal", "102", MessageType.REVERSE, OperationType.DEBIT, "Top-up Purse Reversal"),

	UNLOAD_ACCOUNT_PURSE("unloadAccountPurse", "101", MessageType.ORIGINAL, OperationType.DEBIT, "Unload Account Purse"),
	UNLOAD_ACCOUNT_PURSE_REVERSAL("unloadAccountPurseReversal", "101", MessageType.REVERSE, OperationType.CREDIT,
			"Unload Account Purse Reversal"),

	UPDATE_PURSE_STATUS("updatePurseStatus", "103", MessageType.ORIGINAL, OperationType.NONFINANCIAL, "Update Purse Status"),
	UPDATE_PURSE_STATUS_REVERSAL("updatePurseStatusReversal", "107", MessageType.REVERSE, OperationType.NONFINANCIAL,
			"Update Purse Status Reversal"),

	VALUE_INSERTION("valins", "014", MessageType.ORIGINAL, OperationType.CREDIT, "Value Insertion"),
	VALUE_INSERTION_REVERSAL("revvalins", "014", MessageType.REVERSE, OperationType.DEBIT, "Value Insertion Reversal"),

	RECHARGE("recharge", "012", MessageType.ORIGINAL, OperationType.CREDIT, "Recharge"),
	RECHARGE_REVERSAL("rechargereversal", "012", MessageType.REVERSE, OperationType.DEBIT, "Recharge Reversal"),

	CARD_SWAP("cardSwap", "111", MessageType.ORIGINAL, OperationType.DEBIT, "Card Swap"),
	CARD_SWAP_REVERSAL("cardSwapReversal", "111", MessageType.REVERSE, OperationType.CREDIT, "Card Swap Reversal");

	private String transactionShortName;
	private String transactionCode;
	private MessageType messageType;
	private OperationType operationType;
	private String transactionDescription;

	public static Optional<TransactionType> byTransactionShortNameAndMessageType(String transactionShortName, MessageType messageType) {

		return Arrays.stream(TransactionType.values())
			.filter(type -> type.getTransactionShortName()
				.equals(transactionShortName) && type.messageType == messageType)
			.findFirst();

	}
}
