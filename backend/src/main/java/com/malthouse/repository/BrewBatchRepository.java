package com.malthouse.repository;

import com.malthouse.entity.BrewBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BrewBatchRepository extends JpaRepository<BrewBatch, Long> {
    List<BrewBatch> findByFermenterId(Long fermenterId);

    long countByStatus(String status);

    @Query("select b.status, count(b) from BrewBatch b group by b.status")
    List<Object[]> countGroupByStatus();
}
