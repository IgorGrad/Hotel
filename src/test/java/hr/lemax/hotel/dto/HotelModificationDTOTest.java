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

public class HotelModificationDTOTest {
    private static Validator validator;

    private static final String VALID_HOTEL_NAME = "Hilton Hotel";
    private static final Double VALID_PRICE = 100.0;
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
        final HotelModificationDTO dto = new HotelModificationDTO(VALID_HOTEL_NAME, VALID_PRICE, VALID_LONGITUDE, VALID_LATITUDE);
        final Set<ConstraintViolation<HotelModificationDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Validation should not fail for valid DTO");
    }

    @Test
    void testMissingHotelName() {
        final HotelModificationDTO dto = new HotelModificationDTO(null, VALID_PRICE, VALID_LONGITUDE, VALID_LATITUDE);
        final Set<ConstraintViolation<HotelModificationDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals(Messages.Error.Hotel.HOTEL_NAME_REQUIRED, violations.iterator().next().getMessage());
    }

    @Test
    void testMissingPrice() {
        final HotelModificationDTO dto = new HotelModificationDTO(VALID_HOTEL_NAME, null, VALID_LONGITUDE, VALID_LATITUDE);
        final Set<ConstraintViolation<HotelModificationDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals(Messages.Error.Hotel.HOTEL_PRICE_REQUIRED, violations.iterator().next().getMessage());
    }

    @Test
    void testMissingLongitude() {
        final HotelModificationDTO dto = new HotelModificationDTO(VALID_HOTEL_NAME, VALID_PRICE, null, VALID_LATITUDE);
        final Set<ConstraintViolation<HotelModificationDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals(Messages.Error.Hotel.HOTEL_LONGITUDE_REQUIRED, violations.iterator().next().getMessage());
    }

    @Test
    void testMissingLatitude() {
        final HotelModificationDTO dto = new HotelModificationDTO(VALID_HOTEL_NAME, VALID_PRICE, VALID_LONGITUDE, null);
        final Set<ConstraintViolation<HotelModificationDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals(Messages.Error.Hotel.HOTEL_LATITUDE_REQUIRED, violations.iterator().next().getMessage());
    }

    @Test
    void testMissingAll() {
        final HotelModificationDTO dto = new HotelModificationDTO(null, null, null, null);
        final Set<ConstraintViolation<HotelModificationDTO>> violations = validator.validate(dto);
        assertEquals(4, violations.size());
    }

    @Test
    void testNegativePrice() {
        HotelModificationDTO dto = new HotelModificationDTO(VALID_HOTEL_NAME, -VALID_PRICE, VALID_LONGITUDE, VALID_LATITUDE);
        Set<ConstraintViolation<HotelModificationDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals(Messages.Error.Hotel.HOTEL_PRICE_POSITIVE, violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidGeolocation() {
        HotelModificationDTO dto = new HotelModificationDTO(VALID_HOTEL_NAME, VALID_PRICE, INVALID_LONGITUDE, INVALID_LATITUDE);
        Set<ConstraintViolation<HotelModificationDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals(Messages.Error.Hotel.HOTEL_GEOLOCATION_INVALID, violations.iterator().next().getMessage());
    }
}
