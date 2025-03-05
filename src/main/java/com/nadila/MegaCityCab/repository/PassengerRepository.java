package com.nadila.MegaCityCab.repository;

import com.nadila.MegaCityCab.model.Passenger;
import com.nadila.MegaCityCab.requests.BookingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.function.Predicate;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger,Long> {

    Passenger findByCabUserId(Long id);

    boolean existsByCabUserId(long id);
}
