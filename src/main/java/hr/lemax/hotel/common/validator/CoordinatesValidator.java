package hr.lemax.hotel.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static hr.lemax.hotel.common.util.Validator.areCoordinatesValid;

@Slf4j
public class CoordinatesValidator implements ConstraintValidator<ValidGeolocation, Object> {
    @Override
    public void initialize(ValidGeolocation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validates the coordinates (latitude and longitude) of the given object.
     * This method uses reflection to retrieve the values of the "getLatitude" and "getLongitude" methods
     * from the object and checks if they fall within valid geographical ranges.
     *
     * @param obj the object to validate, expected to have "getLatitude" and "getLongitude" methods.
     * @param context the context in which the constraint is evaluated, allowing custom error reporting.
     *
     * @return {@code true} if the coordinates are valid or null (which is handled by @NotNull validation elsewhere),
     *         {@code false} otherwise. If reflection fails (i.e., the object does not have the required methods),
     *         validation will fail and return {@code false}.
     *
     * @throws NoSuchMethodException if the object does not have "getLatitude" or "getLongitude" methods.
     * @throws InvocationTargetException if there is an issue with invoking the getter methods via reflection.
     * @throws IllegalAccessException if the method cannot be accessed due to security constraints.
     */
    @Override
    public boolean isValid(
            final Object obj,
            final ConstraintValidatorContext context) {
        try {
            // Reflection to get the latitude and longitude values
            final Method getLatitude = obj.getClass().getMethod("getLatitude");
            final Method getLongitude = obj.getClass().getMethod("getLongitude");

            final Double latitude = (Double) getLatitude.invoke(obj);
            final Double longitude = (Double) getLongitude.invoke(obj);

            // If either coordinate is null, rely on @NotNull validation
            if (latitude == null || longitude == null) {
                return true;
            }

            // Validate coordinates
            return areCoordinatesValid(longitude, latitude);
        } catch (final NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            log.error("Error while validate coordinates: {}", e.getMessage());
            return false;
        }
    }
}
