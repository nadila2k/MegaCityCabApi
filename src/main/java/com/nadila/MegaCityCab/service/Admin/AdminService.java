package com.nadila.MegaCityCab.service.Admin;

import com.nadila.MegaCityCab.dto.BookingDto;
import com.nadila.MegaCityCab.model.Admin;
import com.nadila.MegaCityCab.service.Booking.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class AdminService implements IAdminService {

    private final BookingService bookingService;
    @Override
    public List<BookingDto> getAllBookings() {
        return bookingService.getAllBooking();
    }

    @Override
    public BookingDto getBookingById(int bookingId) {
        return null;
    }
}
