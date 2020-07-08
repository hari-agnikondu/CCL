package com.incomm.cclp.account.domain.model;

import static com.incomm.cclp.account.util.CodeUtil.isNotNull;
import static com.incomm.cclp.account.util.CodeUtil.isNull;
import static com.incomm.cclp.account.util.CodeUtil.not;
import static com.incomm.cclp.account.util.CodeUtil.setScale;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;
import java.util.function.BiFunction;

import org.junit.Test;

import com.incomm.cclp.account.domain.model.AccountPurseUpdateNew.AccountPurseUpdateNewBuilder;
import com.incomm.cclp.account.util.DateTimeUtil;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.account.domain.model.AccountPurse;

public class AccountPurseTest {

	private AccountPurseKey accountPurseKey = AccountPurseKey.from(140000036, 25, 216122); // long accountId, long
																							// purseId, long
																							// accountPurseId

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	private LocalDateTime effectiveDate = LocalDateTime.parse("2019-12-02 11:30", formatter);
	private LocalDateTime expiryDate = LocalDateTime.parse("2020-06-02 11:30", formatter);
	private String skuCode = "";

	private AccountPurseAltKeyAttributes accountPurseAltKeyAttributes = AccountPurseAltKeyAttributes.from(effectiveDate, expiryDate,
			skuCode);

	private BigDecimal ledgerBalance = new BigDecimal("173.29");
	private BigDecimal availableBalance = new BigDecimal("173.29");

	/*
	 * private LocalDateTime firstLoadDate = LocalDateTime.parse("12-NOV-19", formatter); private int purseTypeId = 4;
	 * 
	 * private String purseName = "Walgreens-USD";
	 */
	AccountPurseUpdateNewBuilder accountPurseUpdate = AccountPurseUpdateNew.builder()
		.accountPurseKey(this.accountPurseKey)
		.attributes(this.accountPurseAltKeyAttributes)
		.previousLedgerBalance(this.ledgerBalance)
		.previousAvailableBalance(this.availableBalance);

	AccountPurse accountPurse = AccountPurse.builder()
		.accountPurseKey(this.accountPurseKey)
		.accountPurseAltKeyAttributes(this.accountPurseAltKeyAttributes)
		.availableBalance(this.availableBalance)
		.minorUnits(2)
		.ledgerBalance(this.ledgerBalance)
		.build();

	@Test
	public void getPurseIdTest() {

		long response = accountPurse.getAccountPurseKey().getPurseId();
		long expected = 25L;

		assertEquals(expected, response);
	}

	@Test
	public void hasBalanceForDebitTest() {
		BigDecimal transactionAmount = new BigDecimal("100.0");
		boolean expected = true;

		boolean response = accountPurse.hasBalanceForDebit(transactionAmount);
		assertEquals(expected, response);
	}
	
	@Test //LedgerEntry
	public void creditPurseBalanceTest() {
		BigDecimal transactionAmount = new BigDecimal("13.43");
		LedgerEntryType ledgerEntryType = LedgerEntryType.TRANSACTION_AMOUNT;
		PurseBalanceType purseBalanceType = PurseBalanceType.BOTH;
		
		LedgerEntry response =  accountPurse.creditPurseBalance(transactionAmount, ledgerEntryType, purseBalanceType);
		
		BigDecimal expectedAvailableBalance = new BigDecimal("186.72");
		//BigDecimal expectedLedgerBalance = new BigDecimal("180");
		
		assertEquals(expectedAvailableBalance, response.getNewAvailableBalance());
	}

	@Test //LedgerEntry
	public void debitPurseBalanceTest() {
		BigDecimal transactionAmount = new BigDecimal("13.43");
		LedgerEntryType ledgerEntryType = LedgerEntryType.TRANSACTION_AMOUNT;
		PurseBalanceType purseBalanceType = PurseBalanceType.BOTH;
		
		LedgerEntry response =  accountPurse.debitPurseBalance(transactionAmount, ledgerEntryType, purseBalanceType);
		
		BigDecimal expectedAvailableBalance = new BigDecimal("159.86");
		//BigDecimal expectedLedgerBalance = new BigDecimal("160");
		
		assertEquals(expectedAvailableBalance, response.getNewAvailableBalance());
		
	}

	@Test //LedgerEntry
	public void debitPurseBalanceAllowNegative() {
		BigDecimal transactionAmount = new BigDecimal("181.47");
		LedgerEntryType ledgerEntryType = LedgerEntryType.TRANSACTION_AMOUNT;
		PurseBalanceType purseBalanceType = PurseBalanceType.BOTH;
		
		LedgerEntry response =  accountPurse.debitPurseBalanceAllowNegative(transactionAmount, ledgerEntryType, purseBalanceType);
		
		BigDecimal expectedAvailableBalance = new BigDecimal("-8.18");
		//BigDecimal expectedLedgerBalance = new BigDecimal("-10");
		
		assertEquals(expectedAvailableBalance, response.getNewAvailableBalance());
	}

	@Test //LedgerEntry
	public void debitPurseBalanceForUnlockTest() {
		BigDecimal transactionAmount = new BigDecimal("13.49");
		LedgerEntryType ledgerEntryType = LedgerEntryType.TRANSACTION_AMOUNT;
		PurseBalanceType purseBalanceType = PurseBalanceType.BOTH;
		BigDecimal lockedAmount = new BigDecimal("37.13");
		
		LedgerEntry response =  accountPurse.debitPurseBalanceForUnlock(transactionAmount,lockedAmount, ledgerEntryType, purseBalanceType);
		
		BigDecimal expectedAvailableBalance = new BigDecimal("159.80");
		//BigDecimal expectedLedgerBalance = new BigDecimal("10.0");
		
		assertEquals(expectedAvailableBalance, response.getNewAvailableBalance());
	}
	
	@Test
	public void isZeroBalancePurseTest() {
		boolean response = accountPurse.isZeroBalancePurse();
		boolean expected = false;
		
		assertEquals(expected, response);
	}


	@Test
	public void isUnexpiredEffectivePurseTest() {
		boolean response = accountPurse.isUnexpiredEffectivePurse();
		boolean expected = true;

		assertEquals(expected, response);
	}

	@Test  
	public void isExpiredTest() {
		boolean response = accountPurse.isExpired();
		boolean expected = false;

		assertEquals(expected, response);
	}
	
	@Test
	public void isEffectiveTest() {
		
		boolean response = this.accountPurse.isEffective();
		boolean expected = true;

		assertEquals(expected, response);
	}
		

}