package com.nadila.MegaCityCab.service.Drivers;

import com.nadila.MegaCityCab.dto.DriverDto;
import com.nadila.MegaCityCab.model.Drivers;
import com.nadila.MegaCityCab.requests.DriverRequest;
import com.nadila.MegaCityCab.requests.DriverUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IDriverService {
    DriverDto updateDriver(DriverUpdateRequest driverUpdateRequest, MultipartFile image);
    void deleteDriver(String email, String password);
    List<DriverDto> getByFirstName(String firstName);
    List<DriverDto> getAllDrivers();
}
