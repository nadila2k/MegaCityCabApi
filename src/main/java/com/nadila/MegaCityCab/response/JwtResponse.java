package com.nadila.MegaCityCab.response;

import com.nadila.MegaCityCab.InBuildUseObjects.UserData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private UserData userData;
    private String token;
}
