package hr.lemax.hotel.dto;

import hr.lemax.hotel.common.enums.Messages;
import hr.lemax.hotel.common.validator.ValidGeolocation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ValidGeolocation(message = Messages.Error.User.USER_GEOLOCATION_INVALID)
public class UserGeoModificationDTO {
    @Schema(description = "Longitude (X) Coordinate", example = "45.123")
    @NotNull(message = Messages.Error.User.USER_LONGITUDE_REQUIRED)
    private Double longitude;

    @Schema(description = "Latitude (Y) Coordinate", example = "-45.123")
    @NotNull(message = Messages.Error.User.USER_LATITUDE_REQUIRED)
    private Double latitude;
}
