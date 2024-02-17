package com.excel.hoster.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UploadExcelRequestDTO {
    @NotBlank(message = "definitionName is mandatory")
    private String definitionName;
    @NotBlank(message = "brickName is mandatory")
    private String brickName;
    @NotBlank(message = "attributeName is mandatory")
    private String attributeName;
    private byte[] excelFile;
}

