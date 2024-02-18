package com.excel.hoster.controller;

import com.excel.hoster.domain.ExcelFile;
import com.excel.hoster.service.ExcelFileService;
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
@WebMvcTest(ExcelApiController.class)
public class UploadExcelControllerApiTest {
    MockMultipartFile mockFile;
    String sampleDefinitionName;
    String sampleBrickName;
    String sampleAttributeName;
    String sampleFullTextId;
    String sampleVersion;



    @BeforeEach
    public void init() {

        mockFile = new MockMultipartFile(
                "file",
                "test.xlsx",
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                "<<excel file content>>".getBytes()
        );

        this.sampleDefinitionName = "SampleDefinition";
        this.sampleBrickName = "SampleBrick";
        this.sampleAttributeName = "SampleAttribute";
        this.sampleFullTextId = sampleDefinitionName + "." + sampleBrickName + "." + sampleAttributeName;
        this.sampleVersion = "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee";
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExcelFileService excelFileService;

    private ExcelFile setUpMockExcelFile() {
        ExcelFile mockExcelFile = mock(ExcelFile.class);
        when(mockExcelFile.definitionName()).thenReturn(sampleDefinitionName);
        when(mockExcelFile.brickName()).thenReturn(sampleBrickName);
        when(mockExcelFile.attributeName()).thenReturn(sampleAttributeName);
        when(mockExcelFile.fileName()).thenReturn(mockFile.getOriginalFilename());
        when(mockExcelFile.excelFile()).thenReturn("<<excel file content>>".getBytes());
        when(mockExcelFile.version()).thenReturn(sampleVersion);
        when(mockExcelFile.fullTextId()).thenReturn(sampleFullTextId);
        return mockExcelFile;
    }

    @DisplayName("it should give success if the request is correct")
    @Test
    void uploadExcelSubmitTest() throws Exception {

        ExcelFile mockExcelFile = setUpMockExcelFile();
        when(excelFileService.getExcelFileByFullTextId(sampleFullTextId))
                .thenReturn(mockExcelFile);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/uploadExcel")
                        .file(mockFile)
                        .param("definitionName", sampleDefinitionName)
                        .param("brickName", sampleBrickName)
                        .param("attributeName", sampleAttributeName))
                .andExpect(status().isOk());

        verify(excelFileService,times(1)).saveExcelFile(any(ExcelFile.class));
    }


    @DisplayName("it should fail if the file is missing")
    @Test
    void uploadExcelSubmitTestMissingFile() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/uploadExcel")
                        .param("definitionName", sampleDefinitionName)
                        .param("brickName", sampleBrickName)
                        .param("attributeName", sampleAttributeName))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("Excel file is mandatory")));
        verifyNoInteractions(excelFileService);
    }

    @DisplayName("it should fail if the file is null")
    @Test
    void uploadExcelSubmitTestFileIsNull() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/uploadExcel")
                        .param("file","")
                        .param("definitionName", sampleDefinitionName)
                        .param("brickName", sampleBrickName)
                        .param("attributeName", sampleAttributeName))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("Excel file is mandatory")));
        verifyNoInteractions(excelFileService);

    }

    @DisplayName("it should fail if the brickName is missing")
    @Test
    void uploadExcelSubmitTestMissingBrick() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/uploadExcel")
                        .file(mockFile)
                        .param("definitionName", sampleDefinitionName)
                        .param("attributeName", sampleAttributeName))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("brickName is mandatory")));
        verifyNoInteractions(excelFileService);

    }

    @DisplayName("it should fail if the brickName is empty")
    @Test
    void uploadExcelSubmitTestEmptyBrick() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/uploadExcel")
                        .file(mockFile)
                        .param("definitionName", sampleDefinitionName)
                        .param("brickName", "")
                        .param("attributeName", sampleAttributeName))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("brickName is mandatory")));
        verifyNoInteractions(excelFileService);
    }

    @DisplayName("it should fail if all fields are missing")
    @Test
    void uploadExcelSubmitTestMissingAllFields() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/uploadExcel")
                )
                .andExpect(status().is4xxClientError());
        verifyNoInteractions(excelFileService);

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
        verifyNoInteractions(excelFileService);

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
                        .param("definitionName", sampleDefinitionName)
                        .param("brickName", sampleBrickName)
                        .param("attributeName", sampleAttributeName))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("Excel file must be .xls or .xlsx")));
        verifyNoInteractions(excelFileService);

    }



}
