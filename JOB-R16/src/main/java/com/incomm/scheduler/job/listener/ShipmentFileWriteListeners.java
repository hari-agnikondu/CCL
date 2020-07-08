package com.incomm.scheduler.job.listener;

/**
 * CnShipmentFileWriteListeners meant for pre/post processing of an DB insert. 
 * author venkateshgaddam
 */
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.annotation.OnProcessError;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.core.annotation.OnWriteError;
import org.springframework.stereotype.Component;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.dao.ShipmentFileDao;
import com.incomm.scheduler.model.ShipmentFile;
import com.incomm.scheduler.utils.JobConstants;

@Component
public class ShipmentFileWriteListeners implements ItemWriteListener<ShipmentFile> {

	private final ShipmentFileDao shipmentFileDao;
	
	private static final Logger logger = LogManager.getLogger(ShipmentFileWriteListeners.class);

	public ShipmentFileWriteListeners(ShipmentFileDao shipmentFileDao) {
		this.shipmentFileDao = shipmentFileDao;
	}

	@OnReadError
	public void onItemReadError() {
		logger.error("error encountered in file reading...");

	}
	@OnWriteError
	public void onitemWriteError() {
		logger.error("error encountered in file writing...");

	}

	@OnProcessError
	public void onitemProcessError() {
		logger.error("error encountered in file processing...");

	}

	@BeforeStep
	public void beforeStep() {
		logger.debug("before starting the step ");

	}

	@Override
	public void beforeWrite(List<? extends ShipmentFile> items) {
		logger.debug("before writing data");
	}

	@Override
	public void afterWrite(List<? extends ShipmentFile> items) {
		if(items!=null && !items.isEmpty()){
			Set<String> shipmentFiles=new HashSet<>();
				items.forEach(item->shipmentFiles.add(item.getFileName()));
				updateFileStatus(shipmentFiles,"COMPLETED",JobConstants.CN_FILE_SUCCESS);
		}
	}

	@Override
	@OnWriteError
	public void onWriteError(Exception exception,
			List<? extends ShipmentFile> items) {
		Set<String> shipmentFiles=new HashSet<>();
		items.forEach(item->shipmentFiles.add(item.getFileName()));
		String errorMsg=exception.getCause().getMessage();
		if (errorMsg != null) {
    		errorMsg = errorMsg.substring(0, errorMsg.length() > 500 ? 500 : errorMsg.length());
		}
		updateFileStatus(shipmentFiles,"FAILED",errorMsg);
	}
	
	
	public void updateFileStatus(Set<String> shipmentFiles,String existStatus,String errorMsg){
		
		logger.info(CCLPConstants.ENTER);
		 int result = 0;
		 for(String fileName:shipmentFiles){
	    	result = shipmentFileDao.chkDuplicateFileStatus(fileName,false);
			if (result > 0) {
        		//Update if the file was already failed
        		logger.info("updating CN file: {} status: {} with message: {} ",fileName,existStatus,errorMsg);
        		shipmentFileDao.updateShipmentFileStatus(fileName,existStatus,errorMsg);
        	}else {
        		shipmentFileDao.insertShipmentFileStatus(fileName,existStatus,errorMsg);
        	}
		 }
		logger.info(CCLPConstants.EXIT);
	}
	
	
}
