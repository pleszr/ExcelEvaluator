package com.excel.hoster.dto;

import lombok.NonNull;

import java.util.UUID;

public record ExampleDataModel(
    @NonNull UUID id,
    @NonNull String name,
    @NonNull String address,
    String vqNumber,
    String gqNumber,
    boolean isDeleted
) {
}
