package com.incomm.scheduler.jobsteps;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ItemWriter;

import com.incomm.scheduler.dao.SerialNumberFileDao;
import com.incomm.scheduler.model.SerialNumberFile;

public class SerialNumberFileWriter implements ItemWriter<SerialNumberFile> {

	private final SerialNumberFileDao serialNumberFileDao;
	
	private static final Logger logger = LogManager.getLogger(SerialNumberFileWriter.class);

	public SerialNumberFileWriter(SerialNumberFileDao serialNumberFileDao) {
		this.serialNumberFileDao = serialNumberFileDao;
	}

	@Override
	public void write(List<? extends SerialNumberFile> serialNumberFile) throws Exception {
		logger.debug("SerialNumberFile write");
		serialNumberFileDao.insertSerialNumberFile(serialNumberFile);
	}

}
