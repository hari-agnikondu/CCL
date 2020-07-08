package com.incomm.cclp.dao.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.incomm.cclp.dao.service.SequenceService;
import com.incomm.cclp.dao.service.impl.LogServiceImpl;
import com.incomm.cclp.domain.StatementsLog;
import com.incomm.cclp.domain.StatementsLogInfo;
import com.incomm.cclp.dto.RequestInfo;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.repos.StatementsLogRepository;

@RunWith(MockitoJUnitRunner.class)
public class LogServiceTest {

	@Mock
	StatementsLogRepository statementsLogRepositoryMock;

	@Mock
	SequenceService sequenceServiceMock;

	@Autowired
	@InjectMocks
	LogServiceImpl logServiceImplInstance;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected = ServiceException.class)
	public void doStatementLog_withNullInput() throws ServiceException, ParseException {
		RequestInfo req = new RequestInfo();
		req.setValueDTO(new ValueDTO());
		when(sequenceServiceMock.getNextRecordSeqId()).thenReturn(new BigDecimal(34636));
		logServiceImplInstance.doStatementsLog(req);
	}

	@Test
	public void doStatementLog_withvalidInput() throws ServiceException, ParseException {
		RequestInfo req = new RequestInfo();
		req.setValueDTO(setValueDto());
		when(sequenceServiceMock.getNextRecordSeqId()).thenReturn(new BigDecimal(34636));
		logServiceImplInstance.doStatementsLog(req);
		verify(statementsLogRepositoryMock, times(1)).save(Mockito.any(StatementsLog.class));
	}

	@Test
	public void testGeneratestatementLog() {
		double txnAmount = 25.00;
		double txnFee = 2.50;
		double openingAvailableBalance = 10;
		double openingLedgerBalance = 10;
		double closingBalance = 10;
		String prodFeeCondition = "";
		String cr_db_flag = "C";
		double availClosingBal = 20;
		double availOpeningBal = 0;
		RequestInfo req = setRequest(txnAmount, txnFee, openingAvailableBalance, openingLedgerBalance, closingBalance, cr_db_flag,
				prodFeeCondition);
		logServiceImplInstance.generateStatementLogObj(req, openingLedgerBalance, closingBalance, txnAmount, cr_db_flag, "N",
				"Sale Active Code Reversal Fee", availClosingBal, availOpeningBal);
		// Why was 35
		assertEquals(10, req.getClosingBalance(), 0);
	}

	@Test
	/*
	 * isFinancial = true ProductionFeeCondition != A txnFee >0
	 */
	public void testDoStatementLog_whenIsFinancial3() {
		double txnAmount = 25.00;
		double txnFee = 2;
		double openingAvailableBalance = 10;
		double openingLedgerBalance = 10;
		double closingBalance = 30;
		String prodFeeCondition = "";
		String cr_db_flag = "C";

		RequestInfo req = setRequest(txnAmount, txnFee, openingAvailableBalance, openingLedgerBalance, closingBalance, cr_db_flag,
				prodFeeCondition);
		req.setValueDTO(setValueDto());

		when(statementsLogRepositoryMock.save(Mockito.any(StatementsLog.class))).thenReturn(new StatementsLog());
		when(sequenceServiceMock.getNextRecordSeqId()).thenReturn(new BigDecimal(34636));

		List<StatementsLogInfo> stmtlog = getLastFeeTransaction();
		logServiceImplInstance.addReversalStatementlog(req, stmtlog);
		verify(statementsLogRepositoryMock, times(3)).save(Mockito.any(StatementsLog.class));
	}

	@Test
	public void testDoStatementLog_whenIsFinancial() {
		double txnAmount = 25.00;
		double txnFee = 0;
		double openingAvailableBalance = 10;
		double openingLedgerBalance = 10;
		double closingBalance = 30;
		String prodFeeCondition = "";
		String cr_db_flag = "D";
		List<StatementsLogInfo> stmtlog = null;
		RequestInfo req = setRequest(txnAmount, txnFee, openingAvailableBalance, openingLedgerBalance, closingBalance, cr_db_flag,
				prodFeeCondition);
		req.setValueDTO(setValueDto());

		when(statementsLogRepositoryMock.save(Mockito.any(StatementsLog.class))).thenReturn(new StatementsLog());
		when(sequenceServiceMock.getNextRecordSeqId()).thenReturn(new BigDecimal(34636));

		logServiceImplInstance.addReversalStatementlog(req, stmtlog);

		verify(statementsLogRepositoryMock, times(1)).save(Mockito.any(StatementsLog.class));
	}

	@Test
	public void testDoStatementLog_whenIsNotFinancial() {
		double txnAmount = 25.00;
		double txnFee = 0;
		double openingAvailableBalance = 10;
		double openingLedgerBalance = 10;
		double closingBalance = 30;
		String prodFeeCondition = "";
		String cr_db_flag = "NA";
		List<StatementsLogInfo> stmtlog = null;
		RequestInfo req = setRequest(txnAmount, txnFee, openingAvailableBalance, openingLedgerBalance, closingBalance, cr_db_flag,
				prodFeeCondition);

		logServiceImplInstance.addReversalStatementlog(req, stmtlog);

		verify(statementsLogRepositoryMock, times(0)).save(Mockito.any(StatementsLog.class));
	}

	@Test
	/*
	 * isFinancial = true ProductionFeeCondition != A txnFee =0
	 */
	public void testDoStatementLog_whenIsFinancial1() {
		double txnAmount = 25.00;
		double txnFee = 0;
		double openingAvailableBalance = 10;
		double openingLedgerBalance = 10;
		double closingBalance = 30;
		String prodFeeCondition = "";
		String cr_db_flag = "C";

		when(statementsLogRepositoryMock.save(Mockito.any(StatementsLog.class))).thenReturn(new StatementsLog());
		when(sequenceServiceMock.getNextRecordSeqId()).thenReturn(new BigDecimal(34636));

		List<StatementsLogInfo> stmtlog = null;
		RequestInfo req = setRequest(txnAmount, txnFee, openingAvailableBalance, openingLedgerBalance, closingBalance, cr_db_flag,
				prodFeeCondition);
		req.setValueDTO(setValueDto());
		logServiceImplInstance.addReversalStatementlog(req, stmtlog);

		verify(statementsLogRepositoryMock, times(1)).save(Mockito.any(StatementsLog.class));
	}

	@Test
	/*
	 * isFinancial = true ProductionFeeCondition == A txnFee =0
	 */
	public void testDoStatementLog_whenIsFinancial2() {
		double txnAmount = 25.00;
		double txnFee = 0;
		double openingAvailableBalance = 10;
		double openingLedgerBalance = 10;
		double closingBalance = 30;
		String prodFeeCondition = "A";
		String cr_db_flag = "C";
		List<StatementsLogInfo> stmtlog = getLastFeeTransaction();
		RequestInfo req = setRequest(txnAmount, txnFee, openingAvailableBalance, openingLedgerBalance, closingBalance, cr_db_flag,
				prodFeeCondition);
		req.setValueDTO(setValueDto());

		when(statementsLogRepositoryMock.save(Mockito.any(StatementsLog.class))).thenReturn(new StatementsLog());
		when(sequenceServiceMock.getNextRecordSeqId()).thenReturn(new BigDecimal(34636));

		logServiceImplInstance.addReversalStatementlog(req, stmtlog);

		verify(statementsLogRepositoryMock, times(3)).save(Mockito.any(StatementsLog.class));
	}

	public List<StatementsLogInfo> getLastFeeTransaction() {
		ArrayList<StatementsLogInfo> al = new ArrayList<StatementsLogInfo>();

		StatementsLogInfo stmtLogInfo1 = new StatementsLogInfo("Activation Fee", 2, BigInteger.valueOf(11254), 5345534L);
		al.add(stmtLogInfo1);

		StatementsLogInfo stmtLogInfo2 = new StatementsLogInfo("Activation Percent Fee", 2, BigInteger.valueOf(11254), 5345534L);
		al.add(stmtLogInfo2);

		return al;
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
		req.setTxnCode("002");
		req.setOldCardStatus("1");
		req.setStoreCity("Atlanta");
		req.setStoreState("GA");
		return req;
	}

	private ValueDTO setValueDto() {
		ValueDTO valueDto = new ValueDTO();
		Map<String, Object> prodAtt = new HashMap<>();
		prodAtt.put("cutOverTime", "00:30:00");
		Map<String, Map<String, Object>> product = new HashMap<>();
		product.put("Product", prodAtt);
		valueDto.setProductAttributes(product);
		return valueDto;
	}
}
