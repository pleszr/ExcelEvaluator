package com.excel.hoster.domain;

import com.excel.hoster.repository.entity.ExcelFileEntity;

public record ExcelFile (
        String definitionName,
        String brickName,
        String attributeName,
        String fullTextId,
        String version,
        String fileName,
        byte[] excelFile
        ) {

    public static ExcelFile createExcelFile(String definitionName, String brickName, String attributeName, String fileName, byte[] excelFile) {
        return new ExcelFile(
                definitionName,
                brickName,
                attributeName,
                definitionName + "." +  brickName + "." + attributeName,
                null,
                fileName,
                excelFile);
    }
    public static ExcelFile createExcelFileFromEntity(ExcelFileEntity excelFileEntity) {
        return new ExcelFile(
                excelFileEntity.getDefinitionName(),
                excelFileEntity.getBrickName(),
                excelFileEntity.getAttributeName(),
                excelFileEntity.getFullTextId(),
                excelFileEntity.getVersion(),
                excelFileEntity.getFileName(),
                excelFileEntity.getExcelFile());
    }
}
