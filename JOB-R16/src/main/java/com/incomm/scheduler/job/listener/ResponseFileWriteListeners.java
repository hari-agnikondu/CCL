package com.incomm.scheduler.job.listener;

/**
 * ResponseFileWriteListeners meant for pre/post processing of an DB insert. 
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
import com.incomm.scheduler.dao.ResponseFileDao;
import com.incomm.scheduler.model.ResponseFile;
import com.incomm.scheduler.utils.JobConstants;

@Component
public class ResponseFileWriteListeners implements ItemWriteListener<ResponseFile> {

	private final ResponseFileDao responseFileDao;
	
	private static final Logger logger = LogManager.getLogger(ResponseFileWriteListeners.class);

	public ResponseFileWriteListeners(ResponseFileDao responseFileDao) {
		this.responseFileDao = responseFileDao;
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
		logger.info("before starting the step ");

	}

	@Override
	public void beforeWrite(List<? extends ResponseFile> items) {
		logger.info("before write data");
	}

	@Override
	public void afterWrite(List<? extends ResponseFile> items) {
		if(items!=null && !items.isEmpty()){
			Set<String> responseFiles=new HashSet<>();
				items.forEach(item->responseFiles.add(item.getFileName()));
				updateFileStatus(responseFiles,"COMPLETED",JobConstants.CN_FILE_SUCCESS);
		}
	}

	@Override
	@OnWriteError
	public void onWriteError(Exception exception,
			List<? extends ResponseFile> items) {
		Set<String> responseFiles=new HashSet<>();
		items.forEach(item->responseFiles.add(item.getFileName()));
		String errorMsg=exception.getCause().getMessage();
		if (errorMsg != null) {
    		errorMsg = errorMsg.substring(0, errorMsg.length() > 500 ? 500 : errorMsg.length());
		}
		updateFileStatus(responseFiles,"FAILED",errorMsg);
	}
	
	
	public void updateFileStatus(Set<String> responseFiles,String existStatus,String errorMsg){
		
		logger.info(CCLPConstants.ENTER);
		int result = 0;
		 for(String fileName:responseFiles){
	    	result = responseFileDao.chkDuplicateFileStatus(fileName,false);
			if (result > 0) {
        		//Update if the file was already failed
        		logger.info("updating CN file: {} status: {} with message: {} ",fileName,existStatus,errorMsg);
        		responseFileDao.updateResponseFileStatus(fileName,existStatus,errorMsg);
        	}else {
        		responseFileDao.insertResponseFileStatus(fileName,existStatus,errorMsg);
        	}
		 }
		 logger.info(CCLPConstants.EXIT);
	}
	
	
}
