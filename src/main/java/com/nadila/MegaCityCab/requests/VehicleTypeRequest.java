package com.nadila.MegaCityCab.requests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class VehicleTypeRequest {
    private String name;
    private double price;
    private MultipartFile image;
}
