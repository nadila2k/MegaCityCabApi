package com.nadila.MegaCityCab.dto;

import com.nadila.MegaCityCab.enums.BookingStatus;
import com.nadila.MegaCityCab.model.VehicleType;
import lombok.Data;

import java.util.Date;

@Data
public class BookingDto {

    private Long id;
    private Date date;
    private String pickupLocation;
    private String destinationLocation;
    private double totalDistanceKM;
    private double pricePerKM;
    private double totalPrice;
    private BookingStatus bookingStatus;
    private PassengerDto passenger;
    private DriversDto driver;
    private VehicleType vehicleType;
    private PaymentDto payment;
}
