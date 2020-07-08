package com.incomm.cclpvms.config.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.incomm.cclpvms.config.model.MerchantProduct;




@Component
@PropertySource("classpath:ValidationMessages.properties")
public class ValidateMerchantProduct implements Validator {
	boolean errorFlag = false;

	@Autowired
	Environment env;

	@Override
	public boolean supports(Class<?> clazz) {
		return MerchantProduct.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		MerchantProduct merchantProduct = (MerchantProduct) target;
		
	String merchantName= merchantProduct.getMerchantName();
	String productName=  merchantProduct.getProductName();
	
		
		if (merchantName.equalsIgnoreCase("-1,")) {
			errors.rejectValue("merchantName", "messageNotEmpty.merchantProduct.merchantName",
					"error");
			errorFlag = true;

		}
		
		
		if (productName.equalsIgnoreCase("-1,")) {
			errors.rejectValue("productName", "messageNotEmpty.merchantProduct.productName",
					"error");
			errorFlag = true;

		}
		
		if (errorFlag) {
			return;
		}
		
	}

}
