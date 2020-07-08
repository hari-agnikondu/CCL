package com.incomm.scheduler.tasklet;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.constants.FSAPIConstants;
import com.incomm.scheduler.dao.BulkTransactionConfigDAO;
import com.incomm.scheduler.dao.CurrencyRateUploadDAO;
import com.incomm.scheduler.utils.Util;

public class InitialCurrencyRateUploadTasklet {

	private final CurrencyRateUploadDAO currencyRateUploadDao;
	
	private final BulkTransactionConfigDAO bulkTransactionConfigDao;
	
	// The Logger
	private static final Logger logger = LogManager.getLogger(InitialCurrencyRateUploadTasklet.class);


	public InitialCurrencyRateUploadTasklet(CurrencyRateUploadDAO currencyRateUploadDao, BulkTransactionConfigDAO bulkTransactionConfigDao) {
		this.currencyRateUploadDao = currencyRateUploadDao;
		this.bulkTransactionConfigDao=bulkTransactionConfigDao;
	}


	public List<String> chkDuplicateFile(String directoyPath, List<String> fileNamesList) {
		
		logger.info(CCLPConstants.ENTER);
		File rootDir = new File(directoyPath);
		List<String> newFileNamesList=new ArrayList<>();
		if (fileNamesList != null && !fileNamesList.isEmpty()) {
			for (Iterator<String> iterator = fileNamesList.iterator(); iterator
					.hasNext();) {
				String fileName =  iterator.next();
				logger.info("File Name:" + fileName);
				int result = currencyRateUploadDao.chkDuplicateFiles(fileName);
				logger.debug("result" + result);
				if (result > 0) {
					logger.info("Duplicate File found");
					Util.RemoveFile(rootDir, fileName);
				} 
				else{
					if(chkFileFormat(directoyPath,fileName)) {
					newFileNamesList.add(fileName);
					Util.moveToTempDir(rootDir, fileName);
					}
				}
			}
		} 
		logger.info(CCLPConstants.EXIT);
		return newFileNamesList;
	}

	public List<Map<String,Object>> getThreadProperties() {
		return bulkTransactionConfigDao.getThreadProperties();
	}

	public boolean chkFileFormat(String directoryPath, String fileName) {
		
		logger.info(CCLPConstants.ENTER);
		String fileFormat = FSAPIConstants.CURRENCY_RATE_PATTERN_DATE;
		File rootDir = new File(directoryPath);
	
				String mdmId = fileName.split("_")[0];
				String fileDate = fileName.split("_")[3].split(".csv")[0];
				if(!fileDate.matches(fileFormat)) {
					logger.info("File format validation failed, fileName: "+fileName);
					currencyRateUploadDao.updateDuplicateFileStatus(fileName,"Invalid File Name format/TimeStamp","error");
					Util.moveToArchive(rootDir, fileName);
					return false;
					
				}else if(bulkTransactionConfigDao.verifyMdmId(mdmId) < 1){
					logger.info("File format validation failed: Mdm id not present in master fileName: "+fileName);
					currencyRateUploadDao.updateDuplicateFileStatus(fileName,"Mdm id not present in master","error");
					Util.moveToArchive(rootDir, fileName);
					return false;
				}else {
					return true;
				}
			
	}
}
