package com.excel.hoster.validator;

import com.excel.hoster.exception.ErrorCode;
import com.excel.hoster.exception.HosterException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Service
public class ExcelFileValidator {

    public static void validateExcel(MultipartFile file) {
            if (file == null) {
                log.error("File is null");
                throw new HosterException(ErrorCode.MISSING_FIELD,"Excel file is mandatory");
            }

            String fileName = file.getOriginalFilename();
            log.info("Excel validation starts for " + fileName );
            if (file.getSize() > 20 * 1024 * 1024) {
                log.error(fileName + " max is 20MB, but received " + file.getSize());
                throw new HosterException(ErrorCode.EXCEL_FILE_TOO_LARGE, "Excel file is too large. Max size is 20MB, but received " + file.getSize());
            }

            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            if (!fileExtension.equals("xlsx") && !fileExtension.equals("xls") && !fileExtension.equals("txt")) {
                log.error(fileName + " (extension: " + fileExtension + ") is not .xls or .xlsx or .txt");
                throw new HosterException(ErrorCode.INVALID_FIELD, "Excel file must be .xls or .xlsx or .txt");
            }
        }

    }
