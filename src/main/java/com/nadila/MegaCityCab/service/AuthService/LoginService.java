package com.nadila.MegaCityCab.service.AuthService;

import com.nadila.MegaCityCab.InBuildUseObjects.ImagesObj;
import com.nadila.MegaCityCab.InBuildUseObjects.UserData;
import com.nadila.MegaCityCab.config.jwt.JwtUtil;
import com.nadila.MegaCityCab.enums.Roles;
import com.nadila.MegaCityCab.exception.AlreadyExistsException;
import com.nadila.MegaCityCab.exception.ResourceNotFound;
import com.nadila.MegaCityCab.model.Admin;
import com.nadila.MegaCityCab.model.CabUser;
import com.nadila.MegaCityCab.model.Drivers;
import com.nadila.MegaCityCab.model.Passenger;
import com.nadila.MegaCityCab.repository.*;
import com.nadila.MegaCityCab.requests.DriverRequest;
import com.nadila.MegaCityCab.requests.LoginRequest;
import com.nadila.MegaCityCab.requests.PassangerRequest;
import com.nadila.MegaCityCab.response.JwtResponse;
import com.nadila.MegaCityCab.service.Image.IImageService;
import com.nadila.MegaCityCab.service.Passenger.IPassangerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final AdminRepository adminRepository;
    private final DriverRepository driverRepository;
    private final PassengerRepository passengerRepository;
    private final JwtUtil jwtUtil;
    private final IPassangerService passengerService;
    private final CabUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VehicaleTypeRepository vehicaleTypeRepository;
    public final IImageService imageService;

    public JwtResponse passengerSignup(PassangerRequest  passangerRequest) {
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
                    passengerRepository.save(passenger);

                    LoginRequest loginRequest = new LoginRequest();
                    loginRequest.setEmail(passangerRequest.getEmail());
                    loginRequest.setPassword(passangerRequest.getPassword());
                    return signin(loginRequest);
                })
                .orElseThrow(() -> new AlreadyExistsException(passangerRequest.getEmail() + " already exists"));
    }

    public JwtResponse driverSignup(DriverRequest driverRequest,  MultipartFile image) {


            return Optional.of(driverRequest)
                    .filter(user ->
                            !userRepository.existsByEmail(driverRequest.getEmail()) &&
                                    !driverRepository.existsByVehicalNumber(driverRequest.getVehicalNumber()) &&
                                    vehicaleTypeRepository.existsByName(driverRequest.getVehicleType().getName())
                    )
                    .map(driverRequest1 -> {
                        CabUser cabUser = new CabUser();
                        cabUser.setEmail(driverRequest.getEmail());
                        cabUser.setPassword(passwordEncoder.encode(driverRequest.getPassword()));
                        cabUser.setRoles(driverRequest.getRoles());
                        return userRepository.save(cabUser);
                    }).map(cabUser -> {
                        ImagesObj images = imageService.uploadImage(image);

                        Drivers drivers = new Drivers();

                        drivers.setFirstName(driverRequest.getFirstName());
                        drivers.setLastName(driverRequest.getLastName());
                        drivers.setAddress(driverRequest.getAddress());
                        drivers.setMobileNumber(driverRequest.getMobileNumber());
                        drivers.setLicenseNumber(driverRequest.getLicenseNumber());
                        drivers.setVehicaleName(driverRequest.getVehicaleName());
                        drivers.setVehicalNumber(driverRequest.getVehicalNumber());
                        drivers.setImageUrl(images.getImageUrl());
                        drivers.setImageId(images.getImageId());
                        drivers.setVehicleType(driverRequest.getVehicleType());
                        drivers.setCabUser(cabUser);
                        driverRepository.save(drivers);

                        LoginRequest loginRequest = new LoginRequest();
                        loginRequest.setEmail(driverRequest.getEmail());
                        loginRequest.setPassword(driverRequest.getPassword());

                        return signin(loginRequest);
                    }).orElseThrow(() -> new AlreadyExistsException("Email or Vehicle number already exists"));

    }

   public JwtResponse signin(LoginRequest request) {
 
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtil.generateTokenForUser(authentication);
            CabUserDetails userDetails = (CabUserDetails) authentication.getPrincipal();

            UserData jwtUserData = new UserData();
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


            return new JwtResponse( jwtUserData, jwt);
        }catch (AuthenticationException e){
            throw new RuntimeException("Invalid email or password", e);
        }


   }



}
