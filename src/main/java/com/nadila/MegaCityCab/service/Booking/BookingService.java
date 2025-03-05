package com.nadila.MegaCityCab.service.Booking;


import com.nadila.MegaCityCab.enums.BookingStatus;
import com.nadila.MegaCityCab.exception.ResourceNotFound;
import com.nadila.MegaCityCab.model.Booking;
import com.nadila.MegaCityCab.repository.BookingRepository;
import com.nadila.MegaCityCab.repository.DriverRepository;
import com.nadila.MegaCityCab.repository.PassengerRepository;
import com.nadila.MegaCityCab.repository.VehicaleTypeRepository;
import com.nadila.MegaCityCab.requests.BookingRequest;
import com.nadila.MegaCityCab.service.AuthService.GetAuthId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookingService implements IBookingService{

    private final BookingRepository bookingRepository;
    private final VehicaleTypeRepository vehicaleTypeRepository;
    private final GetAuthId getAuthId;
    private final PassengerRepository passengerRepository;
    private final DriverRepository driverRepository;


    @Override
    public Booking createBooking(BookingRequest booking) {
        long userId = getAuthId.getCurrentUserId();
        return Optional.of(booking)
                .filter(bookingRequest -> passengerRepository.existsByCabUserId(userId) && vehicaleTypeRepository.existsById(booking.getVehicleType().getId()))
                .map(bookingRequest -> {
                    Booking bookingObj = new Booking();
                    bookingObj.setDate(booking.getDate());
                    bookingObj.setPickupLocation(booking.getPickupLocation());
                    bookingObj.setDestinationLocation(booking.getDestinationLocation());
                    bookingObj.setTotalDistanceKM(booking.getTotalDistanceKM());
                    bookingObj.setBookingStatus(BookingStatus.ACTIVE);
                    bookingObj.setPricePerKM(booking.getPricePerKM());
                    bookingObj.setVehicleType(booking.getVehicleType());

                    return bookingRepository.save(bookingObj);
                }).orElseThrow(() -> new ResourceNotFound("Booking process failed: No valid passenger or vehicle type found "));

    }

    @Override
    public void deleteBooking(Booking booking) {

        bookingRepository.findById(booking.getId())
                .ifPresentOrElse(bookingRepository ::delete, () -> {throw new ResourceNotFound("Booking process failed: No valid passenger or vehicle type found");});
    }

    @Override
    public Booking pasangerUpdateBooking(Booking booking) {
        return bookingRepository.findById(booking.getId())
                .map(existingBooking -> {
                    existingBooking.setDate(booking.getDate());
                    existingBooking.setBookingStatus(booking.getBookingStatus());
                    existingBooking.setPickupLocation(booking.getPickupLocation());
                    existingBooking.setDestinationLocation(booking.getDestinationLocation());
                    existingBooking.setTotalDistanceKM(booking.getTotalDistanceKM());
                    existingBooking.setTotalPrice(booking.getTotalPrice());
                    existingBooking.setVehicleType(booking.getVehicleType());
                    existingBooking.setPricePerKM(booking.getPricePerKM());
                    existingBooking.setBookingStatus(booking.getBookingStatus());
                    return bookingRepository.save(existingBooking);
                }).orElseThrow(() ->  new ResourceNotFound("Booking process failed: No valid booking found"));
    }

    @Override
    public List<Booking> getBookingsByVehicleTypeAndStatus(Long vehicleTypeId) {

        return bookingRepository.findByVehicleTypeIdAndBookingStatusIn(
                vehicleTypeId,
                List.of(BookingStatus.ACTIVE, BookingStatus.CANCELLEDBYDRIVER)
        );
    }



}
