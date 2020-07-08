package com.incomm.scheduler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.scheduler.config.CurrencyRateFilePollingConfiguration;

@RestController
@RequestMapping("/CurrencyRate")
public class CurrencyRateUploadController {

	@Autowired
	CurrencyRateFilePollingConfiguration currencyRateFilePollingConfiguration;
	
	@RequestMapping(value="/CurrencyRateJobStatus/{message}/",method = RequestMethod.POST)
	public String setConfigTest(@PathVariable String message,@RequestBody String body){
		return currencyRateFilePollingConfiguration.resetPollerReset(message);
		
	}
}
