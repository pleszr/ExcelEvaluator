package com.excel.hoster.service;

import com.excel.hoster.repository.ExcelRepository;
import com.excel.hoster.repository.entity.ExcelFileEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
public class ExcelFileService {

    private final ExcelRepository excelRepository;

    @Autowired
    public ExcelFileService(ExcelRepository excelRepository) {
        this.excelRepository = excelRepository;
    }

    public ExcelFileEntity getExcelFileByFullTextId(String fullTextId) {
        log.info("Excel file searched for fullTextId: " + fullTextId);
        Optional<ExcelFileEntity> excelFile = excelRepository.findById(fullTextId);
        return excelFile.orElse(null);
    }


}
