package com.incomm.scheduler.tasklet;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.dao.BulkTransactionConfigDAO;
import com.incomm.scheduler.dao.BulkTransactionDAO;
import com.incomm.scheduler.utils.Util;

public class InitialBulkTransactionTasklet {

	private final BulkTransactionDAO bulkTransactionDao;
	
	private final BulkTransactionConfigDAO bulkTransactionConfigDao;
	
	// The Logger
	private static final Logger logger = LogManager.getLogger(InitialBulkTransactionTasklet.class);


	public InitialBulkTransactionTasklet(BulkTransactionDAO bulkTransactionDao, BulkTransactionConfigDAO bulkTransactionConfigDao) {
		this.bulkTransactionDao = bulkTransactionDao;
		this.bulkTransactionConfigDao=bulkTransactionConfigDao;
	}

	public void InsertBlkTxnFiles(Map<String, ExecutionContext> fileList) {
		try {
			bulkTransactionDao.InsertBlkTxnFiles(fileList);
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
	}


	public List<String> chkDuplicateFile(String directoyPath, String masterTblName, List<String> fileNamesList, ChunkContext context) {
		
		logger.info(CCLPConstants.ENTER);
		File rootDir = new File(directoyPath);
		List<String> newFileNamesList=new ArrayList<>();
		if (fileNamesList != null && !fileNamesList.isEmpty()) {
			for (Iterator<String> iterator = fileNamesList.iterator(); iterator
					.hasNext();) {
				String fileName =  iterator.next();
				logger.debug("File Name:" + fileName);
				int result = bulkTransactionDao.chkDuplicateFiles(fileName);
			
				if (result > 0) {
					logger.info("Duplicated file");
					Util.RemoveFile(rootDir, fileName);
				} 
				else{
					newFileNamesList.add(fileName);
					Util.moveToTempDir(rootDir, fileName);
				}
			}
		} 
		logger.info(CCLPConstants.EXIT);
		return newFileNamesList;
	}

	public List<Map<String,Object>> getThreadProperties() {
		List<Map<String,Object>> threadProps = bulkTransactionConfigDao.getThreadProperties();
		return threadProps;
	}


}