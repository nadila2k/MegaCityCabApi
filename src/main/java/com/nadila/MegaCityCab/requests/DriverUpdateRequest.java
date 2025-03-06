package com.nadila.MegaCityCab.requests;

import com.nadila.MegaCityCab.model.VehicleType;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverUpdateRequest {

    private String firstName;
    private String lastName;
    private String address;
    private String mobileNumber;
    private int licenseNumber;
    private String vehicaleName;
    private String vehicalNumber;
    private String imageUrl;
    private String imageId;
    private VehicleType vehicleType;
}
