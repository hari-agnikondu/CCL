package com.incomm.cclpvms.config.validator;

import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.incomm.cclpvms.config.model.Product;
import com.incomm.cclpvms.config.model.ProductAlert;

@Component
@PropertySource("classpath:serverValidation.properties")
public class ValidateProductAlerts implements Validator {
	boolean errorFlag = false;
	
	public static final String ERR="error";
	public static final String ALERT_SMS_CODE="alertAttributes['alertSMSCode']";
	public static final String ALERT_INACTIVITY_PERIOD="alertAttributes['alertInactivityPeriod']";
	public static final String ALERT_FROM_EMAIL="alertAttributes['alertFromEmail']";
	@Autowired
	Environment env;

	public boolean supports(Class<?> clazz) {

		return Product.class.isAssignableFrom(clazz);
	}


	public void validate(Object target, Errors errors) {
	
		ProductAlert product = (ProductAlert) target;
		String pattern = "";
		String patternNumeric="";
		patternNumeric = "^[0-9]+$";
		
	
		
		
		Map<String, String> alertMap = product.getAlertAttributes();
		for (String key : alertMap.keySet()) {
			String value =  alertMap.get(key);
			
			pattern="^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
			if (key.equals("alertAppName")) {

				if ((value == null)  || value.equals("")) {
					errors.rejectValue("alertAttributes['alertAppName']", "messageNotEmpty.productAlert.applicationName", ERR);
					errorFlag = true;

				}
				else if(value!=null && value.length()>50) {
					errors.rejectValue("alertAttributes['alertAppName']", "messageLength.productAlert.applicationName", ERR);
					errorFlag = true;

				}
			}
			   if(key.equals("alertFromEmail"))
			   {
				
				
				 Pattern validEmailRegex = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
				 
				 
				 if(value==null || value == ""){
					errors.rejectValue(ALERT_FROM_EMAIL, "messageNotEmpty.productAlert.alertFromEmail", "Email ID for Statement Should be Max of 50 Characters");
					errorFlag = true;
					}
				 else if((value!=null || !value.trim().isEmpty()) && !(validEmailRegex.matcher(value).matches())  ) 
					{
					errors.rejectValue(ALERT_FROM_EMAIL, "messagepattern.productAlert.alertFromEmail", "Email ID for Statement is Invalid12");
					errorFlag = true;
					}
				 else if((value!=null && !value.trim().isEmpty())  && (value.length()>50))
				 {
			errors.rejectValue(ALERT_FROM_EMAIL, "messageLength.productAlert.alertFromEmail", "Email ID for Statement is Invalid");
			errorFlag = true;
			}
				 
			}
			if (key.equals("alertAppNotifyType")) {

				if((value == null)  || value.equals("")) {
					errors.rejectValue("alertAttributes['alertAppNotifyType']", "messageNotEmpty.productAlert.applicationNotifyType", ERR);
					errorFlag = true;

				}
			}
			if (key.equals("alertSMSCode")) {

				if ((value == null)  || value.equals("")) {
					errors.rejectValue(ALERT_SMS_CODE, "messageNotEmpty.productAlert.alertSMSCode", ERR);
					errorFlag = true;

				}
				else if(value!=null && value.matches(patternNumeric) && (Long.valueOf(value)==0)) {
					errors.rejectValue(ALERT_SMS_CODE, "messageStartsWithZero.productAlert.alertSMSCode",
							ERR);
					
				}
			
				else if(value!=null &&(value.length()<3 || value.length()>6)) {
					errors.rejectValue(ALERT_SMS_CODE, "messageLength.productAlert.alertSMSCode", ERR);
					errorFlag = true;

				}
			}
			if (key.equals("alertMinBalance")) {
				if(value!=null && value.length()>6) {
					errors.rejectValue("alertAttributes['alertMinBalance']", "messageLength.productAlert.alertMinBalance",
							ERR);
					errorFlag = true;
					
				}
				else if(value!=null && value.length()>0 && !(value.matches(patternNumeric))) {
					errors.rejectValue("alertAttributes['alertMinBalance']", "messagepattern.productAlert.alertMinBalance",
							ERR);
					errorFlag = true;
				}
				
			}
			
			
			if (key.equals("alertInactivityPeriod")) {
				if(value!=null && value.length()>6) {
					errors.rejectValue(ALERT_INACTIVITY_PERIOD, "messageLength.productAlert.alertInactivityPeriod",
							ERR);
					errorFlag = true;
					
				}
				else if(value!=null  && value.length()>0 && !(value.matches(patternNumeric))) {
					errors.rejectValue(ALERT_INACTIVITY_PERIOD, "messagepattern.productAlert.alertInactivityPeriod",
							ERR);
					errorFlag = true;
				}
				else if(value!=null && value.matches(patternNumeric) && Long.valueOf(value)==0) {
					errors.rejectValue(ALERT_INACTIVITY_PERIOD, "messageStartsWithZero.productAlert.alertInactivityPeriod",
							ERR);
					
				}
				
			}
			
	

		}
		if (errorFlag) {
			return;
		}
	}
}

	
