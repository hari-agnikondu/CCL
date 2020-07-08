package com.incomm.cclpvms.config.validator;

import java.util.Map;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

public class CheckMonthlyFeeCapMap  implements ConstraintValidator<MonthlyFeeCapFee, Map<String, Object>> {
    
	private boolean errorFlag = true;
	

	public void initialize(MonthlyFeeCapFee chk) {
		
        
	}
	
	public boolean isValid(Map<String, Object> value, ConstraintValidatorContext context) {
	
		value.entrySet().stream().forEach(p -> {
			
			if(p.getKey().contains("feeDesc")) {
				if(p.getValue().toString().isEmpty()) {
					context.buildConstraintViolationWithTemplate( "{monthlyFeeCap_feeDesc.empty}" )
			        .addPropertyNode( null ).inIterable().atKey(p.getKey())
			        .addConstraintViolation(); 
					errorFlag = false;
					
				}else if(p.getValue().toString().length() < 1 || p.getValue().toString().length() > 100) {
					context.buildConstraintViolationWithTemplate( "{monthlyFeeCap_feeDesc.length}" )
			        .addPropertyNode( null ).inIterable().atKey(p.getKey())
			        .addConstraintViolation();
					errorFlag = false;
					
				}
			}else if(p.getKey().contains("feeCapAmt")) {
				if(p.getValue().toString().isEmpty()) {
					context.buildConstraintViolationWithTemplate( "{monthlyFeeCap_feeCapAmt.empty}" )
			        .addPropertyNode( null ).inIterable().atKey(p.getKey())
			        .addConstraintViolation(); 
					errorFlag = false;
					
				}else if(!isValidAmount(p.getValue())) {
					context.buildConstraintViolationWithTemplate( "{monthlyFeeCap_feeCapAmt.invalid}" )
			        .addPropertyNode( null ).inIterable().atKey(p.getKey())
			        .addConstraintViolation(); 
					errorFlag = false;
					
				}
				
			}
		});
		
		return errorFlag;
	}

	static boolean isValidAmount(Object amount) {

		String regex = "^[0-9.]+$";
		return ((!amount.toString().matches(regex) || amount.toString().endsWith(".")
				|| StringUtils.countOccurrencesOf(amount.toString(), ".") > 1) || (amount.toString().length() > 10)
				|| (amount.toString().contains(".") && amount.toString().split("\\.")[1].length() > 3)) ? false : true;

	}

}
