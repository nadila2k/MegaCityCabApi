package com.nadila.MegaCityCab.service.Passenger;

import com.nadila.MegaCityCab.dto.PassengerDto;
import com.nadila.MegaCityCab.exception.ResourceNotFound;
import com.nadila.MegaCityCab.model.Passenger;
import com.nadila.MegaCityCab.repository.PassengerRepository;
import com.nadila.MegaCityCab.requests.PassengerUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PassengerServiceTest {

    @Mock
    private PassengerRepository passengerRepository;

    @InjectMocks
    private PassengerService passengerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updatePassenger_Success() {
        Passenger existingPassenger = new Passenger();
        existingPassenger.setId(1L);
        existingPassenger.setFirstName("OldFirstName");
        existingPassenger.setLastName("OldLastName");
        existingPassenger.setMobileNumber("0000000000");
        existingPassenger.setAddress("Old Address");

        PassengerUpdateRequest updateRequest = new PassengerUpdateRequest();
        updateRequest.setId(1L);
        updateRequest.setFirstName("John");
        updateRequest.setLastName("Doe");
        updateRequest.setMobileNumber("1234567890");
        updateRequest.setAddress("New Address");

        when(passengerRepository.findById(anyLong())).thenReturn(Optional.of(existingPassenger));
        when(passengerRepository.save(any(Passenger.class))).thenReturn(existingPassenger);

        PassengerDto updatedPassenger = passengerService.updatePassenger(updateRequest);

        assertNotNull(updatedPassenger);
        assertEquals("John", updatedPassenger.getFirstName());
        assertEquals("Doe", updatedPassenger.getLastName());
        assertEquals("1234567890", updatedPassenger.getMobileNumber());
        assertEquals("New Address", updatedPassenger.getAddress());

        verify(passengerRepository, times(1)).findById(anyLong());
        verify(passengerRepository, times(1)).save(any(Passenger.class));
    }

    @Test
    void updatePassenger_NotFound() {
        PassengerUpdateRequest updateRequest = new PassengerUpdateRequest();
        updateRequest.setId(1L);
        updateRequest.setFirstName("John");
        updateRequest.setLastName("Doe");
        updateRequest.setMobileNumber("1234567890");
        updateRequest.setAddress("New Address");

        when(passengerRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, () -> passengerService.updatePassenger(updateRequest));

        verify(passengerRepository, times(1)).findById(anyLong());
        verify(passengerRepository, never()).save(any(Passenger.class));
    }
}
