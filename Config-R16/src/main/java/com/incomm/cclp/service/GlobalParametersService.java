package com.incomm.cclp.service;

import java.util.Map;

public interface GlobalParametersService {

	public Map<String, Object> getGlobalParameters();

	public void updateGlobalParameters(Map<String, Object> globalParameters);

	public void updateThreadProperties(int chunkSize, int threadPoolSize, int maxThreadPoolSize);

}
