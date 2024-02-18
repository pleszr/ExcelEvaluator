package com.excel.hoster.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public record UploadExcelRequestDTO (
    @NotBlank(message = "definitionName is mandatory")
    String definitionName,
    @NotBlank(message = "brickName is mandatory")
    String brickName,
    @NotBlank(message = "attributeName is mandatory")
    String attributeName,
    byte[] excelFile) {
}

