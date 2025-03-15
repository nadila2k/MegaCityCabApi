package com.nadila.MegaCityCab.controller;


import com.nadila.MegaCityCab.dto.DriversDto;
import com.nadila.MegaCityCab.enums.ResponseStatus;
import com.nadila.MegaCityCab.exception.ResourceNotFound;
import com.nadila.MegaCityCab.requests.DriverUpdateRequest;
import com.nadila.MegaCityCab.response.ApiResponse;
import com.nadila.MegaCityCab.service.Drivers.IDriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/admin")

public class AdminController {

    private final IDriverService driverService;
    @PutMapping("/update/{driverId}/driver")
    public ResponseEntity<ApiResponse> updateDriver(
            @PathVariable Long driverId,
            @ModelAttribute DriverUpdateRequest driverUpdateRequest) {

        try {
            DriversDto driversDto = driverService.updateDriverAdmin(driverId, driverUpdateRequest);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse(ResponseStatus.SUCCESS, "Successfully updated driver", driversDto));
        } catch (ResourceNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(ResponseStatus.FAILURE, "Driver not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(ResponseStatus.ERROR, "Internal server error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{driverId}/driver")
    public ResponseEntity<ApiResponse> deleteDriver(@PathVariable Long driverId) {
        try {
            driverService.deleteDriverAdmin(driverId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiResponse(ResponseStatus.SUCCESS, "Successfully deleted driver", "driver has been deleted"));
        } catch (ResourceNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(ResponseStatus.FAILURE, "Driver not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(ResponseStatus.ERROR, "Internal server error", e.getMessage()));
        }
    }
}
