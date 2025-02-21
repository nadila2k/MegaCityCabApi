package com.nadila.MegaCityCab.service.Drivers;

import com.nadila.MegaCityCab.dto.DriverDto;
import com.nadila.MegaCityCab.model.Drivers;
import com.nadila.MegaCityCab.requests.DriverRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IDriverService {
    DriverDto createDriver(DriverRequest driverRequest, MultipartFile image);
    Drivers updateDriver(long id, Drivers drivers, MultipartFile image);
    void deleteDriver(long id);
    List<Drivers> getByFirstName(String firstName);
    List<Drivers> getAllDrivers();
}
