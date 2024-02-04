package com.excel.hoster.controller;

import com.excel.hoster.excelfile.ExcelFile;
import com.excel.hoster.excelfile.ExcelFileService;
import com.excel.hoster.excelfile.ExcelRepository;
import com.jayway.jsonpath.JsonPath;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("IntegrationTest")
@DisplayName("When consuming ExposeExcel API")
@WebMvcTest(ExposeExcelController.class)
class ExposeExcelControllerTest {

    @MockBean
    ExcelRepository excelRepository;

    @MockBean
    ExcelFileService excelFileService;

    @Autowired
    private MockMvc mockMvc;

    @Value("${apache.poi.version}")
    private String apachePoiVersionUnitTest;

    @Test
    @DisplayName("and getApachePoiVersion it should give me back the Apache POI version")
    void getApachePoiVersion() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/getApachePoiVersion"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("apachePoiVersion")))
                .andExpect(content().string(containsString(apachePoiVersionUnitTest)));
    }

    @Nested
    @DisplayName("and requesting the version of an Excel")
    class GetExcelVersionTest {

        @DisplayName("if the fullTextId exists it should give back the version")
        @Test
        void successfulRequest() throws  Exception {

            ExcelFile mockExcelFile = mock(ExcelFile.class);
            when(mockExcelFile.getVersion()).thenReturn("6c58ba55-40cb-4a72-970a-be4e15af3260");
            when(mockExcelFile.getDefinitionName()).thenReturn("sampleDefName");
            when(mockExcelFile.getBrickName()).thenReturn("sampleBrickName");
            when(mockExcelFile.getAttributeName()).thenReturn("sampleAttributeName");
            when(mockExcelFile.getFullTextId()).thenReturn("sampleDefName.sampleBrickName.sampleAttributeName");

            when(excelFileService.getExcelVersion("sampleDefName.sampleBrickName.sampleAttributeName")).thenReturn(mockExcelFile);

//            mockMvc.perform(MockMvcRequestBuilders.get("/api/getExcelVersion")
//                    .param("fullTextId","sampleDefName.sampleBrickName.sampleAttributeName"))
//                    .andExpect(status().isOk())
//                    .andExpect(content().string(containsString("Excel version successfully requested")))
//                    .andExpect(content().string(containsString("sampleDefName.sampleBrickName.sampleAttributeName")));

            String responseJson = mockMvc.perform(MockMvcRequestBuilders.get("/api/getExcelVersion")
                    .param("fullTextId","sampleDefName.sampleBrickName.sampleAttributeName"))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            String specificValue = JsonPath.read(responseJson, "$.some.nested.jsonpath");
            assertEquals("test",specificValue);

            verify(excelFileService,times(1)).getExcelVersion("sampleDefName.sampleBrickName.sampleAttributeName");


        }
    }

}