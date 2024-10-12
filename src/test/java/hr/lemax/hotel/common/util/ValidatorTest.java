package hr.lemax.hotel.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidatorTest {
    @Test
    public void testAreCoordinatesValid() {
        assertTrue(Validator.areCoordinatesValid(0.0, 0.0));

        assertTrue(Validator.areCoordinatesValid(10.0, 10.0));
        assertTrue(Validator.areCoordinatesValid(-10.0, 10.0));
        assertTrue(Validator.areCoordinatesValid(10.0, -10.0));

        assertTrue(Validator.areCoordinatesValid(180.0, 90.0));
        assertTrue(Validator.areCoordinatesValid(-180.0, 90.0));
        assertTrue(Validator.areCoordinatesValid(180.0, -90.0));
        assertTrue(Validator.areCoordinatesValid(-180.0, -90.0));

        assertFalse(Validator.areCoordinatesValid(200.0, 10.0));
        assertFalse(Validator.areCoordinatesValid(200.0, -10.0));
        assertFalse(Validator.areCoordinatesValid(10.0, 100.0));
        assertFalse(Validator.areCoordinatesValid(-10.0, -100.0));

        assertFalse(Validator.areCoordinatesValid(200.0, 100.0));
        assertFalse(Validator.areCoordinatesValid(200.0, -100.0));
        assertFalse(Validator.areCoordinatesValid(-200.0, 100.0));
        assertFalse(Validator.areCoordinatesValid(-200.0, -100.0));
    }
}
