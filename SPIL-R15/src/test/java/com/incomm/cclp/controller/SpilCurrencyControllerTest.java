package com.incomm.cclp.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.incomm.cclp.service.SpilCurrencyConversionService;

@RunWith(MockitoJUnitRunner.class)
public class SpilCurrencyControllerTest {

	@Mock
	SpilCurrencyConversionService spilCurrencyConversionServiceMock;

	@Autowired
	@InjectMocks
	SpilCurrencyController spilCurrencyControllerInstance;

	@Test
	public void getCurrencyRateTest() {
		String mdmId = "6767";
		String transactionCurrency = "INR";
		String issuingCurrency = "USD";
		String rate = "34";
		String exchangeRate = "{\"exchangeRate\":34}";
		when(spilCurrencyConversionServiceMock.getCurrencyRate(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
			.thenReturn(rate);
		String respnseMsg = spilCurrencyControllerInstance.getCurrencyRate(mdmId, transactionCurrency, issuingCurrency);

		assertEquals(this.removeNewLine(respnseMsg), exchangeRate);
	}

	private String removeNewLine(String text) {
		return text.replace("\n", "")
			.replace("\r", "");
	}

}
