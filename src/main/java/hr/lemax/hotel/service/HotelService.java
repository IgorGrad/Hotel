package hr.lemax.hotel.service;

import hr.lemax.hotel.common.exception.HotelNotFoundException;
import hr.lemax.hotel.common.strategy.HotelSortStrategy;
import hr.lemax.hotel.dto.HotelModificationDTO;
import hr.lemax.hotel.model.Hotel;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
        try {
            log.info("getAllHotels() called");
            return hotels;
        } catch (final Exception e) {
            log.error("Error while fetching hotels: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Get {@link Optional} Hotel by ID
     *
     * @param id Hotel ID
     * @return {@link Optional} Hotel
     */
    @Override
    public Optional<Hotel> getHotelById(@NonNull final Long id) {
        try {
            log.info("getHotelById() called with ID: {}", id);

            return hotels.stream()
                    .filter(hotel -> hotel.getId().equals(id))
                    .findFirst();
        } catch (final Exception e) {
            log.error("Error while fetching hotel by ID: {}, error: {}", id, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Add {@link Hotel} to list of hotels
     *
     * @param hotelDto Hotel data
     * @return Saved Hotel
     */
    @Override
    public Hotel addHotel(@NonNull final HotelModificationDTO hotelDto) {
        try {
            log.debug("addHotel() called for ID: {} with data: {}", idCounter, hotelDto);

            final Hotel hotel = mapper.map(hotelDto, Hotel.class);
            hotel.setId(idCounter);

            hotels.add(hotel);
            // If everything is fine increment id counter
            idCounter++;
            return hotel;
        } catch (final Exception e) {
            log.error("Error while add new hotel: {}", e.getMessage());
            throw new RuntimeException(e);
        }
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
        try {
            log.debug("updateHotel() called for ID: {} with data: {}", id, updatedHotelDto);

            // Fetch existing hotel
            final Hotel hotel = getHotelById(id).orElseThrow();
            log.debug("Existing hotel fetched: {}", hotel);

            // Map (update) fields from update DTO to hotel
            mapper.map(updatedHotelDto, hotel);

            return hotel;
        } catch (final NoSuchElementException e) {
            log.error("Error while fetching hotel with id: {}, error: {}", id, e.getMessage());
            throw new HotelNotFoundException(id);
        } catch (final Exception e) {
            log.error("Error while update hotel with id: {}, error: {}", id, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete {@link Hotel} with given ID from list of hotels
     *
     * @param id of hotel to remove
     */
    @Override
    public void deleteHotel(@NonNull final Long id) {
        try {
            log.info("delete() called with ID: {}", id);

            final Hotel hotel = getHotelById(id).orElseThrow();

            log.debug("Deleting hotel: {}", hotel);
            hotels.removeIf(hotelForRemove -> hotelForRemove.getId().equals(id));
        } catch (final NoSuchElementException e) {
            log.error("Error while fetching hotel with id: {}, error: {}", id, e.getMessage());
            throw new HotelNotFoundException(id);
        } catch (Exception e) {
            log.error("Error while deleting hotel with ID: {}, error: {}", id, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Searches for hotels based on the user's current geographical location.
     * The method returns a list of hotels sorted primarily by distance from the user's location
     * and secondarily by price. The sorting ensures that hotels closer to the user and cheaper
     * are listed first.
     *
     * @param currentLon Longitude of the user's current location (X axis).
     * @param currentLat Latitude of the user's current location (Y axis).
     * @param sortStrategy Strategy of sorting hotels.
     * @return A sorted list of {@link Hotel} objects, first by distance and then by price.
 */
    @Override
    public List<Hotel> searchHotels(
            @NonNull final Double currentLon,
            @NonNull final Double currentLat,
            @NotNull final HotelSortStrategy sortStrategy) {
        try {
            return sortStrategy.sort(hotels, currentLon, currentLat);
        } catch (final Exception e) {
            log.error("Error while searching hotels: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Searches for hotels based on the user's current geographical location.
     * The method returns a paginated and sorted list of hotels, sorted primarily by distance
     * from the user's location and secondarily by price. The pagination ensures a manageable
     * number of results are returned.
     *
     * @param currentLon Longitude of the user's current location (X axis).
     * @param currentLat Latitude of the user's current location (Y axis).
     * @param sortStrategy Strategy of sorting hotels.
     * @param pageable Pageable object for pagination, containing page number, size, and sort.
     * @return A paginated and sorted list of {@link Hotel} objects, first by distance and then by price.
     */
    @Override
    public Page<Hotel> searchHotelsByPage(
            @NonNull final Double currentLon,
            @NonNull final Double currentLat,
            @NotNull final HotelSortStrategy sortStrategy,
            @NonNull final Pageable pageable) {
        try {
            // Sort hotels using the given sorting strategy
            final List<Hotel> sortedHotels = sortStrategy.sort(hotels, currentLon, currentLat);

            // Create a paginated view of the sorted hotels list
            int pageSize = pageable.getPageSize();
            int currentPage = pageable.getPageNumber();
            int startItem = currentPage * pageSize;

            List<Hotel> paginatedHotels;

            // Check if the starting item index exceeds the size of the list
            if (sortedHotels.size() < startItem) {
                paginatedHotels = Collections.emptyList(); // No hotels for this page
            } else {
                int toIndex = Math.min(startItem + pageSize, sortedHotels.size());
                paginatedHotels = sortedHotels.subList(startItem, toIndex);
            }

            return new PageImpl<>(paginatedHotels, pageable, sortedHotels.size());
        } catch (final Exception e) {
            log.error("Error while searching hotels: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
