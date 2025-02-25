package com.nadila.MegaCityCab.controller;

import com.nadila.MegaCityCab.InBuildUseObjects.JwtUserData;
import com.nadila.MegaCityCab.config.jwt.JwtUtil;
import com.nadila.MegaCityCab.enums.ResponseStatus;
import com.nadila.MegaCityCab.enums.Roles;
import com.nadila.MegaCityCab.exception.ResourceNotFound;
import com.nadila.MegaCityCab.model.Admin;
import com.nadila.MegaCityCab.model.Drivers;
import com.nadila.MegaCityCab.model.Passenger;
import com.nadila.MegaCityCab.repository.AdminRepository;
import com.nadila.MegaCityCab.repository.DriverRepository;
import com.nadila.MegaCityCab.repository.PassengerRepository;
import com.nadila.MegaCityCab.requests.LoginRequest;
import com.nadila.MegaCityCab.response.ApiResponse;
import com.nadila.MegaCityCab.response.JwtResponse;
import com.nadila.MegaCityCab.service.AuthService.CabUserDetailes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AdminRepository adminRepository;
    private final DriverRepository driverRepository;
    private final PassengerRepository passengerRepository;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {
        System.out.println("test login");
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtil.generateTokenForUser(authentication);
            CabUserDetailes userDetails = (CabUserDetailes) authentication.getPrincipal();

            JwtUserData jwtUserData = new JwtUserData();
            if (userDetails.getCabUser().getRoles().equals(Roles.ADMIN)) {
                Admin admin = adminRepository.findByCabUserId(userDetails.getCabUser().getId());
                jwtUserData.setFirstName(admin.getFirstName());
                jwtUserData.setLastName(admin.getLastName());

            } else if (userDetails.getCabUser().getRoles().equals(Roles.PASSENGER)) {
                Passenger passenger = passengerRepository.findByCabUserId(userDetails.getCabUser().getId());
                jwtUserData.setFirstName(passenger.getFirstName());
                jwtUserData.setLastName(passenger.getLastName());
            }else if (userDetails.getCabUser().getRoles().equals(Roles.DRIVER)) {
                Drivers driver = driverRepository.findByCabUserId(userDetails.getCabUser().getId());
                jwtUserData.setFirstName(driver.getFirstName());
                jwtUserData.setLastName(driver.getLastName());
            }else {
                throw  new ResourceNotFound("User not found");
            }

            jwtUserData.setId(userDetails.getCabUser().getId());
            jwtUserData.setEmail(userDetails.getCabUser().getEmail());
            jwtUserData.setRoles(userDetails.getCabUser().getRoles());



            JwtResponse jwtResponse = new JwtResponse( jwtUserData, jwt);
            return ResponseEntity.ok(new ApiResponse(ResponseStatus.SUCCESS,"Login Success!", jwtResponse));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(ResponseStatus.FAILURE, e.getMessage(), null));
        }

    }



}
