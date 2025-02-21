package com.nadila.MegaCityCab.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Drivers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String mobileNumber;
    private String vehicaleName;
    @Column(unique = true)
    private String vehicalNumber;
    private String imageUrl;
    private String imageId;

    @ManyToOne()
    @JoinColumn(name = "vehicleType_Id")
    private VehicleType vehicleType;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cabUser_Id")
    private CabUser cabUser;

    @OneToMany(mappedBy = "drivers", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;
}
