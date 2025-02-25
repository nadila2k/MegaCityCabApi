package com.nadila.MegaCityCab.response;

import com.nadila.MegaCityCab.InBuildUseObjects.JwtUserData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private JwtUserData jwtUserData;
    private String token;
}
