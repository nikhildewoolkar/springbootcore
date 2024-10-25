package com.bridgeup.fiscus.core.dal.repository;
import com.bridgeup.fiscus.core.dal.entity.ErrorLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ErrorLogRepository extends JpaRepository<ErrorLogEntity, String> {

    Optional<ErrorLogEntity> findByTaskBatchId(UUID taskId);

}
