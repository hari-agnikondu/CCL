package com.incomm.cclp.dao.services;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.eq;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.incomm.cclp.dao.service.impl.AuthChecksServiceImpl;
import com.incomm.cclp.domain.AccountPurseBalance;
import com.incomm.cclp.dto.RequestInfo;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.exception.SuspendCardException;
import com.incomm.cclp.repos.GenericRepo;
import com.incomm.cclp.service.AccountPurseService;
import com.incomm.cclp.transaction.constants.TransactionConstant;

/**
 *
 * @author
 */

@RunWith(MockitoJUnitRunner.class)
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class AuthChecksServiceTest {
	RequestInfo req = null;

	@Mock
	GenericRepo genericRepoMock;

	@Autowired
	@InjectMocks
	AuthChecksServiceImpl authChecksServiceinstance;

	@Mock
	AccountPurseService accountPurseServiceMock;

	public AuthChecksServiceTest() {
	}

	@BeforeClass
	public static void setUpClass() {

	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void tearDown() {

	}

	/**
	 * Test of doBalanceAuthCredit method, of class AuthChecksService.
	 * 
	 * @throws ServiceException
	 */

	@Test
	public void testdoBalanceAuthCredit_getClosingBalance() throws ServiceException {

		double txnAmount = 25.00;
		double txnFee = 3;
		double openingAvailableBalance = 10;
		double openingLedgerBalance = 10;
		double maxCardBal = 100;

		RequestInfo req = setRequest(txnAmount, txnFee, openingAvailableBalance, openingLedgerBalance, maxCardBal, 0, 0);
		when(accountPurseServiceMock.getCumulativePurseBalance(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(BigDecimal.TEN);
		authChecksServiceinstance.doBalanceAuthCredit(req);
		assertEquals(32.00, req.getClosingAvailBalance(), 0);
	}

	@Test(expected = ServiceException.class)
	public void testdoBalanceAuthCredit_closingBalanceLessThanZero() throws ServiceException {
		double txnAmount = 25.00;
		double txnFee = 26.00;
		double openingAvailableBalance = 0;
		double openingLedgerBalance = 0;
		double maxCardBal = 100;

		RequestInfo req = setRequest(txnAmount, txnFee, openingAvailableBalance, openingLedgerBalance, maxCardBal, 0, 0);
		authChecksServiceinstance.doBalanceAuthCredit(req);
	}

	@Test(expected = SuspendCardException.class)
	public void testdoBalanceAuthCredit_closingBalGreaterThanMaxBal() throws ServiceException {
		double txnAmount = 25.00;
		double txnFee = 3;
		double openingAvailableBalance = 10;
		double openingLedgerBalance = 10;
		double maxCardBal = 10;

		RequestInfo req = setRequest(txnAmount, txnFee, openingAvailableBalance, openingLedgerBalance, maxCardBal, 0, 0);
		when(accountPurseServiceMock.getCumulativePurseBalance(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(BigDecimal.TEN);
		authChecksServiceinstance.doBalanceAuthCredit(req);
	}

	//@Test
	public void testdoBalanceAuthCredit_newPurse() throws ServiceException {
		double txnAmount = 25.00;
		double txnFee = 3;
		double openingAvailableBalance = 10;
		double openingLedgerBalance = 10;
		double maxCardBal = 100;

		RequestInfo req = setRequest(txnAmount, txnFee, openingAvailableBalance, openingLedgerBalance, maxCardBal, 0, 0);
		req.setPurAuthReq(buildPurseAuthReqObject());

		when(accountPurseServiceMock.getCumulativePurseBalance(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(BigDecimal.TEN);
		when(accountPurseServiceMock.addAccountPurse(Mockito.any())).thenReturn(1);

		authChecksServiceinstance.doBalanceAuthCredit(req);

		assertEquals(BigInteger.valueOf(req.getPurAuthReq().getPurseId()), req.getPurseId());
		verify(accountPurseServiceMock, times(1)).addAccountPurse(eq(req.getPurAuthReq()));
	}

	// HandleSuspendCredit
	@Test
	public void testdoHandleSupendedCreditDebit_closingBalGreaterThanMaxBal() throws ServiceException {

		double txnAmount = 25.00;
		double txnFee = 2.50;
		double openingAvailableBalance = 10;
		double openingLedgerBalance = 10;
		double maxCardBal = 10.0;
		double closingAvailBalance = 15.0;

		RequestInfo req = setRequest(txnAmount, txnFee, openingAvailableBalance, openingLedgerBalance, maxCardBal, closingAvailBalance, 0);

		req.setCreditDebitFlg(TransactionConstant.CREDIT_CARD);
		req.setCardStatus(TransactionConstant.INACTIVE_CARD);
		when(genericRepoMock.updateOldCurrentCardStatus(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(1);
		authChecksServiceinstance.doHandleSupendedCreditDebit(req);
		verify(genericRepoMock, times(1)).updateOldCurrentCardStatus(eq(req.getCardNumHash()), eq(TransactionConstant.SUSPENDED_CREDIT),
				eq(TransactionConstant.INACTIVE_CARD));

	}

	// No update call
	@Test
	public void testdoHandleSupendedCreditDebit_closingBalLessThanMaxBal() throws ServiceException {

		double txnAmount = 25.00;
		double txnFee = 2.50;
		double openingAvailableBalance = 10;
		double openingLedgerBalance = 10;
		double maxCardBal = 10.0;
		double closingAvailBalance = 5.0;

		RequestInfo req = setRequest(txnAmount, txnFee, openingAvailableBalance, openingLedgerBalance, maxCardBal, closingAvailBalance, 0);

		req.setCreditDebitFlg(TransactionConstant.CREDIT_CARD);
		req.setCardStatus(TransactionConstant.INACTIVE_CARD);
		authChecksServiceinstance.doHandleSupendedCreditDebit(req);
		verify(genericRepoMock, times(0)).updateOldCurrentCardStatus(eq(req.getCardNumHash()), eq(TransactionConstant.SUSPENDED_DEBIT),
				eq(TransactionConstant.INACTIVE_CARD));

	}

	// shouldn't call update as closing bal is +ive
	@Test
	public void testdoHandleSupendedCreditDebit_debitSuspended() throws ServiceException {

		double txnAmount = 25.00;
		double txnFee = 2.50;
		double openingAvailableBalance = 10;
		double openingLedgerBalance = 10;
		double maxCardBal = 10.0;
		double closingAvailBalance = 15.0;

		RequestInfo req = setRequest(txnAmount, txnFee, openingAvailableBalance, openingLedgerBalance, maxCardBal, closingAvailBalance, 0);

		req.setCreditDebitFlg(TransactionConstant.DEBIT_CARD);
		req.setCardStatus(TransactionConstant.SUSPENDED_DEBIT);
		authChecksServiceinstance.doHandleSupendedCreditDebit(req);
		verify(genericRepoMock, times(0)).updateOldCurrentCardStatus(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

	}

	// should call update
	@Test
	public void testdoHandleSupendedCreditDebit_debitSuspendedClosingBalLessThanZero() throws ServiceException {

		double txnAmount = 25.00;
		double txnFee = 2.50;
		double openingAvailableBalance = 10;
		double openingLedgerBalance = 10;
		double maxCardBal = 10.0;
		double closingAvailBalance = -10.0;

		RequestInfo req = setRequest(txnAmount, txnFee, openingAvailableBalance, openingLedgerBalance, maxCardBal, closingAvailBalance, 0);
		req.setCreditDebitFlg(TransactionConstant.DEBIT_CARD);
		req.setCardStatus(TransactionConstant.INACTIVE_CARD);
		authChecksServiceinstance.doHandleSupendedCreditDebit(req);
		verify(genericRepoMock, times(1)).updateOldCurrentCardStatus(eq(req.getCardNumHash()), eq(TransactionConstant.SUSPENDED_DEBIT),
				eq(TransactionConstant.INACTIVE_CARD));
	}

	// shouldn't call update
	@Test
	public void testdoHandleSupendedCreditDebit_debitSuspendedClosingBalZero() throws ServiceException {

		double txnAmount = 25.00;
		double txnAuthorizedAmount = 25.00;
		double txnFee = 2.50;
		double openingAvailableBalance = 10;
		double openingLedgerBalance = 10;
		double maxCardBal = 10.0;
		double closingAvailBalance = 0.0;

		RequestInfo req = setRequest(txnAmount, txnFee, openingAvailableBalance, openingLedgerBalance, maxCardBal, closingAvailBalance, 0);
		// for reversal case
		req.setOrgnAuthAmt(txnAuthorizedAmount);
		req.setOrgnlTxnFee(txnFee);
		req.setCreditDebitFlg(TransactionConstant.DEBIT_CARD);
		req.setCardStatus(TransactionConstant.INACTIVE_CARD);
		authChecksServiceinstance.doHandleSupendedCreditDebit(req);
		verify(genericRepoMock, times(0)).updateOldCurrentCardStatus(eq(req.getCardNumHash()), eq(TransactionConstant.SUSPENDED_DEBIT),
				eq(TransactionConstant.INACTIVE_CARD));
	}

	@Test
	public void testdoBalanceCreditReversal_getClosingBalance() throws ServiceException {
		double txnAmount = 10.0;
		double txnAuthorizedAmount = 25.00;
		double txnFee = 3;
		double openingAvailableBalance = 22.00;
		double openingLedgerBalance = 22.00;
		double maxCardBal = 100;

		RequestInfo req = setRequest(txnAmount, txnFee, openingAvailableBalance, openingLedgerBalance, maxCardBal, 0, txnAuthorizedAmount);
		// for reversal case
		req.setOrgnAuthAmt(txnAuthorizedAmount);
		req.setOrgnlTxnFee(txnFee);

		when(genericRepoMock.updateBalancesByAccountPurseId(Mockito.anyLong(), Mockito.anyDouble(), Mockito.anyDouble())).thenReturn(1);
		authChecksServiceinstance.doBalanceCreditReversal(req);
		assertEquals(0, req.getClosingAvailBalance(), 0);
	}

	@Test
	public void testdoBalanceCreditReversal_getClosingLedBalance() throws ServiceException {
		double txnAmount = 10.0;
		double txnAuthorizedAmount = 25.00;
		double txnFee = 3;
		double openingAvailableBalance = 30;
		double openingLedgerBalance = 22;
		double maxCardBal = 100;

		RequestInfo req = setRequest(txnAmount, txnFee, openingAvailableBalance, openingLedgerBalance, maxCardBal, 0, txnAuthorizedAmount);
		// for reversal case
		req.setOrgnAuthAmt(txnAuthorizedAmount);
		req.setOrgnlTxnFee(txnFee);
		when(genericRepoMock.updateBalancesByAccountPurseId(Mockito.anyLong(), Mockito.anyDouble(), Mockito.anyDouble())).thenReturn(1);
		authChecksServiceinstance.doBalanceCreditReversal(req);
		assertEquals(0, req.getClosingLedgerBalance(), 0);
	}

	@Test
	public void testValidatePurseMaxBalanace() {
		double closingAvailableBalance = 10;
		double maxPurseBalance = 100;
		BigInteger accountId = BigInteger.valueOf(800287356);
		BigInteger productId = BigInteger.valueOf(32);
		BigInteger purseId = BigInteger.valueOf(1);
		when(accountPurseServiceMock.getCumulativePurseBalance(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(BigDecimal.TEN);
		authChecksServiceinstance.validatePurseMaxBalanace(accountId, productId, purseId, closingAvailableBalance, maxPurseBalance);
	}

	@Test(expected = ServiceException.class)
	public void testValidatePurseMaxBalanaceException() {
		double closingAvailableBalance = 100;
		double maxPurseBalance = 100;
		BigInteger accountId = BigInteger.valueOf(800287356);
		BigInteger productId = BigInteger.valueOf(32);
		BigInteger purseId = BigInteger.valueOf(1);
		when(accountPurseServiceMock.getCumulativePurseBalance(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(BigDecimal.TEN);
		authChecksServiceinstance.validatePurseMaxBalanace(accountId, productId, purseId, closingAvailableBalance, maxPurseBalance);
	}

	@Test
	public void testGetAccountPurseBalnceObj() {
		AccountPurseBalance reqPurse = buildPurseAuthReqObject();
		double closingLedBalanceOut = 10;
		double closingAvailBalanceOut = 10;
		authChecksServiceinstance.getAccountPurseBalnceObj(reqPurse, closingLedBalanceOut, closingAvailBalanceOut);
		assertEquals(BigDecimal.valueOf(closingLedBalanceOut), reqPurse.getLedgerBalance());
		assertEquals(BigDecimal.valueOf(closingAvailBalanceOut), reqPurse.getAvailableBalance());
	}

	private RequestInfo setRequest(double txnAmount, double txnFee, double openingAvailableBalance, double openingLedgerBalance,
			double maxCardBal, double closingAvailBalance, double txnAuthorizedAmount) {

		req = new RequestInfo();
		Map<String,Object> usage = new HashMap<>();
		Map<String,Object> usageFee = new HashMap<String,Object>();
		ValueDTO valueDto = new ValueDTO();
		valueDto.setUsageFee(usageFee);
		valueDto.setUsageLimit(usage);
		
		req.setAccountId("1232345");
		req.setTxnAmount(txnAmount);
		req.setTxnFee(txnFee);
		req.setOpeningAvailBalance(openingAvailableBalance);
		req.setOpeningLedgerBalance(openingLedgerBalance);
		req.setMaxPurseBalance(maxCardBal);
		req.setClosingAvailBalance(closingAvailBalance);
		req.setCardNumHash("lkzzA/ewe76ZyH3ObRBaiu9PwveHCLaMPd68hIwIM3c=");
		req.setAuthorizedAmount(txnAuthorizedAmount);
		req.setAccountPurseId(3324);
		req.setValueDTO(valueDto);
		req.setSysDate("2019-09-06");
		return req;

	}

	private AccountPurseBalance buildPurseAuthReqObject() {
		return AccountPurseBalance.builder()
			.accountPurseId(3324)
			.accountId(800287356)
			.productId(32)
			.purseId(1)
			.build();
	}
}
