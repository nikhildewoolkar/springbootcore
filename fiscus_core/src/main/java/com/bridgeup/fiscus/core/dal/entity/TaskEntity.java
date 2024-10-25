package com.bridgeup.fiscus.core.dal.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import java.util.Date;
import java.util.UUID;
@Data
@Entity
@Table(name = "Task")
public class TaskEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(name = "case_id", nullable = false)
    private String caseId;

    @Column(name = "status")
    private String status;

    @Column(name = "error")
    private String error;

    //    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    //    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
}
