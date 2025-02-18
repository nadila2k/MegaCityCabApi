package com.nadila.MegaCityCab.controller;

import com.nadila.MegaCityCab.dto.PassengerDto;
import com.nadila.MegaCityCab.exception.AlreadyExistsException;
import com.nadila.MegaCityCab.model.CabUser;
import com.nadila.MegaCityCab.model.Passenger;
import com.nadila.MegaCityCab.requests.PassangerRequest;
import com.nadila.MegaCityCab.response.ApiResponse;
import com.nadila.MegaCityCab.service.Passenger.IPassangerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CONFLICT;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/passenger")
public class PassengerController {

    private final IPassangerService passangerService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createUser(@RequestBody PassangerRequest passangerRequest){

        System.out.println(passangerRequest+ "controller");
        try {
            PassengerDto passenger = passangerService.createUser(passangerRequest);
            return ResponseEntity.ok(new ApiResponse("User created successfully",passenger));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(),null));
        }
    }
}
