package com.nadila.MegaCityCab.repository;

import com.nadila.MegaCityCab.model.CabUser;
import com.nadila.MegaCityCab.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger,Long> {

    Passenger findByCabUserId(Long id);
}
