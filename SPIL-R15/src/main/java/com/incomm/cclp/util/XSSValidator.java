package com.incomm.cclp.util;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.transaction.constants.GeneralConstants;

/**
 * VMSCL-455 Blacklisting - Maintain list of html and javascript tags and Validate the entire input XML message for
 * cross site scripting vulnerabilities such as <script> tag
 */
@Component
public class XSSValidator {

	private static final Logger logger = LogManager.getLogger(ValidationUtil.class);

	@Value("#{'${cclp.supportlog.blacklistData}'.split(',')}")
	private List<String> blackListTags;

	public void validateRequestXml(String inXML) throws ServiceException {
		logger.debug(GeneralConstants.ENTER);

		if (!CollectionUtils.isEmpty(blackListTags)) {
			try {
				String req = inXML.toLowerCase();

				boolean foundScript = false;
				for (String blackListTag : blackListTags) {
					if (req.indexOf("</" + blackListTag + ">") >= 0 || req.indexOf("<" + blackListTag + ">") >= 0
							|| req.indexOf("<" + blackListTag + " ") >= 0) {
						foundScript = true;
						logger.error("Blacklisting data: {} found in request", blackListTag);
					}
				}

				if (foundScript) {
					throw new ServiceException(SpilExceptionMessages.SPIL_INVALID_REQUEST, ResponseCodes.INVALID_REQUEST);
				}

			} catch (ServiceException e) {
				throw e;
			} catch (Exception e) {
				logger.error("Error occured in validating request xml: " + e.getMessage(), e);
				throw new ServiceException("Exception occured while validating xml", ResponseCodes.INVALID_REQUEST);
			}

		} else {
			logger.debug("There is no Blacklisting data found");
		}

		logger.debug(GeneralConstants.EXIT);
	}

}
