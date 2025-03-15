package com.nadila.MegaCityCab.controller;

import com.nadila.MegaCityCab.dto.PassengerDto;
import com.nadila.MegaCityCab.enums.ResponseStatus;
import com.nadila.MegaCityCab.exception.ResourceNotFound;
import com.nadila.MegaCityCab.requests.PassengerUpdateRequest;
import com.nadila.MegaCityCab.response.ApiResponse;
import com.nadila.MegaCityCab.service.Passenger.IPassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/passenger")
public class PassengerController {

    private final IPassengerService passengerService;


    @PutMapping("/update/passenger")
    public ResponseEntity<ApiResponse> updatePassenger(@RequestBody PassengerUpdateRequest passangerUpdateRequest) {

        try {
            PassengerDto passengerDto = passengerService.updatePassenger(passangerUpdateRequest);

            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(ResponseStatus.SUCCESS, "Passenger Updated", passengerDto));
        } catch (ResourceNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(ResponseStatus.FAILURE, "not found", e.getMessage()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(ResponseStatus.ERROR, "Server error", e.getMessage()));
        }
    }

    @GetMapping("/all/passenger")
    public ResponseEntity<ApiResponse> getAllPassenger() {

        try {
            List<PassengerDto> passengerDto = passengerService.getAllPassengers();

            return ResponseEntity.ok().body(new ApiResponse(ResponseStatus.SUCCESS, "All Passengers", passengerDto));
        } catch (ResourceNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(ResponseStatus.FAILURE, "not found", e.getMessage()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(ResponseStatus.ERROR, "Server error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{email}/{password}")
    public ResponseEntity<ApiResponse> deletePassenger(@PathVariable String email, @PathVariable String password) {
        try {
            passengerService.deletePassenger(email, password);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse(ResponseStatus.SUCCESS, "Passenger Deleted", null));
        } catch (ResourceNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(ResponseStatus.FAILURE, "Passenger not found", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(ResponseStatus.ERROR, "Deletion failed", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(ResponseStatus.ERROR, "Server error", e.getMessage()));
        }
    }


    @GetMapping("/me/passenger")
    public ResponseEntity<ApiResponse> getPassenger() {
        try {
            PassengerDto passengerDto = passengerService.getPassenger();  // Call the service method to get passenger
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse(ResponseStatus.SUCCESS, "Passenger found", passengerDto));
        } catch (ResourceNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(ResponseStatus.FAILURE, "Passenger not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(ResponseStatus.ERROR, "Internal server error", e.getMessage()));
        }
    }


}
