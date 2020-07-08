package com.incomm.cclp.account.domain.model;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import com.incomm.cclp.account.domain.factory.AccountAggregateRepository;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.exception.ServiceException;

@RunWith(MockitoJUnitRunner.class)
public class AccountPurseGroupTest {
	@Mock
	private Comparator<AccountPurse> sortAscendingOrder;

	@Mock
	private Comparator<AccountPurse> sortDescendingOrder;

	@Mock
	private ApplicationContext context;

	@Mock
	private AccountAggregateRepository accountAggregateRepository;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void testCreditPurseBalance_withoutAccountPurseId_PurseNotPresent_createNewPurseIsFalse() throws Exception {
		BigDecimal transactionAmount = BigDecimal.TEN;
		Map<LedgerEntryType, BigDecimal> transactionFees = getTransactionFees(1);
		boolean createPurseIfNoneExist = false;

		AccountPurseGroup accountPurseGroup = buildAccountPurseGroup(Boolean.TRUE,PurseStatusType.ACTIVE);

		expectedException.expect(ServiceException.class);
		expectedException.expectMessage(SpilExceptionMessages.INVALID_PURSE);

		accountPurseGroup.creditPurseBalance(transactionAmount, transactionFees, PurseBalanceType.BOTH, createPurseIfNoneExist);

	}

	@Test
	public void testCreditPurseBalance_withoutAccountPurseId_PursePresent_createNewPurseIsFalse_withFee() {
		BigDecimal transactionAmount = BigDecimal.valueOf(10.23);
		Map<LedgerEntryType, BigDecimal> transactionFees = getTransactionFees(1);
		boolean createPurseIfNoneExist = false;

		AccountPurseGroup accountPurseGroup = buildAccountPurseGroup(Boolean.FALSE,PurseStatusType.ACTIVE);

		AccountPurseGroupUpdate accountPurseGroupUpdate = accountPurseGroup.creditPurseBalance(transactionAmount, transactionFees,
				PurseBalanceType.BOTH, createPurseIfNoneExist);
		List<AccountPurseUpdateNew> accountPurseUpdates = accountPurseGroupUpdate.getAccountPurseUpdates();
		
		BigDecimal expectedAvailableBalance=BigDecimal.valueOf(19.21);
		BigDecimal expectedLedgerBalance=BigDecimal.valueOf(19.21);
		Boolean expectedIsNew=Boolean.FALSE;
		long expectedAccountPurseId = 123;
		int expectedSize = 1;
		
		assertAccountPurseUpdate(accountPurseUpdates,expectedAccountPurseId,expectedAvailableBalance,expectedLedgerBalance,expectedIsNew,expectedSize);
	}
	
	private void assertAccountPurseUpdate(List<AccountPurseUpdateNew> accountPurseUpdates,long expectedAccountPurseId, BigDecimal expectedAvailableBalance,BigDecimal expectedLedgerBalance,
			Boolean expectedIsNew,int expectedSize) {
		assertEquals(expectedSize, accountPurseUpdates.size());
		assertEquals(expectedAccountPurseId, accountPurseUpdates.get(0)
			.getAccountPurseKey()
			.getAccountPurseId());
		assertEquals(expectedAvailableBalance, accountPurseUpdates.get(0)
			.getNewAvailableBalance());
		assertEquals(expectedLedgerBalance, accountPurseUpdates.get(0)
			.getNewLedgerBalance());
		assertEquals(expectedIsNew, accountPurseUpdates.get(0)
			.isNew());
		assertEquals(BigDecimal.TEN, accountPurseUpdates.get(0)
			.getPreviousAvailableBalance());
		assertEquals(BigDecimal.TEN, accountPurseUpdates.get(0)
			.getPreviousLedgerBalance());
	}
	
