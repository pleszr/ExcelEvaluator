package com.excel.hoster.controller;

import com.excel.hoster.domain.ExcelFile;
import com.excel.hoster.dto.ExcelResponseDTO;
import com.excel.hoster.dto.UploadExcelRequestDTO;
import com.excel.hoster.service.ExcelFileService;
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
public class ExcelWebController {

    private final ExcelFileService excelFileService;

    @Autowired
    public ExcelWebController(ExcelFileService excelFileService) {
        this.excelFileService = excelFileService;
    }



    @GetMapping("/uploadExcel")
    public String uploadExcelForm(Model model) {
        log.info("Excel file upload form requested");
        model.addAttribute("excelFileDTO",new UploadExcelRequestDTO());
        return "ExcelUpload";
    }

    @GetMapping("/error")
    public String error() {
        log.info("Error page requested");
        return "Error";
    }

    @PostMapping("/uploadExcel")
    public String uploadExcelSubmit(
            @Valid @ModelAttribute UploadExcelRequestDTO uploadExcelRequestDTO,
            @RequestParam(name="file",required = false) MultipartFile file,
            BindingResult bindingResult, Model model) throws IOException {
        log.info("Excel file upload requested: " + uploadExcelRequestDTO.toString());
        ExcelFileValidator.validateExcel(bindingResult,file);

        ExcelFile excelFile = new ExcelFile(
                uploadExcelRequestDTO.getDefinitionName(),
                uploadExcelRequestDTO.getBrickName(),
                uploadExcelRequestDTO.getAttributeName(),
                file.getOriginalFilename(),
                file.getBytes());
        ExcelResponseDTO excelResponseDTO = new ExcelResponseDTO(excelFile);
        model.addAttribute("excelResponseDTO", excelResponseDTO);
        excelFileService.saveExcelFile(excelFile);
        return "Result";
    }

}

