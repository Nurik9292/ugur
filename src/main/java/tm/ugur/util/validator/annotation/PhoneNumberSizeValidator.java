package tm.ugur.util.validator.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class PhoneNumberSizeValidator implements ConstraintValidator<ValidPhoneSize, List<String>> {

    @Override
    public void initialize(ValidPhoneSize constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(List<String> phoneNumbers, ConstraintValidatorContext constraintValidatorContext) {
        if (phoneNumbers == null) {
            return true;
        }

        for (String phoneNumber : phoneNumbers) {
                if(phoneNumber.length() != 12)
                    return false;
        }

        return true;
    }
}
