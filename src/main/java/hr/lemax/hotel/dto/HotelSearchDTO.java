package hr.lemax.hotel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HotelSearchDTO {
    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "Hotel Name", example = "Hilton Hotel")
    private String name;

    @Schema(description = "Price of hotel", example = "100.00")
    private Double price;

    @Schema(description = "Longitude Coordinate", example = "45.123")
    private Double longitude;

    @Schema(description = "Latitude Coordinate", example = "-45.123")
    private Double latitude;

    @Schema(description = "Distance from user", example = "00.00")
    private Double distance;
}
