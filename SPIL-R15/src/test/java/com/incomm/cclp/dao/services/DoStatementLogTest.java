package com.incomm.cclp.dao.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.text.ParseException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.incomm.cclp.dao.service.LogService;
import com.incomm.cclp.dao.service.impl.AuthChecksServiceImpl;
import com.incomm.cclp.dao.service.impl.SpilSaleActiveCodeDAOServiceImpl;
import com.incomm.cclp.dto.RequestInfo;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.repos.GenericRepo;

@RunWith(MockitoJUnitRunner.class)
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class DoStatementLogTest {

	@Mock
	private LogService logServiceMock;

	@Mock
	private GenericRepo genericRepoMock;
	@Mock
	private SpilSaleActiveCodeDAOServiceImpl spilSaleActiveCodeServiceMock;

	@Autowired
	@InjectMocks
	private SpilSaleActiveCodeDAOServiceImpl spilSaleActiveCodeServiceinstance;

	@Autowired
	@InjectMocks
	private AuthChecksServiceImpl authChecksServiceinstance;

	@Test
	public void testGeneratestatementLog_WhenIsCredit() throws ParseException, ServiceException {
		double txnAmount = 25.00;
		double txnFee = 2.50;
		double openingAvailableBalance = 10;
		double openingLedgerBalance = 10;
		double closingBalance = 32.5;
		String prodFeeCondition = "";
		String cr_db_flag = "C";
		RequestInfo req = setRequest(txnAmount, txnFee, openingAvailableBalance, openingLedgerBalance, closingBalance, cr_db_flag,
				prodFeeCondition);
		spilSaleActiveCodeServiceinstance.generateStatementLogObj(req, openingLedgerBalance, closingBalance, txnAmount, cr_db_flag, "N",
				"Sale Active Code Fee");
		assertEquals(33, req.getClosingBalance(), 0);
	}

	@Test
	public void testGeneratestatementLog_WhenIsDebit() throws ParseException, ServiceException {
		double txnAmount = 25.00;
		double txnFee = 2.50;
		double openingAvailableBalance = 10;
		double openingLedgerBalance = 10;
		double closingBalance = 5;
		String prodFeeCondition = "";
		String cr_db_flag = "D";
		RequestInfo req = setRequest(txnAmount, txnFee, openingAvailableBalance, openingLedgerBalance, closingBalance, cr_db_flag,
				prodFeeCondition);
		spilSaleActiveCodeServiceinstance.generateStatementLogObj(req, openingLedgerBalance, closingBalance, txnAmount, cr_db_flag, "N",
				"Sale Active Code Fee");
		assertEquals(5, req.getClosingBalance(), 0);
	}

	@Test
	public void testDoStatementLog_whenIsFinancial() throws ServiceException, ParseException {
		double txnAmount = 25.00;
		double txnFee = 0;
		double openingAvailableBalance = 10;
		double openingLedgerBalance = 10;
		double closingBalance = 30;
		String prodFeeCondition = "";
		String cr_db_flag = "D";
		RequestInfo req = setRequest(txnAmount, txnFee, openingAvailableBalance, openingLedgerBalance, closingBalance, cr_db_flag,
				prodFeeCondition);
		doNothing().when(logServiceMock)
			.doStatementsLog(Mockito.any(RequestInfo.class));
		spilSaleActiveCodeServiceinstance.doStatementLog(req);
		verify(logServiceMock, times(1)).doStatementsLog(Mockito.any(RequestInfo.class));
	}

	@Test
	public void testDoStatementLog_whenIsNotFinancial() throws ServiceException, ParseException {
		double txnAmount = 25.00;
		double txnFee = 0;
		double openingAvailableBalance = 10;
		double openingLedgerBalance = 10;
		double closingBalance = 30;
		String prodFeeCondition = "";
		String cr_db_flag = "NA";
		RequestInfo req = setRequest(txnAmount, txnFee, openingAvailableBalance, openingLedgerBalance, closingBalance, cr_db_flag,
				prodFeeCondition);
		spilSaleActiveCodeServiceinstance.doStatementLog(req);
		verify(logServiceMock, times(0)).doStatementsLog(Mockito.any(RequestInfo.class));
	}

	@Test
	/*
	 * isFinancial = true txnAmount > 0 ProductionFeeCondition != A txnFee =0
	 */
	public void testDoStatementLog_whenIsFinancial1() throws ServiceException, ParseException {
		double txnAmount = 25.00;
		double txnFee = 0;
		double openingAvailableBalance = 10;
		double openingLedgerBalance = 10;
		double closingBalance = 30;
		String prodFeeCondition = "";
		String cr_db_flag = "C";
		doNothing().when(logServiceMock)
			.doStatementsLog(Mockito.any(RequestInfo.class));
		RequestInfo req = setRequest(txnAmount, txnFee, openingAvailableBalance, openingLedgerBalance, closingBalance, cr_db_flag,
				prodFeeCondition);
		spilSaleActiveCodeServiceinstance.doStatementLog(req);
		verify(logServiceMock, times(1)).doStatementsLog(Mockito.any(RequestInfo.class));
	}

	@Test
	/*
	 * isFinancial = true txnAmount > 0 ProductionFeeCondition == A txnFee =0
	 */
	public void testDoStatementLog_whenIsFinancial2() throws ServiceException, ParseException {
		double txnAmount = 25.00;
		double txnFee = 0;
		double openingAvailableBalance = 10;
		double openingLedgerBalance = 10;
		double closingBalance = 30;
		String prodFeeCondition = "A";
		String cr_db_flag = "C";
		RequestInfo req = setRequest(txnAmount, txnFee, openingAvailableBalance, openingLedgerBalance, closingBalance, cr_db_flag,
				prodFeeCondition);
		doNothing().when(logServiceMock)
			.doStatementsLog(Mockito.any(RequestInfo.class));
		spilSaleActiveCodeServiceinstance.doStatementLog(req);
		verify(logServiceMock, times(3)).doStatementsLog(Mockito.any(RequestInfo.class));
	}

	@Test
	/*
	 * isFinancial = true txnAmount > 0 ProductionFeeCondition != A txnFee >0
	 */
	public void testDoStatementLog_whenIsFinancial3() throws ServiceException, ParseException {
		double txnAmount = 25.00;
		double txnFee = 2;
		double openingAvailableBalance = 10;
		double openingLedgerBalance = 10;
		double closingBalance = 30;
		String prodFeeCondition = "";
		String cr_db_flag = "C";
		RequestInfo req = setRequest(txnAmount, txnFee, openingAvailableBalance, openingLedgerBalance, closingBalance, cr_db_flag,
				prodFeeCondition);
		doNothing().when(logServiceMock)
			.doStatementsLog(Mockito.any(RequestInfo.class));
		spilSaleActiveCodeServiceinstance.doStatementLog(req);
		verify(logServiceMock, times(2)).doStatementsLog(Mockito.any(RequestInfo.class));
	}

	public RequestInfo setRequest(double txnAmt, double txn_fee, double openingAvailableBalance, double openingLedgerBalance,
			double closingBalance, String cr_db_flag, String prodFeeCondition) {
		RequestInfo req = new RequestInfo();
		req.setTxnFee(txn_fee);
		req.setTxnAmount(txnAmt);
		req.setOpeningAvailBalance(openingAvailableBalance);
		req.setOpeningLedgerBalance(openingLedgerBalance);
		req.setMaxPurseBalance(100);
		req.setCreditDebitFlg(cr_db_flag);
		req.setClosingBalance(closingBalance);
		req.setProdFeeCondition(prodFeeCondition);
		req.setStoreCity("Atlanta");
		req.setStoreState("GA");
		return req;
	}
}
