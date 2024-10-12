package hr.lemax.hotel.service;

import hr.lemax.hotel.common.strategy.HotelSortStrategy;
import hr.lemax.hotel.dto.HotelModificationDTO;
import hr.lemax.hotel.model.Hotel;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface IHotelService {
    List<Hotel> getAllHotels();

    Optional<Hotel> getHotelById(@NonNull Long id);

    Hotel addHotel(@NonNull HotelModificationDTO createDto);

    Hotel updateHotel(@NonNull HotelModificationDTO updateDto, @NonNull Long id);

    void deleteHotel(@NonNull Long id);

    List<Hotel> searchHotels(@NonNull Double currentLon, @NonNull Double currentLat, @NotNull HotelSortStrategy sortStrategy);
}
