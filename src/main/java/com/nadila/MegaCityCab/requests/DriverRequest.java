package com.nadila.MegaCityCab.requests;

import com.nadila.MegaCityCab.enums.Roles;
import com.nadila.MegaCityCab.model.VehicleType;
import lombok.Data;

@Data
public class DriverRequest {

    private String email;
    private String password;
    private Roles roles = Roles.DRIVER;
    private String firstName;
    private String lastName;
    private String address;
    private String mobileNumber;
    private String vehicaleName;
    private String vehicalNumber;
    private VehicleType vehicleType;

}
