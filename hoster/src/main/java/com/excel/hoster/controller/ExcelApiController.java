package com.excel.hoster.controller;


import com.excel.hoster.domain.ExcelFile;
import com.excel.hoster.dto.UploadExcelRequestDTO;
import com.excel.hoster.dto.ExcelResponseDTO;
import com.excel.hoster.service.ExcelFileService;
import com.excel.hoster.validator.ExcelFileValidator;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api")
@Log4j2
public class ExcelApiController {

    @Value("${apache.poi.version}")
    private String apachePoiVersion;

    private final ExcelFileService excelFileService;

    @Autowired
    public ExcelApiController(ExcelFileService excelFileService) {
        this.excelFileService = excelFileService;
    }

    @PostMapping("/uploadExcel")
    public ResponseEntity<?> uploadExcelSubmit(
            @Valid @ModelAttribute UploadExcelRequestDTO uploadExcelRequestDTO,
            @RequestParam(name="file",required = false) MultipartFile file,
            BindingResult bindingResult)
            throws IOException {

        log.info("Excel file upload requested: " + uploadExcelRequestDTO.toString());

        ExcelFileValidator.validateExcel(bindingResult, file);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        ExcelFile excelFile = new ExcelFile(
                uploadExcelRequestDTO.getDefinitionName(),
                uploadExcelRequestDTO.getBrickName(),
                uploadExcelRequestDTO.getAttributeName(),
                file.getOriginalFilename(),
                file.getBytes());
        excelFileService.saveExcelFile(excelFile);
        ExcelResponseDTO excelResponseDTO = new ExcelResponseDTO(excelFile);
        return ResponseEntity.ok(excelResponseDTO);
    }


    @GetMapping("/getApachePoiVersion")
    public ResponseEntity<?> getApachePoiVersion(){
        log.info("Apache POI version requested: " + apachePoiVersion);
        Map<String,String> apachePoiVersionMap = new HashMap<>();
        apachePoiVersionMap.put("apachePoiVersion",apachePoiVersion);
        return ResponseEntity.ok(apachePoiVersionMap);
    }

    @GetMapping("/getExcelVersion")
    public ResponseEntity<?> getExcelVersion(
            @RequestParam(name="fullTextId") String fullTextId) {

        log.info("Excel version requested for fullTextId: " + fullTextId);

        ExcelFile excelFile = excelFileService.getExcelFileByFullTextId(fullTextId);
        if (excelFile ==null) {
            Map<String,String> responseMap = new HashMap<>();
            responseMap.put("responseMessage","No Excel found with fullTextId: " + fullTextId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMap);
        }
        ExcelResponseDTO excelResponseDTO = new ExcelResponseDTO(excelFile);


        return ResponseEntity.ok(excelResponseDTO);
    }

    @GetMapping("/getExcelFile")
    public ResponseEntity<?> getExcelFile(
            @RequestParam(name = "fullTextId") String fullTextId) {

        log.info("Excel file requested for fullTextId: " + fullTextId);
        ExcelFile excelFile = excelFileService.getExcelFileByFullTextId(fullTextId);

        if (excelFile ==null) {
            Map<String,String> responseMap = new HashMap<>();
            responseMap.put("responseMessage","No Excel found with fullTextId: " + fullTextId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMap);
        }

        byte[] excelFileByteArray = excelFile.getExcelFile();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + excelFile.getFileName() + "\"");

        ByteArrayResource byteArrayResource = new ByteArrayResource(excelFileByteArray);

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .contentLength(excelFileByteArray.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(byteArrayResource);
    }



}

