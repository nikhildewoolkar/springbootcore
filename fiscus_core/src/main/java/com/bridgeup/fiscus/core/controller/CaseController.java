package com.bridgeup.fiscus.core.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("fiscus/v1/case")
public interface CaseController {
    @PostMapping("/create-case")
    public ResponseEntity<List<String>> createCase(
            @RequestParam String companyName,
            @RequestParam String accountNumber,
            @RequestParam String relatedParty,
            @RequestParam("bankStatementFile") MultipartFile bankStatementFile,
            @RequestParam String metadata,
            @RequestParam String companyAddress
    ) throws IOException;
    @PostMapping("/run-new-task")
    public ResponseEntity<List<String>> createNewTask(
            @RequestParam String caseId
    ) throws IOException;
    @PostMapping("/truncate-case-table")
    public ResponseEntity<String> truncateCaseTable() throws IOException;

//    @PostMapping("/read-csv")
//    public ResponseEntity<String> readCsvByCaseId(
//            @RequestParam String caseDataId) throws IOException ;
}
