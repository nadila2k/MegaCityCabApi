package com.nadila.MegaCityCab.controller;

import com.nadila.MegaCityCab.dto.BookingDto;
import com.nadila.MegaCityCab.dto.DriversDto;
import com.nadila.MegaCityCab.enums.BookingStatus;
import com.nadila.MegaCityCab.enums.ResponseStatus;
import com.nadila.MegaCityCab.exception.ResourceNotFound;
import com.nadila.MegaCityCab.model.Booking;
import com.nadila.MegaCityCab.requests.BookingRequest;
import com.nadila.MegaCityCab.response.ApiResponse;
import com.nadila.MegaCityCab.service.Booking.BookingService;
import com.nadila.MegaCityCab.service.Booking.IBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/booking")
public class BookingController {

    final private IBookingService bookingService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> bookingCreate(@RequestBody BookingRequest bookingRequest) {

        try {
            BookingDto bookingDto = bookingService.createBooking(bookingRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(ResponseStatus.SUCCESS, "booking created", bookingDto));
        } catch (ResourceNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(ResponseStatus.FAILURE, "resource not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(ResponseStatus.ERROR, "server error", e.getMessage()));
        }
    }

    @PutMapping("/update/{id}/pasanger")
    public ResponseEntity<ApiResponse> pasangerUpdateBooking(@PathVariable long id, @RequestBody Booking booking) {

        try {
            BookingDto bookingDto = bookingService.pasangerUpdateBooking(id, booking);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(ResponseStatus.SUCCESS, "booking updated", bookingDto));
        } catch (ResourceNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(ResponseStatus.FAILURE, "Booing not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(ResponseStatus.ERROR, "server error", e.getMessage()));
        }
    }

    @GetMapping("/get/booking/driver")
    public ResponseEntity<ApiResponse> getBookingsByVehicleTypeAndStatus() {

        System.out.println("getBookingsByVehicleTypeAndStatus");
        try {
            List<BookingDto> bookingDto = bookingService.getBookingsByVehicleTypeAndStatus();
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(ResponseStatus.SUCCESS, "booking list", bookingDto));
        } catch (ResourceNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(ResponseStatus.FAILURE, "booking not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(ResponseStatus.ERROR, "server error", e.getMessage()));
        }
    }

    @PutMapping("/update/{id}/driver")
    public ResponseEntity<ApiResponse> driverUpdateBooking(@PathVariable long id,
                                                           @RequestParam BookingStatus bookingStatus) {


        try {
            BookingDto bookingDto = bookingService.driverUpdateBooking(id,bookingStatus);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse(ResponseStatus.SUCCESS,"Booking status updated to "+ bookingDto.getBookingStatus() , bookingDto));
        } catch (ResourceNotFound e) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(ResponseStatus.FAILURE, "resource not found", e.getMessage()));
        } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(ResponseStatus.ERROR, "server error", e.getMessage()));
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteBooking(@PathVariable long id) {
        try {
            bookingService.deleteBooking(id);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(ResponseStatus.SUCCESS, "booking deleted", true));
        }catch (ResourceNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(ResponseStatus.FAILURE, "resource not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(ResponseStatus.ERROR, "server error", e.getMessage()));
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllBookings() {
        try {
            List<BookingDto> bookingDtos = bookingService.getAllBooking();
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(ResponseStatus.SUCCESS, "booking list", bookingDtos));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("get/driver/booking")
    public ResponseEntity<ApiResponse> getBookingByDriverId() {

        try {
            List<BookingDto>  bookingDtos = bookingService.getBookingsByDriver();
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(ResponseStatus.SUCCESS, "booking list", bookingDtos));
        } catch (ResourceNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(ResponseStatus.FAILURE, "resource not found", e.getMessage()));
        }
    }

    @GetMapping("/get/booking/driver/ongoing")
    public ResponseEntity<ApiResponse> getBookingByStatusOngoingDriver() {
        System.out.println("getBookingByStatusOngoingDriver");
        try {
            // Call the service to get ongoing bookings
            List<BookingDto> bookingDtos = bookingService.getBookingByStatusOngoingDriver();

            // Return successful response with ongoing bookings
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(ResponseStatus.SUCCESS, "Ongoing booking list", bookingDtos));
        } catch (ResourceNotFound e) {
            // Handle driver not found error
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(ResponseStatus.FAILURE, "Driver not found", e.getMessage()));
        } catch (Exception e) {
            // Handle any other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(ResponseStatus.ERROR, "Server error", e.getMessage()));
        }
    }

    @GetMapping("/get/booking/driver/completed")
    public ResponseEntity<ApiResponse> getBookingsByStatusCompletedDriver() {
        System.out.println("getBookingsByStatusCompletedDriver");
        try {
            // Call the service to get completed bookings
            List<BookingDto> bookingDtoList = bookingService.getBookingsByStatusCompletedDriver();

            // Return successful response with completed bookings
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(ResponseStatus.SUCCESS, "Completed booking list", bookingDtoList));
        } catch (ResourceNotFound e) {
            // Handle driver not found error
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(ResponseStatus.FAILURE, "Driver not found", e.getMessage()));
        } catch (Exception e) {
            // Handle any other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(ResponseStatus.ERROR, "Server error", e.getMessage()));
        }
    }

}
