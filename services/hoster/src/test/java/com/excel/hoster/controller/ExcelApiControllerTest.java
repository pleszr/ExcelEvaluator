package com.excel.hoster.controller;

import com.excel.hoster.domain.ExcelFile;
import com.excel.hoster.service.ExcelFileService;
import com.excel.hoster.repository.ExcelRepository;
import com.jayway.jsonpath.JsonPath;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("UnitTest")
@DisplayName("When consuming ExposeExcel API")
@WebMvcTest(ExcelApiController.class)
class ExcelApiControllerTest {
    String defaultSampleVersion;
    String defaultSampleDefinitionName;
    String defaultSampleBrickName;
    String defaultSampleAttributeName;
    String defaultSampleFullTextId;

    @BeforeEach
    void init() {
        this.defaultSampleVersion = "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee";
        this.defaultSampleDefinitionName = "sampleDefName";
        this.defaultSampleBrickName = "sampleBrickName";
        this.defaultSampleAttributeName = "sampleAttributeName";
        this.defaultSampleFullTextId = "sampleDefName.sampleBrickName.sampleAttributeName";
    }

    @MockBean
    ExcelRepository excelRepository;

    @MockBean
    ExcelFileService excelFileService;

    @Autowired
    private MockMvc mockMvc;

    @Value("${apache.poi.version}")
    private String apachePoiVersionUnitTest;

    @Nested
    @Tag("UnitTest")
    @DisplayName("and doing services via an ExcelApiController")
    public class UploadExcelControllerApiTest {
        ExcelFile defaultMockExcelFile;
        @BeforeEach
        public void init() {
            defaultMockExcelFile = mock(ExcelFile.class);
            when(defaultMockExcelFile.version()).thenReturn(defaultSampleVersion);
            when(defaultMockExcelFile.definitionName()).thenReturn(defaultSampleDefinitionName);
            when(defaultMockExcelFile.brickName()).thenReturn(defaultSampleBrickName);
            when(defaultMockExcelFile.attributeName()).thenReturn(defaultSampleAttributeName);
            when(defaultMockExcelFile.fileName()).thenReturn("test.xlsx");
            when(defaultMockExcelFile.fullTextId()).thenReturn(defaultSampleFullTextId);
        }

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
            void shouldReturnCorrectVersionForExistingFullTextId() throws Exception {

                when(excelFileService.getExcelFileByFullTextId(defaultSampleFullTextId)).thenReturn(defaultMockExcelFile);

                String responseJson = mockMvc.perform(MockMvcRequestBuilders.get("/api/getExcelVersion")
                                .param("fullTextId", defaultSampleFullTextId))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

                String versionFromJson = JsonPath.read(responseJson, "$.version");
                assertEquals(defaultSampleVersion, versionFromJson, "The version from JSON should match with the sample");

                String fullTextIdFromJson = JsonPath.read(responseJson, "$.fullTextId");
                assertEquals(defaultSampleFullTextId, fullTextIdFromJson, "The full-text-id from JSON should match with the sample");

                verify(excelFileService, times(1)).getExcelFileByFullTextId(defaultSampleFullTextId);
            }

