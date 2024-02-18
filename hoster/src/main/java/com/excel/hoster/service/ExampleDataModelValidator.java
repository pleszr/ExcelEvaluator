package com.excel.hoster.service;

import com.excel.hoster.dto.ExampleDataModel;
import com.excel.hoster.dto.ValidationError;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ExampleDataModelValidator {
    public List<ValidationError> validate(List<ExampleDataModel> data) {
        return Stream.concat(
            validateVqNumber(data).stream(),
            validateGqNumber(data).stream()
        ).collect(Collectors.toList());
    }

    private List<ValidationError> validateVqNumber(List<ExampleDataModel> data) {
        return data.stream()
            .filter(element -> element.vqNumber() == null)
            .map(element -> new ValidationError("vqNumber", "vqNumber must be present"))
            .collect(Collectors.toList());
    }

    private List<ValidationError> validateGqNumber(List<ExampleDataModel> entities) {
        return entities.stream()
            .filter(entity -> entity.gqNumber() == null)
            .map(entity -> new ValidationError("gqNumber", "gqNumber must be present"))
            .collect(Collectors.toList());
    }
}
