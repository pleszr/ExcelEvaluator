package com.excel.hoster;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;


import java.util.UUID;



@Entity
public class ExcelFile {

    private String version;
    @NotBlank(message = "definitionName is mandatory")
    private String definitionName;
    @NotBlank(message = "location name is mandatory")
    private String brickName;
    @NotBlank(message = "brickName is mandatory")
    private String attributeName;
    @NotBlank(message = "file name is mandatory")
    private String fileName;
    @Id
    private String fullTextId;
    @JsonIgnore
    private byte[] excelFile;

    public ExcelFile(String definitionName, String location, String attributeName, String fileName, byte[] excelFile) {
        this.version = version;
        this.definitionName = definitionName;
        this.brickName = location;
        this.attributeName = attributeName;
        this.excelFile = excelFile;
        this.fileName = fileName;
        version = UUID.randomUUID().toString();
        this.fullTextId = definitionName + "." +  brickName + "." + attributeName;
    }

    public ExcelFile() {
        version = UUID.randomUUID().toString();
        fullTextId = definitionName + "." +  brickName + "." + attributeName;
    }

    public void setVersion(String version) {
    }

    public void setDefinitionName(String definitionName) {
        this.definitionName = definitionName;
    }

    public void setBrickName(String location) {
        this.brickName = location;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public void setFullTextId(String fullTextIdPath) {
        this.fullTextId = fullTextIdPath;
    }



    public void setExcelFile(byte[] excelFile) {
        this.excelFile = excelFile;
    }



    @PrePersist
    @PreUpdate
    private void saveOrUpdate() {
        setVersion();
    }

    private void setFullTextIdPath() {
        fullTextId = definitionName + "." + brickName + "." + attributeName;
    }

    private void setVersion() {
        version = UUID.randomUUID().toString();
    }

    public String getVersion() {
        return version;
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

    public String getFullTextId() {
        return fullTextId;
    }

    public byte[] getExcelFile() {
        return excelFile;
    }


}
