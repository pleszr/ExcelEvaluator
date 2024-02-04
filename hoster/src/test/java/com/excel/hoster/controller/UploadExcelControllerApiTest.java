package com.excel.hoster.controller;

import com.excel.hoster.excelfile.ExcelFile;
import com.excel.hoster.excelfile.ExcelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("IntegrationTest")
@DisplayName("When uploading an Excel file via API")
@WebMvcTest(UploadExcelControllerApi.class)
public class UploadExcelControllerApiTest {
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

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExcelRepository excelRepository;

    @DisplayName("it should give success if the request is correct")
    @Test
    void uploadExcelSubmitTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/uploadExcel")
                        .file(mockFile)
                        .param("definitionName", definitionName)
                        .param("brickName", brickName)
                        .param("attributeName", attributeName))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Excel file uploaded successfully")));

        verify(excelRepository,times(1)).save(any(ExcelFile.class));
    }


    @DisplayName("it should fail if the file is missing")
    @Test
    void uploadExcelSubmitTestMissingFile() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/uploadExcel")
                        .param("definitionName", definitionName)
                        .param("brickName", brickName)
                        .param("attributeName", attributeName))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("excelFile is mandatory")));
        verifyNoInteractions(excelRepository);
    }

    @DisplayName("it should fail if the file is null")
    @Test
    void uploadExcelSubmitTestFileIsNull() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/uploadExcel")
                        .param("file","")
                        .param("definitionName", definitionName)
                        .param("brickName", brickName)
                        .param("attributeName", attributeName))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("excelFile is mandatory")));
        verifyNoInteractions(excelRepository);

    }

    @DisplayName("it should fail if the brickName is missing")
    @Test
    void uploadExcelSubmitTestMissingBrick() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/uploadExcel")
                        .file(mockFile)
                        .param("definitionName", definitionName)
                        .param("attributeName", attributeName))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("brickName is mandatory")));
        verifyNoInteractions(excelRepository);

    }

    @DisplayName("it should fail if the brickName is empty")
    @Test
    void uploadExcelSubmitTestEmptyBrick() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/uploadExcel")
                        .file(mockFile)
                        .param("definitionName", definitionName)
                        .param("brickName", "")
                        .param("attributeName", attributeName))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("brickName is mandatory")));
        verifyNoInteractions(excelRepository);
    }

    @DisplayName("it should fail if all fields are missing")
    @Test
    void uploadExcelSubmitTestMissingAllFields() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/uploadExcel")
                )
                .andExpect(status().is4xxClientError());
        verifyNoInteractions(excelRepository);

    }

    @DisplayName("it should fail if all fields are empty")
    @Test
    void uploadExcelSubmitTestAllFieldsEmpty() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/uploadExcel")
                        .file(mockFile)
                        .param("definitionName", "")
                        .param("brickName", "")
                        .param("attributeName", ""))
                .andExpect(status().is4xxClientError());
        verifyNoInteractions(excelRepository);

    }

    @DisplayName("it should fail if file type is not valid")
    @Test
    void uploadExcelSubmitTestInvalidFileType() throws Exception {
        MockMultipartFile invalidMockFile;
        invalidMockFile = new MockMultipartFile(
                "file",
                "test.css",
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                "<<excel file content>>".getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/uploadExcel")
                        .file(invalidMockFile)
                        .param("definitionName", definitionName)
                        .param("brickName", brickName)
                        .param("attributeName", attributeName))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("File must be .xls or .xlsx")));
        verifyNoInteractions(excelRepository);

    }



}
