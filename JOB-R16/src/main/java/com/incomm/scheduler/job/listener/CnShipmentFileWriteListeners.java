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

import com.incomm.scheduler.dao.CnShipmentFileDao;
import com.incomm.scheduler.model.CnShipmentFile;
import com.incomm.scheduler.utils.JobConstants;

@Component
public class CnShipmentFileWriteListeners implements ItemWriteListener<CnShipmentFile> {

	private final CnShipmentFileDao cnShipmentFileDao;
	
	private static final Logger logger = LogManager.getLogger(CnShipmentFileWriteListeners.class);

	public CnShipmentFileWriteListeners(CnShipmentFileDao cnShipmentFileDao) {
		this.cnShipmentFileDao = cnShipmentFileDao;
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
	public void beforeWrite(List<? extends CnShipmentFile> items) {
		logger.debug("before write data");
	}

	@Override
	public void afterWrite(List<? extends CnShipmentFile> items) {
		if(items!=null && !items.isEmpty()){
			Set<String> cnShipmentFiles=new HashSet<>();
				items.forEach(item->cnShipmentFiles.add(item.getFileName()));
				updateFileStatus(cnShipmentFiles,"COMPLETED",JobConstants.CN_FILE_SUCCESS);
		}
	}

	@Override
	@OnWriteError
	public void onWriteError(Exception exception,
			List<? extends CnShipmentFile> items) {
		Set<String> cnShipmentFiles=new HashSet<>();
		items.forEach(item->cnShipmentFiles.add(item.getFileName()));
		String errorMsg=exception.getCause().getMessage();
		if (errorMsg != null) {
    		errorMsg = errorMsg.substring(0, errorMsg.length() > 500 ? 500 : errorMsg.length());
		}
		updateFileStatus(cnShipmentFiles,"FAILED",errorMsg);
	}
	
	
	public void updateFileStatus(Set<String> cnShipmentFiles,String existStatus,String errorMsg){
		 int result = 0;
		 for(String fileName:cnShipmentFiles){
	    	result = cnShipmentFileDao.chkDuplicateFileStatus(fileName,false);
			if (result > 0) {
        		//Update if the file was already failed
        		logger.debug("updating CN file: {} status: {} with message: {} ",fileName,existStatus,errorMsg);
        		cnShipmentFileDao.updateCNFileStatus(fileName,existStatus,errorMsg);
        	}else {
        		cnShipmentFileDao.insertCNFileStatus(fileName,existStatus,errorMsg);
        	}
		 }
	}
	
	
}
