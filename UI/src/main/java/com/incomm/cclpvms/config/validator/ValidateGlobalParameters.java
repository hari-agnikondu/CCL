package com.incomm.cclpvms.config.validator;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.incomm.cclpvms.config.model.GlobalParameters;
import com.incomm.cclpvms.util.Util;

@Component
@PropertySource("classpath:ValidationMessages.properties")
public class ValidateGlobalParameters implements Validator {

	boolean errorFlag = false;
	public static final String MASKING_CHAR_VALUE = "globalParameters['maskingCharValue']";
	public static final String CUSTOMER_PD_LENGTH = "globalParameters['customerPasswordLength']";
	public static final String ERRORS = "error";
	private static final Logger logger = LogManager.getLogger(ValidateGlobalParameters.class);

	@Autowired
	Environment env;

	public boolean supports(Class<?> clazz) {

		return GlobalParameters.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		String pattern = "^[0-9]+$";
		String pattern1 = "^[x*]+$";
		String urlPatten = "^[a-zA-Z0-9 ,.:%&_/\\-]+$";

		GlobalParameters globalParam = (GlobalParameters) target;
		Map<String, Object> globalParametersMap = globalParam.getGlobalParameters();
		for (String key : globalParametersMap.keySet()) {
			/**
			 * inside the map validating fields
			 */
			String value = (String) globalParametersMap.get(key);
			if (key.equals("customerPasswordLength")) {
				if (value == "null" || value.equals("")) {
					errors.rejectValue(CUSTOMER_PD_LENGTH, "messageNotEmpty.globalParam.customerPasswordLength",
							ERRORS);
					errorFlag = true;
				} else if (value != null && (Long.valueOf(value) == 0)) {
					errors.rejectValue(CUSTOMER_PD_LENGTH, "messageStartsWithZero.globalParam.customerPasswordLength",
							ERRORS);
				} else if (value != null && !(value.matches(pattern))) {
					errors.rejectValue(CUSTOMER_PD_LENGTH, "messagePattern.globalParam.customerPasswordLength", ERRORS);
					errorFlag = true;
				} else if (value != null && (value.length() < 1 || value.length() > 2)) {
					errors.rejectValue(CUSTOMER_PD_LENGTH, "messageLength.globalParam.customerPasswordLength", ERRORS);
					errorFlag = true;
				}
			} else if (key.equals("hsm")) {
				if (value == "null" || value.equals("") || value.equals("-1")) {
					errors.rejectValue("globalParameters['hsm']", "messageNotEmpty.globalParam.hsm", ERRORS);
					errorFlag = true;
				}
			} else if (key.equals("maskingCharValue")) {

				if (value == "null" || value.equals("")) {
					errors.rejectValue(MASKING_CHAR_VALUE, "messageNotEmpty.globalParam.maskingCharValue", ERRORS);
					errorFlag = true;

				} else if (value != null && !(value.matches(pattern1))) {

					errors.rejectValue(MASKING_CHAR_VALUE, "messagePattern.globalParam.maskingCharValue", "error ");
					errorFlag = true;
				} else if (value != null && (value.length() < 1 || value.length() > 1)) {
					errors.rejectValue(MASKING_CHAR_VALUE, "messageLength.globalParam.maskingCharValue", ERRORS);
					errorFlag = true;

				}

			} else if (key.equals("mftUrl")) {
				if (!Util.isEmpty(value) && !value.matches(urlPatten)) {
					logger.error("MFT URL Value should be AlphaNumeric");
					errors.rejectValue("globalParameters['mftUrl']", "messagePattern.globalParam.mftUrl",
							"MFT URL Value should be AlphaNumeric");
					errorFlag = true;
				} else if (!Util.isEmpty(value) && value.length() > 100) {
					logger.error("MFT URL Value should contain maximum 100 characters");
					errors.rejectValue("globalParameters['mftUrl']", "messageLength.globalParam.mftUrl",
							"MFT URL Value should contain maximum 100 characters");
					errorFlag = true;
				}
			} else if (key.equals("mftPostBackUrl")) {
				if (!Util.isEmpty(value) && !value.matches(urlPatten)) {
					logger.error("MFT Post Back URL Value should be AlphaNumeric");
					errors.rejectValue("globalParameters['mftPostBackUrl']",
							"messagePattern.globalParam.mftPostBackUrl",
							"MFT Post Back URL Value should be AlphaNumeric");
					errorFlag = true;
				} else if (!Util.isEmpty(value) && value.length() > 100) {
					logger.error("MFT Post Back URL Value should contain maximum 100 characters");
					errors.rejectValue("globalParameters['mftPostBackUrl']", "messageLength.globalParam.mftPostBackUrl",
							"MFT Post Back URL Value should contain maximum 100 characters");
					errorFlag = true;
				}
			} else if (key.equals("mftPostBackUrlHeader")) {
				if (!Util.isEmpty(value) && !value.matches(urlPatten)) {
					logger.error("MFT Post Back URL Header Value should be AlphaNumeric");
					errors.rejectValue("globalParameters['mftPostBackUrlHeader']",
							"messagePattern.globalParam.mftPostBackUrlHeader",
							"MFT Post Back URL Header Value should be AlphaNumeric");
					errorFlag = true;
				} else if (!Util.isEmpty(value) && value.length() > 100) {
					logger.error("MFT Post Back URL Header Value should contain maximum 100 characters");
					errors.rejectValue("globalParameters['mftPostBackUrlHeader']",
							"messageLength.globalParam.mftPostBackUrlHeader",
							"MFT Post Back URL Header Value should contain maximum 100 characters");
					errorFlag = true;
				}
			} else if (key.equals("mftRetryCount")) {
				if (!Util.isEmpty(value) && !value.matches(pattern)) {
					logger.error("MFT Retry Count Value should be Numeric");
					errors.rejectValue("globalParameters['mftRetryCount']", "messagePattern.globalParam.mftRetryCount",
							"MFT Retry Count Value should be Numeric");
					errorFlag = true;
				} else if (!Util.isEmpty(value) && value.length() > 100) {
					logger.error("MFT Retry Count Value should contain maximum 100 characters");
					errors.rejectValue("globalParameters['mftRetryCount']", "messageLength.globalParam.mftRetryCount",
							"MFT Retry Count Value should contain maximum 100 characters");
					errorFlag = true;
				}
			} else if (key.equals("mftChannelIdentifier")) {
				if (!Util.isEmpty(value) && !value.matches(urlPatten)) {
					logger.error("MFT Channel Identifier Value should be Numeric");
					errors.rejectValue("globalParameters['mftChannelIdentifier']", "messagePattern.globalParam.mftChannelIdentifier",
							"MFT Channel Identifier Value should be Numeric");
					errorFlag = true;
				} else if (!Util.isEmpty(value) && value.length() > 100) {
					logger.error("MFT Channel Identifier Value should contain maximum 100 characters");
					errors.rejectValue("globalParameters['mftChannelIdentifier']", "messageLength.globalParam.mftChannelIdentifier",
							"MFT Channel Identifier Value should contain maximum 100 characters");
					errorFlag = true;
				}
			}

			/**
			 * ending the validations
			 */
		}

		if (errorFlag) {
			return;
		}

	}

}
