package com.excel.hoster.excelfile;

import com.excel.hoster.exception.MissingFieldException;
import com.excel.hoster.controller.UploadExcelControllerWeb;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExcelFileService {

    private static final Logger logger = LogManager.getLogger(UploadExcelControllerWeb.class);
    private final ExcelRepository excelRepository;

    @Autowired
    public ExcelFileService(ExcelRepository excelRepository) {
        this.excelRepository = excelRepository;
    }

    public static void validateExcel(BindingResult bindingResult, MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            logger.info("Excel validation starts for " + fileName );
            if (!bindingResult.hasErrors() && file.getSize() > 20 * 1024 * 1024) {
                logger.error(fileName + " is larger then 20MB");
                bindingResult.rejectValue("excelFile", "400", "Excel file must be less than 20MB");
            }

            if (!bindingResult.hasErrors()) {
                String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                if (!fileExtension.equals("xlsx") && !fileExtension.equals("xls") && !fileExtension.equals("txt")) {
                    logger.error(fileName + " (extension: " + fileExtension + ") is not .xls or .xlsx or .txt");
                    bindingResult.rejectValue("excelFile", "400", "File must be .xls or .xlsx");
                }
            }
        }
        catch (NullPointerException nullPointerException) {
            logger.error("File is null");
            bindingResult.rejectValue("excelFile", "400", "excelFile is mandatory");
        }
        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
            throw new MissingFieldException(errors);
        }
    }

    public ExcelFile getExcelFileByFullTextId(String fullTextId) {
        Optional<ExcelFile> excelFile = excelRepository.findById(fullTextId);
        return excelFile.orElse(null);
    }


}
