package tm.ugur.util.validator.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoZeroValidator implements ConstraintValidator<NonZero, Double> {

    @Override
    public void initialize(NonZero constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        return value != null && value != 0.0;
    }
}
