package com.incomm.scheduler.dao;

import java.util.List;

import com.incomm.scheduler.model.ShipmentFile;

public interface ShipmentFileDao {
	
	
	//Return File
	public void insertShipmentFile(List<? extends ShipmentFile> shipmentFile);
	public void updateShipmentFileStatus(String fileName, String status, String errorMsg);
	public void insertShipmentFileStatus(String fileName, String status, String errorMsg);
	public void shipmentFileProcCall();
	
	public int chkDuplicateFiles(String fileName,String tableName,String whereCondition);
	public int chkDuplicateFileStatus(String fileName, boolean jobStatus);
	public void truncateDataFromTemp(String tableName) ;
	int chkDestinationId(String fileName);
	public int chkSrcName(String string);
	String getShipmentFileFormat();
	
	public void truncateDataFromTemp(String tableName, List<String> fileNamesList);
	
	
}
