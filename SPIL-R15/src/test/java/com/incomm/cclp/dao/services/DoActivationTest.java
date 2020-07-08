package com.incomm.cclp.dao.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.dao.AccountPurseDAO;
import com.incomm.cclp.dao.service.AccountPurseDAOService;
import com.incomm.cclp.dao.service.AuthChecksService;
import com.incomm.cclp.dao.service.LogService;
import com.incomm.cclp.dao.service.SequenceService;
import com.incomm.cclp.dao.service.SpilActivationDAOService;
import com.incomm.cclp.dao.service.impl.SpilActivationDAOServiceImpl;
import com.incomm.cclp.dto.RequestInfo;
import com.incomm.cclp.dto.ResponseInfo;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.repos.GenericRepo;

@RunWith(MockitoJUnitRunner.class)
public class DoActivationTest {

	@Mock
	SequenceService sequenceServiceMock;
	@Mock
	GenericRepo genericRepoMock;
	@Mock
	AuthChecksService authServiceCheckMock;
	@Mock
	LogService logServiceMock;
	@Mock
	SpilActivationDAOService spilActivationDaoServiceMock;
	@Mock
	AccountPurseDAO accountPurseDAOMock;
	@Mock
	AccountPurseDAOService accountPurseServiceMock;

	@Autowired
	@InjectMocks
	SpilActivationDAOServiceImpl spilActivationDaoServiceImplInstance;

	@Test
	public void testDoActivationSuccess() {
		RequestInfo requestObj = setRequestObject();

		doNothing().when(authServiceCheckMock)
			.authorizeTransaction(Mockito.any(RequestInfo.class));

		doNothing().when(logServiceMock)
			.doTransactionLog(Mockito.any(RequestInfo.class));
		doNothing().when(logServiceMock)
			.addStatementLog(Mockito.any(RequestInfo.class));
		doNothing().when(accountPurseServiceMock)
			.getAccountPurseDetails(Mockito.any(RequestInfo.class));

		when(sequenceServiceMock.getNextTxnSeqId()).thenReturn(new BigDecimal(123456));
		when(sequenceServiceMock.getNextAuthSeqId()).thenReturn("5345345");

		when(genericRepoMock.updateCurrentCardStatus(Mockito.anyString(), Mockito.anyString())).thenReturn(1);
		when(genericRepoMock.getDamagedCardCount(Mockito.any(), Mockito.anyString())).thenReturn(0);
		when(genericRepoMock.updateActivationStatus(Mockito.anyString(), Mockito.any())).thenReturn(1);
		when(genericRepoMock.updateTopup(Mockito.anyString(), Mockito.anyString())).thenReturn(1);
		when(genericRepoMock.updateInitialLoadBalance(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(1);
		when(genericRepoMock.updateFirstLoadDate(Mockito.anyLong(), Mockito.any())).thenReturn(1);

		ResponseInfo resp = spilActivationDaoServiceImplInstance.doActivation(requestObj);

		assertEquals(ResponseCodes.SUCCESS, resp.getRespCode());
	}

	@Test
	public void testDoTransactionLog_whenCardStatNotConsumed() {
		RequestInfo requestObj = setRequestObject();

		doNothing().when(authServiceCheckMock)
			.authorizeTransaction(Mockito.any(RequestInfo.class));
		doNothing().when(logServiceMock)
			.doTransactionLog(Mockito.any(RequestInfo.class));
		doNothing().when(logServiceMock)
			.addStatementLog(Mockito.any(RequestInfo.class));
		doNothing().when(accountPurseServiceMock)
			.getAccountPurseDetails(Mockito.any(RequestInfo.class));

		when(sequenceServiceMock.getNextTxnSeqId()).thenReturn(new BigDecimal(123456));
		when(sequenceServiceMock.getNextAuthSeqId()).thenReturn("5345345");

		when(genericRepoMock.updateCurrentCardStatus(Mockito.anyString(), Mockito.anyString())).thenReturn(1);
		when(genericRepoMock.getDamagedCardCount(Mockito.any(), Mockito.anyString())).thenReturn(0);
		when(genericRepoMock.updateActivationStatus(Mockito.anyString(), Mockito.any())).thenReturn(1);
		when(genericRepoMock.updateTopup(Mockito.anyString(), Mockito.anyString())).thenReturn(1);
		when(genericRepoMock.updateInitialLoadBalance(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(1);
		when(genericRepoMock.updateFirstLoadDate(Mockito.anyLong(), Mockito.any())).thenReturn(1);

		spilActivationDaoServiceImplInstance.doActivation(requestObj);
		verify(logServiceMock, times(1)).doTransactionLog(Mockito.any(RequestInfo.class));
	}

	@Test
	public void testDoTransactionLog_whenCardStatConsumed() {
		RequestInfo requestObj = setRequestObject();
		requestObj.setCardStatus("17");

		doNothing().when(authServiceCheckMock)
			.authorizeTransaction(Mockito.any(RequestInfo.class));
		doNothing().when(logServiceMock)
			.doTransactionLog(Mockito.any(RequestInfo.class));
		doNothing().when(logServiceMock)
			.addStatementLog(Mockito.any(RequestInfo.class));
		doNothing().when(accountPurseServiceMock)
			.getAccountPurseDetails(Mockito.any(RequestInfo.class));

		when(sequenceServiceMock.getNextTxnSeqId()).thenReturn(new BigDecimal(123456));
		when(sequenceServiceMock.getNextAuthSeqId()).thenReturn("5345345");

		when(genericRepoMock.updateCurrentCardStatus(Mockito.anyString(), Mockito.anyString())).thenReturn(1);
		when(genericRepoMock.getDamagedCardCount(Mockito.any(), Mockito.anyString())).thenReturn(0);
		when(genericRepoMock.updateActivationStatus(Mockito.anyString(), Mockito.any())).thenReturn(1);
		when(genericRepoMock.updateTopup(Mockito.anyString(), Mockito.anyString())).thenReturn(1);
		when(genericRepoMock.updateInitialLoadBalance(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(1);
		when(genericRepoMock.updateFirstLoadDate(Mockito.anyLong(), Mockito.any())).thenReturn(1);

		spilActivationDaoServiceImplInstance.doActivation(requestObj);
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
		req.setSysDate("2019-08-19");
		req.setAccountPurseId(1234);
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
		req.setCardStatus("99");
		req.setChannel("01");
		req.setCreditDebitFlg("C");
		req.setTranCurr("USD");
		req.setErrorMsg("OK");
		req.setFirstTimeTopupFlag("Y");
		req.setMsgType("0200");
		req.setOldCardStatus("98");
		req.setProductFunding("");
		req.setProductId(BigInteger.valueOf(32));
		req.setProxyNumber("003200001317");
		req.setRespCode("R0001");
		req.setRrn("371453425645");
		req.setTxnAmount(10.0);
		req.setTxnCode("098");
		req.setTxnDesc("Sale Active Code");
		req.setTxnReversalFlag("N");
		req.setSerialNumber("345346");
		req.setSysDate("2019-08-19");
		req.setAccountPurseId(1234);
		req.setValueDTO(new ValueDTO());
		return req;
	}

}
