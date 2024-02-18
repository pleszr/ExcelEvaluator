package com.excel.hoster.dto;

import com.excel.hoster.domain.ExcelFile;

public record ExcelResponseDTO (
    String definitionName,
    String brickName,
    String attributeName,
    String fileName,
    byte[] excelFile,
    String version,
    String fullTextId) {

    public static ExcelResponseDTO createFromExcelFile(ExcelFile excelFile) {
        return new ExcelResponseDTO(
            excelFile.definitionName(),
            excelFile.brickName(),
            excelFile.attributeName(),
            excelFile.fileName(),
            excelFile.excelFile(),
            excelFile.version(),
            excelFile.fullTextId()
        );
    }
}


