package com.nadila.MegaCityCab.service.VehicaleType;

import com.nadila.MegaCityCab.InBuildUseObjects.ImagesObj;
import com.nadila.MegaCityCab.exception.AlreadyExistsException;
import com.nadila.MegaCityCab.exception.ResourceNotFound;
import com.nadila.MegaCityCab.model.VehicleType;
import com.nadila.MegaCityCab.repository.VehicaleTypeRepository;
import com.nadila.MegaCityCab.requests.VehicaleTypeRequest;
import com.nadila.MegaCityCab.service.Image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicalTypeService implements IVehicalTypeService {

    private final VehicaleTypeRepository vehicaleTypeRepository;
    private final ImageService imageService;


    @Override
    public VehicleType createVehicalType(VehicaleTypeRequest vehicaleTypeRequest, MultipartFile image) {
        return Optional.of(vehicaleTypeRequest)
                .filter( vehicleType1 -> !vehicaleTypeRepository.existsByName(vehicaleTypeRequest.getName()))
                .map(vehicaleTypeRequest1 -> {

                    ImagesObj imagesObj =  imageService.uploadImage(image);
                    VehicleType vehicleType = new VehicleType();

                    vehicleType.setName(vehicaleTypeRequest.getName());
                    vehicleType.setPrice(vehicaleTypeRequest.getPrice());
                    vehicleType.setImageId(imagesObj.getImageId());
                    vehicleType.setImageUrl(imagesObj.getImageUrl());

                    return vehicaleTypeRepository.save(vehicleType);

                })
                .orElseThrow(() -> new AlreadyExistsException("Vehicle type '" + vehicaleTypeRequest.getName() + "' already exists."));
    }

    @Override
    public VehicleType updateVehicalType(long id, VehicleType vehicleType) {
        return vehicaleTypeRepository.findById(id)
                .map(vehicleType1 -> {
                    vehicleType1.setName(vehicleType.getName());
                    vehicleType1.setPrice(vehicleType.getPrice());
                    return vehicaleTypeRepository.save(vehicleType1);
                }).orElseThrow(() -> new ResourceNotFound("Vehicle type not found"));
    }

    @Override
    public void deleteVehicalType(long id) {
        vehicaleTypeRepository.findById(id)
                .ifPresentOrElse(vehicaleTypeRepository::delete,() -> {throw new ResourceNotFound("Vehicle type not found");});
    }

    @Override
    public List<VehicleType> getAllVehicalType() {
        return vehicaleTypeRepository.findAll();
    }

    @Override
    public VehicleType getVehicalTypeByName(String name) {
        return Optional.ofNullable(vehicaleTypeRepository.findByName(name))
                .orElseThrow(() -> new ResourceNotFound("Vehicle type not found"));
    }
}
