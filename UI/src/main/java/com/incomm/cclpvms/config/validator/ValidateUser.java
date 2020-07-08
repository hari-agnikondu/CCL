package com.incomm.cclpvms.config.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.incomm.cclpvms.admin.model.ClpUser;
@Component
@PropertySource("classpath:ValidationMessages.properties")
public class ValidateUser implements Validator {

	private static final String ERROR = "error";
	private static final String USER_LOGIN_ID = "userLoginId";
	
	boolean errorflag = false;

	@Autowired
	Environment env;


	@Override
	public boolean supports(Class<?> clazz) {
		return ClpUser.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ClpUser clpUser = (ClpUser) target;
		
		String userLoginId=clpUser.getUserLoginId();
 		String userName=clpUser.getUserName();
		
		String[] groupName = clpUser.getGroupNames().toArray(new String[0]);
		String patternAlpha="^[a-zA-Z0-9.\\-_]*" ;
		String patternAlphaNumeric="^[A-Za-z0-9 ,&.;'_-]+$" ;
	
		
		
		if (userLoginId == null  || userLoginId.equals("")) {
			errors.rejectValue(USER_LOGIN_ID, "messageNotEmpty.user.userLoginId",
					ERROR);
			errorflag = true;

		}
		else if(!(userLoginId).matches(patternAlpha)&& userLoginId.length()>0 ) {
			errors.rejectValue(USER_LOGIN_ID, "messagepattern.user.userLoginId",
					ERROR);
			errorflag = true;
		}
		else if(userLoginId.length()<2 || userLoginId.length()>40) {
			errors.rejectValue(USER_LOGIN_ID, "messageLength.user.userLoginId",
					ERROR);
			errorflag = true;

		}
		
		if(!(userName).matches(patternAlphaNumeric)&& userName.length()>0 ) {
			errors.rejectValue("userName", "messagepattern.user.userName",
					ERROR);
			errorflag = true;
		}
		else if( userName.length()> 50) {
			errors.rejectValue("userName", "messageLength.user.userName",
					ERROR);
			errorflag = true;

		}
		
		  
		  
		  if(groupName==null || groupName.length==0 ) {
				
				errors.rejectValue("groupNames", "messageNotEmpty.user.groupNames",
						ERROR);
				errorflag = true;

			}
			if (errorflag) {
				return;
			}
		
	}

}
