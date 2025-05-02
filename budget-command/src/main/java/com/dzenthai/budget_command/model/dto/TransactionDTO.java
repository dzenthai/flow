package com.dzenthai.budget_command.model.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;


public record TransactionDTO <T> (
        BigDecimal amount,
        T category,
        OffsetDateTime createdAt,
        String note
) {
}
