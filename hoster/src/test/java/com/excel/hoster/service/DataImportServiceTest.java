package com.excel.hoster.service;

import com.excel.hoster.dto.ExampleDataModel;
import com.excel.hoster.dto.ValidationError;
import com.excel.hoster.repository.ExcelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataImportServiceTest {
    @Mock
    private ExampleDataModelValidator validator;
    @Mock
    private ExcelRepository repository;

    private DataImportService underTest;

    @BeforeEach
    void init() {
        underTest = new DataImportService(validator, repository);
    }

    // example test, actual assertion is different than the title
    @Test
    void givenEntitiesWithErrors_whenImportData_thenReturnsErrors() {
        // given
        ExampleDataModel data = getExampleDataModel("vqNumber", null);

        when(
            validator.validate(
                List.of(data)
            )
        ).thenReturn(
            List.of(
                new ValidationError("vqNumber", "vqNumber must be present"),
                new ValidationError("gqNumber", "gqNumber must be present")
            )
        );

        // when
        List<ValidationError> errors = underTest.importData(
            List.of(data)
        );

        // then
        verify(repository).save(
            argThat(
                entity -> Objects.equals(entity.getFileName(), data.vqNumber())
            )
        );

        assertThat(errors).containsExactly(
            new ValidationError("vqNumber", "vqNumber must be present"),
            new ValidationError("gqNumber", "gqNumber must be present")
        );
    }

    @Test
    void givenEntitiesWithoutErrors_whenImportData_thenReturnsNoErrors() {
        ExampleDataModel entity = getExampleDataModel("vqNumber", "gqNumber");

        when(
            validator.validate(
                List.of(entity)
            )
        ).thenReturn(
            List.of()
        );

        List<ValidationError> errors = underTest.importData(List.of(entity));

        assertThat(errors).hasSize(0);
    }

    private ExampleDataModel getExampleDataModel(String vqNumber, String gqNumber) {
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
