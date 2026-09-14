package com.malthouse.repository;

import com.malthouse.entity.GravityReading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface GravityReadingRepository extends JpaRepository<GravityReading, Long> {
    List<GravityReading> findByBatchIdOrderByMeasuredAtDesc(Long batchId);

    long countByMeasuredAtAfter(LocalDateTime after);
}
