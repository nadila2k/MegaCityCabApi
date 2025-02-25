package com.nadila.MegaCityCab.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.nadila.MegaCityCab.model.Drivers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Drivers,Long> {
    boolean existsByVehicalNumber(String vehicalNumber);

    List<Drivers> findByFirstName(String firstName);




    Drivers findByCabUserId(Long id);
}
