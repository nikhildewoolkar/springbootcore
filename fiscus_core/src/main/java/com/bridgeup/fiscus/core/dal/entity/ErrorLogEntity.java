package com.bridgeup.fiscus.core.dal.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;


@Data
@Entity
@Table(name = "etl_error_log")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ErrorLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "case_id")
    private UUID caseId;

    @Column(name = "task_id")
    private UUID taskId;

    @Column(name = "task_batch_id")
    private UUID taskBatchId;

    @Column(name = "log_data", length = 255)
    private String logData;
    
//    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    //    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

}