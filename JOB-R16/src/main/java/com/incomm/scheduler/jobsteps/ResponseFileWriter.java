package com.incomm.scheduler.jobsteps;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ItemWriter;

import com.incomm.scheduler.dao.ResponseFileDao;
import com.incomm.scheduler.model.ResponseFile;

public class ResponseFileWriter implements ItemWriter<ResponseFile> {

	private final ResponseFileDao responseFileDao;
	
	private static final Logger logger = LogManager.getLogger(ResponseFileWriter.class);

	public ResponseFileWriter(ResponseFileDao responseFileDao) {
		this.responseFileDao = responseFileDao;
	}

	@Override
	public void write(List<? extends ResponseFile> responseFile) throws Exception {
		logger.debug("CnShipmentFile write");
		responseFileDao.insertResponseFile(responseFile);
	}

}
