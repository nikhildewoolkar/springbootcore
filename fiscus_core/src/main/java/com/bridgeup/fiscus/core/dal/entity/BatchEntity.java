package com.bridgeup.fiscus.core.dal.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "etl_batch")
public class BatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "case_id")
    private UUID caseId;

    @Column(name = "task_id")
    private UUID taskId;

    @Column(name = "row_start_index")
    private Integer rowStartIndex;

    @Column(name = "row_end_index")
    private Integer rowEndIndex;

//    @ElementCollection
    @Column(name="data_fields")
    private byte[] data_fields;

    @ElementCollection
    @Column(name="batch_row_data")
    private List<String> batch_row_data;
}
