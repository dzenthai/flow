package com.dzenthai.budget_query.model.dto;

import com.dzenthai.budget_query.model.enums.OperationType;


public record Payload <T> (
        T data,
        OperationType operation
) {
}
