package com.nadila.MegaCityCab.service.Drivers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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
import com.nadila.MegaCityCab.service.AuthService.GetAuthId;
import com.nadila.MegaCityCab.service.image.IImageService;
import com.nadila.MegaCityCab.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DriverService implements IDriverService{

    private final CabUserRepository userRepository;
    private final VehicaleTypeRepository vehicaleTypeRepository;
    private final DriverRepository driverRepository;
    private final IImageService imageService;
    private final PasswordEncoder passwordEncoder;
    private final GetAuthId getAuthId;

    @Override
    public DriverDto createDriver(DriverRequest driverRequest, MultipartFile image) {

        return Optional.of(driverRequest)
                .filter(user -> !userRepository.existsByEmail(driverRequest.getEmail()) && !driverRepository.existsByVehicalNumber(driverRequest.getVehicalNumber())
                && vehicaleTypeRepository.existsByName(driverRequest.getVehicleType().getName()))
                .map(driverRequest1 -> {
                    CabUser cabUser = new CabUser();
                    cabUser.setEmail(driverRequest.getEmail());
                    cabUser.setPassword(passwordEncoder.encode(driverRequest.getPassword()));
                    return userRepository.save(cabUser);
                }).map(cabUser -> {
                     ImagesObj images = imageService.uploadImage(image);

                     Drivers drivers = new Drivers();
                     drivers.setFirstName(driverRequest.getFirstName());
                     drivers.setLastName(driverRequest.getLastName());
                     drivers.setAddress(driverRequest.getAddress());
                     drivers.setMobileNumber(driverRequest.getMobileNumber());
                     drivers.setVehicaleName(driverRequest.getVehicaleName());
                     drivers.setVehicalNumber(driverRequest.getVehicalNumber());
                     drivers.setImageUrl(images.getImageUrl());
                     drivers.setImageId(images.getImageId());
                     drivers.setVehicleType(driverRequest.getVehicleType());
                    driverRepository.save(drivers);
                     return convertToDriverDto(cabUser, drivers);
                }).orElseThrow(() -> new AlreadyExistsException("Email or Vehicle number already exists"));
    }

    @Override
    public Drivers updateDriver(long id, Drivers drivers, MultipartFile image) {

        if (image.isEmpty()) {
            return driverRepository.findById(getAuthUserId())
                    .map(existingDriver  -> {
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
                        existingDriver.setVehicaleName(drivers.getVehicaleName());
                        existingDriver.setVehicalNumber(drivers.getVehicalNumber());
                        existingDriver.setVehicleType(drivers.getVehicleType());

                        return driverRepository.save(existingDriver);

                    }).orElseThrow(() -> new ResourceNotFound("Driver  not found"));
        }else {
            return driverRepository.findById(id)
                    .map(existingDriver ->{
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
                        existingDriver.setVehicaleName(drivers.getVehicaleName());
                        existingDriver.setVehicalNumber(drivers.getVehicalNumber());
                        existingDriver.setVehicleType(drivers.getVehicleType());
                        existingDriver.setImageUrl(driverImages.getImageUrl());
                        existingDriver.setImageId(driverImages.getImageId());
                        return driverRepository.save(existingDriver);


                    }).orElseThrow(() ->  new ResourceNotFound("Driver  not found"));
        }
    }
    @Override
    public void deleteDriver(long id) {
        driverRepository.findById(id).ifPresentOrElse(drivers -> {
            try {
                imageService.deleteImage(drivers.getImageId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            driverRepository.delete(drivers); // Delete driver after image is handled
        }, () -> {
            throw new ResourceNotFound("Driver not found");
        });
    }
    @Override
    public List<Drivers> getByFirstName(String firstName) {
        return Optional.ofNullable(driverRepository.findByFirstName(firstName))
                .orElseThrow(() ->  new ResourceNotFound("Driver not found"));
    }

    @Override
    public List<Drivers> getAllDrivers() {
        return driverRepository.findAll();
    }

    public DriverDto convertToDriverDto(CabUser cabUser, Drivers drivers){
       CabUserDto cabUserDto = new CabUserDto();
       cabUserDto.setId(cabUser.getId());
       cabUserDto.setRoles(cabUser.getRoles());
       cabUserDto.setEmail(cabUser.getEmail());

       DriverDto driverDto = new DriverDto();
       driverDto.setId(drivers.getId());
       driverDto.setFirstName(drivers.getFirstName());
       driverDto.setLastName(drivers.getLastName());
       driverDto.setAddress(drivers.getAddress());
       driverDto.setMobileNumber(drivers.getMobileNumber());
       driverDto.setVehicaleName(drivers.getVehicaleName());
       driverDto.setVehicalNumber(drivers.getVehicalNumber());
       driverDto.setImageUrl(drivers.getImageUrl());
       driverDto.setImageId(drivers.getImageId());
       driverDto.setCabUserDto(cabUserDto);
       driverDto.setVehicleType(drivers.getVehicleType());

       return driverDto;
    }

    public Long getAuthUserId(){

        Drivers driver = driverRepository.findByCabUserId(getAuthId.getCurrentUserId());
        if (driver != null) {
            return driver.getId();
        } else {
            throw new ResourceNotFound("Driver not found for user");
        }

    }

}


