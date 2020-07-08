package com.incomm.cclpvms.config.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.incomm.cclpvms.config.model.FulFillment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


@Component
@PropertySource("classpath:ValidationMessages.properties")
public class ValidateFulfillment implements Validator {
	boolean errorflag = false;

	@Autowired
	Environment env;

	@Override
	public boolean supports(Class<?> clazz) {

		return FulFillment.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		FulFillment fulfillment = (FulFillment) target;

		
		String b2bCnFileIdentifier =fulfillment.getB2bCnFileIdentifier();
		if (fulfillment.getB2bVendorConfReq().equalsIgnoreCase("enabled")) {
			if (b2bCnFileIdentifier == null || b2bCnFileIdentifier.equals("")) 
			{
				errorflag = true;
				errors.rejectValue("b2bCnFileIdentifier", "messageNotEmpty.fulFillment.b2bCnFileIdentifier", "Please Enter B2B CN File Identifier");
			}
			else if (b2bCnFileIdentifier.length()>20 ) 
			{
				errorflag = true;
				errors.rejectValue("b2bCnFileIdentifier", "messageLength.fulFillment.b2bCnFileIdentifie", "B2B CN File Identifier should contain  maximum 20 characters");
			}

		}
		
		if (errorflag) {
			return;
		}
	}

}

