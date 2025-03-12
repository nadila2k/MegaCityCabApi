package com.nadila.MegaCityCab.service.Admin;

import com.nadila.MegaCityCab.dto.BookingDto;
import com.nadila.MegaCityCab.enums.Roles;
import com.nadila.MegaCityCab.model.Booking;

import java.util.List;

public interface IAdminService {

    List<BookingDto> getAllBookings();
    BookingDto getBookingById(int bookingId);
}
