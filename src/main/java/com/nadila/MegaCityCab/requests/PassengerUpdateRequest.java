package com.nadila.MegaCityCab.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengerUpdateRequest {

    private long id;
    private String firstName;
    private String lastName;
    private String address;
    private String mobileNumber;

}
