package com.incomm.scheduler.tasklet;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.dao.SerialNumberFileDao;
import com.incomm.scheduler.utils.Util;

public class InitialSerialNumberFileTasklet {

	private final SerialNumberFileDao serialNumberFileDao;

	// The Logger
	private static final Logger logger = LogManager.getLogger(InitialSerialNumberFileTasklet.class);


	public InitialSerialNumberFileTasklet(SerialNumberFileDao serialNumberFileDao) {
		this.serialNumberFileDao = serialNumberFileDao;
	}

	public void truncateFromTemp(String tableName) {
		serialNumberFileDao.truncateDataFromTemp(tableName);
	}


	public List<String> chkDuplicateFile(String directoyPath, String masterTblName, List<String> fileNamesList) {

		logger.info(CCLPConstants.ENTER);
		File rootDir = new File(directoyPath);
		List<String> newFileNamesList=new ArrayList<>();
		if (fileNamesList != null && !fileNamesList.isEmpty()) {
			for (Iterator<String> iterator = fileNamesList.iterator(); iterator
					.hasNext();) {
				String fileName =  iterator.next();
				logger.debug("Serial File Name:" + fileName);
				int result = serialNumberFileDao.chkDuplicateFiles(fileName, masterTblName,
						" WHERE UPPER(FILE_NAME) = UPPER(?)");

				if (result > 0) {
					logger.info("Duplicated file");
					Util.moveToArchive(rootDir, fileName);
				} 
				else{
					newFileNamesList.add(fileName);
				}
			}
		} 
		logger.info(CCLPConstants.EXIT);
		return newFileNamesList;
	}

	public void chkDuplicateFileStatus(String directoyPath, List<String> fileNamesList) {
		
		logger.info(CCLPConstants.ENTER);
		File rootDir = new File(directoyPath);
		if (fileNamesList != null && !fileNamesList.isEmpty()) {
			fileNamesList.forEach(fileName->{
				
				int result = 0;
				result = serialNumberFileDao.chkDuplicateFileStatus(fileName,true);
				logger.info("File Name:" + fileName+"  result:"+result);
				if (result > 0) {
					Util.moveToArchive(rootDir, fileName);
				} 
			});

		} 
		logger.info(CCLPConstants.EXIT);
	}

}
