package com.incomm.cclp.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

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

import com.incomm.cclp.dao.CurrencyConversionDAO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.impl.SpilCurrencyConversionServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class SpilCurrencyConversionServiceImplTest {

	@Autowired
	@InjectMocks
	SpilCurrencyConversionServiceImpl spilCurrencyConversionServiceImplInstance;

	@Mock
	CurrencyConversionDAO currencyConversionDAOMock;

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

	@Test
	public void getCurrencyRateTestSuccess() {
		String mdmId = "6767";
		String transactionCurrency = "INR";
		String issuingCurrency = "USD";
		String rate = "34";
		when(currencyConversionDAOMock.getConversionRate(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(rate);
		String response = spilCurrencyConversionServiceImplInstance.getCurrencyRate(mdmId, transactionCurrency, issuingCurrency);
		assertEquals(rate, response);
	}

	@Test(expected = ServiceException.class)
	public void getCurrencyRateTestException() {
		String mdmId = "6767";
		String transactionCurrency = "inr";
		String issuingCurrency = "USD";
		when(currencyConversionDAOMock.getConversionRate(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
			.thenThrow(new ServiceException());
		spilCurrencyConversionServiceImplInstance.getCurrencyRate(mdmId, transactionCurrency, issuingCurrency);
	}

}
