package com.incomm.cclpvms.config.validator;

import java.util.Map;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.incomm.cclpvms.config.model.PackageID;
import com.incomm.cclpvms.util.Util;

@Component
@PropertySource("classpath:ValidationMessages.properties")
public class ValidationPackageID {

	private static final String PACKAGE_ID = "packageId";
	
	private static final String ERROR = "error";
	
	boolean errorFlag = false;

	public boolean supports(Class<?> clazz) {

		return PackageID.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {

		PackageID packageID = (PackageID) target;
		String patternNumeric = "^[0-9]+$";
		String patternAlphaNumeric = "^[a-zA-Z0-9]+$";
		String patternAlphaNumericSpace="^[a-zA-Z0-9 ]+$";
		String packId=packageID.getPackageId();
	
	
		
		//validate shipping methods values
		
		if(packId== null||packId.isEmpty())
		{
			errors.rejectValue(PACKAGE_ID, "messageNotEmpty.package.packageID", ERROR);
			errorFlag = true;
		}
		else if(packId.length() > 0 && !(packId).matches(patternAlphaNumeric)) {
			errors.rejectValue(PACKAGE_ID, "messagepattern.package.packageId", ERROR);
			errorFlag = true;
			
		}
		else if(packId.length() < 2 || packId.length() > 4) {
			errors.rejectValue(PACKAGE_ID, "messageLength.package.packageId", ERROR);
			errorFlag = true;
			
		}
		if(packageID.getFulfillmentId().equalsIgnoreCase("-1")){
			errors.rejectValue("fulfillmentId", "messagepattern.package.fulfillmenttpackageId", ERROR);
			errorFlag = true;
		}
		

		Map<String, String> attributesMap = null;
		attributesMap = packageID.getPackageAttributes();
		for (String key : attributesMap.keySet()) {
			String value =  attributesMap.get(key);
		
			if(key.equals("shipMethods"))
			{
				value = Util.returnBlank(value);
				if(value.equals(""))
				{
				errors.rejectValue("packageAttributes['shipMethods']", "messagepattern.package.shipMethods", ERROR);
				errorFlag = true;
				}
			}
			
			else if (key.equals("carrierId")) {
				if (value.length() > 0 && !(value).matches(patternNumeric)) {
					errors.rejectValue("packageAttributes['carrierId']", "messagepattern.package.carrierId", ERROR);
					errorFlag = true;
				}
			}

			else if (key.equals("logoId")) {
				if (value.length() > 0 && !(value).matches(patternAlphaNumeric)) {
					errors.rejectValue("packageAttributes['logoId']", "messagepattern.package.logoId", ERROR);
					errorFlag = true;
				}
			}

			else if (key.equals("embossLine3") && value.length() > 0 && !(value).matches(patternAlphaNumericSpace)) {
				
					errors.rejectValue("packageAttributes['embossLine3']", "messagepattern.package.embossLine3",
							ERROR);
					errorFlag = true;
				
			}

			if (key.equals("embossLine4") && value.length() > 0 && !(value).matches(patternAlphaNumericSpace)) {
				
					errors.rejectValue("packageAttributes['embossLine4']", "messagepattern.package.embossLine4",
							ERROR);
					errorFlag = true;
				
			}

			else if (key.equals("envelopeId") && value.length() > 0 && !(value).matches(patternAlphaNumeric)) {
				
					errors.rejectValue("packageAttributes['envelopeId']", "messagepattern.package.envelopeId", ERROR);
					errorFlag = true;
				
			}

			else if (key.equals("envelopeSealed") && value.length() > 0 && !(value).matches(patternAlphaNumeric)) {
				
					errors.rejectValue("packageAttributes['envelopeSealed']", "messagepattern.package.envelopeSealed",
							ERROR);
					errorFlag = true;
				
			}

			else if (key.equals("insertId") && value.length() > 0 && !(value).matches(patternAlphaNumeric)) {
				
					errors.rejectValue("packageAttributes['insertId']", "messagepattern.package.insertId", ERROR);
					errorFlag = true;
				
			} else if (key.equals("activationStickerId") && value.length() > 0 && !(value).matches(patternAlphaNumeric)) {
				
					errors.rejectValue("packageAttributes['activationStickerId']",
							"messagepattern.package.activationStickerId", ERROR);
					errorFlag = true;
				
			}

			else if (key.equals("thermalPrintColorId") && value.length() > 0 && !(value).matches(patternAlphaNumeric)) {
				
					errors.rejectValue("packageAttributes['thermalPrintColorId']",
							"messagepattern.package.thermalPrintColorId", ERROR);
					errorFlag = true;
				
			}

			else if (key.equals("cardPrintVersionId") && value.length() > 0 && !(value).matches(patternAlphaNumeric)) {
				
					errors.rejectValue("packageAttributes['cardPrintVersionId']",
							"messagepattern.package.cardPrintVersionId", ERROR);
					errorFlag = true;
				
			}

			else if (key.equals("packingSlipId") && value.length() > 0 && !(value).matches(patternAlphaNumeric)) {
				
					errors.rejectValue("packageAttributes['packingSlipId']", "messagepattern.package.packingSlipId",
							ERROR);
					errorFlag = true;
				
			}

			else if (key.equals("bundleSize") && value.length() > 0 && !(value).matches(patternNumeric)) {
				
					errors.rejectValue("packageAttributes['bundleSize']", "messagepattern.package.bundleSize", ERROR);
					errorFlag = true;
				
			}
		}

		if (errorFlag) {
			return;
		}
	}
}
