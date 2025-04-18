package com.nadila.MegaCityCab.dto;

import com.nadila.MegaCityCab.model.VehicleType;
import lombok.Data;

@Data
public class DriversDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String mobileNumber;
    private int licenseNumber;
    private String vehicaleName;
    private String vehicalNumber;
    private String imageUrl;
    private String imageId;
    private CabUserDto cabUserDto;
    private VehicleType vehicleType;
}
