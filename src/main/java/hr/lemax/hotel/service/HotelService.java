package hr.lemax.hotel.service;

import hr.lemax.hotel.dto.HotelModificationDTO;
import hr.lemax.hotel.model.Hotel;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static hr.lemax.hotel.common.util.GeoUtils.calculateDistance;

@Service
@Slf4j
public class HotelService implements IHotelService {
    private final List<Hotel> hotels;
    private long idCounter;
    private final ModelMapper mapper;

    public HotelService(final ModelMapper mapper) {
        this.mapper = mapper;
        this.idCounter = 1;
        this.hotels = new ArrayList<>();
    }

    /**
     * Get all Hotels
     *
     * @return {@link List} of all Entities
     */
    @Override
    public List<Hotel> getAllHotels() {
        log.trace("getAllHotels() called");
        return hotels;
    }

    /**
     * Get {@link Optional} Hotel by ID
     *
     * @param id Hotel ID
     * @return {@link Optional} Hotel
     */
    @Override
    public Optional<Hotel> getHotelById(@NonNull final Long id) {
        log.trace("getHotelById() called with ID: {}", id);

        return hotels.stream()
                .filter(hotel -> hotel.getId().equals(id))
                .findFirst();
    }

    /**
     * Add {@link Hotel} to list of hotels
     *
     * @param hotelDto Hotel data
     * @return Saved Hotel
     */
    @Override
    public Hotel addHotel(@NonNull final HotelModificationDTO hotelDto) {
        log.trace("addHotel() called for ID: {} with data: {}", idCounter, hotelDto);

        Hotel hotel = mapper.map(hotelDto, Hotel.class);
        hotel.setId(idCounter);

        hotels.add(hotel);
        // If everything is fine increment id counter
        idCounter++;
        return hotel;
    }

    /**
     * Update {@link Hotel} to list of hotels
     *
     * @param updatedHotelDto Hotel data for update
     * @param id Hotel data for update
     * @return Saved Hotel
     */
    @Override
    public Hotel updateHotel(
            @NonNull final HotelModificationDTO updatedHotelDto,
            @NonNull final Long id) {
        log.trace("updateHotel() called for ID: {} with data: {}", id, updatedHotelDto);

        // Fetch existing hotel
        Hotel hotel = getHotelById(id).orElseThrow();
        log.trace("Existing hotel fetched: {}", hotel);

        // Map (update) fields from update DTO to hotel
        mapper.map(updatedHotelDto, hotel);

        return hotel;
    }

    /**
     * Delete {@link Hotel} with given ID from list of hotels
     *
     * @param id of hotel to remove
     */
    @Override
    public void deleteHotel(@NonNull final Long id) {
        log.trace("delete() called with ID: {}", id);

        final Hotel hotel = getHotelById(id).orElseThrow();
        log.trace("Hotel fetched: {}", hotel);

        log.trace("Deleting hotel: {}", hotel);
        hotels.removeIf(hotelForRemove -> hotelForRemove.getId().equals(id));
    }

    /**
     * Searches for hotels based on the user's current geographical location.
     * The method returns a list of hotels sorted primarily by distance from the user's location
     * and secondarily by price. The sorting ensures that hotels closer to the user and cheaper
     * are listed first.
     *
     * @param currentLon Longitude of the user's current location (X axis).
     * @param currentLat Latitude of the user's current location (Y axis).
     * @return A sorted list of {@link Hotel} objects, first by distance and then by price.
 */
    @Override
    public List<Hotel> searchHotels(
            @NonNull final Double currentLon,
            @NonNull final Double currentLat) {

        return hotels.stream()
                .sorted(Comparator.<Hotel>comparingDouble(h -> {
                    final double distance = calculateDistance(currentLon, currentLat, h.getLongitude(), h.getLatitude());
                    final double roundedDistance = Math.round(distance * 100.0) / 100.0;
                    h.setDistance(roundedDistance);
                    return roundedDistance;
                }).thenComparingDouble(Hotel::getPrice))
                .toList();
    }

}
