package com.nadila.MegaCityCab.service.Drivers;

import com.nadila.MegaCityCab.dto.DriversDto;
import com.nadila.MegaCityCab.requests.DriverUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IDriverService {
    DriversDto updateDriver(DriverUpdateRequest driverUpdateRequest, MultipartFile image);
    void deleteDriver(String email, String password);
    List<DriversDto> getByFirstName(String firstName);
    List<DriversDto> getAllDrivers();
}
