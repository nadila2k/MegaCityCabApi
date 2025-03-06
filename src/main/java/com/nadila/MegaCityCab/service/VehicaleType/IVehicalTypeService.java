package com.nadila.MegaCityCab.service.VehicaleType;

import com.nadila.MegaCityCab.model.VehicleType;
import com.nadila.MegaCityCab.requests.VehicaleTypeRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IVehicalTypeService {

    VehicleType createVehicalType(VehicaleTypeRequest vehicaleTypeRequest, MultipartFile image);
    VehicleType updateVehicalType(long id, VehicleType vehicleType);
    void deleteVehicalType(long id);
    List<VehicleType>  getAllVehicalType();
    VehicleType getVehicalTypeByName(String name);
}
