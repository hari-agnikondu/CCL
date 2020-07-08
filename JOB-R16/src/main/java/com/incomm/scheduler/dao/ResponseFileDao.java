package com.incomm.scheduler.dao;

import java.util.List;

import com.incomm.scheduler.model.ResponseFile;

public interface ResponseFileDao {

	public void insertResponseFile(List<? extends ResponseFile> responseFiles);
	public void updateResponseFileStatus(String fileName, String status, String errorMsg);
	public void insertResponseFileStatus(String fileName, String status, String errorMsg);
	public void responseFileProcCall();

	public int chkDuplicateFiles(String fileName,String tableName,String whereCondition);
	public int chkDuplicateFileStatus(String fileName, boolean jobStatus);
	public void trauncteDataFromTemp(String tableName) ;
	public String getResponseFileFormat();
	public int chkDestinationId(String string);
	public int chkSrcName(String string);
	public void trauncteDataFromTemp(String tableName, List<String> fileNamesList);
	
}
