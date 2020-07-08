
package com.incomm.cclpvms.config.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

@Component
public class CustomValidator implements ConstraintValidator<FieldValidation, Object> {

	private Boolean notEmpty;
	private String pattern;
	private Integer min;
	private Integer max;
	private String messageNotEmpty;
	private String messageLength;
	private String messagePattern;
	private String startsWithSpace;
	

	public void initialize(FieldValidation field) {
		
		notEmpty = field.notEmpty();		
		min = field.min();
		max = field.max();
		pattern = field.pattern();
		messageNotEmpty = field.messageNotEmpty();
		messageLength = field.messageLength();
		messagePattern = field.messagePattern();
		startsWithSpace = field.startsWithSpace();
		
	}

	public boolean isValid(Object value, ConstraintValidatorContext context) {
		
		context.disableDefaultConstraintViolation();
		String val;
		if(value!=null) {
		   val = String.valueOf(value);
		}else {
	       val ="";
		}
			

		if (notEmpty && val.isEmpty()) {
			context.buildConstraintViolationWithTemplate(messageNotEmpty).addConstraintViolation();
			return false;
		}
		if (notEmpty &&  val.startsWith(" ")) {
			context.buildConstraintViolationWithTemplate(startsWithSpace).addConstraintViolation();
			return false;
		}
		if (!pattern.isEmpty() && val.length() > 0) {
			if (!(val.matches(pattern))) {
				context.buildConstraintViolationWithTemplate(messagePattern).addConstraintViolation();

				return false;
			}
		}
		if (!notEmpty && val.length()>0) {
			if ((min > 0 || max < Integer.MAX_VALUE) && (val.length() < min || val.length() > max)) {
				context.buildConstraintViolationWithTemplate(messageLength).addConstraintViolation();
				return false;
			}
		}
		if (notEmpty && val.length()>0) {
			if ((min > 0 || max < Integer.MAX_VALUE) && (val.length() < min || val.length() > max)) {
				context.buildConstraintViolationWithTemplate(messageLength).addConstraintViolation();
				return false;
			}
		}
		

		
		return true;
	}

}