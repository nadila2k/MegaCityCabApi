package com.nadila.MegaCityCab.service.Drivers;

import com.nadila.MegaCityCab.InBuildUseObjects.ImagesObj;
import com.nadila.MegaCityCab.dto.DriversDto;
import com.nadila.MegaCityCab.exception.ImageProcessingException;
import com.nadila.MegaCityCab.exception.ResourceNotFound;
import com.nadila.MegaCityCab.model.Drivers;
import com.nadila.MegaCityCab.repository.CabUserRepository;
import com.nadila.MegaCityCab.repository.DriverRepository;
import com.nadila.MegaCityCab.repository.VehicaleTypeRepository;
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
    public DriversDto updateDriver(DriverUpdateRequest drivers) {

        return driverRepository.findById(getAuthUserId())
                .map(existingDriver -> {

                    // Vehicle number validation check
                    if (!existingDriver.getVehicalNumber().equals(drivers.getVehicalNumber())) {
                        boolean isVehicleNumberTaken = driverRepository.existsByVehicalNumber(drivers.getVehicalNumber());
                        if (isVehicleNumberTaken) {
                            throw new IllegalArgumentException("Vehicle number already exists");
                        }
                    }

                    // Update driver details
                    existingDriver.setFirstName(drivers.getFirstName());
                    existingDriver.setLastName(drivers.getLastName());
                    existingDriver.setAddress(drivers.getAddress());
                    existingDriver.setMobileNumber(drivers.getMobileNumber());
                    existingDriver.setLicenseNumber(drivers.getLicenseNumber());
                    existingDriver.setVehicaleName(drivers.getVehicaleName());
                    existingDriver.setVehicalNumber(drivers.getVehicalNumber());

                    // Set vehicle type, ensuring the vehicle type exists
                    existingDriver.setVehicleType(vehicaleTypeRepository.findById(drivers.getVehicleTypeId())
                            .orElseThrow(() -> new ResourceNotFound("Vehicle type not found")));

                    // Handle image if provided
                    if (!drivers.getImage().isEmpty()) {
                        try {
                            // Delete the old image if exists
                            imageService.deleteImage(drivers.getImageId());

                            // Upload the new image
                            ImagesObj driverImages = imageService.uploadImage(drivers.getImage());

                            // Set the new image URL and ID on the existing driver
                            existingDriver.setImageUrl(driverImages.getImageUrl());
                            existingDriver.setImageId(driverImages.getImageId());

                        } catch (IOException e) {
                            // Rethrow the IOException as a RuntimeException (unchecked exception)
                            throw new RuntimeException("Error processing image", e);
                        }
                    }

                    // Save and return updated driver as DTO
                    return convertToDriverDto(driverRepository.save(existingDriver));

                }).orElseThrow(() -> new ResourceNotFound("Driver not found"));
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
    public List<DriversDto> getAllDrivers() {
        return driverRepository.findAll().stream().map(this::convertToDriverDto).toList();
    }

    @Override
    public List<DriversDto> getByFirstName(String firstName) {
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

    @Override
    public void deleteDriverAdmin(Long driverId) {
        driverRepository.findById(driverId)
                .ifPresent(driver -> {cabUserRepository.deleteById(driver.getCabUser().getId());});

    }

    @Override
    public DriversDto getDriver() {
        return driverRepository.findById(getAuthUserId())
                .map(this::convertToDriverDto)
                .orElseThrow(() -> new ResourceNotFound("Driver not found"));
    }

    @Override
    public DriversDto updateDriverAdmin(Long driverId, DriverUpdateRequest drivers) {

        return driverRepository.findById(driverId)
                .map(existingDriver -> {

                    // Vehicle number validation check
                    if (!existingDriver.getVehicalNumber().equals(drivers.getVehicalNumber())) {
                        boolean isVehicleNumberTaken = driverRepository.existsByVehicalNumber(drivers.getVehicalNumber());
                        if (isVehicleNumberTaken) {
                            throw new IllegalArgumentException("Vehicle number already exists");
                        }
                    }

                    // Update driver details
                    existingDriver.setFirstName(drivers.getFirstName());
                    existingDriver.setLastName(drivers.getLastName());
                    existingDriver.setAddress(drivers.getAddress());
                    existingDriver.setMobileNumber(drivers.getMobileNumber());
                    existingDriver.setLicenseNumber(drivers.getLicenseNumber());
                    existingDriver.setVehicaleName(drivers.getVehicaleName());
                    existingDriver.setVehicalNumber(drivers.getVehicalNumber());

                    // Set vehicle type, ensuring the vehicle type exists
                    existingDriver.setVehicleType(
                            vehicaleTypeRepository.findById(drivers.getVehicleTypeId())
                                    .orElseThrow(() -> new ResourceNotFound("Vehicle type not found"))
                    );

                    // Handle image if provided
                    if (drivers.getImage() != null && !drivers.getImage().isEmpty()) {
                        try {
                            // Delete the old image if it exists
                            if (existingDriver.getImageId() != null) {
                                imageService.deleteImage(existingDriver.getImageId());
                            }

                            // Upload the new image
                            ImagesObj driverImages = imageService.uploadImage(drivers.getImage());

                            // Set the new image URL and ID on the existing driver
                            existingDriver.setImageUrl(driverImages.getImageUrl());
                            existingDriver.setImageId(driverImages.getImageId());

                        } catch (IOException e) {
                            throw new RuntimeException("Error processing image", e);
                        }
                    }

                    // Save and return updated driver as DTO
                    return convertToDriverDto(driverRepository.save(existingDriver));

                }).orElseThrow(() -> new ResourceNotFound("Driver not found with ID: " + driverId));
    }


    public DriversDto convertToDriverDto(Drivers drivers) {
        return modelMapper.map(drivers, DriversDto.class);
    }

}


