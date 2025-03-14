package com.nadila.MegaCityCab.service.VehicaleType;

import com.nadila.MegaCityCab.model.VehicleType;
import com.nadila.MegaCityCab.requests.VehicleTypeRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IVehicalTypeService {

    VehicleType  createVehicleType(VehicleTypeRequest vehicleTypeRequest);
    VehicleType updateVehicalType(long id, VehicleType vehicleType, MultipartFile image);
    void deleteVehicalType(long id);
    List<VehicleType>  getAllVehicalType();
    VehicleType getVehicalTypeByName(String name);
}
