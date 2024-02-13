package com.excel.hoster.controller;

import com.excel.hoster.excelfile.ExcelFile;
import com.excel.hoster.excelfile.ExcelFileDTO;
import com.excel.hoster.excelfile.ExcelFileService;
import com.excel.hoster.excelfile.ExcelRepository;
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
        logger.info("Excel file upload form requested via /web/uploadExcel");
        model.addAttribute("excelFileDTO",new ExcelFileDTO());
        return "ExcelUpload";
    }

    @GetMapping("/error")
    public String error() {
        logger.info("Error page requested via /error");
        return "Error";
    }

    @PostMapping("/uploadExcel")
    public String uploadExcelSubmit(@Valid @ModelAttribute ExcelFileDTO excelFileDTO, @RequestParam(name="file",required = false) MultipartFile file, BindingResult bindingResult, Model model) throws IOException {
        logger.info("Excel file upload requested via /web/uploadExcel");
        ExcelFileService.validateExcel(bindingResult,file);

        ExcelFile excelFile = new ExcelFile(excelFileDTO.getDefinitionName(), excelFileDTO.getBrickName(), excelFileDTO.getAttributeName(),file.getOriginalFilename(), file.getBytes());
        model.addAttribute("excelFile", excelFile);
        excelRepository.save(excelFile);
        return "Result";
    }

}

