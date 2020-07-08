package com.incomm.scheduler.tasklet;

import java.io.File;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.dao.CCFGenerationDAO;
import com.incomm.scheduler.service.CcfGenerationService;
import com.incomm.scheduler.utils.Util;




@Component
public class InitialCCFFileDirDelete {

	private static final Logger logger = LogManager.getLogger(InitialCCFFileDirDelete.class);

	@Autowired
	CcfGenerationService ccfGenerationService;
	
	@Autowired
	CCFGenerationDAO ccfGenerationDao;
	
	public void setCcfGenerationDao(CCFGenerationDAO ccfGenerationDao) {
		this.ccfGenerationDao = ccfGenerationDao;
		
	}
	
	public void setCcfGenerationService(CcfGenerationService ccfGenerationService) {
		this.ccfGenerationService=ccfGenerationService;
	}

	@Value("${CCF_DIR_PATH}")
	String psFilePath;
	
	@Value("${CCF_FILE_DELETION_GAP}")
	String ccfFileDelGap;
	

	
	public void deleteDir() {
		logger.info(CCLPConstants.ENTER);
		File psDir = new File(psFilePath);

		try {
			if (!psDir.exists()) {
				logger.error("Directiry Does not exists");
			}

			List<Map<String, Object>> ccfFiles = ccfGenerationService.getCCFFilesToDelete(ccfFileDelGap);

			if (!Objects.isNull(ccfFiles)) {
				Iterator<Map<String, Object>> it = ccfFiles.iterator();

				while (it.hasNext()) {
					Map<String, Object> fileMap = it.next();
					String fileName = Util.convertAsString(fileMap.get("EMB_FNAME"));
					String vendorId = Util.convertAsString(fileMap.get("VENDOR_NAME"));
					if (fileName.contains(".tmp"))
						fileName = fileName.replace(".tmp", ".csv");

					String psStrPath = new StringBuilder().append(psDir).append("/").append(getVendorName(vendorId))
							.append("/").toString();
					searchDirectory(new File(psStrPath), fileName);
				}
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		logger.info(CCLPConstants.EXIT);
	}

	private void searchDirectory(File psDir, String fileName) {
		
		if(psDir.isDirectory()) {
			search(psDir,fileName);
		}
		
	}

	public String getVendorName(String vendorId) {
		logger.info(CCLPConstants.ENTER);
		String vendorName = "";
		vendorName = ccfGenerationDao.getVendorNameForId(vendorId);
		logger.debug("vendor Name {} and vendor ID {} ", vendorName, vendorId);
		logger.info(CCLPConstants.EXIT);
		return vendorName;
	}
	
	private void search(File psDir, String fileName) {

		if (psDir.canRead()) {
			for (File temp : psDir.listFiles()) {
				if (temp.isDirectory()) {
					search(psDir, fileName);
				} else {
					if (fileName.equals(temp.getName()) && temp.getAbsoluteFile().delete()) {
							logger.info(" File deleted succesfully");
						}
					
				}
			}
		}
	}

	

	

}
