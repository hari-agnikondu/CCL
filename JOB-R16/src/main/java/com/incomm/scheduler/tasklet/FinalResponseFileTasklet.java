package com.incomm.scheduler.tasklet;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.dao.ResponseFileDao;
import com.incomm.scheduler.utils.Util;

public class FinalResponseFileTasklet {
	
	private static final Logger logger = LogManager.getLogger(FinalResponseFileTasklet.class);
	
	private final ResponseFileDao responseFileDao;

	public FinalResponseFileTasklet(ResponseFileDao responseFileDao) {
		this.responseFileDao = responseFileDao;
	}

	public void responseFileProcCall()
	{
		responseFileDao.responseFileProcCall();
	}


	public void chkDuplicateFile(String directoyPath, String masterTblName, List<String> fileNamesList) {

		logger.info(CCLPConstants.ENTER);
		File rootDir = new File(directoyPath);
		if (fileNamesList != null && !fileNamesList.isEmpty()) {
			for (Iterator<String> iterator = fileNamesList.iterator(); iterator
					.hasNext();) {
				String fileName =  iterator.next();
				int result = responseFileDao.chkDuplicateFiles(fileName, masterTblName,
						" WHERE UPPER(FILE_NAME) = UPPER(?)");
				logger.info("Filename and result {}  :  {}",fileName,result);
				if (result > 0) {
					Util.moveToArchive(rootDir, fileName);
				} 

			}
		} 
		logger.info(CCLPConstants.EXIT);
	}

}
