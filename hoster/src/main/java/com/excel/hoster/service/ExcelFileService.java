package com.excel.hoster.service;

import com.excel.hoster.exception.MissingFieldException;
import com.excel.hoster.controller.UploadExcelControllerWeb;
import com.excel.hoster.repository.ExcelRepository;
import com.excel.hoster.repository.entity.ExcelFileEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExcelFileService {

    private static final Logger logger = LogManager.getLogger(UploadExcelControllerWeb.class);
    private final ExcelRepository excelRepository;

    @Autowired
    public ExcelFileService(ExcelRepository excelRepository) {
        this.excelRepository = excelRepository;
    }





    public ExcelFileEntity getExcelFileByFullTextId(String fullTextId) {
        Optional<ExcelFileEntity> excelFile = excelRepository.findById(fullTextId);
        return excelFile.orElse(null);
    }


}
