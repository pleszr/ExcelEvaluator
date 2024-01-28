package com.excel.hoster;

import jakarta.validation.constraints.NotBlank;

public class ExcelFileDTO {
    @NotBlank(message = "Definition name is mandatory")
    private String definitionName;
    @NotBlank(message = "Brick name is mandatory")
    private String brickName;
    @NotBlank(message = "Attribute name is mandatory")
    private String attributeName;
    //@NotNull(message = "Excel file is mandatory")
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
