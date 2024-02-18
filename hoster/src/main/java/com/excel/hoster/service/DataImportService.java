package com.excel.hoster.service;

import com.excel.hoster.dto.ExampleDataModel;
import com.excel.hoster.dto.ValidationError;
import com.excel.hoster.repository.ExcelRepository;
import com.excel.hoster.repository.entity.ExcelFileEntity;

import java.util.List;

public class DataImportService {
    private final ExampleDataModelValidator validator;
    private final ExcelRepository repository;

    public DataImportService(
        ExampleDataModelValidator validator,
        ExcelRepository repository
    ) {
        this.validator = validator;
        this.repository = repository;
    }

    public List<ValidationError> importData(List<ExampleDataModel> data) {
        List<ValidationError> errors = validator.validate(data);

        // do the logic
        String vqNumber = data.get(0).vqNumber();
        ExcelFileEntity entity = new ExcelFileEntity(
            "definitionName",
            "brickName",
            "attributeName",
            vqNumber,
            new byte[0]
        );

        repository.save(entity);

        return errors;
    }
}
