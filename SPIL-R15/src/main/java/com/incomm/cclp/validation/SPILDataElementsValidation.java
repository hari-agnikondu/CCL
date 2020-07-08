package com.incomm.cclp.validation;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.transaction.bean.TransactionInputValidation;
import com.incomm.cclp.transaction.service.TransactionService;
import com.incomm.cclp.util.Util;

/**
 * Class used to validate the data elements which has been populated after parsing the incoming XML message for SPIL
 * Added on 15th, May 2018 Venkatesh Gaddam
 */
@Component
public class SPILDataElementsValidation {
	@Autowired
	TransactionService transactionService;

	Logger logger = LogManager.getLogger(SPILDataElementsValidation.class);

	public boolean validate(Map<String, String> xmlParsedElements) {
		/**
		 * validation property file will have key as data element name and value as regular expression for matching the
		 * pattern
		 */

		try {
			List<TransactionInputValidation> inputValidationList = transactionService.getInputValidations(xmlParsedElements);
			if (inputValidationList != null && !inputValidationList.isEmpty()) {
				String elementName = null;
				for (Iterator<TransactionInputValidation> iterator = inputValidationList.iterator(); iterator.hasNext();) {
					TransactionInputValidation transactionInputValidation = iterator.next();
					if ((transactionInputValidation.getParentTag() != null && !Util.isEmpty(transactionInputValidation.getParentTag()))
							&& ((xmlParsedElements.get(transactionInputValidation.getParentTag()) == null)
									&& (Util.isEmpty(xmlParsedElements.get(transactionInputValidation.getParentTag()))))) {
						continue;
					}
					elementName = transactionInputValidation.getFieldName();
					String dataElement = null;
					if (ValueObjectKeys.SPIL_TRAN_AMT.equalsIgnoreCase(elementName)) {
						dataElement = xmlParsedElements.get(ValueObjectKeys.ORGNL_TRAN_AMOUNT);
					} else {
						dataElement = xmlParsedElements.get(elementName);
					}

					String pattern = transactionInputValidation.getPattern();

					if ("M".equals(transactionInputValidation.getType())) {
						if (dataElement != null && !dataElement.isEmpty()) {
							if (!isValidData(dataElement, pattern)) {
								if (ValueObjectKeys.CVV2.equalsIgnoreCase(elementName)) {
									elementName = ValueObjectKeys.CLCVV;
								}
								throw new ServiceException("Data Element Name " + elementName + " is Invalid",
										ResponseCodes.INVALID_REQUEST);
							}
						} else {
							if (ValueObjectKeys.CVV2.equalsIgnoreCase(elementName)) {
								elementName = ValueObjectKeys.CLCVV;
							}
							throw new ServiceException("Data Element Name " + elementName + " is Empty", ResponseCodes.INVALID_REQUEST);
						}

					} else if (("O".equals(transactionInputValidation.getType()) && dataElement != null && !dataElement.isEmpty())
							&& !isValidData(dataElement, pattern) && !elementName.equalsIgnoreCase(ValueObjectKeys.PURAUTHREQ)) {
						if (ValueObjectKeys.CVV2.equalsIgnoreCase(elementName)) {
							elementName = ValueObjectKeys.CLCVV;
						}
						throw new ServiceException("Data Element Name " + elementName + " is Invalid", ResponseCodes.INVALID_REQUEST);
					}
				}
			} // if
		} catch (ServiceException servEx) {
			logger.info("Exception in data element validation" + servEx.getMessage());
			throw servEx;
		}
		return true;
	}

	// Method will return boolean value true if the validation is success
	// return boolean false if the validation is failure
	public boolean isValidData(String data, String validationPattern) {
		return data.matches(validationPattern);
	}

