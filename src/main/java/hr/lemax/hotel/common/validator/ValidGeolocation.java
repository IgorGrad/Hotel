package hr.lemax.hotel.common.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = CoordinatesValidator.class)
@ReportAsSingleViolation
public @interface ValidGeolocation {
    String message() default "GEOLOCATION";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