	@Test
	public void testCreditPurseBalance_withAccountPurseId_PursePresent_withFee() {
		BigDecimal transactionAmount = BigDecimal.valueOf(10.23);
		Map<LedgerEntryType, BigDecimal> transactionFees = getTransactionFees(1);

		AccountPurseGroup accountPurseGroup = buildAccountPurseGroup(Boolean.FALSE,PurseStatusType.ACTIVE);

		AccountPurseGroupUpdate accountPurseGroupUpdate = accountPurseGroup.creditPurseBalance(123,transactionAmount, transactionFees,
				PurseBalanceType.BOTH);
		List<AccountPurseUpdateNew> accountPurseUpdates = accountPurseGroupUpdate.getAccountPurseUpdates();

		assertEquals(1, accountPurseUpdates.size());
		assertEquals(123, accountPurseUpdates.get(0)
			.getAccountPurseKey()
			.getAccountPurseId());
		assertEquals(BigDecimal.valueOf(19.21), accountPurseUpdates.get(0)
			.getNewAvailableBalance());
		assertEquals(BigDecimal.valueOf(19.21), accountPurseUpdates.get(0)
			.getNewLedgerBalance());
		assertEquals(Boolean.FALSE, accountPurseUpdates.get(0)
			.isNew());
		assertEquals(BigDecimal.TEN, accountPurseUpdates.get(0)
			.getPreviousAvailableBalance());
		assertEquals(BigDecimal.TEN, accountPurseUpdates.get(0)
			.getPreviousLedgerBalance());
	}
	
	@Test
	public void testCreditPurseBalance_withAccountPurseId_PursePresent_withoutFee() {
		BigDecimal transactionAmount = BigDecimal.valueOf(10.23);
		Map<LedgerEntryType, BigDecimal> transactionFees = getTransactionFees(0);

		AccountPurseGroup accountPurseGroup = buildAccountPurseGroup(Boolean.FALSE,PurseStatusType.ACTIVE);

		AccountPurseGroupUpdate accountPurseGroupUpdate = accountPurseGroup.creditPurseBalance(123,transactionAmount, transactionFees,
				PurseBalanceType.BOTH);
		List<AccountPurseUpdateNew> accountPurseUpdates = accountPurseGroupUpdate.getAccountPurseUpdates();

		assertEquals(1, accountPurseUpdates.size());
		assertEquals(123, accountPurseUpdates.get(0)
			.getAccountPurseKey()
			.getAccountPurseId());
		assertEquals(BigDecimal.valueOf(20.23), accountPurseUpdates.get(0)
			.getNewAvailableBalance());
		assertEquals(BigDecimal.valueOf(20.23), accountPurseUpdates.get(0)
			.getNewLedgerBalance());
		assertEquals(Boolean.FALSE, accountPurseUpdates.get(0)
			.isNew());
		assertEquals(BigDecimal.TEN, accountPurseUpdates.get(0)
			.getPreviousAvailableBalance());
		assertEquals(BigDecimal.TEN, accountPurseUpdates.get(0)
			.getPreviousLedgerBalance());
	}

	@Test
	public void testGetLastExpiringPurse() throws Exception {
		AccountPurseGroup accountPurseGroup = buildAccountPurseGroup(Boolean.FALSE,PurseStatusType.ACTIVE);
		Optional<AccountPurse> purse = accountPurseGroup.getFarthestExpiringEffectivePurse();
		assertEquals(123, purse.get()
			.getAccountPurseKey()
			.getAccountPurseId());
		assertEquals(BigDecimal.valueOf(10), purse.get()
			.getAvailableBalance());
	}

	@Test
	public void testSortAccountPurseAscOrder() throws Exception {
		AccountPurseGroup accountPurseGroup = buildAccountPurseGroup(Boolean.FALSE,PurseStatusType.ACTIVE);

		List<AccountPurse> purse = accountPurseGroup.getSortedDebitEligiblePurses();

		assertEquals(432, purse.get(0)
			.getAccountPurseKey()
			.getAccountPurseId());
		assertEquals(543, purse.get(1)
			.getAccountPurseKey()
			.getAccountPurseId());
		assertEquals(123, purse.get(2)
			.getAccountPurseKey()
			.getAccountPurseId());
	}
	
