package tm.ugur.util.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NoZeroValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NonZero {

    String message() default "Value must not be zero";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
