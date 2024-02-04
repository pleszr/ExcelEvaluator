package com.excel.hoster.controller;


import com.excel.hoster.excelfile.ExcelFile;
import com.excel.hoster.excelfile.ExcelFileDTO;
import com.excel.hoster.excelfile.ExcelFileService;
import com.excel.hoster.excelfile.ExcelRepository;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class ExposeExcelController {

    private final ExcelRepository excelRepository;
    private static final Logger logger = LogManager.getLogger(UploadExcelControllerApi.class);
    @Value("${apache.poi.version}")
    private String apachePoiVersion;

    @Autowired
    private ExcelFileService excelFileService;

    @Autowired
    public ExposeExcelController(ExcelRepository excelRepository) {
        this.excelRepository = excelRepository;
    }

    @GetMapping("/getApachePoiVersion")
    public ResponseEntity<?> getApachePoiVersion(){
        Map<String,String> apachePoiVersionMap = new HashMap<String,String>();
        apachePoiVersionMap.put("apachePoiVersion",apachePoiVersion);
        ObjectResponse<Map<String,String>> response = new ObjectResponse<>(HttpStatus.OK.value(), "Apache POI version successfully requested",apachePoiVersionMap);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getExcelVersion")
    public ResponseEntity<?> getExcelVersion(@RequestParam(name="fullTextId",required = true) String fullTextId) {
        ExcelFile excelFile = excelFileService.getExcelVersion(fullTextId);

        if (excelFile ==null) {
            ObjectResponse<String> response = new ObjectResponse<>(HttpStatus.NOT_FOUND.value(), "No Excel found with fullTextId: " + fullTextId,null);
            return ResponseEntity.badRequest().body(response);
        }

        ObjectResponse<ExcelFile> response = new ObjectResponse<>(HttpStatus.OK.value(), "Excel version successfully requested",excelFile);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/old")
    public ResponseEntity<?> uploadExcelSubmit(@Valid @ModelAttribute ExcelFileDTO excelFileDTO, @RequestParam(name="file",required = false) MultipartFile file, BindingResult bindingResult, Model model) throws IOException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        ExcelFile excelFile = new ExcelFile(excelFileDTO.getDefinitionName(), excelFileDTO.getBrickName(), excelFileDTO.getAttributeName(),file.getOriginalFilename(), file.getBytes());
        excelRepository.save(excelFile);

        ObjectResponse<ExcelFile> response = new ObjectResponse<>(HttpStatus.OK.value(), "Excel file uploaded successfully", excelFile);

        return ResponseEntity.ok(response);
    }




}

