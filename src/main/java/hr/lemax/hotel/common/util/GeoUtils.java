package hr.lemax.hotel.common.util;

import lombok.extern.slf4j.Slf4j;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;

@Slf4j
public class GeoUtils {

    /**
     * Calculate the distance between two geographical points (user and hotel) using the Haversine formula.
     * This method returns the great-circle distance between the user's current location and the hotel location.
     *
     * @param userLon       Longitude of the user's current location (X axis)
     * @param userLat       Latitude of the user's current location (Y axis)
     * @param hotelLongitude Longitude of the hotel's location (X axis)
     * @param hotelLatitude  Latitude of the hotel's location (Y axis)
     * @return Distance in kilometers between the user and the hotel
     */
    public static double calculateDistance(
            final double userLon,
            final double userLat,
            final double hotelLongitude,
            final double hotelLatitude) {

        try {
            final double earthRadius = 6371; // kilometers
            final double lonDistance = toRadians(hotelLongitude - userLon);
            final double latDistance = toRadians(hotelLatitude - userLat);

            final double a = sin(latDistance / 2) * sin(latDistance / 2)
                    + cos(toRadians(userLat)) * cos(toRadians(hotelLatitude))
                    * sin(lonDistance / 2) * sin(lonDistance / 2);
            final double c = 2 * atan2(sqrt(a), sqrt(1 - a));
            return earthRadius * c;
        } catch (Exception e) {
            log.error("Error while calculating distance user: ({}, {}), error: ({}, {})", userLon, userLat, hotelLongitude, hotelLatitude );
            throw new RuntimeException(e);
        }
    }
}
