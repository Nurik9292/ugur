package tm.ugur.util.validator.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, List<String>> {

    private Pattern pattern;

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
        pattern = Pattern.compile(constraintAnnotation.regexp());
    }

    @Override
    public boolean isValid(List<String> phoneNumbers, ConstraintValidatorContext constraintValidatorContext) {
        if (phoneNumbers == null) {
            return true;
        }

        for (String phoneNumber : phoneNumbers) {
            if (!pattern.matcher(phoneNumber).matches()) {
                return false;
            }
        }

        return true;
    }
}
