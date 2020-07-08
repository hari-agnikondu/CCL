package com.incomm.scheduler.tasklet;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.dao.CnShipmentFileDao;
import com.incomm.scheduler.utils.Util;

public class InitialTasklet {

	private final CnShipmentFileDao cnShipmentFileDao;

	// The Logger
	private static final Logger logger = LogManager.getLogger(InitialTasklet.class);


	public InitialTasklet(CnShipmentFileDao cnShipmentFileDao) {
		this.cnShipmentFileDao = cnShipmentFileDao;
	}

	public void truncateFromTemp(String tableName) {
		try {
		cnShipmentFileDao.trauncteDataFromTemp(tableName);
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
	}


	public List<String> chkDuplicateFile(String directoyPath, String masterTblName, List<String> fileNamesList) {

		logger.info(CCLPConstants.ENTER);
		File rootDir = new File(directoyPath);
		List<String> newFileNamesList=new ArrayList<>();
		if (fileNamesList != null && !fileNamesList.isEmpty()) {
			for (Iterator<String> iterator = fileNamesList.iterator(); iterator
					.hasNext();) {
				String fileName =  iterator.next();
		
				int result = cnShipmentFileDao.chkDuplicateFiles(fileName, masterTblName,
						" WHERE UPPER(FILE_NAME) = UPPER(?)");
				
				logger.info("cnFile Name:" + fileName);
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
				logger.info("File Name:" + fileName);
				int result = 0;
				result = cnShipmentFileDao.chkDuplicateFileStatus(fileName,true);
				logger.info("File Name:" + fileName+"  result:"+result);
				if (result > 0) {
					Util.moveToArchive(rootDir, fileName);
				} 
			});

		} 
		logger.info(CCLPConstants.EXIT);
	}

}
