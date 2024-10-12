package hr.lemax.hotel.common.strategy;

import hr.lemax.hotel.model.Hotel;

import java.util.List;

public interface HotelSortStrategy {
    List<Hotel> sort(List<Hotel> hotels, Double currentLon, Double currentLat);
}
