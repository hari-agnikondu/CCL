package com.incomm.scheduler.dao;

import java.util.List;

import com.incomm.scheduler.model.SerialNumberFile;

public interface SerialNumberFileDao {
	
	
	//Return File
	public void insertSerialNumberFile(List<? extends SerialNumberFile> serialNumberFile);
	public void updateSerialNumberFileStatus(String fileName, String status, String errorMsg);
	public void insertSerialNumberFileStatus(String fileName, String status, String errorMsg);
	public void serialNumberFileProcCall();
	
	public int chkDuplicateFiles(String fileName,String tableName,String whereCondition);
	public int chkDuplicateFileStatus(String fileName, boolean jobStatus);
	public void truncateDataFromTemp(String tableName) ;
	
	
}
