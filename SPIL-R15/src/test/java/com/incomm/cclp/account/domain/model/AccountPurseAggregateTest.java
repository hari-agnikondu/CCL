package com.incomm.cclp.account.domain.model;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.exception.ServiceException;

@RunWith(MockitoJUnitRunner.class)
public class AccountPurseAggregateTest {

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Test
	public void testCreditPurseBalance_success_withoutAccountPurseId() throws Exception {
		long purseId = 1;
		BigDecimal transactionAmount = BigDecimal.valueOf(10.11);
		Map<LedgerEntryType, BigDecimal> transactionFees = Collections.emptyMap();
		boolean createPurseIfNoneExist = false;
		BigDecimal finalAvailableBalance = new BigDecimal("20.11");

		AccountPurseAggregateNew accountPurseAggregate = buildAccountPurseAggregate();
		accountPurseAggregate.setMaxPurseBalance(purseId, BigDecimal.valueOf(50.00));
		AccountPurseGroupUpdate accountPurseGroupUpdate = accountPurseAggregate.creditPurseBalance(purseId, transactionAmount,
				transactionFees, createPurseIfNoneExist);

		assertEquals(LedgerEntryType.TRANSACTION_AMOUNT, accountPurseGroupUpdate.getLedgerEntries()
			.get(0)
			.getLedgerEntryType());
		assertEquals(finalAvailableBalance, accountPurseGroupUpdate.getLedgerEntries()
			.get(0)
			.getNewAvailableBalance());
	}

	@Test
	public void testCreditPurseBalance_purseIdNotPresent_withoutAccountPurseId() throws Exception {
		long purseId = 5;
		BigDecimal transactionAmount = BigDecimal.valueOf(10.11);
		Map<LedgerEntryType, BigDecimal> transactionFees = Collections.emptyMap();
		boolean createPurseIfNoneExist = false;

		expectedEx.expect(ServiceException.class);
		expectedEx.expectMessage(SpilExceptionMessages.ACCOUNT_PURSE_GROUP_NOT_FOUND);

		AccountPurseAggregateNew accountPurseAggregate = buildAccountPurseAggregate();
		accountPurseAggregate.creditPurseBalance(purseId, transactionAmount, transactionFees, createPurseIfNoneExist);
	}

	@Test
	public void testCreditPurseBalance_success_withAccountPurseId() throws Exception {
		long purseId = 1;
		long accountPurseId = 12121;
		BigDecimal transactionAmount = BigDecimal.valueOf(10.11);
		Map<LedgerEntryType, BigDecimal> transactionFees = Collections.emptyMap();
		BigDecimal finalAvailableBalance = new BigDecimal("20.11");

		AccountPurseAggregateNew accountPurseAggregate = buildAccountPurseAggregate();
		accountPurseAggregate.setMaxPurseBalance(purseId, BigDecimal.valueOf(50.00));
		AccountPurseGroupUpdate accountPurseGroupUpdate = accountPurseAggregate.creditPurseBalance(purseId, accountPurseId,
				transactionAmount, transactionFees);

		assertEquals(LedgerEntryType.TRANSACTION_AMOUNT, accountPurseGroupUpdate.getLedgerEntries()
			.get(0)
			.getLedgerEntryType());
		assertEquals(finalAvailableBalance, accountPurseGroupUpdate.getLedgerEntries()
			.get(0)
			.getNewAvailableBalance());
	}

	@Test
	public void testCreditPurseBalance_purseIdNotPresent_withAccountPurseId() throws Exception {
		long purseId = 5;
		long accountPurseId = 12212;
		BigDecimal transactionAmount = BigDecimal.valueOf(10.11);
		Map<LedgerEntryType, BigDecimal> transactionFees = Collections.emptyMap();

		expectedEx.expect(ServiceException.class);
		expectedEx.expectMessage(SpilExceptionMessages.ACCOUNT_PURSE_GROUP_NOT_FOUND);

		AccountPurseAggregateNew accountPurseAggregate = buildAccountPurseAggregate();
		accountPurseAggregate.creditPurseBalance(purseId, accountPurseId, transactionAmount, transactionFees);
	}

	@Test
	public void testDebitPurseBalance_purseIdNotPresent_withAccountPurseId() throws Exception {
		long purseId = 5;
		long accountPurseId = 12212;
		BigDecimal transactionAmount = BigDecimal.valueOf(5.10);
		Map<LedgerEntryType, BigDecimal> transactionFees = Collections.emptyMap();
		boolean allowPartialAuth = false;

		expectedEx.expect(ServiceException.class);
		expectedEx.expectMessage(SpilExceptionMessages.ACCOUNT_PURSE_GROUP_NOT_FOUND);

		AccountPurseAggregateNew accountPurseAggregate = buildAccountPurseAggregate();
		accountPurseAggregate.debitPurseBalance(purseId, accountPurseId, transactionAmount, transactionFees, PurseBalanceType.BOTH,
				allowPartialAuth);
	}

