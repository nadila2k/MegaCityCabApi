package com.nadila.MegaCityCab.repository;

import com.nadila.MegaCityCab.model.Drivers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Drivers,Long> {
    boolean existsByVehicalNumber(String vehicalNumber);

    List<Drivers> findByFirstName(String firstName);
}
