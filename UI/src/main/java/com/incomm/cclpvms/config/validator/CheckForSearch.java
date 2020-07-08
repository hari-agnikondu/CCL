package com.incomm.cclpvms.config.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;


@Documented
@Constraint(validatedBy = CheckForSearchValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckForSearch {
	
	String message() default "Invalid Data Entered";

    String messageLength() default "";
    
    String messagepattern() default "";
    
    String messagenotzero() default "";

    int min() default Integer.MIN_VALUE;

    int max() default Integer.MAX_VALUE;
     
    String pattern() default "";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};

}
