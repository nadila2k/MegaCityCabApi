package com.nadila.MegaCityCab.requests;

import com.nadila.MegaCityCab.enums.Roles;
import com.nadila.MegaCityCab.model.VehicleType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DriverRequest {

    private String email;
    private String password;
    private Roles roles = Roles.DRIVER;
    private String firstName;
    private String lastName;
    private String address;
    private String mobileNumber;
    private int licenseNumber;
    private String vehicleName;
    private String vehicleNumber;
    private long vehicleTypeId;
    private MultipartFile image;

}
