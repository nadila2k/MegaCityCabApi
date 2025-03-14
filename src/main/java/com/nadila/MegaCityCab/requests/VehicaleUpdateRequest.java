package com.nadila.MegaCityCab.requests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class VehicaleUpdateRequest {
    private String name;
    private double price;
    private String imageUrl;
    private String imageId;
    private MultipartFile image;
}
