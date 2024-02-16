package com.excel.hoster.controller;

import com.excel.hoster.repository.entity.ExcelFileEntity;
import com.excel.hoster.service.ExcelFileService;
import com.excel.hoster.repository.ExcelRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("UnitTest")
@DisplayName("When consuming ExposeExcel API")
@WebMvcTest(ExposeExcelController.class)
class ExposeExcelControllerTest {
    String sampleVersion;
    String sampleDefName;
    String sampleBrickName;
    String sampleAttributeName;
    String sampleFullTextId;

    ExcelFileEntity mockExcelFile;

    @BeforeEach
    void init() {
        this.sampleVersion = "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee";
        this.sampleDefName = "sampleDefName";
        this.sampleBrickName = "sampleBrickName";
        this.sampleAttributeName = "sampleAttributeName";
        this.sampleFullTextId = "sampleDefName.sampleBrickName.sampleAttributeName";

        mockExcelFile = mock(ExcelFileEntity.class);
        when(mockExcelFile.getVersion()).thenReturn(sampleVersion);
        when(mockExcelFile.getDefinitionName()).thenReturn(sampleDefName);
        when(mockExcelFile.getBrickName()).thenReturn(sampleBrickName);
        when(mockExcelFile.getAttributeName()).thenReturn(sampleAttributeName);
        when(mockExcelFile.getFullTextId()).thenReturn(sampleFullTextId);
    }


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
        void shouldReturnCorrectVersionForExistingFullTextId() throws  Exception {

            when(excelFileService.getExcelFileByFullTextId(sampleFullTextId)).thenReturn(mockExcelFile);

            String responseJson = mockMvc.perform(MockMvcRequestBuilders.get("/api/getExcelVersion")
                    .param("fullTextId",sampleFullTextId))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            String versionFromJson = JsonPath.read(responseJson, "$.version");
            assertEquals(sampleVersion,versionFromJson,"The version from JSON should match with the sample");

            String fullTextIdFromJson = JsonPath.read(responseJson, "$.fullTextId");
            assertEquals(sampleFullTextId,fullTextIdFromJson,"The full-text-id from JSON should match with the sample");

            verify(excelFileService,times(1)).getExcelFileByFullTextId(sampleFullTextId);
        }

        @DisplayName("if the fullTextId does not exists it should give 404 error")
        @Test
        void shouldReturnErrorForNonExistingFullTextId() throws  Exception {

            when(excelFileService.getExcelFileByFullTextId(sampleFullTextId)).thenReturn(null);

            String responseJson = mockMvc.perform(MockMvcRequestBuilders.get("/api/getExcelVersion")
                            .param("fullTextId",sampleFullTextId))
                    .andExpect(status().isNotFound())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            String responseMessageFromJson = JsonPath.read(responseJson, "$.responseMessage");
            assertEquals("No Excel found with fullTextId: " + sampleFullTextId,responseMessageFromJson,"If ExcelFile is not found it should give back 404 error with an error message");

            verify(excelFileService,times(1)).getExcelFileByFullTextId(sampleFullTextId);
        }
    }
}