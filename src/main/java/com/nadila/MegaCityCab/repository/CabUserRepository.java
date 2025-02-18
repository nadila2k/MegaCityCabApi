package com.nadila.MegaCityCab.repository;

import com.nadila.MegaCityCab.model.CabUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CabUserRepository extends JpaRepository<CabUser,Long> {
    boolean existsByEmail(String email);

    CabUser findByEmail(String email);
}
