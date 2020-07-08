package com.incomm.cclpvms.config.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class EmptyCustomValidator implements ConstraintValidator<EmptyValidation, String> {
	
	private Boolean notEmpty;
	private String pattern;
	private Integer min;
	private Integer max;
	private String messageNotEmpty;
	private String messageLength;
	private String messagePattern;
	private String startsWithSpace;
	




	public void initialize(EmptyValidation field) {
		
		notEmpty = field.notEmpty();		
		min = field.min();
		max = field.max();
		pattern = field.pattern();
		messageNotEmpty = field.messageNotEmpty();
		messageLength = field.messageLength();
		messagePattern = field.messagePattern();
		startsWithSpace = field.startsWithSpace();
		
	}

	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		context.disableDefaultConstraintViolation();

		if (notEmpty && value.isEmpty()) {
			context.buildConstraintViolationWithTemplate(messageNotEmpty).addConstraintViolation();
			return false;
		}
		if (notEmpty &&  value.startsWith(" ")) {
			context.buildConstraintViolationWithTemplate(startsWithSpace).addConstraintViolation();
			return false;
		}
		if (!pattern.isEmpty() && value.length() > 0) {
			if (!(value.matches(pattern))) {
				context.buildConstraintViolationWithTemplate(messagePattern).addConstraintViolation();

				return false;
			}
		}
		
        if(!value.isEmpty()) {
			if ((min > 0 || max < Integer.MAX_VALUE) && (value.length() < min || value.length() > max)) {
				context.buildConstraintViolationWithTemplate(messageLength).addConstraintViolation();
				return false;
			}
        }
		
		return true;
	}

}
