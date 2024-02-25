package com.excel.hoster.dto;

import jakarta.validation.constraints.NotBlank;

public record UploadExcelRequest(
    @NotBlank(message = "definitionName is mandatory")
    String definitionName,
    @NotBlank(message = "brickName is mandatory")
    String brickName,
    @NotBlank(message = "attributeName is mandatory")
    String attributeName,
    byte[] excelFile) {
}

