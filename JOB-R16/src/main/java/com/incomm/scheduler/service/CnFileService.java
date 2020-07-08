package com.incomm.scheduler.service;

import java.util.List;
import java.util.Map;

public interface CnFileService {

	List<String> listFiles(String directoryName);
	List<Map<String, Object>> cnFileList();

}
