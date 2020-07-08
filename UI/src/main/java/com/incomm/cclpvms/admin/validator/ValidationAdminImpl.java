package com.incomm.cclpvms.admin.validator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.incomm.cclpvms.admin.model.Group;
import com.incomm.cclpvms.config.model.Product;

/**
 * This service class performs validation checks for customized field level validation.
 */


@Component
@PropertySource("classpath:serverValidation.properties")
public class ValidationAdminImpl implements Validator{
	
	public static final String GROUP_NAME="groupName";
	
	boolean Errorflag = false;
	
	@Autowired
	Environment env;

	public boolean supports(Class<?> clazz) 
	{
		
		return Product.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) 
	{
		String pattern = "^[A-Za-z _]+$";		
		
		Group groupConfig = (Group) target;
		String[] roleName = groupConfig.getSelectedRoleList().toArray(new String[0]);	
		
		if("".equals(groupConfig.getGroupName()) && null==groupConfig.getGroupName())
		{
			errors.rejectValue(GROUP_NAME, "messageempty.group.groupName", "Please Enter Group Name");
			Errorflag = true;
		}
		else if (!groupConfig.getGroupName().matches(pattern))
		{
			errors.rejectValue(GROUP_NAME, "messagepattern.group.groupName", "Group Name contains Invalid Values");
			Errorflag = true;
		}
		else if(groupConfig.getGroupName().length()<2 && groupConfig.getGroupName().length()>100)
		{
			errors.rejectValue(GROUP_NAME, "messagelength.group.groupName", "Group Name should be minimum of 2 and maximum of 100 Characters");
			Errorflag = true;
		}
		
		
		
		if(roleName==null || roleName.length==0 ) {
			
			errors.rejectValue("selectedRoleList", "messageempty.group.roleList", "Please select Role Name");
				Errorflag = true;

			}
		
		
		if (Errorflag)
		{
			return;
		}
		
		
	}

}
