package com.nadila.MegaCityCab.repository;

import com.nadila.MegaCityCab.dto.BookingDto;
import com.nadila.MegaCityCab.enums.BookingStatus;
import com.nadila.MegaCityCab.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {

    List<Booking> findByVehicleTypeIdAndBookingStatusIn(Long vehicleTypeId, List<BookingStatus> statuses);

    List<Booking> findByDriversId(Long id);

    List<Booking> findByBookingStatus(BookingStatus bookingStatus);
}
