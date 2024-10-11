package hr.lemax.hotel.controller;

import hr.lemax.hotel.dto.HotelDTO;
import hr.lemax.hotel.dto.HotelModificationDTO;
import hr.lemax.hotel.dto.HotelSearchDTO;
import hr.lemax.hotel.dto.UserGeoModificationDTO;
import hr.lemax.hotel.model.Hotel;
import hr.lemax.hotel.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hotels")
@Tag(name = "Hotel Management", description = "API for managing hotels")
@Slf4j
public class HotelController {
    private final HotelService hotelService;
    private final ModelMapper mapper;

    public HotelController(final HotelService hotelService,final ModelMapper mapper) {
        this.hotelService = hotelService;
        this.mapper = mapper;
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @Operation(summary = "Get all hotels")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Hotels successfully fetched"),
    })
    @GetMapping
    public ResponseEntity<List<HotelDTO>> getAllHotels() {
        log.debug("Request received: getAll() for hotels.");

        return ResponseEntity.ok(hotelService.getAllHotels()
                .stream()
                .map(entity -> mapper.map(entity, HotelDTO.class))
                .collect(Collectors.toList()));
    }

    @Operation(summary = "Get single hotel by ID")
    @Parameter(name = "id", description = "Hotel ID", example = "1")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fetch successful"),
            @ApiResponse(responseCode = "404", description = "Hotel not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<HotelDTO> getById(
            @PathVariable("id") final Long id ) {
        log.debug("Request received: getById() for hotel with ID: {}", id);

        return hotelService.getHotelById(id)
                .map(hotel -> ResponseEntity.ok(mapper.map(hotel, HotelDTO.class)))
                .orElseThrow(() -> new EntityNotFoundException("Hotel with ID " + id + " was not found"));
    }

    @Operation(summary = "Add hotel")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Creation successful"),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content),
    })
    @PostMapping
    public ResponseEntity<HotelDTO> addHotel(
            @Valid @RequestBody final HotelModificationDTO hotelDto) {
        log.debug("Request received: addHotel().");
        log.trace("Body: {}", hotelDto);

        final Hotel hotel = hotelService.addHotel(hotelDto);
        return ResponseEntity.ok(mapper.map(hotel, HotelDTO.class));
    }

    @Operation(summary = "Update hotel")
    @Parameter(name = "id", description = "Hotel ID", example = "1")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Update successful"),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content),
            @ApiResponse(responseCode = "404", description = "Hotel not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<HotelDTO> update(
            @PathVariable("id") final Long id,
            @Valid @RequestBody final HotelModificationDTO updateDto) {
        log.debug("Request received: update() for hotel with ID: {}", id);
        log.trace("Body: {}", updateDto);

        hotelService.updateHotel(updateDto, id);
        return ResponseEntity.ok(mapper.map(hotelService.getHotelById(id).orElseThrow(), HotelDTO.class));
    }

    @Operation(summary = "Delete hotel")
    @Parameter(name = "id", description = "Hotel ID", example = "1")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Delete successful"),
            @ApiResponse(responseCode = "400", description = "Delete not permitted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Hotel not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable("id") final Long id) {
        log.debug("Request received: delete() for hotel with ID: {}", id);

        hotelService.deleteHotel(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search hotels near user location")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Hotels successfully fetched"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/search")
    public ResponseEntity<List<HotelSearchDTO>> searchHotels(
            @Valid @RequestBody final UserGeoModificationDTO userGeoDto) {
        log.debug("Request received: searchHotels() for user with location: ({}, {})", userGeoDto.getLatitude(), userGeoDto.getLongitude());

        return ResponseEntity.ok(hotelService.searchHotels(userGeoDto.getLongitude(), userGeoDto.getLatitude())
                .stream()
                .map(entity -> mapper.map(entity, HotelSearchDTO.class))
                .collect(Collectors.toList()));
    }
}
