package com.nadila.MegaCityCab.service.VehicaleType;

import com.nadila.MegaCityCab.model.VehicleType;

import java.util.List;

public interface IVehicalTypeService {

    VehicleType createVehicalType(VehicleType vehicleType);
    VehicleType updateVehicalType(long id, VehicleType vehicleType);
    void deleteVehicalType(long id);
    List<VehicleType>  getAllVehicalType();
    VehicleType getVehicalTypeByName(String name);
}
