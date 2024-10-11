package hr.lemax.hotel.common.exception;

import jakarta.persistence.EntityNotFoundException;

public class HotelNotFoundException extends EntityNotFoundException {
    public HotelNotFoundException(final Long id) {
        super("Hotel with ID " + id + " was not found");
    }
}
