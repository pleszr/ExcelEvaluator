package com.excel.hoster.excelfile;

import jakarta.validation.constraints.NotBlank;

public class ExcelFileDTO {
    @NotBlank(message = "definitionName is mandatory")
    private String definitionName;
    @NotBlank(message = "brickName is mandatory")
    private String brickName;
    @NotBlank(message = "attributeName is mandatory")
    private String attributeName;
    private byte[] excelFile;

    public ExcelFileDTO() {

    }


    public void setDefinitionName(String definitionName) {
        this.definitionName = definitionName;
    }

    public void setBrickName(String brickName) {
        this.brickName = brickName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }


    public void setExcelFile(byte[] excelFile) {
        this.excelFile = excelFile;
    }

    public String getDefinitionName() {
        return definitionName;
    }

    public String getBrickName() {
        return brickName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public byte[] getExcelFile() {
        return excelFile;
    }
}
