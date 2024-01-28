package com.excel.hoster;

import com.excel.exception.MissingFieldException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;

public class ExcelFileService {

    private static final Logger logger = LogManager.getLogger(UploadExcelControllerWeb.class);

    public static void validateExcel(BindingResult bindingResult, MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            if (!bindingResult.hasErrors() && file.getSize() > 20 * 1024 * 1024) {
                logger.error(fileName + " is larger then 20MB");
                bindingResult.rejectValue("excelFile", "error.excelFile", "Excel file must be less than 20MB");
            }

            if (!bindingResult.hasErrors()) {
                String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                if (!fileExtension.equals("xlsx") && !fileExtension.equals("xls") && !fileExtension.equals("txt")) {
                    logger.error(fileName + " (extension: " + fileExtension + ") is not .xls or .xlsx or .txt");
                    bindingResult.rejectValue("excelFile", "error.excelFile", "File must be .xls or .xlsx or .txt");
                }
            }
        }
        catch (NullPointerException nullPointerException) {
            logger.error("File is null");
            bindingResult.rejectValue("excelFile", "error.excelFile", "Excel file is mandatory");
        }
        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
            throw new MissingFieldException(errors);
        }
    }


}
