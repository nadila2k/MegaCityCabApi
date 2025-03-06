package com.nadila.MegaCityCab.service.Drivers;

import com.nadila.MegaCityCab.InBuildUseObjects.ImagesObj;
import com.nadila.MegaCityCab.dto.CabUserDto;
import com.nadila.MegaCityCab.dto.DriverDto;
import com.nadila.MegaCityCab.exception.AlreadyExistsException;
import com.nadila.MegaCityCab.exception.ResourceNotFound;
import com.nadila.MegaCityCab.model.CabUser;
import com.nadila.MegaCityCab.model.Drivers;
import com.nadila.MegaCityCab.repository.CabUserRepository;
import com.nadila.MegaCityCab.repository.DriverRepository;
import com.nadila.MegaCityCab.repository.VehicaleTypeRepository;
import com.nadila.MegaCityCab.requests.DriverRequest;
import com.nadila.MegaCityCab.requests.DriverUpdateRequest;
import com.nadila.MegaCityCab.service.AuthService.GetAuthId;
import com.nadila.MegaCityCab.service.Image.IImageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DriverService implements IDriverService {

    private final CabUserRepository userRepository;
    private final VehicaleTypeRepository vehicaleTypeRepository;
    private final DriverRepository driverRepository;
    private final IImageService imageService;
    private final PasswordEncoder passwordEncoder;
    private final GetAuthId getAuthId;
    private final ModelMapper modelMapper;
    private final CabUserRepository cabUserRepository;


    @Override
    public DriverDto updateDriver(DriverUpdateRequest drivers, MultipartFile image) {

        if (image.isEmpty()) {
            return driverRepository.findById(getAuthUserId())
                    .map(existingDriver -> {
                        if (!existingDriver.getVehicalNumber().equals(drivers.getVehicalNumber())) {
                            boolean exists = driverRepository.existsByVehicalNumber(drivers.getVehicalNumber());
                            if (exists) {
                                throw new IllegalArgumentException("Vehicle number already exists");
                            }
                        }
                        existingDriver.setFirstName(drivers.getFirstName());
                        existingDriver.setLastName(drivers.getLastName());
                        existingDriver.setAddress(drivers.getAddress());
                        existingDriver.setMobileNumber(drivers.getMobileNumber());
                        existingDriver.setLicenseNumber(drivers.getLicenseNumber());
                        existingDriver.setVehicaleName(drivers.getVehicaleName());
                        existingDriver.setVehicalNumber(drivers.getVehicalNumber());
                        existingDriver.setVehicleType(drivers.getVehicleType());

                        return convertToDriverDto(driverRepository.save(existingDriver));

                    }).orElseThrow(() -> new ResourceNotFound("Driver  not found"));
        } else {
            return driverRepository.findById(getAuthUserId())
                    .map(existingDriver -> {
                        if (!existingDriver.getVehicalNumber().equals(drivers.getVehicalNumber())) {
                            Boolean exists = driverRepository.existsByVehicalNumber(drivers.getVehicalNumber());
                            if (exists) {
                                throw new IllegalArgumentException("Vehicle number already exists");
                            }
                        }
                        try {
                            imageService.deleteImage(drivers.getImageId());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        ImagesObj driverImages = imageService.uploadImage(image);

                        existingDriver.setFirstName(drivers.getFirstName());
                        existingDriver.setLastName(drivers.getLastName());
                        existingDriver.setAddress(drivers.getAddress());
                        existingDriver.setMobileNumber(drivers.getMobileNumber());
                        existingDriver.setLicenseNumber(drivers.getLicenseNumber());
                        existingDriver.setVehicaleName(drivers.getVehicaleName());
                        existingDriver.setVehicalNumber(drivers.getVehicalNumber());
                        existingDriver.setVehicleType(drivers.getVehicleType());
                        existingDriver.setImageUrl(driverImages.getImageUrl());
                        existingDriver.setImageId(driverImages.getImageId());
                        return convertToDriverDto(driverRepository.save(existingDriver));


                    }).orElseThrow(() -> new ResourceNotFound("Driver  not found"));
        }
    }

    @Override
    public void deleteDriver(String email, String password) {
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
                    throw new ResourceNotFound("Driver not found");
                });
    }

    @Override
    public List<DriverDto> getAllDrivers() {
        return driverRepository.findAll().stream().map(this::convertToDriverDto).toList();
    }

    @Override
    public List<DriverDto> getByFirstName(String firstName) {
        return Optional.of(driverRepository.findByFirstName(firstName).stream().map(this::convertToDriverDto).toList())
                .orElseThrow(() -> new ResourceNotFound("Driver not found"));
    }




    public Long getAuthUserId() {

        Drivers driver = driverRepository.findByCabUserId(getAuthId.getCurrentUserId());
        if (driver != null) {
            return driver.getId();
        } else {
            throw new ResourceNotFound("Driver not found for user");
        }

    }

    public DriverDto convertToDriverDto(Drivers drivers) {
        return modelMapper.map(drivers, DriverDto.class);
    }

}


