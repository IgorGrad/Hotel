package hr.lemax.hotel.service;

import hr.lemax.hotel.common.exception.HotelNotFoundException;
import hr.lemax.hotel.common.strategy.HotelSortStrategy;
import hr.lemax.hotel.dto.HotelModificationDTO;
import hr.lemax.hotel.model.Hotel;
import hr.lemax.hotel.repository.HotelRepository;
import jakarta.persistence.EntityManager;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class HotelServiceTest {
    @Mock
    private HotelRepository hotelRepository;
    @Mock
    private EntityManager entityManager;
    @Mock
    private HotelSortStrategy mockSortStrategy;

    @InjectMocks
    private HotelService hotelService;

    private static final HotelModificationDTO VALID_HOTEL_DTO = new HotelModificationDTO("Hilton", 150.0, 50.0, 10.0);
    private static Hotel VALID_HOTEL;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Mockito.reset(hotelRepository, entityManager, mockSortStrategy);
        // This will be run before each test, ensuring isolation
        VALID_HOTEL = new Hotel(1L, "Hilton", 100.0, 50.0, 10.0);

        ModelMapper modelMapper = new ModelMapper();
        hotelService = new HotelService(modelMapper, hotelRepository);
        // Inject the mock EntityManager into the service if necessary
        ReflectionTestUtils.setField(hotelService, "entityManager", entityManager);
    }

    @Test
    void testAddHotelSuccess() {
        Mockito.when(hotelRepository.save(Mockito.any(Hotel.class))).thenReturn(VALID_HOTEL);

        Hotel addedHotel = hotelService.addHotel(VALID_HOTEL_DTO);

        assertNotNull(addedHotel);
        assertEquals("Hilton", addedHotel.getName());
    }

   @Test
    void testGetAllHotelsSuccess() {
       // Mocking the session and filter
       Session mockSession = Mockito.mock(Session.class);
       Filter mockFilter = Mockito.mock(Filter.class);

       // Ensure that when unwrap is called, it returns the mock session
       Mockito.when(entityManager.unwrap(Session.class)).thenReturn(mockSession);

       // When enableFilter is called, return the mock filter
       Mockito.when(mockSession.enableFilter("softDeleteFilter")).thenReturn(mockFilter);

       // Mock the filter's setParameter method to return the filter (fluent interface)
       Mockito.when(mockFilter.setParameter(Mockito.eq("isDeleted"), Mockito.anyBoolean())).thenReturn(mockFilter);
       // Mocking the repository behavior
       Mockito.when(hotelRepository.findAll()).thenReturn(List.of(VALID_HOTEL));

       // Adding the hotel and retrieving the list of all hotels
       hotelService.addHotel(VALID_HOTEL_DTO);
       List<Hotel> hotels = hotelService.getAllHotels();

       // Assertions
       assertFalse(hotels.isEmpty(), "The hotels list should not be empty");
       assertEquals(1, hotels.size(), "The size of the hotels list should be 1");
       assertEquals("Hilton", hotels.get(0).getName(), "The hotel name should be Hilton");
   }

     @Test
    void testGetHotelByIdSuccess() {
         // Mocking the session and filter
         Session mockSession = Mockito.mock(Session.class);
         Filter mockFilter = Mockito.mock(Filter.class);

         // Ensure that when unwrap is called, it returns the mock session
         Mockito.when(entityManager.unwrap(Session.class)).thenReturn(mockSession);

         // When enableFilter is called, return the mock filter
         Mockito.when(mockSession.enableFilter("softDeleteFilter")).thenReturn(mockFilter);

         // Mock the filter's setParameter method to return the filter (fluent interface)
         Mockito.when(mockFilter.setParameter(Mockito.eq("isDeleted"), Mockito.anyBoolean())).thenReturn(mockFilter);
         // Mock repository behavior to return the valid hotel
         Mockito.when(hotelRepository.findOne(Mockito.<Specification<Hotel>>any()))
                 .thenReturn(Optional.of(VALID_HOTEL));
         // Call the method
         Optional<Hotel> hotelOptional = hotelService.getHotelById(1L);

         // Assertions
         assertTrue(hotelOptional.isPresent(), "The hotel should be present");
         assertEquals("Hilton", hotelOptional.get().getName(), "The hotel name should be Hilton");
     }

    @Test
    void testUpdateHotelSuccess() {
        // Mocking the session and filter
        Session mockSession = Mockito.mock(Session.class);
        Filter mockFilter = Mockito.mock(Filter.class);

        // Ensure that when unwrap is called, it returns the mock session
        Mockito.when(entityManager.unwrap(Session.class)).thenReturn(mockSession);

        // When enableFilter is called, return the mock filter
        Mockito.when(mockSession.enableFilter("softDeleteFilter")).thenReturn(mockFilter);

        // Mock the filter's setParameter method to return the filter (fluent interface)
        Mockito.when(mockFilter.setParameter(Mockito.eq("isDeleted"), Mockito.anyBoolean())).thenReturn(mockFilter);
        // Add initial hotel data
        Mockito.when(hotelRepository.findOne(Mockito.<Specification<Hotel>>any()))
                .thenReturn(Optional.of(VALID_HOTEL));

        // Create update DTO and set expected updated values
        HotelModificationDTO updateDto = new HotelModificationDTO("Hilton Updated", 200.0, 60.0, 20.0);
        Hotel updatedHotel = new Hotel(1L, "Hilton Updated", 200.0, 60.0, 20.0);

        // Mock repository save to return updated hotel
        Mockito.when(hotelRepository.save(Mockito.any(Hotel.class))).thenReturn(updatedHotel);

        // Call the service method to update the hotel
        Hotel result = hotelService.updateHotel(updateDto, 1L);

        // Verify the updated hotel fields
        assertEquals("Hilton Updated", result.getName(), "The hotel name should be updated.");
        assertEquals(200.0, result.getPrice(), "The hotel price should be updated.");
        assertEquals(60.0, result.getLongitude(), "The hotel longitude should be updated.");
        assertEquals(20.0, result.getLatitude(), "The hotel latitude should be updated.");

        // Verify that repository findById was called once
        Mockito.verify(hotelRepository, Mockito.times(1)).findOne(Mockito.<Specification<Hotel>>any());

        // Verify that repository save was called once with the updated hotel
        Mockito.verify(hotelRepository, Mockito.times(1)).save(Mockito.any(Hotel.class));

    }

    @Test
    void testUpdateHotelNotFound() {
        // Mocking the session and filter
        Session mockSession = Mockito.mock(Session.class);
        Filter mockFilter = Mockito.mock(Filter.class);

        // Ensure that when unwrap is called, it returns the mock session
        Mockito.when(entityManager.unwrap(Session.class)).thenReturn(mockSession);

        // When enableFilter is called, return the mock filter
        Mockito.when(mockSession.enableFilter("softDeleteFilter")).thenReturn(mockFilter);

        // Mock the filter's setParameter method to return the filter (fluent interface)
        Mockito.when(mockFilter.setParameter(Mockito.eq("isDeleted"), Mockito.anyBoolean())).thenReturn(mockFilter);
        // Create an update DTO with arbitrary values
        HotelModificationDTO updateDto = new HotelModificationDTO("Hotel", 200.0, 60.0, 20.0);

        // Assert that HotelNotFoundException is thrown when trying to update a non-existing hotel
        assertThrows(HotelNotFoundException.class, () -> hotelService.updateHotel(updateDto, 1L));

        Mockito.verify(hotelRepository, Mockito.times(1)).findOne(Mockito.<Specification<Hotel>>any());

        // Ensure save is never called since the hotel was not found
        Mockito.verify(hotelRepository, Mockito.never()).save(Mockito.any(Hotel.class));
    }

    @Test
    void testDeleteHotelSuccess() {
        // Mocking the session and filter
        Session mockSession = Mockito.mock(Session.class);
        Filter mockFilter = Mockito.mock(Filter.class);

        // Ensure that when unwrap is called, it returns the mock session
        Mockito.when(entityManager.unwrap(Session.class)).thenReturn(mockSession);

        // When enableFilter is called, return the mock filter
        Mockito.when(mockSession.enableFilter("softDeleteFilter")).thenReturn(mockFilter);

        // Mock the filter's setParameter method to return the filter (fluent interface)
        Mockito.when(mockFilter.setParameter(Mockito.eq("isDeleted"), Mockito.anyBoolean())).thenReturn(mockFilter);
        Mockito.when(hotelRepository.findOne(Mockito.<Specification<Hotel>>any()))
                .thenReturn(Optional.of(VALID_HOTEL));
        // Deleting the hotel
        hotelService.deleteHotel(1L);

        // After deletion, the hotel should no longer be found, so we mock findOne to return empty
        Mockito.when(hotelRepository.findOne(Mockito.<Specification<Hotel>>any()))
                .thenReturn(Optional.empty());
        // Verify that after deletion, hotel with ID 1L is no longer found
        assertEquals(Optional.empty(), hotelService.getHotelById(1L));

        // Verify that repository's delete method was called exactly once
        Mockito.verify(hotelRepository, Mockito.times(1)).delete(VALID_HOTEL);
    }

    @Test
    void testDeleteHotelNotFound() {
        // Mocking the session and filter
        Session mockSession = Mockito.mock(Session.class);
        Filter mockFilter = Mockito.mock(Filter.class);

        // Ensure that when unwrap is called, it returns the mock session
        Mockito.when(entityManager.unwrap(Session.class)).thenReturn(mockSession);

        // When enableFilter is called, return the mock filter
        Mockito.when(mockSession.enableFilter("softDeleteFilter")).thenReturn(mockFilter);

        // Mock the filter's setParameter method to return the filter (fluent interface)
        Mockito.when(mockFilter.setParameter(Mockito.eq("isDeleted"), Mockito.anyBoolean())).thenReturn(mockFilter);
        // Mocking findOne to return empty, simulating that no hotel exists with ID 2L
        Mockito.when(hotelRepository.findOne(Mockito.<Specification<Hotel>>any()))
                .thenReturn(Optional.empty());
        // Verify that attempting to delete a non-existent hotel throws HotelNotFoundException
        assertThrows(HotelNotFoundException.class, () -> hotelService.deleteHotel(2L));

        // Verify that the delete method is never called since the hotel doesn't exist
        Mockito.verify(hotelRepository, Mockito.never()).delete(Mockito.any(Hotel.class));
    }

    @Test
    void testSearchHotelsByPageSuccess() {
        // Mocking the session and filter
        Session mockSession = Mockito.mock(Session.class);
        Filter mockFilter = Mockito.mock(Filter.class);

        // Ensure that when unwrap is called, it returns the mock session
        Mockito.when(entityManager.unwrap(Session.class)).thenReturn(mockSession);

        // When enableFilter is called, return the mock filter
        Mockito.when(mockSession.enableFilter("softDeleteFilter")).thenReturn(mockFilter);

        // Mock the filter's setParameter method to return the filter (fluent interface)
        Mockito.when(mockFilter.setParameter(Mockito.eq("isDeleted"), Mockito.anyBoolean())).thenReturn(mockFilter);
        // Mocking the sort strategy to return a single valid hotel
        Mockito.when(mockSortStrategy.sort(Mockito.anyList(), Mockito.anyDouble(), Mockito.anyDouble()))
                .thenReturn(Collections.singletonList(VALID_HOTEL));

        // Performing the search with pagination
        Page<Hotel> hotelPage = hotelService.searchHotelsByPage(50.0, 10.0, mockSortStrategy, PageRequest.of(0, 1));

        // Asserting the results
        assertEquals(1, hotelPage.getContent().size());
        assertEquals("Hilton", hotelPage.getContent().get(0).getName());
        assertEquals(1, hotelPage.getTotalElements());
    }

    @Test
    void testSearchHotelsByPageEmptyResult() {
        // Mocking the session and filter
        Session mockSession = Mockito.mock(Session.class);
        Filter mockFilter = Mockito.mock(Filter.class);

        // Ensure that when unwrap is called, it returns the mock session
        Mockito.when(entityManager.unwrap(Session.class)).thenReturn(mockSession);

        // When enableFilter is called, return the mock filter
        Mockito.when(mockSession.enableFilter("softDeleteFilter")).thenReturn(mockFilter);

        // Mock the filter's setParameter method to return the filter (fluent interface)
        Mockito.when(mockFilter.setParameter(Mockito.eq("isDeleted"), Mockito.anyBoolean())).thenReturn(mockFilter);
        // Mocking the sort strategy to return an empty list
        Mockito.when(mockSortStrategy.sort(Mockito.anyList(), Mockito.anyDouble(), Mockito.anyDouble()))
                .thenReturn(Collections.emptyList());

        // Performing the search with pagination
        Page<Hotel> hotelPage = hotelService.searchHotelsByPage(50.0, 10.0, mockSortStrategy, PageRequest.of(0, 1));

        // Asserting that the result is empty
        assertTrue(hotelPage.getContent().isEmpty());
        assertEquals(0, hotelPage.getTotalElements());
    }

    @Test
    void testSearchHotelsException() {
        // Mocking the session and filter
        Session mockSession = Mockito.mock(Session.class);
        Filter mockFilter = Mockito.mock(Filter.class);

        // Ensure that when unwrap is called, it returns the mock session
        Mockito.when(entityManager.unwrap(Session.class)).thenReturn(mockSession);

        // When enableFilter is called, return the mock filter
        Mockito.when(mockSession.enableFilter("softDeleteFilter")).thenReturn(mockFilter);

        // Mock the filter's setParameter method to return the filter (fluent interface)
        Mockito.when(mockFilter.setParameter(Mockito.eq("isDeleted"), Mockito.anyBoolean())).thenReturn(mockFilter);
        // Mocking the sort strategy to throw an exception
        Mockito.when(mockSortStrategy.sort(Mockito.anyList(), Mockito.anyDouble(), Mockito.anyDouble()))
                .thenThrow(new RuntimeException("Sorting error"));

        // Asserting that a RuntimeException is thrown
        assertThrows(RuntimeException.class, () -> hotelService.searchHotels(50.0, 10.0, mockSortStrategy));
    }
}
