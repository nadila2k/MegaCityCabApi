package com.nadila.MegaCityCab.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double price;
    private String imageUrl;
    private String imageId;

    @JsonIgnore
    @OneToMany(mappedBy = "vehicleType", cascade = CascadeType.ALL)
    private List<Drivers> drivers;

    @JsonIgnore
    @OneToMany(mappedBy = "vehicleType", cascade = CascadeType.ALL)
    private List<Booking> bookings;
}
