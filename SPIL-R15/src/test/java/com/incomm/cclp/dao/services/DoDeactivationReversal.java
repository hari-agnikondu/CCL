package com.incomm.cclp.dao.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doNothing;
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
import com.incomm.cclp.dao.service.AccountPurseDAOService;
import com.incomm.cclp.dao.service.AuthChecksService;
import com.incomm.cclp.dao.service.LogService;
import com.incomm.cclp.dao.service.SequenceService;
import com.incomm.cclp.dao.service.impl.SpilDeactivationRevDAOServiceImpl;
import com.incomm.cclp.domain.TransactionLogInfo;
import com.incomm.cclp.dto.RequestInfo;
import com.incomm.cclp.dto.ResponseInfo;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.repos.GenericRepo;

@RunWith(MockitoJUnitRunner.class)
public class DoDeactivationReversal {

	@Autowired
	@InjectMocks
	SpilDeactivationRevDAOServiceImpl deactivationRevDaoServiceImplInstance;

	@Mock
	SequenceService sequenceServiceMock;
	@Mock
	AccountPurseDAOService accountPurseServiceMock;
	@Mock
	GenericRepo genericRepoMock;
	@Mock
	AuthChecksService authServiceCheckMock;
	@Mock
	LogService logServiceMock;

	@Test
	public void testDoDeactivationRev_defaultPurse() {
		RequestInfo requestObj = setRequestObject();

		when(sequenceServiceMock.getNextTxnSeqId()).thenReturn(new BigDecimal(123456));
		when(sequenceServiceMock.getNextAuthSeqId()).thenReturn("5345345");
		when(genericRepoMock.getLastSuccessTxn(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
				Mockito.any(), Mockito.any())).thenReturn(new TransactionLogInfo(43, 3, null, "Y", 43, null, null, null,null,BigInteger.valueOf(11254),0,0,0,0));
		when(genericRepoMock.updateCardDetails(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(1);
		when(genericRepoMock.updateTransactionReversalFlag(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),Mockito.anyString())).thenReturn(1);
		when(genericRepoMock.updateInitialLoadBalance(Mockito.any(),Mockito.anyDouble(),Mockito.anyDouble())).thenReturn(1);
		
		doNothing().when(accountPurseServiceMock)
		.getAccountPurseDetails(Mockito.any(RequestInfo.class));
		doNothing().when(authServiceCheckMock)
		.authorizeTransaction(Mockito.any(RequestInfo.class));
		doNothing().when(logServiceMock)
		.addReversalStatementlog(Mockito.any(RequestInfo.class), Mockito.anyList());
		doNothing().when(logServiceMock)
		.doTransactionLog(Mockito.any(RequestInfo.class));
		
		ResponseInfo resp = deactivationRevDaoServiceImplInstance.doDeactivationRev(requestObj);
		
		assertEquals(ResponseCodes.SUCCESS, resp.getRespCode());
	}

	public RequestInfo setRequestObject() {
		RequestInfo req = new RequestInfo();
		req.setAccountId(BigInteger.valueOf(320001317));
		req.setAccountPurseId(323455);
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
		return req;
	}

}
