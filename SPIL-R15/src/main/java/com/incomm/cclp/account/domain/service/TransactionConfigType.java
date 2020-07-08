package com.incomm.cclp.account.domain.service;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

import com.incomm.cclp.account.application.command.AccountTransactionCommand;
import com.incomm.cclp.account.domain.model.TransactionType;
import com.incomm.cclp.account.domain.view.AccountSummaryView;
import com.incomm.cclp.config.SpringContext;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionConfigType {

	BALANCE_INQUIRY(TransactionType.BALANCE_INQUIRY, SpringContext.getBean(Constants.DOMAIN_SERVICE)::processBalanceInquiry),

	REDEEM(TransactionType.CARD_REDEMPTION, SpringContext.getBean(Constants.DOMAIN_SERVICE)::processRedemption),
	REDEEM_REVERSAL(TransactionType.CARD_REDEMPTION_REVERSAL, SpringContext.getBean(Constants.DOMAIN_SERVICE)::processRedeemReversal),

	STORE_CREDIT(TransactionType.STORE_CREDIT, SpringContext.getBean(Constants.DOMAIN_SERVICE)::processStoreCredit),
	STORE_CREDIT_REVERSAL(TransactionType.STORE_CREDIT_REVERSAL,
			SpringContext.getBean(Constants.DOMAIN_SERVICE)::processStoreCreditReversal),

	CREDIT(TransactionType.CREDIT, SpringContext.getBean(Constants.DOMAIN_SERVICE)::processStoreCredit),
	CREDIT_REVERSAL(TransactionType.CREDIT_REVERSAL, SpringContext.getBean(Constants.DOMAIN_SERVICE)::processStoreCreditReversal),

	VALUE_INSERTION(TransactionType.VALUE_INSERTION, SpringContext.getBean(Constants.DOMAIN_SERVICE)::processValueInsertion),
	VALUE_INSERTION_REVERSAL(TransactionType.VALUE_INSERTION_REVERSAL,
			SpringContext.getBean(Constants.DOMAIN_SERVICE)::processValueInsertionReversal),

	RECHARGE(TransactionType.RECHARGE, SpringContext.getBean(Constants.DOMAIN_SERVICE)::processValueInsertion),
	RECHARGE_REVERSAL(TransactionType.RECHARGE_REVERSAL, SpringContext.getBean(Constants.DOMAIN_SERVICE)::processValueInsertionReversal),

	REDEMPTION_WITH_LOCK(TransactionType.REDEMPTION_WITH_LOCK, SpringContext.getBean(Constants.DOMAIN_SERVICE)::processRedemptionWithLock),
	REDEMPTION_WITH_LOCK_REVERSAL(TransactionType.REDEMPTION_WITH_LOCK_REVERSAL,
			SpringContext.getBean(Constants.DOMAIN_SERVICE)::processRedemptionWithLockReversal),

	REDEMPTION_WITH_UNLOCK(TransactionType.REDEMPTION_WITH_UNLOCK,
			SpringContext.getBean(Constants.DOMAIN_SERVICE)::processRedemptionWithUnLock),
	REDEMPTION_WITH_UNLOCK_REVERSAL(TransactionType.REDEMPTION_WITH_UNLOCK_REVERSAL,
			SpringContext.getBean(Constants.DOMAIN_SERVICE)::processRedemptionWithUnLockReversal),

	CARD_SWAP(TransactionType.CARD_SWAP, SpringContext.getBean(Constants.DOMAIN_SERVICE)::processCardSwap),
	CARD_SWAP_REVERSAL(TransactionType.CARD_SWAP_REVERSAL, SpringContext.getBean(Constants.DOMAIN_SERVICE)::processCardSwapReversal);

	private final TransactionType transactionType;
	private final Function<AccountTransactionCommand, AccountSummaryView> function;

	public static Optional<TransactionConfigType> byTransactionType(TransactionType transactionType) {
		return Arrays.stream(TransactionConfigType.values())
			.filter(type -> type.transactionType == transactionType)
			.findFirst();

	}

	private static class Constants {
		public static final Class<AccountDomainService> DOMAIN_SERVICE = AccountDomainService.class;
	}

}
