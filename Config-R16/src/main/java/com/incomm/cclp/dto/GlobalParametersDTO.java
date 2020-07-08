package com.incomm.cclp.dto;

import java.util.Map;

public class GlobalParametersDTO {

	private Map<String, Object> globalParameters;

	public GlobalParametersDTO() {
	}

	public GlobalParametersDTO(Map<String, Object> globalParameters) {
		this.globalParameters = globalParameters;
	}

	public Map<String, Object> getGlobalParameters() {
		return globalParameters;
	}

	public void setGlobalParameters(Map<String, Object> globalParameters) {
		this.globalParameters = globalParameters;
	}

	@Override
	public String toString() {
		return "GlobalParametersDTO [globalParameters=" + globalParameters + "]";
	}

}
