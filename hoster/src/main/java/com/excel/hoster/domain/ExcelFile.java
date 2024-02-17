package com.excel.hoster.domain;

import com.excel.hoster.repository.entity.ExcelFileEntity;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ExcelFile {
    private String definitionName;
    private String brickName;
    private String attributeName;
    private String fileName;
    private byte[] excelFile;
    private String version;
    private String fullTextId;

    public ExcelFile(String definitionName, String brickName, String attributeName, String fileName, byte[] excelFile) {
        this.definitionName = definitionName;
        this.brickName = brickName;
        this.attributeName = attributeName;
        this.fileName = fileName;
        this.excelFile = excelFile;
    }

    public ExcelFile(ExcelFileEntity excelFileEntity) {
        this.definitionName = excelFileEntity.getDefinitionName();
        this.brickName = excelFileEntity.getBrickName();
        this.attributeName = excelFileEntity.getAttributeName();
        this.fileName = excelFileEntity.getFileName();
        this.excelFile = excelFileEntity.getExcelFile();
        this.version = excelFileEntity.getVersion();
        this.fullTextId = excelFileEntity.getFullTextId();
    }
}
