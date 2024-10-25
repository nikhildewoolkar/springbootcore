package com.bridgeup.fiscus.core.dal.repository;

import com.bridgeup.fiscus.core.dal.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    Optional<TransactionEntity> findByTaskId(UUID taskId);

}