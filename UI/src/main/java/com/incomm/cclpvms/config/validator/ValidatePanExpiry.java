package com.incomm.cclpvms.config.validator;

import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.incomm.cclpvms.config.model.Product;

@Component
@PropertySource("classpath:ValidationMessages.properties")
public class ValidatePanExpiry implements Validator{

	boolean Errorflag = false;
	public static final String ERRORS="error";
	@Autowired
	Environment env;

	@Override
	public boolean supports(Class<?> clazz) {
		return Product.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		Product product = (Product)target;
		
	
	Long productId=  product.getProductId();
	
	String patterNumeric="^[0-9]+$";
	
	if (Objects.isNull(productId) || productId.equals(Long.parseLong("-1"))) {
		errors.rejectValue("productId", "messageNotEmpty.merchantProduct.productName",
				ERRORS);
		Errorflag = true;

	}
	

	 Map<String, Object> productMap = product.getProductAttributes();
	for (String key : productMap.keySet()) {
		String value = (String) productMap.get(key);
		if (key.equalsIgnoreCase("January")) {
			

				if (!(value).isEmpty()&&!(value).matches(patterNumeric)) {
					errors.rejectValue("productAttributes['January']",
							"messageNotEmpty.product.january", ERRORS);
					Errorflag = true;

				}
				
				else if(!(value).isEmpty() && value.length()>2) {
					errors.rejectValue("productAttributes['January']",
							"messageLength.product.january", ERRORS);
					Errorflag = true;
					
					
				}
		
	}
		if (key.equalsIgnoreCase("February")) {
			
			
							if (!(value).isEmpty()&&!(value).matches(patterNumeric)) {
								errors.rejectValue("productAttributes['February']",
										"messageNotEmpty.product.February", ERRORS);
								Errorflag = true;

							}
							
							else if(!(value).isEmpty() && value.length()>2) {
								errors.rejectValue("productAttributes['February']",
										"messageLength.product.February", ERRORS);
								Errorflag = true;
								
								
							}
					
				}
		if (key.equalsIgnoreCase("March")) {
			
			
							if (!(value).isEmpty()&&!(value).matches(patterNumeric)) {
								errors.rejectValue("productAttributes['March']",
										"messageNotEmpty.product.March", ERRORS);
								Errorflag = true;

							}
							
							else if(!(value).isEmpty() && value.length()>2) {
								errors.rejectValue("productAttributes['March']",
										"messageLength.product.March", ERRORS);
								Errorflag = true;
								
								
							}
					
				}
		if (key.equalsIgnoreCase("April")) {
			
			
							if (!(value).isEmpty()&&!(value).matches(patterNumeric)) {
								errors.rejectValue("productAttributes['April']",
										"messageNotEmpty.product.April", ERRORS);
								Errorflag = true;

							}
							
							else if(!(value).isEmpty() && value.length()>2) {
								errors.rejectValue("productAttributes['April']",
										"messageLength.product.April", ERRORS);
								Errorflag = true;
								
								
							}
					
				}
		if (key.equalsIgnoreCase("May")) {
			
			
							if (!(value).isEmpty()&&!(value).matches(patterNumeric)) {
								errors.rejectValue("productAttributes['May']",
										"messageNotEmpty.product.May", ERRORS);
								Errorflag = true;

							}
							
							else if(!(value).isEmpty() && value.length()>2) {
								errors.rejectValue("productAttributes['May']",
										"messageLength.product.May", ERRORS);
								Errorflag = true;
								
								
							}
					
				}
		if (key.equalsIgnoreCase("June")) {
			
			
							if (!(value).isEmpty()&&!(value).matches(patterNumeric)) {
								errors.rejectValue("productAttributes['June']",
										"messageNotEmpty.product.June", ERRORS);
								Errorflag = true;

							}
							
							else if(!(value).isEmpty() && value.length()>2) {
								errors.rejectValue("productAttributes['June']",
										"messageLength.product.June", ERRORS);
								Errorflag = true;
								
								
							}
					
				}
		if (key.equalsIgnoreCase("July")) {
			
			
							if (!(value).isEmpty()&&!(value).matches(patterNumeric)) {
								errors.rejectValue("productAttributes['July']",
										"messageNotEmpty.product.July", ERRORS);
								Errorflag = true;

							}
							
							else if(!(value).isEmpty() && value.length()>2) {
								errors.rejectValue("productAttributes['July']",
										"messageLength.product.July", ERRORS);
								Errorflag = true;
								
								
							}
					
				}
		if (key.equalsIgnoreCase("August")) {
			
	
							if (!(value).isEmpty()&&!(value).matches(patterNumeric)) {
								errors.rejectValue("productAttributes['August']",
										"messageNotEmpty.product.August", ERRORS);
								Errorflag = true;

							}
							
							else if(!(value).isEmpty() && value.length()>2) {
								errors.rejectValue("productAttributes['August']",
										"messageLength.product.August", ERRORS);
								Errorflag = true;
								
								
							}
					
				}
		if (key.equalsIgnoreCase("September")) {
			
		
							if (!(value).isEmpty()&&!(value).matches(patterNumeric)) {
								errors.rejectValue("productAttributes['September']",
										"messageNotEmpty.product.September", ERRORS);
								Errorflag = true;

							}
							
							else if(!(value).isEmpty() && value.length()>2) {
								errors.rejectValue("productAttributes['September']",
										"messageLength.product.September", ERRORS);
								Errorflag = true;
								
								
							}
					
				}
		if (key.equalsIgnoreCase("October")) {
			
			
							if (!(value).isEmpty()&&!(value).matches(patterNumeric)) {
								errors.rejectValue("productAttributes['October']",
										"messageNotEmpty.product.October", ERRORS);
								Errorflag = true;

							}
							
							else if(!(value).isEmpty() && value.length()>2) {
								errors.rejectValue("productAttributes['October']",
										"messageLength.product.October", ERRORS);
								Errorflag = true;
								
								
							}
					
				}
		if (key.equalsIgnoreCase("November")) {
	
							if (!(value).isEmpty()&&!(value).matches(patterNumeric)) {
								errors.rejectValue("productAttributes['November']",
										"messageNotEmpty.product.November", ERRORS);
								Errorflag = true;

							}
							
							else if(!(value).isEmpty() && value.length()>2) {
								errors.rejectValue("productAttributes['November']",
										"messageLength.product.November", ERRORS);
								Errorflag = true;
								
								
							}
					
				}
		if (key.equalsIgnoreCase("December")) {
			
			
							if (!(value).isEmpty()&&!(value).matches(patterNumeric)) {
								errors.rejectValue("productAttributes['December']",
										"messageNotEmpty.product.December", ERRORS);
								Errorflag = true;

							}
							
							else if(!(value).isEmpty() && value.length()>2) {
								errors.rejectValue("productAttributes['December']",
										"messageLength.product.December", ERRORS);
								Errorflag = true;
								
								
							}
					
				}
			
	
	
	
	
		
	}
	if (Errorflag) {
		return;
	}


}
}
