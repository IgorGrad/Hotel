package hr.lemax.hotel.controller;

import hr.lemax.hotel.common.strategy.SortByDistanceAndPrice;
import hr.lemax.hotel.dto.HotelDTO;
import hr.lemax.hotel.dto.HotelModificationDTO;
import hr.lemax.hotel.dto.HotelSearchDTO;
import hr.lemax.hotel.dto.UserGeoModificationDTO;
import hr.lemax.hotel.model.Hotel;
import hr.lemax.hotel.service.HotelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HotelControllerTest {
    private HotelService hotelService;
    private ModelMapper modelMapper;
    private HotelController hotelController;

    private static final Long VALID_HOTEL_ID = 1L;
    private static final Hotel VALID_HOTEL = new Hotel();
    private static final HotelDTO VALID_HOTEL_DTO = new HotelDTO();
    private static final HotelModificationDTO VALID_HOTEL_MODIFICATION_DTO = new HotelModificationDTO();
    private static final UserGeoModificationDTO VALID_USER_GEO_DTO = new UserGeoModificationDTO();
    private static final HotelSearchDTO VALID_HOTEL_SEARCH_DTO = new HotelSearchDTO();

    @BeforeEach
    void setUp() {
        hotelService = Mockito.mock(HotelService.class);
        modelMapper = Mockito.mock(ModelMapper.class);
        hotelController = new HotelController(hotelService, modelMapper);
    }

    @Test
    void testGetAllHotelsSuccess() {
        List<Hotel> hotels = List.of(VALID_HOTEL);
        Mockito.when(hotelService.getAllHotels()).thenReturn(hotels);
        Mockito.when(modelMapper.map(VALID_HOTEL, HotelDTO.class)).thenReturn(VALID_HOTEL_DTO);

        ResponseEntity<List<HotelDTO>> response = hotelController.getAllHotels();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        assertEquals(VALID_HOTEL_DTO, response.getBody().get(0));
    }

    @Test
    void testGetAllHotelsNoContent() {
        Mockito.when(hotelService.getAllHotels()).thenReturn(Collections.emptyList());

        ResponseEntity<List<HotelDTO>> response = hotelController.getAllHotels();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testGetByIdSuccess() {
        Mockito.when(hotelService.getHotelById(VALID_HOTEL_ID)).thenReturn(Optional.of(VALID_HOTEL));
        Mockito.when(modelMapper.map(VALID_HOTEL, HotelDTO.class)).thenReturn(VALID_HOTEL_DTO);

        ResponseEntity<HotelDTO> response = hotelController.getById(VALID_HOTEL_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(VALID_HOTEL_DTO, response.getBody());
    }

    @Test
    void testGetByIdNoContent() {
        Mockito.when(hotelService.getHotelById(VALID_HOTEL_ID)).thenReturn(Optional.empty());

        ResponseEntity<HotelDTO> response = hotelController.getById(VALID_HOTEL_ID);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testAddHotelSuccess() {
        Mockito.when(hotelService.addHotel(VALID_HOTEL_MODIFICATION_DTO)).thenReturn(VALID_HOTEL);
        Mockito.when(modelMapper.map(VALID_HOTEL, HotelDTO.class)).thenReturn(VALID_HOTEL_DTO);

        ResponseEntity<HotelDTO> response = hotelController.addHotel(VALID_HOTEL_MODIFICATION_DTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(VALID_HOTEL_DTO, response.getBody());
    }

    @Test
    void testUpdateHotelSuccess() {
        Mockito.when(hotelService.updateHotel(VALID_HOTEL_MODIFICATION_DTO, VALID_HOTEL_ID)).thenReturn(VALID_HOTEL);
        Mockito.when(modelMapper.map(VALID_HOTEL, HotelDTO.class)).thenReturn(VALID_HOTEL_DTO);

        ResponseEntity<HotelDTO> response = hotelController.update(VALID_HOTEL_ID, VALID_HOTEL_MODIFICATION_DTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(VALID_HOTEL_DTO, response.getBody());
    }

    @Test
    void testDeleteHotelSuccess() {
        ResponseEntity<String> response = hotelController.delete(VALID_HOTEL_ID);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
