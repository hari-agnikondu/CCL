package com.incomm.scheduler.job.listener;

/**
 * ReturnFileWriteListeners meant for pre/post processing of an DB insert. 
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
import com.incomm.scheduler.dao.ReturnFileDao;
import com.incomm.scheduler.model.ReturnFile;
import com.incomm.scheduler.utils.JobConstants;

@Component
public class ReturnFileWriteListeners implements ItemWriteListener<ReturnFile> {

	private final ReturnFileDao returnFileDao;
	
	private static final Logger logger = LogManager.getLogger(ReturnFileWriteListeners.class);

	public ReturnFileWriteListeners(ReturnFileDao returnFileDao) {
		this.returnFileDao = returnFileDao;
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
	public void beforeWrite(List<? extends ReturnFile> items) {
		logger.debug("before write data ");
	}

	@Override
	public void afterWrite(List<? extends ReturnFile> items) {
		if(items!=null && !items.isEmpty()){
			Set<String> returnFiles=new HashSet<>();
				items.forEach(item->returnFiles.add(item.getFileName()));
				updateFileStatus(returnFiles,"COMPLETED",JobConstants.CN_FILE_SUCCESS);
		}
	}

	@Override
	@OnWriteError
	public void onWriteError(Exception exception,
			List<? extends ReturnFile> items) {
		Set<String> returnFiles=new HashSet<>();
		items.forEach(item->returnFiles.add(item.getFileName()));
		String errorMsg=exception.getCause().getMessage();
		if (errorMsg != null) {
    		errorMsg = errorMsg.substring(0, errorMsg.length() > 500 ? 500 : errorMsg.length());
		}
		updateFileStatus(returnFiles,"FAILED",errorMsg);
	}
	
	
	public void updateFileStatus(Set<String> returnFiles,String existStatus,String errorMsg){
		
		logger.info(CCLPConstants.ENTER);
		int result = 0;
		 for(String fileName:returnFiles){
			
	    	result = returnFileDao.chkDuplicateFileStatus(fileName,false);
			if (result > 0) {
        		//Update if the file was already failed
        		logger.info("updating CN file: {} status: {} with message: {} ",fileName,existStatus,errorMsg);
        		returnFileDao.updateReturnFileStatus(fileName, existStatus, errorMsg);
        	}else {
        		returnFileDao.insertReturnFileStatus(fileName, existStatus, errorMsg);
        	}
		 }
		logger.info(CCLPConstants.EXIT);
	}
	
	
}
