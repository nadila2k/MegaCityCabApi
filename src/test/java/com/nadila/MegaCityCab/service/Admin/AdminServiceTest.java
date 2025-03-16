package com.nadila.MegaCityCab.service.Admin;

import com.nadila.MegaCityCab.dto.BookingDto;
import com.nadila.MegaCityCab.exception.ResourceNotFound;
import com.nadila.MegaCityCab.model.VehicleType;
import com.nadila.MegaCityCab.repository.VehicaleTypeRepository;
import com.nadila.MegaCityCab.requests.VehicaleTypeRequest;
import com.nadila.MegaCityCab.service.Booking.BookingService;
import com.nadila.MegaCityCab.service.Booking.IBookingService;
import com.nadila.MegaCityCab.service.VehicaleType.IVehicalTypeService;
import com.nadila.MegaCityCab.service.VehicaleType.VehicalTypeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @InjectMocks
    BookingService iBookingService;

    @Mock
    VehicalTypeService iVehicalTypeService;



    @Test
    void getAllBookings() {

        when(iBookingService.getAllBooking()).thenReturn(List.of());

        List<BookingDto> bookingDtoList = iBookingService.getAllBooking();

        assertNotNull(bookingDtoList, "The list should not be null");

        assertTrue(bookingDtoList.isEmpty(), "The list should be empty");
    }

    @Test
    void getBookingById() {
    }

    @Test
    void deleteCalecheType(){
        try {
            iVehicalTypeService.deleteVehicalType(3);
        } catch (ResourceNotFound e) {
            throw new ResourceNotFound("Vehical type not found");
        }
    }

    @Test
    public void updateValecheType() {
        long vehicleTypeId = 1;

        // Create a VehicleType object instead of VehicaleTypeRequest
        VehicleType vehicleType = new VehicleType();
        vehicleType.setPrice(200);
        vehicleType.setName("Vehicle Type 1");

        // Call the update method with the correct parameter
        iVehicalTypeService.updateVehicalType(vehicleTypeId, vehicleType, null); // Pass null if no image is being updated
    }



}