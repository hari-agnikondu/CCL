package com.incomm.scheduler.jobsteps;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ItemWriter;
import org.springframework.transaction.annotation.Transactional;

import com.incomm.scheduler.dao.ShipmentFileDao;
import com.incomm.scheduler.model.ShipmentFile;

public class ShipmentFileWriter implements ItemWriter<ShipmentFile> {

	private final ShipmentFileDao shipmentFileDao;
	
	private static final Logger logger = LogManager.getLogger(ShipmentFileWriter.class);

	public ShipmentFileWriter(ShipmentFileDao shipmentFileDao) {
		this.shipmentFileDao = shipmentFileDao;
	}

	@Override
	@Transactional
	public void write(List<? extends ShipmentFile> shipmentFile) throws Exception {
		logger.info("CnShipmentFile write starts");
		shipmentFileDao.insertShipmentFile(shipmentFile);
	}
	
}
