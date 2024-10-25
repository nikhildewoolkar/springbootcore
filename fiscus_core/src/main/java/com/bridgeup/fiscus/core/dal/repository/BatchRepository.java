package com.bridgeup.fiscus.core.dal.repository;

import com.bridgeup.fiscus.core.dal.entity.BatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BatchRepository extends JpaRepository<BatchEntity, UUID> {
    List<BatchEntity> findByTaskId(UUID taskId);
}
