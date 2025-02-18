package com.nadila.MegaCityCab.service.VehicaleType;

import com.nadila.MegaCityCab.exception.AlreadyExistsException;
import com.nadila.MegaCityCab.exception.ResourceNotFound;
import com.nadila.MegaCityCab.model.VehicleType;
import com.nadila.MegaCityCab.repository.VehicaleTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicalTypeService implements IVehicalTypeService {

    private final VehicaleTypeRepository vehicaleTypeRepository;

    @Override
    public VehicleType createVehicalType(VehicleType vehicleType) {
        return Optional.of(vehicleType)
                .filter( vehicleType1 -> !vehicaleTypeRepository.existsByName(vehicleType.getName()))
                .map(vehicaleTypeRepository::save)
                .orElseThrow(() -> new AlreadyExistsException("Vehicle type '" + vehicleType.getName() + "' already exists."));
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
