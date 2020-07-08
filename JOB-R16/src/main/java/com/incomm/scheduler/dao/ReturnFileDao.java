package com.incomm.scheduler.dao;

import java.util.List;

import com.incomm.scheduler.model.ReturnFile;

public interface ReturnFileDao {
	
	
	//Return File
	public void insertReturnFile(List<? extends ReturnFile> returnFile);
	public void updateReturnFileStatus(String fileName, String status, String errorMsg);
	public void insertReturnFileStatus(String fileName, String status, String errorMsg);
	public void returnFileProcCall();
	
	public int chkDuplicateFiles(String fileName,String tableName,String whereCondition);
	public int chkDuplicateFileStatus(String fileName, boolean jobStatus);
	public void trauncteDataFromTemp(String tableName);
	public int chkDestinationId(String string);
	public int chkSrcName(String string);
	String getReturnFileFormat();
	public void trauncteDataFromTemp(String tableName, List<String> fileNamesList);
	
	
	
}
