package com.incomm.cclp.dao.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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

import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.dao.service.AccountPurseDAOService;
import com.incomm.cclp.dao.service.AuthChecksService;
import com.incomm.cclp.dao.service.LogService;
import com.incomm.cclp.dao.service.SequenceService;
import com.incomm.cclp.dao.service.SpilSaleActiveCodeDAOService;
import com.incomm.cclp.dao.service.impl.SpilActivationRevDAOServiceImpl;
import com.incomm.cclp.domain.CardInfo;
import com.incomm.cclp.domain.StatementsLogInfo;
import com.incomm.cclp.domain.TransactionLogInfo;
import com.incomm.cclp.dto.RequestInfo;
import com.incomm.cclp.dto.ResponseInfo;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.repos.GenericRepo;

@RunWith(MockitoJUnitRunner.class)
public class DoActivationRevTest {

	@Mock
	RequestInfo requestInfoMock;

	@Mock
	SequenceService sequenceServiceMock;

	@Mock
	GenericRepo genericRepoMock;

	@Mock
	SpilSaleActiveCodeDAOService spilSaleActiveCodeServiceMock;

	@Mock
	AuthChecksService authServiceCheckMock;

	@Mock
	LogService logServiceMock;
	@Mock
	AccountPurseDAOService accountPurseServiceMock;

	@Autowired
	@InjectMocks
	SpilActivationRevDAOServiceImpl activationRevDaoServiceImplInstance;

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

//	@Test
	public void testDoActivationRev() {
		RequestInfo requestObj = setRequestObject();

		CardInfo cardinfo = new CardInfo(null, null);
		when(sequenceServiceMock.getNextTxnSeqId()).thenReturn(new BigDecimal(123456));
		when(sequenceServiceMock.getNextAuthSeqId()).thenReturn("5345345");

		when(genericRepoMock.getLastSuccessPurseTxn(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
				Mockito.any(), Mockito.any(), Mockito.anyLong())).thenReturn(
						new TransactionLogInfo(43, 3, null, "Y", 43, null, null, null, null, BigInteger.valueOf(11254), 10, 10, 0, 0));
		when(genericRepoMock.getLastFeeTransactions(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
			.thenReturn(getLastFeeTransaction(1));

		when(genericRepoMock.updateActivationDateCardStatus(Mockito.anyString(), Mockito.anyString())).thenReturn(1);
		when(genericRepoMock.updateTopup(Mockito.anyString(), Mockito.anyString())).thenReturn(1);
		when(genericRepoMock.updateInitialLoadBalance(Mockito.any())).thenReturn(1);
		when(genericRepoMock.updateFirstLoadDatetoNull(Mockito.any())).thenReturn(1);

		doReturn(cardinfo).when(genericRepoMock)
			.getReplacedCardStatus(Mockito.any(), Mockito.anyString());

		doNothing().when(authServiceCheckMock)
			.authorizeTransaction(Mockito.any(RequestInfo.class));
		doNothing().when(logServiceMock)
			.doTransactionLog(Mockito.any(RequestInfo.class));
		doNothing().when(accountPurseServiceMock)
			.getAccountPurseDetails(Mockito.any(RequestInfo.class));
		doNothing().when(logServiceMock)
			.addReversalStatementlog(Mockito.any(RequestInfo.class), Mockito.anyList());

		ResponseInfo resp = activationRevDaoServiceImplInstance.doActivationRev(requestObj);
		assertEquals(ResponseCodes.SUCCESS, resp.getRespCode());
	}

	@Test
	public void testDoTransactionLog_whenCardStatNotConsumed() {
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
			.doTransactionLog(Mockito.any(RequestInfo.class));

		activationRevDaoServiceImplInstance.doTransactionLogs(req);
		verify(logServiceMock, times(1)).doTransactionLog(Mockito.any(RequestInfo.class));
	}

	@Test
	public void testDoTransactionLog_whenCardStatConsumed() {
		double txnAmount = 25.00;
		double txnFee = 2;
		double openingAvailableBalance = 10;
		double openingLedgerBalance = 10;
		double closingBalance = 30;
		String prodFeeCondition = "";
		String cr_db_flag = "C";
		RequestInfo req = setRequest(txnAmount, txnFee, openingAvailableBalance, openingLedgerBalance, closingBalance, cr_db_flag,
				prodFeeCondition);
		req.setOldCardStatus("17");
		doNothing().when(logServiceMock)
			.doTransactionLog(Mockito.any(RequestInfo.class));

		activationRevDaoServiceImplInstance.doTransactionLogs(req);
		verify(logServiceMock, times(2)).doTransactionLog(Mockito.any(RequestInfo.class));
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
		req.setPurAuthReq(null);
		return req;
	}

	public RequestInfo setRequestObject() {
		RequestInfo req = new RequestInfo();
		req.setAccountId(BigInteger.valueOf(320001317));
		req.setAccountNumber("A00032000000001317");
		req.setCardLast4digits("0490");
		req.setCardNum("7788778800000490");
		req.setCardNumEncr("37AF3B8C67B90DEEFFD3F7587707F89F2CF1D1DE68A2B5AFCC29C929D0388999");
		req.setCardNumHash("/MzNUVQExTY9KlGVvoCctl1t9zhHKEHv3kTcEMQdBFg=");
		req.setCardStatus("1");
		req.setChannel("01");
		req.setCreditDebitFlg("C");
		req.setTranCurr("USD");
		req.setErrorMsg("OK");
		req.setFirstTimeTopupFlag("Y");
		req.setMsgType("0200");
		req.setOldCardStatus("99");
		req.setProductFunding("");
		req.setProductId(BigInteger.valueOf(32));
		req.setProxyNumber("003200001317");
		req.setRespCode("R0001");
		req.setRrn("371453425645");
		req.setTxnAmount(10.0);
		req.setTxnCode("002");
		req.setTxnDesc("Activation Reversal");
		req.setTxnReversalFlag("N");
		req.setValueDTO(new ValueDTO());
		req.setPurAuthReq(null);
		return req;
	}

	public List<StatementsLogInfo> getLastFeeTransaction(int feeCount) {
		ArrayList<StatementsLogInfo> al = new ArrayList<StatementsLogInfo>();
		switch (feeCount) {
		case 1:
			al.add(new StatementsLogInfo("Activation Fee", 2, BigInteger.valueOf(11254), 25L));
			break;
		case 2:
			al.add(new StatementsLogInfo("Activation Fee", 2, BigInteger.valueOf(11254), 25L));
			al.add(new StatementsLogInfo("Activation Percent Fee", 2, BigInteger.valueOf(11254), 25L));
			break;
		default:
			break;
		}
		return al;
	}
}