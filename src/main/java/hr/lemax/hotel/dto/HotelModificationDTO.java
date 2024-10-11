package hr.lemax.hotel.dto;

import hr.lemax.hotel.common.enums.Messages;
import hr.lemax.hotel.common.util.Validator;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelModificationDTO {
    @Schema(description = "Hotel Name", example = "Hilton hotel")
    @NotEmpty(message = Messages.Error.Hotel.HOTEL_NAME_REQUIRED)
    @Size(min = 2, max = 1024, message = Messages.Error.Hotel.HOTEL_NAME_LENGTH)
    private String name;

    @Schema(description = "Price of Hotel", example = "100.00")
    @NotNull(message = Messages.Error.Hotel.HOTEL_PRICE_REQUIRED)
    @Positive(message = Messages.Error.Hotel.HOTEL_PRICE_POSITIVE)
    private Double price;

    @Schema(description = "Longitude (X) Coordinate", example = "45.123")
    @NotNull(message = Messages.Error.Hotel.HOTEL_LONGITUDE_REQUIRED)
    private Double longitude;

    @Schema(description = "Latitude (Y) Coordinate", example = "-45.123")
    @NotNull(message = Messages.Error.Hotel.HOTEL_LATITUDE_REQUIRED)
    private Double latitude;

    @Valid
    @Schema(hidden = true)
    public boolean isValid() {
        if (longitude != null) {
            if (latitude != null) {
                if(!Validator.areCoordinatesValid(longitude, latitude)) {
                    throw new DataIntegrityViolationException(Messages.Error.Hotel.GEOLOCATION_INVALID);
                }
            }
        }

        return true;
    }
}