	@Test
	public void getAccountPurseBalancesTest() {
	}

	@Test
	public void getFarthestExpiringEffectivePurseTest() {

	}

	@Test
	public void getMatchingAccountPurseTest() throws Exception {
		
	}

	@Test
	public void updatePurseStatusTest() throws Exception {

	}

	@Test
	public void testgetMaxActiveAccountPurseBalance() throws Exception {
		BigDecimal expected = BigDecimal.valueOf(131);
		AccountPurseGroup accountPurseGroup = buildAccountPurseGroup(Boolean.FALSE, PurseStatusType.ACTIVE);
		BigDecimal actual = accountPurseGroup.getMaxActiveAccountPurseBalance();
		assertEquals(expected, actual);
	}

	@Test
	public void getPurseId() {
		long expected = 1L;
		AccountPurseGroup accountPurseGroup = buildAccountPurseGroup(Boolean.FALSE, PurseStatusType.ACTIVE);
		long actualPurseId = accountPurseGroup.getPurseId();
		assertEquals(expected, actualPurseId);
	}

	@Test
	public void getPurseName() {
		String expected = "USD";
		AccountPurseGroup accountPurseGroup = buildAccountPurseGroup(Boolean.FALSE, PurseStatusType.ACTIVE);
		String actualPurseName = accountPurseGroup.getPurseName();
		assertEquals(expected, actualPurseName);
	}

	@Test
	public void validatePurseStatus() {
		AccountPurseGroup accountPurseGroup = buildAccountPurseGroup(Boolean.TRUE,PurseStatusType.INACTIVE);

		expectedException.expect(ServiceException.class);
		expectedException.expectMessage(SpilExceptionMessages.ACCOUNTPURSENOTACTIVE);

		accountPurseGroup.validatePurseStatus();
	}
	

	private List<AccountPurse> getAccountPursesList() {
		List<AccountPurse> accountPurses = new ArrayList<>();
		LocalDateTime today = LocalDateTime.now();

		// effective and never expiring purses
		accountPurses.add(AccountPurse.builder()
			.accountPurseKey(AccountPurseKey.from(154568956, 1, 123))
			.accountPurseAltKeyAttributes(AccountPurseAltKeyAttributes.from(today.minusDays(3), null, null))
			.availableBalance(BigDecimal.TEN)
			.ledgerBalance(BigDecimal.TEN)
			.isNew(false)
			.minorUnits(2)
			.purseName("USD")
			.purseTypeId(1)
			.firstLoadDate(today.minusDays(5))
			.build());

		// currently effective and not expired
		accountPurses.add(AccountPurse.builder()
			.accountPurseKey(AccountPurseKey.from(154568956, 1, 543))
			.accountPurseAltKeyAttributes(AccountPurseAltKeyAttributes.from(today.minusDays(60), today.plusDays(60), null))
			.availableBalance(BigDecimal.valueOf(31))
			.ledgerBalance(BigDecimal.valueOf(31))
			.isNew(false)
			.minorUnits(2)
			.purseName("USD")
			.purseTypeId(1)
			.firstLoadDate(today.minusDays(5))
			.build());

		accountPurses.add(AccountPurse.builder()
			.accountPurseKey(AccountPurseKey.from(154568956, 1, 432))
			.accountPurseAltKeyAttributes(AccountPurseAltKeyAttributes.from(today.minusDays(5), today.plusDays(20), null))
			.availableBalance(BigDecimal.valueOf(22))
			.ledgerBalance(BigDecimal.valueOf(22))
			.isNew(false)
			.minorUnits(2)
			.purseName("USD")
			.purseTypeId(1)
			.firstLoadDate(today.minusDays(5))
			.build());

		// expired purses
		accountPurses.add(AccountPurse.builder()
			.accountPurseKey(AccountPurseKey.from(154568956, 1, 321))
			.accountPurseAltKeyAttributes(AccountPurseAltKeyAttributes.from(today.minusDays(60), today.minusDays(60), null))
			.availableBalance(BigDecimal.valueOf(15))
			.ledgerBalance(BigDecimal.valueOf(15))
			.isNew(false)
			.minorUnits(2)
			.purseName("USD")
			.purseTypeId(1)
			.firstLoadDate(today.minusDays(5))
			.build());

		// Future effective purses
		accountPurses.add(AccountPurse.builder()
			.accountPurseKey(AccountPurseKey.from(154568956, 1, 987))
			.accountPurseAltKeyAttributes(AccountPurseAltKeyAttributes.from(today.plusDays(20), today.plusDays(90), null))
			.availableBalance(BigDecimal.valueOf(8))
			.ledgerBalance(BigDecimal.valueOf(8))
			.isNew(false)
			.minorUnits(2)
			.purseName("USD")
			.purseTypeId(1)
			.firstLoadDate(today.minusDays(5))
			.build());

		// Future effective and never expiring purses
		accountPurses.add(AccountPurse.builder()
			.accountPurseKey(AccountPurseKey.from(154568956, 1, 654))
			.accountPurseAltKeyAttributes(AccountPurseAltKeyAttributes.from(today.plusDays(3), null, null))
			.availableBalance(BigDecimal.valueOf(60))
			.ledgerBalance(BigDecimal.valueOf(60))
			.isNew(false)
			.minorUnits(2)
			.purseName("USD")
			.purseTypeId(1)
			.firstLoadDate(today.minusDays(5))
			.build());

		return accountPurses;
	}

