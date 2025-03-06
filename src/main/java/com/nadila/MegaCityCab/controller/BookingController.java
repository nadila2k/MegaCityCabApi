package com.nadila.MegaCityCab.controller;

import com.nadila.MegaCityCab.dto.BookingDto;
import com.nadila.MegaCityCab.model.Booking;
import com.nadila.MegaCityCab.requests.BookingRequest;
import com.nadila.MegaCityCab.response.ApiResponse;
import com.nadila.MegaCityCab.service.Booking.BookingService;
import com.nadila.MegaCityCab.service.Booking.IBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/booking")
public class BookingController {

   final private IBookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestBody BookingRequest  bookingRequest){
     return bookingService.createBooking(bookingRequest);
    }
}
