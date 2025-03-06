package com.nadila.MegaCityCab.service.Booking;

import com.nadila.MegaCityCab.dto.BookingDto;
import com.nadila.MegaCityCab.enums.BookingStatus;
import com.nadila.MegaCityCab.model.Booking;
import com.nadila.MegaCityCab.requests.BookingRequest;

import java.util.List;

public interface IBookingService {

    BookingDto createBooking(BookingRequest booking);
    void deleteBooking(long id);
    BookingDto pasangerUpdateBooking(long id, Booking booking);
    List<BookingDto> getBookingsByVehicleTypeAndStatus();
    BookingDto driverUpdateBooking(long id, BookingStatus bookingStatus);
}
