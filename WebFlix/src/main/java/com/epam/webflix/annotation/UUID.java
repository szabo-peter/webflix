package com.epam.webflix.annotation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UUIDValidator.class)
public @interface UUID {

    String message() default "Invalid UUID: must be a valid UUID format!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
