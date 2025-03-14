package com.nadila.MegaCityCab.service.VehicaleType;

import com.nadila.MegaCityCab.InBuildUseObjects.ImagesObj;
import com.nadila.MegaCityCab.exception.AlreadyExistsException;
import com.nadila.MegaCityCab.exception.ResourceNotFound;
import com.nadila.MegaCityCab.model.VehicleType;
import com.nadila.MegaCityCab.repository.VehicaleTypeRepository;

import com.nadila.MegaCityCab.requests.VehicaleUpdateRequest;
import com.nadila.MegaCityCab.requests.VehicleTypeRequest;
import com.nadila.MegaCityCab.service.Image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicalTypeService implements IVehicalTypeService {

    private final VehicaleTypeRepository vehicaleTypeRepository;
    private final ImageService imageService;



    public VehicleType createVehicleType(VehicleTypeRequest vehicleTypeRequest)  {
        if (vehicaleTypeRepository.existsByName(vehicleTypeRequest.getName())) {
            throw new AlreadyExistsException("Vehicle type '" + vehicleTypeRequest.getName() + "' already exists.");
        }

        ImagesObj imagesObj = imageService.uploadImage(vehicleTypeRequest.getImage());
        VehicleType vehicleType = new VehicleType();
        vehicleType.setName(vehicleTypeRequest.getName());
        vehicleType.setPrice(vehicleTypeRequest.getPrice());
        vehicleType.setImageId(imagesObj.getImageId());
        vehicleType.setImageUrl(imagesObj.getImageUrl());

        return vehicaleTypeRepository.save(vehicleType);
    }

//    @Override
//    public VehicleType createVehicalType(VehicaleTypeRequest vehicaleTypeRequest, MultipartFile image) {
//        return Optional.of(vehicaleTypeRequest)
//                .filter( vehicleType1 -> !vehicaleTypeRepository.existsByName(vehicaleTypeRequest.getName()))
//                .map(vehicaleTypeRequest1 -> {
//
//
//                    ImagesObj imagesObj = imageService.uploadImage(image);
//                    VehicleType vehicleType = new VehicleType();
//
//                    vehicleType.setName(vehicaleTypeRequest.getName());
//                    vehicleType.setPrice(vehicaleTypeRequest.getPrice());
//                    vehicleType.setImageId(imagesObj.getImageId());
//                    vehicleType.setImageUrl(imagesObj.getImageUrl());
//
//                    return vehicaleTypeRepository.save(vehicleType);
//
//                })
//                .orElseThrow(() -> new AlreadyExistsException("Vehicle type '" + vehicaleTypeRequest.getName() + "' already exists."));
//    }

    @Override
    public VehicleType updateVehicalType(long id, VehicaleUpdateRequest  vehicleType) {

        if (vehicleType.getImage().isEmpty()) {
            return vehicaleTypeRepository.findById(id)
                    .map(existingType -> {
                        existingType.setName(vehicleType.getName());
                        existingType.setPrice(vehicleType.getPrice());
                        return vehicaleTypeRepository.save(existingType);
                    }).orElseThrow(() -> new ResourceNotFound("Vehicle type not found."));
        }else{
            return vehicaleTypeRepository.findById(id)
                    .map(existingtype -> {
                        try {
                            imageService.deleteImage(existingtype.getImageId());
                            ImagesObj imagesObj = imageService.uploadImage(vehicleType.getImage());
                            existingtype.setImageUrl(imagesObj.getImageUrl());
                            existingtype.setImageId(imagesObj.getImageId());
                            existingtype.setName(vehicleType.getName());
                            existingtype.setPrice(vehicleType.getPrice());
                            return vehicaleTypeRepository.save(existingtype);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }


                    }).orElseThrow(() ->  new ResourceNotFound("Vehicle type not found."));
        }
    }

    @Override
    public List<VehicleType> getAllVehicalType() {
        return vehicaleTypeRepository.findAll();
    }

    @Override
    public void deleteVehicalType(long id) {
        vehicaleTypeRepository.findById(id)
                .ifPresentOrElse(vehicaleTypeRepository::delete,() -> {throw new ResourceNotFound("Vehicle type not found");});
    }



    @Override
    public VehicleType getVehicalTypeByName(String name) {
        return Optional.ofNullable(vehicaleTypeRepository.findByName(name))
                .orElseThrow(() -> new ResourceNotFound("Vehicle type not found"));
    }
}
