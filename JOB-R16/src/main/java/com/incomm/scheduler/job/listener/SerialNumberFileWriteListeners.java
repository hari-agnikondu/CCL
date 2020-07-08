package com.incomm.scheduler.job.listener;

/**
 * SerialNumberFileWriteListeners meant for pre/post processing of an DB insert. 
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

import com.incomm.scheduler.dao.SerialNumberFileDao;
import com.incomm.scheduler.model.SerialNumberFile;
import com.incomm.scheduler.utils.JobConstants;

@Component
public class SerialNumberFileWriteListeners implements ItemWriteListener<SerialNumberFile> {

	private final SerialNumberFileDao serialNumberFileDao;
	
	private static final Logger logger = LogManager.getLogger(SerialNumberFileWriteListeners.class);

	public SerialNumberFileWriteListeners(SerialNumberFileDao serialNumberFileDao) {
		this.serialNumberFileDao = serialNumberFileDao;
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
	public void beforeWrite(List<? extends SerialNumberFile> items) {
		logger.debug("before writing data ");
	}

	@Override
	public void afterWrite(List<? extends SerialNumberFile> items) {
		if(items!=null && !items.isEmpty()){
			Set<String> returnFiles=new HashSet<>();
				items.forEach(item->returnFiles.add(item.getFileName()));
				updateFileStatus(returnFiles,"COMPLETED",JobConstants.CN_FILE_SUCCESS);
		}
	}

	@Override
	@OnWriteError
	public void onWriteError(Exception exception,
			List<? extends SerialNumberFile> items) {
		Set<String> returnFiles=new HashSet<>();
		items.forEach(item->returnFiles.add(item.getFileName()));
		String errorMsg=exception.getCause().getMessage();
		if (errorMsg != null) {
    		errorMsg = errorMsg.substring(0, errorMsg.length() > 500 ? 500 : errorMsg.length());
		}
		updateFileStatus(returnFiles,"FAILED",errorMsg);
	}
	
	
	public void updateFileStatus(Set<String> returnFiles,String existStatus,String errorMsg){
		 int result = 0;
		 for(String fileName:returnFiles){
	    	result = serialNumberFileDao.chkDuplicateFileStatus(fileName,false);
			if (result > 0) {
        		//Update if the file was already failed
        		logger.debug("updating CN file: {} status: {} with message: {} ",fileName,existStatus,errorMsg);
        		serialNumberFileDao.updateSerialNumberFileStatus(fileName, existStatus, errorMsg);
        	}else {
        		serialNumberFileDao.insertSerialNumberFileStatus(fileName, existStatus, errorMsg);
        	}
		 }
	}
	
	
}
