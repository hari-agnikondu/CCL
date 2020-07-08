package com.incomm.cclpvms.config.validator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.incomm.cclpvms.config.model.Product;
import com.incomm.cclpvms.config.model.Purse;

/*
 * This service class performs validation checks for customized field level validation.
 */


@Component
@PropertySource("classpath:serverValidation.properties")
public class ValidationPurseImpl implements Validator{
	
	boolean Errorflag = false;
	
	@Autowired
	Environment env;

	public boolean supports(Class<?> clazz) 
	{
		
		return Product.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) 
	{
		String pattern = "^[0-9]+$";		
		
		Purse purse = (Purse) target;
		
		if(purse.getPurseTypeId()==-1)
		{
			Errorflag = true;
			errors.rejectValue("purseTypeId", "messagepattern.purse.purseTypeId", "Please Select one Purse Type Id");
		}
		
		if(purse.getPurseTypeId()==1 && "-1".equals(purse.getCurrencyTypeID()))
		{
			errors.rejectValue("currencyTypeID", "messagepattern.purse.currencyTypeID", "Please Select one Currency Code");
			Errorflag = true;
		}
		
		/*
		 * if(purse.getPurseTypeId()==3 && (purse.getUpc()==null ||
		 * "".equals(purse.getUpc()))) { errors.rejectValue("upc",
		 * "messagepattern.purse.upcEmpty", "Please Enter UPC"); Errorflag = true; }
		 */
		
		/*
		 * else if(purse.getPurseTypeId()==3 && !(purse.getUpc().matches(pattern))) {
		 * errors.rejectValue("upc", "messagepattern.purse.upcPattern",
		 * "UPC values should be Numeric"); Errorflag = true; }
		 */
		
		/*
		 * else if(purse.getPurseTypeId()==3 && purse.getUpc().length()!=12) {
		 * errors.rejectValue("upc", "messagepattern.purse.upcLength",
		 * "UPC Length should be 12"); Errorflag = true; }
		 */
		
		
		if (Errorflag)
		{
			return;
		}
		
		
	}

}
