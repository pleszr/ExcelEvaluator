package com.excel.hoster.controller;


import com.excel.hoster.repository.entity.ExcelFileEntity;
import com.excel.hoster.dto.ExcelFileDTO;
import com.excel.hoster.repository.ExcelRepository;
import com.excel.hoster.validator.ExcelFileValidator;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Log4j2
@RestController
@RequestMapping("/api")
public class UploadExcelControllerApi {

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
        log.info("Excel file upload requested: " + excelFileDTO.toString());
        ExcelFileValidator.validateExcel(bindingResult, file);
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        ExcelFileEntity excelFile = new ExcelFileEntity(excelFileDTO.getDefinitionName(), excelFileDTO.getBrickName(), excelFileDTO.getAttributeName(),file.getOriginalFilename(), file.getBytes());
        excelRepository.save(excelFile);
        return ResponseEntity.ok(excelFile);
    }
}

