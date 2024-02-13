package com.excel.hoster.controller;


import com.excel.hoster.excelfile.ExcelFile;
import com.excel.hoster.excelfile.ExcelFileService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class ExposeExcelController {

    public ExposeExcelController(ExcelFileService excelFileService) {
        this.excelFileService = excelFileService;
    }

    private static final Logger logger = LogManager.getLogger(UploadExcelControllerApi.class);
    @Value("${apache.poi.version}")
    private String apachePoiVersion;
    private final ExcelFileService excelFileService;

    @GetMapping("/getApachePoiVersion")
    public ResponseEntity<?> getApachePoiVersion(){
        logger.info("Apache POI version requested via /getApachePoiVersion");
        Map<String,String> apachePoiVersionMap = new HashMap<>();
        apachePoiVersionMap.put("apachePoiVersion",apachePoiVersion);
        ObjectResponse<Map<String,String>> response = new ObjectResponse<>(HttpStatus.OK.value(), "Apache POI version successfully requested",apachePoiVersionMap);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getExcelVersion")
    public ResponseEntity<?> getExcelVersion(@RequestParam(name="fullTextId") String fullTextId) {
        logger.info("Excel version requested via /getExcelVersion with fullTextId: " + fullTextId);
        ExcelFile excelFile = excelFileService.getExcelFileByFullTextId(fullTextId);

        if (excelFile ==null) {
            ObjectResponse<String> response = new ObjectResponse<>(HttpStatus.NOT_FOUND.value(), "No Excel found with fullTextId: " + fullTextId,null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        ObjectResponse<ExcelFile> response = new ObjectResponse<>(HttpStatus.OK.value(), "Excel version successfully requested",excelFile);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getExcelFile")
    public ResponseEntity<?> getExcelFile(@RequestParam(name = "fullTextId") String fullTextId) {
        logger.info("Excel file requested via /getExcelFile with fullTextId: " + fullTextId);
        ExcelFile excelFile = excelFileService.getExcelFileByFullTextId(fullTextId);

        if (excelFile ==null) {
            ObjectResponse<String> response = new ObjectResponse<>(HttpStatus.NOT_FOUND.value(), "No Excel found with fullTextId: " + fullTextId,null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
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

