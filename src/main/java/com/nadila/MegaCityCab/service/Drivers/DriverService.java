package com.nadila.MegaCityCab.service.Drivers;

import com.cloudinary.Cloudinary;
import com.nadila.MegaCityCab.InBuildUseObjects.DriverImages;
import com.nadila.MegaCityCab.dto.CabUserDto;
import com.nadila.MegaCityCab.dto.DriverDto;
import com.nadila.MegaCityCab.exception.AlreadyExistsException;
import com.nadila.MegaCityCab.model.CabUser;
import com.nadila.MegaCityCab.model.Drivers;
import com.nadila.MegaCityCab.repository.CabUserRepository;
import com.nadila.MegaCityCab.repository.DriverRepository;
import com.nadila.MegaCityCab.repository.VehicaleTypeRepository;
import com.nadila.MegaCityCab.requests.DriverRequest;
import lombok.RequiredArgsConstructor;
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
    private final Cloudinary cloudinary;
    private final PasswordEncoder passwordEncoder;

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
                     DriverImages images = uploadImage(image);

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
        return null;
    }

    @Override
    public void deleteDriver(long id) {

    }

    @Override
    public Drivers getByFirstName(String firstName) {
        return null;
    }

    @Override
    public List<Drivers> getAllDrivers() {
        return List.of();
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
    public DriverImages uploadImage(MultipartFile file) {
        String fileName = file.getOriginalFilename();

        // Validate file extension
        if (fileName != null && !fileName.matches("(?i).*\\.(jpg|jpeg|png)$")) {
            throw new IllegalArgumentException("Invalid file format. Only JPG, JPEG, and PNG are allowed.");
        }

        String imageUrl;
        String imageId;
        try {
            Map uploadRes = cloudinary.uploader().upload(file.getBytes(), Map.of());
            imageUrl = uploadRes.get("url").toString();
            imageId = uploadRes.get("public_id").toString();

            DriverImages imageDetails = new DriverImages();
            imageDetails.setImageUrl(imageUrl);
            imageDetails.setImageId(imageId);

            return imageDetails;
        } catch (IOException e) {
            throw new RuntimeException("Error uploading image", e);
        }
    }

}
