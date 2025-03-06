package com.nadila.MegaCityCab.controller;

import com.nadila.MegaCityCab.enums.ResponseStatus;
import com.nadila.MegaCityCab.exception.AlreadyExistsException;
import com.nadila.MegaCityCab.exception.ResourceNotFound;
import com.nadila.MegaCityCab.model.VehicleType;
import com.nadila.MegaCityCab.requests.VehicaleTypeRequest;
import com.nadila.MegaCityCab.response.ApiResponse;
import com.nadila.MegaCityCab.service.VehicaleType.IVehicalTypeService;
import com.nadila.MegaCityCab.service.VehicaleType.VehicalTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/vehicle")
public class VehicleTypeController {

    public final IVehicalTypeService vehicaleTypeService;

    @GetMapping
    public ResponseEntity<ApiResponse> getVehicleType() {
        try{
            List<VehicleType> vehicleTypes = vehicaleTypeService.getAllVehicalType();

            return ResponseEntity.ok(new ApiResponse(ResponseStatus.SUCCESS,"success",vehicleTypes ));
        }catch(ResourceNotFound e){
            return ResponseEntity.status(404).body(new ApiResponse(ResponseStatus.ERROR,"not found",e.getMessage()));
        }
    }


    @PostMapping
    public  ResponseEntity<ApiResponse> addVehicaleType(@RequestPart VehicaleTypeRequest  vehicaleTypeRequest, @RequestPart MultipartFile image) {
        try{
            VehicleType vehicleType = vehicaleTypeService.createVehicalType(vehicaleTypeRequest, image);

            return ResponseEntity.ok(new ApiResponse(ResponseStatus.SUCCESS,"Vehicale Type Add Success",vehicleType));
        }catch(AlreadyExistsException e){
            return ResponseEntity.status(404).body(new ApiResponse(ResponseStatus.ERROR,"already exists",e.getMessage()));
        }
    }
}
