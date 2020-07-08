package com.incomm.scheduler.dao;

import java.util.List;

import com.incomm.scheduler.model.CnShipmentFile;

public interface CnShipmentFileDao {
	
	public void insert(List<? extends CnShipmentFile> cnShipmentFiles);
	public int chkDuplicateFiles(String fileName,String tableName,String whereCondition);
	public int chkDuplicateFileStatus(String fileName, boolean jobStatus);
	public void trauncteDataFromTemp(String tableName);
	public void makeProcCall();
	void insertCNFileStatus(String fileName, String status, String errorMsg);
	void updateCNFileStatus(String fileName, String status, String errorMsg);
}
