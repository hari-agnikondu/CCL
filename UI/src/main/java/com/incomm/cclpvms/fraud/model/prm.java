package com.incomm.cclpvms.fraud.model;

import java.util.Map;

public class prm {
	
	private Map<String, String> prmAttributes ;

	public Map<String, String> getPrmAttributes() {
		return prmAttributes;
	}

	@Override
	public String toString() {
		return "prm [prmAttributes=" + prmAttributes + "]";
	}

	public void setPrmAttributes(Map<String, String> prmAttributes) {
		this.prmAttributes = prmAttributes;
	}

	
}
