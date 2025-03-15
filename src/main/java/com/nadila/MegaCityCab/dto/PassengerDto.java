package com.nadila.MegaCityCab.dto;

import lombok.Data;

@Data
public class PassengerDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String mobileNumber;
}
