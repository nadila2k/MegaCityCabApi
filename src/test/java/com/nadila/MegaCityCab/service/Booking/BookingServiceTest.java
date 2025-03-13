package com.nadila.MegaCityCab.service.Booking;

import com.nadila.MegaCityCab.dto.BookingDto;
import com.nadila.MegaCityCab.enums.BookingStatus;
import com.nadila.MegaCityCab.model.Booking;
import com.nadila.MegaCityCab.model.VehicleType;
import com.nadila.MegaCityCab.repository.BookingRepository;
import com.nadila.MegaCityCab.requests.BookingRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    BookingService bookingService;
    private BookingService b;


    @Test
    void createBooking() throws ParseException {
        long vehicleId = 1;
        VehicleType vehicleType = new VehicleType();
        vehicleType.setId(vehicleId);
        vehicleType.setName("van");
        vehicleType.setPrice(35.75);
        vehicleType.setImageUrl("http://res.cloudinary.com/dbiddrued/image/upload/v1741693155/gnbsffivdzfjolziaudg.jpg");
        vehicleType.setImageId("gnbsffivdzfjolziaudg");


        BookingRequest bookingRequest = new BookingRequest();


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date bookingDate = sdf.parse("2025-03-05T10:30:00Z");


        bookingRequest.setDate(bookingDate);
        bookingRequest.setPickupLocation("123 Main Street, Mega City");
        bookingRequest.setDestinationLocation("456 Elm Avenue, Mega City");
        bookingRequest.setTotalDistanceKM(15.5);
        bookingRequest.setPricePerKM(2.5);
        bookingRequest.setVehicleTypeId(1);

        try {
            bookingService.createBooking(bookingRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAllBooking() throws ParseException {
        try {
            List<BookingDto> bookingDtos = bookingService.getAllBooking();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void updateBooking() throws ParseException {
        long vehicleId = 1;

        // Create a mock for the bookingService
        BookingService bookingService = mock(BookingService.class);

        // Create the vehicle type object
        VehicleType vehicleType = new VehicleType();
        vehicleType.setId(vehicleId);
        vehicleType.setName("van");
        vehicleType.setPrice(35.75);
        vehicleType.setImageUrl("http://res.cloudinary.com/dbiddrued/image/upload/v1741693155/gnbsffivdzfjolziaudg.jpg");
        vehicleType.setImageId("gnbsffivdzfjolziaudg");

        // Create the booking request object
        Booking bookingRequest = new Booking();

        // Parse the date string to Date object
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date bookingDate = sdf.parse("2025-03-05T10:30:00Z");

        // Set values in the booking request
        bookingRequest.setDate(bookingDate);
        bookingRequest.setPickupLocation("123 Main Street, Mega City");
        bookingRequest.setDestinationLocation("456 Elm Avenue, Mega City");
        bookingRequest.setTotalDistanceKM(156.5);
        bookingRequest.setPricePerKM(26.5);
        bookingRequest.setBookingStatus(BookingStatus.ACTIVE);
        bookingRequest.setVehicleType(vehicleType);

        // Call the passenger update method (note: corrected to 'pasangerUpdateBooking')
        try {
            bookingService.pasangerUpdateBooking(1L, bookingRequest); // Using pasangerUpdateBooking instead of updateBooking
        } catch (Exception e) {
            throw new RuntimeException("Error updating booking", e);
        }

        // Verify that pasangerUpdateBooking was called exactly once with the correct arguments
        verify(bookingService, times(1)).pasangerUpdateBooking(1L, bookingRequest);

        // Additional checks for the updated booking can be added here if necessary
    }

    @Test
    void BookingComplete() {
        try {
            // Simulate the call to the service and assert the expected behavior
            BookingDto bookingDto = bookingService.driverUpdateBooking(1, BookingStatus.COMPLETED);

            // Optionally, you can add assertions to verify the result of the update
            assertNotNull(bookingDto); // Example assertion

        } catch (Exception e) {
            // Catch other exceptions and log the error message
            fail("Unexpected error occurred: " + e.getMessage());
        }
    }
    }



