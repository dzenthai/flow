package com.dzenthai.budget_command.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.OffsetDateTime;


public record TransactionDTO <T> (
        @DecimalMin("0.1") BigDecimal amount,
        @NotNull T category,
        OffsetDateTime createdAt,
        String note
) {
}
