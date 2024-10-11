package hr.lemax.hotel.dto;

import hr.lemax.hotel.common.enums.Messages;
import hr.lemax.hotel.common.util.Validator;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGeoModificationDTO {

    @Schema(description = "Longitude (X) Coordinate", example = "45.123")
    @NotNull(message = Messages.Error.User.USER_LONGITUDE_REQUIRED)
    private Double longitude;

    @Schema(description = "Latitude (Y) Coordinate", example = "-45.123")
    @NotNull(message = Messages.Error.User.USER_LATITUDE_REQUIRED)
    private Double latitude;

    @Valid
    @Schema(hidden = true)
    public boolean isValid() {
        // Check for non-null longitude and latitude first,
        // null values catch by NotNull annotation
        if (longitude == null || latitude == null) {
            return true;
        }

        // Validate coordinates
        if (!Validator.areCoordinatesValid(longitude, latitude)) {
            throw new DataIntegrityViolationException(Messages.Error.User.USER_GEOLOCATION_INVALID);
        }

        return true;
    }
}
