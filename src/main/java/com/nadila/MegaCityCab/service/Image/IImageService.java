package com.nadila.MegaCityCab.service.Image;

import com.nadila.MegaCityCab.InBuildUseObjects.ImagesObj;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IImageService {

    ImagesObj uploadImage(MultipartFile file);
    void deleteImage(String imageId) throws IOException;

}
