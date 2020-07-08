package com.incomm.cclpvms.util;

import java.util.ResourceBundle;

public class ResourceBundleUtil {

	private static ResourceBundle messageBundle= ResourceBundle.getBundle("messages");
	
	public static String getMessage(String key) {
		if(!Util.isEmpty(key))
			return messageBundle.getString(key);
		else
			return "";
	}
}
