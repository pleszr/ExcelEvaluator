package com.excel.hoster.controller;


import com.excel.hoster.repository.entity.ExcelFileEntity;
import com.excel.hoster.dto.ExcelFileDTO;
import com.excel.hoster.repository.ExcelRepository;
import com.excel.hoster.service.ObjectResponse;
import com.excel.hoster.validator.ExcelFileValidator;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api")
public class UploadExcelControllerApi {


    private static final Logger logger = LogManager.getLogger(UploadExcelControllerApi.class);
    private final ExcelRepository excelRepository;

    @Autowired
    public UploadExcelControllerApi(ExcelRepository excelRepository) {
        this.excelRepository = excelRepository;
    }

    @PostMapping("/uploadExcel")
    public ResponseEntity<?> uploadExcelSubmit(
            @Valid @ModelAttribute ExcelFileDTO excelFileDTO,
            @RequestParam(name="file",required = false) MultipartFile file,
            BindingResult bindingResult)
            throws IOException {
        logger.info("Excel file upload requested: " + excelFileDTO.toString());
        ExcelFileValidator.validateExcel(bindingResult, file);
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        ExcelFileEntity excelFile = new ExcelFileEntity(excelFileDTO.getDefinitionName(), excelFileDTO.getBrickName(), excelFileDTO.getAttributeName(),file.getOriginalFilename(), file.getBytes());
        excelRepository.save(excelFile);
        ObjectResponse<ExcelFileEntity> response = new ObjectResponse<>(HttpStatus.OK.value(), "Excel file uploaded successfully", excelFile);
        return ResponseEntity.ok(response);
    }
}

