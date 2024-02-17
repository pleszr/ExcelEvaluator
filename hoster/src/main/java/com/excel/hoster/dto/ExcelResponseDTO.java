package com.excel.hoster.dto;

import com.excel.hoster.domain.ExcelFile;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ExcelResponseDTO {
    private String definitionName;
    private String brickName;
    private String attributeName;
    private String fileName;
    private byte[] excelFile;
    private String version;
    private String fullTextId;

    public ExcelResponseDTO(ExcelFile excelFile) {
        this.definitionName = excelFile.getDefinitionName();
        this.brickName = excelFile.getBrickName();
        this.attributeName = excelFile.getAttributeName();
        this.fileName = excelFile.getFileName();
        this.excelFile = excelFile.getExcelFile();
        this.version = excelFile.getVersion();
        this.fullTextId = excelFile.getFullTextId();
    }
}

