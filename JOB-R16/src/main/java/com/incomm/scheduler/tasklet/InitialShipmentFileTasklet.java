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
import com.incomm.scheduler.dao.ShipmentFileDao;
import com.incomm.scheduler.utils.JobConstants;
import com.incomm.scheduler.utils.Util;

public class InitialShipmentFileTasklet {

	private final ShipmentFileDao shipmentFileDao;

	// The Logger
	private static final Logger logger = LogManager.getLogger(InitialShipmentFileTasklet.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	public InitialShipmentFileTasklet(ShipmentFileDao shipmentFileDao) {
		this.shipmentFileDao = shipmentFileDao;
	}

	public void truncateFromTemp(String tableName, List<String> fileNamesList) {
		shipmentFileDao.truncateDataFromTemp(tableName,fileNamesList);
	}

	public List<String> chkDuplicateFile(String directoyPath, String masterTblName, List<String> fileNamesList) {

		logger.info(CCLPConstants.ENTER);
		File rootDir = new File(directoyPath);
		List<String> newFileNamesList = new ArrayList<>();
		if (fileNamesList != null && !fileNamesList.isEmpty()) {
			for (Iterator<String> iterator = fileNamesList.iterator(); iterator.hasNext();) {
				String fileName = iterator.next();
				int result = shipmentFileDao.chkDuplicateFiles(fileName, masterTblName,
						" WHERE UPPER(FILE_NAME) = UPPER(?)");
				logger.info("Shipment File Name:" + fileName );
				if (result > 0) {
					logger.info("Duplicated file");
					Util.moveToArchive(rootDir, fileName);
				} else {
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
			fileNamesList.forEach(fileName -> {
				int result = 0;
				result = shipmentFileDao.chkDuplicateFileStatus(fileName, true);
				logger.info("File Name:" + fileName + "  result:" + result);
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
				fileParam = "Name";
				if(shipmentFileFormat==null) {
				shipmentFileFormat = shipmentFileDao.getShipmentFileFormat();
				}
				if(fileNameSplit.length==(shipmentFileFormat.split("_").length)) {
				fileParam = "destinationID";
				int destinationID = shipmentFileDao.chkDestinationId(fileNameSplit[0]);
				if (destinationID > 0) {
					fileParam = "sourcePlatform";
					int sourcePlatform = shipmentFileDao.chkSrcName(fileNameSplit[1]);
					if (sourcePlatform > 0) {
						fileParam = "date";
						int date = validateDateFormat(fileNameSplit[2]);
						if (date > 0) {
							fileParam = "sequenceNbr";
							int sequenceNbr = shipmentFileDao.chkDuplicateFiles(fileName,
									JobConstants.SHIPMENT_FILE_MAST_TABLE, " WHERE UPPER(FILE_NAME) = UPPER(?)");
							if (sequenceNbr == 0) {
								fileParam = "fileType";
								if ("SHIP.csv".equalsIgnoreCase(fileNameSplit[4])) {
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
					logger.debug("Invalid FileName Format :  Invalid " + fileParam);
					shipmentFileDao.insertShipmentFileStatus(fileName, "FAILED",
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
			logger.info("Invalid Date" + e);

			return 0;
		}
		return formatDate.equals(date) ? 1 : 0;
	}

}
