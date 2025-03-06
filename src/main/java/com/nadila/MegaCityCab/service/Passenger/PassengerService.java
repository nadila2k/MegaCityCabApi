package com.nadila.MegaCityCab.service.Passenger;

import com.nadila.MegaCityCab.dto.PassengerDto;
import com.nadila.MegaCityCab.exception.ResourceNotFound;
import com.nadila.MegaCityCab.model.Passenger;
import com.nadila.MegaCityCab.repository.CabUserRepository;
import com.nadila.MegaCityCab.repository.PassengerRepository;
import com.nadila.MegaCityCab.requests.PassengerUpdateRequest;
import com.nadila.MegaCityCab.service.AuthService.GetAuthId;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PassengerService implements IPassengerService {
    private final PassengerRepository passengerRepository;
    private final CabUserRepository userRepository;
    private final GetAuthId getAuthId;
    private final ModelMapper modelMapper;
    private final CabUserRepository cabUserRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public PassengerDto updatePassenger(PassengerUpdateRequest passengerUpdateRequest) {
        return passengerRepository.findById(getUserId())
                .map(exisitingPassenger -> {
                    exisitingPassenger.setFirstName(passengerUpdateRequest.getFirstName());
                    exisitingPassenger.setLastName(passengerUpdateRequest.getLastName());
                    exisitingPassenger.setMobileNumber(passengerUpdateRequest.getMobileNumber());
                    exisitingPassenger.setAddress(passengerUpdateRequest.getAddress());
                    return convertToPassengerDto(passengerRepository.save(exisitingPassenger));
                }).orElseThrow(() -> new ResourceNotFound("Passenger  not found"));
    }


    @Override
    public List<PassengerDto> getAllPassengers() {
        return passengerRepository.findAll().stream().map(this::convertToPassengerDto).toList();
    }


    @Override
    public void deletePassenger(String email, String password) {
        cabUserRepository.findById(getAuthId.getCurrentUserId())
                .ifPresentOrElse(cabUser -> {

                    if (!email.equals(cabUser.getEmail())) {
                        throw new RuntimeException("Email mismatch. Cannot delete account.");
                    }
                    if (!passwordEncoder.matches(password, cabUser.getPassword())) {
                        throw new RuntimeException("Incorrect password. Cannot delete account.");
                    }


                    cabUserRepository.deleteById(cabUser.getId());


                }, () -> {
                    throw new ResourceNotFound("Passenger not found");
                });
    }


    public long getUserId() {
        Passenger passenger = passengerRepository.findByCabUserId(getAuthId.getCurrentUserId());
        if (passenger != null) {
            return passenger.getId();
        } else {
            throw new ResourceNotFound("Passenger not found");
        }
    }

    public PassengerDto convertToPassengerDto(Passenger passenger) {
        return modelMapper.map(passenger, PassengerDto.class);
    }
}
