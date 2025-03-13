package com.nadila.MegaCityCab.requests;

import com.nadila.MegaCityCab.enums.BookingStatus;
import com.nadila.MegaCityCab.model.VehicleType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.util.Date;

@Data
public class BookingRequest {

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    private String pickupLocation;
    private String destinationLocation;
    private double totalDistanceKM;
    private double pricePerKM;
    private long VehicleTypeId;
}
