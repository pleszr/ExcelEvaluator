package com.excel.hoster.validator;

import com.excel.hoster.exception.MissingFieldException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;

@Log4j2
@Service
public class ExcelFileValidator {

    public static void validateExcel(BindingResult bindingResult, MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            log.info("Excel validation starts for " + fileName );
            if (!bindingResult.hasErrors() && file.getSize() > 20 * 1024 * 1024) {
                log.error(fileName + " is larger then 20MB");
                bindingResult.rejectValue("excelFile", "400", "Excel file must be less than 20MB");
            }

            if (!bindingResult.hasErrors()) {
                String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                if (!fileExtension.equals("xlsx") && !fileExtension.equals("xls") && !fileExtension.equals("txt")) {
                    log.error(fileName + " (extension: " + fileExtension + ") is not .xls or .xlsx or .txt");
                    bindingResult.rejectValue("excelFile", "400", "File must be .xls or .xlsx");
                }
            }
        }
        catch (NullPointerException nullPointerException) {
            log.error("File is null");
            bindingResult.rejectValue("excelFile", "400", "excelFile is mandatory");
        }
        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
            throw new MissingFieldException(errors);
        }
    }
}
