package com.excel.hoster.service;

import com.excel.hoster.validator.ExcelFileValidator;
import org.junit.jupiter.api.*;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.BindingResult;

import static org.mockito.Mockito.*;

@DisplayName("When executing logic on an ExcelFile")
public class ExcelFileServiceTest {

    private AutoCloseable closeable;


    @Mock
    MockMultipartFile mockFile;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void init() {
        closeable = MockitoAnnotations.openMocks(this);

        when(mockFile.getSize()).thenReturn(2L*1024*1024);
        when(mockFile.getOriginalFilename()).thenReturn("test.xlsx");
    }

    @AfterEach
    void closeMocks() throws Exception {
        closeable.close();
    }

    @Nested
    @DisplayName("validating if the multipart file is correct")
    class ValidateExcelTest {

        @Tag("UnitTest")
        @DisplayName("it should not give an error if the file is valid")
        @Test
        void testExcelFileValid() {
            ExcelFileValidator.validateExcel(bindingResult,mockFile);
            verify(bindingResult,never()).rejectValue(anyString(),anyString(),anyString());
        }

        @Tag("UnitTest")
        @DisplayName("it should give error if the file is too big")
        @Test
        void testExcelFileTooBig() {
            when(mockFile.getSize()).thenReturn(21L*1024*1024);

            ExcelFileValidator.validateExcel(bindingResult,mockFile);
            verify(bindingResult,times(1)).rejectValue("excelFile","400","Excel file must be less than 20MB");
        }

        @Tag("UnitTest")
        @DisplayName("it should give error if file is not an excel file")
        @Test
        void testExcelFileFileNotExcel() {
            when(mockFile.getOriginalFilename()).thenReturn("test.ppt");

            ExcelFileValidator.validateExcel(bindingResult,mockFile);
            verify(bindingResult,times(1)).rejectValue("excelFile","400","File must be .xls or .xlsx");
        }

        @Tag("UnitTest")
        @DisplayName("it should give error if the file is null")
        @Test
        void testExcelFileIsNull() {

            ExcelFileValidator.validateExcel(bindingResult,null);
            verify(bindingResult,times(1)).rejectValue("excelFile","400","excelFile is mandatory");


        }
    }





}
