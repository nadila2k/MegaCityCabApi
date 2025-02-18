package com.nadila.MegaCityCab.requests;

import com.nadila.MegaCityCab.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassangerRequest {
    private String email;
    private String password;
    private Roles roles = Roles.PASSENGER;
    private String firstName;
    private String lastName;
    private String address;
    private String mobileNumber;

}
