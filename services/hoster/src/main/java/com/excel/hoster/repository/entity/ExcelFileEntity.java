package com.excel.hoster.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.UUID;


@Entity @Getter @Log4j2
public class ExcelFileEntity {

    @NotBlank(message = "definitionName is mandatory")
    @Setter
    private String definitionName;

    @NotBlank(message = "brickName is mandatory")
    @Setter
    private String brickName;

    @Setter
    @NotBlank(message = "attributeName is mandatory")
    private String attributeName;

    @Id
    private String fullTextId;

    private String version;

    @Setter
    @NotBlank(message = "file name is mandatory")
    private String fileName;

    @Setter
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
        this.fullTextId = definitionName + "." +  brickName + "." + attributeName;
        this.excelFile = excelFile;
        this.fileName = fileName;

        calcNewVersion();

        log.info("Excel file created for fullTextId: " + fullTextId);
    }

    public ExcelFileEntity() {
        version = UUID.randomUUID().toString();
        fullTextId = definitionName + "." +  brickName + "." + attributeName;
    }

    public void calcNewVersion() {
        version = UUID.randomUUID().toString();
    }
}
