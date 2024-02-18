package com.excel.hoster.service;

import com.excel.hoster.dto.ExampleDataModel;
import com.excel.hoster.dto.ValidationError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ExampleDataModelDataValidatorTest {
    private ExampleDataModelValidator underTest;

    @BeforeEach
    void setUp() {
        underTest = new ExampleDataModelValidator();
    }

    @Test
    void givenEntityWithNullNumbers_thenReturnsTwoErrors() {
        ExampleDataModel entity = getExampleEntity(null, null);

        List<ValidationError> errors = underTest.validate(
            List.of(entity)
        );

        assertThat(errors)
            .containsExactly(
                new ValidationError("vqNumber", "vqNumber must be present"),
                new ValidationError("gqNumber", "gqNumber must be present")
            );
    }

    @Test
    void givenEntityWithNullVqNumber_thenReturnsOneError() {
        ExampleDataModel entity = getExampleEntity(null, "gqNumber");

        List<ValidationError> errors = underTest.validate(
            List.of(entity)
        );

        assertThat(errors).containsExactly(
            new ValidationError("vqNumber", "vqNumber must be present")
        );
    }

    @Test
    void givenEntityWithNullGqNumber_thenReturnsOneError() {
        ExampleDataModel entity = getExampleEntity("vqNumber", null);

        List<ValidationError> errors = underTest.validate(
            List.of(entity)
        );

        assertThat(errors).containsExactly(
            new ValidationError("gqNumber", "gqNumber must be present")
        );
    }

    @Test
    void givenEntityWithNonNullNumbers_thenReturnsNoErrors() {
        ExampleDataModel entity = getExampleEntity("vqNumber", "gqNumber");

        List<ValidationError> errors = underTest.validate(
            List.of(entity)
        );

        assertThat(errors).isEmpty();
    }

    @Test
    void givenMultipleInvalidData_thenReturnErrorForEach() {
        List<ExampleDataModel> data = List.of(
            getExampleEntity(null, null),
            getExampleEntity(null, "gqNumber"),
            getExampleEntity("vqNumber", null),
            getExampleEntity("vqNumber", "gqNumber")
        );

        List<ValidationError> errors = underTest.validate(data);

        assertThat(errors)
            .containsExactlyInAnyOrder(
                new ValidationError("vqNumber", "vqNumber must be present"),
                new ValidationError("gqNumber", "gqNumber must be present"),
                new ValidationError("vqNumber", "vqNumber must be present"),
                new ValidationError("gqNumber", "gqNumber must be present")
            );
    }


    private ExampleDataModel getExampleEntity(
        String vqNumber,
        String gqNumber
    ) {
        return new ExampleDataModel(
            UUID.randomUUID(),
            "name",
            "address",
            vqNumber,
            gqNumber,
            false
        );
    }
}
