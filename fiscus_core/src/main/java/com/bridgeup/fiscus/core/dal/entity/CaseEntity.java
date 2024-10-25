package com.bridgeup.fiscus.core.dal.entity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "Casedata")
public class CaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "company_address", nullable = false)
    private String companyAddress;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Lob
    @Column(name = "bank_statement", nullable = false)
    private byte[] bankStatement;

    @Column(name = "metadata")
    private String metadata;

    @Column(name = "related_party")
    private String relatedParty;

//    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

//    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
}
