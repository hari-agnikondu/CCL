package com.incomm.scheduler.jobsteps;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ItemWriter;
import org.springframework.transaction.annotation.Transactional;

import com.incomm.scheduler.dao.CnShipmentFileDao;
import com.incomm.scheduler.model.CnShipmentFile;

public class BatchFileWriter implements ItemWriter<CnShipmentFile> {

	private final CnShipmentFileDao cnShipmentFileDao;
	
	private static final Logger logger = LogManager.getLogger(BatchFileWriter.class);

	public BatchFileWriter(CnShipmentFileDao cnShipmentFileDao) {
		this.cnShipmentFileDao = cnShipmentFileDao;
	}

	@Override
	@Transactional
	public void write(List<? extends CnShipmentFile> cnShipmentFile) throws Exception {
		logger.debug("CnShipmentFile write starts");
		cnShipmentFileDao.insert(cnShipmentFile);
	}
	
}
