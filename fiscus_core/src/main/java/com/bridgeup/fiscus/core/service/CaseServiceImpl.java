package com.bridgeup.fiscus.core.service;


import com.bridgeup.fiscus.core.config.RedisService;
import com.bridgeup.fiscus.core.dal.repository.*;
import com.bridgeup.fiscus.core.dal.entity.CaseEntity;
import com.bridgeup.fiscus.core.dal.entity.ErrorLogEntity;
import com.bridgeup.fiscus.core.dal.entity.TaskEntity;
import com.bridgeup.fiscus.core.dal.entity.TransactionEntity;

import java.nio.charset.StandardCharsets;

import com.bridgeup.fiscus.core.vo.CaseVo;
import jakarta.transaction.Transactional;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CaseServiceImpl implements CaseService{

    @Autowired
    ErrorLogService errorLogService;
    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ErrorLogRepository errorLogRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BatchRepository batchRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Override
    public String createCase(CaseVo caseVo) {
        CaseEntity caseEntity=new CaseEntity();
        caseEntity.setCompanyName(caseVo.getCompanyName());
        caseEntity.setAccountNumber(caseVo.getAccountNumber());
        caseEntity.setRelatedParty(caseVo.getRelatedParty());
        caseEntity.setBankStatement(caseVo.getBankStatementFile());
        caseEntity.setMetadata(caseVo.getMetadata());
        caseEntity.setCompanyAddress(caseVo.getCompanyAddress());
        caseRepository.save(caseEntity);
        return caseEntity.getId().toString();
    }

    public List<String> newtask(String caseId)
    {
        List<String>a =new ArrayList<>();
        TaskEntity taskEntity=new TaskEntity();
        taskEntity.setCaseId(caseId);
        taskEntity.setStatus("CREATED");
        taskEntity.setError("task created successfully");
        taskRepository.save(taskEntity);
        a.add(taskEntity.getCaseId());
        a.add(taskEntity.getId().toString());
        //implement rabbitmq logic
//        rabbitTemplate.convertAndSend("task_queue", a);
        return a;
    }
    @Transactional
    @Override
    public String readCsvToValidate(CaseEntity caseEntity,String task_id,byte[] bankStatement) {

        if (bankStatement != null && bankStatement.length > 0) {
            try (ByteArrayInputStream bis = new ByteArrayInputStream(bankStatement);
                 InputStreamReader isr = new InputStreamReader(bis);
                 CSVReader br = new CSVReader(isr)) {
                String filePath = "src/main/resources/Data/companies.json";
                String jsonString = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
//            String jsonString = new String(Files.readAllBytes(Paths.get(filePath)));
            JsonObject jsonObject = new Gson().fromJson(jsonString, JsonObject.class);
//                JsonArray csvFieldsArray = new Gson().fromJson(jsonString, JsonArray.class);
            JsonArray csvFieldsArray = jsonObject.getAsJsonArray("csvfields");
                String[] line=br.readNext();
                List<String> csvHeaders = new ArrayList<>(Arrays.asList(line));
                int index=csvFieldsArray.size();
                for (int i = 0; i < index; i++) {
                    if(!csvHeaders.contains(csvFieldsArray.get(i).getAsString()))
                    {
                        System.out.println(caseEntity.getId()+" "+task_id+" "+"csv_fields_error");
                        errorLogService.createTaskErrorLog(caseEntity.getId().toString(),task_id,"csv_fields_error");
                        return "csv_fields_error";
                    }
                }
//                List<byte[]> chunks= divideIntoChunks(removeFirstLine(bankStatement),20);
//                for(int i=0;i<chunks.size();i++)
//                {
//                    BatchEntity batchEntity = new BatchEntity();
//                    batchEntity.setRowStartIndex(1);
//                    batchEntity.setRowEndIndex(1);
//                    batchEntity.setTaskId(UUID.fromString(task_id));
//                    batchEntity.setCaseId(caseEntity.getId());
//                    batchEntity.setData_fields(chunks.get(i));
//                    batchEntity.setBatch_row_data(csvHeaders);
//                    BatchEntity n=batchRepository.save(batchEntity);
//                    System.out.println(n.getBatch_row_data());
//                    List<Object> batch_mq=new ArrayList<>();
//                    rabbitTemplate.convertAndSend("batch_etl_queue", batch_mq);
//                }

//                String batch_id="";
                System.out.println(managetransactions(caseEntity,task_id,bankStatement));
                return "valid_csv_fields";
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CsvValidationException e) {
                throw new RuntimeException(e);
            }
        } else {
            errorLogService.createTaskErrorLog(caseEntity.getId().toString(),task_id,"no_data_found");
            return "no_data_found";
        }
        errorLogService.createTaskErrorLog(caseEntity.getId().toString(),task_id,"corner_test_case");
        return "corner_test_case";
    }

    public byte[] removeFirstLine(byte[] data) {
        byte[] newLineBytes = System.lineSeparator().getBytes();
        int newLineIndex = indexOf(data, newLineBytes);
        if (newLineIndex == -1) {
            // No newline found, return original data
            return data;
        }
        int startIndex = newLineIndex + newLineBytes.length;
        return Arrays.copyOfRange(data, startIndex, data.length);
    }

    private int indexOf(byte[] data, byte[] pattern) {
        for (int i = 0; i <= data.length - pattern.length; i++) {
            boolean found = true;
            for (int j = 0; j < pattern.length; j++) {
                if (data[i + j] != pattern[j]) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return i;
            }
        }
        return -1;
    }

    public List<byte[]> divideIntoChunks(byte[] data, int chunkSize) {
        List<byte[]> chunks = new ArrayList<>();
        int offset = 0;
        while (offset < data.length) {
            int length = Math.min(chunkSize, data.length - offset);
            byte[] chunk = Arrays.copyOfRange(data, offset, offset + length);
            chunks.add(chunk);
            offset += length;
        }
        return chunks;
    }
    public static String extractReferenceNumber(String description) {
        String referenceNumber = " ";
        Pattern pattern = Pattern.compile("\\(Ref# (\\w+)\\)");
        Matcher matcher = pattern.matcher(description);
        if (matcher.find()) {
            referenceNumber = matcher.group(1);
        }
        return referenceNumber;
    }
    public static boolean isNumeric(String input) {
        String numericPattern = "^-?\\d+(\\.\\d+)?$";
        Pattern pattern = Pattern.compile(numericPattern);
        return pattern.matcher(input).matches();
    }
    public static boolean checkValidity(HashMap<String, String> line)
    {
        if(line.get("Date").isEmpty() ||line.get("Balance").isEmpty()||line.get("Amount").isEmpty()||line.get("Description").isEmpty()){
            return false;
        }
        if(!isNumeric(line.get("Amount").replace(",", "")) || !isNumeric(line.get("Balance").replace(",", ""))){
            return false;
        }
        double a=Double.parseDouble(line.get("Amount").replace(",", ""));
        if(a==0 || a==0.0){
            return false;}
        return true;
    }
    public static boolean isValid(String json) {
        try {
            new JSONObject(json);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }

    public int managetransactions(CaseEntity caseEntity,String task_id,byte[] bankStatement) {
        int i=0;
        try (CSVReader reader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(bankStatement)))) {
            String[] headers = reader.readNext();
            String[] line;
            HashMap<String, String> transaction;
            while ((line = reader.readNext()) != null) {
                transaction= new HashMap<>();
                for (int index = 0; index < headers.length; index++) {
                    String key = headers[index];
                    String value = line[index];
                    transaction.put(key, value);
                }
                if(!checkValidity(transaction))
                {
                    ErrorLogEntity skipTransaction=new ErrorLogEntity();
                    skipTransaction.setLogData("error in data fields of amount,balance,description,date");
                    skipTransaction.setCaseId(caseEntity.getId());
                    skipTransaction.setTaskId(UUID.fromString(task_id));
                    errorLogRepository.save(skipTransaction);
                    continue;
                }
                TransactionEntity csvData = new TransactionEntity();
                i++;
                csvData.setCaseId(caseEntity.getId());
                csvData.setTaskId(UUID.fromString(task_id));
                csvData.setCompanyName(caseEntity.getCompanyName());
                csvData.setAccountNumber(caseEntity.getAccountNumber());
                csvData.setDate(transaction.get("Date"));
                csvData.setChequeNumber(transaction.get("Cheque No."));
                csvData.setRefNumber(extractReferenceNumber(transaction.get("Description")));
                if (!transaction.get("Amount").isEmpty()) {
                    csvData.setAmount(Double.parseDouble(transaction.get("Amount").replace(",", ""))); // Remove commas
                }

                if (!transaction.get("Balance").isEmpty()) {
                    csvData.setBalance(Double.parseDouble(transaction.get("Balance").replace(",", ""))); // Remove commas
                }
                if(csvData.getAmount()>0) csvData.setType("CREDIT");
                else
                {
                    csvData.setType("DEBIT");
                }
                csvData.setDescription(transaction.get("Description"));
                csvData.setPerfios_category(transaction.get("Category"));
                csvData.setFiscus_category(null);
                csvData.setFinal_category(null);
                csvData.setTaskBatchId(null);
                TransactionEntity data=transactionRepository.save(csvData);
//                implement rabbitmq logic
//                rabbitTemplate.convertAndSend("task_queue", data.getId());
                System.out.println(data.getId());
                RedisService.lPush(RedisService.Queues.FISCUS_CATEGORIZATION.toString(), data.getId().toString());
                i++;
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }return i;
    }

    public CaseEntity getCaseEntityById(String id)
    {
        UUID s=UUID.fromString(id);
        return caseRepository.findById(UUID.fromString(id));
    }

    public String truncateCaseTable(){
        caseRepository.truncateCaseTable();
        return "truncate successfull";
    }
}


