package com.incomm.scheduler.jobsteps;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ItemWriter;

import com.incomm.scheduler.dao.CurrencyRateUploadDAO;
import com.incomm.scheduler.model.CurrencyRateRequestFile;

public class CurrencyRateUploadRequestWriter  implements ItemWriter<CurrencyRateRequestFile>{


	private final CurrencyRateUploadDAO currencyRateUploadDAO;
	
	private static final Logger logger = LogManager.getLogger(CurrencyRateUploadRequestWriter.class);

	public CurrencyRateUploadRequestWriter(CurrencyRateUploadDAO currencyRateUploadDAO) {
		this.currencyRateUploadDAO = currencyRateUploadDAO;
	}

	@Override
	public void write(List<? extends CurrencyRateRequestFile> currencyRateRequestFiles) throws Exception {
		logger.debug("writing request..");
		currencyRateUploadDAO.insertRequest(currencyRateRequestFiles);
		
		
	}

}
