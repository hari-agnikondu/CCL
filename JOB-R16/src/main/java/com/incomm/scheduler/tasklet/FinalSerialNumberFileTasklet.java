package com.incomm.scheduler.tasklet;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.dao.SerialNumberFileDao;
import com.incomm.scheduler.utils.Util;

public class FinalSerialNumberFileTasklet {
	
	private static final Logger logger = LogManager.getLogger(FinalSerialNumberFileTasklet.class);
	private final SerialNumberFileDao serialNumberFileDao;

	public FinalSerialNumberFileTasklet(SerialNumberFileDao serialNumberFileDao) {
		this.serialNumberFileDao = serialNumberFileDao;
	}
	
	public void serialNumberFileProcCall()
	{
		serialNumberFileDao.serialNumberFileProcCall();
	}


	public void chkDuplicateFile(String directoyPath, String masterTblName, List<String> fileNamesList) {
		logger.info(CCLPConstants.ENTER);
		File rootDir = new File(directoyPath);
		if (fileNamesList != null && !fileNamesList.isEmpty()) {
			for (Iterator<String> iterator = fileNamesList.iterator(); iterator
					.hasNext();) {
				String fileName =  iterator.next();
				int result = serialNumberFileDao.chkDuplicateFiles(fileName, masterTblName,
						" WHERE UPPER(FILE_NAME) = UPPER(?)");
				if (result > 0) {
					Util.moveToArchive(rootDir, fileName);
				} 

			}
		} 
		logger.info(CCLPConstants.EXIT);
	}

}
