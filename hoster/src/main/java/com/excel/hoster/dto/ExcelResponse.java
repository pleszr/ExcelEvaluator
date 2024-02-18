package com.excel.hoster.dto;

import com.excel.hoster.domain.ExcelFile;
import lombok.NonNull;

public record ExcelResponse(
    @NonNull String definitionName,
    @NonNull String brickName,
    @NonNull String attributeName,
    @NonNull String fileName,
    byte[] excelFile,
    String version,
    @NonNull String fullTextId) {

    public static ExcelResponse createFromExcelFile(ExcelFile excelFile) {
        return new ExcelResponse(
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


