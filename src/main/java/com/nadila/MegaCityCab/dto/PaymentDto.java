package com.nadila.MegaCityCab.dto;

import com.nadila.MegaCityCab.enums.PaymentStatus;
import lombok.Data;

import java.util.Date;

@Data
public class PaymentDto {
    private Long id;
    private double amount;
    private PaymentStatus paymentStatus;
    private Date paymentDateTime;
}
