package com.excel.hoster.controller;


import com.excel.hoster.excelfile.ExcelFile;
import com.excel.hoster.excelfile.ExcelFileDTO;
import com.excel.hoster.excelfile.ExcelFileService;
import com.excel.hoster.excelfile.ExcelRepository;
import com.excel.hoster.exception.MissingFieldException;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
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
    public ResponseEntity<?> uploadExcelSubmit(@Valid @ModelAttribute ExcelFileDTO excelFileDTO, @RequestParam(name="file",required = false) MultipartFile file, BindingResult bindingResult, Model model) throws IOException {

        ExcelFileService.validateExcel(bindingResult, file);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        ExcelFile excelFile = new ExcelFile(excelFileDTO.getDefinitionName(), excelFileDTO.getBrickName(), excelFileDTO.getAttributeName(),file.getOriginalFilename(), file.getBytes());
        excelRepository.save(excelFile);

        ObjectResponse<ExcelFile> response = new ObjectResponse<>(HttpStatus.OK.value(), "Excel file uploaded successfully", excelFile);

        return ResponseEntity.ok(response);
    }




}

