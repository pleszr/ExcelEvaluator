package com.excel.hoster.excelfile;

import com.excel.hoster.controller.UploadExcelControllerWeb;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class ExcelFileTest {
    MockMultipartFile mockFile;
    String definitionName;
    String brickName;
    String attributeName;

    @BeforeEach
    public void init() {
        mockFile = new MockMultipartFile(
                "file",
                "test.xlsx",
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                "<<excel file content>>".getBytes()
        );

        definitionName = "SampleDefinition";
        brickName = "SampleBrick";
        attributeName = "SampleAttribute";
    }

    @MockBean
    private ExcelRepository excelRepository;

    @DisplayName("Save excel")
    @Test
    void saveExcelFile() {
        Path path = Path.of("src/main/resources/test/dummyExcelFile.xlsx");
        byte[] dummyExcel = null;
        try {
            dummyExcel = Files.readAllBytes(path);
        } catch (IOException ioException) {
            fail("DummyExcel Not found" + ioException.getMessage());
        }
        ExcelFile excelFile = new ExcelFile(definitionName,brickName,attributeName,"dummyExcelFile.xlsx",dummyExcel);
        excelRepository.save(excelFile);
        verify(excelRepository,times(1)).save(any(ExcelFile.class));
        assertNotNull(excelFile);
    }

}