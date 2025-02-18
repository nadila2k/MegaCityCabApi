package com.nadila.MegaCityCab.service.Passenger;

import com.nadila.MegaCityCab.dto.CabUserDto;
import com.nadila.MegaCityCab.dto.PassengerDto;
import com.nadila.MegaCityCab.exception.AlreadyExistsException;
import com.nadila.MegaCityCab.model.CabUser;
import com.nadila.MegaCityCab.model.Passenger;
import com.nadila.MegaCityCab.repository.CabUserRepository;
import com.nadila.MegaCityCab.repository.PassengerRepository;
import com.nadila.MegaCityCab.requests.PassangerRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PassangerService implements IPassangerService{
    private final PassengerRepository passengerRepository;
    private final CabUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PassengerDto createUser(PassangerRequest passangerRequest) {


        return Optional.of(passangerRequest)
                .filter(user -> !userRepository.existsByEmail(user.getEmail()) )
                .map(passanger -> {
                    CabUser cabUser = new CabUser();
                    cabUser.setEmail(passangerRequest.getEmail());
                    cabUser.setPassword(passwordEncoder.encode(passangerRequest.getPassword()));
                    cabUser.setRoles(passangerRequest.getRoles());
                    return userRepository.save(cabUser);
                })
                .map(cabUser -> {

                    System.out.println(passangerRequest+ " in ");
                    Passenger passenger = new Passenger();
                    passenger.setFirstName(passangerRequest.getFirstName());
                    passenger.setLastName(passangerRequest.getLastName());
                    passenger.setAddress(passangerRequest.getAddress());
                    passenger.setMobileNumber(passangerRequest.getMobileNumber());
                    passenger.setCabUser(cabUser);

                    return convertToPassengerDto(cabUser, passengerRepository.save(passenger));
                })
                .orElseThrow(() -> new AlreadyExistsException(passangerRequest.getEmail() + " already exists"));
    }

    public PassengerDto convertToPassengerDto(CabUser cabUser, Passenger passenger){
        CabUserDto cabUserDto = new CabUserDto();
        cabUserDto.setId(cabUser.getId());
        cabUserDto.setEmail(cabUser.getEmail());
        cabUserDto.setRoles(cabUser.getRoles());

        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setId(passenger.getId());
        passengerDto.setFirstName(passenger.getFirstName());
        passengerDto.setLastName(passenger.getLastName());
        passengerDto.setAddress(passenger.getAddress());
        passengerDto.setMobileNumber(passenger.getMobileNumber());
        passengerDto.setCabUserDto(cabUserDto);

        return passengerDto;
    }
}
