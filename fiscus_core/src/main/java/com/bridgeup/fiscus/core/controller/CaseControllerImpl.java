package com.bridgeup.fiscus.core.controller;

import com.bridgeup.fiscus.core.service.CaseService;
import com.bridgeup.fiscus.core.vo.CaseVo;
import com.bridgeup.fiscus.core.dal.entity.CaseEntity;
import com.bridgeup.fiscus.core.service.ErrorLogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CaseControllerImpl implements CaseController {

    @Autowired
    private CaseService caseService;

    @Autowired
    private ErrorLogService errorLogService;



    public ResponseEntity<List<String>> createCase(
            @RequestParam String companyName,
            @RequestParam String accountNumber,
            @RequestParam String relatedParty,
            @RequestParam("bankStatementFile") MultipartFile bankStatementFile,
            @RequestParam String metadata,
            @RequestParam String companyAddress
    ) throws IOException{
        try {
            if (bankStatementFile.isEmpty()) {
                List<String> errorList = new ArrayList<>();
                errorList.add("File is empty");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorList);
            }
            String filename = bankStatementFile.getOriginalFilename();
            CaseVo casevo= new CaseVo(companyName,accountNumber, relatedParty,bankStatementFile.getBytes(),metadata,companyAddress);
            if (filename != null && filename.toLowerCase().endsWith(".csv")) {
                // File is a CSV
                String case_id=caseService.createCase(casevo);
                List<String> a=caseService.newtask(case_id);

                // this validation should be moved to ETL
                byte[] bankStatement=bankStatementFile.getBytes();
                CaseEntity b=caseService.getCaseEntityById(a.get(0));
                String output=caseService.readCsvToValidate(b,a.get(1),bankStatement);
                a.add(output);
                return ResponseEntity.ok(a);
            } else {
                // File is not a CSV
                String case_id=caseService.createCase(casevo);
                String error=errorLogService.createInvalidCaseLog(case_id,"Invalid CSV");
                List<String> errorList = new ArrayList<>();
                errorList.add("File isn't CSV");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorList);
            }

        } catch (IOException e) {
            e.printStackTrace();
            List<String> errorList = new ArrayList<>();
            errorList.add("Failed to create case.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorList);
        }
    };

    public ResponseEntity<List<String>> createNewTask(
            @RequestParam String caseId
    ) throws IOException {
        try {
            return ResponseEntity.ok(caseService.newtask(caseId));
        }catch (Exception e) {
            e.printStackTrace();
            List<String> errorList = new ArrayList<>();
            errorList.add("Failed to create task.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorList);
        }
    };

    public ResponseEntity<String> truncateCaseTable() throws IOException
    {
        try {
            return ResponseEntity.ok(caseService.truncateCaseTable());
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to truncate case table.");
        }
    }

//    public ResponseEntity<String> readCsvByCaseId(String caseDataId) throws IOException {
//        try {
//            CaseEntity currentCase=caseService.getCaseEntityById(caseDataId);
//            byte[] bankStatement = currentCase.getBankStatement();
//            return ResponseEntity.ok(caseService.readCsvToValidate(currentCase,null,bankStatement));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed get caseid.");
//        }
//    }
}
