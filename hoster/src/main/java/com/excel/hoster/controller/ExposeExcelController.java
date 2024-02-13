package com.excel.hoster.controller;


import com.excel.hoster.repository.entity.ExcelFileEntity;
import com.excel.hoster.service.ExcelFileService;
import com.excel.hoster.repository.ExcelRepository;
import com.excel.hoster.service.ObjectResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

        ExcelFileEntity excelFile = excelFileService.getExcelFileByFullTextId(fullTextId);

        if (excelFile ==null) {
            ObjectResponse<String> response = new ObjectResponse<>(HttpStatus.NOT_FOUND.value(), "No Excel found with fullTextId: " + fullTextId,null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        ObjectResponse<ExcelFileEntity> response = new ObjectResponse<>(HttpStatus.OK.value(), "Excel version successfully requested",excelFile);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getExcelFile")
    public ResponseEntity<?> getExcelFile(@RequestParam(name = "fullTextId", required = true) String fullTextId) {

        ExcelFileEntity excelFile = excelFileService.getExcelFileByFullTextId(fullTextId);

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

