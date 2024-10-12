package hr.lemax.hotel.dto;

import hr.lemax.hotel.common.enums.Messages;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserGeoModificationDTOTest {
    private static Validator validator;
    private static final Double VALID_LONGITUDE = 45.123;
    private static final Double VALID_LATITUDE = -45.123;
    private static final Double INVALID_LONGITUDE = 200.0;
    private static final Double INVALID_LATITUDE = -100.0;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidHotelModificationDTO() {
        final UserGeoModificationDTO dto = new UserGeoModificationDTO(VALID_LONGITUDE, VALID_LATITUDE);
        final Set<ConstraintViolation<UserGeoModificationDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Validation should not fail for valid DTO");
    }

    @Test
    void testMissingLongitude() {
        final UserGeoModificationDTO dto = new UserGeoModificationDTO( null, VALID_LATITUDE);
        final Set<ConstraintViolation<UserGeoModificationDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals(Messages.Error.User.USER_LONGITUDE_REQUIRED, violations.iterator().next().getMessage());
    }

    @Test
    void testMissingLatitude() {
        final UserGeoModificationDTO dto = new UserGeoModificationDTO( VALID_LONGITUDE, null);
        final Set<ConstraintViolation<UserGeoModificationDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals(Messages.Error.User.USER_LATITUDE_REQUIRED, violations.iterator().next().getMessage());
    }

    @Test
    void testMissingAll() {
        final UserGeoModificationDTO dto = new UserGeoModificationDTO(null, null);
        final Set<ConstraintViolation<UserGeoModificationDTO>> violations = validator.validate(dto);
        assertEquals(2, violations.size());
    }

    @Test
    void testInvalidGeolocation() {
        UserGeoModificationDTO dto = new UserGeoModificationDTO(INVALID_LONGITUDE, INVALID_LATITUDE);
        Set<ConstraintViolation<UserGeoModificationDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals(Messages.Error.User.USER_GEOLOCATION_INVALID, violations.iterator().next().getMessage());
    }
}
