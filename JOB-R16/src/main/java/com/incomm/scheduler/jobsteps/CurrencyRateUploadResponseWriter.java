package com.incomm.scheduler.jobsteps;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ItemWriter;
import org.springframework.transaction.annotation.Transactional;

public class CurrencyRateUploadResponseWriter implements ItemWriter<String> {

	private static final Logger logger = LogManager.getLogger(CurrencyRateUploadResponseWriter.class);

	@Override
	@Transactional
	public void write(List<? extends String> currencyRateResponseFiles) throws Exception {
		logger.debug("Dummy Writer Response...");
		
		
	}
	
}
