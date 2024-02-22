package com.excel.hoster.dto;

import com.excel.hoster.domain.ExcelFile;
import lombok.NonNull;

public record ExcelResponse(
    @NonNull String definitionName,
    @NonNull String brickName,
    @NonNull String attributeName,
    @NonNull String fullTextId,
    String version,
    @NonNull String fileName,
    byte[] excelFile
    ) {

    public static ExcelResponse createFromExcelFile(ExcelFile excelFile) {
        return new ExcelResponse(
            excelFile.definitionName(),
            excelFile.brickName(),
            excelFile.attributeName(),
            excelFile.fullTextId(),
            excelFile.version(),
            excelFile.fileName(),
            excelFile.excelFile()
        );
    }
}


