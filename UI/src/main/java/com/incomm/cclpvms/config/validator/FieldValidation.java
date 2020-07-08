package com.incomm.cclpvms.config.validator;
 
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
 
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
@Target(ElementType.FIELD)
@Constraint(validatedBy = CustomValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
@Documented
public @interface FieldValidation {
    
    String message() default "";
    String messageNotEmpty() default "";
    String messagePattern() default "";
 
    String messageLength() default "Wrong length of field";
 
    boolean notEmpty() default false;
    
    String startsWithSpace() default "";
 
    int min() default 0;
 
    int max() default Integer.MAX_VALUE;
    String pattern() default "";
 
    Class<?>[] groups() default {};
 
    Class<? extends Payload>[] payload() default {};
}
