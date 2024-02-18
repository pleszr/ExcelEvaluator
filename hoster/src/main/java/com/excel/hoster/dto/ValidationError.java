package com.excel.hoster.dto;

import lombok.NonNull;

public record ValidationError(
    @NonNull String field,
    @NonNull String message
) {
}
