package com.nadila.MegaCityCab.model;

import com.nadila.MegaCityCab.enums.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CabUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Roles roles;

    @OneToOne(mappedBy = "cabUser", cascade = CascadeType.ALL)
    private Admin admin;

    @OneToOne(mappedBy = "cabUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Drivers driver;

    @OneToOne(mappedBy = "cabUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Passenger passenger;


}
