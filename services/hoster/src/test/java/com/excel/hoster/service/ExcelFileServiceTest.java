package com.excel.hoster.service;

import com.excel.hoster.exception.ErrorCode;
import com.excel.hoster.exception.HosterException;
import com.excel.hoster.validator.ExcelFileValidator;
import org.junit.jupiter.api.*;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("When executing logic on an ExcelFile")
public class ExcelFileServiceTest {

    private AutoCloseable closeable;


    @Mock
    MockMultipartFile mockFile;

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
            assertDoesNotThrow(()->ExcelFileValidator.validateExcel(mockFile),"Expected validateExcel to not throw an exception, but it did");
        }

        @Tag("UnitTest")
        @DisplayName("it should give error if the file is too big")
        @Test
        void testExcelFileTooBig() {
            when(mockFile.getSize()).thenReturn(21L*1024*1024);

            HosterException hosterException = assertThrows(
                    HosterException.class,
                    () -> ExcelFileValidator.validateExcel(mockFile),
                    "Expected validateExcel to throw HosterException, but it didn't"
            );

            assertEquals(ErrorCode.EXCEL_FILE_TOO_LARGE.toString(), hosterException.getErrorCode(),"Expected error code to be EXCEL_FILE_TOO_LARGE");
            assertTrue(hosterException.getMessage().contains("Excel file is too large"),"Expected message to contain 'Excel file is too large");
        }

        @Tag("UnitTest")
        @DisplayName("it should give error if file is not an excel file")
        @Test
        void testExcelFileFileNotExcel() {
            when(mockFile.getOriginalFilename()).thenReturn("test.ppt");

            HosterException hosterException = assertThrows(
                    HosterException.class,
                    () -> ExcelFileValidator.validateExcel(mockFile),
                    "Expected validateExcel to throw HosterException, but it didn't"
            );

            assertEquals(ErrorCode.INVALID_FIELD.toString(), hosterException.getErrorCode(),"Expected error code to be INVALID_FIELD");
            assertTrue(hosterException.getMessage().contains("Excel file must be .xls or .xlsx or .txt"),"Expected message to contain 'Excel file must be .xls or .xlsx or .txt");
        }

        @Tag("UnitTest")
        @DisplayName("it should give error if the file is null")
        @Test
        void testExcelFileIsNull() {

            HosterException hosterException = assertThrows(
                    HosterException.class,
                    () -> ExcelFileValidator.validateExcel(null),
                    "Expected validateExcel to throw HosterException, but it didn't"
            );

            assertEquals(ErrorCode.MISSING_FIELD.toString(), hosterException.getErrorCode(),"Expected error code to be MISSING_FIELD");
            assertTrue(hosterException.getMessage().contains("Excel file is mandatory"),"Expected message to contain 'Excel file is mandatory");

        }
    }





}
