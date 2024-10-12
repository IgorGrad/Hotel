package hr.lemax.hotel.common.strategy;

import hr.lemax.hotel.model.Hotel;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;

import static hr.lemax.hotel.common.util.GeoUtils.calculateDistance;

@Slf4j
public class SortByDistanceAndPrice implements HotelSortStrategy {
    @Override
    public List<Hotel> sort(
            final List<Hotel> hotels,
            final Double currentLon,
            final Double currentLat) {
        return hotels.stream().peek(
                hotel -> {
                    double distance = calculateDistance(currentLon, currentLat, hotel.getLongitude(), hotel.getLatitude());
                    double roundedDistance = Math.round(distance * 100.0) / 100.0;
                    log.debug("Calculating distance for hotel: {}", hotel.getName());
                    log.debug("Distance: {} -> Rounded distance: {}", distance, roundedDistance);

                    hotel.setDistance(roundedDistance);
                }).sorted(Comparator.comparingDouble(Hotel::getDistance)
                .thenComparingDouble(Hotel::getPrice)).toList();
    }
}
