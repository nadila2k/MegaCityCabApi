package com.nadila.MegaCityCab.controller;

import com.nadila.MegaCityCab.config.jwt.JwtUtil;
import com.nadila.MegaCityCab.enums.ResponseStatus;
import com.nadila.MegaCityCab.exception.AlreadyExistsException;
import com.nadila.MegaCityCab.repository.AdminRepository;
import com.nadila.MegaCityCab.repository.DriverRepository;
import com.nadila.MegaCityCab.repository.PassengerRepository;
import com.nadila.MegaCityCab.requests.DriverRequest;
import com.nadila.MegaCityCab.requests.LoginRequest;
import com.nadila.MegaCityCab.requests.PassangerRequest;
import com.nadila.MegaCityCab.response.ApiResponse;
import com.nadila.MegaCityCab.response.JwtResponse;
import com.nadila.MegaCityCab.service.AuthService.LoginService;
import com.nadila.MegaCityCab.service.Passenger.IPassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.HttpStatus.CONFLICT;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {


    private final LoginService loginService;

    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {
        System.out.println("test login");
        try {
            JwtResponse jwtResponse = loginService.signin(request);

            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(ResponseStatus.SUCCESS, "Login Success!", jwtResponse));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(ResponseStatus.FAILURE, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(ResponseStatus.FAILURE, e.getMessage(), null));
        }

    }

    @PostMapping("/sign-up/passenger")
    public ResponseEntity<ApiResponse> passengerSignup(@RequestBody PassangerRequest passangerRequest) {
        try {
            JwtResponse jwtResponse = loginService.passengerSignup(passangerRequest);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(ResponseStatus.SUCCESS, "User signup successfully", jwtResponse));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(ResponseStatus.FAILURE, e.getMessage(), null));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(ResponseStatus.FAILURE, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(ResponseStatus.FAILURE, e.getMessage(), null));
        }
    }


    @PostMapping("/sign-up/driver")
    public ResponseEntity<ApiResponse> driverSignup(@RequestPart DriverRequest driverRequest, @RequestPart MultipartFile image) {
        try {
            JwtResponse jwtResponse = loginService.driverSignup(driverRequest, image);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(ResponseStatus.SUCCESS, "Driver signup successfully", jwtResponse));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(ResponseStatus.FAILURE, e.getMessage(), null));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(ResponseStatus.FAILURE, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(ResponseStatus.FAILURE, e.getMessage(), null));
        }
    }


}
