package com.bridgeup.fiscus.core.service;

import com.bridgeup.fiscus.core.dal.entity.ErrorLogEntity;
import com.bridgeup.fiscus.core.dal.repository.ErrorLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ErrorLogServiceImpl implements ErrorLogService {

    @Autowired
    ErrorLogRepository errorLogRepository;
    @Override
    public String createInvalidCaseLog(String case_id,String error)
    {
        ErrorLogEntity errorLogEntity=new ErrorLogEntity();
        errorLogEntity.setLogData(error);
        errorLogEntity.setCaseId(UUID.fromString(case_id));
        errorLogRepository.save(errorLogEntity);
        return errorLogEntity.getId().toString();
    }
    @Override
    public String createCaseLog(String case_id, String error)
    {
        ErrorLogEntity errorLogEntity=new ErrorLogEntity();
        errorLogEntity.setLogData(error);
        errorLogEntity.setCaseId(UUID.fromString(case_id));
        errorLogRepository.save(errorLogEntity);
        return errorLogEntity.getId().toString();
    }



@Override
    public String createTaskErrorLog(String case_id, String task_id, String error)
    {
        ErrorLogEntity errorLogEntity=new ErrorLogEntity();
        errorLogEntity.setLogData(error);
        errorLogEntity.setTaskId(UUID.fromString(task_id));
        errorLogEntity.setCaseId(UUID.fromString(case_id));
        errorLogRepository.save(errorLogEntity);
        return errorLogEntity.getId().toString();
    }
    @Override
    public String createBatchErrorLog(String case_id, String error)
    {
        ErrorLogEntity errorLogEntity=new ErrorLogEntity();
        errorLogEntity.setLogData(error);
        errorLogEntity.setCaseId(UUID.fromString(case_id));
        errorLogRepository.save(errorLogEntity);
        return errorLogEntity.getId().toString();
    }
}
