package com.incomm.scheduler.jobsteps;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.annotation.OnProcessError;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.core.annotation.OnWriteError;
import org.springframework.stereotype.Component;

import com.incomm.scheduler.dao.CnShipmentFileDao;
import com.incomm.scheduler.utils.JobConstants;

@Component("Listener")
public class BatchStepListeners {
	
private final CnShipmentFileDao cnShipmentFileDao;
	
	private static final Logger logger = LogManager.getLogger(BatchStepListeners.class);
	 
	public BatchStepListeners(CnShipmentFileDao cnShipmentFileDao) {
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
	
	@AfterStep
	public void afterStep(StepExecution stepExecution) {
        String exitCode = stepExecution.getExitStatus().getExitCode();
        String fileName = stepExecution.getExecutionContext().getString(JobConstants.CN_FILE_NAME);
        String errorMsg;
        

        try {
        	errorMsg =  stepExecution.getFailureExceptions().get(0).getCause().getMessage();
        	if (errorMsg != null) {
        		errorMsg = errorMsg.substring(0, errorMsg.length() > 500 ? 500 : errorMsg.length());
			}
        }catch(Exception e) {
        	errorMsg=JobConstants.CN_FILE_SUCCESS;
        }
        
        int result = 0;
    	result = cnShipmentFileDao.chkDuplicateFileStatus(fileName,false);

        if (exitCode.equals(ExitStatus.FAILED.getExitCode())) {
        
        	logger.debug("updating CN file: {} status: {} with message: {} ",fileName,exitCode,errorMsg);
        	logger.info("Some of the records are not valid");
        	if (result > 0) {
        		cnShipmentFileDao.updateCNFileStatus(fileName,exitCode,errorMsg);
        	}else {
        		cnShipmentFileDao.insertCNFileStatus(fileName,exitCode,errorMsg);
        	}
        	      	
            
        }
        else {
        	logger.info("Step execution completed.....");
        	
        	if (result > 0) {
        		//Update if the file was already failed
        		logger.debug("updating CN file: {} status: {} with message: {} ",fileName,exitCode,errorMsg);
        		cnShipmentFileDao.updateCNFileStatus(fileName,exitCode,errorMsg);
        	}else {
        		cnShipmentFileDao.insertCNFileStatus(fileName,exitCode,errorMsg);
        	}
        	
        	
        }
    }

	@BeforeStep
	 public void beforeStep() {
	  logger.debug("before starting the step ");
	   
	 }
	
	
	
}
