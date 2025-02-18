package com.nadila.MegaCityCab.repository;

import com.nadila.MegaCityCab.model.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicaleTypeRepository extends JpaRepository<VehicleType,Long> {
    boolean existsByName(String name);

    VehicleType findByName(String name);
}
