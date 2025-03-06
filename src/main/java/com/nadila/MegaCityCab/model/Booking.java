package com.nadila.MegaCityCab.model;

import com.nadila.MegaCityCab.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    private String pickupLocation;
    private String destinationLocation;
    private double totalDistanceKM;
    private double pricePerKM;
    private double totalPrice;


    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    @ManyToOne
    @JoinColumn(name = "vehicle_type_id")
    private VehicleType vehicleType;

    @ManyToOne
    @JoinColumn(name = "driver_Id")
    private Drivers drivers;

    @ManyToOne
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private Payment payment;


    @PrePersist
    @PreUpdate
    public void setTotalPrice() {
        this.totalPrice = this.pricePerKM * this.totalDistanceKM;
    }
}
