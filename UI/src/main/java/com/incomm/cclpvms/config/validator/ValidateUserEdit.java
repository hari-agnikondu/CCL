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
public class ValidateUserEdit implements Validator {
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
		
		 
		String[] groupName = clpUser.getGroupNames().toArray(new String[0]);
		
		  if(groupName==null || groupName.length==0 ) {
				
				errors.rejectValue("groupNames", "messageNotEmpty.user.groupNames",
						"error");
				errorflag = true;

			}
			if (errorflag) {
				return;
			}
		
	}

}