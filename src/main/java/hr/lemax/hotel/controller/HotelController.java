package hr.lemax.hotel.controller;

import hr.lemax.hotel.common.strategy.SortByDistanceAndPrice;
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
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
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

    @Operation(summary = "Get all hotels")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Hotels successfully fetched"),
            @ApiResponse(responseCode = "204", description = "No hotel found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<HotelDTO>> getAllHotels() {
        log.info("Request received: getAll() for hotels.");

        final List<HotelDTO> hotels = hotelService.getAllHotels()
                .stream()
                .map(entity -> mapper.map(entity, HotelDTO.class))
                .toList();

        if (hotels.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(hotels);
    }

    @Operation(summary = "Get single hotel by ID")
    @Parameter(name = "id", description = "Hotel ID", example = "1")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fetch successful"),
            @ApiResponse(responseCode = "204", description = "Hotel not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<HotelDTO> getById(
            @PathVariable("id") final Long id ) {
        log.info("Request received: getById() for hotel with ID: {}", id);

        return hotelService.getHotelById(id)
                .map(h -> mapper.map(h, HotelDTO.class))
                .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @Operation(summary = "Add hotel")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Creation successful"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping
    public ResponseEntity<HotelDTO> addHotel(
            @Valid @RequestBody final HotelModificationDTO hotelDto) {
        log.info("Request received: addHotel().");
        log.debug("Body: {}", hotelDto);

        final Hotel hotel = hotelService.addHotel(hotelDto);
        return ResponseEntity.ok(mapper.map(hotel, HotelDTO.class));
    }

    @Operation(summary = "Update hotel")
    @Parameter(name = "id", description = "Hotel ID", example = "1")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Update successful"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Hotel not found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)

    })
    @PutMapping("/{id}")
    public ResponseEntity<HotelDTO> update(
            @PathVariable("id") final Long id,
            @Valid @RequestBody final HotelModificationDTO updateDto) {
        log.info("Request received: update() for hotel with ID: {}", id);
        log.debug("Body: {}", updateDto);

        final Hotel hotel = hotelService.updateHotel(updateDto, id);
        return ResponseEntity.ok(mapper.map(hotel, HotelDTO.class));
    }

    @Operation(summary = "Delete hotel")
    @Parameter(name = "id", description = "Hotel ID", example = "1")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Delete successful", content = @Content),
            @ApiResponse(responseCode = "404", description = "Hotel not found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable("id") final Long id) {
        log.info("Request received: delete() for hotel with ID: {}", id);

        hotelService.deleteHotel(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search hotels near user location")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Hotels successfully fetched"),
            @ApiResponse(responseCode = "204", description = "No hotel found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/search")
    public ResponseEntity<List<HotelSearchDTO>> searchHotels(
            @Valid @RequestBody final UserGeoModificationDTO userGeoDto) {
        log.info("Request received: searchHotels() for user with location: ({}, {})", userGeoDto.getLatitude(), userGeoDto.getLongitude());
        final List<HotelSearchDTO> hotels = hotelService.searchHotels(userGeoDto.getLongitude(), userGeoDto.getLatitude(), new SortByDistanceAndPrice())
                .stream()
                .map(entity -> mapper.map(entity, HotelSearchDTO.class))
                .toList();
        if (hotels.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(hotels);
    }

    @Operation(summary = "Search hotels near user location with pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Hotels successfully fetched"),
            @ApiResponse(responseCode = "204", description = "No hotel found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/search/page")
    public ResponseEntity<PagedModel<HotelSearchDTO>> searchHotelsByPage(
            @Valid @RequestBody final UserGeoModificationDTO userGeoDto,
            @ParameterObject @PageableDefault(size = 20) Pageable pageable)
    {
        log.info("Request received: searchHotels() for user with location: ({}, {}), page: {}, size: {}",
                userGeoDto.getLatitude(), userGeoDto.getLongitude(), pageable.getPageNumber(), pageable.getPageSize());

        // Fetch hotels from service with pagination
        final Page<Hotel> hotelsPage = hotelService.searchHotelsByPage(
                userGeoDto.getLongitude(),
                userGeoDto.getLatitude(),
                new SortByDistanceAndPrice(),
               pageable
        );

        // Map Hotel entities to DTOs
        final List<HotelSearchDTO> hotelDTOList = hotelsPage
                .getContent()
                .stream()
                .map(entity -> mapper.map(entity, HotelSearchDTO.class))
                .toList();

        // If no hotels found, return no content
        if (hotelDTOList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Create PageMetadata for PagedModel
        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
                hotelsPage.getSize(),
                hotelsPage.getNumber(),
                hotelsPage.getTotalElements(),
                hotelsPage.getTotalPages());

        // Wrap the DTOs in a PagedModel without wrapping in EntityModel
        PagedModel<HotelSearchDTO> pagedModel = PagedModel.of(hotelDTOList, metadata);

        // Return paginated hotels
        return ResponseEntity.ok(pagedModel);
    }
}
