package com.nadila.MegaCityCab.service.Passenger;

import com.nadila.MegaCityCab.dto.PassengerDto;
import com.nadila.MegaCityCab.model.Passenger;
import com.nadila.MegaCityCab.requests.PassangerUpdateRequest;

import java.util.List;

public interface IPassengerService {

    PassengerDto updatePassenger(PassangerUpdateRequest passengerUpdateRequest);
    List<PassengerDto> getAllPassengers();
    void deletePassenger(String email, String password);

}
