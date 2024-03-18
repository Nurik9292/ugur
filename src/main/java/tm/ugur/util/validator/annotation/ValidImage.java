package tm.ugur.util.validator.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ImageValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidImage {

    String message() default "Неверный формат или размер изображения";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