	@Test
	public void testDebitPurseBalance_success_withAccountPurseId() throws Exception {
		long purseId = 1;
		long accountPurseId = 12121;
		BigDecimal transactionAmount = BigDecimal.valueOf(5.11);
		Map<LedgerEntryType, BigDecimal> transactionFees = Collections.emptyMap();
		BigDecimal finalAvailableBalance = new BigDecimal("4.89");
		boolean allowPartialAuth = false;

		AccountPurseAggregateNew accountPurseAggregate = buildAccountPurseAggregate();
		accountPurseAggregate.setMaxPurseBalance(purseId, BigDecimal.valueOf(50.00));
		AccountPurseGroupUpdate accountPurseGroupUpdate = accountPurseAggregate.debitPurseBalance(purseId, accountPurseId,
				transactionAmount, transactionFees, PurseBalanceType.BOTH, allowPartialAuth);

		assertEquals(LedgerEntryType.TRANSACTION_AMOUNT, accountPurseGroupUpdate.getLedgerEntries()
			.get(0)
			.getLedgerEntryType());
		assertEquals(finalAvailableBalance, accountPurseGroupUpdate.getLedgerEntries()
			.get(0)
			.getNewAvailableBalance());
	}

	@Test
	public void testDebitPurseBalance_success_withoutAccountPurseId() throws Exception {
		long purseId = 1;
		BigDecimal transactionAmount = BigDecimal.valueOf(5.11);
		Map<LedgerEntryType, BigDecimal> transactionFees = Collections.emptyMap();
		BigDecimal finalAvailableBalance = new BigDecimal("4.89");
		boolean allowPartialAuth = false;

		AccountPurseAggregateNew accountPurseAggregate = buildAccountPurseAggregate();
		accountPurseAggregate.setMaxPurseBalance(purseId, BigDecimal.valueOf(50.00));
		AccountPurseGroupUpdate accountPurseGroupUpdate = accountPurseAggregate.debitPurseBalance(purseId, transactionAmount,
				transactionFees, PurseBalanceType.BOTH, allowPartialAuth);

		assertEquals(LedgerEntryType.TRANSACTION_AMOUNT, accountPurseGroupUpdate.getLedgerEntries()
			.get(0)
			.getLedgerEntryType());
		assertEquals(finalAvailableBalance, accountPurseGroupUpdate.getLedgerEntries()
			.get(0)
			.getNewAvailableBalance());
	}

	@Test
	public void testDebitPurseBalance_purseIdNotPresent_withoutAccountPurseId() throws Exception {
		long purseId = 5;
		BigDecimal transactionAmount = BigDecimal.valueOf(5.11);
		Map<LedgerEntryType, BigDecimal> transactionFees = Collections.emptyMap();
		boolean allowPartialAuth = false;

		expectedEx.expect(ServiceException.class);
		expectedEx.expectMessage(SpilExceptionMessages.ACCOUNT_PURSE_GROUP_NOT_FOUND);

		AccountPurseAggregateNew accountPurseAggregate = buildAccountPurseAggregate();
		accountPurseAggregate.debitPurseBalance(purseId, transactionAmount, transactionFees, PurseBalanceType.BOTH, allowPartialAuth);
	}

	private AccountPurseAggregateNew buildAccountPurseAggregate() {
		return AccountPurseAggregateNew.builder()
			.accountId(212121212)
			.accountPurseGroupByPurseId(getAccountPurseGroupByPurseId())
			.productId(2)
			.build();
	}

	private Map<Long, AccountPurseGroup> getAccountPurseGroupByPurseId() {
		AccountPurseGroup accountPurseGroup1 = getAccountPurseGroup(1, Boolean.TRUE, "USD");
		AccountPurseGroup accountPurseGroup2 = getAccountPurseGroup(2, Boolean.FALSE, "USD");

		Map<Long, AccountPurseGroup> accountPurseGroupByPurseId = new HashMap<>();
		accountPurseGroupByPurseId.put(Long.valueOf(1), accountPurseGroup1);
		accountPurseGroupByPurseId.put(Long.valueOf(2), accountPurseGroup2);

		return accountPurseGroupByPurseId;
	}

	private AccountPurseGroup getAccountPurseGroup(long purseId, boolean isDefaultPurse, String purseName) {
		AccountPurse accountPurse = buildAccountPurse(purseId);
		List<AccountPurse> listAccountPurses = new ArrayList<>();
		listAccountPurses.add(accountPurse);
		return AccountPurseGroup.builder()
			.accountPurseGroupKey(AccountPurseGroupKey.from(212121212, purseId))
			.accountPurses(listAccountPurses)
			.groupStatus(AccountPurseGroupStatus.from(PurseStatusType.ACTIVE))
			.productPurse(buildProductPurse(isDefaultPurse, purseName))
			.build();
	}

	private AccountPurse buildAccountPurse(long purseId) {
		return AccountPurse.builder()
			.accountPurseAltKeyAttributes(AccountPurseAltKeyAttributes.DEFAULT)
			.accountPurseKey(AccountPurseKey.from(212121212, purseId, 12121))
			.availableBalance(BigDecimal.TEN)
			.ledgerBalance(BigDecimal.TEN)
			.minorUnits(2)
			.build();
	}

	private ProductPurse buildProductPurse(boolean isDefaultPurse, String purseName) {
		return ProductPurse.builder()
			.isDefault(isDefaultPurse)
			.purseName(purseName)
			.build();
	}

}
