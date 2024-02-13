package com.excel.hoster.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;


import java.util.UUID;



@Entity
public class ExcelFileEntity {

    private String version;
    @NotBlank(message = "definitionName is mandatory")
    private String definitionName;
    @NotBlank(message = "brickName is mandatory")
    private String brickName;
    @NotBlank(message = "attributeName is mandatory")
    private String attributeName;
    @NotBlank(message = "file name is mandatory")
    private String fileName;
    @Id
    private String fullTextId;
    @JsonIgnore
    @Lob
    @Column(nullable=false, columnDefinition="blob")
    private byte[] excelFile;

    public ExcelFileEntity(
            String definitionName,
            String brickName,
            String attributeName,
            String fileName,
            byte[] excelFile) {
        this.definitionName = definitionName;
        this.brickName = brickName;
        this.attributeName = attributeName;
        this.excelFile = excelFile;
        this.fileName = fileName;
        version = UUID.randomUUID().toString();
        this.fullTextId = definitionName + "." +  brickName + "." + attributeName;
    }

    public ExcelFileEntity() {
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

    public String getFileName() {
        return fileName;
    }
}
