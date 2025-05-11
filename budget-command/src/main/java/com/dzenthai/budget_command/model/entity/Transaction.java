package com.dzenthai.budget_command.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.OffsetDateTime;


@Data
@Embeddable
@EqualsAndHashCode
public class Transaction <T extends Enum<T>> {

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    protected T category;

    @Temporal(TemporalType.TIMESTAMP)
    private OffsetDateTime createdAt;

    private String note;

    private String userId;
}
