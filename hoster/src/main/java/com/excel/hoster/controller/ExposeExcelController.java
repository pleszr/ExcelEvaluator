package com.excel.hoster.controller;


import com.excel.hoster.repository.entity.ExcelFileEntity;
import com.excel.hoster.service.ExcelFileService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api")
@Log4j2
public class ExposeExcelController {

    @Value("${apache.poi.version}")
    private String apachePoiVersion;

    private final ExcelFileService excelFileService;

    @Autowired
    public ExposeExcelController(ExcelFileService excelFileService) {
        this.excelFileService = excelFileService;
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
        ExcelFileEntity excelFile = excelFileService.getExcelFileByFullTextId(fullTextId);

        if (excelFile ==null) {
            Map<String,String> responseMap = new HashMap<>();
            responseMap.put("responseMessage","No Excel found with fullTextId: " + fullTextId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMap);
        }
        return ResponseEntity.ok(excelFile);
    }

    @GetMapping("/getExcelFile")
    public ResponseEntity<?> getExcelFile(
            @RequestParam(name = "fullTextId") String fullTextId) {
        log.info("Excel file requested for fullTextId: " + fullTextId);
        ExcelFileEntity excelFile = excelFileService.getExcelFileByFullTextId(fullTextId);

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

