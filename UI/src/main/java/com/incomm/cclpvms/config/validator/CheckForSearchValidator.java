package com.incomm.cclpvms.config.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckForSearchValidator implements ConstraintValidator<CheckForSearch, Object>{
		
		    private Integer min;
		    private Integer max;
		    private String messageLength;
		    private String pattern;
		    private String messagepattern;

	   
		public void initialize(CheckForSearch chk) {
			
		        min = chk.min();
		        max = chk.max();
		        pattern=chk.pattern();
		        messageLength = chk.messageLength();
		        messagepattern = chk.messagepattern();
		}

		public boolean isValid(Object value, ConstraintValidatorContext cxt) {
			
			cxt.disableDefaultConstraintViolation();

				if(value==null||"".equals(value))
				{
					return true;
				}
			
					String val=String.valueOf(value);
					
					if (!(val).matches(pattern)) {
			             cxt.buildConstraintViolationWithTemplate(messagepattern).addConstraintViolation();
			            
			            return false;
			        }
	
					if((val).matches("^\\s*$"))
					{
						
						cxt.buildConstraintViolationWithTemplate(messagepattern).addConstraintViolation();
				        return false;
					}
					if(Character.isWhitespace((val).charAt(0)))
					{
						
						cxt.buildConstraintViolationWithTemplate(messagepattern).addConstraintViolation();
				        return false;
					}
					 if ((min > Integer.MIN_VALUE || max < Integer.MAX_VALUE) && ((val.length())< min || (val).length() > max)) {
						 cxt.buildConstraintViolationWithTemplate(messageLength).addConstraintViolation();
				         return false;
				    } 
				
			
			
				 return (val).matches("(?!^ +$)^.+$");
		 	
			
		}


		
	}


