package com.excel.hoster.service;

import com.excel.hoster.domain.ExcelFile;
import com.excel.hoster.repository.ExcelRepository;
import com.excel.hoster.repository.entity.ExcelFileEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Log4j2
@Service
public class ExcelFileService {

    private final ExcelRepository excelRepository;

    @Autowired
    public ExcelFileService(ExcelRepository excelRepository) {
        this.excelRepository = excelRepository;
    }

    public ExcelFile getExcelFileByFullTextId(String fullTextId) {
        log.info("Excel file searched for fullTextId: " + fullTextId);
        return excelRepository.findExcelEntityByFullTextId(fullTextId)
                .map(ExcelFile::createExcelFileFromEntity)
                .orElse(null);
    }

    public void saveExcelFile(ExcelFile excelFile) {
        ExcelFileEntity excelFileEntity = new ExcelFileEntity(
                excelFile.definitionName(),
                excelFile.brickName(),
                excelFile.attributeName(),
                excelFile.fileName(),
                excelFile.excelFile()
        );
        excelRepository.saveOrUpdateExcelFileEntity(
                excelFileEntity.getDefinitionName(),
                excelFileEntity.getBrickName(),
                excelFileEntity.getAttributeName(),
                excelFileEntity.getFullTextId(),
                UUID.randomUUID().toString(),
                excelFileEntity.getFileName(),
                excelFileEntity.getExcelFile());
    }


}
