package com.bridgeup.fiscus.core.service;

import com.bridgeup.fiscus.core.dal.entity.CaseEntity;
import com.bridgeup.fiscus.core.vo.CaseVo;

import java.util.List;

public interface CaseService {
    String createCase(CaseVo caseVo);
    List<String> newtask(String caseId);
    String truncateCaseTable();

    String readCsvToValidate(CaseEntity caseEntity,String task_id,byte[] bankStatement);

    CaseEntity getCaseEntityById(String id);
}
