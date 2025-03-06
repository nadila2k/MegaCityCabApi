package com.nadila.MegaCityCab.service.PaymentService;

import com.nadila.MegaCityCab.dto.PaymentDto;
import com.nadila.MegaCityCab.model.Booking;

public interface IPaymentService {
    PaymentDto createPayment(Booking booking);
    PaymentDto updatePayment(Booking booking);
}
