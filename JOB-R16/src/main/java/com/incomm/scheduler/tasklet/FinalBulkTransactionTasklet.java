package com.incomm.scheduler.tasklet;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.dao.BulkTransactionDAO;
import com.incomm.scheduler.utils.Util;

public class FinalBulkTransactionTasklet {
	
	private static final Logger logger = LogManager.getLogger(FinalBulkTransactionTasklet.class);
	private final BulkTransactionDAO bulkTransactionDao;

	public FinalBulkTransactionTasklet(BulkTransactionDAO bulkTransactionDao) {
		this.bulkTransactionDao = bulkTransactionDao;
	}
	

	public void chkDuplicateFile(String directoyPath,String bulkpath, String masterTblName, List<String> fileNamesList) {

		logger.info(CCLPConstants.ENTER);
		File rootDir = new File(directoyPath);
		File delDir = new File(bulkpath);
		if (!CollectionUtils.isEmpty(fileNamesList)) {
			for (String fileName : fileNamesList) {
				int result = bulkTransactionDao.chkDuplicateFiles(fileName);
				if (result > 0) {
					Util.moveFromTempToArchive(rootDir,delDir,fileName);
				} 

			}
		}
		logger.info(CCLPConstants.EXIT);
	}


	public void updateFileStatus(Map<String, String> fileList1) {
	
		for(Entry<String, String> file : fileList1.entrySet()) {
			bulkTransactionDao.updateFileStatus(file.getValue());
		}
		
	}

}
