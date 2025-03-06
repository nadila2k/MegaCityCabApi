package com.nadila.MegaCityCab.model;

import com.nadila.MegaCityCab.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentStatus payementStatus;

    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDateTime;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking  booking;

    @PreUpdate
    @PrePersist
    public void setPaymentDateTime() {
        this.paymentDateTime = new Date(); // Automatically set to the current date-time when inserted
    }
}
