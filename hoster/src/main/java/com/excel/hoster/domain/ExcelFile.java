package com.excel.hoster.domain;

import com.excel.hoster.repository.entity.ExcelFileEntity;

public record ExcelFile (
    String definitionName,
    String brickName,
    String attributeName,
    String fileName,
    byte[] excelFile,
    String version,
    String fullTextId) {

    public static ExcelFile createExcelFile(String definitionName, String brickName, String attributeName, String fileName, byte[] excelFile) {
        return new ExcelFile(
                definitionName,
                brickName,
                attributeName,
                fileName,
                excelFile,
                null,
                definitionName + "." +  brickName + "." + attributeName);
    }
    public static ExcelFile createExcelFileFromEntity(ExcelFileEntity excelFileEntity) {
        return new ExcelFile(
                excelFileEntity.getDefinitionName(),
                excelFileEntity.getBrickName(),
                excelFileEntity.getAttributeName(),
                excelFileEntity.getFileName(),
                excelFileEntity.getExcelFile(),
                excelFileEntity.getVersion(),
                excelFileEntity.getFullTextId());
    }
}
