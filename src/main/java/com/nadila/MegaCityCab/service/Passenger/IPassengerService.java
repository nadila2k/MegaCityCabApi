package com.nadila.MegaCityCab.service.Passenger;

import com.nadila.MegaCityCab.dto.PassengerDto;
import com.nadila.MegaCityCab.requests.PassengerUpdateRequest;

import java.util.List;

public interface IPassengerService {

    PassengerDto updatePassenger(PassengerUpdateRequest passengerUpdateRequest);
    List<PassengerDto> getAllPassengers();
    void deletePassenger(String email, String password);

}
