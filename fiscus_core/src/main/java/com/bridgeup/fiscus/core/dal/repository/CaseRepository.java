package com.bridgeup.fiscus.core.dal.repository;

import com.bridgeup.fiscus.core.dal.entity.CaseEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CaseRepository extends CrudRepository<CaseEntity, Long> {
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE Casedata", nativeQuery = true)
    void truncateCaseTable();

    CaseEntity findById(UUID caseDataId);
}
