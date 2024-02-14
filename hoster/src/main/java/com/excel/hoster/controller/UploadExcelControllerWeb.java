package com.excel.hoster.controller;

import com.excel.hoster.repository.entity.ExcelFileEntity;
import com.excel.hoster.dto.ExcelFileDTO;
import com.excel.hoster.repository.ExcelRepository;
import com.excel.hoster.validator.ExcelFileValidator;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Log4j2
@Controller
@RequestMapping("/web")
public class UploadExcelControllerWeb {

    private final ExcelRepository excelRepository;

    @Autowired
    public UploadExcelControllerWeb(ExcelRepository excelRepository) {
        this.excelRepository = excelRepository;
    }



    @GetMapping("/uploadExcel")
    public String uploadExcelForm(Model model) {
        log.info("Excel file upload form requested");
        model.addAttribute("excelFileDTO",new ExcelFileDTO());
        return "ExcelUpload";
    }

    @GetMapping("/error")
    public String error() {
        log.info("Error page requested");
        return "Error";
    }

    @PostMapping("/uploadExcel")
    public String uploadExcelSubmit(
            @Valid @ModelAttribute ExcelFileDTO excelFileDTO,
            @RequestParam(name="file",required = false) MultipartFile file,
            BindingResult bindingResult, Model model) throws IOException {
        log.info("Excel file upload requested: " + excelFileDTO.toString());
        ExcelFileValidator.validateExcel(bindingResult,file);

        ExcelFileEntity excelFile = new ExcelFileEntity(excelFileDTO.getDefinitionName(), excelFileDTO.getBrickName(), excelFileDTO.getAttributeName(),file.getOriginalFilename(), file.getBytes());
        model.addAttribute("excelFile", excelFile);
        excelRepository.save(excelFile);

        return "Result";
    }

}

