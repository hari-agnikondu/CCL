package com.incomm.cclpvms.config.validator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.incomm.cclpvms.config.model.GroupAccess;
import com.incomm.cclpvms.config.model.Product;
import com.incomm.cclpvms.config.model.Purse;

/*
 * This service class performs validation checks for customized field level validation.
 */


@Component
@PropertySource("classpath:serverValidation.properties")
public class ValidationGroupAccessImpl implements Validator{
	
	boolean Errorflag = false;
	@Autowired
	Environment env;

	public void validate(Object target, Errors errors) 
	{
		
		
		String patternAlphawithspace= "^[A-Za-z0-9 ]+$" ;
		GroupAccess groupAccess = (GroupAccess) target;
		String[] partners = groupAccess.getSelectedPartnerList().toArray(new String[0]);
		
	
		if(groupAccess.getGroupAccessName() == null || groupAccess.getGroupAccessName().trim().isEmpty() )
		{
			errors.rejectValue("groupAccessName", "messageNotEmpty.addGroupAccessId.groupAccessName", "Please enter Group Access Name");
			Errorflag = true;
		}
		else if((groupAccess.getGroupAccessName()!=null && !groupAccess.getGroupAccessName().trim().isEmpty()) && ((groupAccess.getGroupAccessName().length()) >51 ||(groupAccess.getGroupAccessName().length()) < 2) )
		{
			errors.rejectValue("groupAccessName", "messageLength.addGroupAccessId.groupAccessName", "Group Access Name should contain minimum 2 characters and maximum 50 characters");
			Errorflag = true;
		}
		
		else if((groupAccess.getGroupAccessName()!=null && !groupAccess.getGroupAccessName().trim().isEmpty()) && !(groupAccess.getGroupAccessName()).matches(patternAlphawithspace))
		{
			errors.rejectValue("groupAccessName", "messagepattern.addGroupAccessId.groupAccessName", "Group Access Name should be Alphanumeric");
			Errorflag = true;
		}
		
		
		
		if(partners==null || partners.length==0 ) {
			
			errors.rejectValue("selectedPartnerList", "messageNotEmpty.addGroupAccessId.selectedPartnerList", "Please select Partners");
			Errorflag = true;

		}
		
				
		
		if (Errorflag)
		{
			return;
		}
		
		
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return false;
	}

}
