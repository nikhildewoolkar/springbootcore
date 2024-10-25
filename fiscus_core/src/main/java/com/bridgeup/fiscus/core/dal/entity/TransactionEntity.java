package com.bridgeup.fiscus.core.dal.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "transactions")
public class TransactionEntity {

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

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(name = "date", nullable = false)
    private String date;

    @Column(name = "cheque_number")
    private String chequeNumber;

    @Column(name = "ref_number")
    private String refNumber;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "balance", nullable = false)
    private double balance;

    @Column(name="type", nullable = false)
    private String type;


    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "perfios_category")
    private String perfios_category;

    @Column(name = "fiscus_category")
    private String fiscus_category;

    @Column(name = "final_category")
    private String final_category;

    //    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    //    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

}