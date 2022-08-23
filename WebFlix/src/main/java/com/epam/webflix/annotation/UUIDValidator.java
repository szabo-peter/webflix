package com.epam.webflix.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class UUIDValidator implements ConstraintValidator<UUID, String> {

    private static final String REGULAR_EXPRESSION = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";

    @Override
    public boolean isValid(String id, ConstraintValidatorContext constraintValidatorContext) {

        Pattern pattern = Pattern.compile(REGULAR_EXPRESSION);

        return pattern.matcher(id).matches();
    }


}
