package com.incomm.scheduler.jobsteps;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ItemWriter;

import com.incomm.scheduler.dao.ReturnFileDao;
import com.incomm.scheduler.model.ReturnFile;

public class ReturnFileWriter implements ItemWriter<ReturnFile> {

	private final ReturnFileDao returnFileDao;
	
	private static final Logger logger = LogManager.getLogger(ReturnFileWriter.class);

	public ReturnFileWriter(ReturnFileDao returnFileDao) {
		this.returnFileDao = returnFileDao;
	}

	@Override
	public void write(List<? extends ReturnFile> returnFile) throws Exception {
		logger.debug("ReturnFile write");
		returnFileDao.insertReturnFile(returnFile);
	}

}
