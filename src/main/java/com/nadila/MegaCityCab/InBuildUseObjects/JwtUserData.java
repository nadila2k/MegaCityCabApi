package com.nadila.MegaCityCab.InBuildUseObjects;

import com.nadila.MegaCityCab.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtUserData {
    private Long id;
    private Roles roles;
    private String email;
    private String firstName;
    private String lastName;
}
