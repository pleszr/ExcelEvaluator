package com.excel.hoster.service;

import com.excel.hoster.domain.ExcelFile;
import com.excel.hoster.repository.ExcelRepository;
import com.excel.hoster.repository.entity.ExcelFileEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return excelRepository.findById(fullTextId)
                .map(ExcelFile::new)
                .orElse(null);
    }

    public void saveExcelFile(ExcelFile excelFile) {
        ExcelFileEntity excelFileEntity = new ExcelFileEntity(
                excelFile.getDefinitionName(),
                excelFile.getBrickName(),
                excelFile.getAttributeName(),
                excelFile.getFileName(),
                excelFile.getExcelFile());
        excelRepository.save(excelFileEntity);
    }


}
