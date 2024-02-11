package com.excel.hoster.controller;


import com.excel.hoster.exception.ErrorCode;
import com.excel.hoster.exception.HosterException;
import com.excel.hoster.repository.entity.ExcelFile;
import com.excel.hoster.excelfile.ExcelFileDTO;
import com.excel.hoster.service.ExcelFileService;
import com.excel.hoster.repository.ExcelRepository;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api")
public class UploadExcelControllerApi {


    private static final Logger logger = LogManager.getLogger(UploadExcelControllerApi.class);
    private final ExcelRepository excelRepository;

    @Autowired
    public UploadExcelControllerApi(ExcelRepository excelRepository) {
        this.excelRepository = excelRepository;
    }

    @PostMapping("/uploadExcel")
    public ResponseEntity<ExcelFile> uploadExcelSubmit(
            @Valid @ModelAttribute ExcelFileDTO excelFileDTO,
            @RequestParam(name="file",required = false) Resource file,
            BindingResult bindingResult,
            Model model
    ) throws IOException {
        excelfileRequestValidatorService.validateExcel(bindingResult, file);

        ExcelFileService.validateExcel(bindingResult, file);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        ExcelFile excelFile = new ExcelFile(excelFileDTO.getDefinitionName(), excelFileDTO.getBrickName(), excelFileDTO.getAttributeName(),file.getOriginalFilename(), file.getBytes());
        excelRepository.save(excelFile);

        ObjectResponse<ExcelFile> response = new ObjectResponse<>(HttpStatus.OK.value(), "Excel file uploaded successfully", excelFile);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/userCreation")
    public ResponseEntity<UserResponseApiModel> upload(
            @RequestBody UserRequestApiModel requestBody
    ) {
        throw new HosterException(ErrorCode.USER_CURRENCY_NOT_FOUND, "Currency not found: " + requestBody.getCurrency());

        return userService.createUser(requestBody);
    }



}

// Request object
class UserRequestApiModel {
    private String name;
    private int age;
}

// Response object
class UserResponseApiModel {
    private String name;
    private String currency;
    private int age;
}

// Domain model
class UserDomainEntity {
    private UUID id;
    private String name;
    private String currency;
    private int age;
}

// Service
class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserResponseApiModel createUser(UserRequestApiModel user) {
        UserDomainEntity entity = new UserDomainEntity(
            UUID.randomUUID(),
            user.getName(),
            "USD",
            user.getAge()
        );

        UserDomainEntity savedEntity = userRepository.save(user);
        return new UserResponseApiModel(
            savedEntity.getName(),
            savedEntity.getCurrency(),
            savedEntity.getAge()
        );
    }
}
