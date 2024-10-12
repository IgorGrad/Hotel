package hr.lemax.hotel.service;

import hr.lemax.hotel.common.exception.HotelNotFoundException;
import hr.lemax.hotel.common.strategy.HotelSortStrategy;
import hr.lemax.hotel.dto.HotelModificationDTO;
import hr.lemax.hotel.model.Hotel;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class HotelServiceTest {
    private static HotelService hotelService;

    private static final HotelSortStrategy mockSortStrategy = Mockito.mock(HotelSortStrategy.class);
    private static final HotelModificationDTO VALID_HOTEL_DTO = new HotelModificationDTO("Hilton", 150.0, 50.0, 10.0);
    private static final Hotel VALID_HOTEL = new Hotel(1L, "Hilton", 150.0, 50.0, 10.0);

    @BeforeAll
    static void setup() {
        ModelMapper modelMapper = new ModelMapper();
        hotelService = new HotelService(modelMapper);
    }

    @Test
    void testAddHotelSuccess() {
        Hotel addedHotel = hotelService.addHotel(VALID_HOTEL_DTO);
        assertNotNull(addedHotel);
        assertEquals("Hilton", addedHotel.getName());
    }

 /*    @Test
    void testGetAllHotelsSuccess() {
        hotelService.addHotel(VALID_HOTEL_DTO);
        List<Hotel> hotels = hotelService.getAllHotels();
        assertFalse(hotels.isEmpty());
        assertEquals(1, hotels.size());
        assertEquals("Hilton", hotels.get(0).getName());
    }

   @Test
    void testGetHotelByIdSuccess() {
        hotelService.addHotel(VALID_HOTEL_DTO);
        Optional<Hotel> hotelOptional = hotelService.getHotelById(1L);
        assertTrue(hotelOptional.isPresent());
        assertEquals("Hilton", hotelOptional.get().getName());
    }

    @Test
    void testUpdateHotelSuccess() {
        hotelService.addHotel(VALID_HOTEL_DTO);
        HotelModificationDTO updateDto = new HotelModificationDTO("Hilton Updated", 200.0, 60.0, 20.0);
        Hotel updatedHotel = hotelService.updateHotel(updateDto, 1L);
        assertEquals("Hilton Updated", updatedHotel.getName());
        assertEquals(200.0, updatedHotel.getPrice());
    }*/

    @Test
    void testUpdateHotelNotFound() {
        HotelModificationDTO updateDto = new HotelModificationDTO("Hotel", 200.0, 60.0, 20.0);
        assertThrows(HotelNotFoundException.class, () -> hotelService.updateHotel(updateDto, 99L));
    }

    @Test
    void testDeleteHotelSuccess() {
        hotelService.addHotel(VALID_HOTEL_DTO);
        hotelService.deleteHotel(1L);
        assertEquals(Optional.empty(), hotelService.getHotelById(1L));
    }

    @Test
    void testDeleteHotelNotFound() {
        hotelService.addHotel(VALID_HOTEL_DTO);
        assertThrows(HotelNotFoundException.class, () -> hotelService.deleteHotel(2L));
    }

/*    @Test
    void testSearchHotelsByPageSuccess() {
        hotelService.addHotel(VALID_HOTEL_DTO);
        Mockito.when(mockSortStrategy.sort(Mockito.anyList(), Mockito.anyDouble(), Mockito.anyDouble()))
                .thenReturn(Collections.singletonList(VALID_HOTEL));

        Page<Hotel> hotelPage = hotelService.searchHotelsByPage(50.0, 10.0, mockSortStrategy, PageRequest.of(0, 1));

        assertEquals(1, hotelPage.getContent().size());
        assertEquals("Hilton", hotelPage.getContent().get(0).getName());
        assertEquals(1, hotelPage.getTotalElements());
    }*/

    @Test
    void testSearchHotelsByPageEmptyResult() {
        hotelService.addHotel(VALID_HOTEL_DTO);
        Mockito.when(mockSortStrategy.sort(Mockito.anyList(), Mockito.anyDouble(), Mockito.anyDouble()))
                .thenReturn(Collections.emptyList());

        Page<Hotel> hotelPage = hotelService.searchHotelsByPage(50.0, 10.0, mockSortStrategy, PageRequest.of(0, 1));

        assertTrue(hotelPage.getContent().isEmpty());
        assertEquals(0, hotelPage.getTotalElements());
    }

    @Test
    void testSearchHotelsException() {
        Mockito.when(mockSortStrategy.sort(Mockito.anyList(), Mockito.anyDouble(), Mockito.anyDouble()))
                .thenThrow(new RuntimeException("Sorting error"));

        assertThrows(RuntimeException.class, () -> hotelService.searchHotels(50.0, 10.0, mockSortStrategy));
    }
}
