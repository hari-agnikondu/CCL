package com.incomm.scheduler.service;

import java.util.List;
import java.util.Map;

public interface CcfGenerationService {

	void generateCcfFile(String[] ccf,String userId)/* throws IOException, ServiceException, ParseException*/;

	List<Map<String, Object>> getCCFFilesToDelete(String ccfFileDelGap);

	
		

	
}
