package com.nadila.MegaCityCab.service.Booking;


import com.nadila.MegaCityCab.dto.BookingDto;
import com.nadila.MegaCityCab.dto.DriversDto;
import com.nadila.MegaCityCab.enums.BookingStatus;
import com.nadila.MegaCityCab.exception.ResourceNotFound;
import com.nadila.MegaCityCab.model.Booking;
import com.nadila.MegaCityCab.model.Drivers;
import com.nadila.MegaCityCab.model.Passenger;
import com.nadila.MegaCityCab.repository.*;
import com.nadila.MegaCityCab.requests.BookingRequest;
import com.nadila.MegaCityCab.service.AuthService.GetAuthId;
import com.nadila.MegaCityCab.service.PaymentService.IPaymentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookingService implements IBookingService {

    private final BookingRepository bookingRepository;
    private final VehicaleTypeRepository vehicaleTypeRepository;
    private final GetAuthId getAuthId;
    private final PassengerRepository passengerRepository;
    private final DriverRepository driverRepository;
    private final ModelMapper modelMapper;
    private final IPaymentService paymentService;
    private final PaymentRepository paymentRepository;


    @Override
    public BookingDto createBooking(BookingRequest booking) {
        long userId = getAuthId.getCurrentUserId();


        return Optional.of(booking).filter(bookingRequest -> passengerRepository.existsByCabUserId(userId) && vehicaleTypeRepository.existsById(bookingRequest.getVehicleTypeId())).map(bookingRequest -> {
            Passenger passenger = passengerRepository.findByCabUserId(userId);

            Booking bookingObj = new Booking();
            bookingObj.setDate(booking.getDate());
            bookingObj.setPickupLocation(booking.getPickupLocation());
            bookingObj.setDestinationLocation(booking.getDestinationLocation());
            bookingObj.setTotalDistanceKM(booking.getTotalDistanceKM());
            bookingObj.setBookingStatus(BookingStatus.ACTIVE);
            bookingObj.setPricePerKM(booking.getPricePerKM());
            bookingObj.setVehicleType(vehicaleTypeRepository.findById(booking.getVehicleTypeId())
                    .orElseThrow(() ->  new ResourceNotFound("VehicleType not found")));
            bookingObj.setDrivers(null);
            bookingObj.setPassenger(passenger);

            return getBookingDto(bookingRepository.save(bookingObj));
        }).orElseThrow(() -> new ResourceNotFound("Booking process failed: No valid passenger or vehicle type found "));

    }

    @Override
    public BookingDto pasangerUpdateBooking(long id, Booking booking) {

        return bookingRepository.findById(id).map(existingBooking -> {
            existingBooking.setDate(booking.getDate());
            existingBooking.setBookingStatus(booking.getBookingStatus());
            existingBooking.setPickupLocation(booking.getPickupLocation());
            existingBooking.setDestinationLocation(booking.getDestinationLocation());
            existingBooking.setTotalDistanceKM(booking.getTotalDistanceKM());
            existingBooking.setTotalPrice(booking.getTotalPrice());
            existingBooking.setVehicleType(booking.getVehicleType());
            existingBooking.setPricePerKM(booking.getPricePerKM());
            existingBooking.setBookingStatus(booking.getBookingStatus());
            return getBookingDto(bookingRepository.save(existingBooking));
        }).orElseThrow(() -> new ResourceNotFound("Booking process failed: No valid booking found"));
    }

    @Override
    public List<BookingDto> getBookingsByVehicleTypeAndStatus() {


        return Optional.ofNullable(driverRepository.findByCabUserId(getAuthId.getCurrentUserId()))
                .map(driver -> {
                    List<Booking> bookings = bookingRepository.findByVehicleTypeIdAndBookingStatusIn(
                            driver.getVehicleType().getId(),
                            List.of(BookingStatus.ACTIVE, BookingStatus.CANCELLEDBYDRIVER)
                    );
                    return bookings.stream().map(this::getBookingDto).toList();
                })
                .orElseThrow(() -> new ResourceNotFound("Driver not found"));
    }


    @Override
    public BookingDto driverUpdateBooking(long id, BookingStatus bookingStatus) {


        Drivers driver = driverRepository.findByCabUserId(getAuthId.getCurrentUserId());


        if (driver == null) {
            throw new ResourceNotFound("Driver not found");
        }

        return bookingRepository.findById(id).map(booking -> {
            if (BookingStatus.DRIVERCONFIRMED.equals(bookingStatus)) {
                booking.setBookingStatus(bookingStatus);
                booking.setDrivers(driver);
                paymentService.createPayment(booking);

            } else if (BookingStatus.CANCELLEDBYDRIVER.equals(bookingStatus) && booking.getDrivers().getId().equals(driver.getId())) {
                booking.setBookingStatus(BookingStatus.ACTIVE);
                booking.setDrivers(null);
                paymentRepository.deleteById(booking.getPayment().getId());
            } else if (BookingStatus.ONGOING.equals(bookingStatus) && booking.getDrivers().getId().equals(driver.getId())) {
                booking.setBookingStatus(bookingStatus);


            } else if (BookingStatus.COMPLETED.equals(bookingStatus) && booking.getDrivers().getId().equals(driver.getId())) {
                booking.setBookingStatus(bookingStatus);
                paymentService.updatePayment(booking);
            }
            return getBookingDto(bookingRepository.save(booking));
        }).orElseThrow(() -> new ResourceNotFound("Booking not found"));
    }


    @Override
    public void deleteBooking(long id) {
        bookingRepository.findById(id)
                .ifPresentOrElse(
                        booking -> {
                            if (booking.getPayment() != null) {
                                paymentRepository.deleteById(booking.getPayment().getId());
                            }
                            bookingRepository.delete(booking);
                        },
                        () -> {
                            throw new ResourceNotFound("Booking delete failed: No valid booking found");
                        }
                );
    }




    public List<BookingDto> getAllBooking() {
        return bookingRepository.findAll().stream()
                .map(this::getBookingDto)  // Convert each Booking to BookingDto
                .collect(Collectors.toList()); // Use Collectors.toList() instead of toList()
    }

    @Override
    public List<BookingDto> getBookingsByDriver() {

        return  Optional.ofNullable(driverRepository.findByCabUserId(getAuthId.getCurrentUserId()))
                .map(drivers -> {
                    return bookingRepository.findByDriversId(drivers.getId()).stream()
                            .map(this::getBookingDto)  // Convert each Booking to BookingDto
                            .collect(Collectors.toList()); // Use Collectors.toList() instead of toList()
                }).orElseThrow(() ->  new ResourceNotFound("Driver not found"));
    }

    @Override
    public List<BookingDto> getBookingByStatusOngoingDriver() {
        return Optional.ofNullable(driverRepository.findByCabUserId(getAuthId.getCurrentUserId()))
                .map(driver -> {
                    // Fetch bookings with ONGOING status
                    List<Booking> bookings = bookingRepository.findByBookingStatus(BookingStatus.ONGOING);

                    // Convert to BookingDto and return the list
                    return bookings.stream().map(this::getBookingDto).toList();
                })
                .orElseThrow(() -> new ResourceNotFound("Driver not found"));
    }

    @Override
    public List<BookingDto> getBookingsByStatusCompletedDriver() {
        return Optional.ofNullable(driverRepository.findByCabUserId(getAuthId.getCurrentUserId()))
                .map(driver -> {
                    // Fetch bookings with COMPLETED status
                    List<Booking> bookings = bookingRepository.findByBookingStatus(BookingStatus.COMPLETED);

                    // Convert to BookingDto and return the list
                    return bookings.stream().map(this::getBookingDto).toList();
                })
                .orElseThrow(() -> new ResourceNotFound("Driver not found"));
    }

    public List<BookingDto> getPassengerBookings() {
        return Optional.ofNullable(passengerRepository.findByCabUserId(getAuthId.getCurrentUserId()))
                .map(passenger -> {
                    // Find bookings for the passenger
                    return bookingRepository.findByPassengerId(passenger.getId()).stream()
                            .map(this::getBookingDto)  // Convert each Booking to BookingDto
                            .collect(Collectors.toList()); // Use Collectors.toList() instead of toList()
                })
                .orElseThrow(() -> new ResourceNotFound("Passenger not found"));
    }



    private BookingDto getBookingDto(Booking booking) {

        BookingDto bookingDto = modelMapper.map(booking, BookingDto.class);

        // Manually map the 'driver' field if it's not automatically mapped
        if (booking.getDrivers() != null) {

            DriversDto driverDto = modelMapper.map(booking.getDrivers(), DriversDto.class);

        } else {

            bookingDto.setDriver(null);
        }

        return bookingDto;
    }




}




