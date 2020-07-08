package com.incomm.cclpvms.config.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.incomm.cclpvms.config.model.ProgramID;
import com.incomm.cclpvms.constants.CCLPConstants;

@Component
@PropertySource("classpath:ValidationMessages.properties")
public class ValidateProgramID implements Validator {
	

	@Autowired
	Environment env;

	boolean errorFlag = false;
	private static final String PROGRAM_ID_NAME = "programIDName";
	
	
	@Override
	public boolean supports(Class<?> clazz) {
		return ProgramID.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		String patternProgramName="^[A-Za-z0-9 ,.;'_-]+$";
		ProgramID programId = (ProgramID) target;
		String programIdName=programId.getProgramIDName();
		Long partnerId=programId.getPartnerId();
		
		if (programIdName == "null"  || programIdName.equals("")) {
			errors.rejectValue(PROGRAM_ID_NAME, "messageNotEmpty.programId.programIDName",
					CCLPConstants.ERROR);
			errorFlag = true;

		}
		else if(!(programIdName).matches(patternProgramName)&& programIdName.length()>0 ) {
			errors.rejectValue(PROGRAM_ID_NAME, "messagepattern.programId.programIDName",
					CCLPConstants.ERROR);
			errorFlag = true;
		}
		else if(programIdName.length()<2 || programIdName.length()>100) {
			errors.rejectValue(PROGRAM_ID_NAME, "messageLength.programId.programIDName",
					CCLPConstants.ERROR);
			errorFlag = true;

		}
		
	if(partnerId.longValue()==-1) {
		errors.rejectValue("partnerId", "messageNotEmpty.program.partnerName",
				CCLPConstants.ERROR);
		errorFlag = true;
		
	}
		
		if (errorFlag) {
			return;
		}
	}

}
