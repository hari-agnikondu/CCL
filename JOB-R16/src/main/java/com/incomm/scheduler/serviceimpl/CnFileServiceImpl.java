package com.incomm.scheduler.serviceimpl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.scheduler.dao.CCFGenerationDAO;
import com.incomm.scheduler.service.CnFileService;

@Service
public class CnFileServiceImpl implements CnFileService {

	private static final Logger logger = LogManager.getLogger(CnFileServiceImpl.class);
	
	@Autowired
    CCFGenerationDAO ccfGenerationDao;
	
	@Override
	  public List<String> listFiles(String cnFilePath){
		
		List<String> filesList = new ArrayList<>();
	        File directory = new File(cnFilePath);
	        //get all the files from a directory
	        logger.debug("List all files from location: "+cnFilePath);
	        File[] fList = directory.listFiles();
	        for (File file : fList){
	            if (file.isFile() && file.getName().endsWith(".csv")){
	            	
	            	logger.debug(file.getName());
	                filesList.add(file.getName());
	            }
	        }
			return filesList;
	    }

	@Override
	public List<Map<String, Object>> cnFileList() {
		return ccfGenerationDao.cnFileList();
	}
	  
}
