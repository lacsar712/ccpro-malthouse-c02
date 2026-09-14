package com.malthouse.repository;

import com.malthouse.entity.Fermenter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FermenterRepository extends JpaRepository<Fermenter, Long> {
    List<Fermenter> findBySiteId(Long siteId);

    Optional<Fermenter> findBySiteIdAndTankCode(Long siteId, String tankCode);

    boolean existsBySiteIdAndTankCode(Long siteId, String tankCode);

    boolean existsBySiteIdAndTankCodeAndIdNot(Long siteId, String tankCode, Long id);
}
