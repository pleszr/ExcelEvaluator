package com.excel.hoster.controller;

import com.excel.hoster.repository.entity.ExcelFileEntity;
import com.excel.hoster.dto.ExcelFileDTO;
import com.excel.hoster.service.ExcelFileService;
import com.excel.hoster.repository.ExcelRepository;
import com.excel.hoster.validator.ExcelFileValidator;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Controller
@RequestMapping("/web")
public class UploadExcelControllerWeb {

    private final ExcelRepository excelRepository;
    private static final Logger logger = LogManager.getLogger(UploadExcelControllerWeb.class);

    @Autowired
    public UploadExcelControllerWeb(ExcelRepository excelRepository) {
        this.excelRepository = excelRepository;
    }



    @GetMapping("/uploadExcel")
    public String uploadExcelForm(Model model) {
        model.addAttribute("excelFileDTO",new ExcelFileDTO());
        return "ExcelUpload";
    }

    @GetMapping("/error")
    public String error(Model model) {
        return "Error";
    }

    @PostMapping("/uploadExcel")
    public String uploadExcelSubmit(
            @Valid @ModelAttribute ExcelFileDTO excelFileDTO,
            @RequestParam(name="file",required = false) MultipartFile file,
            BindingResult bindingResult, Model model) throws IOException {

        ExcelFileValidator.validateExcel(bindingResult,file);

        ExcelFileEntity excelFile = new ExcelFileEntity(excelFileDTO.getDefinitionName(), excelFileDTO.getBrickName(), excelFileDTO.getAttributeName(),file.getOriginalFilename(), file.getBytes());
        model.addAttribute("excelFile", excelFile);
        excelRepository.save(excelFile);


        return "Result";
    }

}

