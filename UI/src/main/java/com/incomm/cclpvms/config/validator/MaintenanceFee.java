package com.incomm.cclpvms.config.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = ValidateMaintenanceFee.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)

public @interface MaintenanceFee {

	 String message() default "Invalid Data Entered";
	    Class<? extends Payload>[] payload() default {};
	    Class<?>[] groups() default {};
}
