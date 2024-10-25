package com.bridgeup.fiscus.core.vo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class CaseVo {


    private String companyName;
    private String accountNumber;
    private String relatedParty;
    private byte[] bankStatementFile;
    private String metadata;
    private String companyAddress;

    public CaseVo() {}

    public CaseVo(String companyName, String accountNumber, String relatedParty, byte[] bankStatementFile, String metadata, String companyAddress) {
        this.companyName = companyName;
        this.accountNumber = accountNumber;
        this.relatedParty = relatedParty;
        this.bankStatementFile = bankStatementFile;
        this.metadata = metadata;
        this.companyAddress = companyAddress;
    }

}