	private Map<LedgerEntryType, BigDecimal> getTransactionFees(int count) {
		Map<LedgerEntryType, BigDecimal> transactionFees = new HashMap<>();
		switch (count) {
		case 1:
			transactionFees.put(LedgerEntryType.FLAT_FEE, BigDecimal.valueOf(1.02));
			break;
		case 2:
			transactionFees.put(LedgerEntryType.FLAT_FEE, BigDecimal.valueOf(1.02));
			transactionFees.put(LedgerEntryType.PERCENT_FEE, BigDecimal.valueOf(1.02));
			break;
		default:
			transactionFees = Collections.emptyMap();
			break;
		}

		return transactionFees;
	}

	private AccountPurseGroup buildAccountPurseGroup(boolean isNew,PurseStatusType status) {
		ProductPurse productPurse = buildProductPurse(Boolean.FALSE, "USD");
		AccountPurseGroupStatus accountPurseGroupStatus = AccountPurseGroupStatus.from(status);

		if (isNew) {
			return AccountPurseGroup.builder()
				.accountPurseGroupKey(AccountPurseGroupKey.from(154568956, 1))
				.accountPurses(Collections.emptyList())
				.productPurse(productPurse)
				.groupStatus(accountPurseGroupStatus)
				.build();
		}

		List<AccountPurse> accountPurses = getAccountPursesList();

		AccountPurseGroup accountPurseGroup = AccountPurseGroup.builder()
			.accountPurseGroupKey(AccountPurseGroupKey.from(154568956, 1))
			.accountPurses(accountPurses)
			.productPurse(productPurse)
			.groupStatus(accountPurseGroupStatus)
			.build();
		accountPurseGroup.setMaxBalance(BigDecimal.valueOf(500.23));
		
		return accountPurseGroup;
	}

	private ProductPurse buildProductPurse(boolean isDefaultPurse, String purseName) {
		return ProductPurse.builder()
			.isDefault(isDefaultPurse)
			.purseName(purseName)
			.currencyCode("USD")
			.currencyId("840")
			.currencyMinorUnits(2)
			.productId(32)
			.purseId(1)
			.build();
	}



}
