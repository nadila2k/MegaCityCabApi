package com.nadila.MegaCityCab.service.Image;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nadila.MegaCityCab.InBuildUseObjects.ImagesObj;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService{

    private final Cloudinary cloudinary;

    public ImagesObj uploadImage(MultipartFile file) {
        String fileName = file.getOriginalFilename();

        // Validate file extension
        if (fileName != null && !fileName.matches("(?i).*\\.(jpg|jpeg|png)$")) {
            throw new IllegalArgumentException("Invalid file format. Only JPG, JPEG, and PNG are allowed.");
        }

        String imageUrl;
        String imageId;
        try {
            Map uploadRes = cloudinary.uploader().upload(file.getBytes(), Map.of());
            imageUrl = uploadRes.get("url").toString();
            imageId = uploadRes.get("public_id").toString();

            ImagesObj imageDetails = new ImagesObj();
            imageDetails.setImageUrl(imageUrl);
            imageDetails.setImageId(imageId);

            return imageDetails;
        } catch (IOException e) {
            throw new RuntimeException("Error uploading image", e);
        }
    }



    public void deleteImage(String imageId) throws IOException {
        cloudinary.uploader().destroy(imageId, ObjectUtils.emptyMap());
    }

}