	public void validateBatchElements(Map<String, String> valueObj) {

		String referenceNumber = valueObj.get(ValueObjectKeys.SOURCE_REF_NUMBER);
		String spNumber = valueObj.get(ValueObjectKeys.SPNUMBER);
		String action = valueObj.get(ValueObjectKeys.ACTION);
		String amount = valueObj.get(ValueObjectKeys.SPIL_TRAN_AMT);
		String mdmId = valueObj.get(ValueObjectKeys.MDM_ID);
		String storeId = valueObj.get(ValueObjectKeys.SPIL_STORE_ID);
		String terminalId = valueObj.get(ValueObjectKeys.SPIL_TERM_ID);

		if (referenceNumber != null && !referenceNumber.isEmpty()) {
			if (!regexValidation(referenceNumber, "[0-9a-zA-Z \\\\s]{1,40}")) {
				throw new ServiceException("Data Element Name " + ValueObjectKeys.SOURCE_REF_NUMBER + " is Invalid",
						ResponseCodes.INVALID_REQUEST);
			}
		} else {
			throw new ServiceException("Data Element Name " + ValueObjectKeys.SOURCE_REF_NUMBER + " is Invalid",
					ResponseCodes.INVALID_REQUEST);
		}

		if (spNumber != null && !spNumber.isEmpty()) {
			if (!regexValidation(spNumber, "[a-zA-Z0-9]{1,21}")) {
				throw new ServiceException("Data Element Name " + ValueObjectKeys.SPNUMBER + " is Invalid", ResponseCodes.INVALID_REQUEST);
			}
		} else {
			throw new ServiceException("Data Element Name " + ValueObjectKeys.SPNUMBER + " is Invalid", ResponseCodes.INVALID_REQUEST);
		}

		if (action != null && !action.isEmpty()) {
			if (!regexValidation(action.toUpperCase(), "ACT|DEACT|VALINS|BALINQ")) {
				throw new ServiceException("Data Element Name " + ValueObjectKeys.ACTION + " is Invalid", ResponseCodes.INVALID_REQUEST);
			}
		} else {
			throw new ServiceException("Data Element Name " + ValueObjectKeys.ACTION + " is Invalid", ResponseCodes.INVALID_REQUEST);
		}

		if (amount != null && !amount.isEmpty()) {
			if (!regexValidation(amount, "[0-9]{1,012}|([0-9]{1,012}\\.[0-9]{1,3})")) {
				throw new ServiceException("Data Element Name " + ValueObjectKeys.SPIL_TRAN_AMT + " is Invalid",
						ResponseCodes.INVALID_REQUEST);
			}
		} else {
			throw new ServiceException("Data Element Name " + ValueObjectKeys.SPIL_TRAN_AMT + " is Invalid", ResponseCodes.INVALID_REQUEST);
		}

		if (mdmId != null && !mdmId.isEmpty()) {
			if (!regexValidation(mdmId, "[0-9]{1,20}")) {
				throw new ServiceException("Data Element Name " + ValueObjectKeys.MDM_ID + " is Invalid", ResponseCodes.INVALID_REQUEST);
			}
		} else {
			throw new ServiceException("Data Element Name " + ValueObjectKeys.MDM_ID + " is Invalid", ResponseCodes.INVALID_REQUEST);
		}

		if (storeId != null && !storeId.isEmpty()) {
			if (!regexValidation(storeId, "[a-zA-Z0-9#\\\\s-]{1,64}")) {
				throw new ServiceException("Data Element Name " + ValueObjectKeys.SPIL_STORE_ID + " is Invalid",
						ResponseCodes.INVALID_REQUEST);
			}
		}

		if (terminalId != null && !terminalId.isEmpty()) {
			if (!regexValidation(terminalId, "[0-9a-zA-Z \\\\s]{1,15}")) {
				throw new ServiceException("Data Element Name " + ValueObjectKeys.SPIL_TERM_ID + " is Invalid",
						ResponseCodes.INVALID_REQUEST);
			}
		} else {
			throw new ServiceException("Data Element Name " + ValueObjectKeys.SPIL_TERM_ID + " is Invalid", ResponseCodes.INVALID_REQUEST);
		}

	}

	private boolean regexValidation(String value, String regex) {
		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(value)
			.matches();
	}

}
