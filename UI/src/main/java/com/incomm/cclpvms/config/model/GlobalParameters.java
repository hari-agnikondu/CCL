package com.incomm.cclpvms.config.model;

import java.util.Map;

public class GlobalParameters {
	
		
	private Map<String, Object> globalParameters;

	public Map<String, Object> getGlobalParameters() {
		return globalParameters;
	}

	public void setGlobalParameters(Map<String, Object> globalParameters) {
		this.globalParameters = globalParameters;
	}

	public GlobalParameters(Map<String, Object> globalParameters) {
		this.globalParameters = globalParameters;
	}
	
	public GlobalParameters() {
	
	}

}
