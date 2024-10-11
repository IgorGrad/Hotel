package hr.lemax.hotel.common.util;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Validator {

    /**
     * Validate geolocation coordinates of hotel
     *
     * @param longitude longitude coordinate
     * @param latitude latitude coordinate
     * @return {@code true} if coordinates are valid, else {@code false}
     */
    public static boolean areCoordinatesValid(
            @NonNull final Double longitude,
            @NonNull final Double latitude) {
        return latitude >= -90 && latitude <= 90
                && longitude >= -180 && longitude <= 180;
    }
}
