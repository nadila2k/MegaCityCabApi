package com.nadila.MegaCityCab.controller;

import com.nadila.MegaCityCab.dto.DriversDto;
import com.nadila.MegaCityCab.enums.ResponseStatus;
import com.nadila.MegaCityCab.exception.ResourceNotFound;
import com.nadila.MegaCityCab.requests.DriverUpdateRequest;
import com.nadila.MegaCityCab.response.ApiResponse;
import com.nadila.MegaCityCab.service.Drivers.IDriverService;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/driver")
@RequiredArgsConstructor
public class DriverController {

    private final IDriverService driverService;

    @PutMapping("/update/driver")
    public ResponseEntity<ApiResponse>  updateDrive( @ModelAttribute DriverUpdateRequest driverUpdateRequest){

        try {
            DriversDto driversDto = driverService.updateDriver(driverUpdateRequest);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(ResponseStatus.SUCCESS,"Successfully updated driver", driversDto));
        } catch (ResourceNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(ResponseStatus.FAILURE,"Driver not found",e.getMessage()));
        }catch (IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(ResponseStatus.ERROR,"Image upload error",e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(ResponseStatus.ERROR,"Internal server error",e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{email}/{password}")
    public ResponseEntity<ApiResponse> deleteDriver(@PathVariable String email, @PathVariable String password){
        try {
            driverService.deleteDriver(email,password);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse(ResponseStatus.SUCCESS,"Successfully deleted driver",true));
        }catch (ResourceNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(ResponseStatus.FAILURE,"Driver not found",e.getMessage()));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(ResponseStatus.ERROR,"",e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(ResponseStatus.ERROR,"Internal server error",e.getMessage()));
        }

    }

    @GetMapping("/all/drivers")
    public ResponseEntity<ApiResponse> getAllDrivers(){
        try {
            List<DriversDto> driversDto = driverService.getAllDrivers();
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(ResponseStatus.SUCCESS,"Successfully fetched drivers", driversDto));
        }catch (ResourceNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(ResponseStatus.FAILURE,"Driver not found",e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(ResponseStatus.ERROR,"Internal server error",e.getMessage()));
        }
    }

    @GetMapping("driver/{firstName}")
    public ResponseEntity<ApiResponse> getByFirstName(@PathVariable String firstName){

        try {
            List<DriversDto> driversDto = driverService.getByFirstName(firstName);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(ResponseStatus.SUCCESS,"Successfully fetched driver", driversDto));
        } catch (ResourceNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(ResponseStatus.FAILURE,"Driver not found",e.getMessage()));
        }   catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(ResponseStatus.ERROR,"Internal server error",e.getMessage()));
        }


    }


    @GetMapping("/me/driver")  // Endpoint to get the current driver's details
    public ResponseEntity<ApiResponse> getDriver() {
        try {
            DriversDto driverDto = driverService.getDriver();  // Call the service method to get the driver
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse(ResponseStatus.SUCCESS, "Driver found", driverDto));
        } catch (ResourceNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(ResponseStatus.FAILURE, "Driver not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(ResponseStatus.ERROR, "Internal server error", e.getMessage()));
        }
    }




}
