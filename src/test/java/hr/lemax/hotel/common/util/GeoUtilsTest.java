package hr.lemax.hotel.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GeoUtilsTest {
    @Test
    public void testCalculateDistance() {
        // Test case 1: Same location (distance should be 0)
        double distance = GeoUtils.calculateDistance(0.0, 0.0, 0.0, 0.0);
        assertEquals(0.0, distance, 0.01, "Distance should be 0 for the same location");

        // Test case 2: Distance between two locations (close by)
        distance = GeoUtils.calculateDistance(0.0, 0.0, 1.0, 1.0);
        assertEquals(157.2, distance, 0.1, "Distance should be around 157.2 km");

        // Test case 3: Distance between two far locations
        distance = GeoUtils.calculateDistance(-73.9352, 40.7306, 139.6917, 35.6894);
        assertEquals(10849.8, distance, 0.1, "Distance between New York and Tokyo should be around 108498 km");

        // Test case 4: Negative values for latitudes and longitudes (Southern and Western hemispheres)
        distance = GeoUtils.calculateDistance(-45.0, -45.0, -50.0, -50.0);
        assertEquals(670.6, distance, 0.1, "Distance between points in Southern and Western hemispheres should be around 670 km");

        // Test case 5: Edge case for maximum valid coordinates (e.g., poles)
        distance = GeoUtils.calculateDistance(0.0, 0.0, 180.0, 90.0); // Equator to North Pole
        assertEquals(10007.5, distance, 0.1, "Distance between the equator and North Pole should be around 10007.5 km");
    }
}
