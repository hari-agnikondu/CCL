package com.incomm.scheduler.tasklet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.dao.ReturnFileDao;
import com.incomm.scheduler.utils.JobConstants;
import com.incomm.scheduler.utils.Util;

public class InitialReturnFileTasklet {

	private final ReturnFileDao returnFileDao;

	// The Logger
	private static final Logger logger = LogManager.getLogger(InitialReturnFileTasklet.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");


	public InitialReturnFileTasklet(ReturnFileDao returnFileDao) {
		this.returnFileDao = returnFileDao;
	}

	public void truncateFromTemp(String tableName, List<String> fileNamesList) {
		returnFileDao.trauncteDataFromTemp(tableName,fileNamesList);
		
	}

	public List<String> chkDuplicateFile(String directoyPath, String masterTblName, List<String> fileNamesList) {

		logger.info(CCLPConstants.ENTER);
		File rootDir = new File(directoyPath);
		List<String> newFileNamesList=new ArrayList<>();
		if (fileNamesList != null && !fileNamesList.isEmpty()) {
			for (Iterator<String> iterator = fileNamesList.iterator(); iterator
					.hasNext();) {
				String fileName =  iterator.next();
				int result = returnFileDao.chkDuplicateFiles(fileName, masterTblName,
						" WHERE UPPER(FILE_NAME) = UPPER(?)");
				logger.info("Return File Name:" + fileName);
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
				result = returnFileDao.chkDuplicateFileStatus(fileName,true);
				logger.info("File Name:" + fileName+"  result:"+result);
				if (result > 0) {
					Util.moveToArchive(rootDir, fileName);
				} 
			});

		} 
		logger.info(CCLPConstants.EXIT);
	}

	public List<String> chkFileNameFormat(String directoryPath, List<String> fileNamesList) {
		
		logger.info(CCLPConstants.ENTER);
		boolean chkFlag = false;
		String fileParam = "";
		String  shipmentFileFormat = null;

		List<String> newFileNamesList = new ArrayList<>();
		if (fileNamesList != null && !fileNamesList.isEmpty()) {
			for (Iterator<String> iterator = fileNamesList.iterator(); iterator.hasNext();) {
				String fileName = iterator.next();
				String[] fileNameSplit = fileName.split("_");
				logger.info("shipment File Name:" + fileName);
			if(shipmentFileFormat==null) {
				shipmentFileFormat = returnFileDao.getReturnFileFormat();
			}
				fileParam = "Name";
				if(fileNameSplit.length==(shipmentFileFormat.split("_").length)) {
				fileParam = "destinationID";
				int destinationID = returnFileDao.chkDestinationId(fileNameSplit[0]);
				if (destinationID > 0) {
					fileParam = "sourcePlatform";
					int sourcePlatform = returnFileDao.chkSrcName(fileNameSplit[1]);
					if (sourcePlatform > 0) {
						fileParam = "date";
						int date = validateDateFormat(fileNameSplit[2]);
						if (date > 0) {
							fileParam = "sequenceNbr";
							int sequenceNbr = returnFileDao.chkDuplicateFiles(fileName,
									JobConstants.RETURN_FILE_MAST_TABLE, " WHERE UPPER(FILE_NAME) = UPPER(?)");
							if (sequenceNbr== 0) {
								fileParam = "fileType";
								if ("RESP.csv".equalsIgnoreCase(fileNameSplit[4])) {
									chkFlag = true;
								}
							}
						}
					}
				}
				}
				if (chkFlag) {

					newFileNamesList.add(fileName);
					// Util.moveToArchive(rootDir, fileName);
				} else {
					logger.debug("Invalid FileName Format : " + fileParam);
					 returnFileDao.insertReturnFileStatus(fileName, "FAILED",
							"Invalid FileName Format :  Invalid " + fileParam);
				}

			}
		}
		logger.info(CCLPConstants.EXIT);
		return newFileNamesList;
	}
	public static int validateDateFormat(String date) {
		String formatDate = null;
		try {
			Date formatDateTime = sdf.parse(date.trim());

			formatDate = sdf.format(formatDateTime);
		} catch (Exception e) {
			logger.error("Invalid Date" + e.getMessage());

			return 0;
		}
		return formatDate.equals(date) ? 1 : 0;
	}


}


