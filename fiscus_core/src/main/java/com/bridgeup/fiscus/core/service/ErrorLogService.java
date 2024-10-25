package com.bridgeup.fiscus.core.service;

import java.util.UUID;

public interface ErrorLogService {
    String createInvalidCaseLog(String case_id,String error);

    String createCaseLog(String case_id, String error);

//    String createTaskErrorLog(String case_id, String error);

    String createTaskErrorLog(String case_id, String task_id, String error);

    String createBatchErrorLog(String case_id, String error);

}
