package com.bridgeup.fiscus.core.dal.repository;

import com.bridgeup.fiscus.core.dal.entity.TaskEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<TaskEntity, Long> {
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE Task", nativeQuery = true)
    void truncateTaskTable();
}