            @DisplayName("if the fullTextId does not exists it should give 404 error")
            @Test
            void shouldReturnErrorForNonExistingFullTextId() throws Exception {

                when(excelFileService.getExcelFileByFullTextId(defaultSampleFullTextId)).thenReturn(null);

                String responseJson = mockMvc.perform(MockMvcRequestBuilders.get("/api/getExcelVersion")
                                .param("fullTextId", defaultSampleFullTextId))
                        .andExpect(status().isNotFound())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

                String responseMessageFromJson = JsonPath.read(responseJson, "$.detail");
                assertEquals("No Excel found with fullTextId: " + defaultSampleFullTextId, responseMessageFromJson, "If ExcelFile is not found it should give back 404 error with an error message");

                verify(excelFileService, times(1)).getExcelFileByFullTextId(defaultSampleFullTextId);
            }
        }
    }




    @Nested
    @Tag("IntegrationTest")
    @DisplayName("and uploading an Excel file via API")
    public class ExcelServicesTests {
        MockMultipartFile defaultMockFile;

        @BeforeEach
        public void init() {
            defaultMockFile = new MockMultipartFile(
                    "file",
                    "test.xlsx",
                    MediaType.APPLICATION_OCTET_STREAM_VALUE,
                    "<<excel file content>>".getBytes()
            );
        }

        private ExcelFile setUpMockExcelFile() {
            ExcelFile mockExcelFile = mock(ExcelFile.class);
            when(mockExcelFile.definitionName()).thenReturn(defaultSampleDefinitionName);
            when(mockExcelFile.brickName()).thenReturn(defaultSampleBrickName);
            when(mockExcelFile.attributeName()).thenReturn(defaultSampleAttributeName);
            when(mockExcelFile.fileName()).thenReturn(defaultMockFile.getOriginalFilename());
            when(mockExcelFile.excelFile()).thenReturn("<<excel file content>>".getBytes());
            when(mockExcelFile.version()).thenReturn(defaultSampleVersion);
            when(mockExcelFile.fullTextId()).thenReturn(defaultSampleFullTextId);
            return mockExcelFile;
        }


        @DisplayName("it should give success if the request is correct")
        @Test
        void uploadExcelSubmitTest() throws Exception {
            ExcelFile mockExcelFile = setUpMockExcelFile();
            when(excelFileService.getExcelFileByFullTextId(defaultSampleFullTextId))
                    .thenReturn(mockExcelFile);

            mockMvc.perform(MockMvcRequestBuilders.multipart("/api/uploadExcel")
                            .file(defaultMockFile)
                            .param("definitionName", defaultSampleDefinitionName)
                            .param("brickName", defaultSampleBrickName)
                            .param("attributeName", defaultSampleAttributeName))
                    .andExpect(status().isOk());

            verify(excelFileService, times(1)).saveExcelFile(any(ExcelFile.class));
        }

        @DisplayName("it should fail if the file is missing")
        @Test
        void uploadExcelSubmitTestMissingFile() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.multipart("/api/uploadExcel")
                            .param("definitionName", defaultSampleDefinitionName)
                            .param("brickName", defaultSampleBrickName)
                            .param("attributeName", defaultSampleAttributeName))
                    .andExpect(status().is4xxClientError())
                    .andExpect(content().string(Matchers.containsString("Excel file is mandatory")));
            verifyNoInteractions(excelFileService);
        }

        @DisplayName("it should fail if the file is null")
        @Test
        void uploadExcelSubmitTestFileIsNull() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.multipart("/api/uploadExcel")
                            .param("file", "")
                            .param("definitionName", defaultSampleDefinitionName)
                            .param("brickName", defaultSampleBrickName)
                            .param("attributeName", defaultSampleAttributeName))
                    .andExpect(status().is4xxClientError())
                    .andExpect(content().string(Matchers.containsString("Excel file is mandatory")));
            verifyNoInteractions(excelFileService);

        }

        @DisplayName("it should fail if the brickName is missing")
        @Test
        void uploadExcelSubmitTestMissingBrick() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.multipart("/api/uploadExcel")
                            .file(defaultMockFile)
                            .param("definitionName", defaultSampleDefinitionName)
                            .param("attributeName", defaultSampleAttributeName))
                    .andExpect(status().is4xxClientError())
                    .andExpect(content().string(Matchers.containsString("brickName is mandatory")));
            verifyNoInteractions(excelFileService);

        }

        @DisplayName("it should fail if the brickName is empty")
        @Test
        void uploadExcelSubmitTestEmptyBrick() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.multipart("/api/uploadExcel")
                            .file(defaultMockFile)
                            .param("definitionName", defaultSampleDefinitionName)
                            .param("brickName", "")
                            .param("attributeName", defaultSampleAttributeName))
                    .andExpect(status().is4xxClientError())
                    .andExpect(content().string(Matchers.containsString("brickName is mandatory")));
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
                            .file(defaultMockFile)
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
                            .param("definitionName", defaultSampleDefinitionName)
                            .param("brickName", defaultSampleBrickName)
                            .param("attributeName", defaultSampleAttributeName))
                    .andExpect(status().is4xxClientError())
                    .andExpect(content().string(Matchers.containsString("Excel file must be .xls or .xlsx")));
            verifyNoInteractions(excelFileService);
        }

        @DisplayName("if the file is uploaded it should give back a new version if the file changed")
        @Test
        void shouldReturnNewVersionIfOtherFileIsUploaded() throws Exception {
            ExcelFile mockExcelFile = setUpMockExcelFile();
            when(excelFileService.getExcelFileByFullTextId(defaultSampleFullTextId))
                    .thenReturn(mockExcelFile);

            mockMvc.perform(MockMvcRequestBuilders.multipart("/api/uploadExcel")
                            .file(defaultMockFile)
                            .param("definitionName", defaultSampleDefinitionName)
                            .param("brickName", defaultSampleBrickName)
                            .param("attributeName", defaultSampleAttributeName));

            when(mockExcelFile.excelFile()).thenReturn("<<changed excel file content>>".getBytes());

            mockMvc.perform(MockMvcRequestBuilders.multipart("/api/uploadExcel")
                            .file(defaultMockFile)
                            .param("definitionName", defaultSampleDefinitionName)
                            .param("brickName", defaultSampleBrickName)
                            .param("attributeName", defaultSampleAttributeName))
                    .andExpect(status().isOk());




        }
    }
}