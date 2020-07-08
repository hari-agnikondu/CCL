package com.incomm.scheduler.tasklet;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.dao.CnShipmentFileDao;
import com.incomm.scheduler.utils.Util;

public class FinalTasklet {
	
	private static final Logger logger = LogManager.getLogger(FinalTasklet.class);
	
	private final CnShipmentFileDao cnShipmentFileDao;

	public FinalTasklet(CnShipmentFileDao cnShipmentFileDao) {
		this.cnShipmentFileDao = cnShipmentFileDao;
	}
	
	public void makeProcCall()
	{
		cnShipmentFileDao.makeProcCall();
	}
	

	public void chkDuplicateFile(String directoyPath, String masterTblName, List<String> fileNamesList) {
		
		logger.info(CCLPConstants.ENTER);
		File rootDir = new File(directoyPath);
		if (fileNamesList != null && !fileNamesList.isEmpty()) {
			for (Iterator<String> iterator = fileNamesList.iterator(); iterator
					.hasNext();) {
				String fileName =  iterator.next();
				int result = cnShipmentFileDao.chkDuplicateFiles(fileName, masterTblName,
						" WHERE UPPER(FILE_NAME) = UPPER(?)");
				if (result > 0) {
					Util.moveToArchive(rootDir, fileName);
				} 

			}
		} 
		logger.info(CCLPConstants.EXIT);
	}

}
