package tm.ugur.util.validator.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class ImageValidator implements ConstraintValidator<ValidImage, MultipartFile> {

    private static final List<String> ALLOWED_IMAGE_FORMATS = Arrays.asList("image/jpeg", "image/jpg", "image/png");
    private static final long MAX_IMAGE_SIZE_BYTES = 10 * 1024; // 1 MB
    @Override
    public void initialize(ValidImage constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile imageFile, ConstraintValidatorContext constraintValidatorContext) {
        if (imageFile == null || imageFile.isEmpty()) {
            return true; //
        }



        if (!ALLOWED_IMAGE_FORMATS.contains(imageFile.getContentType())) {
            return false;
        }

        if (imageFile.getSize() > MAX_IMAGE_SIZE_BYTES) {
            return false;
        }

        return true;
    }
}
