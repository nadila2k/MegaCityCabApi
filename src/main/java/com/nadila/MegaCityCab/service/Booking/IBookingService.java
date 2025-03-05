package com.nadila.MegaCityCab.service.Booking;

import com.nadila.MegaCityCab.model.Booking;
import com.nadila.MegaCityCab.requests.BookingRequest;

import java.util.List;

public interface IBookingService {

    Booking createBooking(BookingRequest booking);
    void deleteBooking(Booking booking);
    Booking pasangerUpdateBooking(Booking booking);
    List<Booking> getBookingsByVehicleTypeAndStatus(Long vehicaleTypeId);

}
