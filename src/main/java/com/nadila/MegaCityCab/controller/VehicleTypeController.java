package com.nadila.MegaCityCab.controller;

import com.nadila.MegaCityCab.enums.ResponseStatus;
import com.nadila.MegaCityCab.exception.AlreadyExistsException;
import com.nadila.MegaCityCab.exception.ResourceNotFound;
import com.nadila.MegaCityCab.model.VehicleType;

import com.nadila.MegaCityCab.requests.VehicaleUpdateRequest;
import com.nadila.MegaCityCab.requests.VehicleTypeRequest;
import com.nadila.MegaCityCab.response.ApiResponse;
import com.nadila.MegaCityCab.service.VehicaleType.IVehicalTypeService;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/vehicle")

public class VehicleTypeController {

    public final IVehicalTypeService vehicaleTypeService;


    @PostMapping("/create")
    public ResponseEntity<ApiResponse> addVehicleType(@ModelAttribute VehicleTypeRequest vehicleTypeRequest) {
        try {
            VehicleType vehicleType = vehicaleTypeService.createVehicleType(vehicleTypeRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(ResponseStatus.SUCCESS, "Vehicle Type Added Successfully", vehicleType));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(ResponseStatus.FAILURE, "Vehicle type already exists", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(ResponseStatus.ERROR, "Image upload failed", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(ResponseStatus.ERROR, "Server error", e.getMessage()));
        }
    }
//    public  ResponseEntity<ApiResponse> addVehicaleType(@RequestPart VehicaleTypeRequest  vehicaleTypeRequest, @RequestPart MultipartFile image) {
//        try{
//            VehicleType vehicleType = vehicaleTypeService.createVehicalType(vehicaleTypeRequest, image);
//
//            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(ResponseStatus.SUCCESS,"Vehicale Type Add Success",vehicleType));
//        }catch(AlreadyExistsException e){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(ResponseStatus.FAILURE,"already exists",e.getMessage()));
//        }catch (IOException e){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(ResponseStatus.ERROR,"Image upload fail ",e.getMessage()));
//        }
//        catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(ResponseStatus.ERROR,e.getMessage(),"Server error"));
//        }
//    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateVehicaleType(@PathVariable long id, @ModelAttribute VehicaleUpdateRequest vehicleType ) {

        try {
            VehicleType updateVehicalType = vehicaleTypeService.updateVehicalType(id, vehicleType);

            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(ResponseStatus.SUCCESS,"Vehicale Type Update Success",updateVehicalType));
        } catch (ResourceNotFound e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(ResponseStatus.FAILURE,"Vehicle type not found.",e.getMessage()));
        }catch (IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(ResponseStatus.ERROR,e.getMessage(),"Image upload failed"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(ResponseStatus.ERROR,"Server error",e.getMessage()));
        }
    }


    @GetMapping("/all/vehicles")
    public ResponseEntity<ApiResponse> getAllVehicleType() {
        try{
            List<VehicleType> vehicleTypes = vehicaleTypeService.getAllVehicalType();

            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(ResponseStatus.SUCCESS,"success",vehicleTypes ));
        }catch(ResourceNotFound e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(ResponseStatus.FAILURE,"not found",e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(ResponseStatus.ERROR,"server error",e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public  ResponseEntity<ApiResponse> deleteVehicaleType(@PathVariable long id) {

        try {
            vehicaleTypeService.deleteVehicalType(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse(ResponseStatus.SUCCESS,"success",null));
        }catch(ResourceNotFound e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(ResponseStatus.FAILURE,"not found",e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(ResponseStatus.ERROR,"server error",e.getMessage()));
        }
    }

    @GetMapping("/get/{name}/vehicle")
    public ResponseEntity<ApiResponse> getVehicalTypeByName(@PathVariable String name) {

        try {
            VehicleType vehicleType = vehicaleTypeService.getVehicalTypeByName(name);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(ResponseStatus.SUCCESS,"success",vehicleType));
        } catch (ResourceNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(ResponseStatus.FAILURE,"not found",e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(ResponseStatus.ERROR,"server error",e.getMessage()));
        }
    }


}
