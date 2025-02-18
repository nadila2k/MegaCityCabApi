package com.nadila.MegaCityCab.dto;

import com.nadila.MegaCityCab.enums.Roles;
import lombok.Data;

@Data
public class CabUserDto {
    private Long id;
    private String email;
    private Roles roles;
}
