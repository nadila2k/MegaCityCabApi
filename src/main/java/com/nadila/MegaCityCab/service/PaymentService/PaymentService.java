package com.nadila.MegaCityCab.service.PaymentService;

import com.nadila.MegaCityCab.dto.PaymentDto;
import com.nadila.MegaCityCab.enums.PaymentStatus;
import com.nadila.MegaCityCab.model.Booking;
import com.nadila.MegaCityCab.model.Payment;
import com.nadila.MegaCityCab.repository.BookingRepository;
import com.nadila.MegaCityCab.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService{

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final ModelMapper modelMapper;

    @Override
    public PaymentDto createPayment(Booking booking) {
        return bookingRepository.findById(booking.getId())
                .map(existingBooking ->{
                    Payment payment = new Payment();
                    payment.setBooking(booking);
                    payment.setPayementStatus(PaymentStatus.PENDING);
                    return convertToPaymentDto(paymentRepository.save(payment));
                } ).orElseThrow(() ->  new RuntimeException("Booking not found"));
    }

    @Override
    public PaymentDto updatePayment(Booking booking) {
        return bookingRepository.findById(booking.getId())
                .map(existingBooking ->{
                    Payment payment = new Payment();
                    payment.setBooking(booking);
                    payment.setPayementStatus(PaymentStatus.COMPLETED);
                    return convertToPaymentDto(paymentRepository.save(payment));
                } ).orElseThrow(() ->  new RuntimeException("Booking not found"));
    }


    public PaymentDto convertToPaymentDto(Payment payment) {
        return modelMapper.map(payment, PaymentDto.class);
    }
}
